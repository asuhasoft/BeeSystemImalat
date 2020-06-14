package com.imalat.beeSystem.view.profil

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.GeldiginEkraniKapatInterface
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.UyelOlViewModel
import kotlinx.android.synthetic.main.fragment_uye_ol.*
import kotlinx.android.synthetic.main.sms_kayit_giris_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*


class UyeOlFragmentIptal : DialogFragment() {

    private var secilenDogumTarihi: String = ""
    private lateinit var viewModel: UyelOlViewModel
    lateinit var geldiginEkraniKapatInterface: GeldiginEkraniKapatInterface

    private var telefon = ""
    private var politika1 = false
    private var politika2 = false
    private var cinsiyetSecimi = -1
    private var dogumTarihi = ""
    private var secilenYil = ""
    private var secilenAy = ""
    private var secilenGun = ""

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UyelOlViewModel::class.java)
        this.geldiginEkraniKapatInterface = context as GeldiginEkraniKapatInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_uye_ol, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etTelefon.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        checkboxPolitika1.setOnCheckedChangeListener { buttonView, isChecked ->
            politika1 = isChecked
        }

        checkboxPolitika2.setOnCheckedChangeListener { buttonView, isChecked ->
            politika2 = isChecked
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

            SingleDateAndTimePickerDialog.Builder(requireContext())
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
                requireContext(),
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

            SingleDateAndTimePickerDialog.Builder(requireContext())
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
                    tvDogumTarihi.setText(simpleDateFormat2.format(it))

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
                Fonk.alertDialogGoster(requireContext(), "", "Cep telefonu formatı uygun değil")
            } else if (etAd.text.isNullOrBlank()) {
                Fonk.alertDialogGoster(
                    requireContext(), "",
                    getString(R.string.ad_bos_olamaz)
                )
            } else if (etSoyad.text.isNullOrBlank()) {
                Fonk.alertDialogGoster(
                    requireContext(), "",
                    getString(R.string.soyad_bos)
                )
            } else if (cinsiyetSecimi < 0) {
                Fonk.alertDialogGoster(requireContext(), "", "Cinsiyet Seçiniz")
            } else if (!politika1) {
                Fonk.alertDialogGoster(requireContext(), "", "Kullanıcı Sözleşmesini Onaylayınız")
            } else if (!politika2) {
                Fonk.alertDialogGoster(requireContext(), "", "KVKK Metnini Onaylayınız")
            } else if (Fonk.isNetworkAvailable(requireContext())) {

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

            requireDialog().dismiss()
            //requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun observerLiveDataMusteriSMSKayit() {
        viewModel.musteriKayitSMS.removeObservers(viewLifecycleOwner)
        viewModel.ServisError.removeObservers(viewLifecycleOwner)
        viewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)

        viewModel.musteriKayitSMS.observe(viewLifecycleOwner, Observer { Cevap ->

            //countries listesi boş olma ihtimali var eğer boş değilse işlem yap diyoruz
            Cevap?.let {

                if (Cevap.success == 1) {

                    Log.d("gelen", Cevap.message)
                    Log.d("gelen_tempass", Cevap.tempass)


                    val mDialogView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.sms_kayit_giris_dialog, null)
                    val mBuilder = AlertDialog.Builder(requireContext())
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
                                    requireContext(),
                                    EnumX.MusteriSifre.toString()
                                ),
                                etAd.text.toString().capitalize(),
                                etSoyad.text.toString().toUpperCase(),
                                cinsiyetSecimi.toString(),
                                dogumTarihi,
                                Fonk.degerGetir(requireContext(), EnumX.token.toString())
                            )
                            observerLiveDataMusteriGercekKayit()

                            mAlertDialog.dismiss()

                        } else {
                            Fonk.alertDialogGoster(
                                requireContext(),
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



                    Fonk.kayitEkle(requireContext(), EnumX.MusteriCep.toString(), telefon)
                    Fonk.kayitEkle(requireContext(), EnumX.MusteriSifre.toString(), Cevap.tempass)


                    /*  val alert: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                      alert.setTitle("Doğrulama Kodu")
                      // alert.setIcon(R.drawable.soruyu_cevaplayan2)
                      alert.setCancelable(false)
                      alert.setMessage("Cep telefonunuza gelen mesajdaki doğrulama kodunu bu alana giriniz")

                      val input = EditText(requireContext())
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
                                          requireContext(),
                                          EnumX.MusteriSifre.toString()
                                      ),
                                      etAd.text.toString().capitalize(),
                                      etSoyad.text.toString().toUpperCase(),
                                      cinsiyetSecimi.toString(),
                                      dogumTarihi,
                                      Fonk.degerGetir(requireContext(), EnumX.token.toString())
                                  )
                                  observerLiveDataMusteriGercekKayit()

                              } else {
                                  Fonk.alertDialogGoster(
                                      requireContext(),
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
                    Fonk.alertDialogGoster(requireContext(), "", Cevap.message)
                }

                viewModel.musteriKayitSMS.postValue(null)

            }

        })

        viewModel.ServisError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    // tvServisError.visibility = View.VISIBLE
                } else {
                    //tvServisError.visibility = View.GONE

                }
            }
        })

        viewModel.servisYukleniyor.observe(viewLifecycleOwner, Observer { loading ->
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
        viewModel.musteriKayitGercek.removeObservers(viewLifecycleOwner)
        viewModel.ServisError.removeObservers(viewLifecycleOwner)
        viewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)

        viewModel.musteriKayitGercek.observe(viewLifecycleOwner, Observer { Cevap ->

            //countries listesi boş olma ihtimali var eğer boş değilse işlem yap diyoruz
            Cevap?.let {

                if (Cevap.success == 1) {

                    Fonk.kayitEkle(requireContext(), EnumX.MusteriCep.toString(), telefon)
                    Fonk.kayitEkle(requireContext(), EnumX.OturumKodu.toString(), Cevap.oturumKodu)

                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Müşteri Kaydınız Oluşturuldu")
                    builder.setMessage(Cevap.message)
                    // builder.setMessage("Üye olduğunuz için teşekkür ederiz, artık alışveriş yapmaya başlayabilirsiniz")
                    builder.setPositiveButton(
                        "Tamam",
                        DialogInterface.OnClickListener { dialogInterface, i ->

                            geldiginEkraniKapatInterface.kapat()
                            requireDialog().dismiss()
                        })

                    builder.show()

                } else {
                    Fonk.alertDialogGoster(requireContext(), "", Cevap.message)
                    Fonk.kayitEkle(requireContext(), EnumX.MusteriCep.toString(), "")
                    Fonk.kayitEkle(requireContext(), EnumX.OturumKodu.toString(), "")

                }

                viewModel.musteriKayitGercek.postValue(null)

            }

        })

        viewModel.ServisError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    // tvServisError.visibility = View.VISIBLE
                } else {
                    //tvServisError.visibility = View.GONE

                }
            }
        })

        viewModel.servisYukleniyor.observe(viewLifecycleOwner, Observer { loading ->
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

        val adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_item_dogum_tarihi, spinnerValue)
        spinnerYil.setAdapter(adapter)

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

        val adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_item_dogum_tarihi, spinnerValue)
        spinnerAy.setAdapter(adapter)

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

        val takvim = GregorianCalendar(yilX, ayX - 1, 1); //Ocak=0
        System.out.println("2020 Ocak ayının gün sayısı:" + takvim.getActualMaximum(Calendar.DAY_OF_MONTH));


        var secilecekPosition = 0

        val gun = 1..takvim.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (k in gun) {
            spinnerValue.add(k.toString())
        }

        val adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_item_dogum_tarihi, spinnerValue)
        spinnerGun.setAdapter(adapter)

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

    companion object {

        @JvmStatic
        fun newInstance() =
            UyeOlFragmentIptal().apply {}
    }
}