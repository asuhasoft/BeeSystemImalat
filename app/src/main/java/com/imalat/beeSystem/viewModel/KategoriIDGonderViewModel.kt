package com.imalat.beeSystem.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class KategoriIDGonderViewModel : ViewModel() {

    private val nKategoriID = MutableLiveData<String>()
    fun setKategoriID(kategoriID: String) {
        nKategoriID.value = kategoriID
    }

    fun getKategoriID(): LiveData<String>? {
        return nKategoriID
    }

}