package com.diamondxe.Activity.MyOrder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Adapter.MyOrder.OrderSummaryListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.MyOrder.MyOrderListModel;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.CurrencyUtils;
import com.diamondxe.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderSummaryScreenActivity extends SuperActivity implements TwoRecyclerInterface {

    private ImageView back_img, drop_arrow_img, update_drop_arrow_img,total_tax_info_img;
    private RelativeLayout coupon_code_value_rel,rel_other_tax, rel_diamond_tax, wallet_apply_point_rel, update_coupon_code_value_rel,
            update_wallet_apply_point_rel;
    private TextView error_tv, diamond_price_tv, coupon_code_value_tv,
            shipping_and_handling_tv,platform_fees_tv,total_charges_tv, other_taxes_tv, others_txt_gst_perc_tv, diamond_taxes_tv, diamond_txt_gst_perc_tv,
            total_taxes_tv, sub_total_tv, wallet_apply_charges_tv,bank_charges_tv, final_amount_tv, utr_cheque_no_tv,order_status_tv,
            amount_tv,order_place_date_tv, delivery_date_tv,payment_mode_tv,shipping_address_tv, billing_address_tv,contact_no_tv, email_tv,
            final_amount_tv1, update_final_amount_tv1, update_sub_total_price_tv,update_coupon_code_value_tv, update_shipping_and_handling_tv,
            update_platform_fees_tv,update_total_charges_tv, update_total_taxes_tv,update_total_amount_tv,update_wallet_apply_charges_tv,
            update_bank_charges_tv,update_final_amount_tv,order_number_tv, date_time_tv;
    private LinearLayout  tax_dialog_lv,update_order_summery_main_lin, view_order_summary_details_main_lin, update_view_order_summary_details_main_lin;
    private CardView order_summary_view_card_main, update_order_summary_view_card;
    private RecyclerView recycler_view;
    private ArrayList<MyOrderListModel> modelArrayList;
    private OrderSummaryListAdapter adapter;
    private Activity activity;
    private Context context;
    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    Handler handler1 = new Handler(Looper.getMainLooper());
    private boolean isArrowDown = false;
    private boolean isArrowDownUpdate = false;
    String detailsOrderId = "", detailsCreatedAt="";
    String selectedCurrencyValue ="",selectedCurrencyCode = "",selectedCurrencyDesc="",selectedCurrencyImage="";
    RelativeLayout gemstone_parice_rv,diamond_price_rv;
    TextView gemstone_price_tv_lbl,gemstone_price_tv,gemstone_txt_gst_perc_tv,update_diamond_taxes_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary_screen);


        context = activity = this;

        modelArrayList = new ArrayList<>();

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        gemstone_txt_gst_perc_tv=findViewById(R.id.gemstone_txt_gst_perc_tv);
        tax_dialog_lv=findViewById(R.id.tax_dialog_lv);
        diamond_price_rv=findViewById(R.id.diamond_price_rv);
        gemstone_parice_rv=findViewById(R.id.gemstone_parice_rv);
        gemstone_price_tv_lbl=findViewById(R.id.gemstone_price_tv_lbl);
        gemstone_price_tv=findViewById(R.id.gemstone_price_tv);
        update_diamond_taxes_tv=findViewById(R.id.update_diamond_taxes_tv);

        error_tv = findViewById(R.id.error_tv);
        order_number_tv = findViewById(R.id.order_number_tv);
        date_time_tv = findViewById(R.id.date_time_tv);

        coupon_code_value_rel = findViewById(R.id.coupon_code_value_rel);
        update_order_summery_main_lin = findViewById(R.id.update_order_summery_main_lin);
        view_order_summary_details_main_lin = findViewById(R.id.view_order_summary_details_main_lin);

        drop_arrow_img = findViewById(R.id.drop_arrow_img);
        update_drop_arrow_img = findViewById(R.id.update_drop_arrow_img);

        order_summary_view_card_main = findViewById(R.id.order_summary_view_card_main);
        order_summary_view_card_main.setOnClickListener(this);

        update_order_summary_view_card = findViewById(R.id.update_order_summary_view_card);
        update_order_summary_view_card.setOnClickListener(this);

        update_view_order_summary_details_main_lin = findViewById(R.id.update_view_order_summary_details_main_lin);

        update_coupon_code_value_rel = findViewById(R.id.update_coupon_code_value_rel);
        update_wallet_apply_point_rel = findViewById(R.id.update_wallet_apply_point_rel);

        update_final_amount_tv1 = findViewById(R.id.update_final_amount_tv1);
        update_sub_total_price_tv = findViewById(R.id.update_sub_total_price_tv);
        update_coupon_code_value_tv = findViewById(R.id.update_coupon_code_value_tv);
        update_shipping_and_handling_tv = findViewById(R.id.update_shipping_and_handling_tv);
        update_platform_fees_tv = findViewById(R.id.update_platform_fees_tv);
        update_total_charges_tv = findViewById(R.id.update_total_charges_tv);
        update_total_taxes_tv = findViewById(R.id.update_total_taxes_tv);
        update_total_amount_tv = findViewById(R.id.update_total_amount_tv);
        update_wallet_apply_charges_tv = findViewById(R.id.update_wallet_apply_charges_tv);
        update_bank_charges_tv = findViewById(R.id.update_bank_charges_tv);
        update_final_amount_tv = findViewById(R.id.update_final_amount_tv);


        rel_other_tax = findViewById(R.id.rel_other_tax);
        rel_other_tax.setOnClickListener(this);

        rel_diamond_tax = findViewById(R.id.rel_diamond_tax);
        rel_diamond_tax.setOnClickListener(this);

        // TotalTax "i" Icon Image.
        total_tax_info_img = findViewById(R.id.total_tax_info_img);
        total_tax_info_img.setVisibility(View.GONE);

        wallet_apply_point_rel = findViewById(R.id.wallet_apply_point_rel);

        diamond_price_tv = findViewById(R.id.diamond_price_tv);
        coupon_code_value_tv = findViewById(R.id.coupon_code_value_tv);
        shipping_and_handling_tv = findViewById(R.id.shipping_and_handling_tv);
        platform_fees_tv = findViewById(R.id.platform_fees_tv);
        total_charges_tv = findViewById(R.id.total_charges_tv);
        other_taxes_tv = findViewById(R.id.other_taxes_tv);
        others_txt_gst_perc_tv = findViewById(R.id.others_txt_gst_perc_tv);
        diamond_taxes_tv = findViewById(R.id.diamond_taxes_tv);
        diamond_txt_gst_perc_tv = findViewById(R.id.diamond_txt_gst_perc_tv);
        total_taxes_tv = findViewById(R.id.total_taxes_tv);
        sub_total_tv = findViewById(R.id.sub_total_tv);
        wallet_apply_charges_tv = findViewById(R.id.wallet_apply_charges_tv);
        bank_charges_tv = findViewById(R.id.bank_charges_tv);
        final_amount_tv = findViewById(R.id.final_amount_tv);

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
        final_amount_tv1 = findViewById(R.id.final_amount_tv1);

        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setNestedScrollingEnabled(false);

        getCurrencyData();

        getOrderSummaryAPI(false);
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
        else if(id == R.id.order_summary_view_card_main)
        {
           // Log.e("isArrowDown : ", "isArrowDown : " + isArrowDown);
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
        else if(id == R.id.update_order_summary_view_card)
        {
            //Log.e("isArrowDown : ", "isArrowDown : " + isArrowDownUpdate);
            Utils.hideKeyboard(activity);
            if (isArrowDownUpdate) {
                update_drop_arrow_img.setImageResource(R.drawable.down);
                update_view_order_summary_details_main_lin.setVisibility(View.GONE);
            } else {
                update_drop_arrow_img.setImageResource(R.drawable.up);
                update_view_order_summary_details_main_lin.setVisibility(View.VISIBLE);
            }
            isArrowDownUpdate = !isArrowDownUpdate;
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
            tax_dialog_lv.setVisibility(View.VISIBLE);
            //diamond_txt_gst_perc_tv.setVisibility(View.VISIBLE);
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //diamond_txt_gst_perc_tv.setVisibility(View.GONE);
                    tax_dialog_lv.setVisibility(View.GONE);
                }
            }, 2000);
        }
    }

    public void getOrderSummaryAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("orderId", Constant.orderID);
            //urlParameter.put("orderId", "172768804033");

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.ORDER_SUMMARY, ApiConstants.ORDER_SUMMARY_ID,showLoader,
                    "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {
        try {
            Log.v("------Diamond----- : ", "--268****------JSONObjectSummary-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {
                case ApiConstants.ORDER_SUMMARY_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        String currencySymbol = CommonUtility.checkString(jObjDetails.optString("currency_symbol"));
                        Log.e("currencySymbol","....274...................."+currencySymbol);
                        String  getexchange_rate=CommonUtility.checkString(jObjDetails.optString("exchange_rate"));
                        Log.e("getCurrencySymbol","270..@@..."+currencySymbol);
                        Log.e("getexchange_rate","270..@@..."+getexchange_rate);


                        int iscrorder = CommonUtility.checkInt(jObjDetails.optString("is_cr_order"));
                        Log.e("iscrorder","....284...................."+iscrorder);
                        if (iscrorder==1)
                        {
                            JSONObject updatedpricebreakup = jObjDetails.optJSONObject("updated_price_breakup");
                            if (updatedpricebreakup != null) {

                                JSONObject subtotal = updatedpricebreakup.optJSONObject("subtotal");
                                if (subtotal != null) {
                                    if (subtotal.has("gemstone")) {
                                        int gemstoneSubtotal = subtotal.optInt("gemstone", 0);
                                        System.out.println("Gemstone Subtotal: " + gemstoneSubtotal);
                                        String subTotalFormat1 = CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, String.valueOf(gemstoneSubtotal));
                                        gemstone_price_tv.setText(currencySymbol + " " + CommonUtility.currencyFormat(subTotalFormat1));
                                        gemstone_price_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                                    } else {
                                        System.out.println("Gemstone Subtotal: Not Available");
                                        gemstone_parice_rv.setVisibility(View.GONE);
                                    }
                                    if (subtotal.has("diamond"))
                                    {
                                        int diamondSubtotal = subtotal.optInt("diamond", 0);
                                        System.out.println("Diamond Subtotal: " + diamondSubtotal);
                                        String subTotalFormat1 = CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, String.valueOf(diamondSubtotal));
                                        String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat1, getexchange_rate, currencySymbol);
                                        diamond_price_tv.setText(finalAmount);
                                        /*diamond_price_tv.setText(currencySymbol + " " + CommonUtility.currencyFormat(subTotalFormat1));*/
                                        diamond_price_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                                    }
                                    else {
                                        diamond_price_rv.setVisibility(View.GONE);
                                        System.out.println("Gemstone Subtotal: Not Available");
                                    }

                                    JSONObject taxPerc = updatedpricebreakup.optJSONObject("tax_perc");
                                    if (taxPerc != null) {
                                        if (taxPerc.has("gemstone"))
                                        {
                                            double gemstoneTaxPerc = taxPerc.optDouble("gemstone", 0.0);
                                            System.out.println("Gemstone Tax Percentage: " + gemstoneTaxPerc);
                                            gemstone_txt_gst_perc_tv.setText("Gemstone Tax "+gemstoneTaxPerc+" % GST");
                                        } else {
                                            gemstone_txt_gst_perc_tv.setVisibility(View.GONE);
                                            System.out.println("Gemstone Tax Percentage: Not Available");
                                        }

                                        if (taxPerc.has("diamond"))
                                        {
                                            double diamondTaxPerc = taxPerc.optDouble("diamond", 0.0);
                                            System.out.println("Diamond Tax Percentage: " + diamondTaxPerc);
                                            diamond_txt_gst_perc_tv.setText("Diamond Tax "+diamondTaxPerc+" % GST");
                                        }
                                        else {
                                            diamond_txt_gst_perc_tv.setVisibility(View.GONE);
                                        }

                                    }

                                }
                            }
                        }
                        else {
                            JSONObject priceBreakup = jObjDetails.optJSONObject("price_breakup");
                            if (priceBreakup != null) {

                                JSONObject subtotal = priceBreakup.optJSONObject("subtotal");
                                if (subtotal != null) {
                                    if (subtotal.has("gemstone")) {
                                        int gemstoneSubtotal = subtotal.optInt("gemstone", 0);
                                        System.out.println("Gemstone Subtotal: " + gemstoneSubtotal);
                                        String subTotalFormat1 = CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, String.valueOf(gemstoneSubtotal));
                                        String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat1, getexchange_rate, currencySymbol);

                                        gemstone_price_tv.setText(finalAmount);
                                        gemstone_price_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                                    } else {
                                        System.out.println("Gemstone Subtotal: Not Available");
                                        gemstone_parice_rv.setVisibility(View.GONE);
                                    }
                                    if (subtotal.has("diamond"))
                                    {
                                        int diamondSubtotal = subtotal.optInt("diamond", 0);
                                        System.out.println("Diamond Subtotal: " + diamondSubtotal);
                                        String subTotalFormat1 = CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, String.valueOf(diamondSubtotal));
                                        String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat1, getexchange_rate, currencySymbol);
                                        diamond_price_tv.setText(finalAmount);
                                       // diamond_price_tv.setText(currencySymbol + " " + CommonUtility.currencyFormat(subTotalFormat1));
                                        diamond_price_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                                    }
                                    else {
                                        diamond_price_rv.setVisibility(View.GONE);
                                        System.out.println("Gemstone Subtotal: Not Available");
                                    }

                                    JSONObject taxPerc = priceBreakup.optJSONObject("tax_perc");
                                    if (taxPerc != null) {
                                        if (taxPerc.has("gemstone"))
                                        {
                                            double gemstoneTaxPerc = taxPerc.optDouble("gemstone", 0.0);
                                            System.out.println("Gemstone Tax Percentage: " + gemstoneTaxPerc);
                                            gemstone_txt_gst_perc_tv.setText("Gemstone Tax "+gemstoneTaxPerc+" % GST");
                                        } else {
                                            gemstone_txt_gst_perc_tv.setVisibility(View.GONE);
                                            System.out.println("Gemstone Tax Percentage: Not Available");
                                        }

                                        if (taxPerc.has("diamond"))
                                        {
                                            double diamondTaxPerc = taxPerc.optDouble("diamond", 0.0);
                                            System.out.println("Diamond Tax Percentage: " + diamondTaxPerc);
                                            diamond_txt_gst_perc_tv.setText("Diamond Tax "+diamondTaxPerc+" % GST");
                                        }
                                        else {
                                            diamond_txt_gst_perc_tv.setVisibility(View.GONE);
                                        }

                                    }

                                }
                            }
                        }

                        detailsOrderId = CommonUtility.checkString(jObjDetails.optString("order_id"));
                        detailsCreatedAt = CommonUtility.checkString(jObjDetails.optString("created_at"));

                        if(!detailsCreatedAt.equalsIgnoreCase(""))
                        {
                            detailsCreatedAt = CommonUtility.convertDateTimeIntoLocal(detailsCreatedAt, ApiConstants.DATE_FORMAT, "dd/MM/yyyy, hh:mm:ss a");

                        } else{}


                        order_number_tv.setText("#"+detailsOrderId);
                        date_time_tv.setText(detailsCreatedAt);

                        String delivery_date = CommonUtility.checkString(jObjDetails.optString("delivery_date"));

                        if(!delivery_date.equalsIgnoreCase(""))
                        {
                            delivery_date = CommonUtility.convertDateTimeIntoLocal(delivery_date, ApiConstants.DATE_FORMAT, "dd/MM/yyyy, hh:mm:ss a");

                        } else{
                            delivery_date = "-";
                        }

                        // Order Summary
                        String payment_mode = CommonUtility.checkString(jObjDetails.optString("payment_mode"));
                        String payment_status = CommonUtility.checkString(jObjDetails.optString("payment_status"));
                        String order_status = CommonUtility.checkString(jObjDetails.optString("order_status"));
                        String transaction_id = CommonUtility.checkString(jObjDetails.optString("transaction_id"));
                        String shipping_charge = CommonUtility.checkString(jObjDetails.optString("shipping_charge"));
                        String platform_fee = CommonUtility.checkString(jObjDetails.optString("platform_fee"));
                        String bank_charge = CommonUtility.checkString(jObjDetails.optString("bank_charge"));
                        String wallet_points = CommonUtility.checkString(jObjDetails.optString("wallet_points"));
                        String coupon_value = CommonUtility.checkString(jObjDetails.optString("coupon_value"));
                        String is_coupon_applied = CommonUtility.checkString(jObjDetails.optString("is_coupon_applied"));
                        String coupon_code = CommonUtility.checkString(jObjDetails.optString("coupon_code"));
                        String coupon_discount = CommonUtility.checkString(jObjDetails.optString("coupon_discount"));
                        String other_tax = CommonUtility.checkString(jObjDetails.optString("total_charge_tax"));
                        String diamond_tax = CommonUtility.checkString(jObjDetails.optString("tax"));
                        String total_charge = CommonUtility.checkString(jObjDetails.optString("total_charge"));
                        String sub_total = CommonUtility.checkString(jObjDetails.optString("sub_total"));
                        String sub_total_amount = CommonUtility.checkString(jObjDetails.optString("total_amount"));
                        String final_amount = CommonUtility.checkString(jObjDetails.optString("final_amount"));
                        String total_taxes = CommonUtility.checkString(jObjDetails.optString("total_taxes"));

                        String is_cr_order = CommonUtility.checkString(jObjDetails.optString("is_cr_order"));
                        if(is_cr_order.equalsIgnoreCase("1"))
                        {
                            update_order_summery_main_lin.setVisibility(View.VISIBLE);
                            String cr_sub_total = CommonUtility.checkString(jObjDetails.optString("cr_sub_total"));
                            String cr_coupon_discount = CommonUtility.checkString(jObjDetails.optString("cr_coupon_discount"));
                            String cr_total_tax = CommonUtility.checkString(jObjDetails.optString("cr_total_tax"));
                            String cr_shipping_charge = CommonUtility.checkString(jObjDetails.optString("cr_shipping_charge"));
                            String cr_platform_fee = CommonUtility.checkString(jObjDetails.optString("cr_platform_fee"));
                            String cr_total_amount = CommonUtility.checkString(jObjDetails.optString("cr_total_amount"));
                            String cr_bank_charges = CommonUtility.checkString(jObjDetails.optString("cr_bank_charges"));
                            String cr_wallet_points = CommonUtility.checkString(jObjDetails.optString("cr_wallet_points"));
                            String cr_final_amount = CommonUtility.checkString(jObjDetails.optString("cr_final_amount"));
                            String cr_total_charges = CommonUtility.checkString(jObjDetails.optString("cr_total_charges"));

                            if(cr_sub_total!=null && !cr_sub_total.equalsIgnoreCase(""))
                            {
                                String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cr_sub_total);
                                String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                                String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                                update_final_amount_tv1.setText(finalAmount);
                                update_sub_total_price_tv.setText(finalAmount);
                                /*update_final_amount_tv1.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                                /*update_sub_total_price_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                            } else{}

                            if(cr_coupon_discount!=null && !cr_coupon_discount.equalsIgnoreCase("") && !cr_coupon_discount.equalsIgnoreCase("0"))
                            {
                                update_coupon_code_value_rel.setVisibility(View.VISIBLE);
                                String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cr_coupon_discount);
                                String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                                update_coupon_code_value_tv.setText("-"+getCurrencySymbol + "" + subTotalFormat);
                            }
                            else{
                                update_coupon_code_value_rel.setVisibility(View.GONE);
                            }

                            if(cr_shipping_charge!=null && !cr_shipping_charge.equalsIgnoreCase(""))
                            {
                                String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cr_shipping_charge);
                                String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                                String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                                update_shipping_and_handling_tv.setText(finalAmount);
                                /*update_shipping_and_handling_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                            } else{}

                            if(cr_platform_fee!=null && !cr_platform_fee.equalsIgnoreCase(""))
                            {
                                String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cr_platform_fee);
                                String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                                String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                                update_platform_fees_tv.setText(finalAmount);
                                /*update_platform_fees_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                            } else{}

                            if(cr_total_charges!=null && !cr_total_charges.equalsIgnoreCase(""))
                            {
                                String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cr_total_charges);
                                String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                                String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                                update_total_charges_tv.setText(finalAmount);
                                /*update_total_charges_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                            } else{}

                            if(cr_total_tax!=null && !cr_total_tax.equalsIgnoreCase(""))
                            {
                                String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cr_total_tax);
                                String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                                String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                                update_total_taxes_tv.setText(finalAmount);
                                /*update_total_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                            } else{}

                            if(cr_total_amount!=null && !cr_total_amount.equalsIgnoreCase(""))
                            {
                                String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cr_total_amount);
                                String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                                String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                                update_total_amount_tv.setText(finalAmount);
                                /*update_total_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                            } else{}

                            if(cr_wallet_points!=null && !cr_wallet_points.equalsIgnoreCase("") && !cr_wallet_points.equalsIgnoreCase("0"))
                            {
                                update_wallet_apply_point_rel.setVisibility(View.VISIBLE);

                                String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cr_wallet_points);
                                String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                                update_wallet_apply_charges_tv.setText("-"+getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                            } else{
                                update_wallet_apply_point_rel.setVisibility(View.GONE);
                            }

                            if(cr_bank_charges!=null && !cr_bank_charges.equalsIgnoreCase(""))
                            {
                                String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cr_bank_charges);
                                String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                                String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                                update_bank_charges_tv.setText(finalAmount);
                                /*update_bank_charges_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                            } else{}

                            if(cr_final_amount!=null && !cr_final_amount.equalsIgnoreCase(""))
                            {
                                String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, cr_final_amount);
                                String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                                String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                                update_final_amount_tv.setText(finalAmount);
                                /*update_final_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                            } else{}

                        }else{
                            update_order_summery_main_lin.setVisibility(View.GONE);
                        }

                        JSONObject jObjUserDetails = jObjDetails.optJSONObject("user_details");

                        String billing_address = CommonUtility.checkString(jObjUserDetails.optString("billing_address"));
                        String shipping_address = CommonUtility.checkString(jObjUserDetails.optString("shipping_address"));
                        String user_email = CommonUtility.checkString(jObjUserDetails.optString("user_email"));
                        String user_mobile = CommonUtility.checkString(jObjUserDetails.optString("user_mobile"));

                        if(!sub_total.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, sub_total);
                           /* String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);*/
                            String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                            amount_tv.setText(finalAmount);
                            //diamond_price_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                            final_amount_tv1.setText(finalAmount);


                        }else{}

                        if(!final_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, final_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                            amount_tv.setText(finalAmount);
                            /*amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/

                        }else{}

                        utr_cheque_no_tv.setText(transaction_id);

                        order_status_tv.setText(order_status);

                        if(order_status.equalsIgnoreCase("In-Progress"))
                        {
                            order_status_tv.setTextColor(ContextCompat.getColor(context, R.color.yellow));
                        }
                        else if(order_status.equalsIgnoreCase("Partially Cancelled") || order_status.equalsIgnoreCase("Partially Returned"))
                        {
                            order_status_tv.setTextColor(ContextCompat.getColor(context, R.color.red));
                        }
                        else if(order_status.equalsIgnoreCase("Confirmed") || order_status.equalsIgnoreCase("Delivered"))
                        {
                            order_status_tv.setTextColor(ContextCompat.getColor(context, R.color.green));
                        }
                        else
                        {
                            order_status_tv.setTextColor(ContextCompat.getColor(context, R.color.yellow));
                        }

                        order_place_date_tv.setText(detailsCreatedAt);
                        delivery_date_tv.setText(delivery_date);
                        payment_mode_tv.setText(payment_mode);
                        shipping_address_tv.setText(shipping_address);
                        billing_address_tv.setText(billing_address);
                        contact_no_tv.setText(getResources().getString(R.string.contact_no) + " " +user_mobile);
                        email_tv.setText(getResources().getString(R.string.email_lbl) + " " +user_email);


                        if(shipping_charge!=null && !shipping_charge.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, shipping_charge);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                            shipping_and_handling_tv.setText(finalAmount);
                            /*shipping_and_handling_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                        } else{}

                        if(platform_fee!=null && !platform_fee.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, platform_fee);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                            platform_fees_tv.setText(finalAmount);
                            /*platform_fees_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                        } else{}

                        if(total_charge!=null && !total_charge.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, total_charge);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                            total_charges_tv.setText(finalAmount);
                            /*total_charges_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                        } else{}

                        if(other_tax!=null && !other_tax.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, other_tax);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);

                            other_taxes_tv.setText(finalAmount);
                        } else{}

                        if(diamond_tax!=null && !diamond_tax.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, diamond_tax);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                            diamond_taxes_tv.setText(finalAmount);
                        } else{}

                        if(total_taxes!=null && !total_taxes.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, total_taxes);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                            total_taxes_tv.setText(finalAmount);
                            /*total_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                        } else{}

                        if(sub_total_amount!=null && !sub_total_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, sub_total_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                            sub_total_tv.setText(finalAmount);
                            /*sub_total_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                        } else{}

                        if(bank_charge!=null && !bank_charge.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, bank_charge);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                            bank_charges_tv.setText(finalAmount);
                            /*bank_charges_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                        } else{}

                        if(final_amount!=null && !final_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, final_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                            final_amount_tv.setText(finalAmount);
                            /*final_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                        } else{}

                        if(wallet_points!=null && !wallet_points.equalsIgnoreCase("") && !wallet_points.equalsIgnoreCase("0"))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, wallet_points);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            wallet_apply_point_rel.setVisibility(View.VISIBLE); // Coupon Code Apply Value
                            wallet_apply_charges_tv.setText("-"+getCurrencySymbol + "" + subTotalFormat);
                        }else{}

                        if(is_coupon_applied.equalsIgnoreCase("1"))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, coupon_discount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            coupon_code_value_rel.setVisibility(View.VISIBLE); // Coupon Code Apply Value
                            coupon_code_value_tv.setText("-"+getCurrencySymbol + "" + subTotalFormat);
                        }
                        else{}

                        // Update Order Summary

                        // Check If modelArrayList Size Is GraterThen 0 Clear First.
                        if(modelArrayList!=null && modelArrayList.size()>0)
                        {
                            modelArrayList.clear();
                        }else{}

                        JSONArray details = jObjDetails.getJSONArray("diamonds");

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

                            modelArrayList.add(model);
                        }

                        for (int k = 0; k <modelArrayList.size() ; k++)
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, modelArrayList.get(k).getSubTotal());
                           /* String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            modelArrayList.get(k).setShowingSubTotal(subTotalFormat);
                            modelArrayList.get(k).setCurrencySymbol(getCurrencySymbol);*/
                            String finalAmount = CurrencyUtils.calculateFinalAmountwithoutcurrency(subTotalFormat, getexchange_rate);
                            modelArrayList.get(k).setShowingSubTotal(String.valueOf(finalAmount));
                            modelArrayList.get(k).setCurrencySymbol(currencySymbol);
                        }

                        adapter = new OrderSummaryListAdapter(modelArrayList,context,this, "fullOrderCancel");
                        recycler_view.setAdapter(adapter);
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        //getCountryListAPI(true);
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
        finally
        {
            if (modelArrayList !=null && modelArrayList.size()<=0)
            {
                error_tv.setVisibility(View.VISIBLE);
                error_tv.setText(""+ ApiConstants.NO_RESULT_FOUND);
                recycler_view.setVisibility(View.GONE);
            } else {
                error_tv.setVisibility(View.GONE);
                recycler_view.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void getErrorResponce(String error, int service_ID) {

    }

    @Override
    public void itemClick(int parantPosition, int chiledPosition, String action) {

    }
}