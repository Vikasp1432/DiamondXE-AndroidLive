package com.diamondxe.Adapter.MyOrder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.MyOrder.InnerOrderListModel;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class InnerCancelOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<InnerOrderListModel> list;
    Context context;
    public static ArrayList<InnerOrderListModel> arraylist;
    TwoRecyclerInterface recyclerInterface;
    int mainItemPosition = 0;

    public InnerCancelOrderListAdapter(ArrayList<InnerOrderListModel> list, Context context, TwoRecyclerInterface recyclerInterface, int mainItemPosition) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<InnerOrderListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_inner_order_list_history, parent, false);
            return new RecycleViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        if(!list.get(position).getImage().equalsIgnoreCase(""))
        {
            Picasso.with(context)
                    .load(list.get(position).getImage())
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

        holder.order_number_tv.setText("#"+list.get(position).getOrderNumber());
        holder.stock_no_tv.setText("#"+list.get(position).getStockNumber());
        holder.name_tv.setText(list.get(position).getName());
        holder.item_type_tv.setText(list.get(position).getCarat() + context.getResources().getString(R.string.ct) + " | " + list.get(position).getColor() + " | " + list.get(position).getClarity());

        DecimalFormat formatter = new DecimalFormat("#,###,###");

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
        TextView date_time_tv, name_tv, status, item_type_tv, stock_no_tv, return_policy_tv, sub_total_tv, diamond_type_tv, order_number_tv;
        ImageView image_view;
        CardView details_card_view, summary_card_view, track_order_card_view, cancel_order_card_view;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            date_time_tv = itemView.findViewById(R.id.date_time_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            status = itemView.findViewById(R.id.status);
            item_type_tv = itemView.findViewById(R.id.item_type_tv);
            stock_no_tv = itemView.findViewById(R.id.stock_no_tv);
            return_policy_tv = itemView.findViewById(R.id.return_policy_tv);
            sub_total_tv = itemView.findViewById(R.id.sub_total_tv);
            diamond_type_tv = itemView.findViewById(R.id.diamond_type_tv);
            order_number_tv = itemView.findViewById(R.id.order_number_tv);

            image_view = itemView.findViewById(R.id.image_view);
            details_card_view = itemView.findViewById(R.id.details_card_view);
            summary_card_view = itemView.findViewById(R.id.summary_card_view);
            track_order_card_view = itemView.findViewById(R.id.track_order_card_view);
            cancel_order_card_view = itemView.findViewById(R.id.cancel_order_card_view);

        }
    }

}
