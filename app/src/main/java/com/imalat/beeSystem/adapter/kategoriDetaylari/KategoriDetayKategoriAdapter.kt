package com.imalat.beeSystem.adapter.kategoriDetaylari


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.KategoriyeGoreKAtegoriVeUrunGetir
import com.imalat.beeSystem.model.urunKategorileri.UrunKategoriJSON
import kotlinx.android.synthetic.main.liste_satiri_kategoriler_2.view.*
import java.util.*

class KategoriDetayKategoriAdapter(
    val list: ArrayList<UrunKategoriJSON>,
    val kategoriyeGoreKAtegoriVeUrunGetir: KategoriyeGoreKAtegoriVeUrunGetir
) : RecyclerView.Adapter<KategoriDetayKategoriAdapter.ViewHpolder>() {

    private var lastCheckedPosition = -1

    class ViewHpolder(var view: View) : RecyclerView.ViewHolder(view) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHpolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.liste_satiri_kategoriler_2, parent, false)
        return ViewHpolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHpolder, position: Int) {


        holder.view.radioButton.isChecked = (position == lastCheckedPosition)
        holder.view.radioButton.text = list[position].kategoriAd



        if (position == lastCheckedPosition) {
            // holder.view.frameLayout.setBackgroundResource(R.drawable.buton_mavi_acik_secici)
            holder.view.radioButton.setTextColor(Color.parseColor("#ffffff"))

        } else {
            // holder.view.frameLayout.setBackgroundResource(R.drawable.buton_layout_secici)
            holder.view.radioButton.setTextColor(Color.parseColor("#8794de"))
        }

        // holder.view.imUrun.downloadFromUrl(list[position].fotograf, placeholderProgressBar(holder.view.context))

        holder.view.radioButton.setOnClickListener {
            lastCheckedPosition = position
            notifyDataSetChanged()
            kategoriyeGoreKAtegoriVeUrunGetir.ilkAltKategoriGetir(
                list[position].kategoriID,
                list[position].kategoriAd
            )
        }
    }

    fun update(newList: List<UrunKategoriJSON>) {
        lastCheckedPosition = -1
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}