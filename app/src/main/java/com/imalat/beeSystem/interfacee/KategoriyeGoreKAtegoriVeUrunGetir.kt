package com.imalat.beeSystem.interfacee

interface KategoriyeGoreKAtegoriVeUrunGetir {

    fun ilkAltKategoriGetir(Pst_KategoriID: String, kategoriAd: String)
    fun ikinciAltKategoriGetir(Pst_KategoriID: String)
    fun ikinciAltKategoriyeGoreUrunGetir(Pst_KategoriID: String)
}