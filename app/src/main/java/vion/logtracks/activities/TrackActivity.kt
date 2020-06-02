package vion.logtracks.activities

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_track.*
import kotlinx.android.synthetic.main.activity_track_top.*
import vion.logtracks.R
import vion.logtracks.adapters.ReportAdapter
import vion.logtracks.models.BaseApiResponse
import vion.logtracks.models.GetReports
import vion.logtracks.models.GetSubscriber
import vion.logtracks.network.ApiController
import vion.logtracks.network.AppConstant
import vion.logtracks.network.ResponseListener
import vion.logtracks.utils.PrefManager
import java.text.SimpleDateFormat
import java.util.*

class TrackActivity : AppCompatActivity(), ResponseListener {

    lateinit var apiController: ApiController
    var userReports: GetReports? = null
    lateinit var onlineStatusListener: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        apiController = ApiController(this, this)

        btnWhatsTrack.setOnClickListener {
            openWhats()
        }

        btnStopTracking.setOnClickListener {
            Toast.makeText(this@TrackActivity, "Please wait while we process your request.", Toast.LENGTH_SHORT).show()
            unSubscribeUser()
        }

        getSubscriber()
        rvTime.layoutManager = LinearLayoutManager(this@TrackActivity, LinearLayoutManager.VERTICAL, false)

        onlineStatusListener = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                var num = PrefManager.getString("targetNumber")
                val params = HashMap<String, String>()
                params["mobile_number"] = PrefManager.getString("code") + num
                apiController.makeCall(AppConstant.GET_REPORT, params)
            }
        }
        LocalBroadcastManager.getInstance(this@TrackActivity)
            .registerReceiver(onlineStatusListener, IntentFilter("REFRESH"))

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())
        tvTodayDate.text = currentDate
    }


    fun unSubscribeUser() {
        //Call to unsuscribe user
        val params = HashMap<String, String>()
        params["user_id"] = PrefManager.getString("user_id")
        params["mobile_number"] = PrefManager.getString("code") + PrefManager.getString("targetNumber")

        apiController.makeCall(AppConstant.UNSUSCRIBE_USER, params)

    }

    fun getReport() {
        var num = PrefManager.getString("targetNumber")
        val params = HashMap<String, String>()
        params["mobile_number"] = PrefManager.getString("code") + num
        apiController.makeCall(AppConstant.GET_REPORT, params)
    }

    private fun getSubscriber() {
        val params = HashMap<String, String>()
        val userID = PrefManager.getString("user_id")
        params["user_id"] = userID
        apiController.makeCall(AppConstant.GET_SUSCRIBER, params)
    }

    fun openWhats(){
        try{
            val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.setComponent(ComponentName("com.whatsapp", "com.whatsapp.Conversation"))
            sendIntent.setAction(Intent.ACTION_SEND)
            sendIntent.setType("Server is not Working")
            sendIntent.putExtra(Intent.EXTRA_TEXT,"Hello TrackLogs")
            sendIntent.putExtra("jid", "14023964071" +"@s.whatsapp.net")
            sendIntent.setPackage("com.whatsapp")
            startActivity(sendIntent)
        }
        catch(e : Exception ) {
            Toast.makeText(this@TrackActivity, "Whatsapp Not Found.", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        apiController.unSubscribe()
        LocalBroadcastManager.getInstance(this@TrackActivity).unregisterReceiver(onlineStatusListener)
    }

    override fun onSuccess(tag: String, response: String) {
        if (tag == AppConstant.GET_REPORT) {
            userReports = apiController.parseJson(response, GetReports::class.java)

            if (userReports!!.weekly_day_reports.size != 0) {

                /*vTrackTop.visibility = View.GONE
                vTrackBottom.visibility = View.VISIBLE

                tvCurrentStatus.text = userReports!!.new_status*/
                rvTime.adapter = ReportAdapter(this@TrackActivity, userReports!!, "Weekly")

                //todaySessionCount.text = "Today Session : "+userReports!!.result.size
                var difference :Long = 0
                for(batch in userReports!!.result){

                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                    val online = simpleDateFormat.parse(batch.online_created)
                    val offline = simpleDateFormat.parse(batch.offline_created)
                    difference += (offline.time - online.time)
                }
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60
                val daysInMilli = hoursInMilli * 24

                /*val elapsedDays = difference / daysInMilli
                difference = difference % daysInMilli*/
                val elapsedHours = difference / hoursInMilli
                difference = difference % hoursInMilli
                val elapsedMinutes = difference / minutesInMilli
                difference = difference % minutesInMilli
                val elapsedSeconds = difference / secondsInMilli

                //timePeriod.text = "Online Period Today "+String.format("%02d", elapsedHours) + ":" + String.format("%02d", elapsedMinutes)+" HRS"
            }
        }

        if (tag == AppConstant.GET_SUSCRIBER) {
            val getSubscriber = apiController.parseJson(response, GetSubscriber::class.java)

            if (getSubscriber.result != null) {
                if (getSubscriber.status == 1) {
                    if (PrefManager.getBoolean("subscribed")) {
                        getReport()
                        tvContactName.text = getSubscriber.result[0].name
                        tvContactNumber.text = getSubscriber.result[0].mobile_number
                    }
                }
            }
        }

        //Use case for Unsuscribe User
        if (tag == AppConstant.UNSUSCRIBE_USER) {
            val unSubUser = apiController.parseJson(response, BaseApiResponse::class.java)
            if (unSubUser.status == 1) {
                Toast.makeText(this@TrackActivity, "Tracking stopped Successfully", Toast.LENGTH_SHORT).show()
                LocalBroadcastManager.getInstance(this@TrackActivity).sendBroadcast(Intent("REMOVE_DATA"))
                PrefManager.putBoolean("subscribed", false)
                startActivity(Intent(this@TrackActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onFailure(tag: String, msg: String) {
        Toast.makeText(this@TrackActivity, "Oops! Something is wrong! Please contact the support team.", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onError(tag: String, msg: String) {
        Toast.makeText(this@TrackActivity, "Oops! Something is wrong! Please contact the support team.", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onNoConnection(tag: String, msg: String) {
        Toast.makeText(this@TrackActivity, "No Internet Connnection", Toast.LENGTH_SHORT).show()
    }

}