package com.imalat.beeSystem.view.ara

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.integration.android.IntentIntegrator
import com.imalat.beeSystem.R
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.viewModel.BarkodGetirViewModel

class BarkodActivity : AppCompatActivity() {
    internal var txtName: TextView? = null
    private lateinit var barkodGetirViewModel: BarkodGetirViewModel

    private var bulunanBarkod = "***"
    internal var qrScanIntegrator: IntentIntegrator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barkod)


        barkodGetirViewModel = ViewModelProvider(this).get(BarkodGetirViewModel::class.java)

        txtName = findViewById(R.id.name)

        qrScanIntegrator = IntentIntegrator(this)
        qrScanIntegrator?.setOrientationLocked(false)

        // Different Customization option...
        qrScanIntegrator?.setPrompt(getString(R.string.scan_a_code))
        qrScanIntegrator?.setCameraId(0)  // Use a specific camera of the device
        qrScanIntegrator?.setBeepEnabled(true)
        qrScanIntegrator?.setBarcodeImageEnabled(true)

        performAction()

    }

    private fun performAction() {
        qrScanIntegrator?.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            // If QRCode has no data.
            if (result.contents == null) {
                Toast.makeText(this, getString(R.string.result_not_found), Toast.LENGTH_LONG).show()
                finish()
            } else {
                Log.d("bylunanbarkod", result.contents)

                bulunanBarkod = result.contents

                Fonk.kayitEkle(this, EnumX.barkod.toString(), bulunanBarkod)
                // barkodGetirViewModel.setName(result.contents)
                // txtName?.text = result.contents
                finish()

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        barkodGetirViewModel.setName(bulunanBarkod)
    }

}