package com.imalat.beeSystem.view.profil

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.GeldiginEkraniKapatInterface
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.GirisViewModel
import kotlinx.android.synthetic.main.fragment_sms_gir.*


class SMSGirFragment : DialogFragment() {

    private lateinit var viewModel: GirisViewModel
    lateinit var geldiginEkraniKapatInterface: GeldiginEkraniKapatInterface
    var gelenCodeX = ""

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.geldiginEkraniKapatInterface = context as GeldiginEkraniKapatInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { gelenCodeX = it.getString("param1").toString() }
        viewModel = ViewModelProvider(this@SMSGirFragment).get(GirisViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireDialog().getWindow()!!.getAttributes().windowAnimations =
            R.style.alttan_gir_alttan_cik
        return inflater.inflate(R.layout.fragment_sms_gir, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireDialog().getWindow()!!.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        tvSmsDogrula.setOnClickListener {

            val girilenSms = Fonk.md5(etSmsDogrulamaKodu.text.toString())

            if (girilenSms.equals(
                    Fonk.degerGetir(
                        requireContext(),
                        EnumX.MusteriSifre.toString()
                    )
                )
            ) {


                viewModel.musteriGirisGercekX(
                    GlobalDegiskenlerX.g008MusteriLogin,
                    "Login",
                    Fonk.degerGetir(requireContext(), EnumX.MusteriCep.toString()),
                    Fonk.degerGetir(requireContext(), EnumX.MusteriSifre.toString()),
                    gelenCodeX,
                    Fonk.degerGetir(requireContext(), EnumX.token.toString())
                )

                observerLiveDataMusteriGercekGiris()

            } else {
                Fonk.alertDialogGoster(
                    requireContext(),
                    "",
                    "Girdiğiniz doğrulama kodu yanlış yada eksik"
                )
            }

        }


        kapat.setOnClickListener {

            Fonk.kayitEkle(requireContext(), EnumX.MusteriCep.toString(), "")
            requireDialog().dismiss()
        }


    }

    companion object {
        @JvmStatic
        fun newInstance(gelenParametre: String) = SMSGirFragment().apply {
            arguments = Bundle().apply {
                putString("param1", gelenParametre)
            }
        }
    }


    private fun observerLiveDataMusteriGercekGiris() {
        viewModel.musteriGirisGercek.removeObservers(viewLifecycleOwner)
        viewModel.ServisError.removeObservers(viewLifecycleOwner)
        viewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)

        viewModel.musteriGirisGercek.observe(viewLifecycleOwner, Observer { Cevap ->

            //countries listesi boş olma ihtimali var eğer boş değilse işlem yap diyoruz
            Cevap?.let {

                if (Cevap.success == 1) {


                    //Log.d("gelen", Cevap.message)
                    // Fonk.alertDialogGoster(this, "", Cevap.message)

                    Fonk.kayitEkle(
                        requireContext(),
                        EnumX.OturumKodu.toString(),
                        Cevap.loginJSON[0].oturumKodu
                    )
                    Fonk.kayitEkle(
                        requireContext(),
                        EnumX.KullaniciAdi.toString(),
                        Cevap.loginJSON[0].adi + " " + Cevap.loginJSON[0].soyadi
                    )
                    Toast.makeText(requireContext(), Cevap.message, Toast.LENGTH_LONG).show()


                } else {
                    //Fonk.kayitEkle(requireContext(), EnumX.MusteriCep.toString(), "")

                    Toast.makeText(requireContext(), Cevap.message, Toast.LENGTH_LONG).show()

                }

                viewModel.musteriGirisGercek.postValue(null)

                requireDialog().dismiss()
                geldiginEkraniKapatInterface.kapat()

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

}