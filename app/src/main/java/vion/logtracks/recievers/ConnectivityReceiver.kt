package vion.logtracks.recievers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log

class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent : Intent?) {
        Log.i("BroadCast"," On Recieve")
        if (connectivityReceiverListenerActivity != null) {
            Log.i("BroadCast"," On Recieve not null")
            connectivityReceiverListenerActivity!!.onNetworkConnectionChanged(isConnectedOrConnecting(context))
        }
        if (connectivityReceiverListenerFragment != null) {
            Log.i("BroadCast"," On Recieve not null")
            connectivityReceiverListenerFragment!!.onNetworkConnectionChanged(isConnectedOrConnecting(context))
        }
    }

    private fun isConnectedOrConnecting(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        var connectivityReceiverListenerActivity: ConnectivityReceiverListener? = null
        var connectivityReceiverListenerFragment: ConnectivityReceiverListener? = null
    }
}