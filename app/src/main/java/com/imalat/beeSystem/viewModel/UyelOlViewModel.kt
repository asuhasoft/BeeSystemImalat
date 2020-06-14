package com.imalat.beeSystem.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imalat.beeSystem.BuildConfig
import com.imalat.beeSystem.model.genelCevap.GenelCevap
import com.imalat.beeSystem.model.profil.musteriKayitSMS.MusteriKayitSMS
import com.imalat.beeSystem.service.ApiServis
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class UyelOlViewModel : ViewModel() {

    private val apiServis = ApiServis()

    private val disposable = CompositeDisposable()

    val musteriKayitSMS = MutableLiveData<MusteriKayitSMS>()
    val musteriKayitGercek = MutableLiveData<GenelCevap>()

    val ServisError = MutableLiveData<Boolean>()
    val servisYukleniyor = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    fun musteriyiKaydetSMS(
        url: String,
        Pst_Mod: String,
        Pst_MusteriKayitTelefon: String
    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get005MusteriKayitSMSX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_Mod,
                Pst_MusteriKayitTelefon
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MusteriKayitSMS>() {
                    override fun onSuccess(t: MusteriKayitSMS) {
                        musteriKayitSMS.value = t
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

    fun musteriyiKaydetGercek(
        url: String,
        Pst_MusteriTelefon: String,
        Pst_MusteriPassword: String,
        Pst_MusteriAd: String,
        Pst_MusteriSoyad: String,
        Pst_MusteriCinsiyet: String,
        Pst_MusteriDogumTarih: String,
        Pst_Token: String
    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get006MusteriKayitX(
                url,
                BuildConfig.Pst_CAKKey,

                Pst_MusteriTelefon,
                Pst_MusteriPassword,
                Pst_MusteriAd,
                Pst_MusteriSoyad,
                Pst_MusteriCinsiyet,
                Pst_MusteriDogumTarih,
                Pst_Token
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GenelCevap>() {
                    override fun onSuccess(t: GenelCevap) {
                        musteriKayitGercek.value = t
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