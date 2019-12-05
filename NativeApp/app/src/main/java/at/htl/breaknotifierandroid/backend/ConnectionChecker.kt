package at.htl.breaknotifierandroid.backend

import android.net.ConnectivityManager

class ConnectionChecker
{
    companion object {

        fun checkNetworkConnection(connectivityManager: ConnectivityManager): Boolean {
            return run {
                val networkInfo = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            }
        }

    }
}