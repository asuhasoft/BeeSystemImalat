package com.imalat.beeSystem.view.kategoriler

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.imalat.beeSystem.R
import com.imalat.beeSystem.adapter.UrunlerAdapter
import com.imalat.beeSystem.adapter.kategoriDetaylari.AltKategorilerAdapter
import com.imalat.beeSystem.adapter.kategoriDetaylari.IkinciAltKategorilerAdapter
import com.imalat.beeSystem.adapter.kategoriDetaylari.KategoriDetayKategoriAdapter
import com.imalat.beeSystem.interfacee.BadgeGuncelleInterface
import com.imalat.beeSystem.interfacee.KategoriyeGoreKAtegoriVeUrunGetir
import com.imalat.beeSystem.interfacee.UrunlerGuncelleInterface
import com.imalat.beeSystem.model.urunKategorileri.UrunKategoriJSON
import com.imalat.beeSystem.model.urunler.UrunJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.view.ara.UrunAraAnaSayfaFragment
import com.imalat.beeSystem.viewModel.FragmentAnaSayfaViewModel
import com.imalat.beeSystem.viewModel.FragmentKategorilerViewModel
import com.imalat.beeSystem.viewModel.KategoriDetaylariViewModel
import com.imalat.beeSystem.viewModel.KategoriIDGonderViewModel
import kotlinx.android.synthetic.main.fragment_kategori_detaylari.*


