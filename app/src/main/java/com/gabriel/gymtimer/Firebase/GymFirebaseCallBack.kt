package com.gabriel.gymtimer.Firebase

import com.gabriel.gymtimer.model.Gym

interface GymFirebaseCallBack {
    fun onGetGym(gym : Gym)
}