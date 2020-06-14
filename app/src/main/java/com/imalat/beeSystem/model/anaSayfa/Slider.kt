package com.imalat.beeSystem.model.anaSayfa


import com.google.gson.annotations.SerializedName

data class Slider(
    @SerializedName("Aciklama")
    val aciklama: String,
    @SerializedName("DuyuruID")
    val duyuruID: String,
    @SerializedName("Fotograf")
    val fotograf: String,
    @SerializedName("Sira")
    val sira: String,
    @SerializedName("UrunID")
    val urunID: String,
    @SerializedName("KategoriID")
    val kategoriID: String
)