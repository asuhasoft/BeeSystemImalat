package com.imalat.beeSystem.model.siparisOzeti.odemeTipi


import com.google.gson.annotations.SerializedName

data class OdemeTipleriCevap(
    @SerializedName("message")
    val message: String,
    @SerializedName("OdemeTipleriJSON")
    val odemeTipleriJSON: List<OdemeTipleriJSON>,
    @SerializedName("success")
    val success: Int
)