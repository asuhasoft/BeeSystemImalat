package com.imalat.beeSystem.model.kurumsalBilgi


import com.google.gson.annotations.SerializedName

data class KurumsalBilgiJSON(
    @SerializedName("FirmaUnvan")
    val firmaUnvan: String,
    @SerializedName("Hakkimizda")
    val hakkimizda: String,
    @SerializedName("KVKKMetni")
    val kVKKMetni: String,
    @SerializedName("KullaniciSozlesme")
    val kullaniciSozlesme: String,
    @SerializedName("Logo")
    val logo: String
)