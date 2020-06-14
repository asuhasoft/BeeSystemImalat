package com.imalat.beeSystem.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class BarkodGetirViewModel : ViewModel() {

    private val mName = MutableLiveData<String>()
    fun setName(name: String) {
        mName.value = name
    }

    fun getName(): LiveData<String>? {
        return mName
    }

}