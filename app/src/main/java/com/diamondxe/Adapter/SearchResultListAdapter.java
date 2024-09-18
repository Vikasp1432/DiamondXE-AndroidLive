package com.diamondxe.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.SearchResultTypeModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class SearchResultListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<SearchResultTypeModel> list;
    Context context;
    int last;
    public ArrayList<SearchResultTypeModel> arraylist;
    RecyclerInterface recyclerInterface;

    Handler handler = new Handler(Looper.getMainLooper());

    String userRole = "";

    public SearchResultListAdapter(ArrayList<SearchResultTypeModel> list, Context context, RecyclerInterface recyclerInterface, String userRole) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<SearchResultTypeModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
        this.userRole = userRole;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v;
        // Check Login USer Type Like Buyer and Dealer and Set Condition and Layout.
        if(userRole.equalsIgnoreCase("DEALER"))
        {
            v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_search_result_list, parent, false);
        }
        else {
            v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_search_result_list_for_buyer, parent, false);
        }

        return new RecycleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        if(!list.get(position).getDiamond_image().equalsIgnoreCase(""))
        {
            Picasso.with(context)
                    .load(list.get(position).getDiamond_image())
                    .placeholder(R.mipmap.phl_diamond)
                    .error(R.mipmap.phl_diamond)
                    .into(holder.image_view);
        }
        else{
            Picasso.with(context)
                    .load(R.mipmap.phl_diamond)
                    .placeholder(R.mipmap.phl_diamond)
                    .error(R.mipmap.phl_diamond)
                    .resize(500,500)
                    .centerCrop()
                    .into(holder.image_view);
        }

        holder.supplier_id_tv.setText("#"+list.get(position).getStock_no() + " | " + "ID: " + list.get(position).getSupplier_id());
        holder.name_tv.setText(list.get(position).getShape());
        holder.item_type_tv.setText(list.get(position).getCarat() + context.getResources().getString(R.string.ct) + " | " + list.get(position).getColor() + " | " + list.get(position).getClarity());

        if(list.get(position).getCut_grade().equalsIgnoreCase(""))
        {
            holder.cut_grade_tv.setText("-");
        }
        else{
            holder.cut_grade_tv.setText(list.get(position).getCut_grade());
        }

        if(list.get(position).getPolish().equalsIgnoreCase(""))
        {
            holder.polish_tv.setText("-");
        }
        else{
            holder.polish_tv.setText(list.get(position).getPolish());
        }

        if(list.get(position).getSymmetry().equalsIgnoreCase(""))
        {
            holder.symmetry_tv.setText("-");
        }
        else{
            holder.symmetry_tv.setText(list.get(position).getSymmetry());
        }

        if(list.get(position).getFluorescence_intensity().equalsIgnoreCase(""))
        {
            holder.fluorescence_intensity_tv.setText("-");
        }
        else{
            holder.fluorescence_intensity_tv.setText(list.get(position).getFluorescence_intensity());
        }

        if(list.get(position).getCertificate_name().equalsIgnoreCase(""))
        {
            holder.certificate_name_tv.setText("-");
        }
        else {
            holder.certificate_name_tv.setText(list.get(position).getCertificate_name());
        }


        holder.table_perc_tv.setText("T: " + list.get(position).getTable_perc());
        holder.depth_perc.setText("D: " + list.get(position).getDepth_perc());
        holder.measurement_tv.setText("M: " + list.get(position).getMeasurement());

        if(!list.get(position).getR_discount().equalsIgnoreCase("") && !list.get(position).getR_discount().equalsIgnoreCase("-"))
        {
            holder.discount_tv.setText(list.get(position).getR_discount());
            holder.discount_tv.setVisibility(View.VISIBLE);
        }
        else{
            holder.discount_tv.setVisibility(View.GONE);
        }


        DecimalFormat formatter = new DecimalFormat("#,###,###");

        if(!list.get(position).getSubtotal().equalsIgnoreCase(""))
        {
            /*try {
                // Parse the string to a number
                long number = Long.parseLong(list.get(position).getSubtotal());

                // Format the number
                String subTotal = formatter.format(number);
                holder.sub_total_tv.setText(ApiConstants.rupeesIcon + "" + subTotal);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Handle the exception, maybe set a default value or show an error message
            }*/

            holder.sub_total_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));
        }else {}

        if(list.get(position).getCategory().equalsIgnoreCase("Natural"))
        {
            holder.diamond_type.setBackgroundResource(R.drawable.background_yellow);
            holder.diamond_type.setText("NATURAL");
        }
        else{
            holder.diamond_type.setBackgroundResource(R.drawable.background_green_light_small_round_cornor);
            holder.diamond_type.setText("LAB");
        }


        if(list.get(position).getIs_returnable().equalsIgnoreCase("1"))
        {
            holder.returnable_img.setVisibility(View.VISIBLE);
        }
        else{
            holder.returnable_img.setVisibility(View.GONE);
        }
        if(list.get(position).getStatus().equalsIgnoreCase("Available"))
        {
            holder.status_img.setVisibility(View.VISIBLE);

            holder.status_img.setBackgroundResource(R.drawable.available);
        }
        else if(list.get(position).getStatus().equalsIgnoreCase("On Hold")){
            holder.status_img.setVisibility(View.VISIBLE);
            holder.status_img.setBackgroundResource(R.drawable.onhold);
        }
        else
        {
            holder.status_img.setVisibility(View.GONE);
        }

        if(list.get(position).getIs_cart().equalsIgnoreCase("1"))
        {
            holder.add_to_cart_tv.setText(context.getResources().getString(R.string.go_to_cart));
        }
        else{
            holder.add_to_cart_tv.setText(context.getResources().getString(R.string.add_to_cart));
        }

        if(list.get(position).getIs_wishlist().equalsIgnoreCase("1"))
        {
            holder.add_to_favt_img.setBackgroundResource(R.drawable.wishlist_filled);
        }
        else{
            holder.add_to_favt_img.setBackgroundResource(R.drawable.wishlist);
        }

        if(list.get(position).isVisible())
        {
            holder.search_circle_card_lin.setVisibility(View.INVISIBLE);
        }
        else{
            holder.search_circle_card_lin.setVisibility(View.GONE);
        }

        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerInterface.itemClick(position,"searchDiamondDetails");
            }
        });

        holder.returnable_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.return_policy_tv.setVisibility(View.VISIBLE);
                //recyclerInterface.itemClick(position,"returnablePopup");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.return_policy_tv.setVisibility(View.GONE);
                    }
                }, 2000);
            }
        });

        holder.add_to_favt_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(list.get(position).getIs_wishlist().equalsIgnoreCase("1"))
                {
                    recyclerInterface.itemClick(position, "removeFromFavt");
                }
                else
                {
                    recyclerInterface.itemClick(position, "addToFavt");
                }

            }
        });

        holder.add_to_cart_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerInterface.itemClick(position, "addToCart");

                /*if(list.get(position).getIs_cart().equalsIgnoreCase("") || list.get(position).getIs_cart().equalsIgnoreCase("0"))
                {
                    recyclerInterface.itemClick(position, "addToCart");
                }
                else {}*/
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class RecycleViewHolder extends RecyclerView.ViewHolder {

        CardView root_layout;
        TextView supplier_id_tv, name_tv, item_type_tv,cut_grade_tv, certificate_name_tv, polish_tv, symmetry_tv, fluorescence_intensity_tv,table_perc_tv,
                depth_perc,measurement_tv, add_to_cart_tv, sub_total_tv,return_policy_tv, discount_tv,diamond_type;
        ImageView add_to_favt_img,image_view, returnable_img, status_img;
        RelativeLayout search_circle_card_lin;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            root_layout = itemView.findViewById(R.id.root_layout);
            image_view = itemView.findViewById(R.id.image_view);
            add_to_favt_img = itemView.findViewById(R.id.add_to_favt_img);
            returnable_img = itemView.findViewById(R.id.returnable_img);
            status_img = itemView.findViewById(R.id.status_img);

            supplier_id_tv = itemView.findViewById(R.id.supplier_id_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            item_type_tv = itemView.findViewById(R.id.item_type_tv);

            cut_grade_tv = itemView.findViewById(R.id.cut_grade_tv);
            certificate_name_tv = itemView.findViewById(R.id.certificate_name_tv);
            polish_tv = itemView.findViewById(R.id.polish_tv);
            symmetry_tv = itemView.findViewById(R.id.symmetry_tv);
            fluorescence_intensity_tv = itemView.findViewById(R.id.fluorescence_intensity_tv);
            table_perc_tv = itemView.findViewById(R.id.table_perc_tv);
            depth_perc = itemView.findViewById(R.id.depth_perc);
            measurement_tv = itemView.findViewById(R.id.measurement_tv);
            add_to_cart_tv = itemView.findViewById(R.id.add_to_cart_tv);
            sub_total_tv = itemView.findViewById(R.id.sub_total_tv);
            return_policy_tv = itemView.findViewById(R.id.return_policy_tv);
            discount_tv = itemView.findViewById(R.id.discount_tv);
            diamond_type = itemView.findViewById(R.id.diamond_type);

            search_circle_card_lin = itemView.findViewById(R.id.search_circle_card_lin);

        }
    }


}
