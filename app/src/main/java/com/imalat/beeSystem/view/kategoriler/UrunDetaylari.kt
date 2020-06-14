package com.imalat.beeSystem.view.kategoriler

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.imalat.beeSystem.R
import com.imalat.beeSystem.model.uruDetay.UrunDetayJSON
import com.imalat.beeSystem.util.*
import com.imalat.beeSystem.view.imageView.TamEkranResimFragment
import com.imalat.beeSystem.view.profil.GirisYap
import com.imalat.beeSystem.viewModel.FragmentAnaSayfaViewModel
import com.imalat.beeSystem.viewModel.KategoriDetaylariViewModel
import com.imalat.beeSystem.viewModel.UrunDetayViewModel
import kotlinx.android.synthetic.main.activity_urun_detaylari.*


class UrunDetaylari : AppCompatActivity() {

    lateinit var urunDetayViewModel: UrunDetayViewModel
    lateinit var viewModelKategoriDetaylari: KategoriDetaylariViewModel
    private lateinit var viewModelFragmentAnaSayfaViewModel: FragmentAnaSayfaViewModel

    private var gelenUrunID = ""
    private var gelenPosition = 0
    private var gelenFotoAdres = ""
    private var gelenFavori = ""
    private var sepettekiSonAdetDurumu = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_urun_detaylari)
        overridePendingTransition(R.anim.saga_gidis1, R.anim.saga_gidis2)

        layoutFiyat.visibility = View.GONE

        urunDetayViewModel = ViewModelProvider(this).get(UrunDetayViewModel::class.java)
        viewModelKategoriDetaylari =
            ViewModelProvider(this).get(KategoriDetaylariViewModel::class.java)
        viewModelFragmentAnaSayfaViewModel =
            ViewModelProvider(this).get(FragmentAnaSayfaViewModel::class.java)


        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            gelenUrunID = bundle.getString("urunID").toString()
            gelenPosition = bundle.getInt("position")
            gelenFavori = bundle.getString("favori").toString()

            Log.e("gelenurunid", gelenUrunID)
        }


        if (Fonk.isNetworkAvailable(this)) {
            urunDetayViewModel.urunDetaylariGetir(
                GlobalDegiskenlerX.g019UrunDetay,
                Fonk.degerGetir(this, EnumX.OturumKodu.toString()),
                gelenUrunID
            )

            observerLiveDataUrunDetaylari()
        }


        tvAciklama.text = " "

        /* imTamEkran.setOnClickListener {

             val tamEkranResimFragment = TamEkranResimFragment
             tamEkranResimFragment.newInstance(gelenFotoAdres).show(supportFragmentManager, "ad")
         }*/

        imUrunFoto.setOnClickListener {
            val tamEkranResimFragment = TamEkranResimFragment
            tamEkranResimFragment.newInstance(gelenFotoAdres).show(supportFragmentManager, "ad")
        }


        paylas.setOnClickListener {
            val urunBilgisi =
                tvUrunAdi.text.toString() + " sadece ${tvKampanyaliFiyat.text.toString()} hemen ücretsiz uygulamayı indirip sipariş verebilirsin\n"

            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                urunBilgisi + resources.getString(R.string.app_name) + "\nhttp://play.google.com/store/apps/details?id=" + this.packageName
            );
            startActivity(Intent.createChooser(shareIntent, getString(R.string.paylas)))

        }

        tvMarkaIsmi.setOnClickListener {

            /* val frg = UrunAraDialogFragment
             frg.newInstance(tvMarkaIsmi.text.toString()).show(supportFragmentManager, "marka")*/
        }


        geri.setOnClickListener {

            kapat()

        }

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            kapat()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    fun kapat() {
        val resultIntent = Intent()
        resultIntent.putExtra("adet", sepettekiSonAdetDurumu)
        resultIntent.putExtra("favori", gelenFavori)
        resultIntent.putExtra("position", gelenPosition.toString())
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }


    private fun observerLiveDataUrunDetaylari() {
        urunDetayViewModel.urunDetaylari.removeObservers(this)
        urunDetayViewModel.servisYukleniyor.removeObservers(this)
        urunDetayViewModel.ServisError.removeObservers(this)


        urunDetayViewModel.urunDetaylari.observe(this, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    var list: List<UrunDetayJSON> = cevap.urunDetayJSON

                    if (list != null && list.size > 0) {

                        // Log.d("fooooto",list[0].fotograf)
                        gelenFavori = list[0].favori
                        sepettekiSonAdetDurumu = list[0].sepettekiAdet
                        tvUrunAdi.text = list[0].urunAdi
                        viewYukle(list)
                        layoutFiyat.visibility = View.VISIBLE
                    }

                } else {
                    Toast.makeText(this, cevap.message, Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    fun viewYukle(list: List<UrunDetayJSON>) {

        val position = 0
        tvBirimFiyat.text = list[position].birimFiyat
        tvMarkaIsmi.text = list[position].markaAd
        tvAciklama.text = list[position].aciklama
        tvUrunAdi.text = list[position].urunAdi
        tvKampanyaliFiyat.text = list[position].kampanyaliFiyat + " TL"
        tvFiyat.text = list[position].fiyat + " TL"
        tvFiyat.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        tvIndirimOrani.text =
            "%${Fonk.indirimOraniHesapla(list[position].fiyat, list[position].kampanyaliFiyat)}"
        gelenFotoAdres = list[position].fotograf

        //list[position].sepettekiAdet = sepettekiUrunSayisi(list[position].urunID).tos

        tvAdet.text =
            Fonk.artisMikatriParcala(list[position].sepettekiAdet.toString()) + " " + list[position].birimAd

        if (Fonk.doubleCevir(list[position].sepettekiAdet) > 0.0) {
            cardViewSepeteEkle.visibility = View.INVISIBLE
            //frameLayout.visibility = View.VISIBLE
        } else {
            cardViewSepeteEkle.visibility = View.VISIBLE
            // frameLayout.visibility = View.INVISIBLE
        }


        //indirim oranı yoksa gizle
        if (list[position].fiyat.equals(list[position].kampanyaliFiyat)) {
            indirimLayout.visibility = View.INVISIBLE
            tvFiyat.visibility = View.GONE
        } else {
            indirimLayout.visibility = View.VISIBLE
            tvFiyat.visibility = View.VISIBLE
        }

        //ürün favoriye ekli mi değil mi
        if (list[position].favori.equals("1")) {
            imAlisVerisListesi.setImageResource(R.drawable.x_ic_add_list_active)
        } else {
            imAlisVerisListesi.setImageResource(R.drawable.x_ic_add_list)
        }

        imUrunFoto.downloadFromUrl(
            list[position].fotograf,
            placeholderProgressBar(this)
        )


        cardViewSepeteEkle.setOnClickListener {

            if (Fonk.isNetworkAvailable(this)) {

                if (Fonk.degerGetir(this, EnumX.OturumKodu.toString())
                        .isNullOrBlank()
                ) {

                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setTitle(resources.getString(R.string.uyari))
                    builder.setMessage("Üye olun")
                   // builder.setMessage("En uygun fiyatlarla alışveriş yapmak ve kampanyalardan faydalanmak için hemen ücretsiz üye olun")
                    builder.setPositiveButton(
                        "Ücretsiz Üye Ol",
                        DialogInterface.OnClickListener { dialogInterface, i ->

                            startActivity(
                                Intent(
                                    this,
                                    GirisYap::class.java
                                )
                            )

                        })
                    builder.setNegativeButton(
                        resources.getString(R.string.iptal),
                        DialogInterface.OnClickListener { dialogInterface, i ->

                        })
                    builder.show()

                } else {

                    list[position].sepettekiAdet = "1"
                    sepettekiSonAdetDurumu = list[position].sepettekiAdet
                    tvAdet.setText("1 " + list[position].birimAd)
                    sepeteEkleCikartPOST(list[position].urunID, "1")
                    cardViewSepeteEkle.visibility = View.INVISIBLE
                }
            }


        }



        imUrunSayisiArttir.setOnClickListener {


            val artisMiktari: Double = Fonk.doubleCevir(list[position].artisMiktari)
            val maksSipMiktar: Double = Fonk.doubleCevir(list[position].maksSipMiktar)
            val sepettekiAdet: Double = Fonk.doubleCevir(list[position].sepettekiAdet)

            if (maksSipMiktar > sepettekiAdet) {

                val yeniMiktar = Fonk.sayiFormatla(sepettekiAdet + artisMiktari)
                list[position].sepettekiAdet = yeniMiktar.toString()
                sepettekiSonAdetDurumu = list[position].sepettekiAdet
                tvAdet.setText(Fonk.artisMikatriParcala(yeniMiktar.toString()) + " " + list[position].birimAd)

                sepeteEkleCikartPOST(list[position].urunID, yeniMiktar.toString())

            } else {
                Fonk.alertDialogGoster(
                    this,
                    "",
                    "${list[position].urunAdi} için maksimum sipariş adetine ulaştınız"
                )
            }

        }

        imUrunSayisiAzalt.setOnClickListener {

            val artisMiktari: Double = Fonk.doubleCevir(list[position].artisMiktari)
            val maksSipMiktar: Double = Fonk.doubleCevir(list[position].maksSipMiktar)
            val sepettekiAdet: Double = Fonk.doubleCevir(list[position].sepettekiAdet)

            if (sepettekiAdet > 0.0) {

                val yeniMiktar = Fonk.sayiFormatla(sepettekiAdet - artisMiktari)
                list[position].sepettekiAdet = yeniMiktar.toString()
                sepettekiSonAdetDurumu = list[position].sepettekiAdet
                tvAdet.setText(Fonk.artisMikatriParcala(yeniMiktar.toString()) + " " + list[position].birimAd)

                sepeteEkleCikartPOST(list[position].urunID, yeniMiktar.toString())



                if ((sepettekiAdet - artisMiktari) == 0.0) {
                    cardViewSepeteEkle.visibility = View.VISIBLE
                }


            } else {

            }

        }



        imAlisVerisListesi.setOnClickListener {

            if (!Fonk.degerGetir(this, EnumX.OturumKodu.toString()).isNullOrBlank()) {

                if (Fonk.isNetworkAvailable(this)) {
                    alisverisListemeEkleCikartPOST(gelenUrunID)
                }
            } else {

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle(this.resources.getString(R.string.uyari))
                builder.setMessage("Üye olun")
               // builder.setMessage("Kendi alışveriş listenizi oluşturmak ve kampanyalardan faydalanmak için hemen ücretsiz üye olun")
                builder.setPositiveButton(
                    "Ücretsiz Üye Ol",
                    DialogInterface.OnClickListener { dialogInterface, i ->

                        this.startActivity(Intent(this, GirisYap::class.java))

                    })
                builder.setNegativeButton(
                    this.resources.getString(R.string.iptal),
                    DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                builder.show()
            }


        }
    }


    //eğer sepette ürün yoksa bu buton görünecek varsa görünmeyecek
    fun sepeteEkleButonuDurumuAyarla(sepetAdet: Double) {

        if (sepetAdet > 0.0)
            cardViewSepeteEkle.visibility = View.INVISIBLE
        else
            cardViewSepeteEkle.visibility = View.VISIBLE

    }


    private fun alisverisListemeEkleCikartPOST(Pst_UrunID: String) {

        viewModelKategoriDetaylari.alisVerisListesineEkleCikar(
            GlobalDegiskenlerX.g014MUAlisverisListeEdit,
            Fonk.degerGetir(this, EnumX.OturumKodu.toString()),
            Pst_UrunID
        )


        viewModelKategoriDetaylari.alisVerisListeEdit.removeObservers(this)
        viewModelKategoriDetaylari.ServisError.removeObservers(this)
        viewModelKategoriDetaylari.servisYukleniyor.removeObservers(this)

        viewModelKategoriDetaylari.alisVerisListeEdit.observe(
            this,
            Observer { cevap ->
                cevap?.let {

                    if (cevap.success == 1) {
                        gelenFavori = "1"
                        imAlisVerisListesi.setImageResource(R.drawable.x_ic_add_list_active)
                        Toast.makeText(this, cevap.message, Toast.LENGTH_SHORT).show()
                    } else if (cevap.success == 2) {
                        gelenFavori = "0"
                        imAlisVerisListesi.setImageResource(R.drawable.x_ic_add_list)
                        Toast.makeText(this, cevap.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, cevap.message, Toast.LENGTH_SHORT).show()
                    }
                }

                viewModelKategoriDetaylari.alisVerisListeEdit.postValue(null)

            })


    }


    private fun sepeteEkleCikartPOST(Pst_UrunID: String, miktar: String) {

        viewModelFragmentAnaSayfaViewModel.sepetEkleCikart(
            GlobalDegiskenlerX.g027SepetGuncelle,
            Fonk.degerGetir(this, EnumX.OturumKodu.toString()),
            "EkleCikart",
            miktar,
            Pst_UrunID
        )


        viewModelFragmentAnaSayfaViewModel.sepetEkleCikart.removeObservers(this)
        viewModelFragmentAnaSayfaViewModel.ServisError.removeObservers(this)
        viewModelFragmentAnaSayfaViewModel.servisYukleniyor.removeObservers(this)

        viewModelFragmentAnaSayfaViewModel.sepetEkleCikart.observe(
            this,
            Observer { cevap ->
                cevap?.let {

                    /* if (cevap.success == 1) {
                         Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()
                     } else
                         Toast.makeText(requireContext(), cevap.message, Toast.LENGTH_SHORT).show()*/

                    // badgeGuncelleInterface.badgeGuncelle("")
                }

                viewModelFragmentAnaSayfaViewModel.sepetEkleCikart.postValue(null)

            })

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.sola_gidis1, R.anim.sola_gidis2)
    }
}