package com.diamondxe.Activity.PaymentPages

import android.app.Activity
import android.content.Context
import com.diamondxe.R
import com.razorpay.Checkout
import org.json.JSONException
import org.json.JSONObject

object RazorpayUtility {

    private const val RAZORPAY_KEY_ID = "rzp_test_rfy0AcVmf33RWF"

    fun initializePayment(
        activity: Activity,
        amount: Int,
        contact: String,
        email: String,
        paymentMethod: PaymentMethod,
        appName: String,
        appIcon: Int
    ) {
        val checkout = Checkout()
        checkout.setKeyID(RAZORPAY_KEY_ID)
        checkout.setImage(R.drawable.appicon)

        val paymentObject = JSONObject()
        try {
            paymentObject.put("name", appName)
            paymentObject.put("description", "Test payment")
            paymentObject.put("currency", "INR")
            paymentObject.put("amount", amount * 100)
            paymentObject.put("prefill.contact", contact)
            paymentObject.put("prefill.email", email)

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