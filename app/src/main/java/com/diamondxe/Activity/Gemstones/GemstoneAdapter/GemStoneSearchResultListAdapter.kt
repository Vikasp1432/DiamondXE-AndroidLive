package com.diamondxe.Activity.Gemstones.GemstoneAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.diamondxe.Beans.SearchGemstoneTypeModel
import com.diamondxe.Interface.RecyclerInterface
import com.diamondxe.R
import com.diamondxe.Utils.CommonUtility
import com.squareup.picasso.Picasso


class GemStoneSearchResultListAdapter(
    var context: Context,
    userRole: String,
    var list: ArrayList<SearchGemstoneTypeModel>,

    recyclerInterface: RecyclerInterface,

    ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var last: Int = 0
    var arraylist: ArrayList<SearchGemstoneTypeModel> = ArrayList()
    var recyclerInterface: RecyclerInterface
    var handler: Handler = Handler(Looper.getMainLooper())
    var userRole: String = ""
    var searchType: String = ""

    init {
        arraylist.clear()
        arraylist.addAll(list)
        this.recyclerInterface = recyclerInterface
        this.userRole = userRole
        this.searchType = searchType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View
        if (userRole.equals("BUYER", ignoreCase = true)) {
            v = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_search_result_list_wise_for_buyer, parent, false)
        } else {
            v = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_search_result_list_wise, parent, false)
        }

        return RecycleViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val holder = viewHolder as RecycleViewHolder

        if (!list[position].diamond_image.equals("", ignoreCase = true)) {
            Picasso.with(context)
                .load(list[position].diamond_image)
                .placeholder(R.mipmap.phl_diamond)
                .error(R.mipmap.phl_diamond)
                .into(holder.image_view)
        } else {
            Picasso.with(context)
                .load(R.mipmap.phl_diamond)
                .placeholder(R.mipmap.phl_diamond)
                .error(R.mipmap.phl_diamond)
                .into(holder.image_view)
        }
        Log.e("searchType", ".....88.......**************....$searchType")
        if (searchType == "dxeluxe") {
            holder.luex_tag.visibility = View.VISIBLE
        } else {
            holder.luex_tag.visibility = View.GONE
        }

        holder.supplier_id_tv.text =
            "#" + list[position].stock_no + " | " + "ID: " + list[position].supplier_id
        holder.name_tv.text = list[position].shape
        /*holder.item_type_tv.text =
            list[position].carat + context.resources.getString(R.string.ct) + " | " + list[position].color + " | " + list[position].clarity*/
        holder.item_type_tv.text =
            list[position].carat + context.resources.getString(R.string.ct) + " | " + list[position].clarity

        /*if (list[position].cut_grade.equals("", ignoreCase = true)) {
            holder.cut_grade_tv.visibility=View.GONE
        } else {
            holder.cut_grade_tv.text = limitWords(list[position].cut_grade, 4)
        }

        if (list[position].polish.isNullOrEmpty() || list[position].polish.equals("0", ignoreCase = true)) {
            holder.polish_tv.visibility = View.GONE
        } else {
            holder.polish_tv.visibility = View.VISIBLE
            holder.polish_tv.text = limitWords(list[position].polish, 4)
        }

        if (list[position].symmetry.isNullOrEmpty() || list[position].symmetry.equals("0", ignoreCase = true)) {
            holder.symmetry_tv.visibility = View.GONE
        } else {
            holder.symmetry_tv.visibility = View.VISIBLE
            holder.symmetry_tv.text = limitWords(list[position].symmetry, 4)
        }

        if (list[position].fluorescence_intensity.isNullOrEmpty() || list[position].fluorescence_intensity.equals("0", ignoreCase = true)) {
            holder.fluorescence_intensity_tv.visibility = View.GONE
        } else {
            holder.fluorescence_intensity_tv.visibility = View.VISIBLE
            holder.fluorescence_intensity_tv.text = limitWords(list[position].fluorescence_intensity, 4)
        }*/

        if (list[position].certificate_name.isNullOrEmpty() || list[position].certificate_name.equals("0", ignoreCase = true)) {
            //holder.certificate_name_tv.visibility = View.GONE
            holder.certificate_name_tv.text = "-"
        } else {
            holder.certificate_name_tv.visibility = View.VISIBLE
           /* holder.certificate_name_tv.text = limitWords(list[position].certificate_name, 4)*/
            holder.certificate_name_tv.text = list[position].certificate_name
        }

        if (list[position].certificate_name.isNullOrEmpty() || list[position].inscription.equals("0", ignoreCase = true)) {
            //holder.inscription.visibility = View.GONE
            holder.inscription.text = "-"
        } else {
            holder.inscription.visibility = View.VISIBLE
         /*   holder.inscription.text = limitWords(list[position].inscription, 4)*/
            holder.inscription.text = list[position].inscription
        }

       /* if (list[position].polish.equals("", ignoreCase = true)) {
            holder.polish_tv.visibility=View.GONE
        } else {
            holder.polish_tv.text = list[position].polish
        }

        if (list[position].symmetry.equals("", ignoreCase = true)) {
            holder.symmetry_tv.visibility=View.GONE
        } else {
            holder.symmetry_tv.text = list[position].symmetry
        }

        if (list[position].fluorescence_intensity.equals("", ignoreCase = true)) {
            holder.fluorescence_intensity_tv.visibility=View.GONE
        } else {
            holder.fluorescence_intensity_tv.text = list[position].fluorescence_intensity
        }

        if (list[position].certificate_name.equals("", ignoreCase = true)) {
            holder.certificate_name_tv.visibility=View.GONE
        } else {
            holder.certificate_name_tv.text = list[position].certificate_name
        }*/

        if (list[position].setVisible) {
            holder.search_circle_card_lin.visibility = View.INVISIBLE
        } else {
            holder.search_circle_card_lin.visibility = View.GONE
        }

        /*if (!list[position].discount.equals(
                "",
                ignoreCase = true
            ) && !list[position].discount.equals("-", ignoreCase = true)
        ) {
            holder.discount_tv.text = list[position].discount
            holder.discount_tv.visibility = View.VISIBLE
        } else {
            holder.discount_tv.visibility = View.GONE
        }*/

       /* holder.table_perc_tv.text = "T: " + list[position].table_perc
        holder.depth_perc.text = "D: " + list[position].depth_perc*/
        holder.measurement_tv.text = "M: " + list[position].measurement



        if (list[position].coupon_discount_perc > 0) {
            val getsubtotalPrice = list[position].subtotal_after_coupon_discount.toString()
            holder.sub_total_tv.text = list[position].currencySymbol + "" +
                    CommonUtility.currencyFormat(getsubtotalPrice)
            holder.dis_sub_total_tv.paintFlags =
                holder.dis_sub_total_tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.dis_sub_total_tv.text = list[position].currencySymbol + "" +
                    CommonUtility.currencyFormat(list[position].showingSubTotal)

            //   holder.sub_total_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));
        } else {
            if (!list[position].subtotal.equals("", ignoreCase = true)) {
                holder.dis_sub_total_tv.visibility = View.GONE
                holder.sub_total_tv.text = list[position].currencySymbol + "" +
                        CommonUtility.currencyFormat(list[position].showingSubTotal)
            }
        }

        if (list[position].is_returnable.equals("1", ignoreCase = true)) {
            holder.returnable_img.visibility = View.VISIBLE
        } else {
            holder.returnable_img.visibility = View.GONE
        }
        if (list[position].status.equals("Available", ignoreCase = true)) {
            holder.status_img.visibility = View.VISIBLE

            holder.status_img.setBackgroundResource(R.drawable.available)
        } else if (list[position].status.equals("On Hold", ignoreCase = true)) {
            holder.status_img.visibility = View.VISIBLE
            holder.status_img.setBackgroundResource(R.drawable.onhold)
        } else {
            holder.status_img.visibility = View.GONE
        }

        if (list[position].is_cart.equals("1", ignoreCase = true)) {
            holder.add_to_card_img.setBackgroundResource(R.drawable.cart_filled)
        } else {
            holder.add_to_card_img.setBackgroundResource(R.drawable.cart)
        }

        if (list[position].is_wishlist.equals("1", ignoreCase = true)) {
            holder.add_to_favt_img.setBackgroundResource(R.drawable.wishlist_filled)
        } else {
            holder.add_to_favt_img.setBackgroundResource(R.drawable.wishlist)
        }

        if (list[position].category.equals("Natural", ignoreCase = true)) {
            holder.diamond_type.setBackgroundResource(R.drawable.background_yellow)
            holder.diamond_type.text = "NATURAL"
        } else {
            holder.diamond_type.setBackgroundResource(R.drawable.background_green_light_small_round_cornor)
            holder.diamond_type.text = "LAB"
        }

        holder.root_layout.setOnClickListener {
            recyclerInterface.itemClick(
                position,
                "searchDiamondDetails"
            )
        }

        holder.returnable_img.setOnClickListener {
            holder.return_policy_tv.visibility = View.VISIBLE
            //recyclerInterface.itemClick(position,"returnablePopup");
            handler.postDelayed(
                { holder.return_policy_tv.visibility = View.GONE },
                2000
            )
        }

        holder.add_to_favt_card.setOnClickListener {
            if (list[position].is_wishlist.equals("1", ignoreCase = true)) {
                recyclerInterface.itemClick(position, "removeFromFavt")
            } else {
                recyclerInterface.itemClick(position, "addToFavt")
            }
        }

        holder.add_to_card_view.setOnClickListener {
            if (list[position].is_cart.equals(
                    "",
                    ignoreCase = true
                ) || list[position].is_cart.equals("0", ignoreCase = true)
            ) {
                recyclerInterface.itemClick(position, "addToCart")
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun limitWords(text: String, maxChars: Int): String {
        return if (text.length > maxChars) {
            text.take(maxChars) + ".."
        } else {
            text
        }
    }

    internal inner class RecycleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var root_layout: CardView = itemView.findViewById(R.id.root_layout)
        var add_to_favt_card: CardView = itemView.findViewById(R.id.add_to_favt_card)
        var add_to_card_view: CardView = itemView.findViewById(R.id.add_to_card_view)
        var supplier_id_tv: TextView = itemView.findViewById(R.id.supplier_id_tv)
        var name_tv: TextView = itemView.findViewById(R.id.name_tv)
        var item_type_tv: TextView = itemView.findViewById(R.id.item_type_tv)
        var cut_grade_tv: TextView = itemView.findViewById(R.id.cut_grade_tv)
        var certificate_name_tv: TextView = itemView.findViewById(R.id.certificate_name_tv)
        var polish_tv: TextView = itemView.findViewById(R.id.polish_tv)
        var symmetry_tv: TextView = itemView.findViewById(R.id.symmetry_tv)
        var fluorescence_intensity_tv: TextView = itemView.findViewById(R.id.fluorescence_intensity_tv)
        var table_perc_tv: TextView = itemView.findViewById(R.id.table_perc_tv)
        var depth_perc: TextView = itemView.findViewById(R.id.depth_perc)
        var measurement_tv: TextView = itemView.findViewById(R.id.measurement_tv)
        var add_to_cart_tv: TextView? = null
        var sub_total_tv: TextView = itemView.findViewById(R.id.sub_total_tv)

        //add_to_cart_tv = itemView.findViewById(R.id.add_to_cart_tv);
        var dis_sub_total_tv: TextView = itemView.findViewById(R.id.dis_sub_total_tv)
        var return_policy_tv: TextView = itemView.findViewById(R.id.return_policy_tv)
        var discount_tv: TextView = itemView.findViewById(R.id.discount_tv)
        var diamond_type: TextView = itemView.findViewById(R.id.diamond_type)
        var add_to_favt_img: ImageView = itemView.findViewById(R.id.add_to_favt_img)
        var image_view: ImageView = itemView.findViewById(R.id.image_view)
        var returnable_img: ImageView = itemView.findViewById(R.id.returnable_img)
        var status_img: ImageView = itemView.findViewById(R.id.status_img)
        var add_to_card_img: ImageView = itemView.findViewById(R.id.add_to_card_img)
        var search_circle_card_lin: RelativeLayout =
            itemView.findViewById(R.id.search_circle_card_lin)
        var luex_tag: RelativeLayout = itemView.findViewById(R.id.luex_tag)


        var inscription: TextView = itemView.findViewById(R.id.inscription)

    }
}
