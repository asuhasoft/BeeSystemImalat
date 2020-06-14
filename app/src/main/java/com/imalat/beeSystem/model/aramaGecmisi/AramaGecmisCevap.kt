package com.imalat.beeSystem.model.aramaGecmisi


import com.google.gson.annotations.SerializedName

data class AramaGecmisCevap(
    @SerializedName("AramaGecmisJSON")
    val aramaGecmisJSON: List<AramaGecmisJSON>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Int
)