package com.diamondxe.Activity.PaymentPages

import android.app.Activity
import android.content.Context
import android.util.Log
import com.payment.paymentsdk.PaymentSdkActivity
import com.payment.paymentsdk.PaymentSdkConfigBuilder
import com.payment.paymentsdk.integrationmodels.PaymentSdkApms
import com.payment.paymentsdk.integrationmodels.PaymentSdkBillingDetails
import com.payment.paymentsdk.integrationmodels.PaymentSdkConfigurationDetails
import com.payment.paymentsdk.integrationmodels.PaymentSdkLanguageCode
import com.payment.paymentsdk.integrationmodels.PaymentSdkTokenise
import com.payment.paymentsdk.integrationmodels.PaymentSdkTransactionClass
import com.payment.paymentsdk.integrationmodels.PaymentSdkTransactionType
import com.payment.paymentsdk.sharedclasses.interfaces.CallbackPaymentInterface


object PayTabsPaymentManager {

    private const val PROFILE_ID = "146752"
    private const val SERVER_KEY = "SKJ9THKDDW-JJK99WWMTZ-LK6GWN6LWT"
    private const val CLIENT_KEY = "CDK2KR-RMT666-KPPVVN-TDHHDH"
    private const val MERCHANT_COUNTRY_CODE = "AE"
    private const val TRANSACTION_TITLE = "DiamondXE"
    private  var CURRENCY = ""  //AED
    private  var CART_ID = ""
    private const val CART_DESCRIPTION = "Cart description"
    private val LANGUAGE_CODE = PaymentSdkLanguageCode.EN
    private var billingDetails: PaymentSdkBillingDetails? = null


    fun setCurrencyAndCartId(newCurrency: String, newCartId: String) {
        Log.e("Before Update - Currency", CURRENCY)
        Log.e("Before Update - Cart ID", CART_ID)

        CURRENCY = newCurrency
        CART_ID = newCartId

        Log.e("After Update - Currency", CURRENCY)
        Log.e("After Update - Cart ID", CART_ID)
    }


    fun setBillingDetails(
        city: String,
        countryCode: String,
        email: String,
        name: String,
        phone: String,
        state: String,
        addressLine: String,
        zip: String
    ) {
        billingDetails = PaymentSdkBillingDetails(
            city = city,
            countryCode = countryCode,
            email = email,
            name = name,
            phone = phone,
            state = state,
            addressLine = addressLine,
            zip = zip
        )
    }


    fun initiateCardPayment(
        context: Activity,
        amount: Double,
        callback: CallbackPaymentInterface
    ) {
        val configData = generatePaymentConfiguration(amount)
        PaymentSdkActivity.startCardPayment(context, configData, callback)
    }

    private fun generatePaymentConfiguration(
        amount: Double,
        selectedApm: PaymentSdkApms? = null
    ): PaymentSdkConfigurationDetails {
        val configBuilder = PaymentSdkConfigBuilder(
            profileId = PROFILE_ID,
            serverKey = SERVER_KEY,
            clientKey = CLIENT_KEY,
            amount = amount,
            currencyCode = CURRENCY.trim()
        ).apply {

            Log.e("After Update - Currency..***********", CURRENCY)
            Log.e("After Update - Cart ID..************", CART_ID)

            setCartDescription(CART_DESCRIPTION)
            setLanguageCode(LANGUAGE_CODE)
            setMerchantCountryCode(MERCHANT_COUNTRY_CODE)
            setTransactionType(PaymentSdkTransactionType.SALE)
            setTokenise(PaymentSdkTokenise.MERCHANT_MANDATORY)
            setTransactionClass(PaymentSdkTransactionClass.ECOM)
            setBillingData(billingDetails)
            setCartId(CART_ID.trim())
            showBillingInfo(true)
            showShippingInfo(false)
            forceShippingInfo(false)
            hideCardScanner(false)
            linkBillingNameWithCard(false)
            setScreenTitle(TRANSACTION_TITLE)
            selectedApm?.let { setAlternativePaymentMethods(listOf(it)) }
        }
        return configBuilder.build()
    }
}

/*object PayTabsPaymentManager {

    private const val PROFILE_ID = "146752"
    private const val SERVER_KEY = "SKJ9THKDDW-JJK99WWMTZ-LK6GWN6LWT"
    private const val CLIENT_KEY = "CDK2KR-RMT666-KPPVVN-TDHHDH"
    private const val MERCHANT_COUNTRY_CODE = "AE"
    private const val CURRENCY = "AED"
    private const val TRANSACTION_TITLE = "SDK sample"
    private const val CART_ID = "123456"
    private const val CART_DESCRIPTION = "Cart description"
    private val LANGUAGE_CODE = PaymentSdkLanguageCode.EN

    private const val BILLING_CITY = "Dubai"
    private const val BILLING_COUNTRY_CODE = "AE"
    private const val BILLING_EMAIL = "testuser@example.com"
    private const val BILLING_NAME = "Ali Ahmed"
    private const val BILLING_PHONE = "+971501234567"
    private const val BILLING_STATE = "Dubai"
    private const val BILLING_ADDRESS = "1234 Test Street"
    private const val BILLING_ZIP = "00000"


    fun initiateCardPayment(
        context: Activity,
        amount: Double,
        callback: CallbackPaymentInterface
    ) {
        val configData = generatePaymentConfiguration(amount)
        PaymentSdkActivity.startCardPayment(context, configData, callback)
    }

    private fun generatePaymentConfiguration(
        amount: Double,
        selectedApm: PaymentSdkApms? = null
    ): PaymentSdkConfigurationDetails {
        val configBuilder = PaymentSdkConfigBuilder(
            profileId = PROFILE_ID,
            serverKey = SERVER_KEY,
            clientKey = CLIENT_KEY,
            amount = amount,
            currencyCode = CURRENCY
        ).apply {
            setCartDescription(CART_DESCRIPTION)
            setLanguageCode(LANGUAGE_CODE)
            setMerchantCountryCode(MERCHANT_COUNTRY_CODE)
            setTransactionType(PaymentSdkTransactionType.SALE)
            setTokenise(PaymentSdkTokenise.MERCHANT_MANDATORY)
            setTransactionClass(PaymentSdkTransactionClass.ECOM)
            setBillingData(getBillingDetails())
            setCartId(CART_ID)
            showBillingInfo(true)
            showShippingInfo(false)
            forceShippingInfo(false)
            hideCardScanner(false)
            linkBillingNameWithCard(false)
            setScreenTitle(TRANSACTION_TITLE)
            selectedApm?.let { setAlternativePaymentMethods(listOf(it)) }
        }
        return configBuilder.build()
    }

    private fun getBillingDetails(): PaymentSdkBillingDetails {
        return PaymentSdkBillingDetails(
            city = BILLING_CITY,
            countryCode = BILLING_COUNTRY_CODE,
            email = BILLING_EMAIL,
            name = BILLING_NAME,
            phone = BILLING_PHONE,
            state = BILLING_STATE,
            addressLine = BILLING_ADDRESS,
            zip = BILLING_ZIP
        )
    }
}*/
