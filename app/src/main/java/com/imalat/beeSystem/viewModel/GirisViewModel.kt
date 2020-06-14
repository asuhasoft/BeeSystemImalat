package com.imalat.beeSystem.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imalat.beeSystem.BuildConfig
import com.imalat.beeSystem.model.profil.giris.LoginCevap
import com.imalat.beeSystem.model.profil.musteriKayitSMS.MusteriGirisSMS
import com.imalat.beeSystem.service.ApiServis
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class GirisViewModel : ViewModel() {

    private val apiServis = ApiServis()

    private val disposable = CompositeDisposable()

    val musteriGirisSMS = MutableLiveData<MusteriGirisSMS>()
    val musteriGirisGercek = MutableLiveData<LoginCevap>()

    val ServisError = MutableLiveData<Boolean>()
    val servisYukleniyor = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    fun musteriGirisSMSX(
        url: String,
        Pst_Mod: String,
        Pst_MusteriTelefon: String
    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get007MusteriLoginSMSX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_Mod,
                Pst_MusteriTelefon
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MusteriGirisSMS>() {
                    override fun onSuccess(t: MusteriGirisSMS) {
                        musteriGirisSMS.value = t
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


    fun musteriGirisGercekX(
        url: String,
        Pst_Mod: String,
        Pst_MusteriTelefon: String,
        Pst_MusteriPassword: String,
        Pst_MusteriExPassword: String,
        Pst_Token: String
    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get008MusteriLoginX(
                url,
                BuildConfig.Pst_CAKKey,
                Pst_Mod,
                Pst_MusteriTelefon,
                Pst_MusteriPassword,
                Pst_MusteriExPassword,
                Pst_Token
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LoginCevap>() {
                    override fun onSuccess(t: LoginCevap) {
                        musteriGirisGercek.value = t
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