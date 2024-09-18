package com.diamondxe.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.CustomerStoriesModel;
import com.diamondxe.Beans.TopDiamondImageModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CustomerStoriesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<CustomerStoriesModel> list;
    Context context;
    int last;
    public ArrayList<CustomerStoriesModel> arraylist;
    RecyclerInterface recyclerInterface;

    public CustomerStoriesListAdapter(ArrayList<CustomerStoriesModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<CustomerStoriesModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_customer_stories_list, parent, false);
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
                    .resize(500, 500)
                    .centerCrop()
                    .into(holder.customer_profile_img);
        }
        else
        {
            if (!list.get(position).getImage().equalsIgnoreCase(""))
            {
                Picasso.with(context)
                        .load(list.get(position).getImage())
                        .placeholder(list.get(position).getDrawable())
                        .error(list.get(position).getDrawable())
                        .into(holder.customer_profile_img);
            }else {}
        }

        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerInterface.itemClick(position, "CustomerStoriesDetails");
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

        RelativeLayout root_layout;
        ImageView customer_profile_img;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            root_layout = itemView.findViewById(R.id.root_layout);
            customer_profile_img = itemView.findViewById(R.id.customer_profile_img);

        }
    }

}
