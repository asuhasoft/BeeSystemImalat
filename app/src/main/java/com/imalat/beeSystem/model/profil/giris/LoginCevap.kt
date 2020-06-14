package com.imalat.beeSystem.model.profil.giris


import com.google.gson.annotations.SerializedName

data class LoginCevap(
    @SerializedName("LoginJSON")
    val loginJSON: List<LoginJSON>,
    @SerializedName("message")
    val message: String,
    @SerializedName("OturumKodu")
    val oturumKodu: String,
    @SerializedName("success")
    val success: Int
) {
    data class LoginJSON(
        @SerializedName("Adi")
        val adi: String,
        @SerializedName("CinsiyetID")
        val cinsiyetID: String,
        @SerializedName("KayitTarihi")
        val kayitTarihi: String,
        @SerializedName("MusteriID")
        val musteriID: String,
        @SerializedName("OturumKodu")
        val oturumKodu: String,
        @SerializedName("Soyadi")
        val soyadi: String,
        @SerializedName("Telefon")
        val telefon: String
    )
}