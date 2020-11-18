package com.gabriel.gymtimer.Dialogs

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.gabriel.gymtimer.Consts
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.ui.MainActivity

class DeleteDialog(private val mActivity : Activity) {

    fun showDeleteDialog(idTime : String){
        val alert = AlertDialog.Builder(mActivity)
        val inflater = mActivity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_delete, null)

        alert.setView(view)
        val dialog = alert.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val textViewMessageDeleteDialog = view.findViewById<TextView>(R.id.textViewMessageDeleteDialog)
        val textViewCancelDeleteDialog = view.findViewById<TextView>(R.id.textViewCancelDeleteDialog)
        val buttonDeleteDialog = view.findViewById<Button>(R.id.buttonDeleteDialog)

        buttonDeleteDialog.setOnClickListener {
            deleteTime(idTime)
        }
        textViewCancelDeleteDialog.setOnClickListener {
            dialog.cancel()
        }
        dialog.show()

    }

    private fun deleteTime(idTime : String) {
        FirebaseSingleton.getFirebaseFirestore()
            .collection(Consts.TIME_COLLECTION)
            .document(idTime)
            .delete()
            .addOnSuccessListener {
                val intent = Intent(mActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                mActivity.startActivity(intent)
            }
    }
}