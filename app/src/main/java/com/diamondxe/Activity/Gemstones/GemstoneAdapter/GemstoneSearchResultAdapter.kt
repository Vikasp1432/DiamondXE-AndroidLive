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

class GemstoneSearchResultAdapter(private val context: Context,
                                  private val userRole: String,
                                  private val list: MutableList<SearchGemstoneTypeModel>, private val recyclerInterface: RecyclerInterface,
) : RecyclerView.Adapter<GemstoneSearchResultAdapter.RecycleViewHolder>() {
    var handler: Handler = Handler(Looper.getMainLooper())
    private val arrayList: MutableList<SearchGemstoneTypeModel> = ArrayList(list)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val layoutRes = R.layout.gemstone_card_layout
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return RecycleViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        val model = list[position]

        // Load Image
        if (!list[position].diamond_image.equals("", ignoreCase = true)) {
            Picasso.with(context)
                .load(list[position].diamond_image)
                .placeholder(R.mipmap.phl_diamond)
                .error(R.mipmap.phl_diamond)
                .into(holder.imageView)
        } else {
            Picasso.with(context)
                .load(R.mipmap.phl_diamond)
                .placeholder(R.mipmap.phl_diamond)
                .error(R.mipmap.phl_diamond)
                .resize(500, 500)
                .centerCrop()
                .into(holder.imageView)
        }

        // Bind Text Data
        /*holder.supplierIdTv.text="#" + list[position].stock_no + " | " + "ID: " + list[position].supplier_id*/
        holder.supplierIdTv.text=list[position].stock_no + " | " + "ID: " + list[position].supplier_id
        /*holder.itemTypeTv.text=list[position].carat + context.resources.getString(R.string.ct) + " | " + list[position].color + " | " + list[position].clarity*/
        holder.itemTypeTv.text=list[position].carat + context.resources.getString(R.string.ct_gem) + " | " + list[position].clarity
        holder.nameTv.text = model.item_name


        Log.e("cut_grade",""+list[position].cut_grade)
        Log.e("polish",""+list[position].polish)
        Log.e("symmetry",""+list[position].symmetry)
        Log.e("fluorescence_intensity",""+list[position].fluorescence_intensity)
        Log.e("certificate_name",""+list[position].certificate_name)
        Log.e("--","-----------------------------------------")

       /* if (list[position].cut_grade.isNullOrEmpty() || list[position].cut_grade.equals("0", ignoreCase = true)) {
            holder.cutGradeTv.visibility = View.GONE
        } else {
            holder.cutGradeTv.visibility = View.VISIBLE
            holder.cutGradeTv.text = limitWords(list[position].cut_grade, 4)
        }*/

        /*if (list[position].polish.isNullOrEmpty() || list[position].polish.equals("0", ignoreCase = true)) {
            holder.polishTv.visibility = View.GONE
        } else {
            holder.polishTv.visibility = View.VISIBLE
            holder.polishTv.text = limitWords(list[position].polish, 4)
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
           // holder.certificate_name_tv.visibility = View.GONE
            holder.certificate_name_tv.text ="-"
        } else {
            holder.certificate_name_tv.visibility = View.VISIBLE
           /* holder.certificate_name_tv.text = limitWords(list[position].certificate_name, 7)*/
            holder.certificate_name_tv.text = list[position].certificate_name
        }

        if (list[position].inscription.isNullOrEmpty() || list[position].inscription.equals("0", ignoreCase = true)) {

            holder.inscription.text ="-"
        } else {
            holder.inscription.visibility = View.VISIBLE
            /*holder.inscription.text = limitWords(list[position].inscription, 7)*/
            holder.inscription.text = list[position].inscription
        }

        /*if (list[position].cut_grade==("")|| list[position].cut_grade.equals("0", ignoreCase = true)) {
            holder.cutGradeTv.visibility=View.GONE
        } else {
            holder.cutGradeTv.text=list[position].cut_grade
        }
        if (list[position].polish==("") ||  list[position].polish.equals("0", ignoreCase = true)) {
            holder.polishTv.visibility=View.GONE
        } else {
            holder.polishTv.text=list[position].polish
        }
        if (list[position].symmetry==("") || list[position].symmetry.equals("0", ignoreCase = true)) {
            holder.symmetry_tv.visibility = View.GONE
        } else {
            holder.symmetry_tv.visibility = View.VISIBLE
            holder.symmetry_tv.text = list[position].symmetry
        }

        if (list[position].fluorescence_intensity==("") || list[position].fluorescence_intensity.equals("0", ignoreCase = true)) {
            holder.fluorescence_intensity_tv.visibility=View.GONE
        } else {
            holder.fluorescence_intensity_tv.text=list[position].fluorescence_intensity
        }

        if (list[position].certificate_name==("") || list[position].certificate_name.equals("0", ignoreCase = true)) {
            holder.certificateNameTv.visibility=View.GONE
        } else {
            holder.certificateNameTv.text=list[position].certificate_name
        }*/

        /*holder.table_perc_tv.text="T: " + list[position].table_perc
        holder.depth_perc.text="D: " + list[position].depth_perc*/
        holder.measurement_tv.text="M: " + list[position].measurement


        /*if (!list[position].discount
                .equals("", ignoreCase = true) && !list[position].discount
                .equals("-", ignoreCase = true) && !list[position].discount.equals("0", ignoreCase = true)
        ) {
            holder.discount_tv.text=list[position].discount
            holder.discount_tv.visibility=View.VISIBLE
        } else {
            holder.discount_tv.visibility=View.GONE
        }*/


        //discount_sub_total_tv
        /* String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
        String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, subtotal);*/
        if (list[position].coupon_discount_perc > 0) {
            Log.e(
                "getSubtotalaftercoupondiscount",
                "159................" + list[position].subtotal_after_coupon_discount
            )
            Log.e("getShowingSubTotal....", "160........." + list[position].subtotal)

            val selectedCurrencyValue =
                CommonUtility.getGlobalString(context, "selected_currency_value")
            val selectedCurrencyCode =
                CommonUtility.getGlobalString(context, "selected_currency_code")
            val getsubtotalPrice: String =
                list[position].subtotal_after_coupon_discount.toString()
            val subTotalFormat = CommonUtility.currencyConverter(
                selectedCurrencyValue,
                selectedCurrencyCode,
                getsubtotalPrice
            )


            holder.sub_total_tv.text= list[position].currencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat)
            holder.discount_sub_total_tv.paintFlags = holder.discount_sub_total_tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.discount_sub_total_tv.text= list[position].currencySymbol + "" + CommonUtility.currencyFormat(list[position].showingSubTotal)
        } else {
            if (!list[position].subtotal.equals("", ignoreCase = true)) {
                Log.e("getShowingSubTotal..", ".Else........" + list[position].showingSubTotal)
                holder.discount_sub_total_tv.visibility=View.GONE
                holder.sub_total_tv.text= list[position].currencySymbol + "" + CommonUtility.currencyFormat(list[position].showingSubTotal)
            }
        }

        if (list[position].category.equals("Natural", ignoreCase = true)) {
            holder.diamond_type.setBackgroundResource(R.drawable.background_yellow)
            holder.diamond_type.text="NATURAL"
        } else {
            holder.diamond_type.setBackgroundResource(R.drawable.background_green_light_small_round_cornor)
            holder.diamond_type.text="LAB"
        }

        if (list[position].status.equals("Available", ignoreCase = true)) {
            holder.status_img.visibility=View.VISIBLE

            holder.status_img.setBackgroundResource(R.drawable.available)
        } else if (list[position].status.equals("On Hold", ignoreCase = true)) {
            holder.status_img.visibility=View.VISIBLE
            holder.status_img.setBackgroundResource(R.drawable.onhold)
        } else {
            holder.status_img.visibility=View.GONE
        }

        // Visibility Logic
        if (list[position].is_returnable.equals("1", ignoreCase = true)) {
            holder.returnableImg.visibility=View.VISIBLE
        } else {
            holder.returnableImg.visibility=View.GONE
        }


        if (list[position].is_cart.equals("1", ignoreCase = true)) {
            holder.add_to_cart_tv.text=context.resources.getString(R.string.go_to_cart)
        } else {
            holder.add_to_cart_tv.text=context.resources.getString(R.string.add_to_cart)
        }

        if (list[position].is_wishlist.equals("1", ignoreCase = true)) {
            holder.addToFavtImg.setBackgroundResource(R.drawable.wishlist_filled)
        } else {
            holder.addToFavtImg.setBackgroundResource(R.drawable.wishlist)
        }



        if (list[position].setVisible) {
            holder.search_circle_card_lin.visibility=View.INVISIBLE
        } else {
            holder.search_circle_card_lin.visibility=View.GONE
        }


        holder.returnable_img.setOnClickListener(View.OnClickListener {
            holder.return_policy_tv.visibility=View.VISIBLE
            //recyclerInterface.itemClick(position,"returnablePopup");
            handler.postDelayed(Runnable { holder.return_policy_tv.visibility=View.GONE }, 2000)
        })
        // Click Listeners
        holder.addToFavtImg.setOnClickListener {
            model.is_wishlist = model.is_wishlist
            notifyItemChanged(position)
        }

        holder.rootLayout.setOnClickListener {
            recyclerInterface.itemClick(
                position,
                "searchDiamondDetails"
            )
        }

        holder.returnable_img.setOnClickListener {
            holder.return_policy_tv.visibility = View.VISIBLE
            handler.postDelayed(Runnable {
                holder.return_policy_tv.visibility = View.GONE
            }, 2000)
        }

        holder.addToFavtImg.setOnClickListener(View.OnClickListener {
            if (list[position].is_wishlist.equals("1", ignoreCase = true)) {
                recyclerInterface.itemClick(position, "removeFromFavt")
            } else {
                recyclerInterface.itemClick(position, "addToFavt")
            }
        })

        holder.add_to_cart_tv.setOnClickListener {
            recyclerInterface.itemClick(position, "addToCart")
        }
    }

    override fun getItemCount(): Int = list.size

    fun filter(query: String) {
        val filteredList = arrayList.filter {
            it.item_name.contains(query, ignoreCase = true) ||
                    it.growth_type.contains(query, ignoreCase = true)
        }
        list.clear()
        list.addAll(filteredList)
        notifyDataSetChanged()
    }


    fun limitWords(text: String, maxChars: Int): String {
        return if (text.length > maxChars) {
            text.take(maxChars) + ".."
        } else {
            text
        }
    }

    class RecycleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootLayout: CardView = itemView.findViewById(R.id.root_layout)
        val supplierIdTv: TextView = itemView.findViewById(R.id.supplier_id_tv)
        val nameTv: TextView = itemView.findViewById(R.id.name_tv)
        val itemTypeTv: TextView = itemView.findViewById(R.id.item_type_tv)
       // val cutGradeTv: TextView = itemView.findViewById(R.id.cut_grade_tv)
        val certificateNameTv: TextView = itemView.findViewById(R.id.certificate_name_tv)
        val polishTv: TextView = itemView.findViewById(R.id.polish_tv)
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val addToFavtImg: ImageView = itemView.findViewById(R.id.add_to_favt_img)
        val returnableImg: ImageView = itemView.findViewById(R.id.returnable_img)
        val symmetry_tv: TextView = itemView.findViewById(R.id.symmetry_tv)
        val fluorescence_intensity_tv: TextView = itemView.findViewById(R.id.fluorescence_intensity_tv)

        /*val table_perc_tv: TextView = itemView.findViewById(R.id.table_perc_tv)
        val depth_perc: TextView = itemView.findViewById(R.id.depth_perc)*/
        val measurement_tv: TextView = itemView.findViewById(R.id.measurement_tv)
        val discount_tv: TextView = itemView.findViewById(R.id.discount_tv)
        val status_img: ImageView = itemView.findViewById(R.id.status_img)
        val return_policy_tv: TextView = itemView.findViewById(R.id.return_policy_tv)

        val returnable_img: ImageView = itemView.findViewById(R.id.returnable_img)
        val search_circle_card_lin: RelativeLayout=itemView.findViewById(R.id.search_circle_card_lin)

        val diamond_type: TextView = itemView.findViewById(R.id.diamond_type)
        val sub_total_tv: TextView = itemView.findViewById(R.id.sub_total_tv)
        val discount_sub_total_tv: TextView = itemView.findViewById(R.id.discount_sub_total_tv)
        val add_to_cart_tv: TextView = itemView.findViewById(R.id.add_to_cart_tv)
        val  certificate_name_tv:TextView =itemView.findViewById(R.id.certificate_name_tv)

        val  inscription:TextView =itemView.findViewById(R.id.inscription)

    }
}
