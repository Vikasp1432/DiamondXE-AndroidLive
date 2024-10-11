package com.dxe.calc.models

data class CurrencyModel(val currencyName: String, val currencyRate: Double? = 0.0) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CurrencyModel) return false
        if (currencyName == other.currencyName) return true else false
        return false
    }
}
