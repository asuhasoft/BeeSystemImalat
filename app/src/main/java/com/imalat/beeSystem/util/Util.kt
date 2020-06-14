package com.imalat.beeSystem.util


import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.imalat.beeSystem.R
import java.math.RoundingMode
import java.text.DecimalFormat

//Extension

/*
fun String.myExtension(myParameter: String) {
    println(myParameter)
}

 */

fun ImageView.downloadFromUrl(url: String?, progressDrawable: CircularProgressDrawable) {

    //Log.d("url foto",url)

    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.logo_be_siparis)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)

}

fun placeholderProgressBar(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

fun downloadImage(view: ImageView, url: String?) {
    view.downloadFromUrl(url, placeholderProgressBar(view.context))
}

fun roundOffDecimalEski(number: Float): Float? {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(number).toFloat()
}

/*
fun kayitEkle(context: Context, hangisi: String, deger: String) {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    EncryptedSharedPreferences.create(
            "encrypted_preferences", // fileName
            masterKeyAlias, // masterKeyAlias
            context, // context
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // prefKeyEncryptionScheme
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // prefvalueEncryptionScheme
    ).edit {
        this.putString(hangisi,deger)
    }
}

fun kayitGetirSifreli(context: Context, hangisi: String) {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    EncryptedSharedPreferences.create(
            "encrypted_preferences", // fileName
            masterKeyAlias, // masterKeyAlias
            context, // context
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // prefKeyEncryptionScheme
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // prefvalueEncryptionScheme
    ).getString(hangisi,"")
}*/