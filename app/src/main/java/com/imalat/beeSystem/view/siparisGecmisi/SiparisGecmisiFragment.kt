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
import com.imalat.beeSystem.adapter.siparisGecmisi.SiparisGecmisiAdapter
import com.imalat.beeSystem.model.siparisGecmisi.SiparisGecmisiJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.SiparisGecmisiViewModel
import kotlinx.android.synthetic.main.fragment_siparis_gecmisi.*


class SiparisGecmisiFragment : Fragment(R.layout.fragment_siparis_gecmisi) {

    lateinit var viewModel: SiparisGecmisiViewModel

    var siparisGecmisiAdapter = SiparisGecmisiAdapter(arrayListOf())
    private lateinit var progressBarX: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SiparisGecmisiViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progresBarOlustur()

        recycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = siparisGecmisiAdapter


        if (Fonk.isNetworkAvailable(requireContext())) {
            viewModel.gecmisSiparisListesiGetir(
                GlobalDegiskenlerX.g021MusteriSiparisleri,
                Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString())
            )

            observerLiveDataSiparisGecimisiniGetir()
        }

        geri.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun observerLiveDataSiparisGecimisiniGetir() {

        viewModel.siparisGecmisListesi.removeObservers(viewLifecycleOwner)
        viewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)
        viewModel.ServisError.removeObservers(this)

        viewModel.siparisGecmisListesi.observe(viewLifecycleOwner, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    val list: List<SiparisGecmisiJSON> = cevap.siparisGecmisiJSON

                    if (list != null && list.size > 0) {

                        siparisGecmisiAdapter.update(list)

                    } else
                        siparisGecmisiAdapter.update(list)


                } else {

                    siparisGecmisiAdapter.update(arrayListOf())
                    Fonk.alertDialogGoster(requireContext(), "", cevap.message)
                }

                viewModel.siparisGecmisListesi.postValue(null)
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
        layoutSiparisGecmisiFragment.addView(progressBarX)
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SiparisGecmisiFragment().apply {}
    }
}