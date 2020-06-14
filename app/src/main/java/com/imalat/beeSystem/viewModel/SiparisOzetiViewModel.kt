package com.imalat.beeSystem.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imalat.beeSystem.BuildConfig
import com.imalat.beeSystem.model.genelCevap.GenelCevap
import com.imalat.beeSystem.model.siparisOzeti.odemeTipi.OdemeTipleriCevap
import com.imalat.beeSystem.model.siparisOzeti.teslimatTipi.TeslimatTipleriCevap
import com.imalat.beeSystem.model.siparisOzeti.teslimatZamani.ServisPlanlariCevap
import com.imalat.beeSystem.service.ApiServis
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class SiparisOzetiViewModel : ViewModel() {

    private val apiServis = ApiServis()

    private val disposable = CompositeDisposable()

    val servisPlanListesi = MutableLiveData<ServisPlanlariCevap>()
    val teslimaTipiListesi = MutableLiveData<TeslimatTipleriCevap>()
    val odemeTipiListesi = MutableLiveData<OdemeTipleriCevap>()
    val siparisGonder = MutableLiveData<GenelCevap>()

    val ServisError = MutableLiveData<Boolean>()
    val servisYukleniyor = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    fun servisPlanListesiGetir(
        url: String,
        Pst_MusteriOturumKod: String

    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get018SPServisPlanListX(
                url,
                BuildConfig.Pst_CAKKey,

                Pst_MusteriOturumKod

            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ServisPlanlariCevap>() {
                    override fun onSuccess(t: ServisPlanlariCevap) {
                        servisPlanListesi.value = t
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

    fun teslimatTipleriGetir(
        url: String,

        Pst_MusteriOturumKod: String

    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get017SPTeslimatTipListX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_MusteriOturumKod

            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TeslimatTipleriCevap>() {
                    override fun onSuccess(t: TeslimatTipleriCevap) {
                        teslimaTipiListesi.value = t
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


    fun odemeTipleriGetir(
        url: String,
        Pst_MusteriOturumKod: String

    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get016SPOdemeTipListX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_MusteriOturumKod

            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OdemeTipleriCevap>() {
                    override fun onSuccess(t: OdemeTipleriCevap) {
                        odemeTipiListesi.value = t
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


    fun siparisGonder(
        url: String,
        Pst_MusteriOturumKod: String,
        Pst_MusteriAdresID: String,
        Pst_OdemeTipID: String,
        Pst_ServisPlanID: String,
        Pst_TeslimatTipID: String,
        Pst_SiparisNot: String,
        Pst_ZilCalma: String

    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get020SiparisOlusturX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_MusteriOturumKod,
                Pst_MusteriAdresID,
                Pst_OdemeTipID,
                Pst_ServisPlanID,
                Pst_TeslimatTipID,
                Pst_SiparisNot,
                Pst_ZilCalma

            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GenelCevap>() {
                    override fun onSuccess(t: GenelCevap) {
                        siparisGonder.value = t
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