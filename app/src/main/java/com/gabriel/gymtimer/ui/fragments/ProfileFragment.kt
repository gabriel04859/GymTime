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
import android.widget.Toast
import com.gabriel.gymtimer.*
import com.gabriel.gymtimer.Dialogs.DialogHome
import com.gabriel.gymtimer.Dialogs.LoadingDialog
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.model.Gym
import com.gabriel.gymtimer.model.Time
import com.gabriel.gymtimer.model.User
import com.gabriel.gymtimer.ui.LoginActivity
import com.google.firebase.firestore.DocumentChange
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
        imageButtonExitGym = view.findViewById(R.id.imageButtonExitGym)
        textViewYourGym = view.findViewById(R.id.textViewYourGym)
        textViewNameGymProfile = view.findViewById(R.id.textViewNameGymProfile)
        loadingDialog = LoadingDialog(activity!!)
        loadingDialog.starDialog()
        dialogHome = DialogHome(activity!!)
        imageButtonSignOut.setOnClickListener {
            signOut()
        }

        FirebaseSingleton.getUser(object : FirestoreCallBack {
            override fun onGetUser(user: User){
                checkIfIsBoss(user)
                loadingDialog.dialogDimiss()
                imageButtonExitGym.setOnClickListener {
                    val userM = User(user.idUser,user.name,user.email,user.password,user.phone,user.imageUser,user.boss,false)

                    FirebaseSingleton.getFirebaseFirestore().collection(Constants.USER_COLLECTION).document(user.idUser!!).set(userM).addOnSuccessListener {
                        Toast.makeText(mContext, "Editado", Toast.LENGTH_SHORT).show()
                        signOut()
                    }
                }
            }
        })

        FirebaseSingleton.getGymUserFrequenta(object  : GymUserFrequentaFirebaseCallBack {
            override fun onGetUserGym(gym: Gym) {
                textViewNameGymProfile.text = gym.name
                fetchTimes(gym)
            }

        })


    }

    private fun fetchTimes(gym: Gym) {
        FirebaseSingleton.getFirebaseFirestore().collection("TIMERS").whereEqualTo("idGym",gym.idGym)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                querySnapshot?.documentChanges.let{docs ->
                    if(docs != null){
                        for (doc in docs){
                            when(doc.type){
                                DocumentChange.Type.ADDED -> {
                                   val time = doc.document.toObject(Time::class.java)
                                    textViewTimeProfile.text = "De: ${time.tempoInicio} Até: ${time.tempoFinal}"

                                }
                            }
                        }
                    }
                }
            }}
    private fun checkIfIsBoss(user: User) {
        setValuesUser(user)
        if (user.boss == false){

        }

    }

    private fun setValuesUser(user: User) {
        Picasso.with(mContext).load(user.imageUser).into(circleImageViewProfile)
        textViewNameProfile.text = user.name
        textViewEmailProfile.text = user.email
        textViewPhoneProfile.text = user.phone

    }

    private fun signOut() {
        FirebaseSingleton.getFirebaseAuth().signOut()
        val i = Intent(context, LoginActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}