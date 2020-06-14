package com.imalat.beeSystem.viewModel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imalat.beeSystem.BuildConfig
import com.imalat.beeSystem.model.genelCevap.GenelCevap
import com.imalat.beeSystem.model.urunler.UrunCevap
import com.imalat.beeSystem.service.ApiServis
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class KategoriDetaylariViewModel : ViewModel() {

    private val apiServis = ApiServis()

    private val disposable = CompositeDisposable()

    val urunListesi = MutableLiveData<UrunCevap>()
    val alisVerisListeEdit = MutableLiveData<GenelCevap>()

    val ServisError = MutableLiveData<Boolean>()
    val servisYukleniyor = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    fun urunListesiniGetir(
        url: String, Pst_KategoriID: String,
        Pst_MusteriOturumKod: String,
        Pst_SiralamaTip: String
    ) {
        servisYukleniyor.value = true

        Log.d("Pst_KategoriID", Pst_KategoriID)

        disposable.add(
            apiServis.get003URUrunListX(
                url, BuildConfig.Pst_CAKKey, Pst_KategoriID,
                Pst_MusteriOturumKod,
                Pst_SiralamaTip
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UrunCevap>() {
                    override fun onSuccess(t: UrunCevap) {
                        urunListesi.value = t
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


    fun alisVerisListesineEkleCikar(
        url: String,

        Pst_MusteriOturumKod: String,
        Pst_UrunID: String
    ) {
        servisYukleniyor.value = true


        disposable.add(
            apiServis.get014MUAlisverisListeEditX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_MusteriOturumKod,
                Pst_UrunID
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GenelCevap>() {
                    override fun onSuccess(t: GenelCevap) {
                        alisVerisListeEdit.value = t
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