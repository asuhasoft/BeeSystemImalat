package com.imalat.beeSystem.adapter.anaSayfaAdapter


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.imalat.beeSystem.R
import com.imalat.beeSystem.model.anaSayfa.Slider
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.downloadFromUrl
import com.imalat.beeSystem.util.placeholderProgressBar
import com.imalat.beeSystem.view.ara.UrunAraSonucFragment
import com.imalat.beeSystem.view.kategoriler.UrunDetaylari
import com.imalat.beeSystem.viewModel.KategoriIDGonderViewModel
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.liste_satiri_slider.view.*
import java.util.*


class SliderAdapter(val list: ArrayList<Slider>) :
    SliderViewAdapter<SliderAdapter.CountryViewHpolder>() {

    private lateinit var kategoriIDGonderViewModel: KategoriIDGonderViewModel


    class CountryViewHpolder(var view: View) : SliderViewAdapter.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup?): CountryViewHpolder {
        val inflater = LayoutInflater.from(parent?.context)
        val view = inflater.inflate(R.layout.liste_satiri_slider, parent, false)

        kategoriIDGonderViewModel =
            ViewModelProvider(view.context as AppCompatActivity).get(KategoriIDGonderViewModel::class.java)

        return CountryViewHpolder(view)
    }

    override fun getCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: CountryViewHpolder, position: Int) {


        holder.view.sliderFoto.downloadFromUrl(
            list[position].fotograf,
            placeholderProgressBar(holder.view.context)
        )


        holder.view.setOnClickListener {

            Log.d("slider_tikalama", "urunid " + list[position].urunID)


            //urun ve menu de 0 ise sadece açıklama var demektir
            if (Fonk.inteCevir(list[position].urunID) > 0) {

                val intent = Intent(holder.view.context, UrunDetaylari::class.java)
                intent.putExtra("urunID", list[position].urunID)
                holder.view.context.startActivity(intent)

            } else if (Fonk.inteCevir(list[position].duyuruID) > 0) {
                Fonk.alertDialogGoster(holder.view.context, "", list[position].aciklama)
            } else if (Fonk.inteCevir(list[position].kategoriID) > 0) {


                val f = UrunAraSonucFragment()
                val args = Bundle()
                args.putString("urunID", "")
                args.putString("kategoriID", list[position].kategoriID)
                f.setArguments(args)

                //fragmen içinde fragment açmak için (iç içe)
                (holder.view.context as AppCompatActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, f, null)
                    .addToBackStack(null)
                    .commit()


                /*kategoriIDGonderViewModel.setKategoriID(list[position].kategoriID)

                (holder.view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, UrunAraSonucFragment::class.java, null)
                    .addToBackStack(null)
                    .commit()*/

                /*
                val frg = SiparisOzetiFragment
                    frg.newInstance(sepetToplamTutari).show(supportFragmentManager, "ad")
                 */
            }


            /* val builder: AlertDialog.Builder = AlertDialog.Builder(holder.view.context)
             builder.setTitle(holder.view.context.resources.getString(R.string.sepet_secim_yap))
             builder.setMessage("")
             builder.setNegativeButton(holder.view.context.resources.getString(R.string.sil), DialogInterface.OnClickListener { dialogInterface, i ->

                 val db = DatabaseUrunler(holder.view.context)
                 db.sepettekiUrunusil(list[position].grupID)
                 db.close()

                 sepetGuncelleInterface.sepetGuncelle()

             })
             builder.setPositiveButton(holder.view.context.resources.getString(R.string.duzenle), DialogInterface.OnClickListener { dialogInterface, i ->

             })
             builder.show()*/

            /* val intent = Intent(holder.itemView.context, UrunAyrintilari::class.java)
             intent.putExtra("urunID", list[position].urunID)
             intent.putExtra("menuID", "0") // menü olmadığı için menü id olamaz
             holder.itemView.context.startActivity(intent)*/

        }


        //holder.view.imUrunFoto.downloadFromUrl(list[position].fotograf, placeholderProgressBar(holder.view.context))
    }

    fun update(newList: List<Slider>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }


}