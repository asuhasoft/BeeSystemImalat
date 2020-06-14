package com.imalat.beeSystem.adapter.adres


import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.AdresGuncelleInterface
import com.imalat.beeSystem.model.profil.adresler.adreslerim.MusteriAdresJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.view.profil.adres.AdresEkleDuzenle
import kotlinx.android.synthetic.main.liste_satiri_adresler.view.*
import java.util.*

class AdresAdapter(
    val list: ArrayList<MusteriAdresJSON>,
    val adresGuncelleInterface: AdresGuncelleInterface
) :
    RecyclerView.Adapter<AdresAdapter.CountryViewHpolder>() {

    class CountryViewHpolder(var view: View) : RecyclerView.ViewHolder(view) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHpolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.liste_satiri_adresler, parent, false)
        return CountryViewHpolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CountryViewHpolder, position: Int) {

        //  holder.view.tvAdresAdi.text = list[position].adresAd + "(" + list[position].firmaAd + ")"
        holder.view.tvAdresAdi.text = list[position].adresAd
        holder.view.tvMinumumSiparis.text = list[position].minSipTutar + " TL"
        holder.view.tvServisUcreti.text = list[position].servisUcret + " TL"
        holder.view.tvUcretsizServis.text = list[position].UcretsizServis + " TL"


        if (list[position].varsayilan.equals("0"))
            holder.view.tvVarsayilanYap.text = "Varsayılan Yap"
        else if (list[position].varsayilan.equals("1"))
            holder.view.tvVarsayilanYap.text = "Varsayılan"


        //holder.view.tvAdres.text = "${list[position].siteBinaAd} ${list[position].blokAd} \n ${list[position].katNo} ${list[position].ilceAd}/${list[position].mahalleAd}  "
        holder.view.tvAdres.text =
            "${list[position].mahalleAd}  ${list[position].yolAdi} No:${list[position].disKapiNo}\n${list[position].siteBinaAd} Blok:${list[position].blokAd} Kat:${list[position].katNo} Daire:${list[position].icKapiNo}\n${list[position].ilceAd} / ${list[position].ilAdi}     "


        holder.view.tvAdresSil.setOnClickListener {

            val alert: AlertDialog.Builder = AlertDialog.Builder(holder.view.context)
            alert.setTitle("Adres Sil")
            // alert.setIcon(R.drawable.soruyu_cevaplayan2)
            alert.setCancelable(false)
            alert.setMessage("${list[position].adresAd} isimli adresiniz silinecek onaylıyor musunuz?")

            alert.setPositiveButton("Sil",
                DialogInterface.OnClickListener { dialog, whichButton ->

                    if (Fonk.isNetworkAvailable(holder.view.context))
                        adresGuncelleInterface.adresSil(list[position].musteriAdresID)

                })
            alert.setNegativeButton("İptal",
                DialogInterface.OnClickListener { dialog, whichButton -> dialog.cancel() })
            val alertDialog: AlertDialog = alert.create()
            alertDialog.show()


        }

        holder.view.tvDuzenle.setOnClickListener {


            val intent = Intent(holder.view.context, AdresEkleDuzenle::class.java)
            intent.putExtra("musteriAdresID", list[position].musteriAdresID)
            intent.putExtra("adresAd", list[position].adresAd)
            intent.putExtra("adresTarif", list[position].adresTarif)
            intent.putExtra("blokAd", list[position].blokAd)
            intent.putExtra("disKapiNo", list[position].disKapiNo)
            intent.putExtra("firmaAd", list[position].firmaAd)
            intent.putExtra("katNo", list[position].katNo)
            intent.putExtra("mahalleAd", list[position].mahalleAd)
            intent.putExtra("mahalleID", list[position].mahalleID)
            intent.putExtra("siteBinaAd", list[position].siteBinaAd)
            intent.putExtra("vergiDaire", list[position].vergiDaire)
            intent.putExtra("vergiNo", list[position].vergiNo)
            intent.putExtra("yolAdi", list[position].yolAdi)
            intent.putExtra("icKapiNo", list[position].icKapiNo)
            intent.putExtra("ilAdi", list[position].ilAdi)
            intent.putExtra("ilID", list[position].ilID)
            intent.putExtra("ilceAd", list[position].ilceAd)
            intent.putExtra("ilceID", list[position].ilceID)
            intent.putExtra("alternatifKisi", list[position].alternatifKisi)
            intent.putExtra("alternatifTelefon", list[position].alternatifTelefon)
            holder.view.context.startActivity(intent)

            /*  val adresFragment = AdresDuzenleFragment
              adresFragment.newInstance(
                      "AdresGuncelle",
                      list[position].adresAdi,
                      list[position].firmaAdi, // burası değişecek, firma ismi gelecek
                      list[position].kasaba,
                      list[position].sokak,
                      list[position].postaKodu,
                      list[position].kapiNo,
                      list[position].musteriAdresID
                      // ).show(( holder.view.context as AppCompatActivity).supportFragmentManager, "ad")
              ).show((holder.view.context as AppCompatActivity).supportFragmentManager, "ad")

              */
        }

        holder.view.tvVarsayilanYap.setOnClickListener {
            adresGuncelleInterface.adresVarsayilanYap(list[position].musteriAdresID)
        }



        holder.itemView.setOnClickListener {

            Fonk.kayitEkle(
                holder.itemView.context,
                EnumX.minSiparisTutari.toString(),
                list[position].minSipTutar
            )
            Fonk.kayitEkle(
                holder.itemView.context,
                EnumX.secilenAdresID.toString(),
                list[position].musteriAdresID
            )
            Fonk.kayitEkle(
                holder.itemView.context,
                EnumX.secilenAdresBaslik.toString(),
                list[position].adresAd
            )
            Fonk.kayitEkle(
                holder.itemView.context,
                EnumX.secilenAdres.toString(),
                "${list[position].mahalleAd}  ${list[position].yolAdi} No:${list[position].disKapiNo}\n${list[position].siteBinaAd} Blok:${list[position].blokAd} Kat:${list[position].katNo} Daire:${list[position].icKapiNo}\n${list[position].ilceAd} / ${list[position].ilAdi}     "
            )

            Fonk.kayitEkle(
                holder.itemView.context,
                EnumX.servisUcreti.toString(),
                list[position].servisUcret
            )

            Fonk.kayitEkle(
                holder.itemView.context,
                EnumX.ucretsizServis.toString(),
                list[position].UcretsizServis
            )

            adresGuncelleInterface.activiteyiKapat()

        }

    }

    fun update(newList: List<MusteriAdresJSON>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}