package com.diamondxe.Activity;

import static com.diamondxe.ApiCalling.ApiConstants.CART_FRAGMENT;
import static com.diamondxe.ApiCalling.ApiConstants.WISHLIST_FRAGMENT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;

import org.json.JSONObject;

public class SupplierSignupWebViewActivity extends SuperActivity implements RecyclerInterface {

    private Activity activity;
    private Context context;
    private ImageView back_img;

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_signup_web_view);

        context = activity = this;

        webView = findViewById(R.id.webView);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // Return true to indicate that you want to handle the URL loading yourself
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Optional: Add any additional actions to perform when the page finishes loading
            }
        });


        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false); // Disable default zoom controls
        // Set WebView background color to transparent
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        webSettings.setSupportZoom(false);

        webView.loadUrl("https://supplier-uat.diamondxe.com/");
    }

    @Override
    public void onClick(View view)
    {
        Intent intent;
        int id = view.getId();
        if(id == R.id.back_img)
        {
            Utils.hideKeyboard(activity);
            finish();
        }
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

    }

    @Override
    public void getErrorResponce(String error, int service_ID) {

    }

    @Override
    public void itemClick(int position, String action) {

    }
}