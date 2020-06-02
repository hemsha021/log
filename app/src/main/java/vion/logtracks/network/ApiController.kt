package vion.logtracks.network;


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import com.google.gson.Gson
import com.google.gson.JsonElement
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import vion.logtracks.models.BaseApiResponse


class ApiController(val context: Activity, private val apiResponseListener: ResponseListener) {
    //private val loader: Dialog = ProgressView.getLoader(context)
    private val compositeDisposable = CompositeDisposable()

    @SuppressLint("CheckResult")
    fun makeCall(tag: String, params: MutableMap<String, String>?) {
        //loader.show()
        val call = ApiClient.getClient(context).makeCall(AppConstant.BASEURL + tag, params)
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(object : Observer<JsonElement?> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onNext(response: JsonElement) {
                        //loader.dismiss()
                        val baseApiResponse = Gson().fromJson<BaseApiResponse>(response, BaseApiResponse::class.java)
                        if (baseApiResponse.status == AppConstant.APISUCCESS) {
                            try {
                                apiResponseListener.onSuccess(tag, response.toString())
                            } catch (e: JSONException) {
                                apiResponseListener.onFailure(tag, baseApiResponse.message)
                            }
                        } else {
                            apiResponseListener.onFailure(tag, baseApiResponse.message)
                        }
                    }

                    override fun onError(e: Throwable) {
                        //loader.dismiss()
                        if (e is NoConnectivityException) {
                            apiResponseListener.onNoConnection(tag, "No Internet Connection")
                        } else {
                            apiResponseListener.onError(tag, e.message.toString())
                        }
                    }
                })

    }

    @SuppressLint("CheckResult")
    fun makeCallSilent(tag: String, params: MutableMap<String, String>) {

        val call = ApiClient.getClient(context).makeCall(AppConstant.BASEURL + tag, params)
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(object : Observer<JsonElement?> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onNext(response: JsonElement) {
                        val baseApiResponse =
                                Gson().fromJson<BaseApiResponse>(response, BaseApiResponse::class.java)
                        if (baseApiResponse.status == AppConstant.APISUCCESS) {
                            apiResponseListener.onSuccess(tag, response.toString())
                        } else {
                            apiResponseListener.onFailure(tag, baseApiResponse.message)
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is NoConnectivityException) {
                            apiResponseListener.onNoConnection(tag, "No Internet Connection")
                        } else {
                            apiResponseListener.onError(tag, e.message.toString())
                        }
                    }
                })

    }

    fun unSubscribe() {
        compositeDisposable.dispose()
    }

    fun <T> parseJson(jsonString: String, modelClass: Class<T>): T {
        val gson = Gson()
        return gson.fromJson(jsonString, modelClass)
    }
}
