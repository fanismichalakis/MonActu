package fr.fanismichalakis.monactu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val articleUrl = intent.getStringExtra("articleUrl")

        val webView = findViewById<WebView>(R.id.webview)
        webView.webViewClient = object : WebViewClient() {}
        webView.loadUrl(articleUrl.toString())
    }
}