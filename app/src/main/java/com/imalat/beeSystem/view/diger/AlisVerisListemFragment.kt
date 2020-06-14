package com.imalat.beeSystem.view.diger

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
import com.imalat.beeSystem.adapter.AlisverisListemAdapter
import com.imalat.beeSystem.interfacee.AlisverisListemInterface
import com.imalat.beeSystem.interfacee.BadgeGuncelleInterface
import com.imalat.beeSystem.model.alisVerisListesi.AlisverisListesiJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.AlisverisListemViewModel
import com.imalat.beeSystem.viewModel.FragmentAnaSayfaViewModel
import com.imalat.beeSystem.viewModel.KategoriDetaylariViewModel
import kotlinx.android.synthetic.main.fragment_alis_veris_listem.*


class AlisVerisListemFragment : Fragment(R.layout.fragment_alis_veris_listem),
    AlisverisListemInterface {

    lateinit var alisverisListemViewModel: AlisverisListemViewModel
    private lateinit var viewModelKategoriDetaylari: KategoriDetaylariViewModel
    private lateinit var fragmentAnaSayfaViewModel: FragmentAnaSayfaViewModel


    lateinit var badgeGuncelleInterface: BadgeGuncelleInterface
    private var alisverisListemAdapter =
        AlisverisListemAdapter(arrayListOf(), this as AlisverisListemInterface)
    var listX: List<AlisverisListesiJSON> = arrayListOf()

    private lateinit var progressBarX: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.badgeGuncelleInterface = context as BadgeGuncelleInterface
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sepeteEkleButonuGizleGoster(true)
        progresBarOlustur()

        fragmentAnaSayfaViewModel =
            ViewModelProvider(requireActivity()).get(FragmentAnaSayfaViewModel::class.java)

        alisverisListemViewModel =
            ViewModelProvider(requireActivity()).get(AlisverisListemViewModel::class.java)
        viewModelKategoriDetaylari =
            ViewModelProvider(this).get(KategoriDetaylariViewModel::class.java)

        recycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = alisverisListemAdapter

        if (Fonk.isNetworkAvailable(requireContext())) {
            alisverisListemViewModel.alisVerisListesiGetir(
                GlobalDegiskenlerX.g015MUAlisverisListeList,
                Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString())
            )

            observerLiveDataAlisVerisListesi()
        }


        tvTumunuSepetEkle.setOnClickListener {


            tumunuSpeteEkle()
        }

        geriGit.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    fun tumunuSepeteAt() {

        badgeGuncelleInterface.badgeGuncelle("")
    }


    private fun observerLiveDataAlisVerisListesi() {
        alisverisListemViewModel.alisVerisListesi.removeObservers(viewLifecycleOwner)
        alisverisListemViewModel.ServisError.removeObservers(viewLifecycleOwner)
        alisverisListemViewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)


        alisverisListemViewModel.alisVerisListesi.observe(viewLifecycleOwner, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    val list: List<AlisverisListesiJSON> = cevap.alisverisListesiJSON

                    listX = cevap.alisverisListesiJSON

                    if (list != null && list.size > 0) {

                        sepeteEkleButonuGizleGoster(false)

                        alisverisListemAdapter.update(list)
                    } else {
                        sepeteEkleButonuGizleGoster(true)
                        //Log.d("size ", "slider arrray boş")
                        alisverisListemAdapter.update(arrayListOf())
                    }


                } else {
                    alisverisListemAdapter.update(arrayListOf())
                    //Fonk.alertDialogGoster(this, "", cevap.message)

                    //Toast.makeText(this,cevap.message,Toast.LENGTH_SHORT).show()
                }

                alisverisListemViewModel.alisVerisListesi.postValue(null)
            }

        })



        alisverisListemViewModel.ServisError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    // tvServisError.visibility = View.VISIBLE
                } else {
                    // tvServisError.visibility = View.GONE


                }
            }
        })

        alisverisListemViewModel.servisYukleniyor.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    //Log.d("sepetimfRag", "yükleniyor")
                    progressBarX.visibility = View.VISIBLE

                } else {
                    //Log.d("sepetimfRag", "bitti")
                    progressBarX.visibility = View.GONE
                }
                alisverisListemViewModel.servisYukleniyor.postValue(null)
            }
        })
    }


    override fun listedenCikart(Pst_UrunID: String, urunSayisi: Int) {

        //Log.d("urunSayisi", urunSayisi.toString())
        alisverisListemeEkleCikartPOST(Pst_UrunID, urunSayisi)

    }

    override fun sepetGuncelle() {
        badgeGuncelleInterface.badgeGuncelle("")
    }

    override fun sepeteEkleCikart(Pst_UrunID: String, miktar: String) {

        sepeteEkleCikartPOST(Pst_UrunID, miktar)
    }

    private fun alisverisListemeEkleCikartPOST(Pst_UrunID: String, urunSayisi: Int) {

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

                        Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()
                    } else {

                        //Log.d("urunSAyisi_", urunSayisi.toString())
                        //tümünü sepete ekle butonu gizlendi
                        if (urunSayisi == 1) {
                            sepeteEkleButonuGizleGoster(true)
                        }

                        Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()

                    }
                }

                viewModelKategoriDetaylari.alisVerisListeEdit.postValue(null)

            })

    }

    private fun sepeteEkleCikartPOST(Pst_UrunID: String, miktar: String) {

        fragmentAnaSayfaViewModel.sepetEkleCikart(
            GlobalDegiskenlerX.g027SepetGuncelle,
            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString()),
            "EkleCikart",
            miktar,
            Pst_UrunID
        )


        fragmentAnaSayfaViewModel.sepetEkleCikart.removeObservers(viewLifecycleOwner)
        fragmentAnaSayfaViewModel.ServisError.removeObservers(viewLifecycleOwner)
        fragmentAnaSayfaViewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)

        fragmentAnaSayfaViewModel.sepetEkleCikart.observe(
            viewLifecycleOwner,
            Observer { cevap ->
                cevap?.let {

                    if (cevap.success == 1) {
                        Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()
                    } else
                        Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()

                    badgeGuncelleInterface.badgeGuncelle("")
                }

                fragmentAnaSayfaViewModel.sepetEkleCikart.postValue(null)

            })
    }

    private fun sepeteEkleButonuGizleGoster(gizlensinMi: Boolean) {

        if (gizlensinMi) {
            // //Log.d("gizle", "aktif")
            cardALt.visibility = View.INVISIBLE
        } else {
            // //Log.d("gizle", "pasif")
            cardALt.visibility = View.VISIBLE
        }
    }


    private fun tumunuSpeteEkle() {

        if (Fonk.isNetworkAvailable(requireContext())) {

            Log.e("g030AlisverisListesiSepete", GlobalDegiskenlerX.g030AlisverisListesiSepete)

            alisverisListemViewModel.tumunuSepeteEkle(
                GlobalDegiskenlerX.g030AlisverisListesiSepete,
                Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString())
            )


            alisverisListemViewModel.tumunuSepeteEkle.removeObservers(viewLifecycleOwner)
            alisverisListemViewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)
            alisverisListemViewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)


            alisverisListemViewModel.tumunuSepeteEkle.observe(
                viewLifecycleOwner,
                Observer { cevap ->
                    cevap?.let {

                        if (cevap.success == 1) {
                            badgeGuncelleInterface.badgeGuncelle("")
                            Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_LONG)
                                .show()
                        } else {
                            Fonk.alertDialogGoster(requireContext(), "", cevap.message)
                        }
                    }
                })


        }
    }


    private fun progresBarOlustur() {
        progressBarX = ProgressBar(requireContext())
        val params = RelativeLayout.LayoutParams(100, 100)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        progressBarX.layoutParams = params
        layoutAlisVerisListemFragment.addView(progressBarX)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlisVerisListemFragment().apply {}
    }
}