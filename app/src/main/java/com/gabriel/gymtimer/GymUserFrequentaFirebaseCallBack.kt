package com.gabriel.gymtimer

import com.gabriel.gymtimer.model.Gym

interface GymUserFrequentaFirebaseCallBack {
    fun onGetUserGym(gym : Gym)
}