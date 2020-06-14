package com.imalat.beeSystem.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imalat.beeSystem.BuildConfig
import com.imalat.beeSystem.model.genelCevap.GenelCevap
import com.imalat.beeSystem.model.onayBekleyenUrunler.OnayBekleyenUrunlerCevap
import com.imalat.beeSystem.service.ApiServis
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class OnayBekleyenUrunlerFragmentViewModel : ViewModel() {

    private val apiServis = ApiServis()
    private val disposable = CompositeDisposable()

    val onayBekleyenUrunlerListesi = MutableLiveData<OnayBekleyenUrunlerCevap>()
    val siparisDetayiOnayla = MutableLiveData<GenelCevap>()


    val ServisError = MutableLiveData<Boolean>()
    val servisYukleniyor = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    fun onayBekleyeUrunleriGetir(
        url: String,
        Pst_MusteriOturumKod: String
    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get033OnayBekleyenUrunlerX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_MusteriOturumKod
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OnayBekleyenUrunlerCevap>() {
                    override fun onSuccess(t: OnayBekleyenUrunlerCevap) {
                        onayBekleyenUrunlerListesi.value = t
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


    fun siparisDetayiOnayla(
        url: String,
        Pst_MusteriOturumKod: String,
        Pst_SiparisDetayID: String,
        Pst_SiparisDetayDurumID: String
    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get034SiparisDetayOnayX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_MusteriOturumKod,
                Pst_SiparisDetayID,
                Pst_SiparisDetayDurumID
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GenelCevap>() {
                    override fun onSuccess(t: GenelCevap) {
                        siparisDetayiOnayla.value = t
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