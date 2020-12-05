package com.gabriel.gymtimer.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.gabriel.gymtimer.Constants
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.adapter.SearchAdapter
import com.gabriel.gymtimer.model.Gym
import kotlinx.android.synthetic.main.activity_search_gym.*

class SearchGymActivity : AppCompatActivity() {
    private var gymList : List<Gym> = ArrayList()
    private var searchAdapter = SearchAdapter(gymList, this, FirebaseSingleton.getFirebaseAuth().uid!!)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_gym)

        imageButtonBackSearch.setOnClickListener {
            FirebaseSingleton.getFirebaseAuth().signOut()
            val i = Intent(applicationContext,LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }

        FirebaseSingleton.getFirebaseFirestore().collection(Constants.GYM_COLLECTION).get().addOnSuccessListener {
            for (doc in it){
                val gym = it.toObjects(Gym::class.java)
                Log.i("TESTE","List de $gym")
            }
        }


        recyclerViewSearch.let {
            it.hasFixedSize()
            it.adapter = searchAdapter

        }

        editTextSearchGym.addTextChangedListener(object  : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = editTextSearchGym.text.toString()
                searchInFirestore(searchText.toLowerCase())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun searchInFirestore(searchText: String) {
        FirebaseSingleton.getFirebaseFirestore()
            .collection(Constants.GYM_COLLECTION)
            .whereArrayContains("name_keywords", searchText)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    gymList = it.result!!.toObjects(Gym::class.java)
                    searchAdapter.searchList = gymList
                    searchAdapter.notifyDataSetChanged()
                }else{

                }
            }
    }
}