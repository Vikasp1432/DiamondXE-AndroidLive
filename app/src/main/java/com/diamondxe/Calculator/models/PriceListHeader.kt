package com.dxe.calc.models


import com.google.gson.annotations.SerializedName

data class PriceListHeader(
    @SerializedName("error_code")
    val errorCode: Int,
    @SerializedName("error_message")
    val errorMessage: String
)