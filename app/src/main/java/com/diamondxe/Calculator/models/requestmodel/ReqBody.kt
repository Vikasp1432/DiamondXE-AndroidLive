package com.dxe.calc.models.requestmodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ReqBody {
    @SerializedName("shape")
    @Expose
    private var shape: String? = null

    fun getShape(): String? {
        return shape
    }

    fun setShape(shape: String?) {
        this.shape = shape
    }
}