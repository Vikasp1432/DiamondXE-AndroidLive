package com.diamondxe.Adapter.MyOrder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.MyOrder.MyOrderListModel;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ReturnItemOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MyOrderListModel> list;
    Context context;
    int last;
    public ArrayList<MyOrderListModel> arraylist;
    TwoRecyclerInterface recyclerInterface;
    String type = "";

    public ReturnItemOrderListAdapter(ArrayList<MyOrderListModel> list, Context context, TwoRecyclerInterface recyclerInterface, String type) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<MyOrderListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_return_item_order_list, parent, false);
            return new RecycleViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        // Order Cancel Type Full Order Cancel CheckBox Not Show.
        if(type.equalsIgnoreCase("fullOrderReturn"))
        {
            holder.order_item_click.setVisibility(View.GONE);
        }
        else{
            holder.order_item_click.setVisibility(View.VISIBLE);
        }

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

        holder.stock_no_tv.setText("#"+list.get(position).getCertificateNo());
        holder.name_tv.setText(list.get(position).getShape());
        holder.status.setText(list.get(position).getStatus());
        holder.item_type_tv.setText(list.get(position).getCarat() + context.getResources().getString(R.string.ct) + " | " + list.get(position).getColor() + " | " + list.get(position).getClarity());

        if(!list.get(position).getSubTotal().equalsIgnoreCase(""))
        {
            holder.sub_total_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));
        }else {}

        if(list.get(position).getCategory().equalsIgnoreCase("Natural"))
        {
            holder.diamond_type_tv.setBackgroundResource(R.drawable.background_yellow);
            holder.diamond_type_tv.setText("NATURAL");
        }
        else{
            holder.diamond_type_tv.setBackgroundResource(R.drawable.background_green_light_small_round_cornor);
            holder.diamond_type_tv.setText("LAB");
        }

        holder.order_item_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Toggle the checkbox state in the model
               // list.get(position).setChecked(!list.get(position).isChecked());
                // Notify the adapter about the change
               // notifyItemChanged(position);
                // Pass the event to the interface
                recyclerInterface.itemClick(position , 0,"orderReturnPartialCheck");

            }
        });

        if(list.get(position).isChecked())
        {
            holder.order_item_click.setChecked(true);
        }
        else{
            holder.order_item_click.setChecked(false);
        }
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
        TextView  name_tv, status, item_type_tv, stock_no_tv, return_policy_tv, sub_total_tv, diamond_type_tv;
        ImageView image_view;
        CheckBox order_item_click;
        public RecycleViewHolder (View itemView) {
            super (itemView);

            name_tv = itemView.findViewById(R.id.name_tv);
            status = itemView.findViewById(R.id.status);
            item_type_tv = itemView.findViewById(R.id.item_type_tv);
            stock_no_tv = itemView.findViewById(R.id.stock_no_tv);
            return_policy_tv = itemView.findViewById(R.id.return_policy_tv);
            sub_total_tv = itemView.findViewById(R.id.sub_total_tv);
            diamond_type_tv = itemView.findViewById(R.id.diamond_type_tv);
            image_view = itemView.findViewById(R.id.image_view);

            order_item_click = itemView.findViewById(R.id.order_item_click);

        }
    }

}
