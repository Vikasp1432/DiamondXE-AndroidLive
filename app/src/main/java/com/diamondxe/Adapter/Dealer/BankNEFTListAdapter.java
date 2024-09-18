package com.diamondxe.Adapter.Dealer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.Dealer.BankNEFTListModel;
import com.diamondxe.Beans.Dealer.WalletListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class BankNEFTListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<BankNEFTListModel> list;
    Context context;
    int last;
    public ArrayList<BankNEFTListModel> arraylist;
    RecyclerInterface recyclerInterface;

    Handler handler = new Handler(Looper.getMainLooper());

    public BankNEFTListAdapter(ArrayList<BankNEFTListModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<BankNEFTListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_bank_neft_name, parent, false);
            return new RecycleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;


        if(list.get(position).isSelected())
        {
            holder.img_rel.setBackgroundResource(R.drawable.background_select_payment_option);
            //holder.card_layout.setElevation(15f);
        }
        else {
            holder.img_rel.setBackgroundResource(R.drawable.background_payment_option);
        }

            Picasso.with(context)
                .load(list.get(position).getImage())
                .into(holder.img);

        holder.bank_name_tv.setText(list.get(position).getBank().toUpperCase());

        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerInterface.itemClick(position,"selectBank");
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
        TextView bank_name_tv;
        RelativeLayout img_rel;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            root_layout = itemView.findViewById(R.id.root_layout);
            img = itemView.findViewById(R.id.img);
            img_rel = itemView.findViewById(R.id.img_rel);
            bank_name_tv = itemView.findViewById(R.id.bank_name_tv);
        }
    }
}
