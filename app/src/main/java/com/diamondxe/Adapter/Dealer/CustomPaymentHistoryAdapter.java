package com.diamondxe.Adapter.Dealer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.Dealer.CustomPaymentHistoryModel;
import com.diamondxe.Beans.Dealer.WalletListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;

import java.util.ArrayList;


public class CustomPaymentHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<CustomPaymentHistoryModel> list;
    Context context;
    int last;
    public ArrayList<CustomPaymentHistoryModel> arraylist;
    RecyclerInterface recyclerInterface;

    Handler handler = new Handler(Looper.getMainLooper());


    public CustomPaymentHistoryAdapter(ArrayList<CustomPaymentHistoryModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<CustomPaymentHistoryModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_custom_payment_history, parent, false);
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

        if(!list.get(position).getAmount().equalsIgnoreCase(""))
        {
            holder.amount_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(list.get(position).getAmount()));
        }
        else{
            holder.amount_tv.setText("-");
        }

        if(!list.get(position).getMode().equalsIgnoreCase(""))
        {
            holder.payment_mode_tv.setText(list.get(position).getMode());
        }
        else{
            holder.payment_mode_tv.setText("-");
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
        TextView order_id_tv, status_tv, amount_tv, remark_tv, date_time_tv, payment_mode_tv;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            order_id_tv = itemView.findViewById(R.id.order_id_tv);
            status_tv = itemView.findViewById(R.id.status_tv);
            amount_tv = itemView.findViewById(R.id.amount_tv);
            remark_tv = itemView.findViewById(R.id.remark_tv);
            date_time_tv = itemView.findViewById(R.id.date_time_tv);
            payment_mode_tv = itemView.findViewById(R.id.payment_mode_tv);
        }
    }
}
