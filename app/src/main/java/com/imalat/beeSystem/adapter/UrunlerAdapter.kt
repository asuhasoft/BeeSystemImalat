package com.imalat.beeSystem.adapter


import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.imalat.beeSystem.R
import com.imalat.beeSystem.interfacee.UrunlerGuncelleInterface
import com.imalat.beeSystem.model.urunler.UrunJSON
import com.imalat.beeSystem.util.EnumX
import com.imalat.beeSystem.util.Fonk
import com.imalat.beeSystem.util.downloadFromUrl
import com.imalat.beeSystem.util.placeholderProgressBar
import com.imalat.beeSystem.view.kategoriler.UrunDetaylari
import com.imalat.beeSystem.view.profil.GirisYap
import kotlinx.android.synthetic.main.liste_satiri_urunler.view.*
import java.util.*

class UrunlerAdapter(
    val list: ArrayList<UrunJSON>,
    val urunlerGuncelleInterface: UrunlerGuncelleInterface
) :
    RecyclerView.Adapter<UrunlerAdapter.ViewHpolder>() {

    class ViewHpolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHpolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.liste_satiri_urunler, parent, false)
        return ViewHpolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHpolder, position: Int) {

        holder.view.tvBirimFiyat.text = list[position].birimFiyat
        holder.view.tvUrunAdi.text = list[position].urunAdi
        holder.view.tvKampanyaliFiyat.text = list[position].kampanyaliFiyat + " TL"
        holder.view.tvFiyat.text = list[position].fiyat + " TL"
        holder.view.tvFiyat.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        holder.view.tvIndirimOrani.text =
            "%${Fonk.indirimOraniHesapla(list[position].fiyat, list[position].kampanyaliFiyat)}"

        holder.view.indirimLayout.bringToFront()
        holder.view.tvAdet.text =
            Fonk.artisMikatriParcala(list[position].sepettekiAdet.toString()) + " " + list[position].birimAd


        if (Fonk.doubleCevir(list[position].sepettekiAdet) > 0.0) {
            holder.view.cardViewSepeteEkle.visibility = View.INVISIBLE

        } else {
            holder.view.cardViewSepeteEkle.visibility = View.VISIBLE

        }


        //indirim oranı yoksa gizle
        if (list[position].fiyat.equals(list[position].kampanyaliFiyat)) {
            holder.view.indirimLayout.visibility = View.INVISIBLE
            holder.view.tvFiyat.visibility = View.GONE
        } else {
            holder.view.indirimLayout.visibility = View.VISIBLE
            holder.view.tvFiyat.visibility = View.VISIBLE
        }


        //ürün favoriye ekli mi değil mi
        if (list[position].favori.equals("1")) {
            holder.view.imAlisVerisListesi.setImageResource(R.drawable.x_ic_add_list_active)
        } else {
            holder.view.imAlisVerisListesi.setImageResource(R.drawable.x_ic_add_list)
        }

        holder.view.imUrunFoto.downloadFromUrl(
            list[position].fotograf,
            placeholderProgressBar(holder.view.context)
        )


        holder.view.cardViewSepeteEkle.setOnClickListener {

            if (Fonk.isNetworkAvailable(holder.view.context)) {

                if (Fonk.degerGetir(holder.view.context, EnumX.OturumKodu.toString())
                        .isNullOrBlank()
                ) {

                    val builder: AlertDialog.Builder = AlertDialog.Builder(holder.view.context)
                    builder.setTitle(holder.view.context.resources.getString(R.string.uyari))
                   // builder.setMessage("En uygun fiyatlarla alışveriş yapmak ve kampanyalardan faydalanmak için hemen ücretsiz üye olun")
                    builder.setMessage("Üye olun")
                    builder.setPositiveButton(
                        "Ücretsiz Üye Ol",
                        DialogInterface.OnClickListener { dialogInterface, i ->

                            holder.view.context.startActivity(
                                Intent(
                                    holder.view.context,
                                    GirisYap::class.java
                                )
                            )

                        })
                    builder.setNegativeButton(
                        holder.view.context.resources.getString(R.string.iptal),
                        DialogInterface.OnClickListener { dialogInterface, i ->

                        })
                    builder.show()

                } else {

                    list[position].sepettekiAdet = "1"
                    holder.view.tvAdet.setText("1 " + list[position].birimAd)
                    urunlerGuncelleInterface.sepeteEkleCikart(list[position].urunID, "1")
                    holder.view.cardViewSepeteEkle.visibility = View.INVISIBLE
                }
            }

        }

        holder.view.setOnClickListener {

            if (Fonk.isNetworkAvailable(holder.view.context)) {
                val intent = Intent(holder.view.context, UrunDetaylari::class.java)
                intent.putExtra("urunID", list[position].urunID)
                intent.putExtra("favori", list[position].favori)
                intent.putExtra("position", position)
                //holder.itemView.context.startActivity(intent)
                (holder.view.context as AppCompatActivity).startActivityForResult(intent, 111)
            }

        }

        holder.view.imUrunSayisiArttir.setOnClickListener {

            val artisMiktari: Double = Fonk.doubleCevir(list[position].artisMiktari)
            val maksSipMiktar: Double = Fonk.doubleCevir(list[position].maksSipMiktar)
            val sepettekiAdet: Double = Fonk.doubleCevir(list[position].sepettekiAdet)

            if (maksSipMiktar > sepettekiAdet) {

                val yeniMiktar = Fonk.sayiFormatla(sepettekiAdet + artisMiktari)
                list[position].sepettekiAdet = yeniMiktar.toString()
                holder.view.tvAdet.text =
                    Fonk.artisMikatriParcala(yeniMiktar.toString()) + " " + list[position].birimAd

                urunlerGuncelleInterface.sepeteEkleCikart(
                    list[position].urunID,
                    yeniMiktar.toString()
                )

            } else {
                Fonk.alertDialogGoster(
                    holder.view.context,
                    "",
                    "${list[position].urunAdi} için maksimum sipariş adetine ulaştınız"
                )
            }

        }

        holder.view.imUrunSayisiAzalt.setOnClickListener {


            /*val db = DatabaseUrunler(holder.view.context)

            val sepetAdet = db.urunEkle(
                Fonk.inteCevir(list[position].urunID),
                list[position].urunAdi,
                list[position].kampanyaliFiyat,
                "azalt",
                list[position].artisMiktari,
                list[position].maksSipMiktar,
                list[position].birimAd,
                list[position].fotograf
            )
            holder.view.tvAdet.setText(Fonk.artisMikatriParcala(sepetAdet) + " " + list[position].birimAd)

            list[position].sepettekiAdet = Fonk.doubleCevir(sepetAdet)


            db.close()

            urunlerGuncelleInterface.guncelle()
            notifyDataSetChanged()*/

            val artisMiktari: Double = Fonk.doubleCevir(list[position].artisMiktari)
            val maksSipMiktar: Double = Fonk.doubleCevir(list[position].maksSipMiktar)
            val sepettekiAdet: Double = Fonk.doubleCevir(list[position].sepettekiAdet)

            if (sepettekiAdet > 0.0) {

                val yeniMiktar = Fonk.sayiFormatla(sepettekiAdet - artisMiktari)
                list[position].sepettekiAdet = yeniMiktar.toString()
                holder.view.tvAdet.text =
                    Fonk.artisMikatriParcala(yeniMiktar.toString()) + " " + list[position].birimAd

                urunlerGuncelleInterface.sepeteEkleCikart(
                    list[position].urunID,
                    yeniMiktar.toString()
                )

            } else {
                Fonk.alertDialogGoster(
                    holder.view.context,
                    "",
                    "${list[position].urunAdi} için maksimum sipariş adetine ulaştınız"
                )
            }

            notifyDataSetChanged()

        }



        holder.view.imAlisVerisListesi.setOnClickListener {


            if (!Fonk.degerGetir(holder.view.context, EnumX.OturumKodu.toString())
                    .isNullOrBlank()
            ) {

                if (Fonk.isNetworkAvailable(holder.view.context)) {
                    urunlerGuncelleInterface.alisVerisListesineAl(list[position].urunID)
                }
                //yapılan değişikliğin anında yanısması için
                if (list[position].favori != null && list[position].favori.equals("1")) {
                    list[position].favori = "0"
                    // holder.view.imAlisVerisListesi.setImageResource(R.drawable.listeye_eklendi)
                } else {
                    list[position].favori = "1"
                    // holder.view.imAlisVerisListesi.setImageResource(R.drawable.x_ic_add_list)
                }
                notifyDataSetChanged()
            } else {

                val builder: AlertDialog.Builder = AlertDialog.Builder(holder.view.context)
                builder.setTitle(holder.view.context.resources.getString(R.string.uyari))
                builder.setMessage("Üye olun")
                //builder.setMessage("Kendi alışveriş listenizi oluşturmak ve kampanyalardan faydalanmak için hemen ücretsiz üye olun")
                builder.setPositiveButton(
                    "Ücretsiz Üye Ol",
                    DialogInterface.OnClickListener { dialogInterface, i ->

                        holder.view.context.startActivity(
                            Intent(
                                holder.view.context,
                                GirisYap::class.java
                            )
                        )

                    })
                builder.setNegativeButton(
                    holder.view.context.resources.getString(R.string.iptal),
                    DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                builder.show()
            }
        }
    }

    fun update(newList: List<UrunJSON>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun updateAdet(gelenPosition: Int, gelenAdet: String, gelenFavori: String) {
        list[gelenPosition].sepettekiAdet = gelenAdet
        list[gelenPosition].favori = gelenFavori
        notifyDataSetChanged()
    }

}