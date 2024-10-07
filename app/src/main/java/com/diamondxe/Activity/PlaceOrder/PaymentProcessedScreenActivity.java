package com.diamondxe.Activity.PlaceOrder;

import static com.diamondxe.Utils.PaymentUtils.API_END_POINT;
import static com.diamondxe.Utils.PaymentUtils.MERCHANT_ID;
import static com.diamondxe.Utils.PaymentUtils.SALT;
import static com.diamondxe.Utils.PaymentUtils.SALT_INDEX;
import static com.diamondxe.Utils.PaymentUtils.TARGET_URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.diamondxe.Activity.PaymentStatusScreenActivity;
import com.diamondxe.Adapter.Dealer.AllBankNameListAdapter;
import com.diamondxe.Adapter.Dealer.BankNEFTListAdapter;
import com.diamondxe.Adapter.Dealer.PopularBankListAdapter;
import com.diamondxe.Adapter.UPIOptionListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.AddToCartListModel;
import com.diamondxe.Beans.Dealer.BankNEFTListModel;
import com.diamondxe.Beans.UPIAppInfoListModel;
import com.diamondxe.Interface.DialogItemClickInterface;
import com.diamondxe.Interface.PaymentResultCallback;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.PaymentUtils;
import com.diamondxe.Utils.Utils;
import com.google.android.material.tabs.TabLayout;
import com.phonepe.intent.sdk.api.B2BPGRequest;
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder;
import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.PhonePeInitException;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public class PaymentProcessedScreenActivity extends SuperActivity implements RecyclerInterface, DialogItemClickInterface, PaymentResultCallback {

    private ImageView back_img, shipping_img, kyc_img, payment_img, drop_arrow_img, cross_coupon_img, points_drop_arrow_img,cross_wallet_img;
    private RelativeLayout shipping_rel, kyc_rel, payment_rel, cross_coupon_rel, viewpager_layout, rel_wallet, rel_loyalty, rel_coupon,
            rel_other_tax, rel_diamond_tax, coupon_code_value_rel, coupon_error_rel, wallet_apply_point_rel,cross_wallet_rel;
    private CardView shipping_card_view, kyc_card_view, payment_card_view, points_summary_view_card;
    private LinearLayout points_details_lin, view_order_summary_details_lin, apply_wallet_points_lin, apply_loyalty_points_lin, apply_coupon_points_lin;
    private TextView proceed_to_pay_tv, apply_wallet_points_tv, apply_loyalty_points_tv, apply_coupon_tv, coupon_code_value_tv,wallet_apply_charges_tv;
    private EditText coupon_point_et, wallet_point_et, loyalty_point_et;
    private CardView order_summary_view_card;

    //-----------------------------------------For Payment-----------------------------------------------------------------
    private LinearLayout  show_bank_name_lin, bank_account_details_lin, show_all_bank_lin, upi_option_lin, credit_card_main_lin, net_banking_main_lin;
    private TextView rtgs_tv, card_type_tv, net_banking_tv,name_error_tv, company_name_error_tv,
            amount_error_tv, remark_error_tv,branch_name_tv,ifsc_code_tv,swift_code_tv,bank_name_tv,account_number_tv,mode_payment_tv,select_date_tv,
            payment_mode_error_tv, cheque_no_error_tv, date_error_tv, cheque_mode_tv, neft_mode_tv, rgts_mode_tv, wire_transfer_mode_tv,others_mode_tv,
            payment_option_error_tv,select_bank_tv, select_mode_lbl, select_bank_error_tv,total_amount_tv, final_amount_tv1,shipping_and_handling_tv, platform_fees_tv, total_charges_tv, other_taxes_tv, diamond_taxes_tv, total_taxes_tv,
            sub_total_tv, bank_charges_tv, final_amount_tv, others_txt_gst_perc_tv, diamond_txt_gst_perc_tv,available_wallet_points_tv;
    private EditText  cheque_et;
    private RelativeLayout proceed_to_pay_rel, rtgs_rel, card_type_rel, net_banking_rel, upi_rel, mode_payment_rel,select_date_rel,
            cheque_rel, show_all_bank_rel;
    private ImageView upi_option_down_arrow_img;
    boolean isSelectNetBankingBank = false;

    private RecyclerView recycler_view, recycler_view_upi;
    ArrayList<BankNEFTListModel> modelArrayList;
    ArrayList<BankNEFTListModel> popularBankArrayList;
    ArrayList<BankNEFTListModel> allBankArrayList;
    BankNEFTListAdapter adapter;
    PopularBankListAdapter popularBankListAdapter;
    AllBankNameListAdapter allBankNameListAdapter;
    ArrayList<UPIAppInfoListModel> upiAppsArrayList;
    UPIOptionListAdapter upiOptionListAdapter;
    String currencySymbol = "", bankCharge = "", bankChargePerc = "", totalAmount = "", bankID = "",callbackUrl="";
    private static final int PHONEPE_PAYMENT_REQUEST_CODE = 1001;
    int lastPosition = 0;
    double neftCharges = 0;
    double upiCharges = 0;
    double netBankingCharges = 0;
    double cardCharges = 0;
    boolean isWalletPointApplied = false;
    boolean isCouponCodeApplied = false;
    B2BPGRequest b2BPGRequest;
    String payloadBase64 = "", checksum="", amountInPaisaString="",dob="",send_dob_server="",  selectedUPIPackage="", finalSubmitValue="";
    String paymentModeType= "NEFT"; // By Default Payment Mode NEFT
    double amountInRupees = 0;
    private boolean isArrowDownUPI = true;
    String NEFT = "NEFT";
    String CREDIT_CARD = "CreditCard";
    String NET_BANKING = "NetBanking";
    String UPI = "UPI";
    String PAYMENT_BY_UPI = "UPI_INTENT";
    String PAYMENT_BY_NET_BANKING = "NET_BANKING";
    //String PAYMENT_BY_CARD = "CARD";
    String PAYMENT_BY_CARD = "PAY_PAGE";
    String shippingCountryName = "United Arab Emirates";

    //-------------------------------------------------Payment End-------------------------------------------------------------
    ViewPager viewPager;
    TabLayout tabLayout;

    private Activity activity;
    private Context context;
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    private boolean isArrowDown = false;
    private boolean isArrowDownPoint = false;
    public ArrayList<AddToCartListModel> orderItemArrayList;
    int newWith;
    int width;
    Handler handler1 = new Handler(Looper.getMainLooper());
    String selectedCurrencyValue ="",selectedCurrencyCode = "",selectedCurrencyDesc="",selectedCurrencyImage="";
    String isCoupanApplied = "", orderCouponCode = "", orderCouponValue = "", orderCouponDiscount = "", orderSubTotal = "", orderCgst = "", orderCgstPerc = "",
            orderSgst = "", orderSgstPerc = "", orderIgst = "", orderIgstPerc = "", orderDiscountPerc = "", orderTax = "", orderSubTotalWithTax = "", orderShippingCharge = "", orderPlatformFee = "",
            orderTotalCharge = "", orderTotalChargeTax = "", orderTotalChargeWithTax = "", orderTotalTaxes = "", orderTotalAmount = "", orderTaxPerOnCharges = "", orderFinalAmount = "", orderBankCharge = "", orderBankChargePerc = "",orderFinalAmountWithOutFormat="",
            orderCouponStatus="", orderCouponMessage="",orderWalletPonits="",availableWalletPoints="",userRole="";
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_processed_screen);

        context = activity = this;

        orderItemArrayList = new ArrayList<>();

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        userRole = CommonUtility.getGlobalString(activity, "login_user_role");

        shipping_img = findViewById(R.id.shipping_img);
        kyc_img = findViewById(R.id.kyc_img);
        payment_img = findViewById(R.id.payment_img);

        shipping_rel = findViewById(R.id.shipping_rel);
        kyc_rel = findViewById(R.id.kyc_rel);
        payment_rel = findViewById(R.id.payment_rel);

        shipping_card_view = findViewById(R.id.shipping_card_view);
        shipping_card_view.setOnClickListener(this);

        kyc_card_view = findViewById(R.id.kyc_card_view);
        kyc_card_view.setOnClickListener(this);

        payment_card_view = findViewById(R.id.payment_card_view);
        payment_card_view.setOnClickListener(this);

        drop_arrow_img = findViewById(R.id.drop_arrow_img);
        points_drop_arrow_img = findViewById(R.id.points_drop_arrow_img);
        points_details_lin = findViewById(R.id.points_details_lin);

        points_summary_view_card = findViewById(R.id.points_summary_view_card);
        points_summary_view_card.setOnClickListener(this);

        proceed_to_pay_tv = findViewById(R.id.proceed_to_pay_tv);
        proceed_to_pay_tv.setOnClickListener(this);

        wallet_apply_point_rel = findViewById(R.id.wallet_apply_point_rel);
        wallet_apply_charges_tv = findViewById(R.id.wallet_apply_charges_tv);
        rel_wallet = findViewById(R.id.rel_wallet);
        wallet_point_et = findViewById(R.id.wallet_point_et);
        apply_wallet_points_lin = findViewById(R.id.apply_wallet_points_lin);
        apply_wallet_points_tv = findViewById(R.id.apply_wallet_points_tv);
        apply_wallet_points_tv.setOnClickListener(this);

        rel_loyalty = findViewById(R.id.rel_loyalty);
        loyalty_point_et = findViewById(R.id.loyalty_point_et);
        apply_loyalty_points_lin = findViewById(R.id.apply_loyalty_points_lin);
        apply_loyalty_points_tv = findViewById(R.id.apply_loyalty_points_tv);
        apply_loyalty_points_tv.setOnClickListener(this);

        coupon_code_value_rel = findViewById(R.id.coupon_code_value_rel);
        coupon_code_value_tv = findViewById(R.id.coupon_code_value_tv);

        rel_coupon = findViewById(R.id.rel_coupon);
        apply_coupon_points_lin = findViewById(R.id.apply_coupon_points_lin);
        apply_coupon_tv = findViewById(R.id.apply_coupon_tv);
        apply_coupon_tv.setOnClickListener(this);

        coupon_point_et = findViewById(R.id.coupon_point_et);
        cross_coupon_rel = findViewById(R.id.cross_coupon_rel);

        coupon_error_rel = findViewById(R.id.coupon_error_rel);

        cross_coupon_img = findViewById(R.id.cross_coupon_img);
        cross_coupon_img.setOnClickListener(this);

        cross_wallet_rel = findViewById(R.id.cross_wallet_rel);

        cross_wallet_img = findViewById(R.id.cross_wallet_img);
        cross_wallet_img.setOnClickListener(this);

        order_summary_view_card = findViewById(R.id.order_summary_view_card);
        order_summary_view_card.setOnClickListener(this);

        view_order_summary_details_lin = findViewById(R.id.view_order_summary_details_lin);

        viewPager = findViewById (R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        tabLayout = findViewById(R.id.tab_layout);
        viewpager_layout = findViewById(R.id.viewpager_layout);

        newWith = (int) (width/1.2);

        // Show Item Summary Items View Pager.
        setPlaceItemListPager();

        //-----------------------------------------For Payment-------------------------------------------------------------

        modelArrayList = new ArrayList<>();
        upiAppsArrayList = new ArrayList<>();
        popularBankArrayList = new ArrayList<>();
        allBankArrayList = new ArrayList<>();

        total_amount_tv = findViewById(R.id.total_amount_tv);
        final_amount_tv1 = findViewById(R.id.final_amount_tv1);

        shipping_and_handling_tv = findViewById(R.id.shipping_and_handling_tv);
        platform_fees_tv = findViewById(R.id.platform_fees_tv);
        total_charges_tv = findViewById(R.id.total_charges_tv);
        other_taxes_tv = findViewById(R.id.other_taxes_tv);
        diamond_taxes_tv = findViewById(R.id.diamond_taxes_tv);
        total_taxes_tv = findViewById(R.id.total_taxes_tv);
        sub_total_tv = findViewById(R.id.sub_total_tv);
        bank_charges_tv = findViewById(R.id.bank_charges_tv);
        final_amount_tv = findViewById(R.id.final_amount_tv);
        others_txt_gst_perc_tv = findViewById(R.id.others_txt_gst_perc_tv);
        diamond_txt_gst_perc_tv = findViewById(R.id.diamond_txt_gst_perc_tv);

        available_wallet_points_tv = findViewById(R.id.available_wallet_points_tv);

        rel_other_tax = findViewById(R.id.rel_other_tax);
        rel_other_tax.setOnClickListener(this);

        rel_diamond_tax = findViewById(R.id.rel_diamond_tax);
        rel_diamond_tax.setOnClickListener(this);

        cheque_et = findViewById(R.id.cheque_et);

        remark_error_tv = findViewById(R.id.remark_error_tv);

        rtgs_tv = findViewById(R.id.rtgs_tv);
        card_type_tv = findViewById(R.id.card_type_tv);
        net_banking_tv = findViewById(R.id.net_banking_tv);

        rtgs_rel = findViewById(R.id.rtgs_rel);
        rtgs_rel.setOnClickListener(this);

        card_type_rel = findViewById(R.id.card_type_rel);
        card_type_rel.setOnClickListener(this);

        net_banking_rel = findViewById(R.id.net_banking_rel);
        net_banking_rel.setOnClickListener(this);

        recycler_view = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setNestedScrollingEnabled(false);

        recycler_view_upi = findViewById(R.id.recycler_view_upi);
        LinearLayoutManager layoutManagerUPI = new LinearLayoutManager(activity);
        layoutManagerUPI.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_view_upi.setLayoutManager(layoutManagerUPI);
        recycler_view_upi.setNestedScrollingEnabled(false);

        show_bank_name_lin = findViewById(R.id.show_bank_name_lin);
        bank_account_details_lin = findViewById(R.id.bank_account_details_lin);
        show_all_bank_lin = findViewById(R.id.show_all_bank_lin);

        show_all_bank_rel = findViewById(R.id.show_all_bank_rel);
        show_all_bank_rel.setOnClickListener(this);

        select_bank_tv = findViewById(R.id.select_bank_tv);

        branch_name_tv = findViewById(R.id.branch_name_tv);
        ifsc_code_tv = findViewById(R.id.ifsc_code_tv);
        swift_code_tv = findViewById(R.id.swift_code_tv);
        bank_name_tv = findViewById(R.id.bank_name_tv);
        account_number_tv = findViewById(R.id.account_number_tv);
        mode_payment_tv = findViewById(R.id.mode_payment_tv);
        select_date_tv = findViewById(R.id.select_date_tv);

        mode_payment_rel = findViewById(R.id.mode_payment_rel);
        mode_payment_rel.setOnClickListener(this);

        payment_mode_error_tv = findViewById(R.id.payment_mode_error_tv);
        cheque_rel = findViewById(R.id.cheque_rel);
        cheque_no_error_tv = findViewById(R.id.cheque_no_error_tv);
        date_error_tv = findViewById(R.id.date_error_tv);

        select_bank_error_tv = findViewById(R.id.select_bank_error_tv);

        payment_option_error_tv = findViewById(R.id.payment_option_error_tv);
        upi_option_down_arrow_img = findViewById(R.id.upi_option_down_arrow_img);

        select_date_rel = findViewById(R.id.select_date_rel);
        select_date_rel.setOnClickListener(this);

        upi_rel = findViewById(R.id.upi_rel);
        upi_rel.setOnClickListener(this);

        upi_option_lin = findViewById(R.id.upi_option_lin);
        credit_card_main_lin = findViewById(R.id.credit_card_main_lin);
        net_banking_main_lin = findViewById(R.id.net_banking_main_lin);


        if(Constant.shippingAddressNameForShowHidePaymentOption.equalsIgnoreCase(shippingCountryName))
        {
            upi_option_lin.setVisibility(View.GONE);
            credit_card_main_lin.setVisibility(View.GONE);
            net_banking_main_lin.setVisibility(View.GONE);

            cheque_et.setHint(getResources().getString(R.string.enter_utr));

            rtgs_tv.setText(getResources().getString(R.string.bank_transfer));
        }
        else{
            upi_option_lin.setVisibility(View.VISIBLE);
            credit_card_main_lin.setVisibility(View.VISIBLE);
            net_banking_main_lin.setVisibility(View.VISIBLE);

            cheque_et.setHint(getResources().getString(R.string.enter_utr_cheque_no));

            rtgs_tv.setText(getResources().getString(R.string.rtgs_neft));
        }

        // Initialize PhonePe
        PaymentUtils.initializePhonePe(this);

        //PhonePe.init(getApplicationContext(), PhonePeEnvironment.SANDBOX, MERCHANT_ID, SALT); // For SendBox
        //PhonePe.init(getApplicationContext(), PhonePeEnvironment.RELEASE, MERCHANT_ID, SALT); // For Live
        //PhonePe.init(this);

        removeEditTextValidationError();

        upiAppsArrayList = getInstalledUPIApps();

        showUPIAppsOption(upiAppsArrayList);

        getDXEBankDetailsAPI(true);
        getPaymentOptionAPI(false);
        getBankChargesAPI(true);

        // Set Alpha For Background.
        apply_wallet_points_lin.setAlpha(0.5f);
        apply_loyalty_points_lin.setAlpha(0.5f);
        apply_coupon_points_lin.setAlpha(0.5f);
        //-------------------------------------------End Payment----------------------------------------------------------

        // Coupon Code Value Text Change Listener
        coupon_point_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!coupon_point_et.getText().toString().equalsIgnoreCase(""))
                {
                    apply_coupon_points_lin.setAlpha(1f);
                    cross_coupon_rel.setVisibility(View.VISIBLE); // Cross Icon Layout

                    if(orderCouponStatus.equalsIgnoreCase("1"))
                    {
                        apply_coupon_points_lin.setBackgroundResource(R.drawable.verify_button_shadow);
                        apply_coupon_tv.setText(getResources().getString(R.string.apply));
                        isCouponCodeApplied = false;
                    }
                }

                if(coupon_point_et.getText().toString().equalsIgnoreCase(""))
                {
                    apply_coupon_points_lin.setAlpha(0.5f);
                    cross_coupon_rel.setVisibility(View.GONE); // Cross Icon Layout
                    isCouponCodeApplied = false;
                    clearCouponCode();

                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Wallet Point Value Text Change Listener
        wallet_point_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!wallet_point_et.getText().toString().equalsIgnoreCase(""))
                {
                    apply_wallet_points_lin.setAlpha(1f);
                    cross_wallet_rel.setVisibility(View.VISIBLE); // Cross Icon and View Line Layout

                    apply_wallet_points_lin.setBackgroundResource(R.drawable.verify_button_shadow);
                    apply_wallet_points_tv.setText(getResources().getString(R.string.apply));
                    isWalletPointApplied = false;
                }

                if(wallet_point_et.getText().toString().equalsIgnoreCase(""))
                {
                    apply_wallet_points_lin.setAlpha(0.5f);
                    cross_wallet_rel.setVisibility(View.GONE); // Cross Icon and View Line Layout
                    isWalletPointApplied = false;
                    clearWalletPointApply();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Under This Method Check Payment Status Code and Move Payment Status Screen.
        afterPaymentCheckStatus();

        getCurrencyData();
        getCheckOutDetailsAPI(true, "", "", "");

    }
    // Get Currency Value Code and Image
    void getCurrencyData()
    {
        selectedCurrencyValue = CommonUtility.getGlobalString(context, "selected_currency_value");
        selectedCurrencyCode = CommonUtility.getGlobalString(context, "selected_currency_code");
        selectedCurrencyDesc = CommonUtility.getGlobalString(context, "selected_currency_desc");
        selectedCurrencyImage = CommonUtility.getGlobalString(context, "selected_currency_image");
    }

    // Remove Validation Error Under EditText.
    void removeEditTextValidationError()
    {
        // Loyalty Points Condition
        /*loyalty_point_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!loyalty_point_et.getText().toString().equalsIgnoreCase(""))
                {
                    apply_loyalty_points_lin.setAlpha(1f);
                    rel_loyalty.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(loyalty_point_et.getText().toString().equalsIgnoreCase(""))
                {
                    apply_loyalty_points_lin.setAlpha(0.5f);
                    rel_loyalty.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });*/

        cheque_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!cheque_et.getText().toString().equalsIgnoreCase("")){
                    cheque_no_error_tv.setVisibility(View.GONE);
                    cheque_rel.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(cheque_et.getText().toString().equalsIgnoreCase("")){
                    cheque_no_error_tv.setVisibility(View.GONE);
                    cheque_rel.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // If NEFt Selected By Default
        if(paymentModeType.equalsIgnoreCase(NEFT))
        {
            byDefaultNEFTSelected();
        }else{}
    }

    // IF RTGS/NEFT BY Default Selected
    void byDefaultNEFTSelected()
    {
        rtgs_rel.setBackgroundResource(R.drawable.background_select_payment_option);
        card_type_rel.setBackgroundResource(R.drawable.background_payment_option);
        net_banking_rel.setBackgroundResource(R.drawable.background_payment_option);

        rtgs_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
        card_type_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        net_banking_tv.setTextColor(ContextCompat.getColor(context, R.color.black));

        // If Payment Option Selected After that Error Gone
        payment_option_error_tv.setVisibility(View.GONE);

        show_bank_name_lin.setVisibility(View.VISIBLE); // Bank List Layout
        bank_account_details_lin.setVisibility(View.VISIBLE); // Bank Account Details Layout
        show_all_bank_lin.setVisibility(View.GONE); // Select Bank DropDown Layout Gone
    }

    // Create Check Sum For Payment Initiated and Open PhonePay.
    void createCheckSumPaymentInitiatedAndOpenPhonePe()
    {
        amountInPaisaString = orderFinalAmountWithOutFormat;

        JSONObject data = new JSONObject();
        try {

            // Convert the string to an integer value to ensure it's valid
            int amountInPaisa = Integer.parseInt(orderFinalAmountWithOutFormat);

            //data.put("merchantTransactionId", CommonUtility.generateTransactionId()); // String. Mandatory
            data.put("merchantTransactionId", Constant.paymentOrderID); // String. Mandatory
            data.put("merchantId", MERCHANT_ID); // String. Mandatory
            data.put("merchantUserId", Constant.paymentUserID); // String. Mandatory
            data.put("amount", amountInPaisa * 100);
            data.put("mobileNumber", CommonUtility.getGlobalString(activity, "login_mobile_no")); // String. Optional
            data.put("callbackUrl", callbackUrl); // String. Mandatory

            // Logging the amount for verification
            Log.d("AmountConversion", "Amount in Paisa: " + amountInPaisa);
            //Log.d("AmountConversion", "Amount in Paisa1: " + orderFinalAmountWithOutFormat);

            JSONObject paymentInstrument = new JSONObject();

            // Check Payment Mode Type and Set Type.
            if(paymentModeType.equalsIgnoreCase(UPI))
            {
                // For UPI
                paymentInstrument.put("type", PAYMENT_BY_UPI);
                paymentInstrument.put("targetApp", selectedUPIPackage);
            }
            else if(paymentModeType.equalsIgnoreCase(NET_BANKING))
            {
                // For Net Banking
                paymentInstrument.put("type", PAYMENT_BY_NET_BANKING);
                paymentInstrument.put("bankId", bankID);
            }
            else if(paymentModeType.equalsIgnoreCase(CREDIT_CARD))
            {
                // For Credit/Debit Card
                paymentInstrument.put("type", PAYMENT_BY_CARD);
            }

            //paymentInstrument.put("targetApp", TARGET_URL);

            data.put("paymentInstrument", paymentInstrument); // OBJECT. Mandatory

            JSONObject deviceContext = new JSONObject();
            deviceContext.put("deviceOS", "ANDROID");
            data.put("deviceContext", deviceContext);

            payloadBase64 = Base64.encodeToString(data.toString().getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
            checksum = CommonUtility.sha256(payloadBase64 + API_END_POINT + SALT) + "###"+SALT_INDEX;

            b2BPGRequest = new B2BPGRequestBuilder()
                    .setData(payloadBase64)
                    .setChecksum(checksum)
                    .setUrl(API_END_POINT)
                    .build();

            Log.d("PAPAYACODERS", "onCreate: " + data.toString());
            Log.d("PAPAYACODERS", "onCreate: " + payloadBase64);
            Log.d("PAPAYACODERS", "onCreate: " + checksum);
            Log.d("PAPAYACODERS", "onCreate: " + b2BPGRequest);

            // Open Phone Pe Here
            try {
                Intent intent1;
                if(selectedUPIPackage.equalsIgnoreCase(""))
                {
                    intent1 = PhonePe.getImplicitIntent(context, b2BPGRequest, TARGET_URL);
                }
                else{
                    intent1 = PhonePe.getImplicitIntent(context, b2BPGRequest, selectedUPIPackage);
                }
                if (intent1 != null) {
                    Log.d("phonepe", "Intent created successfully");
                    startActivityForResult(intent1, 1);
                } else {
                    Log.e("phonepe", "Intent is null");
                }
            } catch (PhonePeInitException e) {
                Log.e("phonepe", "PhonePe initialization error", e);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("AmountConversion", "Invalid amount format");
        }

    }

    /*void createCheckSumPaymentInitiatedAndOpenPhonePe()
    {
        CommonUtility.createCheckSumPaymentInitiatedAndOpenPhonePe(activity, orderFinalAmountWithOutFormat, paymentModeType, selectedUPIPackage,
                callbackUrl, bankID, this);
    }*/

    // Check Status When Payment Process Complete.
    void afterPaymentCheckStatus()
    {
        // Initialize the ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            // Get the transaction result from the Intent
                            String txnResult = data.getStringExtra("key_txn_result");
                            if (txnResult != null) {
                                Log.d("PAPAYACODERS", "Non-JSON response: " + txnResult);
                            } else {
                                Log.d("PAPAYACODERS", "No transaction result data found");
                                Toast.makeText(activity, "Else : " + result.getResultCode(), Toast.LENGTH_SHORT).show();
                            }
                            Constant.comeFrom = "diamondOrder";
                            Intent intent = new Intent(activity, PaymentStatusScreenActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            finish();
                        } else if (result.getResultCode() == RESULT_CANCELED) {
                            Log.d("PAPAYACODERS", "Transaction Failed");
                        } else {
                            Log.d("PAPAYACODERS", "Transaction failed or no data returned");
                        }
                    }
                }
        );
    }

    // If Payment Mode Not Selected NEFT/RGTS Then Value Blank
    void editTextAndTextViewValueBlank()
    {
        mode_payment_tv.setText("");
        mode_payment_tv.setHint(getResources().getString(R.string.select_payment_mode));

        cheque_et.setText("");

        if(Constant.shippingAddressNameForShowHidePaymentOption.equalsIgnoreCase(shippingCountryName))
        {
            cheque_et.setHint(getResources().getString(R.string.enter_utr));
        }
        else{
            cheque_et.setHint(getResources().getString(R.string.enter_utr_cheque_no));
        }

        select_date_tv.setText("");
        select_date_tv.setHint(getResources().getString(R.string.select_date_month_year));
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
        else if(id == R.id.cross_coupon_img)
        {
            Utils.hideKeyboard(activity);
            coupon_point_et.setText("");
            coupon_point_et.setHint(getResources().getString(R.string.enter_coupon_code));
        }
        else if(id == R.id.cross_wallet_img)
        {
            Utils.hideKeyboard(activity);
            wallet_point_et.setText("");
            wallet_point_et.setHint(getResources().getString(R.string.enter_wallet_points));
        }
        else if(id == R.id.points_summary_view_card)
        {
            Utils.hideKeyboard(activity);
            if (isArrowDownPoint) {
                points_drop_arrow_img.setImageResource(R.drawable.down);
                points_details_lin.setVisibility(View.GONE);
            } else {
                points_drop_arrow_img.setImageResource(R.drawable.up);
                points_details_lin.setVisibility(View.VISIBLE);
            }
            isArrowDownPoint = !isArrowDownPoint;
        }

        else if(id == R.id.shipping_card_view)
        {
            Utils.hideKeyboard(activity);
            intent = new Intent(activity, PlaceOrderScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        }
        else if(id == R.id.kyc_card_view)
        {
            Utils.hideKeyboard(activity);
            if(userRole.equalsIgnoreCase("BUYER"))
            {
                if(Constant.documentStatus.equalsIgnoreCase("0"))
                {
                    intent = new Intent(activity, PlaceOrderBuyerKYCVerificationDocUploadActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();
                }
                else{
                    intent = new Intent(activity, PlaceOrderBuyerKYCVerificationActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();
                }
            }
            else{
                intent = new Intent(activity, PlaceOrderKYCVerificationActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }

        }
        else if(id == R.id.payment_card_view)
        {
            Utils.hideKeyboard(activity);

            //selectPaymentIcon();
        }
        else if(id == R.id.apply_wallet_points_tv)
        {
            Utils.hideKeyboard(activity);
            if(wallet_point_et.getText().toString().trim().equalsIgnoreCase(""))
            {
                wallet_point_et.requestFocus();
                rel_wallet.setBackgroundResource(R.drawable.border_red_line_view);
            } else{
                if(!isWalletPointApplied)
                {
                    getCheckOutDetailsAPI(false, coupon_point_et.getText().toString().trim(), wallet_point_et.getText().toString().trim(),
                            "");
                }else{}
            }
        }
        else if(id == R.id.apply_loyalty_points_tv)
        {
            Utils.hideKeyboard(activity);
            if(loyalty_point_et.getText().toString().trim().equalsIgnoreCase(""))
            {
                loyalty_point_et.requestFocus();
                rel_loyalty.setBackgroundResource(R.drawable.border_red_line_view);
            } else{}
        }
        else if(id == R.id.apply_coupon_tv)
        {
            Utils.hideKeyboard(activity);
            if(coupon_point_et.getText().toString().trim().equalsIgnoreCase(""))
            {
                coupon_point_et.requestFocus();
                rel_coupon.setBackgroundResource(R.drawable.border_red_line_view);
            }
            else
            {
                if(!isCouponCodeApplied)
                {
                    getCheckOutDetailsAPI(false, coupon_point_et.getText().toString().trim(), wallet_point_et.getText().toString().trim(),
                            "");
                }else{}
            }
        }
        else if(id == R.id.order_summary_view_card)
        {
            Utils.hideKeyboard(activity);
            if (isArrowDown) {
                drop_arrow_img.setImageResource(R.drawable.down);
                view_order_summary_details_lin.setVisibility(View.GONE);
            } else {
                drop_arrow_img.setImageResource(R.drawable.up);
                view_order_summary_details_lin.setVisibility(View.VISIBLE);
            }
            isArrowDown = !isArrowDown;
        }
        else if(id == R.id.rtgs_rel)
        {
            Utils.hideKeyboard(activity);

            neftSelected();
        }
        else if(id == R.id.card_type_rel)
        {
            Utils.hideKeyboard(activity);

            creditCardSelected();
        }
        else if(id == R.id.net_banking_rel)
        {
            Utils.hideKeyboard(activity);

            netBankingSelected();

        }
        else if(id == R.id.upi_rel)
        {
            Utils.hideKeyboard(activity);

            upiSelected();

        }
        else if(id == R.id.mode_payment_rel)
        {
            Utils.hideKeyboard(activity);
            paymentModeTypePopupWindow();
        }
        else if(id == R.id.select_date_rel)
        {
            Utils.hideKeyboard(activity);
            dob = select_date_tv.getText().toString().trim();
            //CommonUtility.datePicker(context, CustomPaymentScreenActivity.this, dob, "dob", 0, System.currentTimeMillis());
            CommonUtility.datePicker(context, PaymentProcessedScreenActivity.this, dob, "dob", 0, 0);
        }
        else if(id == R.id.show_all_bank_rel)
        {
            Utils.hideKeyboard(activity);
            showAllBankNamePopupWindow();
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
        else if(id == R.id.proceed_to_pay_tv)
        {
            Utils.hideKeyboard(activity);

            if(validateFields())
            {
                //createCheckSumPaymentInitiatedAndOpenPhonePe();
                getCreateOrderAPI(false);

            }else{}
        }
    }

    void clearCouponCode()
    {
        coupon_error_rel.setVisibility(View.GONE); // Coupon Error Code Layout
        coupon_code_value_rel.setVisibility(View.GONE); // Coupon Code Apply Value
        apply_coupon_points_lin.setBackgroundResource(R.drawable.verify_button_shadow);
        apply_coupon_tv.setText(getResources().getString(R.string.apply));
        apply_coupon_points_lin.setAlpha(0.5f);

        getCheckOutDetailsAPI(false, coupon_point_et.getText().toString().trim(), wallet_point_et.getText().toString().trim(),
                "");
    }

    void clearWalletPointApply()
    {
        wallet_apply_point_rel.setVisibility(View.GONE); // Coupon Error Code Layout
        apply_wallet_points_lin.setBackgroundResource(R.drawable.verify_button_shadow);
        apply_wallet_points_tv.setText(getResources().getString(R.string.apply));
        apply_wallet_points_lin.setAlpha(0.5f);

        getCheckOutDetailsAPI(false, coupon_point_et.getText().toString().trim(), wallet_point_et.getText().toString().trim(),
                "");
    }


    @Override
    protected void onResume() {
        super.onResume();
        userRole = CommonUtility.getGlobalString(activity, "login_user_role");
    }

    // RTGS/NEFT Card Option Selected
    void neftSelected()
    {
        rtgs_rel.setBackgroundResource(R.drawable.background_select_payment_option);
        card_type_rel.setBackgroundResource(R.drawable.background_payment_option);
        net_banking_rel.setBackgroundResource(R.drawable.background_payment_option);

        rtgs_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
        card_type_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        net_banking_tv.setTextColor(ContextCompat.getColor(context, R.color.black));

        // If Payment Option Selected After that Error Gone
        payment_option_error_tv.setVisibility(View.GONE);

        recycler_view.setVisibility(View.VISIBLE); // Bank List Layout Show
        select_bank_error_tv.setVisibility(View.GONE); // Hide Bank Selection Required Error.

        recycler_view_upi.setVisibility(View.GONE); // UPI Option Hide List Layout

        selectPaymentOptionClearUPIOption(NEFT); // UPI Option Clear

        isSelectNetBankingBank = false; // Use For Check Validation Net Banking Bank Not Selected

        lastPosition = 0;
        setNEFTBanKDetails(); // 0 Position Bank Selected and Show Details

        paymentModeType = NEFT;

        adapter = new BankNEFTListAdapter(modelArrayList, context , this);
        recycler_view.setAdapter(adapter);

        // Amount Calculated Final Amount and Show Amount in Bottom of Screen
        calculateAmountWithCharges();

        show_bank_name_lin.setVisibility(View.VISIBLE); // Bank List Layout
        bank_account_details_lin.setVisibility(View.VISIBLE); // Bank Account Details Layout
        show_all_bank_lin.setVisibility(View.GONE); // Select Bank DropDown Layout Gone
    }

    // Debit/Credit Card Option Selected
    void creditCardSelected()
    {
        rtgs_rel.setBackgroundResource(R.drawable.background_payment_option);
        card_type_rel.setBackgroundResource(R.drawable.background_select_payment_option);
        net_banking_rel.setBackgroundResource(R.drawable.background_payment_option);

        rtgs_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        card_type_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
        net_banking_tv.setTextColor(ContextCompat.getColor(context, R.color.black));

        // If Payment Mode Not Selected NEFT/RGTS Then Value Blank
        editTextAndTextViewValueBlank();

        // If Payment Option Selected After that Error Gone
        payment_option_error_tv.setVisibility(View.GONE);
        select_bank_error_tv.setVisibility(View.GONE); // Hide Bank Selection Required Error.

        recycler_view_upi.setVisibility(View.GONE); // UPI Option Hide List Layout

        selectPaymentOptionClearUPIOption(CREDIT_CARD); // UPI Option Clear

        isSelectNetBankingBank = false; // Use For Check Validation Net Banking Bank Not Selected

        paymentModeType = CREDIT_CARD;

        // Amount Calculated Final Amount and Show Amount in Bottom of Screen
        calculateAmountWithCharges();

        show_bank_name_lin.setVisibility(View.GONE); // Bank List Layout
        bank_account_details_lin.setVisibility(View.GONE); // Bank Account Details Layout
        show_all_bank_lin.setVisibility(View.GONE); // Select Bank DropDown Layout Gone
    }

    // Net Banking Option Selected
    void netBankingSelected()
    {
        rtgs_rel.setBackgroundResource(R.drawable.background_payment_option);
        card_type_rel.setBackgroundResource(R.drawable.background_payment_option);
        net_banking_rel.setBackgroundResource(R.drawable.background_select_payment_option);

        rtgs_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        card_type_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        net_banking_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));

        // If Payment Option Selected After that Error Gone
        payment_option_error_tv.setVisibility(View.GONE);

        // If Payment Mode Not Selected NEFT/RGTS Then Value Blank
        editTextAndTextViewValueBlank();

        recycler_view.setVisibility(View.VISIBLE); // Bank List Layout Show
        select_bank_error_tv.setVisibility(View.GONE); // Hide Bank Selection Required Error.

        recycler_view_upi.setVisibility(View.GONE); // UPI Option Hide List Layout

        isSelectNetBankingBank = false; // Use For Check Validation Net Banking Bank Not Selected

        selectPaymentOptionClearUPIOption(NET_BANKING); // UPI Option Clear

        paymentModeType = NET_BANKING;

        // Amount  Calculated Final Amount and Show Amount in Bottom of Screen
        calculateAmountWithCharges();

        // All Position Unselected.
        for (int i = 0; i < popularBankArrayList.size(); i++) {
            popularBankArrayList.get(i).setSelected(false);
        }

        select_bank_tv.setText(""); // Bank Name TextView Blank
        select_bank_tv.setHint(getResources().getString(R.string.select_bank));

        popularBankListAdapter = new PopularBankListAdapter(popularBankArrayList, context , this);
        recycler_view.setAdapter(popularBankListAdapter);

        show_bank_name_lin.setVisibility(View.VISIBLE); // Bank List Layout
        bank_account_details_lin.setVisibility(View.GONE); // Bank Account Details Layout
        show_all_bank_lin.setVisibility(View.VISIBLE); // Select Bank DropDown Layout Visible
    }

    // UPI Option Selected
    void upiSelected()
    {
        // If Payment Mode Not Selected NEFT/RGTS Then Value Blank
        editTextAndTextViewValueBlank();

        // If Payment Option Selected After that Error Gone
        payment_option_error_tv.setVisibility(View.GONE);

            /*if (isArrowDown) {
                upi_option_down_arrow_img.setImageResource(R.drawable.drop_up);
            } else {
                upi_option_down_arrow_img.setImageResource(R.drawable.drop_down);
            }
            isArrowDown = !isArrowDown;*/

        recycler_view_upi.setVisibility(View.VISIBLE); // UPI Option Show List Layout

        selectPaymentOptionClearUPIOption(UPI);

        //ArrayList<UPIAppInfoListModel> upiAppsArrayList = getInstalledUPIApps();
    }

    // If Select Payment Option Like RTGS/CreditCard/DebitCard/Net Banking Or If User Select UPI Option clear Payment Option.
    void selectPaymentOptionClearUPIOption(String paymentOptionSelectionType)
    {
        if(paymentOptionSelectionType.equalsIgnoreCase(UPI))
        {}
        else{
            if(upiAppsArrayList!=null && upiAppsArrayList.size()>0)
            {
                for (UPIAppInfoListModel item : upiAppsArrayList) {
                    item.setSelected(false);
                }
                selectedUPIPackage = "";
                upiOptionListAdapter.notifyDataSetChanged();
            }
            else{}
        }
    }

    public void getDXEBankDetailsAPI(boolean showLoader)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);
            urlParameter.put("countryName", "" + Constant.shippingAddressNameForShowHidePaymentOption);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_DXE_BANK_DETAILS, ApiConstants.GET_DXE_BANK_DETAILS_ID,showLoader,
                    "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void getPaymentOptionAPI(boolean showLoader)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.PHONE_PE_PAYMENT_OPTION, ApiConstants.PHONE_PE_PAYMENT_OPTION_ID,showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void getBankChargesAPI(boolean showLoader)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_BANK_CHARGES, ApiConstants.GET_BANK_CHARGES_ID,
                    showLoader, "GET");


        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void getCheckOutDetailsAPI(boolean showLoader, String couponCode, String walletPoints, String paymentMode)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("couponCode", "" + couponCode);
            urlParameter.put("walletPoints", "" + walletPoints);
            urlParameter.put("paymentMode", "" + paymentModeType);
            //urlParameter.put("paymentMode", "" + paymentMode);
            urlParameter.put("deliveryPincode", "" + Constant.deliveryPincode);
            urlParameter.put("collectFromHub", "" + Constant.collectFromHub);

            if(paymentModeType.equalsIgnoreCase(NEFT))
            {
                urlParameter.put("bankPaymentMethod", mode_payment_tv.getText().toString().trim());
                urlParameter.put("bankUTRNo", cheque_et.getText().toString().trim());
                urlParameter.put("bankPaymentDate", send_dob_server);
                urlParameter.put("bankNeftId", bankID);
            }
            else{}

            urlParameter.put("orderType", Constant.orderType);
            urlParameter.put("certificateNo", Constant.certificateNumber);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_CHECKOUT_DETAILS, ApiConstants.GET_CHECKOUT_DETAILS_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void getCreateOrderAPI(boolean showLoader)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);
            urlParameter.put("billingAddress", Constant.billingAddressID);
            urlParameter.put("shippingAddress", Constant.shippingAddressID);
            urlParameter.put("specialInstructions", "");
            urlParameter.put("walletPoints", wallet_point_et.getText().toString().trim());
            urlParameter.put("couponCode", coupon_point_et.getText().toString().trim());
            urlParameter.put("paymentMode", paymentModeType);
            urlParameter.put("bankPaymentMethod", mode_payment_tv.getText().toString().trim());
            urlParameter.put("bankUTRNo", cheque_et.getText().toString().trim());
            urlParameter.put("bankPaymentDate", send_dob_server);
            urlParameter.put("bankNeftId", bankID);
            urlParameter.put("deliveryPincode", Constant.deliveryPincode);
            urlParameter.put("collectFromHub", Constant.collectFromHub);
            urlParameter.put("orderType", Constant.orderType);
            urlParameter.put("certificateNo", Constant.certificateNumber);
            urlParameter.put("deviceType", "Android");
            urlParameter.put("deviceId", ""+ uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.CREATE_ORDER, ApiConstants.CREATE_ORDER_ID,
                    showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    void selectPaymentIcon()
    {
        shipping_rel.setBackgroundResource(R.drawable.background_image_white);
        shipping_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));

        kyc_rel.setBackgroundResource(R.drawable.background_image_white);
        kyc_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));

        payment_rel.setBackgroundResource(R.drawable.background_image_purple);
        payment_img.setColorFilter(ContextCompat.getColor(context, R.color.white));
    }

    // Amount  Calculated Final Amount and Show Amount in Bottom of Screen
    void calculateAmountWithCharges()
    {
        // First Check Amount Field Blank or Not If not Then Call Amount Calculation.
        if(!orderTotalAmount.equalsIgnoreCase(""))
        {
            String intPutAmount = orderTotalAmount; // This Value set SubTotal TextView Under Order Summary.

            finalSubmitValue = "";
            // Convert the String to a double
            double amount = Double.parseDouble(intPutAmount);
            double charges = 0;

            // Check payment Mode and Calculate Charges.
            if(paymentModeType.equalsIgnoreCase(NEFT))
            {
                // Calculate the UPI charges
                charges = amount * (neftCharges / 100);
            }
            else if(paymentModeType.equalsIgnoreCase(UPI))
            {
                // Calculate the UPI charges
                charges = amount * (upiCharges / 100);
            }
            else if(paymentModeType.equalsIgnoreCase(NET_BANKING))
            {
                // Calculate the net banking charges
                charges = amount * (netBankingCharges / 100);
            }
            else if(paymentModeType.equalsIgnoreCase(CREDIT_CARD))
            {
                // Calculate the Credit/Debit charges
                charges = amount * (cardCharges / 100);
            }

            // Calculate the final amount
            double finalAmount = amount + charges;
            totalAmount = String.valueOf(finalAmount);
            //Log.e("---------totalAmount : ", totalAmount.toString());
            // currencySymbol Blank Then add rupee Icon.
            if(currencySymbol.equalsIgnoreCase(""))
            {
                currencySymbol = ApiConstants.rupeesIcon;
            }else{}

            // Set Final Calculation Value.
            getCheckOutDetailsAPI(false, coupon_point_et.getText().toString().trim(), wallet_point_et.getText().toString().toString(),
                    paymentModeType);

        } else{}
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID)
    {
        try {
            Log.v("------Diamond----- : ", "--------JSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {

                case ApiConstants.GET_DXE_BANK_DETAILS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONArray details = jsonObjectData.getJSONArray("details");

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            BankNEFTListModel model = new BankNEFTListModel();

                            model.setBankId(CommonUtility.checkString(objectCodes.optString("bank_id")));
                            model.setBank(CommonUtility.checkString(objectCodes.optString("bank")));
                            model.setAccountName(CommonUtility.checkString(objectCodes.optString("account_name")));
                            model.setBranchName(CommonUtility.checkString(objectCodes.optString("branch_name")));
                            model.setIfsc(CommonUtility.checkString(objectCodes.optString("ifsc")));
                            model.setBankName(CommonUtility.checkString(objectCodes.optString("bank_name")));
                            model.setSwift(CommonUtility.checkString(objectCodes.optString("swift")));
                            model.setAccountNumber(CommonUtility.checkString(objectCodes.optString("account_number")));
                            model.setImage(CommonUtility.checkString(objectCodes.optString("image")));
                            model.setSelected(false);

                            modelArrayList.add(model);
                        }

                        // Set Bank Detail Using RGTS/NEFT
                        setNEFTBanKDetails();

                        adapter = new BankNEFTListAdapter(modelArrayList, context , this);
                        recycler_view.setAdapter(adapter);
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

                case ApiConstants.PHONE_PE_PAYMENT_OPTION_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");
                        JSONObject jObjNetBankingDetails = jObjDetails.optJSONObject("netBanking");

                        // For Popular Bank
                        JSONArray details = jObjNetBankingDetails.getJSONArray("popularBanks");

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            BankNEFTListModel model = new BankNEFTListModel();

                            model.setBankId(CommonUtility.checkString(objectCodes.optString("bankId")));
                            model.setBankName(CommonUtility.checkString(objectCodes.optString("bankName")));
                            model.setAvailable(CommonUtility.checkString(objectCodes.optString("available")));
                            model.setAccountConstraintSupported(CommonUtility.checkString(objectCodes.optString("accountConstraintSupported")));
                            model.setPriority(CommonUtility.checkString(objectCodes.optString("priority")));
                            model.setImage(CommonUtility.checkString(objectCodes.optString("img")));
                            model.setSelected(false);

                            popularBankArrayList.add(model);
                        }

                        // For all Bank
                        JSONArray detailsAllBanks = jObjNetBankingDetails.getJSONArray("allBanks");
                        for (int i = 0; i < detailsAllBanks.length(); i++)
                        {
                            JSONObject objectCodes = detailsAllBanks.getJSONObject(i);

                            BankNEFTListModel model = new BankNEFTListModel();

                            model.setBankId(CommonUtility.checkString(objectCodes.optString("bankId")));
                            model.setBankName(CommonUtility.checkString(objectCodes.optString("bankName")));
                            model.setAvailable(CommonUtility.checkString(objectCodes.optString("available")));
                            model.setAccountConstraintSupported(CommonUtility.checkString(objectCodes.optString("accountConstraintSupported")));
                            model.setPriority(CommonUtility.checkString(objectCodes.optString("priority")));
                            //model.setSelected(false);

                            allBankArrayList.add(model);
                        }

                        /*popularBankListAdapter = new PopularBankListAdapter(popularBankArrayList, context , this);
                        recycler_view.setAdapter(popularBankListAdapter);*/
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

                case ApiConstants.CREATE_ORDER_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Log.e("-------Diamond : ", "Create Order : " + jsonObjectData.toString());

                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                       // Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();

                        Constant.paymentOrderID = CommonUtility.checkString(jObjDetails.optString("order_id"));

                        JSONObject jObjUserDetails = jObjDetails.optJSONObject("user_details");

                        Constant.paymentUserID = CommonUtility.checkString(jObjUserDetails.optString("user_id"));

                        //Log.e("", "Constant.paymentUserID : " + Constant.paymentUserID);

                        if(paymentModeType.equalsIgnoreCase(NEFT))
                        {
                            Constant.comeFrom = "diamondOrder";
                            Intent intent = new Intent(activity, PaymentStatusScreenActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }
                        else{
                            totalAmount = CommonUtility.checkString(jObjDetails.optString("total_amount"));
                            callbackUrl = CommonUtility.checkString(jObjDetails.optString("callback_url"));
                            createCheckSumPaymentInitiatedAndOpenPhonePe();
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
                    else {}
                    break;

                case ApiConstants.GET_BANK_CHARGES_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        //Log.e("-------Diamond : ", "Bank Charges : " + jsonObjectData.toString());
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        try {
                            // Assuming CommonUtility.checkString returns a string that could be null or empty
                            String creditCardChargeValue = CommonUtility.checkString(jObjDetails.optString("CreditCard"));
                            String netBankingChargeValue = CommonUtility.checkString(jObjDetails.optString("NetBanking"));
                            String neftChargeValue = CommonUtility.checkString(jObjDetails.optString("NEFT"));
                            String upiChargeValue = CommonUtility.checkString(jObjDetails.optString("UPI"));

                            // Parse the string to double if it is not null or empty
                            if (creditCardChargeValue != null && !creditCardChargeValue.equalsIgnoreCase("")) {
                                cardCharges = Double.parseDouble(creditCardChargeValue);
                            }
                            if (netBankingChargeValue != null && !netBankingChargeValue.equalsIgnoreCase("")) {
                                netBankingCharges = Double.parseDouble(netBankingChargeValue);
                            }
                            if (neftChargeValue != null && !neftChargeValue.equalsIgnoreCase("")) {
                                neftCharges = Double.parseDouble(neftChargeValue);
                            }
                            if (upiChargeValue != null && !upiChargeValue.equalsIgnoreCase("")) {
                                upiCharges = Double.parseDouble(upiChargeValue);
                            }
                        } catch (NumberFormatException e) {
                            Log.e("ChargesError", "Invalid format for charges", e);
                        }
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {}
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.GET_CHECKOUT_DETAILS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Log.v("------Diamond----- : ", "--------CheckOut_Details------- : " + jsonObject);

                        isCoupanApplied = CommonUtility.checkString(jsonObjectData.optString("is_coupan_applied"));
                        orderCouponCode = CommonUtility.checkString(jsonObjectData.optString("coupon_code"));
                        orderCouponValue = CommonUtility.checkString(jsonObjectData.optString("coupon_value"));
                        orderCouponStatus = CommonUtility.checkString(jsonObjectData.optString("coupon_status"));
                        orderCouponMessage = CommonUtility.checkString(jsonObjectData.optString("coupon_msg"));
                        orderCouponDiscount = CommonUtility.checkString(jsonObjectData.optString("coupon_discount"));
                        orderSubTotal = CommonUtility.checkString(jsonObjectData.optString("sub_total"));
                        orderCgst = CommonUtility.checkString(jsonObjectData.optString("cgst"));
                        orderCgstPerc = CommonUtility.checkString(jsonObjectData.optString("cgst_perc"));
                        orderSgst = CommonUtility.checkString(jsonObjectData.optString("sgst"));
                        orderSgstPerc = CommonUtility.checkString(jsonObjectData.optString("sgst_perc"));
                        orderIgst = CommonUtility.checkString(jsonObjectData.optString("igst"));
                        orderIgstPerc = CommonUtility.checkString(jsonObjectData.optString("igst_perc"));
                        orderDiscountPerc = CommonUtility.checkString(jsonObjectData.optString("discount_perc"));
                        orderTax = CommonUtility.checkString(jsonObjectData.optString("tax"));
                        orderSubTotalWithTax = CommonUtility.checkString(jsonObjectData.optString("sub_total_with_tax"));
                        orderShippingCharge = CommonUtility.checkString(jsonObjectData.optString("shipping_charge"));
                        orderPlatformFee = CommonUtility.checkString(jsonObjectData.optString("platform_fee"));
                        orderTotalCharge = CommonUtility.checkString(jsonObjectData.optString("total_charge"));
                        orderTotalChargeTax = CommonUtility.checkString(jsonObjectData.optString("total_charge_tax"));
                        orderTotalChargeWithTax = CommonUtility.checkString(jsonObjectData.optString("total_charge_with_tax"));
                        orderTotalTaxes = CommonUtility.checkString(jsonObjectData.optString("total_taxes"));
                        orderTotalAmount = CommonUtility.checkString(jsonObjectData.optString("total_amount"));
                        orderTaxPerOnCharges = CommonUtility.checkString(jsonObjectData.optString("tax_per_on_charges"));
                        orderFinalAmount = CommonUtility.checkString(jsonObjectData.optString("final_amount"));
                        orderFinalAmountWithOutFormat = CommonUtility.checkString(jsonObjectData.optString("final_amount"));
                        orderBankCharge = CommonUtility.checkString(jsonObjectData.optString("bank_charge"));
                        orderBankChargePerc = CommonUtility.checkString(jsonObjectData.optString("bank_charge_perc"));
                        orderWalletPonits = CommonUtility.checkString(jsonObjectData.optString("wallet_points"));
                        availableWalletPoints = CommonUtility.checkString(jsonObjectData.optString("available_wallet_points"));

                        // Check Coupon Applied Or Not.
                        if(isCoupanApplied.equalsIgnoreCase("0")) {}
                        else{
                            if(orderCouponStatus.equalsIgnoreCase("1"))
                            {
                                String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderCouponDiscount);
                                String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                                coupon_code_value_rel.setVisibility(View.VISIBLE); // Coupon Code Apply Value
                                coupon_error_rel.setVisibility(View.GONE); // Coupon Error Code Layout
                                apply_coupon_points_lin.setBackgroundResource(R.drawable.background_green_light);
                                apply_coupon_tv.setText(getResources().getString(R.string.applied));
                                coupon_code_value_tv.setText("-"+getCurrencySymbol + "" + subTotalFormat);
                                isCouponCodeApplied = true;
                            }
                            else{
                                coupon_error_rel.setVisibility(View.VISIBLE); // Coupon Error Code Layout
                                coupon_code_value_rel.setVisibility(View.GONE); // Coupon Code Apply Value
                                apply_coupon_points_lin.setBackgroundResource(R.drawable.verify_button_shadow);
                                apply_coupon_tv.setText(getResources().getString(R.string.apply));
                                coupon_code_value_tv.setText(orderCouponDiscount);
                                isCouponCodeApplied = false;
                            }
                        }

                        if(orderWalletPonits!=null && !orderWalletPonits.equalsIgnoreCase("") && !orderWalletPonits.equalsIgnoreCase("0"))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderWalletPonits);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            wallet_apply_point_rel.setVisibility(View.VISIBLE); // Coupon Code Apply Value
                            apply_wallet_points_lin.setBackgroundResource(R.drawable.background_green_light);
                            apply_wallet_points_tv.setText(getResources().getString(R.string.applied));
                            wallet_apply_charges_tv.setText("-"+getCurrencySymbol + "" + subTotalFormat);
                            isWalletPointApplied = true;
                        }else{}

                        // show Available Points Value
                        if(availableWalletPoints!=null && !availableWalletPoints.equalsIgnoreCase(""))
                        {
                            available_wallet_points_tv.setText("Available " + availableWalletPoints + " Points");
                        }else{}

                        // Sub Total Charges
                        if(orderSubTotal!=null && !orderSubTotal.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderSubTotal);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            final_amount_tv1.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        // Final Amount
                        if(orderFinalAmount!=null && !orderFinalAmount.equalsIgnoreCase(""))
                        {
                            String finalAmountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderFinalAmount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            total_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(finalAmountTotalFormat));
                            final_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(finalAmountTotalFormat));
                        } else{}

                        // Shipping Charges
                        if(orderShippingCharge!=null && !orderShippingCharge.equalsIgnoreCase(""))
                        {
                            if(orderShippingCharge.equalsIgnoreCase("0"))
                            {
                                shipping_and_handling_tv.setText(getResources().getString(R.string.free_shipping));
                                shipping_and_handling_tv.setTextColor(ContextCompat.getColor(context, R.color.green));
                            }
                            else{
                                String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderShippingCharge);
                                String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                                shipping_and_handling_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));
                                shipping_and_handling_tv.setTextColor(ContextCompat.getColor(context, R.color.grey));
                            }
                        }else{}

                        // PlatFrom Charges
                        if(orderPlatformFee!=null && !orderPlatformFee.equalsIgnoreCase(""))
                        {
                            String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderPlatformFee);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            platform_fees_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));

                        }else{}

                        // Total Charges
                        if(orderTotalCharge!=null && !orderTotalCharge.equalsIgnoreCase(""))
                        {
                            String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderTotalCharge);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            total_charges_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));

                        }else{}

                        // Other Taxes
                        if(orderTotalChargeTax!=null && !orderTotalChargeTax.equalsIgnoreCase(""))
                        {
                            String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderTotalChargeTax);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            other_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));

                        }else{}

                        // Other Text GST
                        if(orderTaxPerOnCharges!=null && !orderTaxPerOnCharges.equalsIgnoreCase(""))
                        {
                            others_txt_gst_perc_tv.setText(orderTaxPerOnCharges + "% GST");
                        }else{}

                        // Diamond Taxes
                        if(orderTax!=null && !orderTax.equalsIgnoreCase(""))
                        {
                            String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderTax);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            diamond_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));

                        }else{}

                        // Total Taxes
                        if(orderTotalTaxes!=null && !orderTotalTaxes.equalsIgnoreCase(""))
                        {
                            String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderTotalTaxes);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            total_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));

                        }else{}

                        // Sub Total
                        if(orderTotalAmount!=null && !orderTotalAmount.equalsIgnoreCase(""))
                        {
                            String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderTotalAmount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            sub_total_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));

                        }else{}

                        // Bank Charges
                        if(orderBankCharge!=null && !orderBankCharge.equalsIgnoreCase(""))
                        {
                            String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderBankCharge);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            bank_charges_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));

                        }else{}

                        // Final Amount
                        if(orderFinalAmount!=null && !orderFinalAmount.equalsIgnoreCase(""))
                        {
                            String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderFinalAmount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            final_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));

                        }else{}


                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if(orderItemArrayList.size() > 0)
                        {
                            orderItemArrayList.clear();
                        }
                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            AddToCartListModel model = new AddToCartListModel();
                            model.setStockId(CommonUtility.checkString(objectCodes.optString("stock_id")));
                            model.setItemName(CommonUtility.checkString(objectCodes.optString("item_name")));
                            model.setCategory(CommonUtility.checkString(objectCodes.optString("category")));
                            model.setSupplierId(CommonUtility.checkString(objectCodes.optString("supplier_id")));
                            model.setCertificateNo(CommonUtility.checkString(objectCodes.optString("certificate_no")));
                            model.setCarat(CommonUtility.checkString(objectCodes.optString("carat")));
                            model.setColor(CommonUtility.checkString(objectCodes.optString("color")));
                            model.setClarity(CommonUtility.checkString(objectCodes.optString("clarity")));
                            model.setShape(CommonUtility.checkString(objectCodes.optString("shape")));
                            model.setDiamondImage(CommonUtility.checkString(objectCodes.optString("diamond_image")));
                            model.setTotalGstPerc(CommonUtility.checkString(objectCodes.optString("total_gst_perc")));
                            model.setStatus(CommonUtility.checkString(objectCodes.optString("status")));
                            model.setSubtotal(CommonUtility.checkString(objectCodes.optString("subtotal")));
                            model.setShowingSubTotal(CommonUtility.checkString(objectCodes.optString("subtotal")));
                            model.setTotalPrice(CommonUtility.checkString(objectCodes.optString("total_price")));
                            model.setIsReturnable(CommonUtility.checkString(objectCodes.optString("is_returnable")));
                            model.setDxePrefered(CommonUtility.checkString(objectCodes.optString("dxe_prefered")));
                            model.setOnHold(CommonUtility.checkString(objectCodes.optString("on_hold")));
                            model.setStockNo(CommonUtility.checkString(objectCodes.optString("stock_no")));

                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, CommonUtility.checkString(objectCodes.optString("subtotal")));
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            model.setShowingSubTotal(subTotalFormat);
                            model.setCurrencySymbol(getCurrencySymbol);

                            orderItemArrayList.add(model);
                        }

                        //Log.e("recommandDiamondArrayList", "" + recommandDiamondArrayList.size());
                        if(orderItemArrayList!=null && orderItemArrayList.size()>0)
                        {
                            viewpager_layout.setVisibility(View.VISIBLE);
                            setPlaceItemListPager();
                        }
                        else{
                            viewpager_layout.setVisibility(View.GONE);
                        }

                        // Check condition If Amount is Greater Then 500000 UPI Option is Hide
                        checkAmountConditionForShowHideUPIOption(orderFinalAmount);
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

    // Check condition If Amount is Greater Then 500000 UPI Option is Hide
    void checkAmountConditionForShowHideUPIOption(String totalOrderAmount)
    {
        if(!totalOrderAmount.equalsIgnoreCase(""))
        {
            int enterAmount = Integer.parseInt(totalOrderAmount);
            if(enterAmount>500000)
            {
                upi_option_lin.setVisibility(View.GONE); // UPI Option Hide
                //paymentModeType = ""; // Payment Option Blank
                selectedUPIPackage = ""; // UPI Package name blank
                // Check upiAppsArrayList is not empty or null, unselect all uip option and update ui.
                if(upiAppsArrayList!=null && upiAppsArrayList.size()>0)
                {
                    for (int i = 0; i < upiAppsArrayList.size(); i++)
                    {
                        upiAppsArrayList.get(i).setSelected(false);
                    }
                    if (upiOptionListAdapter != null)
                    {
                        upiOptionListAdapter.notifyDataSetChanged();
                    }else{}
                }else{}
            }
            else
            {
                if(Constant.shippingAddressNameForShowHidePaymentOption.equalsIgnoreCase(shippingCountryName))
                {
                    upi_option_lin.setVisibility(View.GONE); // UPI Option Gone
                }
                else{
                    upi_option_lin.setVisibility(View.VISIBLE); // UPI Option Visible
                }

            }
        }
    }

    @Override
    public void getErrorResponce(String error, int service_ID) {

    }

    @Override
    public void itemClick(int position, String action)
    {
        // RTGS/NEFT Bank
        if(action.equalsIgnoreCase("selectBank"))
        {
            // Determine if the bank at the current position should be selected
            // This toggles the selection state of the item at the given position
            boolean shouldSelect = !modelArrayList.get(position).isSelected();

            // Loop through all items in the modelArrayList
            for (int i = 0; i < modelArrayList.size(); i++) {
                modelArrayList.get(i).setSelected(false);
            }

            // Set the selected state of the item at the specified position
            modelArrayList.get(position).setSelected(shouldSelect);

            // Update the lastPosition variable to the current position
            lastPosition = position;

            // Notify the adapter that the data has changed to refresh the UI
            adapter.notifyDataSetChanged();

            // Set Bank Detail Using RGTS/NEFT
            setNEFTBanKDetails();
        }
        else if(action.equalsIgnoreCase("popularSelectBank"))
        {
            // Determine whether to select or deselect the bank at the current position
            boolean shouldSelect = !popularBankArrayList.get(position).isSelected();

            // Deselect all banks in the popularBankArrayList
            for (int i = 0; i < popularBankArrayList.size(); i++) {
                popularBankArrayList.get(i).setSelected(false);
            }

            // Set the selected state of the bank at the current position based on shouldSelect
            popularBankArrayList.get(position).setSelected(shouldSelect);

            // Update the lastPosition with the current position
            lastPosition = position;

            // Get the name of the bank at the current position
            String bankName = popularBankArrayList.get(position).getBankName();

            // Check if allBankArrayList is initialized and not empty
            if(allBankArrayList!=null && allBankArrayList.size()>0)
            {
                // Iterate through each bank in the allBankArrayList
                for (int i = 0; i <allBankArrayList.size() ; i++)
                {
                    // If the bank is selected, find the corresponding bank in allBankArrayList
                    if(shouldSelect)
                    {
                        // Compare the bank names in a case-insensitive manner
                        if(bankName.equalsIgnoreCase(allBankArrayList.get(i).getBankName()))
                        {
                            // If a match is found, set the TextView to display the bank name
                            select_bank_tv.setText(allBankArrayList.get(i).getBankName()); // Set Bank Name
                            // Exit the loop since the desired bank has been found and set
                            isSelectNetBankingBank = true; // Use For Check Validation
                            break;
                        }else{}
                    }
                    else
                    {
                        // If shouldSelect is false, clear the TextView and set a hint
                        select_bank_tv.setText(""); // Bank Name TextView Blank
                        select_bank_tv.setHint(getResources().getString(R.string.select_bank));

                        isSelectNetBankingBank = false; // Use For Check Validation
                    }
                }
            }

            // Update the bankID with the ID of the selected bank
            bankID = popularBankArrayList.get(position).getBankId();

            select_bank_error_tv.setVisibility(View.GONE); // Hide Error Message

            // Notify the adapter that the data has changed to update the UI
            popularBankListAdapter.notifyDataSetChanged();
        }
        else if(action.equalsIgnoreCase("selectAllBankName"))
        {
            select_bank_tv.setText(allBankArrayList.get(position).getBankName()); // Set Bank Name

            String bankName = allBankArrayList.get(position).getBankName();

            // Check if the popularBankArrayList is not null and contains elements
            if(popularBankArrayList!=null && popularBankArrayList.size()>0)
            {
                // Iterate through each bank in the popularBankArrayList
                for (int i = 0; i < popularBankArrayList.size(); i++) {
                    popularBankArrayList.get(i).setSelected(false);
                }
                // Iterate through each bank in the popularBankArrayList again
                for (int i = 0; i <popularBankArrayList.size() ; i++)
                {
                    // Check if the bankName matches the name of the current bank in the list
                    if(bankName.equalsIgnoreCase(popularBankArrayList.get(i).getBankName()))
                    {
                        // Set the 'selected' status of the matching bank to true
                        popularBankArrayList.get(i).setSelected(true);
                        // Exit the loop since we found the matching bank
                        break;
                    }else{}
                }
            } else{}

            isSelectNetBankingBank = true; // Use For Check Validation

            bankID = allBankArrayList.get(position).getBankId();

            select_bank_error_tv.setVisibility(View.GONE); // Hide Error Message

            popularBankListAdapter.notifyDataSetChanged();
            mDropdown.dismiss(); // DropDown Dismiss

        }
        else if(action.equalsIgnoreCase("selectUPIOption"))
        {
            show_bank_name_lin.setVisibility(View.GONE);
            recycler_view.setVisibility(View.GONE); // Bank List Hide
            select_bank_error_tv.setVisibility(View.GONE); // Hide Bank Selection Required Error.
            show_all_bank_lin.setVisibility(View.GONE); // Select Bank DropDown Layout Gone

            // selection state of the UPI app at the given position.
            boolean shouldSelect = !upiAppsArrayList.get(position).isSelected();
            // Deselect all UPI apps in the list.
            for (int i = 0; i < upiAppsArrayList.size(); i++) {
                upiAppsArrayList.get(i).setSelected(false);
            }

            // Set the selection state of the UPI app at the given position based on the toggle state.
            upiAppsArrayList.get(position).setSelected(shouldSelect);

            // If the UPI option is selected, update related states and UI elements.
            if(shouldSelect)
            {
                // Set payment mode to UPI and store the package name of the selected UPI app.
                paymentModeType = UPI;
                selectedUPIPackage = upiAppsArrayList.get(position).getPackageName();

                // Deselect all RTGS/NEFT bank options.
                for (int i = 0; i < modelArrayList.size(); i++) {
                    modelArrayList.get(i).setSelected(false);
                }

                // Deselect all popular bank options and update hint in the bank selection TextView.
                for (int i = 0; i < popularBankArrayList.size(); i++) {
                    popularBankArrayList.get(i).setSelected(false);
                }

                select_bank_tv.setText(""); // Bank Name TextView Blank
                select_bank_tv.setHint(getResources().getString(R.string.select_bank));

                // Notify the adapters to refresh the UI to reflect the changes.
                if(popularBankArrayList!=null && popularBankArrayList.size()>0) {
                    if (popularBankListAdapter != null)
                    {
                        popularBankListAdapter.notifyDataSetChanged();
                    }else{}

                }else{}

                if(modelArrayList!=null && modelArrayList.size()>0)
                {
                    adapter.notifyDataSetChanged();
                }else{}

                // Log.e("-----shouldSelect : ", "" + shouldSelect);
                // Log.e("-----shouldSelect1 : ", "" + selectedUPIPackage);
            }
            else{
                paymentModeType = "";
                selectedUPIPackage = "";
            }

            isSelectNetBankingBank = false; // Use For Check Validation Net Banking Bank Not Selected

            // Deselect and reset the UI elements for other payment options (RTGS, Card, Net Banking).
            rtgs_rel.setBackgroundResource(R.drawable.background_payment_option);
            card_type_rel.setBackgroundResource(R.drawable.background_payment_option);
            net_banking_rel.setBackgroundResource(R.drawable.background_payment_option);

            rtgs_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
            card_type_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
            net_banking_tv.setTextColor(ContextCompat.getColor(context, R.color.black));

            // Call an calculate the final amount based on the selected payment method.
            calculateAmountWithCharges();

            // Notify the adapter for the UPI options list to update the UI.
            upiOptionListAdapter.notifyDataSetChanged();
        }
    }

    // Set Bank Detail Using RGTS/NEFT
    void setNEFTBanKDetails()
    {
        bankID = modelArrayList.get(lastPosition).getBankId();

        if(!modelArrayList.get(lastPosition).getBranchName().equalsIgnoreCase(""))
        {
            branch_name_tv.setText(modelArrayList.get(lastPosition).getBranchName());
        }
        else{
            branch_name_tv.setText("-");
        }

        if(!modelArrayList.get(lastPosition).getIfsc().equalsIgnoreCase(""))
        {
            ifsc_code_tv.setText(modelArrayList.get(lastPosition).getIfsc());
        }
        else{
            ifsc_code_tv.setText("-");
        }

        if(!modelArrayList.get(lastPosition).getSwift().equalsIgnoreCase(""))
        {
            swift_code_tv.setText(modelArrayList.get(lastPosition).getSwift());
        }
        else{
            swift_code_tv.setText("-");
        }

        if(!modelArrayList.get(lastPosition).getBankName().equalsIgnoreCase(""))
        {
            bank_name_tv.setText(modelArrayList.get(lastPosition).getBankName());
        }
        else{
            bank_name_tv.setText("-");
        }

        if(!modelArrayList.get(lastPosition).getAccountNumber().equalsIgnoreCase(""))
        {
            account_number_tv.setText(modelArrayList.get(lastPosition).getAccountNumber());
        }
        else{
            account_number_tv.setText("-");
        }

        modelArrayList.get(lastPosition).setSelected(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Log.d("PAPAYACODERS", "requestCode: " + requestCode);
            Log.d("PAPAYACODERS", "resultCode: " + resultCode);
            Log.d("PAPAYACODERS", "data: " + data);
            if (resultCode == RESULT_OK && data != null)
            {
                // Get the transaction result from the Intent
                String txnResult = data.getStringExtra("key_txn_result");
                if (txnResult != null) {
                    Log.d("PAPAYACODERS", "Non-JSON response: " + txnResult);

                } else {
                    Log.d("PAPAYACODERS", "No transaction result data found");
                    Toast.makeText(activity, "Else : " + requestCode + " : " +resultCode , Toast.LENGTH_SHORT).show();
                }
                Constant.comeFrom = "diamondOrder";
                Intent intent = new Intent(activity, PaymentStatusScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Log.d("PAPAYACODERS", "Else Transaction Failed");
            }
            else {
                Log.d("PAPAYACODERS", "Transaction failed or no data returned");
            }

            /*This callback indicates only about completion of UI flow.
            Inform your server to make the transaction
            status call to get the status. Update your app with the
            success/failure status.*/
        }
    }

    @Override
    public void dialogItemClick(String value, String action)
    {

        if (action.equalsIgnoreCase("dob")) {
            select_date_tv.setText(value);
            //from_date_required.setVisibility(View.GONE);
            //  Log.e("------dob_value----- : ", value);
            select_date_rel.setBackgroundResource(R.drawable.border_line_view);
            date_error_tv.setVisibility(View.GONE);
            // this date send to server formated date yyyy-MM-dd.
            send_dob_server = CommonUtility.convertDateFormat(value, ApiConstants.DATE_DOB_FORMAT, "dd/MM/yyyy");
        }

    }

    private PopupWindow mDropdown = null;
    LayoutInflater mInflater;
    // Button pop;

    private PopupWindow paymentModeTypePopupWindow() {
        try {
            if (mDropdown != null && mDropdown.isShowing()) {
                mDropdown.dismiss();
            }
            mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.custom_menu_payment_mode_type, null);

            select_mode_lbl = layout.findViewById(R.id.select_mode_lbl);
            cheque_mode_tv = layout.findViewById(R.id.cheque_mode_tv);
            neft_mode_tv = layout.findViewById(R.id.neft_mode_tv);
            rgts_mode_tv = layout.findViewById(R.id.rgts_mode_tv);
            wire_transfer_mode_tv = layout.findViewById(R.id.wire_transfer_mode_tv);
            others_mode_tv = layout.findViewById(R.id.others_mode_tv);

            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            //mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);

            mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);

            /*Drawable background = getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            mDropdown.setBackgroundDrawable(background);*/

            // Ensure pop is not null before using it
            if (mode_payment_rel != null) {
                mDropdown.showAsDropDown(mode_payment_rel, 5, -50);
            } else {
                Log.e("PopupWindow", "pop is null");
            }

            if(Constant.shippingAddressNameForShowHidePaymentOption.equalsIgnoreCase(shippingCountryName))
            {
                cheque_mode_tv.setVisibility(View.GONE);
                neft_mode_tv.setVisibility(View.GONE);
                rgts_mode_tv.setVisibility(View.GONE);
            }
            else{
                cheque_mode_tv.setVisibility(View.VISIBLE);
                neft_mode_tv.setVisibility(View.VISIBLE);
                rgts_mode_tv.setVisibility(View.VISIBLE);
            }

            select_mode_lbl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            cheque_mode_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mode_payment_tv.setText(cheque_mode_tv.getText().toString().trim());
                    removeModePaymentError();
                    mDropdown.dismiss();
                }
            });

            neft_mode_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mode_payment_tv.setText(neft_mode_tv.getText().toString().trim());
                    removeModePaymentError();
                    mDropdown.dismiss();
                }
            });

            rgts_mode_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mode_payment_tv.setText(rgts_mode_tv.getText().toString().trim());
                    removeModePaymentError();
                    mDropdown.dismiss();
                }
            });

            wire_transfer_mode_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mode_payment_tv.setText(wire_transfer_mode_tv.getText().toString().trim());
                    removeModePaymentError();
                    mDropdown.dismiss();
                }
            });

            others_mode_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mode_payment_tv.setText(others_mode_tv.getText().toString().trim());
                    removeModePaymentError();
                    mDropdown.dismiss();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;
    }

    void removeModePaymentError()
    {
        payment_mode_error_tv.setVisibility(View.GONE);
        mode_payment_rel.setBackgroundResource(R.drawable.border_line_view);
    }

    // Show All Bank Name List
    RecyclerView recycler_view_bank_name;
    TextView select_bank_lbl;
    private PopupWindow showAllBankNamePopupWindow() {
        try {
            if (mDropdown != null && mDropdown.isShowing()) {
                mDropdown.dismiss();
            }
            mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.custom_menu_bank_name_list, null);
            recycler_view_bank_name = layout.findViewById(R.id.recycler_view_bank_name);
            select_bank_lbl = layout.findViewById(R.id.select_bank_lbl);

            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);

            // Ensure pop is not null before using it
            if (mode_payment_rel != null) {
                mDropdown.showAsDropDown(show_all_bank_rel, 5, -50);
            } else {
                Log.e("PopupWindow", "pop is null");
            }

            select_bank_lbl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
            recycler_view_bank_name.setLayoutManager(layoutManager);
            recycler_view_bank_name.setNestedScrollingEnabled(false);

            allBankNameListAdapter = new AllBankNameListAdapter(allBankArrayList, context , this);
            recycler_view_bank_name.setAdapter(allBankNameListAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;
    }

    // Get all UPI App
    private ArrayList<UPIAppInfoListModel> getInstalledUPIApps() {
        ArrayList<UPIAppInfoListModel> upiAppsList = new ArrayList<>();
        Uri uri = Uri.parse("upi://pay"); // Use a general UPI URI
        //Uri uri = Uri.parse(String.format("%s://%s", "upi", "pay"));

        Intent upiUriIntent = new Intent(Intent.ACTION_VIEW);
        upiUriIntent.setData(uri);

        PackageManager packageManager = getApplication().getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(upiUriIntent, PackageManager.MATCH_DEFAULT_ONLY);

        if (resolveInfoList != null) {
            for (ResolveInfo resolveInfo : resolveInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                CharSequence appName = resolveInfo.loadLabel(packageManager);
                Drawable appIcon = resolveInfo.loadIcon(packageManager);
                upiAppsList.add(new UPIAppInfoListModel(packageName, appName.toString(), appIcon, false));
            }
        }
        return upiAppsList;
    }

    // Show All UPI App Name
    private void showUPIAppsOption(ArrayList<UPIAppInfoListModel> upiApps) {
        if (upiApps.isEmpty()) {
            Toast.makeText(this, "No UPI apps installed", Toast.LENGTH_SHORT).show();
            upi_option_lin.setVisibility(View.GONE);
            return;
        }
        upiOptionListAdapter = new UPIOptionListAdapter(upiApps, context , this);
        recycler_view_upi.setAdapter(upiOptionListAdapter);
    }


    final Handler handler = new Handler();
    Timer swipeTimer = new Timer();
    Runnable Update;
    int currentPage = 0;
    int NUM_PAGES = 0;

    public void setPlaceItemListPager() {
        viewPager.setAdapter(new MyPagerAdapter(activity, orderItemArrayList));
        tabLayout.setupWithViewPager(viewPager, true);
        final float density = getResources().getDisplayMetrics().density;
        if(orderItemArrayList!=null && orderItemArrayList.size()>=1)
        {
            viewpager_layout.setVisibility(View.VISIBLE);

            if(orderItemArrayList.size()>1){
                tabLayout.setVisibility(View.VISIBLE);
            }else {
                tabLayout.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            viewpager_layout.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
        }

        NUM_PAGES = orderItemArrayList.size();
        currentPage = 0;

        // Cancel any previous timer tasks if they exist
        if (swipeTimer != null) {
            swipeTimer.cancel();
            swipeTimer = new Timer();
        }

        if (Update == null) {
            // Auto start of viewpager
            Update = new Runnable() {
                public void run() {
                    if (NUM_PAGES > 0) {
                        if (currentPage >= NUM_PAGES) {
                            currentPage = 0;
                        }
                        viewPager.setCurrentItem(currentPage++, true);
                    }
                }
            };

            /*swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 8000, 8000);*/
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Optional implementation
            }

            @Override
            public void onPageSelected(int position) {
                // Highlight the selected tab
                // Highlight the selected tab
                tabLayout.setScrollPosition(position, 0f, true);
                // Update currentPage to the new position
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Optional implementation
            }
        });

    }

    @Override
    public void onPaymentResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1) {
            Log.d("PAPAYACODERS_Payment_Result", "requestCode: " + requestCode);
            Log.d("PAPAYACODERS_Payment_Result", "resultCode: " + resultCode);
            Log.d("PAPAYACODERS_Payment_Result", "data: " + data);

            if (resultCode == RESULT_OK && data != null) {
                // Get the transaction result from the Intent
                String txnResult = data.getStringExtra("key_txn_result");
                if (txnResult != null) {
                    Log.d("PAPAYACODERS_Payment_Result", "Non-JSON response: " + txnResult);

                } else {
                    Log.d("PAPAYACODERS_Payment_Result", "No transaction result data found");
                    Toast.makeText(activity, "Else : " + requestCode + " : " +resultCode , Toast.LENGTH_SHORT).show();
                }
                Constant.comeFrom = "diamondOrder";
                Intent intent = new Intent(activity, PaymentStatusScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            } else if (resultCode == RESULT_CANCELED) {
                Log.d("Payment", "Transaction Canceled");
            } else {
                Log.d("Payment", "Transaction failed or no data returned");
            }
        }
    }

    private class MyPagerAdapter extends PagerAdapter {

        ArrayList<AddToCartListModel> list;
        LayoutInflater inflater;
        public MyPagerAdapter(Context context, ArrayList<AddToCartListModel> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }


        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            // Declare Variables
            ImageView pagerImg, status_img, returnable_img;
            CardView root_layout;
            TextView supplier_id_tv_pager, name_tv_Pager, item_type_tv,  return_policy_tv, sub_total_tv,diamond_type;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.row_place_order_item_list, container, false);
            pagerImg = (ImageView) itemView.findViewById(R.id.image_view);
            status_img = (ImageView) itemView.findViewById(R.id.status_img);
            returnable_img = (ImageView) itemView.findViewById(R.id.returnable_img);

            supplier_id_tv_pager = itemView.findViewById(R.id.supplier_id_tv);
            diamond_type = itemView.findViewById(R.id.diamond_type);

            root_layout = itemView.findViewById(R.id.root_layout);

            name_tv_Pager = itemView.findViewById(R.id.name_tv);
            item_type_tv = itemView.findViewById(R.id.item_type_tv);
            return_policy_tv = itemView.findViewById(R.id.return_policy_tv);
            sub_total_tv = itemView.findViewById(R.id.sub_total_tv);

            if(!list.get(position).getDiamondImage().equalsIgnoreCase(""))
            {
                Picasso.with(context)
                        .load(list.get(position).getDiamondImage())
                        .placeholder(R.mipmap.phl_diamond)
                        .error(R.mipmap.phl_diamond)
                        .into(pagerImg);
            }
            else{
                Picasso.with(context)
                        .load(R.mipmap.phl_diamond)
                        .placeholder(R.mipmap.phl_diamond)
                        .error(R.mipmap.phl_diamond)
                        .into(pagerImg);
            }


            supplier_id_tv_pager.setText("#"+list.get(position).getStockNo() + " | " + list.get(position).getSupplierId());
            name_tv_Pager.setText(list.get(position).getShape());
            item_type_tv.setText(list.get(position).getCarat() + getResources().getString(R.string.ct) + " " + list.get(position).getColor() + " " + list.get(position).getClarity());


            if(list.get(position).getCategory().equalsIgnoreCase("Natural"))
            {
                diamond_type.setBackgroundResource(R.drawable.background_yellow);
                diamond_type.setText("NATURAL");
            }
            else{
                diamond_type.setBackgroundResource(R.drawable.background_green_light_small_round_cornor);
                diamond_type.setText("LAB");
            }


            DecimalFormat formatter = new DecimalFormat("#,###,###");

            if(!list.get(position).getSubtotal().equalsIgnoreCase(""))
            {
                sub_total_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));
            }else {}

            if(list.get(position).getIsReturnable().equalsIgnoreCase("1"))
            {
                returnable_img.setVisibility(View.VISIBLE);
            }
            else{
                returnable_img.setVisibility(View.GONE);
            }

            returnable_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    return_policy_tv.setVisibility(View.VISIBLE);

                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            return_policy_tv.setVisibility(View.GONE);
                        }
                    }, 2000);
                }
            });

            if(list.get(position).getStatus().equalsIgnoreCase("Available"))
            {
                status_img.setVisibility(View.VISIBLE);

                status_img.setBackgroundResource(R.drawable.available);
            }
            else if(list.get(position).getStatus().equalsIgnoreCase("On Hold")){
                status_img.setVisibility(View.VISIBLE);
                status_img.setBackgroundResource(R.drawable.onhold);
            }
            else
            {
                status_img.setVisibility(View.GONE);
            }

            root_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            // Add viewpager_item.xml to ViewPager
            ((ViewPager) container).addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Remove viewpager_item.xml from ViewPager
            ((ViewPager) container).removeView((RelativeLayout) object);

        }
    }

    private boolean validateFields()
    {
        String modePayment = mode_payment_tv.getText().toString().trim();
        String chequeNumber = cheque_et.getText().toString().trim();
        String chequeDate = select_date_tv.getText().toString().trim();

        if (paymentModeType.length() == 0 || paymentModeType == null|| paymentModeType.equalsIgnoreCase("")) {
            payment_option_error_tv.setVisibility(View.VISIBLE);
            mode_payment_rel.requestFocus();
            return false;
        }
        else if ((modePayment.length() == 0 || modePayment == null|| modePayment.equalsIgnoreCase("")) && paymentModeType.equalsIgnoreCase(NEFT)) {
            payment_mode_error_tv.setVisibility(View.VISIBLE);
            mode_payment_rel.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }
        else if ((chequeNumber.length() == 0 || chequeNumber == null|| chequeNumber.equalsIgnoreCase("")) && paymentModeType.equalsIgnoreCase(NEFT)) {
            cheque_no_error_tv.setVisibility(View.VISIBLE);
            cheque_et.requestFocus();
            cheque_rel.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }
        else if ((chequeDate.length() == 0 || chequeDate == null|| chequeDate.equalsIgnoreCase("")) && paymentModeType.equalsIgnoreCase(NEFT)) {
            date_error_tv.setVisibility(View.VISIBLE);
            select_date_rel.requestFocus();
            select_date_rel.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }
        else if (!isSelectNetBankingBank && paymentModeType.equalsIgnoreCase(NET_BANKING)) {
            select_bank_error_tv.setVisibility(View.VISIBLE);
            show_all_bank_lin.requestFocus();
            return false;
        }
        return true;
    }
}