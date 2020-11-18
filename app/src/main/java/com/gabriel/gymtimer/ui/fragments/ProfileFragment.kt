package com.gabriel.gymtimer.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.gabriel.gymtimer.*
import com.gabriel.gymtimer.Dialogs.DialogHome
import com.gabriel.gymtimer.Dialogs.LoadingDialog
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.Firebase.FirestoreCallBack
import com.gabriel.gymtimer.Firebase.GymUserFrequentaFirebaseCallBack
import com.gabriel.gymtimer.model.Gym
import com.gabriel.gymtimer.model.User
import com.gabriel.gymtimer.ui.LoginActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.circleImageViewProfile
import kotlinx.android.synthetic.main.activity_profile.textViewEmailProfile
import kotlinx.android.synthetic.main.activity_profile.textViewNameProfile
import kotlinx.android.synthetic.main.activity_profile.textViewPhoneProfile
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {


    private lateinit var mContext: Context
    private lateinit var loadingDialog : LoadingDialog
    private lateinit var dialogHome : DialogHome
    private lateinit var textViewYourGym : TextView
    private lateinit var imageButtonExitGym : ImageButton
    private lateinit var textViewNameGymProfile : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseSingleton.getUser(object : FirestoreCallBack {
            override fun onGetUser(user: User){
                setValuesUser(user)
                if (user.boss!!){
                    //imageButtonDeleteTime.visibility = View.VISIBLE
                }

            }
        })

        imageButtonSignOut.setOnClickListener {
            signOut()
        }



        FirebaseSingleton.getGymUserFrequenta(object  : GymUserFrequentaFirebaseCallBack {
            override fun onGetUserGym(gym: Gym) {
            }

        })


    }

    private fun signOut() {
        FirebaseSingleton.getFirebaseAuth().signOut()
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

}