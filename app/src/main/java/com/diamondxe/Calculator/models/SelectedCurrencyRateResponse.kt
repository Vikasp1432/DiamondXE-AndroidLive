package com.dxe.calc.models

import com.google.gson.annotations.SerializedName

data class SelectedCurrencyRateResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("result")
    val rate: Double = 0.0
)