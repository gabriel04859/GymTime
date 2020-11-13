package com.gabriel.gymtimer.Dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.gabriel.gymtimer.Consts.Companion.TIME_COLLECTION
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.model.Time
import java.text.SimpleDateFormat
import java.util.*

class AddTimeDialog (private val mActivity : Activity){

    private val daysArray = arrayOf("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo")
    private var day = ""

    fun addTime(idGym : String){
    val alert = AlertDialog.Builder(mActivity)
    val inflater = mActivity.layoutInflater
    val view = inflater.inflate(R.layout.dialog_add_time, null)

    alert.setView(view)
    val dialog = alert.create()
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.show()
    val textViewDayFirst = view.findViewById<TextView>(R.id.textViewDayFirst)
    val textViewEnd = view.findViewById<TextView>(R.id.textViewEnd)
    val textViewTimeBegin = view.findViewById<TextView>(R.id.textViewTimeBegin)
    val textViewTimeEnd = view.findViewById<TextView>(R.id.textViewTimeEnd)
    val editTextNumPeople = view.findViewById<EditText>(R.id.editTextNumPeople)
    val buttonSaveTime = view.findViewById<Button>(R.id.buttonSaveTime)


    textViewDayFirst.setOnClickListener {
        setDay(textViewDayFirst)
    }
    textViewEnd.setOnClickListener {
        setDay(textViewEnd)
    }
    textViewTimeBegin.setOnClickListener {
        setTime(textViewTimeBegin)
    }
    textViewTimeEnd.setOnClickListener {
        setTime(textViewTimeEnd)
    }

    buttonSaveTime.setOnClickListener {
        val numPeoples = editTextNumPeople.text.toString().trim()

        if (textViewDayFirst.text == "De:" || textViewEnd.text == "Até:") {
            Toast.makeText(mActivity, "Por favor selecione os dias", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        if (textViewTimeBegin.text == "00:00" || textViewTimeEnd.text == "00:00") {
            Toast.makeText(mActivity, "Por favor selecione todos os horários", Toast.LENGTH_SHORT)
                .show()
            return@setOnClickListener
        }
        if (numPeoples == "0" || numPeoples.isEmpty()) {
            Toast.makeText(mActivity, "Por favor inisira o número de alunos", Toast.LENGTH_SHORT)
                .show()
            return@setOnClickListener
        }
        saveTme(
            textViewDayFirst.text.toString(),
            textViewEnd.text.toString(),
            textViewTimeBegin.text.toString(),
            textViewTimeEnd.text.toString(),
            numPeoples,
            dialog, idGym
        )
    }


}
    private fun setTime(textView: TextView) {
        val calender = Calendar.getInstance()
        val timerListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calender.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calender.set(Calendar.MINUTE, minute)
            textView.text = SimpleDateFormat("HH:mm").format(calender.time)
        }
        TimePickerDialog(
            mActivity,
            timerListener,
            calender.get(Calendar.HOUR_OF_DAY),
            calender.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun setDay(textView: TextView) {
        val alertBuilder = AlertDialog.Builder(mActivity)
        alertBuilder.setTitle("Escolha um dia")
        alertBuilder.setIcon(R.drawable.ic_fitness)
        alertBuilder.setItems(daysArray) { _, position ->
            when (position) {
                position -> {
                    day = daysArray[position]
                    textView.text = day
                }
            }
        }
        val alertDialog = alertBuilder.create()
        alertDialog.show()
    }

    private fun saveTme(
        startDay: String,
        finalDay: String,
        startTime: String,
        finalTime: String,
        numPeoples: String,
        dialog: AlertDialog,
        idGym: String
    ) {
        val firestore = FirebaseSingleton.getFirebaseFirestore().collection(TIME_COLLECTION).document()
        val idTime = firestore.id
        val time = Time(idTime, idGym, startDay, finalDay, startTime, finalTime, numPeoples.toInt())
        firestore.set(time).addOnCompleteListener {
            Log.i("TESTE", "Time save $time")
            dialog.dismiss()

        }.addOnFailureListener {
            Log.i("TESTE", "Erro ao salvar time: ${it.message}")
            dialog.dismiss()
            Toast.makeText(mActivity, "Erro ao salvar", Toast.LENGTH_SHORT).show()
        }


    }

}