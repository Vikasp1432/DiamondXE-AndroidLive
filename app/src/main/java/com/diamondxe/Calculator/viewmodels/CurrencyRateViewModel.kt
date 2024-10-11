package com.dxe.calc.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dxe.calc.api.common.ErrorResponse
import com.dxe.calc.api.common.NetworkResponse
import com.dxe.calc.models.CurrencyRateResponse
import com.dxe.calc.models.SelectedCurrencyRateResponse
import com.dxe.calc.repo.CurrencyRateRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencyRateViewModel(private val currencyRateRepo: CurrencyRateRepo) : ViewModel() {


    val currencyRateLiveData : LiveData<NetworkResponse<CurrencyRateResponse, ErrorResponse>>
        get() = currencyRateRepo.currencyRateLiveDataTest


    suspend fun getCurrencyRate() {
        viewModelScope.launch (Dispatchers.IO){
            currencyRateRepo.currency()
        }
    }


    val selectedCurrencyRateLiveData : LiveData<NetworkResponse<SelectedCurrencyRateResponse, ErrorResponse>>
        get() = currencyRateRepo.selectedCurrencyRateLiveDataTest


    suspend fun getSelectedCurrencyRate(currency: String) {
        viewModelScope.launch (Dispatchers.IO){
            currencyRateRepo.getCurrencyRate(currency)
        }
    }
}