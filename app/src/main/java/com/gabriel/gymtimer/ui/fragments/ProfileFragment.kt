package com.gabriel.gymtimer.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.gymtimer.*
import com.gabriel.gymtimer.Dialogs.LoadingDialog
import com.gabriel.gymtimer.Firebase.*
import com.gabriel.gymtimer.adapter.TimeUserAdapter
import com.gabriel.gymtimer.model.Gym
import com.gabriel.gymtimer.model.User
import com.gabriel.gymtimer.ui.EditGymActivity
import com.gabriel.gymtimer.ui.LoginActivity
import com.gabriel.gymtimer.utils.HomeBottomSheetDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var gymOfBoss : Gym
    private lateinit var loadingDialog : LoadingDialog
    private lateinit var imageGymProfile : ImageView
    private lateinit var imageButtonSignOut : ImageButton
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
        initWidgets(view)

        imageButtonEditGym.setOnClickListener(this)
        imageButtonSignOut.setOnClickListener(this)

        getCurrentUserProfile()
    }

    private fun getCurrentUserProfile() {
        FirebaseUtils.getCurrentUser(object : GetCurrentUserCallBack {
            override fun onGetCurrentUser(user: User) {
                setValuesUser(user)
                if (user.boss == false){

                }else{
                    getBossGym()
                }
                loadingDialog.dialogDimiss()
            }

        })
    }

    private fun getBossGym() {
        FirebaseUtils.getBossGym(object : GetBossGymCallBack{
            override fun onGetBossGym(gym: Gym) {
                textViewNameGymProfile.text = gym.name
                Picasso.with(mContext).load(gym.photo).into(imageGymProfile)
                textViewNameGymProfile.setOnClickListener {
                    val bottomSheet = HomeBottomSheetDialog(gym)
                    bottomSheet.show(activity!!.supportFragmentManager,"homeBottomSheet")

                }
            }

        })

    }
    private fun initWidgets(view: View) {
        imageGymProfile = view.findViewById(R.id.circleImageViewGymProfile)
        textViewNameGymProfile = view.findViewById(R.id.textViewNameGymProfile)
        textViewNFrequentaHorario = view.findViewById(R.id.textViewNFrequentaHorario)
        textViewTimesProfile = view.findViewById(R.id.textViewTimesProfile)
        imageButtonSignOut = view.findViewById(R.id.imageButtonSignOut)
        imageButtonEditGym = view.findViewById(R.id.imageButtonEditGym)
        recyclerTimeUser = view.findViewById(R.id.recyclerTimeUser)
        recyclerTimeUser.apply {
            setHasFixedSize(true)
        }
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

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.imageButtonEditGym ->{
               val i = Intent(mContext, EditGymActivity::class.java)
                i.putExtra("gym",gymOfBoss)
                mContext.startActivity(i)
            }
            R.id.imageButtonSignOut->{
                FirebaseSingleton.getFirebaseAuth().signOut()
                val i = Intent(mContext, LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                mContext.startActivity(i)
            }


        }
    }

    private fun getGymUserGoes(){

    }


}