package com.imalat.beeSystem.view.kategoriler

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.imalat.beeSystem.R
import com.imalat.beeSystem.adapter.KategorilerAdapter
import com.imalat.beeSystem.model.urunKategorileri.UrunKategoriJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.FragmentKategorilerViewModel
import kotlinx.android.synthetic.main.fragment_kategoriler.*


class FragmentKategoriler : Fragment() {

    private lateinit var viewModel: FragmentKategorilerViewModel
    private var kategorilerAdapter = KategorilerAdapter(arrayListOf())

    lateinit var progressBarX: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kategoriler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progresBarOlustur()

        viewModel = ViewModelProvider(this@FragmentKategoriler)
            .get(FragmentKategorilerViewModel::class.java)



        recycleView.layoutManager = GridLayoutManager(requireContext(), 3)
        //recycleView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = kategorilerAdapter


        if (Fonk.isNetworkAvailable(requireContext())) {

            var urlx = GlobalDegiskenlerX.g002URKategoriList.toString()
            // if (urlx.isNullOrBlank()) urlx = Fonk.kayitGetirSifreli(requireContext(), Enum.g00.toString())

            viewModel.kategoriListesiGetir(
                urlx, "",
                Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString())
            )
            observerLiveDataKategoriler()
        }


    }


    private fun observerLiveDataKategoriler() {
        viewModel.urunKategoriListesi.removeObservers(viewLifecycleOwner)
        viewModel.ServisError.removeObservers(viewLifecycleOwner)
        viewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)

        Log.d("tag", "observerLiveDataUrunKategoriListesi " + " çalıştı")
        var tetiklenme = 0

        viewModel.urunKategoriListesi.observe(viewLifecycleOwner, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    val list: List<UrunKategoriJSON> = cevap.urunKategoriJSON



                    if (list != null && list.size > 0) {

                        /* for (x in 0 until list.size) {
                             //sira bilgisi manuel ekleniyor
                             // list[x].sira = (x+1).toString()
                             Log.d("tag", "baslik " + list[x].kategoriAd)
 
                             Log.d("tag", "---- -----------------" )
                         }*/
                        kategorilerAdapter.update(list)
                    } else {
                        Log.d("size ", "slider arrray boş")

                    }


                } else {

                    Fonk.alertDialogGoster(requireContext(), "", cevap.message)
                }

                viewModel.urunKategoriListesi.postValue(null)
            }

        })



        viewModel.ServisError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    // tvServisError.visibility = View.VISIBLE
                } else {
                    // tvServisError.visibility = View.GONE
                    tetiklenme++
                }
                viewModel.ServisError.postValue(null)
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
        layoutFragmentKategoriler.addView(progressBarX)
    }

}