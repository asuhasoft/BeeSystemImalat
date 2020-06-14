package com.imalat.beeSystem.adapter


import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.SepetGuncelleInterface
import com.imalat.beeSystem.model.SepetOnline.SepetJSON
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.downloadFromUrl
import com.imalat.beeSystem.util.placeholderProgressBar
import kotlinx.android.synthetic.main.liste_satiri_sepetim.view.*
import java.util.*

class SepetAdapter(
    val list: ArrayList<SepetJSON>,
    val sepetGuncelleInterface: SepetGuncelleInterface
) :
    RecyclerView.Adapter<SepetAdapter.ViewHpolder>() {

    class ViewHpolder(var view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHpolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.liste_satiri_sepetim, parent, false)
        return ViewHpolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHpolder, position: Int) {

        /*

            "Fiyat": "600.00",
            "KampanyaliFiyat": "499.00",
            "Fotograf": "https://www.biyons.com/beeFoto/urun/Urun6736923571455665.jpg",
            "SepettekiAdet": "1.00",
            "ToplamFiyat": "499.00",
            "Indirim": "249.50",
            "AnaKampanyaID": "2",
            "KampanyaAd": "Muhteşem Bahar Kampanyası"
         */
        /*
        todo:  "ArtisMiktari": "1.00",
            "MaksSipMiktar": "5.00",
            "Fiyat": "600.00",
            "KampanyaliFiyat": "499.00",
            "Fotograf": "https://www.biyons.com/beeFoto/urun/Urun6736923571455665.jpg",
            "SepettekiAdet": "2.00",
            "ToplamFiyat": "998.00",
            "Indirim": "249.50",
            "AnaKampanyaID": "2",
            "KampanyaAd": "Muhteşem Bahar Kampanyası"
         */

        val sepettekiAdet: Double = Fonk.doubleCevir(list[position].sepettekiAdet)
        val fiyat: Double = Fonk.doubleCevir(list[position].fiyat)
        val kampanyaliFiyat: Double = Fonk.doubleCevir(list[position].kampanyaliFiyat)

        val indirimliFiyat: Double = Fonk.doubleCevir(list[position].indirim)
        val toplamFiyat: Double = Fonk.doubleCevir(list[position].toplamFiyat)

        holder.view.tvUrunAdi.text = list[position].urunAdi
        holder.view.tvFiyat.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        holder.view.tvAdetSepet.setText(Fonk.artisMikatriParcala(list[position].sepettekiAdet.toString()) + " " + list[position].birimAd)



        if (list[position].kampanyaAd.isNullOrBlank()) {

            //normal fiyat kullnılacak
            holder.view.tvFiyat.text = Fonk.sayiFormatla((sepettekiAdet * fiyat)) + " TL"
            holder.view.tvTutar.text = list[position].toplamFiyat + " TL"

            if (list[position].kampanyaliFiyat.equals(list[position].fiyat)) {
                holder.view.tvBilgilendirme.setText("")
                holder.view.tvFiyat.visibility = View.INVISIBLE
                holder.view.tvBilgilendirme.visibility = View.INVISIBLE
            } else {
                holder.view.tvBilgilendirme.setText(" İndirimli ")
                holder.view.tvFiyat.visibility = View.VISIBLE
                holder.view.tvBilgilendirme.visibility = View.VISIBLE
            }

        } else {

            //indirimli fiyat kullanılacak
            holder.view.tvFiyat.text = "${Fonk.sayiFormatla((sepettekiAdet * kampanyaliFiyat))}  TL"
            holder.view.tvTutar.text = "${Fonk.sayiFormatla(toplamFiyat - indirimliFiyat)} TL"

            holder.view.tvBilgilendirme.setText(list[position].kampanyaAd)
            holder.view.tvFiyat.visibility = View.VISIBLE
            holder.view.tvBilgilendirme.visibility = View.VISIBLE
        }


        holder.view.imUrunFoto.downloadFromUrl(
            list[position].fotograf,
            placeholderProgressBar(holder.view.context)
        )


        holder.view.imUrunSayisiArttir.setOnClickListener {

            if (Fonk.isNetworkAvailable(holder.view.context)) {
                val artisMiktari: Double = Fonk.doubleCevir(list[position].artisMiktari)
                val maksSipMiktar: Double = Fonk.doubleCevir(list[position].maksSipMiktar)
                val sepettekiAdet: Double = Fonk.doubleCevir(list[position].sepettekiAdet)

                if (maksSipMiktar > sepettekiAdet) {

                    val yeniMiktarFortmatli = Fonk.sayiFormatla(sepettekiAdet + artisMiktari)
                    // val yeniMiktarFormatsiz = sepettekiAdet + artisMiktari

                    //  val yeniToplamFiyat:Double = yeniMiktarFormatsiz * Fonk.doubleCevir(list[position].kampanyaliFiyat)


                    //   list[position].sepettekiAdet = yeniMiktarFortmatli.toString()
                    //   list[position].toplamFiyat = Fonk.sayiFormatla(yeniToplamFiyat)

                    //   holder.view.tvAdetSepet.setText(Fonk.artisMikatriParcala(yeniMiktarFortmatli.toString()) + " " + list[position].birimAd)
                    // holder.view.tvTutar.text = list[position].toplamFiyat + " TL"

                    sepetGuncelleInterface.sepeteEkleCikart(
                        list[position].urunID,
                        yeniMiktarFortmatli.toString()
                    )

                } else {
                    Fonk.alertDialogGoster(
                        holder.view.context,
                        "",
                        "${list[position].urunAdi} için maksimum sipariş adetine ulaştınız"
                    )
                }
            }


        }

        holder.view.imUrunSayisiAzalt.setOnClickListener {


            if (Fonk.isNetworkAvailable(holder.view.context)) {
                val artisMiktari: Double = Fonk.doubleCevir(list[position].artisMiktari)
                val maksSipMiktar: Double = Fonk.doubleCevir(list[position].maksSipMiktar)
                val sepettekiAdet: Double = Fonk.doubleCevir(list[position].sepettekiAdet)

                if (sepettekiAdet > 0.0) {

                    val yeniMiktarFortmatli = Fonk.sayiFormatla(sepettekiAdet - artisMiktari)
                    //  val yeniMiktarFormatsiz = sepettekiAdet - artisMiktari

                    //  val yeniToplamFiyat:Double = yeniMiktarFormatsiz * Fonk.doubleCevir(list[position].kampanyaliFiyat)


                    //   list[position].sepettekiAdet = yeniMiktarFortmatli.toString()
                    //  list[position].toplamFiyat = Fonk.sayiFormatla(yeniToplamFiyat)

                    //  holder.view.tvAdetSepet.setText(Fonk.artisMikatriParcala(yeniMiktarFortmatli.toString()) + " " + list[position].birimAd)
                    // holder.view.tvTutar.text = list[position].toplamFiyat + " TL"

                    sepetGuncelleInterface.sepeteEkleCikart(
                        list[position].urunID,
                        yeniMiktarFortmatli.toString()
                    )


                    //sıfıra ulaştıysa listeden kaldır
                    Log.d("yeniMiktar", yeniMiktarFortmatli)
                    if (yeniMiktarFortmatli.equals("0") || yeniMiktarFortmatli.equals("0.00"))
                        list.removeAt(position)

                }

                // notifyDataSetChanged()
            }


        }

        holder.view.imSil.setOnClickListener {

            if (Fonk.isNetworkAvailable(holder.view.context)) {

                sepetGuncelleInterface.sepeteEkleCikart(list[position].urunID, "0")
                //list.removeAt(position)

                // notifyDataSetChanged()

                Toast.makeText(holder.view.context, "Sepetten çıkartıldı", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }

    fun update(newList: List<SepetJSON>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}