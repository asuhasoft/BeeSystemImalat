package com.imalat.beeSystem.adapter.siparisGecmisi


import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.OnayBekleyenUrunlerGuncelleInterface
import com.imalat.beeSystem.model.onayBekleyenUrunler.OnayBekleyenUrunlerCevap
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.downloadFromUrl
import com.imalat.beeSystem.util.placeholderProgressBar
import com.imalat.beeSystem.view.kategoriler.UrunDetaylari
import kotlinx.android.synthetic.main.include_liste_satiri_alternatif_urunler.view.*
import kotlinx.android.synthetic.main.include_liste_satiri_urunler.view.*
import kotlinx.android.synthetic.main.liste_satiri_onay_bekleyen_urunler.view.*
import java.util.*

class OnayBekleyenUrunlerAdapter(
    val list: ArrayList<OnayBekleyenUrunlerCevap.OnayBekleyenUrunlerJSON>,
    val onayBekleyenUrunlerGuncelleInterface: OnayBekleyenUrunlerGuncelleInterface
) :
    RecyclerView.Adapter<OnayBekleyenUrunlerAdapter.ViewHpolder>() {

    class ViewHpolder(var view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHpolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.liste_satiri_onay_bekleyen_urunler, parent, false)
        return ViewHpolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHpolder, position: Int) {

        holder.view.tvBirimFiyat.text = list[position].birimFiyat
        holder.view.tvUrunAdi.text = list[position].urunAd
        holder.view.tvTalepFiyat.text = list[position].talepFiyat + " TL"
        holder.view.tvTalepToplamFiyat.text = list[position].talepToplamFiyat + " TL"
        holder.view.tvTalepMiktar.text =
            "${list[position].talepMiktar.replace(".00", "")} ${list[position].birimAd}"


        holder.view.tvBirimFiyatAlternatif.text = list[position].birimFiyatAlternatif
        holder.view.tvAlternatifUrunAd.text = list[position].alternatifUrunAd
        holder.view.tvSonFiyat.text = list[position].sonFiyat + " TL"
        holder.view.tvSonToplamFiyat.text = list[position].sonToplamFiyat + " TL"
        holder.view.tvSonMiktar.text =
            "${list[position].sonMiktar.replace(".00", "")} ${list[position].birimAd}"


        holder.view.tvFiyatFarki.text = list[position].fiyatFarki.replace("-", "") + " TL"
        //fiyat farkı eksi ise
        if (list[position].fiyatFarki.contains("-")) {
            holder.view.tvFiyatFarki.setBackgroundColor(Color.parseColor("#b80909"))
        } else {
            holder.view.tvFiyatFarki.setBackgroundColor(Color.parseColor("#4ba42f"))
        }

        holder.view.tvFiyatFarkiMesaj.text = list[position].fiyatFarkiMesaj


        holder.view.imUrunFoto.downloadFromUrl(
            list[position].urunFoto,
            placeholderProgressBar(holder.view.context)
        )

        holder.view.imAlternatifUrunFoto.downloadFromUrl(
            list[position].alternatifUrunFoto,
            placeholderProgressBar(holder.view.context)
        )

        holder.view.siparisEdilenUrun.setOnClickListener {

            if (Fonk.isNetworkAvailable(holder.view.context)) {
                val intent = Intent(holder.view.context, UrunDetaylari::class.java)
                intent.putExtra("urunID", list[position].urunID)
                intent.putExtra("foto", list[position].urunFoto)
                intent.putExtra("position", position)
                holder.itemView.context.startActivity(intent)
            }

        }

        holder.view.alternatifUrun.setOnClickListener {

            if (Fonk.isNetworkAvailable(holder.view.context)) {
                val intent = Intent(holder.view.context, UrunDetaylari::class.java)
                intent.putExtra("urunID", list[position].alternatifUrunID)
                intent.putExtra("foto", list[position].alternatifUrunFoto)
                intent.putExtra("position", position)
                holder.itemView.context.startActivity(intent)
            }

        }


        /* holder.view.tvSiparisiIptalEt.setOnClickListener {

             if (Fonk.isNetworkAvailable(holder.view.context)) {
                 onayBekleyenUrunlerGuncelleInterface.guncelle(list[position].siparisDetayID, "6")
             }
         }*/


        holder.view.tvUrunIptalEt.setOnClickListener {

            if (Fonk.isNetworkAvailable(holder.view.context)) {
                onayBekleyenUrunlerGuncelleInterface.guncelle(list[position].siparisDetayID, "5")

            }
        }

        holder.view.tvUrunuOnayla.setOnClickListener {

            if (Fonk.isNetworkAvailable(holder.view.context)) {
                onayBekleyenUrunlerGuncelleInterface.guncelle(list[position].siparisDetayID, "4")
            }
        }


    }

    fun update(newList: List<OnayBekleyenUrunlerCevap.OnayBekleyenUrunlerJSON>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}