package com.dxe.calc.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.diamondxe.R
import com.diamondxe.databinding.ActivityQuotationsListBinding
import com.dxe.calc.DataBase.QuotationsActivity.QuotationDatabase
import com.dxe.calc.DataBase.QuotationsActivity.QuotationModelDao
import com.dxe.calc.DataBase.QuotationsActivity.QuotationModelEntity
import com.dxe.calc.DataBase.QuotationsActivity.QuotationModelManager
import com.dxe.calc.adapters.QuotationModelAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class QuotationsListActivity : AppCompatActivity() {

    private lateinit var dataModelManager: QuotationModelManager
    private lateinit var database: QuotationDatabase
    private lateinit var dao: QuotationModelDao
    private lateinit var dataModelAdapter: QuotationModelAdapter
    private lateinit var binding : ActivityQuotationsListBinding

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuotationsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener(){onBackPressed()
            finish()}

        // comment for test
        /*binding.countrySearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                dataModelAdapter.filter.filter(newText)
                return false
            }
        })*/

        database = QuotationDatabase.getDatabase(this)
        dao = database.dataModelDao()
        dataModelManager = QuotationModelManager(dao)
        binding.quotationrv.layoutManager = LinearLayoutManager(this)

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.natural -> {
                    loadDataFromDatabase()
                }
                R.id.lab_grown -> {
                    loadDataFromDatabase()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        loadDataFromDatabase()
    }
    private fun loadDataFromDatabase() {
        val selectedRadioButton = findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId)
        var selectedValue = selectedRadioButton.text.toString()

        if (selectedValue=="Natural")
        {
            selectedValue="Natural Diamond"
        }
        else
        {
            selectedValue="Lab Grown"
        }
        lifecycleScope.launch(Dispatchers.IO)
        {
            val dataModels = dataModelManager.getDataModels()

            val filteredDataModels = dataModels.filter { it.radiobuttonName == selectedValue }

            Log.e("FilteredDataSize", "....Filtered data size: ${filteredDataModels.size}")

            withContext(Dispatchers.Main) {
                dataModelAdapter = QuotationModelAdapter(filteredDataModels) { clickedDataModel ->
                    showDetailsOfDataModel(clickedDataModel)
                }

                if (filteredDataModels.size == 0) {
                    binding.noquotationfound.visibility = View.VISIBLE
                    binding.quotationrv.visibility = View.GONE
                } else {
                    binding.noquotationfound.visibility = View.GONE
                    binding.quotationrv.visibility = View.VISIBLE
                }
                binding.quotationrv.adapter = dataModelAdapter
            }
        }


        binding.countrySearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                dataModelAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun showDetailsOfDataModel(quotationDetails: QuotationModelEntity) {

        val intent = Intent(this, GetQuotationActivity::class.java)
        intent.putExtra("ID", quotationDetails.id)
        intent.putExtra("metalwt", quotationDetails.metalWeight)
        intent.putExtra("metalrategm", quotationDetails.metalRate)
        intent.putExtra("metalrateop", quotationDetails.metalOutput)

        intent.putExtra("labour", quotationDetails.labourWeight)
        intent.putExtra("labourrategm", quotationDetails.labourRate)
        intent.putExtra("labourop", quotationDetails.labourOutput)

        intent.putExtra("solitaire", quotationDetails.solitaireWeight)
        intent.putExtra("solitairerategm", quotationDetails.solitaireRate)
        intent.putExtra("solitaireop", quotationDetails.solitaireOutput)

        intent.putExtra("sidedia", quotationDetails.sideWeight)
        intent.putExtra("sidediarategm", quotationDetails.sideRate)
        intent.putExtra("sidediaop", quotationDetails.sideOutput)

        intent.putExtra("colstonewt", quotationDetails.colStoneWeight)
        intent.putExtra("colstonerategm", quotationDetails.colStoneRate)
        intent.putExtra("colstoneop", quotationDetails.colStoneOutput)

        intent.putExtra("charges", quotationDetails.charges)
        intent.putExtra("tax", quotationDetails.tax)
        intent.putExtra("totalPrice", quotationDetails.totalPrice)

        intent.putExtra("date", quotationDetails.currentdatetime)
        intent.putExtra("itemName", quotationDetails.itemName)
        intent.putExtra("radiobuttonName", quotationDetails.radiobuttonName)
        intent.putExtra("caret", quotationDetails.caret)
        intent.putExtra("chargeText", quotationDetails.charges)
        intent.putExtra("solitaireText", quotationDetails.solitairetxt)
        intent.putExtra("sideDIAText", quotationDetails.sidediatxt)

        intent.putExtra("currencyCode", quotationDetails.currencyCode)
        intent.putExtra("currencyValue", quotationDetails.currencyvalue)
        startActivity(intent)
    }


}