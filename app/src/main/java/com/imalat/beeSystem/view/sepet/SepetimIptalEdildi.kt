package com.imalat.beeSystem.view.sepet

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imalat.beeSystem.R
import com.imalat.beeSystem.adapter.SepetAdapter
import com.imalat.beeSystem.interfacee.GeldiginEkraniKapatInterface
import com.imalat.beeSystem.interfacee.SepetGuncelleInterface
import com.imalat.beeSystem.model.SepetOnline.SepetJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.view.profil.GirisYap
import com.imalat.beeSystem.view.sepet.siparisOzeti.SiparisOzetiFragment
import com.imalat.beeSystem.viewModel.AnaSayfaViewModel
import com.imalat.beeSystem.viewModel.FragmentAnaSayfaViewModel
import com.imalat.beeSystem.viewModel.SepetViewModel
import kotlinx.android.synthetic.main.activity_sepetim.*


class SepetimIptalEdildi : AppCompatActivity(), SepetGuncelleInterface,
    GeldiginEkraniKapatInterface {

    private var sepetAdapter = SepetAdapter(arrayListOf(), this as SepetGuncelleInterface)
    lateinit var sepetViewModel: SepetViewModel
    private lateinit var fragmentAnaSayfaViewModel: FragmentAnaSayfaViewModel
    lateinit var anaSayfaViewModel: AnaSayfaViewModel

    // var badgeGuncelleInterface: BadgeGuncelleInterface = this as BadgeGuncelleInterface

    var sepetToplamTutari: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sepetim)
        overridePendingTransition(R.anim.saga_gidis1, R.anim.saga_gidis2)
        sepetBosMu(false)

        sepetViewModel = ViewModelProvider(this).get(SepetViewModel::class.java)
        fragmentAnaSayfaViewModel =
            ViewModelProvider(this).get(FragmentAnaSayfaViewModel::class.java)
        anaSayfaViewModel = ViewModelProvider(this).get(AnaSayfaViewModel::class.java)


        // this.badgeGuncelleInterface = this as (BadgeGuncelleInterface)

        // badgeGuncelleInterface = this as BadgeGuncelleInterface

        recycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = sepetAdapter

        if (Fonk.isNetworkAvailable(this))
            sepetiGetirPOST()

        sil.setOnClickListener {

            if (Fonk.isNetworkAvailable(this)) {


                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle(this.resources.getString(R.string.uyari))
                builder.setMessage("Listeyi Boşaltmak İstiyor musunuz?")
                builder.setNegativeButton(
                    this.resources.getString(R.string.evet),
                    DialogInterface.OnClickListener { dialogInterface, i ->

                        sepetBosaltPOST()

                    })
                builder.setPositiveButton(
                    this.resources.getString(R.string.iptal),
                    DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                builder.show()
            }


        }


        tvIleri.setOnClickListener {

            if (sepetToplamTutari > 0.0) {

                if (Fonk.degerGetir(this, EnumX.OturumKodu.toString()).isNullOrBlank()) {

                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setTitle(this.resources.getString(R.string.uyari))
                    builder.setMessage("Üye olun")
                    //builder.setMessage("Sipariş vermek için hemen ücretsiz üye olup kampanyalardan faydalanbilirsiniz")
                    builder.setNegativeButton(
                        "Ücretsiz Üye Ol",
                        DialogInterface.OnClickListener { dialogInterface, i ->

                            startActivity(Intent(this, GirisYap::class.java))

                        })
                    builder.setPositiveButton(
                        this.resources.getString(R.string.iptal),
                        DialogInterface.OnClickListener { dialogInterface, i ->

                        })
                    builder.show()

                } else {

                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, SiparisOzetiFragment::class.java, null)
                        .addToBackStack(null)
                        .commit()

                    /* val frg = SiparisOzetiFragment
                     frg.newInstance().show(supportFragmentManager, "ad")*/
                }


            } else {


                //Fonk.alertDialogGoster(this,"","Sepetiniz ")
            }

        }

        geri.setOnClickListener {
            finish()
        }
    }

    fun sepetiGetirPOST() {

        sepetViewModel.sepetGetir(
            GlobalDegiskenlerX.g028SepetGetir,
            Fonk.degerGetir(this, EnumX.OturumKodu.toString())
        )


        sepetViewModel.sepetListesi.removeObservers(this)
        sepetViewModel.servisYukleniyor.removeObservers(this)
        sepetViewModel.ServisError.removeObservers(this)

        sepetViewModel.sepetListesi.observe(this, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    val list: List<SepetJSON> = cevap.sepetJSON

                    if (list != null && list.size > 0) {
                        sepetAdapter.update(list)
                        sepetBosMu(false)
                        sepetToplamTutari = cevap.sepettoplam

                        tvSepetToplamTutar.text = "${cevap.sepettoplam} TL "

                    } else {
                        sepetAdapter.update(arrayListOf())
                        sepetBosMu(true)
                    }

                } else {
                    sepetAdapter.update(arrayListOf())
                    sepetBosMu(true)
                    sepetToplamTutari = 0.0
                }
            }
        })


    }

    private fun sepetBosMu(bosMu: Boolean) {
        if (bosMu) {
            sepetBosLayout.visibility = View.VISIBLE
            sil.visibility = View.INVISIBLE
            cardALt.visibility = View.INVISIBLE
        } else {
            sepetBosLayout.visibility = View.INVISIBLE
            sil.visibility = View.VISIBLE
            cardALt.visibility = View.VISIBLE

        }
    }

    override fun onResume() {
        super.onResume()
        sepetiGetirPOST()
    }

    override fun guncelle() {
        //sepetiGetirPOST()
    }

    override fun sepeteEkleCikart(Pst_UrunID: String, miktar: String) {
        if (Fonk.isNetworkAvailable(this))
            sepeteEkleCikartPOST(Pst_UrunID, miktar)
    }


    private fun sepeteEkleCikartPOST(Pst_UrunID: String, miktar: String) {

        fragmentAnaSayfaViewModel.sepetEkleCikart(
            GlobalDegiskenlerX.g027SepetGuncelle,
            Fonk.degerGetir(this, EnumX.OturumKodu.toString()),
            "EkleCikart",
            miktar,
            Pst_UrunID
        )


        fragmentAnaSayfaViewModel.sepetEkleCikart.removeObservers(this)
        fragmentAnaSayfaViewModel.ServisError.removeObservers(this)
        fragmentAnaSayfaViewModel.servisYukleniyor.removeObservers(this)

        fragmentAnaSayfaViewModel.sepetEkleCikart.observe(
            this,
            Observer { cevap ->
                cevap?.let {

                    sepetiGetirPOST()
                    // sepetToplamTutariGetirPOST()

                    /* if (cevap.success == 1) {
                         Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()
                     } else
                         Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()*/

                    // badgeGuncelleInterface.badgeGuncelle("")
                }

                fragmentAnaSayfaViewModel.sepetEkleCikart.postValue(null)

            })
    }


    private fun sepetBosaltPOST() {

        fragmentAnaSayfaViewModel.sepetEkleCikart(
            GlobalDegiskenlerX.g027SepetGuncelle,
            Fonk.degerGetir(this, EnumX.OturumKodu.toString()),
            "Bosalt",
            "",
            ""
        )


        fragmentAnaSayfaViewModel.sepetEkleCikart.removeObservers(this)
        fragmentAnaSayfaViewModel.ServisError.removeObservers(this)
        fragmentAnaSayfaViewModel.servisYukleniyor.removeObservers(this)

        fragmentAnaSayfaViewModel.sepetEkleCikart.observe(
            this,
            Observer { cevap ->
                cevap?.let {

                    if (cevap.success == 1) {
                        Toast.makeText(this, cevap.message, Toast.LENGTH_SHORT).show()
                        sepetAdapter.update(arrayListOf())
                        sepetBosMu(true)

                    } else
                        Toast.makeText(this, cevap.message, Toast.LENGTH_SHORT).show()


                    // badgeGuncelleInterface.badgeGuncelle("")
                }

                fragmentAnaSayfaViewModel.sepetEkleCikart.postValue(null)

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