package com.gabriel.gymtimer.Firebase

import com.gabriel.gymtimer.model.User

interface FirebaseQueryCallBacks {
    fun onCheckIfGymAlreadyExists(user : User)
}