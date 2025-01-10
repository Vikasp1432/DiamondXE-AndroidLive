package com.dxe.calc.dashboard


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diamondxe.Activity.HomeScreenActivity
import com.diamondxe.Adapter.CurrencyListAdapter
import com.diamondxe.Beans.CountryListModel
import com.diamondxe.Interface.RecyclerInterface
import com.diamondxe.R
import com.diamondxe.Utils.CommonUtility
import com.diamondxe.Utils.Constant
import com.diamondxe.databinding.ActivityJewelleryBinding
import com.dxe.calc.DataBase.MenuDB.PurityDatabase
import com.dxe.calc.DataBase.QuotationsActivity.QuotationDatabase
import com.dxe.calc.DataBase.QuotationsActivity.QuotationModelDao
import com.dxe.calc.DataBase.QuotationsActivity.QuotationModelEntity
import com.dxe.calc.DataBase.QuotationsActivity.QuotationModelManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.round


class JewelleryActivity : AppCompatActivity(), RecyclerInterface {

    private var isMetalCalculationActive = false
    private var isLabourCalculationActive = false
    private var isSolitaireCalculationActive = false
    private var isSideCalculationActive = false
    private var isColStoneCalculationActive = false
    var radiobuttonName: String = "Natural Diamond"
    var finalTotalPrice: String = ""
    var chargeType: String = ""
    var chargeText: String = ""
    var solitaireText: String = ""
    var sideDIAText: String = ""
    var caret: String = ""
    var caretIntent: String = ""
    private lateinit var dataModelManager: QuotationModelManager
    private lateinit var database: QuotationDatabase
    private lateinit var dao: QuotationModelDao
    private lateinit var binding: ActivityJewelleryBinding
    lateinit var currencyListAdapter: CurrencyListAdapter
    var currencyValue: String = ""
    var currencyCode: String = ""
    var currencyRate: String = "1"
    var selectmetalrate: String = ""
    var getLabourFromDB: String = ""
    var getChargesFromDB: String = ""
    var selectedCurrencyCode: String = ""

    var dbCurrencyValue: String = ""
    var dbPurityEditValue: String = ""
    var PurityselectValue: String = ""
    var selectmetalrateDB: String = ""
    var priviousCurrency: String = "1"
    var currentCurrencySelect: String = "1"
    lateinit var recyclerViewCurrency: RecyclerView
    var isreceivedFromDB = false
    var localCurrencyArrayList: ArrayList<CountryListModel> = ArrayList()

    var Metalactualvalue: String = ""
    var Labouractualvalue: String = ""
    var Solitairectualvalue: String = ""
    var Sidealvalue: String = ""
    var Colactualvalue: String = ""
    var chargesactualvalue: String = ""
    var solEdit:String=""
    var sideEdit:String=""
    var colEdit:String=""

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding = ActivityJewelleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener() {
            onBackPressed()
        }

        binding.solitairerategm.setText("0.0")
        binding.sidediarategm.setText("0.0")
        binding.colstonerategm.setText("0.0")

        getEditQuotationDetails()
        binding.menu.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.solitalerNote.setOnClickListener {
            openBottomSheetDialog(R.string.mainsolitaore_note, "solitaire")
        }

        binding.sideDia.setOnClickListener {
            openBottomSheetDialog(R.string.sidediamondnote, "sidedia")
        }
        binding.chargesNote.setOnClickListener {
            openBottomSheetDialog(R.string.otherchargesnote, "charges")
        }

        binding.currencyPriceValue.text = currencyCode

        val radiobuttonNameIntent =
            intent.getStringExtra("radiobuttonName")?.takeIf { it.isNotBlank() } ?: ""
        if (radiobuttonNameIntent != "") {
            if (radiobuttonNameIntent == "Natural Diamond") {
                radiobuttonName = "Natural Diamond"
                binding.natural.isChecked = true
                binding.labGrown.isChecked = false
                binding.natural.buttonTintList =
                    ContextCompat.getColorStateList(this, R.color.color_7E3080)
                binding.labGrown.buttonTintList =
                    ContextCompat.getColorStateList(this, R.color.color_37083B)
            } else {
                radiobuttonName = "Lab Grown"
                binding.natural.isChecked = false
                binding.labGrown.isChecked = true
                binding.labGrown.buttonTintList =
                    ContextCompat.getColorStateList(this, R.color.color_7E3080)
                binding.natural.buttonTintList =
                    ContextCompat.getColorStateList(this, R.color.color_37083B)
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.natural -> {
                    val selectedOption = getString(R.string.natural)
                    binding.natural.buttonTintList =
                        ContextCompat.getColorStateList(this, R.color.color_7E3080)
                    binding.labGrown.buttonTintList =
                        ContextCompat.getColorStateList(this, R.color.color_37083B)
                    radiobuttonName = "Natural Diamond"
                }

                R.id.lab_grown -> {
                    val selectedOption = getString(R.string.lab_grown)
                    binding.labGrown.buttonTintList =
                        ContextCompat.getColorStateList(this, R.color.color_7E3080)
                    binding.natural.buttonTintList =
                        ContextCompat.getColorStateList(this, R.color.color_37083B)
                    radiobuttonName = "Lab Grown"
                }
            }
        }

        binding.dataclear.setOnClickListener() {
            binding.metalwt.setText("")
            binding.metalwt.hint="00.00"
            binding.metalrateop.setText("")
            binding.metalrateop.hint="00.00"

            binding.labour.setText("")
            binding.labour.hint="00.00"
            binding.labourop.setText("")
            binding.labourop.hint="00.00"

            binding.solitaire.setText("")
            binding.solitaire.hint="00.00"
            binding.solitaireop.setText("")
            binding.solitaireop.hint="00.00"
            binding.solitairerategm.setText("")
            binding.solitairerategm.hint="00.00"

            binding.sidedia.setText("")
            binding.sidedia.hint="00.00"
            binding.sidediaop.setText("")
            binding.sidediaop.hint="00.00"
            binding.sidediarategm.setText("")
            binding.sidediarategm.hint="00.00"


            binding.colstonewt.setText("")
            binding.colstonewt.hint="00.00"
            binding.colstonerategm.setText("")
            binding.colstonerategm.hint="00.00"
            binding.colstoneop.setText("")
            binding.colstoneop.hint="00.00"

            binding.totalPrice.text="00"
        }
        database = QuotationDatabase.getDatabase(this)
        dao = database.dataModelDao()
        dataModelManager = QuotationModelManager(dao)

