package com.febriandev.vocary.utils

import android.content.Context
import android.net.ConnectivityManager

object ConnHelper {

    fun hasConnection(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}