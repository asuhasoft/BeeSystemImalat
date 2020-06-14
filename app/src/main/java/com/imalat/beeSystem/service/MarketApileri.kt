package com.imalat.beeSystem.service

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
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

interface MarketApileri {

    @POST("000ServisListesi.php")
    @FormUrlEncoded
    fun getServisListesi(
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_Token") Pst_Token: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String
    ): Single<ServisListesiCevap>

    //kategoriler ------------------------------------------------------------------------------------------------

    @POST()
    @FormUrlEncoded
    fun get002URKategoriList(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_PKey: String,
        @Field("Pst_KategoriID") Pst_KategoriID: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String

    ): Single<UrunKategoriCevap>

    //ürünler ------------------------------------------------------------------------------------------------

    @POST()
    @FormUrlEncoded
    fun get003URUrunList(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_PKey: String,
        @Field("Pst_KategoriID") Pst_KategoriID: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String,
        @Field("Pst_SiralamaTip") Pst_SiralamaTip: String
    ): Single<UrunCevap>


    //ana sayfa ------------------------------------------------------------------------------------------------


    @POST()
    @FormUrlEncoded
    fun get004AnaMenuIcerik(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_PKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String
    ): Single<AnaMenuCevap>


    //profil ------------------------------------------------------------------------------------------------


    @POST()
    @FormUrlEncoded
    fun get005MusteriKayitSMS(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_Mod") Pst_Mod: String,
        @Field("Pst_MusteriKayitTelefon") Pst_MusteriKayitTelefon: String
    ): Single<MusteriKayitSMS>


    @POST()
    @FormUrlEncoded
    fun get006MusteriKayit(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriTelefon") Pst_MusteriTelefon: String,
        @Field("Pst_MusteriPassword") Pst_MusteriPassword: String,
        @Field("Pst_MusteriAd") Pst_MusteriAd: String,
        @Field("Pst_MusteriSoyad") Pst_MusteriSoyad: String,
        @Field("Pst_MusteriCinsiyet") Pst_MusteriCinsiyet: String,
        @Field("Pst_MusteriDogumTarih") Pst_MusteriDogumTarih: String,
        @Field("Pst_Token") Pst_Token: String
    ): Single<GenelCevap>


    @POST()
    @FormUrlEncoded
    fun get007MusteriLoginSMS(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_Mod") Pst_Mod: String,
        @Field("Pst_MusteriTelefon") Pst_MusteriTelefon: String
    ): Single<MusteriGirisSMS>


    @POST()
    @FormUrlEncoded
    fun get008MusteriLogin(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_Mod") Pst_Mod: String,
        @Field("Pst_MusteriTelefon") Pst_MusteriTelefon: String,
        @Field("Pst_MusteriPassword") Pst_MusteriPassword: String,
        @Field("Pst_MusteriExPassword") Pst_MusteriExPassword: String,
        @Field("Pst_Token") Pst_Token: String
    ): Single<LoginCevap>


    @POST()
    @FormUrlEncoded
    fun get009MusteriProfil(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String

    ): Single<ProfilCevap>

    @POST()
    @FormUrlEncoded
    fun get010ProfilGuncelle(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_Mod") Pst_Mod: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String,
        @Field("Pst_MusteriAd") Pst_MusteriAd: String,
        @Field("Pst_MusteriSoyad") Pst_MusteriSoyad: String,
        @Field("Pst_MusteriDogumTarih") Pst_MusteriDogumTarih: String,
        @Field("Pst_MusteriCinsiyet") Pst_MusteriCinsiyet: String
    ): Single<GenelCevap>


    @POST()
    @FormUrlEncoded
    fun get011ADAdresListIller(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_Mod") Pst_Mod: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String,
        @Field("Pst_IlID") Pst_IlID: String,
        @Field("Pst_IlceID") Pst_IlceID: String
    ): Single<IllerCevap>


    @POST()
    @FormUrlEncoded
    fun get011ADAdresListIlceler(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_Mod") Pst_Mod: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String,
        @Field("Pst_IlID") Pst_IlID: String,
        @Field("Pst_IlceID") Pst_IlceID: String
    ): Single<IlcelerCevap>


    @POST()
    @FormUrlEncoded
    fun get011ADAdresListMaheller(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_Mod") Pst_Mod: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String,
        @Field("Pst_IlID") Pst_IlID: String,
        @Field("Pst_IlceID") Pst_IlceID: String
    ): Single<MahallelerCevap>


    @POST()
    @FormUrlEncoded
    fun get012MUAdresEditInsert(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_Mod") Pst_Mod: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String,
        @Field("Pst_MAAdresAd") Pst_MAAdresAd: String,
        @Field("Pst_MAFirmaAd") Pst_MAFirmaAd: String,
        @Field("Pst_MAVergiDaire") Pst_MAVergiDaire: String,
        @Field("Pst_MAVergiNo") Pst_MAVergiNo: String,
        @Field("Pst_MAMahalleID") Pst_MAMahalleID: String,
        @Field("Pst_MAYolAdi") Pst_MAYolAdi: String,
        @Field("Pst_MADisKapiNo") Pst_MADisKapiNo: String,
        @Field("Pst_MASiteBinaAd") Pst_MASiteBinaAd: String,
        @Field("Pst_MABlokAd") Pst_MABlokAd: String,
        @Field("Pst_MAIcKapiNo") Pst_MAIcKapiNo: String,
        @Field("Pst_MAAdresTarif") Pst_MAAdresTarif: String,
        @Field("Pst_MAAlternatifKisi") Pst_MAAlternatifKisi: String,
        @Field("Pst_MAAlternatifTelefon") Pst_MAAlternatifTelefon: String,
        @Field("Pst_MAEnlem") Pst_MAEnlem: String,
        @Field("Pst_MABoylam") Pst_MABoylam: String,
        @Field("Pst_MAVarsayilan") Pst_MAVarsayilan: String,
        @Field("Pst_MusteriAdresID") Pst_MusteriAdresID: String,
        @Field("Pst_MAKatNo") Pst_MAKatNo: String
    ): Single<GenelCevap>


