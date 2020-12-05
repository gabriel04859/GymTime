package com.gabriel.gymtimer.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.gabriel.gymtimer.R
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_splash_screen)



        Handler().postDelayed({
           //autheticationUser()
        },3000)
    }



    private fun autheticationUser() {
        if (FirebaseAuth.getInstance().currentUser == null){
            Log.i("TESTE","Usuario nulo")
            val i = Intent(this, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            return
        } else {
            Log.i("TESTE", "${FirebaseAuth.getInstance().uid}")
            val i = Intent(this, LoadingInfomationActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            return
        }

    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }
    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        if (firebaseAuth.currentUser == null){
            Log.i("TESTE","Usuario nulo")
            val i = Intent(this, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            return
        }else{
            Log.i("TESTE", "${FirebaseAuth.getInstance().uid}")
            val i = Intent(this, LoadingInfomationActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            return
        }

    }
}
