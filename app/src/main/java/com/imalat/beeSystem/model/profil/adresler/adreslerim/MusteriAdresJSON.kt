package com.imalat.beeSystem.model.profil.adresler.adreslerim


import com.google.gson.annotations.SerializedName

data class MusteriAdresJSON(
    @SerializedName("AdresAd")
    val adresAd: String,
    @SerializedName("AdresTarif")
    val adresTarif: String,
    @SerializedName("AlternatifKisi")
    val alternatifKisi: String,
    @SerializedName("AlternatifTelefon")
    val alternatifTelefon: String,
    @SerializedName("BlokAd")
    val blokAd: String,
    @SerializedName("Boylam")
    val boylam: String,
    @SerializedName("DisKapiNo")
    val disKapiNo: String,
    @SerializedName("Enlem")
    val enlem: String,
    @SerializedName("FirmaAd")
    val firmaAd: String,
    @SerializedName("KatNo")
    val katNo: String,
    @SerializedName("MahalleAd")
    val mahalleAd: String,
    @SerializedName("MahalleID")
    val mahalleID: String,
    @SerializedName("MusteriAdresID")
    val musteriAdresID: String,
    @SerializedName("SiteBinaAd")
    val siteBinaAd: String,
    @SerializedName("Varsayilan")
    val varsayilan: String,
    @SerializedName("VergiDaire")
    val vergiDaire: String,
    @SerializedName("VergiNo")
    val vergiNo: String,
    @SerializedName("YolAdi")
    val yolAdi: String,
    @SerializedName("IcKapiNo")
    val icKapiNo: String,
    @SerializedName("IlAdi")
    val ilAdi: String,
    @SerializedName("IlID")
    val ilID: String,
    @SerializedName("IlceAd")
    val ilceAd: String,
    @SerializedName("IlceID")
    val ilceID: String,
    @SerializedName("MinSipTutar")
    val minSipTutar: String,
    @SerializedName("ServisUcret")
    val servisUcret: String,
    @SerializedName("UcretsizServis")
    val UcretsizServis: String
)