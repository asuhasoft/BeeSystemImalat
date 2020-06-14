package com.imalat.beeSystem.view.profil.adres

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.KoordinatGonderInterface
import com.imalat.beeSystem.model.profil.adresler.ilceler.IlcelerJSON
import com.imalat.beeSystem.model.profil.adresler.iller.IllerJSON
import com.imalat.beeSystem.model.profil.adresler.mahalle.MahallelerJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.AdresEkleDuzenleViewModel
import kotlinx.android.synthetic.main.activity_adres_ekle_duzenle.*
import java.util.*

class AdresEkleDuzenle : AppCompatActivity(), KoordinatGonderInterface {


    lateinit var viewModel: AdresEkleDuzenleViewModel

    var gelenMusteriAdresID = ""
    var gelenilID = ""
    var gelenilceID = ""
    var gelenMahalleID = ""
    var yapilacakIslem = "Ekle"

    var secilenIlID = ""
    var secilenIlceID = ""
    var secilenMahalleID = ""
    var faturaTipi = "bireysel"
    var enlemX = ""
    var boylamX = ""

    lateinit var listMahaller: List<MahallelerJSON>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adres_ekle_duzenle)
        overridePendingTransition(R.anim.saga_gidis1, R.anim.saga_gidis2)

        viewModel = ViewModelProvider(this).get(AdresEkleDuzenleViewModel::class.java)


        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            gelenMusteriAdresID = bundle.getString("musteriAdresID").toString()

            if (!gelenMusteriAdresID.isNullOrBlank()) {
                yapilacakIslem = "Guncelle"

                if (!bundle.getString("adresAd").toString().isNullOrBlank())
                    etAdresAdi.setText(bundle.getString("adresAd").toString())

                if (!bundle.getString("adresTarif").toString().isNullOrBlank())
                    etAdresTarifi.setText(bundle.getString("adresTarif").toString())

                if (!bundle.getString("blokAd").toString().isNullOrBlank())
                    etBlokAd.setText(bundle.getString("blokAd").toString())

                if (!bundle.getString("firmaAd").toString().isNullOrBlank())
                    etFirmaAdi.setText(bundle.getString("firmaAd").toString())

                if (!bundle.getString("katNo").toString().isNullOrBlank())
                    etKatNo.setText(bundle.getString("katNo").toString())

                if (!bundle.getString("siteBinaAd").toString().isNullOrBlank())
                    etBinaAdi.setText(bundle.getString("siteBinaAd").toString())

                if (!bundle.getString("vergiDaire").toString().isNullOrBlank())
                    etVergiDairesi.setText(bundle.getString("vergiDaire").toString())


                if (!bundle.getString("vergiNo").toString().isNullOrBlank())
                    etVergiNumarasi.setText(bundle.getString("vergiNo").toString())

                if (!bundle.getString("yolAdi").toString().isNullOrBlank())
                    etCaddeSokak.setText(bundle.getString("yolAdi").toString())

                if (!bundle.getString("icKapiNo").toString().isNullOrBlank())
                    etDaireNo.setText(bundle.getString("icKapiNo").toString())

                if (!bundle.getString("disKapiNo").toString().isNullOrBlank())
                    etBinaNo.setText(bundle.getString("disKapiNo").toString())


                if (!bundle.getString("alternatifKisi").toString().isNullOrBlank())
                    etAlterantifKisi.setText(bundle.getString("alternatifKisi").toString())

                if (!bundle.getString("alternatifTelefon").toString().isNullOrBlank())
                    etAlternatifTelefonn.setText(bundle.getString("alternatifTelefon").toString())


                if (!bundle.getString("ilID").toString().isNullOrBlank())
                    gelenilID = bundle.getString("ilID").toString()

                if (!bundle.getString("ilceID").toString().isNullOrBlank())
                    gelenilceID = bundle.getString("ilceID").toString()

                if (!bundle.getString("mahalleID").toString().isNullOrBlank())
                    gelenMahalleID = bundle.getString("mahalleID").toString()

                // illeriGetirPOST()

            }

            Log.e("gelenMusteriAdresID", gelenMusteriAdresID)
        }

        layoutKurumsal.visibility = View.GONE

        if (Fonk.isNetworkAvailable(this)) {

            illeriGetirPOST()
        }


        radioBireysel.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                faturaTipi = "bireysel"
                layoutKurumsal.visibility = View.GONE
            }

        }

        radioKurumsal.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                faturaTipi = "kurumsal"
                layoutKurumsal.visibility = View.VISIBLE

            }

        }


        kaydet.setOnClickListener {

            if (Fonk.isNetworkAvailable(this)) {
                adresEkleGuncelle()
            }
        }


        adresiOtomatikBul.setOnClickListener {

            val tamEkranResimFragment = HaritadaAdresBulmaFragment
            tamEkranResimFragment.newInstance().show(supportFragmentManager, "ad")

            // startActivity(Intent(this, KoordinataGoreAdresBul::class.java))
        }

        geri.setOnClickListener {
            finish()
        }

    }


    // il ayarla
    private fun illeriGetirPOST() {

        viewModel.ilGetir(
            GlobalDegiskenlerX.g011ADAdresList,
            "Il",
            Fonk.degerGetir(this, EnumX.OturumKodu.toString()),
            "",
            ""
        )
        observableLiveDataIlleriGetir()
    }

    private fun observableLiveDataIlleriGetir() {
        viewModel.ilListesi.removeObservers(this)
        viewModel.ServisError.removeObservers(this)
        viewModel.servisYukleniyor.removeObservers(this)

        viewModel.ilListesi.observe(this, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    var list: List<IllerJSON> = cevap.illerJSON
                    if (list != null && list.size > 0) {
                        spinnerIllerYukle(list)
                        /* for (x in 0 until list.size) {
                             //Log.d("aaaa", list[x].ilAdi)
                         }*/
                    } else spinnerIllerYukle(arrayListOf())


                } else {
                    spinnerIllerYukle(arrayListOf())

                }
            }

            viewModel.ilListesi.postValue(null)

        })

    }

    fun spinnerIllerYukle(list: List<IllerJSON>) {

        val spinnerValue = ArrayList<String>()
        val spinnerKey = ArrayList<String>()
        spinnerValue.add("İl Seçiniz")
        spinnerKey.add("-1")


        var secilecekPosition = 0
        for (i in list.indices) {
            spinnerValue.add(list[i].ilAdi)
            spinnerKey.add(list.get(i).ilID)

            //duzenleme yapılırken se.ilen il ilçe mahalle otomatik getirilecek
            if (!gelenilID.isNullOrBlank() && gelenilID.equals(list.get(i).ilID)) {
                secilecekPosition = i + 1
                secilenIlID = list.get(i).ilID
                //Log.d("secilecekPosition", secilecekPosition.toString())
            }
        }


        val adapter = ArrayAdapter(this, R.layout.spinner_item, spinnerValue)
        sipinnerIller.setAdapter(adapter)

        sipinnerIller.setSelection(secilecekPosition)

        sipinnerIller.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                if (position != 0) {
                    //Log.d("seçilen_value", spinnerValue[position])
                    //Log.d("seçilen_key", spinnerKey[position])
                    secilenIlID = spinnerKey[position]
                    tvServisUcreti.setText("") // var olan yazıyı siliyoruz


                    if (!secilenIlID.equals(gelenilID)) {
                        gelenMahalleID = ""
                        gelenilceID = ""
                        secilenIlceID = "0"
                    }

                    ilceleriGetir()
                    sipinnerMahalle.visibility = View.INVISIBLE


                    /* //burada mahalle listesi eklendiyse onu siliyoruz, çünkü başka şehir seçmiş olabilir
                     gelenMahalleID = ""
                     spinnerMahalleleriYukle(arrayListOf())
                     */


                } else {
                    secilenIlID = ""
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                secilenIlID = ""
            }
        }
    }


