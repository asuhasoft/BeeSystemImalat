package com.imalat.beeSystem.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.BadgeGuncelleInterface
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.GlobalDegiskenlerX
import com.imalat.beeSystem.view.anaSayfa.FragmentAnaSayfa
import com.imalat.beeSystem.view.anaSayfa.FragmentDiger
import com.imalat.beeSystem.view.ara.UrunAraAnaSayfaFragment
import com.imalat.beeSystem.view.ara.UrunAraSonucFragment
import com.imalat.beeSystem.view.diger.SepetimFragment
import com.imalat.beeSystem.view.kategoriler.FragmentKategoriler
import com.imalat.beeSystem.view.kategoriler.UrunDetaylari
import com.imalat.beeSystem.viewModel.AnaSayfaViewModel


class AnaSayfa : AppCompatActivity(), BadgeGuncelleInterface {

    private var currentSelection = 0

    private lateinit var bottomNavItemView: BottomNavigationItemView
    private var messageBadgeView: View? = null

    private lateinit var menu: Menu

    private lateinit var anaSayfaViewModel: AnaSayfaViewModel

    private val tag = "AnaSayfa"

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ana_sayfa2)

        anaSayfaViewModel = ViewModelProvider(this).get(AnaSayfaViewModel::class.java)
        bottomNavigationView = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView

        bottomNavigation()
        loadFragment(FragmentAnaSayfa())
        // loadFragment(UyeOlFragment())


        val bundle: Bundle? = intent.extras
        if (bundle != null) {

            if (!bundle.getString("bildirimdenGelenUrunId").toString().isNullOrBlank() &&
                Fonk.inteCevir(bundle.getString("bildirimdenGelenUrunId").toString()) > 0
            ) {

                if (Fonk.isNetworkAvailable(this)) {
                    val intent = Intent(this, UrunDetaylari::class.java)
                    intent.putExtra("urunID", bundle.getString("bildirimdenGelenUrunId").toString())
                    intent.putExtra("foto", "")
                    intent.putExtra("position", 0)
                    startActivity(intent)
                }


            } else if (!bundle.getString("bildirimdenGelenKategoriID").toString().isNullOrBlank() &&
                Fonk.inteCevir(bundle.getString("bildirimdenGelenKategoriID").toString()) > 0
            ) {


                val f = UrunAraSonucFragment()
                val args = Bundle()
                args.putString("urunID", "")
                args.putString(
                    "kategoriID",
                    bundle.getString("bildirimdenGelenKategoriID").toString()
                )
                f.arguments = args

                //fragmen içinde fragment açmak için (iç içe)
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, f, null)
                    .addToBackStack(null)
                    .commit()



            } else if (!bundle.getString("bildirimdenGelenDuyuruID").toString().isNullOrBlank() &&
                Fonk.inteCevir(bundle.getString("bildirimdenGelenDuyuruID").toString()) > 0
            ) {

            }

        }


    }


    override fun badgeGuncelle(urunFiyati: String) {
        badgeAyarla()
    }

    override fun onResume() {
        super.onResume()
        badgeAyarla()


        if (GlobalDegiskenlerX.g001ClientLogin.isNullOrBlank()) {
            startActivity(Intent(this, AcilisSayfasi::class.java))
            finish()
        }
    }

    private fun badgeAyarla() {

        sepetToplamTutariGetirPOST()

/*
        val mBottomNavigationMenuView = bottom_navigation.getChildAt(0) as BottomNavigationMenuView
        val view = mBottomNavigationMenuView.getChildAt(2)
        bottomNavItemView = view as BottomNavigationItemView

        messageBadgeView = LayoutInflater.from(this).inflate(R.layout.item_message_count_badge, mBottomNavigationMenuView, false)

        messageBadgeView!!.counter_badge.text = " ${sepetiGetir()} "
        //Add badge
        bottomNavItemView.addView(messageBadgeView)

*/


    }

    private fun sepetToplamTutariGetirPOST() {
        //Log.d("badgeAyarla", "çalıştı")
        var sepetToplamTutari = "0.0"

        anaSayfaViewModel.sepetToplamiGetir(
            GlobalDegiskenlerX.g029SepetToplam,
            Fonk.degerGetir(this, EnumX.OturumKodu.toString())
        )

        anaSayfaViewModel.sepetToplami.removeObservers(this)
        anaSayfaViewModel.ServisError.removeObservers(this)
        anaSayfaViewModel.servisYukleniyor.removeObservers(this)

        anaSayfaViewModel.sepetToplami.observe(this, Observer { cevap ->
            cevap?.let {

                if (cevap.success == 1) {

                    //Log.d("sepetToplam", cevap.sepetToplamJSON[0].sepetToplam)

                    sepetToplamTutari = cevap.sepetToplamJSON[0].sepetToplam

                    val bottomNavigationView =
                        findViewById<View>(R.id.bottom_navigation) as BottomNavigationView

                    val menu = bottomNavigationView.menu
                    //  menu.findItem(R.id.action_sepet).title = " ${sepetiGetir()} TL "
                    menu.findItem(R.id.action_sepet).title = " ${sepetToplamTutari} TL "

                }
            }

        })
    }

    fun bottomNavigation() {

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_anasayfa -> {
                    loadFragment(FragmentAnaSayfa())

                    /*  supportFragmentManager.popBackStack("action_anasayfa", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                     supportFragmentManager
                         .beginTransaction()
                         .replace(R.id.fragment_container, FragmentAnaSayfa())
                         .addToBackStack("action_anasayfa")
                         .commit()*/
                }


                R.id.action_urunler -> loadFragment(FragmentKategoriler())
                R.id.action_sepet -> loadFragment(SepetimFragment())
                // R.id.action_sepet -> startActivity(Intent(this, Sepetim::class.java))

                R.id.action_ara -> loadFragment(UrunAraAnaSayfaFragment())
              //  R.id.action_diger -> loadFragment(FragmentDiger())
            }
            true
        }
    }


    private fun loadFragment(fragment: Fragment?) {
        //switching fragment
        if (fragment != null) {

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                // .addToBackStack(null)
                .commit()
        }
    }


    override fun onBackPressed() {

        val count = supportFragmentManager.backStackEntryCount
        Log.e("countt", count.toString())

        if (count == 0) {

            if (bottomNavigationView.selectedItemId === R.id.action_anasayfa) {
                super.onBackPressed()
            } else {
                bottomNavigationView.selectedItemId = R.id.action_anasayfa
            }

            //super.onBackPressed();

        } else {
            supportFragmentManager.popBackStack()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }

    }


}