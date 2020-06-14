package com.imalat.beeSystem.adapter


import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.model.subeler.SubelerCevap
import kotlinx.android.synthetic.main.liste_satiri_subeler.view.*
import java.util.*

class SubelerAdapter(val list: ArrayList<SubelerCevap.SubelerJSON>) :
    RecyclerView.Adapter<SubelerAdapter.ViewHpolder>() {


    class ViewHpolder(var view: View) : RecyclerView.ViewHolder(view) {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHpolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.liste_satiri_subeler, parent, false)
        return ViewHpolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHpolder, position: Int) {

        holder.view.tvSubeAd.text = list[position].subeAd
        holder.view.tvAdresi.text = list[position].adres
        holder.view.tvTelefon.text = list[position].telefon
        holder.view.tvEposta.text = list[position].ePosta


        holder.view.tvSubeAra.setOnClickListener {

            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${list[position].telefon}")
            holder.view.context.startActivity(intent)

        }

        holder.view.tvSubeMailAt.setOnClickListener {

            val mIntent = Intent(Intent.ACTION_SEND)
            mIntent.data = Uri.parse("mailto:")
            mIntent.type = "text/plain"
            mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(list[position].ePosta))
            mIntent.putExtra(Intent.EXTRA_SUBJECT, "İstek ve Görüşler (Android)")
            mIntent.putExtra(Intent.EXTRA_TEXT, "")
            try {
                holder.view.context.startActivity(Intent.createChooser(mIntent, "Mail seçiniz"))
            } catch (e: Exception) {
                Toast.makeText(holder.view.context, e.message, Toast.LENGTH_LONG).show()
            }

        }

    }

    fun update(newList: List<SubelerCevap.SubelerJSON>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}