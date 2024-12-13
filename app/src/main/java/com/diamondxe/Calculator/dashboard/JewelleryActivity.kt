package com.dxe.calc.dashboard


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.diamondxe.R
import com.diamondxe.databinding.ActivityJewelleryBinding
import com.dxe.calc.DataBase.MenuDB.PurityDatabase
import com.dxe.calc.DataBase.QuotationsActivity.QuotationDatabase
import com.dxe.calc.DataBase.QuotationsActivity.QuotationModelDao
import com.dxe.calc.DataBase.QuotationsActivity.QuotationModelEntity
import com.dxe.calc.DataBase.QuotationsActivity.QuotationModelManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class JewelleryActivity : AppCompatActivity() {

    private var isMetalCalculationActive = false
    private var isLabourCalculationActive = false
    private var isSolitaireCalculationActive = false
    private var isSideCalculationActive = false
    private var isColStoneCalculationActive = false
    var radiobuttonName:String ="Natural Diamond"
    var finalTotalPrice:String=""
    var chargeType:String=""
    var chargeText:String=""
    var solitaireText:String=""
    var sideDIAText:String=""
    var caret:String=""
    var caretIntent:String=""
    private lateinit var dataModelManager: QuotationModelManager
    private lateinit var database: QuotationDatabase
    private lateinit var dao: QuotationModelDao
    private lateinit var binding: ActivityJewelleryBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding=ActivityJewelleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener(){
            onBackPressed()
        }

        getEditQuotationDetails()
        binding.menu.setOnClickListener { startActivity(Intent(this, MenuActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)}

        binding.solitalerNote.setOnClickListener {
            openBottomSheetDialog(R.string.mainsolitaore_note, "solitaire")
        }

        binding.sideDia.setOnClickListener {
            openBottomSheetDialog(R.string.sidediamondnote, "sidedia")
        }
        binding.chargesNote.setOnClickListener {
            openBottomSheetDialog(R.string.otherchargesnote, "charges")
        }

        val radiobuttonNameIntent = intent.getStringExtra("radiobuttonName")?.takeIf { it.isNotBlank() } ?: ""
        if (radiobuttonNameIntent!="")
        {
            if(radiobuttonNameIntent=="Natural Diamond")
            {
                radiobuttonName="Natural Diamond"
                binding.natural.isChecked=true
                binding.labGrown.isChecked=false
                binding.natural.buttonTintList= ContextCompat.getColorStateList(this, R.color.color_7E3080)
                binding.labGrown.buttonTintList= ContextCompat.getColorStateList(this, R.color.color_37083B)
            }
            else
            {
                radiobuttonName="Lab Grown"
                binding.natural.isChecked=false
                binding.labGrown.isChecked=true
                binding.labGrown.buttonTintList= ContextCompat.getColorStateList(this, R.color.color_7E3080)
                binding.natural.buttonTintList= ContextCompat.getColorStateList(this, R.color.color_37083B)
            }
        }


        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.natural -> {
                    val selectedOption = getString(R.string.natural)
                    binding.natural.buttonTintList= ContextCompat.getColorStateList(this, R.color.color_7E3080)
                    binding.labGrown.buttonTintList= ContextCompat.getColorStateList(this, R.color.color_37083B)
                    radiobuttonName="Natural Diamond"
                    Log.e("RadioGroup", "Selected: $selectedOption")
                }
                R.id.lab_grown -> {
                    val selectedOption = getString(R.string.lab_grown)
                    binding.labGrown.buttonTintList= ContextCompat.getColorStateList(this, R.color.color_7E3080)
                    binding.natural.buttonTintList= ContextCompat.getColorStateList(this, R.color.color_37083B)
                    Log.e("RadioGroup", "Selected: $selectedOption")
                    radiobuttonName="Lab Grown"
                }
            }
        }

        binding.dataclear.setOnClickListener(){
            binding.metalwt.setText("")
            binding.metalwt.setHint("00.00")
            binding.metalrateop.setText("")
            binding.metalrateop.setHint("00.00")

            binding.labour.setText("")
            binding.labour.setHint("00.00")
            binding.labourop.setText("")
            binding.labourop.setHint("00.00")

            binding.solitaire.setText("")
            binding.solitaire.setHint("00.00")
            binding.solitaireop.setText("")
            binding.solitaireop.setHint("00.00")
            binding.solitairerategm.setText("")
            binding.solitairerategm.setHint("00.00")

            binding.sidedia.setText("")
            binding.sidedia.setHint("00.00")
            binding.sidediaop.setText("")
            binding.sidediaop.setHint("00.00")
            binding.sidediarategm.setText("")
            binding.sidediarategm.setHint("00.00")


            binding.colstonewt.setText("")
            binding.colstonewt.setHint("00.00")
            binding.colstonerategm.setText("")
            binding.colstonerategm.setHint("00.00")
            binding.colstoneop.setText("")
            binding.colstoneop.setHint("00.00")

            binding.totalPrice.setText("00")
        }
        database = QuotationDatabase.getDatabase(this)
        dao = database.dataModelDao()
        dataModelManager = QuotationModelManager(dao)

        binding.getQuotation.setOnClickListener()
        {
            Log.e("Enable",",..166....."+binding.getQuotation.isEnabled)
            if (binding.getQuotation.isEnabled)
            {

                val intentID = intent.getLongExtra("ID", 0L).takeIf { it != 0L } ?: 0L
                Log.e("intentID", "169...**********........$intentID")


                val metalwt = binding.metalwt.text.toString()
                val metalrategm = binding.metalrategm.text.toString()
                val metalrateop = binding.metalrateop.text.toString()

                val labour = binding.labour.text.toString()
                val labourrategm = binding.labourrategm.text.toString()
                val labourop = binding.labourop.text.toString()

                val solitaire = binding.solitaire.text.toString()
                val solitairerategm = binding.solitairerategm.text.toString()
                val solitaireop = binding.solitaireop.text.toString()

                val sidedia = binding.sidedia.text.toString()
                val sidediarategm = binding.sidediarategm.text.toString()
                val sidediaop = binding.sidediaop.text.toString()

                val colstonewt = binding.colstonewt.text.toString()
                val colstonerategm = binding.colstonerategm.text.toString()
                val colstoneop = binding.colstoneop.text.toString()

                val charges = binding.charges.text.toString()
                val tax = binding.tax.text.toString()
                val totalPrice = finalTotalPrice


                val itemName = binding.itemname.text.toString()
                var RBName = radiobuttonName
                /*val totalPrice = binding.totalPrice.text.toString()
                val totalPrice = binding.totalPrice.text.toString()*/
                Log.e("chargeText",""+chargeText)
                Log.e("chargeType",""+chargeType)

                Log.e("itemname",""+binding.itemname.text.toString())
                Log.e("radiobuttonName",""+radiobuttonName)
                Log.e("radiobuttonName",""+radiobuttonName)
                Log.e("caret",""+caret)
                val formattedDateTime = getCurrentDateTime()
                Log.e("formattedDateTime",""+formattedDateTime)
                // println(formattedDateTime)
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
                    caret =caret ,
                    itemName = itemName,
                    radiobuttonName = radiobuttonName,
                    chargeTxt = chargeText,
                    solitairetxt = solitaireText,
                    sidediatxt = sideDIAText
                )
                saveDataToDatabase(dataModel,intentID!!)


                val intent = Intent(this, GetQuotationActivity::class.java)
                intent.putExtra("metalwt", metalwt)
                intent.putExtra("metalrategm", metalrategm)
                intent.putExtra("metalrateop", metalrateop)

                intent.putExtra("labour", labour)
                intent.putExtra("labourrategm", labourrategm)
                intent.putExtra("labourop", labourop)

                intent.putExtra("solitaire", solitaire)
                intent.putExtra("solitairerategm", solitairerategm)
                intent.putExtra("solitaireop", solitaireop)

                intent.putExtra("sidedia", sidedia)
                intent.putExtra("sidediarategm", sidediarategm)
                intent.putExtra("sidediaop", sidediaop)

                intent.putExtra("colstonewt", colstonewt)
                intent.putExtra("colstonerategm", colstonerategm)
                intent.putExtra("colstoneop", colstoneop)

                intent.putExtra("charges", charges)
                intent.putExtra("tax", tax)
                intent.putExtra("totalPrice", totalPrice)

                intent.putExtra("date", formattedDateTime)
                intent.putExtra("itemName", itemName)
                intent.putExtra("radiobuttonName", radiobuttonName)
                intent.putExtra("caret", caret)
                intent.putExtra("chargeText", chargeText)
                intent.putExtra("solitaireText", solitaireText)
                intent.putExtra("sideDIAText", sideDIAText)

                startActivity(intent)
            } else {
                Toast.makeText(this, "Total price is zero, please update the amounts.", Toast.LENGTH_SHORT).show()
            }

        }

        val caretIntent = intent.getStringExtra("caret")?.takeIf { it.isNotBlank() } ?: ""
        val labourrategmIntent= intent.getStringExtra("labourrategm")?.takeIf { it.isNotBlank() } ?: "0.0"
        val chargesIntent = intent.getStringExtra("charges")?.takeIf { it.isNotBlank() } ?: "0.0"
        val taxIntent = intent.getStringExtra("tax")?.takeIf { it.isNotBlank() } ?: "0.0"
        CoroutineScope(Dispatchers.IO).launch {
            val purityDao = PurityDatabase.getDatabase(this@JewelleryActivity).purityDao()
            val laborValueFromDb = purityDao.getValueByType("labor")
            val chargesValueFromDb = purityDao.getValueByType("charges")
            val taxValueFromDb = purityDao.getValueByType("tax")
            val purityListFromDb = purityDao.getAllPurities()

            withContext(Dispatchers.Main) {
                try {

                    if (labourrategmIntent=="0.0")
                    {
                        binding.labourrategm.setText(laborValueFromDb)
                    }
                    else{
                        binding.labourrategm.setText(labourrategmIntent)
                    }

                    if (chargesIntent=="0.0")
                    {
                        binding.charges.setText(chargesValueFromDb)
                    }
                    else{
                        binding.charges.setText(chargesIntent)
                    }

                    if (taxIntent=="0.0")
                    {
                        binding.tax.setText(taxValueFromDb)
                    }
                    else{
                        binding.tax.setText(taxIntent)
                    }
                    val purityTypes: List<String> = if (purityListFromDb.isNotEmpty()) {
                        purityListFromDb.map { it.purityType }.also {
                            Log.e("purityTypes", "Database values: $it")
                        }
                    } else {
                        listOf("22K", "18K", "14K", "9K", "Other").also {
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
                        } else {
                            Log.e("SpinnerSelection", "Caret intent value not found in purity types: $caretIntent")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("Error", "Exception in setting values: ${e.message}")
                }
            }
        }

        binding.spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (binding.spinner.adapter?.count == 0) {
                    Log.e("SpinnerSelection", "No items in the spinner.")
                    return
                }

                val selectedPurityType = parent?.getItemAtPosition(position).toString()

                CoroutineScope(Dispatchers.IO).launch {
                    val purityDao = PurityDatabase.getDatabase(this@JewelleryActivity).purityDao()
                    val purityListFromDb = purityDao.getAllPurities()
                    val purityMap = purityListFromDb.associate { it.purityType to it.purityValue }

                    val selectedPurityValue = purityMap[selectedPurityType]
                    caret = selectedPurityType
                    withContext(Dispatchers.Main) {
                        try {
                            selectedPurityValue?.let {
                                binding.metalrategm.setText(it.toString())
                            } ?: run {
                                binding.metalrategm.setText("0.0")
                                Log.e("SpinnerSelection", "Purity value not found for type: $selectedPurityType")
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
        })

        setupListeners()

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

    private fun saveDataToDatabase(dataModel: QuotationModelEntity,DataID:Long) {
        dataModelManager = QuotationModelManager(dao)

        lifecycleScope.launch {
            if(DataID!=null)
            {
                val dataModelCheck = dataModelManager.getDataModelById(DataID!!)
                if (dataModelCheck != null) {
                    Log.e("DataModel", "Found data model with ID: ${dataModelCheck.itemName}")
                    var existingDataModel = dataModelManager.getDataModelByItemName(dataModelCheck.id.toString())

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
                    dataModelManager.updateDataModel(existingDataModel)

                } else {
                    Log.e("DataModel", "No data model found with ID: check else ")

                    dataModelManager.addDataModel(dataModel)
                }

            }
            else
            {
                Log.e("DataModel", "No data model found with ID: Else ")
                dataModelManager.addDataModel(dataModel)
            }

        }
    }

    fun setupListeners() {

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
        val dialog = BottomSheetDialog(this,R.style.DialogStyle)

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

            Log.e("type",""+type)
            Log.e("bottomEdit",""+bottomEdit)

            if(type=="charges")
            {
                chargeText=bottomEdit.text.toString()
                chargeType=type
            }
             if(type=="solitaire")
            {
                solitaireText=bottomEdit.text.toString()

            }
             if(type=="sidedia")
            {
                sideDIAText=bottomEdit.text.toString()
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
        Log.e("metal o/p","....   "+decimalFormat.format(output))
        val formattedOutput = if (output == 0.0) "00" else decimalFormat.format(output)
        if(formattedOutput=="00"){
            binding.metalrateop.hint="00"
        }
        else{
            //val roundedOutput = Math.round(formattedOutput.toDouble())
            //val roundedOutput = Math.ceil(output).toInt()
            val integerPart = output.toInt()
            val fractionalPart = output - integerPart
            val roundedOutput = if (fractionalPart > 0.50) {
                integerPart + 1
            } else {
                integerPart
            }

            // Set the rounded value as text in the TextView
            binding.metalrateop.setText(roundedOutput.toString())
            //binding.metalrateop.setText(formattedOutput)
        }
        //binding.metalrateop.setText(formattedOutput)
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
        Log.e("Labour..", "calculateLabourOutput: $output")
        if(formattedOutput=="00"){
            binding.labourop.hint="00"
        }
        else{
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
            Log.e("Sol..........",".........."+decimalFormat.format(rate))
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

    private fun calculateTotalPrice() {
        val metalOutput = binding.metalrateop.text.toString().toDoubleOrNull() ?: 0.0
        val solitaireOutput = binding.solitaireop.text.toString().toDoubleOrNull() ?: 0.0
        val labuorOutput = binding.labourop.text.toString().toDoubleOrNull() ?: 0.0
        val sideOutput = binding.sidediaop.text.toString().toDoubleOrNull() ?: 0.0
        val colStoneOutput = binding.colstoneop.text.toString().toDoubleOrNull() ?: 0.0

        var totalPrice = metalOutput + solitaireOutput + sideOutput + colStoneOutput+labuorOutput
        var chargeAmount = binding.charges.text.toString().toDoubleOrNull() ?: 0.0
        totalPrice += chargeAmount

        val taxPercentage = binding.tax.text.toString().toDoubleOrNull() ?: 0.0
        val taxAmount = (totalPrice * taxPercentage) / 100
        totalPrice += taxAmount
        totalPrice = Math.round(totalPrice).toDouble()

        Log.e("totalPrice","341......"+totalPrice)

        val integerPart = totalPrice.toInt()
        val fractionalPart = totalPrice - integerPart
        val roundedOutput = if (fractionalPart > 0.50) {
            integerPart + 1
        } else {
            integerPart
        }
        finalTotalPrice=roundedOutput.toString()
        binding.totalPrice.text =formatAmountWithCommas(roundedOutput.toString())


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

    fun getEditQuotationDetails()
    {
        if (intent?.extras != null)
        {
            Log.e("Intent..","Cal.l.........@@@@@@@@@@............")
            val metalwt = intent.getStringExtra("metalwt")?.takeIf { it.isNotBlank() } ?: "0.0"
            val metalrategm = intent.getStringExtra("metalrategm")?.takeIf { it.isNotBlank() } ?: "0.0"
            val metalrateop = intent.getStringExtra("metalrateop")?.takeIf { it.isNotBlank() } ?: "0.0"

            binding.metalwt.setText(metalwt)
            binding.metalrategm.setText(metalrategm)
            binding.metalrateop.setText(metalrateop)

            val labour = intent.getStringExtra("labour")?.takeIf { it.isNotBlank() } ?: "0.0"
            val labourrategm = intent.getStringExtra("labourrategm")?.takeIf { it.isNotBlank() } ?: "0.0"
            val labourop = intent.getStringExtra("labourop")?.takeIf { it.isNotBlank() } ?: "0.0"

            binding.labour.setText(labour)
            binding.labourrategm.setText(labourrategm)
            binding.labourop.setText(labourop)

            val solitaire = intent.getStringExtra("solitaire")?.takeIf { it.isNotBlank() } ?: "0.0"
            val solitairerategm = intent.getStringExtra("solitairerategm")?.takeIf { it.isNotBlank() } ?: "0.0"
            val solitaireop = intent.getStringExtra("solitaireop")?.takeIf { it.isNotBlank() } ?: "0.0"

            binding.solitaire.setText(solitaire)
            binding.solitairerategm.setText(solitairerategm)
            binding.solitaireop.setText(solitaireop)

            val sidedia = intent.getStringExtra("sidedia")?.takeIf { it.isNotBlank() } ?: "0.0"
            val sidediarategm = intent.getStringExtra("sidediarategm")?.takeIf { it.isNotBlank() } ?: "0.0"
            val sidediaop = intent.getStringExtra("sidediaop")?.takeIf { it.isNotBlank() } ?: "0.0"

            binding.sidedia.setText(sidedia)
            binding.sidediarategm.setText(sidediarategm)
            binding.sidediaop.setText(sidediaop)

            val colstonewt = intent.getStringExtra("colstonewt")?.takeIf { it.isNotBlank() } ?: "0.0"
            val colstonerategm = intent.getStringExtra("colstonerategm")?.takeIf { it.isNotBlank() } ?: "0.0"
            val colstoneop = intent.getStringExtra("colstoneop")?.takeIf { it.isNotBlank() } ?: "0.0"

            binding.colstonewt.setText(colstonewt)
            binding.colstonerategm.setText(colstonerategm)
            binding.colstoneop.setText(colstoneop)

            val charges = intent.getStringExtra("charges")?.takeIf { it.isNotBlank() } ?: "0"
            val tax = intent.getStringExtra("tax")?.takeIf { it.isNotBlank() } ?: "0"
            var totalPrice = intent.getStringExtra("totalPrice")?.takeIf { it.isNotBlank() } ?: "0"

            /*binding.charges.setText(charges)
            binding.tax.setText(tax)*/
            binding.totalPrice.setText(tax)

            val itemName = intent.getStringExtra("itemName")?.takeIf { it.isNotBlank() } ?: ""
            val radiobuttonName = intent.getStringExtra("radiobuttonName")?.takeIf { it.isNotBlank() } ?: ""
            val caret = intent.getStringExtra("caret")?.takeIf { it.isNotBlank() } ?: ""
            val chargeText = intent.getStringExtra("chargeText")?.takeIf { it.isNotBlank() } ?: ""

            binding.itemname.setText(itemName)

            val solitaireText = intent.getStringExtra("solitaireText")?.takeIf { it.isNotBlank() } ?: ""
            val sideDIAText = intent.getStringExtra("sideDIAText")?.takeIf { it.isNotBlank() } ?: ""

            caretIntent=caret
            Log.e("radiobuttonName..","Cal.l.@@@@@@....  "+radiobuttonName)
            Log.e("caret..","Cal.l.........@.  "+caret)
            Log.e("chargeText..","Cal.@@@@@............  "+chargeText)
            Log.e("solitaireText..","Call.@@@@@@.......  "+solitaireText)
            Log.e("sideDIAText..","Call.@@@@@@.......  "+sideDIAText)

        }
        else
        {
            Log.e("Intent..","Cal.l..Nulll.......@@@@@@@@@@............")
        }
    }
}