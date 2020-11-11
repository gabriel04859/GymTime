package com.gabriel.gymtimer.Firebase

import android.util.Log
import com.gabriel.gymtimer.Constants
import com.gabriel.gymtimer.FirestoreCallBack
import com.gabriel.gymtimer.GymFirebaseCallBack
import com.gabriel.gymtimer.GymUserFrequentaFirebaseCallBack
import com.gabriel.gymtimer.model.Gym
import com.gabriel.gymtimer.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object FirebaseSingleton{

    private var firebaseAuth : FirebaseAuth? = null
    private var firebaseFirestore : FirebaseFirestore? = null
    private var firebaseUser : FirebaseUser? = null
    private var storageReference : StorageReference? = null
    private var user : User? = null


    fun getFirebaseAuth() : FirebaseAuth{
        if (firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance()
        }
        return firebaseAuth as FirebaseAuth
    }


    fun getFirebaseUser(): FirebaseUser{
        if (firebaseUser == null){
            firebaseUser = getFirebaseAuth().currentUser
        }
        return firebaseUser as FirebaseUser
    }
    fun getFirebaseFirestore() : FirebaseFirestore{
        if (firebaseFirestore == null){
            firebaseFirestore = FirebaseFirestore.getInstance()
        }
        return  firebaseFirestore as FirebaseFirestore
    }

    fun getStorageReference(reference : String) : StorageReference{
        if(storageReference == null){
            storageReference = FirebaseStorage.getInstance().getReference(reference)
        }
        return storageReference as StorageReference
    }

    fun getUser(firestoreCallBack : FirestoreCallBack) {
        getFirebaseFirestore().collection(Constants.USER_COLLECTION).document(getFirebaseAuth().uid!!).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                Log.i("TESTE", "user: $user")
                firestoreCallBack.onGetUser(user!!)
            }
    }

    fun getGym(gymFirebaseCallBack: GymFirebaseCallBack){
        getFirebaseFirestore().collection(Constants.GYM_COLLECTION)
            .whereEqualTo("idUser", getFirebaseAuth().uid).get()
            .addOnSuccessListener { docs ->
                for (doc in docs){
                    val gym = doc.toObject(Gym::class.java)
                    Log.i("TESTE","Id gym: ${gym.idGym}")
                    gymFirebaseCallBack.onGetGym(gym)
                }
            }
    }

    fun getGymUserFrequenta(gymUserFrequentaFirebaseCallBack : GymUserFrequentaFirebaseCallBack){
        val idUser : String = getFirebaseAuth().uid!!
        val gymRef = getFirebaseFirestore().collection(Constants.GYM_COLLECTION)
        gymRef.whereArrayContains("listAlunosGym", idUser).get()
            .addOnSuccessListener { docs ->
                for (doc in docs){
                    val gym = doc.toObject(Gym::class.java)
                    Log.i("TESTE","Id gym: ${gym.idGym}")
                    gymUserFrequentaFirebaseCallBack.onGetUserGym(gym)
                }
            }
    }

}