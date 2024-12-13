package com.diamondxe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.diamondxe.Activity.Dealer.CustomPaymentHistoryScreenActivity;
import com.diamondxe.Activity.Dealer.CustomPaymentScreenActivity;
import com.diamondxe.Activity.MyOrder.MyOrderListScreenActivity;
import com.diamondxe.Activity.PlaceOrder.PlaceOrderScreenActivity;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;

public class PaymentStatusScreenActivity extends SuperActivity implements RecyclerInterface {

    private ImageView payment_status_img;
    private TextView payment_status_tv, payment_status_msg_tv, order_id_tv, amount_tv, cheque_no_tv, payment_mode_tv,order_place_msg_tv, availability_msg_tv,
            my_order_tv, back_to_home_tv, order_id_lbl_tv, amount_lbl_tv, utr_no_lbl_tv, payment_mode_lbl_tv, retry_payment_tv,
            need_assistance_tv, mobile_number_tv;
    private RelativeLayout bg_rel;
    private CardView card_view;
    private LinearLayout shadow_bg_lin, retry_payment_lin, order_home_lin, order_back_home_lin, cheque_no_mode_lin,mobile_lin;

    private Activity activity;
    private Context context;

    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    String referenceNo = "", transactionId = "", currencyCode = "", currencySymbol = "", amount = "", bankCharge = "", finalAmount = "",
            paymentStatus = "",paymentMode="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status_screen);

        context = activity = this;

        bg_rel = findViewById(R.id.bg_rel);
        card_view = findViewById(R.id.card_view);
        shadow_bg_lin = findViewById(R.id.shadow_bg_lin);

        payment_status_img = findViewById(R.id.payment_status_img);

        payment_status_tv = findViewById(R.id.payment_status_tv);
        payment_status_msg_tv = findViewById(R.id.payment_status_msg_tv);
        order_id_tv = findViewById(R.id.order_id_tv);
        amount_tv = findViewById(R.id.amount_tv);
        cheque_no_tv = findViewById(R.id.cheque_no_tv);
        payment_mode_tv = findViewById(R.id.payment_mode_tv);
        order_place_msg_tv = findViewById(R.id.order_place_msg_tv);
        availability_msg_tv = findViewById(R.id.availability_msg_tv);

        order_id_lbl_tv = findViewById(R.id.order_id_lbl_tv);
        amount_lbl_tv = findViewById(R.id.amount_lbl_tv);
        utr_no_lbl_tv = findViewById(R.id.utr_no_lbl_tv);
        payment_mode_lbl_tv = findViewById(R.id.payment_mode_lbl_tv);

        order_back_home_lin = findViewById(R.id.order_back_home_lin);
        cheque_no_mode_lin = findViewById(R.id.cheque_no_mode_lin);

        retry_payment_lin = findViewById(R.id.retry_payment_lin);
        need_assistance_tv = findViewById(R.id.need_assistance_tv);
        order_home_lin = findViewById(R.id.order_home_lin);

        mobile_number_tv = findViewById(R.id.mobile_number_tv);
        mobile_lin = findViewById(R.id.mobile_lin);
        mobile_lin.setOnClickListener(this);

        retry_payment_tv = findViewById(R.id.retry_payment_tv);
        retry_payment_tv.setOnClickListener(this);

        my_order_tv = findViewById(R.id.my_order_tv);
        my_order_tv.setOnClickListener(this);

        back_to_home_tv = findViewById(R.id.back_to_home_tv);
        back_to_home_tv.setOnClickListener(this);

        getCustomPaymentAPI(false);

