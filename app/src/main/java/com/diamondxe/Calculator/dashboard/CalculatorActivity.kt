package com.dxe.calc.dashboard

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.diamondxe.R
import com.diamondxe.databinding.ActivityCalculatorBinding
import com.dxe.calc.CurrencyListActivity
import com.dxe.calc.api.apiclient.CurrencyRateClient
import com.dxe.calc.api.apiclient.PriceListClient
import com.dxe.calc.api.common.NetworkResponse
import com.dxe.calc.custom.CustomWheelPickerView
import com.dxe.calc.models.Price
import com.dxe.calc.models.requestmodel.PriceListRequest
import com.dxe.calc.models.requestmodel.PriceListRequestModel
import com.dxe.calc.models.requestmodel.ReqBody
import com.dxe.calc.models.requestmodel.ReqHeader
import com.dxe.calc.repo.CurrencyRateRepo
import com.dxe.calc.repo.PriceListRepo
import com.dxe.calc.utils.AppPreferences
import com.dxe.calc.utils.Constants
import com.dxe.calc.viewmodelfactories.CurrencyRateViewModelFactory
import com.dxe.calc.viewmodelfactories.PriceListViewModelFactory
import com.dxe.calc.viewmodels.CurrencyRateViewModel
import com.dxe.calc.viewmodels.PriceListViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sh.tyy.wheelpicker.core.BaseWheelPickerView
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class CalculatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorBinding
    private val diamondShape = arrayListOf(
        "Round",
        "Pear",
        "Princess",
        "Marqise",
        "Sq. Emerald",
        "Oval",
        "Radiant",
        "Emerald",
        "Trilliant",
        "Heart",
        "European Cut",
        "Old Miner",
        "Flanders",
        "Cushion Brilliant",
        "Cushion Modified",
        "Baguette",
        "Kite",
        "Star",
        "Other",
        "Half Moon",
        "Trapezoid",
        "Bullets",
        "Hexagonal",
        "Lozenge",
        "Pentagonal",
        "Rose",
        "Shield",
        "Square",
        "Triangular",
        "Briolette",
        "Octagonal",
        "Tapered Baguette",
        "Calf",
        "Circular",
        "Circular Brilliant"
    )

    private val diamondColor = arrayListOf(
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        /*"N",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "W",
        "X",
        "Y",
        "Z"*/
    )
    private val diamondClarity =
        arrayListOf(
            "IF", /*"FL",*/
            "VVS1",
            "VVS2",
            "VS1",
            "VS2",
            "SI1",
            "SI2",
            "SI3",
            "I1",
            "I2",
            "I3"
        )
    private var isDiscountSelected = true
    private var popupMenu: PopupMenu? = null
    private var progressDialog: ProgressDialog? = null
    private var roundShapePriceList = ArrayList<Price>()
    private var pearShapePriceList = ArrayList<Price>()
    private var selectedShape = Constants.SHAPE_ROUND
    private var selectedColor = diamondColor[0]
    private var selectedClarity = diamondClarity[0]
    private var isRoundShapeSelected = true
    private val startForCurrencyResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                binding.tvCurrencyName.text = AppPreferences.currency
                binding.lblCurrency.text = AppPreferences.currency
                binding.lblSymbol2.text=AppPreferences.currency
                binding.tvCurrencyRate.text = AppPreferences.currencyRate.toString()
                calculatePrice()
            }

        }

    // customized for check
    /*private val formatter: NumberFormat by lazy {
        NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    }*/

    private val formatter: NumberFormat by lazy {
        (NumberFormat.getCurrencyInstance(Locale("en", "IN")) as DecimalFormat).apply {
            // Set the currency symbol to an empty string
            decimalFormatSymbols = decimalFormatSymbols.apply {
                currencySymbol = ""  // Remove the currency symbol
            }
        }
    }

    private val regex: Regex by lazy {
        Regex("[^,\\d]")
    }

    var isUserInput = false
    var isTextUpdating = false
    var isEditTextUpdating = false
    var isEditINRvalue = false
    private var isCaretTextUpdating = false
    private var isPercentEdited = false

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppPreferences.init(this)

        binding.back.setOnClickListener(){
            onBackPressed()
        }
        binding.ivJewellary.setOnClickListener(){
            startActivity(Intent(this, JewelleryActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        binding.rlMenu.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.ivRefresh.setOnClickListener {
            AppPreferences.roundShapePrice = ""
            AppPreferences.pearShapePrice = ""
            fetchRoundPriceList()
        }

        binding.pickerShape.selectedIndex = 0
        binding.pickerColor.selectedIndex = 0
        binding.pickerClearity.selectedIndex = 0


        binding.edtCaret.doAfterTextChanged {
            val caretText = binding.edtCaret.text.toString()
            Log.e("Edit CT", "...187.... $caretText")
            isOriginalTotalPriceSetUSD=false
            isOriginalTotalPriceSet=false
            isCaretTextUpdating = true
            // Check if the text is blank, a single dot (.), or an invalid decimal
            if(caretText.isEmpty())
            {
                binding.edtPercent.setText("")
                binding.edtPercent.hint="00.00"
            }
            if (caretText.isEmpty() || caretText == "0" || caretText == "." || caretText == "0." || caretText == ".0") {
                Log.e("In If","...ET crt.....194..............")
                binding.tvMarketPrice.text = "00"
                binding.tvPrice.text = "00"
                binding.tvTotalPrice.setText("")
                binding.tvTotalPrice.hint="00.00"
                binding.edtLessPercent.setText("")
                binding.edtLessPercent.hint="00.00"
                //binding.edtPercent.setText("")
                binding.editamount.setText("")

            } else {
                Log.e("In Else","...ET crt.....204.............."+binding.edtPercent.text?.isEmpty())
                if (binding.edtPercent.text?.isEmpty() == true)
                {
                    calculatePrice()
                }
                else{
                    calculatePriceWheel()
                    //calculatePrice()
                }


            }
            isCaretTextUpdating = false
        }


        binding.edtPercent.setOnFocusChangeListener { v, hasFocus ->
            //Log.e("Edit..%%%.","CAll.1..."+hasFocus)
            if (hasFocus) {
                binding.edtPercent.doAfterTextChanged {
                    isPercentEdited = true

                    Log.e("Edit..%%%.","CAll...2."+isTextUpdating)
                    if (!isTextUpdating) {
                        isUserInput = true
                        calculatePrice()
                        isUserInput = false

                    }
                }
            }
        }



        binding.tvTotalPrice.setOnFocusChangeListener { v, hasFocus ->
            Log.e("hasFocus",".**************.239........."+hasFocus)
            if (!hasFocus) {
                isEditINRvalue = true
            }
            if (hasFocus)
            {
                binding.tvTotalPrice.doAfterTextChanged { text ->

                    if (!isUserInput || !isTextUpdating)
                    {
                        Log.e("245","....Value...1")
                        try {
                            val newUSDValue = text?.toString()?.toDoubleOrNull()
                            Log.e("245","....Value...1..."+newUSDValue)
                            if (newUSDValue != null && newUSDValue > 0.0)
                            {
                                Log.e("245","....Value...2")
                                /*updateBasedOnUSD(newUSDValue)*/
                                updateCalculationsUSD()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

            }
            else {
                isEditINRvalue = true
            }
        }
        binding.editamount.setOnFocusChangeListener { v, hasFocus ->
            //Log.e("hasFocus",".**************.239........."+hasFocus)
            if (!hasFocus) {
                isEditINRvalue = true
            } else {

            }

            if (hasFocus)
            {
                isEditINRvalue = false
                binding.editamount.doAfterTextChanged { text ->
                    if (!isCaretTextUpdating || isEditTextUpdating) {
                        Log.e("In editamount", "...editing amount...")

                        // Handle the case where editamount is cleared
                        if (text.toString().isEmpty()) {
                            val discountPercentage1 = 100.0
                            binding.edtPercent.setText(String.format("%.4f", discountPercentage1))
                            binding.tvTotalPrice.setText("")
                            binding.tvTotalPrice.setHint("0")
                            val finalPrice = 0.0
                            binding.tvPrice.text =
                                getFormattedAmount(BigDecimal(finalPrice)).toString()
                        } else {
                            isUserInput = true
                            updateCalculations()
                            isUserInput = false
                        }
                    }

                }
            }
            else {
                isEditINRvalue = true
            }
        }

        binding.edtLessPercent.doAfterTextChanged {
            calculateLessAmount()
        }

        binding.ivClear.setOnClickListener() {
            resetFields()
        }

        binding.edtTaxPercent.doAfterTextChanged {
            calculateLessAmount()
        }

        //Diamond shape picker value
        binding.pickerShape.adapter.values = diamondShape.map {
            isOriginalTotalPriceSetUSD=false;
            isOriginalTotalPriceSet=false
            CustomWheelPickerView.Item(
                it, ContextCompat.getDrawable(
                    this,
                    R.drawable.menu
                ), it
            )
        }

        binding.pickerShape.setWheelListener(object : BaseWheelPickerView.WheelPickerViewListener {
            override fun didSelectItem(picker: BaseWheelPickerView, index: Int) {
                selectedShape = diamondShape[index]
                isRoundShapeSelected = selectedShape.equals(Constants.SHAPE_ROUND, true)
                isOriginalTotalPriceSet=false
                isOriginalTotalPriceSetUSD=false;
                calculatePriceWheel1()
            }
        })

        //Diamond color picker value
        binding.pickerColor.adapter.values = diamondColor.map {
            isOriginalTotalPriceSet=false
            isOriginalTotalPriceSetUSD=false;
            CustomWheelPickerView.Item(
                it, ContextCompat.getDrawable(
                    this,
                    R.drawable.menu
                ), it
            )
        }


        binding.ivRing.setOnClickListener(){
            val intent = Intent(this, RingSizeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }



        binding.pickerColor.setWheelListener(object : BaseWheelPickerView.WheelPickerViewListener {
            override fun didSelectItem(picker: BaseWheelPickerView, index: Int) {
                selectedColor = diamondColor[index]
                isOriginalTotalPriceSet=false
                isOriginalTotalPriceSetUSD=false;
                calculatePriceWheel1()
            }
        })

        //Diamond clarity picker value
        binding.pickerClearity.adapter.values = diamondClarity.map {
            isOriginalTotalPriceSet=false
            isOriginalTotalPriceSetUSD=false;
            CustomWheelPickerView.Item(
                it, ContextCompat.getDrawable(
                    this,
                    R.drawable.menu
                ), it
            )
        }

        binding.pickerClearity.setWheelListener(object :
            BaseWheelPickerView.WheelPickerViewListener {
            override fun didSelectItem(picker: BaseWheelPickerView, index: Int) {
                selectedClarity = diamondClarity[index]
                isOriginalTotalPriceSet=false
                isOriginalTotalPriceSetUSD=false;
                calculatePriceWheel1()
            }
        })


        binding.rlSelectedCurrency.setOnClickListener {

            Log.e("rlSelectedCurrency","..$$$$$$$$$$$$$...>..................405............")
            val intent = Intent(this, CurrencyListActivity::class.java)
            startForCurrencyResult.launch(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        val themedContext = ContextThemeWrapper(this, R.style.CustomPopupTheme)
        /*popupMenu = PopupMenu(themedContext, binding.rlDiscountDropdown)*/
        popupMenu = PopupMenu(themedContext, binding.rlDiscountDropdown, Gravity.START,
            0,
            R.style.CustomPopupTheme)
        popupMenu?.inflate(R.menu.popup_menu)
        binding.rlDiscountDropdown.setOnClickListener { showPopupMenu() }
        updateDiscountUI()
        fetchCurrencyRate()
        forceUpdatePriceList()
    }




    override fun onResume() {
        super.onResume()

        binding.tvCurrencyName.text = AppPreferences.currency
        binding.lblCurrency.text = AppPreferences.currency
        binding.lblSymbol2.text=AppPreferences.currency
        binding.totalcurrency.text=AppPreferences.currency
        calculatePrice()
    }

    private fun resetFields() {
        binding.tvMarketPrice.text=""
        binding.tvPrice.text=""
        binding.editamount.setText("")
        binding.tvTotalPrice.setText("")
        binding.edtTaxPercent.setText("")
        binding.edtLessPercent.setText("")
        binding.edtPercent.setText("")
        binding.edtCaret.setText("")
        binding.edtCaret.hint="00.00"
        binding.tvMarketPrice.hint = "00"
        binding.tvPrice.hint = "00.00"
        binding.tvTotalPrice.hint="00.00"
        binding.tvTotalAmount.hint = "00.00"
        binding.editamount.hint="00.00"
        binding.tvTotalAmountUsd.hint="00.00"
        binding.edtTaxPercent.hint="00.00"
        binding.edtLessPercent.hint="00.00"
        binding.tvTotalAmountUsd.text="00"
        binding.tvTotalAmount.text="00"
        binding.edtPercent.hint="00.00"
        originalTotalPrice=0.0
        originalTotalPriceUSD=0.0
        isOriginalTotalPriceSetUSD=false
        isOriginalTotalPriceSet=false
        isUserInput = false
        isTextUpdating = false
        isEditTextUpdating = false
        isEditINRvalue = false
        isCaretTextUpdating = false
    }

    private var originalTotalPrice: Double = 0.0
    private var originalTotalPriceUSD: Double = 0.0
    private var isOriginalTotalPriceSetUSD = false
    private var isOriginalTotalPriceSet = false

    //perfect working old code
    /*private fun filterPriceList(ct: Double, isCtGreaterThanRange: Boolean): List<Price> {
        var filteredList = emptyList<Price>()

        val priceList = if (isRoundShapeSelected) roundShapePriceList else pearShapePriceList
        filteredList = if (isCtGreaterThanRange) {
            priceList.filter { price -> 5.99 <= price.highSize && ct >= 5.0 }
        } else {
            priceList.filter { price -> ct <= price.highSize && ct >= price.lowSize }
        }

        filteredList = filteredList.filter { price ->
            selectedColor.equals(price.color, true) && selectedClarity.equals(price.clarity, true)
        }

        return filteredList
    }*/

    // check for demo
    private fun filterPriceList(ct: Double, isCtGreaterThanRange: Boolean): List<Price> {
        var filteredList = emptyList<Price>()

        Log.e("isRoundShapeSelected","....509..........."+isRoundShapeSelected)
        Log.e("isCtGreaterThanRange","....509..........."+isCtGreaterThanRange)
        val priceList = if (isRoundShapeSelected) roundShapePriceList else pearShapePriceList
        filteredList = if (isCtGreaterThanRange) {
            priceList.filter { price -> 5.99 <= price.highSize && ct >= 5.0 }
        } else {
            /*priceList.filter { price -> ct <= price.highSize && ct >= price.lowSize }*/
            priceList.filter { price -> ct <= price.highSize && ct >= price.lowSize }
        }

        /* 10.99 >=  price.highSize
         price.lowSize  <=10.00*/

        filteredList = filteredList.filter { price ->
            selectedColor.equals(price.color, true) && selectedClarity.equals(price.clarity, true)
        }

        return filteredList
    }

    private fun calculateDiscountOrPremium(ct: Double, percentage: Double, caretPrice: Double, isDiscountSelected: Boolean): Double {
        val marketPrice = binding.tvMarketPrice.text.toString().toDouble()

        val adjustedAmount = if (isDiscountSelected) {
            marketPrice - ((marketPrice * percentage) / 100)
        } else {
            marketPrice + ((marketPrice * percentage) / 100)
        }
        binding.tvPrice.text = String.format("%.2f", adjustedAmount)

        return adjustedAmount * ct
    }

    private fun convertCurrencyAndSetTotal(ct: Double) {
        val price = binding.tvPrice.text.toString().toBigDecimal()
        val totalAmount = (price * ct.toBigDecimal()) * binding.tvCurrencyRate.text.toString().toBigDecimal()
        val formattedAmount = totalAmount.setScale(2, RoundingMode.HALF_EVEN)
        val roundedAmount = getRoundedAmount(formattedAmount)
        val moneyString = formatter.format(roundedAmount).substringBefore(".")
        val result = regex.replace(moneyString, "")

        Log.e("result",".......484......."+moneyString)
        binding.tvTotalAmount.text = formatAmountWithCommas(result)
        binding.editamount.setText(roundedAmount.toString())
    }

    fun formatAmountWithCommas(amount: String): String {
        val formatter = DecimalFormat("#,##,###")
        val parsedAmount = amount.toBigDecimalOrNull() ?: return amount
        return formatter.format(parsedAmount)
    }

    fun getCaretPrice(ct: Double, switchValue: Boolean): Double {
        val isCtGreaterThenRange = ct > 5.99

        val filteredList = filterPriceList(ct, isCtGreaterThenRange)

        val index = if (isCtGreaterThenRange) 1 else 0

        val caretPrice = filteredList[index].caratprice

        Log.e("isCtGreaterThenRange", "Switch: $switchValue, ct > 5.99: $isCtGreaterThenRange")
        Log.e("filteredList", "Filtered List: $filteredList")
        Log.e("caretPrice", "Calculated caret price: $caretPrice")

        return caretPrice.toDouble()
    }


    private fun calculatePrice() {

        if (!isTextUpdating) {
            isTextUpdating = true
            isUserInput = true
            if (binding.edtCaret.text?.isNotEmpty() == true) {
                try {
                    val ct = binding.edtCaret.text.toString().toDouble()
                    var caretPrice=0
                    val switchValue = getPriceSwitchValuePreference(this, "PriceValue", false)
                    Log.e("switchValue","...542...."+switchValue)
                    if (switchValue)
                    {

                        if (ct > 5.99)
                        {
                            val isCtGreaterThenRange = ct > 5.99
                            Log.e("isCtGreaterThenRange","Switch on.1......"+isCtGreaterThenRange)
                            val filteredList = filterPriceList(ct, isCtGreaterThenRange)
                            Log.e("filteredList","Switch On......"+filteredList)
                            caretPrice = filteredList[1].caratprice
                            Log.e("caretPrice","Switch On..1...."+caretPrice)

                        }
                        else
                        {
                            val isCtGreaterThenRange = ct > 5.99
                            Log.e("isCtGreaterThenRange","Switch on..2....."+isCtGreaterThenRange)
                            val filteredList = filterPriceList(ct, isCtGreaterThenRange)
                            Log.e("filteredList","Switch On......"+filteredList)
                            caretPrice = filteredList[0].caratprice
                            Log.e("caretPrice","Switch on.2....."+caretPrice)
                        }

                    }
                    else
                    {


                        val isCtGreaterThenRange = ct > 5.99
                        Log.e("isCtGreaterThenRange","Switch on..2....."+isCtGreaterThenRange)
                        val filteredList = filterPriceList(ct, isCtGreaterThenRange)
                        Log.e("filteredList","Switch On......"+filteredList)
                        caretPrice = filteredList[0].caratprice
                        Log.e("caretPrice","Switch on.2....."+caretPrice)
                    }

                    //  caretPrice= getCaretPrice(ct,switchValue)

                    if (ct > 0.0)
                    {
                        /* val isCtGreaterThenRange = ct > 5.99
                         Log.e("isCtGreaterThenRange","......."+isCtGreaterThenRange)
                         val filteredList = filterPriceList(ct, isCtGreaterThenRange)
                         Log.e("filteredList","..."+filteredList)*/
                        /*if (filteredList.isNotEmpty())
                        {
                            caretPrice = filteredList[0].caratprice*/
                        Log.e("caretPrice","..."+caretPrice)
                        val totalPrice = BigDecimal(caretPrice.toDouble()) * BigDecimal(ct)
                        Log.e("totalPrice","..."+totalPrice)
                        binding.tvMarketPrice.text = caretPrice.toString()
                        binding.tvTotalPrice.setText(getFormattedAmount(totalPrice).toString())
                        binding.tvTotalAmountUsd.text = getRoundInt(totalPrice).toString()

                        if (!isOriginalTotalPriceSetUSD) {
                            originalTotalPriceUSD = getRoundInt(totalPrice).toDouble()
                            isOriginalTotalPriceSetUSD = true
                        }

                        if (binding.edtPercent.text?.isNotEmpty() == true) {
                            val percentage = binding.edtPercent.text.toString().toDouble()
                            val totalPerc = calculateDiscountOrPremium(ct, percentage, caretPrice.toDouble(), isDiscountSelected)
                            binding.tvTotalPrice.setText(String.format("%.2f", totalPerc))
                            binding.tvTotalAmountUsd.text = getRoundInt(totalPerc.toBigDecimal()).toString()

                            convertCurrencyAndSetTotal(ct)

                            if (!isOriginalTotalPriceSet) {
                                originalTotalPrice = totalPerc
                                isOriginalTotalPriceSet = true
                            }
                        } else {
                            /* binding.tvPrice.text = caretPrice.toString()*/
                            binding.tvPrice.text = String.format("%.2f", caretPrice.toDouble())

                            convertCurrencyAndSetTotal(ct)

                            if (!isOriginalTotalPriceSet) {
                                originalTotalPrice = totalPrice.toDouble()
                                isOriginalTotalPriceSet = true
                            }
                        }
                        /*} else {
                            binding.tvMarketPrice.text = "0"
                            binding.tvTotalAmount.text = "0"
                        }*/

                        calculateLessAmount()
                    }
                    else {
                        binding.tvMarketPrice.text = "00"
                        binding.tvTotalAmount.text = "00"
                        binding.tvTotalPrice.setText("00")
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            isUserInput = false
            isTextUpdating = false
        }

    }



    private fun calculatePriceWheel() {
        if (!isTextUpdating) {
            isTextUpdating = true
            isUserInput = true

            try {
                val ct = binding.edtCaret.text.toString().toDoubleOrNull() ?: 0.0

                val switchValue = getPriceSwitchValuePreference(this, "PriceValue", false)
                var caretPrice=BigDecimal.ZERO
                Log.e("switchValue","...542...."+switchValue)
                if (switchValue)
                {

                    if (ct > 5.99)
                    {
                        val isCtGreaterThenRange = ct > 5.99
                        Log.e("isCtGreaterThenRange","Switch on.1......"+isCtGreaterThenRange)
                        val filteredList = filterPriceList(ct, isCtGreaterThenRange)
                        Log.e("filteredList","Switch On......"+filteredList)
                        caretPrice = BigDecimal(filteredList[1].caratprice.toString())
                        Log.e("caretPrice  Wheel ","Switch On..1...."+caretPrice)

                    }
                    else
                    {
                        val isCtGreaterThenRange = ct > 5.99
                        Log.e("isCtGreaterThenRange","Switch on..2....."+isCtGreaterThenRange)
                        val filteredList = filterPriceList(ct, isCtGreaterThenRange)
                        Log.e("filteredList","Switch On......"+filteredList)
                        caretPrice = BigDecimal(filteredList[0].caratprice.toString())
                        Log.e("caretPrice Wheel","Switch on.2....."+caretPrice)
                    }

                }
                else
                {
                    val isCtGreaterThenRange = ct > 5.99
                    Log.e("isCtGreaterThenRange","Switch on..2....."+isCtGreaterThenRange)
                    val filteredList = filterPriceList(ct, isCtGreaterThenRange)
                    Log.e("filteredList","Switch On......"+filteredList)
                    caretPrice = BigDecimal(filteredList[0].caratprice.toString())
                    Log.e("caretPrice","Switch on.2....."+caretPrice)
                }
                if (ct > 0.0) {
                    /*val isCtGreaterThenRange = ct > 5.99
                    val filteredList = filterPriceList(ct, isCtGreaterThenRange)

                    if (filteredList.isNotEmpty()) {
                        val caretPrice = BigDecimal(filteredList[0].caratprice.toString())*/

                    // Total price in USD
                    val totalPrice = caretPrice.multiply(BigDecimal(ct))
                    Log.e("Total Price (USD)", "Total Price: $totalPrice")

                    // Get currency rate
                    val currencyRate = binding.tvCurrencyRate.text.toString().toDoubleOrNull() ?: 00.00
                    Log.e("currencyRate", "currencyRate: $currencyRate")

                    // Total amount in INR
                    val totalAmountInINR = totalPrice.multiply(BigDecimal(currencyRate))

                    Log.e("isPercentEdited","...."+isPercentEdited)
                    // Check if the percentage was edited
                    if (isPercentEdited) {
                        val percentage = binding.edtPercent.text.toString().toDoubleOrNull() ?: 0.0
                        val discountedTotalPrice = totalPrice.subtract(
                            totalPrice.multiply(BigDecimal(percentage).divide(BigDecimal(100)))
                        )
                        val pricePerCarat = discountedTotalPrice.divide(BigDecimal(ct), 2, RoundingMode.HALF_UP)
                        binding.tvPrice.text = String.format("%.2f", pricePerCarat)
                        binding.tvTotalPrice.setText(String.format("%.2f", discountedTotalPrice))
                        binding.editamount.setText(String.format("%.2f", discountedTotalPrice.multiply(BigDecimal(currencyRate))))
                    } else {
                        binding.tvTotalPrice.setText(String.format("%.2f", totalPrice))
                        binding.editamount.setText(String.format("%.2f", totalAmountInINR))
                        binding.tvPrice.text = String.format("%.2f", caretPrice)
                    }

                    //Log.e("623..", "USD: ${String.format("%.2f", caretPrice.toPlainString())}")
                    binding.tvMarketPrice.text = caretPrice.toPlainString()
                    //binding.tvPrice.text = caretPrice.toPlainString()

                    Log.e("USD", "USD: ${String.format("%.2f", totalPrice)}")
                    Log.e("INR", "INR: ${String.format("%.2f", totalAmountInINR)}")
                    calculateLessAmount()
                    //  }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isUserInput = false
                isTextUpdating = false
                isPercentEdited = false
            }
        }
    }


    private fun calculatePriceWheel1() {
        if (!isTextUpdating) {
            isTextUpdating = true
            isUserInput = true

            try {
                val ct = binding.edtCaret.text.toString().toDoubleOrNull() ?: 0.0

                val switchValue = getPriceSwitchValuePreference(this, "PriceValue", false)
                var caretPrice=BigDecimal.ZERO

                Log.e("switchValue","Wheel call...542...."+switchValue)
                if (switchValue)
                {

                    if (ct > 5.99)
                    {
                        val isCtGreaterThenRange = ct > 5.99
                        Log.e("isCtGreaterThenRange","Wheel call Switch on.1......"+isCtGreaterThenRange)
                        val filteredList = filterPriceList(ct, isCtGreaterThenRange)
                        Log.e("filteredList","Switch On......"+filteredList)
                        caretPrice = BigDecimal(filteredList[1].caratprice.toString())
                        Log.e("caretPrice  Wheel cal ","Switch On..1...."+caretPrice)

                    }
                    else
                    {
                        val isCtGreaterThenRange = ct > 5.99
                        Log.e("isCtGreaterThenRange","Switch on..2....."+isCtGreaterThenRange)
                        val filteredList = filterPriceList(ct, isCtGreaterThenRange)
                        Log.e("filteredList","Switch On......"+filteredList)
                        caretPrice = BigDecimal(filteredList[0].caratprice.toString())
                        Log.e("Wheel call Wheel","Switch on.2....."+caretPrice)
                    }

                }
                else
                {

                    val isCtGreaterThenRange = ct > 5.99
                    Log.e("isCtGreaterThenRange","Switch on..2....."+isCtGreaterThenRange)
                    val filteredList = filterPriceList(ct, isCtGreaterThenRange)
                    Log.e("filteredList","Switch On......"+filteredList)
                    caretPrice = BigDecimal(filteredList[0].caratprice.toString())
                    Log.e("caretPrice","Switch on.2....."+caretPrice)
                }

                if (ct > 0.0)
                {
                    /*val isCtGreaterThenRange = ct > 5.99
                    val filteredList = filterPriceList(ct, isCtGreaterThenRange)
                    if (filteredList.isNotEmpty())
                    {
                        val caretPrice =
                            BigDecimal(filteredList[0].caratprice.toString())*/
                    val ctBigDecimal = BigDecimal(ct.toString())
                    val totalPrice = caretPrice.multiply(ctBigDecimal)
                    Log.e("caretPrice", "Caret Price: $caretPrice")
                    Log.e("totalPrice", "Total Price: $totalPrice")

                    binding.tvMarketPrice.text = caretPrice.toPlainString()
                    binding.tvTotalPrice.setText(getFormattedAmount(totalPrice).toString())
                    binding.tvTotalAmountUsd.text = getRoundInt(totalPrice).toString()

                    if (!isOriginalTotalPriceSetUSD) {
                        originalTotalPriceUSD = getRoundInt(totalPrice).toDouble()
                        isOriginalTotalPriceSetUSD = true
                    }
                    if (binding.edtPercent.text?.isNotEmpty() == true) {
                        val percentage = binding.edtPercent.text.toString().toDouble()
                        val totalPerc = calculateDiscountOrPremium(ct, percentage, caretPrice.toDouble(), isDiscountSelected)
                        binding.tvTotalPrice.setText(String.format("%.2f", totalPerc))
                        binding.tvTotalAmountUsd.text = getRoundInt(totalPerc.toBigDecimal()).toString()

                        convertCurrencyAndSetTotal(ct)

                        if (!isOriginalTotalPriceSet) {
                            originalTotalPrice = totalPerc
                            isOriginalTotalPriceSet = true
                        }
                    } else {
                        // When no percentage is provided
                        binding.tvPrice.text = caretPrice.toPlainString()
                        convertCurrencyAndSetTotal(ct)

                        if (!isOriginalTotalPriceSet) {
                            originalTotalPrice = totalPrice.toDouble()
                            isOriginalTotalPriceSet = true
                        }
                    }

                    //  }

                    calculateLessAmount()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isUserInput = false
                isTextUpdating = false
            }
        }
    }
    fun getPriceSwitchValuePreference(context: Context, key: String, defaultValue: Boolean): Boolean {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, defaultValue)
    }
    private fun updateCalculationsUSD() {
        if (isUpdating) return

        isUpdating = true
        isTextUpdating = true
        val usdAmountText = binding.tvTotalPrice.text.toString()
        val usdAmount = usdAmountText.toDoubleOrNull() ?: 0.0
        val currencyRate = binding.tvCurrencyRate.text.toString().toDoubleOrNull() ?: 1.0
        val marketPrice = binding.tvMarketPrice.text.toString().toDoubleOrNull() ?: 0.0
        val ct = binding.edtCaret.text.toString().toDoubleOrNull() ?: 0.0
        val inrAmountFromUsd = usdAmount * currencyRate
        val newAmount = BigDecimal(usdAmount)
        val oldAmount = BigDecimal(originalTotalPriceUSD)
        val discount = calculateDiscount(newAmount, oldAmount).let {
            String.format("%.4f", it).toDoubleOrNull() ?: 0.0
        }
        Log.e("Calculated Discount", "Discount: $discount")
        val discountAmount = marketPrice * discount / 100
        val finalPrice = marketPrice - discountAmount
        Log.e("Final Price", "Final Price: $finalPrice")
        binding.apply {
            tvPrice.text = getFormattedAmount(BigDecimal(finalPrice))
            isTextUpdating = true
            edtPercent.setText(String.format("%.4f", discount))
            isTextUpdating = false

            val formattedUsdAmountText = getFormattedAmount(BigDecimal(usdAmount))
            Log.e("Formatted USD Amount", "Formatted USD Amount: $formattedUsdAmountText")
            tvTotalAmountUsd.text = formattedUsdAmountText
            editamount.setText(getFormattedAmount(BigDecimal(inrAmountFromUsd)))
            Log.e("inrAmountFromUsd",".......814......."+inrAmountFromUsd)
            tvTotalAmount.text = formatAmountWithCommas(getFormattedAmount(BigDecimal(inrAmountFromUsd)))
        }

        calculateLessAmount()
        isTextUpdating = false
        isUpdating = false
    }

    private fun updateCalculations() {

        if (isUpdating) return

        isUpdating = true
        isTextUpdating = true

        if (!isEditINRvalue) {
            if (isUserInput) {
                val newTotalAmount = binding.editamount.text.toString().toDoubleOrNull() ?: 0.0
                val currencyRate = binding.tvCurrencyRate.text.toString().toDoubleOrNull() ?: 1.0
                val usdAmount = convertInrToUsd(newTotalAmount, currencyRate)

                val newAmount = BigDecimal(usdAmount)
                val oldAmount = BigDecimal(originalTotalPriceUSD)
                val discount = calculateDiscount(newAmount, oldAmount).let {
                    String.format("%.4f", it).toDoubleOrNull() ?: 0.0
                }
                val marketPrice = binding.tvMarketPrice.text.toString().toDoubleOrNull() ?: 0.0
                val discountAmount = marketPrice * discount / 100
                val finalPrice = marketPrice - discountAmount
                binding.apply {
                    tvPrice.text = getFormattedAmount(BigDecimal(finalPrice))
                    isTextUpdating = true
                    edtPercent.setText(String.format("%.4f", discount))
                    isTextUpdating = false

                    val formattedUsdAmountText = getFormattedAmount(BigDecimal(usdAmount))
                    tvTotalPrice.setText(formattedUsdAmountText)
                    tvTotalAmountUsd.text = formattedUsdAmountText

                    val inrAmount = usdAmount * currencyRate
                    Log.e("inrAmountFromUsd",".......814..2....."+inrAmount)
                    tvTotalAmount.text = formatAmountWithCommas(getFormattedAmount(BigDecimal(inrAmount)))
                }
            }
            isTextUpdating = false
            isUpdating = false
        }

        calculateLessAmount()
        isUpdating = false
    }


    private var isUpdating = false





    fun calculateDiscount(newAmount: BigDecimal, oldAmount: BigDecimal): BigDecimal {
        if (oldAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO
        }
        val difference = oldAmount.subtract(newAmount)
        val discountPercentage = difference.divide(oldAmount, 10, RoundingMode.HALF_UP).multiply(BigDecimal(100))

        return discountPercentage.setScale(4, RoundingMode.HALF_UP)
    }


    fun convertInrToUsd(inrAmount: Double, exchangeRate: Double): Double {
        return inrAmount / exchangeRate
    }

    fun convertUsdToInr(usdAmount: Double, exchangeRate: Double): Double {
        return usdAmount * exchangeRate
    }
    private fun calculateLessAmount() {
        var lessPercentage = 0.0
        var taxPercentage = 0.0
        var AddLessPer = 0.0
        var originalPrice = 0.0
        var finalAmount = 0.0

        try {

            if (binding.edtLessPercent.text?.isNotEmpty() == true) {
                lessPercentage = binding.edtLessPercent.text.toString().toDoubleOrNull() ?: 0.0
            }
            if (binding.edtTaxPercent.text?.isNotEmpty() == true) {
                taxPercentage = binding.edtTaxPercent.text.toString().toDoubleOrNull() ?: 0.0
            }
            originalPrice = binding.tvPrice.text.toString().toDoubleOrNull() ?: 0.0

            AddLessPer = taxPercentage - lessPercentage

            finalAmount = originalPrice * (1 + (AddLessPer / 100))

            val finalAmountAfterLess = finalAmount * (binding.edtCaret.text.toString().toDoubleOrNull() ?: 1.0)
            val finalAmountInINR = finalAmountAfterLess * (binding.tvCurrencyRate.text.toString().toDoubleOrNull() ?: 1.0)
            val roundedAmount = getRoundInt(BigDecimal(finalAmountInINR))
            val formattedAmount = formatter.format(roundedAmount)
            // USD Amount
            val exchangeRate =binding.tvCurrencyRate.text.toString().toDouble()
            val usdAmount = convertInrToUsd(finalAmountInINR.toDouble(), exchangeRate)
            /*  Log.e("usdAmount","........1084......Tax........"+usdAmount+"..."+getFormattedAmount(BigDecimal(usdAmount)))
              Log.e("formattedAmount","........1085......Tax........"+formattedAmount)*/
            binding.tvTotalAmountUsd.text=getFormattedAmount(BigDecimal(usdAmount))
            Log.e("inrAmountFromUsd",".......814..3....."+formattedAmount)
            binding.tvTotalAmount.text = formatAmountWithCommas(roundedAmount.toString())
            //}

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private inline fun getFormattedAmount(value: BigDecimal): String {
        val formattedAmount = value.setScale(2, RoundingMode.HALF_EVEN)
        return formattedAmount.toPlainString() // Convert BigDecimal to plain string format for display
    }

    private inline fun getRoundedAmount(value: BigDecimal): BigDecimal {
        val amount = value.setScale(2, RoundingMode.HALF_EVEN)
        println("Rounded Amount = $amount")
        return amount
    }


    private inline fun getRoundInt(value: BigDecimal): Int {
        val bigDecimalFormat = BigDecimal(value.toDouble())
        val amount = bigDecimalFormat.setScale(2, RoundingMode.HALF_EVEN)
        val y: Double = amount.toDouble()
        // println("y = $y")
        val z: Int = y.roundToInt()
        println("z = $z")
        return z
    }

    private fun forceUpdatePriceList() {
        if (TextUtils.isEmpty(AppPreferences.roundShapePrice)) {
            fetchRoundPriceList()
        } else {
            try {
                val formatterCurrent = SimpleDateFormat("yyyy-MM-dd")
                val dateCurrent = Date()
                val current = formatterCurrent.format(dateCurrent)
                //Log.e("forceUpdatePriceList: ", "today date - $current")

                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val text = AppPreferences.apiRefreshDate
                val date = formatter.parse(text)

                val cal = Calendar.getInstance()
                cal.time = date
                cal.add(Calendar.DAY_OF_MONTH, 7)
                val futureDate = formatter.parse(formatter.format(cal.time))
                //Log.e("forceUpdatePriceList: ", "$futureDate")

                /* if (dateCurrent.after(futureDate)) {
                //Log.e("forceUpdatePriceList: ", "api")
                }*/
                /* = 0, if both dates are equal.
                < 0, if date is before the specified date.
                > 0, if date is after the specified date.*/
                val cmp = dateCurrent.compareTo(futureDate)
                when {
                    cmp > 0 -> {
                        print("Both dates are equal")
                        AppPreferences.roundShapePrice = ""
                        AppPreferences.pearShapePrice = ""
                        fetchRoundPriceList()
                    }
                    cmp < 0 -> {
                        fetchRoundPriceList()
                        //do nothing
                        //System.out.printf("%s is before %s", d1, d2)
                    }
                    else -> {
                        print("Both dates are equal")
                        AppPreferences.roundShapePrice = ""
                        AppPreferences.pearShapePrice = ""
                        fetchRoundPriceList()

                    }
                }

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                // Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun fetchRoundPriceList() {
        if (TextUtils.isEmpty(AppPreferences.roundShapePrice)) {
            CoroutineScope(Dispatchers.Main).launch {
                showProgress()
                val client = PriceListClient.getInstance()
                val priceListRepo = PriceListRepo(client)

                val priceListViewModel =
                    ViewModelProvider(
                        this@CalculatorActivity,
                        PriceListViewModelFactory(priceListRepo)
                    )[PriceListViewModel::class.java]
                var priceListRequest = PriceListRequest()
                var priceListRequestModel = PriceListRequestModel()
                var reqHeader = ReqHeader()
                reqHeader.setUsername(Constants.REQ_USER_NAME)
                reqHeader.setPassword(Constants.REQ_PASSWORD)
                priceListRequestModel.setHeader(reqHeader)

                var reqBody = ReqBody()
                reqBody.setShape(Constants.SHAPE_ROUND)
                priceListRequestModel.setBody(reqBody)
                priceListRequest.setRequest(priceListRequestModel)
                priceListViewModel.getPrices(priceListRequest)
                priceListViewModel.priceListLiveData.observe(this@CalculatorActivity) { response ->
                    when (response) {
                        is NetworkResponse.Success -> {
                            Log.e("priceListLiveData NetworkResponse.Success----", "$response")
                            //hideProgress()
                            val resTemp = response.body
                            if (resTemp.response.header.errorCode == 0) {
                                Log.e("list", "${resTemp.response.body.price.size}")
                                if (resTemp.response.body.price.isNotEmpty()) {
                                    val listTemp = resTemp.response.body.price
                                    roundShapePriceList.clear()
                                    roundShapePriceList.addAll(listTemp)
                                    AppPreferences.roundShapePrice =
                                        Gson().toJson(roundShapePriceList)

                                }

                            }
                            fetchPearPriceList()
                        }
                        is NetworkResponse.NetworkError -> {
                            hideProgress()
                        }
                        else -> {
                            hideProgress()
                        }
                    }
                }

            }
        } else {
            roundShapePriceList.clear()
            val typeToken = object : TypeToken<List<Price>>() {}.type
            val tmpRound = Gson().fromJson<List<Price>>(AppPreferences.roundShapePrice, typeToken)
            roundShapePriceList.addAll(tmpRound)
            fetchPearPriceList()
        }
    }

    private fun fetchPearPriceList() {
        if (TextUtils.isEmpty(AppPreferences.pearShapePrice)) {
            CoroutineScope(Dispatchers.Main).launch {
                showProgress()
                val client = PriceListClient.getInstance()
                val priceListRepo = PriceListRepo(client)

                val priceListViewModel =
                    ViewModelProvider(
                        this@CalculatorActivity,
                        PriceListViewModelFactory(priceListRepo)
                    )[PriceListViewModel::class.java]
                var priceListRequest = PriceListRequest()
                var priceListRequestModel = PriceListRequestModel()
                var reqHeader = ReqHeader()
                reqHeader.setUsername(Constants.REQ_USER_NAME)
                reqHeader.setPassword(Constants.REQ_PASSWORD)
                priceListRequestModel.setHeader(reqHeader)

                var reqBody = ReqBody()
                reqBody.setShape(Constants.SHAPE_PEAR)
                priceListRequestModel.setBody(reqBody)
                priceListRequest.setRequest(priceListRequestModel)
                priceListViewModel.getPearPrices(priceListRequest)
                priceListViewModel.pearPriceListLiveData.observe(this@CalculatorActivity) { response ->
                    when (response) {
                        is NetworkResponse.Success -> {

                            hideProgress()
                            val resTemp = response.body
                            if (resTemp.response.header.errorCode == 0) {
                                //Log.e("list", "${resTemp.response.body.price.size}")
                                if (resTemp.response.body.price.isNotEmpty()) {
                                    val listTemp = resTemp.response.body.price
                                    pearShapePriceList.clear()
                                    pearShapePriceList.addAll(listTemp)
                                    AppPreferences.apiRefreshDate = pearShapePriceList[0].date
                                    AppPreferences.pearShapePrice =
                                        Gson().toJson(pearShapePriceList)
                                }

                            }

                        }
                        is NetworkResponse.NetworkError -> {
                            hideProgress()
                        }
                        else -> {
                            hideProgress()
                        }
                    }
                }

            }
        } else {
            pearShapePriceList.clear()
            val typeToken = object : TypeToken<List<Price>>() {}.type
            val tmpRound = Gson().fromJson<List<Price>>(AppPreferences.pearShapePrice, typeToken)
            pearShapePriceList.addAll(tmpRound)
        }
    }

    private fun fetchCurrencyRate() {
        CoroutineScope(Dispatchers.Main).launch {
            val client = CurrencyRateClient.getInstance()
            val loginRepo = CurrencyRateRepo(client)

            val currencyRateViewModel =
                ViewModelProvider(
                    this@CalculatorActivity,
                    CurrencyRateViewModelFactory(loginRepo)
                )[CurrencyRateViewModel::class.java]

            currencyRateViewModel.getSelectedCurrencyRate(AppPreferences.currency)
            currencyRateViewModel.selectedCurrencyRateLiveData.observe(this@CalculatorActivity) { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        Log.e(
                            "selectedCurrencyRateLiveData NetworkResponse.Success----",
                            "$response"
                        )
                        // hideProgress()
                        val res = response.body
                        if (res.success) {
                            val df = DecimalFormat("#.##")
                            val price = df.format(res.rate)
                            binding.tvCurrencyRate.text = price
                            AppPreferences.currencyRate = price.toFloat()
                        }
                    }
                    is NetworkResponse.NetworkError -> {
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun showProgress() {
        try {
            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = ProgressDialog(this@CalculatorActivity)
                progressDialog!!.setMessage("Loading...")
                progressDialog!!.show()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    private fun hideProgress() {
        try {
            if (progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
                progressDialog = null
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
    private fun showPopupMenu() {
        popupMenu?.show()
        val discountItem = popupMenu?.menu?.findItem(R.id.action_discount)
        val premiumItem = popupMenu?.menu?.findItem(R.id.action_premium)

        if (isDiscountSelected) {
            discountItem?.isChecked = true
            premiumItem?.isChecked = false
            updateMenuItemAppearance(discountItem, true)
            updateMenuItemAppearance(premiumItem, false)
        } else {
            discountItem?.isChecked = false
            premiumItem?.isChecked = true
            updateMenuItemAppearance(discountItem, false)
            updateMenuItemAppearance(premiumItem, true)
        }
        popupMenu?.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_discount -> {
                    isDiscountSelected = true
                    item.isChecked = true
                    item.icon!!.setTint(ContextCompat.getColor(this, R.color.color_D4AA60))
                    updateMenuItemAppearance(item, true)
                    updateMenuItemAppearance(premiumItem, false)
                    updateDiscountUI()
                    calculatePrice()
                }
                R.id.action_premium -> {
                    isDiscountSelected = false
                    item.isChecked = true

                    updateMenuItemAppearance(item, true)
                    updateMenuItemAppearance(discountItem, false)
                    updateDiscountUI()
                    calculatePrice()
                }
            }
            true
        }

    }

    private fun updateMenuItemAppearance(menuItem: MenuItem?, isSelected: Boolean) {


        menuItem?.let {
            it.icon?.setTint(ContextCompat.getColor(this, if (isSelected) R.color.colorAccent else R.color.colorAccent))

            val spannableTitle = SpannableString(it.title)
            Log.e("Boolean",",.......1378..."+isSelected)
            Log.e("spannableTitle",",.......1378..."+spannableTitle)

            spannableTitle.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this, if (isSelected) R.color.colorAccent else R.color.color_666666)),
                0,
                spannableTitle.length,
                0
            )
            it.title = spannableTitle
        }
    }

    private fun updateDiscountUI() {
        if (isDiscountSelected) {
            binding.tvDiscountSymbol.setText("-")
            binding.tvPremiumDiscount.text = getString(R.string.txt_discount)
            binding.tvPremiumDiscount.setTextColor(ContextCompat.getColor(this, R.color.color_7E3080))


        } else {
            binding.tvDiscountSymbol.setText("+")
            binding.tvPremiumDiscount.text = getString(R.string.txt_premium)
            binding.tvPremiumDiscount.setTextColor(ContextCompat.getColor(this, R.color.color_7E3080))

        }
    }




}