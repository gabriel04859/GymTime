package com.gabriel.gymtimer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.model.Gym
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_gym.*

class EditGymActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_gym)
        val gym = intent?.getParcelableExtra<Gym>("gym")
        gym?.let {
            Picasso.with(applicationContext).load(it.photo).into(imageViewGymEG)
            editTextNameGymEdit.setText(it.name)
            textViewUFEdit.text = it.address!!.UF
            editTextCepEdit.setText(it.address.CEP.toString())
            editTextRuaEdit.setText(it.address.rua)
            editTextNumEdit.setText(it.address.num.toString())
            editTextComplementoEdit.setText(it.address.complemento)
            editTextCidadeEdit.setText(it.address.cidade)

        }
    }
}