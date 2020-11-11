package com.gabriel.gymtimer.Dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.TextView
import com.gabriel.gymtimer.R

class LoadingDialog(val mActivity: Activity) {
    private var activity : Activity = mActivity
    private lateinit var dialog : Dialog

    fun starDialog(){
        val buider = AlertDialog.Builder(activity)
        val inflate = activity.layoutInflater
        buider.setView(inflate.inflate(R.layout.dialog_loading, null))
        buider.setCancelable(false)

        dialog = buider.create()
        dialog = buider.show()

    }

    fun  dialogDimiss(){
        dialog.dismiss()
    }

     fun showDialogRegisterGym() {
        val alert = androidx.appcompat.app.AlertDialog.Builder(mActivity)
        val inflater = mActivity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_regisrer_gym,null)

        val textViewOkDialog = view.findViewById<TextView>(R.id.textViewOkDialog)
        alert.setView(view)
        val dialog = alert.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
         textViewOkDialog.setOnClickListener {
             dialog.dismiss()
         }
    }

}