package com.diamondxe.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.MediaSpotLightModel;
import com.diamondxe.Beans.TopDiamondImageModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;
import com.diamondxe.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MediaSpotLightListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MediaSpotLightModel> list;
    Context context;
    int last;
    public ArrayList<MediaSpotLightModel> arraylist;
    RecyclerInterface recyclerInterface;

    public MediaSpotLightListAdapter(ArrayList<MediaSpotLightModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<MediaSpotLightModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_media_spot_light_list, parent, false);
            return new RecycleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        if(list.get(position).isLocal())
        {
            Log.v("-----ImageSize-----" ,  ""+ list.get(position).getDrawable());
            Picasso.with(context)
                    .load(list.get(position).getDrawable())
                    .placeholder(list.get(position).getDrawable())
                    .error(list.get(position).getDrawable())
                    .into(holder.image_view);
        }
        else
        {
            if (!list.get(position).getImage().equalsIgnoreCase(""))
            {
                Picasso.with(context)
                        .load(list.get(position).getImage())
                        .placeholder(R.drawable.diamond_placeholder)
                        .error(R.drawable.diamond_placeholder)
                        .into(holder.image_view);
            }else {}
        }

        if(list.get(position).getDescription()!=null && list.get(position).getDescription()!="")
        {
            holder.description_tv.setVisibility(View.VISIBLE);
        }
        else{
            holder.description_tv.setVisibility(View.GONE);
        }
        holder.description_tv.setText(list.get(position).getDescription());

        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerInterface.itemClick(position,"mediaSpotLightDetails");
            }
        });

        /*holder.read_more_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerInterface.itemClick(position, "readMore");
            }
        });*/

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

        RelativeLayout root_layout;
        ImageView image_view;
        TextView top_diamond_tv;
        TextView  description_tv, read_more_tv;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            root_layout = itemView.findViewById(R.id.root_layout);
            image_view = itemView.findViewById(R.id.image_view);
            description_tv = itemView.findViewById(R.id.description_tv);
            read_more_tv = itemView.findViewById(R.id.read_more_tv);

        }
    }


}
