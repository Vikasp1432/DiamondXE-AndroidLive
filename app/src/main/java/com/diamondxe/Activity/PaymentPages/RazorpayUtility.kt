package com.diamondxe.Activity.PaymentPages

import android.app.Activity
import android.util.Log
import com.diamondxe.BuildConfig
import com.diamondxe.R
import com.razorpay.Checkout
import org.json.JSONException
import org.json.JSONObject

object RazorpayUtility {

    fun initializePayment(
        activity: Activity,
        amount: Int,
        contact: String,
        email: String,
        paymentMethod: PaymentMethod,
        appName: String,
        orderId: String,
    ) {
        Log.e("orderId", "..22.payment start...$orderId")
        val checkout = Checkout()
        checkout.setKeyID(BuildConfig.RAZORPAY_KEY_ID)
        checkout.setImage(R.drawable.appicon)

        val paymentObject = JSONObject()
        try {
            paymentObject.put("name", appName)
            paymentObject.put("description", "Test payment")
            paymentObject.put("currency", "INR")
            paymentObject.put("amount", amount * 100)
            paymentObject.put("prefill.contact", contact)
            paymentObject.put("prefill.email", email)
            paymentObject.put("order_id", orderId)

            val paymentMethods = JSONObject().apply {
                put("netbanking", paymentMethod == PaymentMethod.NET_BANKING)
                put("upi", paymentMethod == PaymentMethod.UPI)
                put("card", paymentMethod == PaymentMethod.CARD)
                put("wallet", false)
                put("paylater", false)
            }
            paymentObject.put("method", paymentMethods)

            checkout.open(activity, paymentObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}