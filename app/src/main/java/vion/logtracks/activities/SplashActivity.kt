package vion.logtracks.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_spash.*
import vion.logtracks.R
import vion.logtracks.billing.PurchaseHelper
import vion.logtracks.models.BaseApiResponse
import vion.logtracks.models.CreateUser
import vion.logtracks.models.GetSubscriber
import vion.logtracks.network.ApiController
import vion.logtracks.network.AppConstant
import vion.logtracks.network.ResponseListener
import vion.logtracks.utils.PrefManager

class SplashActivity : AppCompatActivity(), ResponseListener, PurchaseHelper.PurchaseHelperListener{

    lateinit var apiController: ApiController
    lateinit var purchaseHelper: PurchaseHelper
    var isPremium = false

    var activity: Activity = this@SplashActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spash)
        FirebaseApp.initializeApp(this@SplashActivity)
        val animation = AlphaAnimation(0.2f, 1.0f)
        animation.duration = 1500
        animation.startOffset = 0
        animation.fillAfter = true
        logo.animation = animation
        bottomSplash.animation = animation

        /*val handler = Handler()
        handler.postDelayed(Runnable {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 2000)*/

        apiController = ApiController(this@SplashActivity, this)
        purchaseHelper = PurchaseHelper(this@SplashActivity, this)

        PrefManager.putString("fb", "dfsdf")
        if (!PrefManager.getBoolean("firstSettings")) {
            PrefManager.putBoolean("notification_on", true)
            PrefManager.putBoolean("online_on", true)
            PrefManager.putBoolean("offline_on", true)
        }
    }

    fun getSuscriber() {
        val params = HashMap<String, String>()
        val userID = PrefManager.getString("user_id")
        params["user_id"] = userID

        apiController.makeCall(AppConstant.GET_SUSCRIBER, params)
    }

    fun unSubscribeUser() {
        //Call to unsuscribe user
        val params = HashMap<String, String>()
        params["user_id"] = PrefManager.getString("user_id")
        params["mobile_number"] = PrefManager.getString("code") + PrefManager.getString("targetNumber")

        apiController.makeCall(AppConstant.UNSUSCRIBE_USER, params)
    }

    override fun onSuccess(tag: String, response: String) {

        if (tag == AppConstant.CREATE_USER) {
            val createUser = apiController.parseJson(response, CreateUser::class.java)
            PrefManager.putString("user_id", createUser.result[0].id)
            PrefManager.putString("start", createUser.result[0].start)
            PrefManager.putString("end", createUser.result[0].end)
            PrefManager.putString("plan_id", createUser.result[0].plan_id)
            getSuscriber()
        }

        //Use case for GET SUSCRIBER
        if (tag == AppConstant.GET_SUSCRIBER) {

            val getSubscriber = apiController.parseJson(response, GetSubscriber::class.java)

            if (getSubscriber.result != null) {

                if (getSubscriber.status == 1) {

                    if (getSubscriber.result.isNotEmpty()) {
                        if (isPremium) {
                            PrefManager.putBoolean("subscribed", true)
                            PrefManager.putString("targetName", getSubscriber.result[0].name)
                            PrefManager.putString("code", getSubscriber.result[0].code)
                            PrefManager.putString("targetNumber", getSubscriber.result[0].mobile_number)
                            //if suscriber is there and isPremium
                            startActivity(Intent(this@SplashActivity, TrackActivity::class.java))
                            finish()
                        } else {
                            unSubscribeUser()
                        }
                    } else {
                        PrefManager.putBoolean("subscribed", false)
                        if(!PrefManager.getBoolean("firstSettings")){
                            PrefManager.putBoolean("firstSettings", true)
                            startActivity(Intent(this@SplashActivity, ActivityPrivacyPolicy::class.java))
                        }else{
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        }
                        finish()
                    }
                }
            }else{
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }

        }

        //Use case for Unsuscribe User
        if (tag == AppConstant.UNSUSCRIBE_USER) {

            val unSubUser = apiController.parseJson(response, BaseApiResponse::class.java)
            if (unSubUser.status == 1) {
                if(!PrefManager.getBoolean("firstSettings")){
                    PrefManager.putBoolean("firstSettings", true)
                    startActivity(Intent(this@SplashActivity, ActivityPrivacyPolicy::class.java))
                }else{
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                }
                finish()
                LocalBroadcastManager.getInstance(activity!!).sendBroadcast(Intent("REMOVE_DATA"))
            }
            PrefManager.putBoolean("subscribed", false)
        }

    }

    override fun onFailure(tag: String, msg: String) {
        //Toast.makeText(this,"Failure"+msg, Toast.LENGTH_SHORT).show()
        if (PrefManager.getBoolean("subscribed")) {
            startActivity(Intent(this@SplashActivity, TrackActivity::class.java))
            finish()
        } else {
            if(!PrefManager.getBoolean("firstSettings")){
                PrefManager.putBoolean("firstSettings", true)
                startActivity(Intent(this@SplashActivity, ActivityPrivacyPolicy::class.java))
            }else{
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
            finish()
        }
    }

    override fun onError(tag: String, msg: String) {
        //Toast.makeText(this,"Error - "+msg, Toast.LENGTH_SHORT).show()
        if (PrefManager.getBoolean("subscribed")) {
            startActivity(Intent(this@SplashActivity, TrackActivity::class.java))
            finish()
        } else {
            if(!PrefManager.getBoolean("firstSettings")){
                PrefManager.putBoolean("firstSettings", true)
                startActivity(Intent(this@SplashActivity, ActivityPrivacyPolicy::class.java))
            }else{
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
            finish()
        }
    }

    override fun onNoConnection(tag: String, msg: String) {
        Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()

        if (PrefManager.getBoolean("subscribed")) {
            startActivity(Intent(this@SplashActivity, TrackActivity::class.java))
            finish()
        } else {
            if(!PrefManager.getBoolean("firstSettings")){
                PrefManager.putBoolean("firstSettings", true)
                startActivity(Intent(this@SplashActivity, ActivityPrivacyPolicy::class.java))
            }else{
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
            finish()
        }
    }

    override fun onServiceConnected(resultCode: Int) {
        purchaseHelper.getPurchasedItems(BillingClient.SkuType.SUBS)
    }

    override fun onSkuQueryResponse(skuDetails: MutableList<SkuDetails>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPurchasehistoryResponse(purchasedItems: MutableList<Purchase>?) {
        isPremium = !purchasedItems.isNullOrEmpty()
        saveToken()
    }

    override fun onPurchasesUpdated(responseCode: Int, purchases: MutableList<Purchase>?) {
        purchaseHelper.getPurchasedItems(BillingClient.SkuType.SUBS)
    }

    fun saveToken() {
        FirebaseInstanceId.getInstance()
            .instanceId.addOnSuccessListener(activity) { instanceIdResult ->
                val newToken = instanceIdResult.token
                PrefManager.putString("fb", newToken)
                val params = HashMap<String, String>()
                val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                params["device_id"] = androidId
                params["token"] = newToken
                params["device_type"] = AppConstant.DeviceType

                apiController.makeCall(AppConstant.CREATE_USER, params)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        apiController.unSubscribe()
    }

}