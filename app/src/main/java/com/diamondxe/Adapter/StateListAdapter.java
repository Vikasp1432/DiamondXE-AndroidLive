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
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.CountryListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;


public class StateListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<CountryListModel> list;
    Context context;
    int last;
    public ArrayList<CountryListModel> arraylist;
    RecyclerInterface recyclerInterface;

    public StateListAdapter(ArrayList<CountryListModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<CountryListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_state_list, parent, false);
            return new RecycleViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        holder.name_tv.setText(list.get(position).getTitle());

        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerInterface.itemClick(position,"stateName");
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
        TextView name_tv;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            root_layout = itemView.findViewById(R.id.root_layout);
            name_tv = itemView.findViewById(R.id.name_tv);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }


    public void filter(String charText, int textlength)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(arraylist);
        } else {

            for (CountryListModel listModel : arraylist) {
                try {
                    String name = listModel.getTitle().toLowerCase();
                    name = name.trim();

                    if (name.contains(charText.toString().toLowerCase()))
                    {
                        list.add(listModel);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        notifyDataSetChanged();
    }

}
