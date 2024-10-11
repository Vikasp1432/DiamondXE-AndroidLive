package com.dxe.calc.models


import com.google.gson.annotations.SerializedName

data class PriceListResponse(
    @SerializedName("body")
    val body: PriceListBody,
    @SerializedName("header")
    val header: PriceListHeader
)