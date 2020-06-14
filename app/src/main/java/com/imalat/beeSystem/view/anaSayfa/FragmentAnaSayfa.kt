package com.imalat.beeSystem.view.anaSayfa

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.adapter.UrunlerVitrinAdapter
import com.imalat.beeSystem.adapter.anaSayfaAdapter.SliderAdapter
import com.imalat.beeSystem.interfacee.BadgeGuncelleInterface
import com.imalat.beeSystem.interfacee.UrunlerGuncelleInterface
import com.imalat.beeSystem.model.anaSayfa.Slider
import com.imalat.beeSystem.model.anaSayfa.UrunVitrin
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.view.bildirim.BildirimFragment
import com.imalat.beeSystem.view.profil.GirisYap
import com.imalat.beeSystem.view.profil.Profilim
import com.imalat.beeSystem.viewModel.FragmentAnaSayfaViewModel
import com.imalat.beeSystem.viewModel.KategoriDetaylariViewModel
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_ana_sayfa.*
import kotlinx.android.synthetic.main.include_arama_edittext.*


class FragmentAnaSayfa : Fragment(R.layout.fragment_ana_sayfa), UrunlerGuncelleInterface {

    lateinit var badgeGuncelleInterface: BadgeGuncelleInterface
    private lateinit var fragmentAnaSayfaViewModel: FragmentAnaSayfaViewModel
    private lateinit var viewModelKategoriDetaylari: KategoriDetaylariViewModel

    lateinit var bottomNavigationView: BottomNavigationView

    private var sliderAdapter = SliderAdapter(arrayListOf())
    private var urunlerVitrinAdapter =
        UrunlerVitrinAdapter(arrayListOf(), this@FragmentAnaSayfa as UrunlerGuncelleInterface)

    lateinit var progressBarX: ProgressBar

    val tagg = "FragmentAnaSayfa"
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(tagg, "onAttach")
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        Log.d(tagg, "onAttachFragment")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.badgeGuncelleInterface = context as BadgeGuncelleInterface

        Log.d(tagg, "onCreate")
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progresBarOlustur()

        etArananUrun.isFocusable = false

        fragmentAnaSayfaViewModel =
            ViewModelProvider(this@FragmentAnaSayfa).get(FragmentAnaSayfaViewModel::class.java)

        viewModelKategoriDetaylari =
            ViewModelProvider(this@FragmentAnaSayfa).get(KategoriDetaylariViewModel::class.java)


        bottomNavigationView =
            requireActivity().findViewById<View>(R.id.bottom_navigation) as BottomNavigationView


        imageSlider.sliderAdapter = sliderAdapter
        imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        imageSlider.autoCycleDirection = SliderView.FOCUS_RIGHT
        imageSlider.scrollTimeInSec = 4


        recycleViewAUrunler.layoutManager = GridLayoutManager(requireContext(), 2)
        //recycleView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycleViewAUrunler.adapter = urunlerVitrinAdapter


        if (Fonk.isNetworkAvailable(requireContext())) {

            var urlx = GlobalDegiskenlerX.g004AnaMenuIcerik.toString()
            //if (urlx.isNullOrBlank()) urlx = Fonk.kayitGetirSifreli(requireContext(), Enum.g023AnaMenuIcerik.toString())

            fragmentAnaSayfaViewModel.anaSayfaVerileriGetir(
                urlx,
                Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString())
            )

