package com.diamondxe.Adapter.MyOrder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;


public class PastOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<MyOrderListModel> list;
    Context context;
    int last;
    public ArrayList<MyOrderListModel> arraylist;
    TwoRecyclerInterface recyclerInterface;

    public PastOrderListAdapter(ArrayList<MyOrderListModel> list, Context context, TwoRecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<MyOrderListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_past_order_list_history, parent, false);
            return new RecycleViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        holder.order_number_tv.setText("#"+list.get(position).getOrderId());
        holder.date_time_tv.setText(list.get(position).getCreatedAt());

        if(!list.get(position).getDeliveryDate().equalsIgnoreCase(""))
        {
            holder.delivery_date_tv.setText(context.getResources().getString(R.string.delivery_date_lbl) + " " +list.get(position).getDeliveryDate());
        }
        else{
            holder.delivery_date_tv.setText("");
        }

        if(list.get(position).getIsReturnable().equalsIgnoreCase("1"))
        {
            holder.returnable_view_layout.setVisibility(View.VISIBLE);

            if(!list.get(position).getReturnEligibleDate().equalsIgnoreCase(""))
            {
                holder.returnable_date_tv.setText(context.getResources().getString(R.string.eligible_for_return_till) + " " +list.get(position).getReturnEligibleDate());
            }
            else{
                holder.returnable_date_tv.setText("");
            }
        }
        else{
            holder.returnable_view_layout.setVisibility(View.GONE);
        }

        // Check Condition Order Status is Delivered and Is Returnable 1 Then Return Button Visible Other Wise Gone
        if(list.get(position).getOrderStatus().equalsIgnoreCase("Delivered")
                && list.get(position).getIsReturnable().equalsIgnoreCase("1"))
        {
           // holder.return_card_view.setVisibility(View.GONE);
            holder.return_card_view.setVisibility(View.VISIBLE);
        }
        else{
            holder.return_card_view.setVisibility(View.GONE);
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

        holder.return_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerInterface.itemClick(position , 0,"orderReturn");
            }
        });

        holder.view_policy_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerInterface.itemClick(position , 0,"orderViewPolicy");
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
        TextView date_time_tv, order_number_tv,delivery_date_tv, returnable_date_tv, view_policy_tv;
        CardView details_card_view, summary_card_view, track_order_card_view, cancel_order_card_view,return_card_view;
        RecyclerView inner_recycler_view;
        RelativeLayout returnable_view_layout;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            date_time_tv = itemView.findViewById(R.id.date_time_tv);
            order_number_tv = itemView.findViewById(R.id.order_number_tv);
            delivery_date_tv = itemView.findViewById(R.id.delivery_date_tv);
            returnable_date_tv = itemView.findViewById(R.id.returnable_date_tv);
            view_policy_tv = itemView.findViewById(R.id.view_policy_tv);

            details_card_view = itemView.findViewById(R.id.details_card_view);
            summary_card_view = itemView.findViewById(R.id.summary_card_view);
            track_order_card_view = itemView.findViewById(R.id.track_order_card_view);
            cancel_order_card_view = itemView.findViewById(R.id.cancel_order_card_view);
            return_card_view = itemView.findViewById(R.id.return_card_view);

            inner_recycler_view = itemView.findViewById(R.id.inner_recycler_view);
            returnable_view_layout = itemView.findViewById(R.id.returnable_view_layout);

        }
    }

}
