package com.imalat.beeSystem.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imalat.beeSystem.BuildConfig
import com.imalat.beeSystem.model.genelCevap.GenelCevap
import com.imalat.beeSystem.model.profil.adresler.ilceler.IlcelerCevap
import com.imalat.beeSystem.model.profil.adresler.iller.IllerCevap
import com.imalat.beeSystem.model.profil.adresler.mahalle.MahallelerCevap
import com.imalat.beeSystem.service.ApiServis
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class AdresEkleDuzenleViewModel : ViewModel() {

    private val apiServis = ApiServis()

    private val disposable = CompositeDisposable()

    val ilListesi = MutableLiveData<IllerCevap>()
    val ilceListesi = MutableLiveData<IlcelerCevap>()
    val mahalleListesi = MutableLiveData<MahallelerCevap>()
    val adresEkleGuncelle = MutableLiveData<GenelCevap>()
    val adresSil = MutableLiveData<GenelCevap>()

    val ServisError = MutableLiveData<Boolean>()
    val servisYukleniyor = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    fun ilGetir(
        url: String,
        Pst_Mod: String,
        Pst_MusteriOturumKod: String,
        Pst_IlID: String,
        Pst_IlceID: String
    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get011ADAdresListIllerX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_Mod,
                Pst_MusteriOturumKod,
                Pst_IlID,
                Pst_IlceID
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<IllerCevap>() {
                    override fun onSuccess(t: IllerCevap) {
                        ilListesi.value = t
                        Log.d("başarılı", t.message)
                        //  Log.d("başarılı", t.servisListesiJSON.toString())
                        ServisError.value = false
                        servisYukleniyor.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.d("hata", e.localizedMessage)
                        Log.d("hata", e.printStackTrace().toString())
                        ServisError.value = true
                        servisYukleniyor.value = false
                    }
                })
        )
    }


    fun ilceGetir(
        url: String,
        Pst_Mod: String,
        Pst_MusteriOturumKod: String,

        Pst_IlID: String,
        Pst_IlceID: String
    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get011ADAdresListIlcelerX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_Mod,
                Pst_MusteriOturumKod,
                Pst_IlID,
                Pst_IlceID
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<IlcelerCevap>() {
                    override fun onSuccess(t: IlcelerCevap) {
                        ilceListesi.value = t
                        Log.d("başarılı", t.message)
                        //  Log.d("başarılı", t.servisListesiJSON.toString())
                        ServisError.value = false
                        servisYukleniyor.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.d("hata", e.localizedMessage)
                        Log.d("hata", e.printStackTrace().toString())
                        ServisError.value = true
                        servisYukleniyor.value = false
                    }
                })
        )
    }


    fun mahalleGetir(
        url: String,
        Pst_Mod: String,
        Pst_MusteriOturumKod: String,
        Pst_IlID: String,
        Pst_IlceID: String
    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get011ADAdresListMahellerX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_Mod,
                Pst_MusteriOturumKod,
                Pst_IlID,
                Pst_IlceID
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MahallelerCevap>() {
                    override fun onSuccess(t: MahallelerCevap) {
                        mahalleListesi.value = t
                        Log.d("başarılı", t.message)
                        //  Log.d("başarılı", t.servisListesiJSON.toString())
                        ServisError.value = false
                        servisYukleniyor.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.d("hata", e.localizedMessage)
                        Log.d("hata", e.printStackTrace().toString())
                        ServisError.value = true
                        servisYukleniyor.value = false
                    }
                })
        )
    }


    fun adresEkleGuncelle(
        url: String,
        Pst_Mod: String,
        Pst_MusteriOturumKod: String,
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
    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get012MUAdresEditInsertX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_Mod,
                Pst_MusteriOturumKod,
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
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GenelCevap>() {
                    override fun onSuccess(t: GenelCevap) {
                        adresEkleGuncelle.value = t
                        Log.d("başarılı", t.message)
                        //  Log.d("başarılı", t.servisListesiJSON.toString())
                        ServisError.value = false
                        servisYukleniyor.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.d("hata", e.localizedMessage)
                        Log.d("hata", e.printStackTrace().toString())
                        ServisError.value = true
                        servisYukleniyor.value = false
                    }
                })
        )
    }

    fun adresSil(
        url: String,
        Pst_Mod: String,
        Pst_MusteriOturumKod: String,
        Pst_MusteriAdresID: String

    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get012MUAdresSilX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_Mod,
                Pst_MusteriOturumKod,
                Pst_MusteriAdresID

            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GenelCevap>() {
                    override fun onSuccess(t: GenelCevap) {
                        adresSil.value = t
                        Log.d("başarılı", t.message)
                        //  Log.d("başarılı", t.servisListesiJSON.toString())
                        ServisError.value = false
                        servisYukleniyor.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.d("hata", e.localizedMessage)
                        Log.d("hata", e.printStackTrace().toString())
                        ServisError.value = true
                        servisYukleniyor.value = false
                    }
                })
        )
    }


}