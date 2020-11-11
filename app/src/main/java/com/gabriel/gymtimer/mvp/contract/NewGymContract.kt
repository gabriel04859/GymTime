package com.gabriel.gymtimer.mvp.contract

import com.gabriel.gymtimer.model.Address

interface NewGymContract {

    interface Presenter{
        fun saveGym(name: String,address : Address, imageGym : String)
    }


}