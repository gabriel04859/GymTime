package com.gabriel.gymtimer.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.gabriel.gymtimer.Constants
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.Dialogs.LoadingDialog
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loadingDialog = LoadingDialog(this)

        buttonLogin.setOnClickListener {
            loadingDialog.starDialog()
            singUser()
        }

        textViewDontHaveAccount.setOnClickListener {
            showDialog()
        }



    }



    private fun showDialog() {
        val alert = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.dialog_type_user,null)

        val buttonBoss = view.findViewById<Button>(R.id.buttonBoss)
        val buttonAluno = view.findViewById<Button>(R.id.buttonAluno)
        alert.setView(view)
        val dialog = alert.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val i =  Intent(this, RegisterActivity::class.java)
        buttonAluno.setOnClickListener {
            i.putExtra("typeUser",false)
            startActivity(i)
            dialog.dismiss()
        }

        buttonBoss.setOnClickListener {
            i.putExtra("typeUser",true)
            startActivity(i)
            dialog.dismiss()
        }
    }






    private fun singUser() {
        val email = editTextEmailLogin.text.toString().trim()
        val password = editTextPasswordLogin.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            loadingDialog.dialogDimiss()
            return
        }

        val firebaseAuth  = FirebaseSingleton.getFirebaseAuth()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(!it.isSuccessful){
                    loadingDialog.dialogDimiss()
                    if (it.exception is FirebaseAuthInvalidCredentialsException){
                        Log.i("TESTE","Erro ao efetuar login: ${it.exception}")
                        Toast.makeText(this, "Senha inválida", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }else if (it.exception is FirebaseAuthInvalidUserException){
                        Log.i("TESTE","Erro ao efetuar login: ${it.exception}")
                        Toast.makeText(this, "Email não cadastrado", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }else if (it.exception is FirebaseAuthInvalidUserException){
                        Log.i("TESTE","Erro ao efetuar login: ${it.exception}")
                        Toast.makeText(this, "Email não cadastrado", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }else{

                        Log.i("TESTE","Erro ao efetuar login: ${it.exception}")
                        Toast.makeText(this, "Não foi possivel entrar", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                }else{
                    createUser(firebaseAuth)
                    Log.i("TESTE", "Login com Sucesso")
                }
            }.addOnFailureListener {
                //Toast.makeText(this, "NÃ£o foi possivel realizar login", Toast.LENGTH_SHORT).show()
                Log.i("TESTE", "Erro ao efetuar login: ${it.message}")

                loadingDialog.dialogDimiss()

            }
    }


    private fun createUser(firebaseAuth : FirebaseAuth){
        FirebaseSingleton.getFirebaseFirestore().collection(Constants.USER_COLLECTION)
            .document(firebaseAuth.uid!!).get().addOnSuccessListener {
                Log.i("TESTE", "user logado na login: ${firebaseAuth.uid}")
                val user = it.toObject(User::class.java)!!
                val i = Intent(this, LoadingInfomationActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                i.putExtra("user",user)
                startActivity(i)
            }


    }


}