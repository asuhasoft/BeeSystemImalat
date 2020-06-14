package com.imalat.beeSystem.model.aramaSonucu


import com.google.gson.annotations.SerializedName

data class AramaSonucuJSON(
    @SerializedName("Kategoriler")
    val kategoriler: List<Kategoriler>,
    @SerializedName("Urunler")
    val urunler: List<Urunler>
)