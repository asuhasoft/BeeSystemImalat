package com.imalat.beeSystem.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imalat.beeSystem.BuildConfig
import com.imalat.beeSystem.model.subeler.SubelerCevap
import com.imalat.beeSystem.service.ApiServis
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class SubelerFragmentViewModel : ViewModel() {

    private val apiServis = ApiServis()
    private val disposable = CompositeDisposable()

    val subeListesi = MutableLiveData<SubelerCevap>()


    val ServisError = MutableLiveData<Boolean>()
    val servisYukleniyor = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    fun subeleriGetir(
        url: String
    ) {
        servisYukleniyor.value = true

        disposable.add(
            apiServis.get025CRSubeListX(
                url,
                BuildConfig.Pst_CAKKey
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubelerCevap>() {
                    override fun onSuccess(t: SubelerCevap) {
                        subeListesi.value = t
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