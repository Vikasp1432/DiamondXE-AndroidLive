package com.diamondxe.Activity.MyOrder;

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

import java.util.ArrayList;
import java.util.HashMap;

public class ReturnOrderSummaryScreenActivity extends SuperActivity implements TwoRecyclerInterface {

    private ImageView back_img, drop_arrow_img,return_drop_arrow_img, total_tax_info_img;
    private RelativeLayout coupon_code_value_rel,rel_other_tax, rel_diamond_tax, wallet_apply_point_rel, other_tax_main_rel, diamond_tax_main_rel,
            return_wallet_apply_point_rel, rel_total_tax;
    private TextView error_tv, diamond_price_tv, coupon_code_value_tv,
            shipping_and_handling_tv,platform_fees_tv,total_charges_tv, other_taxes_tv, others_txt_gst_perc_tv, diamond_taxes_tv, diamond_txt_gst_perc_tv,
            total_taxes_tv, sub_total_tv, wallet_apply_charges_tv,bank_charges_tv, final_amount_tv, utr_cheque_no_tv,order_status_tv,
            amount_tv,order_place_date_tv, delivery_date_tv,payment_mode_tv,shipping_address_tv, billing_address_tv,contact_no_tv, email_tv,
            final_amount_tv1,return_diamond_price_tv,return_sub_total_tv,return_wallet_apply_charges_tv,return_final_amount_tv,
            return_final_amount_tv1,refund_status_tv, total_others_txt_gst_perc_tv,order_number_tv, date_time_tv;
    private LinearLayout  view_order_summary_details_main_lin,return_view_order_summary_details_lin;
    private CardView order_summary_view_card_main,return_order_summary_view_card;
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
    private boolean isArrowDownReturn = false;
    String detailsOrderId = "", detailsCreatedAt="", detailsReturnAt="";
    String selectedCurrencyValue ="",selectedCurrencyCode = "",selectedCurrencyDesc="",selectedCurrencyImage="";

