package com.gabriel.gymtimer

import com.gabriel.gymtimer.model.Gym

interface GymFirebaseCallBack {
    fun onGetGym(gym : Gym)
}