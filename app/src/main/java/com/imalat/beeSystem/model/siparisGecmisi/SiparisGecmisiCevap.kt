package com.imalat.beeSystem.model.siparisGecmisi


import com.google.gson.annotations.SerializedName

data class SiparisGecmisiCevap(
    @SerializedName("message")
    val message: String,
    @SerializedName("SiparisGecmisiJSON")
    val siparisGecmisiJSON: List<SiparisGecmisiJSON>,
    @SerializedName("success")
    val success: Int
)