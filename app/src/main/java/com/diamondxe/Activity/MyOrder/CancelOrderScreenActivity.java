package com.diamondxe.Activity.MyOrder;

import static com.diamondxe.ApiCalling.ApiConstants.rupeesIcon;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Activity.HomeScreenActivity;
import com.diamondxe.Adapter.CurrencyListAdapter;
import com.diamondxe.Adapter.MyOrder.CancelOrderListAdapter;
import com.diamondxe.Adapter.MyOrder.CancelReasonOrderListAdapter;
import com.diamondxe.Adapter.MyOrder.InnerCancelOrderListAdapter;
import com.diamondxe.Adapter.MyOrder.InnerOrderListAdapter;
import com.diamondxe.Adapter.MyOrder.OrderSummaryListAdapter;
import com.diamondxe.Adapter.MyOrder.RecentOrderListAdapter;
import com.diamondxe.Adapter.MyOrder.ReturnOrderListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.AttributeDetailsModel;
import com.diamondxe.Beans.MyOrder.CancelOrderReasonListModel;
import com.diamondxe.Beans.MyOrder.InnerOrderListModel;
import com.diamondxe.Beans.MyOrder.MyOrderListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CancelOrderScreenActivity extends SuperActivity implements TwoRecyclerInterface {

    private ImageView back_img, cancel_drop_arrow_img, drop_arrow_img;
    private RecyclerView recycler_view;
    private RelativeLayout cancel_coupon_code_value_rel, cancel_wallet_apply_point_rel,cancel_rel_other_tax,cancel_rel_diamond_tax, coupon_code_value_rel, wallet_apply_point_rel,
            rel_other_tax,rel_diamond_tax, reason_rel,cancel_rel_other_main_tax, cancel_rel_diamond_main_tax, cancel_rel_total_tax;
    private TextView error_tv, cancel_order_tv, processed_order_tv, cancel_diamond_price_tv, cancel_shipping_and_handling_tv, cancel_platform_fees_tv,
            cancel_total_charges_tv,cancel_other_taxes_tv, cancel_diamond_taxes_tv, cancel_total_taxes_tv, cancel_sub_total_tv, cancel_wallet_apply_charges_tv,
            cancel_bank_charges_tv,cancel_final_amount_tv, cancel_others_txt_gst_perc_tv, cancel_diamond_txt_gst_perc_tv, coupon_code_value_tv, diamond_price_tv, shipping_and_handling_tv,platform_fees_tv, total_charges_tv,other_taxes_tv, diamond_taxes_tv,
            total_taxes_tv, sub_total_tv,wallet_apply_charges_tv, bank_charges_tv,final_amount_tv,others_txt_gst_perc_tv,diamond_txt_gst_perc_tv,
            reason_type_tv,reason_error_tv, message_error_tv, cancel_final_amount_tv1, final_amount_tv1, order_number_tv, date_time_tv,
            cancel_coupon_code_value_tv, cancel_total_taxes_tv_lbl, cancel_total_others_txt_gst_perc_tv, utr_cheque_no_tv,order_status_tv,
    amount_tv,order_place_date_tv, delivery_date_tv,payment_mode_tv,shipping_address_tv, billing_address_tv,contact_no_tv, email_tv;
    private EditText write_message_et;
    private RadioGroup radio_group;
    private RadioButton full_cancel_order_radio, partial_cancel_order_radio;
    private CardView cancel_order_card_view, processed_order_card_view, cancel_order_summary_view_card, order_summary_view_card_main;
    private LinearLayout cancel_view_order_summary_details_lin, view_order_summary_details_main_lin, order_summery_main_lin;
    private Activity activity;
    private Context context;
    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    private ArrayList<MyOrderListModel> modelArrayList;
    private ArrayList<CancelOrderReasonListModel> cancelReasonArrayList;
    CancelReasonOrderListAdapter cancelReasonOrderListAdapter;
    private CancelOrderListAdapter adapter;
    Handler handler1 = new Handler(Looper.getMainLooper());
    String selectCancelOrderReasonType="", cancelType="full";
    String refundMode = "wallet";
    String selectedCurrencyValue ="",selectedCurrencyCode = "",selectedCurrencyDesc="",selectedCurrencyImage="";
    String detailsOrderId = "", detailsCreatedAt="",selectedCertificateNumber="";
    private boolean isArrowDown = false;
    private boolean isArrowDownCancel = false;

    // Initialize a list to keep track of selected IDs
    //List<String> selectedIds = new ArrayList<>();

    StringBuilder selectedIds = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order_screen);

        context = activity = this;

        modelArrayList = new ArrayList<>();
        cancelReasonArrayList = new ArrayList<>();

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

        utr_cheque_no_tv = findViewById(R.id.utr_cheque_no_tv);
        order_status_tv = findViewById(R.id.order_status_tv);
        amount_tv = findViewById(R.id.amount_tv);
        order_place_date_tv = findViewById(R.id.order_place_date_tv);
        delivery_date_tv = findViewById(R.id.delivery_date_tv);
        payment_mode_tv = findViewById(R.id.payment_mode_tv);
        shipping_address_tv = findViewById(R.id.shipping_address_tv);
        billing_address_tv = findViewById(R.id.billing_address_tv);
        contact_no_tv = findViewById(R.id.contact_no_tv);
        email_tv = findViewById(R.id.email_tv);

        date_time_tv = findViewById(R.id.date_time_tv);
        order_number_tv = findViewById(R.id.order_number_tv);

        cancel_order_card_view = findViewById(R.id.cancel_order_card_view);
        processed_order_card_view = findViewById(R.id.processed_order_card_view);

        cancel_order_summary_view_card = findViewById(R.id.cancel_order_summary_view_card);
        cancel_order_summary_view_card.setOnClickListener(this);

        cancel_drop_arrow_img = findViewById(R.id.cancel_drop_arrow_img);
        cancel_final_amount_tv1 = findViewById(R.id.cancel_final_amount_tv1);
        cancel_view_order_summary_details_lin = findViewById(R.id.cancel_view_order_summary_details_lin);

        order_summary_view_card_main = findViewById(R.id.order_summary_view_card_main);
        order_summary_view_card_main.setOnClickListener(this);

        drop_arrow_img = findViewById(R.id.drop_arrow_img);
        final_amount_tv1 = findViewById(R.id.final_amount_tv1);
        view_order_summary_details_main_lin = findViewById(R.id.view_order_summary_details_main_lin);

        //------------------------------Cancel Order Summary ID's Start--------------------------------------------

        cancel_coupon_code_value_rel = findViewById(R.id.cancel_coupon_code_value_rel);
        cancel_wallet_apply_point_rel = findViewById(R.id.cancel_wallet_apply_point_rel);

        cancel_rel_other_tax = findViewById(R.id.cancel_rel_other_tax);
        cancel_rel_other_tax.setOnClickListener(this);

        cancel_rel_diamond_tax = findViewById(R.id.cancel_rel_diamond_tax);
        cancel_rel_diamond_tax.setOnClickListener(this);

        cancel_rel_total_tax = findViewById(R.id.cancel_rel_total_tax);
        cancel_rel_total_tax.setOnClickListener(this);
        cancel_total_others_txt_gst_perc_tv = findViewById(R.id.cancel_total_others_txt_gst_perc_tv);

        order_summery_main_lin = findViewById(R.id.order_summery_main_lin);

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
        cancel_coupon_code_value_tv = findViewById(R.id.cancel_coupon_code_value_tv);

        cancel_total_taxes_tv_lbl = findViewById(R.id.cancel_total_taxes_tv_lbl);
        cancel_total_taxes_tv_lbl.setText(getResources().getString(R.string.total_taxes));

        cancel_rel_other_main_tax = findViewById(R.id.cancel_rel_other_main_tax);
        cancel_rel_other_main_tax.setVisibility(View.GONE);
        cancel_rel_diamond_main_tax = findViewById(R.id.cancel_rel_diamond_main_tax);
        cancel_rel_diamond_main_tax.setVisibility(View.GONE);

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
                    order_summery_main_lin.setVisibility(View.VISIBLE);
                    cancelType = "full";
                    getOrderCancelListAPI(false);
                   // setFullCancelOrderAdapter();
                }
                else if (checkedId == R.id.partial_cancel_order_radio)
                {
                    order_summery_main_lin.setVisibility(View.GONE);
                    cancelType = "partial";
                    getOrderCancelListAPI(false);
                   // setPartialCancelOrderAdapter();
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setNestedScrollingEnabled(false);

        getCurrencyData();

        //setDummyData();

        cancelType = "full";
        getOrderCancelListAPI(false);
    }

    void getCurrencyData()
    {
        selectedCurrencyValue = CommonUtility.getGlobalString(context, "selected_currency_value");
        selectedCurrencyCode = CommonUtility.getGlobalString(context, "selected_currency_code");
        selectedCurrencyDesc = CommonUtility.getGlobalString(context, "selected_currency_desc");
        selectedCurrencyImage = CommonUtility.getGlobalString(context, "selected_currency_image");
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

            if(cancelType.equalsIgnoreCase("full"))
            {
                orderCancelConfirmationPopup(activity,context);
            }
            else{
                // Check if any items are selected before proceeding
                if (selectedIds.length()==0) {
                    Toast.makeText(activity, getResources().getString(R.string.select_at_least_one_diamond), Toast.LENGTH_SHORT).show();
                }
                else{
                    orderCancelConfirmationPopup(activity,context);
                }
            }
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
        else if(id == R.id.cancel_order_summary_view_card)
        {
            Utils.hideKeyboard(activity);
            if (isArrowDownCancel) {
                cancel_drop_arrow_img.setImageResource(R.drawable.down);
                cancel_view_order_summary_details_lin.setVisibility(View.GONE);
            } else {
                cancel_drop_arrow_img.setImageResource(R.drawable.up);
                cancel_view_order_summary_details_lin.setVisibility(View.VISIBLE);
            }
            isArrowDownCancel = !isArrowDownCancel;
        }
        else if(id == R.id.order_summary_view_card_main)
        {
            Utils.hideKeyboard(activity);
            if (isArrowDown) {
                drop_arrow_img.setImageResource(R.drawable.down);
                view_order_summary_details_main_lin.setVisibility(View.GONE);
            } else {
                drop_arrow_img.setImageResource(R.drawable.up);
                view_order_summary_details_main_lin.setVisibility(View.VISIBLE);
            }
            isArrowDown = !isArrowDown;
        }
        else if(id == R.id.cancel_rel_total_tax)
        {
            Utils.hideKeyboard(activity);
            cancel_total_others_txt_gst_perc_tv.setVisibility(View.VISIBLE);
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cancel_total_others_txt_gst_perc_tv.setVisibility(View.GONE);
                }
            }, 2000);
        }
    }

    public void getOrderCancelListAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("orderId", "" + Constant.orderID);
            urlParameter.put("type", "" + "full"); // Full Pass For Get List Value

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.CANCEL_ORDER_CHECKOUT, ApiConstants.CANCEL_ORDER_CHECKOUT_ID,showLoader,
                    "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void getCancelOrderAPI(boolean showLoader, String reason, String comment, String refundMode)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("orderId", "" + Constant.orderID);
            urlParameter.put("type", "" + cancelType);

            if(cancelType.equalsIgnoreCase("partial"))
            {
                urlParameter.put("diamonds", "" + selectedIds);
            } else{}

            if(!comment.equalsIgnoreCase(""))
            {
                comment = comment.replace("\n", " "); // '\n' replace
            }
            else{}

            urlParameter.put("reason", "" + reason);
            urlParameter.put("comment", "" + comment);
            urlParameter.put("refundMode", "" + refundMode);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.CANCEL_ORDER, ApiConstants.CANCEL_ORDER_ID,showLoader,
                    "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void getOrderCancelPartialAPI(boolean showLoader, String selectedCertificateNumber)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("orderId", "" + Constant.orderID);
            urlParameter.put("type", "" + "partial");
            urlParameter.put("diamonds", "" + selectedCertificateNumber); // Pass Certificate Value "," Separated.

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.CANCEL_ORDER_CHECKOUT, ApiConstants.CANCEL_ORDER_PARTIAL_CHECKOUT_ID,showLoader,
                    "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void getOrderCancelReasonAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.ORDER_CANCEL_REASON, ApiConstants.ORDER_CANCEL_REASON_ID,showLoader,
                    "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {
        try {
            Log.v("------Diamond----- : ", "--------JSONObjectCancel-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {

                case ApiConstants.CANCEL_ORDER_CHECKOUT_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        detailsOrderId = CommonUtility.checkString(jObjDetails.optString("order_id"));
                        detailsCreatedAt = CommonUtility.checkString(jObjDetails.optString("created_at"));

                        String payment_mode = CommonUtility.checkString(jObjDetails.optString("payment_mode"));
                        String transaction_id = CommonUtility.checkString(jObjDetails.optString("transaction_id"));
                        String order_status = CommonUtility.checkString(jObjDetails.optString("order_status"));
                        String delivery_date = CommonUtility.checkString(jObjDetails.optString("delivery_date"));

                        if(!detailsCreatedAt.equalsIgnoreCase(""))
                        {
                            detailsCreatedAt = CommonUtility.convertDateTimeIntoLocal(detailsCreatedAt, ApiConstants.DATE_FORMAT, "dd/MM/yyyy, hh:mm:ss a");

                        } else{}

                        order_number_tv.setText("#"+detailsOrderId);
                        date_time_tv.setText(detailsCreatedAt);

                        String sub_total = CommonUtility.checkString(jObjDetails.optString("sub_total"));
                        String tax = CommonUtility.checkString(jObjDetails.optString("tax"));
                        String shipping_charge = CommonUtility.checkString(jObjDetails.optString("shipping_charge"));
                        String platform_fee = CommonUtility.checkString(jObjDetails.optString("platform_fee"));
                        String bank_charge = CommonUtility.checkString(jObjDetails.optString("bank_charge"));
                        String total_charge = CommonUtility.checkString(jObjDetails.optString("total_charge"));
                        String total_charge_tax = CommonUtility.checkString(jObjDetails.optString("total_charge_tax"));
                        String is_coupon_applied = CommonUtility.checkString(jObjDetails.optString("is_coupon_applied"));
                        String coupon_discount = CommonUtility.checkString(jObjDetails.optString("coupon_discount"));
                        String final_amount = CommonUtility.checkString(jObjDetails.optString("final_amount"));
                        String wallet_points = CommonUtility.checkString(jObjDetails.optString("wallet_points"));
                        String total_amount = CommonUtility.checkString(jObjDetails.optString("total_amount"));
                        String total_taxes = CommonUtility.checkString(jObjDetails.optString("total_taxes"));


                        if(sub_total!=null && !sub_total.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, sub_total);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            final_amount_tv1.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                            diamond_price_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                            amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(is_coupon_applied!=null && is_coupon_applied.equalsIgnoreCase("1"))
                        {
                            coupon_code_value_rel.setVisibility(View.VISIBLE);
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, coupon_discount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            coupon_code_value_tv.setText("-" + getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(shipping_charge!=null && !shipping_charge.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, shipping_charge);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            shipping_and_handling_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(platform_fee!=null && !platform_fee.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, platform_fee);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            platform_fees_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(total_charge!=null && !total_charge.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, total_charge);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            total_charges_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(total_charge_tax!=null && !total_charge_tax.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, total_charge_tax);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            other_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(tax!=null && !tax.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, tax);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            diamond_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(total_taxes!=null && !total_taxes.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, total_taxes);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            total_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(total_amount!=null && !total_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, total_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            sub_total_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(wallet_points!=null && !wallet_points.equalsIgnoreCase("") && !wallet_points.equalsIgnoreCase("0"))
                        {
                            wallet_apply_point_rel.setVisibility(View.VISIBLE);
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, wallet_points);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            wallet_apply_charges_tv.setText("-" + getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(bank_charge!=null && !bank_charge.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, bank_charge);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            bank_charges_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(final_amount!=null && !final_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, final_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            final_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        //-------------------------------------Cancel Order Summary-------------------------------------------------
                        JSONObject jObjCancelOrderDetails = jObjDetails.optJSONObject("cancel_order_summery");

                        String cancel_sub_total = CommonUtility.checkString(jObjCancelOrderDetails.optString("sub_total"));
                        String cancel_coupon_discount = CommonUtility.checkString(jObjCancelOrderDetails.optString("coupon_discount"));
                        String cancel_shipping_charge = CommonUtility.checkString(jObjCancelOrderDetails.optString("shipping_charge"));
                        String cancel_platform_fee = CommonUtility.checkString(jObjCancelOrderDetails.optString("platform_fee"));
                        String cancel_total_charges = CommonUtility.checkString(jObjCancelOrderDetails.optString("total_charges"));
                        String cancel_tax = CommonUtility.checkString(jObjCancelOrderDetails.optString("tax"));
                        String cancel_total_charge_tax = CommonUtility.checkString(jObjCancelOrderDetails.optString("total_charge_tax"));
                        String cancel_total_taxes = CommonUtility.checkString(jObjCancelOrderDetails.optString("total_taxes"));
                        String cancel_total_amount = CommonUtility.checkString(jObjCancelOrderDetails.optString("total_amount"));
                        String cancel_bank_charge = CommonUtility.checkString(jObjCancelOrderDetails.optString("bank_charge"));
                        String cancel_wallet_points = CommonUtility.checkString(jObjCancelOrderDetails.optString("wallet_points"));
                        String cancel_final_amount = CommonUtility.checkString(jObjCancelOrderDetails.optString("final_amount"));

                        if(cancel_sub_total!=null && !cancel_sub_total.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_sub_total);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_final_amount_tv1.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                            cancel_diamond_price_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_coupon_discount!=null && !cancel_coupon_discount.equalsIgnoreCase("") && !cancel_coupon_discount.equalsIgnoreCase("0"))
                        {
                            cancel_coupon_code_value_rel.setVisibility(View.VISIBLE);
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_coupon_discount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_coupon_code_value_tv.setText("-" + getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_shipping_charge!=null && !cancel_shipping_charge.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_shipping_charge);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_shipping_and_handling_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_platform_fee!=null && !cancel_platform_fee.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_platform_fee);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_platform_fees_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_total_charges!=null && !cancel_total_charges.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_total_charges);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_total_charges_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_tax!=null && !cancel_tax.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_tax);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_diamond_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_total_taxes!=null && !cancel_total_taxes.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_total_taxes);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_total_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_total_amount!=null && !cancel_total_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_total_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_sub_total_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_wallet_points!=null && !cancel_wallet_points.equalsIgnoreCase("") && !cancel_wallet_points.equalsIgnoreCase("0"))
                        {
                            cancel_wallet_apply_point_rel.setVisibility(View.VISIBLE);
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_wallet_points);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_wallet_apply_charges_tv.setText("-" + getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_bank_charge!=null && !cancel_bank_charge.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_bank_charge);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_bank_charges_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_final_amount!=null && !cancel_final_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_final_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_final_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        //--------------------------------------Cancel Order Summary End--------------------------------------------------

                        // User Details Data Fetch
                        JSONObject jObjUserDetails = jObjDetails.optJSONObject("user_details");

                        String billing_address = CommonUtility.checkString(jObjUserDetails.optString("billing_address"));
                        String shipping_address = CommonUtility.checkString(jObjUserDetails.optString("shipping_address"));
                        String user_email = CommonUtility.checkString(jObjUserDetails.optString("user_email"));
                        String user_mobile = CommonUtility.checkString(jObjUserDetails.optString("user_mobile"));

                        utr_cheque_no_tv.setText(transaction_id);
                        order_status_tv.setText(order_status);
                        order_place_date_tv.setText(detailsCreatedAt);
                        delivery_date_tv.setText(delivery_date);
                        payment_mode_tv.setText(payment_mode);
                        shipping_address_tv.setText(shipping_address);
                        billing_address_tv.setText(billing_address);

                        if(user_mobile!=null && !user_mobile.equalsIgnoreCase(""))
                        {
                            contact_no_tv.setText(getResources().getString(R.string.contact_no) + " " +user_mobile);
                        }
                        else{}

                        if(user_email!=null && !user_email.equalsIgnoreCase(""))
                        {
                            email_tv.setText(getResources().getString(R.string.email_lbl) + " " +user_email);
                        }
                        else{}

                        JSONArray details = jObjDetails.getJSONArray("diamonds");

                        if(modelArrayList!=null && modelArrayList.size()>0)
                        {
                            modelArrayList.clear();
                        }else{}

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject jOBJNEW = details.getJSONObject(i);

                            MyOrderListModel model = new MyOrderListModel();

                            model.setOrderNumber(CommonUtility.checkString(detailsOrderId));
                            model.setOrderDateTime(CommonUtility.checkString(detailsCreatedAt));
                            model.setStockId(CommonUtility.checkString(jOBJNEW.optString("stock_id")));
                            model.setCertificateNo(CommonUtility.checkString(jOBJNEW.optString("certificate_no")));
                            model.setStockNo(CommonUtility.checkString(jOBJNEW.optString("stock_no")));
                            model.setIsReturnable(CommonUtility.checkString(jOBJNEW.optString("is_returnable")));
                            model.setDxePrefered(CommonUtility.checkString(jOBJNEW.optString("dxe_prefered")));
                            model.setCategory(CommonUtility.checkString(jOBJNEW.optString("category")));
                            model.setSubTotal(CommonUtility.checkString(jOBJNEW.optString("sub_total")));
                            model.setShowingSubTotal(CommonUtility.checkString(jOBJNEW.optString("sub_total")));
                            model.setDiamondImage(CommonUtility.checkString(jOBJNEW.optString("diamond_image")));
                            model.setCarat(CommonUtility.checkString(jOBJNEW.optString("carat")));
                            model.setColor(CommonUtility.checkString(jOBJNEW.optString("color")));
                            model.setClarity(CommonUtility.checkString(jOBJNEW.optString("clarity")));
                            model.setShape(CommonUtility.checkString(jOBJNEW.optString("shape")));
                            model.setGrowthType(CommonUtility.checkString(jOBJNEW.optString("growth_type")));
                            model.setCut(CommonUtility.checkString(jOBJNEW.optString("cut_grade")));
                            model.setPolish(CommonUtility.checkString(jOBJNEW.optString("polish")));
                            model.setSymmetry(CommonUtility.checkString(jOBJNEW.optString("symmetry")));
                            model.setFir(CommonUtility.checkString(jOBJNEW.optString("fluorescence_intensity")));
                            model.setDepth(CommonUtility.checkString(jOBJNEW.optString("depth_perc")));
                            model.setTable(CommonUtility.checkString(jOBJNEW.optString("table_perc")));
                            model.setCertificateName(CommonUtility.checkString(jOBJNEW.optString("certificate_name")));
                            model.setStatus(CommonUtility.checkString(jOBJNEW.optString("status")));
                            model.setCurrencySymbol(ApiConstants.rupeesIcon);
                            model.setChecked(false);

                            modelArrayList.add(model);
                        }

                        for (int k = 0; k <modelArrayList.size() ; k++)
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, modelArrayList.get(k).getSubTotal());
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            modelArrayList.get(k).setShowingSubTotal(subTotalFormat);
                            modelArrayList.get(k).setCurrencySymbol(getCurrencySymbol);
                        }

                        if(cancelType.equalsIgnoreCase("full"))
                        {
                            adapter = new CancelOrderListAdapter(modelArrayList,context,this, "fullOrderCancel");
                            recycler_view.setAdapter(adapter);
                        }
                        else{
                            adapter = new CancelOrderListAdapter(modelArrayList,context,this, "partialOrderCancel");
                            recycler_view.setAdapter(adapter);
                        }
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.CANCEL_ORDER_PARTIAL_CHECKOUT_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        //-------------------------------------Cancel Order Summary-------------------------------------------------
                        JSONObject jObjCancelOrderDetails = jObjDetails.optJSONObject("cancel_order_summery");

                        String cancel_sub_total = CommonUtility.checkString(jObjCancelOrderDetails.optString("sub_total"));
                        String cancel_coupon_discount = CommonUtility.checkString(jObjCancelOrderDetails.optString("coupon_discount"));
                        String cancel_shipping_charge = CommonUtility.checkString(jObjCancelOrderDetails.optString("shipping_charge"));
                        String cancel_platform_fee = CommonUtility.checkString(jObjCancelOrderDetails.optString("platform_fee"));
                        String cancel_total_charges = CommonUtility.checkString(jObjCancelOrderDetails.optString("total_charges"));
                        String cancel_tax = CommonUtility.checkString(jObjCancelOrderDetails.optString("tax"));
                        String cancel_total_charge_tax = CommonUtility.checkString(jObjCancelOrderDetails.optString("total_charge_tax"));
                        String cancel_total_taxes = CommonUtility.checkString(jObjCancelOrderDetails.optString("total_taxes"));
                        String cancel_total_amount = CommonUtility.checkString(jObjCancelOrderDetails.optString("total_amount"));
                        String cancel_bank_charge = CommonUtility.checkString(jObjCancelOrderDetails.optString("bank_charge"));
                        String cancel_wallet_points = CommonUtility.checkString(jObjCancelOrderDetails.optString("wallet_points"));
                        String cancel_final_amount = CommonUtility.checkString(jObjCancelOrderDetails.optString("final_amount"));

                        if(cancel_sub_total!=null && !cancel_sub_total.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_sub_total);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_final_amount_tv1.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                            cancel_diamond_price_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_coupon_discount!=null && !cancel_coupon_discount.equalsIgnoreCase("") && !cancel_coupon_discount.equalsIgnoreCase("0"))
                        {
                            cancel_coupon_code_value_rel.setVisibility(View.VISIBLE);
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_coupon_discount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_coupon_code_value_tv.setText("-" + getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_shipping_charge!=null && !cancel_shipping_charge.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_shipping_charge);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_shipping_and_handling_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_platform_fee!=null && !cancel_platform_fee.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_platform_fee);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_platform_fees_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_total_charges!=null && !cancel_total_charges.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_total_charges);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_total_charges_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_tax!=null && !cancel_tax.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_tax);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_diamond_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_total_taxes!=null && !cancel_total_taxes.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_total_taxes);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_total_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_total_amount!=null && !cancel_total_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_total_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_sub_total_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_wallet_points!=null && !cancel_wallet_points.equalsIgnoreCase("") && !cancel_wallet_points.equalsIgnoreCase("0"))
                        {
                            cancel_wallet_apply_point_rel.setVisibility(View.VISIBLE);
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_wallet_points);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_wallet_apply_charges_tv.setText("-" + getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_bank_charge!=null && !cancel_bank_charge.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_bank_charge);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_bank_charges_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(cancel_final_amount!=null && !cancel_final_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cancel_final_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_final_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        //--------------------------------------Cancel Order Summary End--------------------------------------------------
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.ORDER_CANCEL_REASON_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if(cancelReasonArrayList!=null && cancelReasonArrayList.size()>0)
                        {
                            cancelReasonArrayList.clear();
                        }else{}

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            CancelOrderReasonListModel model = new CancelOrderReasonListModel();
                            model.setId(CommonUtility.checkString(objectCodes.optString("id")));
                            model.setReason(CommonUtility.checkString(objectCodes.optString("reason")));
                            cancelReasonArrayList.add(model);
                        }
                        reasonTypePopupWindow();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.CANCEL_ORDER_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Log.v("------Diamond----- : ", "--------JSONObjectCancelOrderId-------- : " + jsonObjectData);
                       // JSONArray details = jsonObjectData.getJSONArray("details");

                        orderCancelSuccessfullyPopup(activity, context);
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getErrorResponce(String error, int service_ID) {

    }

    @Override
    public void itemClick(int parantPosition, int chiledPosition, String action)
    {
        if(action.equalsIgnoreCase("orderCancelPartialCheck"))
        {
            /*MyOrderListModel item = modelArrayList.get(parantPosition);
            MyOrderListModel item = modelArrayList.get(parantPosition);
            item.setChecked(!item.isChecked());

            // Notify the adapter about the change in item state
            adapter.notifyItemChanged(parantPosition);
            adapter.notifyDataSetChanged();*/


            MyOrderListModel item = modelArrayList.get(parantPosition);
            modelArrayList.get(parantPosition).setChecked(!modelArrayList.get(parantPosition).isChecked());
            adapter.notifyDataSetChanged();

            Log.e("selectedIds", "Item ID: " + item.getCertificateNo() + " isChecked: " + item.isChecked());

            // Update selected IDs list
            updateSelectedIds(item);

            Log.e("selectedIds : ", "selectedIds : " + selectedIds.toString());



            /*for (int i = 0; i < modelArrayList.size(); i++)
            {
                // Check if the current color type is selected
                if(modelArrayList.get(i).isChecked())
                {
                    if (selectedCertificateNumber.equalsIgnoreCase("")) {
                        selectedCertificateNumber = modelArrayList.get(i).getCertificateNo();
                    } else {
                        // Append the attribute code for subsequent selected items
                        selectedCertificateNumber = selectedCertificateNumber + "," + modelArrayList.get(i).getCertificateNo();
                    }
                    getOrderCancelPartialAPI(false, selectedCertificateNumber);
                }
                else {
                    if(modelArrayList.get(i).isChecked())
                    {
                        if (selectedCertificateNumber.equalsIgnoreCase("")) {
                            selectedCertificateNumber = modelArrayList.get(i).getCertificateNo();
                        } else {
                            // Append the attribute code for subsequent selected items
                            selectedCertificateNumber = selectedCertificateNumber + "," + modelArrayList.get(i).getCertificateNo();
                        }
                        getOrderCancelPartialAPI(false, selectedCertificateNumber);
                    }
                }
            }*/

        }
        else if(action.equalsIgnoreCase("reason"))
        {
            reason_type_tv.setText(cancelReasonArrayList.get(parantPosition).getReason());
            selectCancelOrderReasonType = cancelReasonArrayList.get(parantPosition).getReason();
            mDropdown.dismiss();
            removeValidationError();
        }
    }

    private void updateSelectedIds(MyOrderListModel item) {
        String certificateNo = item.getCertificateNo();

        boolean isLastItem = false;

        if (item.isChecked()) {
            // If the item is selected, add its ID
            if (selectedIds.length() > 0) {
                selectedIds.append(","); // Add a comma if there's already a value
            }
            selectedIds.append(certificateNo);
            Log.e("selectedIds", "Added ID: " + certificateNo);
        } else {
            // If the item is unselected, check if it's the last selected item
            isLastItem = selectedIds.toString().equals(certificateNo);

            Log.e("isLastItem : ", "isLastItem : " + isLastItem);
            if (isLastItem) {
                // Make API call with the current ID before removing it
                getOrderCancelPartialAPI(false, certificateNo);
                selectedIds.setLength(0); // Clear the StringBuilder
                order_summery_main_lin.setVisibility(View.GONE);
            }
            else
            {
                // Remove the ID from the StringBuilder
                String[] ids = selectedIds.toString().split(",");
                selectedIds.setLength(0); // Clear StringBuilder
                for (String id : ids) {
                    if (!id.equals(certificateNo)) {
                        if (selectedIds.length() > 0) {
                            selectedIds.append(",");
                        }
                        selectedIds.append(id);
                    }
                }
                Log.e("selectedIds", "Removed ID: " + certificateNo);
            }

        }
        Log.e("selectedIds", "Removed ID1: " + certificateNo);

        if(isLastItem)
        {
            order_summery_main_lin.setVisibility(View.GONE);
        }else{
            // Make API call with the current selected IDs
            getOrderCancelPartialAPI(false, selectedIds.toString());
            order_summery_main_lin.setVisibility(View.VISIBLE);
        }
        // Log the current state of selected IDs
        Log.e("selectedIds : ", "selectedIds111 : " + selectedIds.toString());
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
                    refundMode = "wallet";
                } else if (checkedId == R.id.payment_mode_radio)
                {
                    refundMode = "source";
                }
            }
        });

        reason_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getOrderCancelReasonAPI(false);

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

                    getCancelOrderAPI(false, selectCancelOrderReasonType, write_message_et.getText().toString().trim(), refundMode);

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
            public void onClick(View view)
            {
                afterCancelManageRedirection();
                alertDialog.dismiss();
            }
        });

        done_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                afterCancelManageRedirection();
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog.setCancelable(false); // Disable touch outside to dismiss
        alertDialog.setCanceledOnTouchOutside(false);

        // Prevent back button from dismissing the dialog
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    // Do nothing, effectively ignoring the back button
                    return true; // Returning true indicates that the event is consumed
                }
                return false; // Other key events are not consumed
            }
        });

        alertDialog.show();

    }

    void afterCancelManageRedirection()
    {
        Constant.afterCancelOrderManageScreenCall = "yes";
        Intent intent = new Intent(activity, MyOrderListScreenActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    private PopupWindow mDropdown = null;
    LayoutInflater mInflater;
    // Button pop;
    private TextView select_mode_lbl;
    private RecyclerView recycler_view_reason;
    private PopupWindow reasonTypePopupWindow() {
        try {
            if (mDropdown != null && mDropdown.isShowing()) {
                mDropdown.dismiss();
            }
            mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.custom_menu_reason_type, null);

            select_mode_lbl = layout.findViewById(R.id.select_mode_lbl);
            recycler_view_reason = layout.findViewById(R.id.recycler_view_reason);
            recycler_view_reason.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
            recycler_view_reason.setLayoutManager(layoutManager);
            recycler_view_reason.setNestedScrollingEnabled(false);

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

            setAdapter();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;
    }

    void setAdapter()
    {
        cancelReasonOrderListAdapter = new CancelReasonOrderListAdapter(cancelReasonArrayList,context,this);
        recycler_view_reason.setAdapter(cancelReasonOrderListAdapter);
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