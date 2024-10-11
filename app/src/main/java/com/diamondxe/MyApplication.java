package com.diamondxe;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.diamondxe.Activity.TransparentActivity;
import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment;


public class MyApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks{

    private static boolean isTransparentActivityRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (activity instanceof TransparentActivity) {
            isTransparentActivityRunning = true;
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activity instanceof TransparentActivity) {
            isTransparentActivityRunning = false;
        }
    }

    // Implement other lifecycle methods as needed
    @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
    @Override public void onActivityResumed(Activity activity) {}
    @Override public void onActivityPaused(Activity activity) {}
    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
    @Override public void onActivityDestroyed(Activity activity) {}
    public static boolean isTransparentActivityRunning() {
        return isTransparentActivityRunning;
    }
}
