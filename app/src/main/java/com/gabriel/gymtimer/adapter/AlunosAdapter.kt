package com.gabriel.gymtimer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.model.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AlunosAdapter (val context : Context) : RecyclerView.Adapter<AlunosAdapter.AlunosViewHolder>(){
    private var alunosList : MutableList<User> = ArrayList()
    fun setAlunos(alunos : MutableList<User>){
        alunosList = alunos
    }

    inner class AlunosViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val circleImageViewAluno = itemView.findViewById<CircleImageView>(R.id.circleImageViewAluno)
        val textViewAlunoName = itemView.findViewById<TextView>(R.id.textViewAlunoName)
        val textViewAlunoPhone = itemView.findViewById<TextView>(R.id.textViewAlunoPhone)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlunosViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_alunos, parent, false)
        return AlunosViewHolder(view)
    }

    override fun getItemCount() = alunosList.size

    override fun onBindViewHolder(holder: AlunosViewHolder, position: Int) {
        val user = alunosList[position]
        holder.textViewAlunoName.text = user.name
        holder.textViewAlunoPhone.text = user.phone
        Picasso.with(context).load(user.imageUser).placeholder(R.drawable.user_default).into(holder.circleImageViewAluno)
    }

}