            observerLiveDataAnaMenu()
        }

        profil.setOnClickListener {

            if (Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString()).isNullOrBlank()) {
                startActivity(Intent(requireContext(), GirisYap::class.java))
            } else {
                startActivity(Intent(requireContext(), Profilim::class.java))
            }
        }


        bildirimler.setOnClickListener {

            if (Fonk.isNetworkAvailable(requireContext()))
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, BildirimFragment::class.java, null)
                    .addToBackStack(null)
                    .commit()
        }


        aramaLayout.setOnClickListener {
            bottomNavigationView.selectedItemId = R.id.action_ara
        }

        etArananUrun.setOnClickListener {
            bottomNavigationView.selectedItemId = R.id.action_ara
        }


    }


    private fun observerLiveDataAnaMenu() {
        fragmentAnaSayfaViewModel.anaMenuListesi.removeObservers(viewLifecycleOwner)
        fragmentAnaSayfaViewModel.ServisError.removeObservers(viewLifecycleOwner)
        fragmentAnaSayfaViewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)

        Log.d("tag", "observerLiveDataUrunKategoriListesi " + " çalıştı")
        var tetiklenme = 0

        fragmentAnaSayfaViewModel.anaMenuListesi.observe(
            viewLifecycleOwner,
            Observer { AnaMenuCevap ->
                AnaMenuCevap?.let {

                    if (AnaMenuCevap.success == 1) {

                        val listSlider: List<Slider> = it.anaMenuJSON[0].slider
                        val listUrunVitrin: List<UrunVitrin> = it.anaMenuJSON[0].urunVitrin
                        //  val listMenuVitrin: List<com.biyons.doner.by_mika.mvvm.model.anaSayfa.MenuVitrin> = it.anaMenuJSON[0].menuVitrin

                        Log.d("size ", "sizee " + listSlider.size)

                        if (listSlider != null && listSlider.size > 0) {

                            /*  for (x in 0 until listSlider.size) {
                                  //sira bilgisi manuel ekleniyor
                              }*/
                            sliderAdapter.update(listSlider)
                        } else {
                            Log.d("size ", "slider arrray boş")

                        }


                        if (listUrunVitrin != null && listUrunVitrin.size > 0) {

                            for (x in 0 until listUrunVitrin.size) {
                                //sira bilgisi manuel ekleniyor
                                // list[x].sira = (x+1).toString()
                                // Log.d("tag", "baslik " + listUrunVitrin[x].urunAdi)


                                Log.d("tag", "---- -----------------")
                            }
                            urunlerVitrinAdapter.update(listUrunVitrin)
                        } else {
                            Log.d("size ", "slider arrray boş")
                            urunlerVitrinAdapter.update(arrayListOf())
                        }

                    } else {

                        Fonk.alertDialogGoster(requireContext(), "", AnaMenuCevap.message)
                    }

                    fragmentAnaSayfaViewModel.anaMenuListesi.postValue(null)
                }

            })


        fragmentAnaSayfaViewModel.ServisError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    // tvServisError.visibility = View.VISIBLE
                } else {
                    // tvServisError.visibility = View.GONE
                    tetiklenme++
                }

                fragmentAnaSayfaViewModel.ServisError.postValue(null)
            }
        })



        fragmentAnaSayfaViewModel.servisYukleniyor.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    progressBarX.visibility = View.VISIBLE
                } else {
                    progressBarX.visibility = View.GONE
                }

                fragmentAnaSayfaViewModel.servisYukleniyor.postValue(null)
            }
        })
    }


    override fun guncelle() {
    }

    override fun onResume() {
        super.onResume()
        //eğer burda yapmazsak sepetten geri geldiğimizde sepet sıfırlanırsa güncellenmiyor
        badgeGuncelleInterface.badgeGuncelle("")
    }

    override fun alisVerisListesineAl(Pst_UrunID: String) {
        alisverisListemeEkleCikartPOST(Pst_UrunID)
    }

    override fun sepeteEkleCikart(Pst_UrunID: String, miktar: String) {
        sepeteEkleCikartPOST(Pst_UrunID, miktar)
    }


    private fun alisverisListemeEkleCikartPOST(Pst_UrunID: String) {

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
                    } else
                        Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()
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

                    /* if (cevap.success == 1) {
                         Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()
                     } else
                         Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()*/

                    badgeGuncelleInterface.badgeGuncelle("")
                }

                fragmentAnaSayfaViewModel.sepetEkleCikart.postValue(null)

            })


        fragmentAnaSayfaViewModel.servisYukleniyor.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    progressBarX.visibility = View.VISIBLE
                } else {
                    progressBarX.visibility = View.GONE
                }

                fragmentAnaSayfaViewModel.servisYukleniyor.postValue(null)
            }
        })


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111) {
            if (resultCode == RESULT_OK) {

                data?.let {

                    val resultAdet = data.getStringExtra("adet")
                    val resultFavori = data.getStringExtra("favori")
                    val resultPosition = data.getStringExtra("position")
                    urunlerVitrinAdapter.updateAdet(
                        resultPosition.toInt(),
                        resultAdet,
                        resultFavori
                    )
                }

            }
            if (resultCode == RESULT_CANCELED) {
                // tvAlinan.setText("Nothing selected");
            }
        } else {
            //tvAlinan.setText(" hataaa ");
        }
    }

    private fun progresBarOlustur() {
        progressBarX = ProgressBar(requireContext())
        val params = RelativeLayout.LayoutParams(100, 100)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        progressBarX.layoutParams = params
        layoutFragmentAnaSayfa.addView(progressBarX)
    }

}