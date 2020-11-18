package com.gabriel.gymtimer.Dialogs

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.gabriel.gymtimer.Consts.Companion.TIME_COLLECTION
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.model.Time
import com.gabriel.gymtimer.utils.SetTimeAndDay

class EditTimeDialog(private val mActivity : Activity) {
    private lateinit var textViewDayFirstEdit : TextView
    private lateinit var textViewEndEdit : TextView
    private lateinit var textViewTimeBeginEdit : TextView
    private lateinit var textViewTimeEndEdit : TextView
    private lateinit var editTextNumPeopleEdit : EditText
    private lateinit var buttonEditTime : Button

    private lateinit var dialog : AlertDialog

    private val setTimeAndDay by lazy {
        SetTimeAndDay(mActivity)
    }


    fun showDialogEditTime(idTime : String){
        val builder = AlertDialog.Builder(mActivity)
        val inflater = mActivity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_edit_time,null)
        builder.setView(view)
        dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        textViewDayFirstEdit = view.findViewById(R.id.textViewDayFirstEdit)
        textViewEndEdit = view.findViewById(R.id.textViewEndEdit)
        textViewTimeBeginEdit = view.findViewById(R.id.textViewTimeBeginEdit)
        textViewTimeEndEdit = view.findViewById(R.id.textViewTimeEndEdit)
        editTextNumPeopleEdit = view.findViewById(R.id.editTextNumPeopleEdit)
        buttonEditTime = view.findViewById(R.id.buttonEditTime)


        FirebaseSingleton.getFirebaseFirestore().collection(TIME_COLLECTION).document(idTime).get().addOnSuccessListener {
            val time = it.toObject(Time::class.java)
            Log.i("TESTE","Time : ${time}")
            setValuesToDialog(time!!)
        }

        dialog.show()

    }

    private fun setValuesToDialog(time: Time) {
        time.let {
            textViewDayFirstEdit.text = it.diaInico.toString()
            textViewEndEdit.text = it.diaFinal.toString()
            textViewTimeBeginEdit.text = it.tempoInicio.toString()
            textViewTimeEndEdit.text = it.tempoFinal.toString()
            editTextNumPeopleEdit.setText(it.numPessoas.toString())

        }

        textViewDayFirstEdit.setOnClickListener {
            setTimeAndDay.setDay(textViewDayFirstEdit)
        }
        textViewEndEdit.setOnClickListener {
            setTimeAndDay.setDay(textViewEndEdit)
        }
        textViewTimeBeginEdit.setOnClickListener {
            setTimeAndDay.setTime(textViewTimeBeginEdit)
        }
        textViewTimeEndEdit.setOnClickListener {
            setTimeAndDay.setTime(textViewTimeEndEdit)
        }


            buttonEditTime.setOnClickListener {
                val numPeoples  = editTextNumPeopleEdit.text.toString().toInt()
                val timeUpdate = Time(time.idTime,
                    time.idGym,
                    textViewDayFirstEdit.text.toString(),
                    textViewEndEdit.text.toString(),
                    textViewTimeBeginEdit.text.toString(),
                textViewTimeEndEdit.text.toString(),
                numPeoples,time.listAlunosTime,
                time.countTotal)
                Log.i("TESTE","Time update ${timeUpdate}")
               editTime(timeUpdate)
            }




    }

    private fun editTime(time: Time) {
        Log.i("TESTE","id time edit: ${time.idTime}")
        FirebaseSingleton.getFirebaseFirestore().collection(TIME_COLLECTION).document(time.idTime!!)
            .set(time).
            addOnSuccessListener {
                dialog.cancel()
                Log.i("TESTE","Update Time")

        }.addOnFailureListener {
                dialog.cancel()
                Log.i("TESTE","Failed update Time: ${it.message}")

        }
    }
}