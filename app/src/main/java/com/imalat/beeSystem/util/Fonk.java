package com.imalat.beeSystem.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.appcompat.app.AlertDialog;

import com.imalat.beeSystem.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

/**
 *
 */

public class Fonk {

    ////kullanıcı işlemleri
    public static ArrayList<HashMap<String, String>> linkHashMap = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> yetkiHashMap = new ArrayList<HashMap<String, String>>();


    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if ((activeNetworkInfo != null) && (activeNetworkInfo.isConnected())) {
            return true;
        } else {
            alertDialogGoster(context,
                    context.getString(R.string.uyari),
                    context.getString(R.string.internet_baglantisi_yok));
            return false;
        }
    }

    public static String sha1(String data) //Sha1 şifreleme yapar
    {
        try {
            byte[] b = data.getBytes();
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(b);
            byte messageDigest[] = md.digest();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                result.append(Integer.toString((messageDigest[i] & 0xff) + 0x100, 16).substring(1));
            }

            return result.toString();

        } catch (NoSuchAlgorithmException e) {

            //  Log.e("ARTags", "SHA1 is not a supported algorithm");
        }
        return null;
    }

    public static String karakterTemizle(String gelenDeger) {

        gelenDeger = gelenDeger.replaceFirst("\\s+$", "");//sonundaki boşlukları atar
        gelenDeger = gelenDeger.replaceFirst("^\\s+", "");//başındaki boşlukları atar
        // gelenDeger = gelenDeger.replaceAll("\\p{P}", "");//noktalam işaretleriniatar
        gelenDeger = gelenDeger.replace("$", "");
        gelenDeger = gelenDeger.replace("=", "");
        gelenDeger = gelenDeger.replace("+", "");
        gelenDeger = gelenDeger.replace("£", "");
        gelenDeger = gelenDeger.replace("¥", "");
        gelenDeger = gelenDeger.replace("¢", "");
        gelenDeger = gelenDeger.replace("~", "");
        gelenDeger = gelenDeger.replace("^", "");
        gelenDeger = gelenDeger.replace("<", "");
        gelenDeger = gelenDeger.replace(">", "");

        gelenDeger = gelenDeger.replace("½", "");
        gelenDeger = gelenDeger.replace("/", "");
        gelenDeger = gelenDeger.replace("|", "");
        gelenDeger = gelenDeger.replace("÷", "");
        gelenDeger = gelenDeger.replace("°", "");
        gelenDeger = gelenDeger.replace("©", "");
        gelenDeger = gelenDeger.replace("®", "");
        gelenDeger = gelenDeger.replace("¬", "");
        gelenDeger = gelenDeger.replace("€", "");
        gelenDeger = gelenDeger.replace("¦", "");
        gelenDeger = gelenDeger.replace("™", "");
        gelenDeger = gelenDeger.replace("ₓ", "");
        gelenDeger = gelenDeger.replace("×", "");
        gelenDeger = gelenDeger.replace("′", "");
        gelenDeger = gelenDeger.replace("'", "`");
        gelenDeger = gelenDeger.replace("%", "");


        return gelenDeger;
    }


    public static String stringBoslukTemizle(String gelenDeger) {
        gelenDeger = gelenDeger.replaceFirst("\\s+$", "");//sonundaki boşlukları atar
        gelenDeger = gelenDeger.replaceFirst("^\\s+", "");//başındaki boşlukları atar
        // gelenDeger = gelenDeger.replaceAll("\\p{P}", "");//noktalam işaretleriniatar

        return gelenDeger;
    }

    public static String stringBoslukTemizleHepsiniKucult(String gelenDeger) {

        gelenDeger = gelenDeger.replaceFirst("\\s+$", "");//sonundaki boşlukları atar
        gelenDeger = gelenDeger.replaceFirst("^\\s+", "");//başındaki boşlukları atar
        // gelenDeger = gelenDeger.replaceAll("\\p{P}", "");//noktalam işaretleriniatar

        return gelenDeger;
    }

    public static String KarakterTemizleEnter(String gelen) {
        // gelen = gelen.replace("\n", " ").replace("\r", " ");
        gelen = gelen.replace("\r", " ").replace("\r", " ");

        gelen = stringBoslukTemizle(gelen);
        gelen = gelen.replace(":", "");
        return gelen;
    }


    public static void alertDialogGoster(Context mContext, String Baslik, String Mesaj) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle(Baslik);
        alertDialog.setMessage(Mesaj);
        alertDialog.setCancelable(false);
        alertDialog.setButton(RESULT_OK, mContext.getString(R.string.tamam), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    public static boolean isEmailValid(String email) { //mail formatı kontrol eder
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    ////

    public static String versiyonName(Context mcontext) {
        PackageInfo pinfo = null;
        String versionName = "";
        try {
            pinfo = mcontext.getPackageManager().getPackageInfo(mcontext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // int versionNumber = pinfo.versionCode;
        //uygulamaninVersiyonNumarasi = pinfo.versionCode;
        versionName = pinfo.versionName;
        return versionName;
    }

    public static int versiyoncode(Context mcontext) {
        PackageInfo pinfo = null;
        String versionName = "";
        try {
            pinfo = mcontext.getPackageManager().getPackageInfo(mcontext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // int versionNumber = pinfo.versionCode;
        //uygulamaninVersiyonNumarasi = pinfo.versionCode;
        return pinfo.versionCode;
    }

    //----------------------buton basma anim
    public static void butonBasmaAnim(Context mContext, View view) {
        //view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.buton_basma));

       /* güzel
        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation growAnimation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ScaleAnimation shrinkAnimation = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        animationSet.addAnimation(growAnimation);
        animationSet.addAnimation(shrinkAnimation);
        animationSet.setDuration(200);

        view.startAnimation(animationSet);*/

        ScaleAnimation anim = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(300);


        //fadeout.setDuration(500);

        view.startAnimation(anim);
       /* view.postDelayed(new Runnable() {
            @Override
            public void run() {
                //animasyonAsagidanYukariCikis(view);
            }
        }, 200);*/
    }
   /* public static  void shakeAnim(Context mContext, View view)
    {
        final Animation animShake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
        view.startAnimation(animShake);
    }*/


    public static void writeToFile(Context context, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


    public static void kayitEkle(Context context, String hangisi, String deger) {

        //arkaPlanRengi
        //yaziRengi
        //yaziBoyutu
        //yaziFontu
        //secilenFoto
        //secilenFotoID
        //reklamGosterimi

        SharedPreferences sharedPref = context.getSharedPreferences("share", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(hangisi, deger);
        editor.apply();
    }

    public static String degerGetir(Context context, String hangisi) {
        String donenDeger = "";
        SharedPreferences sharedPref = context.getSharedPreferences("share", Context.MODE_PRIVATE);

        if (hangisi.equals(String.valueOf(EnumX.KullaniciAdi)))
            donenDeger = sharedPref.getString(hangisi, "");

        else if (hangisi.equals(String.valueOf(EnumX.benzersizID)))
            donenDeger = sharedPref.getString(hangisi, "");
        else if (hangisi.equals(String.valueOf(EnumX.token)))
            donenDeger = sharedPref.getString(hangisi, "");
        else if (hangisi.equals(String.valueOf(EnumX.puanVer)))
            donenDeger = sharedPref.getString(hangisi, "");
        else if (hangisi.equals(String.valueOf(EnumX.puanVerildi)))
            donenDeger = sharedPref.getString(hangisi, "");
        else if (hangisi.equals(String.valueOf(EnumX.sozlesmeOnaylandiMi)))
            donenDeger = sharedPref.getString(hangisi, "");

        else if (hangisi.equals(String.valueOf(EnumX.Pst_UserName)))
            donenDeger = sharedPref.getString(hangisi, "");
        else if (hangisi.equals(String.valueOf(EnumX.Pst_Password)))
            donenDeger = sharedPref.getString(hangisi, "");

        else if (hangisi.equals(String.valueOf(EnumX.MusteriCep)))
            donenDeger = sharedPref.getString(hangisi, "");
        else if (hangisi.equals(String.valueOf(EnumX.MusteriSifre)))
            donenDeger = sharedPref.getString(hangisi, "");
        else if (hangisi.equals(String.valueOf(EnumX.secilenAdresBaslik)))
            donenDeger = sharedPref.getString(hangisi, "");
        else if (hangisi.equals(String.valueOf(EnumX.secilenAdres)))
            donenDeger = sharedPref.getString(hangisi, "");
        else if (hangisi.equals(String.valueOf(EnumX.secilenAdresID)))
            donenDeger = sharedPref.getString(hangisi, "");
        else if (hangisi.equals(String.valueOf(EnumX.minSiparisTutari)))
            donenDeger = sharedPref.getString(hangisi, "");

        else if (hangisi.equals(String.valueOf(EnumX.katkiMaddeleri)))
            donenDeger = sharedPref.getString(hangisi, "");
        else if (hangisi.equals(String.valueOf(EnumX.alerjenler)))
            donenDeger = sharedPref.getString(hangisi, "");

        else if (hangisi.equals(String.valueOf(EnumX.servisUcreti)))
            donenDeger = sharedPref.getString(hangisi, "");
        else if (hangisi.equals(String.valueOf(EnumX.ucretsizServis)))
            donenDeger = sharedPref.getString(hangisi, "");


        else if (hangisi.equals(String.valueOf(EnumX.ServisPlanID)))
            donenDeger = sharedPref.getString(hangisi, "");
        else if (hangisi.equals(String.valueOf(EnumX.ServisAd)))
            donenDeger = sharedPref.getString(hangisi, "");


        else if (hangisi.equals(String.valueOf(EnumX.OdemeTipi)))
            donenDeger = sharedPref.getString(hangisi, "");
        else if (hangisi.equals(String.valueOf(EnumX.OdemeTipID)))
            donenDeger = sharedPref.getString(hangisi, "");

        else if (hangisi.equals(String.valueOf(EnumX.TeslimatTipi)))
            donenDeger = sharedPref.getString(hangisi, "");
        else if (hangisi.equals(String.valueOf(EnumX.TeslimatTipID)))
            donenDeger = sharedPref.getString(hangisi, "");


        else if (hangisi.equals(String.valueOf(EnumX.SiparisNotum)))
            donenDeger = sharedPref.getString(hangisi, "");

        else if (hangisi.equals(String.valueOf(EnumX.OturumKodu)))
            donenDeger = sharedPref.getString(hangisi, "");

        else if (hangisi.equals(String.valueOf(EnumX.barkod)))
            donenDeger = sharedPref.getString(hangisi, "");


        return donenDeger;
    }


    public static String benzersizIDOlustur(final int sizeOfRandomString) {
        // String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm_!{}][$<>";
        String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnmABCDEFGHIJKLMNOPQRSTUVWXYZ";

        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public static String sifreOlustur(final int sizeOfRandomString) {
        // String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm_!{}][$<>";
        String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnmABCDEFGHIJKLMNOPQRSTUVWXYZ-_";

        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public static String begeniSayisiAyarla(String gelenSayi, String islem) {

        int sayi = 0;

        Log.d("gelen", gelenSayi);

        try {
            sayi = Integer.parseInt(gelenSayi);

        } catch (NumberFormatException e) {
            sayi = 0;
        }

        if (islem.equals("arttir")) sayi++;
        else sayi--;

        return String.valueOf(sayi);
    }


    public static String TarihAyarla(String geleneTarihVerisi) {
        String gunIsmi = "";
        String donenTarihVerisi = ""; //2016-12-11 22:36:24
        String yil = geleneTarihVerisi.substring(0, 4); //4.karakter dahil değil  2016
        String ay = geleneTarihVerisi.substring(5, 7);
        String gun = geleneTarihVerisi.substring(8, 10);
        String saat = "";

        if (geleneTarihVerisi.length() > 10)
            saat = " - " + geleneTarihVerisi.substring(11, 16);

        switch (ay) {
            case "01":
                ay = "Ocak";
                break;
            case "02":
                ay = "Şubat";
                break;
            case "03":
                ay = "Mart";
                break;
            case "04":
                ay = "Nisan";
                break;
            case "05":
                ay = "Mayıs";
                break;
            case "06":
                ay = "Haziran";
                break;
            case "07":
                ay = "Temmuz";
                break;
            case "08":
                ay = "Ağustos";
                break;
            case "09":
                ay = "Eylül";
                break;
            case "10":
                ay = "Ekim";
                break;
            case "11":
                ay = "Kasım";
                break;
            case "12":
                ay = "Aralık";
                break;

        }

        try {

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Date date = df.parse(geleneTarihVerisi);  // bulmak istediğimiz tarih  yıl ay gün şeklinde

            Calendar calendar = Calendar.getInstance();

            calendar.setTime(date);

            int day = calendar.get(Calendar.DAY_OF_WEEK);

            if (day == 1)

                gunIsmi = "Pazar";

            else if (day == 2)

                gunIsmi = "Pazartesi";

            else if (day == 3)

                gunIsmi = "Salı";

            else if (day == 4)

                gunIsmi = "Çarşamba";

            else if (day == 5)

                gunIsmi = "Perşembe";

            else if (day == 6)

                gunIsmi = "Cuma";

            else if (day == 7)

                gunIsmi = "Cumartesi";

        } catch (Exception e) {

            gunIsmi = "?" + e.getMessage();

        }


        return donenTarihVerisi = gun + " " + ay + " " + yil + " " + gunIsmi + saat;
    }


    public static int inteCevir(String gelen) {
        int donen = 0;
        Log.d("intecevir_gelen", gelen);
        try {
            //donen = Integer.parseInt(gelen);
            donen = Integer.parseInt(gelen);
        } catch (NumberFormatException e) {
            donen = 0;
        }

        Log.d("intecevir_donen", String.valueOf(donen));

        return donen;
    }

    public static float floataCevir(String gelen) {
        float donen = 0;
        Log.d("floataCevir", gelen);
        try {
            //donen = Integer.parseInt(gelen);

            donen = Float.parseFloat(gelen);
        } catch (NumberFormatException e) {
            donen = 0;
        }

        //Log.d("floataCevir", String.valueOf(donen));

        return donen;
    }

    public static double doubleCevir(String gelen) {
        double donen = 0.0;
        //eğer gelen sayı içinde virgül bulunuyorsa hata duble ceviremiyor
        // bu yüzden virgülü silmek gerek

        gelen = gelen.replace(",", "");

        //Log.d("doubleCevir", gelen);
        try {
            //donen = Integer.parseInt(gelen);

            donen = Double.parseDouble(gelen);
        } catch (NumberFormatException e) {
            donen = 0;
        }

        //  Log.d("doubleCevir", String.valueOf(donen));

        return donen;
    }

    public static int floatToInt(float gelen) {
        int donen = 0;
        Log.d("floatToInt", String.valueOf(gelen));
        try {
            //donen = Integer.parseInt(gelen);

            donen = (int) gelen;
        } catch (NumberFormatException e) {
            donen = 0;
        }

        Log.d("floataCevir", String.valueOf(donen));

        return donen;
    }


    public static void puanKontrol(Context ctx) {


        if (degerGetir(ctx, String.valueOf(EnumX.puanVerildi)).equals("0")) {

            int kacDefadaSorulacak = 10;


            int simdikiDegeri = inteCevir(degerGetir(ctx, String.valueOf(EnumX.puanVer)));
            simdikiDegeri++;

            if (simdikiDegeri > kacDefadaSorulacak) {
                puanVer(ctx);
            } else {
                kayitEkle(ctx, String.valueOf(EnumX.puanVer), String.valueOf(simdikiDegeri));
            }

            Log.d("puan", String.valueOf(simdikiDegeri));


        } else
            Log.d("puan", "puanverildi");


    }

    public static void puanVer(final Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Puan Verebilir misiniz?");
        // builder.setIcon(R.drawable.ic_star);
        builder.setMessage("Üye olun");
     //   builder.setMessage("Eğer bu uygulamayı beğendiyseniz, Google Play mağazasında yorum yapıp puan verir miydiniz?\nDesteğiniz için teşekkür ederiz");

        builder.setPositiveButton("Şimdi Puan Ver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        builder.setNegativeButton("Daha Sonra", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });


        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Toast.makeText(mContext, "tammmm", Toast.LENGTH_LONG).show();

                if (isNetworkAvailable(mContext)) {

                   /* dataBasePuanVer db = new dataBasePuanVer(mContext);
                    db.puanVeridldiOlarakIsaretle();
                    db.close();*/

                    String url = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mContext.startActivity(i);

                    kayitEkle(mContext, String.valueOf(EnumX.puanVerildi), "1");
                }
                dialog.dismiss();


                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                kayitEkle(mContext, String.valueOf(EnumX.puanVer), "0");
                // sayacSifirlaHedefArttir(mContext);
                //Toast.makeText(mContext, "Daha Sonra ", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });


    }


    public static void gradientArkaPlanOlustur(Context ctx, View view, String renk) {

        String ikinciRenk = renk;
        ikinciRenk = ikinciRenk.replace("#", "#60");


        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.BR_TL,
                new int[]{Color.parseColor(renk), Color.parseColor(ikinciRenk)});
        gd.setCornerRadius(20f);
        view.setBackground(gd);
    }


    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }


    public static void print(String yazi) {

        Log.d("yazdir", yazi);
    }

    public static String linkParcala(String gelenLink) {
        String sonuc = "";

        String[] dizi = gelenLink.split("/");

        for (int i = 0; i < dizi.length; i++) {
            sonuc = dizi[i];
        }

        return sonuc;
    }

    public static String artisMikatriParcala(String gelen) {
        String sonuc = "";

        // Log.d("gelen",gelen);

        String[] dizi = gelen.split("\\."); // nokta tek başına kullanılamaz

        //Log.d("dizi", String.valueOf(dizi.length));
        for (int i = 0; i < dizi.length; i++) {
            sonuc = dizi[i];
            //Log.d("sonuc",sonuc);
        }

        //eğer gelen 1.0 veya 1.00 geliyorsa bunu parçalayıp 1 yaptık değilse aynen devam ettik
        if (sonuc.equals("0") || sonuc.equals("00")) {
            sonuc = dizi[0];
            //Log.d("sonuc1",sonuc);
        } else {
            sonuc = gelen;
            // Log.d("sonuc2",sonuc);
        }

        //Log.d("sonuc2","---------");

        return sonuc;
    }

    //--------şifreli


    public static String sayiFormatla(double deger) {
       /* Log.d("formatGelen", String.valueOf(deger));
        NumberFormat nf = NumberFormat.getInstance(); // get instance
        nf.setMaximumFractionDigits(3); // set decimal places
        String donus = nf.format(deger).replace(",",".");

        Log.d("formatGiden",nf.format(deger));
        return donus;*/

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;

        {
            df.applyPattern("###,##0.00");
        }


       /* DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(symbols);
        df.setGroupingSize(3);
        df.setMaximumFractionDigits(2);*/
        return df.format(deger);


        // DecimalFormat form = new DecimalFormat("#.##");
        // return form.format(deger) ;
    }

    public static String indirimOraniHesapla(String gercekFiyat, String indirimlifiyat) {

        double sonuc = 0.0;

        double gercek = doubleCevir(gercekFiyat);
        double indirimli = doubleCevir(indirimlifiyat);

        sonuc = (int) (Math.round(100 - ((100 * indirimli) / gercek)));

        //sonun 17.0 gibi vir değer olmasın
        return String.valueOf(sonuc).replace(".0", "");
    }


}
