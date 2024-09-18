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

import com.dd.ShadowLayout;
import com.diamondxe.Beans.GiftImageModel;
import com.diamondxe.Beans.TopImageOptionModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class TopImageOptionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<TopImageOptionModel> list;
    Context context;
    int last;
    public ArrayList<TopImageOptionModel> arraylist;
    RecyclerInterface recyclerInterface;
    private int width=0;

    public TopImageOptionListAdapter(ArrayList<TopImageOptionModel> list, Context context, RecyclerInterface recyclerInterface,int width) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<TopImageOptionModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.width = width;
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_top_option_list, parent, false);
            return new RecycleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        if (list.size()>1)
        {
            //For set width of Card or Image.
            ViewGroup.LayoutParams layoutParams = holder.root_layout.getLayoutParams();
            layoutParams.width = width;
            holder.root_layout.setLayoutParams(layoutParams);
        } else {
        }

        Picasso.with(context)
                .load(list.get(position).getDrawable())
                .placeholder(list.get(position).getDrawable())
                .error(list.get(position).getDrawable())
                .into(holder.imageView);

        holder.textView.setText(list.get(position).getName());


        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerInterface.itemClick(position,"cardDetails");
            }
        });

        if(list.get(position).isFirstPosition())
        {
            Log.e("position : ", ""+list.get(position).isFirstPosition());
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.root_layout.getLayoutParams();
            params.setMargins(50,0, 15, 0);
            holder.root_layout.setLayoutParams(params);
        }
        else  if(list.get(position).isLastPosition())
        {
            Log.e("position : ", ""+list.get(position).isLastPosition());
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.root_layout.getLayoutParams();
            params.setMargins(15,0, 50, 0);
            holder.root_layout.setLayoutParams(params);
        }
        else{}

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
        CardView card_view;
        ShadowLayout shadow_layout;
        ImageView imageView;
        TextView textView;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            root_layout = itemView.findViewById(R.id.root_layout);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            shadow_layout = itemView.findViewById(R.id.shadow_layout);
            card_view = itemView.findViewById(R.id.card_view);

        }
    }


}
