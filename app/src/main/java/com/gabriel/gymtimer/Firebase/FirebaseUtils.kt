package com.gabriel.gymtimer.Firebase

import android.util.Log
import com.gabriel.gymtimer.Constants
import com.gabriel.gymtimer.Consts.Companion.USER_COLLECTION
import com.gabriel.gymtimer.model.Gym
import com.gabriel.gymtimer.model.User

object FirebaseUtils {

    fun getCurrentUser(getCurrentUserCallBack: GetCurrentUserCallBack){
        val uid = FirebaseSingleton.getFirebaseAuth().uid
        FirebaseSingleton.getFirebaseFirestore().collection(USER_COLLECTION)
            .document(uid!!)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                Log.i("TESTE", "user: $user")

                if (user != null){
                    getCurrentUserCallBack.onGetCurrentUser(user)
                }

        }


    }

    fun getBossGym(getBossGymCallBack: GetBossGymCallBack){
        FirebaseSingleton.getFirebaseFirestore().collection(Constants.GYM_COLLECTION)
            .whereEqualTo("idUser", FirebaseSingleton.getFirebaseAuth().uid).get()
            .addOnSuccessListener { docs ->
                for (doc in docs){
                    val gym = doc.toObject(Gym::class.java)
                    Log.i("TESTE","Id gym: ${gym.idGym}")
                    getBossGymCallBack.onGetBossGym(gym)
                }
            }

    }


    fun getGymUserGoes(getGymUserGoesCallBack: GetGymUserGoesCallBack){
        val uid : String = FirebaseSingleton.getFirebaseAuth().uid!!
        val gymRef = FirebaseSingleton.getFirebaseFirestore().collection(Constants.GYM_COLLECTION)
        gymRef.whereArrayContains("listAlunosGym", uid).get()
            .addOnSuccessListener { docs ->
                for (doc in docs){
                    val gym = doc.toObject(Gym::class.java)
                    Log.i("TESTE","Id gym: ${gym.idGym}")
                    getGymUserGoesCallBack.onGetGymUserGoes(gym)
                }
            }
    }
}