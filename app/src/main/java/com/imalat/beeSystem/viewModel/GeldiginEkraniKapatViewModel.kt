package com.imalat.beeSystem.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class GeldiginEkraniKapatViewModel : ViewModel() {

    private val nAdet = MutableLiveData<String>()


    fun setUrunAdet(adet: String) {
        nAdet.value = adet
    }

    fun getAdet(): LiveData<String>? {
        return nAdet
    }


}