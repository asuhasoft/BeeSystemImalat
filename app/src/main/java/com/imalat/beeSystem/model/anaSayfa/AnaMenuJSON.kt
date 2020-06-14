package com.imalat.beeSystem.model.anaSayfa


import com.google.gson.annotations.SerializedName

data class AnaMenuJSON(
    @SerializedName("Slider")
    val slider: List<Slider>,
    @SerializedName("UrunVitrin")
    val urunVitrin: List<UrunVitrin>
)