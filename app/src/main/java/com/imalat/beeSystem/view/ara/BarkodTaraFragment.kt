package com.imalat.beeSystem.view.ara

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.imalat.beeSystem.R
import com.imalat.beeSystem.viewModel.BarkodGetirViewModel


class BarkodTaraFragment : Fragment() {
    private lateinit var svBarcode: SurfaceView
    private lateinit var tvBarcode: TextView
    private lateinit var detector: BarcodeDetector
    private lateinit var cameraSource: CameraSource

    private lateinit var barkodGetirViewModel: BarkodGetirViewModel

    var bulunanBarkod = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        barkodGetirViewModel =
            ViewModelProvider(requireActivity()).get(BarkodGetirViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_barkod_tara, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        svBarcode = view.findViewById(R.id.sv_barcode)
        tvBarcode = view.findViewById(R.id.tv_barcode)

        detector =
            BarcodeDetector.Builder(requireContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        detector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                //fragment kapatılıyor
                requireActivity().supportFragmentManager.popBackStack()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                var barcodes = detections?.detectedItems

                if (barcodes!!.size() > 10) {

                    val builder = StringBuilder()
                    tvBarcode.post {
                        builder.append("Bulunan Barkod : ")
                        builder.append("\n")
                        builder.append(barcodes.valueAt(0).displayValue)
                        tvBarcode.text = builder.toString()
                        //tvBarcode.text = barcodes.valueAt(0).displayValue
                    }

                    bulunanBarkod = barcodes.valueAt(0).displayValue
                    detector.release()

                }
            }
        })

        cameraSource =
            CameraSource.Builder(requireContext(), detector).setRequestedPreviewSize(1024, 768)
                .setRequestedFps(25f).setAutoFocusEnabled(true).build()

        svBarcode.holder.addCallback(object : SurfaceHolder.Callback2 {
            override fun surfaceRedrawNeeded(holder: SurfaceHolder?) {}

            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, w: Int, h: Int) {}

            //Main Camera processing.
            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                cameraSource.stop()
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                //Check Camera permission
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    cameraSource.start(holder)
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(android.Manifest.permission.CAMERA),
                        123
                    )
                }
            }
        })


        tvBarcode.setOnClickListener {

        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraSource.start(svBarcode.holder)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Kamera izni verilmediği için barkod okutulamıyor",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detector.release()
        cameraSource.stop()
        cameraSource.release()
        barkodGetirViewModel.setName(bulunanBarkod)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BarkodTaraFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BarkodTaraFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    fun fragmentKapat() {

        Handler().postDelayed({


        }, 1000)

        // requireActivity().supportFragmentManager.beginTransaction().remove(requireParentFragment()).commit()

    }
}


