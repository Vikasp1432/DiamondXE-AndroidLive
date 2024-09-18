package com.diamondxe.Adapter.Dealer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.Dealer.DealerMarkupListModel;
import com.diamondxe.Beans.Dealer.WalletListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;

import java.util.ArrayList;


public class WalletAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<WalletListModel> list;
    Context context;
    int last;
    public ArrayList<WalletListModel> arraylist;
    RecyclerInterface recyclerInterface;

    Handler handler = new Handler(Looper.getMainLooper());


    public WalletAdapter(ArrayList<WalletListModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<WalletListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_wallet_transaction, parent, false);
            return new RecycleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        if(!list.get(position).getOrderID().equalsIgnoreCase(""))
        {
            holder.order_id_tv.setText(list.get(position).getOrderID());
        }
        else{
            holder.order_id_tv.setText("-");
        }

        if(!list.get(position).getStatus().equalsIgnoreCase(""))
        {
            holder.status_tv.setText(list.get(position).getStatus());
        }
        else{
            holder.status_tv.setText("-");
        }

        if(!list.get(position).getPoints().equalsIgnoreCase(""))
        {
            holder.points_tv.setText(list.get(position).getPoints());
        }
        else{
            holder.points_tv.setText("-");
        }

        if(!list.get(position).getRemark().equalsIgnoreCase(""))
        {
            holder.remark_tv.setText(list.get(position).getRemark());
        }
        else{
            holder.remark_tv.setText("-");
        }

        if(!list.get(position).getDateTime().equalsIgnoreCase(""))
        {
            holder.date_time_tv.setText(list.get(position).getDateTime());
        }
        else{
            holder.date_time_tv.setText("-");
        }

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
        TextView order_id_tv, status_tv, points_tv, remark_tv, date_time_tv;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            order_id_tv = itemView.findViewById(R.id.order_id_tv);
            status_tv = itemView.findViewById(R.id.status_tv);
            points_tv = itemView.findViewById(R.id.points_tv);
            remark_tv = itemView.findViewById(R.id.remark_tv);
            date_time_tv = itemView.findViewById(R.id.date_time_tv);
        }
    }
}