class KategoriDetaylariFragment : Fragment(R.layout.fragment_kategori_detaylari),
    KategoriyeGoreKAtegoriVeUrunGetir, UrunlerGuncelleInterface {

    private var siralamaTipi: Int = 1
    private lateinit var viewModel: KategoriDetaylariViewModel
    private lateinit var viewModelKategoriler: FragmentKategorilerViewModel
    private lateinit var kategoriIDGonderViewModel: KategoriIDGonderViewModel
    private lateinit var viewModelFragmentAnaSayfa: FragmentAnaSayfaViewModel

    lateinit var badgeGuncelleInterface: BadgeGuncelleInterface

    private var kategorilerAdapter =
        KategoriDetayKategoriAdapter(
            arrayListOf(),
            this@KategoriDetaylariFragment as KategoriyeGoreKAtegoriVeUrunGetir
        )


    private var altKategorilerAdapter =
        AltKategorilerAdapter(
            arrayListOf(),
            this@KategoriDetaylariFragment as KategoriyeGoreKAtegoriVeUrunGetir
        )

    private var ikinciAltKategorilerAdapter =
        IkinciAltKategorilerAdapter(
            arrayListOf(),
            this@KategoriDetaylariFragment as KategoriyeGoreKAtegoriVeUrunGetir
        )

    private var urunlerAdapter =
        UrunlerAdapter(arrayListOf(), this@KategoriDetaylariFragment as UrunlerGuncelleInterface)
    private var gelenKategoriID = ""

    lateinit var progressBarX: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.badgeGuncelleInterface = context as BadgeGuncelleInterface
        kategoriIDGonderViewModel =
            ViewModelProvider(requireActivity()).get(KategoriIDGonderViewModel::class.java)
        viewModelFragmentAnaSayfa =
            ViewModelProvider(requireActivity()).get(com.imalat.beeSystem.viewModel.FragmentAnaSayfaViewModel::class.java)

        //  setContentView(R.layout.fragment_kategori_detaylari)
        // overridePendingTransition(R.anim.saga_gidis1, R.anim.saga_gidis2)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        urunBulunamadiLayout.visibility = View.GONE
        progresBarOlustur()



        viewModel = ViewModelProvider(requireActivity()).get(KategoriDetaylariViewModel::class.java)
        viewModelKategoriler =
            ViewModelProvider(requireActivity()).get(FragmentKategorilerViewModel::class.java)


        recycleViewGenelKategoriler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recycleViewGenelKategoriler.adapter = kategorilerAdapter


        recycleViewIlkKategori.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recycleViewIlkKategori.adapter = altKategorilerAdapter


        recycleViewIkinciKategori.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recycleViewIkinciKategori.adapter = ikinciAltKategorilerAdapter


        recycleViewUrunler.layoutManager = GridLayoutManager(requireContext(), 2)
        //recycleViewUrunler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycleViewUrunler.adapter = urunlerAdapter


        /*
                  gelenKategoriID = bundle.getString("kategoriID").toString()
      
                  tvKategoriDetaylariBaslik.setText(bundle.getString("kategoriAd").toString())
      */
        if (Fonk.isNetworkAvailable(requireContext())) {

            kategoriIDGonderViewModel.getKategoriID()!!.observe(viewLifecycleOwner, Observer() {
                Log.d("gelen_KategoriID", it.toString())
                gelenKategoriID = it.toString()

                Log.d("gelen_KategoriIDX", gelenKategoriID)
                urunleriGetir(gelenKategoriID)
                kategorileriGetir("genel", "")
                //gelenKategoriID = "3"
            })

        }


        aramaSayfasiniAc.setOnClickListener {

            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, UrunAraAnaSayfaFragment::class.java, null)
                .addToBackStack(null)
                .commit()

        }



        sirala.setOnClickListener {

            showDialog()
        }
    }

    private fun urunleriGetir(kategoriID: String) {

        var urlx = GlobalDegiskenlerX.g003URUrunList.toString()
        // if (urlx.isNullOrBlank()) urlx = Fonk.kayitGetirSifreli(this, Enum.g00.toString())
        viewModel.urunListesiniGetir(
            urlx,
            kategoriID,
            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString()),
            siralamaTipi.toString()
        )
        observerLiveDataUrunListesi()
    }

    private fun kategorileriGetir(hangiKategori: String, kategoriID: String) {

        var url2x = GlobalDegiskenlerX.g002URKategoriList.toString()
        // if (urlx.isNullOrBlank()) urlx = Fonk.kayitGetirSifreli(this, Enum.g00.toString())
        viewModelKategoriler.kategoriListesiGetir(
            url2x,
            kategoriID,
            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString())
        )

        if (hangiKategori.equals("genel"))
            observerLiveDataKategorilerGenel(hangiKategori)
        else
            observerLiveDataKategoriler(hangiKategori)
    }


    private fun observerLiveDataUrunListesi() {
        viewModel.urunListesi.removeObservers(viewLifecycleOwner)
        viewModel.ServisError.removeObservers(viewLifecycleOwner)
        viewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)

        Log.d("tag", "observerLiveDataUrunKategoriListesi " + " çalıştı")
        var tetiklenme = 0

        viewModel.urunListesi.observe(viewLifecycleOwner, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    val list: List<UrunJSON> = cevap.urunJSON

                    if (list != null && list.size > 0) {

                        urunBulunamadiLayout.visibility = View.GONE

                        for (x in 0 until list.size) {

                            /* val db = DatabaseUrunler(requireContext())
                             list[x].sepettekiAdet =
                                 (db.sepetteKacAdetVar(Fonk.inteCevir(list[x].urunID)))
                             db.close()*/

                        }
                        urunlerAdapter.update(list)
                    } else {
                        Log.d("size ", "slider arrray boş")
                        urunlerAdapter.update(arrayListOf())
                    }


                } else {
                    urunlerAdapter.update(arrayListOf())
                    //Fonk.alertDialogGoster(this, "", cevap.message)
                    urunBulunamadiLayout.visibility = View.VISIBLE

                    //Toast.makeText(requireContext(),cevap.message,Toast.LENGTH_SHORT).show()
                }

                viewModel.urunListesi.postValue(null)
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
            }
        })

        viewModel.servisYukleniyor.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    progressBarX.visibility = View.VISIBLE
                } else {
                    progressBarX.visibility = View.GONE
                }
                viewModel.servisYukleniyor.postValue(null)

            }
        })
    }


    private fun observerLiveDataKategorilerGenel(hangiAltKategoriyeEklenecek: String) {

        viewModelKategoriler.urunKategoriListesi.removeObservers(viewLifecycleOwner)
        viewModelKategoriler.ServisError.removeObservers(viewLifecycleOwner)
        viewModelKategoriler.servisYukleniyor.removeObservers(viewLifecycleOwner)

        Log.d("tag", "observerLiveDataUrunKategoriListesi " + hangiAltKategoriyeEklenecek)
        var tetiklenme = 0

        viewModelKategoriler.urunKategoriListesi.observe(viewLifecycleOwner, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    val list: List<UrunKategoriJSON> = cevap.urunKategoriJSON

                    if (list != null && list.size > 0) {

                        /* for (x in 0 until list.size) {
                             //sira bilgisi manuel ekleniyor
                             // list[x].sira = (x+1).toString()
                             Log.d("tag", "baslik " + list[x].kategoriAd)

                             Log.d("tag", "---- -----------------")
                         }*/

                        kategorilerAdapter.update(list)

                    } else {
                        Log.d("size ", "slider arrray boş")

                        kategorilerAdapter.update(arrayListOf())
                    }


                } else {

                    Log.d("altkategoriler", "sifirlandi")

                    kategorilerAdapter.update(arrayListOf())
                    Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()

                    //Fonk.alertDialogGoster(this, "", cevap.message)
                }


                kategorileriGetir("ilk", gelenKategoriID)

                viewModelKategoriler.urunKategoriListesi.postValue(null)
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
            }
        })

        viewModel.servisYukleniyor.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    //  progresServisYukleniyor.visibility = View.VISIBLE
                    // countryList.visibility = View.GONE
                    //tvServisError.visibility = View.GONE
                } else {
                    // progresServisYukleniyor.visibility = View.GONE

                }
            }
        })
    }

    private fun observerLiveDataKategoriler(hangiAltKategoriyeEklenecek: String) {
        viewModelKategoriler.urunKategoriListesi.removeObservers(viewLifecycleOwner)
        viewModelKategoriler.ServisError.removeObservers(viewLifecycleOwner)
        viewModelKategoriler.servisYukleniyor.removeObservers(viewLifecycleOwner)

        Log.d("tag", "observerLiveDataUrunKategoriListesi " + hangiAltKategoriyeEklenecek)
        var tetiklenme = 0

        viewModelKategoriler.urunKategoriListesi.observe(viewLifecycleOwner, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    val list: List<UrunKategoriJSON> = cevap.urunKategoriJSON

                    if (list != null && list.size > 0) {

                        /* for (x in 0 until list.size) {
                             //sira bilgisi manuel ekleniyor
                             // list[x].sira = (x+1).toString()
                             Log.d("tag", "baslik " + list[x].kategoriAd)

                             Log.d("tag", "---- -----------------")
                         }*/

                        if (hangiAltKategoriyeEklenecek.equals("ilk"))
                            altKategorilerAdapter.update(list)
                        else if (hangiAltKategoriyeEklenecek.equals("ikinci"))
                            ikinciAltKategorilerAdapter.update(list)


                    } else {
                        Log.d("size ", "slider arrray boş")
                        if (hangiAltKategoriyeEklenecek.equals("ilk"))
                            altKategorilerAdapter.update(arrayListOf())
                        else if (hangiAltKategoriyeEklenecek.equals("ikinci"))
                            ikinciAltKategorilerAdapter.update(arrayListOf())

                    }


                } else {

                    Log.d("altkategoriler", "sifirlandi")

                    if (hangiAltKategoriyeEklenecek.equals("ilk"))
                        altKategorilerAdapter.update(arrayListOf())
                    else if (hangiAltKategoriyeEklenecek.equals("ikinci"))
                        ikinciAltKategorilerAdapter.update(arrayListOf())

                    Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()

                    //Fonk.alertDialogGoster(this, "", cevap.message)
                }



                viewModelKategoriler.urunKategoriListesi.postValue(null)
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
            }
        })

        viewModel.servisYukleniyor.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    //  progresServisYukleniyor.visibility = View.VISIBLE
                    // countryList.visibility = View.GONE
                    //tvServisError.visibility = View.GONE
                } else {
                    // progresServisYukleniyor.visibility = View.GONE

                }
            }
        })
    }


    override fun ilkAltKategoriGetir(Pst_KategoriID: String, kategoriAd: String) {
        kategorileriGetir("ilk", Pst_KategoriID)
        tvKategoriDetaylariBaslik.setText(kategoriAd)
        ikinciAltKategorilerAdapter.update(arrayListOf())
        gelenKategoriID = Pst_KategoriID
        urunleriGetir(Pst_KategoriID)

    }

    override fun ikinciAltKategoriGetir(Pst_KategoriID: String) {
        gelenKategoriID = Pst_KategoriID
        urunleriGetir(Pst_KategoriID)
        kategorileriGetir("ikinci", Pst_KategoriID)
        Log.d("tikala", Pst_KategoriID)


    }

    override fun ikinciAltKategoriyeGoreUrunGetir(Pst_KategoriID: String) {
        gelenKategoriID = Pst_KategoriID
        urunleriGetir(Pst_KategoriID)

    }


    override fun guncelle() {

        badgeGuncelleInterface.badgeGuncelle("")

    }

    override fun alisVerisListesineAl(Pst_UrunID: String) {
        alisverisListemeEkleCikartPOST(Pst_UrunID)
    }

    override fun sepeteEkleCikart(Pst_UrunID: String, miktar: String) {
        sepeteEkleCikartPOST(Pst_UrunID, miktar)
    }

    private fun alisverisListemeEkleCikartPOST(Pst_UrunID: String) {

        viewModel.alisVerisListesineEkleCikar(
            GlobalDegiskenlerX.g014MUAlisverisListeEdit,
            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString()),
            Pst_UrunID
        )


        viewModel.alisVerisListeEdit.removeObservers(viewLifecycleOwner)
        viewModel.ServisError.removeObservers(viewLifecycleOwner)
        viewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)

        viewModel.alisVerisListeEdit.observe(viewLifecycleOwner, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {
                    Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()
            }

            viewModel.alisVerisListeEdit.postValue(null)

        })


    }

    private fun sepeteEkleCikartPOST(Pst_UrunID: String, miktar: String) {

        viewModelFragmentAnaSayfa.sepetEkleCikart(
            GlobalDegiskenlerX.g027SepetGuncelle,
            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString()),
            "EkleCikart",
            miktar,
            Pst_UrunID
        )


        viewModelFragmentAnaSayfa.sepetEkleCikart.removeObservers(viewLifecycleOwner)
        viewModelFragmentAnaSayfa.ServisError.removeObservers(viewLifecycleOwner)
        viewModelFragmentAnaSayfa.servisYukleniyor.removeObservers(viewLifecycleOwner)

        viewModelFragmentAnaSayfa.sepetEkleCikart.observe(
            viewLifecycleOwner,
            Observer { cevap ->
                cevap?.let {

                    /* if (cevap.success == 1) {
                         Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()
                     } else
                         Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()*/

                    badgeGuncelleInterface.badgeGuncelle("")
                }

                viewModelFragmentAnaSayfa.sepetEkleCikart.postValue(null)

            })
    }

    override fun onResume() {
        super.onResume()

    }

    private fun showDialog() {
        // Late initialize an alert dialog object
        lateinit var dialog: AlertDialog

     /*   val array = arrayOf(
            "Ürün Adı (A->Z)",
            "Ürün Adı (Z->A)",
            "Fiyat Azdan Çoğa",
            "Fiyat Çoktan Aza",
            "İndirim Tutarı Çoktan Aza",
            "İndirim Oranı Çoktan Aza"
        )  */

        val array = arrayOf(
            "Ürün Adı (A->Z)",
            "Ürün Adı (Z->A)",
            "Fiyat Azdan Çoğa",
            "Fiyat Çoktan Aza"
        )

        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(requireContext())

        // Set a title for alert dialog
        builder.setTitle("Sıralama kriteri Seçin")


        builder.setSingleChoiceItems(array, -1) { _, which ->

            val color = array[which]

            try {

                siralamaTipi = which + 1
                urunleriGetir(gelenKategoriID)
                Log.d("which", which.toString())

            } catch (e: IllegalArgumentException) {

            }

            dialog.dismiss()
        }

        dialog = builder.create()
        dialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        Log.e("ssss_KategoriDetay", requestCode.toString())

        if (requestCode == 111) {
            if (resultCode == Activity.RESULT_OK) {

                data?.let {

                    val resultAdet = data.getStringExtra("adet")
                    val resultFavori = data.getStringExtra("favori")
                    val resultPosition = data.getStringExtra("position")
                    urunlerAdapter.updateAdet(resultPosition.toInt(), resultAdet, resultFavori)
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
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
        layoutKategoriDetaylariFragment.addView(progressBarX)
    }

}