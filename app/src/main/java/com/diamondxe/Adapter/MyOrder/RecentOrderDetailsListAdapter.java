package com.diamondxe.Adapter.MyOrder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
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


public class RecentOrderDetailsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MyOrderListModel> list;
    Context context;
    int last;
    public ArrayList<MyOrderListModel> arraylist;
    TwoRecyclerInterface recyclerInterface;
    Handler handler = new Handler(Looper.getMainLooper());

    public RecentOrderDetailsListAdapter(ArrayList<MyOrderListModel> list, Context context, TwoRecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<MyOrderListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_order_recent_history_details, parent, false);
            return new RecycleViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        if(list.size()>1)
        {
            holder.view_line.setVisibility(View.VISIBLE);
        }else{
            holder.view_line.setVisibility(View.GONE);
        }

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

        holder.stock_no_tv.setText("#"+list.get(position).getStockNumber());
        holder.name_tv.setText(list.get(position).getName());
        holder.shape_tv.setText(list.get(position).getShape());
        holder.carat_tv.setText(list.get(position).getCarat());
        holder.color_tv.setText(list.get(position).getColor());
        holder.clarity_tv.setText(list.get(position).getClarity());
        holder.type_tv.setText(list.get(position).getType());

        if(list.get(position).getCut().equalsIgnoreCase(""))
        {
            holder.cut_tv.setText("-");
        }
        else{
            holder.cut_tv.setText(list.get(position).getCut());
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

        if(list.get(position).getFir().equalsIgnoreCase(""))
        {
            holder.flr_tv.setText("-");
        }else{
            holder.flr_tv.setText(list.get(position).getFir());
        }

        if(list.get(position).getLab().equalsIgnoreCase(""))
        {
            holder.lab_tv.setText("-");
        }else{
            holder.lab_tv.setText(list.get(position).getLab());
        }

        if(list.get(position).getTable().equalsIgnoreCase(""))
        {
            holder.table_perc_tv.setText("-");
        }else{
            holder.table_perc_tv.setText(list.get(position).getTable());
        }

        if(list.get(position).getDepth().equalsIgnoreCase(""))
        {
            holder.depth_perc_tv.setText("-");
        }
        else{
            holder.depth_perc_tv.setText(list.get(position).getDepth());
        }

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

        if(list.get(position).getIsReturnable().equalsIgnoreCase("1"))
        {
            holder.returnable_img.setVisibility(View.VISIBLE);
        }
        else{
            holder.returnable_img.setVisibility(View.GONE);
        }

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
        TextView name_tv, stock_no_tv, return_policy_tv, diamond_type_tv,
                shape_tv, carat_tv, color_tv, clarity_tv, type_tv,cut_tv, polish_tv, symmetry_tv, flr_tv, lab_tv, table_perc_tv, depth_perc_tv,
                sub_total_tv;
        ImageView image_view, returnable_img;
        View view_line;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            name_tv = itemView.findViewById(R.id.name_tv);
            stock_no_tv = itemView.findViewById(R.id.stock_no_tv);
            return_policy_tv = itemView.findViewById(R.id.return_policy_tv);
            sub_total_tv = itemView.findViewById(R.id.sub_total_tv);
            diamond_type_tv = itemView.findViewById(R.id.diamond_type_tv);
            shape_tv = itemView.findViewById(R.id.shape_tv);
            carat_tv = itemView.findViewById(R.id.carat_tv);
            color_tv = itemView.findViewById(R.id.color_tv);
            clarity_tv = itemView.findViewById(R.id.clarity_tv);
            type_tv = itemView.findViewById(R.id.type_tv);
            cut_tv = itemView.findViewById(R.id.cut_tv);
            polish_tv = itemView.findViewById(R.id.polish_tv);
            symmetry_tv = itemView.findViewById(R.id.symmetry_tv);
            flr_tv = itemView.findViewById(R.id.flr_tv);
            lab_tv = itemView.findViewById(R.id.lab_tv);
            table_perc_tv = itemView.findViewById(R.id.table_perc_tv);
            depth_perc_tv = itemView.findViewById(R.id.depth_perc_tv);

            image_view = itemView.findViewById(R.id.image_view);
            view_line = itemView.findViewById(R.id.view_line);
            returnable_img = itemView.findViewById(R.id.returnable_img);


        }
    }

}
