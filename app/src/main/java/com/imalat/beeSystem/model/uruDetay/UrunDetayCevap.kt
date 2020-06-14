package com.imalat.beeSystem.model.uruDetay


import com.google.gson.annotations.SerializedName

data class UrunDetayCevap(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Int,
    @SerializedName("UrunDetayJSON")
    val urunDetayJSON: List<UrunDetayJSON>
)