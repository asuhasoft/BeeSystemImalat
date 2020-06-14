package com.imalat.beeSystem.view.profil.adres

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imalat.beeSystem.R
import com.imalat.beeSystem.adapter.adres.AdresAdapter
import com.imalat.beeSystem.interfacee.AdresGuncelleInterface
import com.imalat.beeSystem.model.profil.adresler.adreslerim.MusteriAdresJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.AdresEkleDuzenleViewModel
import com.imalat.beeSystem.viewModel.AdreslerimViewModel
import kotlinx.android.synthetic.main.activity_adreslerim.*

class Adreslerim : AppCompatActivity(), AdresGuncelleInterface {

    lateinit var viewModel: AdreslerimViewModel
    lateinit var viewModelAdresGuncelle: AdresEkleDuzenleViewModel


    var adresAdapter = AdresAdapter(arrayListOf(), this as AdresGuncelleInterface)

    var nerdenCagrildi = "Profilim"
    private lateinit var progressBarX: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adreslerim)
        overridePendingTransition(R.anim.saga_gidis1, R.anim.saga_gidis2)

        progresBarOlustur()

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            nerdenCagrildi = bundle.getString("nerdenCagrildi").toString()
        }

        viewModel = ViewModelProvider(this).get(AdreslerimViewModel::class.java)
        viewModelAdresGuncelle =
            ViewModelProvider(this).get(AdresEkleDuzenleViewModel::class.java)

        recycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = adresAdapter

        if (Fonk.isNetworkAvailable(this)) {

            adresleriGetir()
        }

        tvAdresEkle.setOnClickListener {

            startActivity(Intent(this, AdresEkleDuzenle::class.java))

        }

        geri.setOnClickListener {
            finish()
        }
    }

    private fun adresleriGetir() {
        viewModel.adresGetir(
            GlobalDegiskenlerX.g013MUAdresList,
            Fonk.degerGetir(this, EnumX.OturumKodu.toString())
        )
        observerLiveDataAdresler()
    }


    override fun onResume() {
        super.onResume()
        adresleriGetir()
    }

    private fun observerLiveDataAdresler() {
        viewModel.musteriAdresleri.removeObservers(this)
        viewModel.ServisError.removeObservers(this)
        viewModel.servisYukleniyor.removeObservers(this)

        //Log.d("tag", "observerLiveDataUrunKategoriListesi " + " çalıştı")
        var tetiklenme = 0

        viewModel.musteriAdresleri.observe(this, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    val list: List<MusteriAdresJSON> = cevap.musteriAdresJSON

                    if (list != null && list.size > 0) {


                        adresAdapter.update(list)
                    } else {
                        //Log.d("size ", "slider arrray boş")
                        adresAdapter.update(arrayListOf())
                    }


                } else {
                    adresAdapter.update(arrayListOf())


                    //Toast.makeText(this,cevap.message,Toast.LENGTH_SHORT).show()
                }

                viewModel.musteriAdresleri.postValue(null)
            }

        })



        viewModel.ServisError.observe(this, Observer { error ->
            error?.let {
                if (it) {
                    // tvServisError.visibility = View.VISIBLE
                } else {
                    // tvServisError.visibility = View.GONE
                    tetiklenme++

                }

                viewModel.ServisError.postValue(null)
            }
        })

        viewModel.servisYukleniyor.observe(this, Observer { loading ->
            loading?.let {
                if (it) {
                    //Log.d("sepetimfRag", "yükleniyor")
                    progressBarX.visibility = View.VISIBLE

                } else {
                    //Log.d("sepetimfRag", "bitti")
                    progressBarX.visibility = View.GONE
                }

                viewModel.servisYukleniyor.postValue(null)
            }
        })
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.sola_gidis1, R.anim.sola_gidis2)
    }

    override fun adresGuncelle() {

    }

    override fun adresSil(MusteriAdresID: String) {

        if (Fonk.isNetworkAvailable(this)) {

            viewModelAdresGuncelle.adresSil(
                GlobalDegiskenlerX.g012MUAdresEdit,
                "Sil",
                Fonk.degerGetir(this, EnumX.OturumKodu.toString()),
                MusteriAdresID
            )


            viewModelAdresGuncelle.adresSil.removeObservers(this)


            viewModelAdresGuncelle.adresSil.observe(this, Observer { cevap ->
                cevap?.let {

                    if (cevap.success == 1) {

                        Toast.makeText(this, cevap.message, Toast.LENGTH_SHORT).show()

                    } else {

                        Toast.makeText(this, cevap.message, Toast.LENGTH_SHORT).show()
                    }

                    adresleriGetir()

                    viewModelAdresGuncelle.adresSil.postValue(null)
                }

            })


        }
    }

    override fun adresVarsayilanYap(MusteriAdresID: String) {


        viewModelAdresGuncelle.adresEkleGuncelle(
            GlobalDegiskenlerX.g012MUAdresEdit,
            "Varsayilan",
            Fonk.degerGetir(this, EnumX.OturumKodu.toString()),
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "1",
            MusteriAdresID,
            ""
        )


        viewModelAdresGuncelle.adresEkleGuncelle.removeObservers(this)

        viewModelAdresGuncelle.adresEkleGuncelle.observe(this, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    adresleriGetir()

                    Toast.makeText(this, "Varsayılan yapıldı", Toast.LENGTH_SHORT).show()
                } else {
                    Fonk.alertDialogGoster(this, "", cevap.message)

                }

                viewModelAdresGuncelle.adresEkleGuncelle.postValue(null)
            }

        })

    }

    private fun progresBarOlustur() {
        progressBarX = ProgressBar(this)
        val params = RelativeLayout.LayoutParams(100, 100)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        progressBarX.layoutParams = params
        layoutAdreslerim.addView(progressBarX)
    }

    override fun activiteyiKapat() {

        if (nerdenCagrildi.equals("SiparisOzeti"))
            finish()
    }
}