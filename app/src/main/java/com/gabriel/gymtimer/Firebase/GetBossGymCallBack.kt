package com.gabriel.gymtimer.Firebase

import com.gabriel.gymtimer.model.Gym

interface GetBossGymCallBack {
    fun onGetBossGym(gym : Gym)
}