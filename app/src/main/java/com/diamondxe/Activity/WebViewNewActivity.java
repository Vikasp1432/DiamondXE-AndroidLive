package com.diamondxe.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diamondxe.R;
import com.diamondxe.databinding.ActivityWebViewNewBinding;

public class WebViewNewActivity extends AppCompatActivity {
    private ActivityWebViewNewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_web_view_new);
        binding = ActivityWebViewNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rlBack.setOnClickListener(view -> finish());

        // WebView settings
        WebSettings webSettings = binding.webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        binding.webview.getSettings().setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        binding.webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        binding.webview.setScrollbarFadingEnabled(false);
        webSettings.setBuiltInZoomControls(true);
        if (getIntent() != null) {
            String title = getIntent().getStringExtra("title");
            String url = getIntent().getStringExtra("url");

            Log.d("WebViewActivity", "Loading URL: " + url); // Debugging URL

            if (TextUtils.isEmpty(url)) {
                finish();
            } else {
                binding.lblTitle.setText(title);
                binding.webview.loadUrl("https://diamondxe.com/limitedoffer");
            }
        }
        // Allow mixed content
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        binding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("onPageFinished","...62..."+url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("error","...62..."+error.getErrorCode());
            }
        });


        // Handle intent data

    }
    }
