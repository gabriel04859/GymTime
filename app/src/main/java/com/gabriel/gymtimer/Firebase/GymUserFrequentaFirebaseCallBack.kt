package com.gabriel.gymtimer.Firebase

import com.gabriel.gymtimer.model.Gym

interface GymUserFrequentaFirebaseCallBack {
    fun onGetUserGym(gym : Gym)
}