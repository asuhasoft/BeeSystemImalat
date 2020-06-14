package com.imalat.beeSystem.model.aramaSonucu


import com.google.gson.annotations.SerializedName

data class Kategoriler(
    @SerializedName("Fotograf")
    val fotograf: String,
    @SerializedName("KategoriAd")
    val kategoriAd: String,
    @SerializedName("KategoriID")
    val kategoriID: String,
    @SerializedName("UrunAdet")
    val urunAdet: String
)