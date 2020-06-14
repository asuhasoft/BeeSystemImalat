package com.imalat.beeSystem.view.sepet.siparisOzeti

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.SiparisOzetiSecimleriInterface
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.view.profil.adres.Adreslerim
import com.imalat.beeSystem.viewModel.AnaSayfaViewModel
import com.imalat.beeSystem.viewModel.GeldiginEkraniKapatViewModel
import com.imalat.beeSystem.viewModel.SiparisOzetiViewModel
import kotlinx.android.synthetic.main.activity_siparis_ozeti.*


class SiparisOzetiFragment : Fragment(R.layout.activity_siparis_ozeti),
    SiparisOzetiSecimleriInterface {

    private var neYapilacak: String = "sadeceGeriGit"
    private lateinit var viewModel: SiparisOzetiViewModel
    // private lateinit var geldiginEkraniKapatInterface: GeldiginEkraniKapatInterface

    private var minumumSiparisTutari = "0.0" // adrese göre
    private var servisUcreti = "0.0" //
    private var genelToplam: Double = 0.0
    private var zilCalinmasinMi = "0" // zil çalınacak
    private var sepetToplamTutari: Double = 0.0
    private lateinit var anaSayfaViewModel: AnaSayfaViewModel

    private lateinit var geldiginEkraniKapatViewModel: GeldiginEkraniKapatViewModel


    /*override fun getTheme(): Int {
        return R.style.DialogTheme
    }*/

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // this.geldiginEkraniKapatInterface = context as GeldiginEkraniKapatInterface
        Log.d("SiparisOzetiFragment", "onAttach")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SiparisOzetiFragment", "onCreate")
        geldiginEkraniKapatViewModel =
            ViewModelProvider(requireActivity()).get(GeldiginEkraniKapatViewModel::class.java)

        anaSayfaViewModel = ViewModelProvider(this).get(AnaSayfaViewModel::class.java)
        viewModel = ViewModelProvider(this).get(SiparisOzetiViewModel::class.java)

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //requireDialog().getWindow()!!.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        Log.d("SiparisOzetiFragment", "onActivityCreated")

        if (Fonk.isNetworkAvailable(requireContext()))
            sepetToplamTutariGetirPOST()


        teslimatAdresim.setOnClickListener {
            val i = Intent(requireContext(), Adreslerim::class.java)
            i.putExtra("nerdenCagrildi", "SiparisOzeti")
            startActivity(i)
        }

        teslimatZamani.setOnClickListener {

            val i = Intent(requireContext(), TeslimatZamaniSec::class.java)
            startActivity(i)
            /*  val frg = TeslimatZamaniSecFragment
              frg.newInstance().show((requireContext() as AppCompatActivity).supportFragmentManager, "")
              */
        }

        odemeYontemi.setOnClickListener {

            val i = Intent(requireContext(), OdemeYontemiSec::class.java)
            startActivity(i)

            /* val frg = OdemeYontemiSecFragment
             frg.newInstance().show((requireContext() as AppCompatActivity).supportFragmentManager, "")*/
        }

        teslimatTipi.setOnClickListener {

            startActivity(Intent(requireContext(), TeslimatTipiSec::class.java))
            /* val frg = TeslimatTipiSecFragment
             frg.newInstance()
                 .show((requireContext() as AppCompatActivity).supportFragmentManager, "")*/
        }


        siparisNotu.setOnClickListener {
            val i = Intent(requireContext(), SiparisNotu::class.java)
            i.putExtra("siparisNotu", tvSiparisNotu.text.toString())
            startActivity(i)
        }


        geri.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }


        zilAyariCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked)
                zilCalinmasinMi = "1"
            else
                zilCalinmasinMi = "0"
        }

        siparisiOnayla.setOnClickListener {

            if (Fonk.isNetworkAvailable(requireContext())) {


                if (Fonk.degerGetir(requireContext(), EnumX.secilenAdresID.toString())
                        .isNullOrBlank()
                )
                    Fonk.alertDialogGoster(requireContext(), "", "Teslimat adresi Seçiniz")
                else if (Fonk.degerGetir(requireContext(), EnumX.ServisPlanID.toString())
                        .isNullOrBlank()
                )
                    Fonk.alertDialogGoster(requireContext(), "", "Teslimat Zamanı Seçiniz")
                else if (Fonk.degerGetir(requireContext(), EnumX.OdemeTipID.toString())
                        .isNullOrBlank()
                )
                    Fonk.alertDialogGoster(requireContext(), "", "Ödeme Tipi Seçiniz")
                else if (Fonk.degerGetir(requireContext(), EnumX.TeslimatTipID.toString())
                        .isNullOrBlank()
                )
                    Fonk.alertDialogGoster(requireContext(), "", "Teslimat Tipi Seçiniz")
                else {
                    if (genelToplam > Fonk.doubleCevir(minumumSiparisTutari)) {


                        viewModel.siparisGonder(
                            GlobalDegiskenlerX.g020SiparisOlustur,
                            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString()),
                            Fonk.degerGetir(requireContext(), EnumX.secilenAdresID.toString()),
                            Fonk.degerGetir(requireContext(), EnumX.OdemeTipID.toString()),
                            Fonk.degerGetir(requireContext(), EnumX.ServisPlanID.toString()),
                            Fonk.degerGetir(requireContext(), EnumX.TeslimatTipID.toString()),
                            tvSiparisNotu.text.toString(),
                            zilCalinmasinMi
                        )

                        observerLiveDataSiparisGonder()
                    } else {
                        Fonk.alertDialogGoster(
                            requireContext(), "",
                            "Belirlediğiniz adres için minumum sipariş ücreti ${Fonk.sayiFormatla(
                                Fonk.doubleCevir(
                                    minumumSiparisTutari
                                )
                            )} TL olup Genel Toplam ödemenizden düşük olduğu için sipariş alınamamaktadır."
                        )
                    }
                }


            }

        }


    }


    override fun onStart() {
        super.onStart()
        Log.d("SiparisOzetiFragment", "onStart")


    }

    override fun onResume() {
        super.onResume()
        Log.d("SiparisOzetiFragment", "onResume")

        hesaplamaYap()

    }


    fun hesaplamaYap() {

        //adress
        tvTeslimatAdresimBaslik.text =
            Fonk.degerGetir(requireContext(), EnumX.secilenAdresBaslik.toString())
        tvTeslimatAdresim.text = Fonk.degerGetir(requireContext(), EnumX.secilenAdres.toString())
        minumumSiparisTutari = Fonk.degerGetir(requireContext(), EnumX.minSiparisTutari.toString())
        servisUcreti = Fonk.degerGetir(requireContext(), EnumX.servisUcreti.toString())
        val ucretsizServisTutari =
            Fonk.degerGetir(requireContext(), EnumX.ucretsizServis.toString())

        Log.d("SiparisOzetiFragment", "onResume" + ucretsizServisTutari)

        tvMinumumSiparisTutari.text = "Minumum Sipariş Ücreti: " + minumumSiparisTutari + " TL"

        tvTeslimatUcreti.text = servisUcreti + " TL"


        //ücretsiz servis tutarindan büyük olduğu için servis ücreti alınmıyor
        if (sepetToplamTutari > Fonk.doubleCevir(ucretsizServisTutari)) {

            if (!ucretsizServisTutari.isNullOrBlank())
                tvTeslimatUcreti.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            genelToplam = sepetToplamTutari
        } else {
            genelToplam = Fonk.doubleCevir(servisUcreti) + sepetToplamTutari
        }
        tvGenelToplam.text = Fonk.sayiFormatla(genelToplam) + " TL"


        tvTeslimatZamani.text = Fonk.degerGetir(requireContext(), EnumX.ServisAd.toString())
        tvOdemeYontemi.text = Fonk.degerGetir(requireContext(), EnumX.OdemeTipi.toString())
        tvTeslimatTipi.text = Fonk.degerGetir(requireContext(), EnumX.TeslimatTipi.toString())
        tvSiparisNotu.text = Fonk.degerGetir(requireContext(), EnumX.SiparisNotum.toString())
    }


    override fun onPause() {
        super.onPause()
        Log.d("SiparisOzetiFragment", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("SiparisOzetiFragment", "onStop")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("SiparisOzetiFragment", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SiparisOzetiFragment", "onDestroy")

    }

    override fun onDetach() {
        super.onDetach()

        geldiginEkraniKapatViewModel.setUrunAdet(neYapilacak)

        // geldiginEkraniKapatViewModel
        Log.d("SiparisOzetiFragment", "onDetach")
    }


    private fun observerLiveDataSiparisGonder() {

        viewModel.siparisGonder.removeObservers(viewLifecycleOwner)
        viewModel.ServisError.removeObservers(viewLifecycleOwner)
        viewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)

        viewModel.siparisGonder.observe(viewLifecycleOwner, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    varsayilanSecimleriSifirla()

                    val alert: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    alert.setTitle("Teşekküler, Siparişinz Alındı")
                    // alert.setIcon(R.drawable.soruyu_cevaplayan2)
                    alert.setCancelable(false)
                    alert.setMessage(cevap.message)

                    alert.setPositiveButton("Tamam",
                        DialogInterface.OnClickListener { dialog, whichButton ->

                            neYapilacak = "kapat"
                            requireActivity().supportFragmentManager.popBackStack()

                            //geldiginEkraniKapatInterface.kapat()

                        })

                    val alertDialog: AlertDialog = alert.create()
                    alertDialog.show()


                } else {
                    Fonk.alertDialogGoster(requireContext(), "", cevap.message)

                }

                viewModel.siparisGonder.postValue(null)

            }

        })
    }

    private fun varsayilanSecimleriSifirla() {

        Fonk.kayitEkle(requireContext(), EnumX.secilenAdresID.toString(), "")
        Fonk.kayitEkle(requireContext(), EnumX.secilenAdresBaslik.toString(), "")
        Fonk.kayitEkle(requireContext(), EnumX.secilenAdres.toString(), "")
        Fonk.kayitEkle(requireContext(), EnumX.minSiparisTutari.toString(), "")
        Fonk.kayitEkle(requireContext(), EnumX.servisUcreti.toString(), "")
        Fonk.kayitEkle(requireContext(), EnumX.ucretsizServis.toString(), "")

        Fonk.kayitEkle(requireContext(), EnumX.ServisPlanID.toString(), "")
        Fonk.kayitEkle(requireContext(), EnumX.ServisAd.toString(), "")

        Fonk.kayitEkle(requireContext(), EnumX.OdemeTipID.toString(), "")
        Fonk.kayitEkle(requireContext(), EnumX.OdemeTipi.toString(), "")

        Fonk.kayitEkle(requireContext(), EnumX.TeslimatTipID.toString(), "")
        Fonk.kayitEkle(requireContext(), EnumX.TeslimatTipi.toString(), "")

        Fonk.kayitEkle(requireContext(), EnumX.SiparisNotum.toString(), "")
    }

    override fun siparisOzetiguncelle() {
        tvTeslimatZamani.text = Fonk.degerGetir(requireContext(), EnumX.ServisAd.toString())
        tvOdemeYontemi.text = Fonk.degerGetir(requireContext(), EnumX.OdemeTipi.toString())
        tvTeslimatTipi.text = Fonk.degerGetir(requireContext(), EnumX.TeslimatTipi.toString())
        tvSiparisNotu.text = Fonk.degerGetir(requireContext(), EnumX.SiparisNotum.toString())
    }


    private fun sepetToplamTutariGetirPOST() {
        Log.d("badgeAyarla", "çalıştı")


        anaSayfaViewModel.sepetToplamiGetir(
            GlobalDegiskenlerX.g029SepetToplam,
            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString())
        )

        anaSayfaViewModel.sepetToplami.removeObservers(viewLifecycleOwner)
        anaSayfaViewModel.ServisError.removeObservers(viewLifecycleOwner)
        anaSayfaViewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)

        anaSayfaViewModel.sepetToplami.observe(viewLifecycleOwner, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    Log.d("sepetToplam", cevap.sepetToplamJSON[0].sepetToplam)

                    tvSepettekiUrunlerToplami.text = cevap.sepetToplamJSON[0].urunler + " TL"
                    tvKampanyaIndirimleri.text = cevap.sepetToplamJSON[0].indirim + " TL"
                    tvSepetToplami.text = cevap.sepetToplamJSON[0].sepetToplam + " TL"

                    sepetToplamTutari = Fonk.doubleCevir(cevap.sepetToplamJSON[0].sepetToplam)

                    hesaplamaYap()

                }
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = SiparisOzetiFragment()
            .apply {
            }
    }


}