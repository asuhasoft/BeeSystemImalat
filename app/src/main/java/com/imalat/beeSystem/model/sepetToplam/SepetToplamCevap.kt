package com.imalat.beeSystem.model.sepetToplam


import com.google.gson.annotations.SerializedName

data class SepetToplamCevap(
    @SerializedName("message")
    val message: String,
    @SerializedName("SepetToplamJSON")
    val sepetToplamJSON: List<SepetToplamJSON>,
    @SerializedName("success")
    val success: Int
)