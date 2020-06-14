package com.imalat.beeSystem.model.profil.adresler.mahalle


import com.google.gson.annotations.SerializedName

data class MahallelerJSON(
    @SerializedName("Aktif")
    val aktif: String,
    @SerializedName("MahalleAd")
    val mahalleAd: String,
    @SerializedName("MahalleID")
    val mahalleID: String,
    @SerializedName("Sira")
    val sira: String,
    @SerializedName("IlceID")
    val ilceID: String,
    @SerializedName("MinSipTutar")
    val minSipTutar: String,
    @SerializedName("ServisUcret")
    val servisUcret: String,
    @SerializedName("UcretsizServis")
    val UcretsizServis: String

)