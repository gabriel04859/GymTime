package com.gabriel.gymtimer.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.gymtimer.*
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.model.Gym
import com.gabriel.gymtimer.model.User
import com.gabriel.gymtimer.ui.MainActivity
import kotlinx.android.synthetic.main.item_search.view.*

class SearchAdapter(var searchList : List<Gym>, private val context : Context, private val idUser : String) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(gym: Gym){
            itemView.textViewNameGymItem.text = gym.name
            itemView.textViewCityGymItem.text = gym.address!!.cidade
        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                val gym  = searchList[position]

                addTrueGymToUser(gym)


            }
        }

    }

    private fun addTrueGymToUser(gym: Gym) {
        FirebaseSingleton.getUser(object : FirestoreCallBack {
            override fun onGetUser(user: User){
                user.frequentaGym = true
                gym.listAlunosGym.add(user.idUser!!)
                updateUserFrequentaGym(user,gym)
            }
        })

    }

    private fun updateUserFrequentaGym(user: User?, gym: Gym) {
        FirebaseSingleton.getFirebaseFirestore().collection(Constants.USER_COLLECTION).document(idUser).set(user!!)
            .addOnSuccessListener {
                FirebaseSingleton.getFirebaseFirestore().collection(Constants.GYM_COLLECTION).document(gym.idGym!!).set(gym)
                    .addOnSuccessListener {
                        val i = Intent(context,MainActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(i)
                    }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search,parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(searchList[position])
    }

    override fun getItemCount() = searchList.size
}