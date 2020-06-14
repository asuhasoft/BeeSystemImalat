package com.imalat.beeSystem.adapter.siparisGecmisi


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.model.siparisGecmisDetayi.SiparisDetayJSON
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.downloadFromUrl
import com.imalat.beeSystem.util.placeholderProgressBar
import com.imalat.beeSystem.view.kategoriler.UrunDetaylari
import com.imalat.beeSystem.view.siparisGecmisi.OnayBekleyeUrunlerFragment
import kotlinx.android.synthetic.main.liste_satiri_siparis_gecmisi_detay.view.*
import java.util.*

class SiparisGecmisiDetayAdapter(val list: ArrayList<SiparisDetayJSON>) :
    RecyclerView.Adapter<SiparisGecmisiDetayAdapter.ViewHpolder>() {

    class ViewHpolder(var view: View) : RecyclerView.ViewHolder(view) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHpolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.liste_satiri_siparis_gecmisi_detay, parent, false)
        return ViewHpolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHpolder, position: Int) {

        holder.view.tvUrunAdi.text = list[position].urunAdi
        holder.view.tvTalepFiyat.text = list[position].talepFiyat
        holder.view.tvTalepToplamFiyat.text = list[position].talepToplamFiyat
        holder.view.tvDetayDurum.text = " " + list[position].detayDurumAd + " "
        holder.view.tvTalepMiktar.text =
            "${list[position].talepMiktar.replace(".00", "")} ${list[position].birimAd}"

        holder.view.imUrunFoto.downloadFromUrl(
            list[position].fotograf,
            placeholderProgressBar(holder.view.context)
        )



        if (list[position].siparisDetayDurumID.equals("1")) {
            //holder.view.imDurum.setImageResource(R.drawable.ic_urun_bekliyor)
            holder.view.tvDetayDurum.setBackgroundResource(R.drawable.buton_gri_secici)
        } else if (list[position].siparisDetayDurumID.equals("2")) {
            //  holder.view.imDurum.setImageResource(R.drawable.ic_urun_hazir)

            holder.view.tvDetayDurum.setBackgroundResource(R.drawable.buton_yesil_secici)
        } else if (list[position].siparisDetayDurumID.equals("3")) {

            holder.view.tvDetayDurum.setBackgroundResource(R.drawable.buton_kirmizi_secici)
            // holder.view.imDurum.setImageResource(R.drawable.ic_onay_bekliyor)
        } else if (list[position].siparisDetayDurumID.equals("4")) {
            // holder.view.imDurum.setImageResource(R.drawable.ic_urun_hazir)
            holder.view.tvDetayDurum.setBackgroundResource(R.drawable.buton_yesil_secici)
        }



        holder.view.setOnClickListener {

            if (Fonk.isNetworkAvailable(holder.view.context)) {


                if (list[position].siparisDetayDurumID.equals("3")) {

                    if (Fonk.isNetworkAvailable(holder.view.context))
                        (holder.view.context as AppCompatActivity)
                            .supportFragmentManager
                            .beginTransaction()
                            .replace(
                                R.id.fragment_container,
                                OnayBekleyeUrunlerFragment::class.java,
                                null
                            )
                            .addToBackStack(null)
                            .commit()

                } else {

                    val intent = Intent(holder.view.context, UrunDetaylari::class.java)
                    intent.putExtra("urunID", list[position].urunID)
                    intent.putExtra("foto", list[position].fotograf)
                    intent.putExtra("position", position)
                    holder.itemView.context.startActivity(intent)
                }


            }

        }

    }

    fun update(newList: List<SiparisDetayJSON>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}