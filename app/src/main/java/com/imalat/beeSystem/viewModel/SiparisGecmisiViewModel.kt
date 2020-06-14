package com.imalat.beeSystem.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imalat.beeSystem.BuildConfig
import com.imalat.beeSystem.model.siparisGecmisDetayi.SiparisDetayCevap
import com.imalat.beeSystem.model.siparisGecmisi.SiparisGecmisiCevap
import com.imalat.beeSystem.service.ApiServis
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class SiparisGecmisiViewModel : ViewModel() {

    private val apiServis = ApiServis()

    private val disposable = CompositeDisposable()

    val siparisGecmisListesi = MutableLiveData<SiparisGecmisiCevap>()
    val siparisGecmisDetayListesi = MutableLiveData<SiparisDetayCevap>()


    val ServisError = MutableLiveData<Boolean>()
    val servisYukleniyor = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    fun gecmisSiparisListesiGetir(
        url: String,
        Pst_MusteriOturumKod: String

    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get021MusteriSiparisleriX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_MusteriOturumKod

            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SiparisGecmisiCevap>() {
                    override fun onSuccess(t: SiparisGecmisiCevap) {
                        siparisGecmisListesi.value = t
                        Log.d("başarılı", t.message)
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


    fun gecmisSiparisDetayListesiGetir(
        url: String,
        Pst_MusteriOturumKod: String,
        Pst_SiparisID: String

    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get022SiparisDetayListesiX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_MusteriOturumKod,
                Pst_SiparisID

            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SiparisDetayCevap>() {
                    override fun onSuccess(t: SiparisDetayCevap) {
                        siparisGecmisDetayListesi.value = t
                        Log.d("başarılı", t.message)
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