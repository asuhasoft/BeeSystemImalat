package com.imalat.beeSystem.viewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imalat.beeSystem.BuildConfig
import com.imalat.beeSystem.model.sepetToplam.SepetToplamCevap
import com.imalat.beeSystem.service.ApiServis
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class AnaSayfaViewModel : ViewModel() {

    private val apiServis = ApiServis()

    private val disposable = CompositeDisposable()


    val sepetToplami = MutableLiveData<SepetToplamCevap>()

    val ServisError = MutableLiveData<Boolean>()
    val servisYukleniyor = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    fun sepetToplamiGetir(
        url: String,

        Pst_MusteriOturumKod: String
    ) {
        servisYukleniyor.value = true


        disposable.add(
            apiServis.get029SepetToplamX(
                url,
                BuildConfig.Pst_CAKKey,

                Pst_MusteriOturumKod
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SepetToplamCevap>() {
                    override fun onSuccess(t: SepetToplamCevap) {
                        sepetToplami.value = t
                        //Log.d("başarılı", t.message)
                        //  //Log.d("başarılı", t.servisListesiJSON.toString())
                        ServisError.value = false
                        servisYukleniyor.value = false
                    }

                    override fun onError(e: Throwable) {
                        //Log.d("hata", e.localizedMessage)
                        //Log.d("hata", e.printStackTrace().toString())
                        ServisError.value = true
                        servisYukleniyor.value = false
                    }
                })
        )
    }


}