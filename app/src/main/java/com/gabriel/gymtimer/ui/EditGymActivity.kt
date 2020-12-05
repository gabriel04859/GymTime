package com.gabriel.gymtimer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.model.Gym

class EditGymActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_gym)
        val gym = intent?.getParcelableExtra<Gym>("gym")
        Log.i("TESTE","Gym: $gym")
    }
}