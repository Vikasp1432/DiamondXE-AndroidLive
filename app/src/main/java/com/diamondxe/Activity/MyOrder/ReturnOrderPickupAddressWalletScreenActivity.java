package com.diamondxe.Activity.MyOrder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Activity.AddShippingAddressActivity;
import com.diamondxe.Activity.TransparentActivity;
import com.diamondxe.Adapter.MyOrder.ReturnOrderItemImageVideoListAdapter;
import com.diamondxe.Adapter.MyOrder.ReturnOrderPickupAddressListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.AddressListModel;
import com.diamondxe.Beans.MyOrder.MyOrderListModel;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.InputStreamRequestBody;
import com.diamondxe.Utils.MultipartUtility;
import com.diamondxe.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import android.Manifest;

public class ReturnOrderPickupAddressWalletScreenActivity extends SuperActivity implements TwoRecyclerInterface {

    private NestedScrollView nested_scrollable_view;
    private ImageView back_img,  return_drop_arrow_img, drop_arrow_img;
    private RecyclerView shipping_address_recycler_view;
    ArrayList<AddressListModel> shippingAddressArrayList;
    ReturnOrderPickupAddressListAdapter addressListAdapter;
    private CardView return_product_card_view, return_reason_card_view, order_review_card_view,return_order_summary_view_card,no_shipping_address_card;
    private RelativeLayout coupon_code_value_rel, wallet_apply_point_rel, rel_other_tax,rel_diamond_tax, return_wallet_apply_point_rel;
    private TextView  previous_tv, confirm_tv,  coupon_code_value_tv, diamond_price_tv, shipping_and_handling_tv,platform_fees_tv, total_charges_tv,other_taxes_tv, diamond_taxes_tv,
            total_taxes_tv, sub_total_tv,wallet_apply_charges_tv, bank_charges_tv,final_amount_tv,others_txt_gst_perc_tv,diamond_txt_gst_perc_tv,
            final_amount_tv1, order_number_tv, date_time_tv, utr_cheque_no_tv,order_status_tv,
            amount_tv,order_place_date_tv, delivery_date_tv,payment_mode_tv,shipping_address_tv, billing_address_tv,contact_no_tv, email_tv,
            return_diamond_price_tv, return_sub_total_tv,  return_wallet_apply_charges_tv, return_final_amount_tv, return_final_amount_tv1,
            add_shipping_address_tv, wallet_msg_tv, payment_mode_msg_tv, check_terms_condition_tv;
    private CheckBox item_sealed_checkbox;
    private LinearLayout wallet_payment_layout_lin, original_payment_mode_lin;
    private RadioButton wallet_radio, original_mode_radio;
    private CardView cancel_order_card_view, processed_order_card_view,  order_summary_view_card_main;
    private LinearLayout view_order_summary_details_main_lin, order_summery_main_lin, return_view_order_summary_details_lin;
    private Activity activity;
    private Context context;
    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    Handler handler1 = new Handler(Looper.getMainLooper());
    String selectedCurrencyValue ="",selectedCurrencyCode = "",selectedCurrencyDesc="",selectedCurrencyImage="";
    String detailsOrderId = "", detailsCreatedAt="",wheretoRemove="", shippingAddressID="", itemSealedCheck="";
    private boolean isArrowDown = false;
    private boolean isArrowDownReturn = false;
    StringBuilder selectedIds = new StringBuilder();
    int newWith;
    int width;
    boolean isSelectShippingAddress = false;
    int lastPosition = 0;
    private static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_order_pickup_address_wallet_screen);

        context = activity = this;

        shippingAddressArrayList = new ArrayList<>();

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        nested_scrollable_view = findViewById(R.id.nested_scrollable_view);

        return_product_card_view = findViewById(R.id.return_product_card_view);
        return_product_card_view.setOnClickListener(this);

        return_reason_card_view = findViewById(R.id.return_reason_card_view);
        return_reason_card_view.setOnClickListener(this);

        order_review_card_view = findViewById(R.id.order_review_card_view);
        order_review_card_view.setOnClickListener(this);

        previous_tv = findViewById(R.id.previous_tv);
        previous_tv.setOnClickListener(this);

        confirm_tv = findViewById(R.id.confirm_tv);
        confirm_tv.setOnClickListener(this);

        wallet_payment_layout_lin = findViewById(R.id.wallet_payment_layout_lin);
        original_payment_mode_lin = findViewById(R.id.original_payment_mode_lin);

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

        //----------------------------------------Pickup Address-------------------------------------------------------

        //For calculate Screen width for manage RecyclerView's card's width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        shipping_address_recycler_view = findViewById(R.id.shipping_address_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        shipping_address_recycler_view.setLayoutManager(layoutManager);
        shipping_address_recycler_view.setNestedScrollingEnabled(false);

        no_shipping_address_card = findViewById(R.id.no_shipping_address_card);

        add_shipping_address_tv = findViewById(R.id.add_shipping_address_tv);
        add_shipping_address_tv.setOnClickListener(this);

        wallet_radio = findViewById(R.id.wallet_radio);
        original_mode_radio = findViewById(R.id.original_mode_radio);

        wallet_msg_tv = findViewById(R.id.wallet_msg_tv);
        payment_mode_msg_tv = findViewById(R.id.payment_mode_msg_tv);

        newWith = (int) (width/1.2); // For Pickup Address Layout.

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

        item_sealed_checkbox = findViewById(R.id.item_sealed_checkbox);
        check_terms_condition_tv = findViewById(R.id.check_terms_condition_tv);

        item_sealed_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item_sealed_checkbox.isChecked()) {
                    itemSealedCheck = "yes";
                    check_terms_condition_tv.setVisibility(View.GONE);
                } else {
                    itemSealedCheck = "no";
                }
            }
        });

        //----------------------Return Order Summary ID's End-------------------------------------------------

        Log.e("Constant.refundPaymentMode : ", Constant.refundPaymentMode.toString());
        if(Constant.refundPaymentMode.equalsIgnoreCase("Wallet"))
        {
            wallet_payment_layout_lin.setVisibility(View.VISIBLE);
            original_payment_mode_lin.setVisibility(View.GONE);
            wallet_radio.setChecked(true);
        }
        else{
            wallet_payment_layout_lin.setVisibility(View.GONE);
            original_payment_mode_lin.setVisibility(View.VISIBLE);
            original_mode_radio.setChecked(true);
        }
        getCurrencyData();

        getOrderCancelPartialAPI(true);
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
            intent = new Intent(activity, ReturnOrderScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        }
        else if(id == R.id.return_reason_card_view)
        {
            Utils.hideKeyboard(activity);
            moveToPreviousScreen();
        }
        else if(id == R.id.order_review_card_view)
        {
            Utils.hideKeyboard(activity);
        }
        else if(id == R.id.previous_tv)
        {
            Utils.hideKeyboard(activity);
            moveToPreviousScreen();
        }
        else if(id == R.id.confirm_tv)
        {
            Utils.hideKeyboard(activity);
            if(validateFields())
            {
                boolean isShippingAddressValid = validateShippingAddress();
                // Check Address Selection Validation
                if (isShippingAddressValid) {
                }else{}

                if (isShippingAddressValid)
                {
                    // Checkd Item Sealed Condition Or Not
                    if(itemSealedCheck.equalsIgnoreCase("yes"))
                    {
                        ArrayList<MyOrderListModel> updatedList = Constant.finalReturnOrderItemArrayList;
                        SendReturnItemAsyncTask asyncTask = new SendReturnItemAsyncTask(context, updatedList);
                        asyncTask.executeNetworkCall();
                    }
                    else{
                        nested_scrollable_view.fullScroll(View.FOCUS_DOWN);
                        //Toast.makeText(activity, getResources().getString(R.string.please_check_term_condition), Toast.LENGTH_SHORT).show();
                        check_terms_condition_tv.setVisibility(View.VISIBLE);
                        check_terms_condition_tv.requestFocus();
                    }
                    //checkPermissionsAndExecuteTask(updatedList);

                }
            }else{}
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
    }

    void moveToPreviousScreen()
    {
        Intent intent = new Intent(activity, ReturnOrderItemsImageVideoActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    //  Not Use This Code
    private void checkPermissionsAndExecuteTask(ArrayList<MyOrderListModel> updatedList) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE);
        } else {
            SendReturnItemAsyncTask asyncTask = new SendReturnItemAsyncTask(context, updatedList);
            asyncTask.executeNetworkCall();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getShippingAddressListAPI(false);
    }

    public void getOrderCancelPartialAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("orderId", "" + Constant.orderID);

            if(Constant.orderReturnType.equalsIgnoreCase("partial"))
            {
                urlParameter.put("type", "" + "partial");
                urlParameter.put("diamonds", "" + Constant.returnOrderCertificateIDs); // Pass Certificate Value "," Separated.
            }
            else{
                urlParameter.put("type", "" + "full");
                urlParameter.put("diamonds", "" + "");
            }

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.RETURN_ORDER_CHECKOUT, ApiConstants.RETURN_ORDER_PARTIAL_CHECKOUT_ID,showLoader,
                    "POST");
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void getShippingAddressListAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_ADDRESS_SHIPPING, ApiConstants.GET_ADDRESS_SHIPPING_ID,showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void getRemoveAddressAPI(boolean showLoader, String addressID)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("addressId", ""+ addressID);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.REMOVE_ADDRESS, ApiConstants.REMOVE_ADDRESS_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {
        try {
            Log.v("------Diamond----- : ", "--------JSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {

                case ApiConstants.RETURN_ORDER_PARTIAL_CHECKOUT_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Log.v("------Diamond----- : ", "--------JSONObjectReturn-------- : " + jsonObjectData);

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

                        /*order_number_tv.setText("#" + detailsOrderId);
                        date_time_tv.setText(detailsCreatedAt);*/

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

                            String showingAmount = getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat);

                            wallet_msg_tv.setText(getResources().getString(R.string.wallet_msg) + " " + showingAmount + " " + getResources().getString(R.string.wallet_msg1));
                            payment_mode_msg_tv.setText(getResources().getString(R.string.payment_mode_msg) + " " +  showingAmount + " "+ getResources().getString(R.string.payment_mode_msg1)
                                    + " " +  showingAmount + " " + getResources().getString(R.string.payment_mode_msg2));

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

                case ApiConstants.GET_ADDRESS_SHIPPING_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if(shippingAddressArrayList.size() > 0)
                        {
                            shippingAddressArrayList.clear();
                        } else{}

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            AddressListModel model = new AddressListModel();

                            model.setAddressId(CommonUtility.checkString(objectCodes.optString("AddressId")));
                            model.setAddressTypeId(CommonUtility.checkString(objectCodes.optString("AddressTypeId")));
                            model.setAddressType(CommonUtility.checkString(objectCodes.optString("AddressType")));
                            model.setAddress1(CommonUtility.checkString(objectCodes.optString("Address1")));
                            model.setAddress2(CommonUtility.checkString(objectCodes.optString("Address2")));
                            model.setCityNameS(CommonUtility.checkString(objectCodes.optString("CityNameS")));
                            model.setCityName(CommonUtility.checkString(objectCodes.optString("CityName")));
                            model.setStateNameS(CommonUtility.checkString(objectCodes.optString("StateNameS")));
                            model.setStateName(CommonUtility.checkString(objectCodes.optString("StateName")));
                            model.setCountryNameS(CommonUtility.checkString(objectCodes.optString("CountryNameS")));
                            model.setCountryName(CommonUtility.checkString(objectCodes.optString("CountryName")));
                            model.setPinCode(CommonUtility.checkString(objectCodes.optString("PinCode")));
                            model.setIsDefault(CommonUtility.checkString(objectCodes.optString("IsDefault")));
                            model.setEmailId(CommonUtility.checkString(objectCodes.optString("EmailId")));
                            model.setMobileNo(CommonUtility.checkString(objectCodes.optString("MobileNo")));
                            model.setgSTNo(CommonUtility.checkString(objectCodes.optString("GSTNo")));
                            model.setBusinessName(CommonUtility.checkString(objectCodes.optString("BusinessName")));
                            model.setMobileDialCode(CommonUtility.checkString(objectCodes.optString("MobileDialCode")));
                            model.setMobileNumber(CommonUtility.checkString(objectCodes.optString("MobileNumber")));

                            // Check IS Default Address Set Selected For Select Radio Button
                            if(objectCodes.optString("IsDefault").equalsIgnoreCase("1"))
                            {
                                model.setSelected(true);
                                isSelectShippingAddress = true;
                                Constant.deliveryPincode = CommonUtility.checkString(objectCodes.optString("PinCode"));
                                Constant.shippingAddressID = CommonUtility.checkString(objectCodes.optString("AddressId"));
                                Constant.pickupAddressId = CommonUtility.checkString(objectCodes.optString("AddressId"));
                                Constant.shippingAddressNameForShowHidePaymentOption = CommonUtility.checkString(objectCodes.optString("CountryNameS"));
                            }
                            else{
                                model.setSelected(false);
                            }
                            shippingAddressArrayList.add(model);
                        }

                        if(shippingAddressArrayList.size()>0)
                        {
                            no_shipping_address_card.setVisibility(View.GONE);
                            shipping_address_recycler_view.setVisibility(View.VISIBLE);
                        }
                        else{
                            no_shipping_address_card.setVisibility(View.VISIBLE);
                            shipping_address_recycler_view.setVisibility(View.GONE);
                        }

                        addressListAdapter = new ReturnOrderPickupAddressListAdapter(shippingAddressArrayList,context,this, newWith);
                        shipping_address_recycler_view.setAdapter(addressListAdapter);

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

                case ApiConstants.REMOVE_ADDRESS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();

                        if(wheretoRemove.equalsIgnoreCase("shippingAddress"))
                        {
                            shippingAddressArrayList.remove(lastPosition);
                            addressListAdapter.notifyDataSetChanged();
                        } else{}
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
    public void itemClick(int position, int chiledPosition, String action)
    {

        if (action.equalsIgnoreCase("selectShippingAddress")) {
            boolean shouldSelect = !shippingAddressArrayList.get(position).isSelected();
            if (shouldSelect) {
                // Deselect all items
                for (int i = 0; i < shippingAddressArrayList.size(); i++) {
                    shippingAddressArrayList.get(i).setSelected(false);
                }
                // Select the clicked item
                shippingAddressArrayList.get(position).setSelected(true);
                shippingAddressID = shippingAddressArrayList.get(position).getAddressId();
                isSelectShippingAddress = true;
            } else {
                // If the clicked item is already selected, don't change the selection state
                shippingAddressArrayList.get(position).setSelected(true);
                shippingAddressID = shippingAddressArrayList.get(position).getAddressId();
            }

            Constant.deliveryPincode = shippingAddressArrayList.get(position).getPinCode();
            Constant.shippingAddressID = shippingAddressArrayList.get(position).getAddressId();
            Constant.pickupAddressId = shippingAddressArrayList.get(position).getAddressId();
            Constant.shippingAddressNameForShowHidePaymentOption = shippingAddressArrayList.get(position).getCountryNameS();
            addressListAdapter.notifyDataSetChanged();
        }
        else  if(action.equalsIgnoreCase("editAddress"))
        {
            Constant.editShippingAddress = "yes";
            AddressListModel model = shippingAddressArrayList.get(position);

            Constant.addressID = model.getAddressId();
            Constant.address1 = model.getAddress1();
            Constant.address2 = model.getAddress2();
            Constant.country = model.getCountryNameS();
            Constant.state = model.getStateNameS();
            Constant.city = model.getCityNameS();
            Constant.countryID = model.getCountryName();
            Constant.stateID = model.getStateName();
            Constant.cityID = model.getCityName();
            Constant.pinCode = model.getPinCode();
            Constant.mobileCode = model.getMobileDialCode();
            Constant.mobileNumber = model.getMobileNumber();
            Constant.email = model.getEmailId();
            Constant.setIsDefault = model.getIsDefault();

            Intent intent = new Intent(activity, AddShippingAddressActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else if(action.equalsIgnoreCase("deleteAddress"))
        {
            AddressListModel model = shippingAddressArrayList.get(position);
            wheretoRemove = "shippingAddress";
            lastPosition = position;

            if(shippingAddressArrayList.get(position).getIsDefault().equalsIgnoreCase("1"))
            {
                Toast.makeText(activity, getResources().getString(R.string.default_address_can_not_delete), Toast.LENGTH_SHORT).show();
            }
            else{
                removeAddressConfirmationPopup(activity, context, getResources().getString(R.string.address_delete_msg), model.getAddressId(),lastPosition);
            }
        }
    }

    void removeAddressConfirmationPopup(final Activity activity,final Context context,String message, String addressID, int position)
    {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_remove_layout, null);
        dialogBuilder.setView(dialogView);
        final android.app.AlertDialog alertDialog = dialogBuilder.create();

        final TextView title = dialogView.findViewById(R.id.title);
        final TextView message1 =  dialogView.findViewById(R.id.message);
        final TextView yes_tv =  dialogView.findViewById(R.id.yes_tv);
        final TextView no_tv =  dialogView.findViewById(R.id.no_tv);

        title.setText(""+context.getResources().getString(R.string.app_name));

        message1.setText(""+message);

        yes_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getRemoveAddressAPI(false, addressID);
                alertDialog.dismiss();
            }
        });

        no_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog.setCancelable(true);
        alertDialog.show();

    }

    private boolean validateFields()
    {
        if (shippingAddressArrayList == null || shippingAddressArrayList.isEmpty()) {
            Toast.makeText(activity, getResources().getString(R.string.please_add_shipping_address), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateShippingAddress() {
        for (int i = 0; i < shippingAddressArrayList.size(); i++) {
            if (shippingAddressArrayList.get(i).isSelected()) {
                // At least one item is selected, return true
                return true;
            }
        }
        // No items were selected, show a toast message and return false
        Toast.makeText(activity, getResources().getString(R.string.please_select_pickup_address), Toast.LENGTH_SHORT).show();
        return false;
    }

    // Return Order Successfully Call this popup
    void orderReturnSuccessfullyPopup(final Activity activity,final Context context, String msg)
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

        message.setText(getResources().getString(R.string.return_request_successful));
        //message1.setText(getResources().getString(R.string.return_request_successful_msg));
        message1.setText(msg);

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

    // Return Order Failed Call this popup
    void orderReturnFailedPopup(final Activity activity,final Context context)
    {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_cancel_order_successfully_layout, null);
        dialogBuilder.setView(dialogView);
        final android.app.AlertDialog alertDialog = dialogBuilder.create();

        final ImageView cross_img = dialogView.findViewById(R.id.cross_img);
        final LinearLayout support_email_mob_lin = dialogView.findViewById(R.id.support_email_mob_lin);
        final TextView done_tv = dialogView.findViewById(R.id.done_tv);
        final TextView message = dialogView.findViewById(R.id.message);
        final TextView message1 = dialogView.findViewById(R.id.message1);
        final TextView support_emali_tv = dialogView.findViewById(R.id.support_emali_tv);
        final TextView support_mobile_number_tv = dialogView.findViewById(R.id.support_mobile_number_tv);

        support_email_mob_lin.setVisibility(View.VISIBLE);

        message.setText(getResources().getString(R.string.return_request_failed));
        message1.setText(getResources().getString(R.string.return_request_failed_msg));

        done_tv.setText(getResources().getString(R.string.retry));

        cross_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                afterCancelManageRedirection();
                alertDialog.dismiss();
            }
        });

        support_emali_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtility.forSendEmail(context);
            }
        });

        support_mobile_number_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommonUtility.makeACallIntent(context);
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
        Constant.afterReturnOrderManageScreenCall = "yes";
        Intent intent = new Intent(activity, MyOrderListScreenActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    public class SendReturnItemAsyncTask {
        private String reply = "";
        //private ProgressDialog _dialog;
        private Context context;
        private ExecutorService executorService;
        private Handler handler;
        private ArrayList<MyOrderListModel> imageUris; // Add this field

        public SendReturnItemAsyncTask(Context context, ArrayList<MyOrderListModel> imageUris) {
            this.context = context;
            this.executorService = Executors.newSingleThreadExecutor();
            this.handler = new Handler(Looper.getMainLooper());
            this.imageUris = imageUris; // Initialize the image list
        }

        public void executeNetworkCall() {
            if (Utils.isNetworkAvailable(context)) {
                onPreExecute();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        doInBackground();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onPostExecute();
                            }
                        });
                    }
                });
            } else {
                Toast.makeText(context, ApiConstants.MSG_INTERNETERROR, Toast.LENGTH_SHORT).show();
            }
        }

        protected void onPreExecute() {
            context.startActivity(new Intent(context, TransparentActivity.class));
        }

        protected void doInBackground() {
            try {
                MultipartUtility mu;
                mu = new MultipartUtility(ApiConstants.DOMAIN_NAME + ApiConstants.RETURN_ORDER, "UTF-8", context);

                mu.addFormField("orderId", Constant.orderID);
                mu.addFormField("type", Constant.orderReturnType);
                mu.addFormField("pickupAddressId", Constant.pickupAddressId);
                mu.addFormField("paymentMode", Constant.refundPaymentMode);
                mu.addFormField("paymentMode1", ""); // This Line Cannot Remove Because Using MultiPart Last Item Contain /n/r Avoid This Use Dummy Key to Remove Last Item /n/r

                // Loop through the imageUris list and add data
                for (int i = 0; i < imageUris.size(); i++) {
                    MyOrderListModel imageUri = imageUris.get(i);

                    mu.addFormDataPart("diamonds[" + i + "][certificate_no]", imageUri.getCertificateNo())
                            .addFormDataPart("diamonds[" + i + "][reason]", imageUri.getSelectedReason())
                            .addFormDataPart("diamonds[" + i + "][remark]", imageUri.getWriteMessage())
                            .addFormDataPart("diamonds[" + i + "][video_url]", imageUri.getReturnOrderVideoUrl())
                            .addFormDataPart("diamonds[" + i + "][video_url1]", ""); // This Line Cannot Remove Because Using MultiPart Last Item Contain /n/r Avoid This Use Dummy Key to Remove Last Item /n/r

                    //---------------------------------------------For Image------------------------------------------------------------------
                    //File file1 = new File(imageUri.getReturnOrderImage());
                    if(imageUri.getReturnOrderImage()!=null && !imageUri.getReturnOrderImage().equalsIgnoreCase(""))
                    {
                        File file = new File(imageUri.getReturnOrderImage());
                       // Log.d("FilePath", "File path1: " + file.getPath());
                       // Log.d("FilePath", "File path: " + file.getAbsolutePath());
                        if (!file.exists()) {
                            //Log.e("FileError", "File does not exist: " + file.getAbsolutePath());
                            return; // or handle the error appropriately
                        }
                        // Inside your doInBackground() method
                        RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
                        mu.addFormDataPartBody("diamonds[" + i + "][image]", file.getAbsolutePath(), imageBody);
                    }else{}

                    //--------------------------------------------For Video--------------------------------------------------------------

                    // Assuming you have a method to get the video URI from your model
                    if(imageUri.getReturnOrderVideo()!=null && !imageUri.getReturnOrderVideo().equalsIgnoreCase(""))
                    {
                        File videoFile = new File(imageUri.getReturnOrderVideo()); // Get the video file path
                       // Log.d("File", "Video file path: " + videoFile.getAbsolutePath());
                        // Check if the video file exists
                        if (!videoFile.exists()) {
                            //Log.e("FileError", "Video file does not exist: " + videoFile.getAbsolutePath());
                            return; // Handle the error appropriately
                        }
                        // Create RequestBody for the video
                        RequestBody videoBody = RequestBody.create(MediaType.parse("video/mp4"), videoFile); // Adjust MIME type if necessary
                        // Add video to the Multipart request
                        mu.addFormDataPartBody("diamonds[" + i + "][video]", videoFile.getAbsolutePath(), videoBody); // Use videoFile.getName() for the filename
                    } else{}

                }

                reply = mu.finish();

            } catch (Exception e) {
                Log.e("Exception : ", e.toString());
                e.printStackTrace();
            }
        }

        protected void onPostExecute() {
            //_dialog.dismiss();
            TransparentActivity.terminateTransparentActivity();
            if (reply != null) {
                showLog("reply : " + reply);
                try {
                    JSONObject jsonObject = new JSONObject(reply);
                    JSONObject jsonObjectData = jsonObject;
                    String message = jsonObjectData.optString("msg");
                    if (jsonObjectData.optString("status").equalsIgnoreCase("1")) {
                        orderReturnSuccessfullyPopup(activity, context,message);
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //  Not Use This Code
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with task
                //executeNetworkTask();
            } else {
                Toast.makeText(this, "Permission denied. Unable to access files.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}