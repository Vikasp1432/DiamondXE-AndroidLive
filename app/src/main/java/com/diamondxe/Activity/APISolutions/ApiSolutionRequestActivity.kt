package com.diamondxe.Activity.APISolutions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diamondxe.Activity.PaymentPages.PayTabsPaymentManager
import com.diamondxe.Activity.PaymentPages.PaymentMethod
import com.diamondxe.Activity.PaymentPages.RazorpayUtility
import com.diamondxe.Activity.PaymentStatusScreenActivity
import com.diamondxe.Adapter.ApiSolutionAdapter
import com.diamondxe.Adapter.Dealer.AllBankNameListAdapter
import com.diamondxe.Adapter.Dealer.BankNEFTListAdapter
import com.diamondxe.Adapter.Dealer.PopularBankListAdapter
import com.diamondxe.Adapter.UPIOptionListAdapter
import com.diamondxe.ApiCalling.ApiConstants
import com.diamondxe.ApiCalling.VollyApiActivity
import com.diamondxe.Beans.Dealer.BankNEFTListModel
import com.diamondxe.Beans.Transaction
import com.diamondxe.Beans.UPIAppInfoListModel
import com.diamondxe.Interface.DialogItemClickInterface
import com.diamondxe.Interface.RecyclerInterface
import com.diamondxe.Network.EndlessRecyclerViewScrollListener
import com.diamondxe.Network.SuperActivity
import com.diamondxe.R
import com.diamondxe.Utils.CommonUtility
import com.diamondxe.Utils.Constant
import com.diamondxe.Utils.Utils
import com.diamondxe.databinding.ActivityApiSolutionRequest2Binding
import com.diamondxe.databinding.ApiSolutionRequestDialogBinding
import com.diamondxe.databinding.ApirequestSendDialogBinding
import com.diamondxe.databinding.ApisolutionFilterLayoutBinding
import com.payment.paymentsdk.integrationmodels.PaymentSdkError
import com.payment.paymentsdk.integrationmodels.PaymentSdkTransactionDetails
import com.payment.paymentsdk.sharedclasses.interfaces.CallbackPaymentInterface
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

