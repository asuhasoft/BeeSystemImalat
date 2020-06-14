package com.imalat.beeSystem.adapter.kategoriDetaylari


import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.KategoriyeGoreKAtegoriVeUrunGetir
import com.imalat.beeSystem.model.urunKategorileri.UrunKategoriJSON
import kotlinx.android.synthetic.main.liste_satiri_alt_kategoriler.view.*

import java.util.*

class IkinciAltKategorilerAdapter(
    val list: ArrayList<UrunKategoriJSON>,
    val kategoriyeGoreKAtegoriVeUrunGetir: KategoriyeGoreKAtegoriVeUrunGetir
) :
    RecyclerView.Adapter<IkinciAltKategorilerAdapter.ViewHpolder>() {

    private var lastCheckedPosition = -1;

    class ViewHpolder(var view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHpolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.liste_satiri_alt_kategoriler, parent, false)
        return ViewHpolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHpolder, position: Int) {

        holder.view.radioButton.isChecked = (position == lastCheckedPosition)

        if (position == lastCheckedPosition) {
            // holder.view.relativeLayout.setBackgroundColor(Color.parseColor("#4ba42f"))
            holder.view.relativeLayout.setBackgroundResource(R.drawable.buton_mavi_cerceve_oval_basilmis)
            holder.view.radioButton.setTextColor(Color.parseColor("#ffffff"))

        } else {
            //holder.view.relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"))
            holder.view.relativeLayout.setBackgroundResource(R.drawable.buton_mavi_cerceve_oval)
            holder.view.radioButton.setTextColor(Color.parseColor("#081769"))
        }


        holder.view.radioButton.text = list[position].kategoriAd
        //holder.view.tvKategoriIsmi.text = list[position].kategoriAd


        holder.view.radioButton.setOnClickListener {
            lastCheckedPosition = position
            notifyDataSetChanged()

            Log.d("altkategoriBasilan", list[position].kategoriID)
            kategoriyeGoreKAtegoriVeUrunGetir.ikinciAltKategoriyeGoreUrunGetir(list[position].kategoriID)

            /* val intent = Intent(holder.itemView.context, KategoriDetaylari::class.java)
             intent.putExtra("kategoriID", list[position].kategoriID)
             intent.putExtra("kategoriAd", list[position].kategoriAd)
             holder.itemView.context.startActivity(intent)*/

        }
    }

    fun update(newList: List<UrunKategoriJSON>) {
        lastCheckedPosition = -1
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}