package com.diamondxe.Activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.databinding.ActivityNavWebViewBinding;
import com.diamondxe.databinding.ActivityWebViewNewBinding;

import org.json.JSONObject;

public class NavWebViewActivity extends SuperActivity {

    ActivityNavWebViewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_nav_web_view);
        binding = ActivityNavWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.rlBack.setOnClickListener(view -> finish());
        binding.lblTitle.setText(getString(R.string.back));

        String url = getIntent().getStringExtra("url");
        if (url != null) {
            loadWebView(url);
        } else {
            // Handle case where URL is not provided
        }
        /*WebSettings webSettings = binding.webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        binding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                binding.webview.evaluateJavascript(
                        "window.onerror = function(message, source, lineno, colno, error) {" +
                                "   console.log('Error: ' + message);" +
                                "   return true;" +
                                "};", null);
            }
        });
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        WebView.setWebContentsDebuggingEnabled(true);
        binding.webview.loadUrl("https://diamondxe.com/limitedoffer/");*/
    }

    private void loadWebView(String url) {
        WebSettings webSettings = binding.webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        binding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                binding.webview.evaluateJavascript(
                        "window.onerror = function(message, source, lineno, colno, error) {" +
                                "   console.log('Error: ' + message);" +
                                "   return true;" +
                                "};", null);
            }
        });

        WebView.setWebContentsDebuggingEnabled(true);
        binding.webview.loadUrl(url); // Load the URL received from the Intent
    }
    @Override
    public void onClick(View view) {

    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

    }

    @Override
    public void getErrorResponce(String error, int service_ID) {

    }
}

/*
Intent intent = new Intent(CurrentActivity.this, NavWebViewActivity.class);
intent.putExtra("url", "https://diamondxe.com/limitedoffer/"); // Replace with the desired URL
startActivity(intent);
*/
