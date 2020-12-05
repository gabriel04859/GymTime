package com.gabriel.gymtimer.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.gymtimer.*
import com.gabriel.gymtimer.Dialogs.DialogHome
import com.gabriel.gymtimer.Dialogs.LoadingDialog
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.adapter.TimeUserAdapter
import com.gabriel.gymtimer.model.Gym
import com.gabriel.gymtimer.model.User
import com.gabriel.gymtimer.ui.EditGymActivity
import com.gabriel.gymtimer.ui.LoginActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var gymOfBoss : Gym
    private lateinit var loadingDialog : LoadingDialog
    private lateinit var dialogHome : DialogHome
    private lateinit var imageGymProfile : ImageView
    private lateinit var textViewNameGymProfile : TextView
    private lateinit var textViewNFrequentaHorario : TextView
    private lateinit var imageButtonEditGym : ImageButton
    private lateinit var textViewTimesProfile : TextView
    private lateinit var recyclerTimeUser : RecyclerView

    private lateinit var timeUserAdapter: TimeUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)



    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog(activity!!)
        loadingDialog.starDialog()

        dialogHome = DialogHome(activity!!)
        initWidgets(view)
        timeUserAdapter = TimeUserAdapter(mContext,activity!!)

        imageButtonEditGym.setOnClickListener(this)
        /*FirebaseSingleton.getUser(object : FirestoreCallBack {
            override fun onGetUser(user: User){
                setValuesUser(user)
                if (user.boss!!){
                    getGymIfCurrentUser()
                    textViewTimesProfile.visibility = View.GONE
                    textViewNFrequentaHorario.visibility = View.GONE
                    imageButtonEditGym.visibility = View.VISIBLE

                }else{
                    loadingDialog.dialogDimiss()
                   setGymUserFrequenta()
                }

            }
        })

        imageButtonSignOut.setOnClickListener {
            signOut()
        }*/
    }

    /*private fun setGymUserFrequenta() {
        FirebaseSingleton.getGymUserFrequenta(object  : GymUserFrequentaFirebaseCallBack {
            override fun onGetUserGym(gym: Gym) {
                gym.let{
                    Picasso.with(mContext).load(gym.photo).into(imageGymProfile)
                    textViewNameGymProfile.text = it.name
                    getTimeOfUser()

                }
                textViewNameGymProfile.setOnClickListener {
                    val bottomSheet = HomeBottomSheetDialog(gym)
                    bottomSheet.show(activity!!.supportFragmentManager,"homeBottomSheet")
                }

            }


        })
    }

    private fun getTimeOfUser(){
        val timesList : MutableList<Time> = ArrayList()
        FirebaseSingleton
            .getFirebaseFirestore()
            .collection(TIME_COLLECTION)
            .whereArrayContains("listAlunosTime",FirebaseSingleton.getFirebaseAuth().uid!!)
            .addSnapshotListener { value, error ->
                value?.documentChanges.let { docs ->
                    if (docs == null){
                        textViewNFrequentaHorario.text = "Você ainda não esta frequentando algum horário"
                        return@addSnapshotListener

                    }
                    timesList.clear()
                    for (doc in docs){
                        when(doc.type){

                            DocumentChange.Type.ADDED ->{
                                textViewNFrequentaHorario.visibility = View.GONE
                                textViewTimesProfile.text = mContext.getString(R.string.hor_rios)
                                val time = doc.document.toObject(Time::class.java)
                                Log.i("TESTE","Time do user: ${time}")
                                timesList.add(time)
                                timeUserAdapter.setTimes(timesList)


                            }
                        }
                    }
                    recyclerTimeUser.adapter = timeUserAdapter
                    timeUserAdapter.notifyDataSetChanged()

                }
            }
    }*/
    private fun initWidgets(view: View) {
        imageGymProfile = view.findViewById(R.id.circleImageViewGymProfile)
        textViewNameGymProfile = view.findViewById(R.id.textViewNameGymProfile)
        textViewNFrequentaHorario = view.findViewById(R.id.textViewNFrequentaHorario)
        textViewTimesProfile = view.findViewById(R.id.textViewTimesProfile)
        imageButtonEditGym = view.findViewById(R.id.imageButtonEditGym)
        recyclerTimeUser = view.findViewById(R.id.recyclerTimeUser)
        recyclerTimeUser.apply {
            setHasFixedSize(true)
        }
    }


    private fun signOut() {
        FirebaseSingleton.getFirebaseAuth().signOut()
        Log.i("TESTE","Click singout")
        val intent = Intent(mContext,LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    private fun setValuesUser(user: User) {
        user.let{
            textViewNameProfile.text = it.name
            textViewEmailProfile.text = it.email
            textViewPhoneProfile.text = it.phone
            Picasso.with(mContext).load(it.imageUser).into(circleImageViewProfile)
        }
    }

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    /*private fun getGymIfCurrentUser(){
        FirebaseSingleton.getGym(object : GymFirebaseCallBack {
            override fun onGetGym(gym: Gym) {
                gymOfBoss = gym
                Picasso.with(mContext).load(gym.photo).into(imageGymProfile)

                textViewNameGymProfile.text = gym.name
                textViewNameGymProfile.setOnClickListener {
                    val bottomSheet = HomeBottomSheetDialog(gym)
                    bottomSheet.show(activity!!.supportFragmentManager,"homeBottomSheet")
                }
                loadingDialog.dialogDimiss()

            }


        })

    }*/

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.imageButtonEditGym ->{
               val i = Intent(mContext, EditGymActivity::class.java)
                i.putExtra("gym",gymOfBoss)
                mContext.startActivity(i)
            }


        }
    }


}