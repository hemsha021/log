package vion.logtracks.utils;

interface RefreshData {

    abstract fun onSubscribed(pos: Int)
    abstract fun onUnsubscribed(pos: Int)
}