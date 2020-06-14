package com.imalat.beeSystem.model.servisListesi


import com.google.gson.annotations.SerializedName

data class ServisListesiJSON(

    @SerializedName("ServisAd")
    val servisAd: String,

    @SerializedName("ServisAdres")
    val servisAdres: String
)