package com.dxe.calc

import android.os.Bundle
import android.text.TextUtils
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.diamondxe.databinding.ActivityWebViewBinding


class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rlBack.setOnClickListener { finish() }
        // Javascript inabled on webview
        binding.webview.settings.setJavaScriptEnabled(true)

        // Other webview options
        binding.webview.settings.loadWithOverviewMode = true
        binding.webview.settings.useWideViewPort = true
        binding.webview.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        binding.webview.isScrollbarFadingEnabled = false
        binding.webview.settings.builtInZoomControls = true

        binding.webview.webViewClient = object : WebViewClient() {
           // var progressDialog: ProgressDialog? = null

            //If you will not use this method url links are opeen in new brower not in webview
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            //Show loader on url load
            override fun onLoadResource(view: WebView, url: String) {
                /*if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = ProgressDialog(this@WebViewActivity)
                    progressDialog!!.setMessage("Loading...")
                    progressDialog!!.show()
                }*/
            }

            override fun onPageFinished(view: WebView, url: String) {
                /*try {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                        progressDialog = null
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }*/
            }
        }

        intent?.let {
            val title = intent.getStringExtra("title")
            val url = intent.getStringExtra("url")
            if (TextUtils.isEmpty(url)) {
                finish()
            }
            binding.lblTitle.text = title
            binding.webview.loadUrl(url!!)
        }

    }


}