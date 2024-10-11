package com.dxe.calc.dashboard

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.diamondxe.R
import com.diamondxe.databinding.ActivityMenuNewBinding
import com.dxe.calc.CurrencyListActivity
import com.dxe.calc.DataBase.MenuDB.PurityDatabase
import com.dxe.calc.DataBase.MenuDB.PurityEntity
import com.dxe.calc.DataBase.MenuDB.PurityPercentageEntry
import com.dxe.calc.DataBase.MenuDB.ValueEntity
import com.dxe.calc.utils.MenuCustomDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import kotlin.math.round


class MenuActivity : AppCompatActivity() {

    private lateinit var  binding :ActivityMenuNewBinding

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMenuNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener(){
            onBackPressed()
        }

        binding.quotationlistrv.setOnClickListener(){
            startActivity(Intent(this, QuotationsListActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        getStoreValue()
        binding.currencyLayout.setOnClickListener { startActivity(Intent(this, CurrencyListActivity::class.java)) }
        val switchValue = getPriceSwitchValuePreference(this, "PriceValue", false)
        Log.e("switchValue","...542...."+switchValue)
        if(switchValue)
        {
            binding.priceValue.isChecked=true
        }
        else
        {
            binding.priceValue.isChecked=false
        }
        binding.priceValue.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            setPriceSwitchValuePreference(this,"PriceValue",isChecked)
        })


        //App version
        try {
            val pInfo: PackageInfo =
                packageManager.getPackageInfo(packageName, 0)
            val version: String = pInfo.versionName
            val res: Resources = resources
            val text: String = res.getString(R.string.txt_app_version, version)
            binding.tvAppVersion.text = text
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        binding.menuShare.setOnClickListener(){
            val customDialog = MenuCustomDialog(this,binding.editMaterialEV.text.toString(),
                binding.purity22kop.text.toString(),binding.purity18kop.text.toString(),
                binding.purity14kop.text.toString()
                ,binding.purity9kop.text.toString())
            customDialog.show()
        }

        binding.savedetailsbutton.setOnClickListener(){

            if (binding.editMaterialEV.text.toString()==""){

                Toast.makeText(this,"Enter Metal Rate/gm value",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val metalRate = binding.editMaterialEV.text.toString().toDouble()
            val Purity22k = binding.purity22kPer.text.toString().toDouble()
            val Purity18k = binding.purity18kPer.text.toString().toDouble()
            val Purity14k = binding.purity14kPer.text.toString().toDouble()
            val Purity9k = binding.purity9kPer.text.toString().toDouble()

            val first22kOutput = calculateOutputUsingStandardFormula(metalRate, Purity22k)
            val second18kOutput = calculateOutputUsingStandardFormula(metalRate, Purity18k)
            val third14kOutput = calculateOutputUsingStandardFormula(metalRate, Purity14k)
            val fourth9kOutput = calculateOutputUsingStandardFormula(metalRate, Purity9k)

            val purityList = listOf(
                PurityEntity(purityType = "22k", purityValue = String.format("%.2f", first22kOutput.toDouble())),
                PurityEntity(purityType = "18k", purityValue = String.format("%.2f", second18kOutput.toDouble())),
                PurityEntity(purityType = "14k", purityValue = String.format("%.2f", third14kOutput.toDouble())),
                PurityEntity(purityType = "9k", purityValue = String.format("%.2f", fourth9kOutput.toDouble())),
                PurityEntity(purityType = "Other", purityValue = binding.editMaterialEV.text.toString())
            )

            val purityPerList = listOf(
                PurityPercentageEntry(purityPerType = "22k", purityPerValue = binding.purity22kPer.text.toString()),
                PurityPercentageEntry(purityPerType = "18k", purityPerValue = binding.purity18kPer.text.toString()),
                PurityPercentageEntry(purityPerType = "14k", purityPerValue = binding.purity14kPer.text.toString()),
                PurityPercentageEntry(purityPerType = "9k", purityPerValue = binding.purity9kPer.text.toString()),

            )

            val laborValue = ValueEntity(type = "labor", value = binding.laborEdt.text.toString())
            val chargesValue = ValueEntity(type = "charges", value = binding.chargesEdt.text.toString())
            val taxValue = ValueEntity(type = "tax", value = binding.taxEdt.text.toString())

            val purityDao = PurityDatabase.getDatabase(this).purityDao()


            CoroutineScope(Dispatchers.IO).launch {
                purityDao.deleteAllPurities()
                purityDao.insertValue(laborValue)
                purityDao.insertValue(chargesValue)
                purityDao.insertValue(taxValue)
            }

            CoroutineScope(Dispatchers.IO).launch {
                purityDao.deleteallValue()
                purityList.forEach { purity ->
                    purityDao.insertPurity(purity)
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                purityDao.deleteallpurityPerValue()
                purityPerList.forEach { purityper ->
                    purityDao.insertpurityPercentage(purityper)
                }
            }
            Toast.makeText(this,R.string.datasaved,Toast.LENGTH_SHORT).show()
        }

        setupEditTextListeners()
    }

    private fun setupEditTextListeners() {
        binding.purity22kPer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("TextChange", "metalrate PriceEdit: ${s.toString()}")
            }

            override fun afterTextChanged(s: Editable?) {
                calculateAndUpdateOutputs()
            }
        })

        binding.purity18kPer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("TextChange", "firstEdit: ${s.toString()}")
            }

            override fun afterTextChanged(s: Editable?) {
                calculateAndUpdateOutputs()
            }
        })

        binding.purity14kPer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("TextChange", "secoundEdit: ${s.toString()}")
            }

            override fun afterTextChanged(s: Editable?) {
                calculateAndUpdateOutputs()
            }
        })


        binding.purity9kPer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("TextChange", "thirdEdit: ${s.toString()}")
            }

            override fun afterTextChanged(s: Editable?) {
                calculateAndUpdateOutputs()
            }
        })


        binding.editMaterialEV.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("TextChange", "fourthEdit: ${s.toString()}")
            }

            override fun afterTextChanged(s: Editable?) {
                Log.e("Edit..","123......."+s.toString())
                calculateAndUpdateOutputs()
            }
        })
    }

    fun roundOffAmount(amount: Double): Int {
        return round(amount).toInt()
    }
    private fun calculateAndUpdateOutputs()
    {
        val metalRate = binding.editMaterialEV.text.toString().toDoubleOrNull() ?: return
        val Purity22k = binding.purity22kPer.text.toString().toDoubleOrNull() ?: return
        val Purity18k = binding.purity18kPer.text.toString().toDoubleOrNull() ?: return
        val Purity14k = binding.purity14kPer.text.toString().toDoubleOrNull() ?: return
        val Purity9k = binding.purity9kPer.text.toString().toDoubleOrNull() ?: return

        val first22kOutput = roundOffAmount(calculateOutput(metalRate, Purity22k))
        val second18kOutput = roundOffAmount(calculateOutput(metalRate, Purity18k))
        val third14kOutput = roundOffAmount(calculateOutput(metalRate, Purity14k))
        val fourth9kOutput = roundOffAmount(calculateOutput(metalRate, Purity9k))

        binding.purity22kop.text = roundAndFormatAmount(first22kOutput.toDouble())
        binding.purity18kop.text = roundAndFormatAmount(second18kOutput.toDouble())
        binding.purity14kop.text = roundAndFormatAmount(third14kOutput.toDouble())
        binding.purity9kop.text = roundAndFormatAmount(fourth9kOutput.toDouble())
    }

    fun setPriceSwitchValuePreference(context: Context, key: String, value: Boolean) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }



    fun getPriceSwitchValuePreference(context: Context, key: String, defaultValue: Boolean): Boolean {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun calculateOutputUsingStandardFormula(metalRate: Double, purityPercentage: Double): Double {
        return metalRate * (purityPercentage / 100)
    }

    fun calculateOutput(metalRate: Double, purityPercentage: Double): Double {
        return metalRate * (purityPercentage / 100)
    }

    fun roundAndFormatAmount(amount: Double): String {
        val integerPart = amount.toInt()
        val fractionalPart = amount - integerPart

        val roundedOutput = if (fractionalPart > 0.50) {
            integerPart + 1
        } else {
            integerPart
        }
        return formatAmountWithCommas(roundedOutput.toString())
    }

    fun formatAmountWithCommas(amount: String): String {
        val formatter = DecimalFormat("#,##,###")
        val parsedAmount = amount.toBigDecimalOrNull() ?: return amount
        return formatter.format(parsedAmount)
    }


     fun getStoreValue()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val purityDao = PurityDatabase.getDatabase(this@MenuActivity).purityDao()

            val purityListFromDb = purityDao.getAllPurities()
            val purityPerListFromDb = purityDao.getAllPuritiesPer()

            val laborValueFromDb = purityDao.getValueByType("labor")
            val chargesValueFromDb = purityDao.getValueByType("charges")
            val taxValueFromDb = purityDao.getValueByType("tax")

            //val laborValue = laborValueFromDb.toDoubleOrNull() ?: 0.0

           if (laborValueFromDb!=null)
           {
               binding.laborEdt.setText(laborValueFromDb.toString())
           }
            if (chargesValueFromDb!=null)
            {
                binding.chargesEdt.setText(chargesValueFromDb.toString())
            }
            if (taxValueFromDb!=null)
            {
                binding.taxEdt.setText(taxValueFromDb.toString())
            }

            withContext(Dispatchers.Main) {
                purityListFromDb.forEach { purity ->
                    val purityValue = purity.purityValue ?: "0.0"
                    when (purity.purityType) {
                        "22k" -> binding.purity22kop.setText(if (purityValue.isBlank()) "0.0" else purityValue)
                        "18k" -> binding.purity18kop.setText(if (purityValue.isBlank()) "0.0" else purityValue)
                        "14k" -> binding.purity14kop.setText(if (purityValue.isBlank()) "0.0" else purityValue)
                        "9k" -> binding.purity9kop.setText(if (purityValue.isBlank()) "0.0" else purityValue)
                        "Other" -> binding.editMaterialEV.setText(if (purityValue.isBlank()) "0.0" else purityValue)
                    }
                }

                purityPerListFromDb.forEach { purityPer ->
                    val purityPerValue = purityPer.purityPerValue ?: "75.0"
                    when (purityPer.purityPerType) {
                        "22k" -> binding.purity22kPer.setText(if (purityPerValue.isBlank()) "75.0" else purityPerValue)
                        "18k" -> binding.purity18kPer.setText(if (purityPerValue.isBlank()) "58.5" else purityPerValue)
                        "14k" -> binding.purity14kPer.setText(if (purityPerValue.isBlank()) "60.0" else purityPerValue)
                        "9k" -> binding.purity9kPer.setText(if (purityPerValue.isBlank()) "37.5" else purityPerValue)
                    }
                }

            }
        }
    }
}

