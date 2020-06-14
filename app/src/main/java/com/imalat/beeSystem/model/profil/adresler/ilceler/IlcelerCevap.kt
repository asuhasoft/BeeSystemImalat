package com.imalat.beeSystem.model.profil.adresler.ilceler


import com.google.gson.annotations.SerializedName

data class IlcelerCevap(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Int,
    @SerializedName("IlcelerJSON")
    val ilcelerJSON: List<IlcelerJSON>
)