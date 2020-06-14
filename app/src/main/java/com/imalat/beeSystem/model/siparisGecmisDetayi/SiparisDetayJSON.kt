package com.imalat.beeSystem.model.siparisGecmisDetayi


import com.google.gson.annotations.SerializedName

data class SiparisDetayJSON(
    @SerializedName("Aktif")
    val aktif: String,
    @SerializedName("BirimAd")
    val birimAd: String,
    @SerializedName("BirimID")
    val birimID: String,
    @SerializedName("Fotograf")
    val fotograf: String,
    @SerializedName("MarkaAd")
    val markaAd: String,
    @SerializedName("MarkaID")
    val markaID: String,
    @SerializedName("SiparisDetayID")
    val siparisDetayID: String,
    @SerializedName("SiparisID")
    val siparisID: String,
    @SerializedName("SonMiktar")
    val sonMiktar: String,
    @SerializedName("SonToplamFiyat")
    val sonToplamFiyat: String,
    @SerializedName("TalepFiyat")
    val talepFiyat: String,
    @SerializedName("TalepMiktar")
    val talepMiktar: String,
    @SerializedName("TalepToplamFiyat")
    val talepToplamFiyat: String,
    @SerializedName("UrunAdi")
    val urunAdi: String,
    @SerializedName("UrunID")
    val urunID: String,

    @SerializedName("SiparisDetayDurumID")
    val siparisDetayDurumID: String,

    @SerializedName("DetayDurumAd")
    val detayDurumAd: String


)