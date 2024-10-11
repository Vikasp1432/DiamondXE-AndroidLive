package com.dxe.calc.models.requestmodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class PriceListRequest {
    @SerializedName("request")
    @Expose
    private var request: PriceListRequestModel? = null

    fun getRequest(): PriceListRequestModel? {
        return request
    }

    fun setRequest(request: PriceListRequestModel?) {
        this.request = request
    }
}