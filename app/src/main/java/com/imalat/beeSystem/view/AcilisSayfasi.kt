package com.imalat.beeSystem.view


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.imalat.beeSystem.R
import com.imalat.beeSystem.model.servisListesi.ServisListesiJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.Fonk.*
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.AcilisSayfasiViewModel
import kotlinx.android.synthetic.main.acilis_sayfasi.*


//yapılacaklar
/*
* ++1- ürün ayrıntıları
* ++ 2- adres ekleme
* ++ 3- adres guncelleme
* ++ 4- Sipariş Özeti
* ++ 5- geçimiş siparişlerim
* ++ 6- Sipariş detayları
* + 7- Arama ekranı
* +8- Diğer ana sayfa
* ++ 9- alışveriş listem
* +10- hakkımızda
* todo: 11- iletişim
* + 12- bilgilendirme metni
*
*
*
++  1. SMS Şifresi Giriş Ekranı düzenlenebilir mi? En azından textbox ortalı olsa daha şık durur.
++ 2. Sepet boşaltmada. "Sepeti Boşaltmak İstiyor musunuz? Onayla / İptal" desek daha hoş olur sanki.
++ 3. Sepet boşaltılınca alt toplam 0 olmuyor.
++ 4. Alışveriş Listesinde Tümünü Sepete Ekle butonu planlamıştık.
todo:  5. Profildeki Doğum Tarihi Gün Ay Yıl göstermek gerek.
todo:  6. Üyelik İptal butonu da koyalım bence. Ona bir de servis yazarız.
todo:  7. Adres Blok Daire vb veriler boşsa Text olarak yazılan özet adreste Blok: Kat: Daire: şeklinde hiç göstermeyelim hacım. Hem satır rahatlar.
todo:  8. Ücretsiz Servis Limitine -1 koydum hacım. İstersen başka birşey de yapabiliriz. O değer olunca bence Ücretsiz Servis satırını hiç göstermeyelim.
      todo:  İstemeyen firma  olabilir. Ya da o mahalleye ücretsiz servis yapmak istemeyebilir.
      *
++ 9. Adres tarifi bir kaç satır olabilir. entere bastım satır kayboldu üstteki.
*
++  10. Adres Adı etiketi Adrese adı olmuş.
++  11. Site adı veya Apartman adı yerine --> Site veya Apartman Adı daha iyi olabilir.
+  12. Navbarı her sayfaya yapamıyorsak da bir butonla Ana Sayfaya döndüremez miyiz?
++   13. Ürün Detayları sayfa başlığı Detaylari olmuş.
++  14. Sayfa Başlıkları biraz büyük gibi geliyor bana. Kategori seçip ürün seçme ekranındaki başlık büyüklüğü iyi gibi geldi bana.
++  15. Tıklanan Ana kategorinin çerçevesini mavi yapsak ve ekranı ortalatsak mümkün müdür?
todo:  8. maddede 3 parasal değer de -1se Bu adres sadece Gel Al Teslimat için kullanılabilir diye yazsak daha iyi olur mu?

*
* */


class AcilisSayfasi : AppCompatActivity() {

    private lateinit var viewModel: AcilisSayfasiViewModel
    private val tag = "AcilisSayfasi: "
    private var bildirimdenGelenUrunID = ""
    private var bildirimdenGelenKategoriID = ""
    private var bildirimdenGelenDuyuruID = ""


