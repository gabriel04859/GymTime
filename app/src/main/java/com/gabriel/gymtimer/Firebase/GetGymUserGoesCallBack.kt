package com.gabriel.gymtimer.Firebase

import com.gabriel.gymtimer.model.Gym

interface GetGymUserGoesCallBack {
    fun onGetGymUserGoes(gym : Gym)
}