package com.diamondxe.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.Beans.GiftImageModel;
import com.diamondxe.Beans.HomeListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;
import com.diamondxe.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class GiftImageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<GiftImageModel> list;
    Context context;
    int last;
    public ArrayList<GiftImageModel> arraylist;
    RecyclerInterface recyclerInterface;

    public GiftImageListAdapter(ArrayList<GiftImageModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<GiftImageModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_home_list, parent, false);
            return new RecycleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        if(list.get(position).isLocal())
        {
            Picasso.with(context)
                    .load(list.get(position).getDrawable())
                    .placeholder(list.get(position).getDrawable())
                    .error(list.get(position).getDrawable())
                    .into(holder.gift_img);
        }
        else
        {
            if (!list.get(position).getImage().equalsIgnoreCase(""))
            {
                Picasso.with(context)
                        .load(list.get(position).getImage())
                        .placeholder(list.get(position).getDrawable())
                        .error(list.get(position).getDrawable())
                        .into(holder.gift_img);
            }else {}
        }


        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerInterface.itemClick(position,"giftDetails");
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
        ImageView gift_img;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            root_layout = itemView.findViewById(R.id.root_layout);
            gift_img = itemView.findViewById(R.id.gift_img);

        }
    }


}
