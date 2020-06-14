package com.imalat.beeSystem.service


import com.imalat.beeSystem.BuildConfig
import com.imalat.beeSystem.model.SepetOnline.SepetCevap
import com.imalat.beeSystem.model.alisVerisListesi.AlisverisListesiCevap
import com.imalat.beeSystem.model.anaSayfa.AnaMenuCevap
import com.imalat.beeSystem.model.aramaGecmisi.AramaGecmisCevap
import com.imalat.beeSystem.model.aramaSonucu.AramaSonucuCevap
import com.imalat.beeSystem.model.bildirim.BildirimlerCevap
import com.imalat.beeSystem.model.genelCevap.GenelCevap
import com.imalat.beeSystem.model.kurumsalBilgi.KurumsalBilgiCevap
import com.imalat.beeSystem.model.onayBekleyenUrunler.OnayBekleyenUrunlerCevap
import com.imalat.beeSystem.model.profil.adresler.adreslerim.MusteriAdresCevap
import com.imalat.beeSystem.model.profil.adresler.ilceler.IlcelerCevap
import com.imalat.beeSystem.model.profil.adresler.iller.IllerCevap
import com.imalat.beeSystem.model.profil.adresler.mahalle.MahallelerCevap
import com.imalat.beeSystem.model.profil.giris.LoginCevap
import com.imalat.beeSystem.model.profil.musteriKayitSMS.MusteriGirisSMS
import com.imalat.beeSystem.model.profil.musteriKayitSMS.MusteriKayitSMS
import com.imalat.beeSystem.model.profil.profilBilgileri.ProfilCevap
import com.imalat.beeSystem.model.sepetToplam.SepetToplamCevap
import com.imalat.beeSystem.model.servisListesi.ServisListesiCevap
import com.imalat.beeSystem.model.siparisGecmisDetayi.SiparisDetayCevap
import com.imalat.beeSystem.model.siparisGecmisi.SiparisGecmisiCevap
import com.imalat.beeSystem.model.siparisOzeti.odemeTipi.OdemeTipleriCevap
import com.imalat.beeSystem.model.siparisOzeti.teslimatTipi.TeslimatTipleriCevap
import com.imalat.beeSystem.model.siparisOzeti.teslimatZamani.ServisPlanlariCevap
import com.imalat.beeSystem.model.subeler.SubelerCevap
import com.imalat.beeSystem.model.uruDetay.UrunDetayCevap
import com.imalat.beeSystem.model.urunKategorileri.UrunKategoriCevap
import com.imalat.beeSystem.model.urunler.UrunCevap
import com.imalat.beeSystem.model.yandexAdres.YandexAdresCevap
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiServis {

    private val apiAcilis = Retrofit.Builder()
        .baseUrl(BuildConfig.BaseURLAcilis)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MarketApileri::class.java)

    private val api = Retrofit.Builder()
        .baseUrl(BuildConfig.BaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MarketApileri::class.java)

    private val apiYandex = Retrofit.Builder()
        .baseUrl("https://geocode-maps.yandex.ru/1.x/?apikey=" + BuildConfig.YandexApi + "&format=json&geocode=")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MarketApileri::class.java)


    fun servisListesiGetir(
        Pst_CAKKey: String,
        Pst_Token: String,
        Pst_MusteriOturumKod: String
    ): Single<ServisListesiCevap> {
        return apiAcilis.getServisListesi(Pst_CAKKey, Pst_Token, Pst_MusteriOturumKod)
    }

    fun get002URKategoriListX(
        url: String,
        Pst_CAKKey: String,
        Pst_KategoriID: String,
        Pst_MusteriOturumKod: String
    ): Single<UrunKategoriCevap> {
        return api.get002URKategoriList(url, Pst_CAKKey, Pst_KategoriID, Pst_MusteriOturumKod)
    }

    fun get003URUrunListX(
        url: String,
        Pst_CAKKey: String,
        Pst_KategoriID: String,
        Pst_MusteriOturumKod: String,
        Pst_SiralamaTip: String
    ): Single<UrunCevap> {
        return api.get003URUrunList(
            url,
            Pst_CAKKey,
            Pst_KategoriID,
            Pst_MusteriOturumKod,
            Pst_SiralamaTip
        )
    }

    fun get004AnaMenuIcerikX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String
    ): Single<AnaMenuCevap> {
        return api.get004AnaMenuIcerik(url, Pst_CAKKey, Pst_MusteriOturumKod)
    }

    fun get005MusteriKayitSMSX(
        url: String,
        Pst_CAKKey: String,
        Pst_Mod: String,
        Pst_MusteriKayitTelefon: String
    ): Single<MusteriKayitSMS> {
        return api.get005MusteriKayitSMS(url, Pst_CAKKey, Pst_Mod, Pst_MusteriKayitTelefon)
    }


    fun get006MusteriKayitX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriTelefon: String,
        Pst_MusteriPassword: String,
        Pst_MusteriAd: String,
        Pst_MusteriSoyad: String,
        Pst_MusteriCinsiyet: String,
        Pst_MusteriDogumTarih: String,
        Pst_Token: String
    ): Single<GenelCevap> {
        return api.get006MusteriKayit(
            url,
            Pst_CAKKey,
            Pst_MusteriTelefon,
            Pst_MusteriPassword,
            Pst_MusteriAd,
            Pst_MusteriSoyad,
            Pst_MusteriCinsiyet,
            Pst_MusteriDogumTarih,
            Pst_Token
        )
    }


    fun get007MusteriLoginSMSX(
        url: String,
        Pst_CAKKey: String,
        Pst_Mod: String,
        Pst_MusteriTelefon: String
    ): Single<MusteriGirisSMS> {
        return api.get007MusteriLoginSMS(
            url,
            Pst_CAKKey,
            Pst_Mod,
            Pst_MusteriTelefon
        )
    }

    fun get008MusteriLoginX(
        url: String,
        Pst_CAKKey: String,
        Pst_Mod: String,
        Pst_MusteriTelefon: String,
        Pst_MusteriPassword: String,
        Pst_MusteriExPassword: String,
        Pst_Token: String
    ): Single<LoginCevap> {
        return api.get008MusteriLogin(
            url,
            Pst_CAKKey,
            Pst_Mod,
            Pst_MusteriTelefon,
            Pst_MusteriPassword,
            Pst_MusteriExPassword,
            Pst_Token
        )
    }


    fun get009MusteriProfilX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String
    ): Single<ProfilCevap> {
        return api.get009MusteriProfil(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod
        )
    }


    fun get010ProfilGuncelleX(
        url: String,
        Pst_CAKKey: String,
        Pst_Mod: String,
        Pst_MusteriOturumKod: String,
        Pst_MusteriAd: String,
        Pst_MusteriSoyad: String,
        Pst_MusteriDogumTarih: String,
        Pst_MusteriCinsiyet: String
    ): Single<GenelCevap> {
        return api.get010ProfilGuncelle(
            url,
            Pst_CAKKey,
            Pst_Mod,
            Pst_MusteriOturumKod,
            Pst_MusteriAd,
            Pst_MusteriSoyad,
            Pst_MusteriDogumTarih,
            Pst_MusteriCinsiyet
        )
    }


    fun get011ADAdresListIllerX(
        url: String,
        Pst_CAKKey: String,
        Pst_Mod: String,
        Pst_MusteriOturumKod: String,
        Pst_IlID: String,
        Pst_IlceID: String
    ): Single<IllerCevap> {
        return api.get011ADAdresListIller(
            url,
            Pst_CAKKey,
            Pst_Mod,
            Pst_MusteriOturumKod,
            Pst_IlID,
            Pst_IlceID
        )
    }

    fun get011ADAdresListIlcelerX(
        url: String,
        Pst_CAKKey: String,
        Pst_Mod: String,
        Pst_MusteriOturumKod: String,
        Pst_IlID: String,
        Pst_IlceID: String
    ): Single<IlcelerCevap> {
        return api.get011ADAdresListIlceler(
            url,
            Pst_CAKKey,
            Pst_Mod,
            Pst_MusteriOturumKod,
            Pst_IlID,
            Pst_IlceID
        )
    }

    fun get011ADAdresListMahellerX(
        url: String,
        Pst_CAKKey: String,
        Pst_Mod: String,
        Pst_MusteriOturumKod: String,

        Pst_IlID: String,
        Pst_IlceID: String
    ): Single<MahallelerCevap> {
        return api.get011ADAdresListMaheller(
            url,
            Pst_CAKKey,
            Pst_Mod,
            Pst_MusteriOturumKod,
            Pst_IlID,
            Pst_IlceID
        )
    }


    fun get012MUAdresEditInsertX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String,
        Pst_Mod: String,
        Pst_MAAdresAd: String,
        Pst_MAFirmaAd: String,
        Pst_MAVergiDaire: String,
        Pst_MAVergiNo: String,
        Pst_MAMahalleID: String,
        Pst_MAYolAdi: String,
        Pst_MADisKapiNo: String,
        Pst_MASiteBinaAd: String,
        Pst_MABlokAd: String,
        Pst_MAIcKapiNo: String,
        Pst_MAAdresTarif: String,
        Pst_MAAlternatifKisi: String,
        Pst_MAAlternatifTelefon: String,
        Pst_MAEnlem: String,
        Pst_MABoylam: String,
        Pst_MAVarsayilan: String,
        Pst_MusteriAdresID: String,
        Pst_MAKatNo: String
    ): Single<GenelCevap> {
        return api.get012MUAdresEditInsert(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod,
            Pst_Mod,
            Pst_MAAdresAd,
            Pst_MAFirmaAd,
            Pst_MAVergiDaire,
            Pst_MAVergiNo,
            Pst_MAMahalleID,
            Pst_MAYolAdi,
            Pst_MADisKapiNo,
            Pst_MASiteBinaAd,
            Pst_MABlokAd,
            Pst_MAIcKapiNo,
            Pst_MAAdresTarif,
            Pst_MAAlternatifKisi,
            Pst_MAAlternatifTelefon,
            Pst_MAEnlem,
            Pst_MABoylam,
            Pst_MAVarsayilan,
            Pst_MusteriAdresID,
            Pst_MAKatNo
        )
    }


    fun get012MUAdresSilX(
        url: String,
        Pst_CAKKey: String,
        Pst_Mod: String,

        Pst_MusteriOturumKod: String,
        Pst_MusteriAdresID: String
    ): Single<GenelCevap> {
        return api.get012MUAdresSil(
            url,
            Pst_CAKKey,
            Pst_Mod,
            Pst_MusteriOturumKod,
            Pst_MusteriAdresID
        )
    }

    fun get013MUAdresListX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String
    ): Single<MusteriAdresCevap> {
        return api.get013MUAdresList(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod
        )
    }


    fun get014MUAlisverisListeEditX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String,
        Pst_UrunID: String
    ): Single<GenelCevap> {
        return api.get014MUAlisverisListeEdit(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod,
            Pst_UrunID
        )
    }

    fun get015MUAlisverisListeListX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String
    ): Single<AlisverisListesiCevap> {
        return api.get015MUAlisverisListeList(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod
        )
    }


    fun get016SPOdemeTipListX(
        url: String,
        Pst_CAKKey: String,

        Pst_MusteriOturumKod: String
    ): Single<OdemeTipleriCevap> {
        return api.get016SPOdemeTipList(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod
        )
    }

    fun get017SPTeslimatTipListX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String
    ): Single<TeslimatTipleriCevap> {
        return api.get017SPTeslimatTipList(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod
        )
    }

    fun get018SPServisPlanListX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String
    ): Single<ServisPlanlariCevap> {
        return api.get018SPServisPlanList(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod
        )
    }


    fun get019UrunDetayX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String,
        Pst_UrunID: String
    ): Single<UrunDetayCevap> {
        return api.get019UrunDetay(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod,
            Pst_UrunID
        )
    }


    fun get020SiparisOlusturX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String,
        Pst_MusteriAdresID: String,
        Pst_OdemeTipID: String,
        Pst_ServisPlanID: String,
        Pst_TeslimatTipID: String,
        Pst_SiparisNot: String,
        Pst_ZilCalma: String

    ): Single<GenelCevap> {
        return api.get020SiparisOlustur(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod,
            Pst_MusteriAdresID,
            Pst_OdemeTipID,
            Pst_ServisPlanID,
            Pst_TeslimatTipID,
            Pst_SiparisNot,
            Pst_ZilCalma
        )
    }


    fun get021MusteriSiparisleriX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String

    ): Single<SiparisGecmisiCevap> {
        return api.get021MusteriSiparisleri(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod
        )
    }

    fun get022SiparisDetayListesiX(
        url: String,
        Pst_CAKKey: String,

        Pst_MusteriOturumKod: String,
        Pst_SiparisID: String

    ): Single<SiparisDetayCevap> {
        return api.get022SiparisDetayListesi(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod,
            Pst_SiparisID
        )
    }

    fun get023UrunAramaX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String,

        Pst_AramaKelime: String,
        Pst_KategoriID: String,
        Pst_SiralamaTip: String,
        Pst_KategoriIDAra: String

    ): Single<AramaSonucuCevap> {
        return api.get023UrunArama(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod,
            Pst_AramaKelime,
            Pst_KategoriID,
            Pst_SiralamaTip,
            Pst_KategoriIDAra
        )
    }


    fun get024KurumsalBilgiX(
        url: String,
        Pst_CAKKey: String
    ): Single<KurumsalBilgiCevap> {
        return api.get024KurumsalBilgi(
            url,
            Pst_CAKKey
        )
    }

    fun get025CRSubeListX(
        url: String,
        Pst_CAKKey: String
    ): Single<SubelerCevap> {
        return api.get025CRSubeList(
            url,
            Pst_CAKKey
        )
    }

    fun getConYandexGeoCodeX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String,
        Pst_MAEnlem: String,
        Pst_MABoylam: String
    ): Single<YandexAdresCevap> {
        return api.getConYandexGeoCode(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod,
            Pst_MAEnlem,
            Pst_MABoylam
        )
    }

    fun get026CRBildirimListX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String

    ): Single<BildirimlerCevap> {
        return api.get026CRBildirimList(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod
        )
    }


    fun get027SepetGuncelleX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String,
        Pst_Mod: String,
        Pst_Miktar: String,
        Pst_UrunID: String
    ): Single<GenelCevap> {
        return api.get027SepetGuncelle(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod,
            Pst_Mod,
            Pst_Miktar,
            Pst_UrunID

        )
    }

    fun get028SepetGetirX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String

    ): Single<SepetCevap> {
        return api.get028SepetGetir(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod

        )
    }

    fun get029SepetToplamX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String

    ): Single<SepetToplamCevap> {
        return api.get029SepetToplam(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod

        )
    }

    fun get030AlisverisListesiSepeteX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String

    ): Single<GenelCevap> {
        return api.get030AlisverisListesiSepete(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod
        )
    }

    fun get031AramaGecmisGetirX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String

    ): Single<AramaGecmisCevap> {
        return api.get031AramaGecmisGetir(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod
        )
    }

    fun get032AramaGecmisSilX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String,
        Pst_AramaKelime: String

    ): Single<GenelCevap> {
        return api.get032AramaGecmisSil(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod,
            Pst_AramaKelime
        )
    }

    fun get033OnayBekleyenUrunlerX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String

    ): Single<OnayBekleyenUrunlerCevap> {
        return api.get033OnayBekleyenUrunler(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod
        )
    }


    fun get034SiparisDetayOnayX(
        url: String,
        Pst_CAKKey: String,
        Pst_MusteriOturumKod: String,
        Pst_SiparisDetayID: String,
        Pst_SiparisDetayDurumID: String
    ): Single<GenelCevap> {
        return api.get034SiparisDetayOnay(
            url,
            Pst_CAKKey,
            Pst_MusteriOturumKod,
            Pst_SiparisDetayID,
            Pst_SiparisDetayDurumID
        )
    }


}

