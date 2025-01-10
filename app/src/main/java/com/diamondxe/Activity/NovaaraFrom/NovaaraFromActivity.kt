package com.diamondxe.Activity.NovaaraFrom

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diamondxe.Adapter.CityListAdapter
import com.diamondxe.Adapter.CountryListAdapter
import com.diamondxe.Adapter.StateListAdapter
import com.diamondxe.ApiCalling.ApiConstants
import com.diamondxe.ApiCalling.VollyApiActivity
import com.diamondxe.Beans.CountryListModel
import com.diamondxe.Interface.DialogItemClickInterface
import com.diamondxe.Interface.RecyclerInterface
import com.diamondxe.Network.SuperActivity
import com.diamondxe.R
import com.diamondxe.Utils.CommonUtility
import com.diamondxe.Utils.Utils
import com.diamondxe.databinding.ActivityNovaaraFromBinding
import com.diamondxe.databinding.NovvaraSuccessDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import kotlinx.serialization.json.JsonArray
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

class NovaaraFromActivity : SuperActivity(), DialogItemClickInterface, RecyclerInterface {
    lateinit var binding: ActivityNovaaraFromBinding
    private var activity: Activity? = null
    private var context: Context? = null
    private var isApiCalling = false
    lateinit var dialog: BottomSheetDialog
    lateinit var stateListAdapter: StateListAdapter
    lateinit var cityListAdapter: CityListAdapter
    var stateArrayList: java.util.ArrayList<CountryListModel>? = null
    var countryArrayList: ArrayList<CountryListModel?>? = null
    private var urlParameter: java.util.HashMap<String, String>? = null
    var vollyApiActivity: VollyApiActivity? = null
    var cityArrayList: java.util.ArrayList<CountryListModel>? = null
    var dob = ""
    var countryCodeFor = ""
    var showCountryCode = ""
    var send_dob_server: String = ""
    var countryId = ""
    var stateCodeFor = ""
    var stateId = ""
    var cityCodeFor = ""
    var cityId = ""
    var whoSelectBusinessNatureType = ""
    var supplier_business_nature_lin: LinearLayout? = null
    var buyerPhCountryCode: String = "+91"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovaaraFromBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this.also { activity = it }
        cityArrayList = ArrayList()
        countryArrayList = ArrayList()
        stateArrayList = ArrayList()
        binding.countryCodeLin.setOnClickListener(this)
        binding.backImg.setOnClickListener(this)
        binding.countryLin.setOnClickListener(this)
        binding.bookAppointmentImg.setOnClickListener(this)
        binding.stateLin.setOnClickListener(this)
        binding.cityLin.setOnClickListener(this)
        binding.natureOfBuss.setOnClickListener(this)
        binding.natureImg.setOnClickListener(this)
        binding.createDemandBtn.setOnClickListener(this)

