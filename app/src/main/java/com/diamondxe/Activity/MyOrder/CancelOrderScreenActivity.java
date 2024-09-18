package com.diamondxe.Activity.MyOrder;

import static com.diamondxe.ApiCalling.ApiConstants.rupeesIcon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Activity.HomeScreenActivity;
import com.diamondxe.Adapter.MyOrder.CancelOrderListAdapter;
import com.diamondxe.Adapter.MyOrder.InnerCancelOrderListAdapter;
import com.diamondxe.Adapter.MyOrder.InnerOrderListAdapter;
import com.diamondxe.Adapter.MyOrder.RecentOrderListAdapter;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.MyOrder.InnerOrderListModel;
import com.diamondxe.Beans.MyOrder.MyOrderListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CancelOrderScreenActivity extends SuperActivity implements TwoRecyclerInterface {

    private ImageView back_img;
    private RecyclerView recycler_view;
    private RelativeLayout cancel_coupon_code_value_rel, cancel_wallet_apply_point_rel,cancel_rel_other_tax,cancel_rel_diamond_tax, coupon_code_value_rel, wallet_apply_point_rel,
            rel_other_tax,rel_diamond_tax, reason_rel;
    private TextView error_tv, cancel_order_tv, processed_order_tv, cancel_diamond_price_tv, cancel_shipping_and_handling_tv, cancel_platform_fees_tv,
            cancel_total_charges_tv,cancel_other_taxes_tv, cancel_diamond_taxes_tv, cancel_total_taxes_tv, cancel_sub_total_tv, cancel_wallet_apply_charges_tv,
            cancel_bank_charges_tv,cancel_final_amount_tv, cancel_others_txt_gst_perc_tv, cancel_diamond_txt_gst_perc_tv, coupon_code_value_tv, diamond_price_tv, shipping_and_handling_tv,platform_fees_tv, total_charges_tv,other_taxes_tv, diamond_taxes_tv,
            total_taxes_tv, sub_total_tv,wallet_apply_charges_tv, bank_charges_tv,final_amount_tv,others_txt_gst_perc_tv,diamond_txt_gst_perc_tv,
            reason_type_tv,reason_error_tv, message_error_tv;
    private EditText write_message_et;
    private RadioGroup radio_group;
    private RadioButton full_cancel_order_radio, partial_cancel_order_radio;
    private CardView cancel_order_card_view, processed_order_card_view;
    private Activity activity;
    private Context context;
    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    private ArrayList<MyOrderListModel> modelArrayList;
    private CancelOrderListAdapter adapter;
    private ArrayList<InnerOrderListModel> innerOrderArrayList;
    InnerCancelOrderListAdapter innerCancelOrderListAdapter;
    Handler handler1 = new Handler(Looper.getMainLooper());
    String selectCancelOrderReasonType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order_screen);

        context = activity = this;

        modelArrayList = new ArrayList<>();

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        radio_group = findViewById(R.id.radio_group);
        full_cancel_order_radio = findViewById(R.id.full_cancel_order_radio);
        partial_cancel_order_radio = findViewById(R.id.partial_cancel_order_radio);

        cancel_order_tv = findViewById(R.id.cancel_order_tv);
        cancel_order_tv.setOnClickListener(this);

        processed_order_tv = findViewById(R.id.processed_order_tv);
        processed_order_tv.setOnClickListener(this);

        error_tv = findViewById(R.id.error_tv);
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);

        cancel_order_card_view = findViewById(R.id.cancel_order_card_view);
        processed_order_card_view = findViewById(R.id.processed_order_card_view);

        //------------------------------Cancel Order Summary ID's Start--------------------------------------------

        cancel_coupon_code_value_rel = findViewById(R.id.cancel_coupon_code_value_rel);
        cancel_wallet_apply_point_rel = findViewById(R.id.cancel_wallet_apply_point_rel);

        cancel_rel_other_tax = findViewById(R.id.cancel_rel_other_tax);
        cancel_rel_other_tax.setOnClickListener(this);

        cancel_rel_diamond_tax = findViewById(R.id.cancel_rel_diamond_tax);
        cancel_rel_diamond_tax.setOnClickListener(this);

        cancel_diamond_price_tv = findViewById(R.id.cancel_diamond_price_tv);
        cancel_shipping_and_handling_tv = findViewById(R.id.cancel_shipping_and_handling_tv);
        cancel_platform_fees_tv = findViewById(R.id.cancel_platform_fees_tv);
        cancel_total_charges_tv = findViewById(R.id.cancel_total_charges_tv);
        cancel_other_taxes_tv = findViewById(R.id.cancel_other_taxes_tv);
        cancel_diamond_taxes_tv = findViewById(R.id.cancel_diamond_taxes_tv);
        cancel_total_taxes_tv = findViewById(R.id.cancel_total_taxes_tv);
        cancel_sub_total_tv = findViewById(R.id.cancel_sub_total_tv);
        cancel_wallet_apply_charges_tv = findViewById(R.id.cancel_wallet_apply_charges_tv);
        cancel_bank_charges_tv = findViewById(R.id.cancel_bank_charges_tv);
        cancel_final_amount_tv = findViewById(R.id.cancel_final_amount_tv);
        cancel_others_txt_gst_perc_tv = findViewById(R.id.cancel_others_txt_gst_perc_tv);
        cancel_diamond_txt_gst_perc_tv = findViewById(R.id.cancel_diamond_txt_gst_perc_tv);

        //------------------------------Cancel Order Summary ID's End---------------------------------------------

        //------------------------------My Order Summary ID'S Start----------------------------------------------

        coupon_code_value_rel = findViewById(R.id.coupon_code_value_rel);
        wallet_apply_point_rel = findViewById(R.id.wallet_apply_point_rel);

        rel_other_tax = findViewById(R.id.rel_other_tax);
        rel_other_tax.setOnClickListener(this);

        rel_diamond_tax = findViewById(R.id.rel_diamond_tax);
        rel_diamond_tax.setOnClickListener(this);

        coupon_code_value_tv = findViewById(R.id.coupon_code_value_tv);
        diamond_price_tv = findViewById(R.id.diamond_price_tv);
        shipping_and_handling_tv = findViewById(R.id.shipping_and_handling_tv);
        platform_fees_tv = findViewById(R.id.platform_fees_tv);
        total_charges_tv = findViewById(R.id.total_charges_tv);
        other_taxes_tv = findViewById(R.id.other_taxes_tv);
        diamond_taxes_tv = findViewById(R.id.diamond_taxes_tv);
        total_taxes_tv = findViewById(R.id.total_taxes_tv);
        sub_total_tv = findViewById(R.id.sub_total_tv);
        wallet_apply_charges_tv = findViewById(R.id.wallet_apply_charges_tv);
        bank_charges_tv = findViewById(R.id.bank_charges_tv);
        final_amount_tv = findViewById(R.id.final_amount_tv);
        others_txt_gst_perc_tv = findViewById(R.id.others_txt_gst_perc_tv);
        diamond_txt_gst_perc_tv = findViewById(R.id.diamond_txt_gst_perc_tv);

        //------------------------------My Order Summary ID'S End----------------------------------------------

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.full_cancel_order_radio)
                {
                    setFullCancelOrderAdapter();
                }
                else if (checkedId == R.id.partial_cancel_order_radio)
                {
                    setPartialCancelOrderAdapter();
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setNestedScrollingEnabled(false);

       setDummyData();
    }

    // Radio Button Check Full Cancellation Then Work This Method.
    void setFullCancelOrderAdapter()
    {
        adapter = new CancelOrderListAdapter(modelArrayList,context,this, "fullOrderCancel");
        recycler_view.setAdapter(adapter);
    }

    // Radio Button Check Partial Cancellation Then Work This Method.
    void setPartialCancelOrderAdapter()
    {
        adapter = new CancelOrderListAdapter(modelArrayList,context,this, "partialOrderCancel");
        recycler_view.setAdapter(adapter);
    }

    void setDummyData()
    {
        MyOrderListModel model = new MyOrderListModel();

        model.setOrderNumber("170245895326589");
        model.setOrderDateTime("06/03/24, 05:14:24 PM");
        model.setName("Eleganted Cushion");
        model.setStatus("Confirmed");
        model.setCarat("0.19Ct");
        model.setColor("E");
        model.setClarity("SI2");
        model.setStockNumber("6481931311");
        model.setCategory("LAB");
        model.setSubTotal("1877100000");
        model.setShowingSubTotal("1877100000");
        model.setCurrencySymbol(rupeesIcon);
        model.setImage("https://v360.in/V360Images.aspx?cid=vd&d=KG230-184A");
        model.setChecked(false);

        /*innerOrderArrayList = new ArrayList<InnerOrderListModel>();

        InnerOrderListModel innerOrderListModel = new InnerOrderListModel();

        innerOrderListModel.setOrderNumber("170245895326589");
        innerOrderListModel.setOrderDateTime("06/03/24, 05:14:24 PM");
        innerOrderListModel.setName("Eleganted Cushion");
        innerOrderListModel.setStatus("Confirmed");
        innerOrderListModel.setCarat("0.19");
        innerOrderListModel.setColor("E");
        innerOrderListModel.setClarity("SI2");
        innerOrderListModel.setStockNumber("6481931311");
        innerOrderListModel.setCategory("LAB");
        innerOrderListModel.setSubTotal("1877100000");
        innerOrderListModel.setShowingSubTotal("1877100000");
        innerOrderListModel.setCurrencySymbol(rupeesIcon);
        innerOrderListModel.setImage("https://v360.in/V360Images.aspx?cid=vd&d=KG230-184A");
        innerOrderListModel.setChecked(false);

        innerOrderArrayList.add(innerOrderListModel);

        model.setAllItemsInSection(innerOrderArrayList);*/

        modelArrayList.add(model);
        modelArrayList.add(model);
        modelArrayList.add(model);
        modelArrayList.add(model);
        modelArrayList.add(model);
        modelArrayList.add(model);

        // Set Adapter Value.
        setFullCancelOrderAdapter();
    }

    @Override
    public void onClick(View view)
    {
        Intent intent;
        int id = view.getId();

        if(id == R.id.back_img)
        {
            Utils.hideKeyboard(activity);
            finish();
        }
        else if(id == R.id.cancel_order_tv)
        {
            Utils.hideKeyboard(activity);
            finish();
        }
        else if(id == R.id.processed_order_tv)
        {
            Utils.hideKeyboard(activity);
            selectCancelOrderReasonType="";
            orderCancelConfirmationPopup(activity,context);
        }
        else if(id == R.id.cancel_rel_other_tax)
        {
            Utils.hideKeyboard(activity);
            cancel_others_txt_gst_perc_tv.setVisibility(View.VISIBLE);
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cancel_others_txt_gst_perc_tv.setVisibility(View.GONE);
                }
            }, 2000);
        }
        else if(id == R.id.cancel_rel_diamond_tax)
        {
            Utils.hideKeyboard(activity);
            cancel_diamond_txt_gst_perc_tv.setVisibility(View.VISIBLE);
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cancel_diamond_txt_gst_perc_tv.setVisibility(View.GONE);
                }
            }, 2000);
        }
        else if(id == R.id.rel_other_tax)
        {
            Utils.hideKeyboard(activity);
            others_txt_gst_perc_tv.setVisibility(View.VISIBLE);
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    others_txt_gst_perc_tv.setVisibility(View.GONE);
                }
            }, 2000);
        }
        else if(id == R.id.rel_diamond_tax)
        {
            Utils.hideKeyboard(activity);
            diamond_txt_gst_perc_tv.setVisibility(View.VISIBLE);
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    diamond_txt_gst_perc_tv.setVisibility(View.GONE);
                }
            }, 2000);
        }
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

    }

    @Override
    public void getErrorResponce(String error, int service_ID) {

    }

    @Override
    public void itemClick(int parantPosition, int chiledPosition, String action)
    {
        if(action.equalsIgnoreCase("orderCancelPartialCheck"))
        {
            MyOrderListModel item = modelArrayList.get(parantPosition);
            item.setChecked(!item.isChecked());
            // Notify the adapter about the change in item state
            adapter.notifyItemChanged(parantPosition);
            adapter.notifyDataSetChanged();

        }
    }

    // Order Cancellation Confirmation Popup
    void orderCancelConfirmationPopup(final Activity activity,final Context context)
    {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_order_cancel_confirmation_layout, null);
        dialogBuilder.setView(dialogView);
        final android.app.AlertDialog alertDialog = dialogBuilder.create();

        final ImageView cross_img = dialogView.findViewById(R.id.cross_img);
        final RadioGroup radio_group = dialogView.findViewById(R.id.radio_group);
        final RadioButton wallet_radio = dialogView.findViewById(R.id.wallet_radio);
        final RadioButton payment_mode_radio = dialogView.findViewById(R.id.payment_mode_radio);
        reason_rel = dialogView.findViewById(R.id.reason_rel);
        reason_type_tv = dialogView.findViewById(R.id.reason_type_tv);
        write_message_et = dialogView.findViewById(R.id.write_message_et);
        reason_error_tv = dialogView.findViewById(R.id.reason_error_tv);
        message_error_tv = dialogView.findViewById(R.id.message_error_tv);
        final TextView cancel_order_tv = dialogView.findViewById(R.id.cancel_order_tv);
        final TextView processed_order_tv = dialogView.findViewById(R.id.processed_order_tv);

        cross_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.wallet_radio)
                {
                } else if (checkedId == R.id.payment_mode_radio)
                {
                }
            }
        });

        reason_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                reasonTypePopupWindow();
            }
        });

        write_message_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!write_message_et.getText().toString().equalsIgnoreCase("")){
                    message_error_tv.setVisibility(View.GONE);
                    write_message_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });


        cancel_order_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                alertDialog.dismiss();
            }
        });

        processed_order_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(validateFields())
                {
                    alertDialog.dismiss();
                    orderCancelSuccessfullyPopup(activity, context);

                }else{}

            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    // Order Cancel Successfully Popup.
    void orderCancelSuccessfullyPopup(final Activity activity,final Context context)
    {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_cancel_order_successfully_layout, null);
        dialogBuilder.setView(dialogView);
        final android.app.AlertDialog alertDialog = dialogBuilder.create();

        final ImageView cross_img = dialogView.findViewById(R.id.cross_img);
        final TextView done_tv = dialogView.findViewById(R.id.done_tv);
        final TextView message = dialogView.findViewById(R.id.message);
        final TextView message1 = dialogView.findViewById(R.id.message1);

        message.setText(getResources().getString(R.string.cancellation_successful));
        message1.setText(getResources().getString(R.string.cancellation_request_msg));

        cross_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        done_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    private PopupWindow mDropdown = null;
    LayoutInflater mInflater;
    // Button pop;
    private TextView select_mode_lbl, too_expensive_tv, wrong_diamond_tv,other_tv;
    private PopupWindow reasonTypePopupWindow() {
        try {
            if (mDropdown != null && mDropdown.isShowing()) {
                mDropdown.dismiss();
            }
            mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.custom_menu_reason_type, null);

            select_mode_lbl = layout.findViewById(R.id.select_mode_lbl);
            too_expensive_tv = layout.findViewById(R.id.too_expensive_tv);
            wrong_diamond_tv = layout.findViewById(R.id.wrong_diamond_tv);
            other_tv = layout.findViewById(R.id.other_tv);


            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            //mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);

            mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);

            /*Drawable background = getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            mDropdown.setBackgroundDrawable(background);*/

            // Ensure pop is not null before using it
            if (reason_rel != null) {
                mDropdown.showAsDropDown(reason_rel, 5, -50);
            } else {
                Log.e("PopupWindow", "pop is null");
            }

            select_mode_lbl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            too_expensive_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectCancelOrderReasonType = too_expensive_tv.getText().toString().trim();
                    reason_type_tv.setText(too_expensive_tv.getText().toString().trim());
                    removeValidationError();
                    mDropdown.dismiss();
                }
            });

            wrong_diamond_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectCancelOrderReasonType = wrong_diamond_tv.getText().toString().trim();
                    reason_type_tv.setText(wrong_diamond_tv.getText().toString().trim());
                    removeValidationError();
                    mDropdown.dismiss();
                }
            });

            other_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectCancelOrderReasonType = other_tv.getText().toString().trim();
                    reason_type_tv.setText(other_tv.getText().toString().trim());
                    removeValidationError();
                    mDropdown.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;
    }

    private boolean validateFields()
    {
        String writeMessage = write_message_et.getText().toString().trim();

        if (selectCancelOrderReasonType.equalsIgnoreCase(""))
        {
            reason_error_tv.setVisibility(View.VISIBLE);
            reason_rel.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }
        else if (writeMessage.length() == 0 || writeMessage == null|| writeMessage.equalsIgnoreCase(""))
        {
            message_error_tv.setVisibility(View.VISIBLE);
            write_message_et.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }

        return true;
    }
    void removeValidationError()
    {
        reason_error_tv.setVisibility(View.GONE);
        reason_rel.setBackgroundResource(R.drawable.border_line_view);
    }
}