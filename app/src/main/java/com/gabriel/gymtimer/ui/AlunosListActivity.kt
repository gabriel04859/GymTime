package com.gabriel.gymtimer.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import com.gabriel.gymtimer.Consts.Companion.USER_COLLECTION
import com.gabriel.gymtimer.Dialogs.DeleteDialog
import com.gabriel.gymtimer.Dialogs.EditTimeDialog
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.Firebase.FirebaseUtils
import com.gabriel.gymtimer.Firebase.GetCurrentUserCallBack
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.adapter.AlunosAdapter
import com.gabriel.gymtimer.model.Time
import com.gabriel.gymtimer.model.User
import kotlinx.android.synthetic.main.activity_alunos_list.*

class AlunosListActivity : AppCompatActivity() {
    private lateinit var time: Time
    private lateinit var adapterAlunos : AlunosAdapter
    private  val deleteDialog by lazy{
        DeleteDialog(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alunos_list)

        time = intent.extras?.getParcelable("time")!!
        Log.i("TESTE","$time")
        supportActionBar!!.hide()
        adapterAlunos = AlunosAdapter(applicationContext)

        FirebaseUtils.getCurrentUser(object : GetCurrentUserCallBack{
            override fun onGetCurrentUser(user: User) {
                if (user.boss == true){
                    imageButtonEditTime.visibility = View.VISIBLE
                }
            }

        })

        showAllUsers()
        imageButtonBackAlunosList.setOnClickListener {
            val i = Intent(applicationContext,MainActivity::class.java)
            finish()
            startActivity(i)
        }
        imageButtonEditTime.setOnClickListener {
            showPopup(it)
        }
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

    private fun showPopup(view: View) {
        val popUp = PopupMenu(applicationContext, view)
        popUp.inflate(R.menu.menu_popup_alunos)
        popUp.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menuDeleteTime -> {
                    deleteDialog.showDeleteDialog(time.idTime!!)
                }
                R.id.menuEditTime -> {
                    val dialogEdit = EditTimeDialog(this)
                    dialogEdit.showDialogEditTime(time.idTime!!)
                }
            }
            true
        }
        popUp.show()
    }

}