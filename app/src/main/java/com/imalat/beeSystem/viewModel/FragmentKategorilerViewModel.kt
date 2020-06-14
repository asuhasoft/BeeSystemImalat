package com.imalat.beeSystem.viewModel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imalat.beeSystem.BuildConfig
import com.imalat.beeSystem.model.urunKategorileri.UrunKategoriCevap
import com.imalat.beeSystem.service.ApiServis
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class FragmentKategorilerViewModel : ViewModel() {

    private val apiServis = ApiServis()

    private val disposable = CompositeDisposable()

    val urunKategoriListesi = MutableLiveData<UrunKategoriCevap>()

    val ServisError = MutableLiveData<Boolean>()
    val servisYukleniyor = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    fun kategoriListesiGetir(url: String, Pst_KategoriID: String, Pst_MusteriOturumKod: String) {
        servisYukleniyor.value = true

        Log.d("Pst_KategoriID", Pst_KategoriID)

        disposable.add(
            apiServis.get002URKategoriListX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_KategoriID,
                Pst_MusteriOturumKod
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UrunKategoriCevap>() {
                    override fun onSuccess(t: UrunKategoriCevap) {
                        urunKategoriListesi.value = t
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