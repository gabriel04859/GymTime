package com.gabriel.gymtimer.mvp.contract

interface BaseContract {
    interface View{
        fun onSuccess()
        fun onFailure()
    }
}