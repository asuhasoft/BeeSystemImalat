package com.imalat.beeSystem.model.subeler


import com.google.gson.annotations.SerializedName

data class SubelerCevap(
    @SerializedName("message")
    val message: String,
    @SerializedName("SubelerJSON")
    val subelerJSON: List<SubelerJSON>,
    @SerializedName("success")
    val success: Int
) {
    data class SubelerJSON(
        @SerializedName("Adres")
        val adres: String,
        @SerializedName("Boylam")
        val boylam: String,
        @SerializedName("EPosta")
        val ePosta: String,
        @SerializedName("Enlem")
        val enlem: String,
        @SerializedName("SubeAd")
        val subeAd: String,
        @SerializedName("SubeID")
        val subeID: String,
        @SerializedName("Telefon")
        val telefon: String
    )
}