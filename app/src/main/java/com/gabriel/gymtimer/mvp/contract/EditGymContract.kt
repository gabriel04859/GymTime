package com.gabriel.gymtimer.mvp.contract

import com.gabriel.gymtimer.model.Gym

interface EditGymContract {
    interface Presenter{
        fun editGym(gym : Gym)

    }
}