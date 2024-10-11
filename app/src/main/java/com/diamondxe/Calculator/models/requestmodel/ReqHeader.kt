package com.dxe.calc.models.requestmodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class ReqHeader {
    @SerializedName("username")
    @Expose
    private var username: String? = null

    @SerializedName("password")
    @Expose
    private var password: String? = null

    fun getUsername(): String? {
        return username
    }

    fun setUsername(username: String?) {
        this.username = username
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String?) {
        this.password = password
    }
}