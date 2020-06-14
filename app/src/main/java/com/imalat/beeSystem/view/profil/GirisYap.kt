package com.imalat.beeSystem.view.profil

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.GeldiginEkraniKapatInterface
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.GirisViewModel
import kotlinx.android.synthetic.main.activity_giris.*

class GirisYap : AppCompatActivity(), GeldiginEkraniKapatInterface {


    private lateinit var viewModel: GirisViewModel
    var telefon = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giris)
        overridePendingTransition(R.anim.saga_gidis1, R.anim.saga_gidis2)


        viewModel = ViewModelProvider(this).get(GirisViewModel::class.java)

        etTelefon.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        tvGirisYap.setOnClickListener {

            telefon = etTelefon.text.toString().replace("(", "")
            telefon = telefon.replace(")", "")
            telefon = telefon.replace("-", "")
            telefon = telefon.replace(" ", "")


            if (etTelefon.text.toString().isNullOrBlank() || telefon.length != 10) {
                Fonk.alertDialogGoster(
                    this,
                    "",
                    "Cep telefonu formatı uygun değil, numaranızın başında sıfır olmadan giriniz.\nÖrnek: 5071234567"
                )
            } else {

                viewModel.musteriGirisSMSX(GlobalDegiskenlerX.g007MusteriLoginSMS, "Login", telefon)
                observerLiveDataMusteriSMSGiris()
            }


        }

        tvTelefon.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+90${resources.getString(R.string.telefon)}")
            startActivity(intent)
        }



        tvUyeOl.setOnClickListener {

            if (Fonk.isNetworkAvailable(this)) {
                val i = Intent(this, UyeOl::class.java)
                startActivityForResult(i, 222)
            }

            /* val frg = UyeOlFragment
             frg.newInstance().show(supportFragmentManager, "ad")
             */
        }

        geri.setOnClickListener {
            finish()
        }
    }


    private fun observerLiveDataMusteriSMSGiris() {
        viewModel.musteriGirisSMS.removeObservers(this)
        viewModel.ServisError.removeObservers(this)
        viewModel.servisYukleniyor.removeObservers(this)

        viewModel.musteriGirisSMS.observe(this, Observer { Cevap ->

            //countries listesi boş olma ihtimali var eğer boş değilse işlem yap diyoruz
            Cevap?.let {

                if (Cevap.success == 1) {

                    //Log.d("gelen", Cevap.message)
                    Log.d("gelen_tempass", Cevap.tempass)



                    Fonk.kayitEkle(this, EnumX.MusteriCep.toString(), telefon)
                    Fonk.kayitEkle(this, EnumX.MusteriSifre.toString(), Cevap.tempass)


                    val frg = SMSGirFragment
                    frg.newInstance(Cevap.codex).show(supportFragmentManager, "ad")


                    /* val alert: AlertDialog.Builder = AlertDialog.Builder(this)
                     alert.setTitle("Doğrulama Kodu")
                     // alert.setIcon(R.drawable.soruyu_cevaplayan2)
                     alert.setCancelable(false)
                     alert.setMessage("${telefon} numaralı telefonunuza gelen mesajdaki doğrulama kodunu bu alana giriniz")

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
                                 viewModel.musteriGirisGercekX(
                                     GlobalDegiskenlerX.g008MusteriLogin,
                                     "Login",
                                     telefon,
                                     Fonk.degerGetir(this, EnumX.MusteriSifre.toString()),
                                     Cevap.codex,
                                     Fonk.degerGetir(this,EnumX.token.toString())

                                 )
                                 observerLiveDataMusteriGercekGiris()

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
                     alertDialog.show()

                     */


                } else {
                    Fonk.alertDialogGoster(this, "", Cevap.message)
                }

                viewModel.musteriGirisSMS.postValue(null)

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

    private fun observerLiveDataMusteriGercekGiris() {
        viewModel.musteriGirisGercek.removeObservers(this)
        viewModel.ServisError.removeObservers(this)
        viewModel.servisYukleniyor.removeObservers(this)

        viewModel.musteriGirisGercek.observe(this, Observer { Cevap ->

            //countries listesi boş olma ihtimali var eğer boş değilse işlem yap diyoruz
            Cevap?.let {

                if (Cevap.success == 1) {

                    Fonk.kayitEkle(this, EnumX.MusteriCep.toString(), telefon)

                    //Log.d("gelen", Cevap.message)
                    // Fonk.alertDialogGoster(this, "", Cevap.message)

                    Toast.makeText(this, Cevap.message, Toast.LENGTH_LONG).show()

                    finish()

                } else {
                    Fonk.kayitEkle(this, EnumX.MusteriCep.toString(), "")

                    Fonk.alertDialogGoster(this, "", Cevap.message)
                }

                viewModel.musteriGirisGercek.postValue(null)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 222) {
            if (resultCode == Activity.RESULT_OK) {

                data?.let {

                    if (data.getStringExtra("sonuc").equals("kapat"))
                        finish()
                }

            }
            if (resultCode == RESULT_CANCELED) {

            }
        } else {

        }
    }

    //sms fragmentte kullanılıyor
    override fun kapat() {
        finish()
    }

}