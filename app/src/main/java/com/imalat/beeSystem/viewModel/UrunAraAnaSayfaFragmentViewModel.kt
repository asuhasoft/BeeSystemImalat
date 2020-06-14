package com.imalat.beeSystem.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imalat.beeSystem.BuildConfig
import com.imalat.beeSystem.model.aramaGecmisi.AramaGecmisCevap
import com.imalat.beeSystem.model.genelCevap.GenelCevap
import com.imalat.beeSystem.service.ApiServis
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class UrunAraAnaSayfaFragmentViewModel : ViewModel() {

    private val apiServis = ApiServis()
    private val disposable = CompositeDisposable()

    val aramaGecmisiListesi = MutableLiveData<AramaGecmisCevap>()
    val aramaGecmisiSil = MutableLiveData<GenelCevap>()

    val ServisError = MutableLiveData<Boolean>()
    val servisYukleniyor = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    fun aramaGecmisiGetir(
        url: String,
        Pst_MusteriOturumKod: String
    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get031AramaGecmisGetirX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_MusteriOturumKod
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AramaGecmisCevap>() {
                    override fun onSuccess(t: AramaGecmisCevap) {
                        aramaGecmisiListesi.value = t
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


    fun aramaGecmisiSil(
        url: String,
        Pst_MusteriOturumKod: String,
        Pst_AramaKelime: String
    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get032AramaGecmisSilX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_MusteriOturumKod,
                Pst_AramaKelime
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GenelCevap>() {
                    override fun onSuccess(t: GenelCevap) {
                        aramaGecmisiSil.value = t
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