package com.gabriel.gymtimer

import com.gabriel.gymtimer.model.Gym
import com.gabriel.gymtimer.model.User

interface FirestoreCallBack {
    fun onGetUser(user : User)

}