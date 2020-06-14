package com.imalat.beeSystem.viewModel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imalat.beeSystem.BuildConfig
import com.imalat.beeSystem.model.alisVerisListesi.AlisverisListesiCevap
import com.imalat.beeSystem.model.genelCevap.GenelCevap
import com.imalat.beeSystem.service.ApiServis
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class AlisverisListemViewModel : ViewModel() {

    private val apiServis = ApiServis()

    private val disposable = CompositeDisposable()

    val alisVerisListesi = MutableLiveData<AlisverisListesiCevap>()
    val tumunuSepeteEkle = MutableLiveData<GenelCevap>()

    val ServisError = MutableLiveData<Boolean>()
    val servisYukleniyor = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    fun alisVerisListesiGetir(
        url: String,

        Pst_MusteriOturumKod: String
    ) {
        servisYukleniyor.value = true


        disposable.add(
            apiServis.get015MUAlisverisListeListX(url, BuildConfig.Pst_CAKKey, Pst_MusteriOturumKod)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AlisverisListesiCevap>() {
                    override fun onSuccess(t: AlisverisListesiCevap) {
                        alisVerisListesi.value = t
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

    fun tumunuSepeteEkle(
        url: String,
        Pst_MusteriOturumKod: String
    ) {
        servisYukleniyor.value = true


        disposable.add(
            apiServis.get030AlisverisListesiSepeteX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_MusteriOturumKod
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GenelCevap>() {
                    override fun onSuccess(t: GenelCevap) {
                        tumunuSepeteEkle.value = t
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