package com.imalat.beeSystem.model.profil.musteriKayitSMS


import com.google.gson.annotations.SerializedName

data class MusteriKayitSMS(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Int,
    @SerializedName("tempass")
    val tempass: String
)