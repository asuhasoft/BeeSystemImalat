package com.imalat.beeSystem.model.SepetOnline


import com.google.gson.annotations.SerializedName

data class SepetJSON(
    @SerializedName("AnaKampanyaID")
    val anaKampanyaID: String,
    @SerializedName("ArtisMiktari")
    val artisMiktari: String,
    @SerializedName("BirimAd")
    val birimAd: String,
    @SerializedName("BirimID")
    val birimID: String,
    @SerializedName("Fiyat")
    val fiyat: String,
    @SerializedName("Fotograf")
    val fotograf: String,
    @SerializedName("KampanyaAd")
    val kampanyaAd: String,
    @SerializedName("KampanyaliFiyat")
    val kampanyaliFiyat: String,
    @SerializedName("MaksSipMiktar")
    val maksSipMiktar: String,
    @SerializedName("MarkaAd")
    val markaAd: String,
    @SerializedName("MarkaID")
    val markaID: String,
    @SerializedName("SepettekiAdet")
    var sepettekiAdet: String,
    @SerializedName("ToplamFiyat")
    var toplamFiyat: String,
    @SerializedName("UrunAdi")
    val urunAdi: String,
    @SerializedName("UrunID")
    val urunID: String,
    @SerializedName("Indirim")
    val indirim: String
)