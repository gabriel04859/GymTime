package com.gabriel.gymtimer.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.gabriel.gymtimer.Firebase.*
import com.gabriel.gymtimer.R
import com.gabriel.gymtimer.model.Gym
import com.gabriel.gymtimer.model.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.gym_bottom_sheet.*

class HomeBottomSheetDialog(gym : Gym) : BottomSheetDialogFragment() {
    private var mGym = gym
    private lateinit var circleImageViewGym : CircleImageView
    private lateinit var textViewNameGymBottomSheet : TextView
    private lateinit var textViewStreetSheet : TextView
    private lateinit var textViewCitySheet : TextView
    private lateinit var textViewNumSheet : TextView
    private lateinit var textViewCEPSheet : TextView
    private lateinit var textViewUFSheet : TextView
    private lateinit var textViewPhoneGymBottomSheet : TextView

    private lateinit var mContext: Context



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.gym_bottom_sheet, container,false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       circleImageViewGym = view.findViewById(R.id.circleImageViewGymSheet)
        textViewNameGymBottomSheet = view.findViewById(R.id.textViewNameGymBottomSheet)
        textViewStreetSheet = view.findViewById(R.id.textViewStreetSheet)
        textViewCitySheet = view.findViewById(R.id.textViewCitySheet)
        textViewNumSheet = view.findViewById(R.id.textViewNumSheet)
        textViewCEPSheet = view.findViewById(R.id.textViewCEPSheet)
        textViewUFSheet = view.findViewById(R.id.textViewUFSheet)
        textViewPhoneGymBottomSheet = view.findViewById(R.id.textViewPhoneGymBottomSheet)



        showWidgetsSheet(mGym)


    }

    private fun showWidgetsSheet(gym : Gym){
        gym.let {

            textViewStreetSheet.text = it.address?.rua
            textViewCitySheet.text = it.address?.cidade
            textViewNumSheet.text = it.address?.num.toString()
            textViewCEPSheet.text = it.address?.CEP.toString()
            textViewUFSheet.text = it.address?.UF
        }

        FirebaseUtils.getCurrentUser(object : GetCurrentUserCallBack{
            override fun onGetCurrentUser(user: User) {Picasso.with(mContext).load(user.imageUser).into(circleImageViewGym)
                textViewNameGymBottomSheet.text = user.name
                textViewPhoneGymBottomSheet.text = user.phone
            }

        })



    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }


}