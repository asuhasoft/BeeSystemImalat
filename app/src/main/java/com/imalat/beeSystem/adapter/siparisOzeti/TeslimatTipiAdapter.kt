package com.imalat.beeSystem.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.TeslimatTipiSecInterface
import com.imalat.beeSystem.model.siparisOzeti.teslimatTipi.TeslimatTipleriJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import kotlinx.android.synthetic.main.liste_satiri_teslimat_zamani.view.*
import java.util.*

class TeslimatTipiAdapter(
    val list: ArrayList<TeslimatTipleriJSON>,
    val teslimatTipiSecInterface: TeslimatTipiSecInterface
) :
    RecyclerView.Adapter<TeslimatTipiAdapter.ViewHpolder>() {

    class ViewHpolder(var view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHpolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.liste_satiri_teslimat_zamani, parent, false)
        return ViewHpolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHpolder, position: Int) {

        holder.view.tvBaslik.text = list[position].teslimatTipi


        holder.view.setOnClickListener {

            Fonk.kayitEkle(
                holder.view.context,
                EnumX.TeslimatTipID.toString(),
                list[position].teslimatTipID
            )
            Fonk.kayitEkle(
                holder.view.context,
                EnumX.TeslimatTipi.toString(),
                list[position].teslimatTipi
            )
            teslimatTipiSecInterface.kapat()
        }

    }

    fun update(newList: List<TeslimatTipleriJSON>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}