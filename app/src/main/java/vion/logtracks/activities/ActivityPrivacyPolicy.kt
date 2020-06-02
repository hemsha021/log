package vion.logtracks.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import vion.logtracks.R
import vion.logtracks.network.AppConstant

class ActivityPrivacyPolicy : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        btnAgree.setOnClickListener {
            startActivity(Intent(this@ActivityPrivacyPolicy, MainActivity::class.java))
            finish()
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }

        webView.loadUrl(AppConstant.PRIVACY_POLICY)
        val webSettings = webView.getSettings()
        webSettings.setJavaScriptEnabled(true)
        webView.setVerticalScrollBarEnabled(true)
        webView.setHorizontalScrollBarEnabled(true)
        webView.loadUrl(AppConstant.PRIVACY_POLICY)
    }
}