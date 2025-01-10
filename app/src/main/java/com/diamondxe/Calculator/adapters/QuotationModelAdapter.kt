package com.dxe.calc.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diamondxe.R
import com.diamondxe.Utils.CommonUtility
import com.dxe.calc.DataBase.QuotationsActivity.QuotationModelEntity
import java.text.DecimalFormat
import java.util.Locale

class QuotationModelAdapter(
    private var dataList: List<QuotationModelEntity>,
    private val onItemClick: (QuotationModelEntity) -> Unit
) : RecyclerView.Adapter<QuotationModelAdapter.DataViewHolder>(), Filterable {

    private var filteredDataList: List<QuotationModelEntity> = dataList

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val qdatetv: TextView = itemView.findViewById(R.id.qdatetv)
        val qNameTV: TextView = itemView.findViewById(R.id.qNameTV)
        val qcaretTv: TextView = itemView.findViewById(R.id.qcaretTv)
        val totalpriceTv: TextView = itemView.findViewById(R.id.totalpriceTv)
        val totalpricecurrency: TextView = itemView.findViewById(R.id.total_price_currency)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.quotation_list_layout, parent, false)
        return DataViewHolder(view)
    }

    fun formatSubTotal(outputValue: String, currencyValue: String, currencyCode: String): String {
        return CommonUtility.currencyConverter(
            currencyValue,
            currencyCode,
            outputValue
        )
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val dataModel = filteredDataList[position]
        Log.e("currencysymbol","....."+dataModel.currencysymbol)
        Log.e("currencyCode","....."+dataModel.currencyCode)
        /*val subTotalFormat = formatSubTotal(dataModel.totalPrice,dataModel.currencyvalue,dataModel.currencyCode)
        Log.e("subTotalFormat","..52.....@@@..."+subTotalFormat)*/

        holder.qdatetv.text = dataModel.currentdatetime
        holder.qNameTV.text = dataModel.itemName
        holder.qcaretTv.text = dataModel.caret
        holder.totalpriceTv.text = formatAmountWithCommas("${dataModel.currencysymbol}${dataModel.totalPrice}")
        /*holder.totalpriceTv.text = subTotalFormat*/
        holder.totalpricecurrency.text = "Total Price (${dataModel.currencyCode})"
        holder.itemView.setOnClickListener {
            onItemClick(dataModel)
        }
    }

    fun formatAmountWithCommas(amount: String): String {
        val formatter = DecimalFormat("#,##,###")
        val parsedAmount = amount.toBigDecimalOrNull() ?: return amount
        return formatter.format(parsedAmount)
    }

    override fun getItemCount() = filteredDataList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint?.toString()?.lowercase(Locale.ROOT) ?: ""
                filteredDataList = if (charSearch.isEmpty()) {
                    dataList
                } else {
                    dataList.filter {
                        it.itemName.lowercase(Locale.ROOT).contains(charSearch)

                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredDataList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredDataList = results?.values as List<QuotationModelEntity>
                notifyDataSetChanged()
            }
        }
    }
}
