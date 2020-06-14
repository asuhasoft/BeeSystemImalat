package com.imalat.beeSystem.adapter


import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.KategoriyeGoreKAtegoriVeUrunGetir
import com.imalat.beeSystem.model.aramaSonucu.Kategoriler
import kotlinx.android.synthetic.main.liste_satiri_kategoriler_arama.view.*
import java.util.*

class AramaKategorilerAdapter(
    val list: ArrayList<Kategoriler>,
    val kategoriyeGoreKAtegoriVeUrunGetir: KategoriyeGoreKAtegoriVeUrunGetir
) :
    RecyclerView.Adapter<AramaKategorilerAdapter.ViewHpolder>() {

    private var lastCheckedPosition = -1

    class ViewHpolder(var view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHpolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.liste_satiri_kategoriler_arama, parent, false)
        return ViewHpolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHpolder, position: Int) {


        holder.view.radioButton.isChecked = (position == lastCheckedPosition)
        holder.view.radioButton.text = list[position].kategoriAd
        holder.view.tvUrunSayisi.text = "( ${list[position].urunAdet} )"


        if (position == lastCheckedPosition) {
            holder.view.frameLayout.setBackgroundResource(R.drawable.buton_mavi_acik_secici)
            holder.view.radioButton.setTextColor(Color.parseColor("#ffffff"))
            holder.view.tvUrunSayisi.setTextColor(Color.parseColor("#ffffff"))

        } else {
            holder.view.frameLayout.setBackgroundResource(R.drawable.buton_layout_secici)
             holder.view.radioButton.setTextColor(Color.parseColor("#8794de"))
             holder.view.tvUrunSayisi.setTextColor(Color.parseColor("#8794de"))
        }


        //holder.view.tvKategoriIsmi.text = list[position].kategoriAd

        holder.view.radioButton.setOnClickListener {
            lastCheckedPosition = position
            notifyDataSetChanged()
            kategoriyeGoreKAtegoriVeUrunGetir.ilkAltKategoriGetir(
                list[position].kategoriID,
                list[position].kategoriAd
            )
        }
    }

    fun update(newList: List<Kategoriler>) {
        //lastCheckedPosition = -1
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}