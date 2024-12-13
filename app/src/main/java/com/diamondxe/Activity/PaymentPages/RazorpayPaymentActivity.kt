package com.diamondxe.Activity.PaymentPages

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.diamondxe.Network.SuperActivity
import com.diamondxe.R
import com.diamondxe.databinding.ActivityZarorpayPaymentBinding
import com.razorpay.Checkout
import com.razorpay.ExternalWalletListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONException
import org.json.JSONObject

class RazorpayPaymentActivity : SuperActivity(), PaymentResultWithDataListener, ExternalWalletListener {

    lateinit var binding: ActivityZarorpayPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityZarorpayPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.upi.setOnClickListener(View.OnClickListener {
            UPIRazorpayUPI()
        })

        binding.netbanking.setOnClickListener(View.OnClickListener {
            UPIRazorpayNetbancking()
        })
        binding.card.setOnClickListener(View.OnClickListener {
            UPIRazorpaycard()
        })

        /**Phone Pe comment here */
        //createCheckSumPaymentInitiatedAndOpenPhonePe();
        /*RazorpayUtility.initializePayment(
            this.also { context = it },
            1.also { amount = it },  // In rupees
            "9284064503".also { contact = it },
            "chaitanyamunje@gmail.com".also { email = it },
            also { paymentMethod = it },
            resources.getString(R.string.app_name).also { appName = it },
            R.drawable.appicon.also { appIcon = it }
        )*/
    }

    private  fun  UPIRazorpayUPI()
    {
        val samount = 1

        val amount = Math.round(samount.toFloat() * 100)

        val checkout = Checkout()

        checkout.setKeyID("rzp_test_rfy0AcVmf33RWF")


        checkout.setImage(R.drawable.appicon)

        val object2 = JSONObject()
        try {
            object2.put("name", resources.getString(R.string.app_name))

            object2.put("description", "Test payment")

            object2.put("currency", "INR")
            /*object2.put("color",resources.getColor(R.color.green))
            object2.put("theme", resources.getColor(R.color.green))*/

            val paymentMethods = JSONObject()
            paymentMethods.put("netbanking", false)
            paymentMethods.put("upi", true)
            paymentMethods.put("card", false)
            paymentMethods.put("wallet", false)
            paymentMethods.put("paylater", false)
            object2.put("method", paymentMethods)
            object2.put("amount", amount)
            object2.put("prefill.contact", "9284064503")
            object2.put("prefill.email", "chaitanyamunje@gmail.com")
            checkout.open(this@RazorpayPaymentActivity, object2)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private  fun  UPIRazorpayNetbancking()
    {
        val samount = 1

        val amount = Math.round(samount.toFloat() * 100)

        val checkout = Checkout()

        checkout.setKeyID("rzp_test_rfy0AcVmf33RWF")

        checkout.setImage(R.drawable.payment)

        val object2 = JSONObject()
        try { object2.put("name", resources.getString(R.string.app_name))

            object2.put("description", "Test payment")

            object2.put("theme.color", "")

            object2.put("currency", "INR")

            val paymentMethods = JSONObject()
            paymentMethods.put("netbanking", true)
            paymentMethods.put("upi", false)
            paymentMethods.put("card", false)
            paymentMethods.put("wallet", false)
            paymentMethods.put("paylater", false)
            object2.put("method", paymentMethods)
            object2.put("reminderenable", true)
            object2.put("amount", amount)
            object2.put("prefill.contact", "9284064503")
            object2.put("prefill.email", "chaitanyamunje@gmail.com")
            checkout.open(this@RazorpayPaymentActivity, object2)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private  fun  UPIRazorpaycard()
    {
        val samount = 1

        val amount = Math.round(samount.toFloat() * 100)

        val checkout = Checkout()

        checkout.setKeyID("rzp_test_rfy0AcVmf33RWF")

        checkout.setImage(R.drawable.payment)

        val object2 = JSONObject()
        try { object2.put("name", resources.getString(R.string.app_name))

            object2.put("description", "Test payment")

            object2.put("currency", "INR")

            val paymentMethods = JSONObject()
            paymentMethods.put("netbanking", false)
            paymentMethods.put("upi", false)
            paymentMethods.put("card", true)
            paymentMethods.put("wallet", false)
            paymentMethods.put("paylater", false)
            object2.put("method", paymentMethods)
            object2.put("amount", amount)
            object2.put("prefill.contact", "9284064503")
            object2.put("prefill.email", "chaitanyamunje@gmail.com")
            checkout.open(this@RazorpayPaymentActivity, object2)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun getSuccessResponce(jsonObject: JSONObject?, service_ID: Int) {

    }

    override fun getErrorResponce(error: String?, service_ID: Int) {
        TODO("Not yet implemented")
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    override fun onPaymentSuccess(s: String?, p1: PaymentData?) {
        Log.e("paymentId","P1......"+p1)
        Log.e("paymentId","...."+p1?.paymentId)
        Log.e("all data","...."+p1?.data)
        Log.e("method","...."+p1?.data?.get("method"))
        Toast.makeText(this, "Payment is successful : " + s, Toast.LENGTH_SHORT).show();
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Payment is successful : " + p1, Toast.LENGTH_SHORT).show();    }

    override fun onExternalWalletSelected(p0: String?, p1: PaymentData?) {
        TODO("Not yet implemented")
    }
}