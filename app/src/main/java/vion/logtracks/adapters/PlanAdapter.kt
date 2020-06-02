package vion.logtracks.adapters

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.SkuDetails
import kotlinx.android.synthetic.main.activity_plan_card_view.view.*
import vion.logtracks.R
import vion.logtracks.utils.PlanClick

class PlanAdapter(private val activity: Activity,  private val getPlans: MutableList<SkuDetails>, val planRvClick: PlanClick)
 : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {

        var title = getPlans[position].title.substring(0,getPlans[position].title.toString().indexOf("(")).trim()

        holder.tvPlan.text = title
        holder.tvPrice.text = getPlans[position].price
        holder.tvPlanType.text = getPlans[position].description
        var currency = getPlans[position].price.toString().substring(0,1)

        when (title) {
            "Weekly Plan" -> {
                var price = getPlans[position].priceAmountMicros.toFloat()/(7*1000000)
                holder.tvDailyPrice.text = currency+ "%.2f".format(price)+"/day  ( 7 Days)"
                holder.planCard!!.setCardBackgroundColor(Color.parseColor("#377975"))
            }
            "Monthly Plan" -> {
                holder.tvDailyPrice.text = "$ 0.33/day ( 90 Days )"
                var price  = getPlans[position].priceAmountMicros.toFloat()/(30*1000000)
                holder.tvDailyPrice.text = currency + "%.2f".format(price)+"/day ( 30 Days)"
            }
            "Three Month" -> {
                var price =  getPlans[position].priceAmountMicros.toFloat()/(90*1000000)
                holder.tvDailyPrice.text = currency+"%.2f".format(price)+"/day ( 90 Days)"
                holder.planCard!!.setCardBackgroundColor(Color.parseColor("#368D88"))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanAdapter.PlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_plan_card_view, parent, false)

        return PlanViewHolder(view)
    }

    override fun getItemCount(): Int {
        return getPlans.size
    }

    inner class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvPlan: TextView = itemView.tvPlan
        val tvPrice: TextView = itemView.tvPrice
        val tvPlanType: TextView = itemView.tvPlanType
        val tvDailyPrice: TextView = itemView.tvDailyPrice
        val planCard: CardView? = itemView.cardView

    }
}
