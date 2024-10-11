package com.dxe.calc.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diamondxe.R
import com.dxe.calc.models.RingSize



class RingSizeAdapter (private val ringSize: List<RingSize>,
                       private var selectedPosition: Int,
                       private val onItemClick: (Int, RingSize) -> Unit

) : RecyclerView.Adapter<RingSizeAdapter.RingSizeAdapter>(){


    class RingSizeAdapter(view: View) : RecyclerView.ViewHolder(view) {
        val tvDiameterMM: TextView = view.findViewById(R.id.tvDiameterMM)
        val tvUSA: TextView = view.findViewById(R.id.tvUSA)
        val tvEUR: TextView = view.findViewById(R.id.tvEUR)
        val tvUK: TextView = view.findViewById(R.id.tvUK)
        val tvIndia: TextView = view.findViewById(R.id.tvIndia)
        val layoutselect: LinearLayout =view.findViewById(R.id.layout_select)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RingSizeAdapter {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ringsize_adapter, parent, false)
        return RingSizeAdapter(view)
    }

    override fun getItemCount(): Int {

        return ringSize.size
    }

    override fun onBindViewHolder(holder: RingSizeAdapter, position: Int) {
        val  size=ringSize[position]
        holder.tvDiameterMM.text = size.diameter_mm
        holder.tvUSA.text = size.usa
        holder.tvEUR.text = size.diameter_EUR
        holder.tvUK.text = size.diameter_UK
        holder.tvIndia.text = size.india


        if (position == selectedPosition) {
            holder.layoutselect.setBackgroundColor(Color.parseColor("#D9D9D9"));
        } else {
            holder.layoutselect.setBackgroundColor(Color.TRANSPARENT)
        }

        holder.itemView.setOnClickListener {
            onItemClick(position, size)
            updateSelectedIndex(position)
        }
    }

    fun updateSelectedIndex(newSelectedIndex: Int) {
        val previousSelectedIndex = selectedPosition
        selectedPosition = newSelectedIndex
        notifyItemChanged(previousSelectedIndex)
        notifyItemChanged(newSelectedIndex)
    }


}