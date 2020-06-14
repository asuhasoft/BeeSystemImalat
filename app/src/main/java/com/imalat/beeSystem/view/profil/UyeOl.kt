package com.imalat.beeSystem.view.profil

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.imalat.beeSystem.R
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.view.diger.BilgilendirmeActivity
import com.imalat.beeSystem.viewModel.UyelOlViewModel
import kotlinx.android.synthetic.main.fragment_uye_ol.*
import kotlinx.android.synthetic.main.sms_kayit_giris_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*


class UyeOl : AppCompatActivity() {

    private var neYapilacak = "sadeceGeriDon"
    private var secilenDogumTarihi: String = ""
    private lateinit var viewModel: UyelOlViewModel

    private var telefon = ""
    private var politika1 = true
    private var politika2 = true
    private var cinsiyetSecimi = -1
    private var dogumTarihi = ""
    private var secilenYil = ""
    private var secilenAy = ""
    private var secilenGun = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_uye_ol)
        overridePendingTransition(R.anim.saga_gidis1, R.anim.saga_gidis2)

        viewModel = ViewModelProvider(this).get(UyelOlViewModel::class.java)


        etTelefon.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        checkboxPolitika1.setOnCheckedChangeListener { buttonView, isChecked ->
            politika1 = isChecked
        }

        checkboxPolitika2.setOnCheckedChangeListener { buttonView, isChecked ->
            politika2 = isChecked
        }


        checkboxPolitika1.setOnClickListener {

            if (Fonk.isNetworkAvailable(this))
                startActivity(Intent(this, BilgilendirmeActivity::class.java))
        }

        checkboxPolitika2.setOnClickListener {

            if (Fonk.isNetworkAvailable(this))
                startActivity(Intent(this, BilgilendirmeActivity::class.java))
        }


        radiKadin.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                cinsiyetSecimi = 1
        }

        radioErkek.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                cinsiyetSecimi = 2
        }


        //spinnerYillar()
        //spinnerAylar()


        etDogumTarihi.setOnClickListener {

            Log.d("tiklandi", "etDogumTarihi")

            val simpleDateFormat: SimpleDateFormat
            val simpleDateFormatKullanici: SimpleDateFormat
            simpleDateFormatKullanici = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            simpleDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

            SingleDateAndTimePickerDialog.Builder(this)
                .bottomSheet()
                .curved()
                .displayMinutes(false)
                .displayHours(false)
                .displayDays(false)
                .displayMonth(true)
                .displayYears(true)
                .displayDaysOfMonth(true)
                .displayListener(SingleDateAndTimePickerDialog.DisplayListener {
                    Log.d("seciii", it.date.toString())
                })
                .title("Doğum Tarihi Seçiniz")
                .listener {

                    Log.d("seciii", it.toString())
                    Log.d("seciii", simpleDateFormat.format(it))
                    dogumTarihi = simpleDateFormat.format(it)
                    etDogumTarihi.setText(simpleDateFormatKullanici.format(it))

                }
                .display()

            /*val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    etDogumTarihi.setText("${year.toString()}-${monthOfYear + 1}-$dayOfMonth")
                    dogumTarihi = etDogumTarihi.text.toString()

                },
                year,
                month,
                day
            )

            dpd.show()*/

        }


        dogumTarihiSec.setOnClickListener {

            var simpleDateFormat: SimpleDateFormat
            var simpleDateFormat2: SimpleDateFormat
            simpleDateFormat2 = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            SingleDateAndTimePickerDialog.Builder(this)
                .bottomSheet()
                .curved()
                .displayMinutes(false)
                .displayHours(false)
                .displayDays(false)
                .displayMonth(true)
                .displayYears(true)
                .displayDaysOfMonth(true)
                .displayListener(SingleDateAndTimePickerDialog.DisplayListener {

                    Log.d("seciii", it.date.toString())

                })
                .title("Doğum Tarihi Seçiniz")
                .listener {

                    Log.d("seciii", it.toString())
                    Log.d("seciii", simpleDateFormat.format(it))
                    dogumTarihi = simpleDateFormat.format(it)
                    tvDogumTarihi.text = simpleDateFormat2.format(it)

                }
                .display()

        }

        tvTelefon.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+90${resources.getString(R.string.telefon)}")
            startActivity(intent)
        }


        tvUyeOl.setOnClickListener {

            etAd.setText(Fonk.karakterTemizle(etAd.text.toString()))
            etSoyad.setText(Fonk.karakterTemizle(etSoyad.text.toString()))

            if (etTelefon.text.toString()
                    .isNullOrBlank() && etTelefon.text.toString().length < 14
            ) {
                Fonk.alertDialogGoster(this, "", "Cep telefonu formatı uygun değil")
            } else if (etAd.text.isNullOrBlank()) {
                Fonk.alertDialogGoster(
                    this, "",
                    getString(R.string.ad_bos_olamaz)
                )
            } else if (etSoyad.text.isNullOrBlank()) {
                Fonk.alertDialogGoster(
                    this, "",
                    getString(R.string.soyad_bos)
                )
            } else if (cinsiyetSecimi < 0) {
                Fonk.alertDialogGoster(this, "", "Cinsiyet Seçiniz")
            } else if (!politika1) {
                Fonk.alertDialogGoster(this, "", "Kullanıcı Sözleşmesini Onaylayınız")
            } else if (!politika2) {
                Fonk.alertDialogGoster(this, "", "KVKK Metnini Onaylayınız")
            } else if (Fonk.isNetworkAvailable(this)) {

                telefon = etTelefon.text.toString().replace("(", "")
                telefon = telefon.replace(")", "")
                telefon = telefon.replace("-", "")
                telefon = telefon.replace(" ", "")

                Log.d("cinsiyet", cinsiyetSecimi.toString())


                viewModel.musteriyiKaydetSMS(
                    GlobalDegiskenlerX.g005MusteriKayitSMS,
                    "Kayit",
                    telefon
                )
                observerLiveDataMusteriSMSKayit()

            }

        }


        geri.setOnClickListener {

            kapat()
            // requireDialog().dismiss()
            //requireActivity().supportFragmentManager.popBackStack()
        }

    }


    private fun observerLiveDataMusteriSMSKayit() {
        viewModel.musteriKayitSMS.removeObservers(this)
        viewModel.ServisError.removeObservers(this)
        viewModel.servisYukleniyor.removeObservers(this)

        viewModel.musteriKayitSMS.observe(this, Observer { Cevap ->

            //countries listesi boş olma ihtimali var eğer boş değilse işlem yap diyoruz
            Cevap?.let {

                if (Cevap.success == 1) {

                    Log.d("gelen", Cevap.message)
                    Log.d("gelen_tempass", Cevap.tempass)


                    val mDialogView =
                        LayoutInflater.from(this).inflate(R.layout.sms_kayit_giris_dialog, null)
                    val mBuilder = AlertDialog.Builder(this)
                        .setView(mDialogView)
                    // .setTitle("Login Form")

                    val mAlertDialog = mBuilder.show()
                    mDialogView.tvSmsDogrula.setOnClickListener {


                        val input = mDialogView.etSmsDogrulamaKodu.text.toString()

                        val girilenSms = Fonk.md5(input)


                        if (!secilenYil.isNullOrBlank() && !secilenAy.isNullOrBlank() && !secilenGun.isNullOrBlank())
                            dogumTarihi = secilenYil + "-" + secilenAy + "-" + secilenGun



                        if (girilenSms.equals(Cevap.tempass)) {
                            viewModel.musteriyiKaydetGercek(
                                GlobalDegiskenlerX.g006MusteriKayit,
                                telefon,
                                Fonk.degerGetir(
                                    this,
                                    EnumX.MusteriSifre.toString()
                                ),
                                etAd.text.toString().capitalize(),
                                etSoyad.text.toString().toUpperCase(),
                                cinsiyetSecimi.toString(),
                                dogumTarihi,
                                Fonk.degerGetir(this, EnumX.token.toString())
                            )
                            observerLiveDataMusteriGercekKayit()

                            mAlertDialog.dismiss()

                        } else {
                            Fonk.alertDialogGoster(
                                this,
                                "",
                                "Girdiğiniz doğrulama kodu yanlış yada eksik"
                            )
                        }

                        //set the input text in TextView
                        //  mainInfoTv.setText("Name:"+ name +"\nEmail: "+ email +"\nPassword: "+ password)
                    }
                    //cancel button click of custom layout
                    mDialogView.kapat.setOnClickListener {
                        //dismiss dialog
                        mAlertDialog.dismiss()
                    }



                    Fonk.kayitEkle(this, EnumX.MusteriCep.toString(), telefon)
                    Fonk.kayitEkle(this, EnumX.MusteriSifre.toString(), Cevap.tempass)


                    /*  val alert: AlertDialog.Builder = AlertDialog.Builder(this)
                      alert.setTitle("Doğrulama Kodu")
                      // alert.setIcon(R.drawable.soruyu_cevaplayan2)
                      alert.setCancelable(false)
                      alert.setMessage("Cep telefonunuza gelen mesajdaki doğrulama kodunu bu alana giriniz")

                      val input = EditText(this)
                      alert.setView(input)
                      input.textSize = 16f
                      input.hint = "Doğrulama kodu"
                      input.inputType =
                          InputType.TYPE_NUMBER_FLAG_SIGNED + InputType.TYPE_CLASS_NUMBER

                      alert.setPositiveButton("Gönder",
                          DialogInterface.OnClickListener { dialog, whichButton ->

                              val girilenSms = Fonk.md5(input.editableText.toString())


                              if (girilenSms.equals(Cevap.tempass)) {
                                  viewModel.musteriyiKaydetGercek(
                                      GlobalDegiskenlerX.g006MusteriKayit,
                                      telefon,
                                      Fonk.degerGetir(
                                          this,
                                          EnumX.MusteriSifre.toString()
                                      ),
                                      etAd.text.toString().capitalize(),
                                      etSoyad.text.toString().toUpperCase(),
                                      cinsiyetSecimi.toString(),
                                      dogumTarihi,
                                      Fonk.degerGetir(this, EnumX.token.toString())
                                  )
                                  observerLiveDataMusteriGercekKayit()

                              } else {
                                  Fonk.alertDialogGoster(
                                      this,
                                      "",
                                      "Girdiğiniz doğrulama kodu yanlış yada eksik"
                                  )
                              }


                          })
                      alert.setNegativeButton("İptal",
                          DialogInterface.OnClickListener { dialog, whichButton -> dialog.cancel() })
                      val alertDialog: AlertDialog = alert.create()
                      alertDialog.show()*/


                } else {
                    Fonk.alertDialogGoster(this, "", Cevap.message)
                }

                viewModel.musteriKayitSMS.postValue(null)

            }

        })

        viewModel.ServisError.observe(this, Observer { error ->
            error?.let {
                if (it) {
                    // tvServisError.visibility = View.VISIBLE
                } else {
                    //tvServisError.visibility = View.GONE

                }
            }
        })

        viewModel.servisYukleniyor.observe(this, Observer { loading ->
            loading?.let {
                if (it) {
                    // progresServisYukleniyor.visibility = View.VISIBLE
                    // countryList.visibility = View.GONE
                    // tvServisError.visibility = View.GONE
                } else {
                    // progresServisYukleniyor.visibility = View.GONE
                }
            }
        })
    }

    private fun observerLiveDataMusteriGercekKayit() {
        viewModel.musteriKayitGercek.removeObservers(this)
        viewModel.ServisError.removeObservers(this)
        viewModel.servisYukleniyor.removeObservers(this)

        viewModel.musteriKayitGercek.observe(this, Observer { Cevap ->

            //countries listesi boş olma ihtimali var eğer boş değilse işlem yap diyoruz
            Cevap?.let {

                if (Cevap.success == 1) {

                    Fonk.kayitEkle(this, EnumX.MusteriCep.toString(), telefon)
                    Fonk.kayitEkle(this, EnumX.OturumKodu.toString(), Cevap.oturumKodu)

                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setTitle("Müşteri Kaydınız Oluşturuldu")
                    builder.setMessage(Cevap.message)
                    // builder.setMessage("Üye olduğunuz için teşekkür ederiz, artık alışveriş yapmaya başlayabilirsiniz")
                    builder.setPositiveButton(
                        "Tamam",
                        DialogInterface.OnClickListener { dialogInterface, i ->

                            neYapilacak = "kapat"
                            kapat()
                        })

                    builder.show()

                } else {
                    Fonk.alertDialogGoster(this, "", Cevap.message)
                    Fonk.kayitEkle(this, EnumX.MusteriCep.toString(), "")
                    Fonk.kayitEkle(this, EnumX.OturumKodu.toString(), "")

                }

                viewModel.musteriKayitGercek.postValue(null)

            }

        })

        viewModel.ServisError.observe(this, Observer { error ->
            error?.let {
                if (it) {
                    // tvServisError.visibility = View.VISIBLE
                } else {
                    //tvServisError.visibility = View.GONE

                }
            }
        })

        viewModel.servisYukleniyor.observe(this, Observer { loading ->
            loading?.let {
                if (it) {
                    // progresServisYukleniyor.visibility = View.VISIBLE
                    // countryList.visibility = View.GONE
                    // tvServisError.visibility = View.GONE
                } else {
                    // progresServisYukleniyor.visibility = View.GONE
                }
            }
        })
    }


    fun spinnerYillar() {

        val spinnerValue = ArrayList<String>()
        spinnerValue.add("Yıl Seçiniz")

        var secilecekPosition = 0

        val yil = 1930..2020

        for (k in yil) {
            spinnerValue.add(k.toString())
        }

        val adapter = ArrayAdapter(this, R.layout.spinner_item_dogum_tarihi, spinnerValue)
        spinnerYil.adapter = adapter

        spinnerYil.setSelection(secilecekPosition)

        spinnerYil.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                if (position != 0) {
                    Log.d("seçilen_value", spinnerValue[position])
                    secilenYil = spinnerValue[position]

                    if (!secilenYil.isNullOrBlank() && !secilenAy.isNullOrBlank()) {
                        spinnerGunler()
                    }
                    // ilceleriGetir()
                    // sipinnerMahalle.visibility = View.INVISIBLE

                } else {
                    secilenYil = ""
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                secilenYil = ""
            }
        }
    }

    fun spinnerAylar() {

        val spinnerValue = ArrayList<String>()
        spinnerValue.add("Ay Seçiniz")

        var secilecekPosition = 0

        val ay = 1..12

        for (k in ay) {
            spinnerValue.add(k.toString())
        }

        val adapter = ArrayAdapter(this, R.layout.spinner_item_dogum_tarihi, spinnerValue)
        spinnerAy.adapter = adapter

        spinnerAy.setSelection(secilecekPosition)

        spinnerAy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                if (position != 0) {
                    Log.d("seçilen_value", spinnerValue[position])
                    secilenAy = spinnerValue[position]

                    if (!secilenYil.isNullOrBlank() && !secilenAy.isNullOrBlank()) {
                        spinnerGunler()
                    }

                    // ilceleriGetir()
                    // sipinnerMahalle.visibility = View.INVISIBLE

                } else {
                    secilenAy = ""
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                secilenAy = ""
            }
        }
    }

    fun spinnerGunler() {

        val spinnerValue = ArrayList<String>()
        spinnerValue.add("Gün Seçiniz")

        val yilX = Fonk.inteCevir(secilenYil)
        val ayX = Fonk.inteCevir(secilenAy)

        val takvim = GregorianCalendar(yilX, ayX - 1, 1) //Ocak=0
        System.out.println("2020 Ocak ayının gün sayısı:" + takvim.getActualMaximum(Calendar.DAY_OF_MONTH))


        var secilecekPosition = 0

        val gun = 1..takvim.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (k in gun) {
            spinnerValue.add(k.toString())
        }

        val adapter = ArrayAdapter(this, R.layout.spinner_item_dogum_tarihi, spinnerValue)
        spinnerGun.adapter = adapter

        spinnerGun.setSelection(secilecekPosition)

        spinnerGun.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                if (position != 0) {
                    Log.d("seçilen_value", spinnerValue[position])
                    secilenGun = spinnerValue[position]


                } else {
                    secilenGun = ""
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                secilenGun = ""
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.sola_gidis1, R.anim.sola_gidis2)
    }

    private fun kapat() {

        val resultIntent = Intent()
        resultIntent.putExtra("sonuc", neYapilacak)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }


}