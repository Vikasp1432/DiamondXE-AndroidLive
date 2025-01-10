package com.diamondxe.Activity.CreateDemand

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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diamondxe.Activity.HomeScreenActivity
import com.diamondxe.Adapter.CertificateTypeListAdapter
import com.diamondxe.Adapter.ClarityTypeListAdapter
import com.diamondxe.Adapter.ColorTypeListAdapter
import com.diamondxe.Adapter.CountryListAdapter
import com.diamondxe.Adapter.CurrencyListAdapter
import com.diamondxe.Adapter.FancyColorTypeListAdapter
import com.diamondxe.Adapter.FluoRescenceTypeListAdapter
import com.diamondxe.Adapter.MakeTypeListAdapter
import com.diamondxe.Adapter.ShapeImageListAdapter
import com.diamondxe.ApiCalling.ApiConstants
import com.diamondxe.ApiCalling.ApiConstants.INDIA_CURRENCY_CODE
import com.diamondxe.ApiCalling.ApiConstants.INDIA_CURRENCY_VALUE
import com.diamondxe.ApiCalling.VollyApiActivity
import com.diamondxe.Beans.AttributeDetailsModel
import com.diamondxe.Beans.CountryListModel
import com.diamondxe.Beans.ShapeImageModel
import com.diamondxe.Interface.RecyclerInterface
import com.diamondxe.Network.SuperActivity
import com.diamondxe.R
import com.diamondxe.Utils.CommonUtility
import com.diamondxe.Utils.Constant
import com.diamondxe.Utils.Constant.makeSelectedValue
import com.diamondxe.Utils.Utils
import com.diamondxe.databinding.ActivityCreateDemandHomeBinding
import com.diamondxe.databinding.CreateDemandSuccessLayoutBinding
import com.diamondxe.databinding.DialogShowCountryCodeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.util.Locale

class CreateDemandHome : SuperActivity(), RecyclerInterface {

    lateinit var binding: ActivityCreateDemandHomeBinding
    lateinit var dialogBinding: DialogShowCountryCodeBinding
    private var urlParameter: HashMap<String, String>? = null
    lateinit var vollyApiActivity: VollyApiActivity
    private var activity: Activity? = null
    private var context: Context? = null
    val shapeImageArrayList = ArrayList<ShapeImageModel>()
    lateinit var shapeImageListAdapter: ShapeImageListAdapter
    lateinit var colorTypeListAdapter: ColorTypeListAdapter
    var colorTypeArrayList = ArrayList<AttributeDetailsModel>()
    var fancyColorTypeArrayList = ArrayList<AttributeDetailsModel>()
    var clarityTypeArrayList = ArrayList<AttributeDetailsModel>()
    var rescenceTypeModelArrayList = ArrayList<AttributeDetailsModel>()
    var makeTypeArrayList = ArrayList<AttributeDetailsModel>()

    val attributeDetailsModels = ArrayList<AttributeDetailsModel>()

    var countryCodeFor: String = ""
    var showCountryCode: String = ""
    var select_color_type: String = ""
    var LOW: String = "low"
    var WHITE: String = "white"
    var FANCY: String = "fancy"
    var clearAllClick: String = ""
    var buyerPhCountryCode: String = "+91"
    var countryArrayList: ArrayList<CountryListModel> = ArrayList()
    var isApiCalling: Boolean = false
    var fancyColorTypeListAdapter: FancyColorTypeListAdapter? = null
    var clarityTypeListAdapter: ClarityTypeListAdapter? = null
    var certificateTypeListAdapter: CertificateTypeListAdapter? = null
    var fluoRescenceTypeListAdapter: FluoRescenceTypeListAdapter? = null
    var makeTypeListAdapter: MakeTypeListAdapter? = null
    var certificateTypeArrayList= ArrayList<AttributeDetailsModel>()
    lateinit var currencyListAdapter: CurrencyListAdapter
    lateinit var dialog: BottomSheetDialog
    lateinit var countryListAdapter: CountryListAdapter
    lateinit var mInflater: LayoutInflater
    var getDiamondType: String = "Natural"
    var getSelectCurrencyType: String = "INR"
    var getPriceType: String = "Price / Carat"
    var getSelectNoPieces: String = "Total Pieces"
    var getSelectPriority: String = "Immediate (Within 3 days)"
    var getColorType: String = "white"

