package com.diamondxe.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.diamondxe.Beans.AttributeDetailsModel;
import com.diamondxe.R;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FancyColorIntensityListAdapter extends ArrayAdapter<AttributeDetailsModel> {

    private static final int HINT_POSITION = 0;
    private String hint;
    private Context context;

    private ArrayList<AttributeDetailsModel> algorithmList;

    public FancyColorIntensityListAdapter(Context context, ArrayList<AttributeDetailsModel> algorithmList, String hint) {
        super(context, 0, algorithmList);
        this.context = context;
        this.hint = hint;
        this.algorithmList = algorithmList;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_drop_down_fancy_color_spinner, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.text_view);

        AttributeDetailsModel currentItem = getItem(position);

        if (position == 0) { // Hint item
            textView.setText(hint); // Set hint if the item is null (i.e., at HINT_POSITION)
            textView.setTextColor(ContextCompat.getColor(context, R.color.white)); // Red color for hint in dropdown
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_light));
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
            textView.setTextColor(ContextCompat.getColor(context, R.color.black)); // Black color for other items
            textView.setClickable(false);
            textView.setEnabled(false);
        }
        //textView.setTextColor(position == 0 ? Color.RED : Color.BLACK); // Red for hint in dropdown, black for other items
        return convertView;
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_fancy_color_spinner, parent, false);
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
