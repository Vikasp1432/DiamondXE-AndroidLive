package com.diamondxe.Adapter.MyOrder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.CountryListModel;
import com.diamondxe.Beans.MyOrder.CancelOrderReasonListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;


public class CancelReasonOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<CancelOrderReasonListModel> list;
    Context context;
    int last;
    public ArrayList<CancelOrderReasonListModel> arraylist;
    TwoRecyclerInterface recyclerInterface;


    public CancelReasonOrderListAdapter(ArrayList<CancelOrderReasonListModel> list, Context context, TwoRecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<CancelOrderReasonListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_cancel_order_reason_list, parent, false);
            return new RecycleViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        holder.name_tv.setText(list.get(position).getReason());

        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerInterface.itemClick(position,0, "reason");
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

        RelativeLayout root_layout, cardView;
        ImageView country_img;
        TextView name_tv;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            root_layout = itemView.findViewById(R.id.root_layout);
            name_tv = itemView.findViewById(R.id.name_tv);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }

}
