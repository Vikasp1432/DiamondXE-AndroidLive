package com.dxe.calc.utils

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {
    private const val NAMEAPP = "dxe_calculator"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences


    private val CURRENCY_NAME = Pair("currency", "INR")
    private val CURRENCY_RATE = Pair("currencyRate", 0F)

    private val ROUND_SHAPE_PRICE = Pair("roundShapePrice", "")
    private val PEAR_SHAPE_PRICE = Pair("pearShapePrice", "")
    private val API_FETCH_DATE = Pair("apiFetchDate", "")

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAMEAPP, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var roundShapePrice: String
        get() = preferences.getString(ROUND_SHAPE_PRICE.first, ROUND_SHAPE_PRICE.second)!!
        set(value) = preferences.edit {
            it.putString(ROUND_SHAPE_PRICE.first, value)
        }

    var pearShapePrice: String
        get() = preferences.getString(PEAR_SHAPE_PRICE.first, PEAR_SHAPE_PRICE.second)!!
        set(value) = preferences.edit {
            it.putString(PEAR_SHAPE_PRICE.first, value)
        }


    var currency: String
        get() = preferences.getString(CURRENCY_NAME.first, CURRENCY_NAME.second)!!
        set(value) = preferences.edit {
            it.putString(CURRENCY_NAME.first, value)
        }

    var currencyRate: Float
        get() = preferences.getFloat(CURRENCY_RATE.first, CURRENCY_RATE.second)!!
        set(value) = preferences.edit {
            it.putFloat(CURRENCY_RATE.first, value)
        }

    var apiRefreshDate: String
        get() = preferences.getString(API_FETCH_DATE.first, API_FETCH_DATE.second)!!
        set(value) = preferences.edit {
            it.putString(API_FETCH_DATE.first, value)
        }
}