package com.imalat.beeSystem.model.urunler


import com.google.gson.annotations.SerializedName

data class UrunJSON(
    @SerializedName("Aciklama")
    val aciklama: String,
    @SerializedName("ArtisMiktari")
    val artisMiktari: String,
    @SerializedName("Barkod")
    val barkod: String,
    @SerializedName("BirimAd")
    val birimAd: String,
    @SerializedName("BirimID")
    val birimID: String,
    @SerializedName("Fiyat")
    val fiyat: String,
    @SerializedName("Fotograf")
    val fotograf: String,
    @SerializedName("KampanyaliFiyat")
    val kampanyaliFiyat: String,
    @SerializedName("MaksSipMiktar")
    val maksSipMiktar: String,
    @SerializedName("MarkaAd")
    val markaAd: String,
    @SerializedName("MarkaID")
    val markaID: String,
    @SerializedName("UrunAdi")
    val urunAdi: String,
    @SerializedName("UrunID")
    val urunID: String,
    @SerializedName("SepettekiAdet")
    var sepettekiAdet: String,
    @SerializedName("Favori")
    var favori: String,
    @SerializedName("BirimFiyat")
    var birimFiyat: String
)