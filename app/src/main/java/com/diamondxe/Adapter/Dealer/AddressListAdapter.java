package com.diamondxe.Adapter.Dealer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.AddressListModel;
import com.diamondxe.Beans.SearchResultTypeModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class AddressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<AddressListModel> list;
    Context context;
    int last;
    public ArrayList<AddressListModel> arraylist;
    RecyclerInterface recyclerInterface;
    private int width=0;
    Handler handler = new Handler(Looper.getMainLooper());


    public AddressListAdapter(ArrayList<AddressListModel> list, Context context, RecyclerInterface recyclerInterface, int width) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<AddressListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.width = width;
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_horizontal_address_list, parent, false);
            return new RecycleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        if (list.size()>1)
        {
            //For set width of Card or Image.
            ViewGroup.LayoutParams layoutParams = holder.main_root_layout.getLayoutParams();
            layoutParams.width = width;
            holder.main_root_layout.setLayoutParams(layoutParams);
        } else {}

        holder.name_tv.setText(list.get(position).getCityNameS());
        holder.address_tv.setText(list.get(position).getAddress1() + ", " + list.get(position).getCityNameS()
                + ", " + list.get(position).getStateNameS() + " " + list.get(position).getPinCode() + ", " + list.get(position).getCountryNameS());
        holder.phone_no_tv.setText("Phone: " + list.get(position).getMobileDialCode() + " " +list.get(position).getMobileNumber());
        holder.email_tv.setText("Email: " + list.get(position).getEmailId());
        holder.address_type_tv.setText(list.get(position).getAddressType());

        // This is Use For User Use Address For Place Order, If User Use For Place Order Delete, Edit Icon Not Show Only Show Radio Selection
        // OtherCase Radio Button Not Visible Only Edit and Delete Icon Work.

        holder.set_is_default_btn.setVisibility(View.GONE);

        // Check Select Position Address Radio Button Selected
        if(list.get(position).isSelected())
        {
            holder.set_is_default_btn.setChecked(true);
        }
        else
        {
            holder.set_is_default_btn.setChecked(false);
        }

        // If Is Default Address 1 then User Not Delete this address
        if(list.get(position).getIsDefault().equalsIgnoreCase("1"))
        {
            holder.is_default_address_lbl.setVisibility(View.VISIBLE);
            holder.delete_img.setVisibility(View.VISIBLE);
        }
        else{
            holder.is_default_address_lbl.setVisibility(View.INVISIBLE);
            holder.delete_img.setVisibility(View.VISIBLE);
        }

        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerInterface.itemClick(position, "selectShippingAddress");

            }
        });

        holder.edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerInterface.itemClick(position, "editAddress");

            }
        });

        holder.delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerInterface.itemClick(position, "deleteAddress");

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

        RelativeLayout main_root_layout;
        CardView root_layout;
        TextView name_tv, address_tv, phone_no_tv,email_tv, address_type_tv,is_default_address_lbl;
        ImageView delete_img,edit_img;
        RadioButton set_is_default_btn;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            main_root_layout = itemView.findViewById(R.id.main_root_layout);
            root_layout = itemView.findViewById(R.id.root_layout);
            name_tv = itemView.findViewById(R.id.name_tv);
            address_tv = itemView.findViewById(R.id.address_tv);
            phone_no_tv = itemView.findViewById(R.id.phone_no_tv);
            email_tv = itemView.findViewById(R.id.email_tv);
            address_type_tv = itemView.findViewById(R.id.address_type_tv);
            is_default_address_lbl = itemView.findViewById(R.id.is_default_address_lbl);

            delete_img = itemView.findViewById(R.id.delete_img);
            edit_img = itemView.findViewById(R.id.edit_img);
            set_is_default_btn = itemView.findViewById(R.id.set_is_default_btn);

        }
    }


}
