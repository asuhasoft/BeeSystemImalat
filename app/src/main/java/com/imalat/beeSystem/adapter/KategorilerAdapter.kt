package com.imalat.beeSystem.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.model.urunKategorileri.UrunKategoriJSON
import com.imalat.beeSystem.util.downloadFromUrl
import com.imalat.beeSystem.util.placeholderProgressBar
import com.imalat.beeSystem.view.kategoriler.KategoriDetaylariFragment
import com.imalat.beeSystem.viewModel.KategoriIDGonderViewModel
import kotlinx.android.synthetic.main.liste_satiri_kategoriler.view.*
import java.util.*

class KategorilerAdapter(val list: ArrayList<UrunKategoriJSON>) :
    RecyclerView.Adapter<KategorilerAdapter.ViewHpolder>() {

    private lateinit var kategoriIDGonderViewModel: KategoriIDGonderViewModel

    class ViewHpolder(var view: View) : RecyclerView.ViewHolder(view) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHpolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.liste_satiri_kategoriler, parent, false)

        kategoriIDGonderViewModel =
            ViewModelProvider(view.context as androidx.appcompat.app.AppCompatActivity).get(
                KategoriIDGonderViewModel::class.java
            )

        return ViewHpolder(view)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHpolder, position: Int) {


        holder.view.tvKategoriIsmi.text = list[position].kategoriAd

        holder.view.imUrun.downloadFromUrl(
            list[position].fotograf,
            placeholderProgressBar(holder.view.context)
        )

        // Log.d("fooo",list[position].fotograf)
        /*
          when(position){
              0-> //Fonk.gradientArkaPlanOlustur(holder.view.context,holder.view.relativeLayout,"fffa54")
                  holder.view.relativeLayout.setBackgroundColor(Color.parseColor("#fffa54"))
              1-> holder.view.relativeLayout.setBackgroundColor(Color.parseColor("#d3f4bd"))
              2-> holder.view.relativeLayout.setBackgroundColor(Color.parseColor("#f9d8d8"))
              3-> holder.view.relativeLayout.setBackgroundColor(Color.parseColor("#f3edb7"))
              4-> holder.view.relativeLayout.setBackgroundColor(Color.parseColor("#eecda5"))
              5-> holder.view.relativeLayout.setBackgroundColor(Color.parseColor("#cee8e1"))
              6-> holder.view.relativeLayout.setBackgroundColor(Color.parseColor("#f7d2fa"))
              7-> holder.view.relativeLayout.setBackgroundColor(Color.parseColor("#d4fde7"))
              8-> holder.view.relativeLayout.setBackgroundColor(Color.parseColor("#fae1cb"))
              9-> holder.view.relativeLayout.setBackgroundColor(Color.parseColor("#dbdbdb"))
              10-> holder.view.relativeLayout.setBackgroundColor(Color.parseColor("#d7e3fc"))
          }
          */

        holder.view.setOnClickListener {

            //  (holder.view.context as AppCompatActivity).parentFragmentManager.beginTransaction()

            kategoriIDGonderViewModel.setKategoriID(list[position].kategoriID)

            (holder.view.context as AppCompatActivity)
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, KategoriDetaylariFragment::class.java, null)
                .addToBackStack(null)
                .commit()


            /* val intent = Intent(holder.itemView.context, KategoriDetaylari::class.java)
             intent.putExtra("kategoriID", list[position].kategoriID)
             intent.putExtra("kategoriAd", list[position].kategoriAd)
             holder.itemView.context.startActivity(intent)*/

            // val tamEkranResimFragment = TamEkranResimFragment
            // tamEkranResimFragment.newInstance(list[position].fotograf).show((holder.view.context as AppCompatActivity).supportFragmentManager, "ad")

        }
    }

    fun update(newList: List<UrunKategoriJSON>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}