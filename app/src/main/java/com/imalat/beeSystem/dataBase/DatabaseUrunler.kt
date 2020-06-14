package com.imalat.beeSystem.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.imalat.beeSystem.model.sepet.SepetModel

import com.imalat.beeSystem.util.Fonk
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class DatabaseUrunler(val context: Context) {

    companion object {

        //general
        val dbVersion = 1
        val dbName = "urun_db.sqlite3"
    }

    val database: SQLiteDatabase

    init {
        database = open()
    }

    fun open(): SQLiteDatabase {
        val dbFile = context.getDatabasePath("$dbName")

        // Log.d("dbFile", dbFile.toString())

        if (!dbFile.exists()) {
            try {
                val checkDB = context.openOrCreateDatabase("$dbName", Context.MODE_PRIVATE, null)
                checkDB.close()
                copyDatabase(dbFile)
            } catch (e: IOException) {
                throw RuntimeException("Error opening db")
            }
        }
        return SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READWRITE)
    }

    private fun copyDatabase(dbFile: File) {
        val iss = context.assets.open("$dbName")
        val os = FileOutputStream(dbFile)

        val buffer = ByteArray(1024)
        while (iss.read(buffer) > 0) {
            os.write(buffer)
        }

        os.flush()
        os.close()
        iss.close()
    }

    fun close() {
        database.close()
    }


    //sepette göster------------------------------------------------------------------------------------------------------------------------------------------------------------------------
