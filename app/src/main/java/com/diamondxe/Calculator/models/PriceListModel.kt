package com.dxe.calc.models


import com.google.gson.annotations.SerializedName

data class PriceListModel(
    @SerializedName("response")
    val response: PriceListResponse
)