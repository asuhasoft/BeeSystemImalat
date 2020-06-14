package com.imalat.beeSystem.model.profil.profilBilgileri


import com.google.gson.annotations.SerializedName

data class ProfilCevap(
    @SerializedName("message")
    val message: String,
    @SerializedName("ProfilJSON")
    val profilJSON: List<ProfilJSON>,
    @SerializedName("success")
    val success: Int
)