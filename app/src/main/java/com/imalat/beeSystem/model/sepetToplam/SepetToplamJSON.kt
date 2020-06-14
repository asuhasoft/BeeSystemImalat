package com.imalat.beeSystem.model.sepetToplam


import com.google.gson.annotations.SerializedName

data class SepetToplamJSON(
    @SerializedName("MusteriID")
    val musteriID: String,
    @SerializedName("SepetToplam")
    val sepetToplam: String,
    @SerializedName("Urunler")
    val urunler: String,
    @SerializedName("Indirim")
    val indirim: String,
    @SerializedName("UrunAdet")
    val urunAdet: String
)