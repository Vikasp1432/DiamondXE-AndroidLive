package com.dxe.calc.models


import com.google.gson.annotations.SerializedName

data class Price(
    @SerializedName("caratprice")
    val caratprice: Int,
    @SerializedName("clarity")
    val clarity: String,
    @SerializedName("color")
    val color: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("high_size")
    val highSize: Double,
    @SerializedName("low_size")
    val lowSize: Double,
    @SerializedName("shape")
    val shape: String
)