package com.gabriel.gymtimer.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val idUser : String? = "", val name : String? = "", val email : String? = "", val password : String? = "", val phone : String? = "",
    val imageUser : String? = "", val boss : Boolean? = false, var frequentaGym : Boolean? = false) : Parcelable {


}