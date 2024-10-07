package com.diamondxe.Activity.MyOrder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Adapter.MyOrder.CancelReasonOrderListAdapter;
import com.diamondxe.Adapter.MyOrder.ReturnItemOrderListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.MyOrder.CancelOrderReasonListModel;
import com.diamondxe.Beans.MyOrder.MyOrderListModel;
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

public class ReturnOrderScreenActivity extends SuperActivity implements TwoRecyclerInterface {

    private ImageView back_img,  return_drop_arrow_img, drop_arrow_img;
    private CardView return_product_card_view, return_reason_card_view, order_review_card_view,return_order_summary_view_card;
    private RecyclerView recycler_view;
    private RelativeLayout coupon_code_value_rel, wallet_apply_point_rel, rel_other_tax,rel_diamond_tax, reason_rel,return_wallet_apply_point_rel,
            returnable_view_layout;
    private TextView error_tv, cancel_order_tv, next_tv,  coupon_code_value_tv, diamond_price_tv, shipping_and_handling_tv,platform_fees_tv, total_charges_tv,other_taxes_tv, diamond_taxes_tv,
            total_taxes_tv, sub_total_tv,wallet_apply_charges_tv, bank_charges_tv,final_amount_tv,others_txt_gst_perc_tv,diamond_txt_gst_perc_tv,
            final_amount_tv1, order_number_tv, date_time_tv, utr_cheque_no_tv,order_status_tv,
            amount_tv,order_place_date_tv, delivery_date_tv,payment_mode_tv,shipping_address_tv, billing_address_tv,contact_no_tv, email_tv,
            return_diamond_price_tv, return_sub_total_tv,  return_wallet_apply_charges_tv, return_final_amount_tv, return_final_amount_tv1,returnable_date_tv,view_policy_tv;
    private RadioGroup radio_group;
    private RadioButton full_return_order_radio, partial_return_order_radio;
    private CardView cancel_order_card_view, processed_order_card_view,  order_summary_view_card_main;
    private LinearLayout view_order_summary_details_main_lin, order_summery_main_lin, return_view_order_summary_details_lin;
    private Activity activity;
    private Context context;
    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    private ArrayList<MyOrderListModel> modelArrayList;
    private ArrayList<CancelOrderReasonListModel> cancelReasonArrayList;
    CancelReasonOrderListAdapter cancelReasonOrderListAdapter;
    private ReturnItemOrderListAdapter adapter;
    Handler handler1 = new Handler(Looper.getMainLooper());
    String selectCancelOrderReasonType="", returnType="full";
    String FULL_RETURN = "full";
    String PARTIAL_RETURN = "partial";
    String selectedCurrencyValue ="",selectedCurrencyCode = "",selectedCurrencyDesc="",selectedCurrencyImage="";
    String detailsOrderId = "", detailsCreatedAt="";
    private boolean isArrowDown = false;
    private boolean isArrowDownReturn = false;
    StringBuilder selectedIds = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_order_screen);

        context = activity = this;

        modelArrayList = new ArrayList<>();
        cancelReasonArrayList = new ArrayList<>();

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);


        return_product_card_view = findViewById(R.id.return_product_card_view);
        return_product_card_view.setOnClickListener(this);

        return_reason_card_view = findViewById(R.id.return_reason_card_view);
        return_reason_card_view.setOnClickListener(this);

        order_review_card_view = findViewById(R.id.order_review_card_view);
        order_review_card_view.setOnClickListener(this);

        radio_group = findViewById(R.id.radio_group);
        full_return_order_radio = findViewById(R.id.full_return_order_radio);
        partial_return_order_radio = findViewById(R.id.partial_return_order_radio);

        cancel_order_tv = findViewById(R.id.cancel_order_tv);
        cancel_order_tv.setOnClickListener(this);

        next_tv = findViewById(R.id.next_tv);
        next_tv.setOnClickListener(this);

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

        order_summary_view_card_main = findViewById(R.id.order_summary_view_card_main);
        order_summary_view_card_main.setOnClickListener(this);

        drop_arrow_img = findViewById(R.id.drop_arrow_img);
        final_amount_tv1 = findViewById(R.id.final_amount_tv1);
        view_order_summary_details_main_lin = findViewById(R.id.view_order_summary_details_main_lin);

        returnable_view_layout = findViewById(R.id.returnable_view_layout);
        returnable_date_tv = findViewById(R.id.returnable_date_tv);
        view_policy_tv = findViewById(R.id.view_policy_tv);

        //------------------------------My Order Summary ID'S Start----------------------------------------------

        order_summery_main_lin = findViewById(R.id.order_summery_main_lin);

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

        //----------------------Return Order Summary ID's Start-----------------------------------------------

        return_order_summary_view_card = findViewById(R.id.return_order_summary_view_card);
        return_order_summary_view_card.setOnClickListener(this);

        return_drop_arrow_img = findViewById(R.id.return_drop_arrow_img);
        return_diamond_price_tv = findViewById(R.id.return_diamond_price_tv);
        return_sub_total_tv = findViewById(R.id.return_sub_total_tv);
        return_wallet_apply_point_rel = findViewById(R.id.return_wallet_apply_point_rel);
        return_wallet_apply_charges_tv = findViewById(R.id.return_wallet_apply_charges_tv);

        return_final_amount_tv = findViewById(R.id.return_final_amount_tv);
        return_final_amount_tv1 = findViewById(R.id.return_final_amount_tv1);

        return_view_order_summary_details_lin = findViewById(R.id.return_view_order_summary_details_lin);

        //----------------------Return Order Summary ID's End-------------------------------------------------


        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.full_return_order_radio)
                {
                    order_summery_main_lin.setVisibility(View.VISIBLE);
                    returnType = FULL_RETURN;
                    getOrderCancelListAPI(false);
                    // setFullCancelOrderAdapter();
                }
                else if (checkedId == R.id.partial_return_order_radio)
                {
                    order_summery_main_lin.setVisibility(View.GONE);
                    returnType = PARTIAL_RETURN;
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

        returnType = FULL_RETURN;
        getOrderCancelListAPI(false);
    }

    void getCurrencyData()
    {
        selectedCurrencyValue = CommonUtility.getGlobalString(context, "selected_currency_value");
        selectedCurrencyCode = CommonUtility.getGlobalString(context, "selected_currency_code");
        selectedCurrencyDesc = CommonUtility.getGlobalString(context, "selected_currency_desc");
        selectedCurrencyImage = CommonUtility.getGlobalString(context, "selected_currency_image");
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
        else if(id == R.id.return_product_card_view)
        {
            Utils.hideKeyboard(activity);

        }
        else if(id == R.id.return_reason_card_view)
        {
            Utils.hideKeyboard(activity);
            selectCancelOrderReasonType="";
            moveToReturnOrderItemImageVideoScreen();
        }
        else if(id == R.id.order_review_card_view)
        {
            Utils.hideKeyboard(activity);
            Toast.makeText(activity, getResources().getString(R.string.complete_return_reason_order_step), Toast.LENGTH_SHORT).show();
            //moveToReturnOrderItemReviewScreen();
        }
        else if(id == R.id.cancel_order_tv)
        {
            Utils.hideKeyboard(activity);
            finish();
        }
        else if(id == R.id.next_tv)
        {
            Utils.hideKeyboard(activity);
            selectCancelOrderReasonType="";
            moveToReturnOrderItemImageVideoScreen();
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
        else if(id == R.id.return_order_summary_view_card)
        {
            Utils.hideKeyboard(activity);
            if (isArrowDownReturn) {
                return_drop_arrow_img.setImageResource(R.drawable.down);
                return_view_order_summary_details_lin.setVisibility(View.GONE);
            } else {
                return_drop_arrow_img.setImageResource(R.drawable.up);
                return_view_order_summary_details_lin.setVisibility(View.VISIBLE);
            }
            isArrowDownReturn = !isArrowDownReturn;
        }
        else if(id == R.id.view_policy_tv)
        {
            Utils.hideKeyboard(activity);
        }
    }

    void moveToReturnOrderItemImageVideoScreen()
    {
        if(returnType.equalsIgnoreCase(FULL_RETURN))
        {
            Constant.orderReturnType = FULL_RETURN;
            Intent intent = new Intent(activity, ReturnOrderItemsImageVideoActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        }
        else{
            // Check if any items are selected before proceeding
            if (selectedIds.length()==0) {
                Toast.makeText(activity, getResources().getString(R.string.select_at_least_one_diamond), Toast.LENGTH_SHORT).show();
            }
            else{
                Constant.orderReturnType = PARTIAL_RETURN;
                Intent intent = new Intent(activity, ReturnOrderItemsImageVideoActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        }
    }

    void moveToReturnOrderItemReviewScreen()
    {
        if(returnType.equalsIgnoreCase(FULL_RETURN))
        {
            Constant.orderReturnType = FULL_RETURN;
            Intent intent = new Intent(activity, ReturnOrderPickupAddressWalletScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        }
        else{
            // Check if any items are selected before proceeding
            if (selectedIds.length()==0) {
                Toast.makeText(activity, getResources().getString(R.string.select_at_least_one_diamond), Toast.LENGTH_SHORT).show();
            }
            else{
                Constant.orderReturnType = PARTIAL_RETURN;
                Intent intent = new Intent(activity, ReturnOrderPickupAddressWalletScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
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
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.RETURN_ORDER_CHECKOUT, ApiConstants.RETURN_ORDER_CHECKOUT_ID,
                    showLoader,
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
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.RETURN_ORDER_CHECKOUT, ApiConstants.RETURN_ORDER_PARTIAL_CHECKOUT_ID,showLoader,
                    "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {
        try {
            Log.v("------Diamond----- : ", "--------JSONObjectReturn-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {

                case ApiConstants.RETURN_ORDER_CHECKOUT_ID:

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

                        order_number_tv.setText("#" + detailsOrderId);
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

                        //-------------------------------------Return Order Summary-------------------------------------------------
                        JSONObject jObjCancelOrderDetails = jObjDetails.optJSONObject("return_order_summery");

                        String return_sub_total = CommonUtility.checkString(jObjCancelOrderDetails.optString("sub_total"));
                        String return_coupon_discount = CommonUtility.checkString(jObjCancelOrderDetails.optString("coupon_discount"));
                        String return_total_amount = CommonUtility.checkString(jObjCancelOrderDetails.optString("total_amount"));
                        String return_wallet_points = CommonUtility.checkString(jObjCancelOrderDetails.optString("wallet_points"));
                        String return_final_amount = CommonUtility.checkString(jObjCancelOrderDetails.optString("final_amount"));

                        if(return_sub_total!=null && !return_sub_total.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_sub_total);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            return_final_amount_tv1.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                            return_diamond_price_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        /*if(return_coupon_discount!=null && !return_coupon_discount.equalsIgnoreCase("") && !return_coupon_discount.equalsIgnoreCase("0"))
                        {
                            cancel_coupon_code_value_rel.setVisibility(View.VISIBLE);
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_coupon_discount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_coupon_code_value_tv.setText("-" + getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}*/

                        if(return_total_amount!=null && !return_total_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_total_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            return_sub_total_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(return_wallet_points!=null && !return_wallet_points.equalsIgnoreCase("") && !return_wallet_points.equalsIgnoreCase("0"))
                        {
                            return_wallet_apply_point_rel.setVisibility(View.VISIBLE);
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_wallet_points);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            return_wallet_apply_charges_tv.setText("-" + getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(return_final_amount!=null && !return_final_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_final_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            return_final_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        // User Details Data Fetch
                        JSONObject jObjUserDetails = jObjDetails.optJSONObject("user_details");

                        String billing_address = CommonUtility.checkString(jObjUserDetails.optString("billing_address"));
                        String shipping_address = CommonUtility.checkString(jObjUserDetails.optString("shipping_address"));
                        String user_email = CommonUtility.checkString(jObjUserDetails.optString("user_email"));
                        String user_mobile = CommonUtility.checkString(jObjUserDetails.optString("user_mobile"));

                        utr_cheque_no_tv.setText(transaction_id);

                        if(order_status.equalsIgnoreCase("Delivered") || order_status.equalsIgnoreCase("delivered"))
                        {
                            order_status_tv.setTextColor(ContextCompat.getColor(context, R.color.green));
                        }
                        else{
                            order_status_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                        }
                        order_status_tv.setText(order_status);

                        order_place_date_tv.setText(detailsCreatedAt);
                        delivery_date_tv.setText(delivery_date);
                        payment_mode_tv.setText(payment_mode);
                        shipping_address_tv.setText(shipping_address);
                        billing_address_tv.setText(billing_address);
                        contact_no_tv.setText(getResources().getString(R.string.contact_no) + " " +user_mobile);
                        email_tv.setText(getResources().getString(R.string.email_lbl) + " " +user_email);


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

                        if(returnType.equalsIgnoreCase(FULL_RETURN))
                        {
                            adapter = new ReturnItemOrderListAdapter(modelArrayList,context,this, "fullOrderReturn");
                            recycler_view.setAdapter(adapter);
                        }
                        else{
                            adapter = new ReturnItemOrderListAdapter(modelArrayList,context,this, "partialOrderReturn");
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

                case ApiConstants.RETURN_ORDER_PARTIAL_CHECKOUT_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        //-------------------------------------Return Order Summary-------------------------------------------------
                        JSONObject jObjCancelOrderDetails = jObjDetails.optJSONObject("return_order_summery");

                        String return_sub_total = CommonUtility.checkString(jObjCancelOrderDetails.optString("sub_total"));
                        String return_coupon_discount = CommonUtility.checkString(jObjCancelOrderDetails.optString("coupon_discount"));
                        String return_total_amount = CommonUtility.checkString(jObjCancelOrderDetails.optString("total_amount"));
                        String return_wallet_points = CommonUtility.checkString(jObjCancelOrderDetails.optString("wallet_points"));
                        String return_final_amount = CommonUtility.checkString(jObjCancelOrderDetails.optString("final_amount"));

                        if(return_sub_total!=null && !return_sub_total.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_sub_total);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            return_final_amount_tv1.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                            return_diamond_price_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        /*if(return_coupon_discount!=null && !return_coupon_discount.equalsIgnoreCase("") && !return_coupon_discount.equalsIgnoreCase("0"))
                        {
                            cancel_coupon_code_value_rel.setVisibility(View.VISIBLE);
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_coupon_discount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_coupon_code_value_tv.setText("-" + getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}*/

                        if(return_total_amount!=null && !return_total_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_total_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            return_sub_total_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(return_wallet_points!=null && !return_wallet_points.equalsIgnoreCase("") && !return_wallet_points.equalsIgnoreCase("0"))
                        {
                            return_wallet_apply_point_rel.setVisibility(View.VISIBLE);
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_wallet_points);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            return_wallet_apply_charges_tv.setText("-" + getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(return_final_amount!=null && !return_final_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_final_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            return_final_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        //--------------------------------------Return Order Summary End--------------------------------------------------
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

        if(action.equalsIgnoreCase("orderReturnPartialCheck"))
        {
            MyOrderListModel item = modelArrayList.get(parantPosition);
            modelArrayList.get(parantPosition).setChecked(!modelArrayList.get(parantPosition).isChecked());
            adapter.notifyDataSetChanged();

            Log.e("selectedIds", "Item ID: " + item.getCertificateNo() + " isChecked: " + item.isChecked());

            // Update selected IDs list
            updateSelectedIds(item);

            Log.e("selectedIds : ", "selectedIds : " + selectedIds.toString());
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
                Constant.returnOrderCertificateIDs = certificateNo; // Store Certificate Number to Show How Many Items Return and Manage Image Video Screen UI
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
            Constant.returnOrderCertificateIDs = selectedIds.toString(); // Store Certificate Number to Show How Many Items Return and Manage Image Video Screen UI
            getOrderCancelPartialAPI(false, selectedIds.toString());
            order_summery_main_lin.setVisibility(View.VISIBLE);
        }
        // Log the current state of selected IDs
        Log.e("selectedIds : ", "selectedIds111 : " + selectedIds.toString());
    }
}