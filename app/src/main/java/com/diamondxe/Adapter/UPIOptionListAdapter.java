package com.diamondxe.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.AttributeDetailsModel;
import com.diamondxe.Beans.UPIAppInfoListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UPIOptionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<UPIAppInfoListModel> list;
    Context context;
    int last;
    public ArrayList<UPIAppInfoListModel> arraylist;
    RecyclerInterface recyclerInterface;

    public UPIOptionListAdapter(ArrayList<UPIAppInfoListModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<UPIAppInfoListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_upi_option_name, parent, false);
            return new RecycleViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        holder.img.setImageDrawable(list.get(position).getAppIcon());
        holder.upi_app_name_tv.setText(list.get(position).getAppName());

        if(list.get(position).isSelected())
        {
            holder.img_rel.setBackgroundResource(R.drawable.background_select_payment_option);
            //holder.card_layout.setElevation(15f);
        }
        else {
            holder.img_rel.setBackgroundResource(R.drawable.background_payment_option);
        }

        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerInterface.itemClick(position,"selectUPIOption");
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
        ImageView img;
        RelativeLayout img_rel;
        TextView upi_app_name_tv;
        public RecycleViewHolder (View itemView) {
            super (itemView);

            root_layout = itemView.findViewById(R.id.root_layout);
            img = itemView.findViewById(R.id.img);
            img_rel = itemView.findViewById(R.id.img_rel);
            upi_app_name_tv = itemView.findViewById(R.id.upi_app_name_tv);

        }
    }


}
