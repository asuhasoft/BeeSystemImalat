package com.imalat.beeSystem.model.profil.adresler.mahalle


import com.google.gson.annotations.SerializedName

data class MahallelerCevap(
    @SerializedName("MahallelerJSON")
    val mahallelerJSON: List<MahallelerJSON>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Int
)