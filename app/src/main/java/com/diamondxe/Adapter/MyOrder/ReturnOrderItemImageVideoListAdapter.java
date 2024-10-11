package com.diamondxe.Adapter.MyOrder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Activity.MyOrder.ReturnOrderItemsImageVideoActivity;
import com.diamondxe.Beans.Dealer.DealerMarkupListModel;
import com.diamondxe.Beans.MyOrder.MyOrderListModel;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ReturnOrderItemImageVideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MyOrderListModel> list;
    Context context;
    int last;
    public ArrayList<MyOrderListModel> arraylist;
    TwoRecyclerInterface recyclerInterface;

    public ReturnOrderItemImageVideoListAdapter(ArrayList<MyOrderListModel> list, Context context, TwoRecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<MyOrderListModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_return_order_item_image_video_list, parent, false);
            return new RecycleViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        if(list.size()>1)
        {
            holder.view_line.setVisibility(View.VISIBLE);
        }
        else {
            holder.view_line.setVisibility(View.GONE);
        }

        if(!list.get(position).getDiamondImage().equalsIgnoreCase(""))
        {
            Picasso.with(context)
                    .load(list.get(position).getDiamondImage())
                    .placeholder(R.mipmap.phl_diamond)
                    .error(R.mipmap.phl_diamond)
                    .into(holder.image_view);
        }
        else{
            Picasso.with(context)
                    .load(R.mipmap.phl_diamond)
                    .placeholder(R.mipmap.phl_diamond)
                    .error(R.mipmap.phl_diamond)
                    .resize(500,500)
                    .centerCrop()
                    .into(holder.image_view);
        }

        holder.stock_no_tv.setText("#"+list.get(position).getCertificateNo());
        holder.name_tv.setText(list.get(position).getShape());
        holder.status.setText(list.get(position).getStatus());
        holder.item_type_tv.setText(list.get(position).getCarat() + context.getResources().getString(R.string.ct) + " | " + list.get(position).getColor() + " | " + list.get(position).getClarity());

        if(!list.get(position).getSubTotal().equalsIgnoreCase(""))
        {
            holder.sub_total_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));
        }else {}

        if(list.get(position).getCategory().equalsIgnoreCase("Natural"))
        {
            holder.diamond_type_tv.setBackgroundResource(R.drawable.background_yellow);
            holder.diamond_type_tv.setText("NATURAL");
        }
        else{
            holder.diamond_type_tv.setBackgroundResource(R.drawable.background_green_light_small_round_cornor);
            holder.diamond_type_tv.setText("LAB");
        }

        // Set Reason
        if(!list.get(position).getSelectedReason().equalsIgnoreCase(""))
        {
            holder.reason_type_tv.setText(list.get(position).getSelectedReason());
        }
        else{
            holder.reason_type_tv.setHint(context.getResources().getString(R.string.select_reason));
        }

        // Set Comment
        if(!list.get(position).getWriteMessage().equalsIgnoreCase(""))
        {
            holder.write_message_et.setText(list.get(position).getWriteMessage());
        }
        else{
            holder.write_message_et.setHint(context.getResources().getString(R.string.write_your_message));
        }

        // Set Video URL
        if(!list.get(position).getReturnOrderVideoUrl().equalsIgnoreCase(""))
        {
            holder.video_url_et.setText(list.get(position).getReturnOrderVideoUrl());
        }
        else{
            holder.video_url_et.setHint(context.getResources().getString(R.string.video_link_url));
        }

        // Reason Click Event
        holder.reason_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ReturnOrderItemsImageVideoActivity) context).setLastClickedView(view); // Store the clicked view
                recyclerInterface.itemClick(position, 0, "reasonType");
            }
        });

        // Write Comment Set a text change listener to update the model when the text changes
        holder.write_message_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Update the model in the list based on the current position
                list.get(position).setWriteMessage(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // --------------------------------------Start Upload Image Click Event and Set------------------------------------------------
        holder.upload_verify_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerInterface.itemClick(position, 0, "selectImage");
            }
        });
        holder.upload_image_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerInterface.itemClick(position, 0, "selectImage");
            }
        });

        // After Upload Image Image UI.
        if(!list.get(position).getReturnOrderImage().equalsIgnoreCase(""))
        {
            holder.upload_image_lin.setVisibility(View.GONE);
            holder.upload_verify_image.setVisibility(View.VISIBLE);
        }
        else{
            holder.upload_image_lin.setVisibility(View.VISIBLE);
            holder.upload_verify_image.setVisibility(View.GONE);
        }
        // -----------------------------------------End Upload Image Click Event and Set------------------------------------------------


        // --------------------------------------Start Upload Video Click Event and Set------------------------------------------------
        holder.upload_video_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerInterface.itemClick(position, 0, "selectVideo");
            }
        });

        holder.upload_verify_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerInterface.itemClick(position, 0, "selectVideo");
            }
        });

        // After Upload Video UI Refresh.
        if(!list.get(position).getReturnOrderVideo().equalsIgnoreCase(""))
        {
            holder.upload_video_lin.setVisibility(View.GONE);
            holder.upload_verify_video.setVisibility(View.VISIBLE);

        }
        else{
            holder.upload_video_lin.setVisibility(View.VISIBLE);
            holder.upload_verify_video.setVisibility(View.GONE);
        }
        // -----------------------------------------End Upload Video Click Event and Set------------------------------------------------


        // Video URl Set a text change listener to update the model when the text changes
        holder.video_url_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Update the model in the list based on the current position
                list.get(position).setReturnOrderVideoUrl(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
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

    public ArrayList<MyOrderListModel> getUpdatedList() {
        return list;
    }

    class RecycleViewHolder extends RecyclerView.ViewHolder {
        TextView  name_tv, status, item_type_tv, stock_no_tv, return_policy_tv, sub_total_tv, diamond_type_tv,reason_error_tv,reason_type_tv,
                valid_url_required_tv;
        ImageView image_view, upload_verify_image, upload_verify_video;
        RelativeLayout reason_rel;
        EditText write_message_et, video_url_et;
        LinearLayout upload_image_lin,upload_video_lin, url_know_more_lin;
        View view_line;
        public RecycleViewHolder (View itemView) {
            super (itemView);

            name_tv = itemView.findViewById(R.id.name_tv);
            status = itemView.findViewById(R.id.status);
            item_type_tv = itemView.findViewById(R.id.item_type_tv);
            stock_no_tv = itemView.findViewById(R.id.stock_no_tv);
            return_policy_tv = itemView.findViewById(R.id.return_policy_tv);
            sub_total_tv = itemView.findViewById(R.id.sub_total_tv);
            diamond_type_tv = itemView.findViewById(R.id.diamond_type_tv);
            image_view = itemView.findViewById(R.id.image_view);

            reason_rel = itemView.findViewById(R.id.reason_rel);
            reason_type_tv = itemView.findViewById(R.id.reason_type_tv);
            reason_error_tv = itemView.findViewById(R.id.reason_error_tv);
            write_message_et = itemView.findViewById(R.id.write_message_et);
            upload_image_lin = itemView.findViewById(R.id.upload_image_lin);
            upload_verify_image = itemView.findViewById(R.id.upload_verify_image);
            upload_video_lin = itemView.findViewById(R.id.upload_video_lin);
            upload_verify_video = itemView.findViewById(R.id.upload_verify_video);
            video_url_et = itemView.findViewById(R.id.video_url_et);
            url_know_more_lin = itemView.findViewById(R.id.url_know_more_lin);

            view_line = itemView.findViewById(R.id.view_line);
            valid_url_required_tv = itemView.findViewById(R.id.valid_url_required_tv);

        }
    }

}
