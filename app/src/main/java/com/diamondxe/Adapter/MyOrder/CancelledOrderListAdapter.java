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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.MyOrder.InnerOrderListModel;
import com.diamondxe.Beans.MyOrder.MyOrderListModel;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class CancelledOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<MyOrderListModel> list;
    Context context;
    int last;
    public ArrayList<MyOrderListModel> arraylist;
    TwoRecyclerInterface recyclerInterface;

    public CancelledOrderListAdapter(ArrayList<MyOrderListModel> list, Context context, TwoRecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<MyOrderListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_cancelled_order_list_history, parent, false);
            return new RecycleViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        holder.order_number_tv.setText("#"+list.get(position).getOrderId());
        holder.date_time_tv.setText(list.get(position).getCreatedAt());

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

        ArrayList<InnerOrderListModel> singleSectionItems = list.get(position).getAllItemsInSection();

        InnerOrderListAdapter itemListDataAdapter = new InnerOrderListAdapter(singleSectionItems, context, recyclerInterface, position);

        holder.inner_recycler_view.setHasFixedSize(true);
        holder.inner_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.inner_recycler_view.setAdapter(itemListDataAdapter);
        holder.inner_recycler_view.setNestedScrollingEnabled(false);
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
        TextView date_time_tv, order_number_tv;
        CardView details_card_view, summary_card_view, track_order_card_view, cancel_order_card_view;
        RecyclerView inner_recycler_view;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            date_time_tv = itemView.findViewById(R.id.date_time_tv);
            order_number_tv = itemView.findViewById(R.id.order_number_tv);

            details_card_view = itemView.findViewById(R.id.details_card_view);
            summary_card_view = itemView.findViewById(R.id.summary_card_view);
            track_order_card_view = itemView.findViewById(R.id.track_order_card_view);
            cancel_order_card_view = itemView.findViewById(R.id.cancel_order_card_view);

            inner_recycler_view = itemView.findViewById(R.id.inner_recycler_view);

        }
    }

}
