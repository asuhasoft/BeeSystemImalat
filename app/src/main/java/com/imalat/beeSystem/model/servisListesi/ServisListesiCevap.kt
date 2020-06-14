package com.imalat.beeSystem.model.servisListesi


import com.google.gson.annotations.SerializedName


data class ServisListesiCevap(


    @SerializedName("message")
    val message: String,

    @SerializedName("ServisListesiJSON")
    val servisListesiJSON: List<ServisListesiJSON>,

    @SerializedName("success")
    val success: Int
)