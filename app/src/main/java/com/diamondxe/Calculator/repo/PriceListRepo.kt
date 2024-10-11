package com.dxe.calc.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dxe.calc.api.apiinterface.PriceListApiInterface
import com.dxe.calc.api.common.ErrorResponse
import com.dxe.calc.api.common.NetworkResponse
import com.dxe.calc.models.PriceListModel
import com.dxe.calc.models.requestmodel.PriceListRequest

class PriceListRepo(private val apiClient: PriceListApiInterface) {

    private val priceListReqLiveData =
        MutableLiveData<NetworkResponse<PriceListModel, ErrorResponse>>()

    val priceListLiveData: LiveData<NetworkResponse<PriceListModel, ErrorResponse>>
        get() = priceListReqLiveData

    suspend fun getPrice(priceListRequest: PriceListRequest) {
        val response = apiClient.getPriceList(priceListRequest)
        priceListReqLiveData.postValue(response)
    }


    private val pearPriceListReqLiveData =
        MutableLiveData<NetworkResponse<PriceListModel, ErrorResponse>>()

    val pearPriceListLiveData: LiveData<NetworkResponse<PriceListModel, ErrorResponse>>
        get() = pearPriceListReqLiveData

    suspend fun getPearPrice(priceListRequest: PriceListRequest) {
        val response = apiClient.getPriceList(priceListRequest)
        pearPriceListReqLiveData.postValue(response)
    }
}