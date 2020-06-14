package com.imalat.beeSystem.model.siparisGecmisDetayi


import com.google.gson.annotations.SerializedName

data class SiparisDetayCevap(
    @SerializedName("message")
    val message: String,
    @SerializedName("SiparisDetayJSON")
    val siparisDetayJSON: List<SiparisDetayJSON>,
    @SerializedName("success")
    val success: Int
)