package com.imalat.beeSystem.view.kategoriler

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.imalat.beeSystem.R
import com.imalat.beeSystem.model.uruDetay.UrunDetayJSON
import com.imalat.beeSystem.util.*
import com.imalat.beeSystem.view.profil.GirisYap
import com.imalat.beeSystem.viewModel.KategoriDetaylariViewModel
import com.imalat.beeSystem.viewModel.UrunDetayViewModel
import kotlinx.android.synthetic.main.fragment_urun_detaylari.*


class UrunDetaylariFrgamentIptal : Fragment() {

    lateinit var urunDetayViewModel: UrunDetayViewModel
    lateinit var viewModelKategoriDetaylari: KategoriDetaylariViewModel

    var gelenUrunID = ""
    var gelenFotoAdres = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        urunDetayViewModel =
            ViewModelProvider(requireActivity()).get(UrunDetayViewModel::class.java)
        viewModelKategoriDetaylari =
            ViewModelProvider(requireActivity()).get(KategoriDetaylariViewModel::class.java)


        arguments?.let {
            gelenUrunID = it.getString("urunID").toString()
            Log.d("bgelenn argu", it.getString("urunID").toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_urun_detaylari, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (Fonk.isNetworkAvailable(requireContext())) {
            urunDetayViewModel.urunDetaylariGetir(
                GlobalDegiskenlerX.g019UrunDetay,
                Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString()),
                gelenUrunID
            )

            observerLiveDataUrunDetaylari()
        }

    }

    private fun observerLiveDataUrunDetaylari() {
        urunDetayViewModel.urunDetaylari.removeObservers(viewLifecycleOwner)
        urunDetayViewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)
        urunDetayViewModel.ServisError.removeObservers(viewLifecycleOwner)


        urunDetayViewModel.urunDetaylari.observe(viewLifecycleOwner, Observer { cevap ->
            cevap?.let {


                if (cevap.success == 1) {

                    var list: List<UrunDetayJSON> = cevap.urunDetayJSON


                    if (list != null && list.size > 0) {

                        tvUrunAdi.text = list[0].urunAdi

                        viewYukle(list)
                    }

                } else {
                    Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()
                }
            }

        })


    }

    fun viewYukle(list: List<UrunDetayJSON>) {

        val position = 0
        tvMarkaIsmi.text = list[position].markaAd
        tvAciklama.text = list[position].aciklama
        tvUrunAdi.text = list[position].urunAdi
        tvKampanyaliFiyat.text = list[position].kampanyaliFiyat + " TL"
        tvFiyat.text = list[position].fiyat + " TL"
        tvFiyat.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        tvIndirimOrani.text =
            "%${Fonk.indirimOraniHesapla(list[position].fiyat, list[position].kampanyaliFiyat)}"
        gelenFotoAdres = list[position].fotograf

        //list[position].sepettekiAdet = sepettekiUrunSayisi(list[position].urunID)

        tvAdet.text =
            Fonk.artisMikatriParcala(list[position].sepettekiAdet.toString()) + " " + list[position].birimAd

        if (Fonk.doubleCevir(list[position].sepettekiAdet) > 0.0) {
            cardViewArttirmaAzaltma.visibility = View.INVISIBLE
            //frameLayout.visibility = View.VISIBLE
        } else {
            cardViewArttirmaAzaltma.visibility = View.VISIBLE
            // frameLayout.visibility = View.INVISIBLE
        }


        //indirim oranı yoksa gizle
        if (list[position].fiyat.equals(list[position].kampanyaliFiyat)) {
            indirimLayout.visibility = View.INVISIBLE
            tvFiyat.visibility = View.INVISIBLE
        } else {
            indirimLayout.visibility = View.VISIBLE
            tvFiyat.visibility = View.VISIBLE
        }

        //ürün favoriye ekli mi değil mi
        if (list[position].favori.equals("1")) {
            imAlisVerisListesi.setImageResource(R.drawable.x_ic_add_list_active)
        } else {
            imAlisVerisListesi.setImageResource(R.drawable.x_ic_add_list)
        }

        imUrunFoto.downloadFromUrl(
            list[position].fotograf,
            placeholderProgressBar(requireContext())
        )







        imAlisVerisListesi.setOnClickListener {

            if (!Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString()).isNullOrBlank()) {

                if (Fonk.isNetworkAvailable(requireContext())) {
                    alisverisListemeEkleCikartPOST(gelenUrunID)
                }
            } else {

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
            }

        }


        geriUrunDetaylariFragment.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun alisverisListemeEkleCikartPOST(Pst_UrunID: String) {

        viewModelKategoriDetaylari.alisVerisListesineEkleCikar(
            GlobalDegiskenlerX.g014MUAlisverisListeEdit,
            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString()),
            Pst_UrunID
        )


        viewModelKategoriDetaylari.alisVerisListeEdit.removeObservers(viewLifecycleOwner)
        viewModelKategoriDetaylari.ServisError.removeObservers(viewLifecycleOwner)
        viewModelKategoriDetaylari.servisYukleniyor.removeObservers(viewLifecycleOwner)

        viewModelKategoriDetaylari.alisVerisListeEdit.observe(
            viewLifecycleOwner,
            Observer { cevap ->
                cevap?.let {

                    if (cevap.success == 1) {
                        imAlisVerisListesi.setImageResource(R.drawable.x_ic_add_list_active)
                        Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()
                    } else if (cevap.success == 2) {
                        imAlisVerisListesi.setImageResource(R.drawable.x_ic_add_list)
                        Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()
                    }
                }

                viewModelKategoriDetaylari.alisVerisListeEdit.postValue(null)

            })


    }


    companion object {

        fun newInstance(gelenParametre: String) = UrunDetaylariFrgamentIptal().apply {
            arguments = Bundle().apply {
                putString("urunID", gelenParametre)
            }
        }

    }


}