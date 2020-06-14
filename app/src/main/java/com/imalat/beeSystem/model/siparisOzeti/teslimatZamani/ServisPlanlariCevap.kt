package com.imalat.beeSystem.model.siparisOzeti.teslimatZamani


import com.google.gson.annotations.SerializedName

data class ServisPlanlariCevap(
    @SerializedName("message")
    val message: String,
    @SerializedName("ServisPlanlariJSON")
    val servisPlanlariJSON: List<ServisPlanlariJSON>,
    @SerializedName("success")
    val success: Int
)