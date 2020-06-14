package com.imalat.beeSystem.view.sepet.siparisOzeti

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imalat.beeSystem.R
import com.imalat.beeSystem.adapter.TeslimatTipiAdapter
import com.imalat.beeSystem.interfacee.TeslimatTipiSecInterface
import com.imalat.beeSystem.model.siparisOzeti.teslimatTipi.TeslimatTipleriJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.SiparisOzetiViewModel
import kotlinx.android.synthetic.main.fragment_teslimat_zamani.*


class TeslimatTipiSec : AppCompatActivity(), TeslimatTipiSecInterface {

    private lateinit var viewModel: SiparisOzetiViewModel
    private var teslimatTipiAdapter = TeslimatTipiAdapter(
        arrayListOf(), this as TeslimatTipiSecInterface
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teslimat_zamani_sec)
        overridePendingTransition(R.anim.saga_gidis1, R.anim.saga_gidis2)
        tvKategoriDetaylariBaslik.setText("Teslimat Tipi Seçin")

        viewModel = ViewModelProvider(this)
            .get(SiparisOzetiViewModel::class.java)

        recycleViewX.layoutManager = LinearLayoutManager(this)
        recycleViewX.adapter = teslimatTipiAdapter


        if (Fonk.isNetworkAvailable(this))
            teslimatTipiGetir()


        geriX.setOnClickListener {
            finish()
        }

    }


    fun teslimatTipiGetir() {

        viewModel.teslimatTipleriGetir(
            GlobalDegiskenlerX.g017SPTeslimatTipList,
            Fonk.degerGetir(this, EnumX.OturumKodu.toString())
        )

        viewModel.teslimaTipiListesi.removeObservers(this)
        viewModel.ServisError.removeObservers(this)
        viewModel.servisYukleniyor.removeObservers(this)

        viewModel.teslimaTipiListesi.observe(this, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    val list: List<TeslimatTipleriJSON> = cevap.teslimatTipleriJSON

                    if (list != null && list.size > 0) {

                        for (i in 0 until list.size) {
                            Log.d("servisAd", list[i].teslimatTipi)
                        }
                    }

                    teslimatTipiAdapter.update(list)

                } else {
                    teslimatTipiAdapter.update(arrayListOf())
                    Toast.makeText(this, cevap.message, Toast.LENGTH_SHORT).show()
                }

                viewModel.teslimaTipiListesi.postValue(null)

            }

        })
    }


    override fun kapat() {

        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.sola_gidis1, R.anim.sola_gidis2)
    }

}