class ApiSolutionRequestActivity : SuperActivity(), RecyclerInterface, DialogItemClickInterface,
    CallbackPaymentInterface, PaymentResultWithDataListener
{
    lateinit var binding:ActivityApiSolutionRequest2Binding
    private var urlParameter: HashMap<String, String>? = null
    lateinit var vollyApiActivity: VollyApiActivity
    var pageNo:Int=1
    var startDate:String=""
    var endDate:String=""
    var accountStatementResponse: ArrayList<Transaction> = ArrayList()
    lateinit  var apiSolutionAdapter:ApiSolutionAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    //Payment Section
    lateinit var  adapter: BankNEFTListAdapter
    lateinit var popularBankListAdapter: PopularBankListAdapter
    var isSelectNetBankingBank: Boolean = false
    lateinit var upiAppsArrayList: ArrayList<UPIAppInfoListModel?>
    var upiOptionListAdapter: UPIOptionListAdapter? = null
    var payloadBase64 = ""
    var checksum = ""
    var amountInPaisaString = ""
    var dob = ""
    var sendDobServer = ""
    var selectedUPIPackage = ""
    var finalSubmitValue = ""
    var NEFT: String? = "NEFT"
    var CREDIT_CARD: String = "CreditCard"
    var NET_BANKING: String = "NetBanking"
    var UPI: String = "UPI"
    var lastPosition: Int = 0
    var PAYMENT_BY_UPI: String = "UPI_INTENT"
    var PAYMENT_BY_NET_BANKING: String = "NET_BANKING"
    var totalAmount:String=""
    var currencySymbol:String=""
    var send_dob_server:String=""
    var userRole:String=""
    val modelArrayList: ArrayList<BankNEFTListModel> = ArrayList()
    val popularBankArrayList: ArrayList<BankNEFTListModel> = ArrayList()
    val allBankArrayList: ArrayList<BankNEFTListModel> = ArrayList()
    var neftCharges: Double = 0.0
    var upiCharges: Double = 0.0
    var netBankingCharges: Double = 0.0
    var cardCharges: Double = 0.0
    //String PAYMENT_BY_CARD = "CARD";
    var PAYMENT_BY_CARD: String = "PAY_PAGE"
    var paymentModeType: String = "NEFT"
    var bankChargePerc: String = ""
    var bankCharge: String = ""
    var callbackUrl:String=""
    var bankID:String=""
    var RegionType: String = "IND"
    var paymentGetwayType:String=""
    private var activity: Activity? = null
    private var context: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityApiSolutionRequest2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this.also { activity = it }
        binding.backImg.setOnClickListener(this)
        binding.accountBalanceLi.setOnClickListener(this)
        binding.uploadAmountLi.setOnClickListener(this)
        binding.apiKeyLi.setOnClickListener(this)
        binding.generateApiButton.setOnClickListener(this)
        binding.filterImg.setOnClickListener(this)
        binding.copyBtn.setOnClickListener(this)
        binding.paymentOption.rtgsRel.setOnClickListener(this)
        binding.paymentOption.cardTypeRel.setOnClickListener(this)
        binding.paymentOption.netBankingRel.setOnClickListener(this)
        binding.paymentOption.upiRel.setOnClickListener(this)
        binding.paymentOption.upiRela.setOnClickListener(this)
        binding.paymentOption.modePaymentRel.setOnClickListener(this)
        binding.paymentOption.selectDateRel.setOnClickListener(this)
        binding.paymentOption.showAllBankRel.setOnClickListener(this)
        binding.proceedToPayRel.setOnClickListener(this)
        binding.copyImg.setOnClickListener(this)
        binding.paymentOption.upiMainLin.setVisibility(View.VISIBLE)

        getCheckAPiAccess(false)

        val layoutManager = LinearLayoutManager(this)
        binding.amountBalanceRv.layoutManager = layoutManager
        binding.amountBalanceRv.isNestedScrollingEnabled = false

        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (accountStatementResponse.size >= Constant.lazyLoadingLimit) {
                    binding.progressBar.visibility = View.VISIBLE
                    pageNo += 1
                    getAccountStatement(true,startDate,endDate)
                }
            }
        }

        binding.amountBalanceRv.addOnScrollListener(scrollListener)

        UploadAmountFiled()

        binding.paymentOption.paymentImage.visibility=View.VISIBLE
        binding.paymentOption.paymentButtonSelect.visibility=View.VISIBLE
        binding.paymentOption.indiaLayout.setOnClickListener(this)
        binding.paymentOption.internationalLayout.setOnClickListener(this)

        binding.amountEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val amountText = binding.amountEt.text.toString()

                if (amountText.isNotEmpty()) {
                    binding.amountErrorTv.visibility = View.GONE
                    binding.amountEt.setBackgroundResource(R.drawable.border_purple_line_view)
                    finalSubmitValue = ""
                    calculateAmountWithCharges()
                }

                if (amountText.isEmpty()) {
                    binding.amountErrorTv.visibility = View.GONE
                    binding.amountEt.setBackgroundResource(R.drawable.border_line_view)
                    totalAmount = "0.00"
                    binding.amountErrorTv.text = "$currencySymbol 0.00"
                    finalSubmitValue = ""
                }

                // Check condition if amount is greater than 500000, UPI option is hidden
                if (amountText.isNotEmpty()) {
                    val enterAmount = amountText.trim().toIntOrNull() ?: 0
                    if (enterAmount > 500000) {
                        binding.paymentOption.upiOptionLin.visibility = View.GONE
                        binding.paymentOption.upiMainLin.visibility = View.GONE
                        paymentModeType = ""
                        selectedUPIPackage = ""
                        upiAppsArrayList.let { apps ->
                            if (apps.isNotEmpty()) {
                                for (app in apps) {
                                    app!!.isSelected = false
                                }
                                upiOptionListAdapter?.notifyDataSetChanged()
                            }
                        }
                    } else {
                        Log.e("RegionType","....208..........."+RegionType)
                        if (RegionType.equals("IND", ignoreCase = true)) {
                            binding.paymentOption.upiMainLin.visibility = View.VISIBLE
                        } else if (RegionType.equals("GCC", ignoreCase = true)) {
                            binding.paymentOption.upiMainLin.visibility = View.GONE
                        }
                        binding.paymentOption.upiOptionLin.visibility = View.GONE
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        getApiKEY(true)

        // Initialize PhonePe
       // PaymentUtils.initializePhonePe(this)
        upiAppsArrayList = getInstalledUPIApps()
        //hide the UPI option
        //  showUPIAppsOption(upiAppsArrayList)
        getDXEBankDetailsAPI(true,"IND")
        getPaymentOptionAPI(true)
        getBankChargesAPI(true)
        // If NEFT is selected by default
        if (paymentModeType.equals(NEFT, ignoreCase = true)) {
            byDefaultNEFTSelected()
        }
    }
    private fun byDefaultNEFTSelected() {
        binding.paymentOption.rtgsRel.setBackgroundResource(R.drawable.background_select_payment_option)
        binding.paymentOption.cardTypeRel.setBackgroundResource(R.drawable.background_payment_option)
        binding.paymentOption.netBankingRel.setBackgroundResource(R.drawable.background_payment_option)
        binding.paymentOption.rtgsTv.setTextColor(ContextCompat.getColor(context!!, R.color.purple_light))
        binding.paymentOption.cardTypeTv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        binding.paymentOption.netBankingTv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        binding.paymentOption.paymentOptionErrorTv.visibility = View.GONE
        binding.paymentOption.showBankNameLin.visibility = View.VISIBLE
        binding.paymentOption.bankAccountDetailsLin.visibility = View.VISIBLE
        binding.paymentOption.showAllBankLin.visibility = View.GONE
    }
    private var mDropdown: PopupWindow? = null
    private lateinit var mInflater: LayoutInflater

    private fun neftSelected() {
        with(binding.paymentOption) {
            rtgsRel.setBackgroundResource(R.drawable.background_select_payment_option)
            cardTypeRel.setBackgroundResource(R.drawable.background_payment_option)
            netBankingRel.setBackgroundResource(R.drawable.background_payment_option)
            upiRela.setBackgroundResource(R.drawable.background_payment_option)
            rtgsTv.setTextColor(ContextCompat.getColor(context!!, R.color.purple_light))
            cardTypeTv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
            netBankingTv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
            upiTv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
            paymentOptionErrorTv.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            selectBankErrorTv.visibility = View.GONE
            recyclerViewUpi.visibility = View.GONE
            selectPaymentOptionClearUPIOption(NEFT!!)
            isSelectNetBankingBank = false
            lastPosition = 0
            setNEFTBanKDetails()

            paymentModeType = NEFT!!

            Log.e("modelArrayList",".......215...."+modelArrayList.size)
            adapter = BankNEFTListAdapter(modelArrayList, context, this@ApiSolutionRequestActivity)
            binding.paymentOption.recyclerView.adapter = adapter

            calculateAmountWithCharges()
            showUpiList.setVisibility(View.GONE)
            showBankNameLin.visibility = View.VISIBLE
            bankAccountDetailsLin.visibility = View.VISIBLE
            showAllBankLin.visibility = View.GONE
        }
    }

    private fun netBankingSelected() {
        binding.paymentOption.rtgsRel.setBackgroundResource(R.drawable.background_payment_option)
        binding.paymentOption.cardTypeRel.setBackgroundResource(R.drawable.background_payment_option)
        binding.paymentOption.netBankingRel.setBackgroundResource(R.drawable.background_select_payment_option)
        binding.paymentOption.upiRela.setBackgroundResource(R.drawable.background_payment_option)

        binding.paymentOption.rtgsTv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        binding.paymentOption.cardTypeTv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        binding.paymentOption.netBankingTv.setTextColor(ContextCompat.getColor(context!!, R.color.purple_light))
        binding.paymentOption.upiTv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        // If Payment Option Selected After that Error Gone
        binding.paymentOption.paymentOptionErrorTv.visibility = View.GONE
        editTextAndTextViewValueBlank()

        // binding.paymentOption.recyclerView.visibility = View.VISIBLE // Bank List Layout Show
        binding.paymentOption.selectBankErrorTv.visibility = View.GONE // Hide Bank Selection Required Error.

        binding.paymentOption.recyclerViewUpi.visibility = View.GONE // UPI Option Hide List Layout

        isSelectNetBankingBank = false // Use For Check Validation Net Banking Bank Not Selected

        selectPaymentOptionClearUPIOption(NET_BANKING) // UPI Option Clear

        paymentModeType = NET_BANKING

        // Amount Calculated Final Amount and Show Amount in Bottom of Screen
        calculateAmountWithCharges()

        // All Position Unselected.
        for (i in popularBankArrayList.indices) {
            popularBankArrayList[i].isSelected=false
        }

        binding.paymentOption.selectBankTv.text = "" // Bank Name TextView Blank
        binding.paymentOption.selectBankTv.hint = getString(R.string.select_bank)

        //netbancking list comment
        /*popularBankListAdapter = PopularBankListAdapter(popularBankArrayList, context, this)
        binding.paymentOption.recyclerView.adapter = popularBankListAdapter*/
        binding.paymentOption.showUpiList.visibility=View.GONE
        binding.paymentOption.showAllBankLin.visibility=View.GONE
        binding.paymentOption.showBankNameLin.visibility=View.GONE
        //  binding.paymentOption.showBankNameLin.visibility = View.VISIBLE // Bank List Layout
        // binding.paymentOption.bankAccountDetailsLin.visibility = View.GONE // Bank Account Details Layout
        // binding.paymentOption.showAllBankLin.visibility = View.VISIBLE
    }

    fun creditCardSelected() {
        binding.paymentOption.rtgsRel.setBackgroundResource(R.drawable.background_payment_option)
        binding.paymentOption.cardTypeRel.setBackgroundResource(R.drawable.background_select_payment_option)
        binding.paymentOption.netBankingRel.setBackgroundResource(R.drawable.background_payment_option)
        binding.paymentOption.upiRela.setBackgroundResource(R.drawable.background_payment_option)

        binding.paymentOption.rtgsTv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        binding.paymentOption.cardTypeTv.setTextColor(ContextCompat.getColor(context!!, R.color.purple_light))
        binding.paymentOption.netBankingTv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        binding.paymentOption.upiTv.setTextColor(ContextCompat.getColor(context!!, R.color.black))

        // If Payment Mode Not Selected NEFT/RGTS Then Value Blank
        editTextAndTextViewValueBlank()

        // If Payment Option Selected After that Error Gone
        binding.paymentOption.paymentOptionErrorTv.visibility = View.GONE
        binding.paymentOption.selectBankErrorTv.visibility = View.GONE // Hide Bank Selection Required Error.

        binding.paymentOption.recyclerViewUpi.visibility = View.GONE // UPI Option Hide List Layout

        selectPaymentOptionClearUPIOption(CREDIT_CARD) // UPI Option Clear

        isSelectNetBankingBank = false // Use For Check Validation Net Banking Bank Not Selected

        paymentModeType = CREDIT_CARD

        // Amount Calculated Final Amount and Show Amount in Bottom of Screen
        calculateAmountWithCharges()
        binding.paymentOption.showUpiList.setVisibility(View.GONE)
        binding.paymentOption.showBankNameLin.visibility = View.GONE // Bank List Layout
        binding.paymentOption.bankAccountDetailsLin.visibility = View.GONE // Bank Account Details Layout
        binding.paymentOption.showAllBankLin.visibility = View.GONE // Select Bank DropDown Layout Gone
    }

    private fun selectPaymentOptionClearUPIOption(paymentOptionSelectionType: String) {
        if (paymentOptionSelectionType.equals(UPI, ignoreCase = true)) {
        } else {
            if (upiAppsArrayList != null && upiAppsArrayList.size > 0) {
                for (item in upiAppsArrayList) {
                    item!!.isSelected = false
                }
                selectedUPIPackage = ""
                upiOptionListAdapter!!.notifyDataSetChanged()
            } else {
            }
        }
    }

    fun calculateAmountWithCharges() {
        val inputAmount = binding.amountEt.text.toString().trim()
        if (inputAmount.isNotEmpty()) {
            finalSubmitValue = ""

            val amount = inputAmount.toDoubleOrNull() ?: 0.0
            var charges = 0.0

            when (paymentModeType) {
                NEFT -> {
                    charges = amount * (neftCharges / 100)
                }
                UPI -> {
                    charges = amount * (upiCharges / 100)
                }
                NET_BANKING -> {
                    charges = amount * (netBankingCharges / 100)
                }
                CREDIT_CARD -> {
                    charges = amount * (cardCharges / 100)
                }
            }

            val finalAmount = amount + charges
            totalAmount = finalAmount.toString()

            if (currencySymbol.isEmpty()) {
                currencySymbol = ApiConstants.rupeesIcon
            }

            binding.totalAmountTv.text = "$currencySymbol$totalAmount"
        }
    }

    fun setNEFTBanKDetails() {
        val bank = modelArrayList[lastPosition]
        binding.paymentOption.branchNameTv.text = if (bank.branchName.isNotEmpty()) bank.branchName else "-"
        binding.paymentOption.ifscCodeTv.text = if (bank.ifsc.isNotEmpty()) bank.ifsc else "-"
        binding.paymentOption.swiftCodeTv.text = if (bank.swift.isNotEmpty()) bank.swift else "-"
        binding.paymentOption.bankNameTv.text = if (bank.bankName.isNotEmpty()) bank.bankName else "-"
        binding.paymentOption.accountNumberTv.text = if (bank.accountNumber.isNotEmpty()) bank.accountNumber else "-"
        bank.isSelected = true
    }

    fun getBankChargesAPI(showLoader: Boolean) {
        val uuid = CommonUtility.getAndroidId(context)
        if (Utils.isNetworkAvailable(context)) {
            val urlParameter = hashMapOf<String, String>()

            urlParameter["sessionId"] = uuid

            vollyApiActivity = VollyApiActivity(
                context, this, urlParameter,
                ApiConstants.GET_BANK_CHARGES,
                ApiConstants.GET_BANK_CHARGES_ID,
                showLoader, "GET"
            )
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR)
        }
    }

    fun getDXEBankDetailsAPI(showLoader: Boolean,regionType:String) {
        val uuid = CommonUtility.getAndroidId(context)
        if (Utils.isNetworkAvailable(context)) {
            val urlParameter = HashMap<String, String>().apply {
                put("sessionId", uuid)
                put("countryName", "India")
                put("region", regionType)
            }

            vollyApiActivity = VollyApiActivity(
                context,
                this,
                urlParameter,
                ApiConstants.GET_DXE_BANK_DETAILS,
                ApiConstants.GET_DXE_BANK_DETAILS_ID,
                showLoader,
                "POST"
            )
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR)
        }
    }

    private fun getPaymentOptionAPI(showLoader: Boolean) {
        val uuid = CommonUtility.getAndroidId(context)
        if (Utils.isNetworkAvailable(context)) {
            val urlParameter = HashMap<String, String>().apply {
                put("sessionId", uuid)
            }

            vollyApiActivity = VollyApiActivity(
                context,
                this,
                urlParameter,
                ApiConstants.PHONE_PE_PAYMENT_OPTION,
                ApiConstants.PHONE_PE_PAYMENT_OPTION_ID,
                showLoader,
                "GET"
            )
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR)
        }
    }

    private fun getInstalledUPIApps(): ArrayList<UPIAppInfoListModel?> {
        val upiAppsList = ArrayList<UPIAppInfoListModel?>()
        val uri = Uri.parse("upi://pay")

        val upiUriIntent = Intent(Intent.ACTION_VIEW).apply {
            data = uri
        }

        val packageManager = application.packageManager
        val resolveInfoList = packageManager.queryIntentActivities(upiUriIntent, PackageManager.MATCH_DEFAULT_ONLY)

        resolveInfoList.forEach { resolveInfo ->
            val packageName = resolveInfo.activityInfo.packageName
            val appName = resolveInfo.loadLabel(packageManager).toString()
            val appIcon = resolveInfo.loadIcon(packageManager)
            upiAppsList.add(UPIAppInfoListModel(packageName, appName, appIcon, false))
        }

        return upiAppsList
    }

    private fun getCheckAPiAccess(showLoader:Boolean)
    {
        val uuid = CommonUtility.getAndroidId(this)
        urlParameter = java.util.HashMap()
        urlParameter!!["sessionId"] = "" + uuid
        vollyApiActivity = VollyApiActivity(
            this,
            this,
            urlParameter,
            ApiConstants.API_SOLUTION_ACCESS,
            ApiConstants.API_SOLUTION_ACCESS_ID,
            showLoader,
            "GET"
        )
    }

    fun getAccountStatement(showLoader:Boolean,startDate:String,endDate:String)
    {
        val uuid = CommonUtility.getAndroidId(this)
        urlParameter = java.util.HashMap()
        urlParameter!!["sessionId"] = "" + uuid
        urlParameter!!["limit"] = "" + Constant.lazyLoadingLimit
        urlParameter!!["page"] = "" + pageNo
        urlParameter!!["startDate"] = "" + startDate
        urlParameter!!["endDate"] = "" + endDate
        vollyApiActivity = VollyApiActivity(
            this,
            this,
            urlParameter,
            ApiConstants.ACCOUNT_STATEMENT,
            ApiConstants.ACCOUNT_STATEMENT_ID,
            showLoader,
            "GET"
        )
    }

    private fun getGenerateAPIKey(showLoader:Boolean)
    {
        val uuid = CommonUtility.getAndroidId(this)
        urlParameter = java.util.HashMap()
        urlParameter!!["sessionId"] = "" + uuid
        vollyApiActivity = VollyApiActivity(
            this,
            this,
            urlParameter,
            ApiConstants.GENERATEAPI_KEY,
            ApiConstants.GENERATEAPI_KEY_ID,
            showLoader,
            "POST"
        )
    }

    override fun getSuccessResponce(jsonObject: JSONObject?, service_ID: Int) {
        binding.progressBar.setVisibility(View.GONE)
        Log.e("jsonObject","...49..."+jsonObject);
        val jsonObjectData = jsonObject!!
        val message = jsonObjectData.optString("msg")
        when (service_ID) {
            ApiConstants.SOLUTION_ACCOUNT_RECHARGE_ID -> {
                if (jsonObjectData.optString("status").equals("1", ignoreCase = true)) {
                     Log.e("-------Diamond : ", "RTGS/NEFT : $jsonObjectData")
                    val jObjDetails = jsonObjectData.optJSONObject("details")

                    if (finalSubmitValue.equals("yes", ignoreCase = true)) {

                        Constant.paymentOrderID = CommonUtility.checkString(jObjDetails?.optString("order_id"))
                        Constant.paymentUserID = CommonUtility.checkString(jObjDetails?.optString("user_id"))
                        Log.e("paymentOrderID","...**********......."+Constant.paymentOrderID)
                        if (paymentModeType.equals(NEFT, ignoreCase = true)) {
                            Constant.comeFrom = "APISOLUTION"
                            val intent = Intent(activity, PaymentStatusScreenActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(0, 0)
                        } else {
                            totalAmount = CommonUtility.checkString(jObjDetails?.optString("total_amount"))
                            callbackUrl = CommonUtility.checkString(jObjDetails?.optString("callback_url"))
                            //createCheckSumPaymentInitiatedAndOpenPhonePe()

                            val jObjUserDetails = jObjDetails.optJSONObject("user_data")
                            val firstname = CommonUtility.checkString(jObjUserDetails?.optString("firstname"))
                            val lastname = CommonUtility.checkString(jObjUserDetails?.optString("lastname"))
                            val userEmail = CommonUtility.checkString(jObjUserDetails?.optString("email"))
                            val userMobile = CommonUtility.checkString(jObjUserDetails?.optString("mobile"))

                            //Log.e("razorpay_id", "615..(1)..*****************.....${jObjDetails.optString("razorpay_order_id")}")

                            val razorpay_id = CommonUtility.checkString(jObjDetails.optString("razorpay_order_id"))
                            //Log.e("razorpay_id", "615....*****************.....$razorpay_id")


                           // Log.e("paymentModeType", "1266.......$paymentModeType")
                            /*println("First Name: $firstname")
                            println("Last Name: $lastname")
                            println("Email: $userEmail")
                            println("Mobile: $userMobile")*/

                            /*Log.e("amount_et", "..1239...****************.....${binding.amountEt.text.toString()}")
                            Log.e("Amount In INT", "..1240.....${binding.amountEt.text.toString().toInt()}")
                            Log.e("RegionType", "..1307....$RegionType")*/


                            if (RegionType.equals("IND", ignoreCase = true)) {
                                when {
                                    paymentModeType.equals(UPI, ignoreCase = true) -> {
                                        RazorpayUtility.initializePayment(
                                            this,
                                            totalAmount.toInt(),
                                            userMobile,
                                            userEmail,
                                            PaymentMethod.UPI,
                                            resources.getString(R.string.app_name),
                                            razorpay_id
                                        )
                                    }
                                    paymentModeType.equals(NET_BANKING, ignoreCase = true) -> {
                                        RazorpayUtility.initializePayment(
                                            this,
                                            totalAmount.toInt(),
                                            userMobile,
                                            userEmail,
                                            PaymentMethod.NET_BANKING,
                                            resources.getString(R.string.app_name),
                                            razorpay_id
                                        )
                                    }
                                    paymentModeType.equals(CREDIT_CARD, ignoreCase = true) -> {
                                        RazorpayUtility.initializePayment(
                                            this,
                                            totalAmount.toInt(),
                                            userMobile,
                                            userEmail,
                                            PaymentMethod.CARD,
                                            resources.getString(R.string.app_name),
                                            razorpay_id
                                        )
                                    }
                                }
                            } else if (RegionType.equals("GCC", ignoreCase = true))
                            {
                                val billingAddress = jObjUserDetails.optJSONObject("billing_address")
                                var getAddress = ""
                                var getCity = ""
                                var getState = ""
                                var getCountry = ""
                                var getZip = ""
                                var getIp = ""

                                billingAddress?.let {
                                    getAddress = it.optString("address", "")
                                    getCity = it.optString("city", "")
                                    getState = it.optString("state", "")
                                    getCountry = it.optString("country", "")
                                    getZip = it.optString("zip", "")
                                    getIp = it.optString("ip", "")

                                    /*Log.e("Billing Address", "Address: $getAddress")
                                    Log.e("Billing Address", "City: $getCity")
                                    Log.e("Billing Address", "State: $getState")
                                    Log.e("Billing Address", "Country: $getCountry")
                                    Log.e("Billing Address", "Zip: $getZip")*/
                                    // Log.e("Billing Address", "IP: $getIp")
                                }

                                val exchangeRate = CommonUtility.checkDouble(jObjDetails.optString("exchange_rate"))
                                val amount = CommonUtility.checkInt(jObjDetails.optString("total_amount"))

                                val resultDouble = amount * exchangeRate
                                val result = resultDouble.toInt()

                                //Log.e("result", "..14000...$result")
                                val bd = BigDecimal(resultDouble).setScale(2, RoundingMode.HALF_UP)
                                val resultWithTwoDecimals = bd.toDouble()

                                println("Result: $resultWithTwoDecimals")

                                val currencySymbol = jObjDetails?.optString("currency_symbol")
                                val orderID = jObjDetails?.optString("order_id")

                                Log.e("currency Symbol", "..1424....$currencySymbol....orderID....$orderID")
                                //  String callbackurl=jObjDetails.optString("callback_url");
                                val callbackurl =
                                    CommonUtility.checkString(jObjDetails.optString("callback_url"))
                                Log.e(
                                    "callbackurl",
                                    "#####..1403....$callbackurl"
                                )
                                PayTabsPaymentManager.setCurrencyAndCartId(
                                    currencySymbol!!,
                                    orderID!!,
                                    callbackurl
                                )

                                PayTabsPaymentManager.setBillingDetails(
                                    getCity,
                                    getCountry,
                                    userEmail,
                                    "$firstname$lastname",
                                    userMobile,
                                    getState,
                                    getAddress,
                                    getIp
                                )

                                PayTabsPaymentManager.initiateCardPayment(
                                    this@ApiSolutionRequestActivity,
                                    resultWithTwoDecimals.toDouble(),
                                    this@ApiSolutionRequestActivity
                                )

                            }

                        }
                    } else {
                        currencySymbol = CommonUtility.checkString(jObjDetails?.optString("currency_symbol"))
                        bankCharge = CommonUtility.checkString(jObjDetails?.optString("bank_charge"))
                        bankChargePerc = CommonUtility.checkString(jObjDetails?.optString("bank_charge_perc"))
                        totalAmount = CommonUtility.checkString(jObjDetails?.optString("total_amount"))
                    }
                } else if (jsonObjectData.optString("status").equals("0", ignoreCase = true)) {
                } else if (jsonObjectData.optString("status").equals("4", ignoreCase = true)) {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                } else {
                    // Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                }


            }
            ApiConstants.API_SOLUTION_ACCESS_ID -> {

                if (jsonObjectData.optString("status").equals("1", ignoreCase = true)) {
                    val jObjDetails = jsonObjectData.optJSONObject("details")
                    val access = jObjDetails?.optInt("access", 0) ?: 0 // Safely handle null and default to 0

                    if (access == 1) {
                        Log.d("AccessCheck", "Access granted")
                        getAccountStatement(true,startDate,endDate)
                    } else {
                        Log.d("AccessCheck", "Access denied")
                        ApiSolutionRegisterDialog(this)
                    }
                } else {
                    Log.e("StatusCheck", "Status not 1 or access data unavailable")
                }

            }
            ApiConstants.APPLY_API_SOLUTION_ID -> {
                val status = jsonObjectData.optString("status")
                val msg = jsonObjectData.optString("msg")
                ApiSolutionRequestSendDialog(this,msg)
            }

            ApiConstants.ACCOUNT_STATEMENT_ID -> {
                //accountStatementResponse
                if (jsonObjectData.optString("status").equals("1", ignoreCase = true)) {
                    val jObjDetails = jsonObjectData.optJSONObject("details")
                    val account_balance = jObjDetails.getString("account_balance")
                    binding.availablePointsTv.text = getString(R.string.available_wallet_points) + " " + account_balance

                    val details = jObjDetails.getJSONArray("history")
                    if (pageNo == 1) {
                        if (accountStatementResponse.size > 0) {
                            accountStatementResponse.clear()
                        }
                    }
                    if (details != null && details.length() < Constant.lazyLoadingLimit) {
                        scrollListener.loading = false
                    } else {
                        scrollListener.loading = true
                    }

                    for (i in 0 until details.length()) {
                        val objectCodes = details.getJSONObject(i)

                        val model = Transaction(
                            reference_no = CommonUtility.checkString(objectCodes.optString("reference_no")),
                            currency_symbol = CommonUtility.checkString(objectCodes.optString("currency_symbol")),
                            amount = CommonUtility.checkString(objectCodes.optString("amount")),
                            payment_mode = CommonUtility.checkString(objectCodes.optString("payment_mode")),
                            payment_status = CommonUtility.checkString(objectCodes.optString("payment_status")),
                            description = CommonUtility.checkString(objectCodes.optString("description")),
                            created_at = CommonUtility.checkString(objectCodes.optString("created_at")),
                            type = CommonUtility.checkString(objectCodes.optString("type")),
                            currency_code = CommonUtility.checkString(objectCodes.optString("currency_code")),
                            exchange_rate = CommonUtility.checkString(objectCodes.optString("exchange_rate")),
                            bank_charge_perc = CommonUtility.checkString(objectCodes.optString("bank_charge_perc")),
                            bank_charge = CommonUtility.checkString(objectCodes.optString("bank_charge")),
                            final_amount = CommonUtility.checkString(objectCodes.optString("final_amount")),
                            bank_utr_no = CommonUtility.checkString(objectCodes.optString("bank_utr_no")),
                            bank_payment_method = CommonUtility.checkString(objectCodes.optString("bank_payment_method")),
                            bank_transaction_date = CommonUtility.checkString(objectCodes.optString("bank_transaction_date")),
                            opening_balance = CommonUtility.checkString(objectCodes.optString("opening_balance")),
                            closing_balance = CommonUtility.checkString(objectCodes.optString("closing_balance")),
                            transaction_id = CommonUtility.checkString(objectCodes.optString("transaction_id")),
                        )

                        if (objectCodes.optString("created_at").isNotEmpty()) {
                            val convertedDate = CommonUtility.convertDateTimeIntoLocal(
                                objectCodes.optString("created_at"),
                                ApiConstants.DATE_FORMAT,
                                "dd/MM/yyyy, hh:mm:ss a"
                            )
                            model.created_at = convertedDate
                        }

                        accountStatementResponse.add(model)
                    }

                    if (pageNo == 1) {
                        apiSolutionAdapter = ApiSolutionAdapter(accountStatementResponse!!, this, this)
                        binding.amountBalanceRv.setAdapter(apiSolutionAdapter)
                    } else {
                        apiSolutionAdapter.notifyDataSetChanged()
                    }
                    if (accountStatementResponse.isEmpty())
                    {
                        binding.errorTv.visibility=View.VISIBLE
                        binding.errorTv.text=ApiConstants.NO_RESULT_FOUND
                        binding.amountBalanceRv.visibility=View.GONE
                    } else {
                        binding.errorTv.visibility=View.GONE
                        binding.amountBalanceRv.visibility=View.VISIBLE
                    }

                } else if (jsonObjectData.optString("status").equals("0", ignoreCase = true)) {
                    Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show()
                } else if (jsonObjectData.optString("status").equals("4", ignoreCase = true)) {
                    Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show()
                }
            }

            ApiConstants.GENERATEAPI_KEY_ID -> {
                if (jsonObjectData.optString("status") == "1") {
                    val jObjDetails = jsonObjectData.optJSONObject("details")
                    var apiKey = jObjDetails?.optString("api_key") ?: "" // Safely extract the API key

                    if (apiKey.isNotEmpty())
                    {
                        Log.e("APIKey", "API Key retrieved..##: $apiKey")
                        binding.apikeyGenerateTv.setText(apiKey)
                    } else {
                        Log.d("APIKey", "API Key is missing, redirecting to registration dialog")
                    }
                } else {
                    Log.e("StatusCheck", "Status is not 1 or API key data unavailable")
                }
            }

            ApiConstants.GET_API_KEY_ID -> {
                if (jsonObjectData.optString("status") == "1") {
                    val jObjDetails = jsonObjectData.optJSONObject("details")
                    var apiKey = jObjDetails?.optString("api_key") ?: "" // Safely extract the API key

                    if (apiKey.isNotEmpty())
                    {
                        Log.e("APIKey", "API Key retrieved..##: $apiKey")
                        binding.apikeyGenerateTv.setText(apiKey)
                    } else {
                        Log.d("APIKey", "API Key is missing, redirecting to registration dialog")
                    }
                } else {
                    Log.e("StatusCheck", "Status is not 1 or API key data unavailable")
                }
            }


            ApiConstants.GET_DXE_BANK_DETAILS_ID -> {

                if (modelArrayList.isNotEmpty()) {
                    modelArrayList.clear()
                }
                if (jsonObjectData.optString("status").equals("1", ignoreCase = true)) {
                    val details = jsonObjectData.getJSONArray("details")

                    for (i in 0 until details.length()) {
                        val objectCodes = details.getJSONObject(i)

                        val model = BankNEFTListModel().apply {
                            bankId = CommonUtility.checkString(objectCodes.optString("bank_id"))
                            bank = CommonUtility.checkString(objectCodes.optString("bank"))
                            accountName = CommonUtility.checkString(objectCodes.optString("account_name"))
                            branchName = CommonUtility.checkString(objectCodes.optString("branch_name"))
                            ifsc = CommonUtility.checkString(objectCodes.optString("ifsc"))
                            bankName = CommonUtility.checkString(objectCodes.optString("bank_name"))
                            swift = CommonUtility.checkString(objectCodes.optString("swift"))
                            accountNumber = CommonUtility.checkString(objectCodes.optString("account_number"))
                            image = CommonUtility.checkString(objectCodes.optString("image"))
                            isSelected = false
                        }

                        modelArrayList.add(model)
                    }

                    setNEFTBanKDetails()
                    Log.e("modelArrayList",".......595.."+modelArrayList.size)
                    val layoutManager = LinearLayoutManager(context)
                    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                    binding.paymentOption.recyclerView.layoutManager = layoutManager
                    binding.paymentOption.recyclerView.setNestedScrollingEnabled(false)
                    adapter = BankNEFTListAdapter(modelArrayList, context, this)
                    binding.paymentOption.recyclerView.adapter = adapter

                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }

            ApiConstants.PHONE_PE_PAYMENT_OPTION_ID -> {
                if (jsonObjectData.optString("status").equals("1", ignoreCase = true)) {
                    val jObjDetails = jsonObjectData.optJSONObject("details")
                    val jObjNetBankingDetails = jObjDetails?.optJSONObject("netBanking")

                    // For Popular Bank
                    val popularBanks = jObjNetBankingDetails?.getJSONArray("popularBanks")

                    for (i in 0 until (popularBanks?.length() ?: 0)) {
                        val objectCodes = popularBanks?.getJSONObject(i)

                        val model = BankNEFTListModel().apply {
                            bankId = CommonUtility.checkString(objectCodes?.optString("bankId"))
                            bankName = CommonUtility.checkString(objectCodes?.optString("bankName"))
                            available = CommonUtility.checkString(objectCodes?.optString("available"))
                            accountConstraintSupported = CommonUtility.checkString(objectCodes?.optString("accountConstraintSupported"))
                            priority = CommonUtility.checkString(objectCodes?.optString("priority"))
                            image = CommonUtility.checkString(objectCodes?.optString("img"))
                            isSelected = false
                        }

                        popularBankArrayList.add(model)
                    }

                    // For all Banks
                    val allBanks = jObjNetBankingDetails?.getJSONArray("allBanks")
                    for (i in 0 until (allBanks?.length() ?: 0)) {
                        val objectCodes = allBanks?.getJSONObject(i)

                        val model = BankNEFTListModel().apply {
                            bankId = CommonUtility.checkString(objectCodes?.optString("bankId"))
                            bankName = CommonUtility.checkString(objectCodes?.optString("bankName"))
                            available = CommonUtility.checkString(objectCodes?.optString("available"))
                            accountConstraintSupported = CommonUtility.checkString(objectCodes?.optString("accountConstraintSupported"))
                            priority = CommonUtility.checkString(objectCodes?.optString("priority"))
                        }

                        allBankArrayList.add(model)
                    }
                    Log.e("allBankArrayList","....628............."+allBankArrayList.size)
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }

            ApiConstants.GET_BANK_CHARGES_ID -> {
                if (jsonObjectData.optString("status").equals("1", ignoreCase = true)) {
                    val jObjDetails = jsonObjectData.optJSONObject("details")

                    try {
                        val creditCardChargeValue = CommonUtility.checkString(jObjDetails?.optString("CreditCard"))
                        val netBankingChargeValue = CommonUtility.checkString(jObjDetails?.optString("NetBanking"))
                        val neftChargeValue = CommonUtility.checkString(jObjDetails?.optString("NEFT"))
                        val upiChargeValue = CommonUtility.checkString(jObjDetails?.optString("UPI"))

                        creditCardChargeValue?.takeIf { it.isNotEmpty() }?.let {
                            cardCharges = it.toDouble()
                        }
                        netBankingChargeValue?.takeIf { it.isNotEmpty() }?.let {
                            netBankingCharges = it.toDouble()
                        }
                        neftChargeValue?.takeIf { it.isNotEmpty() }?.let {
                            neftCharges = it.toDouble()
                        }
                        upiChargeValue?.takeIf { it.isNotEmpty() }?.let {
                            upiCharges = it.toDouble()
                        }
                    } catch (e: NumberFormatException) {
                        Log.e("ChargesError", "Invalid format for charges", e)
                    }
                } else {
                }
            }
            ApiConstants.DISMISS_ORDER_ID -> {
                if (jsonObjectData.optString("status").equals("1", ignoreCase = true)) {
                    val failstatus = CommonUtility.checkString(jsonObjectData?.optString("msg"))
                    Toast.makeText(this, failstatus, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun UploadAmountFiled()
    {
        val firstName = CommonUtility.getGlobalString(this, "login_first_name")
        val lastName = CommonUtility.getGlobalString(this, "login_last_name")
        val companyName = CommonUtility.getGlobalString(this, "login_company_name")
        userRole = CommonUtility.getGlobalString(this, "login_user_role")
        binding.nameEt.setText("$firstName $lastName")
        binding.companyNameEt.setText(companyName)
        binding.nameEt.isFocusable = false
        binding.companyNameEt.isEnabled = false
    }

    private fun getApiRequest(showLoader:Boolean)
    {
        val uuid = CommonUtility.getAndroidId(this)
        urlParameter = java.util.HashMap()
        urlParameter!!["sessionId"] = "" + uuid
        vollyApiActivity = VollyApiActivity(
            this,
            this,
            urlParameter,
            ApiConstants.APPLY_API_SOLUTION,
            ApiConstants.APPLY_API_SOLUTION_ID,
            showLoader,
            "POST"
        )
        ApiSolutionRegisterDialog(this)
    }

    fun getApiKEY(showLoader:Boolean)
    {
        val uuid = CommonUtility.getAndroidId(this)
        urlParameter = java.util.HashMap()
        urlParameter!!["sessionId"] = "" + uuid
        vollyApiActivity = VollyApiActivity(
            this,
            this,
            urlParameter,
            ApiConstants.GET_API_KEY,
            ApiConstants.GET_API_KEY_ID,
            showLoader,
            "GET"
        )

    }

    override fun getErrorResponce(error: String?, service_ID: Int) {
    }

    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.back_img -> {
                finish()
            }
            R.id.generate_api_button -> {
                getGenerateAPIKey(false)
            }
            R.id.copy_btn -> {
                copyTextToClipboard(binding.apikeyGenerateTv.text!!,this)
            }
            R.id.filter_img -> {
                FilterSearch(this,"")
            }
            R.id.upload_amount_li -> {
                updateUI(
                    activeView = binding.uploadAmountLayout,
                    activeTextView = binding.uploadTv,
                    activeLayout = binding.uploadAmountLi,
                    inactiveTextViews = listOf(binding.apiKeyTv, binding.accountTv),
                    inactiveLayouts = listOf(binding.apiKeyLi, binding.accountBalanceLi),
                    showBottomLayout = false
                )
            }
            R.id.apiKey_li -> {
                updateUI(
                    activeView = binding.apiKeyLayout,
                    activeTextView = binding.apiKeyTv,
                    activeLayout = binding.apiKeyLi,
                    inactiveTextViews = listOf(binding.uploadTv, binding.accountTv),
                    inactiveLayouts = listOf(binding.uploadAmountLi, binding.accountBalanceLi),
                    showBottomLayout = false
                )

            }
            R.id.account_balance_li -> {
                updateUI(
                    activeView = binding.accountBalanceBottomLayout,
                    activeTextView = binding.accountTv,
                    activeLayout = binding.accountBalanceLi,
                    inactiveTextViews = listOf(binding.uploadTv, binding.apiKeyTv),
                    inactiveLayouts = listOf(binding.uploadAmountLi, binding.apiKeyLi),
                    showBottomLayout = true
                )
            }
            R.id.upi_rela -> {
                Utils.hideKeyboard(activity)

                UPISelected()
            }
            R.id.rtgs_rel -> {
                Utils.hideKeyboard(this)
                neftSelected()
            }
            R.id.card_type_rel -> {
                Utils.hideKeyboard(this)
                creditCardSelected()
            }
            R.id.net_banking_rel -> {
                Utils.hideKeyboard(this)
                netBankingSelected()
            }
            R.id.mode_payment_rel -> {
                Utils.hideKeyboard(this)
                paymentModeTypePopupWindow()
            }
            R.id.select_date_rel -> {
                Utils.hideKeyboard(this)
                dob = binding.paymentOption.selectDateTv.text.toString().trim { it <= ' ' }
                //CommonUtility.datePicker(context, CustomPaymentScreenActivity.this, dob, "dob", 0, System.currentTimeMillis());
                CommonUtility.datePicker(
                    context,
                    this@ApiSolutionRequestActivity,
                    dob,
                    "dob",
                    0,
                    0
                )
                //paymentModeTypePopupWindow()
            }
            R.id.show_all_bank_rel -> {
                Log.e("Call...","946...########### api solution......")
                Utils.hideKeyboard(this)
                showAllBankNamePopupWindow()
            }
            R.id.custom_payment_tv ->{
                Utils.hideKeyboard(this)
            }
            R.id.copy_img -> {
                copyTextToClipboard(binding.apikeyGenerateTv.text!!,this)
            }
            R.id.proceed_to_pay_rel ->{
                Utils.hideKeyboard(this)
                Log.e("paymentModeType","$$$$$$$$............1161...."+validateFields())

                if (validateFields()) {

                    if (paymentModeType.equals(NEFT, ignoreCase = true))
                    {
                        finalSubmitValue = "yes"
                        getCustomPaymentAPI(false, "1")
                    } else {
                        finalSubmitValue = "yes"
                        getCustomPaymentAPI(false, "1")
                    }
                } else {
                }
            }
            R.id.india_layout -> {
                RegionType = "IND"
                binding.paymentOption.indiaLayout.background =
                    ContextCompat.getDrawable(this, R.drawable.background_button_shadow)
                binding.paymentOption.indiaText.setTextColor(
                    ContextCompat.getColor(this, R.color.white)
                )
                binding.paymentOption.internationalText.setTextColor(
                    ContextCompat.getColor(this, R.color.purple_light)
                )
                binding.paymentOption.internationalLayout.background =
                    ContextCompat.getDrawable(this, R.drawable.backgroud_with_border)

                binding.paymentOption.paymentImage.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.razorpay_laog)
                )
                binding.paymentOption.netBankingMainLin.visibility =(View.VISIBLE)

                getDXEBankDetailsAPI(true,"IND")
                if (!binding.amountEt.text.toString().equals("", ignoreCase = true)) {
                    val enterAmount: Int = binding.amountEt.text.toString().trim { it <= ' ' }.toInt()
                    if (enterAmount > 500000) {
                        binding.paymentOption.upiMainLin.visibility = View.GONE
                    } else
                    {
                        if (RegionType.equals("IND", ignoreCase = true)) {
                            binding.paymentOption.upiMainLin.visibility = View.VISIBLE
                        } else if (RegionType.equals("GCC", ignoreCase = true)) {
                            binding.paymentOption.upiMainLin.visibility = View.GONE
                        }
                        binding.paymentOption.upiOptionLin.visibility = View.GONE
                    }
                } else {
                    binding.paymentOption.upiMainLin.visibility = View.VISIBLE
                }
            }
            R.id.international_layout -> {
                RegionType = "GCC"
                binding.paymentOption.internationalLayout.background =
                    ContextCompat.getDrawable(this, R.drawable.background_button_shadow)
                binding.paymentOption.indiaLayout.background =
                    ContextCompat.getDrawable(this, R.drawable.backgroud_with_border)
                binding.paymentOption.indiaText.setTextColor(
                    ContextCompat.getColor(this, R.color.purple_light)
                )
                binding.paymentOption.internationalText.setTextColor(
                    ContextCompat.getColor(this, R.color.white)
                )
                binding.paymentOption.paymentImage.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.paytabs_logo)
                )
                binding.paymentOption.netBankingMainLin.visibility =(View.INVISIBLE)
                binding.paymentOption.upiMainLin.visibility =(View.GONE)
                getDXEBankDetailsAPI(true, "GCC")
            }
        }
    }

    private fun UPISelected() {
        binding.paymentOption.rtgsRel.setBackgroundResource(R.drawable.background_payment_option)
        binding.paymentOption.cardTypeRel.setBackgroundResource(R.drawable.background_payment_option)
        binding.paymentOption.netBankingRel.setBackgroundResource(R.drawable.background_payment_option)
        binding.paymentOption.upiRela.setBackgroundResource(R.drawable.background_select_payment_option)
        binding.paymentOption.rtgsTv.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.paymentOption.cardTypeTv.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.paymentOption.netBankingTv.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.paymentOption.upiTv.setTextColor(ContextCompat.getColor(this, R.color.purple_light))
        editTextAndTextViewValueBlank()
        binding.paymentOption.paymentOptionErrorTv.visibility = View.GONE
        binding.paymentOption.showUpiList.visibility = View.VISIBLE
        binding.paymentOption.recyclerViewUpi.visibility = View.VISIBLE
        selectPaymentOptionClearUPIOption(UPI)

        //  showUPIAppsOption(upiAppsArrayList)

        editTextAndTextViewValueBlank()
        paymentModeType = UPI
        binding.paymentOption.recyclerView.visibility = View.GONE
        binding.paymentOption.selectBankErrorTv.visibility = View.GONE

        isSelectNetBankingBank = false

        // Amount Calculated Final Amount and Show Amount in Bottom of Screen

        calculateAmountWithCharges()

        binding.paymentOption.showBankNameLin.visibility = View.GONE
        binding.paymentOption.bankAccountDetailsLin.visibility = View.GONE
        binding.paymentOption.showAllBankLin.visibility = View.GONE
    }

    private fun paymentModeTypePopupWindow(): PopupWindow? {
        var mDropdown: PopupWindow? = null
        try {
            if (mDropdown?.isShowing == true) {
                mDropdown.dismiss()
            }

            val mInflater = getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout = mInflater.inflate(R.layout.custom_menu_payment_mode_type, null)

            val selectModeLbl = layout.findViewById<TextView>(R.id.select_mode_lbl)
            val chequeModeTv = layout.findViewById<TextView>(R.id.cheque_mode_tv)
            val neftModeTv = layout.findViewById<TextView>(R.id.neft_mode_tv)
            val rgtsModeTv = layout.findViewById<TextView>(R.id.rgts_mode_tv)
            val wireTransferModeTv = layout.findViewById<TextView>(R.id.wire_transfer_mode_tv)
            val othersModeTv = layout.findViewById<TextView>(R.id.others_mode_tv)

            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

            mDropdown = PopupWindow(
                layout,
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                true
            )

            binding.paymentOption.modePaymentRel?.let {
                mDropdown.showAsDropDown(it, 5, -50)
            } ?: Log.e("PopupWindow", "mode_payment_rel is null")


            chequeModeTv.setOnClickListener {
                binding.paymentOption.modePaymentTv.text = chequeModeTv.text.toString().trim()
                removeModePaymentError()
                mDropdown.dismiss()
            }

            neftModeTv.setOnClickListener {
                binding.paymentOption.modePaymentTv.text = neftModeTv.text.toString().trim()
                removeModePaymentError()
                mDropdown.dismiss()
            }

            rgtsModeTv.setOnClickListener {
                binding.paymentOption.modePaymentTv.text = rgtsModeTv.text.toString().trim()
                removeModePaymentError()
                mDropdown.dismiss()
            }

            wireTransferModeTv.setOnClickListener {
                binding.paymentOption.modePaymentTv.text = wireTransferModeTv.text.toString().trim()
                removeModePaymentError()
                mDropdown.dismiss()
            }

            othersModeTv.setOnClickListener {
                binding.paymentOption.modePaymentTv.text = othersModeTv.text.toString().trim()
                removeModePaymentError()
                mDropdown.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return mDropdown
    }

    private fun removeModePaymentError() {
        binding.paymentOption.paymentModeErrorTv.visibility=View.GONE
        binding.paymentOption.modePaymentRel.setBackgroundResource(R.drawable.border_line_view)
    }

    private fun validateFields(): Boolean {
        val name = binding.nameEt.text.toString().trim()
        val companyName = binding.companyNameEt.text.toString().trim()
        val amount = binding.amountEt.text.toString().trim()
        val remark = ""
        val modePayment = binding.paymentOption.modePaymentTv.text.toString().trim()
        val chequeNumber = binding.paymentOption.chequeEt.text.toString().trim()
        val chequeDate = binding.paymentOption.selectDateTv.text.toString().trim()

        if (name.isEmpty()) {
            binding.nameErrorTv.visibility = View.VISIBLE
            binding.nameEt.requestFocus()
            binding.nameEt.setBackgroundResource(R.drawable.border_red_line_view)
            return false
        } else if (companyName.isEmpty() && userRole != "BUYER") {
            binding.companyNameErrorTv.visibility = View.VISIBLE
            binding.companyNameEt.requestFocus()
            binding.companyNameEt.setBackgroundResource(R.drawable.border_red_line_view)
            return false
        } else if (amount.isEmpty()) {
            binding.amountErrorTv.setVisibility(View.VISIBLE)
            binding.amountEt.requestFocus()
            binding.amountEt.setBackgroundResource(R.drawable.border_red_line_view)
            return false
        } else if (paymentModeType.isEmpty()) {
            binding.paymentOption.paymentOptionErrorTv.visibility = View.VISIBLE
            binding.paymentOption.modePaymentRel.requestFocus()
            return false
        } else if (modePayment.isEmpty() && paymentModeType == NEFT) {
            binding.paymentOption.paymentModeErrorTv.visibility = View.VISIBLE
            binding.paymentOption.modePaymentRel.setBackgroundResource(R.drawable.border_red_line_view)
            return false
        } else if (chequeNumber.isEmpty() && paymentModeType == NEFT) {
            binding.paymentOption.chequeNoErrorTv.visibility = View.VISIBLE
            binding.paymentOption.chequeRel.requestFocus()
            binding.paymentOption.chequeRel.setBackgroundResource(R.drawable.border_red_line_view)
            return false
        } else if (chequeDate.isEmpty() && paymentModeType == NEFT) {
            binding.paymentOption.dateErrorTv.visibility = View.VISIBLE
            binding.paymentOption.selectDateRel.requestFocus()
            binding.paymentOption.selectDateRel.setBackgroundResource(R.drawable.border_red_line_view)
            return false
        } /*else if (!isSelectNetBankingBank && paymentModeType == NET_BANKING) {
            binding.paymentOption.selectBankErrorTv.visibility = View.VISIBLE
            binding.paymentOption.showAllBankLin.requestFocus()
            return false
        }*/
        return true
    }

    fun getCustomPaymentAPI(showLoader: Boolean, submit: String) {
        val uuid = CommonUtility.getAndroidId(context)
        if (Utils.isNetworkAvailable(context)) {


            Log.e("selectedUPIPackage",",1385...."+selectedUPIPackage)
            //this both use
            // "region" : "IND", // IND, GCC
            //  "paymentGateway" : "RAZORPAY", // PHONEPE,RAZORPAY,PAYTABS
            if (RegionType.equals("IND", ignoreCase = true)) {
                paymentGetwayType = "RAZORPAY"
            } else if (RegionType.equals("GCC", ignoreCase = true)) {
                paymentGetwayType = "PAYTABS"
            }
            val urlParameter = HashMap<String, String>()

            urlParameter["sessionId"] = uuid
            urlParameter["amount"] = binding.amountEt.text.toString().trim()
            urlParameter["paymentMode"] = paymentModeType

            // Check PaymentMode is NEFT then send if condition value
            if (paymentModeType.equals(NEFT, ignoreCase = true)) {
                urlParameter["bankPaymentMethod"] = binding.paymentOption.modePaymentTv.text.toString().trim()
                urlParameter["bankUTRNo"] = binding.paymentOption.chequeEt.text.toString().trim()
                urlParameter["bankPaymentDate"] = send_dob_server
                urlParameter["bankNeftId"] = bankID
            }
            else if (paymentModeType.equals(UPI))
            {
                urlParameter["UPI"] = "RAZORPAY"
            }
            /*Log.e("paymentModeType","...1426...."+paymentModeType)
            Log.e("RegionType","...1357...."+RegionType)
            Log.e("paymentGetwayType","...1357...."+paymentGetwayType)*/
            urlParameter["region"] = RegionType
            urlParameter["paymentGateway"] = paymentGetwayType

            var remark = ""
            if (remark.isNotEmpty()) {
                remark = remark.replace("\n", " ") // '\n' replaced
                // remark = remark.replaceAll("\\s+", " ") // Uncomment if needed
            }

            urlParameter["remark"] = remark
            urlParameter["submit"] = "true"

            val jsonObject = JSONObject(urlParameter as Map<*, *>)
            Log.e("URL Parameter JSON 1429..", jsonObject.toString())

            vollyApiActivity = VollyApiActivity(context, this, urlParameter, ApiConstants.SOLUTION_ACCOUNT_RECHARGE,
                ApiConstants.SOLUTION_ACCOUNT_RECHARGE_ID, showLoader, "POST")
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR)
        }
    }

    private fun showAllBankNamePopupWindow(): PopupWindow? {
        return try {
            mDropdown?.takeIf { it.isShowing }?.dismiss()

            mInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout = mInflater.inflate(R.layout.custom_menu_bank_name_list, null)
            val recyclerViewBankName: RecyclerView = layout.findViewById(R.id.recycler_view_bank_name)
            val selectBankLbl: TextView = layout.findViewById(R.id.select_bank_lbl)

            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

            // Initialize the PopupWindow
            mDropdown = PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, true)

            // Show the PopupWindow if 'mode_payment_rel' is not null
            if (binding.paymentOption.modePaymentRel != null) {
                mDropdown?.showAsDropDown(binding.paymentOption.showAllBankRel, 5, -50)
            } else {
                Log.e("PopupWindow", "mode_payment_rel is null")
            }

            selectBankLbl.setOnClickListener {
                // Add functionality if needed
            }

            val layoutManager = LinearLayoutManager(this)
            recyclerViewBankName.layoutManager = layoutManager
            recyclerViewBankName.isNestedScrollingEnabled = false
            val allBankNameListAdapter = AllBankNameListAdapter(allBankArrayList, context, this)
            recyclerViewBankName.adapter = allBankNameListAdapter

            mDropdown
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun copyTextToClipboard(textToCopy: CharSequence, context: Context) {
        if (textToCopy.isNotEmpty()) {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Copied Text", textToCopy)
            clipboardManager.setPrimaryClip(clipData)
        } else {
            Toast.makeText(context, "No text to copy", Toast.LENGTH_SHORT).show()
        }
    }

    fun editTextAndTextViewValueBlank() {
        binding.paymentOption.modePaymentTv.text = ""
        binding.paymentOption.modePaymentTv.hint = getString(R.string.select_payment_mode)

        binding.paymentOption.chequeEt.setText("")
        binding.paymentOption.chequeEt.hint = getString(R.string.enter_utr_cheque_no)

        binding.paymentOption.selectDateTv.text = ""
        binding.paymentOption.selectDateTv.hint = getString(R.string.select_date_month_year)
    }

    private fun updateUI(
        activeView: View,
        activeTextView: TextView,
        activeLayout: View,
        inactiveTextViews: List<TextView>,
        inactiveLayouts: List<View>,
        showBottomLayout: Boolean
    ) {
        binding.accountBalanceBottomLayout.visibility = if (showBottomLayout) View.VISIBLE else View.GONE
        binding.uploadAmountLayout.visibility = if (activeView == binding.uploadAmountLayout) View.VISIBLE else View.GONE
        binding.apiKeyLayout.visibility = if (activeView == binding.apiKeyLayout) View.VISIBLE else View.GONE
        binding.bottomBarRel.visibility = if (activeView == binding.uploadAmountLayout) View.VISIBLE else View.GONE

        activeTextView.setTextColor(resources.getColor(R.color.white))
        activeLayout.background =
            ContextCompat.getDrawable(this, R.drawable.background_button_shadow)

        inactiveTextViews.forEach { it.setTextColor(resources.getColor(R.color.purple_light)) }
        inactiveLayouts.forEach { it.background = resources.getDrawable(R.drawable.backgroud_with_border) }
    }

    override fun itemClick(position: Int, action: String)
    {
        when (action) {
            "selectBank" -> {
                val shouldSelect = !modelArrayList[position].isSelected

                for (i in modelArrayList.indices) {
                    modelArrayList[i].isSelected = false
                }
                modelArrayList[position].isSelected = shouldSelect
                lastPosition = position
                adapter.notifyDataSetChanged()
                setNEFTBanKDetails()
            }

            "popularSelectBank" -> {
                val shouldSelect = !popularBankArrayList[position].isSelected
                for (i in popularBankArrayList.indices) {
                    popularBankArrayList[i].isSelected = false
                }
                popularBankArrayList[position].isSelected = shouldSelect
                lastPosition = position

                val bankName = popularBankArrayList[position].bankName

                if (allBankArrayList != null && allBankArrayList.isNotEmpty()) {
                    for (i in allBankArrayList.indices) {
                        if (shouldSelect) {
                            if (bankName.equals(allBankArrayList[i].bankName, ignoreCase = true)) {
                                binding.paymentOption.selectBankTv.text = allBankArrayList[i].bankName // Set Bank Name
                                isSelectNetBankingBank = true
                                break
                            }
                        } else {
                            binding.paymentOption.selectDateTv.text = "" // Clear Bank Name
                            binding.paymentOption.selectDateTv.hint = getString(R.string.select_bank)
                            isSelectNetBankingBank = false
                        }
                    }
                }

                bankID = popularBankArrayList[position].bankId

                binding.paymentOption.selectBankErrorTv.visibility = View.GONE
                popularBankListAdapter.notifyDataSetChanged()
            }

            "selectAllBankName" -> {
                binding.paymentOption.selectBankTv.text = allBankArrayList[position].bankName
                val bankName = allBankArrayList[position].bankName
                if (popularBankArrayList != null && popularBankArrayList.isNotEmpty()) {
                    for (i in popularBankArrayList.indices) {
                        popularBankArrayList[i].isSelected = false
                    }
                    for (i in popularBankArrayList.indices) {
                        if (bankName.equals(popularBankArrayList[i].bankName, ignoreCase = true)) {
                            popularBankArrayList[i].isSelected = true
                            break
                        }
                    }
                }
                isSelectNetBankingBank = true
                bankID = allBankArrayList[position].bankId
                binding.paymentOption.selectBankErrorTv.visibility = View.GONE
                popularBankListAdapter.notifyDataSetChanged()
                mDropdown!!.dismiss()
            }

            "selectUPIOption" -> {
                binding.paymentOption.showAllBankLin.visibility = View.GONE
                binding.paymentOption.recyclerView.visibility = View.GONE
                binding.paymentOption.selectBankErrorTv.visibility = View.GONE
                binding.paymentOption.showAllBankLin.visibility = View.GONE
                val shouldSelect = !upiAppsArrayList[position]!!.isSelected
                for (i in upiAppsArrayList.indices) {
                    upiAppsArrayList[i]!!.isSelected = false
                }
                upiAppsArrayList[position]!!.isSelected = shouldSelect

                if (shouldSelect) {
                    paymentModeType = UPI
                    selectedUPIPackage = upiAppsArrayList[position]!!.packageName
                    for (i in modelArrayList.indices) {
                        modelArrayList[i].isSelected = false
                    }
                    for (i in popularBankArrayList.indices) {
                        popularBankArrayList[i].isSelected = false
                    }

                    binding.paymentOption.selectBankTv.text = ""
                    binding.paymentOption.selectBankTv.hint = getString(R.string.select_bank)
                    popularBankListAdapter?.notifyDataSetChanged()
                    adapter?.notifyDataSetChanged()
                } else {
                    paymentModeType = ""
                    selectedUPIPackage = ""
                }

                isSelectNetBankingBank = false
                binding.paymentOption.rtgsRel.setBackgroundResource(R.drawable.background_payment_option)
                binding.paymentOption.cardTypeRel.setBackgroundResource(R.drawable.background_payment_option)
                binding.paymentOption.netBankingRel.setBackgroundResource(R.drawable.background_payment_option)

                binding.paymentOption.rtgsTv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
                binding.paymentOption.cardTypeTv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
                binding.paymentOption.netBankingTv.setTextColor(ContextCompat.getColor(context!!, R.color.black))

                calculateAmountWithCharges()

                upiOptionListAdapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun ApiSolutionRegisterDialog(activity: Activity) {
        val dialogBuilder = AlertDialog.Builder(activity)
        val binding = ApiSolutionRequestDialogBinding.inflate(LayoutInflater.from(activity))
        dialogBuilder.setView(binding.root)
        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(true)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
        binding.requestNow.setOnClickListener{
            getApiRequest(false)

        }

    }

    private fun ApiSolutionRequestSendDialog(activity: Activity, msg:String) {
        val dialogBuilder = AlertDialog.Builder(activity)
        val binding =ApirequestSendDialogBinding.inflate(LayoutInflater.from(activity))
        dialogBuilder.setView(binding.root)
        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(true)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
        binding.apiMsg.text=msg
        binding.crossImg.setOnClickListener{
            alertDialog.dismiss()
        }
        binding.okButton.setOnClickListener {
            alertDialog.dismiss()
            finish()
        }

    }

    private lateinit var filterDialogBinding: ApisolutionFilterLayoutBinding

    private fun FilterSearch(activity: Activity, msg: String) {
        val dialogBuilder = AlertDialog.Builder(activity)

        filterDialogBinding = ApisolutionFilterLayoutBinding.inflate(LayoutInflater.from(activity))
        dialogBuilder.setView(filterDialogBinding.root)

        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(true)
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.show()

        filterDialogBinding.searchBtn.setOnClickListener {
            Log.e("startdate", "SEarch......." + filterDialogBinding.startdate.text)
            Log.e("enddate", "SEarch......." + filterDialogBinding.enddate.text)
            getAccountStatement(
                false,
                filterDialogBinding.startdate.text.toString(),
                filterDialogBinding.enddate.text.toString()
            )
            alertDialog.dismiss()
        }

        filterDialogBinding.startDateImg.setOnClickListener {
            Utils.hideKeyboard(activity)
            CommonUtility.datePicker1(
                activity,
                this,
                startDate,
                "startDate",
                0,
                System.currentTimeMillis()
            )
        }

        filterDialogBinding.refreshLv.setOnClickListener {
            filterDialogBinding.startdate.text=""
            filterDialogBinding.enddate.text=""
            filterDialogBinding.startdate.hint="DD/MM/YYYY"
            filterDialogBinding.enddate.hint="DD/MM/YYYY"
        }
        filterDialogBinding.endDateImg.setOnClickListener {
            Utils.hideKeyboard(activity)
            CommonUtility.datePicker1(
                activity,
                this,
                endDate,
                "endDate",
                0,
                System.currentTimeMillis()
            )
        }
    }

    override fun dialogItemClick(value: String?, action: String?)
    {
        Log.e("value","..407...."+value)
        if (!value.isNullOrEmpty()) {
            when (action?.lowercase(Locale.getDefault()))
            {
                "startdate" -> {
                    Log.e("value", "Start Date: $value")

                    filterDialogBinding.startdate.text = value
                    startDate = value
                }
                "enddate" -> {
                    Log.e("value", "End Date: $value")

                    filterDialogBinding.enddate.text = value
                    endDate = value
                }
                "dob" -> {
                    Log.e("value", "End Date: $value")

                    binding.paymentOption.selectDateTv.text = value
                    binding.paymentOption.selectDateRel.setBackgroundResource(R.drawable.border_line_view)
                    binding.paymentOption.dateErrorTv.visibility = View.GONE
                    send_dob_server = CommonUtility.convertDateFormat(value, ApiConstants.DATE_DOB_FORMAT, "dd/MM/yyyy")

                }
            }
        } else {
            Log.e("dialog Item Click", "Received null or empty date.")
        }

    }

    override fun onError(error: PaymentSdkError) {
        /*Toast.makeText(this, "Payment is Fail  1834: $error", Toast.LENGTH_SHORT)
            .show()*/
        Log.e("On payment", "error......2096...."+error)
    }

    override fun onPaymentCancel() {

        getPaymentCancel(false)
    }

    override fun onPaymentFinish(paymentSdkTransactionDetails: PaymentSdkTransactionDetails) {
        Log.e("On payment", "Finish......2096....")
        Log.e("CartAmount", "...2112..**...." + paymentSdkTransactionDetails.cartAmount)
        Log.e("TransactionType", "...2113..**...." + paymentSdkTransactionDetails.transactionType)
        Constant.comeFrom = "APISOLUTION"
        val intent = Intent(activity, PaymentStatusScreenActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    override fun onPaymentSuccess(s: String?, p1: PaymentData?) {
        Log.e("On payment", "succes......1872...."+s)
        Constant.comeFrom = "APISOLUTION"
        val intent = Intent(activity, PaymentStatusScreenActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()

    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Log.e("On payment", "error......1872...."+p1)
        getPaymentCancel(false)

    }
    private fun getPaymentCancel(showLoader:Boolean)
    {
        Log.e("paymentOrderID","...1871......"+ Constant.paymentOrderID)
        val uuid = CommonUtility.getAndroidId(this)
        urlParameter = java.util.HashMap()
        urlParameter!!["sessionId"] = "" + uuid
        urlParameter!!["orderId"] = "" +  Constant.paymentOrderID
        urlParameter!!["type"] = "account-recharge"
        vollyApiActivity = VollyApiActivity(
            this,
            this,
            urlParameter,
            ApiConstants.DISMISS_ORDER,
            ApiConstants.DISMISS_ORDER_ID,
            showLoader,
            "POST"
        )
    }

}
