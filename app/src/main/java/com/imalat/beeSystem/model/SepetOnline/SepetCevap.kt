package com.imalat.beeSystem.model.SepetOnline


import com.google.gson.annotations.SerializedName

data class SepetCevap(
    @SerializedName("message")
    val message: String,
    @SerializedName("SepetJSON")
    val sepetJSON: List<SepetJSON>,
    @SerializedName("success")
    val success: Int,

    @SerializedName("uruntoplam")
    val uruntoplam: Double,

    @SerializedName("indirimtoplam")
    val indirimtoplam: Double,

    @SerializedName("sepettoplam")
    val sepettoplam: Double,

    @SerializedName("urunsayi")
    val urunsayi: Int
)