        binding.flagImg.setOnClickListener({

            if (isreceivedFromDB) {
                //getAllSaveData()
                lifecycleScope.launch {
                    val quotationModelEntity = getAllSaveData()
                    priviousCurrency = quotationModelEntity!!.currencyRate
                    selectmetalrate = quotationModelEntity.metalRate
                    getLabourFromDB = quotationModelEntity.labourRate
                    getChargesFromDB = quotationModelEntity.charges

                    Metalactualvalue = quotationModelEntity.metalRate
                    Labouractualvalue = quotationModelEntity.labourRate
                    chargesactualvalue = quotationModelEntity.charges

                    Solitairectualvalue = quotationModelEntity.solitaireRate
                    Sidealvalue = quotationModelEntity.sideRate
                    Colactualvalue = quotationModelEntity.colStoneRate


                }
            }
            initiateCurrencyPopupWindow()

        })
        binding.getQuotation.setOnClickListener()
        {
            if (binding.getQuotation.isEnabled) {
                val intentID = intent.getLongExtra("ID", 0L).takeIf { it != 0L } ?: 0L
                if (Metalactualvalue == "") {
                    Metalactualvalue = binding.metalrategm.text.toString()
                }
                if (Labouractualvalue == "") {
                    Labouractualvalue = binding.labourrategm.text.toString()
                }
                if (chargesactualvalue == "") {
                    chargesactualvalue = binding.charges.text.toString()
                }
                if (Solitairectualvalue == "" || solEdit =="1") {
                    Solitairectualvalue = binding.solitairerategm.text.toString()
                }
                if (Sidealvalue == "" || sideEdit =="1") {
                    Sidealvalue = binding.sidediarategm.text.toString()
                }
                if (Colactualvalue == "" || solEdit =="1" ) {
                    Colactualvalue = binding.colstonerategm.text.toString()
                }

                val metalwt = binding.metalwt.text.toString()
                val metalrategm = Metalactualvalue
                val metalrateop = binding.metalrateop.text.toString()

                val labour = binding.labour.text.toString()
                val labourrategm = Labouractualvalue
                val labourop = binding.labourop.text.toString()

                val solitaire = binding.solitaire.text.toString()
                val solitairerategm = Solitairectualvalue
                val solitaireop = binding.solitaireop.text.toString()

                val sidedia = binding.sidedia.text.toString()
                val sidediarategm = Sidealvalue
                val sidediaop = binding.sidediaop.text.toString()

                val colstonewt = binding.colstonewt.text.toString()
                val colstonerategm = Colactualvalue
                val colstoneop = binding.colstoneop.text.toString()

                val charges = chargesactualvalue
                val tax = binding.tax.text.toString()
                val totalPrice = finalTotalPrice


                val itemName = binding.itemname.text.toString()
                var RBName = radiobuttonName

                val getCurrencySymbol = CommonUtility.getCurrencySymbol(currencyCode)
                val formattedDateTime = getCurrentDateTime()

                /*Log.e("", "================== SAVE DATA CAll =======================================")
                Log.e("metalrategm", "" + metalrategm)
                Log.e("labourrategm", "" + labourrategm)
                Log.e("charges", "" + charges)
                Log.e("solitairerategm", "" + solitairerategm)
                Log.e("sidediarategm", "" + sidediarategm)
                Log.e("colstonerategm", "" + colstonerategm)
                Log.e("", "=============================================================================")*/

                val dataModel = QuotationModelEntity(
                    metalWeight = binding.metalwt.text.toString(),
                    metalRate = metalrategm,
                    metalOutput = metalrateop,
                    labourWeight = labour,
                    labourRate = labourrategm,
                    labourOutput = labourop,
                    solitaireWeight = solitaire,
                    solitaireRate = solitairerategm,
                    solitaireOutput = solitaireop,
                    sideWeight = sidedia,
                    sideRate = sidediarategm,
                    sideOutput = sidediaop,
                    colStoneWeight = colstonewt,
                    colStoneRate = colstonerategm,
                    colStoneOutput = colstoneop,
                    charges = charges,
                    tax = tax,
                    totalPrice = totalPrice,
                    currentdatetime = formattedDateTime,
                    caret = caret,
                    itemName = itemName,
                    radiobuttonName = radiobuttonName,
                    chargeTxt = chargeText,
                    solitairetxt = solitaireText,
                    sidediatxt = sideDIAText,
                    currencyCode = currencyCode,
                    currencysymbol = getCurrencySymbol,
                    currencyvalue = currencyValue,
                    currencyRate = currencyRate)

                saveDataToDatabase(dataModel, intentID)
            } else {
                Toast.makeText(
                    this,
                    "Total price is zero, please update the amounts.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        val getcurrencyCode =
            intent.getStringExtra("currencyCode")?.takeIf { it.isNotBlank() } ?: ""
        val getcurrencyValue =
            intent.getStringExtra("currencyValue")?.takeIf { it.isNotBlank() } ?: ""


        if (Constant.currencyArrayList != null) {
            for (country in Constant.currencyArrayList) {
                if (country.currency == getcurrencyCode) {
                    if (!country.image.equals("", ignoreCase = true)) {
                        Picasso.with(HomeScreenActivity.context)
                            .load(country.image)
                            .into(binding.flagImg)
                    } else {
                    }
                }
            }
        }

        if (getcurrencyCode == "" && getcurrencyValue == "") {
            currencyCode =
                CommonUtility.getGlobalString(HomeScreenActivity.context, "selected_currency_code")
            currencyValue =
                CommonUtility.getGlobalString(HomeScreenActivity.context, "selected_currency_value")
            Log.e("currency Value...", "If.......$currencyValue...$currencyCode")
        } else {
            currencyCode = getcurrencyCode
            currencyValue = getcurrencyValue
        }
        val caretIntent = intent.getStringExtra("caret")?.takeIf { it.isNotBlank() } ?: ""
        val labourrategmIntent =
            intent.getStringExtra("labourrategm")?.takeIf { it.isNotBlank() } ?: "0.0"
        val chargesIntent = intent.getStringExtra("charges")?.takeIf { it.isNotBlank() } ?: "0.0"
        val taxIntent = intent.getStringExtra("tax")?.takeIf { it.isNotBlank() } ?: "0.0"

        CoroutineScope(Dispatchers.IO).launch {

            val purityDao = PurityDatabase.getDatabase(this@JewelleryActivity).purityDao()
            val laborValueFromDb = purityDao.getValueByType("labor")
            if (laborValueFromDb != null) {
                getLabourFromDB = laborValueFromDb
            }

            val chargesValueFromDb = purityDao.getValueByType("charges")
            if (chargesValueFromDb != null) {
                getChargesFromDB = chargesValueFromDb
            }

            val taxValueFromDb = purityDao.getValueByType("tax")
            val purityListFromDb = purityDao.getAllPurities()


            withContext(Dispatchers.Main) {
                try {

                    if (labourrategmIntent == "0.0") {
                        binding.labourrategm.setText(laborValueFromDb)
                    } else {
                        binding.labourrategm.setText(labourrategmIntent)
                    }

                    if (chargesIntent == "0.0") {
                        binding.charges.setText(chargesValueFromDb)
                    } else {
                        binding.charges.setText(chargesIntent)
                    }

                    if (taxIntent == "0.0") {
                        binding.tax.setText(taxValueFromDb)
                    } else {
                        binding.tax.setText(taxIntent)
                    }
                    val purityTypes: List<String> = if (purityListFromDb.isNotEmpty()) {
                        purityListFromDb.map { it.purityType }.also {
                            Log.e("purityTypes", "Database values: $it")

                        }
                    } else {
                        listOf("18K","22K", "14K", "9K", "Other").also {
                            Log.e("purityTypes", "No purities in DB, using predefined values: $it")
                        }
                    }

                    val filteredPurityType = if (caretIntent.isNotEmpty()) {
                        purityTypes.find { it.equals(caretIntent, ignoreCase = true) }
                    } else null
                    val adapter = ArrayAdapter(
                        this@JewelleryActivity,
                        android.R.layout.simple_spinner_item,
                        purityTypes
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinner.adapter = adapter

                    filteredPurityType?.let {
                        val defaultIndex = purityTypes.indexOf(it)
                        if (defaultIndex != -1) {
                            binding.spinner.setSelection(defaultIndex)
                            Log.e("SpinnerSelection", "Set default selection to: $it")
                            dbPurityEditValue = it

                        } else {
                            Log.e(
                                "SpinnerSelection",
                                "Caret intent value not found in purity types: $caretIntent"
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.e("Error", "Exception in setting values: ${e.message}")
                }
            }
        }
        binding.spinner.setOnTouchListener { _, _ ->
            isreceivedFromDB = false
            false
        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (binding.spinner.adapter?.count == 0) {
                    Log.e("SpinnerSelection", "No items in the spinner.")
                    return
                }

                val selectedPurityType = parent?.getItemAtPosition(position).toString()
                PurityselectValue = selectedPurityType

                CoroutineScope(Dispatchers.IO).launch {
                    val purityDao = PurityDatabase.getDatabase(this@JewelleryActivity).purityDao()
                    val purityListFromDb = purityDao.getAllPurities()
                    val purityMap = purityListFromDb.associate { it.purityType to it.purityValue }
                    val selectedPurityValue = purityMap[selectedPurityType]

                    caret = selectedPurityType
                    withContext(Dispatchers.Main) {
                        try {
                            selectedPurityValue?.let {
                                if (isreceivedFromDB) {
                                    lifecycleScope.launch {
                                        val quotationModelEntity = getAllSaveData()
                                        selectmetalrate =
                                            quotationModelEntity!!.metalRate.toString()
                                        getLabourFromDB = quotationModelEntity.labourRate.toString()
                                        selectmetalrateDB =
                                            quotationModelEntity.metalRate.toString()
                                        priviousCurrency = quotationModelEntity.currencyRate
                                        getChargesFromDB = quotationModelEntity.charges
                                    }

                                } else {
                                    val finalvalue = calculateMetalRateFinalValue(
                                        priviousCurrency.toDouble(),
                                        currentCurrencySelect.toDouble(),
                                        it.toDouble()
                                    )

                                    val FinalValueCharges = String.format("%.2f", finalvalue)
                                    binding.metalrategm.setText(FinalValueCharges)

                                }


                            } ?: run {
                                binding.metalrategm.setText("0.0")
                            }
                            calculateMetalOutput()
                        } catch (e: Exception) {
                            Log.e("Error", "Error in Spinner selection: ${e.message}")
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        setupListeners()
    }

    private var mDropdown: PopupWindow? = null
    var mInflater: LayoutInflater? = null

    // Button pop;
    private fun initiateCurrencyPopupWindow(): PopupWindow {
        try {

            if (mDropdown != null && mDropdown!!.isShowing()) {
                mDropdown!!.dismiss()
            }
            mInflater =
                applicationContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout: View = mInflater!!.inflate(R.layout.custom_menu_currency, null)

            recyclerViewCurrency =
                layout.findViewById<RecyclerView>(R.id.recycler_view_currency)
            val layoutManager = LinearLayoutManager(HomeScreenActivity.context)
            recyclerViewCurrency.layoutManager = layoutManager

            updateLocalCurrencyList()

            val fixedWidth: Int = dpToPx(200) // Width in dp
            mDropdown = PopupWindow(layout, fixedWidth, FrameLayout.LayoutParams.WRAP_CONTENT, true)
            if (binding.flagImg != null) {
                mDropdown!!.showAsDropDown(binding.flagImg, 5, -40)
                //dimOverlay.visibility = View.VISIBLE
            } else {
                Log.e("PopupWindow", "flag_img is null")
            }

            mDropdown!!.setOnDismissListener(PopupWindow.OnDismissListener {
                //dimOverlay.visibility = View.GONE
            })

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return mDropdown!!
    }

    fun dpToPx(dp: Int): Int {
        val density = HomeScreenActivity.context.resources.displayMetrics.density
        return Math.round(dp * density)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateTime(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss")
        return currentDateTime.format(formatter)
    }

    suspend fun getAllSaveData(): QuotationModelEntity? {
        return withContext(Dispatchers.IO) {
            var existingDataModel: QuotationModelEntity? = null

            if (intent?.extras != null) {
                val intentID = intent.getLongExtra("ID", 0L).takeIf { it != 0L } ?: 0L

                if (intentID != 0L) {
                    val dataModelCheck = dataModelManager.getDataModelById(intentID)
                    existingDataModel =
                        dataModelManager.getDataModelByItemName(dataModelCheck!!.id.toString())

                    if (existingDataModel != null) {
                        dbCurrencyValue = existingDataModel.currencyCode
                    }
                } else {
                }
            } else {
            }
            existingDataModel
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveDataToDatabase(dataModel: QuotationModelEntity, DataID: Long) {
        dataModelManager = QuotationModelManager(dao)
        var savedDataID: Long = 0
        lifecycleScope.launch {
            if (DataID != null) {
                val dataModelCheck = dataModelManager.getDataModelById(DataID!!)
                if (dataModelCheck != null) {
                    val existingDataModel =
                        dataModelManager.getDataModelByItemName(dataModelCheck.id.toString())
                    existingDataModel!!.metalWeight = dataModel.metalWeight
                    existingDataModel.metalRate = dataModel.metalRate
                    existingDataModel.metalOutput = dataModel.metalOutput
                    existingDataModel.labourWeight = dataModel.labourWeight
                    existingDataModel.labourRate = dataModel.labourRate
                    existingDataModel.labourOutput = dataModel.labourOutput
                    existingDataModel.solitaireWeight = dataModel.solitaireWeight
                    existingDataModel.solitaireRate = dataModel.solitaireRate
                    existingDataModel.solitaireOutput = dataModel.solitaireOutput
                    existingDataModel.sideWeight = dataModel.sideWeight
                    existingDataModel.sideRate = dataModel.sideRate
                    existingDataModel.sideOutput = dataModel.sideOutput
                    existingDataModel.colStoneWeight = dataModel.colStoneWeight
                    existingDataModel.colStoneRate = dataModel.colStoneRate
                    existingDataModel.colStoneOutput = dataModel.colStoneOutput
                    existingDataModel.charges = dataModel.charges
                    existingDataModel.tax = dataModel.tax
                    existingDataModel.totalPrice = dataModel.totalPrice
                    existingDataModel.caret = dataModel.caret
                    existingDataModel.radiobuttonName = dataModel.radiobuttonName
                    existingDataModel.chargeTxt = dataModel.chargeTxt
                    existingDataModel.solitairetxt = dataModel.solitairetxt
                    existingDataModel.sidediatxt = dataModel.sidediatxt
                    existingDataModel.currentdatetime = dataModel.currentdatetime
                    existingDataModel.currencyCode = dataModel.currencyCode
                    existingDataModel.currencyRate = dataModel.currencyRate
                    existingDataModel.currencyvalue = dataModel.currencyvalue
                    existingDataModel.currencysymbol = dataModel.currencysymbol
                    existingDataModel.itemName = dataModel.itemName
                    dataModelManager.updateDataModel(existingDataModel)
                    savedDataID = existingDataModel.id
                    passDataToIntent(savedDataID)

                } else {

                    dataModelManager.addDataModel(dataModel)
                    var savedDataIDDB = dataModelManager.addDataModel(dataModel)
                    savedDataID = savedDataIDDB
                    passDataToIntent(savedDataID)
                }

            } else {
                var savedDataIDDB = dataModelManager.addDataModel(dataModel)
                passDataToIntent(savedDataIDDB)
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun passDataToIntent(savedDataID: Long) {

        if (Metalactualvalue == "") {
            Metalactualvalue = binding.metalrategm.text.toString()
        }
        if (Labouractualvalue == "") {
            Labouractualvalue = binding.labourrategm.text.toString()
        }
        if (Solitairectualvalue == "" || solEdit =="1") {
            Solitairectualvalue = binding.solitairerategm.text.toString()
        }
        if (Sidealvalue == "" || sideEdit=="1") {
            Sidealvalue = binding.sidediarategm.text.toString()
        }
        if (Colactualvalue == ""|| colEdit=="1") {
            Colactualvalue = binding.colstonerategm.text.toString()
        }
        if (chargesactualvalue == "") {
            chargesactualvalue = binding.charges.text.toString()
        }

        val metalwt = binding.metalwt.text.toString()
        val metalrategm = Metalactualvalue
        val metalrateop = binding.metalrateop.text.toString()

        val labour = binding.labour.text.toString()
        val labourrategm = Labouractualvalue
        val labourop = binding.labourop.text.toString()

        val solitaire = binding.solitaire.text.toString()
        val solitairerategm = Solitairectualvalue
        val solitaireop = binding.solitaireop.text.toString()

        val sidedia = binding.sidedia.text.toString()
        val sidediarategm = Sidealvalue
        val sidediaop = binding.sidediaop.text.toString()

        val colstonewt = binding.colstonewt.text.toString()
        val colstonerategm = Colactualvalue
        val colstoneop = binding.colstoneop.text.toString()

        val charges = chargesactualvalue
        val tax = binding.tax.text.toString()
        val totalPrice = finalTotalPrice
        val formattedDateTime = getCurrentDateTime()
        val itemName = binding.itemname.text.toString()

        val intent = Intent(this, GetQuotationActivity::class.java).apply {
            putExtra("metalwt", metalwt)
            putExtra("ID", savedDataID)
            putExtra("metalrategm", metalrategm)
            putExtra("metalrateop", metalrateop)
            putExtra("labour", labour)
            putExtra("labourrategm", labourrategm)
            putExtra("labourop", labourop)
            putExtra("solitaire", solitaire)
            putExtra("solitairerategm", solitairerategm)
            putExtra("solitaireop", solitaireop)
            putExtra("sidedia", sidedia)
            putExtra("sidediarategm", sidediarategm)
            putExtra("sidediaop", sidediaop)
            putExtra("colstonewt", colstonewt)
            putExtra("colstonerategm", colstonerategm)
            putExtra("colstoneop", colstoneop)
            putExtra("charges", charges)
            putExtra("tax", tax)
            putExtra("totalPrice", totalPrice)
            putExtra("date", formattedDateTime)
            putExtra("itemName", itemName)
            putExtra("radiobuttonName", radiobuttonName)
            putExtra("caret", caret)
            putExtra("chargeText", chargeText)
            putExtra("solitaireText", solitaireText)
            putExtra("sideDIAText", sideDIAText)
            putExtra("currencyCode", currencyCode)
            putExtra("currencyValue", currencyValue)
            putExtra("currencyRate", currencyRate)

        }
        solEdit="0"
        sideEdit="0"
        colEdit="0"
        startActivity(intent)
    }

    fun setupListeners() {



        binding.solitairerategm.setOnFocusChangeListener{v,hasFocus ->
            if (hasFocus) {
                solEdit="1"
                println("Course is focused.")
            } else {
                println("Course is not focused.")
            }
        }
        binding.sidediarategm.setOnFocusChangeListener{v,hasFocus ->
            if (hasFocus) {
                sideEdit="1"
                println("Course is focused.")
            } else {
                println("Course is not focused.")
            }
        }
        binding.colstonerategm.setOnFocusChangeListener{v,hasFocus ->
            if (hasFocus) {
                colEdit="1"
                println("Course is focused.")
            } else {
                println("Course is not focused.")
            }
        }
        //Metal wt
        binding.metalwt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!isMetalCalculationActive) {
                    isMetalCalculationActive = true
                    val input = s.toString()

                    /* if (input != binding.labour.text.toString()) {
                         binding.labour.setText(input)
                         Log.e("metalwt", "...." + input)
                     }*/
                    calculateMetalOutput()
                    isMetalCalculationActive = false
                }
            }
        })

        binding.metalrategm.doAfterTextChanged {
            if (!isMetalCalculationActive) {
                isMetalCalculationActive = true
                calculateMetalOutput()

                isMetalCalculationActive = false
            }
            calculateTotalPrice()
        }

        binding.metalrateop.doAfterTextChanged {
            if (!isMetalCalculationActive) {
                isMetalCalculationActive = true
                reverseCalculateMetal()

                isMetalCalculationActive = false
            }
            calculateTotalPrice()
        }

        //Labour
        binding.labour.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!isLabourCalculationActive) {
                    isLabourCalculationActive = true
                    val input = s.toString()

                    /*if (input != binding.metalwt.text.toString()) {
                        binding.metalwt.setText(input)
                    }*/
                    calculateLabourOutput()

                    isLabourCalculationActive = false
                }
            }
        })
        // Labour rate change listener
        binding.labourrategm.doAfterTextChanged {
            if (!isLabourCalculationActive) {
                isLabourCalculationActive = true
                calculateLabourOutput()
                isLabourCalculationActive = false
            }
        }

        // Labour output change listener
        binding.labourop.doAfterTextChanged {
            if (!isLabourCalculationActive) {
                reverseCalculateLabour()
            }

            calculateTotalPrice()
        }

        //Solitaire wt
        binding.solitaire.doAfterTextChanged {
            if (!isSolitaireCalculationActive) {
                calculateSolitaireOutput()
            }
        }

        binding.solitairerategm.doAfterTextChanged {

            if (!isSolitaireCalculationActive) {
                Log.e("", "Call...........921")

                /*if (Solitairectualvalue != "") {
                    Solitairectualvalue = binding.solitairerategm.text.toString()
                }*/
                calculateSolitaireOutput()
            }
        }
        binding.solitaireop.doAfterTextChanged {
            if (!isSolitaireCalculationActive) {
                reverseCalculateSolitaire()
            }
            calculateTotalPrice()
        }

        // Side DIA
        binding.sidedia.doAfterTextChanged {
            if (!isSideCalculationActive) {
                calculateSideOutput()
            }
        }
        binding.sidediarategm.doAfterTextChanged {
            if (!isSideCalculationActive) {
                calculateSideOutput()
            }
        }
        binding.sidediaop.doAfterTextChanged {
            if (!isSideCalculationActive) {
                reverseCalculateSide()
            }
            calculateTotalPrice()
        }

        //Col. stone wt.
        binding.colstonewt.doAfterTextChanged {
            if (!isColStoneCalculationActive) {
                calculateColStoneOutput()
            }
        }
        binding.colstonerategm.doAfterTextChanged {
            if (!isColStoneCalculationActive) {
                calculateColStoneOutput()
            }
        }
        binding.colstoneop.doAfterTextChanged {
            if (!isColStoneCalculationActive) {
                reverseCalculateColStone()
            }
            calculateTotalPrice()
        }

        // Charges
        binding.charges.doAfterTextChanged {
            calculateTotalPrice()
        }
        binding.tax.doAfterTextChanged {
            calculateTotalPrice()
        }
    }

    private fun openBottomSheetDialog(headerTextResId: Int, type: String) {
        Log.e("Bottom sheet", "Open....22..........")
        val dialog = BottomSheetDialog(this, R.style.DialogStyle)

        val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        val headertext = view.findViewById<TextView>(R.id.headertext)
        val bottomEdit = view.findViewById<EditText>(R.id.bottom_edit)

        headertext.setText(headerTextResId)

        when (type) {
            "solitaire" -> {
                bottomEdit.hint = "Enter Solitaire Note"
            }

            "sidedia" -> {
                bottomEdit.hint = "Enter Side DIA Note"
            }

            "charges" -> {
                bottomEdit.hint = "Enter Other Charges Note"
            }
        }

        bottomEdit.requestFocus()
        bottomEdit.postDelayed({
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(bottomEdit, InputMethodManager.SHOW_IMPLICIT)
        }, 100)


        val btnClose = view.findViewById<TextView>(R.id.btn_cancel)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        val btnSave = view.findViewById<TextView>(R.id.btn_done)
        btnSave.setOnClickListener {

            Log.e("type", "" + type)
            Log.e("bottomEdit", "" + bottomEdit)

            if (type == "charges") {
                chargeText = bottomEdit.text.toString()
                chargeType = type
            }
            if (type == "solitaire") {
                solitaireText = bottomEdit.text.toString()

            }
            if (type == "sidedia") {
                sideDIAText = bottomEdit.text.toString()
            }

            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)

        dialog.show()
    }

    private val decimalFormat = DecimalFormat("#.00")

    private fun calculateMetalOutput() {
        val weight = binding.metalwt.text.toString().toDoubleOrNull() ?: 0.0
        val rate = binding.metalrategm.text.toString().toDoubleOrNull() ?: 0.0
        val output = weight * rate
        //Log.e("metal o/p", "....   " + decimalFormat.format(output))
        val formattedOutput = if (output == 0.0) "00" else decimalFormat.format(output)
        if (formattedOutput == "00") {
            binding.metalrateop.hint = "00"
        } else {
            val integerPart = output.toInt()
            val fractionalPart = output - integerPart
            val roundedOutput = if (fractionalPart > 0.50) {
                integerPart + 1
            } else {
                integerPart
            }
            binding.metalrateop.setText(roundedOutput.toString())
        }
    }

    private fun reverseCalculateMetal() {
        if (isMetalCalculationActive) return

        val output = binding.metalrateop.text.toString().toDoubleOrNull() ?: 0.0
        val weight = binding.metalwt.text.toString().toDoubleOrNull() ?: 0.0
        if (weight != 0.0) {
            val rate = output / weight
            binding.metalrategm.setText(decimalFormat.format(rate))
        }
    }

    //Labour calculation
    private fun calculateLabourOutput() {

        val weight = binding.labour.text.toString().toDoubleOrNull() ?: 0.0
        val rate = binding.labourrategm.text.toString().toDoubleOrNull() ?: 0.0
        val output = weight * rate
        val formattedOutput = if (output == 0.0) "00" else decimalFormat.format(output)
        if (formattedOutput == "00") {
            binding.labourop.hint = "00"
        } else {
            val integerPart = output.toInt()
            val fractionalPart = output - integerPart
            val roundedOutput = if (fractionalPart > 0.50) {
                integerPart + 1
            } else {
                integerPart
            }

            binding.labourop.setText(roundedOutput.toString())
        }

    }

    private fun reverseCalculateLabour() {
        if (isLabourCalculationActive) return
        isLabourCalculationActive = true

        val output = binding.labourop.text.toString().toDoubleOrNull() ?: 0.0
        val weight = binding.labour.text.toString().toDoubleOrNull() ?: 0.0
        if (weight != 0.0) {
            val rate = output / weight
            Log.e("Labour..", "reverseCalculateLabour: $rate")
            binding.labourrategm.setText(decimalFormat.format(rate))
        }

        isLabourCalculationActive = false
    }

    //Solitaire calculation
    private fun calculateSolitaireOutput() {
        if (isSolitaireCalculationActive) return

        isSolitaireCalculationActive = true

        val weight = binding.solitaire.text.toString().toDoubleOrNull() ?: 0.0
        val rate = binding.solitairerategm.text.toString().toDoubleOrNull() ?: 0.0
        val output = weight * rate
        val integerPart = output.toInt()
        val fractionalPart = output - integerPart
        val roundedOutput = if (fractionalPart > 0.50) {
            integerPart + 1
        } else {
            integerPart
        }
        binding.solitaireop.setText(roundedOutput.toString())

        isSolitaireCalculationActive = false
    }

    private fun reverseCalculateSolitaire() {
        if (isSolitaireCalculationActive) return

        isSolitaireCalculationActive = true

        val output = binding.solitaireop.text.toString().toDoubleOrNull() ?: 0.0
        val weight = binding.solitaire.text.toString().toDoubleOrNull() ?: 0.0
        if (weight != 0.0) {
            val rate = output / weight
            Log.e("Sol..........", ".........." + decimalFormat.format(rate))
            binding.solitairerategm.setText(decimalFormat.format(rate))
        }

        isSolitaireCalculationActive = false
    }

    //Side DIA Calculation
    private fun calculateSideOutput() {
        if (isSideCalculationActive) return

        isSideCalculationActive = true

        val weight = binding.sidedia.text.toString().toDoubleOrNull() ?: 0.0
        val rate = binding.sidediarategm.text.toString().toDoubleOrNull() ?: 0.0
        val output = weight * rate
        //val roundedOutput = Math.round(output)
        val integerPart = output.toInt()
        val fractionalPart = output - integerPart
        val roundedOutput = if (fractionalPart > 0.50) {
            integerPart + 1
        } else {
            integerPart
        }
        binding.sidediaop.setText(roundedOutput.toString())

        isSideCalculationActive = false
    }

    private fun reverseCalculateSide() {
        if (isSideCalculationActive) return

        isSideCalculationActive = true

        val output = binding.sidediaop.text.toString().toDoubleOrNull() ?: 0.0
        val weight = binding.sidedia.text.toString().toDoubleOrNull() ?: 0.0
        if (weight != 0.0) {
            val rate = output / weight
            binding.sidediarategm.setText(decimalFormat.format(rate))
        }

        isSideCalculationActive = false
    }

    //Col Stone wt.
    private fun calculateColStoneOutput() {
        if (isColStoneCalculationActive) return

        isColStoneCalculationActive = true

        val weight = binding.colstonewt.text.toString().toDoubleOrNull() ?: 0.0
        val rate = binding.colstonerategm.text.toString().toDoubleOrNull() ?: 0.0
        val output = weight * rate
        //val roundedOutput = Math.round(output)
        val integerPart = output.toInt()
        val fractionalPart = output - integerPart
        val roundedOutput = if (fractionalPart > 0.50) {
            integerPart + 1
        } else {
            integerPart
        }
        binding.colstoneop.setText(roundedOutput.toString())

        isColStoneCalculationActive = false
    }

    private fun reverseCalculateColStone() {
        if (isColStoneCalculationActive) return

        isColStoneCalculationActive = true

        val output = binding.colstoneop.text.toString().toDoubleOrNull() ?: 0.0
        val weight = binding.colstonewt.text.toString().toDoubleOrNull() ?: 0.0
        if (weight != 0.0) {
            val rate = output / weight
            binding.colstonerategm.setText(decimalFormat.format(rate))
        }
        isColStoneCalculationActive = false
    }

    @SuppressLint("SetTextI18n")
    private fun calculateTotalPrice() {
        val metalOutput = binding.metalrateop.text.toString().toDoubleOrNull() ?: 0.0
        val solitaireOutput = binding.solitaireop.text.toString().toDoubleOrNull() ?: 0.0
        val labuorOutput = binding.labourop.text.toString().toDoubleOrNull() ?: 0.0
        val sideOutput = binding.sidediaop.text.toString().toDoubleOrNull() ?: 0.0
        val colStoneOutput = binding.colstoneop.text.toString().toDoubleOrNull() ?: 0.0

        var totalPrice = metalOutput + solitaireOutput + sideOutput + colStoneOutput + labuorOutput
        var chargeAmount = binding.charges.text.toString().toDoubleOrNull() ?: 0.0
        totalPrice += chargeAmount

        val taxPercentage = binding.tax.text.toString().toDoubleOrNull() ?: 0.0
        val taxAmount = (totalPrice * taxPercentage) / 100
        totalPrice += taxAmount

        totalPrice = Math.round(totalPrice).toDouble()


        val integerPart = totalPrice.toInt()
        val fractionalPart = totalPrice - integerPart
        val roundedOutput = if (fractionalPart > 0.50) {
            integerPart + 1
        } else {
            integerPart
        }
        finalTotalPrice = roundedOutput.toString()
        val getCurrencySymbol = CommonUtility.getCurrencySymbol(currencyCode)
        binding.totalPrice.text ="$getCurrencySymbol ${formatAmountWithCommas(finalTotalPrice)}"
        binding.currencyPriceValue.text = "($currencyCode)"
        val isEnabled = totalPrice > 0
        binding.getQuotation.isEnabled = isEnabled

        if (isEnabled) {
            binding.getQuotation.setBackgroundResource(R.drawable.rignbutton)
        } else {
            binding.savetext.setTextColor(getColor(R.color.color_F0F0F0))
            binding.getQuotation.setBackgroundResource(R.drawable.disable_button)
        }
    }

    fun formatAmountWithCommas(amount: String): String {
        val formatter = DecimalFormat("#,##,###")
        val parsedAmount = amount.toBigDecimalOrNull() ?: return amount
        return formatter.format(parsedAmount)
    }

    fun getEditQuotationDetails() {
        if (intent?.extras != null) {

            isreceivedFromDB = true
            val metalwt = intent.getStringExtra("metalwt")?.takeIf { it.isNotBlank() } ?: "0.0"
            val metalrategm =
                intent.getStringExtra("metalrategm")?.takeIf { it.isNotBlank() } ?: "0.0"
            val metalrateop =
                intent.getStringExtra("metalrateop")?.takeIf { it.isNotBlank() } ?: "0.0"
            val currencyCode =
                intent.getStringExtra("currencyCode")?.takeIf { it.isNotBlank() } ?: "0.0"
            val labour = intent.getStringExtra("labour")?.takeIf { it.isNotBlank() } ?: "0.0"
            val labourrategm =
                intent.getStringExtra("labourrategm")?.takeIf { it.isNotBlank() } ?: "0.0"
            val labourop = intent.getStringExtra("labourop")?.takeIf { it.isNotBlank() } ?: "0.0"
            val solitaire = intent.getStringExtra("solitaire")?.takeIf { it.isNotBlank() } ?: "0.0"
            val solitairerategm =
                intent.getStringExtra("solitairerategm")?.takeIf { it.isNotBlank() } ?: "0.0"
            val solitaireop =
                intent.getStringExtra("solitaireop")?.takeIf { it.isNotBlank() } ?: "0.0"
            val sidedia = intent.getStringExtra("sidedia")?.takeIf { it.isNotBlank() } ?: "0.0"
            val sidediarategm =
                intent.getStringExtra("sidediarategm")?.takeIf { it.isNotBlank() } ?: "0.0"
            val sidediaop = intent.getStringExtra("sidediaop")?.takeIf { it.isNotBlank() } ?: "0.0"

            val colstonewt =
                intent.getStringExtra("colstonewt")?.takeIf { it.isNotBlank() } ?: "0.0"
            val colstonerategm =
                intent.getStringExtra("colstonerategm")?.takeIf { it.isNotBlank() } ?: "0.0"
            val colstoneop =
                intent.getStringExtra("colstoneop")?.takeIf { it.isNotBlank() } ?: "0.0"
            val tax = intent.getStringExtra("tax")?.takeIf { it.isNotBlank() } ?: "0"
            val itemName = intent.getStringExtra("itemName")?.takeIf { it.isNotBlank() } ?: ""
            val caret = intent.getStringExtra("caret")?.takeIf { it.isNotBlank() } ?: ""

            selectedCurrencyCode = currencyCode
            binding.metalwt.setText(metalwt)
            binding.metalrategm.setText(metalrategm)
            binding.metalrateop.setText(metalrateop)
            binding.labour.setText(labour)
            binding.labourrategm.setText(labourrategm)
            binding.labourop.setText(labourop)
            selectmetalrate = metalrategm
            getLabourFromDB = labourrategm
            binding.solitaire.setText(solitaire)
            binding.solitairerategm.setText(solitairerategm)
            binding.solitaireop.setText(solitaireop)
            binding.sidedia.setText(sidedia)
            binding.sidediarategm.setText(sidediarategm)
            binding.sidediaop.setText(sidediaop)
            binding.colstonewt.setText(colstonewt)
            binding.colstonerategm.setText(colstonerategm)
            binding.colstoneop.setText(colstoneop)
            binding.totalPrice.text = tax
            binding.itemname.setText(itemName)

            caretIntent = caret

        } else {
        }
    }


    fun calculateMetalRateFinalValue(
        previousCurrency: Double,
        currencyRate: Double,
        selectMetalRate: Double
    ): Double {
        val estimateValue = previousCurrency / currencyRate
        //Log.e("estimateValue", ".......$estimateValue")
        val metalRateFinalValue = estimateValue * selectMetalRate

        return metalRateFinalValue
    }

    fun updateLocalCurrencyList() {
        localCurrencyArrayList.clear()
        for (i in Constant.currencyArrayList.indices) {
            if (Constant.currencyArrayList[i].currency != selectedCurrencyCode) {
                localCurrencyArrayList.add(Constant.currencyArrayList[i])
            } else {
            }
        }
        currencyListAdapter = CurrencyListAdapter(localCurrencyArrayList, this, this)
        recyclerViewCurrency.adapter = currencyListAdapter
        currencyListAdapter.notifyDataSetChanged()
    }

    fun currencyConverter(value: String, currencyCode: String, subTotalAmount: String): String {
        val currencyValue = value.toDouble()
        val subTotal = subTotalAmount.toDouble()

        val valueType = if (currencyCode.equals("INR", ignoreCase = true)) {
            currencyValue
        } else {
            currencyValue
        }
        val finalAmount = subTotal / valueType
        //val subTotalFormat = String.format("%.2f", finalAmount)
        val roundedAmount = round(finalAmount)
        return roundedAmount.toInt().toString()
    }

    override fun itemClick(position: Int, action: String?) {

        if (action.equals("countryType", ignoreCase = true)) {
            val model = localCurrencyArrayList[position]
            currencyValue = model.value
            currencyCode = model.currency
            currencyRate = model.currencyRate
            selectedCurrencyCode = model.currency
            currentCurrencySelect = model.currencyRate.toString()
           /* Log.e(
                "************",
                "================== FLAG CHANGE DATA BEFORE =============================================="
            )
            Log.e("priviousCurrency", "priviousCurrency   ...." + priviousCurrency.toDouble())
            Log.e(
                "currentCurrencySelect",
                "currentCurrencySelect  ...." + currentCurrencySelect.toDouble()
            )
            Log.e("selectmetalrate", "Metalactualvalue  ...." + Metalactualvalue.toDouble())
            Log.e("getChargesFromDB ..", "getChargesFromDB  ...." + getChargesFromDB)
            Log.e("Labouractualvalue ..", "Labouractualvalue  ...." + Labouractualvalue)
            Log.e("chargesactualvalue ..", "chargesactualvalue   ...." + chargesactualvalue)
            Log.e("Solitairectualvalue ..", "Solitairectualvalue  ...." + Solitairectualvalue)
            Log.e("Sidealvalue ..", "Sidealvalue  ...." + Sidealvalue)
            Log.e("Colactualvalue ..", "Colactualvalue  ...." + Colactualvalue)


            Log.e(
                "************",
                "=================================================================="
            )*/

            val finalvalueMetal = calculateMetalRateFinalValue(
                priviousCurrency.toDouble(), currentCurrencySelect.toDouble(),
                Metalactualvalue.toDouble()
            )
            Metalactualvalue = finalvalueMetal.toString()

            val finalvalueLabour = calculateMetalRateFinalValue(
                priviousCurrency.toDouble(), currentCurrencySelect.toDouble(),
                Labouractualvalue.toDouble()
            )
            Labouractualvalue = finalvalueLabour.toString()

            val finalvalueCharges = calculateMetalRateFinalValue(
                priviousCurrency.toDouble(), currentCurrencySelect.toDouble(),
                chargesactualvalue.toDouble()
            )

            chargesactualvalue = finalvalueCharges.toString()

            val finalvalueSolitaire = calculateMetalRateFinalValue(
                priviousCurrency.toDouble(), currentCurrencySelect.toDouble(),
                Solitairectualvalue.toDouble()
            )
            Solitairectualvalue = finalvalueSolitaire.toString()
            val finalvalueSide = calculateMetalRateFinalValue(
                priviousCurrency.toDouble(), currentCurrencySelect.toDouble(),
                Sidealvalue.toDouble()
            )
            Sidealvalue = finalvalueSide.toString()
            val finalvalueCol = calculateMetalRateFinalValue(
                priviousCurrency.toDouble(), currentCurrencySelect.toDouble(),
                Colactualvalue.toDouble()
            )
            Colactualvalue = finalvalueCol.toString()
            val FinalValueMetal = String.format("%.2f", finalvalueMetal)
            val FinalValueLabour = String.format("%.2f", finalvalueLabour)
            val FinalValueCharges = String.format("%.2f", finalvalueCharges)
            val FinalValueSolitaire = String.format("%.2f", finalvalueSolitaire)
            val FinalValueSideDIA = String.format("%.2f", finalvalueSide)
            val FinalValueCOL = String.format("%.2f", finalvalueCol)
            binding.metalrategm.setText(FinalValueMetal)
            binding.labourrategm.setText(FinalValueLabour)
            binding.charges.setText(FinalValueCharges)
            binding.solitairerategm.setText(FinalValueSolitaire)
            binding.sidediarategm.setText(FinalValueSideDIA)
            binding.colstonerategm.setText(FinalValueCOL)

            if (!model.image.equals("", ignoreCase = true)) {
                Picasso.with(HomeScreenActivity.context)
                    .load(model.image)
                    .into(binding.flagImg)
            }
            if (mDropdown != null && mDropdown!!.isShowing) {
                mDropdown!!.dismiss()
            } else {
                Log.e("PopupWindow", "mDropdown is null or not showing")
            }
            calculateTotalPrice()
            updateLocalCurrencyList()
        }
    }
}

