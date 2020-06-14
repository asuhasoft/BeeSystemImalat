package com.imalat.beeSystem.model.yandexAdres


import com.google.gson.annotations.SerializedName

data class GeoObjectCollection(
    @SerializedName("featureMember")
    val featureMember: List<FeatureMember>,
    @SerializedName("metaDataProperty")
    val metaDataProperty: MetaDataPropertyX
)