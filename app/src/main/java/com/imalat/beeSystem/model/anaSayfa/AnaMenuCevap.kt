package com.imalat.beeSystem.model.anaSayfa


import com.google.gson.annotations.SerializedName

data class AnaMenuCevap(
    @SerializedName("AnaMenuJSON")
    val anaMenuJSON: List<AnaMenuJSON>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Int
)