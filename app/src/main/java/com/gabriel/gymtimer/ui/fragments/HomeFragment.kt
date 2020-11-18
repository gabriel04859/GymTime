
package com.gabriel.gymtimer.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.gymtimer.*
import com.gabriel.gymtimer.Consts.Companion.TIME_COLLECTION
import com.gabriel.gymtimer.Dialogs.AddTimeDialog
import com.gabriel.gymtimer.Dialogs.DialogHome
import com.gabriel.gymtimer.Dialogs.LoadingDialog
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.Firebase.FirestoreCallBack
import com.gabriel.gymtimer.Firebase.GymFirebaseCallBack
import com.gabriel.gymtimer.Firebase.GymUserFrequentaFirebaseCallBack
import com.gabriel.gymtimer.model.Gym
import com.gabriel.gymtimer.model.Time
import com.gabriel.gymtimer.model.User
import com.gabriel.gymtimer.ui.AlunosListActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentChange

import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_timer_list.view.*

class HomeFragment : Fragment() {
    private lateinit var textViewNameGymHome: TextView
    private lateinit var textViewNotHaveTime : TextView
    private lateinit var buttonMoreInformation: ImageButton
    private lateinit var imageViewGymHome: ImageView
    private lateinit var recyclerHome: RecyclerView
    private lateinit var idGym: String
    private lateinit var imageGym: String
    private lateinit var mContext: Context
    private lateinit var timeAdapter: GroupAdapter<ViewHolder>
    private lateinit var dialogHome: DialogHome
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var addTimeDialog : AddTimeDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val floatingActionButtonAddTime = view.findViewById<FloatingActionButton>(R.id.floatingActionButtonAddTime)
        initWidgets(view)
        loadingDialog.starDialog()
        dialogHome = DialogHome(activity!!)


        FirebaseSingleton.getUser(object : FirestoreCallBack {
            override fun onGetUser(user: User) {
                if (user.boss == false) {
                    Log.i("TESTE", "Não é dono")
                    whenUserNotBoss()
                    floatingActionButtonAddTime.visibility = View.GONE
                } else if (user.boss == true) {
                    Log.i("TESTE", "É dono")
                    getGymCurrentUser()
                }
            }
        })

        recyclerHome.adapter = timeAdapter
        addTimeDialog = AddTimeDialog(activity!!)


        floatingActionButtonAddTime.setOnClickListener {
         addTimeDialog.addTime(idGym)
        }

        timeAdapter.setOnItemClickListener { item, v ->
            val timeItem : TimeItem = item as TimeItem
            showPopupOption(v,timeItem.time)
        }

    }

    private fun initWidgets(view: View) {
        textViewNameGymHome = view.findViewById(R.id.textViewNameGymHome)
        buttonMoreInformation = view.findViewById(R.id.buttonMoreInformation)
        imageViewGymHome = view.findViewById(R.id.imageViewGymHome)
        textViewNotHaveTime = view.findViewById(R.id.textViewNotHaveTime)
        recyclerHome = view.findViewById(R.id.recyclerHome)
        recyclerHome.apply {
            layoutManager = GridLayoutManager(mContext,2)
            setHasFixedSize(true)

        }
        loadingDialog = LoadingDialog(context as Activity)
        timeAdapter = GroupAdapter()
    }


    private fun getGymCurrentUser() {
        FirebaseSingleton.getGym(object : GymFirebaseCallBack {
            override fun onGetGym(gym: Gym) {
                gym.let {
                    Picasso.with(mContext).load(it.photo).into(imageViewGymHome)
                    textViewNameGymHome.text = it.name
                    idGym = it.idGym!!
                }

                buttonMoreInformation.setOnClickListener {
                    dialogHome.showDialogMoreInformation(gym)
                }

                fetchTimes(gym)
                loadingDialog.dialogDimiss()

            }
        })


    }

    private fun fetchTimes(gym: Gym) {
        FirebaseSingleton.getFirebaseFirestore().collection(TIME_COLLECTION)
            .whereEqualTo("idGym", gym.idGym)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                querySnapshot?.documentChanges.let { docs ->
                    if (docs != null) {
                        for (doc in docs) {
                            when (doc.type) {
                                DocumentChange.Type.ADDED -> {
                                    val time = doc.document.toObject(Time::class.java)
                                    timeAdapter.add(TimeItem(time))
                                    textViewNotHaveTime.visibility = View.GONE
                                    timeAdapter.notifyDataSetChanged()

                                }
                                DocumentChange.Type.MODIFIED -> {
                                    val time = doc.document.toObject(Time::class.java)
                                    timeAdapter.add(TimeItem(time))
                                    timeAdapter.notifyDataSetChanged()

                                }
                            }
                        }
                    }
                }
            }
    }

    private fun whenUserNotBoss() {
        FirebaseSingleton.getGymUserFrequenta(object : GymUserFrequentaFirebaseCallBack {
            override fun onGetUserGym(gym: Gym) {
                textViewNameGymHome.text = gym.name
                buttonMoreInformation.setOnClickListener {
                    dialogHome.showDialogMoreInformation(gym)
                }
                imageGym = gym.photo!!
                loadingDialog.dialogDimiss()
                Picasso.with(mContext).load(imageGym).placeholder(R.drawable.gym_default).into(imageViewGymHome)
                fetchTimes(gym)
            }
        })
    }





    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    private inner class TimeItem(val time: Time) : Item<ViewHolder>() {
        override fun bind(viewHolder: ViewHolder, position: Int) {
            val days = "${time.diaInico} - ${time.diaFinal}"
            val times = "${time.tempoInicio} - ${time.tempoFinal}"
            viewHolder.itemView.textViewDayGymItemTimer.text = days
            viewHolder.itemView.textViewTimesGymItemTimer.text = times
                viewHolder.itemView.textViewNumPeopleGymItemTimer.text =
                    "${time.countTotal.toString()} / ${time.numPessoas.toString()}"

            



        }

        override fun getLayout() = R.layout.item_timer_list

    }

    private fun showPopupOption(view: View, time : Time) {
        val popUp = PopupMenu(mContext, view)
        popUp.inflate(R.menu.menu_popup_home)
        popUp.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menuVerAlunos -> {
                    val intent = Intent(mContext, AlunosListActivity::class.java)
                    intent.putExtra("time", time)
                    startActivity(intent)
                }
                R.id.menuEntrarHorario -> {
                    dialogHome.dialogConfirm(time)
                }
            }
            true
        }
        popUp.show()

    }
}