//ilce ayarla --------------------------

    fun ilceleriGetir() {

        if (Fonk.isNetworkAvailable(this)) {

            viewModel.ilceGetir(
                GlobalDegiskenlerX.g011ADAdresList,
                "Ilce",
                Fonk.degerGetir(this, EnumX.OturumKodu.toString()),
                secilenIlID,
                ""
            )

            observableLiveDataIlceriGetir()

        }
    }

    private fun observableLiveDataIlceriGetir() {
        viewModel.ilceListesi.removeObservers(this)
        viewModel.ServisError.removeObservers(this)
        viewModel.servisYukleniyor.removeObservers(this)

        viewModel.ilceListesi.observe(this, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    var list: List<IlcelerJSON> = cevap.ilcelerJSON
                    if (list != null && list.size > 0) {
                        spinnerIlcelerYukle(list)
                        /*for (x in 0 until list.size) {
                            //Log.d("aaaa", list[x].ilceAd)
                        }*/
                    } else
                        spinnerIlcelerYukle(arrayListOf())


                } else {
                    spinnerIlcelerYukle(arrayListOf())

                }

                viewModel.ilceListesi.postValue(null)

            }

        })

    }


    fun spinnerIlcelerYukle(list: List<IlcelerJSON>) {

        val spinnerValue = ArrayList<String>()
        val spinnerKey = ArrayList<String>()
        spinnerValue.add("İlçe Seçiniz")
        spinnerKey.add("-1")

        var secilecekPosition = 0
        for (i in list.indices) {
            spinnerValue.add(list[i].ilceAd)
            spinnerKey.add(list.get(i).ilceID)

            //duzenleme yapılırken se.ilen il ilçe mahalle otomatik getirilecek
            if (!gelenilceID.isNullOrBlank() && gelenilceID.equals(list.get(i).ilceID)) {
                secilecekPosition = i + 1
                secilenIlceID = list.get(i).ilceID
                //Log.d("secilecekPosition", secilecekPosition.toString())
            }
        }


        val adapter = ArrayAdapter(this, R.layout.spinner_item, spinnerValue)
        sipinnerIlceler.setAdapter(adapter)

        sipinnerIlceler.setSelection(secilecekPosition)

        sipinnerIlceler.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                if (position != 0) {
                    //Log.d("seçilen_value", spinnerValue[position])
                    //Log.d("seçilen_key", spinnerKey[position])
                    secilenIlceID = spinnerKey[position]
                    tvServisUcreti.setText("") // var olan yazıyı siliyoruz

                    mahalleGetir()


                    sipinnerMahalle.visibility = View.VISIBLE

                } else {
                    secilenIlceID = ""
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                secilenIlceID = ""
            }
        }

    }


