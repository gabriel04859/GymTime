package com.gabriel.gymtimer.Firebase

import com.gabriel.gymtimer.model.User

interface GetCurrentUserCallBack {

    fun onGetCurrentUser(user : User)

}