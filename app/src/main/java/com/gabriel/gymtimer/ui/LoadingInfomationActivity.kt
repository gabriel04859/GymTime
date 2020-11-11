package com.gabriel.gymtimer.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gabriel.gymtimer.Constants
import com.gabriel.gymtimer.Firebase.FirebaseSingleton
import com.gabriel.gymtimer.FirestoreCallBack
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.model.User
import com.gabriel.gymtimer.mvp.contract.LoadingInformantionContract
import com.gabriel.gymtimer.mvp.presenter.LoadingInformationPresenter

class LoadingInfomationActivity : AppCompatActivity(), LoadingInformantionContract.View {
    private val loadingInformationPresenter by lazy {
        LoadingInformationPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_infomation)

        FirebaseSingleton.getUser(object : FirestoreCallBack {
            override fun onGetUser(user: User){
                loadingInformationPresenter.checkUser(user)
            }
        })

    }

    override fun onUserNaoFrequentaGym() {
        val intent = Intent(this, SearchGymActivity::class.java)
        intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onUserFrequentaGym() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onGymNotExists() {
        Log.i("TESTE","N possui gym")
        val intent = Intent(this, NewGymActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onGymExists() { Log.i("TESTE", "possui gym")
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
