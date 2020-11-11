package com.gabriel.gymtimer.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.gabriel.gymtimer.Constants
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.Dialogs.LoadingDialog
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.model.User
import com.gabriel.gymtimer.mvp.contract.RegisterContract
import com.gabriel.gymtimer.mvp.presenter.RegisterPresenter
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity(), RegisterContract.View, View.OnClickListener {
    private var imageUri: Uri? = null
    private lateinit var loadingDialog: LoadingDialog
    private val registerPresenter = RegisterPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        loadingDialog = LoadingDialog(this)

        setContentView(R.layout.activity_register)


        buttonRegister.setOnClickListener(this)

        buttonOpenGalery.setOnClickListener(this)


        imageButtonBackRegister.setOnClickListener(this)


    }


    private fun openGalery() {
        val intentOpen = Intent(Intent.ACTION_PICK)
        intentOpen.type = "image/*"
        startActivityForResult(intentOpen, 123)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == Activity.RESULT_OK && data?.data != null) {
            imageUri = data.data
            imageViewUserPhotoRegister.setImageURI(imageUri)
            buttonOpenGalery.alpha = 0F
        }
    }

    private fun getValues() {
        val name = editTextNameRegister.text.toString().trim()
        val email = editTextEmailRegister.text.toString().trim()
        val password = editTextPasswordRegister.text.toString().trim()
        val phone = editTextPhoneRegister.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            Toast.makeText(applicationContext, "Preencha todos os campos", Toast.LENGTH_SHORT)
                .show()
            loadingDialog.dialogDimiss()
            return
        }


        val fileName = UUID.randomUUID().toString()
        val reference = FirebaseSingleton.getStorageReference(Constants.STORAGE_REFERENCE_USER).child(fileName)
        if (imageUri == null) {
            imageUri = Uri.parse("android.resource://$packageName/${R.drawable.user_default}")
            reference.putFile(imageUri!!)
        } else {
            reference.putFile(imageUri!!)
        }

        val typeUser = intent.getBooleanExtra("typeUser", false)
        reference.putFile(imageUri!!).addOnSuccessListener {
            reference.downloadUrl.addOnSuccessListener {
                Log.i("TESTE", "Download ref Uri: $it")
                val idUser = FirebaseSingleton.getFirebaseAuth().uid
                Log.i("TESTE", "Id User ${idUser}")
                val user = User(idUser, name, email, password, phone, it.toString(), typeUser, false)
                registerPresenter.createUser(user)
            }

        }.addOnFailureListener {
            loadingDialog.dialogDimiss()
            Log.i("TESTE", "Erro ao salvar: $it")
        }

    }
    private fun showToast(msg : String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        loadingDialog.dialogDimiss()
    }


    override fun onFailure(message: String) {
        showToast(message)
    }

    override fun onSuccess() {
        val intent = Intent(applicationContext, LoadingInfomationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonRegister -> {
                loadingDialog.starDialog()
                getValues()
            }

            R.id.buttonOpenGalery -> {
                openGalery()
            }

            R.id.imageButtonBackRegister -> {
                onBackPressed()
            }
        }

    }

}