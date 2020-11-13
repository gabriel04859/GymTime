package com.gabriel.gymtimer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.gabriel.gymtimer.Constants
import com.gabriel.gymtimer.Consts.Companion.USER_COLLECTION
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.adapter.AlunosAdapter
import com.gabriel.gymtimer.model.Time
import com.gabriel.gymtimer.model.User
import com.google.firebase.firestore.DocumentChange
import kotlinx.android.synthetic.main.activity_alunos_list.*
import kotlinx.android.synthetic.main.fragment_home.*

class AlunosListActivity : AppCompatActivity() {
    private lateinit var time: Time
    private lateinit var adapterAlunos : AlunosAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alunos_list)

        time = intent.extras?.getParcelable("time")!!
        Log.i("TESTE","$time")
        supportActionBar!!.hide()
        adapterAlunos = AlunosAdapter(applicationContext)

        showAllUsers()
    }

    private fun showAllUsers() {
        val alunosList : MutableList<User> = ArrayList()
        if (time.countTotal == 0){
            textVieAindaNaoTemAlunos.visibility = View.VISIBLE
            return
        }

        for (aluno in time.listAlunosTime){
            FirebaseSingleton.getFirebaseFirestore()
                .collection(USER_COLLECTION).document(aluno).get().addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    Log.i("TESTE","$user")
                    alunosList.add(user!!)
                    adapterAlunos.setAlunos(alunosList)
                    recyclerAlunosTurma.apply {
                        setHasFixedSize(true)
                        adapter = adapterAlunos
                    }
                }
        }


    }

}