    private lateinit var progressBarX: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acilis_sayfasi)
        overridePendingTransition(R.anim.saga_gidis1, R.anim.saga_gidis2)
        viewModel = ViewModelProvider(this).get(AcilisSayfasiViewModel::class.java)

        Log.d("oturum", Fonk.degerGetir(this, EnumX.OturumKodu.toString()))
        progresBarOlustur()

        // Fonk.kayitEkle(this,EnumX.OturumKodu.toString(),"VoFvh6yMj3nowq6I2JBtp3sbs0GaNeAmindnYLhciMbbRj2f2Aeb8c333237df8c3c936938471dc11c87")
        //  tvServisError.visibility = View.GONE
        //  progresServisYukleniyor.visibility = View.GONE

        fireBase()

        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                //Log.d(tag, "Key: $key Value: $value")

                if (key.equals("urunid")) bildirimdenGelenUrunID = value.toString()
                if (key.equals("kategoriid")) bildirimdenGelenKategoriID = value.toString()
            }
        }

        //Log.d(tag, "bildirimdenGelenUrunID: " + bildirimdenGelenUrunID)
        //Log.d(tag, "bildirimdenGelenKategoriid: " + bildirimdenGelenKategoriID)


        //Log.d("sifre", degerGetir(this, EnumX.MusteriSifre.toString()))
        //av_from_code.setAnimation("drink.json")
        //av_from_code.playAnimation()
        //av_from_code.loop(true)

        swipeRefresh.setOnRefreshListener {

            verileriYuklePOST()
            swipeRefresh.isRefreshing =
                false   // reset the SwipeRefreshLayout (stop the loading spinner)
        }


        if (isNetworkAvailable(this)) {
            verileriYuklePOST()
        } else {
            yenidenBaglanmayiDene()
        }


        /* val db = DatabaseUrunler(this)
         db.siparisJsonOlustur()
         //  db.soslariGetir("OVZybOV3O30FDAWOWBjTL5KvIhuxZCTNiBSN67at")
         db.close()*/

    }

    fun yenidenBaglanmayiDene() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(this.resources.getString(R.string.uyari))
        builder.setMessage("İnternet bağlantınızı açtıktan sonra yeniden bağlan butonuna basınız")
        builder.setPositiveButton(
            "Yeniden bağlan",
            DialogInterface.OnClickListener { dialogInterface, i ->

                if (isNetworkAvailable(this)) {

                    verileriYuklePOST()
                } else
                    yenidenBaglanmayiDene()

            })
        builder.setNeutralButton(
            this.resources.getString(R.string.iptal),
            DialogInterface.OnClickListener { dialogInterface, i ->

                finish()
            })
        builder.show()
    }

    fun verileriYuklePOST() {

        viewModel.servislerGelsin(
            degerGetir(this, EnumX.token.toString()),
            Fonk.degerGetir(this, EnumX.OturumKodu.toString())
        )

        observerLiveDataServisListesi()

    }

    private fun observerLiveDataServisListesi() {
        viewModel.servisListesi.removeObservers(this)
        viewModel.ServisError.removeObservers(this)
        viewModel.servisYukleniyor.removeObservers(this)


        //Log.d(tag, "observerLiveDataS " + "çalıştı")
        var tetiklenme = 0

        viewModel.servisListesi.observe(this, Observer { ServisListesiCevapX ->

            //countries listesi boş olma ihtimali var eğer boş değilse işlem yap diyoruz
            ServisListesiCevapX?.let {


                if (ServisListesiCevapX.success == 1) {

                    var list: List<ServisListesiJSON> = it.servisListesiJSON

                    if (list != null && list.size > 0) {

                        // //Log.d("list.size",list.size.toString())
                        // //Log.d("list.size","**********************")
                        //Log.d("servislisteis", "başladı")
                        for (x in 0 until list.size) {

                            ////Log.d("aaaa", list[x].servisAd)

                            when (list[x].servisAd) {
                                "001ClientLogin" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.gConLogin.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g001ClientLogin =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }
                                "002URKategoriList" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g001UrunKategoriList.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g002URKategoriList =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "003URUrunList" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g003URUrunList =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "004AnaMenuIcerik" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g004AnaMenuIcerik =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "005MusteriKayitSMS" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g005MusteriKayitSMS =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }
                                "006MusteriKayit" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g006MusteriKayit =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }


                                "007MusteriLoginSMS" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g007MusteriLoginSMS =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "008MusteriLogin" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g008MusteriLogin =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "009MusteriProfil" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g009MusteriProfil =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "010ProfilGuncelle" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g010ProfilGuncelle =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "011ADAdresList" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g011ADAdresList =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "012MUAdresEdit" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g012MUAdresEdit =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "013MUAdresList" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g013MUAdresList =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "014MUAlisverisListeEdit" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g014MUAlisverisListeEdit =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "015MUAlisverisListeList" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g015MUAlisverisListeList =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "016SPOdemeTipList" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g016SPOdemeTipList =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "017SPTeslimatTipList" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g017SPTeslimatTipList =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "018SPServisPlanList" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g018SPServisPlanList =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "019UrunDetay" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g019UrunDetay =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "020SiparisOlustur" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g020SiparisOlustur =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }
                                "021MusteriSiparisleri" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g021MusteriSiparisleri =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "022SiparisDetayListesi" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g022SiparisDetayListesi =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }
                                "023UrunArama" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g023UrunArama =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }
                                "024KurumsalBilgi" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g024KurumsalBilgi =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "025CRSubeList" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g025CRSubeList =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "ConYandexGeoCode" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.gConYandexGeoCode =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }


                                "026CRBildirimList" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g026CRBildirimList =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "027SepetGuncelle" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g027SepetGuncelle =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "028SepetGetir" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g028SepetGetir =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "029SepetToplam" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g029SepetToplam =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }

                                "030AlisverisListesiSepete" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g030AlisverisListesiSepete =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }
                                "031AramaGecmisGetir" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g031AramaGecmisGetir =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }
                                "032AramaGecmisSil" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g032AramaGecmisSil =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }
                                "033OnayBekleyenUrunler" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g033OnayBekleyenUrunler =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }
                                "034SiparisDetayOnay" -> {
                                    //kayitEkle(this, KayitSecenekleriEnum.g002UrunKategoriEdit.toString(), linkParcala(list[x].servisAdres))
                                    GlobalDegiskenlerX.g034SiparisDetayOnay =
                                        Fonk.linkParcala(list[x].servisAdres)
                                }


                            }
                        }

                        //Log.d("servislisteis", "bitti")


                        Handler().postDelayed({

                            val intent = Intent(this, AnaSayfa::class.java)
                            intent.putExtra("bildirimdenGelenUrunId", bildirimdenGelenUrunID)
                            intent.putExtra(
                                "bildirimdenGelenKategoriID",
                                bildirimdenGelenKategoriID
                            )
                            intent.putExtra("bildirimdenGelenDuyuruID", bildirimdenGelenDuyuruID)
                            startActivity(intent)
                            // startActivity(Intent(this, AnaSayfaNavigation::class.java))
                            finish()

                        }, 500)


                    }


                } else {
                    alertDialogGoster(this, "", ServisListesiCevapX.message)
                }

                viewModel.servisListesi.postValue(null)

            }

        })

        viewModel.ServisError.observe(this, Observer { error ->
            error?.let {
                if (it) {
                    //tvServisError.visibility = View.VISIBLE

                    Fonk.alertDialogGoster(
                        this,
                        "",
                        "Sunucu hatası oluştu lütfen 1 dakika sonra tekrar deneyiniz"
                    )
                    //Log.d(tag + "ServisError", "var")
                } else {
                    //tvServisError.visibility = View.GONE
                    tetiklenme++

                }

                viewModel.ServisError.postValue(null)
            }
        })

        viewModel.servisYukleniyor.observe(this, Observer { loading ->
            loading?.let {
                if (it) {
                    progressBarX.visibility = View.VISIBLE
                } else {
                    progressBarX.visibility = View.GONE
                }

            }

            viewModel.servisYukleniyor.postValue(null)
        })
    }


    private fun fireBase() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    // Log.w(ContentValues.TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result!!.token
                kayitEkle(this, (EnumX.token.toString()), token)
                //Log.d("token: ", token)
            })
    }


    private fun progresBarOlustur() {
        progressBarX = ProgressBar(this)
        val params = RelativeLayout.LayoutParams(100, 100)
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        progressBarX.layoutParams = params
        layoutAciliSayfasi.addView(progressBarX)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.sola_gidis1, R.anim.sola_gidis2)
    }

}