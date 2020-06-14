package com.imalat.beeSystem.model.siparisOzeti.teslimatZamani


import com.google.gson.annotations.SerializedName

data class ServisPlanlariJSON(
    @SerializedName("Aktif")
    val aktif: String,
    @SerializedName("Saat")
    val saat: String,
    @SerializedName("ServisAd")
    val servisAd: String,
    @SerializedName("ServisPlanID")
    val servisPlanID: String
)