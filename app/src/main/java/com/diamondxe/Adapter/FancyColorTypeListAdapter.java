package com.diamondxe.Adapter;

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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.AttributeDetailsModel;
import com.diamondxe.Beans.ColorTypeModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;

import java.util.ArrayList;


public class FancyColorTypeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<AttributeDetailsModel> list;
    Context context;
    int last;
    public ArrayList<AttributeDetailsModel> arraylist;
    RecyclerInterface recyclerInterface;

    public FancyColorTypeListAdapter(ArrayList<AttributeDetailsModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<AttributeDetailsModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_fancy_color_type_list, parent, false);
            return new RecycleViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        holder.color_type_tv.setText(list.get(position).getDisplayAttr());

        if(list.get(position).isSelected())
        {
             holder.color_type_tv.setBackgroundResource(R.drawable.background_gradient);
             holder.color_type_tv.setTextColor(ContextCompat.getColor(context, R.color.white));

            holder.cardView.setElevation(37f);
            holder.cardView.setCardElevation(18f);
            holder.cardView.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.black_50));
            holder.cardView.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.black_50));
        }
        else
        {
            holder.color_type_tv.setBackgroundResource(R.drawable.background_white);
            holder.color_type_tv.setTextColor(ContextCompat.getColor(context, R.color.black));

            holder.cardView.setElevation(0f);
            holder.cardView.setCardElevation(0f);
            holder.cardView.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.white));
            holder.cardView.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.white));
        }


        if(list.get(position).isFirstPosition())
        {
            //Log.e("position : ", ""+list.get(position).isFirstPosition());
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.root_layout.getLayoutParams();
            params.setMargins(60,0, 12, 0);
            holder.root_layout.setLayoutParams(params);
        }else{}

        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerInterface.itemClick(position,"fancyColorType");
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
        CardView cardView;
        TextView color_type_tv;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            root_layout = itemView.findViewById(R.id.root_layout);
            color_type_tv = itemView.findViewById(R.id.color_type_tv);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }


}
