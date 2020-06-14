package com.imalat.beeSystem.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.OdemeYontemiSecSecInterface
import com.imalat.beeSystem.model.siparisOzeti.odemeTipi.OdemeTipleriJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import kotlinx.android.synthetic.main.liste_satiri_teslimat_zamani.view.*
import java.util.*

class OdemeYontemiSecAdapter(
    val list: ArrayList<OdemeTipleriJSON>,
    val odemeYontemiSecSecInterface: OdemeYontemiSecSecInterface
) :
    RecyclerView.Adapter<OdemeYontemiSecAdapter.ViewHpolder>() {

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

        holder.view.tvBaslik.text = list[position].odemeTipi


        holder.view.setOnClickListener {

            Fonk.kayitEkle(
                holder.view.context,
                EnumX.OdemeTipi.toString(),
                list[position].odemeTipi
            )
            Fonk.kayitEkle(
                holder.view.context,
                EnumX.OdemeTipID.toString(),
                list[position].odemeTipID
            )
            odemeYontemiSecSecInterface.kapat()
        }

    }

    fun update(newList: List<OdemeTipleriJSON>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}