        binding.counImg.setOnClickListener(this)
        binding.countryTv.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun getSuccessResponce(jsonObject: JSONObject?, service_ID: Int) {

        Log.e("getSuccessResponce", "...105....@@@...." + jsonObject)
        val jsonObjectData = jsonObject!!
        val message = jsonObjectData.optString("msg")
        when (service_ID) {
            ApiConstants.GET_COUNTRY_LIST_ID -> {
                isApiCalling = false
                if (jsonObjectData.optString("status").equals("1", ignoreCase = true)) {
                    val details = jsonObjectData.getJSONArray("details")

                    if (countryArrayList!!.size > 0) {
                        countryArrayList!!.clear()
                    } else {
                    }

                    for (i in 0 until details.length()) {
                        val objectCodes = details.getJSONObject(i)

                        val model = CountryListModel()
                        model.id = CommonUtility.checkString(objectCodes.optString("id"))
                        model.title = CommonUtility.checkString(objectCodes.optString("name"))
                        model.countryCode =
                            CommonUtility.checkString(objectCodes.optString("country_code"))
                        model.phoneCode =
                            CommonUtility.checkString(objectCodes.optString("phonecode"))
                        model.image = CommonUtility.checkString(objectCodes.optString("flag"))
                        countryArrayList!!.add(model)
                    }

                    showCountryCodeList()
                } else if (jsonObjectData.optString("status").equals("0", ignoreCase = true)) {
                    Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show()
                } else if (jsonObjectData.optString("status").equals("4", ignoreCase = true)) {
                    Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show()
                }

            }

            ApiConstants.GET_STATE_LIST_ID -> {
                isApiCalling = false
                if (jsonObjectData.optString("status").equals("1", ignoreCase = true)) {
                    val details = jsonObjectData.getJSONArray("details")

                    if (stateArrayList!!.size > 0) {
                        stateArrayList!!.clear()
                    } else {
                    }

                    for (i in 0 until details.length()) {
                        val objectCodes = details.getJSONObject(i)

                        val model = CountryListModel()
                        model.id = CommonUtility.checkString(objectCodes.optString("id"))
                        model.title = CommonUtility.checkString(objectCodes.optString("name"))
                        stateArrayList!!.add(model)
                    }

                    showStateList()
                } else if (jsonObjectData.optString("status").equals("0", ignoreCase = true)) {
                    Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show()
                } else if (jsonObjectData.optString("status").equals("4", ignoreCase = true)) {
                    Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show()
                }
            }

            ApiConstants.GET_CITY_LIST_ID -> {
                isApiCalling = false
                if (jsonObjectData.optString("status").equals("1", ignoreCase = true)) {
                    val details = jsonObjectData.getJSONArray("details")

                    if (cityArrayList!!.size > 0) {
                        cityArrayList!!.clear()
                    }
                    for (i in 0 until details.length()) {
                        val objectCodes = details.getJSONObject(i)

                        val model = CountryListModel()
                        model.id = CommonUtility.checkString(objectCodes.optString("id"))
                        model.title = CommonUtility.checkString(objectCodes.optString("name"))
                        cityArrayList!!.add(model)
                    }

                    showCityList()
                } else if (jsonObjectData.optString("status").equals("0", ignoreCase = true)) {
                    Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show()
                } else if (jsonObjectData.optString("status").equals("4", ignoreCase = true)) {
                    Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show()
                }
            }

            ApiConstants.GET_NOVVARA_FROM_ID -> {
                if (jsonObjectData.optString("status") == "1") {
                    CreateNovvaraSuccessDialog(this)
                } else if (jsonObjectData.optString("status") == "0") {
                    val msg = jsonObjectData.getString("msg")

                    println("Message: $msg")
                    val errors: JSONArray = jsonObjectData.getJSONArray("errors")
                    println("Errors:")
                    for (i in 0 until errors.length()) {
                        println("- ${errors[i].toString()}")
                        Toast.makeText(activity, "" + errors[i], Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    private fun showCityList() {
        dialog = BottomSheetDialog(context!!, R.style.BottomSheetDialog)
        dialog.setContentView(R.layout.dialog_show_country_code)

        val recyclerView: RecyclerView? = dialog.findViewById(R.id.recycler_view)
        val searchEt: EditText? = dialog.findViewById(R.id.search_et)
        val textView2: TextView? = dialog.findViewById(R.id.textView2)
        val ibCross: ImageView? = dialog.findViewById(R.id.ib_cross)

        textView2?.text = getString(R.string.city_list)

        recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            cityListAdapter = CityListAdapter(cityArrayList, context, this@NovaaraFromActivity)
            adapter = cityListAdapter
            cityListAdapter.notifyDataSetChanged()
        }

        searchEt?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    val text = searchEt.text.toString().lowercase(Locale.getDefault())
                    val textLength = searchEt.text.length
                    cityListAdapter.filter(text, textLength)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        ibCross?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.window?.let { window ->
            val lp = WindowManager.LayoutParams().apply {
                copyFrom(window.attributes)
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = 1000
            }
            window.attributes = lp
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        }
        dialog.show()
    }


    override fun getErrorResponce(error: String?, service_ID: Int) {

    }

    @SuppressLint("NewApi")
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.back_img -> {
                finish()
            }

            R.id.country_code_lin -> {
                Utils.hideKeyboard(activity)
                countryCodeFor = "countryCodeBuyerSection"
                showCountryCode = "yes"
                getCountryListAPI(false)
            }

            R.id.state_lin -> {
                if (!binding.countryTv.text.toString().trim().equals("", ignoreCase = true)) {
                    if (!isApiCalling) {
                        isApiCalling = true
                        Utils.hideKeyboard(activity)
                        stateCodeFor = ""
                        getStateListAPI(false)
                    }
                } else {
                    binding.countryErrorTv.visibility = View.VISIBLE
                    binding.countryLin.setBackgroundResource(R.drawable.border_red_line_view)
                }

            }

            R.id.book_appointment_img -> {
                Utils.hideKeyboard(this)
                dob = binding.bookAppointmentTv.text.toString().trim { it <= ' ' }
                CommonUtility.datePicker(
                    context,
                    this@NovaaraFromActivity,
                    dob,
                    "dob",
                    0,
                    0
                )
            }

            R.id.coun_img -> {
                isApiCalling = true
                Utils.hideKeyboard(activity)
                countryCodeFor = ""
                showCountryCode = ""
                getCountryListAPI(false)

            }

            R.id.country_tv -> {
                isApiCalling = true
                Utils.hideKeyboard(activity)
                countryCodeFor = ""
                showCountryCode = ""
                getCountryListAPI(false)

            }

            R.id.country_lin -> {
                isApiCalling = true
                Utils.hideKeyboard(activity)
                countryCodeFor = ""
                showCountryCode = ""
                getCountryListAPI(false)

            }

            R.id.city_lin -> {
                Utils.hideKeyboard(activity)
                if (!binding.stateTv.text.toString().trim { it <= ' ' }
                        .equals("", ignoreCase = true)) {
                    if (!isApiCalling) {
                        isApiCalling = true
                        Utils.hideKeyboard(activity)
                        cityCodeFor = ""
                        getCityListAPI(false)
                    } else {
                    }
                } else {
                    binding.stateErrorTv.visibility = View.VISIBLE
                    binding.stateLin.setBackgroundResource(R.drawable.border_red_line_view)
                }
            }

            R.id.create_demand_btn -> {

                //Toast.makeText(activity, "Submit..", Toast.LENGTH_SHORT).show()
                /*Log.e("nameEt","..${binding.nameEt.text}")
                Log.e("mobileNumberEt","..${binding.mobileNumberEt.text}")
                Log.e("emailEt","..${binding.emailEt.text}")
                Log.e("remarkEt","..${binding.remarkEt.text}")
                Log.e("businessNatureTv","..${binding.businessNatureTv.text}")
                Log.e("bookAppointmentTv","..${send_dob_server}")
                Log.e("pincodeEt","..${binding.pincodeEt.text}")
                Log.e("cityTv","..${binding.cityTv.text}....${cityId}")
                Log.e("stateTv","..${binding.stateTv.text}...${stateId}")
                Log.e("countryTv","..${binding.countryTv.text}....${countryId}")*/
                if (validateFields()) {
                    getNovvaraFrom(false)
                }

            }

            R.id.nature_of_buss -> {
                initiateBusinessTypePopupWindow()
            }

            R.id.nature_img -> {

                initiateBusinessTypePopupWindow()
            }

        }
    }

    fun getNovvaraFrom(showLoader: Boolean) {
        /*Log.e("nameEt","..${binding.nameEt.text}")
        Log.e("mobileNumberEt","..${binding.mobileNumberEt.text}")
        Log.e("emailEt","..${binding.emailEt.text}")
        Log.e("remarkEt","..${binding.remarkEt.text}")
        Log.e("businessNatureTv","..${binding.businessNatureTv.text}")
        Log.e("bookAppointmentTv","..${send_dob_server}")
        Log.e("pincodeEt","..${binding.pincodeEt.text}")
        Log.e("cityTv","..${binding.cityTv.text}....${cityId}")
        Log.e("stateTv","..${binding.stateTv.text}...${stateId}")
        Log.e("countryTv","..${binding.countryTv.text}....${countryId}")*/

        val uuid = CommonUtility.getAndroidId(this)
        urlParameter = java.util.HashMap()
        urlParameter!!["sessionId"] = "" + uuid
        urlParameter!!["name"] = "" + binding.nameEt.text
        urlParameter!!["emailId"] = "" + binding.emailEt.text
        urlParameter!!["mobileNo"] = buyerPhCountryCode + binding.mobileNumberEt.text
        urlParameter!!["country"] = "" + countryId
        urlParameter!!["state"] = "" + stateId
        urlParameter!!["city"] = "" + cityId
        urlParameter!!["pinCode"] = "" + binding.pincodeEt.text
        urlParameter!!["natureOfBusiness"] = "" + binding.businessNatureTv.text
        urlParameter!!["appointmentDate"] = "" + send_dob_server
        urlParameter!!["remark"] = "" + binding.remarkEt.text

        vollyApiActivity = VollyApiActivity(
            this,
            this,
            urlParameter,
            ApiConstants.GET_NOVVARA_FROM,
            ApiConstants.GET_NOVVARA_FROM_ID,
            showLoader,
            "POST"
        )

    }

    fun getStateListAPI(showLoader: Boolean) {
        if (Utils.isNetworkAvailable(context)) {
            urlParameter = java.util.HashMap()
            urlParameter!!["countryId"] = "" + countryId
            vollyApiActivity = null
            vollyApiActivity = VollyApiActivity(
                context,
                this,
                urlParameter,
                ApiConstants.GET_STATE_LIST,
                ApiConstants.GET_STATE_LIST_ID,
                showLoader,
                "POST"
            )
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR)
        }
    }

    private fun validateFields(): Boolean {
        val email = binding.emailEt.text.toString().trim()
        val name = binding.nameEt.text.toString().trim()
        val mobileNumber = binding.mobileNumberEt.text.toString().trim()
        val bookappointmenttv = binding.bookAppointmentTv.text.toString().trim()
        val businessNatureTv = binding.businessNatureTv.text.toString().trim()

        if (name.isEmpty()) {
            binding.nameRequiredErrorTv.visibility = View.VISIBLE
            binding.nameEt.setBackgroundResource(R.drawable.border_red_line_view)
            binding.nameEt.requestFocus()
            return false
        } else if (mobileNumber.isEmpty()) {
            binding.mobileNumberErrorTv.visibility = View.VISIBLE
            binding.mobileNumberLin.setBackgroundResource(R.drawable.border_red_line_view)
            binding.mobileNumberErrorTv.text = getString(R.string.phone_number_required)
            binding.mobileNumberLin.requestFocus()
            return false
        } else if (mobileNumber.length < 7 || mobileNumber.length > 14) {
            binding.mobileNumberErrorTv.visibility = View.VISIBLE
            binding.mobileNumberLin.setBackgroundResource(R.drawable.border_purple_line_view)
            binding.mobileNumberErrorTv.text = getString(R.string.phone_number_valid_msg)
            binding.mobileNumberLin.requestFocus()
            return false
        } else if (email.isEmpty()) {
            binding.emailRequiredErrorTv.visibility = View.VISIBLE
            binding.emailEt.setBackgroundResource(R.drawable.border_red_line_view)
            binding.emailRequiredErrorTv.text = getString(R.string.email_required)
            binding.emailEt.requestFocus()
            return false
        } else if (!Utils.emailValidator(email)) {
            binding.emailRequiredErrorTv.visibility = View.VISIBLE
            binding.emailEt.setBackgroundResource(R.drawable.border_red_line_view)
            binding.emailRequiredErrorTv.text = getString(R.string.email_valid_msg)
            binding.emailEt.requestFocus()
            return false
        } else if (countryId.isEmpty()) {
            binding.countryErrorTv.visibility = View.VISIBLE
            binding.countryLin.setBackgroundResource(R.drawable.border_red_line_view)
            binding.countryTv.requestFocus()
            return false
        } else if (stateId.isEmpty()) {
            binding.stateErrorTv.visibility = View.VISIBLE
            binding.stateLin.setBackgroundResource(R.drawable.border_red_line_view)
            binding.stateTv.requestFocus()
            return false
        } else if (cityId.isEmpty()) {
            binding.cityErrorTv.visibility = View.VISIBLE
            binding.cityLin.setBackgroundResource(R.drawable.border_red_line_view)
            binding.cityTv.requestFocus()
            return false
        } else if (cityId.isEmpty()) {
            binding.cityErrorTv.visibility = View.VISIBLE
            binding.cityLin.setBackgroundResource(R.drawable.border_red_line_view)
            binding.cityTv.requestFocus()
            return false
        } else if (bookappointmenttv.isEmpty()) {
            binding.bookAppointmentTvError.visibility = View.VISIBLE
            binding.bookAppointmentRv.setBackgroundResource(R.drawable.border_red_line_view)
            binding.bookAppointmentTvError.text =
                getString(R.string.book_your_appointment_required_nova)
            binding.bookAppointmentTv.requestFocus()
            return false
        } else if (businessNatureTv.isEmpty()) {
            binding.businessNatureErrorTv.visibility = View.VISIBLE
            binding.natureOfBuss.setBackgroundResource(R.drawable.border_red_line_view)
            binding.businessNatureTv.text = getString(R.string.phone_number_required)
            binding.businessNatureTv.requestFocus()
            return false
        }


        return true
    }

    private fun showCountryCodeList() {
        dialog = BottomSheetDialog(context!!, R.style.BottomSheetDialog).apply {
            setContentView(R.layout.dialog_show_country_code)
            setCancelable(false)
        }

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recycler_view)
        val searchEt = dialog.findViewById<EditText>(R.id.search_et)
        val textView2 = dialog.findViewById<TextView>(R.id.textView2)
        val ibCross = dialog.findViewById<ImageView>(R.id.ib_cross)

        textView2?.text = getString(R.string.country_list)

        recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = CountryListAdapter(
                countryArrayList,
                context,
                this@NovaaraFromActivity,
                showCountryCode
            ).also {
                it.notifyDataSetChanged()
            }
        }

        searchEt?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    val text = s.toString().lowercase(Locale.getDefault())
                    recyclerView?.adapter?.let { adapter ->
                        if (adapter is CountryListAdapter) {
                            adapter.filter(text, text.length)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        ibCross?.setOnClickListener { dialog.dismiss() }

        dialog.window?.let { window ->
            val layoutParams = WindowManager.LayoutParams().apply {
                copyFrom(window.attributes)
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = 1000
            }
            window.attributes = layoutParams
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        }

        dialog.show()
    }

    private fun showStateList() {
        dialog = BottomSheetDialog(context!!, R.style.BottomSheetDialog)
        dialog.setContentView(R.layout.dialog_show_country_code)

        val recyclerView: RecyclerView? = dialog.findViewById(R.id.recycler_view)
        val searchEt: EditText? = dialog.findViewById(R.id.search_et)
        val textView2: TextView? = dialog.findViewById(R.id.textView2)
        val ibCross: ImageView? = dialog.findViewById(R.id.ib_cross)

        textView2?.text = getString(R.string.state_list)

        recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            stateListAdapter = StateListAdapter(stateArrayList, context, this@NovaaraFromActivity)
            adapter = stateListAdapter
            stateListAdapter.notifyDataSetChanged()
        }

        searchEt?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    val text = searchEt.text.toString().lowercase(Locale.getDefault())
                    val textLength = searchEt.text.length
                    stateListAdapter.filter(text, textLength)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        ibCross?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.window?.let { window ->
            val lp = WindowManager.LayoutParams().apply {
                copyFrom(window.attributes)
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = 1000
            }
            window.attributes = lp
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        }
        dialog.show()
    }

    override fun dialogItemClick(value: String?, action: String?) {
        if (!value.isNullOrEmpty()) {
            when (action?.lowercase(Locale.getDefault())) {
                "dob" -> {
                    binding.bookAppointmentTv.text = value
                    //binding.bookAppointmentTv.selectDateRel.setBackgroundResource(R.drawable.border_line_view)
                    binding.bookAppointmentTvError.visibility = View.GONE
                    send_dob_server = CommonUtility.convertDateFormat1(
                        value,
                        ApiConstants.DATE_DOB_FORMAT,
                        "yyyy-MM-dd"
                    )
                    Log.e("send_dob_server", "End : $send_dob_server")

                }
            }
        }
    }

    private fun getCityListAPI(showLoader: Boolean) {
        if (Utils.isNetworkAvailable(context)) {
            urlParameter = java.util.HashMap()
            urlParameter!!["stateId"] = "" + stateId
            vollyApiActivity = null
            vollyApiActivity = VollyApiActivity(
                context,
                this,
                urlParameter,
                ApiConstants.GET_CITY_LIST,
                ApiConstants.GET_CITY_LIST_ID,
                showLoader,
                "POST"
            )
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR)
        }
    }

