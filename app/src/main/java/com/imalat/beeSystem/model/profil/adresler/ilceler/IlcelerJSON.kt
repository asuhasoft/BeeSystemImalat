package com.imalat.beeSystem.model.profil.adresler.ilceler


import com.google.gson.annotations.SerializedName

data class IlcelerJSON(
    @SerializedName("Aktif")
    val aktif: String,
    @SerializedName("Sira")
    val sira: String,
    @SerializedName("IlID")
    val ilID: String,
    @SerializedName("IlceAd")
    val ilceAd: String,
    @SerializedName("IlceID")
    val ilceID: String
)