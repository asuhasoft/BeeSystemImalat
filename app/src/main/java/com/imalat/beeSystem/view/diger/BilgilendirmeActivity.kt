package com.imalat.beeSystem.view.diger

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.imalat.beeSystem.R
import com.imalat.beeSystem.model.kurumsalBilgi.KurumsalBilgiJSON
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.KurumsalBilgiViewModel
import kotlinx.android.synthetic.main.fragment_bilgilendirme.*


class BilgilendirmeActivity : AppCompatActivity() {
    private var sozlesmeMetni = ""
    private var kvkkMetni = ""
    lateinit var kurumsalBilgiViewModel: KurumsalBilgiViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_bilgilendirme)
        kurumsalBilgiViewModel =
            ViewModelProvider(this).get(KurumsalBilgiViewModel::class.java)

        radioSozlesme.visibility = View.INVISIBLE
        radioKvkk.visibility = View.INVISIBLE

        // radioSozlesme.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        //radioKvkk.paintFlags = Paint.ANTI_ALIAS_FLAG

        if (Fonk.isNetworkAvailable(this)) {
            kurumsalBilgiViewModel.kurumsalBilgiGetir(GlobalDegiskenlerX.g024KurumsalBilgi)
            observerLiveDataKurumsalBilgiler()
        }

        radioSozlesme.setOnCheckedChangeListener { compoundButton, isChecked ->

            if (isChecked) {
                tvBilgilendirme.setText(sozlesmeMetni)

                radioSozlesme.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                radioKvkk.paintFlags = Paint.ANTI_ALIAS_FLAG
                // radioSozlesme.setBackgroundResource(R.drawable.buton_mavi_cerceve_oval_basilmis)
            } else {

                radioSozlesme.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                radioKvkk.paintFlags = Paint.ANTI_ALIAS_FLAG

                // radioSozlesme.setBackgroundResource(R.drawable.buton_mavi_cerceve_oval)

            }
        }

        radioKvkk.setOnCheckedChangeListener { compoundButton, isChecked ->

            if (isChecked) {
                tvBilgilendirme.setText(kvkkMetni)
                radioSozlesme.paintFlags = Paint.ANTI_ALIAS_FLAG
                radioKvkk.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            } else {
                radioSozlesme.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                radioKvkk.paintFlags = Paint.ANTI_ALIAS_FLAG
            }
        }

        geriBligilendirme.setOnClickListener {
            finish()
        }

    }


    private fun observerLiveDataKurumsalBilgiler() {

        kurumsalBilgiViewModel.kurumsalBilgi.removeObservers(this)
        kurumsalBilgiViewModel.servisYukleniyor.removeObservers(this)
        kurumsalBilgiViewModel.ServisError.removeObservers(this)

        kurumsalBilgiViewModel.kurumsalBilgi.observe(this, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    val list: List<KurumsalBilgiJSON> = cevap.kurumsalBilgiJSON

                    if (list != null && list.size > 0) {

                        radioSozlesme.visibility = View.VISIBLE
                        radioKvkk.visibility = View.VISIBLE

                        sozlesmeMetni = list[0].kullaniciSozlesme
                        kvkkMetni = list[0].kVKKMetni

                        tvBilgilendirme.setText(list[0].kullaniciSozlesme)
                    }
                }

            }

        })

    }

}