/*
    fun sepetteGoster(): Pair<ArrayList<SepetModel>, Double> {
        var list: ArrayList<SepetModel> = arrayListOf()

        val db = this.open()
        //val query = "SELECT * FROM gruplamaTablosu where sepettemi = '1' order by id asc "
        val query = "SELECT * FROM gruplamaTablosu where sepettemi = '1' group by GrupID order by id desc "

        Log.d("xxxsepet", query)
        val cursor = db.rawQuery(query, null)

        //var i = 0
        var sepetToplamTutar :Double = 0.0
        if (cursor != null) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                Log.d("XXXurunAdiiii", cursor.getString(6))
                Log.d("XXXicerikkk", cursor.getString(8))

                if (cursor.count > 0) {

                    //eğer menü değilse
                    if (cursor.getString(3).equals("0")) {
                        val urunFiyati = cursor.getString(7)
                        val hammadeBilgi = hammadeGetirBenzersizIdyeGore(db, cursor.getString(2))
                        val hammadeAdlari = hammadeBilgi.first
                        val hammdeToplamFiyati = hammadeBilgi.second

                        val sosBilgi = sosGetirBenzersizIdyeGore(db, cursor.getString(2))
                        val sosAdlari = sosBilgi.first
                        val sostoplamFiyati = sosBilgi.second

                        val ekstraBilgi = ekstraGetirBenzersizIdyeGore(db, cursor.getString(2))
                        val ekstraAdlari = ekstraBilgi.first
                        val ekstraToplamFiyati = ekstraBilgi.second

                        val urunToplamFiyati = Fonk.floataCevir(urunFiyati) + Fonk.floataCevir(sostoplamFiyati) + Fonk.floataCevir(ekstraToplamFiyati)

                        sepetToplamTutar = sepetToplamTutar + urunToplamFiyati


                        var xxx = SepetModel(cursor.getString(1),
                                cursor.getString(6),
                                cursor.getString(8),
                                urunFiyati,
                                hammadeAdlari,
                                hammdeToplamFiyati,
                                sosAdlari,
                                sostoplamFiyati,
                                ekstraAdlari,
                                ekstraToplamFiyati,
                                urunToplamFiyati.toString(),
                                cursor.getString(4) // grupid
                        )
                        list.add(xxx)
                    } else {

                        val urunMenuFiyati = cursor.getString(7)

                        val hammadeBilgiMenu = hammadeGetirMenuyeGore(db, cursor.getString(4))
                        val hammadeAdlariMenu = hammadeBilgiMenu.first
                        val hammdeToplamFiyatiMenu = hammadeBilgiMenu.second

                        val sosBilgiMenu = soslariGetirMenuyeGore(db, cursor.getString(4))
                        val sosAdlariMenu = sosBilgiMenu.first
                        val sosToplamFiyatiMenu = sosBilgiMenu.second

                        val ekstraBilgiMenu = ekstralariGetirMenuyeGore(db, cursor.getString(4))
                        val ekstraAdlariMenu = ekstraBilgiMenu.first
                        val ekstraToplamFiyatiMenu = ekstraBilgiMenu.second

                        val urunMenuToplamFiyati = Fonk.floataCevir(urunMenuFiyati) + Fonk.floataCevir(sosToplamFiyatiMenu) + Fonk.floataCevir(ekstraToplamFiyatiMenu)

                        sepetToplamTutar = sepetToplamTutar + urunMenuToplamFiyati

                        var xxx = SepetModel("-1",
                                cursor.getString(9),
                                cursor.getString(8),
                                urunMenuFiyati,
                                hammadeAdlariMenu,
                                hammdeToplamFiyatiMenu,
                                sosAdlariMenu,
                                sosToplamFiyatiMenu,
                                ekstraAdlariMenu,
                                ekstraToplamFiyatiMenu,
                                urunMenuToplamFiyati.toString(),
                                cursor.getString(4) // grupid
                        )
                        list.add(xxx)

                    }

                }
                cursor.moveToNext()
            }

            cursor.close()
        }
        db.close()

        Log.d("sepetToplamTutar", sepetToplamTutar.toString())

        return Pair(list, sepetToplamTutar)

    }
*/


    //siparis json olustur---------------------------------------------------------------------------------------------------------------------------

    fun siparisJsonOlustur(): String {
        val db = this.open()
        val query = "SELECT * FROM urun  order by id asc "
        val cursor = db.rawQuery(query, null)

        val liste = JSONArray()
        var jobj: JSONObject? = null

        if (cursor != null) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                if (cursor.count > 0) {

                    jobj = JSONObject()
                    try {
                        jobj.put("UrunID", cursor.getString(1))
                        jobj.put("TalepMiktar", cursor.getString(5))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    liste.put(jobj)

                    //----soslar--------

                    /* jobj.put("hammadde", hammaddeGetirJSONArray(db, cursor.getString(2)))
                     jobj.put("soslar", soslariGetirJSONArray(db, cursor.getString(2)))
                     jobj.put("ekstralar", ekstraGetirJSONArray(db, cursor.getString(2)))*/
                    //------------------

                    try {
                        jobj = JSONObject()
                        jobj.put("UrunlerJSON", liste)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
                cursor.moveToNext()
            }

            cursor.close()
        }
        db.close()

        val str_post = jobj.toString()
        Log.d("gonderilenliste", str_post)

        return str_post
    }


    //-urun ekleme--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    fun urunEkle(
        urunID: Int,
        urunAdi: String,
        fiyat: String,
        yapicakIslem: String,
        artisMiktari: String,
        maksSipMiktar: String,
        birimAd: String,
        foto: String
    ): String {

        if (urunEkliMi(urunID)) {
            return urunSayisiArttirAzalt(urunID, yapicakIslem, artisMiktari)
        } else {

            if (yapicakIslem.equals("arttir")) {
                val db = this.open();
                val values = ContentValues()
                values.put("urunID", urunID)
                values.put("urunAdi", urunAdi)
                values.put("fiyat", Fonk.floataCevir(fiyat))
                values.put("adet", 1) // her ürün eklendiğinde önce +1 artsın
                //  values.put("adet", artisMiktari)
                values.put("artisMiktari", artisMiktari)
                values.put("artisMiktari", artisMiktari)
                values.put("maksSipMiktar", maksSipMiktar)
                values.put("birimAd", birimAd)
                values.put("foto", foto)

                Log.d("urunEkle", "ürün eklendi")

                db.insert("urun", null, values);
                db.close();
            } else
                return "0"


            //return artisMiktari
            return "1" // her ürün eklendiğinde önce +1 artsın
        }


    }

    fun urunSayisiArttirAzalt(urunID: Int, yapicakIslem: String, artisMiktari: String): String {

        val db = this.open();
        val values = ContentValues()

        //urun adet ------------------------------------------------------------------------------------
        val query = "SELECT adet from urun  where  urunID = $urunID "
        // Log.d("sqlll", query)
        var urunAdet = 0.0
        val cursor = db.rawQuery(query, null)
        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()
            // if (cursor.isNull(0)) sonuc = 0.0 else sonuc = Fonk.doubleCevir(cursor.getString(0))
            if (cursor.isNull(0)) urunAdet = 0.0 else urunAdet =
                Fonk.doubleCevir(cursor.getString(0))
            cursor.close()

            // Log.d("adett", urunAdet.toString())
        }
        //------------------------------------------------------------------------------------

        if (yapicakIslem.equals("arttir")) {
            urunAdet += Fonk.doubleCevir(artisMiktari)
        } else {

            Log.d("adett_azaltma", urunAdet.toString())
            if (urunAdet > 0) {
                urunAdet -= Fonk.doubleCevir(artisMiktari)

                if (urunAdet == 0.0) {
                    urunSil(urunID, db)
                    return Fonk.sayiFormatla(urunAdet)
                }
            }
        }


        Log.d("sayiformatXX", urunAdet.toString())
        Log.d("sayiformatXX", Fonk.sayiFormatla(urunAdet))
        Log.d("sayiformatXX", "-----------------")

        values.put("adet", Fonk.sayiFormatla(urunAdet))

        db.update("urun", values, "urunID = $urunID ", null)
        db.close();

        return Fonk.sayiFormatla(urunAdet)
    }

    fun urunEkliMi(urunID: Int): Boolean {

        var sonuc = false
        val db = this.open();


        //urun adet ------------------------------------------------------------------------------------
        val query = "SELECT fiyat from urun  where  urunID = $urunID "

        val cursor = db.rawQuery(query, null)
        if (cursor != null && cursor.count > 0) {
            sonuc = true
            cursor.close()
        }
        db.close();
        return sonuc
    }

    fun sepetteKacAdetVar(urunID: Int): Double {

        val db = this.open();
        //urun adet ------------------------------------------------------------------------------------
        val query = "SELECT adet from urun  where  urunID = $urunID "
        // Log.d("sqlll", query)
        var urunAdet = 0.0
        val cursor = db.rawQuery(query, null)
        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()
            // if (cursor.isNull(0)) sonuc = 0.0 else sonuc = Fonk.doubleCevir(cursor.getString(0))
            if (cursor.isNull(0)) urunAdet = 0.0 else urunAdet =
                Fonk.doubleCevir(cursor.getString(0))
            cursor.close()

            //Log.d("adett", urunAdet.toString())
        }

        db.close()
        //------------------------------------------------------------------------------------

        return urunAdet
    }


    fun urunSil(urunID: Int, db: SQLiteDatabase) {
        //val db = this.open()
        db.execSQL("delete from urun where urunID = $urunID ")
        // db.delete("urun", " urunID = $urunID ", null)
        Log.d("urun", "silindi")
        db.close()
    }

    fun resetTablesUrun() {
        val db = this.open()
        db.delete("urun", null, null)
        db.close()
    }


    //-sepetteki üürnler-------------------------------------------------------------------------------------------------------------------------------------------------------------------

    fun sepettekiUrunSayisi(): String {
        val db = this.open()
        var urunSayisi = ""


        val query = "SELECT count (*) from urun where adet > 0"
        val cursor = db.rawQuery(query, null)

        if (cursor != null) {
            cursor.moveToFirst()
            if (cursor.isNull(0)) urunSayisi = "0" else urunSayisi = (cursor.getString(0))
            cursor.close()
        }
        db.close()

        Log.d("sepettekiUrunSayisi", urunSayisi)

        return urunSayisi
    }


    fun sepettekiUrunusil(urunID: String) {
        val db = this.open()
        //db.execSQL("delete from gruplamaTablosu " )
        db.delete("urun", "  urunID = $urunID ", null)
        db.close()
    }


    fun sepetToplamTutari(): String {
        val db = this.open()
        var toplamTutar = ""

        val query = "SELECT sum(fiyat*adet) from urun where adet > 0"
        val cursor = db.rawQuery(query, null)

        if (cursor != null) {
            cursor.moveToFirst()
            if (cursor.isNull(0)) toplamTutar = "0.0" else toplamTutar = (cursor.getString(0))
            cursor.close()
        }
        db.close()

        return toplamTutar
    }


    fun sepetHepsiniGetir(): ArrayList<SepetModel> {
        var list: ArrayList<SepetModel> = arrayListOf()

        val db = this.open()
        //val query = "SELECT * FROM gruplamaTablosu where sepettemi = '1' order by id asc "
        val query = "SELECT * FROM urun where adet > 0  order by id asc "
        val cursor = db.rawQuery(query, null)

        //var i = 0
        var sepetToplamTutar: Double = 0.0
        if (cursor != null) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {

                if (cursor.count > 0) {

                    val urunID = cursor.getString(1)
                    val urunAdi = cursor.getString(2)
                    val urunFiyati = cursor.getString(3)
                    val urunAdet = cursor.getString(5)
                    val maksSipMiktar = cursor.getString(6)
                    val artisMikatri = cursor.getString(7)
                    val foto = cursor.getString(8)
                    val birimAd = cursor.getString(9)

                    Log.d("lokal", "**********************")
                    Log.d("query", query)
                    Log.d("count", cursor.count.toString())
                    Log.d("urunID", urunID)
                    Log.d("urunAdi", urunAdi)
                    Log.d("urunFiyati", urunFiyati)
                    Log.d("urunAdet", urunAdet)
                    Log.d("---", "**********************")


                    val urunToplamTutar: Double =
                        Fonk.doubleCevir(urunFiyati) * Fonk.doubleCevir(urunAdet)


                    var xxx = SepetModel(
                        urunID,
                        urunAdi,
                        Fonk.sayiFormatla(Fonk.doubleCevir(urunFiyati)),
                        urunAdet,
                        maksSipMiktar,
                        artisMikatri,
                        foto,
                        birimAd,
                        Fonk.sayiFormatla(urunToplamTutar).toString()


                    )
                    list.add(xxx)


                }
                cursor.moveToNext()
            }

            cursor.close()
        }
        db.close()



        return list

    }


}