    @POST()
    @FormUrlEncoded
    fun get012MUAdresSil(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_Mod") Pst_Mod: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String,
        @Field("Pst_MusteriAdresID") Pst_MusteriAdresID: String
    ): Single<GenelCevap>


    @POST()
    @FormUrlEncoded
    fun get013MUAdresList(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String
    ): Single<MusteriAdresCevap>


    //ürün alış veriş listesi
    @POST()
    @FormUrlEncoded
    fun get014MUAlisverisListeEdit(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String,
        @Field("Pst_UrunID") Pst_UrunID: String
    ): Single<GenelCevap>


    @POST()
    @FormUrlEncoded
    fun get015MUAlisverisListeList(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String
    ): Single<AlisverisListesiCevap>

    @POST()
    @FormUrlEncoded
    fun get016SPOdemeTipList(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String
    ): Single<OdemeTipleriCevap>


    @POST()
    @FormUrlEncoded
    fun get017SPTeslimatTipList(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String
    ): Single<TeslimatTipleriCevap>

    @POST()
    @FormUrlEncoded
    fun get018SPServisPlanList(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String
    ): Single<ServisPlanlariCevap>

    @POST()
    @FormUrlEncoded
    fun get019UrunDetay(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String,
        @Field("Pst_UrunID") Pst_UrunID: String
    ): Single<UrunDetayCevap>


    @POST()
    @FormUrlEncoded
    fun get020SiparisOlustur(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String,
        @Field("Pst_MusteriAdresID") Pst_MusteriAdresID: String,
        @Field("Pst_OdemeTipID") Pst_OdemeTipID: String,
        @Field("Pst_ServisPlanID") Pst_ServisPlanID: String,
        @Field("Pst_TeslimatTipID") Pst_TeslimatTipID: String,
        @Field("Pst_SiparisNot") Pst_SiparisNot: String,
        @Field("Pst_ZilCalma") Pst_ZilCalma: String
    ): Single<GenelCevap>


    @POST()
    @FormUrlEncoded
    fun get021MusteriSiparisleri(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String
    ): Single<SiparisGecmisiCevap>


    @POST()
    @FormUrlEncoded
    fun get022SiparisDetayListesi(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String,
        @Field("Pst_SiparisID") Pst_SiparisID: String
    ): Single<SiparisDetayCevap>


    //ürün arama
    @POST()
    @FormUrlEncoded
    fun get023UrunArama(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String,
        @Field("Pst_AramaKelime") Pst_AramaKelime: String,
        @Field("Pst_KategoriID") Pst_KategoriID: String,
        @Field("Pst_SiralamaTip") Pst_SiralamaTip: String,
        @Field("Pst_KategoriIDAra") Pst_KategoriIDAra: String
    ): Single<AramaSonucuCevap>


    @POST()
    @FormUrlEncoded
    fun get024KurumsalBilgi(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String
    ): Single<KurumsalBilgiCevap>


    @POST()
    @FormUrlEncoded
    fun get025CRSubeList(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String
    ): Single<SubelerCevap>

    @POST()
    @FormUrlEncoded
    fun getConYandexGeoCode(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String,
        @Field("Pst_MAEnlem") Pst_MAEnlem: String,
        @Field("Pst_MABoylam") Pst_MABoylam: String
    ): Single<YandexAdresCevap>


    @POST()
    @FormUrlEncoded
    fun get026CRBildirimList(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String
    ): Single<BildirimlerCevap>


    //ürün ekle çıkart

    @POST()
    @FormUrlEncoded
    fun get027SepetGuncelle(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String,
        @Field("Pst_Mod") Pst_Mod: String,
        @Field("Pst_Miktar") Pst_Miktar: String,
        @Field("Pst_UrunID") Pst_UrunID: String
    ): Single<GenelCevap>


    //sepet içeriği
    @POST()
    @FormUrlEncoded
    fun get028SepetGetir(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String
    ): Single<SepetCevap>

    //sepet toplam tutari
    @POST()
    @FormUrlEncoded
    fun get029SepetToplam(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String
    ): Single<SepetToplamCevap>

    @POST()
    @FormUrlEncoded
    fun get030AlisverisListesiSepete(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String
    ): Single<GenelCevap>


    @POST()
    @FormUrlEncoded
    fun get031AramaGecmisGetir(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String
    ): Single<AramaGecmisCevap>


    @POST()
    @FormUrlEncoded
    fun get032AramaGecmisSil(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String,
        @Field("Pst_AramaKelime") Pst_AramaKelime: String
    ): Single<GenelCevap>

    @POST()
    @FormUrlEncoded
    fun get033OnayBekleyenUrunler(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String
    ): Single<OnayBekleyenUrunlerCevap>

    @POST()
    @FormUrlEncoded
    fun get034SiparisDetayOnay(
        @Url url: String,
        @Field("Pst_CAKKey") Pst_CAKKey: String,
        @Field("Pst_MusteriOturumKod") Pst_MusteriOturumKod: String,
        @Field("Pst_SiparisDetayID") Pst_SiparisDetayID: String,
        @Field("Pst_SiparisDetayDurumID") Pst_SiparisDetayDurumID: String
    ): Single<GenelCevap>


}