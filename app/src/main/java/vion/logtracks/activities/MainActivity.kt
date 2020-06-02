package vion.logtracks.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import kotlinx.android.synthetic.main.activity_main.*
import vion.logtracks.R
import vion.logtracks.billing.PurchaseHelper
import vion.logtracks.models.SusUser
import vion.logtracks.network.ApiController
import vion.logtracks.network.AppConstant
import vion.logtracks.network.ResponseListener
import vion.logtracks.utils.PrefManager

class MainActivity : AppCompatActivity(), ResponseListener, PurchaseHelper.PurchaseHelperListener {

    lateinit var apiController: ApiController
    lateinit var purchaseHelper: PurchaseHelper
    var isPremium = false
    var activity = this@MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        purchaseHelper = PurchaseHelper(activity, this)
        apiController = ApiController(this@MainActivity, this)
        viewClicks()
    }

    override fun onResume() {
        super.onResume()
        btnPlan.isEnabled = true
    }

    private fun viewClicks() {
        btnPlan.setOnClickListener {
            startActivity(Intent(this@MainActivity, PlanActivity::class.java))
            btnPlan.isEnabled =false
        }
        /*btnPlanBottom.setOnClickListener {
            startActivity(Intent(this@MainActivity, PlanActivity::class.java))
            btnPlan.isEnabled =false
        }*/
        btnStartTracking.setOnClickListener {
            if (!isPremium) {
                showDialog(null)
            } else {
                Toast.makeText(activity, "Please wait while we process your request.", Toast.LENGTH_SHORT).show()
                subscribeUser()
            }
        }
    }

    fun subscribeUser() {
        //Call to wsuscribe User
        val params = HashMap<String, String>()
        params["user_id"] = PrefManager.getString("user_id")
        params["mobile_number"] = etvPhoneNumberText.text.toString()
        params["name"] = etvName.text.toString()
        params["code"] = ccp.selectedCountryCode

        if (etvName.text.toString() == "") {
            Toast.makeText(activity!!, "Name cannot be empty", Toast.LENGTH_LONG).show()
        } else if (etvPhoneNumberText.text.toString() == "") {
            Toast.makeText(activity!!, "Number cannot be empty", Toast.LENGTH_LONG).show()
        } else if ((etvPhoneNumberText.text.toString().length < 4) || (etvPhoneNumberText.text.toString().length > 15)) {
            Toast.makeText(activity!!, "Please enter a valid number", Toast.LENGTH_LONG).show()
        } else{
            apiController.makeCall(AppConstant.SUSCRIBE_USER, params)
        }
    }

    fun showDialog( bv: View?){
        //bv!!.visibility = View.VISIBLE
        val dialog = Dialog(this@MainActivity, R.style.DialogFragmentTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val yesBtn = dialog.findViewById(R.id.btnPlanBottom) as Button
        yesBtn.setOnClickListener {
            //bv!!.visibility = View.GONE
            dialog.dismiss()
            startActivity(Intent(activity, PlanActivity::class.java))
        }
        dialog.show()
    }

    override fun onSuccess(tag: String, response: String) {
        //Use case for User Suscribed
        if (tag == AppConstant.SUSCRIBE_USER) {

            ccp.setCcpClickable(false)

            ccp.setCcpClickable(false)
            val susUser = apiController.parseJson(response, SusUser::class.java)
            PrefManager.putBoolean("subscribed", true)
            PrefManager.putString("targetName", etvName.text.toString())
            PrefManager.putString("targetNumber", etvPhoneNumberText.text.toString())
            PrefManager.putString("code", ccp.selectedCountryCode)
            if (susUser.status == 1) {
                btnStartTracking.isEnabled = true
                Toast.makeText(activity, "Tracking started Successfully", Toast.LENGTH_SHORT).show()
                LocalBroadcastManager.getInstance(activity!!).sendBroadcast(Intent("UPDATE_DATA"))
                startActivity(Intent(this@MainActivity, TrackActivity::class.java))
                finishAffinity()
            }
        }

    }

    override fun onFailure(tag: String, msg: String) {
        Toast.makeText(activity, "Oops! Something is wrong! Please contact the support team. ", Toast.LENGTH_SHORT).show()
    }

    override fun onError(tag: String, msg: String) {
        Toast.makeText(activity, "Oops! Something is wrong! Please contact the support team. ", Toast.LENGTH_SHORT).show()
    }

    override fun onNoConnection(tag: String, msg: String) {
        Toast.makeText(activity, "No internet Connection", Toast.LENGTH_SHORT).show()
    }

    override fun onServiceConnected(resultCode: Int) {
        purchaseHelper.getPurchasedItems(BillingClient.SkuType.SUBS)
    }

    override fun onSkuQueryResponse(skuDetails: MutableList<SkuDetails>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPurchasehistoryResponse(purchasedItems: MutableList<Purchase>?) {
        if (!purchasedItems.isNullOrEmpty()) {
            isPremium = true
            btnPlan.visibility = View.GONE
        }else{
            btnPlan.visibility = View.VISIBLE
            isPremium = false
            btnPlan.isEnabled =true
        }
    }

    override fun onPurchasesUpdated(responseCode: Int, purchases: MutableList<Purchase>?) {
        purchaseHelper.getPurchasedItems(BillingClient.SkuType.SUBS)
    }

    override fun onDestroy() {
        super.onDestroy()
        apiController.unSubscribe()
    }

}
