package com.gabriel.gymtimer.Dialogs

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.TextView
import com.gabriel.gymtimer.Consts.Companion.TIME_COLLECTION
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.model.Time
import de.hdodenhof.circleimageview.CircleImageView

class DialogExitTime(private val activity: Activity) {
    fun showDialogExitTime(time : Time){
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_exit_time, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()




        val textViewCancelExitDialog = view.findViewById<TextView>(R.id.textViewCancelExitDialog)
        val buttonExitDialog = view.findViewById<TextView>(R.id.buttonExitDialog)

        textViewCancelExitDialog.setOnClickListener {
            dialog.cancel()
        }
        buttonExitDialog.setOnClickListener {
            Log.i("TESTE","Time antes de remover: ${time}")
            exitTime(time)
            dialog.cancel()

        }



    }

    private fun exitTime(timeRemoveUser: Time) {
        val position = timeRemoveUser.listAlunosTime.indexOf(FirebaseSingleton.getFirebaseAuth().uid)
        timeRemoveUser.listAlunosTime.removeAt(position)
        timeRemoveUser.countTotal -= timeRemoveUser.countTotal
        Log.i("TESTE","Position: ${position}")
        Log.i("TESTE","Time antes de remover: ${timeRemoveUser}")
        FirebaseSingleton.getFirebaseFirestore().collection(TIME_COLLECTION)
            .document(timeRemoveUser.idTime!!)
            .set(timeRemoveUser)
            .addOnSuccessListener {
            Log.i("TESTE","User removido do time com sucesso")
        }.addOnFailureListener {
            Log.i("TESTE","Erro ao remover user do time")
        }


    }
}