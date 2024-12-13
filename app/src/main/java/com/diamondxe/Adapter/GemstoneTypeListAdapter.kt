package com.diamondxe.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.diamondxe.Beans.GemstoneType
import com.diamondxe.Interface.RecyclerInterface
import com.diamondxe.R
import com.squareup.picasso.Picasso

class GemstoneTypeListAdapter(
    private val list: ArrayList<GemstoneType>,
    private val context: Context,
    private val recyclerInterface: RecyclerInterface
) : RecyclerView.Adapter<GemstoneTypeListAdapter.RecycleViewHolder>() {

    private val arraylist: ArrayList<GemstoneType> = ArrayList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_shape_image_list, parent, false)
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
}
