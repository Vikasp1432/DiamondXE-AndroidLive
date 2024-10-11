package com.dxe.calc.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dxe.calc.api.apiinterface.CurrencyRateApiInterface
import com.dxe.calc.api.common.ErrorResponse
import com.dxe.calc.api.common.NetworkResponse
import com.dxe.calc.models.CurrencyRateResponse
import com.dxe.calc.models.SelectedCurrencyRateResponse

class CurrencyRateRepo(private val apiClient: CurrencyRateApiInterface) {

    private val currencyRateReqLiveData =
        MutableLiveData<NetworkResponse<CurrencyRateResponse, ErrorResponse>>()

    val currencyRateLiveDataTest: LiveData<NetworkResponse<CurrencyRateResponse, ErrorResponse>>
        get() = currencyRateReqLiveData

    suspend fun currency() {
        val response = apiClient.currency()
        currencyRateReqLiveData.postValue(response)
    }

    private val selectedCurrencyRateReqLiveData =
        MutableLiveData<NetworkResponse<SelectedCurrencyRateResponse, ErrorResponse>>()

    val selectedCurrencyRateLiveDataTest: LiveData<NetworkResponse<SelectedCurrencyRateResponse, ErrorResponse>>
        get() = selectedCurrencyRateReqLiveData

    suspend fun getCurrencyRate(currency: String) {
        val response = apiClient.currencyRate(currency)
        selectedCurrencyRateReqLiveData.postValue(response)
    }
}