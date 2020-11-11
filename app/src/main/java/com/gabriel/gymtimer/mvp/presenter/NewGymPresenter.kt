package com.gabriel.gymtimer.mvp.presenter

import android.util.Log
import com.gabriel.gymtimer.Consts.Companion.GYM_COLLECTION
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.model.Address
import com.gabriel.gymtimer.model.Gym
import com.gabriel.gymtimer.mvp.contract.BaseContract
import com.gabriel.gymtimer.mvp.contract.NewGymContract

class NewGymPresenter(private val view : BaseContract.View) : NewGymContract.Presenter {

    override fun saveGym(name: String, address: Address, imageGym: String) {
        val firestore = FirebaseSingleton.getFirebaseFirestore().collection(GYM_COLLECTION).document()
        val idGym = firestore.id
        Log.i("TESTE","Id gym: $idGym")
        val idUser = FirebaseSingleton.getFirebaseAuth().uid
        val searchKeywords = generateSearchKeywords(name)
        val gym = Gym(idGym,name,searchKeywords,address,imageGym,idUser!!)
        firestore.set(gym).addOnSuccessListener {
            view.onSuccess()

    }.addOnFailureListener {
        Log.i("TESTE","$it.message")
        view.onFailure()

        }
    }

    private fun generateSearchKeywords(name: String) : List<String>{
        var inputString = name.toLowerCase()
        val keywords = mutableListOf<String>()

        val words = inputString.split(" ")
        for (word in words){
            var appendString : String
            for (charPosition in inputString.indices){
                appendString = inputString[charPosition].toString()
                keywords.add(appendString)
            }

            inputString = inputString.replace("$word ", "")

        }
        return keywords

    }
}