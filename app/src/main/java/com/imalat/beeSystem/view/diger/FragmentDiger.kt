package com.imalat.beeSystem.view.anaSayfa

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.imalat.beeSystem.R

import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.view.bildirim.BildirimFragment
import com.imalat.beeSystem.view.diger.AlisVerisListemFragment
import com.imalat.beeSystem.view.diger.BilgilendirmeFragment
import com.imalat.beeSystem.view.diger.HakkimizdaFragment
import com.imalat.beeSystem.view.diger.SubelerFragment
import com.imalat.beeSystem.view.profil.GirisYap
import com.imalat.beeSystem.view.profil.Profilim
import com.imalat.beeSystem.view.profil.adres.Adreslerim
import com.imalat.beeSystem.view.siparisGecmisi.OnayBekleyeUrunlerFragment

import com.imalat.beeSystem.view.siparisGecmisi.SiparisGecmisiFragment
import kotlinx.android.synthetic.main.fragment_diger.*
import kotlinx.android.synthetic.main.include_diger.*
import kotlinx.android.synthetic.main.include_kullanici_verileri.*


class FragmentDiger : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_diger, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvVersiyon.setText("Ver. " + Fonk.versiyonName(requireContext()))


        tvKullaniciIsmi.setText(Fonk.degerGetir(requireContext(), EnumX.KullaniciAdi.toString()))

        kullaniciBilgilerim.setOnClickListener {

            if (Fonk.isNetworkAvailable(requireContext()))
                if (Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString())
                        .isNullOrBlank()
                ) {
                    startActivity(Intent(requireContext(), GirisYap::class.java))
                } else {
                    startActivity(Intent(requireContext(), Profilim::class.java))
                }

        }


        adreslerim.setOnClickListener {

            if (Fonk.isNetworkAvailable(requireContext()))
                if (Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString())
                        .isNullOrBlank()
                ) {
                    startActivity(Intent(requireContext(), GirisYap::class.java))
                } else {
                    val i = Intent(requireContext(), Adreslerim::class.java)
                    i.putExtra("nerdenCagrildi", "Diger")
                    startActivity(i)
                }
        }



        alisVerisListem.setOnClickListener {

            if (Fonk.isNetworkAvailable(requireContext())) {
                if (Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString())
                        .isNullOrBlank()
                ) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(this.resources.getString(R.string.uyari))
                    builder.setMessage("Üye olun")
                   // builder.setMessage("Kendi alışveriş listenizi oluşturmak ve kampanyalardan faydalanmak için hemen ücretsiz üye olun")
                    builder.setPositiveButton(
                        "Ücretsiz Üye Ol",
                        DialogInterface.OnClickListener { _, i ->

                            startActivity(Intent(requireContext(), GirisYap::class.java))

                        })
                    builder.setNegativeButton(
                        this.resources.getString(R.string.iptal),
                        DialogInterface.OnClickListener { dialogInterface, i ->

                        })
                    builder.show()

                } else {

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, AlisVerisListemFragment::class.java, null)
                        .addToBackStack(null)
                        .commit()

                    //startActivity(Intent(requireContext(),AlisVerisListem::class.java))
                }
            }

        }


        siparisGecmisim.setOnClickListener {

            if (Fonk.isNetworkAvailable(requireContext())) {
                if (Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString())
                        .isNullOrBlank()
                ) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(this.resources.getString(R.string.uyari))
                    builder.setMessage("Üye olun")
                  //  builder.setMessage("Sipariş geçmişinizi görmek ve kampanyalardan yararlanmak için hemen ücretsiz üye olun")
                    builder.setPositiveButton(
                        "Ücretsiz Üye Ol",
                        DialogInterface.OnClickListener { _, i ->

                            startActivity(Intent(requireContext(), GirisYap::class.java))
                        })
                    builder.setNegativeButton(
                        this.resources.getString(R.string.iptal),
                        DialogInterface.OnClickListener { dialogInterface, i ->

                        })
                    builder.show()

                } else {

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SiparisGecmisiFragment::class.java, null)
                        .addToBackStack(null)
                        .commit()

                }
            }
        }

        siparisOnayi.setOnClickListener {

            if (Fonk.isNetworkAvailable(requireContext())) {
                if (Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString())
                        .isNullOrBlank()
                ) {

                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(this.resources.getString(R.string.uyari))
                    builder.setMessage("Üye olun")

                   // builder.setMessage("Kendi alışveriş listenizi oluşturmak ve kampanyalardan faydalanmak için hemen ücretsiz üye olun")
                    builder.setPositiveButton(
                        "Ücretsiz Üye Ol",
                        DialogInterface.OnClickListener { dialogInterface, i ->

                            startActivity(Intent(requireContext(), GirisYap::class.java))

                        })
                    builder.setNegativeButton(
                        this.resources.getString(R.string.iptal),
                        DialogInterface.OnClickListener { dialogInterface, i ->

                        })
                    builder.show()

                } else {

                    parentFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            OnayBekleyeUrunlerFragment::class.java,
                            null
                        )
                        .addToBackStack(null)
                        .commit()

                }
            }


        }


        bildirimler.setOnClickListener {

            if (Fonk.isNetworkAvailable(requireContext()))
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, BildirimFragment::class.java, null)
                    .addToBackStack(null)
                    .commit()

        }


        hakkimizda.setOnClickListener {

            if (Fonk.isNetworkAvailable(requireContext()))
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HakkimizdaFragment::class.java, null)
                    .addToBackStack(null)
                    .commit()
        }

        iletisim.setOnClickListener {

            if (Fonk.isNetworkAvailable(requireContext()))
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SubelerFragment::class.java, null)
                    .addToBackStack(null)
                    .commit()
        }

        bilgilendirme.setOnClickListener {

            if (Fonk.isNetworkAvailable(requireContext()))
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, BilgilendirmeFragment::class.java, null)
                    .addToBackStack(null)
                    .commit()
        }

        paylas.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "http://play.google.com/store/apps/details?id=" + requireActivity().packageName
            );
            startActivity(Intent.createChooser(shareIntent, getString(R.string.paylas)))
        }

        puanVer.setOnClickListener {

            Log.d("aaa", requireActivity().packageName)
            try {
                var playstoreuri1: Uri =
                    Uri.parse("market://details?id=" + requireActivity().packageName)
                //or you can add
                //var playstoreuri:Uri=Uri.parse("market://details?id=manigautam.app.myplaystoreratingapp")
                var playstoreIntent1: Intent = Intent(Intent.ACTION_VIEW, playstoreuri1)
                startActivity(playstoreIntent1)
                //it genrate exception when devices do not have playstore
            } catch (exp: Exception) {
                var playstoreuri2: Uri =
                    Uri.parse("http://play.google.com/store/apps/details?id=" + requireActivity().packageName)
                //var playstoreuri:Uri=Uri.parse("http://play.google.com/store/apps/details?id=manigautam.app.myplaystoreratingapp")
                var playstoreIntent2: Intent = Intent(Intent.ACTION_VIEW, playstoreuri2)
                startActivity(playstoreIntent2)
            }
        }

    }


}