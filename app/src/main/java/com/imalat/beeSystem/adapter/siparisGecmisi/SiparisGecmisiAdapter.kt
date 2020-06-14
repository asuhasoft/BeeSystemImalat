package com.imalat.beeSystem.adapter.siparisGecmisi


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.model.siparisGecmisi.SiparisGecmisiJSON
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.view.siparisGecmisi.SiparisGecmisiDetayFragmnet
import kotlinx.android.synthetic.main.liste_satiri_siparis_gecmisi.view.*
import java.util.*

class SiparisGecmisiAdapter(val list: ArrayList<SiparisGecmisiJSON>) :
    RecyclerView.Adapter<SiparisGecmisiAdapter.ViewHpolder>() {

    class ViewHpolder(var view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHpolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.liste_satiri_siparis_gecmisi, parent, false)
        return ViewHpolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHpolder, position: Int) {

        holder.view.tvSiparisTarih.text = list[position].siparisTarih

        holder.view.tvSiparisNo.text = "#" + list[position].siparisID
        holder.view.tvSiparisTutari.text = list[position].toplamUcret + " TL"
        //holder.view.tvAdetSepet.text = list[position].adet.toString()
        holder.view.tvSiparisDurumu.text = list[position].durumAd
        holder.view.tvUrunSayisi.text = "(" + list[position].urunAdet + " Ürün)"


        when (list[position].siparisDurumID) {
            "1" -> holder.view.siparisDurumuLayout.setBackgroundResource(R.drawable.buton_gri_secici) //Siparişiniz Alındı

            "2" -> holder.view.siparisDurumuLayout.setBackgroundResource(R.drawable.buton_mavi_acik_secici) //hazırlanıyor
            "3" -> holder.view.siparisDurumuLayout.setBackgroundResource(R.drawable.buton_mavi_secici) // dağıtıma hazır
            "4" -> holder.view.siparisDurumuLayout.setBackgroundResource(R.drawable.buton_mavi_secici)

            "5" -> holder.view.siparisDurumuLayout.setBackgroundResource(R.drawable.buton_yesil_secici)

            "6" -> holder.view.siparisDurumuLayout.setBackgroundResource(R.drawable.buton_turuncu_secici)

            "7" -> holder.view.siparisDurumuLayout.setBackgroundResource(R.drawable.buton_kirmizi_secici)

        }



        holder.view.setOnClickListener {

            if (Fonk.isNetworkAvailable(holder.view.context)) {

                val f = SiparisGecmisiDetayFragmnet()
                val args = Bundle()
                args.putString("siparisID", list[position].siparisID)

                args.putString("toplamUcret", list[position].toplamUcret)
                args.putString("durumAd", list[position].durumAd)
                args.putString("zilCalma", list[position].zilCalma)
                args.putString("siparisNot", list[position].siparisNot)
                args.putString("urunAdet", list[position].urunAdet)

                args.putString("siparisTarih", list[position].siparisTarih)
                args.putString("servisAd", list[position].servisAd)
                args.putString("teslimatTipi", list[position].teslimatTipi)
                args.putString("odemeTipi", list[position].odemeTipi)
                f.setArguments(args)

                //fragmen içinde fragment açmak için (iç içe)
                (holder.view.context as AppCompatActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, f, null)
                    .addToBackStack(null)
                    .commit()

                /* val i = Intent(holder.view.context,SiparisGecmisiDetay::class.java)
                 i.putExtra("siparisID",list[position].siparisID)
                 holder.view.context.startActivity(i)*/


            }
        }
    }

    fun update(newList: List<SiparisGecmisiJSON>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}