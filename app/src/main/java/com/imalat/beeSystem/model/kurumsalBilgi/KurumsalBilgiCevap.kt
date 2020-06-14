package com.imalat.beeSystem.model.kurumsalBilgi


import com.google.gson.annotations.SerializedName

data class KurumsalBilgiCevap(
    @SerializedName("KurumsalBilgiJSON")
    val kurumsalBilgiJSON: List<KurumsalBilgiJSON>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Int
)