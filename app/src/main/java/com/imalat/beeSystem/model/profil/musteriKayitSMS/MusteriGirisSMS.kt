package com.imalat.beeSystem.model.profil.musteriKayitSMS


import com.google.gson.annotations.SerializedName

data class MusteriGirisSMS(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Int,
    @SerializedName("tempass")
    val tempass: String,
    @SerializedName("codex")
    val codex: String
)