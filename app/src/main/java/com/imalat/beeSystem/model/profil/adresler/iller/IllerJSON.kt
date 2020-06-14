package com.imalat.beeSystem.model.profil.adresler.iller


import com.google.gson.annotations.SerializedName

data class IllerJSON(
    @SerializedName("Aktif")
    val aktif: String,
    @SerializedName("Plaka")
    val plaka: String,
    @SerializedName("Sira")
    val sira: String,
    @SerializedName("IlAdi")
    val ilAdi: String,
    @SerializedName("IlID")
    val ilID: String
)