package com.imalat.beeSystem.model.aramaSonucu


import com.google.gson.annotations.SerializedName

data class AramaSonucuCevap(
    @SerializedName("AramaSonucuJSON")
    val aramaSonucuJSON: List<AramaSonucuJSON>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Int
)