package vion.logtracks.activities

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import kotlinx.android.synthetic.main.activity_plan.*
import kotlinx.android.synthetic.main.activity_plan_userid.*
import vion.logtracks.R
import vion.logtracks.adapters.PlanAdapter
import vion.logtracks.billing.PurchaseHelper
import vion.logtracks.network.ApiController
import vion.logtracks.network.AppConstant
import vion.logtracks.network.ResponseListener
import vion.logtracks.utils.PlanClick
import vion.logtracks.utils.PrefManager
import vion.logtracks.utils.Utils

class PlanActivity : AppCompatActivity(), ResponseListener, PurchaseHelper.PurchaseHelperListener {

    lateinit var apiController: ApiController
    var activity: Activity = this@PlanActivity
    lateinit var helper:PurchaseHelper
    private lateinit var skuDetailsList: MutableList<SkuDetails>
    var skuname = "";
    private var myClipboard: ClipboardManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)

        helper = PurchaseHelper(this,this)
        tvUserId.text = PrefManager.getString("user_id")
        initAPI()
        viewClicks()
    }

    fun initAPI() {
        apiController = ApiController(activity, this)
    }

    fun viewClicks() {
        btnBackPlan.setOnClickListener {
            onBackPressed()
        }
        myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?

        ivCopy.setOnClickListener {
            var myClip = ClipData.newPlainText("text", PrefManager.getString("user_id"));
            myClipboard?.setPrimaryClip(myClip)
            Utils.showSnackBar(activity,"User Id "+ PrefManager.getString("user_id")+" has been copied to clipboard.")
        }
    }

    override fun onSuccess(tag: String, response: String) {
        if (tag == AppConstant.PAYMENT_SUCCESS) {
            finish()
            //Toast.makeText(this, "Payment Successfully", Toast.LENGTH_LONG).show()
        }
    }

    override fun onFailure(tag: String, msg: String) {
        //startActivity(Intent(this@PlanActivity, MainActivity::class.java))
        finish()
    }

    override fun onError(tag: String, msg: String) {
        //startActivity(Intent(this@PlanActivity, MainActivity::class.java))
        finish()
    }

    override fun onNoConnection(tag: String, msg: String) {
        Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
        //startActivity(Intent(this@PlanActivity, MainActivity::class.java))
        finish()
    }


    override fun onServiceConnected(resultCode: Int) {
        helper.getSkuDetails(listOf(AppConstant.PlanMonthly, AppConstant.PlanWeekly, AppConstant.PlanThreeMonthly),
            BillingClient.SkuType.SUBS)
    }

    override fun onSkuQueryResponse(skuDetails: MutableList<SkuDetails>?) {
        this.skuDetailsList = skuDetails!!
        setPlanRV(skuDetails)
    }

    private fun setPlanRV(result: MutableList<SkuDetails>) {
        rvPremiumPlan.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvPremiumPlan.adapter = PlanAdapter(activity, result, PlanClick {
            skuname = skuDetailsList[it].sku
            helper.launchBillingFLow(BillingClient.SkuType.SUBS,skuDetailsList[it].sku)
        })
    }

    override fun onPurchasehistoryResponse(purchasedItems: MutableList<Purchase>?) {

    }

    override fun onPurchasesUpdated(responseCode: Int, purchases: MutableList<Purchase>?) {

        purchases?.let {
            val params = HashMap<String, String>()
            var plan_id = ""
            if(skuname == "sub_weekly"){
                plan_id = "1"
                params["plan_id"] = plan_id
                params["user_id"] = PrefManager.getString("user_id")
                apiController.makeCall(AppConstant.PAYMENT_SUCCESS, params)
            }else if(skuname == "sub_monthly"){
                plan_id = "2"
                params["plan_id"] = plan_id
                params["user_id"] = PrefManager.getString("user_id")
                apiController.makeCall(AppConstant.PAYMENT_SUCCESS, params)
            }else if(skuname == "sub_three_month"){
                plan_id = "3"
                params["plan_id"] = plan_id
                params["user_id"] = PrefManager.getString("user_id")
                apiController.makeCall(AppConstant.PAYMENT_SUCCESS, params)
            }else{
                Utils.showSnackBar(activity,"Please buy a Plan to use the service")
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        apiController.unSubscribe()
    }

}
