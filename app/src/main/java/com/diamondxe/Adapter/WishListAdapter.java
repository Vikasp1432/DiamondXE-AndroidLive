package com.diamondxe.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.Beans.SearchResultTypeModel;
import com.diamondxe.Beans.WishListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class WishListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<WishListModel> list;
    Context context;
    int last;
    public ArrayList<WishListModel> arraylist;
    RecyclerInterface recyclerInterface;

    Handler handler = new Handler(Looper.getMainLooper());


    public WishListAdapter(ArrayList<WishListModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<WishListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_wish_list, parent, false);
            return new RecycleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        if(!list.get(position).getDiamondImage().equalsIgnoreCase(""))
        {
            Picasso.with(context)
                    .load(list.get(position).getDiamondImage())
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

        holder.supplier_id_tv.setText(list.get(position).getStockNo() + " | " + "ID: " +  list.get(position).getSupplierId());
        holder.name_tv.setText(list.get(position).getShape());
        holder.item_type_tv.setText(list.get(position).getCarat() + " | " + list.get(position).getColor() + " | " + list.get(position).getClarity());

        /*holder.cut_grade_tv.setText(list.get(position).getCut_grade());
        holder.polish_tv.setText(list.get(position).getPolish());
        holder.symmetry_tv.setText(list.get(position).getSymmetry());
        holder.fluorescence_intensity_tv.setText(list.get(position).getFluorescence_intensity());
        holder.certificate_name_tv.setText(list.get(position).getCertificate_name());

        holder.table_perc_tv.setText("T: " + list.get(position).getTable_perc());
        holder.depth_perc.setText("D: " + list.get(position).getDepth_perc());
        holder.measurement_tv.setText("M: " + list.get(position).getMeasurement());*/

        if(!list.get(position).getSubtotal().equalsIgnoreCase(""))
        {
            holder.sub_total_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));
        }else {}

        if(list.get(position).getIsReturnable().equalsIgnoreCase("1"))
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

        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        holder.delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                recyclerInterface.itemClick(position,"deleteView");
            }
        });

        holder.add_to_cart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerInterface.itemClick(position,"addToCart");
            }
        });

        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerInterface.itemClick(position,"viewDetails");
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

        CardView root_layout, add_to_card_view;
        TextView supplier_id_tv, name_tv, item_type_tv,cut_grade_tv, certificate_name_tv, polish_tv, symmetry_tv, fluorescence_intensity_tv,table_perc_tv,
                depth_perc,measurement_tv, add_to_cart_tv, sub_total_tv,return_policy_tv, discount_tv;
        ImageView image_view, returnable_img, status_img, add_to_cart_img, delete_img;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            root_layout = itemView.findViewById(R.id.root_layout);
            image_view = itemView.findViewById(R.id.image_view);
            returnable_img = itemView.findViewById(R.id.returnable_img);
            status_img = itemView.findViewById(R.id.status_img);

            add_to_cart_img = itemView.findViewById(R.id.add_to_cart_img);
            delete_img = itemView.findViewById(R.id.delete_img);

            supplier_id_tv = itemView.findViewById(R.id.supplier_id_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            item_type_tv = itemView.findViewById(R.id.item_type_tv);

            /*cut_grade_tv = itemView.findViewById(R.id.cut_grade_tv);
            certificate_name_tv = itemView.findViewById(R.id.certificate_name_tv);
            polish_tv = itemView.findViewById(R.id.polish_tv);
            symmetry_tv = itemView.findViewById(R.id.symmetry_tv);
            fluorescence_intensity_tv = itemView.findViewById(R.id.fluorescence_intensity_tv);
            table_perc_tv = itemView.findViewById(R.id.table_perc_tv);
            depth_perc = itemView.findViewById(R.id.depth_perc);
            measurement_tv = itemView.findViewById(R.id.measurement_tv);
            discount_tv = itemView.findViewById(R.id.discount_tv);*/
            sub_total_tv = itemView.findViewById(R.id.sub_total_tv);
            return_policy_tv = itemView.findViewById(R.id.return_policy_tv);

        }
    }

}
