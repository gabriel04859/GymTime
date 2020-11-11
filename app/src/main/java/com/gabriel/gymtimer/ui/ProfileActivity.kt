package com.gabriel.gymtimer.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gabriel.gymtimer.Constants
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.model.Gym
import com.gabriel.gymtimer.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var gym : Gym
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar!!.hide()
        gym = intent.extras?.getParcelable<Gym>("gym")!!
        setGymValue(gym)
        setUserInformation()

        imageButtonBack.setOnClickListener {
            onBackPressed()
        }
        imageButtonSettings.setOnClickListener {
            FirebaseSingleton.getFirebaseAuth().signOut()
            val i = Intent(this, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }

    }

    private fun setUserInformation() {
        FirebaseSingleton.getFirebaseFirestore()
            .collection(Constants.USER_COLLECTION)
            .document(FirebaseSingleton.getFirebaseAuth().uid!!)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                if (user != null){
                    Picasso.with(applicationContext).load(user.imageUser).into(circleImageViewProfile)
                    textViewNameProfile.text = user.name
                    textViewEmailProfile.text = user.email
                    textViewPhoneProfile.text = user.phone
                }

            }
    }

    private fun setGymValue(gym: Gym) {
        gym.let {
            Picasso.with(applicationContext).load(gym.photo).into(circleImageViewGymProfile)
            textViewNameGymProfile.text = it.name
            textViewCepProfile.text = "Cep - ${it.address!!.CEP.toString()}"
            textViewRuaProfile.text = "Rua: ${it.address.rua}"
            textViewNumProfile.text = "Número: ${it.address.num.toString()}"
            textViewComplementoProfile.text = "Complemento: ${it.address.complemento}"
            textViewCidadeProfile.text = "Cidade: ${it.address.cidade}"
        }


    }

}