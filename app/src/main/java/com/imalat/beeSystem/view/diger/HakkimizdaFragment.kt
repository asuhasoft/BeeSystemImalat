package com.imalat.beeSystem.view.diger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.imalat.beeSystem.R
import com.imalat.beeSystem.model.kurumsalBilgi.KurumsalBilgiJSON
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.util.downloadFromUrl
import com.imalat.beeSystem.util.placeholderProgressBar
import com.imalat.beeSystem.viewModel.KurumsalBilgiViewModel
import kotlinx.android.synthetic.main.fragment_hakkimizda.*


class HakkimizdaFragment : Fragment() {


    lateinit var kurumsalBilgiViewModel: KurumsalBilgiViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kurumsalBilgiViewModel =
            ViewModelProvider(requireActivity()).get(KurumsalBilgiViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hakkimizda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (Fonk.isNetworkAvailable(requireContext())) {

            kurumsalBilgiViewModel.kurumsalBilgiGetir(GlobalDegiskenlerX.g024KurumsalBilgi)
            observerLiveDataKurumsalBilgiler()
        }


        geriHakkimzida.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    private fun observerLiveDataKurumsalBilgiler() {

        kurumsalBilgiViewModel.kurumsalBilgi.removeObservers(viewLifecycleOwner)
        kurumsalBilgiViewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)
        kurumsalBilgiViewModel.ServisError.removeObservers(viewLifecycleOwner)

        kurumsalBilgiViewModel.kurumsalBilgi.observe(viewLifecycleOwner, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    val list: List<KurumsalBilgiJSON> = cevap.kurumsalBilgiJSON

                    if (list != null && list.size > 0) {

                        tvHakkimzida.setText(list[0].hakkimizda)

                        imLogo.downloadFromUrl(
                            list[0].logo,
                            placeholderProgressBar(requireContext())
                        )
                    }
                }

            }

        })

    }


}