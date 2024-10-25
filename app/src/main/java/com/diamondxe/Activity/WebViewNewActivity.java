package com.diamondxe.Activity;

import static com.diamondxe.Activity.HomeScreenActivity.context;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.databinding.ActivityWebViewNewBinding;

import org.json.JSONObject;

public class WebViewNewActivity extends SuperActivity {
    private ActivityWebViewNewBinding binding;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_new);
        binding = ActivityWebViewNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rlBack.setOnClickListener(view -> finish());
        binding.lblTitle.setText(getString(R.string.back));

        WebSettings webSettings = binding.webview.getSettings();
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
        binding.webview.loadUrl("https://diamondxe.com/limitedoffer/");


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
