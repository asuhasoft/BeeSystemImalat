package com.imalat.beeSystem.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.TeslimatZamaniSecInterface
import com.imalat.beeSystem.model.siparisOzeti.teslimatZamani.ServisPlanlariJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import kotlinx.android.synthetic.main.liste_satiri_teslimat_zamani.view.*
import java.util.*

class TeslimatZamaniAdapter(
    val list: ArrayList<ServisPlanlariJSON>,
    val teslimatZamaniSecInterface: TeslimatZamaniSecInterface
) :
    RecyclerView.Adapter<TeslimatZamaniAdapter.ViewHpolder>() {

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

        holder.view.tvBaslik.text = list[position].servisAd


        holder.view.setOnClickListener {

            Fonk.kayitEkle(
                holder.view.context,
                EnumX.ServisPlanID.toString(),
                list[position].servisPlanID
            )
            Fonk.kayitEkle(holder.view.context, EnumX.ServisAd.toString(), list[position].servisAd)
            teslimatZamaniSecInterface.kapat()
        }

    }

    fun update(newList: List<ServisPlanlariJSON>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}