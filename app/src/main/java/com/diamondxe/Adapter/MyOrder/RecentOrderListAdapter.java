package com.diamondxe.Adapter.MyOrder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.CountryListModel;
import com.diamondxe.Beans.MyOrder.InnerOrderListModel;
import com.diamondxe.Beans.MyOrder.MyOrderListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class RecentOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<MyOrderListModel> list;
    Context context;
    int last;
    public ArrayList<MyOrderListModel> arraylist;
    TwoRecyclerInterface recyclerInterface;
    /*private static final long SiXTY_FIVE_MINUTES_IN_MILLIS = 65 * 60 * 1000L; // 65 minutes in milliseconds
    private static final long CHECK_INTERVAL = 1000L; // 1 second interval, // The 'L' suffix indicates that this is a long literal
    private Handler handler = new Handler();*/

    public RecentOrderListAdapter(ArrayList<MyOrderListModel> list, Context context, TwoRecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<MyOrderListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_recent_order_list_history, parent, false);
            return new RecycleViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        holder.order_number_tv.setText("#"+list.get(position).getOrderId());
        holder.date_time_tv.setText(list.get(position).getCreatedAt());


        if(list.get(position).getIsCancelable().equalsIgnoreCase("1"))
        {
            if(list.get(position).getCreatedAt()!=null && !list.get(position).getCreatedAt().equalsIgnoreCase(""))
            {
                // This Condition For 65 Min
                //CommonUtility.startChecking(list.get(position).getCompareDateTime(), holder.cancel_order_card_view);
                holder.timer_tv.setVisibility(View.VISIBLE);
                String dateTimeString = list.get(position).getCompareDateTime();
                CommonUtility.startTimerCheck(dateTimeString, holder.timer_tv, holder.cancel_order_card_view);

            }else{}
        }else{
            holder.cancel_order_card_view.setVisibility(View.GONE);
            holder.timer_tv.setVisibility(View.GONE);
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
        holder.track_order_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerInterface.itemClick(position , 0,"orderTrack");
            }
        });
        holder.cancel_order_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerInterface.itemClick(position , 0,"orderCancel");
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
        TextView date_time_tv, order_number_tv,timer_tv;
        CardView details_card_view, summary_card_view, track_order_card_view, cancel_order_card_view;
        RecyclerView inner_recycler_view;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            date_time_tv = itemView.findViewById(R.id.date_time_tv);
            order_number_tv = itemView.findViewById(R.id.order_number_tv);
            timer_tv = itemView.findViewById(R.id.timer_tv);

            details_card_view = itemView.findViewById(R.id.details_card_view);
            summary_card_view = itemView.findViewById(R.id.summary_card_view);
            track_order_card_view = itemView.findViewById(R.id.track_order_card_view);
            cancel_order_card_view = itemView.findViewById(R.id.cancel_order_card_view);

            inner_recycler_view = itemView.findViewById(R.id.inner_recycler_view);

        }
    }

    // Start the timer
    /*private void startChecking(String createAtTime, CardView cardView) {
        Runnable checkRunnable = new Runnable() {
            @Override
            public void run() {
                if (shouldHideButton(createAtTime))
                {
                    Log.e("-------Diamond------- : " , "Button Hide : " + createAtTime);
                    cardView.setVisibility(View.GONE);
                } else {
                    // Check again after CHECK_INTERVAL milliseconds
                    Log.e("-------Diamond------- : " , "Button Not Hide : " + createAtTime);
                    cardView.setVisibility(View.VISIBLE);
                    handler.postDelayed(this, CHECK_INTERVAL);
                }
            }
        };
        handler.post(checkRunnable);
    }
    private boolean shouldHideButton(String createAtTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date specifiedDate = dateFormat.parse(createAtTime);
            if (specifiedDate != null) {
                long specifiedTimeMillis = specifiedDate.getTime();
                long hideTimeMillis = specifiedTimeMillis + SiXTY_FIVE_MINUTES_IN_MILLIS;
                long currentTimeMillis = System.currentTimeMillis();
                Log.e("currentTimeMillis : ", "currentTimeMillis : "  + currentTimeMillis);
                return currentTimeMillis > hideTimeMillis;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }*/


    /*private boolean shouldHideButton(String createAtTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date specifiedDate = dateFormat.parse(createAtTime);
            if (specifiedDate != null) {
                long currentTimeMillis = System.currentTimeMillis();
                long specifiedTimeMillis = specifiedDate.getTime();
                return (currentTimeMillis - specifiedTimeMillis) > SiXTY_FIVE_MINUTES_IN_MILLIS;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }*/

}
