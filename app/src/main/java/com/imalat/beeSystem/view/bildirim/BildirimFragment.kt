package com.imalat.beeSystem.view.bildirim

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imalat.beeSystem.R
import com.imalat.beeSystem.adapter.BildirimAdapter
import com.imalat.beeSystem.model.bildirim.BildirimlerCevap
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.BildirimFragmentViewModel
import kotlinx.android.synthetic.main.fragment_bildirim.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BildirimFragment : Fragment(R.layout.fragment_bildirim) {


    private lateinit var bildirimFragmentViewModel: BildirimFragmentViewModel
    private var bildirimAdapter = BildirimAdapter(arrayListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bildirimFragmentViewModel =
            ViewModelProvider(this).get(BildirimFragmentViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = bildirimAdapter


        if (Fonk.isNetworkAvailable(requireContext()))
            bildirmleriGetirPOST()

        geriBildirim.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    private fun bildirmleriGetirPOST() {

        bildirimFragmentViewModel.bildirimListesi.removeObservers(viewLifecycleOwner)
        bildirimFragmentViewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)
        bildirimFragmentViewModel.ServisError.removeObservers(viewLifecycleOwner)


        bildirimFragmentViewModel.bildirimleriGetir(
            GlobalDegiskenlerX.g026CRBildirimList,
            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString())
        )

        bildirimFragmentViewModel.bildirimListesi.observe(viewLifecycleOwner, Observer { cevap ->
            cevap?.let {


                if (cevap.success == 1) {

                    val list: List<BildirimlerCevap.BildirimlerJSON> = cevap.bildirimlerJSON

                    if (list != null && list.size > 0) {
                        bildirimAdapter.update(list)
                        bildirmYokLayout.visibility = View.INVISIBLE
                    } else {
                        bildirmYokLayout.visibility = View.VISIBLE
                        bildirimAdapter.update(arrayListOf())
                    }
                } else {
                    bildirimAdapter.update(arrayListOf())
                    bildirmYokLayout.visibility = View.VISIBLE
                }
            }

        })

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BildirimFragment().apply {}
    }
}