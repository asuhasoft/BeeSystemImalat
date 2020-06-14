package com.imalat.beeSystem.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imalat.beeSystem.BuildConfig
import com.imalat.beeSystem.model.aramaSonucu.AramaSonucuCevap
import com.imalat.beeSystem.service.ApiServis
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class UrunAraFragmentViewModel : ViewModel() {

    private val apiServis = ApiServis()

    private val disposable = CompositeDisposable()

    val urunArananListesi = MutableLiveData<AramaSonucuCevap>()

    val ServisError = MutableLiveData<Boolean>()
    val servisYukleniyor = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun urunleriAra(
        url: String,
        Pst_MusteriOturumKod: String,

        Pst_AramaKelime: String,
        Pst_KategoriID: String,

        Pst_SiralamaTip: String,
        Pst_KategoriIDAra: String
    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get023UrunAramaX(
                url,
                BuildConfig.Pst_CAKKey,

                Pst_MusteriOturumKod,
                Pst_AramaKelime,
                Pst_KategoriID,
                Pst_SiralamaTip,
                Pst_KategoriIDAra
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AramaSonucuCevap>() {
                    override fun onSuccess(t: AramaSonucuCevap) {
                        urunArananListesi.value = t
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