package com.imalat.beeSystem.view.diger

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imalat.beeSystem.R
import com.imalat.beeSystem.adapter.SubelerAdapter
import com.imalat.beeSystem.model.subeler.SubelerCevap
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.SubelerFragmentViewModel
import kotlinx.android.synthetic.main.fragment_subeler.*


class SubelerFragment : Fragment(R.layout.fragment_subeler) {


    private lateinit var subelerFragmentViewModel: SubelerFragmentViewModel
    private var subelerAdapter = SubelerAdapter(arrayListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subelerFragmentViewModel =
            ViewModelProvider(this).get(SubelerFragmentViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        recycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = subelerAdapter


        if (Fonk.isNetworkAvailable(requireContext()))
            subeleriGetirPOST()

        geriBildirim.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    private fun subeleriGetirPOST() {

        subelerFragmentViewModel.subeListesi.removeObservers(viewLifecycleOwner)
        subelerFragmentViewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)
        subelerFragmentViewModel.ServisError.removeObservers(viewLifecycleOwner)


        subelerFragmentViewModel.subeleriGetir(
            GlobalDegiskenlerX.g025CRSubeList
        )

        subelerFragmentViewModel.subeListesi.observe(viewLifecycleOwner, Observer { cevap ->
            cevap?.let {


                if (cevap.success == 1) {

                    val list: List<SubelerCevap.SubelerJSON> = cevap.subelerJSON

                    if (list != null && list.size > 0) {
                        subelerAdapter.update(list)
                    } else {
                        subelerAdapter.update(arrayListOf())
                    }
                } else {
                    subelerAdapter.update(arrayListOf())
                }
            }

        })

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubelerFragment().apply {}
    }
}