//mahalle ayarla --------------------------

    fun mahalleGetir() {

        if (Fonk.isNetworkAvailable(this)) {

            viewModel.mahalleGetir(
                GlobalDegiskenlerX.g011ADAdresList,
                "Mahalle",
                Fonk.degerGetir(this, EnumX.OturumKodu.toString()),

                "",
                secilenIlceID
            )

            observableLiveDataMahalleGetir()

        }
    }

    private fun observableLiveDataMahalleGetir() {
        viewModel.mahalleListesi.removeObservers(this)
        viewModel.ServisError.removeObservers(this)
        viewModel.servisYukleniyor.removeObservers(this)

        viewModel.mahalleListesi.observe(this, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    // var list: List<MahallelerJSON> = cevap.mahallelerJSON
                    listMahaller = cevap.mahallelerJSON

                    if (listMahaller != null && listMahaller.size > 0) {
                        spinnerMahalleleriYukle(listMahaller)
                        /*  for (x in 0 until listMahaller.size) {
                              //Log.d("aaaa", listMahaller[x].mahalleAd)
                          }*/
                    } else spinnerMahalleleriYukle(arrayListOf())


                } else {
                    spinnerMahalleleriYukle(arrayListOf())
                }

                viewModel.mahalleListesi.postValue(null)

            }

        })

    }


    fun spinnerMahalleleriYukle(list: List<MahallelerJSON>) {

        val spinnerValue = ArrayList<String>()
        val spinnerKey = ArrayList<String>()
        val spinnerBilgilendirme = ArrayList<String>()
        spinnerValue.add("Mahalle Seçiniz")
        spinnerKey.add("-1")
        spinnerBilgilendirme.add("-1")

        var secilecekPosition = 0
        for (i in list.indices) {
            spinnerValue.add(list[i].mahalleAd)
            spinnerKey.add(list.get(i).mahalleID)

            if (list[i].minSipTutar.equals("-1") || list[i].minSipTutar.equals("-1.00")) {
                spinnerBilgilendirme.add("Bulunduğunuz mahalleye servisimiz yoktur, Siparişinizi uygulama üzerinden verip Gel Al yapabilirsiniz")
            } else {
                spinnerBilgilendirme.add("Minumum sipariş ücreti ${list[i].minSipTutar} TL, Servis ücreti ${list[i].servisUcret} TL olup Ücretsiz servis içinse en az ${list[i].UcretsizServis} TL tutarında alışveriş yapmanız gerekmektedir.")

            }

            //duzenleme yapılırken se.ilen il ilçe mahalle otomatik getirilecek
            if (!gelenMahalleID.isNullOrBlank() && gelenMahalleID.equals(list.get(i).mahalleID)) {
                secilecekPosition = i + 1
                secilenMahalleID = list.get(i).mahalleID
                //Log.d("secilecekPosition", secilecekPosition.toString())
            }
        }


        val adapter = ArrayAdapter(this, R.layout.spinner_item, spinnerValue)
        sipinnerMahalle.setAdapter(adapter)

        sipinnerMahalle.setSelection(secilecekPosition)

        sipinnerMahalle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                if (position != 0) {
                    //Log.d("seçilen_value", spinnerValue[position])
                    //Log.d("seçilen_key", spinnerKey[position])
                    secilenMahalleID = spinnerKey[position]
                    tvServisUcreti.setText(spinnerBilgilendirme[position])
                } else {
                    secilenMahalleID = ""
                    tvServisUcreti.setText("") // var olan yazıyı siliyoruz

                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                secilenMahalleID = ""
            }
        }

    }


