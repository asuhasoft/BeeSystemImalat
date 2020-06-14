package com.imalat.beeSystem.view.siparisGecmisi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imalat.beeSystem.R
import com.imalat.beeSystem.adapter.siparisGecmisi.OnayBekleyenUrunlerAdapter
import com.imalat.beeSystem.interfacee.OnayBekleyenUrunlerGuncelleInterface
import com.imalat.beeSystem.model.onayBekleyenUrunler.OnayBekleyenUrunlerCevap
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.OnayBekleyenUrunlerFragmentViewModel
import kotlinx.android.synthetic.main.fragment_onay_bekleyen_urunler.*


class OnayBekleyeUrunlerFragment : Fragment(R.layout.fragment_onay_bekleyen_urunler),
    OnayBekleyenUrunlerGuncelleInterface {


    private lateinit var onayBekleyenUrunlerFragmentViewModel: OnayBekleyenUrunlerFragmentViewModel
    private var onayBekleyenUrunlerAdapter = OnayBekleyenUrunlerAdapter(
        arrayListOf(),
        this@OnayBekleyeUrunlerFragment as OnayBekleyenUrunlerGuncelleInterface
    )

    private lateinit var progressBarX: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onayBekleyenUrunlerFragmentViewModel =
            ViewModelProvider(this).get(OnayBekleyenUrunlerFragmentViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progresBarOlustur()

        bildirmYokLayout.visibility = View.INVISIBLE

        recycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = onayBekleyenUrunlerAdapter


        if (Fonk.isNetworkAvailable(requireContext()))
            onayBekleyenUrunleriGetirPOST()

        geriOnayBekleyen.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    private fun onayBekleyenUrunleriGetirPOST() {

        onayBekleyenUrunlerFragmentViewModel.onayBekleyenUrunlerListesi.removeObservers(
            viewLifecycleOwner
        )
        onayBekleyenUrunlerFragmentViewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)
        onayBekleyenUrunlerFragmentViewModel.ServisError.removeObservers(viewLifecycleOwner)


        onayBekleyenUrunlerFragmentViewModel.onayBekleyeUrunleriGetir(
            GlobalDegiskenlerX.g033OnayBekleyenUrunler,
            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString())
        )

        onayBekleyenUrunlerFragmentViewModel.onayBekleyenUrunlerListesi.observe(
            viewLifecycleOwner,
            Observer { cevap ->
                cevap?.let {


                    if (cevap.success == 1) {

                        val list: List<OnayBekleyenUrunlerCevap.OnayBekleyenUrunlerJSON> =
                            cevap.onayBekleyenUrunlerJSON

                        if (list != null && list.size > 0) {
                            onayBekleyenUrunlerAdapter.update(list)
                            bildirmYokLayout.visibility = View.INVISIBLE
                        } else {
                            bildirmYokLayout.visibility = View.VISIBLE
                            onayBekleyenUrunlerAdapter.update(arrayListOf())
                        }
                    } else {
                        onayBekleyenUrunlerAdapter.update(arrayListOf())
                        bildirmYokLayout.visibility = View.VISIBLE
                    }

                    onayBekleyenUrunlerFragmentViewModel.onayBekleyenUrunlerListesi.postValue(null)
                }

            })

        onayBekleyenUrunlerFragmentViewModel.servisYukleniyor.observe(
            viewLifecycleOwner,
            Observer { loading ->
                loading?.let {
                    if (it) {
                        Log.d("sepetimfRag", "yükleniyor")
                        progressBarX.visibility = View.VISIBLE

                    } else {
                        Log.d("sepetimfRag", "bitti")
                        progressBarX.visibility = View.GONE
                    }
                    onayBekleyenUrunlerFragmentViewModel.servisYukleniyor.postValue(null)
                }
            })

    }


    override fun guncelle(Pst_SiparisDetayID: String, Pst_SiparisDetayDurumID: String) {
        siparisDetayiOnayla(Pst_SiparisDetayID, Pst_SiparisDetayDurumID)
    }

    fun siparisDetayiOnayla(Pst_SiparisDetayID: String, Pst_SiparisDetayDurumID: String) {

        onayBekleyenUrunlerFragmentViewModel.siparisDetayiOnayla.removeObservers(viewLifecycleOwner)
        onayBekleyenUrunlerFragmentViewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)
        onayBekleyenUrunlerFragmentViewModel.ServisError.removeObservers(viewLifecycleOwner)


        onayBekleyenUrunlerFragmentViewModel.siparisDetayiOnayla(
            GlobalDegiskenlerX.g034SiparisDetayOnay,
            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString()),
            Pst_SiparisDetayID,
            Pst_SiparisDetayDurumID
        )

        onayBekleyenUrunlerFragmentViewModel.siparisDetayiOnayla.observe(
            viewLifecycleOwner,
            Observer { cevap ->
                cevap?.let {

                    if (cevap.success == 1) {
                        Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_LONG).show()
                        onayBekleyenUrunleriGetirPOST()
                    } else {
                        Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_LONG).show()
                    }
                }

                onayBekleyenUrunlerFragmentViewModel.siparisDetayiOnayla.postValue(null)

            })
    }

    private fun progresBarOlustur() {
        progressBarX = ProgressBar(requireContext())
        val params = RelativeLayout.LayoutParams(100, 100)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        progressBarX.layoutParams = params
        layoutOnayBekleyeUrunlerFragment.addView(progressBarX)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            OnayBekleyeUrunlerFragment().apply {}
    }


}