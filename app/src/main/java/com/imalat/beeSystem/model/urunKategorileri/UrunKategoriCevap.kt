package com.imalat.beeSystem.model.urunKategorileri


import com.google.gson.annotations.SerializedName

data class UrunKategoriCevap(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Int,
    @SerializedName("UrunKategoriJSON")
    val urunKategoriJSON: List<UrunKategoriJSON>
)