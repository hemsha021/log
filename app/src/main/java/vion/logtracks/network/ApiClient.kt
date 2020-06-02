package vion.logtracks.network

import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {
        private var retrofit: Retrofit? = null

        private var okHttpClient: OkHttpClient? = null

        fun getClient(context: Context): ApiInterface {
            if (okHttpClient == null) {
                initOkHttp(context)
            }
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(AppConstant.BASEURL)
                        .client(okHttpClient!!)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
            }
            return retrofit!!.create(ApiInterface::class.java)

        }

        private fun initOkHttp(context: Context) {
            val httpClient = OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(interceptor)
            httpClient.addInterceptor(ConnectivityInterceptor(context))
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("password","123456")
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            okHttpClient = httpClient.build()
        }
    }
}
