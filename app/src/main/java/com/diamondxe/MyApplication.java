package com.diamondxe;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment;


public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        //PhonePe.init(getApplicationContext(), "1c560f14-86f2-4317-86bf-658f92554b58");
      //  PhonePe.init(getApplicationContext(), PhonePeEnvironment.SANDBOX, "DIAMONDUAT", "1c560f14-86f2-4317-86bf-658f92554b58");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);

    }
}
