package com.dxe.calc.models.requestmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class PriceListRequestModel {
    @SerializedName("header")
    @Expose
    private var header: ReqHeader? = null

    @SerializedName("body")
    @Expose
    private var body: ReqBody? = null

    fun getHeader(): ReqHeader? {
        return header
    }

    fun setHeader(header: ReqHeader?) {
        this.header = header
    }

    fun getBody(): ReqBody? {
        return body
    }

    fun setBody(body: ReqBody?) {
        this.body = body
    }

}