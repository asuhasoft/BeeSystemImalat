package com.imalat.beeSystem.model.siparisOzeti.teslimatTipi


import com.google.gson.annotations.SerializedName

data class TeslimatTipleriJSON(
    @SerializedName("Aktif")
    val aktif: String,
    @SerializedName("Sira")
    val sira: String,
    @SerializedName("TahminiSure")
    val tahminiSure: String,
    @SerializedName("TeslimatTipID")
    val teslimatTipID: String,
    @SerializedName("TeslimatTipi")
    val teslimatTipi: String
)