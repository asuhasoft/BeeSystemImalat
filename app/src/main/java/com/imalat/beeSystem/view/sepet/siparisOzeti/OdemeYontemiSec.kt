package com.imalat.beeSystem.view.sepet.siparisOzeti

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imalat.beeSystem.R
import com.imalat.beeSystem.adapter.OdemeYontemiSecAdapter
import com.imalat.beeSystem.interfacee.OdemeYontemiSecSecInterface
import com.imalat.beeSystem.model.siparisOzeti.odemeTipi.OdemeTipleriJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.SiparisOzetiViewModel
import kotlinx.android.synthetic.main.fragment_teslimat_zamani.*


class OdemeYontemiSec : AppCompatActivity(), OdemeYontemiSecSecInterface {

    private lateinit var viewModel: SiparisOzetiViewModel
    private var odemeYontemiSecAdapter = OdemeYontemiSecAdapter(
        arrayListOf(),
        this@OdemeYontemiSec as OdemeYontemiSecSecInterface
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teslimat_zamani_sec)
        overridePendingTransition(R.anim.saga_gidis1, R.anim.saga_gidis2)
        tvKategoriDetaylariBaslik.setText("Ödeme Yöntemi Seçin")

        viewModel = ViewModelProvider(this@OdemeYontemiSec)
            .get(SiparisOzetiViewModel::class.java)

        recycleViewX.layoutManager = LinearLayoutManager(this)
        recycleViewX.adapter = odemeYontemiSecAdapter


        if (Fonk.isNetworkAvailable(this))
            odemeYonetimiGetir()


        geriX.setOnClickListener {
            finish()
        }

    }


    fun odemeYonetimiGetir() {

        viewModel.odemeTipleriGetir(
            GlobalDegiskenlerX.g016SPOdemeTipList,
            Fonk.degerGetir(this, EnumX.OturumKodu.toString())
        )

        viewModel.odemeTipiListesi.removeObservers(this)
        viewModel.ServisError.removeObservers(this)
        viewModel.servisYukleniyor.removeObservers(this)

        viewModel.odemeTipiListesi.observe(this, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    val list: List<OdemeTipleriJSON> = cevap.odemeTipleriJSON

                    if (list != null && list.size > 0) {

                        for (i in 0 until list.size) {
                            Log.d("servisAd", list[i].odemeTipID)
                        }
                    }

                    odemeYontemiSecAdapter.update(list)

                } else {
                    odemeYontemiSecAdapter.update(arrayListOf())
                    Toast.makeText(this, cevap.message, Toast.LENGTH_SHORT).show()
                }

                viewModel.odemeTipiListesi.postValue(null)
            }

        })
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.sola_gidis1, R.anim.sola_gidis2)
    }


    override fun kapat() {
        finish()
    }

}