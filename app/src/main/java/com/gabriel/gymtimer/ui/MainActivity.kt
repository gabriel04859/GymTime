package com.gabriel.gymtimer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.ui.fragments.HomeFragment
import com.gabriel.gymtimer.ui.fragments.ProfileFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var timeAdapter : GroupAdapter<ViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBottom()
        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameMain, HomeFragment()).commit()
                bottomMain.setItemSelected(R.id.menuGym)
            }
        }

    }

    private fun initBottom() {
        bottomMain.setOnItemSelectedListener {
            var selectedFragment = Fragment()
            when(it){
                R.id.menuGym->{
                    selectedFragment = HomeFragment()
                }
                R.id.menuProfile->{
                    selectedFragment = ProfileFragment()
                }
            }
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameMain, selectedFragment).commit()
            }
        }
    }

}
