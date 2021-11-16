package com.codelog.secury.function

import android.content.Context
import android.net.ConnectivityManager

class CheckInternet{

    companion object{

        fun check(context: Context): Boolean {
            val con_manager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return (con_manager.activeNetworkInfo != null && con_manager.activeNetworkInfo.isAvailable
                    && con_manager.activeNetworkInfo.isConnected)
        }

    }

}