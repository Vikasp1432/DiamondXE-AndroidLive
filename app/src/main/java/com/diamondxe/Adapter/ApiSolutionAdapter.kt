package com.diamondxe.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diamondxe.Beans.Transaction
import com.diamondxe.Interface.RecyclerInterface
import com.diamondxe.R
import com.diamondxe.Utils.CommonUtility

class ApiSolutionAdapter(private val list: ArrayList<Transaction>,
                         private val context: Context,
                         private val recyclerInterface: RecyclerInterface
) : RecyclerView.Adapter<ApiSolutionAdapter.RecycleViewHolder>() {

    private val arraylist: ArrayList<Transaction> = ArrayList()
    private val handler = Handler(Looper.getMainLooper())

    init {
        arraylist.clear()
        arraylist.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.apisolution_details_layout, parent, false)
        return RecycleViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        val item = list[position]

        holder.transcation_id.text = if (item.transaction_id.isNotEmpty()) item.transaction_id else "-"
        holder.statusTv.text = if (item.payment_status.isNotEmpty()) item.payment_status else "-"

        holder.amountTv.text = if (item.amount.isNotEmpty())
            "${item.currency_symbol} ${item.amount}" else "-"

        holder.remark.text = if (item.description.isNotEmpty()) item.description else "-"
        holder.paymentModeTv.text = if (item.payment_mode.isNotEmpty()) item.payment_mode else "-"
        holder.opening_balance.text = if (item.opening_balance.isNotEmpty())
            "${item.currency_symbol} ${item.opening_balance}" else "-"

        holder.closing_balance.text = if (item.closing_balance.isNotEmpty())
            "${item.currency_symbol} ${item.closing_balance}" else "-"

    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int = position

    inner class RecycleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transcation_id: TextView = itemView.findViewById(R.id.transcation_id)
        val statusTv: TextView = itemView.findViewById(R.id.status_tv)
        val amountTv: TextView = itemView.findViewById(R.id.amount_tv)
        val opening_balance: TextView = itemView.findViewById(R.id.opening_balance)
        val closing_balance: TextView = itemView.findViewById(R.id.closing_balance)
        val paymentModeTv: TextView = itemView.findViewById(R.id.payment_mode_tv)
        val remark: TextView = itemView.findViewById(R.id.remark)
    }
}