package com.diamondxe.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.diamondxe.Beans.GemstoneAttri.GemColorAttribute
import com.diamondxe.Beans.GemstoneAttri.GemCretificateAttribute
import com.diamondxe.Interface.RecyclerInterface
import com.diamondxe.R

class GemCertificateAdapter(
    private val list: List<GemCretificateAttribute>,
    private val context: Context,
    private val recyclerInterface: RecyclerInterface
) : RecyclerView.Adapter<GemCertificateAdapter.RecycleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shapse_gemstone_list, parent, false)
        return RecycleViewHolder(view)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        val shapeName = list[position]

        if (list[position].isSelected) {
            holder.color_type_tv.setBackgroundResource(R.drawable.background_gradient)
            holder.color_type_tv.setTextColor(ContextCompat.getColor(context, R.color.white))

            holder.cardView.elevation = 37f
            holder.cardView.cardElevation = 20f
            holder.cardView.outlineAmbientShadowColor = ContextCompat.getColor(context, R.color.black_50)
            holder.cardView.outlineSpotShadowColor = ContextCompat.getColor(context, R.color.black_50)
        } else {
            holder.color_type_tv.setBackgroundResource(R.drawable.background_white_gray_line)
            holder.color_type_tv.setTextColor(ContextCompat.getColor(context, R.color.black))

            holder.cardView.elevation = 0f
            holder.cardView.cardElevation = 0f
            holder.cardView.outlineAmbientShadowColor = ContextCompat.getColor(context, R.color.white)
            holder.cardView.outlineSpotShadowColor = ContextCompat.getColor(context, R.color.white)
        }
        // Highlight first item for example
        holder.color_type_tv.text=shapeName.name
        // Set click listener
        holder.color_type_tv.setOnClickListener {
            recyclerInterface.itemClick(position, "gemCertificate")
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int = position

    inner class RecycleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val color_type_tv: TextView = itemView.findViewById(R.id.color_type_tv)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }
}