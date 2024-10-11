package com.dxe.calc.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dxe.calc.models.PriceListModel
import com.dxe.calc.repo.CurrencyRateRepo
import com.dxe.calc.repo.PriceListRepo
import com.dxe.calc.viewmodels.CurrencyRateViewModel
import com.dxe.calc.viewmodels.PriceListViewModel


class PriceListViewModelFactory(private val priceListRepo: PriceListRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PriceListViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return PriceListViewModel(priceListRepo) as T
        }
        throw IllegalArgumentException("UNKNOWN VIEW MODEL CLASS")
    }
}