package com.imalat.beeSystem.model.siparisGecmisi


import com.google.gson.annotations.SerializedName

data class SiparisGecmisiJSON(
    @SerializedName("DurumAd")
    val durumAd: String,
    @SerializedName("OdemeTipi")
    val odemeTipi: String,
    @SerializedName("ServisAd")
    val servisAd: String,
    @SerializedName("SiparisAy")
    val siparisAy: String,
    @SerializedName("SiparisGun")
    val siparisGun: String,
    @SerializedName("SiparisID")
    val siparisID: String,
    @SerializedName("SiparisTarih")
    val siparisTarih: String,
    @SerializedName("TeslimatTipi")
    val teslimatTipi: String,
    @SerializedName("ToplamUcret")
    val toplamUcret: String,
    @SerializedName("SiparisDurumID")
    val siparisDurumID: String,

    @SerializedName("ZilCalma")
    val zilCalma: String,

    @SerializedName("SiparisNot")
    val siparisNot: String,

    @SerializedName("UrunAdet")
    val urunAdet: String

)