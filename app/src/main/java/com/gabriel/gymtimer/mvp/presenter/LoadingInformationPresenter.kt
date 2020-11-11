package com.gabriel.gymtimer.mvp.presenter

import android.content.Intent
import android.util.Log
import com.gabriel.gymtimer.Constants
import com.gabriel.gymtimer.Consts
import com.gabriel.gymtimer.Consts.Companion.GYM_COLLECTION
import com.gabriel.gymtimer.Firebase.FirebaseQuerys
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.GymUserFrequentaFirebaseCallBack
import com.gabriel.gymtimer.model.Gym
import com.gabriel.gymtimer.model.User
import com.gabriel.gymtimer.mvp.contract.LoadingInformantionContract
import com.gabriel.gymtimer.ui.MainActivity
import com.gabriel.gymtimer.ui.NewGymActivity

class LoadingInformationPresenter(
    private val view : LoadingInformantionContract.View) : LoadingInformantionContract.Presenter{
    override fun checkUser(user: User) {
        if (user.boss == false){
            checkIfUserFrequentaGym(user)
        }else{
            checkIfGymAlreadyExists(user)
        }
    }

     private fun checkIfUserFrequentaGym(user: User){
         if (user.frequentaGym == false){
             view.onUserNaoFrequentaGym()
         }else{
             view.onUserFrequentaGym()
         }
     }

     private fun checkIfGymAlreadyExists(user: User) {
         FirebaseSingleton.getFirebaseFirestore().collection(GYM_COLLECTION)
             .whereEqualTo("idUser", user.idUser)
             .get().addOnSuccessListener {
                 if (it.isEmpty){
                     view.onGymNotExists()
                     return@addOnSuccessListener

                 }
                 for (d in it){
                     view.onGymExists()
                 }
             }
     }

 }