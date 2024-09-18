package com.diamondxe.Adapter.Dealer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.Dealer.DealerMarkupListModel;
import com.diamondxe.Beans.KYCVerificationModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;

import java.util.ArrayList;


public class DealerMarkupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<DealerMarkupListModel> list;
    Context context;
    int last;
    public ArrayList<DealerMarkupListModel> arraylist;
    RecyclerInterface recyclerInterface;

    Handler handler = new Handler(Looper.getMainLooper());


    public DealerMarkupAdapter(ArrayList<DealerMarkupListModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<DealerMarkupListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_dealer_markup, parent, false);
            return new RecycleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        holder.debit_value.setText(list.get(position).getFromValue() + "-" + list.get(position).getToValue());

        if(!list.get(position).getNaturalValue().equalsIgnoreCase(""))
        {
            holder.natural_et.setText(list.get(position).getNaturalValue());
        }
        else{
            holder.natural_et.setHint(context.getResources().getString(R.string.enter_value));
        }

        if(!list.get(position).getLabGrownValue().equalsIgnoreCase(""))
        {
            holder.lab_grown_et.setText(list.get(position).getLabGrownValue());
        }
        else{
            holder.lab_grown_et.setHint(context.getResources().getString(R.string.enter_value));
        }

        holder.natural_et.setSelection(holder.natural_et.getText().length());
        holder.lab_grown_et.setSelection(holder.lab_grown_et.getText().length());

        // Update item in the list when text changes
        holder.natural_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                list.get(position).setNaturalValue(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        holder.lab_grown_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                list.get(position).setLabGrownValue(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
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

    public ArrayList<DealerMarkupListModel> getUpdatedList() {
        return list;
    }

    class RecycleViewHolder extends RecyclerView.ViewHolder {
        TextView debit_value;
        EditText natural_et, lab_grown_et;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            debit_value = itemView.findViewById(R.id.debit_value);
            natural_et = itemView.findViewById(R.id.natural_et);
            lab_grown_et = itemView.findViewById(R.id.lab_grown_et);
        }
    }
}
