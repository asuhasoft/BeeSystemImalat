package com.imalat.beeSystem.adapter


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.AramaGecmisiSilInterface
import com.imalat.beeSystem.model.aramaGecmisi.AramaGecmisJSON
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.view.ara.UrunAraSonucFragment
import kotlinx.android.synthetic.main.liste_satiri_arama_gecmisi.view.*
import java.util.*

class AramaGecmisiAdapter(
    val list: ArrayList<AramaGecmisJSON>,
    val aramaGecmisiSilInterface: AramaGecmisiSilInterface
) :
    RecyclerView.Adapter<AramaGecmisiAdapter.ViewHpolder>() {


    class ViewHpolder(var view: View) : RecyclerView.ViewHolder(view) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHpolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.liste_satiri_arama_gecmisi, parent, false)
        return ViewHpolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHpolder, position: Int) {

        holder.view.tvAramaGecmisi.text = list[position].aranan


        holder.view.setOnClickListener {

            if (Fonk.isNetworkAvailable(holder.view.context)) {

                val f = UrunAraSonucFragment()
                // Pass index input as an argument.
                val args = Bundle()
                args.putString("kategoriID", "")
                args.putString("urunID", list[position].aranan)
                f.setArguments(args)

                //fragmen içinde fragment açmak için (iç içe)
                (holder.view.context as AppCompatActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, f, null)
                    .addToBackStack(null)
                    .commit()

            }


        }


        holder.view.gecmisKelimeSil.setOnClickListener {

            if (Fonk.isNetworkAvailable(holder.view.context))
                aramaGecmisiSilInterface.aramaGecmisiKelimeSil(list[position].aranan)
        }


    }

    fun update(newList: List<AramaGecmisJSON>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}