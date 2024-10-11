package com.dxe.calc.adapters

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.diamondxe.R
import com.diamondxe.databinding.ItmCurrencyRateBinding
import com.dxe.calc.models.CurrencyModel
import com.dxe.calc.utils.AppPreferences
import java.util.*
import kotlin.collections.ArrayList

class CurrencyRateAdapter(val context: Context, var list: java.util.ArrayList<CurrencyModel>,
                          private val onItemSelect: (CurrencyModel) -> Unit) :
    RecyclerView.Adapter<CurrencyRateAdapter.CurrencyRateViewHolder>(), Filterable {

    var countryFilterList = ArrayList<CurrencyModel>()
    var mContext: Context? = null

    init {
        countryFilterList = list
        mContext = context
    }

    inner class CurrencyRateViewHolder(val binding: ItmCurrencyRateBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyRateViewHolder {
        val binding =
            ItmCurrencyRateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyRateViewHolder(binding)
    }

    override fun getItemCount() = countryFilterList.size

    override fun onBindViewHolder(holder: CurrencyRateViewHolder, position: Int) {
        with(holder) {
            with(countryFilterList[position]) {
                if (!TextUtils.isEmpty(AppPreferences.currency)) {
                    if (this.currencyName == AppPreferences.currency) {
                        mContext?.getColor(R.color.color_EBEBEB)
                            ?.let { binding.currencyContainer.setBackgroundColor(it) }
                    } else {
                        mContext?.getColor(R.color.color_F3F3F6)
                            ?.let { binding.currencyContainer.setBackgroundColor(it) }
                    }
                }
                binding.tvCurrency.text = this.currencyName
                binding.tvCurrentRate.text = this.currencyRate.toString()
            }
            this.itemView.setOnClickListener {
                onItemSelect.invoke(countryFilterList[position])
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                countryFilterList = if (charSearch.isEmpty()) {
                    list
                } else {
                    val resultList = ArrayList<CurrencyModel>()
                    for (row in list) {
                        if (row.currencyName.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = countryFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterList = results?.values as ArrayList<CurrencyModel>
                notifyDataSetChanged()
            }
        }
    }
}