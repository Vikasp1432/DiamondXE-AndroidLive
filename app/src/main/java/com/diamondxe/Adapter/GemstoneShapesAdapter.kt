package com.diamondxe.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.diamondxe.Beans.GemstoneAttri.GemShapeAttribute
import com.diamondxe.Beans.GemstoneType
import com.diamondxe.Interface.RecyclerInterface
import com.diamondxe.R
import com.squareup.picasso.Picasso

class GemstoneShapesAdapter(
    private val list: List<GemShapeAttribute>,
    private val context: Context,
    private val recyclerInterface: RecyclerInterface
) : RecyclerView.Adapter<GemstoneShapesAdapter.RecycleViewHolder>() {

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
            recyclerInterface.itemClick(position, "gemshape")
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int = position

    inner class RecycleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val color_type_tv: TextView = itemView.findViewById(R.id.color_type_tv)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }
}



//This is for when api get
/*class GemstoneShapesAdapter(
    private val list: ArrayList<GemstoneType>,
    private val context: Context,
    private val recyclerInterface: RecyclerInterface
) : RecyclerView.Adapter<GemstoneShapesAdapter.RecycleViewHolder>() {

    private val arraylist: ArrayList<GemstoneType> = ArrayList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_color_type_list, parent, false)
        return RecycleViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        val shapeImageModel = list[position]

        if (shapeImageModel.isSelect) {
            holder.allImg.setBackgroundResource(R.drawable.background_white_with_border)
        } else {
            holder.allImg.setBackgroundResource(0)
        }

        Picasso.with(context)
            .load(shapeImageModel.drawable)
            .placeholder(shapeImageModel.drawable)
            .error(shapeImageModel.drawable)
            .into(holder.allImg)

        if (shapeImageModel.isFirstPosition) {
            val params = holder.rootLayoutRel.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(60, 0, 7, 0)
            holder.rootLayoutRel.layoutParams = params
        }

        holder.allImg.setOnClickListener {
            recyclerInterface.itemClick(position, "shapeImage")
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int = position

    inner class RecycleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootLayoutRel: RelativeLayout = itemView.findViewById(R.id.root_layout_rel)
        val allImg: ImageView = itemView.findViewById(R.id.all_img)
    }
}*/
