package com.diamondxe.Adapter.Dealer;

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
import com.diamondxe.Beans.Dealer.BankNEFTListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;

import java.util.ArrayList;


public class AllBankNameListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<BankNEFTListModel> list;
    Context context;
    int last;
    public ArrayList<BankNEFTListModel> arraylist;
    RecyclerInterface recyclerInterface;

    public AllBankNameListAdapter(ArrayList<BankNEFTListModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<BankNEFTListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_all_bank_name, parent, false);
            return new RecycleViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        holder.name_tv.setText(list.get(position).getBankName());

        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerInterface.itemClick(position,"selectAllBankName");
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
        TextView name_tv;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            root_layout = itemView.findViewById(R.id.root_layout);
            name_tv = itemView.findViewById(R.id.name_tv);
        }
    }

}
