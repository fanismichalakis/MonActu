package fr.fanismichalakis.monactu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val articleUrl = intent.getStringExtra("articleUrl")

        val webView = findViewById<WebView>(R.id.webview)
        webView.webViewClient = object : WebViewClient() {}
        showProgressBar()
        webView.loadUrl(articleUrl.toString())
        hideProgressBar()
    }

    private fun hideProgressBar() {
        val progressBar: ProgressBar = findViewById<ProgressBar>(R.id.progressBar2)
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        val progressBar: ProgressBar = findViewById<ProgressBar>(R.id.progressBar2)
        progressBar.visibility = View.VISIBLE
    }
}