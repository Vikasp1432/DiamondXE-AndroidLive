package com.diamondxe.Activity.PaymentPages

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.diamondxe.Network.SuperActivity
import com.diamondxe.R
import com.diamondxe.databinding.ActivityPayTabBinding
import com.payment.paymentsdk.PaymentSdkActivity.Companion.startAlternativePaymentMethods
import com.payment.paymentsdk.PaymentSdkActivity.Companion.startCardPayment
import com.payment.paymentsdk.PaymentSdkActivity.Companion.startSamsungPayment
import com.payment.paymentsdk.PaymentSdkConfigBuilder
import com.payment.paymentsdk.QuerySdkActivity
import com.payment.paymentsdk.integrationmodels.PaymentSDKQueryConfiguration
import com.payment.paymentsdk.integrationmodels.PaymentSdkApms
import com.payment.paymentsdk.integrationmodels.PaymentSdkBillingDetails
import com.payment.paymentsdk.integrationmodels.PaymentSdkCardDiscount
import com.payment.paymentsdk.integrationmodels.PaymentSdkConfigurationDetails
import com.payment.paymentsdk.integrationmodels.PaymentSdkError
import com.payment.paymentsdk.integrationmodels.PaymentSdkLanguageCode
import com.payment.paymentsdk.integrationmodels.PaymentSdkShippingDetails
import com.payment.paymentsdk.integrationmodels.PaymentSdkTokenFormat
import com.payment.paymentsdk.integrationmodels.PaymentSdkTokenise
import com.payment.paymentsdk.integrationmodels.PaymentSdkTransactionClass
import com.payment.paymentsdk.integrationmodels.PaymentSdkTransactionDetails
import com.payment.paymentsdk.integrationmodels.PaymentSdkTransactionType
import com.payment.paymentsdk.sharedclasses.interfaces.CallbackPaymentInterface
import com.payment.paymentsdk.sharedclasses.interfaces.CallbackQueryInterface
import com.payment.paymentsdk.sharedclasses.model.response.TransactionResponseBody
import org.json.JSONObject

class PayTabActivity : SuperActivity(), CallbackPaymentInterface, CallbackQueryInterface {

    companion object {
        private const val TAG = "MainActivity"

        // Payment Configuration Constants
        private const val PROFILE_ID = "146752"
        private const val SERVER_KEY = "SKJ9THKDDW-JJK99WWMTZ-LK6GWN6LWT"
        private const val CLIENT_KEY = "CDK2KR-RMT666-KPPVVN-TDHHDH"
        private const val MERCHANT_COUNTRY_CODE = "AE" // ISO2 country code for UAE
        private const val CURRENCY = "AED"
        private const val AMOUNT = 20.0
        private const val TRANSACTION_TITLE = "SDK sample"
        private const val CART_ID = "123456"
        private const val CART_DESCRIPTION = "Cart description"
        private val LANGUAGE_CODE = PaymentSdkLanguageCode.EN

        // Billing Details Constants
        private const val BILLING_CITY = "Dubai"
        private const val BILLING_COUNTRY_CODE = "AE" // ISO2 country code for UAE
        private const val BILLING_EMAIL = "testuser@example.com"
        private const val BILLING_NAME = "Ali Ahmed"
        private const val BILLING_PHONE = "+971501234567"
        private const val BILLING_STATE = "Dubai"
        private const val BILLING_ADDRESS = "1234 Test Street"
        private const val BILLING_ZIP = "00000"

        // Shipping Details Constants
       /* private const val SHIPPING_CITY = "Abu Dhabi"
        private const val SHIPPING_COUNTRY_CODE = "AE" // ISO2 country code for UAE
        private const val SHIPPING_EMAIL = "testrecipient@example.com"
        private const val SHIPPING_NAME = "Ali Ahmed"
        private const val SHIPPING_PHONE = "+971501234568"
        private const val SHIPPING_STATE = "Abu Dhabi"
        private const val SHIPPING_ADDRESS = "5678 Sample Avenue"
        private const val SHIPPING_ZIP = "00000"*/


    }

