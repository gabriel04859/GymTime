package com.gabriel.gymtimer.mvp.contract

import com.gabriel.gymtimer.model.User

interface LoadingInformantionContract {


    interface View{
        //Mudar para inglês
        fun onUserNaoFrequentaGym()
        fun onUserFrequentaGym()
        fun onGymNotExists()
        fun onGymExists()
    }

    interface Presenter{
        fun checkUser(user : User)
    }
}

