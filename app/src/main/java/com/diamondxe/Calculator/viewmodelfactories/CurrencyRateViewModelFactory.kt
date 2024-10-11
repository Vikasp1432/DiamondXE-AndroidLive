package com.dxe.calc.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dxe.calc.repo.CurrencyRateRepo
import com.dxe.calc.viewmodels.CurrencyRateViewModel


class CurrencyRateViewModelFactory(private val currencyRateRepo: CurrencyRateRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return LoginViewModel(loginRepo) as T
        if(modelClass.isAssignableFrom(CurrencyRateViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CurrencyRateViewModel(currencyRateRepo) as T
        }
        throw IllegalArgumentException("UNKNOWN VIEW MODEL CLASS")
    }
}