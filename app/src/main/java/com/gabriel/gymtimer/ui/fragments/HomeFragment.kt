
package com.gabriel.gymtimer.ui.fragments

import android.app.Activity
import android.content.Context
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
import com.gabriel.gymtimer.Dialogs.LoadingDialog
import com.gabriel.gymtimer.Firebase.*
import com.gabriel.gymtimer.adapter.HomeAdapter
import com.gabriel.gymtimer.model.Gym
import com.gabriel.gymtimer.model.Time
import com.gabriel.gymtimer.model.User
import com.gabriel.gymtimer.utils.HomeBottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentChange

import com.squareup.picasso.Picasso

class HomeFragment : Fragment() {
    private lateinit var textViewNameGymHome: TextView
    private lateinit var textViewNotHaveTime : TextView
    private lateinit var buttonMoreInformation: ImageButton
    private lateinit var imageViewGymHome: ImageView
    private lateinit var recyclerHome: RecyclerView
    private lateinit var floatingActionButtonAddTime : FloatingActionButton

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var homeAdapter : HomeAdapter

    private lateinit var idGym: String
    private lateinit var imageGym: String
    private lateinit var mContext: Context

    private val addTimeDialog by lazy{
        AddTimeDialog(activity!!)
    }

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
        getUserInformation()

        floatingActionButtonAddTime.setOnClickListener {
         addTimeDialog.addTime(idGym)
        }

    }

    private fun getUserInformation() {
        FirebaseUtils.getCurrentUser(object : GetCurrentUserCallBack {
            override fun onGetCurrentUser(user: User) {
                user.let {
                    if (user.boss == false){
                        Log.i("TESTE", "Não é dono")
                        getGymUserGoes()
                        floatingActionButtonAddTime.visibility = View.GONE
                    }else{
                        Log.i("TESTE", "É dono")
                        getBossGym()

                    }
                }

            }

        })
    }

    private fun getGymUserGoes() {
        FirebaseUtils.getGymUserGoes(object : GetGymUserGoesCallBack{
            override fun onGetGymUserGoes(gym: Gym) {
                gym.let{
                    textViewNameGymHome.text = it.name
                    imageGym = it.photo!!

                    Picasso.with(mContext).load(imageGym).placeholder(R.drawable.gym_default).into(imageViewGymHome)

                }

                textViewNameGymHome.text = gym.name
                buttonMoreInformation.setOnClickListener {
                    val bottomSheet = HomeBottomSheetDialog(gym)
                    bottomSheet.show(activity!!.supportFragmentManager,"homeBottomSheet")

                }
                fetchTimes(gym)
            }

        })
    }

    private fun initWidgets(view: View) {
        textViewNameGymHome = view.findViewById(R.id.textViewNameGymHome)
        buttonMoreInformation = view.findViewById(R.id.buttonMoreInformation)
        imageViewGymHome = view.findViewById(R.id.imageViewGymHome)
        textViewNotHaveTime = view.findViewById(R.id.textViewNotHaveTime)
        floatingActionButtonAddTime = view.findViewById(R.id.floatingActionButtonAddTime)
        activity?.let {
            homeAdapter = HomeAdapter(mContext,it)
        }

        recyclerHome = view.findViewById(R.id.recyclerHome)
        recyclerHome.apply {
            layoutManager = GridLayoutManager(mContext,2)
            setHasFixedSize(true)

        }
        loadingDialog = LoadingDialog(context as Activity)
    }


    private fun getBossGym() {
        FirebaseUtils.getBossGym(object : GetBossGymCallBack{
            override fun onGetBossGym(gym: Gym) {
                gym.let {
                    Picasso.with(mContext).load(it.photo).into(imageViewGymHome)
                    textViewNameGymHome.text = it.name
                    idGym = it.idGym!!
                }
                buttonMoreInformation.setOnClickListener {
                    val bottomSheet = HomeBottomSheetDialog(gym)
                    bottomSheet.show(activity!!.supportFragmentManager,"homeBottomSheet")
                }
                fetchTimes(gym)
                loadingDialog.dialogDimiss()
            }

        })

    }

    private fun fetchTimes(gym: Gym) {
        val timersList : MutableList<Time> = ArrayList()
        FirebaseSingleton.getFirebaseFirestore().collection(TIME_COLLECTION)
            .whereEqualTo("idGym", gym.idGym)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                querySnapshot?.documentChanges.let { docs ->
                    if (docs != null) {
                        try {
                            loadingDialog.dialogDimiss()
                            for (doc in docs) {
                                when (doc.type) {
                                    DocumentChange.Type.ADDED -> {
                                        textViewNotHaveTime.visibility = View.GONE
                                        val time = doc.document.toObject(Time::class.java)
                                        timersList.add(time)
                                        homeAdapter.setTimers(timersList)
                                        homeAdapter.notifyDataSetChanged()


                                    }
                                   DocumentChange.Type.MODIFIED ->{
                                        textViewNotHaveTime.visibility = View.GONE
                                        val time = doc.document.toObject(Time::class.java)
                                        timersList.add(time)
                                        homeAdapter.setTimers(timersList)
                                        homeAdapter.notifyDataSetChanged()
                                    }
                                }
                            }
                            recyclerHome.adapter = homeAdapter

                        }catch (e : Exception){
                            Log.i("TESTE","Erro ao setar timers para recycler: ${e.message}")
                        }

                    }
                }
            }
    }



    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }



}