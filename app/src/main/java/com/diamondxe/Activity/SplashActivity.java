package com.diamondxe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.util.TimeZone;
import android.icu.util.ULocale;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;

import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.TimeZoneCountryCodeMapper;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.UUID;

public class SplashActivity extends Activity {

    private Context context;
    private static int SPLASH_TIME_OUT = 3000;
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);
        //For Android Nougat and above
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_splash);

        context=this;

        mVideoView = findViewById(R.id.mVideoView);

        context=this;

        playVideo();

        hideKeyboard(SplashActivity.this);

        // Get the default TimeZone ID
        String timeZoneId = TimeZone.getDefault().getID();
        String countryCode = TimeZoneCountryCodeMapper.getCountryCodeFromTimeZone(timeZoneId);

        // Use the country code as needed
        // System.out.println("Country_Code_Using_TimeZone: " + timeZoneId);
        // System.out.println("Country_Code_Using_TimeZone1: " + countryCode);

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                Constant.callHomeScreenOnResume = "no";
                // Code to execute when the video finishes playing
                Intent intent = new Intent(SplashActivity.this,HomeScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();

            }
        }, SPLASH_TIME_OUT);
    }


    public void playVideo(){
        MediaController m = new MediaController(this);
        //splash_video.setMediaController(m);
        String path = "android.resource://com.diamondxe/"+R.raw.splash;
        Uri u = Uri.parse(path);
        mVideoView.setVideoURI(u);
        // Set the completion listener

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);

                // Set the video aspect ratio
                float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                float screenRatio = mVideoView.getWidth() / (float) mVideoView.getHeight();
                float scaleX = videoRatio / screenRatio;
                if (scaleX >= 1f) {
                    mVideoView.setScaleX(scaleX);
                } else {
                    mVideoView.setScaleY(1f / scaleX);
                }

                mVideoView.start();
            }
        });

        /*mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                String mobile_auth_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xMjcuMC4wLjE6ODAwMVwvYXBwXC92MVwvbG9naW4iLCJpYXQiOjE3MTgwMjM0MDcsImV4cCI6MTcxODAyNzAwNywibmJmIjoxNzE4MDIzNDA3LCJqdGkiOiJVMVpHdlh2VThVRzZWWkJrIiwic3ViIjozNCwicHJ2IjoiMjNiZDVjODk0OWY2MDBhZGIzOWU3MDFjNDAwODcyZGI3YTU5NzZmNyJ9.4V1XVYdmllRy9-yGLazipCrRCLKCfxoVXoSkgAAh3DQ";
                CommonUtility.setGlobalString(context, "mobile_auth_token", mobile_auth_token);
                // Code to execute when the video finishes playing
                Intent intent = new Intent(SplashActivity.this,HomeScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });*/


        mVideoView.start();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View f = activity.getCurrentFocus();
        if (null != f && null != f.getWindowToken() && EditText.class.isAssignableFrom(f.getClass()))
            imm.hideSoftInputFromWindow(f.getWindowToken(), 0);
        else
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
/*extends Activity {

    private Context context;
    private static int SPLASH_TIME_OUT = 3000;
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);
        //For Android Nougat and above
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        context=this;

        mVideoView = findViewById(R.id.mVideoView);

        context=this;

        playVideo();

        hideKeyboard(SplashActivity.this);

        // Get the default TimeZone ID
        String timeZoneId = TimeZone.getDefault().getID();
        String countryCode = TimeZoneCountryCodeMapper.getCountryCodeFromTimeZone(timeZoneId);

        // Use the country code as needed
        // System.out.println("Country_Code_Using_TimeZone: " + timeZoneId);
        // System.out.println("Country_Code_Using_TimeZone1: " + countryCode);

        new Handler().postDelayed(new Runnable() {
            *//*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             *//*
            @Override
            public void run() {
                Constant.callHomeScreenOnResume = "no";
                // Code to execute when the video finishes playing
                Intent intent = new Intent(SplashActivity.this,HomeScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();

            }
        }, SPLASH_TIME_OUT);
    }


    public void playVideo(){
        MediaController m = new MediaController(this);
        //splash_video.setMediaController(m);
        String path = "android.resource://com.diamondxe/"+R.raw.splash;
        Uri u = Uri.parse(path);
        mVideoView.setVideoURI(u);
        // Set the completion listener

        Log.e("Play video ","Call....105...");
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);

                // Set the video aspect ratio
                float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                float screenRatio = mVideoView.getWidth() / (float) mVideoView.getHeight();
                float scaleX = videoRatio / screenRatio;
                if (scaleX >= 1f) {
                    mVideoView.setScaleX(scaleX);
                } else {
                    mVideoView.setScaleY(1f / scaleX);
                }

                mVideoView.start();
            }
        });

        mVideoView.start();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View f = activity.getCurrentFocus();
        if (null != f && null != f.getWindowToken() && EditText.class.isAssignableFrom(f.getClass()))
            imm.hideSoftInputFromWindow(f.getWindowToken(), 0);
        else
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}*/




        /*mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                String mobile_auth_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xMjcuMC4wLjE6ODAwMVwvYXBwXC92MVwvbG9naW4iLCJpYXQiOjE3MTgwMjM0MDcsImV4cCI6MTcxODAyNzAwNywibmJmIjoxNzE4MDIzNDA3LCJqdGkiOiJVMVpHdlh2VThVRzZWWkJrIiwic3ViIjozNCwicHJ2IjoiMjNiZDVjODk0OWY2MDBhZGIzOWU3MDFjNDAwODcyZGI3YTU5NzZmNyJ9.4V1XVYdmllRy9-yGLazipCrRCLKCfxoVXoSkgAAh3DQ";
                CommonUtility.setGlobalString(context, "mobile_auth_token", mobile_auth_token);
                // Code to execute when the video finishes playing
                Intent intent = new Intent(SplashActivity.this,HomeScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });*/

