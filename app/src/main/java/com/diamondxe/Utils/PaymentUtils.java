package com.diamondxe.Utils;

import android.content.Context;

import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment;

// Java
public class PaymentUtils {

    // For SandBox Phonepe Credentials
    public static final String SALT = "1c560f14-86f2-4317-86bf-658f92554b58";
    public static final String SALT_INDEX = "1";
    public static final String MERCHANT_ID = "DIAMONDUAT";
    public static final String BASE_URL = "https://api-preprod.phonepe.com/apis/pg-sandbox";
    public static final String API_END_POINT = "/pg/v1/pay";
    public static final String TARGET_URL = "com.phonepe.simulator";
    public static final String CALL_BACK_URL = "https://admin-uat.diamondxe.com/payments/phonepe-custom-payment-callback";

    // For Live Phonepe Credentials
    /*public static final String SALT = "b74e2a4c-7d13-43c5-a115-c0372ed85dbd"; // salt key
    public static final String SALT_INDEX = "1";
    public static final String MERCHANT_ID = "DIAMONDXEONLINE"; // Merchant id
    public static final String BASE_URL = "https://api.phonepe.com/apis/hermes";
    public static final String API_END_POINT = "/pg/v1/pay";
    public static final String TARGET_URL = "com.phonepe.app";
    public static final String CALL_BACK_URL = "https://admin-uat.diamondxe.com/payments/phonepe-custom-payment-callback";*/

    // Method to initialize PhonePe
    public static void initializePhonePe(Context context) {
        PhonePe.init(context.getApplicationContext(), PhonePeEnvironment.SANDBOX, MERCHANT_ID, SALT); // For SandBox(Testing)
        //PhonePe.init(context.getApplicationContext(), PhonePeEnvironment.RELEASE, MERCHANT_ID, SALT); // For Release(Live)
    }
}
