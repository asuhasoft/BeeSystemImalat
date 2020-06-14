package com.imalat.beeSystem.view.diger

import android.graphics.Paint
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
import com.imalat.beeSystem.viewModel.KurumsalBilgiViewModel
import kotlinx.android.synthetic.main.fragment_bilgilendirme.*


class BilgilendirmeFragment : Fragment() {

    lateinit var kurumsalBilgiViewModel: KurumsalBilgiViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kurumsalBilgiViewModel =
            ViewModelProvider(requireActivity()).get(KurumsalBilgiViewModel::class.java)
    }


    private var sozlesmeMetni = ""
    private var kvkkMetni = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bilgilendirme, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioSozlesme.visibility = View.INVISIBLE
        radioKvkk.visibility = View.INVISIBLE

        radioSozlesme.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        radioKvkk.paintFlags = Paint.ANTI_ALIAS_FLAG


        if (Fonk.isNetworkAvailable(requireContext())) {
            kurumsalBilgiViewModel.kurumsalBilgiGetir(GlobalDegiskenlerX.g024KurumsalBilgi)
            observerLiveDataKurumsalBilgiler()
        }


        radioSozlesme.setOnCheckedChangeListener { compoundButton, isChecked ->

            if (isChecked) {
                tvBilgilendirme.setText(sozlesmeMetni)
                radioSozlesme.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                radioKvkk.paintFlags = Paint.ANTI_ALIAS_FLAG
            } else {
                // radioSozlesme.setBackgroundResource(R.drawable.buton_mavi_cerceve_oval)

            }
        }

        radioKvkk.setOnCheckedChangeListener { compoundButton, isChecked ->

            if (isChecked) {
                tvBilgilendirme.setText(kvkkMetni)
                radioSozlesme.paintFlags = Paint.ANTI_ALIAS_FLAG
                radioKvkk.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            } else {
                // radioSozlesme.setBackgroundResource(R.drawable.buton_mavi_cerceve_oval)
            }
        }

        geriBligilendirme.setOnClickListener {
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

                        radioSozlesme.visibility = View.VISIBLE
                        radioKvkk.visibility = View.VISIBLE

                        sozlesmeMetni = list[0].kullaniciSozlesme
                        kvkkMetni = list[0].kVKKMetni

                        tvBilgilendirme.setText(list[0].kullaniciSozlesme)
                    }
                }

            }

        })

    }

}

