package com.imalat.beeSystem.model.onayBekleyenUrunler


import com.google.gson.annotations.SerializedName

data class OnayBekleyenUrunlerCevap(
    @SerializedName("message")
    val message: String,
    @SerializedName("OnayBekleyenUrunlerJSON")
    val onayBekleyenUrunlerJSON: List<OnayBekleyenUrunlerJSON>,
    @SerializedName("success")
    val success: Int
) {
    data class OnayBekleyenUrunlerJSON(
        @SerializedName("AlternatifUrunAd")
        val alternatifUrunAd: String,
        @SerializedName("AlternatifUrunFoto")
        val alternatifUrunFoto: String,
        @SerializedName("AlternatifUrunID")
        val alternatifUrunID: String,
        @SerializedName("BirimAd")
        val birimAd: String,
        @SerializedName("DetayDurumAd")
        val detayDurumAd: String,
        @SerializedName("FiyatFarki")
        val fiyatFarki: String,
        @SerializedName("SiparisDetayID")
        val siparisDetayID: String,
        @SerializedName("SiparisID")
        val siparisID: String,
        @SerializedName("SiparisTarih")
        val siparisTarih: String,
        @SerializedName("SonFiyat")
        val sonFiyat: String,
        @SerializedName("SonMiktar")
        val sonMiktar: String,
        @SerializedName("SonToplamFiyat")
        val sonToplamFiyat: String,
        @SerializedName("TalepFiyat")
        val talepFiyat: String,
        @SerializedName("TalepMiktar")
        val talepMiktar: String,
        @SerializedName("TalepToplamFiyat")
        val talepToplamFiyat: String,
        @SerializedName("UrunAd")
        val urunAd: String,
        @SerializedName("UrunFoto")
        val urunFoto: String,
        @SerializedName("UrunID")
        val urunID: String,
        @SerializedName("Indirim")
        val indirim: String,
        @SerializedName("FiyatFarkiMesaj")
        val fiyatFarkiMesaj: String,
        @SerializedName("BirimFiyat")
        val birimFiyat: String,
        @SerializedName("BirimFiyatAlternatif")
        val birimFiyatAlternatif: String
    )
}