package com.imalat.beeSystem.view.diger

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.adapter.SepetAdapter
import com.imalat.beeSystem.interfacee.BadgeGuncelleInterface
import com.imalat.beeSystem.interfacee.SepetGuncelleInterface
import com.imalat.beeSystem.model.SepetOnline.SepetJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.view.profil.GirisYap
import com.imalat.beeSystem.view.sepet.siparisOzeti.SiparisOzetiFragment
import com.imalat.beeSystem.viewModel.AnaSayfaViewModel
import com.imalat.beeSystem.viewModel.FragmentAnaSayfaViewModel
import com.imalat.beeSystem.viewModel.GeldiginEkraniKapatViewModel
import com.imalat.beeSystem.viewModel.SepetViewModel
import kotlinx.android.synthetic.main.fragment_sepetim.*


class SepetimFragment : Fragment(R.layout.fragment_sepetim), SepetGuncelleInterface {

    private var sepetAdapter =
        SepetAdapter(arrayListOf(), this@SepetimFragment as SepetGuncelleInterface)
    lateinit var sepetViewModel: SepetViewModel
    private lateinit var fragmentAnaSayfaViewModel: FragmentAnaSayfaViewModel
    lateinit var anaSayfaViewModel: AnaSayfaViewModel

    private lateinit var geldiginEkraniKapatViewModel: GeldiginEkraniKapatViewModel
    lateinit var badgeGuncelleInterface: BadgeGuncelleInterface


    lateinit var bottomNavigationView: BottomNavigationView
    var sepetToplamTutari: Double = 0.0


    private lateinit var progressBarX: ProgressBar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.badgeGuncelleInterface = context as BadgeGuncelleInterface
        //Log.d("SiparisOzetiFragment", "onAttach")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        geldiginEkraniKapatViewModel =
            ViewModelProvider(requireActivity()).get(GeldiginEkraniKapatViewModel::class.java)

        sepetViewModel = ViewModelProvider(this).get(SepetViewModel::class.java)
        fragmentAnaSayfaViewModel =
            ViewModelProvider(this).get(FragmentAnaSayfaViewModel::class.java)
        anaSayfaViewModel = ViewModelProvider(this).get(AnaSayfaViewModel::class.java)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sepetBosMu(true)
        progresBarOlustur()

        bottomNavigationView =
            requireActivity().findViewById<View>(R.id.bottom_navigation) as BottomNavigationView


        recycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = sepetAdapter


        // val lottie = LottieDialogFragment
        //  lottie.newInstance().show(requireActivity().supportFragmentManager, "ad")


        if (Fonk.isNetworkAvailable(requireContext())) {
            sepetiGetirPOST()
        }

        sil.setOnClickListener {

            if (Fonk.isNetworkAvailable(requireContext())) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                builder.setTitle(this.resources.getString(R.string.uyari))
                builder.setMessage("Listeyi Boşaltmak İstiyor musunuz?")
                builder.setNegativeButton(
                    this.resources.getString(R.string.evet),
                    DialogInterface.OnClickListener { dialogInterface, i ->

                        sepetBosaltPOST()

                    })
                builder.setPositiveButton(
                    this.resources.getString(R.string.iptal),
                    DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                builder.show()
            }


        }


