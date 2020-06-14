package com.imalat.beeSystem.view.ara

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.imalat.beeSystem.R
import com.imalat.beeSystem.adapter.AramaKategorilerAdapter
import com.imalat.beeSystem.adapter.AramaUrunlerAdapter
import com.imalat.beeSystem.interfacee.BadgeGuncelleInterface
import com.imalat.beeSystem.interfacee.KategoriyeGoreKAtegoriVeUrunGetir
import com.imalat.beeSystem.interfacee.UrunlerGuncelleInterface
import com.imalat.beeSystem.model.aramaSonucu.Kategoriler
import com.imalat.beeSystem.model.aramaSonucu.Urunler
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.BarkodGetirViewModel
import com.imalat.beeSystem.viewModel.FragmentAnaSayfaViewModel
import com.imalat.beeSystem.viewModel.KategoriDetaylariViewModel
import com.imalat.beeSystem.viewModel.UrunAraFragmentViewModel
import kotlinx.android.synthetic.main.fragment_kategori_detaylari.recycleViewGenelKategoriler
import kotlinx.android.synthetic.main.fragment_kategori_detaylari.recycleViewUrunler
import kotlinx.android.synthetic.main.fragment_kategori_detaylari.tvKategoriDetaylariBaslik
import kotlinx.android.synthetic.main.fragment_kategori_detaylari.urunBulunamadiLayout
import kotlinx.android.synthetic.main.fragment_urun_ara_sonuc.*


