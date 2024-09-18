package com.diamondxe.Interface;

import android.content.Intent;

public interface PaymentResultCallback {
    void onPaymentResult(int requestCode, int resultCode, Intent data);
}