package com.imalat.beeSystem.model.alisVerisListesi


import com.google.gson.annotations.SerializedName

data class AlisverisListesiCevap(
    @SerializedName("AlisverisListesiJSON")
    val alisverisListesiJSON: List<AlisverisListesiJSON>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Int
)