package com.gabriel.gymtimer.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.gabriel.gymtimer.*
import com.gabriel.gymtimer.Dialogs.LoadingDialog
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.model.Address
import com.gabriel.gymtimer.mvp.contract.BaseContract
import com.gabriel.gymtimer.mvp.presenter.NewGymPresenter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_new_gym.*
import java.util.*

class NewGymActivity : AppCompatActivity(), BaseContract.View {
    private val newGymPresenter by lazy {
        NewGymPresenter(this)
    }

    private lateinit var loadingDialog : LoadingDialog
    private var imageUri : Uri? = null
    private val ufArray = arrayOf("RJ", "SP", "MG", "RS", "PE", "PA","PR")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_gym)
        loadingDialog = LoadingDialog(this)
        loadingDialog.showDialogRegisterGym()

        imageButtonClose.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this,LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }

        buttonOpenGaleryNG.setOnClickListener {
            openGalery()
        }
        textViewUf.setOnClickListener {
            setUf(textViewUf)
        }


        ButtonSaveGym.setOnClickListener {
            getValues()
        }

    }

    private fun getValues() {
        loadingDialog.starDialog()
        val name = editTextNameGymNG.text.toString().trim()
        val cep = editTextCep.text.toString().trim()
        val rua = editTextRua.text.toString().trim()
        val num = editTextNum.text.toString().trim()
        val complemento = editTextComplemento.text.toString().trim()
        val cidade = editTextCidade.text.toString().trim()
        val uf = textViewUf.text.toString().trim()
        if(name.isEmpty() || cep.isEmpty()|| rua.isEmpty()|| num.isEmpty()|| complemento.isEmpty()|| cidade.isEmpty()){
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            loadingDialog.dialogDimiss()
            return
        }
        if (uf == "UF"){
            Toast.makeText(this,"Selecione uma UF",Toast.LENGTH_LONG).show()
            return
        }

        val fileName = UUID.randomUUID().toString()
        val reference = FirebaseSingleton.getStorageReference(Constants.STORAGE_REFERENCE_GYM)
            .child(fileName)
        if (imageUri == null){
            imageUri = Uri.parse("android.resource://$packageName/${R.drawable.gym_default}")
            reference.putFile(imageUri!!)
        }else{
            reference.putFile(imageUri!!)
        }
        reference.putFile(imageUri!!).addOnSuccessListener {
            reference.downloadUrl.addOnSuccessListener {
                Log.i("TESTE", "Download ref Uri: $it")
                val address = Address(uf,cep.toInt(),rua,num.toInt(),complemento,cidade)
                newGymPresenter.saveGym(name,address,it.toString())
            }

        }.addOnFailureListener {
            Toast.makeText(this, "Houve um erro ao salvar", Toast.LENGTH_SHORT).show()
            loadingDialog.dialogDimiss()
            Log.i("TESTE", "Erro ao salvar: $it")
        }


    }

    private fun setUf(textViewUf: TextView){
        val alertBuider = AlertDialog.Builder(this)
        alertBuider.setTitle("Escolha a UF")
        alertBuider.setIcon(R.drawable.ic_fitness)
        alertBuider.setItems(ufArray){_,position ->
            when(position){
                position -> {
                   val uf = ufArray[position]
                    textViewUf.setText(uf)
                }
            }
        }
        val alertDialog = alertBuider.create()
        alertDialog.show()
    }

    private fun openGalery() {
        val intentOpen = Intent(Intent.ACTION_PICK)
        intentOpen.type = "image/*"
        startActivityForResult(intentOpen, 1234)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == Activity.RESULT_OK && data?.data != null){
            imageUri = data.data
            imageViewGymNG.setImageURI(imageUri)
            buttonOpenGaleryNG.alpha = 0F
        }
    }

    override fun onSuccess() {
        val intent = Intent(this,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onFailure() {
        loadingDialog.dialogDimiss()
        Toast.makeText(this, getString(R.string.tente_mais_tarde), Toast.LENGTH_SHORT).show()
    }
}