package com.gabriel.gymtimer.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.gymtimer.Dialogs.DialogHome
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.model.Time
import com.gabriel.gymtimer.ui.AlunosListActivity

class HomeAdapter (private val context : Context) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>(){
    private var timersList : MutableList<Time> = ArrayList()
    //private val dialogHome = DialogHome(mActivity)

    fun setTimers(timers : MutableList<Time>){
        timersList = timers
    }

    inner class HomeViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val textViewDayGymItemTimer = itemView.findViewById<TextView>(R.id.textViewDayGymItemTimer)
        val textViewTimesGymItemTimer = itemView.findViewById<TextView>(R.id.textViewTimesGymItemTimer)
        val textViewNumPeopleGymItemTimer = itemView.findViewById<TextView>(R.id.textViewNumPeopleGymItemTimer)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                val timeItem = timersList[position]
                showPopupOption(context,itemView,timeItem)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timer_list,parent,false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val time = timersList[position]

        val days = "${time.diaInico} - ${time.diaFinal}"
        val times = "${time.tempoInicio} - ${time.tempoFinal}"
        val total = "${time.countTotal.toString()} / ${time.numPessoas.toString()}"

        holder.textViewDayGymItemTimer.text = days
        holder.textViewTimesGymItemTimer.text = times
        holder.textViewNumPeopleGymItemTimer.text = total
    }

    override fun getItemCount() = timersList.size

    private fun showPopupOption(context: Context,view: View, time : Time) {
        val popUp = PopupMenu(context, view)
        popUp.inflate(R.menu.menu_popup_home)
        popUp.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menuVerAlunos -> {
                    val intent = Intent(context, AlunosListActivity::class.java)
                    intent.putExtra("time", time)
                    context.startActivity(intent)
                }
                R.id.menuEntrarHorario -> {
                    //dialogHome.dialogConfirm(time)
                }
            }
            true
        }
        popUp.show()

    }
}

