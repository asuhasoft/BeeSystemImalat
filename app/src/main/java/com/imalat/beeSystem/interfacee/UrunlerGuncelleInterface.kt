package com.imalat.beeSystem.interfacee

interface UrunlerGuncelleInterface {

    fun guncelle()
    fun alisVerisListesineAl(Pst_UrunID: String)
    fun sepeteEkleCikart(Pst_UrunID: String, miktar: String)

}