package com.dxe.calc

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.diamondxe.R
import com.diamondxe.databinding.ActivityCurrencyListBinding
import com.dxe.calc.adapters.CurrencyRateAdapter
import com.dxe.calc.api.apiclient.CurrencyRateClient
import com.dxe.calc.api.common.NetworkResponse
import com.dxe.calc.models.CurrencyModel
import com.dxe.calc.repo.CurrencyRateRepo
import com.dxe.calc.utils.AppPreferences
import com.dxe.calc.viewmodelfactories.CurrencyRateViewModelFactory
import com.dxe.calc.viewmodels.CurrencyRateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat

class CurrencyListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCurrencyListBinding
    private var progressDialog: ProgressDialog? = null
    private lateinit var currencyRateAdapter: CurrencyRateAdapter
    private var listCurrency = ArrayList<CurrencyModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCurrencyListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.back.setOnClickListener(){
            onBackPressed()
        }

        binding.rvCurrency.layoutManager = LinearLayoutManager(this@CurrencyListActivity)
        currencyRateAdapter = CurrencyRateAdapter(this, listCurrency) {
            it.let { currencyModel ->
                binding.tvSelectedCurrency.text = currencyModel.currencyName
                binding.edtSelectedCurrencyRate.setText(currencyModel.currencyRate.toString())
                binding.countrySearch.clearFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.countrySearch.windowToken, 0)
                Log.e("CurrencyListActivity","..Clikc..@@@@@@@@@@.................39*********............")
            }
        }

       // binding.countrySearch.setHintTextColor(ContextCompat.getColor(this, R.color.color_7E3080)) // Replace with your color

        binding.countrySearch.setQueryHint(Html.fromHtml("<font color = #7E3080>" + getResources().getString(R.string.searchby) + "</font>"));

        binding.rvCurrency.adapter = currencyRateAdapter

        binding.countrySearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currencyRateAdapter.filter.filter(newText)
                return false
            }
        })


        //startActivity(intent)
        binding.btnDone.setOnClickListener {
            val intent = Intent()
            val currency = binding.tvSelectedCurrency.text
            val rate = binding.edtSelectedCurrencyRate.text
            val df = DecimalFormat("#.##")
            val rate1 = df.format(rate.toString().toDouble())
            AppPreferences.currency = currency.toString()
            AppPreferences.currencyRate = rate1.toFloat()
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.ivCurrencyRefresh.setOnClickListener {
            if (listCurrency.isNotEmpty()) {
                val index = listCurrency.indexOf(CurrencyModel(AppPreferences.currency))
                if (index != -1) {
                    binding.edtSelectedCurrencyRate.setText(listCurrency[index].currencyRate.toString())
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }

        binding.tvSelectedCurrency.text = AppPreferences.currency
        binding.edtSelectedCurrencyRate.setText(AppPreferences.currencyRate.toString())

        fetchCurrency()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

    private fun fetchCurrency() {
        CoroutineScope(Dispatchers.Main).launch {
            showProgress()
            val client = CurrencyRateClient.getInstance()
            val loginRepo = CurrencyRateRepo(client)

            val currencyRateViewModel =
                ViewModelProvider(
                    this@CurrencyListActivity,
                    CurrencyRateViewModelFactory(loginRepo)
                )[CurrencyRateViewModel::class.java]

            currencyRateViewModel.getCurrencyRate()
            currencyRateViewModel.currencyRateLiveData.observe(this@CurrencyListActivity) { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        Log.e("Currency NetworkResponse.Success----", "$response")
                        hideProgress()
                        val res = response.body
                        if (res.success) {
                            if (res?.data?.entries?.isNullOrEmpty() == false) {
                                val df = DecimalFormat("#.##")
                                var list: ArrayList<CurrencyModel> =
                                    res.data.entries.map {
                                        CurrencyModel(
                                            it.key.removePrefix("USD"),
                                            df.format(it.value).toDouble()
                                        )
                                    } as ArrayList<CurrencyModel>
                                listCurrency.clear()
                                listCurrency.addAll(list)
                                currencyRateAdapter.notifyDataSetChanged()
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
    }

    private fun showProgress() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this@CurrencyListActivity)
            progressDialog!!.setMessage("Loading...")
            progressDialog!!.show()
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

}