    private var mDropdown: PopupWindow? = null
    val priorityOptions by lazy {
        listOf(
            "Immediate (Within 3 days)",
            "Medium (3 - 7 Days)",
            "Normal (7 - 15 Days)",
            "Low (Within 1 Month)"
        )
    }
    val priceType by lazy {
        listOf(
            "Price / Carat",
            "Discount (Less)"
        )
    }
    val piecesType by lazy {
        listOf(
            "Total Pieces",
            "Total Carat",
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateDemandHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this.also { activity = it }
        val layoutManagerShapeView = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.recyclerShapeView.layoutManager = layoutManagerShapeView
        binding.recyclerShapeView.isNestedScrollingEnabled = false

        binding.recyclerColorView.setHasFixedSize(true)
        val layoutManagerColorView = LinearLayoutManager(activity).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.recyclerColorView.layoutManager = layoutManagerColorView
        binding.recyclerColorView.isNestedScrollingEnabled = false

        binding.recyclerClarityView.setHasFixedSize(true)
        val layoutManagerClarityView = LinearLayoutManager(activity)
        layoutManagerClarityView.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerClarityView.layoutManager = layoutManagerClarityView
        binding.recyclerClarityView.isNestedScrollingEnabled = false

        binding.recyclerCertificateView.setHasFixedSize(true)
        val layoutManagerCertificateView = LinearLayoutManager(activity)
        layoutManagerCertificateView.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerCertificateView.layoutManager = layoutManagerCertificateView
        binding.recyclerCertificateView.isNestedScrollingEnabled = false

        binding.recyclerFluorescenceView.setHasFixedSize(true)
        val layoutManagerFluorescenceView = LinearLayoutManager(activity)
        layoutManagerFluorescenceView.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerFluorescenceView.layoutManager = layoutManagerFluorescenceView
        binding.recyclerFluorescenceView.isNestedScrollingEnabled = false

        binding.recyclerMakeView.setHasFixedSize(true)
        val layoutManagerMakeView = LinearLayoutManager(activity)
        layoutManagerMakeView.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerMakeView.layoutManager = layoutManagerMakeView
        binding.recyclerMakeView.isNestedScrollingEnabled = false

        binding.currencyImgPrice.setOnClickListener(this)
        binding.dropdownCurrencyimg.setOnClickListener(this)
        binding.nopicesDropimg.setOnClickListener(this)
        binding.dropdownPricetype.setOnClickListener(this)
        binding.prioritySpinerimg.setOnClickListener(this)
        binding.backImg.setOnClickListener(this)
        binding.countryCodeLin.setOnClickListener(this)
        binding.currencyPriceLi.setOnClickListener(this)
        binding.cardViewNatural.setOnClickListener(this)
        binding.cardViewGrown.setOnClickListener(this)
        binding.createDemandBtn.setOnClickListener(this)
        binding.priorityRelative.setOnClickListener(this)
        binding.clearAllFilter.setOnClickListener(this)

        Constant.cutTypeArrayList = ArrayList()
        Constant.polishTypeArrayList = ArrayList()
        Constant.symmertyTypeArrayList = ArrayList()
        Constant.technologyTypeArrayList = ArrayList()
        Constant.fancyColorIntensityArrayList = ArrayList()
        Constant.fancyColorOverToneArrayList = ArrayList()
        Constant.tableDataPercantageArrayList = ArrayList()
        Constant.depthDataPercantageArrayList = ArrayList()
        Constant.crowmArrayList = ArrayList()
        Constant.pavillionArrayList = ArrayList()
        Constant.eyeCleanArrayList = ArrayList()
        Constant.shadeArrayList = ArrayList()
        Constant.lusterArrayList = ArrayList()
        Constant.colorTypeFilterApploedArrayList = ArrayList()

        Constant.colorType = WHITE
        GetShapesList()
        GetColorType()
        binding.colorRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.white_radio -> {
                    select_color_type = WHITE
                    Constant.colorType = WHITE
                    colorTypeArrayList.forEach { it.isSelected = false }
                    setWhiteColorAdapter()
                    getColorType = "white"
                    Constant.colorType = WHITE
                }

                R.id.fancy_radio -> {
                    select_color_type = FANCY
                    Constant.colorType = FANCY
                    fancyColorTypeArrayList.forEach { it.isSelected = false }
                    setFancyAdapter()
                    getColorType = "fancy"
                    Constant.colorType = FANCY
                }
            }
        }
        binding.emailEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!binding.emailEt.text.toString().equals("", ignoreCase = true)) {
                    binding.emailRequiredErrorTv.visibility = View.GONE
                }

                if (binding.emailEt.text.toString().equals("", ignoreCase = true)) {
                    binding.emailRequiredErrorTv.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.nameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!binding.nameEt.text.toString().equals("", ignoreCase = true)) {
                    binding.nameRequiredErrorTv.visibility = View.GONE
                }

                if (binding.nameEt.text.toString().equals("", ignoreCase = true)) {
                    binding.nameRequiredErrorTv.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.mobileNumberEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!binding.mobileNumberEt.text.toString().equals("", ignoreCase = true)) {
                    binding.mobileNumberErrorTv.visibility = View.GONE
                }

                if (binding.mobileNumberEt.text.toString().equals("", ignoreCase = true)) {
                    binding.mobileNumberErrorTv.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        OpenPriority()
        OpenPriceType()
        OpenPiecesType()

    }

    fun onBindDetails(showLoader: Boolean) {
        Log.e("onBindDetails", "............CALL......................")
        val uuid = CommonUtility.getAndroidId(context)

        if (Utils.isNetworkAvailable(context)) {
            val urlParameter = HashMap<String, String>().apply {
                put("sessionId", uuid)
                // put("user_id", CommonUtility.getGlobalString(activity, "user_id"))
                // put("authToken", CommonUtility.getGlobalString(context, "mobile_auth_token"))
            }

            vollyApiActivity = VollyApiActivity(
                context,
                this,
                urlParameter,
                ApiConstants.GET_ATTRIBUTES,
                ApiConstants.GET_ATTRIBUTES_ID,
                showLoader,
                "GET"
            )
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR)
            // recyclerNaturalGrownView.visibility = View.GONE
        }
    }


    private fun OpenPriority() {
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            priorityOptions
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.prioritySpinnerAttributes.adapter = adapter

        binding.prioritySpinnerAttributes.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedOption = parent.getItemAtPosition(position).toString()
                Log.d("SpinnerSelection", "Selected: $selectedOption")
                getSelectPriority = selectedOption
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })
    }

    private fun OpenPriceType() {
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            priceType
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerAttributesPrice.adapter = adapter

        binding.spinnerAttributesPrice.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedOption = parent.getItemAtPosition(position).toString()
                Log.d("SpinnerSelection", "Selected..Price Type: $selectedOption")
                getPriceType = selectedOption

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })
    }

    private fun OpenPiecesType() {
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            piecesType
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.nopicesSpinnerAttributes.adapter = adapter

        binding.nopicesSpinnerAttributes.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedOption = parent.getItemAtPosition(position).toString()
                Log.d("SpinnerSelection", "Selected..No pieces : $selectedOption")
                getSelectNoPieces = selectedOption
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })
    }

    private fun getCreateDemand(showLoader: Boolean) {
        val uuid = CommonUtility.getAndroidId(this)
        urlParameter = java.util.HashMap()
        urlParameter!!["sessionId"] = "" + uuid
        urlParameter!!["name"] = "" + binding.nameEt.text
        urlParameter!!["emailId"] = "" + binding.emailEt.text
        urlParameter!!["mobileNo"] = "" + binding.mobileNumberEt.text
        urlParameter!!["category"] = "" + getDiamondType
        urlParameter!!["shape"] = "" + getShapes
        urlParameter!!["priority"] = "" + getSelectPriority
        urlParameter!!["caretFrom"] = "" + binding.caratFromEt.text
        urlParameter!!["caretTo"] = "" + binding.caratToEt.text
        urlParameter!!["currency"] = "" + getSelectCurrencyType
        urlParameter!!["priceType"] = "" + getPriceType
        urlParameter!!["priceFrom"] = "" + binding.priceFrom.text
        urlParameter!!["priceTo"] = "" + binding.priceTo.text
        urlParameter!!["piecesType"] = "" + getSelectNoPieces
        urlParameter!!["pieces"] = "" + binding.nopiecesEt.text
        urlParameter!!["colorType"] = "" + getColorType
        urlParameter!!["color"] = "" + getColor
        urlParameter!!["fancyColor"] = "" + getFancyColor
        urlParameter!!["clarity"] = "" + getClarity
        urlParameter!!["certificate"] = "" + getCertificate
        urlParameter!!["fluorescence"] = "" + getFluorescence
        urlParameter!!["make"] = "" + getMakeItems
        urlParameter!!["instructions"] = "" + binding.instructionsEt.text

        Log.d("UrlParameterLog", urlParameter!!.entries.joinToString(
            prefix = "..", postfix = "..", separator = ", "
        ) { (key, value) -> "\"$key\": \"$value\"" })

        vollyApiActivity = VollyApiActivity(
            this,
            this,
            urlParameter,
            ApiConstants.CREATE_DEMAND,
            ApiConstants.CREATE_DEMAND_ID,
            showLoader,
            "POST"
        )
    }

    private fun GetColorType() {

        // Color Type
        val typeColor = object : TypeToken<ArrayList<AttributeDetailsModel>>() {}.type
        val retrieveColorTypeArrayList: ArrayList<AttributeDetailsModel>? =
            Gson().fromJson(CommonUtility.getLocalDataList(context, "colorArrayList"), typeColor)

        if (!retrieveColorTypeArrayList.isNullOrEmpty()) {
            colorTypeArrayList = retrieveColorTypeArrayList
            colorTypeListAdapter = ColorTypeListAdapter(colorTypeArrayList, context, this)
            binding.recyclerColorView.adapter = colorTypeListAdapter
        }

        // Fancy Color Type
        val typeFancyColor = object : TypeToken<ArrayList<AttributeDetailsModel>>() {}.type
        val retrieveFancyColorTypeArrayList: ArrayList<AttributeDetailsModel>? =
            Gson().fromJson(
                CommonUtility.getLocalDataList(context, "fancyColorArrayList"),
                typeFancyColor
            )

        if (!retrieveFancyColorTypeArrayList.isNullOrEmpty()) {
            fancyColorTypeArrayList = retrieveFancyColorTypeArrayList
        }

        // Clarity Type
        val typeClarity = object : TypeToken<ArrayList<AttributeDetailsModel>>() {}.type
        val retrievedClarityTypeArrayList: ArrayList<AttributeDetailsModel>? =
            Gson().fromJson(
                CommonUtility.getLocalDataList(context, "clarityArrayList"),
                typeClarity
            )

        if (!retrievedClarityTypeArrayList.isNullOrEmpty()) {
            clarityTypeArrayList = retrievedClarityTypeArrayList
            clarityTypeListAdapter = ClarityTypeListAdapter(clarityTypeArrayList, context, this)
            binding.recyclerClarityView.adapter = clarityTypeListAdapter
        }

        // Certificate Type
        val typeCertificate = object : TypeToken<ArrayList<AttributeDetailsModel>>() {}.type
        val retrievedCertificateTypeArrayList: ArrayList<AttributeDetailsModel>? =
            Gson().fromJson(
                CommonUtility.getLocalDataList(context, "certificateArrayList"),
                typeCertificate
            )

        if (!retrievedCertificateTypeArrayList.isNullOrEmpty()) {
            certificateTypeArrayList = retrievedCertificateTypeArrayList
            certificateTypeListAdapter =
                CertificateTypeListAdapter(certificateTypeArrayList, context, this)
            binding.recyclerCertificateView.adapter = certificateTypeListAdapter
        }

        // Fluorescence Type
        val typeFluo = object : TypeToken<ArrayList<AttributeDetailsModel>>() {}.type
        val retrievedRescenceTypeModelArrayList: ArrayList<AttributeDetailsModel>? =
            Gson().fromJson(
                CommonUtility.getLocalDataList(context, "rescenceModelArrayList"),
                typeFluo
            )

        if (!retrievedRescenceTypeModelArrayList.isNullOrEmpty()) {
            rescenceTypeModelArrayList = retrievedRescenceTypeModelArrayList
            fluoRescenceTypeListAdapter =
                FluoRescenceTypeListAdapter(rescenceTypeModelArrayList, context, this)
            binding.recyclerFluorescenceView.adapter = fluoRescenceTypeListAdapter
        }

        // Make Type
        val typeMake = object : TypeToken<ArrayList<AttributeDetailsModel>>() {}.type
        val retrievedMakeTypeArrayList: ArrayList<AttributeDetailsModel>? =
            Gson().fromJson(CommonUtility.getLocalDataList(context, "makeArrayList"), typeMake)

        if (!retrievedMakeTypeArrayList.isNullOrEmpty()) {
            makeTypeArrayList = retrievedMakeTypeArrayList
            makeTypeListAdapter = MakeTypeListAdapter(makeTypeArrayList, context, this)
            binding.recyclerMakeView.adapter = makeTypeListAdapter
        }

        if (
            !retrievedClarityTypeArrayList.isNullOrEmpty() &&
            !retrieveColorTypeArrayList.isNullOrEmpty() &&
            !retrieveFancyColorTypeArrayList.isNullOrEmpty() &&
            !retrievedCertificateTypeArrayList.isNullOrEmpty() &&
            !retrievedRescenceTypeModelArrayList.isNullOrEmpty() &&
            !retrievedMakeTypeArrayList.isNullOrEmpty())
            {


            // Your code here
        }
        else{
            onBindDetails(false)
        }

    }

    private fun setWhiteColorAdapter() {
        colorTypeListAdapter = ColorTypeListAdapter(colorTypeArrayList, context, this)
        binding.recyclerColorView.adapter = colorTypeListAdapter
        colorTypeListAdapter.notifyDataSetChanged()
    }

    private fun setFancyAdapter() {
        fancyColorTypeListAdapter =
            FancyColorTypeListAdapter(fancyColorTypeArrayList, context, this)
        binding.recyclerColorView.adapter = fancyColorTypeListAdapter
        fancyColorTypeListAdapter!!.notifyDataSetChanged()
    }

    private fun GetShapesList() {
        shapeImageArrayList.add(ShapeImageModel().apply {
            drawable = R.mipmap.all
            attribCode = "All"
            isSelected = false
            isFirstPosition = true
        })

        shapeImageArrayList.add(ShapeImageModel().apply {
            drawable = R.mipmap.round
            attribCode = "Round"
            isSelected = false
            isFirstPosition = false
        })

        shapeImageArrayList.add(ShapeImageModel().apply {
            drawable = R.mipmap.princess
            attribCode = "Princess"
            isSelected = false
            isFirstPosition = false
        })

        shapeImageArrayList.add(ShapeImageModel().apply {
            drawable = R.mipmap.emerald
            attribCode = "Emerald"
            isSelected = false
            isFirstPosition = false
        })

        shapeImageArrayList.add(ShapeImageModel().apply {
            drawable = R.mipmap.heart
            attribCode = "Heart"
            isSelected = false
            isFirstPosition = false
        })

        shapeImageArrayList.add(ShapeImageModel().apply {
            drawable = R.mipmap.radiant
            attribCode = "Radiant"
            isSelected = false
            isFirstPosition = false
        })

        shapeImageArrayList.add(ShapeImageModel().apply {
            drawable = R.mipmap.oval
            attribCode = "Oval"
            isSelected = false
            isFirstPosition = false
        })

        shapeImageArrayList.add(ShapeImageModel().apply {
            drawable = R.mipmap.pear
            attribCode = "Pear"
            isSelected = false
            isFirstPosition = false
        })

        shapeImageArrayList.add(ShapeImageModel().apply {
            drawable = R.mipmap.marquise
            attribCode = "Marquise"
            isSelected = false
            isFirstPosition = false
        })

        shapeImageArrayList.add(ShapeImageModel().apply {
            drawable = R.mipmap.asscher
            attribCode = "Asscher"
            isSelected = false
            isFirstPosition = false
        })

        shapeImageArrayList.add(ShapeImageModel().apply {
            drawable = R.mipmap.cushion
            attribCode = "Cushion"
            isSelected = false
            isFirstPosition = false
        })

        shapeImageArrayList.add(ShapeImageModel().apply {
            drawable = R.mipmap.others
            attribCode = "Other"
            isSelected = false
            isFirstPosition = false
        })

        shapeImageListAdapter = ShapeImageListAdapter(shapeImageArrayList, context, this)
        binding.recyclerShapeView.adapter = shapeImageListAdapter

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun getSuccessResponce(jsonObject: JSONObject?, service_ID: Int) {
        try {
            val jsonObjectData = jsonObject
            Log.e("jsonObjectData", "....507.." + jsonObjectData)
            val message = jsonObjectData!!.optString("msg")

            when (service_ID) {
                ApiConstants.VERIFY_EMAIL_ID -> {
                    Log.e("status", "142.....****.....${jsonObjectData!!.optString("status")}")
                    if (jsonObjectData.optString("status") == "1") {
                        // Handle VERIFY_EMAIL_ID success response
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }

                ApiConstants.GET_COUNTRY_LIST_ID -> {
                    isApiCalling = false
                    when (jsonObjectData.optString("status")) {
                        "1" -> {
                            val details = jsonObjectData.getJSONArray("details")

                            if (countryArrayList.isNotEmpty()) {
                                countryArrayList.clear()
                            }

                            for (i in 0 until details.length()) {
                                val objectCodes = details.getJSONObject(i)
                                val model = CountryListModel().apply {
                                    id = CommonUtility.checkString(objectCodes.optString("id"))
                                    title = CommonUtility.checkString(objectCodes.optString("name"))
                                    countryCode =
                                        CommonUtility.checkString(objectCodes.optString("country_code"))
                                    phoneCode =
                                        CommonUtility.checkString(objectCodes.optString("phonecode"))
                                    image = CommonUtility.checkString(objectCodes.optString("flag"))
                                }
                                countryArrayList.add(model)
                            }
                            Log.e("countryArrayList", "357.....${countryArrayList.size}")
                            showCountryCodeList()
                        }

                        "0", "4" -> {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                ApiConstants.CREATE_DEMAND_ID -> {
                    isApiCalling = false

                    val status =
                        jsonObjectData.optString("status") ?: "0" // Default to "0" if null
                    when (status) {
                        "1" -> {
                            CreateDemandSuccessDialog(this)
                        }

                        "0" -> {
                            // Check for errors array and display in Toast
                            val errorsArray = jsonObjectData.optJSONArray("errors")
                            if (errorsArray != null && errorsArray.length() > 0) {
                                // Concatenate all errors into a single message
                                val errorMessages = StringBuilder()
                                for (i in 0 until errorsArray.length()) {
                                    errorMessages.append(errorsArray.getString(i))
                                    if (i != errorsArray.length() - 1) {
                                        errorMessages.append("\n") // Add newline between errors
                                    }
                                }
                                Toast.makeText(this, errorMessages.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                // Fallback to the message field if no errors are present
                                Toast.makeText(
                                    this,
                                    jsonObjectData.optString("message", "Error"),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        "4" -> {
                            Toast.makeText(
                                this,
                                jsonObjectData.optString("message", "Error"),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            Toast.makeText(this, "Unexpected error occurred.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                ApiConstants.GET_ATTRIBUTES_ID -> {
                    if (jsonObjectData.optString("status").equals("1", ignoreCase = true)) {
                        val details = jsonObjectData.getJSONArray("details")

                        listOf(
                            colorTypeArrayList,
                            fancyColorTypeArrayList,
                            clarityTypeArrayList,
                            certificateTypeArrayList,
                            rescenceTypeModelArrayList,
                            makeTypeArrayList,
                            Constant.cutTypeArrayList,
                            Constant.polishTypeArrayList,
                            Constant.symmertyTypeArrayList,
                            Constant.technologyTypeArrayList,
                            Constant.fancyColorIntensityArrayList,
                            Constant.fancyColorOverToneArrayList,
                            Constant.tableDataPercantageArrayList,
                            Constant.depthDataPercantageArrayList,
                            Constant.crowmArrayList,
                            Constant.pavillionArrayList,
                            Constant.eyeCleanArrayList,
                            Constant.shadeArrayList,
                            Constant.lusterArrayList
                        ).forEach { list ->
                            list?.takeIf { it.isNotEmpty() }?.clear()
                        }

                        for (i in 0 until details.length()) {
                            attributeDetailsModels.clear()
                            val objectCode = details.getJSONObject(i)

                            var attribTypeString = objectCode.getString("AttribType")
                            val attribDetails = objectCode.getJSONArray("AttribDetails")

                            for (j in 0 until attribDetails.length()) {
                                val innerObjectCodes = attribDetails.getJSONObject(j)

                                val model = AttributeDetailsModel().apply {
                                    attribId = CommonUtility.checkString(innerObjectCodes.optString("AttribId"))
                                    attribTypeId = CommonUtility.checkString(innerObjectCodes.optString("AttribTypeId"))
                                    attribType = CommonUtility.checkString(innerObjectCodes.optString("AttribType"))
                                    attribCode = CommonUtility.checkString(innerObjectCodes.optString("AttribCode"))
                                    sortOrder = CommonUtility.checkString(innerObjectCodes.optString("SortOrder"))
                                    displayAttr = CommonUtility.checkString(innerObjectCodes.optString("DisplayAttr"))
                                    isFirstPosition = getFirstPosition(this, j)
                                    isSelected = false
                                }

                                setParsingData(model, attribTypeString)
                                attributeDetailsModels.add(model)
                            }
                        }
                    } else when (jsonObjectData.optString("status")) {
                        "0" -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        "4" -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }

                }
                // Add other service IDs if needed
                else -> {
                    Log.e("getSuccessResponse", "Unknown service ID: $service_ID")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    fun getFirstPosition(model: AttributeDetailsModel, index: Int): Boolean {
        return if (index == 0) {
            model.isFirstPosition = true
            true
        } else {
            model.isFirstPosition = false
            false
        }
    }

    fun setParsingData(model: AttributeDetailsModel, type: String) {
        when (type.toUpperCase()) {
            "STONE COLOR" -> {
                colorTypeArrayList.add(model)
                val json = Gson().toJson(colorTypeArrayList)
                CommonUtility.saveLocalDataList(context, "colorArrayList", json)

                colorTypeListAdapter = ColorTypeListAdapter(colorTypeArrayList, context, this)
                binding.recyclerColorView.adapter = colorTypeListAdapter
            }
            "FANCY COLOR" -> {
                fancyColorTypeArrayList.add(model)
                val json = Gson().toJson(fancyColorTypeArrayList)
                CommonUtility.saveLocalDataList(context, "fancyColorArrayList", json)

                fancyColorTypeListAdapter = FancyColorTypeListAdapter(fancyColorTypeArrayList, context, this)
                binding.recyclerColorView.adapter = fancyColorTypeListAdapter
            }
            "CLARITY" -> {
                clarityTypeArrayList.add(model)
                val json = Gson().toJson(clarityTypeArrayList)
                CommonUtility.saveLocalDataList(context, "clarityArrayList", json)

                clarityTypeListAdapter = ClarityTypeListAdapter(clarityTypeArrayList, context, this)
               binding.recyclerClarityView.adapter = clarityTypeListAdapter
            }
            "LAB NAME" -> {
                certificateTypeArrayList.add(model)
                val json = Gson().toJson(certificateTypeArrayList)
                CommonUtility.saveLocalDataList(context, "certificateArrayList", json)

                certificateTypeListAdapter = CertificateTypeListAdapter(certificateTypeArrayList, context, this)
              binding.recyclerCertificateView.adapter = certificateTypeListAdapter
            }
            "FLUORESCENCE INTENSITY" -> {
                rescenceTypeModelArrayList.add(model)
                val json = Gson().toJson(rescenceTypeModelArrayList)
                CommonUtility.saveLocalDataList(context, "rescenceModelArrayList", json)

                fluoRescenceTypeListAdapter = FluoRescenceTypeListAdapter(rescenceTypeModelArrayList, context, this)
            binding.recyclerFluorescenceView.adapter = fluoRescenceTypeListAdapter
            }
            "MAKE" -> {
                makeTypeArrayList.add(model)
                val json = Gson().toJson(makeTypeArrayList)
                CommonUtility.saveLocalDataList(context, "makeArrayList", json)

                makeTypeListAdapter = MakeTypeListAdapter(makeTypeArrayList, context, this)
               binding.recyclerMakeView.adapter = makeTypeListAdapter
            }
            "CUT GRADE" -> {
                Constant.cutTypeArrayList.add(model)
                val json = Gson().toJson(Constant.cutTypeArrayList)
                CommonUtility.saveLocalDataList(context, "cutArrayList", json)
            }
            "POLISH" -> {
                Constant.polishTypeArrayList.add(model)
                val json = Gson().toJson(Constant.polishTypeArrayList)
                CommonUtility.saveLocalDataList(context, "polishArrayList", json)
            }
            "SYMMETRY" -> {
                Constant.symmertyTypeArrayList.add(model)
                val json = Gson().toJson(Constant.symmertyTypeArrayList)
                CommonUtility.saveLocalDataList(context, "symmertyArrayList", json)
            }
            "TECHNOLOGY" -> {
                Constant.technologyTypeArrayList.add(model)
                val json = Gson().toJson(Constant.technologyTypeArrayList)
                CommonUtility.saveLocalDataList(context, "technologyArrayList", json)
            }
            "FANCY COLOR INTENSITY" -> {
                Constant.fancyColorIntensityArrayList.add(model)
                val json = Gson().toJson(Constant.fancyColorIntensityArrayList)
                CommonUtility.saveLocalDataList(context, "fancyColorIntensityArrayListData", json)
            }
            "FANCY COLOR OVERTONE" -> {
                Constant.fancyColorOverToneArrayList.add(model)
                val json = Gson().toJson(Constant.fancyColorOverToneArrayList)
                CommonUtility.saveLocalDataList(context, "fancyColorOverToneArrayListData", json)
            }
            "TABLE PERCENTAGE" -> {
                Constant.tableDataPercantageArrayList.add(model)
                val json = Gson().toJson(Constant.tableDataPercantageArrayList)
                CommonUtility.saveLocalDataList(context, "tableDataPercantageArrayListData", json)
            }
            "DEPTH PERCENTAGE" -> {
                Constant.depthDataPercantageArrayList.add(model)
                val json = Gson().toJson(Constant.depthDataPercantageArrayList)
                CommonUtility.saveLocalDataList(context, "depthDataPercantageArrayListData", json)
            }
            "CROWN ANGLE" -> {
                Constant.crowmArrayList.add(model)
                val json = Gson().toJson(Constant.crowmArrayList)
                CommonUtility.saveLocalDataList(context, "crowmArrayListData", json)
            }
            "PAVILION ANGLE" -> {
                Constant.pavillionArrayList.add(model)
                val json = Gson().toJson(Constant.pavillionArrayList)
                CommonUtility.saveLocalDataList(context, "pavillionArrayListData", json)
            }
            "EYE CLEAN" -> {
                Constant.eyeCleanArrayList.add(model)
                val json = Gson().toJson(Constant.eyeCleanArrayList)
                CommonUtility.saveLocalDataList(context, "eyeCleanArrayListData", json)
            }
            "SHADE" -> {
                Constant.shadeArrayList.add(model)
                val json = Gson().toJson(Constant.shadeArrayList)
                CommonUtility.saveLocalDataList(context, "shadeArrayListData", json)
            }
            "LUSTER" -> {
                Constant.lusterArrayList.add(model)
                val json = Gson().toJson(Constant.lusterArrayList)
                CommonUtility.saveLocalDataList(context, "lusterArrayListData", json)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    fun CreateDemandSuccessDialog(activity: Activity) {
        val dialogBuilder = AlertDialog.Builder(activity)
        val binding = CreateDemandSuccessLayoutBinding.inflate(LayoutInflater.from(activity))
        dialogBuilder.setView(binding.root)
        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(true)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()

        val message = activity.getString(R.string.create_demand_thankyoutext)

        val spannable = SpannableString(message)


        val phoneNumber = "+91 9892003399"
        val phoneNumberStartIndex = message.indexOf(phoneNumber)
        val phoneNumberEndIndex = phoneNumberStartIndex + phoneNumber.length

        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                activity.startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.clearShadowLayer()
                ds.color = Color.TRANSPARENT
                ds.isUnderlineText = true
            }
        }, phoneNumberStartIndex, phoneNumberEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannable.setSpan(ForegroundColorSpan(Color.BLACK), phoneNumberStartIndex, phoneNumberEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val email = "support@diamondxe.com"
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

        spannable.setSpan(ForegroundColorSpan(Color.BLACK), emailStartIndex, emailEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.apiMsg.text = spannable
        binding.apiMsg.movementMethod = LinkMovementMethod.getInstance()

        binding.crossImg.setOnClickListener {
            alertDialog.dismiss()
            clearAfterApiCall()
            clearAllFilter()
        }

        // Okay button functionality
        binding.okayTv.setOnClickListener {
            alertDialog.dismiss()
            clearAfterApiCall()
            clearAllFilter()
        }
    }


    private fun showCountryCodeList() {
        // Initialize the BottomSheetDialog
        dialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        dialogBinding = DialogShowCountryCodeBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        // Set the title text
        dialogBinding.textView2.text = getString(R.string.country_list)

        // Set up the RecyclerView
        val layoutManager = LinearLayoutManager(this)
        dialogBinding.recyclerView.layoutManager = layoutManager

        countryListAdapter = CountryListAdapter(countryArrayList, this, this, showCountryCode)
        dialogBinding.recyclerView.adapter = countryListAdapter
        countryListAdapter.notifyDataSetChanged()
        dialogBinding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(arg0: Editable?) {
                try {
                    val text = dialogBinding.searchEt.text.toString().lowercase(Locale.getDefault())
                    val textLength = dialogBinding.searchEt.text.length

                    countryListAdapter.filter(text, textLength)
                } catch (e: Exception) {
                }
            }

            override fun beforeTextChanged(
                arg0: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // No-op
            }

            override fun onTextChanged(arg0: CharSequence?, start: Int, before: Int, count: Int) {
                // No-op
            }
        })

        dialogBinding.ibCross.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)

        val window = dialog.window
        window?.let {
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(it.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = 1000

            it.attributes = lp
            it.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        }

        dialog.show()
    }

    private fun initiateCurrencyPopupWindow(): PopupWindow? {
        return try {
            // Dismiss the existing popup if it's showing
            mDropdown?.takeIf { it.isShowing }?.dismiss()

            mInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout: View = mInflater.inflate(R.layout.custom_menu_currency, null)
            val recyclerViewCurrency: RecyclerView =
                layout.findViewById(R.id.recycler_view_currency)
            val layoutManager = LinearLayoutManager(context)
            recyclerViewCurrency.layoutManager = layoutManager
            currencyListAdapter = CurrencyListAdapter(Constant.currencyArrayList, context, this)
            recyclerViewCurrency.adapter = currencyListAdapter

            val fixedWidth = dpToPx(200) // Width in dp
            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

            mDropdown = PopupWindow(layout, fixedWidth, FrameLayout.LayoutParams.WRAP_CONTENT, true)

            binding.currencyImgPrice?.let {
                mDropdown?.showAsDropDown(it, 5, -40)
            } ?: run {
                Log.e("PopupWindow", "flag_img is null")
            }

            mDropdown
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun dpToPx(dp: Int): Int {
        val density = HomeScreenActivity.context.resources.displayMetrics.density
        return Math.round(dp * density)
    }

    override fun getErrorResponce(error: String?, service_ID: Int) {
        TODO("Not yet implemented")
    }

    private fun validateFields(): Boolean {
        val email = binding.emailEt.text.toString().trim()
        val name = binding.nameEt.text.toString().trim()
        val mobileNumber = binding.mobileNumberEt.text.toString().trim()
        if (name.isEmpty()) {
            binding.nameRequiredErrorTv.visibility = View.VISIBLE
            return false
        } else if (mobileNumber.isEmpty()) {
            binding.mobileNumberErrorTv.visibility = View.VISIBLE
            return false
        } else if (email.isEmpty()) {
            binding.emailRequiredErrorTv.visibility = View.VISIBLE
            binding.mobileNumberErrorTv.visibility = View.GONE
            binding.emailRequiredErrorTv.text = getString(R.string.email_required)
            return false
        } else if (!Utils.emailValidator(email)) {
            binding.emailRequiredErrorTv.visibility = View.VISIBLE
            binding.nameRequiredErrorTv.visibility = View.GONE
            binding.emailRequiredErrorTv.text = getString(R.string.email_valid_msg)
            return false
        }

        return true
    }

    var getShapes: String = ""
    var getColor: String = ""
    var getFancyColor: String = ""
    var getClarity: String = ""
    var getCertificate: String = ""
    var getFluorescence: String = ""
    var getMakeItems: String = ""


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.back_img -> {
                finish()
            }

            R.id.country_code_lin -> {
                Utils.hideKeyboard(this)
                countryCodeFor = "countryCodeBuyerSection"
                showCountryCode = "yes"
                getCountryListAPI(false)
            }

            R.id.priority_spinerimg -> {
                binding.prioritySpinnerAttributes.performClick()
            }

            R.id.dropdown_pricetype -> {
                binding.spinnerAttributesPrice.performClick()
            }

            R.id.nopices_dropimg -> {
                binding.nopicesSpinnerAttributes.performClick()
            }

            R.id.currency_price_li -> {
                initiateCurrencyPopupWindow()
            }

            R.id.card_view_natural -> {
                naturalCardTabColorSet()
                getDiamondType = "Natural"
            }

            R.id.card_view_grown -> {
                labGrownCardTabColorSet()
                getDiamondType = "Lab Grown"
            }

            R.id.priority_relative -> {
                binding.prioritySpinnerAttributes.performClick()
            }

            R.id.create_demand_btn -> {

                //Shapes
                val selectedItems = shapeImageArrayList
                    .filter { it.isSelected && shapeImageArrayList.indexOf(it) != 0 }
                    .joinToString(",") { it.attribCode }
                println("Selected Items: $selectedItems")
                getShapes = selectedItems
                println("getShape Items..###..: $getShapes")

                //color
                val selectedColorItems = colorTypeArrayList
                    .filter { it.isSelected }
                    .joinToString(",") { it.attribCode }
                println("Selected Items Color: $selectedColorItems")
                getColor = selectedColorItems

                val selectedFancyItems = fancyColorTypeArrayList
                    .filter { it.isSelected }
                    .joinToString(",") { it.attribCode }
                println("Selected Items Fancy Color: $selectedFancyItems")
                getFancyColor = selectedFancyItems


                //Clarity
                val selectedClarityItems = clarityTypeArrayList!!
                    .filter { it.isSelected }
                    .joinToString(",") { it.attribCode }
                println("Selected Items Clarity: $selectedClarityItems")
                getClarity = selectedClarityItems

                //Certificate
                val selectedCertificateItems = certificateTypeArrayList!!
                    .filter { it.isSelected }
                    .joinToString(",") { it.attribCode }
                println("Selected Items Clarity: $selectedCertificateItems")
                getCertificate = selectedCertificateItems

                //Fluorescence
                val selectedFluorescenceItems = rescenceTypeModelArrayList!!
                    .filter { it.isSelected }
                    .joinToString(",") { it.attribCode }
                println("Selected Items Fluorescence: $selectedFluorescenceItems")
                getFluorescence = selectedFluorescenceItems

                //Make
                val selectedMakeItems = makeTypeArrayList!!
                    .filter { it.isSelected }
                    .joinToString(",") { it.attribCode }
                println("Selected Items Make: $selectedMakeItems")
                getMakeItems = selectedMakeItems
               // CreateDemandSuccessDialog(this)
                if (validateFields()) {
                    if (getColorType.equals("fancy")) {
                        if (getFancyColor.isEmpty()) {
                            Toast.makeText(
                                this,
                                resources.getText(R.string.pleaseselectcolor),
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }

                    } else if (getColorType.equals("white")) {
                        if (getColor.isEmpty()) {
                            Toast.makeText(
                                this,
                                resources.getText(R.string.pleaseselectcolor),
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }

                    }
                    getCreateDemand(false)
                }
            }

            R.id.clear_all_filter -> {
                clearAllFilter()
                clearSelectionFilterBGColor()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun labGrownCardTabColorSet() {
        binding.naturalTv.setBackgroundResource(R.drawable.border_dark_purple)
        binding.grownTv.setBackgroundResource(R.drawable.background_gradient)

        binding.naturalTv.setTextColor(ContextCompat.getColor(context!!, R.color.purple_light))
        binding.grownTv.setTextColor(ContextCompat.getColor(context!!, R.color.white))

        binding.cardViewNatural.cardElevation = 0f
        binding.cardViewNatural.cardElevation = 0f
        binding.cardViewNatural.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                R.color.bg_color
            )
        )
        binding.cardViewNatural.setCardBackgroundColor(
            ContextCompat.getColor(
                context!!,
                R.color.bg_color
            )
        )
        binding.cardViewNatural.outlineAmbientShadowColor =
            ContextCompat.getColor(context!!, R.color.bg_color)
        binding.cardViewNatural.outlineSpotShadowColor =
            ContextCompat.getColor(context!!, R.color.bg_color)

        binding.cardViewGrown.cardElevation = 37f
        binding.cardViewGrown.cardElevation = 18f
        binding.cardViewGrown.outlineAmbientShadowColor =
            ContextCompat.getColor(context!!, R.color.purple_gradient_bottom)
        binding.cardViewGrown.outlineSpotShadowColor =
            ContextCompat.getColor(context!!, R.color.purple_gradient_bottom)
    }


    fun getCountryListAPI(showLoader: Boolean) {
        if (Utils.isNetworkAvailable(this)) {
            val urlParameter = HashMap<String, String>()
            var vollyApiActivity: VollyApiActivity? = null
            vollyApiActivity = VollyApiActivity(
                this,
                this,
                urlParameter,
                ApiConstants.GET_COUNTRY_LIST,
                ApiConstants.GET_COUNTRY_LIST_ID,
                showLoader,
                "POST"
            )
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR)
            // recyclerNaturalGrownView.visibility = View.GONE
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    fun naturalCardTabColorSet() {
        binding.naturalTv.setBackgroundResource(R.drawable.background_gradient)
        binding.grownTv.setBackgroundResource(R.drawable.border_dark_purple)

        binding.naturalTv.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        binding.grownTv.setTextColor(ContextCompat.getColor(context!!, R.color.purple_light))

        binding.cardViewNatural.cardElevation = 37f
        binding.cardViewNatural.cardElevation = 18f
        binding.cardViewNatural.outlineAmbientShadowColor =
            ContextCompat.getColor(context!!, R.color.purple_gradient_bottom)
        binding.cardViewNatural.outlineSpotShadowColor =
            ContextCompat.getColor(context!!, R.color.purple_gradient_bottom)

        binding.cardViewGrown.cardElevation = 0f
        binding.cardViewGrown.cardElevation = 0f
        binding.cardViewGrown.setCardBackgroundColor(
            ContextCompat.getColor(
                context!!,
                R.color.bg_color
            )
        )
        binding.cardViewGrown.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                R.color.bg_color
            )
        )
        binding.cardViewGrown.outlineAmbientShadowColor =
            ContextCompat.getColor(context!!, R.color.bg_color)
        binding.cardViewGrown.outlineSpotShadowColor =
            ContextCompat.getColor(context!!, R.color.bg_color)

    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun itemClick(position: Int, action: String) {

        Log.e("action", "...673....###############.....$action")
        when (action.lowercase()) {
            "colortype" -> {
                colorTypeArrayList[position].isSelected = !colorTypeArrayList[position].isSelected
                //handleFilterBestTabCombination()
                colorTypeListAdapter.notifyDataSetChanged()
            }

            "fancycolortype" -> {
                fancyColorTypeArrayList[position].isSelected =
                    !fancyColorTypeArrayList[position].isSelected
                fancyColorTypeListAdapter!!.notifyDataSetChanged()
            }

            "claritytype" -> {
                clarityTypeArrayList?.get(position)!!.isSelected =
                    !clarityTypeArrayList!![position].isSelected
                clarityTypeListAdapter!!.notifyDataSetChanged()
            }

            "certificatetype" -> {
                certificateTypeArrayList?.get(position)!!.isSelected =
                    !certificateTypeArrayList?.get(position)!!.isSelected
                certificateTypeListAdapter!!.notifyDataSetChanged()
            }

            "fluoresencetype" -> {
                rescenceTypeModelArrayList?.get(position)!!.isSelected =
                    !rescenceTypeModelArrayList!![position].isSelected
                fluoRescenceTypeListAdapter!!.notifyDataSetChanged()
            }

            "maketype" -> {
                val shouldSelect = !makeTypeArrayList?.get(position)!!.isSelected
                makeTypeArrayList?.forEach { it.isSelected = false }
                makeTypeArrayList?.get(position)!!.isSelected = shouldSelect
                if (shouldSelect) {
                    makeSelectedValue = makeTypeArrayList!![position].displayAttr
                    Constant.makeValue = makeTypeArrayList!![position].attribCode
                } else {
                    makeSelectedValue = ""
                }
                Log.e("------shouldSelect------ : ", makeSelectedValue)
                makeTypeListAdapter!!.notifyDataSetChanged()
            }

            "shapeimage" -> {
                val selectedItem = shapeImageArrayList[position]

                if (position == 0) {
                    val selectAll = !selectedItem.isSelected
                    shapeImageArrayList.forEach { it.isSelected = selectAll }
                } else {
                    val selectedItemSelected = selectedItem.isSelected
                    selectedItem.isSelected = !selectedItemSelected

                    val allSelected = shapeImageArrayList
                        .drop(1)
                        .all { it.isSelected }

                    shapeImageArrayList[0].isSelected = allSelected
                }
                shapeImageListAdapter.notifyDataSetChanged()
            }

            "countryname" -> {

                Log.e(
                    "PhoneCode",
                    "...673.......${countryArrayList.size}....position...***********...$position"
                )
                val model = countryArrayList[position]
                Log.e("PhoneCode", "...673.......${model.phoneCode}")
                Log.e("countryCode", "...673.......${model.countryCode}")
                binding.countryCode.text = model.phoneCode
                buyerPhCountryCode = model.phoneCode
                binding.countryCode.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.white_arrow,
                    0
                )
                if (model.image.isNotEmpty()) {
                    Picasso.with(this)
                        .load(model.image)
                        .into(binding.countryImg)
                }
                dialog.dismiss()
            }

            "countrytype" -> {
                val model = Constant.currencyArrayList[position]
                Log.e("countryCode", "....${model.currency}")
                binding.selectCurrecny.text = model.currency
                getSelectCurrencyType = model.currency
                /*Log.e("countryCode","....${model.baseCurrency}")
                Log.e("countryCode","....${model.countryCode}")
                Log.e("currencySymbol","....${model.currencySymbol}")*/
                if (!model.image.isNullOrEmpty()) {
                    Picasso.with(context)
                        .load(model.image)
                        .into(binding.currencyImgPrice)
                }

                CommonUtility.setGlobalString(context, "selected_currency_value", model.value)
                CommonUtility.setGlobalString(context, "selected_currency_code", model.currency)
                CommonUtility.setGlobalString(context, "selected_currency_desc", model.desc)
                CommonUtility.setGlobalString(context, "selected_currency_image", model.image)

                getCurrencySelectCountryCurrencySymbolAndValue(model.currency, model.value)

                if (mDropdown?.isShowing == true) {
                    mDropdown!!.dismiss()
                } else {
                    Log.e("PopupWindow", "mDropdown is null or not showing")
                }

                currencyListAdapter.notifyDataSetChanged()

            }

            else -> {
            }
        }
    }

    fun getCurrencySelectCountryCurrencySymbolAndValue(
        selectedCurrencyCode: String,
        selectedCurrencyValue: String
    ) {
        if (!Constant.getCurrencySymbol.equals("", ignoreCase = true)) {
            Constant.getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode)
            Constant.getCurrencyValue = selectedCurrencyValue
        } else {
            Constant.getCurrencySymbol = CommonUtility.getCurrencySymbol(INDIA_CURRENCY_CODE)
            Constant.getCurrencyValue = INDIA_CURRENCY_VALUE
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun clearAllFilter() {

        Constant.shapeDiamondValue = ""
        Constant.priceFrm = ""
        Constant.priceTo = ""
        Constant.caratFrm = ""
        Constant.caratTo = ""
        // Constant.colorType = ""
        Constant.colorValue = ""
        Constant.fancyColorValue = ""
        Constant.clarityValue = ""
        Constant.certificateValue = ""
        Constant.fluorescenceValue = ""
        Constant.makeValue = ""
        Constant.isReturnable = ""
        Constant.searchKeyword = ""
        Constant.cutValue = ""
        Constant.polishValue = ""
        Constant.symmetryValue = ""
        Constant.technologyValue = ""
        Constant.eyeCleanValue = ""
        Constant.shadeValue = ""
        Constant.lusterValue = ""
        Constant.fancyColorIntensity = ""
        Constant.fancyColorOvertone = ""
        Constant.tableFrm = ""
        Constant.tableTo = ""
        Constant.depthFrmSpinner = ""
        Constant.depthToSpinner = ""
        Constant.lengthFrm = ""
        Constant.lengthTo = ""
        Constant.widthFrm = ""
        Constant.widthTo = ""
        Constant.depthFrmInput = ""
        Constant.depthToInput = ""
        Constant.crownFrm = ""
        Constant.crownTo = ""
        Constant.pavillionFrm = ""
        Constant.pavillionTo = ""
        Constant.lotID = ""
        Constant.location = ""
        Constant.sortingBy = ""

        if (!clearAllClick.equals("yes", ignoreCase = true)) {
        } else {
            clearAllClick = ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun clearSelectionFilterBGColor() {
        clearFilterApplyAllSelections()

        binding.caratFromTv.setText("")
        binding.caratToTv.setText("")
        binding.priceFrom.setText("")
        binding.priceTo.setText("")
        binding.caratFromEt.setText("")
        binding.caratToEt.setText("")
        binding.nopiecesEt.setText("")

        binding.nameEt.setText("")
        binding.mobileNumberEt.setText("")
        binding.emailEt.setText("")
        binding.instructionsEt.setText("")

        binding.prioritySpinnerAttributes.setSelection(0)
        binding.nopicesSpinnerAttributes.setSelection(0)
        binding.spinnerAttributesPrice.setSelection(0)
        getSelectPriority = "Immediate (Within 3 days)"
        getSelectCurrencyType = "INR"
        getPriceType = "Price / Carat"
        getSelectNoPieces = "Total Pieces"

        shapeImageListAdapter.notifyDataSetChanged()

        if (Constant.colorType.equals(WHITE, ignoreCase = true)) {
            colorTypeListAdapter.notifyDataSetChanged()
        } else {
            fancyColorTypeListAdapter!!.notifyDataSetChanged()
        }
        clarityTypeListAdapter!!.notifyDataSetChanged()
        certificateTypeListAdapter!!.notifyDataSetChanged()
        fluoRescenceTypeListAdapter!!.notifyDataSetChanged()
        makeTypeListAdapter!!.notifyDataSetChanged()

        Constant.filterClear = ""
        Constant.makeSelectedValue = ""
        makeSelectedValue = ""
    }

    private fun clearFilterApplyAllSelections() {
        setShapeImageAllItemsToNotSelected(shapeImageArrayList)
        setAllItemsToNotSelected(colorTypeArrayList)
        setAllItemsToNotSelected(fancyColorTypeArrayList)
        setAllItemsToNotSelected(clarityTypeArrayList!!)
        setAllItemsToNotSelected(certificateTypeArrayList!!)
        setAllItemsToNotSelected(rescenceTypeModelArrayList!!)
        setAllItemsToNotSelected(makeTypeArrayList!!)
    }

    private fun setShapeImageAllItemsToNotSelected(list: ArrayList<ShapeImageModel>) {
        for (item in list) {
            item.isSelected = false
        }
    }

    private fun setAllItemsToNotSelected(list: ArrayList<AttributeDetailsModel>) {
        list.forEach { it.isSelected = false }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun clearAfterApiCall() {
        clearFilterApplyAllSelections()

        binding.caratFromTv.setText("")
        binding.caratToTv.setText("")
        binding.priceFrom.setText("")
        binding.priceTo.setText("")
        binding.caratFromEt.setText("")
        binding.caratToEt.setText("")
        binding.nopiecesEt.setText("")

        binding.prioritySpinnerAttributes.setSelection(0)
        binding.nopicesSpinnerAttributes.setSelection(0)
        binding.spinnerAttributesPrice.setSelection(0)
        getSelectPriority = "Immediate (Within 3 days)"
        getSelectCurrencyType = "INR"
        getPriceType = "Price / Carat"
        getSelectNoPieces = "Total Pieces"

        shapeImageListAdapter.notifyDataSetChanged()

        if (Constant.colorType.equals(WHITE, ignoreCase = true)) {
            colorTypeListAdapter.notifyDataSetChanged()
        } else {
            fancyColorTypeListAdapter!!.notifyDataSetChanged()
        }
        clarityTypeListAdapter!!.notifyDataSetChanged()
        certificateTypeListAdapter!!.notifyDataSetChanged()
        fluoRescenceTypeListAdapter!!.notifyDataSetChanged()
        makeTypeListAdapter!!.notifyDataSetChanged()

        Constant.filterClear = ""
        Constant.makeSelectedValue = ""
        makeSelectedValue = ""
    }

}