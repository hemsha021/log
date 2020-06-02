package vion.logtracks.utils;

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONException
import org.json.JSONObject
import vion.logtracks.R
import vion.logtracks.activities.SplashActivity
import vion.logtracks.billing.PurchaseHelper

class FCMEvents : FirebaseMessagingService(), PurchaseHelper.PurchaseHelperListener {

    var channelId = "notify-online-status"
    var channelName = "notify-online-status"
    lateinit var context: Context
    lateinit var purchaseHelper: PurchaseHelper
    var isPremium = false

    override fun onCreate() {
        super.onCreate()
        context = this
        purchaseHelper = PurchaseHelper(this,this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        try {
            val data = JSONObject(remoteMessage.data["data"]!!)
            Log.e("obj", data.toString())

            if(isPremium){
                val type = data.getString("status")

                if (type.equals(" is Online", ignoreCase = true)) {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(Intent("REFRESH"))
                    if (PrefManager.getBoolean("online_on")) {

                        buildNotification(data.getString("title"), data.getString("status"))

                    }
                }

                if (type.equals(" is Offline", ignoreCase = true)) {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(Intent("REFRESH"))
                    if (PrefManager.getBoolean("offline_on")) {
                        buildNotification(data.getString("title"), data.getString("status"))
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
    }

    private fun buildNotification(title: String, message: String) {
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var importance = NotificationManager.IMPORTANCE_HIGH

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var mChannel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
        }

        val intent = Intent(this, SplashActivity::class.java)
        val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val mBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(null)

        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntent(intent)

        val resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(resultPendingIntent)
        notificationManager.cancelAll()
        notificationManager.notify(Math.random().toInt(), mBuilder.build())

        sendbroadcast()
    }

    private fun sendbroadcast() {
        val intent = Intent("PAGE")
        intent.putExtra("page", 1)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    override fun onServiceConnected(resultCode: Int) {
        purchaseHelper.getPurchasedItems(BillingClient.SkuType.SUBS)
    }

    override fun onSkuQueryResponse(skuDetails: MutableList<SkuDetails>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPurchasehistoryResponse(purchasedItems: MutableList<Purchase>?) {
        isPremium = !purchasedItems.isNullOrEmpty()
    }

    override fun onPurchasesUpdated(responseCode: Int, purchases: MutableList<Purchase>?) {
        purchaseHelper.getPurchasedItems(BillingClient.SkuType.SUBS)
    }

}