        tvIleri.setOnClickListener {

            if (sepetToplamTutari > 0.0) {

                if (Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString())
                        .isNullOrBlank()
                ) {

                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(this.resources.getString(R.string.uyari))
                    builder.setMessage("Üye olun")
                  //  builder.setMessage("Sipariş vermek için hemen ücretsiz üye olup kampanyalardan faydalanbilirsiniz")
                    builder.setNegativeButton(
                        "Ücretsiz Üye Ol",
                        DialogInterface.OnClickListener { dialogInterface, i ->

                            startActivity(Intent(requireContext(), GirisYap::class.java))

                        })
                    builder.setPositiveButton(
                        this.resources.getString(R.string.iptal),
                        DialogInterface.OnClickListener { dialogInterface, i ->

                        })
                    builder.show()

                } else {


                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, SiparisOzetiFragment::class.java, null)
                        .addToBackStack(null)
                        .commit()

                    /* val frg = SiparisOzetiFragment
                     frg.newInstance().show(supportFragmentManager, "ad")*/
                }


            } else {


                //Fonk.alertDialogGoster(this,"","Sepetiniz ")
            }

        }

        tvAlisveriseBasla.setOnClickListener {

            bottomNavigationView.selectedItemId = R.id.action_urunler
        }


        geri.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    fun sepetiGetirPOST() {

        sepetViewModel.sepetGetir(
            GlobalDegiskenlerX.g028SepetGetir,
            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString())
        )


        sepetViewModel.sepetListesi.removeObservers(viewLifecycleOwner)
        sepetViewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)
        sepetViewModel.ServisError.removeObservers(viewLifecycleOwner)

        sepetViewModel.sepetListesi.observe(viewLifecycleOwner, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    val list: List<SepetJSON> = cevap.sepetJSON

                    if (list != null && list.size > 0) {
                        sepetAdapter.update(list)
                        sepetBosMu(false)
                        sepetToplamTutari = cevap.sepettoplam


                        tvUrunSayisi.setText("Sepetinizde ${cevap.urunsayi} adet ürün var ")
                        tvSepetToplamTutar.setText("${cevap.sepettoplam} TL ")

                    } else {
                        sepetAdapter.update(arrayListOf())
                        sepetBosMu(true)
                    }

                } else {
                    sepetAdapter.update(arrayListOf())
                    sepetBosMu(true)
                    sepetToplamTutari = 0.0
                }
            }

            sepetViewModel.sepetListesi.postValue(null)
        })



        sepetViewModel.servisYukleniyor.observe(viewLifecycleOwner, Observer { cevap ->
            val let = cevap?.let {
                if (it) {
                    //Log.d("sepetimfRag", "yükleniyor")
                    progressBarX.visibility = View.VISIBLE

                } else {
                    //Log.d("sepetimfRag", "bitti")
                    progressBarX.visibility = View.GONE
                }
                sepetViewModel.servisYukleniyor.postValue(null)
            }
        })


    }

    private fun sepetBosMu(bosMu: Boolean) {
        if (bosMu) {
            sepetBosLayout.visibility = View.VISIBLE
            sil.visibility = View.INVISIBLE
            cardALt.visibility = View.INVISIBLE
        } else {
            sepetBosLayout.visibility = View.INVISIBLE
            sil.visibility = View.VISIBLE
            cardALt.visibility = View.VISIBLE

        }
    }

    override fun onResume() {
        super.onResume()
        sepetiGetirPOST()

        geldiginEkraniKapatViewModel.getAdet()?.observe(viewLifecycleOwner, Observer { cevap ->

            cevap?.let {

                //Log.d("KapatViewModel_1", it)
                if (it.toString().equals("kapat") || cevap.toString()
                        .equals("kapat") || cevap == "kapat"
                ) {

                    //kapat view model boşaltıldı
                    geldiginEkraniKapatViewModel.setUrunAdet("")
                    //Log.d("niKapatViewModel_2", "kapatılacak")

                    Handler().postDelayed({
                        bottomNavigationView.selectedItemId = R.id.action_anasayfa

                    }, 500)


                } else {
                    //Log.d("KapatViewModel_3", "normal geri")

                }
            }

        })


    }

    override fun guncelle() {
        //sepetiGetirPOST()
    }

    override fun sepeteEkleCikart(Pst_UrunID: String, miktar: String) {
        if (Fonk.isNetworkAvailable(requireContext()))
            sepeteEkleCikartPOST(Pst_UrunID, miktar)
    }


    private fun sepeteEkleCikartPOST(Pst_UrunID: String, miktar: String) {

        fragmentAnaSayfaViewModel.sepetEkleCikart(
            GlobalDegiskenlerX.g027SepetGuncelle,
            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString()),
            "EkleCikart",
            miktar,
            Pst_UrunID
        )


        fragmentAnaSayfaViewModel.sepetEkleCikart.removeObservers(this)
        fragmentAnaSayfaViewModel.ServisError.removeObservers(this)
        fragmentAnaSayfaViewModel.servisYukleniyor.removeObservers(this)

        fragmentAnaSayfaViewModel.sepetEkleCikart.observe(
            this,
            Observer { cevap ->
                cevap?.let {

                    sepetiGetirPOST()
                    badgeGuncelleInterface.badgeGuncelle("")
                    // sepetToplamTutariGetirPOST()

                    /* if (cevap.success == 1) {
                         Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()
                     } else
                         Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()*/

                    // badgeGuncelleInterface.badgeGuncelle("")
                }

                fragmentAnaSayfaViewModel.sepetEkleCikart.postValue(null)

            })
    }


    private fun sepetBosaltPOST() {

        fragmentAnaSayfaViewModel.sepetEkleCikart(
            GlobalDegiskenlerX.g027SepetGuncelle,
            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString()),
            "Bosalt",
            "",
            ""
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
                        sepetAdapter.update(arrayListOf())
                        sepetBosMu(true)
                        badgeGuncelleInterface.badgeGuncelle("")

                    } else
                        Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()


                    // badgeGuncelleInterface.badgeGuncelle("")
                }

                fragmentAnaSayfaViewModel.sepetEkleCikart.postValue(null)

            })
    }


    private fun progresBarOlustur() {
        progressBarX = ProgressBar(requireContext())
        val params = RelativeLayout.LayoutParams(100, 100)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        progressBarX.layoutParams = params
        layoutHepsi.addView(progressBarX)
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SepetimFragment().apply {
            }
    }
}