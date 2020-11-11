package com.gabriel.gymtimer.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Gym(val idGym : String? = "", val name : String? = "", val name_keywords : List<String>? = ArrayList(),
               val address: Address? = null, val photo : String? = "", val idUser : String? = "",val listAlunosGym : MutableList<String> = ArrayList()) :
    Parcelable