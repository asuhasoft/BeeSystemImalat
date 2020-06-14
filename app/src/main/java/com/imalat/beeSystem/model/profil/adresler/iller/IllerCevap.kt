package com.imalat.beeSystem.model.profil.adresler.iller


import com.google.gson.annotations.SerializedName

data class IllerCevap(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Int,
    @SerializedName("IllerJSON")
    val illerJSON: List<IllerJSON>
)