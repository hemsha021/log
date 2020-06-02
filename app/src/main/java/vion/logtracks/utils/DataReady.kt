package vion.logtracks.utils;

import com.android.billingclient.api.SkuDetails

interface DataReady {
    fun readySkuDetailsList(skuDetailsList: MutableList<SkuDetails>?)
    fun planPurchased(boolean: Boolean)
}