    private var token: String? = null
    private var transRef: String? = null
    private lateinit var binding: ActivityPayTabBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPayTabBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupClickListeners()
    }
    private fun setupClickListeners() {
        binding.pay.setOnClickListener { initiateCardPayment() }
       /* binding.knetPay.setOnClickListener { initiateAlternativePayment(PaymentSdkApms.KNET_CREDIT) }
        binding.valuPay.setOnClickListener { initiateAlternativePayment(PaymentSdkApms.VALU) }
        binding.fawryPay.setOnClickListener { initiateAlternativePayment(PaymentSdkApms.FAWRY) }*/
    }

    private fun initiateCardPayment() {
        val configData = generatePaymentConfiguration()
        startCardPayment(this, configData, this)
    }

    private fun initiateAlternativePayment(apm: PaymentSdkApms) {
        val configData = generatePaymentConfiguration(apm)
        startAlternativePaymentMethods(this, configData, this)
    }



    private fun generatePaymentConfiguration(
        selectedApm: PaymentSdkApms? = null
    ): PaymentSdkConfigurationDetails {
        val configBuilder = PaymentSdkConfigBuilder(
            profileId = PROFILE_ID,
            serverKey = SERVER_KEY,
            clientKey = CLIENT_KEY,
            amount = AMOUNT,
            currencyCode = CURRENCY
        ).apply {
            setCartDescription(CART_DESCRIPTION)
            setLanguageCode(LANGUAGE_CODE)
            setMerchantIcon(
                ContextCompat.getDrawable(
                    this@PayTabActivity, R.drawable.cart
                )
            )
            setBillingData(getBillingDetails())
            setMerchantCountryCode(MERCHANT_COUNTRY_CODE)
            setTransactionType(PaymentSdkTransactionType.SALE)
            setTransactionClass(PaymentSdkTransactionClass.ECOM)
           // setShippingData(getShippingDetails())
            setTokenise(PaymentSdkTokenise.MERCHANT_MANDATORY)
            setCartId(CART_ID)
            showBillingInfo(true)
            showShippingInfo(false)
            forceShippingInfo(false)
            setScreenTitle(TRANSACTION_TITLE)
            hideCardScanner(false)
            linkBillingNameWithCard(false)
           // setCardDiscount(getCardDiscounts())
            selectedApm?.let { setAlternativePaymentMethods(listOf(it)) }
        }
        return configBuilder.build()
    }

    private fun getCardDiscounts(): List<PaymentSdkCardDiscount> {
        return listOf(
            PaymentSdkCardDiscount(
                discountCards = listOf("40001"),
                discountValue = 10.0,
                discountTitle = "● 10% discount - 40001",
                isPercentage = true
            )
        )
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


    override fun getSuccessResponce(jsonObject: JSONObject?, service_ID: Int) {
        TODO("Not yet implemented")
    }

    override fun getErrorResponce(error: String?, service_ID: Int) {
        TODO("Not yet implemented")
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }


    override fun onCancel() {
        TODO("Not yet implemented")
    }


    override fun onResult(transactionResponseBody: TransactionResponseBody) {
        Log.e("",""+transactionResponseBody)
        Log.e("Code","198........"+transactionResponseBody.code)
        Log.e("cartAmount","198........"+transactionResponseBody.cartAmount)
        Log.e("message","198........"+transactionResponseBody.message)
    }

    override fun onPaymentCancel() {
        TODO("Not yet implemented")
    }

    override fun onPaymentFinish(paymentSdkTransactionDetails: PaymentSdkTransactionDetails) {
        Log.e("cartAmount",".............."+paymentSdkTransactionDetails.cartAmount)
        Log.e("profileId",".............."+paymentSdkTransactionDetails.profileId)
        Log.e("redirectUrl",".............."+paymentSdkTransactionDetails.payResponseReturn)
        Log.e("serviceId",".............."+paymentSdkTransactionDetails.serviceId)

    }

    override fun onError(error: PaymentSdkError) {
        Log.e("error",".............."+error)
      //  showToast("Error: message: ${error.msg}, code: ${error.code}, trace: ${error.trace}")
    }
}