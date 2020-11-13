package com.gabriel.gymtimer.Dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.*
import com.gabriel.gymtimer.Consts.Companion.TIME_COLLECTION
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.model.Gym
import com.gabriel.gymtimer.model.Time
import java.text.SimpleDateFormat
import java.util.*

class DialogHome(val mActivity: Activity){
    private var activity : Activity = mActivity

    fun showDialogMoreInformation(gym: Gym){
        val buider = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_more_information,null)
        buider.setView(view)
        val dialog = buider.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val textViewNameMoreInfo = view.findViewById<TextView>(R.id.textViewNameMoreInfo)
        val textViewCEPMoreInfo = view.findViewById<TextView>(R.id.textViewCEPMoreInfo)
        val textViewNumMoreInfo = view.findViewById<TextView>(R.id.textViewNumMoreInfo)
        val textViewComplementosMoreInfo = view.findViewById<TextView>(R.id.textViewComplementosMoreInfo)
        val textViewCityMoreInfo = view.findViewById<TextView>(R.id.textViewCityMoreInfo)

        gym.let {
            textViewNameMoreInfo.text = it.name
            textViewCEPMoreInfo.text = "CEP: ${it.address!!.CEP.toString()}"
            textViewNumMoreInfo.text = "Número: ${it.address.num.toString()}"
            textViewComplementosMoreInfo.text ="Endereço: ${it.address.complemento}"
            textViewCityMoreInfo.text = "Cidade: ${it.address.cidade}"

        }
    }

    fun dialogConfirm(time : Time){
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_confirm,null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val textViewMessageMain = view.findViewById<TextView>(R.id.textViewMessageMain)
        val textViewMessaseSub = view.findViewById<TextView>(R.id.textViewMessaseSub)
        val textViewCancel = view.findViewById<TextView>(R.id.textViewCancel)
        val buttonConfirm = view.findViewById<Button>(R.id.buttonConfirm)

        textViewMessageMain.text = "Deseja frequentar esse horário?"
        textViewMessaseSub.text = "Das: ${time.tempoFinal} - Ás: ${time.tempoFinal}"
        textViewCancel.setOnClickListener {
            dialog.dismiss()
        }
        buttonConfirm.setOnClickListener {
            addUserToTime(time,dialog)
        }



    }

    private fun addUserToTime(time: Time, dialog: AlertDialog) {
        Log.i("TESTE","Chamou")
        FirebaseSingleton.getFirebaseFirestore().collection(TIME_COLLECTION)
            .document(time.idTime!!)
            .get()
            .addOnSuccessListener {
                val timeCurrent = it.toObject(Time::class.java)
                if (time.countTotal >= time.numPessoas!!){
                    Toast.makeText(mActivity, "Horário cheio", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener

                }
                if (time.listAlunosTime.contains(FirebaseSingleton.getFirebaseAuth().uid)){
                    Toast.makeText(mActivity, "Você já esta nesse horário", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                timeCurrent!!.countTotal = time.countTotal +1
                timeCurrent.listAlunosTime.add(FirebaseSingleton.getFirebaseAuth().uid!!)
                FirebaseSingleton.getFirebaseFirestore().collection(TIME_COLLECTION).document(time.idTime).set(timeCurrent).addOnSuccessListener {
                    dialog.dismiss()
                }.addOnFailureListener {
                    Log.i("TESTE", "Erro ao entrar na time: ${it.message}")
                }
            }.addOnFailureListener {
                Log.i("TESTE", "Erro obter a time: ${it.message}")
            }

    }


}
