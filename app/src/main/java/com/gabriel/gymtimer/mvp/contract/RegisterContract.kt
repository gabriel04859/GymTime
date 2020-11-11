package com.gabriel.gymtimer.mvp.contract

import android.net.Uri
import com.gabriel.gymtimer.model.User
import com.google.firebase.storage.StorageReference

interface RegisterContract {


    interface View{
        fun onFailure(message : String)
        fun onSuccess()
    }

    interface Presenter{
        fun createUser(user : User)
    }
}