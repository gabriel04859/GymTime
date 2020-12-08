package com.gabriel.gymtimer.Dialogs

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.gabriel.gymtimer.Consts.Companion.TIME_COLLECTION
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.model.Time

class EnterTimeDialog(private val activity: Activity) {
    private lateinit var textViewCancelEnterTimeDialog : TextView
    private lateinit var buttonEnterTimeDialog : Button

    fun showEnterTimeDialog(time : Time?){
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_enter_time,null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        textViewCancelEnterTimeDialog = view.findViewById(R.id.textViewCancelEnterTimeDialog)
        buttonEnterTimeDialog = view.findViewById(R.id.buttonEnterTimeDialog)

        textViewCancelEnterTimeDialog.setOnClickListener {
            dialog.cancel()
        }
        buttonEnterTimeDialog.setOnClickListener {
            val uid = FirebaseSingleton.getFirebaseAuth().uid!!
            if (time!!.listAlunosTime.contains(uid)){
                Toast.makeText(activity, "Você já está nesse horário", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (time.numPessoas!! == time.countTotal){
                Toast.makeText(activity, "Horário cheio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
          time.idTime?.let {
              FirebaseSingleton.getFirebaseFirestore().collection(TIME_COLLECTION).document(it).get().addOnSuccessListener {doc ->
                  val timeCurrent = doc.toObject(Time::class.java)
                  Log.i("TESTE","Time q quer entrar: $time")

                  timeCurrent?.let {timeUpdate ->
                      timeUpdate.listAlunosTime.add(uid)
                      timeUpdate.countTotal += 1
                      FirebaseSingleton.getFirebaseFirestore().collection(TIME_COLLECTION).document(it).set(timeUpdate).addOnSuccessListener {
                          Log.i("TESTE","Add no time")
                          dialog.cancel()
                      }.addOnFailureListener {
                          Log.i("TESTE","Falha no time")
                          dialog.cancel()
                      }
                  }


              }
          }


        }


    }
}