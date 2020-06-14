package com.imalat.beeSystem.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imalat.beeSystem.BuildConfig
import com.imalat.beeSystem.model.genelCevap.GenelCevap
import com.imalat.beeSystem.model.profil.profilBilgileri.ProfilCevap
import com.imalat.beeSystem.service.ApiServis
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ProfilViewModel : ViewModel() {

    private val apiServis = ApiServis()

    private val disposable = CompositeDisposable()

    val profilBilgileri = MutableLiveData<ProfilCevap>()
    val profilGuncelle = MutableLiveData<GenelCevap>()

    val ServisError = MutableLiveData<Boolean>()
    val servisYukleniyor = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    fun profilBilgileriGetir(
        url: String,
        Pst_MusteriOturumKod: String
    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get009MusteriProfilX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_MusteriOturumKod
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProfilCevap>() {
                    override fun onSuccess(t: ProfilCevap) {
                        profilBilgileri.value = t
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

    fun profilGuncelle(
        url: String,
        Pst_Mod: String,
        Pst_MusteriOturumKod: String,
        Pst_MusteriAd: String,
        Pst_MusteriSoyad: String,
        Pst_MusteriDogumTarih: String,
        Pst_MusteriCinsiyet: String
    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get010ProfilGuncelleX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_Mod,
                Pst_MusteriOturumKod,
                Pst_MusteriAd,
                Pst_MusteriSoyad,
                Pst_MusteriDogumTarih,
                Pst_MusteriCinsiyet
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GenelCevap>() {
                    override fun onSuccess(t: GenelCevap) {
                        profilGuncelle.value = t
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