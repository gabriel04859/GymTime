package com.gabriel.gymtimer.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.widget.TextView
import com.gabriel.gymtimer.R
import java.text.SimpleDateFormat
import java.util.*

class SetTimeAndDay(private val mActivity : Activity) {
    private val daysArray = arrayOf("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo")
    private var day = ""

     fun setDay(textView: TextView) {
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

     fun setTime(textView: TextView) {
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


}