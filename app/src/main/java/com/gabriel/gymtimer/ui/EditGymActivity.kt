package com.gabriel.gymtimer.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.gabriel.gymtimer.Constants
import com.gabriel.gymtimer.Consts.Companion.GYM_COLLECTION
import com.gabriel.gymtimer.Consts.Companion.ufArray
import com.gabriel.gymtimer.Dialogs.LoadingDialog
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.model.Address
import com.gabriel.gymtimer.model.Gym
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_gym.*
import kotlinx.android.synthetic.main.activity_new_gym.*
import java.util.*

class EditGymActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var gym: Gym
    private val loadingDialog by lazy{
        LoadingDialog(this)
    }
    private var imageUri : Uri? = null
    private lateinit var textViewUFEdit : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_gym)
        loadingDialog.starDialog()
        textViewUFEdit = findViewById(R.id.textViewUFEdit)
        gym = intent?.getParcelableExtra("gym")!!
        buttonOpenGaleryEG.alpha = 0F
        gym.let {
            Picasso.with(applicationContext).load(it.photo).into(imageViewGymEG)
            editTextNameGymEdit.setText(it.name)
            editTextCepEdit.setText(it.address!!.CEP.toString())
            editTextRuaEdit.setText(it.address.rua)
            editTextNumEdit.setText(it.address.num.toString())
            editTextComplementoEdit.setText(it.address.complemento)
            editTextCidadeEdit.setText(it.address.cidade)
            textViewUFEdit.text = it.address.UF
            loadingDialog.dialogDimiss()
        }

        buttonOpenGaleryEG.setOnClickListener(this)
        textViewUFEdit.setOnClickListener(this)
        ButtonEditGym.setOnClickListener(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1234 && resultCode == Activity.RESULT_OK && data?.data != null){
            imageUri = data.data
            imageViewGymEG.setImageURI(imageUri)
            buttonOpenGaleryEG.alpha = 0F
        }
    }

    private fun openGalery() {
        val intentOpen = Intent(Intent.ACTION_PICK)
        intentOpen.type = "image/*"
        startActivityForResult(intentOpen, 1234)
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

    override fun onClick(v: View) {
        when(v.id){
            R.id.buttonOpenGaleryEG -> openGalery()
            R.id.textViewUFEdit -> setUf(textViewUFEdit)
            R.id.ButtonEditGym -> getValues()
        }
    }

    private fun getValues() {
        loadingDialog.starDialog()
        val name = editTextNameGymEdit.text.toString().trim()
        val cep = editTextCepEdit.text.toString().trim()
        val rua = editTextRuaEdit.text.toString().trim()
        val num = editTextNumEdit.text.toString().trim()
        val complemento = editTextComplementoEdit.text.toString().trim()
        val cidade = editTextCidadeEdit.text.toString().trim()
        val uf = textViewUFEdit.text.toString().trim()
        if(name.isEmpty() || cep.isEmpty()|| rua.isEmpty()|| num.isEmpty()|| complemento.isEmpty()|| cidade.isEmpty()){
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            loadingDialog.dialogDimiss()
            return
        }
        if (uf == "UF"){
            Toast.makeText(this,"Selecione uma UF", Toast.LENGTH_LONG).show()
            loadingDialog.dialogDimiss()
            return
        }
        val searchKeyWords = generateSearchKeywords(name)
        val addresses = Address(uf,cep.toInt(),rua,num.toInt(),complemento,cidade)
        val gymUpdate = Gym(gym.idGym,name,searchKeyWords,addresses,gym.photo,gym.idUser,gym.listAlunosGym)
        FirebaseSingleton.getFirebaseFirestore().collection(GYM_COLLECTION).document(gymUpdate.idGym!!).set(gymUpdate).addOnSuccessListener {
            val i = Intent(applicationContext,MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
        }.addOnFailureListener {
            loadingDialog.dialogDimiss()
            Toast.makeText(applicationContext, "Não foi possivel, Tente novamente mais tarde.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun generateSearchKeywords(name: String) : List<String>{
        var inputString = name.toLowerCase()
        val keywords = mutableListOf<String>()

        val words = inputString.split(" ")
        for (word in words){
            var appendString : String
            for (charPosition in inputString.indices){
                appendString = inputString[charPosition].toString()
                keywords.add(appendString)
            }

            inputString = inputString.replace("$word ", "")

        }
        return keywords

    }
}