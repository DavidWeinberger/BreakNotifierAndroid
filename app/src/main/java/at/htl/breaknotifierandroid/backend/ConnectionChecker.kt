package at.htl.breaknotifierandroid.backend

import android.content.Context
import android.net.ConnectivityManager

class ConnectionChecker
{
    companion object {

        fun checkNetworkConnection(connectivityManager: ConnectivityManager): Boolean {
            return if (connectivityManager is ConnectivityManager && connectivityManager != null) {
                val networkInfo = connectivityManager.activeNetworkInfo
                if(networkInfo != null) {
                    networkInfo.isConnected
                } else {
                    false
                }
            }
            else false
        }

    }
}