package com.imalat.beeSystem.view.profil

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.imalat.beeSystem.R
import com.imalat.beeSystem.model.profil.profilBilgileri.ProfilJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.view.diger.BilgilendirmeActivity
import com.imalat.beeSystem.view.profil.adres.Adreslerim
import com.imalat.beeSystem.viewModel.ProfilViewModel
import kotlinx.android.synthetic.main.activity_profilim.*
import java.text.SimpleDateFormat
import java.util.*

class Profilim : AppCompatActivity() {

    private lateinit var viewModel: ProfilViewModel
    var cinsiyetSecimi = -1

    private var dogumTarihi = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilim)
        overridePendingTransition(R.anim.saga_gidis1, R.anim.saga_gidis2)

        viewModel = ViewModelProvider(this).get(ProfilViewModel::class.java)


        if (Fonk.isNetworkAvailable(this)) {

            viewModel.profilBilgileriGetir(
                GlobalDegiskenlerX.g009MusteriProfil,
                Fonk.degerGetir(this, EnumX.OturumKodu.toString())
            )
            observerLiveDataProfilGetir()
        }


        radiKadin.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                cinsiyetSecimi = 1
        }

        radioErkek.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                cinsiyetSecimi = 2
        }

        checkboxPolitika1.setOnClickListener {

            if (Fonk.isNetworkAvailable(this))
                startActivity(Intent(this, BilgilendirmeActivity::class.java))
        }

        checkboxPolitika2.setOnClickListener {

            if (Fonk.isNetworkAvailable(this))
                startActivity(Intent(this, BilgilendirmeActivity::class.java))
        }


        etDogumTarihi.setOnClickListener {


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

                    //Log.d("seciii", it.date.toString())

                })
                .title("Doğum Tarihi Seçiniz")
                .listener {

                    //Log.d("seciii", it.toString())
                    //Log.d("seciii", simpleDateFormat.format(it))
                    dogumTarihi = simpleDateFormat.format(it)
                    etDogumTarihi.setText(simpleDateFormatKullanici.format(it))

                }
                .display()

            /* val c = Calendar.getInstance()
             val year = c.get(Calendar.YEAR)
             val month = c.get(Calendar.MONTH)
             val day = c.get(Calendar.DAY_OF_MONTH)
             val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                 etDogumTarihi.setText("${year.toString()}-${monthOfYear + 1}-$dayOfMonth")

                 //dogumTarihi = etDogumTarihi.text.toString()
             }, year, month, day)

             dpd.show()*/

        }

        tvGuncelle.setOnClickListener {

            viewModel.profilGuncelle(
                GlobalDegiskenlerX.g010ProfilGuncelle,
                "ProfilGuncelle",
                Fonk.degerGetir(this, EnumX.OturumKodu.toString()),
                etAd.text.toString(),
                etSoyad.text.toString().toUpperCase(),
                dogumTarihi,
                cinsiyetSecimi.toString()
            )

            observerLiveDataProfilGuncelle()


        }


        tvAdreslerim.setOnClickListener {

            val i = Intent(this, Adreslerim::class.java)
            i.putExtra("nerdenCagrildi", "Profilim")
            startActivity(i)
        }

        tvOturumuKapat.setOnClickListener {

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle(this.resources.getString(R.string.uyari))
            builder.setMessage("Oturumu kapatmak istiyor musunuz?")
            builder.setNegativeButton(
                this.resources.getString(R.string.evet),
                DialogInterface.OnClickListener { dialogInterface, i ->

                    Fonk.kayitEkle(this, EnumX.OturumKodu.toString(), "")
                    Fonk.kayitEkle(this, EnumX.MusteriCep.toString(), "")
                    Fonk.kayitEkle(this, EnumX.MusteriSifre.toString(), "")

                    finish()

                })
            builder.setPositiveButton(
                this.resources.getString(R.string.iptal),
                DialogInterface.OnClickListener { dialogInterface, i ->

                })

            builder.show()

        }

        geri.setOnClickListener {
            finish()
        }
    }


    private fun observerLiveDataProfilGetir() {
        viewModel.profilBilgileri.removeObservers(this)
        viewModel.ServisError.removeObservers(this)
        viewModel.servisYukleniyor.removeObservers(this)

        viewModel.profilBilgileri.observe(this, Observer { cevap ->

            cevap?.let {

                if (cevap.success == 1) {

                    val list: List<ProfilJSON> = cevap.profilJSON
                    //Log.d("gelen", cevap.message)

                    if (list != null && list.size > 0) {

                        for (x in 0 until list.size) {
                            //sira bilgisi manuel ekleniyor
                            // list[x].sira = (x+1).toString()
                            // //Log.d("tag", "adi " + list[x].adi)

                            etTelefon.setText(list[x].telefon)
                            etAd.setText(list[x].adi)
                            etSoyad.setText(list[x].soyadi)
                            etDogumTarihi.setText(list[x].dogumTarihi)

                            Fonk.kayitEkle(
                                this,
                                EnumX.KullaniciAdi.toString(),
                                list[x].adi + " " + list[x].soyadi
                            )

                            if (list[x].cinsiyetID.equals("1"))
                                radiKadin.isChecked = true
                            else if (list[x].cinsiyetID.equals("2"))
                                radioErkek.isChecked = true
                            //Log.d("tag", "---- -----------------")
                        }

                    } else {
                        //Log.d("size ", "profil arrray boş")
                    }

                } else {
                    Fonk.kayitEkle(this, EnumX.OturumKodu.toString(), "")
                    Fonk.kayitEkle(this, EnumX.MusteriCep.toString(), "")

                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setTitle(this.resources.getString(R.string.uyari))
                    builder.setMessage(cevap.message)
                    builder.setPositiveButton(
                        "Tamam",
                        DialogInterface.OnClickListener { dialogInterface, i ->

                            finish()

                        })

                    builder.show()


                }

                viewModel.profilBilgileri.postValue(null)

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

    private fun observerLiveDataProfilGuncelle() {
        viewModel.profilGuncelle.removeObservers(this)
        viewModel.ServisError.removeObservers(this)
        viewModel.servisYukleniyor.removeObservers(this)

        viewModel.profilGuncelle.observe(this, Observer { cevap ->

            //countries listesi boş olma ihtimali var eğer boş değilse işlem yap diyoruz
            cevap?.let {

                if (cevap.success == 1) {


                    //Log.d("gelen", cevap.message)
                    Fonk.alertDialogGoster(this, "", cevap.message)

                } else {

                    Fonk.alertDialogGoster(this, "", cevap.message)
                }

                viewModel.profilGuncelle.postValue(null)

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

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.sola_gidis1, R.anim.sola_gidis2)
    }
}