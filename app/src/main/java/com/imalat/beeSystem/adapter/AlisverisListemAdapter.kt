package com.imalat.beeSystem.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.AlisverisListemInterface
import com.imalat.beeSystem.model.alisVerisListesi.AlisverisListesiJSON
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.downloadFromUrl
import com.imalat.beeSystem.util.placeholderProgressBar
import kotlinx.android.synthetic.main.liste_satiri_sepetim.view.*
import kotlinx.android.synthetic.main.liste_satiri_sepetim.view.imUrunFoto
import kotlinx.android.synthetic.main.liste_satiri_sepetim.view.tvFiyat
import kotlinx.android.synthetic.main.liste_satiri_sepetim.view.tvUrunAdi
import kotlinx.android.synthetic.main.liste_satiri_urunler.view.*
import java.util.*

class AlisverisListemAdapter(
    val list: ArrayList<AlisverisListesiJSON>,
    val alisverisListemInterface: AlisverisListemInterface
) :
    RecyclerView.Adapter<AlisverisListemAdapter.ViewHpolder>() {

    class ViewHpolder(var view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHpolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.liste_satiri_alisveris_listem, parent, false)
        return ViewHpolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHpolder, position: Int) {

        holder.view.tvUrunAdi.text = list[position].urunAdi
        holder.view.tvFiyat.text = list[position].kampanyaliFiyat + " TL"
        //holder.view.tvAdetSepet.text = list[position].adet.toString()

        holder.view.imUrunFoto.downloadFromUrl(
            list[position].fotograf,
            placeholderProgressBar(holder.view.context)
        )

        holder.view.imSil.setOnClickListener {

            if (Fonk.isNetworkAvailable(holder.view.context)) {
                alisverisListemInterface.listedenCikart(list[position].urunID, list.size)
                list.removeAt(position)
                notifyDataSetChanged()
            }

        }


        holder.view.tvSepeteEkle.setOnClickListener {


            if (Fonk.isNetworkAvailable(holder.view.context))
                alisverisListemInterface.sepeteEkleCikart(list[position].urunID, "1")

            /*           val db = DatabaseUrunler(holder.view.context)
                       if (!db.urunEkliMi(Fonk.inteCevir(list[position].urunID))) // ürünn ekli değilse öyle sepete eklesin
                       db.urunEkle(
                           Fonk.inteCevir(list[position].urunID),
                           list[position].urunAdi,
                           list[position].kampanyaliFiyat,
                           "arttir",
                           list[position].artisMiktari,
                           list[position].maksSipMiktar,
                           list[position].birimAd,
                           list[position].fotograf
                       )
                       db.close()
                       Toast.makeText(holder.view.context,"Sepete Eklendi",Toast.LENGTH_SHORT).show()
           */


        }


    }

    fun update(newList: List<AlisverisListesiJSON>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}