package com.gabriel.gymtimer.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.gymtimer.Dialogs.DialogExitTime
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.model.Time

class TimeUserAdapter(private val context : Context,private val activity : Activity) : RecyclerView.Adapter<TimeUserAdapter.TimeUserViewHolder>(){

    private val dialogExitTime = DialogExitTime(activity)
    private var timeList : MutableList<Time> = ArrayList()
    fun setTimes(times : MutableList<Time>){
        timeList = times

    }
    inner class TimeUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textViewDayTimeUser = itemView.findViewById<TextView>(R.id.textViewDayTimeUser)
        val textViewTimeTimeUser = itemView.findViewById<TextView>(R.id.textViewTimeTimeUser)
        val textViewNumTimeUser = itemView.findViewById<TextView>(R.id.textViewNumTimeUser)

        init {

            itemView.setOnClickListener {
                val position = adapterPosition
                val time = timeList[position]
                dialogExitTime.showDialogExitTime(time)

            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeUserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_time_user,parent,false)
        return TimeUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeUserViewHolder, position: Int) {
        val time = timeList[position]
        val days = "${time.diaInico} - ${time.diaFinal}"
        val hours = "${time.tempoInicio} - ${time.tempoFinal}"
        val numPeople = "${time.numPessoas} - ${time.countTotal}"

        holder.textViewDayTimeUser.text = days
        holder.textViewTimeTimeUser.text = hours
        holder.textViewNumTimeUser.text = numPeople

    }

    override fun getItemCount() = timeList.size
}