class UrunAraSonucFragment : Fragment(R.layout.fragment_urun_ara_sonuc),
    KategoriyeGoreKAtegoriVeUrunGetir, UrunlerGuncelleInterface {

    private lateinit var etArananUrun: EditText

    private lateinit var viewModelFragmentAnaSayfa: FragmentAnaSayfaViewModel

    private lateinit var viewModel: KategoriDetaylariViewModel
    private lateinit var urunAraFragmentViewModel: UrunAraFragmentViewModel
    lateinit var badgeGuncelleInterface: BadgeGuncelleInterface

    private lateinit var barkodGetirViewModel: BarkodGetirViewModel


    protected var RESULT_SPEECH = 1

    private var aramaKategorilerAdapter = AramaKategorilerAdapter(
        arrayListOf(),
        this@UrunAraSonucFragment as KategoriyeGoreKAtegoriVeUrunGetir
    )


    private var aramaUrunlerAdapter =
        AramaUrunlerAdapter(arrayListOf(), this@UrunAraSonucFragment as UrunlerGuncelleInterface)

    private var arananUrun = ""
    private var kategoriID = ""
    private var siralamaTipi = 1
    private var gelenBarkod = ""
    private var gelenPst_KategoriIDAra = "" // slider dan gelen arama isteği
    lateinit var progressBarX: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.badgeGuncelleInterface = context as BadgeGuncelleInterface

        viewModelFragmentAnaSayfa =
            ViewModelProvider(requireActivity()).get(FragmentAnaSayfaViewModel::class.java)
        barkodGetirViewModel =
            ViewModelProvider(requireActivity()).get(BarkodGetirViewModel::class.java)


        urunAraFragmentViewModel = ViewModelProvider(this).get(UrunAraFragmentViewModel::class.java)
        viewModel = ViewModelProvider(this).get(KategoriDetaylariViewModel::class.java)

        arguments?.let {
            arananUrun = it.getString("urunID").toString()

            gelenPst_KategoriIDAra = it.getString("kategoriID").toString()
            //Log.d("aranan", arananUrun)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progresBarOlustur()

        recycleViewGenelKategoriler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recycleViewGenelKategoriler.adapter = aramaKategorilerAdapter

        recycleViewUrunler.layoutManager = GridLayoutManager(requireContext(), 2)
        //recycleViewUrunler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycleViewUrunler.adapter = aramaUrunlerAdapter

        etArananUrun = view.findViewById(R.id.etArananUrun)



        if (!arananUrun.isNullOrBlank()) {

            //Log.d("arananUrun", arananUrun)

            Handler().postDelayed({

                etArananUrun.setText(arananUrun)

                if (Fonk.isNetworkAvailable(requireContext()))
                    urunleriGetir()

            }, 300)

        } else if (!gelenPst_KategoriIDAra.isNullOrBlank())
            urunleriGetir()



        urunBulunamadiLayout.visibility = View.GONE
        ayracView.visibility = View.GONE

        // etArananUrun.addTextChangedListener(textWatcher)

        etArananUrun.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {

                    true
                }

                EditorInfo.IME_ACTION_SEARCH -> {

                    arananUrun = etArananUrun.text.toString()
                    if (arananUrun.length > 2) {
                        // slider dan gelen kategori id siliniyor, böylece normal arama kısmı düzngün çalışabilecek,
                        // yoksa sliderdan geldikten sonra bir daha arma yapılamıyor
                        //Log.d("yeni_aranan", arananUrun)
                        gelenPst_KategoriIDAra = ""
                        urunleriGetir()
                    }

                    true
                }
                else -> false
            }
        }


        mikrofonlaArama.setOnClickListener {

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US")

            try {
                startActivityForResult(intent, RESULT_SPEECH)
                //txtText.setText("");
            } catch (ex: ActivityNotFoundException) {
                /* Toast t = Toast.makeText(getApplicationContext(),
                         "Opps! Your device doesn't support Speech to Text",
                         Toast.LENGTH_SHORT);
                 t.show();*/
            }

        }

        barkodEkraniAc.setOnClickListener {

            Dexter.withActivity(activity)
                .withPermissions(
                    Manifest.permission.CAMERA
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        startActivity(Intent(requireContext(), BarkodActivity::class.java))
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken
                    ) {
                        /* ... */
                        token.continuePermissionRequest()
                    }
                }).check()


            // startActivity(Intent(requireContext(),BarkodTara::class.java))
        }

        sirala.setOnClickListener {
            showDialog()
        }

        editTemizle.setOnClickListener {
            etArananUrun.setText("")
        }

        geriGit.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            //Log.d("aranan", s.toString())
            //Log.d("aranan", count.toString())

            if (count > 2) {
                // slider dan gelen kategori id siliniyor, böylece normal arama kısmı düzngün çalışabilecek,
                // yoksa sliderdan geldikten sonra bir daha arma yapılamıyor
                gelenPst_KategoriIDAra = ""
                arananUrun = s.toString()
                urunleriGetir()
            }

        }
    }

    private fun urunleriGetir() {

        //Log.d("UrunAraSonuc", "arananUrun:" + arananUrun)
        //Log.d("UrunAraSonuc", "kategoriID:" + kategoriID)
        //Log.d("UrunAraSonuc", "siralamaTipi:" + siralamaTipi.toString())
        //Log.d("UrunAraSonuc", "gelenPst_KategoriIDAra:" + gelenPst_KategoriIDAra.toString())

        var urlx = GlobalDegiskenlerX.g023UrunArama.toString()
        // if (urlx.isNullOrBlank()) urlx = Fonk.kayitGetirSifreli(requireContext(), Enum.g00.toString())
        urunAraFragmentViewModel.urunleriAra(
            urlx,
            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString()),
            arananUrun,
            kategoriID,
            siralamaTipi.toString(),
            gelenPst_KategoriIDAra
        )

        observerLiveDataUrunListesi()
    }


    private fun observerLiveDataUrunListesi() {
        urunAraFragmentViewModel.urunArananListesi.removeObservers(viewLifecycleOwner)
        urunAraFragmentViewModel.ServisError.removeObservers(viewLifecycleOwner)
        urunAraFragmentViewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)

        //Log.d("tag", "observerLiveDataUrunKategoriListesi " + " çalıştı")
        var tetiklenme = 0

        urunAraFragmentViewModel.urunArananListesi.observe(viewLifecycleOwner, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    val listKategoriler: List<Kategoriler> = cevap.aramaSonucuJSON[0].kategoriler
                    val listUrunler: List<Urunler> = cevap.aramaSonucuJSON[0].urunler

                    if (listKategoriler != null && listKategoriler.size > 0) {

                        urunBulunamadiLayout.visibility = View.GONE
                        ayracView.visibility = View.VISIBLE


                        /*for (x in 0 until listKategoriler.size) {
                            //sira bilgisi manuel ekleniyor
                            // list[x].sira = (x+1).toString()
                            //Log.d("tag", "baslik " + listKategoriler[x].kategoriAd)


                            //Log.d("tag", "---- -----------------")
                        }*/
                        aramaKategorilerAdapter.update(listKategoriler)
                    } else {
                        //Log.d("size ", "slider arrray boş")
                        aramaKategorilerAdapter.update(arrayListOf())
                    }


                    if (listUrunler != null && listUrunler.size > 0) {

                        urunBulunamadiLayout.visibility = View.GONE
                        ayracView.visibility = View.VISIBLE

                        for (x in 0 until listUrunler.size) {
                            //sira bilgisi manuel ekleniyor
                            // list[x].sira = (x+1).toString()
                            // //Log.d("tag", "baslik " + listUrunler[x].urunAdi)


                            // //Log.d("tag", "---- -----------------")
                        }
                        aramaUrunlerAdapter.update(listUrunler)
                    } else {
                        //Log.d("size ", "slider arrray boş")
                        aramaUrunlerAdapter.update(arrayListOf())
                    }


                } else {
                    aramaKategorilerAdapter.update(arrayListOf())
                    aramaUrunlerAdapter.update(arrayListOf())
                    //Fonk.alertDialogGoster(requireContext(), "", cevap.message)
                    urunBulunamadiLayout.visibility = View.VISIBLE
                    ayracView.visibility = View.GONE

                    //Toast.makeText(requireContext(),cevap.message,Toast.LENGTH_SHORT).show()
                }

                urunAraFragmentViewModel.urunArananListesi.postValue(null)
            }

        })



        urunAraFragmentViewModel.ServisError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    // tvServisError.visibility = View.VISIBLE
                } else {
                    // tvServisError.visibility = View.GONE
                    tetiklenme++


                }
            }
        })

        urunAraFragmentViewModel.servisYukleniyor.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    progressBarX.visibility = View.VISIBLE
                } else {
                    progressBarX.visibility = View.GONE
                }
                urunAraFragmentViewModel.servisYukleniyor.postValue(null)
            }
        })
    }


    override fun ilkAltKategoriGetir(Pst_KategoriID: String, kategoriAd: String) {
        //  kategorileriGetir("ilk", Pst_KategoriID)
        tvKategoriDetaylariBaslik.text = kategoriAd
        kategoriID = Pst_KategoriID
        urunleriGetir()
        // sepetToplamTutariHesapla()
    }

    override fun ikinciAltKategoriGetir(Pst_KategoriID: String) {
        kategoriID = Pst_KategoriID
        urunleriGetir()
        //  kategorileriGetir("ikinci", Pst_KategoriID)
        //Log.d("tikala", Pst_KategoriID)
        //sepetToplamTutariHesapla()

    }

    override fun ikinciAltKategoriyeGoreUrunGetir(Pst_KategoriID: String) {
        //TODO("Not yet implemented")
    }


    private fun sepetToplamTutariHesapla() {

    }

    override fun guncelle() {
        sepetToplamTutariHesapla()
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


    private fun showDialog() {
        // Late initialize an alert dialog object
        lateinit var dialog: AlertDialog

        val array = arrayOf(
            "Ürün Adı (A->Z)",
            "Ürün Adı (Z->A)",
            "Fiyat Azdan Çoğa",
            "Fiyat Çoktan Aza",
            "İndirim Tutarı Çoktan Aza",
            "İndirim Oranı Çoktan Aza"
        )

        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(requireContext())

        // Set a title for alert dialog
        builder.setTitle("Sıralama kriteri Seçin")


        builder.setSingleChoiceItems(array, -1) { _, which ->

            val color = array[which]

            try {

                siralamaTipi = which + 1
                urunleriGetir()
                //Log.d("which", which.toString())

            } catch (e: IllegalArgumentException) {

            }

            dialog.dismiss()
        }

        dialog = builder.create()
        dialog.show()
    }


    override fun onResume() {
        super.onResume()
        sepetToplamTutariHesapla()
        gelenBarkod = Fonk.degerGetir(requireContext(), EnumX.barkod.toString())
        etArananUrun.setText(gelenBarkod)

        if (!gelenBarkod.isNullOrBlank()) {
            arananUrun = gelenBarkod
            urunleriGetir()
        }

        //Log.d("onresum_arr", "calisti")
        //Log.d("onresum_arr", gelenBarkod)
        Fonk.kayitEkle(requireContext(), EnumX.barkod.toString(), "")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RESULT_SPEECH -> {

                if (resultCode == RESULT_OK && data != null) {
                    val text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

                    for (i in 0..text.size - 1) {
                        //Log.d("konusma", text[i].toString())
                    }

                    Handler().postDelayed({
                        etArananUrun.setText(text[0].toString())
                        arananUrun = text[0].toString()
                        urunleriGetir()
                    }, 1000)
                    // etArananUrun.setText(text[0].toString())
                }
            }

            111 -> {
                if (resultCode == RESULT_OK) {

                    data?.let {

                        val resultAdet = data.getStringExtra("adet")
                        val resultFavori = data.getStringExtra("favori")
                        val resultPosition = data.getStringExtra("position")
                        aramaUrunlerAdapter.updateAdet(
                            resultPosition.toInt(),
                            resultAdet,
                            resultFavori
                        )
                    }

                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    // tvAlinan.setText("Nothing selected");
                }
            }
        }

    }

    private fun progresBarOlustur() {
        progressBarX = ProgressBar(requireContext())
        val params = RelativeLayout.LayoutParams(100, 100)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        progressBarX.layoutParams = params
        layoutUrunAraSonucFragment.addView(progressBarX)
    }

    companion object {
        @JvmStatic
        fun newInstance(gelenParametre: String) = UrunAraSonucFragment()
            .apply {}
    }


}