package com.imalat.beeSystem.view.ara

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.imalat.beeSystem.R
import com.imalat.beeSystem.adapter.AramaGecmisiAdapter
import com.imalat.beeSystem.interfacee.AramaGecmisiSilInterface
import com.imalat.beeSystem.model.aramaGecmisi.AramaGecmisJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.BarkodGetirViewModel
import com.imalat.beeSystem.viewModel.UrunAraAnaSayfaFragmentViewModel
import kotlinx.android.synthetic.main.fragment_urun_ara_ana_sayfa.*
import kotlinx.android.synthetic.main.fragment_urun_ara_ana_sayfa.editTemizle
import kotlinx.android.synthetic.main.fragment_urun_ara_sonuc.*


class UrunAraAnaSayfaFragment : Fragment(R.layout.fragment_urun_ara_ana_sayfa),
    AramaGecmisiSilInterface {

    private var aramaGecmisiAdapter =
        AramaGecmisiAdapter(arrayListOf(), this@UrunAraAnaSayfaFragment as AramaGecmisiSilInterface)

    private lateinit var urunAraAnaSayfaFragmentViewModel: UrunAraAnaSayfaFragmentViewModel
    private lateinit var barkodGetirViewModel: BarkodGetirViewModel


    private lateinit var etArananUrun: EditText
    protected var RESULT_SPEECH = 1

    private var arananUrun = ""
    private var kategoriID = ""
    private var siralamaTipi = 1
    private var gelenBarkod = ""
    private var gelenPst_KategoriIDAra = "" // slider dan gelen arama isteği
    lateinit var progressBarX: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        urunAraAnaSayfaFragmentViewModel =
            ViewModelProvider(this@UrunAraAnaSayfaFragment).get(UrunAraAnaSayfaFragmentViewModel::class.java)
        barkodGetirViewModel =
            ViewModelProvider(requireActivity()).get(BarkodGetirViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etArananUrun = view.findViewById(R.id.etArananUrun)
        etArananUrun.showKeyboard()

        progresBarOlustur()


        //  recycleViewAramaGecmisi.layoutManager = GridLayoutManager(requireContext(), 3)

        val layoutManager = FlexboxLayoutManager(requireContext())
        layoutManager.flexDirection = FlexDirection.ROW
        //layoutManager.justifyContent = JustifyContent.FLEX_START
        layoutManager.justifyContent = JustifyContent.CENTER
        recycleViewAramaGecmisi.layoutManager = layoutManager

        recycleViewAramaGecmisi.adapter = aramaGecmisiAdapter

        if (Fonk.isNetworkAvailable(requireContext()))
            aramaGecmisiGetirPOST()


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
                        gelenPst_KategoriIDAra = ""
                        aramaSonucEkraniniAc()

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


        editTemizle.setOnClickListener {
            etArananUrun.setText("")
        }


        tvGecmisiTemizle.setOnClickListener {

            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle(this.resources.getString(R.string.uyari))
            builder.setMessage("Arama geçmişiniz temizlenecek onaylıyor musunuz?")
            builder.setPositiveButton(
                "Evet",
                DialogInterface.OnClickListener { dialogInterface, i ->

                    if (Fonk.isNetworkAvailable(requireContext()))
                        gecmisiTemizlePOST("")

                })
            builder.setNegativeButton(
                "Vazgeç",
                DialogInterface.OnClickListener { dialogInterface, i ->

                })
            builder.show()


        }

    }


    private fun gecmisiTemizlePOST(silinecek: String) {
        urunAraAnaSayfaFragmentViewModel.aramaGecmisiSil(
            GlobalDegiskenlerX.g032AramaGecmisSil.toString(),
            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString()),
            silinecek
        )


        urunAraAnaSayfaFragmentViewModel.aramaGecmisiSil.removeObservers(viewLifecycleOwner)
        urunAraAnaSayfaFragmentViewModel.ServisError.removeObservers(viewLifecycleOwner)
        urunAraAnaSayfaFragmentViewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)

        Log.d("tag", "observerLiveDataUrunKategoriListesi " + " çalıştı")
        var tetiklenme = 0

        urunAraAnaSayfaFragmentViewModel.aramaGecmisiSil.observe(
            viewLifecycleOwner,
            Observer { cevap ->
                cevap?.let {

                    if (cevap.success == 1) {
                        aramaGecmisiGetirPOST()
                    } else {

                    }

                    urunAraAnaSayfaFragmentViewModel.aramaGecmisiSil.postValue(null)
                }

            })

    }


    private fun aramaGecmisiGetirPOST() {

        urunAraAnaSayfaFragmentViewModel.aramaGecmisiGetir(
            GlobalDegiskenlerX.g031AramaGecmisGetir.toString(),
            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString())
        )

        observerLiveDataUrunListesi()
    }


    private fun observerLiveDataUrunListesi() {
        urunAraAnaSayfaFragmentViewModel.aramaGecmisiListesi.removeObservers(viewLifecycleOwner)
        urunAraAnaSayfaFragmentViewModel.ServisError.removeObservers(viewLifecycleOwner)
        urunAraAnaSayfaFragmentViewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)

        Log.d("tag", "observerLiveDataUrunKategoriListesi " + " çalıştı")
        var tetiklenme = 0

        urunAraAnaSayfaFragmentViewModel.aramaGecmisiListesi.observe(
            viewLifecycleOwner,
            Observer { cevap ->
                cevap?.let {

                    if (cevap.success == 1) {

                        val list: List<AramaGecmisJSON> = cevap.aramaGecmisJSON

                        if (list != null && list.size > 0) {

                            aramaGecmisiAdapter.update(list)
                        } else {
                            Log.d("size ", "slider arrray boş")
                            aramaGecmisiAdapter.update(arrayListOf())
                        }

                    } else {
                        aramaGecmisiAdapter.update(arrayListOf())
                        //Toast.makeText(requireContext(),cevap.message,Toast.LENGTH_SHORT).show()
                    }

                    urunAraAnaSayfaFragmentViewModel.aramaGecmisiListesi.postValue(null)
                }

            })



        urunAraAnaSayfaFragmentViewModel.ServisError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    // tvServisError.visibility = View.VISIBLE
                } else {
                    // tvServisError.visibility = View.GONE
                }

                urunAraAnaSayfaFragmentViewModel.ServisError.postValue(null)

            }
        })

        urunAraAnaSayfaFragmentViewModel.servisYukleniyor.observe(
            viewLifecycleOwner,
            Observer { loading ->
                loading?.let {
                    if (it) {
                        progressBarX.visibility = View.VISIBLE
                    } else {
                        progressBarX.visibility = View.GONE
                    }
                    urunAraAnaSayfaFragmentViewModel.servisYukleniyor.postValue(null)

                }
            })
    }


    override fun onResume() {
        super.onResume()

        gelenBarkod = Fonk.degerGetir(requireContext(), EnumX.barkod.toString())


        if (!gelenBarkod.isNullOrBlank()) {
            arananUrun = gelenBarkod

            Handler().postDelayed({
                etArananUrun.setText(arananUrun)

                aramaSonucEkraniniAc()

            }, 500)


        }

        Log.d("onresum_arr_anasayfa", "calisti")
        Log.d("onresum_arr_anasayfa", gelenBarkod)
        Fonk.kayitEkle(requireContext(), EnumX.barkod.toString(), "")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RESULT_SPEECH -> {

                if (resultCode == RESULT_OK && data != null) {
                    val text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

                    for (i in 0..text.size - 1) {
                        Log.d("konusma", text[i].toString())
                    }

                    Handler().postDelayed({
                        etArananUrun.setText(text[0].toString())
                        arananUrun = text[0].toString()


                        aramaSonucEkraniniAc()


                    }, 1000)
                    // etArananUrun.setText(text[0].toString())
                }
            }
        }

    }


    private fun aramaSonucEkraniniAc() {

        val f = UrunAraSonucFragment()
        val args = Bundle()
        args.putString("urunID", arananUrun)
        args.putString("kategoriID", "")
        f.arguments = args

        //fragmen içinde fragment açmak için
        (requireContext() as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, f, null)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(gelenParametre: String) = UrunAraAnaSayfaFragment()
            .apply {}
    }

    override fun aramaGecmisiKelimeSil(aranan: String) {
        gecmisiTemizlePOST(aranan)
    }

    fun View.showKeyboard() {
        this.requestFocus()
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun progresBarOlustur() {
        progressBarX = ProgressBar(requireContext())
        val params = RelativeLayout.LayoutParams(100, 100)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        progressBarX.layoutParams = params
        layoutUrunAraAnaSayfaFragment.addView(progressBarX)
    }

}