package com.imalat.beeSystem.adapter


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.model.bildirim.BildirimlerCevap
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.view.ara.UrunAraSonucFragment
import com.imalat.beeSystem.view.kategoriler.UrunDetaylari
import kotlinx.android.synthetic.main.liste_satiri_bildirimler.view.*
import java.util.*

class BildirimAdapter(val list: ArrayList<BildirimlerCevap.BildirimlerJSON>) :
    RecyclerView.Adapter<BildirimAdapter.ViewHpolder>() {


    class ViewHpolder(var view: View) : RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHpolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.liste_satiri_bildirimler, parent, false)
        return ViewHpolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHpolder, position: Int) {

        holder.view.tvBaslik.text = list[position].baslik
        holder.view.tvMesaj.text = list[position].mesaj
        holder.view.tvTarih.text = list[position].zaman


        holder.view.setOnClickListener {

            if (Fonk.isNetworkAvailable(holder.view.context)) {

                ///------------------------------------------------------------------------------------------------------------
                //bildirimden urunid veya kategori id gelebilir
                //urun veya kategori gelecek
                // ürün geldiyse 0 dan farklıdır
                if (!list[position].urunID.equals("0")) {
                    val intent = Intent(holder.view.context, UrunDetaylari::class.java)
                    intent.putExtra("urunID", list[position].urunID)
                    intent.putExtra("favori", "")
                    intent.putExtra("position", position)
                    //holder.itemView.context.startActivity(intent)
                    (holder.view.context as AppCompatActivity).startActivityForResult(intent, 111)
                } else if (!list[position].kategoriID.equals("0")) {

                    val f = UrunAraSonucFragment()
                    val args = Bundle()
                    args.putString("urunID", "")
                    args.putString("kategoriID", list[position].kategoriID)
                    f.setArguments(args)

                    //fragmen içinde fragment açmak için (iç içe)
                    (holder.view.context as AppCompatActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, f, null)
                        .addToBackStack(null)
                        .commit()
                } else {
                    Fonk.alertDialogGoster(holder.view.context, "", list[position].mesaj)
                }

                ///------------------------------------------------------------------------------------------------------------

            }


        }

    }

    fun update(newList: List<BildirimlerCevap.BildirimlerJSON>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}