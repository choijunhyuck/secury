package com.codelog.secury.function

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

class GetFavicon{

    companion object{

        fun fetchFavicon(uri: Uri): Bitmap? {
            val iconUri: Uri = uri.buildUpon().path("favicon.ico").build()
            Log.i("TAG", "Fetching favicon from: $iconUri")
            var `is`: InputStream? = null
            var bis: BufferedInputStream? = null
            return try {
                val conn: URLConnection = URL(iconUri.toString()).openConnection()
                conn.connect()
                `is` = conn.getInputStream()
                bis = BufferedInputStream(`is`, 8192)
                BitmapFactory.decodeStream(bis)
            } catch (e: IOException) {
                Log.w("TAG", "Failed to fetch favicon from $iconUri", e)
                null
            }
        }

    }

}