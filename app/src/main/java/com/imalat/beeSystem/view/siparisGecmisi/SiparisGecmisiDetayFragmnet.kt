package com.imalat.beeSystem.view.siparisGecmisi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imalat.beeSystem.R
import com.imalat.beeSystem.adapter.siparisGecmisi.SiparisGecmisiDetayAdapter
import com.imalat.beeSystem.model.siparisGecmisDetayi.SiparisDetayJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.SiparisGecmisiViewModel
import kotlinx.android.synthetic.main.fragment_siparis_gecmisi_detay.*


class SiparisGecmisiDetayFragmnet : Fragment(R.layout.fragment_siparis_gecmisi_detay) {

    lateinit var viewModel: SiparisGecmisiViewModel

    var siparisGecmisiDetayAdapter = SiparisGecmisiDetayAdapter(arrayListOf())

    var gelenSiparisID = ""
    var gelenToplamUcret = ""
    var gelendurumAd = ""
    var gelensiparisTarih = ""
    var gelenservisAd = ""
    var gelenteslimatTipi = ""
    var gelenodemeTipi = ""
    var gelenzilCalma = ""
    var gelensiparisNot = ""
    var gelenurunAdet = ""
    private lateinit var progressBarX: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gelenSiparisID = it.getString("siparisID").toString()
            gelenToplamUcret = it.getString("toplamUcret").toString()
            gelendurumAd = it.getString("durumAd").toString()
            gelensiparisTarih = it.getString("siparisTarih").toString()
            gelenservisAd = it.getString("servisAd").toString()
            gelenteslimatTipi = it.getString("teslimatTipi").toString()
            gelenodemeTipi = it.getString("odemeTipi").toString()
            gelenzilCalma = it.getString("zilCalma").toString()
            gelensiparisNot = it.getString("siparisNot").toString()
            gelenurunAdet = it.getString("urunAdet").toString()
        }

        viewModel = ViewModelProvider(requireActivity()).get(SiparisGecmisiViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progresBarOlustur()

        viewModel = ViewModelProvider(this).get(SiparisGecmisiViewModel::class.java)


        recycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = siparisGecmisiDetayAdapter

        tvSiparisToplamiX.setText(gelenToplamUcret + " TL")

        tvSiparisNoX.setText(gelenSiparisID)
        tvSiparisDurumuX.setText(gelendurumAd)

        tvUrunAdetX.setText("($gelenurunAdet Adet)")


        tvSiparisTarihiX.setText(gelensiparisTarih)
        tvZilDurumuX.setText(gelenzilCalma)


        tvTeslimatAdresimBaslikX.setText(gelenservisAd)
        tvServisZamaniX.setText(gelenservisAd)


        tvTeslimatTipiX.setText(gelenteslimatTipi)
        tvOdemeTipiX.setText(gelenodemeTipi)



        tvNotlarX.setOnClickListener {

            if (!gelensiparisNot.isNullOrBlank())
                Fonk.alertDialogGoster(requireContext(), "Sipariş Notları", gelensiparisNot)
        }



        if (Fonk.isNetworkAvailable(requireContext())) {
            viewModel.gecmisSiparisDetayListesiGetir(
                GlobalDegiskenlerX.g022SiparisDetayListesi,
                Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString()),
                gelenSiparisID
            )

            observerLiveDataSiparisGecimisiniGetir()
        }



        geri.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun observerLiveDataSiparisGecimisiniGetir() {

        viewModel.siparisGecmisDetayListesi.removeObservers(viewLifecycleOwner)
        viewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)
        viewModel.ServisError.removeObservers(viewLifecycleOwner)

        viewModel.siparisGecmisDetayListesi.observe(viewLifecycleOwner, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    val list: List<SiparisDetayJSON> = cevap.siparisDetayJSON

                    if (list != null && list.size > 0) {

                        siparisGecmisiDetayAdapter.update(list)

                    } else
                        siparisGecmisiDetayAdapter.update(list)


                } else {

                    siparisGecmisiDetayAdapter.update(arrayListOf())
                    Fonk.alertDialogGoster(requireContext(), "", cevap.message)
                }

                viewModel.siparisGecmisDetayListesi.postValue(null)
            }
        })


        viewModel.servisYukleniyor.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    Log.d("sepetimfRag", "yükleniyor")
                    progressBarX.visibility = View.VISIBLE

                } else {
                    Log.d("sepetimfRag", "bitti")
                    progressBarX.visibility = View.GONE
                }
                viewModel.servisYukleniyor.postValue(null)
            }
        })
    }


    private fun progresBarOlustur() {
        progressBarX = ProgressBar(requireContext())
        val params = RelativeLayout.LayoutParams(100, 100)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        progressBarX.layoutParams = params
        layoutSiparisGecmisiDetayFragmnet.addView(progressBarX)
    }

    companion object {
        @JvmStatic
        fun newInstance(siparisID: String) =
            SiparisGecmisiFragment().apply {
                arguments = Bundle().apply {
                    putString("siparisID", siparisID)

                }
            }
    }
}