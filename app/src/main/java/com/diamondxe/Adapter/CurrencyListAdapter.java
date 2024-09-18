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
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.CountryListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class CurrencyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<CountryListModel> list;
    Context context;
    int last;
    public ArrayList<CountryListModel> arraylist;
    RecyclerInterface recyclerInterface;

    public CurrencyListAdapter(ArrayList<CountryListModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<CountryListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_country_list, parent, false);
            return new RecycleViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        holder.currency_name_tv.setText(list.get(position).getCurrency());
        holder.description_tv.setText(list.get(position).getDesc());

        if (!list.get(position).getImage().equalsIgnoreCase(""))
        {
            Picasso.with(context)
                    .load(list.get(position).getImage())
                    .into(holder.country_img);
        }
        else {}

        /*if(list.get(position).isSelected())
        {
             holder.type_tv.setBackgroundResource(R.drawable.background_gradient);
             holder.type_tv.setTextColor(ContextCompat.getColor(context, R.color.white));

            holder.cardView.setElevation(37f);
            holder.cardView.setCardElevation(18f);
            holder.cardView.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.black_50));
            holder.cardView.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.black_50));
        }
        else
        {
            holder.type_tv.setBackgroundResource(R.drawable.background_white);
            holder.type_tv.setTextColor(ContextCompat.getColor(context, R.color.purple));

            holder.cardView.setElevation(0f);
            holder.cardView.setCardElevation(0f);
            holder.cardView.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.white));
            holder.cardView.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.white));
        }*/

        /*if(list.get(position).isFirstPosition())
        {
            //Log.e("position : ", ""+list.get(position).isFirstPosition());
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.root_layout.getLayoutParams();
            params.setMargins(60,0, 12, 0);
            holder.root_layout.setLayoutParams(params);
        }else{}*/

        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerInterface.itemClick(position,"countryType");
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
        CircleImageView country_img;
        TextView currency_name_tv, description_tv;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            root_layout = itemView.findViewById(R.id.root_layout);
            currency_name_tv = itemView.findViewById(R.id.currency_name_tv);
            description_tv = itemView.findViewById(R.id.description_tv);
            country_img = itemView.findViewById(R.id.country_img);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }


}