    fun getCountryListAPI(showLoader: Boolean) {
        if (Utils.isNetworkAvailable(context)) {
            urlParameter = HashMap<String, String>()
            vollyApiActivity = null
            vollyApiActivity = VollyApiActivity(
                context,
                this,
                urlParameter,
                ApiConstants.GET_COUNTRY_LIST,
                ApiConstants.GET_COUNTRY_LIST_ID,
                showLoader,
                "POST"
            )
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR)
        }
    }

    override fun itemClick(position: Int, action: String?) {
        Log.e("action", "..235....." + action)
        when (action) {
            "countryName" -> {
                if (countryCodeFor.equals("countryCodeBuyerSection", ignoreCase = true)) {
                    val model = countryArrayList!![position]!!
                    binding.countryCode.text = model.phoneCode

                    buyerPhCountryCode = model.phoneCode
                    Log.e("buyerPhCountryCode", "....997...$buyerPhCountryCode")
                    binding.countryCode.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.down_icon,
                        0
                    )
                    if (!model.image.equals("", ignoreCase = true)) {
                        Picasso.with(context)
                            .load(model.image)
                            .into(binding.countryImg)
                    } else {
                    }
                } else {
                    val model = countryArrayList?.get(position)
                    if (model != null) {
                        binding.countryErrorTv.visibility = View.GONE
                        binding.countryLin.setBackgroundResource(R.drawable.border_purple_line_view)

                        Log.e("country_lin", "304.........${model.id}........${model.title}")
                        countryId = model.id
                        binding.countryTv.text = model.title

                    } else {
                        Log.e("CountryError", "Model is null for position $position")
                    }
                }

                dialog.dismiss()

            }

            "stateName" -> {
                binding.stateErrorTv.visibility = View.GONE
                binding.stateLin.setBackgroundResource(R.drawable.border_purple_line_view)

                val model = stateArrayList!![position]
                Log.e("State", "659.........${model.id}........${model.title}")
                stateId = model.id
                binding.stateTv.text = model.title

                dialog.dismiss()
            }

            "cityName" -> {
                binding.cityErrorTv.visibility = View.GONE
                binding.cityLin.setBackgroundResource(R.drawable.border_purple_line_view)

                val model = cityArrayList!![position]
                Log.e("cityname", "673.........${model.id}........${model.title}")
                cityId = model.id
                binding.cityTv.text = model.title

                dialog.cancel()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun CreateNovvaraSuccessDialog(activity: Activity) {
        val dialogBuilder = AlertDialog.Builder(activity)
        val binding = NovvaraSuccessDialogBinding.inflate(LayoutInflater.from(activity))
        dialogBuilder.setView(binding.root)
        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(true)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()

        val message = activity.getString(R.string.inquiry_submitted_thankyou)

        val spannable = SpannableString(message)

        val email = "franchise@diamondxe.com"
        val emailStartIndex = message.indexOf(email)
        val emailEndIndex = emailStartIndex + email.length

        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
                activity.startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.TRANSPARENT
                ds.isUnderlineText = true
            }
        }, emailStartIndex, emailEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannable.setSpan(
            ForegroundColorSpan(Color.BLACK),
            emailStartIndex,
            emailEndIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.apiMsg.text = spannable
        binding.apiMsg.movementMethod = LinkMovementMethod.getInstance()

        binding.crossImg.setOnClickListener {
            alertDialog.dismiss()
            finish()
        }

        // Okay button functionality
        binding.okayTv.setOnClickListener {
            alertDialog.dismiss()
            finish()

        }
    }

    private var mDropdown: PopupWindow? = null
    var mInflater: LayoutInflater? = null
    private fun initiateBusinessTypePopupWindow(): PopupWindow? {
        try {
            if (mDropdown != null && mDropdown!!.isShowing) {
                mDropdown!!.dismiss()
            }
            mInflater =
                applicationContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout = mInflater!!.inflate(R.layout.custom_menu_sign_business_type, null)

            isApiCalling = false

            val manufacture_tv = layout.findViewById<TextView>(R.id.manufacture_tv)
            val rerailer_tv = layout.findViewById<TextView>(R.id.rerailer_tv)
            val wholesaler_tv = layout.findViewById<TextView>(R.id.wholesaler_tv)
            val trader_tv = layout.findViewById<TextView>(R.id.trader_tv)
            val jeweller_tv = layout.findViewById<TextView>(R.id.jeweller_tv)
            val others_tv = layout.findViewById<TextView>(R.id.others_tv)

            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

            mDropdown = if (whoSelectBusinessNatureType.equals("supplier", ignoreCase = true)) {
                PopupWindow(
                    layout,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    true
                )
            } else {
                PopupWindow(
                    layout,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    true
                )
            }

            mDropdown!!.showAsDropDown(binding.natureOfBuss, 0, -70)

            manufacture_tv.setOnClickListener {
                binding.businessNatureTv.text = manufacture_tv.text.toString().trim { it <= ' ' }
                removeBusinessSelectError()
                mDropdown!!.dismiss()
            }
            rerailer_tv.setOnClickListener {
                binding.businessNatureTv.text = rerailer_tv.text.toString().trim { it <= ' ' }
                removeBusinessSelectError()
                mDropdown!!.dismiss()
            }

            wholesaler_tv.setOnClickListener {
                binding.businessNatureTv.text = wholesaler_tv.text.toString().trim { it <= ' ' }
                removeBusinessSelectError()
                mDropdown!!.dismiss()
            }

            trader_tv.setOnClickListener {
                binding.businessNatureTv.text = trader_tv.text.toString().trim { it <= ' ' }
                removeBusinessSelectError()
                mDropdown!!.dismiss()
            }

            jeweller_tv.setOnClickListener {
                binding.businessNatureTv.text = jeweller_tv.text.toString().trim { it <= ' ' }
                removeBusinessSelectError()
                mDropdown!!.dismiss()
            }

            others_tv.setOnClickListener {
                binding.businessNatureTv.text = others_tv.text.toString().trim { it <= ' ' }
                removeBusinessSelectError()
                mDropdown!!.dismiss()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return mDropdown
    }

    private fun removeBusinessSelectError() {
        binding.natureOfBuss.setBackgroundResource(R.drawable.border_purple_line_view)
        binding.businessNatureErrorTv.visibility = View.GONE
    }


}