    RelativeLayout diamond_return_rv,gemstone_return_rv,rel_diamond_tax_return;
    LinearLayout tax_dialog_lv,return_tax_dialog_lv;
    RelativeLayout gemstone_parice_rv,diamond_price_rv,tax_rv;
    TextView gemstone_price_tv_lbl,gemstone_price_tv,gemstone_txt_gst_perc_tv,gemstone_diamond_price_tv,return_diamond_txt_gst_perc_tv,return_gemstone_txt_gst_perc_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_order_summary_screen);

        context = activity = this;

        modelArrayList = new ArrayList<>();
        tax_rv=findViewById(R.id.tax_rv);
        tax_rv.setVisibility(View.GONE);
        rel_diamond_tax_return=findViewById(R.id.rel_diamond_tax_return);
        rel_diamond_tax_return.setOnClickListener(this);
        return_gemstone_txt_gst_perc_tv=findViewById(R.id.return_gemstone_txt_gst_perc_tv);
        return_diamond_txt_gst_perc_tv=findViewById(R.id.return_diamond_txt_gst_perc_tv);
        return_tax_dialog_lv=findViewById(R.id.return_tax_dialog_lv);
        gemstone_return_rv=findViewById(R.id.gemstone_return_rv);
        diamond_return_rv=findViewById(R.id.diamond_return_rv);
        gemstone_diamond_price_tv=findViewById(R.id.gemstone_diamond_price_tv);
        gemstone_txt_gst_perc_tv=findViewById(R.id.gemstone_txt_gst_perc_tv);
        tax_dialog_lv=findViewById(R.id.tax_dialog_lv);
        diamond_price_rv=findViewById(R.id.diamond_price_rv);
        gemstone_parice_rv=findViewById(R.id.gemstone_parice_rv);
        gemstone_price_tv_lbl=findViewById(R.id.gemstone_price_tv_lbl);
        gemstone_price_tv=findViewById(R.id.gemstone_price_tv);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        error_tv = findViewById(R.id.error_tv);
        order_number_tv = findViewById(R.id.order_number_tv);
        date_time_tv = findViewById(R.id.date_time_tv);

        coupon_code_value_rel = findViewById(R.id.coupon_code_value_rel);
        view_order_summary_details_main_lin = findViewById(R.id.view_order_summary_details_main_lin);

        drop_arrow_img = findViewById(R.id.drop_arrow_img);

        order_summary_view_card_main = findViewById(R.id.order_summary_view_card_main);
        order_summary_view_card_main.setOnClickListener(this);

        other_tax_main_rel = findViewById(R.id.other_tax_main_rel);
        other_tax_main_rel.setVisibility(View.GONE);
        diamond_tax_main_rel = findViewById(R.id.diamond_tax_main_rel);
        diamond_tax_main_rel.setVisibility(View.VISIBLE);
        diamond_tax_main_rel.setOnClickListener(this);

        rel_other_tax = findViewById(R.id.rel_other_tax);
        rel_other_tax.setOnClickListener(this);

        rel_diamond_tax = findViewById(R.id.rel_diamond_tax);
        rel_diamond_tax.setOnClickListener(this);

        total_others_txt_gst_perc_tv = findViewById(R.id.total_others_txt_gst_perc_tv);
        // total_tax_info_img "i" Icon Visible
        total_tax_info_img = findViewById(R.id.total_tax_info_img);
        total_tax_info_img.setVisibility(View.GONE);

        rel_total_tax = findViewById(R.id.rel_total_tax);
        rel_total_tax.setOnClickListener(this);

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


        return_wallet_apply_point_rel = findViewById(R.id.return_wallet_apply_point_rel);
        return_diamond_price_tv = findViewById(R.id.return_diamond_price_tv);
        return_sub_total_tv = findViewById(R.id.return_sub_total_tv);
        return_wallet_apply_charges_tv = findViewById(R.id.return_wallet_apply_charges_tv);
        return_final_amount_tv = findViewById(R.id.return_final_amount_tv);

        return_order_summary_view_card = findViewById(R.id.return_order_summary_view_card);
        return_order_summary_view_card.setOnClickListener(this);

        return_drop_arrow_img = findViewById(R.id.return_drop_arrow_img);
        return_final_amount_tv1 = findViewById(R.id.return_final_amount_tv1);
        return_view_order_summary_details_lin = findViewById(R.id.return_view_order_summary_details_lin);

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
        refund_status_tv = findViewById(R.id.refund_status_tv);

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
        else if(id == R.id.rel_diamond_tax)
        {
            //Toast.makeText(this,"Click,,",Toast.LENGTH_SHORT).show();
            Utils.hideKeyboard(activity);
            tax_dialog_lv.setVisibility(View.VISIBLE);
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tax_dialog_lv.setVisibility(View.GONE);
                }
            }, 2000);
        }
        else if(id == R.id.rel_diamond_tax_return)
        {
            Utils.hideKeyboard(activity);
            return_tax_dialog_lv.setVisibility(View.VISIBLE);
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    return_tax_dialog_lv.setVisibility(View.GONE);
                }
            }, 2000);
        }

    }

    public void getOrderSummaryAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("returnOrderId", Constant.orderID);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.RETURN_ORDER_DETAILS, ApiConstants.RETURN_ORDER_DETAILS_ID,showLoader,
                    "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {
        try {
            Log.v("------Diamond----- : ", "--------JSONObjectReturnSummary-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {
                case ApiConstants.RETURN_ORDER_DETAILS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {

                        Log.e(" jsonObjectData","...268...."+jsonObjectData);
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");
                        String getexchange_rate=CommonUtility.checkString(jObjDetails.optString("exchange_rate"));
                        JSONObject price_breakupDetails = jObjDetails.optJSONObject("price_breakup");
                        Log.e("price breakupDetails","...270...."+price_breakupDetails);

                        Log.e("priceBreakup","351..@@@.................."+price_breakupDetails);
                        String currencySymbol = CommonUtility.checkString(jObjDetails.optString("currency_symbol"));
                        Log.e("currencySymbol","274...................."+currencySymbol);
                        if (price_breakupDetails != null) {

                            JSONObject subtotal = price_breakupDetails.optJSONObject("subtotal");
                            if (subtotal != null) {
                                if (subtotal.has("gemstone")) {
                                    int gemstoneSubtotal = subtotal.optInt("gemstone", 0);
                                    System.out.println("Gemstone Subtotal: " + gemstoneSubtotal);
                                    String subTotalFormat1 = CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, String.valueOf(gemstoneSubtotal));
                                    String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat1, getexchange_rate, currencySymbol);
                                    gemstone_diamond_price_tv.setText(finalAmount);
                                    /*gemstone_diamond_price_tv.setText(currencySymbol + " " + CommonUtility.currencyFormat(subTotalFormat1));*/
                                    gemstone_diamond_price_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                                } else {
                                    System.out.println("Gemstone Subtotal: Not Available");
                                    gemstone_return_rv.setVisibility(View.GONE);
                                }
                                if (subtotal.has("diamond"))
                                {
                                    int diamondSubtotal = subtotal.optInt("diamond", 0);
                                    System.out.println("Diamond Subtotal: " + diamondSubtotal);
                                    String subTotalFormat1 = CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, String.valueOf(diamondSubtotal));
                                    String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat1, getexchange_rate, currencySymbol);
                                    return_diamond_price_tv.setText(finalAmount);
                                    /*return_diamond_price_tv.setText(currencySymbol + " " + CommonUtility.currencyFormat(subTotalFormat1));*/
                                    return_diamond_price_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                                }
                                else {
                                    diamond_return_rv.setVisibility(View.GONE);
                                    System.out.println("Gemstone Subtotal: Not Available");
                                }

                                JSONObject taxPerc = price_breakupDetails.optJSONObject("tax_perc");
                                if (taxPerc != null) {
                                    if (taxPerc.has("gemstone"))
                                    {
                                        double gemstoneTaxPerc = taxPerc.optDouble("gemstone", 0.0);
                                        System.out.println("Gemstone Tax Percentage: " + gemstoneTaxPerc);
                                        return_gemstone_txt_gst_perc_tv.setText("Gemstone Tax "+gemstoneTaxPerc+" % GST");
                                    } else {
                                        return_gemstone_txt_gst_perc_tv.setVisibility(View.GONE);
                                        System.out.println("Gemstone Tax Percentage: Not Available");
                                    }

                                    if (taxPerc.has("diamond"))
                                    {
                                        double diamondTaxPerc = taxPerc.optDouble("diamond", 0.0);
                                        System.out.println("Diamond Tax Percentage: " + diamondTaxPerc);
                                        return_diamond_txt_gst_perc_tv.setText("Diamond Tax "+diamondTaxPerc+" % GST");
                                    }
                                    else {
                                        return_diamond_txt_gst_perc_tv.setVisibility(View.GONE);
                                    }

                                }

                            }
                        }
                        //gemstone_diamond_price_tv

                        //return_diamond_price_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));

                        //detailsOrderId = CommonUtility.checkString(jObjDetails.optString("order_id"));
                        detailsOrderId = CommonUtility.checkString(jObjDetails.optString("return_order_id"));
                        detailsReturnAt = CommonUtility.checkString(jObjDetails.optString("returned_at"));

                        if(!detailsReturnAt.equalsIgnoreCase(""))
                        {
                            detailsReturnAt = CommonUtility.convertDateTimeIntoLocal(detailsReturnAt, ApiConstants.DATE_FORMAT, "dd/MM/yyyy, hh:mm:ss a");

                        } else{}

                        order_number_tv.setText("#"+detailsOrderId);
                        date_time_tv.setText(detailsReturnAt);

                        String order_created_at = CommonUtility.checkString(jObjDetails.optString("order_created_at"));

                        if(!order_created_at.equalsIgnoreCase(""))
                        {
                            order_created_at = CommonUtility.convertDateTimeIntoLocal(order_created_at, ApiConstants.DATE_FORMAT, "dd/MM/yyyy, hh:mm:ss a");

                        } else{
                            order_created_at = "-";
                        }

                        // Details Object Data Fetch
                        String payment_status = CommonUtility.checkString(jObjDetails.optString("payment_status"));
                        String order_status = CommonUtility.checkString(jObjDetails.optString("order_status"));


                        //-----------------------------------------Cancel Order Summary Data Start---------------------------------
                        String return_total_amount = CommonUtility.checkString(jObjDetails.optString("total_amount"));
                        String return_wallet_points = CommonUtility.checkString(jObjDetails.optString("wallet_points"));
                        String return_sub_total = CommonUtility.checkString(jObjDetails.optString("sub_total"));
                        String return_source_amount = CommonUtility.checkString(jObjDetails.optString("source_amount"));
                        String refund_status = CommonUtility.checkString(jObjDetails.optString("refund_status"));
                        String refund_mode = CommonUtility.checkString(jObjDetails.optString("refund_mode"));

                        refund_status_tv.setText(getResources().getString(R.string.refund_status) + " " + refund_status);

                        if(return_sub_total!=null&& !return_sub_total.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_sub_total);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                            /*Log.e("finalAmount","..@@.396...."+finalAmount);*/
                            return_final_amount_tv1.setText(finalAmount);
                           /* return_final_amount_tv1.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                           // return_diamond_price_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        }else{}

                        if(return_total_amount!=null&& !return_total_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_total_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);

                            return_sub_total_tv.setText(finalAmount);
                            /*return_sub_total_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                        }else{}

                        if(return_wallet_points!=null && !return_wallet_points.equalsIgnoreCase("") && !return_wallet_points.equalsIgnoreCase("0"))
                        {
                            return_wallet_apply_point_rel.setVisibility(View.VISIBLE);
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_wallet_points);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            return_wallet_apply_charges_tv.setText("-"+getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        }
                        else{
                            return_wallet_apply_point_rel.setVisibility(View.GONE);
                        }

                        if(return_source_amount!=null&& !return_source_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_source_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                            return_final_amount_tv.setText(finalAmount);
                            /*return_final_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                        }else{}

                        //-----------------------------------------Cancel Order Summary Data End---------------------------------

                        // User Details Data Fetch
                        JSONObject jObjUserDetails = jObjDetails.optJSONObject("user_details");

                        String billing_address = CommonUtility.checkString(jObjUserDetails.optString("billing_address"));
                        String shipping_address = CommonUtility.checkString(jObjUserDetails.optString("shipping_address"));
                        String user_email = CommonUtility.checkString(jObjUserDetails.optString("user_email"));
                        String user_mobile = CommonUtility.checkString(jObjUserDetails.optString("user_mobile"));

                        // Parent Order Summary Object Data Fetch
                        JSONObject jObjParentOrderSummary = jObjDetails.optJSONObject("parent_order_summery");
                        String takFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, jObjParentOrderSummary.optString("tax"));
                        String finalAmount1 = CurrencyUtils.calculateFinalAmount(takFormat, getexchange_rate, currencySymbol);
                        diamond_taxes_tv.setText(finalAmount1);
                        /*diamond_taxes_tv.setText(currencySymbol + "" + CommonUtility.currencyFormat(takFormat));*/

                        JSONObject priceBreakup = jObjParentOrderSummary.optJSONObject("price_breakup");
                        Log.e("priceBreakup","351..@@@.................."+priceBreakup);
                        String currencySymbolreturn = CommonUtility.checkString(jObjDetails.optString("currency_symbol"));
                        Log.e("currencySymbol","274...................."+currencySymbolreturn);
                        if (priceBreakup != null) {

                            JSONObject subtotal = priceBreakup.optJSONObject("subtotal");
                            if (subtotal != null) {
                                if (subtotal.has("gemstone")) {
                                    int gemstoneSubtotal = subtotal.optInt("gemstone", 0);
                                    System.out.println("Gemstone Subtotal: " + gemstoneSubtotal);
                                    String subTotalFormat1 = CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, String.valueOf(gemstoneSubtotal));
                                    String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat1, getexchange_rate, currencySymbol);

                                    gemstone_price_tv.setText(finalAmount);
                                    /*gemstone_price_tv.setText(currencySymbol + " " + CommonUtility.currencyFormat(subTotalFormat1));*/
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

                        /*============================================================================================*/
                        String payment_mode = CommonUtility.checkString(jObjParentOrderSummary.optString("payment_mode"));
                        String sub_total = CommonUtility.checkString(jObjParentOrderSummary.optString("sub_total"));
                        String shipping_charge = CommonUtility.checkString(jObjParentOrderSummary.optString("shipping_charge"));
                        String platform_fee = CommonUtility.checkString(jObjParentOrderSummary.optString("platform_fee"));
                        String bank_charge = CommonUtility.checkString(jObjParentOrderSummary.optString("bank_charge"));
                        String total_charge = CommonUtility.checkString(jObjParentOrderSummary.optString("total_charge"));
                        String total_taxes = CommonUtility.checkString(jObjParentOrderSummary.optString("total_taxes"));
                        String total_amount = CommonUtility.checkString(jObjParentOrderSummary.optString("total_amount"));
                        String coupon_discount = CommonUtility.checkString(jObjParentOrderSummary.optString("coupon_discount"));
                        String wallet_points = CommonUtility.checkString(jObjParentOrderSummary.optString("wallet_points"));
                        String final_amount = CommonUtility.checkString(jObjParentOrderSummary.optString("final_amount"));
                        String transaction_id = CommonUtility.checkString(jObjParentOrderSummary.optString("transaction_id"));

                        if(!sub_total.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, sub_total);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            /*amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                          //  diamond_price_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                            String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);

                            amount_tv.setText(finalAmount);
                            final_amount_tv1.setText(finalAmount);
                            /*final_amount_tv1.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
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
                        order_status_tv.setTextColor(ContextCompat.getColor(context, R.color.red));
                        order_place_date_tv.setText(order_created_at);
                        delivery_date_tv.setText(detailsReturnAt);
                        payment_mode_tv.setText(refund_mode);
                        shipping_address_tv.setText(shipping_address);
                        billing_address_tv.setText(billing_address);
                        contact_no_tv.setText(getResources().getString(R.string.contact_no) + " " +user_mobile);
                        email_tv.setText(getResources().getString(R.string.email_lbl) + " " +user_email);

                        // ------------------Order Summary Data Set Start-------------------------------------------
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


                        if(total_taxes!=null && !total_taxes.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, total_taxes);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            String finalAmount = CurrencyUtils.calculateFinalAmount(subTotalFormat, getexchange_rate, currencySymbol);
                            total_taxes_tv.setText(finalAmount);

                            /*total_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));*/
                        } else{}

                        if(total_amount!=null && !sub_total.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, total_amount);
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

                        if(coupon_discount!=null && !coupon_discount.equalsIgnoreCase("") && !coupon_discount.equalsIgnoreCase("0"))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, coupon_discount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            coupon_code_value_rel.setVisibility(View.VISIBLE); // Coupon Code Apply Value
                            coupon_code_value_tv.setText("-"+getCurrencySymbol + "" + subTotalFormat);
                        }
                        else{}
                        // ------------------Order Summary Data Set End-------------------------------------------

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
                            model.setOrderDateTime(CommonUtility.checkString(detailsReturnAt));
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
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            modelArrayList.get(k).setShowingSubTotal(subTotalFormat);
                            modelArrayList.get(k).setCurrencySymbol(getCurrencySymbol);
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