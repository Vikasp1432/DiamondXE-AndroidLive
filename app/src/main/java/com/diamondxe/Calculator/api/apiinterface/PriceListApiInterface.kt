package com.dxe.calc.api.apiinterface

import com.dxe.calc.api.common.ErrorResponse
import com.dxe.calc.api.common.NetworkResponse
import com.dxe.calc.models.CurrencyRateResponse
import com.dxe.calc.models.PriceListModel
import com.dxe.calc.models.requestmodel.PriceListRequest
import retrofit2.http.*

interface PriceListApiInterface {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("Prices/GetPriceSheet.aspx")
    suspend fun getPriceList(@Body priceListRequest: PriceListRequest): NetworkResponse<PriceListModel, ErrorResponse>
}