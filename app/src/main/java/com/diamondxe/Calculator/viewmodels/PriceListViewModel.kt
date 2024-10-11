package com.dxe.calc.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dxe.calc.api.common.ErrorResponse
import com.dxe.calc.api.common.NetworkResponse
import com.dxe.calc.models.PriceListModel
import com.dxe.calc.models.requestmodel.PriceListRequest
import com.dxe.calc.repo.PriceListRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PriceListViewModel(private val priceListRepo: PriceListRepo) : ViewModel() {


    val priceListLiveData : LiveData<NetworkResponse<PriceListModel, ErrorResponse>>
        get() = priceListRepo.priceListLiveData

    val pearPriceListLiveData : LiveData<NetworkResponse<PriceListModel, ErrorResponse>>
        get() = priceListRepo.pearPriceListLiveData

    suspend fun getPrices(priceListRequest: PriceListRequest) {
        viewModelScope.launch (Dispatchers.IO){
            priceListRepo.getPrice(priceListRequest)
        }
    }

    suspend fun getPearPrices(priceListRequest: PriceListRequest) {
        viewModelScope.launch (Dispatchers.IO){
            priceListRepo.getPearPrice(priceListRequest)
        }
    }

}