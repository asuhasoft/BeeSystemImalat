package com.imalat.beeSystem.viewModel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imalat.beeSystem.BuildConfig
import com.imalat.beeSystem.model.anaSayfa.AnaMenuCevap
import com.imalat.beeSystem.model.genelCevap.GenelCevap
import com.imalat.beeSystem.service.ApiServis
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class FragmentAnaSayfaViewModel : ViewModel() {

    private val apiServis = ApiServis()

    private val disposable = CompositeDisposable()


    val anaMenuListesi = MutableLiveData<AnaMenuCevap>()
    val sepetEkleCikart = MutableLiveData<GenelCevap>()

    val ServisError = MutableLiveData<Boolean>()
    val servisYukleniyor = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    fun anaSayfaVerileriGetir(
        url: String,

        Pst_MusteriOturumKod: String
    ) {
        servisYukleniyor.value = true


        disposable.add(
            apiServis.get004AnaMenuIcerikX(
                url,
                BuildConfig.Pst_CAKKey,

                Pst_MusteriOturumKod
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AnaMenuCevap>() {
                    override fun onSuccess(t: AnaMenuCevap) {
                        anaMenuListesi.value = t
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


    fun sepetEkleCikart(
        url: String,
        Pst_MusteriOturumKod: String,
        Pst_Mod: String,
        Pst_Miktar: String,
        Pst_UrunID: String
    ) {
        servisYukleniyor.value = true


        disposable.add(
            apiServis.get027SepetGuncelleX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_MusteriOturumKod,
                Pst_Mod,
                Pst_Miktar,
                Pst_UrunID
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GenelCevap>() {
                    override fun onSuccess(t: GenelCevap) {
                        sepetEkleCikart.value = t
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