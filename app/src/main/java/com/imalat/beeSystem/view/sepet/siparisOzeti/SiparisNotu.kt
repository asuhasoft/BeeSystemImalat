package com.imalat.beeSystem.view.sepet.siparisOzeti

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.imalat.beeSystem.R
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk

import kotlinx.android.synthetic.main.fragment_siparis_notu.*


class SiparisNotu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_siparis_notu)
        overridePendingTransition(R.anim.saga_gidis1, R.anim.saga_gidis2)


        val bundle: Bundle? = intent.extras
        if (bundle != null) {

            etSiparisNotu.setText(bundle.getString("siparisNotu"))
        }

        //requireDialog().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        tvSiparisNotuKaydet.setOnClickListener {
            Fonk.kayitEkle(
                this,
                EnumX.SiparisNotum.toString(),
                etSiparisNotu.text.toString()
            )
            finish()
        }


        kapat.setOnClickListener {
            finish()
        }

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.sola_gidis1, R.anim.sola_gidis2)
    }
}