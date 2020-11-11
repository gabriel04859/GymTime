package com.gabriel.gymtimer.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(val UF : String? = "",val CEP : Int? = 0, val rua : String? = "",  val num : Int? = 0,  val complemento : String? = "", val cidade  : String? = "", ) :
    Parcelable {
}