        // Add a callback to handle back press disable events
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });

    }

    void paymentStatusSuccessfully()
    {
        bg_rel.setBackgroundResource(R.drawable.background_payment_gradient);
        shadow_bg_lin.setBackgroundResource(R.drawable.background_payment_shadow_success);
        card_view.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
        card_view.setElevation(0f);
        card_view.setElevation(0f);

        // Set Payment Status Image According to Status.
        payment_status_img.setBackgroundResource(R.drawable.payment_successful);

        payment_status_tv.setText(getResources().getString(R.string.payment_successful));
        payment_status_msg_tv.setText(getResources().getString(R.string.payment_processed));
        order_place_msg_tv.setText(getResources().getString(R.string.order_placed_successfully));
        availability_msg_tv.setText(getResources().getString(R.string.availability_of_diamond));

        payment_status_tv.setTextColor(ContextCompat.getColor(context, R.color.yellow_dark));
        payment_status_msg_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        order_id_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        amount_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        cheque_no_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        payment_mode_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        order_place_msg_tv.setTextColor(ContextCompat.getColor(context, R.color.yellow_dark));
        availability_msg_tv.setTextColor(ContextCompat.getColor(context, R.color.yellow_dark));
        order_id_lbl_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        amount_lbl_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        utr_no_lbl_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        payment_mode_lbl_tv.setTextColor(ContextCompat.getColor(context, R.color.white));


        setValueInTextView();

        order_back_home_lin.setVisibility(View.VISIBLE);
        cheque_no_mode_lin.setVisibility(View.VISIBLE);

        order_place_msg_tv.setVisibility(View.VISIBLE);
        availability_msg_tv.setVisibility(View.VISIBLE);

        retry_payment_lin.setVisibility(View.GONE);
        need_assistance_tv.setVisibility(View.GONE);
        mobile_lin.setVisibility(View.GONE);
        order_home_lin.setVisibility(View.VISIBLE);

        changeTextLbl();
    }

    void paymentStatusPending()
    {
        bg_rel.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        shadow_bg_lin.setBackgroundResource(R.drawable.background_payment_shadow);
        card_view.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
        card_view.setElevation(10f);
        card_view.setElevation(10f);

        payment_status_tv.setTextColor(ContextCompat.getColor(context, R.color.purple));
        payment_status_msg_tv.setTextColor(ContextCompat.getColor(context, R.color.purple));
        order_id_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        amount_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        cheque_no_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        payment_mode_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        order_place_msg_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        availability_msg_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        order_id_lbl_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        amount_lbl_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        utr_no_lbl_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        payment_mode_lbl_tv.setTextColor(ContextCompat.getColor(context, R.color.black));

        // Set Payment Status Image According to Status.
        payment_status_img.setBackgroundResource(R.drawable.payment_processing);

        payment_status_tv.setText(getResources().getString(R.string.payment_processing));
        payment_status_msg_tv.setText(getResources().getString(R.string.payment_under_review));
        order_place_msg_tv.setText(getResources().getString(R.string.order_placed_successfully));
        availability_msg_tv.setText(getResources().getString(R.string.availability_of_diamond));

        setValueInTextView();

        order_back_home_lin.setVisibility(View.VISIBLE);
        cheque_no_mode_lin.setVisibility(View.VISIBLE);

        order_place_msg_tv.setVisibility(View.VISIBLE);
        availability_msg_tv.setVisibility(View.VISIBLE);

        retry_payment_lin.setVisibility(View.GONE);
        need_assistance_tv.setVisibility(View.GONE);
        mobile_lin.setVisibility(View.GONE);
        order_home_lin.setVisibility(View.VISIBLE);

        changeTextLbl();
    }

    void paymentStatusFailed()
    {
        bg_rel.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        //shadow_bg_lin.setBackgroundResource(R.drawable.background_payment_shadow);
        card_view.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
        card_view.setElevation(20f);
        card_view.setCardElevation(15f);

        payment_status_tv.setTextColor(ContextCompat.getColor(context, R.color.purple));
        payment_status_msg_tv.setTextColor(ContextCompat.getColor(context, R.color.purple));
        order_id_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        amount_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        cheque_no_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        payment_mode_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        order_place_msg_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        availability_msg_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        order_id_lbl_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        amount_lbl_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        utr_no_lbl_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        payment_mode_lbl_tv.setTextColor(ContextCompat.getColor(context, R.color.black));

        // Set Payment Status Image According to Status.
        payment_status_img.setBackgroundResource(R.drawable.payment_failed);

        payment_status_tv.setText(getResources().getString(R.string.payment_failed));
        payment_status_msg_tv.setText(getResources().getString(R.string.payment_failed_msg));

        order_back_home_lin.setVisibility(View.GONE);
        cheque_no_mode_lin.setVisibility(View.GONE);

        order_place_msg_tv.setVisibility(View.GONE);
        availability_msg_tv.setVisibility(View.GONE);

        retry_payment_lin.setVisibility(View.VISIBLE);
        need_assistance_tv.setVisibility(View.VISIBLE);
        mobile_lin.setVisibility(View.VISIBLE);
        order_home_lin.setVisibility(View.GONE);

        changeTextLbl();
    }

    void setValueInTextView()
    {
        if(!referenceNo.equalsIgnoreCase(""))
        {
            order_id_tv.setText(referenceNo);
        }
        else{
            order_id_tv.setText("-");
        }

        if(!finalAmount.equalsIgnoreCase(""))
        {
            amount_tv.setText(currencySymbol + "" + CommonUtility.currencyFormat(finalAmount));
        }else{
            amount_tv.setText("-");
        }

        if(!transactionId.equalsIgnoreCase(""))
        {
            cheque_no_tv.setText(transactionId);
        }
        else{
            cheque_no_tv.setText("-");
        }

        if(!paymentMode.equalsIgnoreCase(""))
        {
            payment_mode_tv.setText(paymentMode);
        }
        else{
            payment_mode_tv.setText("-");
        }
    }

    // Set Lbl According to Payment Mode NFET Set UTR/Cheque No Otherwise set Transaction ID
    void changeTextLbl()
    {
        if(paymentMode.equalsIgnoreCase("NEFT"))
        {
            utr_no_lbl_tv.setText(getResources().getString(R.string.utr_cheque_no));
        }
        else{
            utr_no_lbl_tv.setText(getResources().getString(R.string.transaction_id));
        }
    }

    @Override
    public void onClick(View view)
    {
        Intent intent;
        int id = view.getId();

        if (id == R.id.my_order_tv)
        {
            Log.e("Constant.comeFrom : ", "Constant.comeFrom11 : " + Constant.comeFrom.toString());
            // Check If User Payment Type Custom Payment Or Not.
            if(Constant.comeFrom.equalsIgnoreCase("customPayment"))
            {
                intent = new Intent(activity, CustomPaymentHistoryScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
            else{
                Constant.afterCancelOrderManageScreenCall="";
                Constant.afterReturnOrderManageScreenCall="";
                intent = new Intent(activity, MyOrderListScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        }
        else if (id == R.id.back_to_home_tv)
        {
            Utils.hideKeyboard(activity);

            intent = new Intent(activity, HomeScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
        else if(id == R.id.mobile_lin)
        {
            Utils.hideKeyboard(activity);
            CommonUtility.makeACallIntent(context);
        }
        else if(id == R.id.retry_payment_tv)
        {
            Utils.hideKeyboard(activity);
            // Check If User Payment Type Custom Payment Or Not.
            if(Constant.comeFrom.equalsIgnoreCase("customPayment"))
            {
                intent = new Intent(activity, CustomPaymentScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
            else{
                intent = new Intent(activity, PlaceOrderScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        }
    }

    public void getCustomPaymentAPI(boolean showLoader)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);
            urlParameter.put("orderId", Constant.paymentOrderID);

            Log.e("comeFrom","..365........"+Constant.comeFrom);
            Log.e("paymentOrderID","..366........"+Constant.paymentOrderID);
            vollyApiActivity = null;

            if(Constant.comeFrom.equalsIgnoreCase("customPayment"))
            {
                vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.CUSTOM_PAYMENT_STATUS,
                        ApiConstants.CUSTOM_PAYMENT_STATUS_ID,showLoader, "POST");
            }
            else if(Constant.comeFrom.equalsIgnoreCase("APISOLUTION"))
            {
                vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.SOLUTION_ACCOUNT_RECHARGE_STATUS,
                        ApiConstants.SOLUTION_ACCOUNT_RECHARGE_STATUS_ID,showLoader, "POST");
            }
            else
            {
                Log.e("In else,,,,","381....@@@@@@@@@@>....................");
                vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.ORDER_PAYMENT_STATUS,
                        ApiConstants.ORDER_PAYMENT_STATUS_ID,showLoader, "POST");
            }

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID)
    {
        try {
            Log.v("------Diamond----- : ", "--------JSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {
                case ApiConstants.CUSTOM_PAYMENT_STATUS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                         referenceNo = CommonUtility.checkString(jObjDetails.optString("reference_no"));
                         transactionId = CommonUtility.checkString(jObjDetails.optString("transaction_id"));
                         currencyCode = CommonUtility.checkString(jObjDetails.optString("currency_code"));
                         currencySymbol = CommonUtility.checkString(jObjDetails.optString("currency_symbol"));
                         amount = CommonUtility.checkString(jObjDetails.optString("amount"));
                         bankCharge = CommonUtility.checkString(jObjDetails.optString("bank_charge"));
                         finalAmount = CommonUtility.checkString(jObjDetails.optString("final_amount"));
                         paymentMode = CommonUtility.checkString(jObjDetails.optString("payment_mode"));
                         paymentStatus = CommonUtility.checkString(jObjDetails.optString("payment_status"));
                         Log.e("paymentStatus","..413...."+paymentStatus);
                        if(paymentStatus.equalsIgnoreCase("Paid"))
                        {
                            paymentStatusSuccessfully();
                        }
                        else if(paymentStatus.equalsIgnoreCase("In-Progress") || paymentStatus.equalsIgnoreCase("Pending"))
                        {
                            paymentStatusPending();
                        }
                        else if(paymentStatus.equalsIgnoreCase("Failed"))
                        {
                            paymentStatusFailed();
                        }
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        Toast.makeText(activity, "" + message , Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }
                    break;



                case ApiConstants.ORDER_PAYMENT_STATUS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Log.e("Order Payment : ", "Order Payment..449*********");
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        referenceNo = CommonUtility.checkString(jObjDetails.optString("order_id"));
                        transactionId = CommonUtility.checkString(jObjDetails.optString("transaction_id"));
                        currencyCode = CommonUtility.checkString(jObjDetails.optString("currency_code"));
                        currencySymbol = CommonUtility.checkString(jObjDetails.optString("currency_symbol"));
                        amount = CommonUtility.checkString(jObjDetails.optString("amount"));
                        bankCharge = CommonUtility.checkString(jObjDetails.optString("bank_charge"));
                        finalAmount = CommonUtility.checkString(jObjDetails.optString("final_amount"));
                        paymentMode = CommonUtility.checkString(jObjDetails.optString("payment_mode"));
                        paymentStatus = CommonUtility.checkString(jObjDetails.optString("payment_status"));

                        if(paymentStatus.equalsIgnoreCase("Paid"))
                        {
                            paymentStatusSuccessfully();
                        }
                        else if(paymentStatus.equalsIgnoreCase("In-Progress") || paymentStatus.equalsIgnoreCase("Pending"))
                        {
                            paymentStatusPending();
                        }
                        else if(paymentStatus.equalsIgnoreCase("Failed"))
                        {
                            paymentStatusFailed();
                        }
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        Toast.makeText(activity, "" + message , Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.SOLUTION_ACCOUNT_RECHARGE_STATUS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Log.e("SOLUTION_ACCOUNT_RECHARGE_STATUS_ID","Success call......@@@@@@@@@@@@@@@@@@.......................");
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        referenceNo = CommonUtility.checkString(jObjDetails.optString("reference_no"));
                        transactionId = CommonUtility.checkString(jObjDetails.optString("transaction_id"));
                        currencyCode = CommonUtility.checkString(jObjDetails.optString("currency_code"));
                        currencySymbol = CommonUtility.checkString(jObjDetails.optString("currency_symbol"));
                        amount = CommonUtility.checkString(jObjDetails.optString("amount"));
                        bankCharge = CommonUtility.checkString(jObjDetails.optString("bank_charge"));
                        finalAmount = CommonUtility.checkString(jObjDetails.optString("final_amount"));
                        paymentMode = CommonUtility.checkString(jObjDetails.optString("payment_mode"));
                        paymentStatus = CommonUtility.checkString(jObjDetails.optString("payment_status"));

                        Log.e("finalAmount","Success call..1....."+finalAmount);
                        Log.e("amount","Success call..2....."+amount);
                        Log.e("transactionId","Success call...3...."+transactionId);
                        Log.e("paymentStatus","Success call..4....."+paymentStatus);


                        if(paymentStatus.equalsIgnoreCase("Paid"))
                        {
                            paymentStatusSuccessfully();
                        }
                        else if(paymentStatus.equalsIgnoreCase("In-Progress") || paymentStatus.equalsIgnoreCase("Pending"))
                        {
                            paymentStatusPending();
                        }
                        else if(paymentStatus.equalsIgnoreCase("Failed"))
                        {
                            paymentStatusFailed();
                        }
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        Toast.makeText(activity, "" + message , Toast.LENGTH_SHORT).show();
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
    public void itemClick(int position, String action) {

    }
}