package com.imalat.beeSystem.model.siparisOzeti.odemeTipi


import com.google.gson.annotations.SerializedName

data class OdemeTipleriJSON(
    @SerializedName("Aktif")
    val aktif: String,
    @SerializedName("OdemeTipID")
    val odemeTipID: String,
    @SerializedName("OdemeTipi")
    val odemeTipi: String,
    @SerializedName("Sira")
    val sira: String
)