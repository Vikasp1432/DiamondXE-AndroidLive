package com.diamondxe.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.GiftImageModel;
import com.diamondxe.Beans.ShapeImageModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;
import com.diamondxe.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ShapeImageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ShapeImageModel> list;
    Context context;
    int last;
    public ArrayList<ShapeImageModel> arraylist;
    RecyclerInterface recyclerInterface;

    public ShapeImageListAdapter(ArrayList<ShapeImageModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<ShapeImageModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_shape_image_list, parent, false);
            return new RecycleViewHolder(v);
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        if(list.get(position).isSelected())
        {
            holder.all_img.setBackgroundResource(R.drawable.background_white_with_border);
            //holder.card_layout.setElevation(15f);
        }
        else
        {
            holder.all_img.setBackgroundResource(0);
            //holder.card_layout.setElevation(0f);
        }

        Picasso.with(context)
                .load(list.get(position).getDrawable())
                .placeholder(list.get(position).getDrawable())
                .error(list.get(position).getDrawable())
                .into(holder.all_img);

        if(list.get(position).isFirstPosition())
        {
            //Log.e("position : ", ""+list.get(position).isFirstPosition());
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.root_layout_rel.getLayoutParams();
            params.setMargins(60,0, 7, 0);
            holder.root_layout_rel.setLayoutParams(params);
        }else{}

        holder.all_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerInterface.itemClick(position,"shapeImage");
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

        RelativeLayout all_img_rel, root_layout_rel, card_layout;
        ImageView all_img;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            //card_layout = itemView.findViewById(R.id.card_layout);
          //  all_img_rel = itemView.findViewById(R.id.all_img_rel);
            root_layout_rel = itemView.findViewById(R.id.root_layout_rel);
            all_img = itemView.findViewById(R.id.all_img);

        }
    }

}
