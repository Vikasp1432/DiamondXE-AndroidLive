package com.diamondxe.Adapter.FilterApplied;

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

import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.Beans.AttributeDetailsModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;

import java.util.ArrayList;


public class ColorTypeAppliedFilterDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<AttributeDetailsModel> list;
    Context context;
    int last;
    public ArrayList<AttributeDetailsModel> arraylist;
    RecyclerInterface recyclerInterface;

    public ColorTypeAppliedFilterDataAdapter(ArrayList<AttributeDetailsModel> list, Context context, RecyclerInterface recyclerInterface) {
        this.list = list;
        this.context = context;
        this.arraylist = new ArrayList<AttributeDetailsModel>();
        this.arraylist.clear();
        this.arraylist.addAll(list);
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

        View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.row_cut_applied_filter_list, parent, false);
        return new RecycleViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final RecycleViewHolder holder = (RecycleViewHolder) viewHolder;

        if(list.get(position).isSelected())
        {
            if(list.get(position).getFilterType().equalsIgnoreCase("SearchFrm"))
            {
                holder.type_tv.setText("Search: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("PriceFrm"))
            {
                holder.type_tv.setText("PriceFrom: " + ApiConstants.rupeesIcon + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("PriceTo"))
            {
                holder.type_tv.setText("PriceTo: " + ApiConstants.rupeesIcon + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("CaratFrom"))
            {
                holder.type_tv.setText("CaratFrom: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("CaratTo"))
            {
                holder.type_tv.setText("CaratTo: " + list.get(position).getDisplayAttr());
            }
           else if(list.get(position).getFilterType().equalsIgnoreCase("Shape"))
            {
                holder.type_tv.setText("Shape: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("Color"))
            {
                holder.type_tv.setText("Color: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("FancyColor"))
            {
                holder.type_tv.setText("FancyColor: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("Clarity")){
                holder.type_tv.setText("Clarity: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("Certificate")){
                holder.type_tv.setText("Certificate: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("FluoRescence")){
                holder.type_tv.setText("FluoRescence: " + list.get(position).getDisplayAttr());
            }
            /*else if(list.get(position).getFilterType().equalsIgnoreCase("Make")){
                holder.type_tv.setText("Make: " + list.get(position).getDisplayAttr());
            }*/
            else if(list.get(position).getFilterType().equalsIgnoreCase("Returnable")){
                holder.type_tv.setText("Returnable: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("Cut")){
                holder.type_tv.setText("Cut: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("Polish")){
                holder.type_tv.setText("Polish: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("Symmetry")){
                holder.type_tv.setText("Symmetry: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("Technology")){
                holder.type_tv.setText("Technology: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("Eye")){
                holder.type_tv.setText("Eye: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("Shade")){
                holder.type_tv.setText("Shade: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("Luster")){
                holder.type_tv.setText("Luster: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("FancyColorIntensity")){
                holder.type_tv.setText("FancyColorIntensity: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("FancyColorOvertone")){
                holder.type_tv.setText("FancyColorOvertone: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("TableFrom")){
                holder.type_tv.setText("TableFrom: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("TableTo")){
                holder.type_tv.setText("TableTo: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("DepthFromSpinner")){
                holder.type_tv.setText("DepthFrom: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("DepthToSpinner")){
                holder.type_tv.setText("DepthTo: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("LengthFrom")){
                holder.type_tv.setText("LengthFrom: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("LengthTo")){
                holder.type_tv.setText("LengthTo: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("WidthFrom")){
                holder.type_tv.setText("WidthFrom: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("WidthTo")){
                holder.type_tv.setText("WidthTo: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("DepthFrmInput")){
                holder.type_tv.setText("DepthFrom: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("DepthToInput")){
                holder.type_tv.setText("DepthTo: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("CrownFrom")){
                holder.type_tv.setText("CrownFrom: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("CrownTo")){
                holder.type_tv.setText("CrownTo: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("PavillionFrom")){
                holder.type_tv.setText("PavillionFrom: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("PavillionTo")){
                holder.type_tv.setText("PavillionTo: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("LotID")){
                holder.type_tv.setText("LotID: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("Location")){
                holder.type_tv.setText("Location: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("Origin")){
                holder.type_tv.setText("Origin: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("Treatment")){
                holder.type_tv.setText("Treatment: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("Item")){
                holder.type_tv.setText("Item: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("CuttingStyle")){
                holder.type_tv.setText("CuttingStyle: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("weightFirst")){
                holder.type_tv.setText("weightTo: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("weightThird")){
                holder.type_tv.setText("weightFrom: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("stockID")){
                holder.type_tv.setText("stockID: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("GemShapes")){
                holder.type_tv.setText("Shape: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("SelectPrice")){
                holder.type_tv.setText("Price: " + list.get(position).getDisplayAttr());
            }
            else if(list.get(position).getFilterType().equalsIgnoreCase("SelectWeight")){
                holder.type_tv.setText("Weight: " + list.get(position).getDisplayAttr());
            }


        }/*else{
            holder.type_tv.setText("");
        }*/

        if(list.get(position).isSelected())
        {
            holder.cardView.setCardElevation(0f);
            holder.cardView.setCardElevation(0f);
            holder.cardView.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.white));
            holder.cardView.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.white));
        }
        else {}

        if(list.get(position).isFirstPosition())
        {
            //Log.e("position : ", ""+list.get(position).isFirstPosition());
           /* ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.root_layout.getLayoutParams();
            params.setMargins(60,0, 12, 0);
            holder.root_layout.setLayoutParams(params);*/
        }else{}

        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerInterface.itemClick(position,"filterAppliedType");
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
        CardView cardView;
        TextView type_tv;

        public RecycleViewHolder (View itemView) {
            super (itemView);

            root_layout = itemView.findViewById(R.id.root_layout);
            type_tv = itemView.findViewById(R.id.type_tv);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }


}
