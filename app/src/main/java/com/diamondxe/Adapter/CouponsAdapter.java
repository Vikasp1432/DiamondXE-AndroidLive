package com.diamondxe.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.diamondxe.Beans.AttributeDetailsModel;
import com.diamondxe.Beans.CouponsListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CouponsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    RecyclerInterface recyclerInterface;
    ArrayList<CouponsListModel> list;
    public CouponsAdapter(ArrayList<CouponsListModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.allcoupone_layout, parent, false);
        return new CouponsAdapter.RecycleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        holder.couponCode.setText(list.get(position).getCode());
        holder.type_tv.setText(list.get(position).getItemType());
        holder.discount.setText(list.get(position).getValue() +"% OFF COUPON");
        holder.validdate.setText("Valid Till "+convertDate(list.get(position).getEndDate()));
       // holder.termsandcondition.setText(list.get(position).getCode());

        if(list.get(position).getCategory().equalsIgnoreCase("Natural"))
        {
            holder.typeboth_tv.setVisibility(View.GONE);
            holder.type_tv.setBackgroundResource(R.drawable.background_yellow);
            holder.type_tv.setText(list.get(position).getCategory());
        }
        else if(list.get(position).getCategory().equalsIgnoreCase("Lab Grown"))
        {
            holder.typeboth_tv.setVisibility(View.GONE);
            holder.type_tv.setBackgroundResource(R.drawable.background_green_light_small_round_cornor);
            holder.type_tv.setText(list.get(position).getCategory());
        }
        else{
            String input = list.get(position).getCategory();
            if (input.contains("/")) {
                String[] parts = input.split("/");
                Log.e("0 index","..."+parts[0]);
                Log.e("1 index","..."+parts[1]);
                holder.typeboth_tv.setVisibility(View.VISIBLE);
                if(parts[0].equalsIgnoreCase("Natural"))
                {
                    holder.type_tv.setBackgroundResource(R.drawable.background_yellow);
                    holder.type_tv.setText(parts[0]);
                }
                if (parts[1].equalsIgnoreCase("Lab Grown"))
                {
                    holder.typeboth_tv.setBackgroundResource(R.drawable.background_green_light_small_round_cornor);
                    holder.typeboth_tv.setText(parts[1]);
                }
            }


           /* holder.type_tv.setBackgroundResource(R.drawable.background_green_light_small_round_cornor);
            holder.type_tv.setText(list.get(position).getCategory());*/
        }

        holder.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerInterface.itemClick(position,"AllCoupon");
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleViewHolder extends RecyclerView.ViewHolder {

        CardView diamondcard;
        TextView couponCode,type_tv,discount,validdate,termsandcondition,apply,typeboth_tv;
        public RecycleViewHolder (View itemView) {
            super (itemView);
            //diamondcard=itemView.findViewById(R.id.diamondcard);
            couponCode = itemView.findViewById(R.id.couponCode);
            type_tv = itemView.findViewById(R.id.type_tv);
            discount = itemView.findViewById(R.id.discount);
            apply=itemView.findViewById(R.id.apply);
            typeboth_tv=itemView.findViewById(R.id.typeboth_tv);
            validdate = itemView.findViewById(R.id.validdate);
            termsandcondition = itemView.findViewById(R.id.termsandcondition);

        }
    }

    public static String convertDate(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());

        String formattedDate = "";
        try {
            Date date = inputFormat.parse(inputDate);
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

}
