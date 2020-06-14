package com.imalat.beeSystem.model.sepet

import com.google.gson.annotations.SerializedName

data class SepetModel(
    @SerializedName("UrunID")
    var urunID: String,
    @SerializedName("UrunAdi")
    var urunAdi: String,

    @SerializedName("Fiyat")
    var fiyat: String,
    @SerializedName("Adet")
    var adet: String,
    @SerializedName("MaksSipMiktar")
    var maksSipMiktar: String,
    @SerializedName("ArtisMiktari")
    var artisMiktari: String,
    @SerializedName("Foto")
    var foto: String,
    @SerializedName("BirimAd")
    var birimAd: String,
    @SerializedName("UrunToplamTutari")
    var urunToplamTutari: String

)