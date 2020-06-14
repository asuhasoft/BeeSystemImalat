package com.imalat.beeSystem.model.profil.profilBilgileri


import com.google.gson.annotations.SerializedName

data class ProfilJSON(
    @SerializedName("Adi")
    val adi: String,
    @SerializedName("CinsiyetID")
    val cinsiyetID: String,
    @SerializedName("DogumTarihi")
    val dogumTarihi: String,
    @SerializedName("MusteriID")
    val musteriID: String,
    @SerializedName("Soyadi")
    val soyadi: String,
    @SerializedName("Telefon")
    val telefon: String
)