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
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Beans.AddressListModel;
import com.diamondxe.Beans.KYCVerificationModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;

import java.util.ArrayList;


public class KYCVerificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<KYCVerificationModel> list;
    Context context;
    int last;
    public ArrayList<KYCVerificationModel> arraylist;
    RecyclerInterface recyclerInterface;

    Handler handler = new Handler(Looper.getMainLooper());


    public KYCVerificationAdapter(ArrayList<KYCVerificationModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<KYCVerificationModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_kyc_verification_doc_list, parent, false);
            return new RecycleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        holder.document_name_tv.setText(list.get(position).getAttachmentType());
        if(!list.get(position).getAttachmentDate().equalsIgnoreCase(""))
        {
            holder.document_submitted_date_tv.setText(list.get(position).getAttachmentDate());
        }
        else{
            holder.document_submitted_date_tv.setText("--");
        }

        if(list.get(position).getVerifiedInd().equalsIgnoreCase("0"))
        {
            holder.document_status_tv.setText("Pending");
            holder.document_status_tv.setBackgroundResource(R.drawable.background_gray);
            holder.document_status_tv.setTextColor(ContextCompat.getColor(context,R.color.black));
        }
        else if(list.get(position).getVerifiedInd().equalsIgnoreCase("1"))
        {
            holder.document_status_tv.setText("Verified");
            holder.document_status_tv.setBackgroundResource(R.drawable.background_green_light);
            holder.document_status_tv.setTextColor(ContextCompat.getColor(context,R.color.white));
        }
        else{
            holder.document_status_tv.setText("Rejected");
            holder.document_status_tv.setBackgroundResource(R.drawable.background_red);
            holder.document_status_tv.setTextColor(ContextCompat.getColor(context,R.color.white));
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
        TextView document_name_tv, document_status_tv, document_submitted_date_tv;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            document_name_tv = itemView.findViewById(R.id.document_name_tv);
            document_status_tv = itemView.findViewById(R.id.document_status_tv);
            document_submitted_date_tv = itemView.findViewById(R.id.document_submitted_date_tv);
        }
    }
}
