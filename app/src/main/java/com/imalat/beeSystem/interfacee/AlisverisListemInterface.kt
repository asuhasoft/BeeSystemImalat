package com.imalat.beeSystem.interfacee

interface AlisverisListemInterface {


    fun listedenCikart(
        Pst_UrunID: String,
        urunSayisi: Int
    ) // ürün sayısını burda belirtme sebebimiz: eğer listede hiç ürün kalmadıysa tümünü sepete ekle butonunu gizleyeceğiz

    fun sepetGuncelle()
    fun sepeteEkleCikart(Pst_UrunID: String, miktar: String)

}