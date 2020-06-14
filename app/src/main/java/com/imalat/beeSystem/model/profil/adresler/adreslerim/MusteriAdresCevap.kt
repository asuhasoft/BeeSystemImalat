package com.imalat.beeSystem.model.profil.adresler.adreslerim


import com.google.gson.annotations.SerializedName

data class MusteriAdresCevap(
    @SerializedName("message")
    val message: String,
    @SerializedName("MusteriAdresJSON")
    val musteriAdresJSON: List<MusteriAdresJSON>,
    @SerializedName("success")
    val success: Int
)