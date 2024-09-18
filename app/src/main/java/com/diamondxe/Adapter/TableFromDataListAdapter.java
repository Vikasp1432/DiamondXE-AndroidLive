package com.diamondxe.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.diamondxe.Beans.AttributeDetailsModel;
import com.diamondxe.R;

import java.util.ArrayList;

public class TableFromDataListAdapter extends ArrayAdapter<AttributeDetailsModel> {

    private static final int HINT_POSITION = 0;
    private String hint;
    Context context;

    public TableFromDataListAdapter(Context context, ArrayList<AttributeDetailsModel> algorithmList, String hint) {
        super(context, 0, algorithmList);
        this.hint = hint;
        this.context = context;
    }

    @Override
    public int getCount() {
        // Adjust count to include hint
        return super.getCount() + 1;
    }

    @Nullable
    @Override
    public AttributeDetailsModel getItem(int position) {
        return position == HINT_POSITION ? null : super.getItem(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position == HINT_POSITION ? -1 : super.getItemId(position - 1);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_fancy_color_overtone_drop_down_list, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.text_view);

        AttributeDetailsModel currentItem = getItem(position);

        if (position == 0) { // Hint item
            textView.setText(hint); // Set hint if the item is null (i.e., at HINT_POSITION)
            textView.setTextColor(ContextCompat.getColor(context, R.color.white)); // Red color for hint in dropdown
            textView.setBackgroundResource(R.drawable.spinner_drop_down_color);
            textView.setClickable(true);
            textView.setEnabled(true);
        } else {
            if (currentItem != null) {
                textView.setText(currentItem.getDisplayAttr()); // Set item name
            } else {
                textView.setText(""); // Clear text for non-hint items if null
            }

            // Reset background and text color for non-hint items
            textView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent)); // Reset background
            textView.setTextColor(ContextCompat.getColor(context, R.color.black)); // Set text color for other items
            textView.setClickable(false);
            textView.setEnabled(false);
        }
        return convertView;
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_table_from_data_list, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.text_view);
        if (position == HINT_POSITION) {
            textViewName.setText(hint);
            //textViewName.setTextColor(ContextCompat.getColor(getContext(), R.color.hint_color)); // Customize hint color
            textViewName.setTextColor(ContextCompat.getColor(getContext(), R.color.black)); // Customize hint color
        } else {
            AttributeDetailsModel currentItem = getItem(position);
            if (currentItem != null) {
                textViewName.setText(currentItem.getDisplayAttr());
                textViewName.setTextColor(ContextCompat.getColor(getContext(), R.color.black)); // Customize item color
            }
        }

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return position != HINT_POSITION; // Disable the hint item
    }
}
