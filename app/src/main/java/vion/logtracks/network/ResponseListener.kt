package vion.logtracks.network;


interface ResponseListener {

    fun onSuccess(tag: String, response: String)

    fun onFailure(tag: String, msg: String)

    fun onError(tag: String, msg: String)

    fun onNoConnection(tag: String, msg: String)
}
