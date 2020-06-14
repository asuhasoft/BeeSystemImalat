package com.imalat.beeSystem.model.siparisOzeti.teslimatTipi


import com.google.gson.annotations.SerializedName

data class TeslimatTipleriCevap(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Int,
    @SerializedName("TeslimatTipleriJSON")
    val teslimatTipleriJSON: List<TeslimatTipleriJSON>
)