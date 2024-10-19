package com.diamondxe.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.Beans.TopDiamondImageModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class NaturalDiamondListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<TopDiamondImageModel> list;
    Context context;
    int last;
    public ArrayList<TopDiamondImageModel> arraylist;
    RecyclerInterface recyclerInterface;

    public NaturalDiamondListAdapter(ArrayList<TopDiamondImageModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<TopDiamondImageModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_natural_diamond_list, parent, false);
            return new RecycleViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        if (!list.get(position).getImage().equalsIgnoreCase(""))
        {
            Picasso.with(context)
                    .load(list.get(position).getImage())
                    .placeholder(R.mipmap.phl_diamond)
                    .error(R.mipmap.phl_diamond)
                    .into(holder.diamond_img);

        }else {}

        Log.e("getCoupondiscountperc..","...65........."+list.get(position).getCoupondiscountperc());
        if (list.get(position).getCoupondiscountperc() > 0) {

            String getsubtotalPrice= String.valueOf(list.get(position).getSubtotalaftercoupondiscount());
            holder.price_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(getsubtotalPrice));
            holder.original_price_tv.setPaintFlags(holder.original_price_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.original_price_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));

        }
        else
        {
            if(!list.get(position).getShowingSubTotal().equalsIgnoreCase(""))
            {
                // this work for cut price
                //holder.price_tv.setPaintFlags(holder.price_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.original_price_tv.setVisibility(View.GONE);
                holder.price_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));
            }
        }




        holder.name_tv.setText(list.get(position).getName());

        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerInterface.itemClick(position,"naturalDiamondDetails");
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
        ImageView diamond_img;
        TextView top_diamond_tv;
        TextView price_tv, name_tv,original_price_tv;

        public RecycleViewHolder (View itemView) {
            super (itemView);
            original_price_tv=itemView.findViewById(R.id.original_price_tv);
            root_layout = itemView.findViewById(R.id.root_layout);
            diamond_img = itemView.findViewById(R.id.diamond_img);
            price_tv = itemView.findViewById(R.id.price_tv);
            name_tv = itemView.findViewById(R.id.name_tv);

        }
    }


}
