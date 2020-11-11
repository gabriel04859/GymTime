package com.gabriel.gymtimer.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Time(val idTime : String? = "", val idGym : String? = "", val diaInico : String? = "", val diaFinal : String? = "",
                val tempoInicio : String? = "",
                val tempoFinal : String? = "",
                val numPessoas : Int? = 0, val listAlunosTime : MutableList<String> = ArrayList(), var countTotal : Int = 0) : Parcelable {
}