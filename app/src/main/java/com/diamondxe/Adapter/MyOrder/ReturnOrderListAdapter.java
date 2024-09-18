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

import com.diamondxe.Beans.MyOrder.MyOrderListModel;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class ReturnOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<MyOrderListModel> list;
    Context context;
    int last;
    public ArrayList<MyOrderListModel> arraylist;
    TwoRecyclerInterface recyclerInterface;

    public ReturnOrderListAdapter(ArrayList<MyOrderListModel> list, Context context, TwoRecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<MyOrderListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_return_order_list_history, parent, false);
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

        holder.details_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerInterface.itemClick(position , 0,"orderDetails");
            }
        });
        holder.summary_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerInterface.itemClick(position , 0,"orderSummary");
            }
        });

        /*ArrayList<InnerOrderListModel> singleSectionItems = list.get(position).getAllItemsInSection();

        InnerOrderListAdapter itemListDataAdapter = new InnerOrderListAdapter(singleSectionItems, context, recyclerInterface, position);

        holder.inner_recycler_view.setHasFixedSize(true);
        holder.inner_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.inner_recycler_view.setAdapter(itemListDataAdapter);
        holder.inner_recycler_view.setNestedScrollingEnabled(false);*/
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
        CardView details_card_view, summary_card_view;
        RecyclerView inner_recycler_view;

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

            inner_recycler_view = itemView.findViewById(R.id.inner_recycler_view);

        }
    }

}
