package com.imalat.beeSystem.view.profil.adres

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.KoordinatGonderInterface
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.viewModel.HaritadaAdresBulmaViewModel
import kotlinx.android.synthetic.main.fragment_haritada_adres_bulma.*


class HaritadaAdresBulmaFragment : DialogFragment(), OnMapReadyCallback {

    /*  private val callback = OnMapReadyCallback { googleMap ->
          val sydney = LatLng(-34.0, 151.0)
          googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
          googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
      }
  */

    lateinit var koordinatGonderInterface: KoordinatGonderInterface
    private var map: GoogleMap? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var viewModel: HaritadaAdresBulmaViewModel

    var myPlace = LatLng(0.0, 0.0)

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(requireActivity()).get(HaritadaAdresBulmaViewModel::class.java)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        this.koordinatGonderInterface = context as KoordinatGonderInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_haritada_adres_bulma, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireDialog().getWindow()!!.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        adresiKullan.setOnClickListener {

            if (myPlace.longitude > 0.0) {

                koordinatGonderInterface.koordinatlar(
                    myPlace.latitude.toString(),
                    myPlace.longitude.toString()
                )
                requireDialog().dismiss()

            }
        }


        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    fetchLocation()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    /* ... */
                    token.continuePermissionRequest()
                }
            }).check()

        //fetchLocation()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        @JvmStatic
        fun newInstance() = HaritadaAdresBulmaFragment().apply {}
    }


    override fun onMapReady(p0: GoogleMap?) {

        map = p0

        map!!.uiSettings.isZoomControlsEnabled = true
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        map!!.isMyLocationEnabled = true
        //map.setOnMarkerClickListener(this@HaritadaAdresBulmaFragment)

        map!!.uiSettings.isMyLocationButtonEnabled = true
        map!!.uiSettings.isZoomControlsEnabled = true

        //val myPlace = LatLng(40.73, -73.99)  // this is New York
        map!!.addMarker(MarkerOptions().position(myPlace).title("Adresim"))
        map!!.moveCamera(CameraUpdateFactory.newLatLng(myPlace))
        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace, 15.0f))

    }

    fun fetchLocation() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        val task = fusedLocationProviderClient.getLastLocation();


        task.addOnSuccessListener {

            it?.let {
                Log.d("latitude", it.latitude.toString())
                Log.d("longitude", it.longitude.toString())

                myPlace = LatLng(it.latitude, it.longitude)
                yandexAdresBul(it.longitude, it.latitude)

                val mapFragment =
                    childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                mapFragment?.getMapAsync(this)
            }
        }

    }


    fun yandexAdresBul(longitude: Double, latitute: Double) {

        // var yandexAdresBulma = "https://geocode-maps.yandex.ru/1.x/?apikey="+ BuildConfig.YandexApi+"&format=json&geocode=36.8535779,37.589788899999995&lang=tr_TR";
        // var yandexAdresBulma = "https://geocode-maps.yandex.ru/1.x/?apikey="+ BuildConfig.YandexApi+"&format=json&geocode=${longitude},${latitute}&lang=tr_TR";
        var yandexAdresBulma = "${longitude},${latitute}&lang=tr_TR";

        Log.d("yandexAdresBul", "çalıştı")
        Log.d("yandexAdresBul", GlobalDegiskenlerX.gConYandexGeoCode)
        Log.d("yandexAdresBul", longitude.toString())
        Log.d("yandexAdresBul", latitute.toString())

        viewModel.adresYandexGelsin(
            GlobalDegiskenlerX.gConYandexGeoCode,
            Fonk.degerGetir(requireContext(), EnumX.OturumKodu.toString()),
            longitude.toString(),
            latitute.toString()
        )

        observableLiveDataYandexAdresGetir()

        //getCurrentData(longitude.toString(),latitute.toString())


    }

    private fun observableLiveDataYandexAdresGetir() {

        viewModel.yandexAdres.removeObservers(viewLifecycleOwner)
        viewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)
        viewModel.servisYukleniyor.removeObservers(viewLifecycleOwner)

        Log.d("yandexAdresBul", "observableLiveDataYandexAdresGetir")

        viewModel.yandexAdres.observe(requireActivity(), Observer { cevap ->
            cevap?.let {

                Fonk.alertDialogGoster(
                    requireContext(),
                    "adresiniz",
                    cevap.response.geoObjectCollection.featureMember[0].geoObject.metaDataProperty.geocoderMetaData.text
                )
                Log.d(
                    "Bulunnaadres",
                    cevap.response.geoObjectCollection.featureMember[0].geoObject.metaDataProperty.geocoderMetaData.text
                )

            }


        })

    }

/*
    internal fun getCurrentData(longitude:String , latitute:String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://geocode-maps.yandex.ru/1.x/?apikey="+ BuildConfig.YandexApi+"&format=json&geocode=")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(MarketApileri::class.java)
        val call = service.getYandex(longitude, latitute)


        call.enqueue(object : Callback<YandexAdresCevap> {
            override fun onResponse(call: Call<YandexAdresCevap>, response: Response<YandexAdresCevap>) {
                if (response.code() == 200) {
                    val cevap = response.body()!!

                    Log.d("Bulunnaadres",cevap.response.geoObjectCollection.featureMember[0].geoObject.metaDataProperty.geocoderMetaData.text)

                }
            }

            override fun onFailure(call: Call<YandexAdresCevap>, t: Throwable) {

            }
        })
    }

*/

}