package com.imalat.beeSystem.model.bildirim


import com.google.gson.annotations.SerializedName

data class BildirimlerCevap(
    @SerializedName("BildirimlerJSON")
    val bildirimlerJSON: List<BildirimlerJSON>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Int
) {
    data class BildirimlerJSON(
        @SerializedName("Baslik")
        val baslik: String,
        @SerializedName("BildirimAy")
        val bildirimAy: String,
        @SerializedName("BildirimDagitimID")
        val bildirimDagitimID: String,
        @SerializedName("KategoriID")
        val kategoriID: String,
        @SerializedName("Mesaj")
        val mesaj: String,
        @SerializedName("UrunID")
        val urunID: String,
        @SerializedName("Zaman")
        val zaman: String
    )
}