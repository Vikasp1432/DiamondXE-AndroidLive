package com.diamondxe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Utils;

import org.json.JSONObject;

public class CertificateWebViewActivity extends SuperActivity implements RecyclerInterface {

    private Activity activity;
    private Context context;
    private ImageView back_img;
    private WebView webView;
    private TextView not_found_tv;

    String certificateFileUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate_web_view);

        context = activity = this;

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        webView = findViewById(R.id.webView);
        not_found_tv = findViewById(R.id.not_found_tv);

        certificateFileUrl = CommonUtility.getGlobalString(context, "certificate_file_url");

        Log.e("-----certificateFileUrl-------- : ", certificateFileUrl.toString());

        // Check URL Available or Not
        if(certificateFileUrl.equalsIgnoreCase(""))
        {

            not_found_tv.setText(getResources().getString(R.string.certificate_not_found));
            not_found_tv.setVisibility(View.VISIBLE);
            webView.setVisibility(View.INVISIBLE);
        }
        else
        {
            not_found_tv.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);

            webView.setWebViewClient(new WebViewClient() {
              /*  @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }*/

                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    // Handle URL loading for API level 24 and above
                    String url = request.getUrl().toString();
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon)
                {
                    startActivity(new Intent(context, TransparentActivity.class));
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    TransparentActivity.terminateTransparentActivity();
                    // Apply CSS to ensure the image scales within WebView boundaries
                    webView.loadUrl("javascript:(function() { " +
                            "var img = document.querySelector('img'); " +
                            "if(img) { " +
                            "img.style.maxWidth = '100%'; " +
                            "img.style.height = 'auto'; " +
                            "img.style.width = 'auto'; " +
                            "document.body.style.overflow = 'hidden'; " +
                            "} " +
                            "})()");
                }

            });

            //webView.setInitialScale(120);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(false); // Disable default zoom controls
            // Set WebView background color to transparent
            webView.setBackgroundColor(Color.TRANSPARENT);
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
            String pdf = certificateFileUrl;
            // webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);;

            String extension =  CommonUtility.getFileExtension(certificateFileUrl);
            webView.getSettings().setSupportZoom(true);
            //Log.e("------extension------ : ", extension.toString());
            if(extension.equalsIgnoreCase(".pdf"))
            {
                webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + certificateFileUrl);
            }
            else
            {
                webView.loadUrl(certificateFileUrl);
            }

            /*webView.setWebViewClient(new WebViewClient() {
                boolean checkOnPageStartedCalled = false;
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    checkOnPageStartedCalled = true;
                    startActivity(new Intent(context, TransparentActivity.class));
                }

                @Override
                public void onPageFinished(WebView view, String url)
                {
                    TransparentActivity.terminateTransparentActivity();
                *//*if (checkOnPageStartedCalled) {

                    hideProgress();
                } else {
                    showPdfFile(imageString);
                }*//*
                }
            });*/
        }
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