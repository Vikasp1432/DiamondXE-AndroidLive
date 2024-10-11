package com.dxe.calc.models


import com.google.gson.annotations.SerializedName

data class PriceListBody(
    @SerializedName("price")
    val price: List<Price>
)