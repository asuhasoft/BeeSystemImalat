package com.imalat.beeSystem.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imalat.beeSystem.BuildConfig
import com.imalat.beeSystem.model.yandexAdres.YandexAdresCevap
import com.imalat.beeSystem.service.ApiServis
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class HaritadaAdresBulmaViewModel : ViewModel() {

    private val apiServis = ApiServis()

    private val disposable = CompositeDisposable()

    val yandexAdres = MutableLiveData<YandexAdresCevap>()

    val ServisError = MutableLiveData<Boolean>()
    val servisYukleniyor = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    fun adresYandexGelsin(
        url: String,
        Pst_MusteriOturumKod: String,
        Pst_MAEnlem: String,
        Pst_MABoylam: String

    ) {
        servisYukleniyor.value = true


        disposable.add(
            apiServis.getConYandexGeoCodeX(
                url, BuildConfig.Pst_CAKKey,
                Pst_MusteriOturumKod,
                Pst_MAEnlem,
                Pst_MABoylam
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<YandexAdresCevap>() {
                    override fun onSuccess(t: YandexAdresCevap) {
                        yandexAdres.value = t
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