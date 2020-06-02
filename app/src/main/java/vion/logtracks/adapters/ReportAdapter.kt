package vion.logtracks.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rv_item.view.*
import vion.logtracks.R
import vion.logtracks.models.GetReports
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ReportAdapter(private val context: FragmentActivity?,private val getReports: GetReports, timeFrame: String) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    var timeFrame = timeFrame
    lateinit var resultBean: List<GetReports.ResultBean>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)

        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        if( position%2 == 0 ){
            holder.card.setCardBackgroundColor(Color.parseColor("#CFFFFC"))
        }else{
            holder.card.setCardBackgroundColor(Color.parseColor("#DEF9F8"))
        }

        resultBean = getReports.result

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

        val online = simpleDateFormat.parse(resultBean[position].online_created)
        val offline = simpleDateFormat.parse(resultBean[position].offline_created)


        holder.tvOnlineTime.text = getTime(resultBean[position].online_created)
        holder.tvOnlineDate.text = getDate(resultBean[position].online_created)

        holder.tvOfflineTime.text = getTime(resultBean[position].offline_created)
        holder.tvOfflineDate.text = getDate(resultBean[position].offline_created)

        holder.tvDuration.text = printDifference(online,offline)

    }

    override fun getItemCount(): Int {

        return getReports.result.size
    }

    fun printDifference(startDate: Date, endDate: Date): String {
        var different = endDate.time - startDate.time
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays = different / daysInMilli
        different = different % daysInMilli
        val elapsedHours = different / hoursInMilli
        different = different % hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different = different % minutesInMilli
        val elapsedSeconds = different / secondsInMilli

        return String.format("%02d", elapsedHours) + ":" + String.format(
            "%02d",
            elapsedMinutes
        ) + ":" + String.format("%02d", elapsedSeconds) + " Online Duration"
    }

    private fun getTime(inputDateStr: String): String {
        var d = ""
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val outputFormat = SimpleDateFormat("hh:mm")
            val date = inputFormat.parse(inputDateStr)
            d = outputFormat.format(date!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return d
    }


    private fun getDate(inputDateStr: String): String {
        var d = ""
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val outputFormat = SimpleDateFormat("dd MMMM yyyy")
            val date = inputFormat.parse(inputDateStr)
            d = outputFormat.format(date!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return d
    }

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvOnlineTime: TextView = itemView.tvOnlineTime
        val tvOnlineDate: TextView = itemView.tvOnlineDate
        val tvOfflineTime: TextView = itemView.tvOfflineTime
        val tvOfflineDate: TextView = itemView.tvOfflineDate
        val card : CardView =  itemView.cardView

        //val tvOfflineDate: TextView = itemView.tvOfflineDate
        val tvDuration: TextView = itemView.tvDuration

    }

}
