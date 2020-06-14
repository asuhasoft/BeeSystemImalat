package com.imalat.beeSystem.view.sepet.siparisOzeti

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imalat.beeSystem.R
import com.imalat.beeSystem.adapter.TeslimatZamaniAdapter
import com.imalat.beeSystem.interfacee.TeslimatZamaniSecInterface
import com.imalat.beeSystem.model.siparisOzeti.teslimatZamani.ServisPlanlariJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.SiparisOzetiViewModel
import kotlinx.android.synthetic.main.fragment_teslimat_zamani.*

class TeslimatZamaniSec : AppCompatActivity(), TeslimatZamaniSecInterface {

    private lateinit var viewModel: SiparisOzetiViewModel
    private var teslimatZamaniAdapter = TeslimatZamaniAdapter(
        arrayListOf(), this as TeslimatZamaniSecInterface
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teslimat_zamani_sec)
        overridePendingTransition(R.anim.saga_gidis1, R.anim.saga_gidis2)

        viewModel = ViewModelProvider(this)
            .get(SiparisOzetiViewModel::class.java)

        recycleViewX.layoutManager = LinearLayoutManager(this)
        recycleViewX.adapter = teslimatZamaniAdapter

        if (Fonk.isNetworkAvailable(this))
            teslimatZamaniGetir()


        geriX.setOnClickListener {
            finish()
        }

    }

    fun teslimatZamaniGetir() {

        viewModel.servisPlanListesiGetir(
            GlobalDegiskenlerX.g018SPServisPlanList,
            Fonk.degerGetir(this, EnumX.OturumKodu.toString())
        )

        viewModel.servisPlanListesi.removeObservers(this)
        viewModel.ServisError.removeObservers(this)
        viewModel.servisYukleniyor.removeObservers(this)

        viewModel.servisPlanListesi.observe(this, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    val list: List<ServisPlanlariJSON> = cevap.servisPlanlariJSON

                    if (list != null && list.size > 0) {

                        for (i in 0 until list.size) {
                            Log.d("servisAd", list[i].servisAd)
                        }
                    }

                    teslimatZamaniAdapter.update(list)

                } else {
                    teslimatZamaniAdapter.update(arrayListOf())
                    Toast.makeText(this, cevap.message, Toast.LENGTH_SHORT).show()
                }

                viewModel.servisPlanListesi.postValue(null)

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