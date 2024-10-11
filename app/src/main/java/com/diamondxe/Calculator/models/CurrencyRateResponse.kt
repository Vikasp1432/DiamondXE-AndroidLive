package com.dxe.calc.models

import com.google.gson.annotations.SerializedName

data class CurrencyRateResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("quotes")
    val data: Map<String, Double>
)