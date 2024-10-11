package com.dxe.calc.api.apiinterface

import com.dxe.calc.api.common.ErrorResponse
import com.dxe.calc.api.common.NetworkResponse
import com.dxe.calc.models.CurrencyRateResponse
import com.dxe.calc.models.SelectedCurrencyRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyRateApiInterface {

    @GET("live?access_key=b0c77183d8b05bf58c91106f2f39a3a1&latest?base=usd")
    suspend fun currency(): NetworkResponse<CurrencyRateResponse, ErrorResponse>

    /*@GET("live?access_key=b0c77183d8b05bf58c91106f2f39a3a1&convert?from=USD")*/
    @GET("convert?access_key=b0c77183d8b05bf58c91106f2f39a3a1&from=USD&to=INR&amount=1")
    suspend fun currencyRate(@Query("to") to: String): NetworkResponse<SelectedCurrencyRateResponse, ErrorResponse>
}