//adres ekle güncelle

    fun adresEkleGuncelle() {

        etAdresAdi.setText(Fonk.karakterTemizle(etAdresAdi.text.toString()))
        etCaddeSokak.setText(Fonk.karakterTemizle(etCaddeSokak.text.toString()))
        etBinaAdi.setText(Fonk.karakterTemizle(etBinaAdi.text.toString()))
        etBinaNo.setText(Fonk.karakterTemizle(etBinaNo.text.toString()))
        etBlokAd.setText(Fonk.karakterTemizle(etBlokAd.text.toString()))
        etKatNo.setText(Fonk.karakterTemizle(etKatNo.text.toString()))
        etDaireNo.setText(Fonk.karakterTemizle(etDaireNo.text.toString()))
        etAdresTarifi.setText(Fonk.karakterTemizle(etAdresTarifi.text.toString()))

        etFirmaAdi.setText(Fonk.karakterTemizle(etFirmaAdi.text.toString()))
        etVergiDairesi.setText(Fonk.karakterTemizle(etVergiDairesi.text.toString()))
        etVergiNumarasi.setText(Fonk.karakterTemizle(etVergiNumarasi.text.toString()))
        etAlterantifKisi.setText(Fonk.karakterTemizle(etAlterantifKisi.text.toString()))


        if (etAdresAdi.text.toString().isNullOrBlank())
            Fonk.alertDialogGoster(
                this,
                "",
                "Adres adı giriniz.(Ev adresim, iş adresim vs. diye adlandırabilirsiniz"
            )
        else if (secilenIlID.isNullOrBlank())
            Fonk.alertDialogGoster(this, "", "İl seçiniz")
        else if (secilenIlceID.isNullOrBlank())
            Fonk.alertDialogGoster(this, "", "İlçe seçiniz")
        else if (secilenMahalleID.isNullOrBlank())
            Fonk.alertDialogGoster(this, "", "Mahalle seçiniz")
        else if (secilenMahalleID.isNullOrBlank())
            Fonk.alertDialogGoster(this, "", "Mahalle seçiniz")
        else if (etKatNo.text.toString().isNullOrBlank())
            Fonk.alertDialogGoster(this, "", "Kat numarası giriniz")
        else if (etDaireNo.text.toString().isNullOrBlank())
            Fonk.alertDialogGoster(this, "", "Daire numarası giriniz")
        else if (faturaTipi.equals("kurumsal")) {

            if (etFirmaAdi.text.toString().isNullOrBlank())
                Fonk.alertDialogGoster(this, "", "Firma adi giriniz")
            else if (etVergiDairesi.text.toString().isNullOrBlank())
                Fonk.alertDialogGoster(this, "", "Vergi dairesi giriniz")
            else if (etVergiNumarasi.text.toString().isNullOrBlank())
                Fonk.alertDialogGoster(this, "", "Vergi numarası giriniz")
            else
                adresEkleGuncellePOST()
        } else
            adresEkleGuncellePOST()

    }

    fun adresEkleGuncellePOST() {
        viewModel.adresEkleGuncelle(
            GlobalDegiskenlerX.g012MUAdresEdit,
            yapilacakIslem,
            Fonk.degerGetir(this, EnumX.OturumKodu.toString()),
            etAdresAdi.text.toString(),
            etFirmaAdi.text.toString(),
            etVergiDairesi.text.toString(),
            etVergiNumarasi.text.toString(),
            secilenMahalleID,
            etCaddeSokak.text.toString(),
            etBinaNo.text.toString(),
            etBinaAdi.text.toString(),
            etBlokAd.text.toString(),
            etDaireNo.text.toString(),
            etAdresTarifi.text.toString(),
            etAlterantifKisi.text.toString(),
            etAlternatifTelefonn.text.toString(),
            enlemX,
            boylamX,
            "1",
            gelenMusteriAdresID,
            etKatNo.text.toString()
        )

        observableLiveDataAdresGuncelleEkle()
    }


    private fun observableLiveDataAdresGuncelleEkle() {
        viewModel.adresEkleGuncelle.removeObservers(this)
        viewModel.ServisError.removeObservers(this)
        viewModel.servisYukleniyor.removeObservers(this)

        viewModel.adresEkleGuncelle.observe(this, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    Toast.makeText(this, cevap.message, Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Fonk.alertDialogGoster(this, "", cevap.message)

                }
            }

            viewModel.adresEkleGuncelle.postValue(null)

        })

    }

    override fun koordinatlar(enlem: String, boylam: String) {
        enlemX = enlem
        boylamX = boylam
        //Log.d("gelenEnlem", enlem)
        Log.d("gelenBoylam", boylam)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.sola_gidis1, R.anim.sola_gidis2)
    }
}