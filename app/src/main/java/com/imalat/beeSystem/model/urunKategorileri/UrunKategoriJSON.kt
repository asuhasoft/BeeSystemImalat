package com.imalat.beeSystem.model.urunKategorileri


import com.google.gson.annotations.SerializedName

data class UrunKategoriJSON(
    @SerializedName("Aktif")
    val aktif: String,
    @SerializedName("Fotograf")
    val fotograf: String,
    @SerializedName("KategoriAd")
    val kategoriAd: String,
    @SerializedName("KategoriID")
    val kategoriID: String,
    @SerializedName("Sira")
    val sira: String,
    @SerializedName("UstKategoriID")
    val ustKategoriID: String
)