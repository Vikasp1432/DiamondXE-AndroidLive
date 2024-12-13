package com.diamondxe.Activity.Dealer;

import static com.diamondxe.Utils.PaymentUtils.API_END_POINT;
import static com.diamondxe.Utils.PaymentUtils.BASE_URL;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.diamondxe.Activity.PaymentPages.PayTabsPaymentManager;
import com.diamondxe.Activity.PaymentPages.PaymentMethod;
import com.diamondxe.Activity.PaymentPages.RazorpayUtility;
import com.diamondxe.Activity.PaymentStatusScreenActivity;
import com.diamondxe.Adapter.Dealer.AllBankNameListAdapter;
import com.diamondxe.Adapter.Dealer.BankNEFTListAdapter;
import com.diamondxe.Adapter.Dealer.PopularBankListAdapter;
import com.diamondxe.Adapter.UPIOptionListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.Dealer.BankNEFTListModel;
import com.diamondxe.Beans.UPIAppInfoListModel;
import com.diamondxe.Interface.DialogItemClickInterface;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.PaymentUtils;
import com.diamondxe.Utils.Utils;
import com.payment.paymentsdk.integrationmodels.PaymentSdkApms;
import com.payment.paymentsdk.integrationmodels.PaymentSdkError;
import com.payment.paymentsdk.integrationmodels.PaymentSdkTransactionDetails;
import com.payment.paymentsdk.sharedclasses.interfaces.CallbackPaymentInterface;
import com.phonepe.intent.sdk.api.B2BPGRequest;
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder;
import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.PhonePeInitException;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


public class CustomPaymentScreenActivity extends SuperActivity implements RecyclerInterface , DialogItemClickInterface, PaymentResultWithDataListener,CallbackPaymentInterface {

    private ImageView back_img, upi_option_down_arrow_img;
    private LinearLayout custom_payment_lin, history_lin,upi_main_lin, show_bank_name_lin, bank_account_details_lin, show_all_bank_lin, upi_option_lin,
            company_name_lin,show_upi_list;
    private TextView custom_payment_tv, history_tv,upi_tv, noupifound_tv,total_amount_tv, rtgs_tv, card_type_tv, net_banking_tv,name_error_tv, company_name_error_tv,
            amount_error_tv, remark_error_tv,branch_name_tv,ifsc_code_tv,swift_code_tv,bank_name_tv,account_number_tv,mode_payment_tv,select_date_tv,
            payment_mode_error_tv, cheque_no_error_tv, date_error_tv, cheque_mode_tv, neft_mode_tv, rgts_mode_tv, wire_transfer_mode_tv,others_mode_tv,
            payment_option_error_tv,select_bank_tv, select_mode_lbl, select_bank_error_tv;
    private EditText name_et, company_name_et, amount_et, remark_et, cheque_et;
    private RelativeLayout proceed_to_pay_rel, rtgs_rel, card_type_rel, net_banking_rel, upi_rel, mode_payment_rel,select_date_rel,
            cheque_rel, show_all_bank_rel,upi_relative;

    boolean isSelectNetBankingBank = false;

    private Activity activity;
    private Context context;

    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    RelativeLayout upi_rela;
    private RecyclerView recycler_view, recycler_view_upi;
    ArrayList<BankNEFTListModel> modelArrayList;
    ArrayList<BankNEFTListModel> popularBankArrayList;
    ArrayList<BankNEFTListModel> allBankArrayList;
    BankNEFTListAdapter adapter;
    PopularBankListAdapter popularBankListAdapter;
    AllBankNameListAdapter allBankNameListAdapter;

    ArrayList<UPIAppInfoListModel> upiAppsArrayList;
    UPIOptionListAdapter upiOptionListAdapter;

    String currencySymbol = "", bankCharge = "", bankChargePerc = "", totalAmount = "", bankID = "",callbackUrl="",userRole="";

    private static final int PHONEPE_PAYMENT_REQUEST_CODE = 1001;

    int lastPosition = 0;

    double neftCharges = 0;
    double upiCharges = 0;
    double netBankingCharges = 0;
    double cardCharges = 0;

    B2BPGRequest b2BPGRequest;
    String payloadBase64 = "", checksum="", amountInPaisaString="",dob="",send_dob_server="",  selectedUPIPackage="", finalSubmitValue="";
    String paymentModeType= "NEFT"; // By Default Payment Mode NEFT
    double amountInRupees = 0;
    private boolean isArrowDown = true;

    String NEFT = "NEFT";
    String CREDIT_CARD = "CreditCard";
    String NET_BANKING = "NetBanking";
    String UPI = "UPI";

    String PAYMENT_BY_UPI = "UPI_INTENT";
    String PAYMENT_BY_NET_BANKING = "NET_BANKING";
    //String PAYMENT_BY_CARD = "CARD";
    String PAYMENT_BY_CARD = "PAY_PAGE";

    LinearLayout india_layout,international_layout,net_banking_main_lin;
    ImageView payment_image;
    TextView india_text,international_text;
    String RegionType="IND";
    String paymentGetwayType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_payment_screen);

        context = activity = this;

        modelArrayList = new ArrayList<>();
        upiAppsArrayList = new ArrayList<>();
        popularBankArrayList = new ArrayList<>();
        allBankArrayList = new ArrayList<>();

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        payment_image=findViewById(R.id.payment_image);
        india_text=findViewById(R.id.india_text);
        international_text=findViewById(R.id.international_text);
        india_layout=findViewById(R.id.india_layout);
        india_layout.setOnClickListener(this);
        net_banking_main_lin=findViewById(R.id.net_banking_main_lin);
        international_layout=findViewById(R.id.international_layout);
        international_layout.setOnClickListener(this);

        show_upi_list=findViewById(R.id.show_upi_list);
        custom_payment_lin = findViewById(R.id.custom_payment_lin);
        history_lin = findViewById(R.id.history_lin);
        upi_relative = findViewById(R.id.upi_rela);
        custom_payment_tv = findViewById(R.id.custom_payment_tv);
        custom_payment_tv.setOnClickListener(this);
        upi_tv=findViewById(R.id.upi_tv);
        history_tv = findViewById(R.id.history_tv);
        history_tv.setOnClickListener(this);
        upi_main_lin =findViewById(R.id.upi_main_lin);
        upi_main_lin.setOnClickListener(this);
        upi_main_lin.setVisibility(View.VISIBLE);
        upi_rela=findViewById(R.id.upi_rela);
        upi_rela.setOnClickListener(this);
        total_amount_tv = findViewById(R.id.total_amount_tv);
        noupifound_tv = findViewById(R.id.noupifound_tv);
        name_et = findViewById(R.id.name_et);
        company_name_et = findViewById(R.id.company_name_et);
        amount_et = findViewById(R.id.amount_et);
        remark_et = findViewById(R.id.remark_et);
        cheque_et = findViewById(R.id.cheque_et);

        company_name_lin = findViewById(R.id.company_name_lin);

        name_error_tv = findViewById(R.id.name_error_tv);
        company_name_error_tv = findViewById(R.id.company_name_error_tv);
        amount_error_tv = findViewById(R.id.amount_error_tv);
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
        upi_option_lin.setOnClickListener(this);
        proceed_to_pay_rel = findViewById(R.id.proceed_to_pay_rel);
        proceed_to_pay_rel.setOnClickListener(this);
        upi_option_lin.setVisibility(View.GONE);
        // Initialize PhonePe
        PaymentUtils.initializePhonePe(this);

         //PhonePe.init(getApplicationContext(), PhonePeEnvironment.SANDBOX, MERCHANT_ID, SALT); // For SendBox
         //PhonePe.init(getApplicationContext(), PhonePeEnvironment.RELEASE, MERCHANT_ID, SALT); // For Live
        //PhonePe.init(this);

        getData();

        removeEditTextValidationError();

        upiAppsArrayList = getInstalledUPIApps();

        //showUPIAppsOption(upiAppsArrayList);

        getDXEBankDetailsAPI(true,"IND");
        getPaymentOptionAPI(false);
        getBankChargesAPI(true);
    }

    // Set First Name, Last Name and Company Name
    void getData()
    {
        String firstName = CommonUtility.getGlobalString(activity, "login_first_name");
        String lastName = CommonUtility.getGlobalString(activity, "login_last_name");
        String companyName = CommonUtility.getGlobalString(activity, "login_company_name");
        userRole = CommonUtility.getGlobalString(activity, "login_user_role");

        // Check First Name Not Blank
        if(!firstName.equalsIgnoreCase(""))
        {
            // Check Last Name Not Blank
            if(!lastName.equalsIgnoreCase(""))
            {
                name_et.setText(firstName + " " + lastName);
            }
            else{
                name_et.setText(firstName);
            }

        }else{}

        // Check Company Name Not Blank and Check Clickable or Not.
        if(!companyName.equalsIgnoreCase(""))
        {
            company_name_et.setText(companyName);
            company_name_et.setClickable(false);
            company_name_et.setEnabled(false);
            company_name_et.setBackgroundResource(R.drawable.bg_gray_out);

        } else{
            company_name_et.setClickable(true);
            company_name_et.setEnabled(true);
            company_name_et.setBackgroundResource(R.drawable.border_line_view);
        }

        // If User Type BYUER Company Name Layout Not Show
        if(userRole.equalsIgnoreCase("BUYER"))
        {
            company_name_lin.setVisibility(View.GONE);
        }
        else{
            company_name_lin.setVisibility(View.VISIBLE);
        }
    }
    // Remove Validation Error Under EditText.
    void removeEditTextValidationError()
    {
        /*name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!name_et.getText().toString().equalsIgnoreCase("")){
                    name_error_tv.setVisibility(View.GONE);
                    name_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(name_et.getText().toString().equalsIgnoreCase("")){
                    name_error_tv.setVisibility(View.GONE);
                    name_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });*/

        company_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!company_name_et.getText().toString().equalsIgnoreCase("")){
                    company_name_error_tv.setVisibility(View.GONE);
                    company_name_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(company_name_et.getText().toString().equalsIgnoreCase("")){
                    company_name_error_tv.setVisibility(View.GONE);
                    company_name_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        amount_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!amount_et.getText().toString().equalsIgnoreCase("")){
                    amount_error_tv.setVisibility(View.GONE);
                    amount_et.setBackgroundResource(R.drawable.border_purple_line_view);
                    finalSubmitValue = "";
                    calculateAmountWithCharges();
                }

                if(amount_et.getText().toString().equalsIgnoreCase(""))
                {
                    amount_error_tv.setVisibility(View.GONE);
                    amount_et.setBackgroundResource(R.drawable.border_line_view);
                    totalAmount = "0.00";
                    total_amount_tv.setText(currencySymbol + "0.00");
                    finalSubmitValue = "";
                }

                // Check condition If Amount is Greater Then 500000 UPI Option is Hide
                if(!amount_et.getText().toString().trim().equalsIgnoreCase(""))
                {
                    int enterAmount = Integer.parseInt(amount_et.getText().toString().trim());
                    if(enterAmount>500000)
                    {
                        upi_main_lin.setVisibility(View.GONE);
                        upi_option_lin.setVisibility(View.GONE);
                        paymentModeType = "";
                        selectedUPIPackage = "";
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
                        if (RegionType.equalsIgnoreCase("IND"))
                        {
                            upi_main_lin.setVisibility(View.VISIBLE);
                        }
                        else if (RegionType.equalsIgnoreCase("GCC")){
                            upi_main_lin.setVisibility(View.GONE);
                        }

                        upi_option_lin.setVisibility(View.GONE); // UPI Option Visible
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        remark_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!remark_et.getText().toString().equalsIgnoreCase("")){
                    remark_error_tv.setVisibility(View.GONE);
                    remark_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(remark_et.getText().toString().equalsIgnoreCase("")){
                    remark_error_tv.setVisibility(View.GONE);
                    remark_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

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
        amountInPaisaString = total_amount_tv.getText().toString().trim();

        JSONObject data = new JSONObject();
        try {

            // Convert the string to an integer value to ensure it's valid
            int amountInPaisa = Integer.parseInt(totalAmount);

            //data.put("merchantTransactionId", CommonUtility.generateTransactionId()); // String. Mandatory
            data.put("merchantTransactionId", Constant.paymentOrderID); // String. Mandatory
            data.put("merchantId", MERCHANT_ID); // String. Mandatory
            data.put("merchantUserId", Constant.paymentUserID); // String. Mandatory
            data.put("amount", amountInPaisa * 100);
            data.put("mobileNumber", CommonUtility.getGlobalString(activity, "login_mobile_no")); // String. Optional
            data.put("callbackUrl", callbackUrl); // String. Mandatory

            // Logging the amount for verification
            Log.d("AmountConversion", "Amount in Paisa: " + amountInPaisa);

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
                Log.e("SEnd_bankID : ", bankID.toString());
                paymentInstrument.put("bankId", bankID);
            }
            else if(paymentModeType.equalsIgnoreCase(CREDIT_CARD))
            {
                // For Credit/Debit Card
                paymentInstrument.put("type", PAYMENT_BY_CARD);
            }

            //paymentInstrument.put("targetApp", TARGET_URL);

            data.put("paymentInstrument", paymentInstrument);

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
                //Intent intent1 = PhonePe.getImplicitIntent(context, b2BPGRequest, TARGET_URL);
                Intent intent1;
                //Intent intent1 = PhonePe.getImplicitIntent(context, b2BPGRequest, TARGET_URL);
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

    // If Payment Mode Not Selected NEFT/RGTS Then Value Blank
    void editTextAndTextViewValueBlank()
    {
        mode_payment_tv.setText("");
        mode_payment_tv.setHint(getResources().getString(R.string.select_payment_mode));

        cheque_et.setText("");
        cheque_et.setHint(getResources().getString(R.string.enter_utr_cheque_no));

        select_date_tv.setText("");
        select_date_tv.setHint(getResources().getString(R.string.select_date_month_year));
    }

    @Override
    public void onClick(View view) {
        AtomicReference<Intent> intent1;
        Intent intent;
        int id = view.getId();

        if (id == R.id.back_img) {
            Utils.hideKeyboard(activity);
            finish();
        }
        else if (id == R.id.custom_payment_tv)
        {
            Utils.hideKeyboard(activity);
        }
        else if (id == R.id.history_tv)
        {
            Utils.hideKeyboard(activity);
            intent = new Intent(activity, CustomPaymentHistoryScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
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
        else if(id == R.id.upi_rela)
        {

            Log.e("Click..@@@@@@@@","..633....");
            Utils.hideKeyboard(activity);
            UPISelected();
        }
        else if(id==R.id.international_layout)
        {
            RegionType="GCC";
            international_layout.setBackground(
                    ContextCompat.getDrawable(this, R.drawable.background_button_shadow)
            );
            india_layout.setBackground(
                    ContextCompat.getDrawable(this, R.drawable.backgroud_with_border)
            );
            india_text.setTextColor(
                    ContextCompat.getColor(this, R.color.purple_light)
            );
            international_text.setTextColor(
                    ContextCompat.getColor(this, R.color.white)
            );
            payment_image.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.paytabs_logo));
            getDXEBankDetailsAPI(true,"GCC");

            net_banking_main_lin.setVisibility(View.INVISIBLE);
            upi_main_lin.setVisibility(View.GONE);



        }
            else if(id==R.id.india_layout)
        {
            RegionType="IND";
            getDXEBankDetailsAPI(true,"IND");
           india_layout.setBackground(
                    ContextCompat.getDrawable(this, R.drawable.background_button_shadow)
            );
            india_text.setTextColor(
                    ContextCompat.getColor(this, R.color.white)
            );
            international_text.setTextColor(
                    ContextCompat.getColor(this, R.color.purple_light)
            );
            international_layout.setBackground(
                    ContextCompat.getDrawable(this, R.drawable.backgroud_with_border)
            );

            payment_image.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.razorpay_laog)
            );

            if(!amount_et.getText().toString().equalsIgnoreCase(""))
            {
                int enterAmount = Integer.parseInt(amount_et.getText().toString().trim());
                if(enterAmount>500000)
                {
                    upi_main_lin.setVisibility(View.GONE);
                }
                else {
                    if (RegionType.equalsIgnoreCase("IND"))
                    {
                        upi_main_lin.setVisibility(View.VISIBLE);
                    }
                    else if (RegionType.equalsIgnoreCase("GCC")){
                        upi_main_lin.setVisibility(View.GONE);
                    }

                    upi_option_lin.setVisibility(View.GONE);
                }
            }
            else {

                upi_main_lin.setVisibility(View.VISIBLE);
            }
            net_banking_main_lin.setVisibility(View.VISIBLE);

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
            CommonUtility.datePicker(context, CustomPaymentScreenActivity.this, dob, "dob", 0, 0);
        }
        else if(id == R.id.show_all_bank_rel)
        {

            Utils.hideKeyboard(activity);
            showAllBankNamePopupWindow();
        }
        else if (id == R.id.proceed_to_pay_rel)
        {
            Utils.hideKeyboard(activity);

            Log.d("validateFields()", "Click..@@..: " + validateFields());
           // Log.d("PAPAYACODERS", "onCreate: " + checksum);

            if(validateFields())
            {
                if(paymentModeType.equalsIgnoreCase(NEFT))
                {
                    finalSubmitValue = "yes";
                    getCustomPaymentAPI(false,"1");
                }
                else{
                    finalSubmitValue = "yes";
                    getCustomPaymentAPI(false,"1");
                }

            } else{}
        }
    }

    // RTGS/NEFT Card Option Selected
    void neftSelected()
    {
        rtgs_rel.setBackgroundResource(R.drawable.background_select_payment_option);
        rtgs_rel.setBackgroundResource(R.drawable.background_select_payment_option);
        card_type_rel.setBackgroundResource(R.drawable.background_payment_option);
        net_banking_rel.setBackgroundResource(R.drawable.background_payment_option);
        upi_relative.setBackgroundResource(R.drawable.background_payment_option);

        rtgs_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
        card_type_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        net_banking_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        upi_tv.setTextColor(ContextCompat.getColor(context, R.color.black));

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
        Log.e("modelArrayList","....Custom...215...."+modelArrayList.size());
        adapter = new BankNEFTListAdapter(modelArrayList, context , this);
        recycler_view.setAdapter(adapter);

        // Amount Calculated Final Amount and Show Amount in Bottom of Screen
        calculateAmountWithCharges();
        show_upi_list.setVisibility(View.GONE);
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
        upi_relative.setBackgroundResource(R.drawable.background_payment_option);

        rtgs_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        card_type_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
        net_banking_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        upi_tv.setTextColor(ContextCompat.getColor(context, R.color.black));

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
        show_upi_list.setVisibility(View.GONE);
        show_bank_name_lin.setVisibility(View.GONE); // Bank List Layout
        bank_account_details_lin.setVisibility(View.GONE); // Bank Account Details Layout
        show_all_bank_lin.setVisibility(View.GONE); // Select Bank DropDown Layout Gone
    }

    // Net Banking Option Selected
    void netBankingSelected()
    {
        rtgs_rel.setBackgroundResource(R.drawable.background_payment_option);
        card_type_rel.setBackgroundResource(R.drawable.background_payment_option);
        upi_relative.setBackgroundResource(R.drawable.background_payment_option);
        net_banking_rel.setBackgroundResource(R.drawable.background_select_payment_option);

        rtgs_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        card_type_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        upi_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
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

        // Amount Calculated Final Amount and Show Amount in Bottom of Screen
        calculateAmountWithCharges();

        // All Position Unselected.
        for (int i = 0; i < popularBankArrayList.size(); i++) {
            popularBankArrayList.get(i).setSelected(false);
        }

        select_bank_tv.setText(""); // Bank Name TextView Blank
        select_bank_tv.setHint(getResources().getString(R.string.select_bank));
        select_bank_tv.setVisibility(View.GONE);
        /*popularBankListAdapter = new PopularBankListAdapter(popularBankArrayList, context , this);
        recycler_view.setAdapter(popularBankListAdapter);*/
        show_upi_list.setVisibility(View.GONE);
        bank_account_details_lin.setVisibility(View.GONE); // Bank Account Details Layout
        show_all_bank_lin.setVisibility(View.GONE);
        show_bank_name_lin.setVisibility(View.GONE);
        //show_bank_name_lin.setVisibility(View.VISIBLE); // Bank List Layout
        //show_all_bank_lin.setVisibility(View.VISIBLE);  // Select Bank DropDown Layout Visible
    }

    void UPISelected()
    {

        rtgs_rel.setBackgroundResource(R.drawable.background_payment_option);
        card_type_rel.setBackgroundResource(R.drawable.background_payment_option);
        net_banking_rel.setBackgroundResource(R.drawable.background_payment_option);
        upi_relative.setBackgroundResource(R.drawable.background_select_payment_option);

        rtgs_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        card_type_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        net_banking_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        upi_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));

        // If Payment Mode Not Selected NEFT/RGTS Then Value Blank
        // editTextAndTextViewValueBlank();

        // If Payment Option Selected After that Error Gone
        payment_option_error_tv.setVisibility(View.GONE);

        show_upi_list.setVisibility(View.VISIBLE);
        //recycler_view_upi.setVisibility(View.VISIBLE); // UPI Option Show List Layout
        paymentModeType = UPI;
        selectPaymentOptionClearUPIOption(UPI);

      //  showUPIAppsOption(upiAppsArrayList);


        ///////////

        editTextAndTextViewValueBlank();

        recycler_view.setVisibility(View.GONE); // Bank List Layout Show
        select_bank_error_tv.setVisibility(View.GONE); // Hide Bank Selection Required Error.

        isSelectNetBankingBank = false; // Use For Check Validation Net Banking Bank Not Selected

        // Amount  Calculated Final Amount and Show Amount in Bottom of Screen
        calculateAmountWithCharges();


        show_bank_name_lin.setVisibility(View.GONE); // Bank List Layout
        bank_account_details_lin.setVisibility(View.GONE); // Bank Account Details Layout
        show_all_bank_lin.setVisibility(View.GONE);
    }

    // UPI Option Selected  old one
    /*void upiSelected()
    {
        // If Payment Mode Not Selected NEFT/RGTS Then Value Blank
        editTextAndTextViewValueBlank();

        // If Payment Option Selected After that Error Gone
        payment_option_error_tv.setVisibility(View.GONE);

            *//*if (isArrowDown) {
                upi_option_down_arrow_img.setImageResource(R.drawable.drop_up);
            } else {
                upi_option_down_arrow_img.setImageResource(R.drawable.drop_down);
            }
            isArrowDown = !isArrowDown;*//*

        recycler_view_upi.setVisibility(View.VISIBLE); // UPI Option Show List Layout

        selectPaymentOptionClearUPIOption(UPI);

        //ArrayList<UPIAppInfoListModel> upiAppsArrayList = getInstalledUPIApps();
    }*/

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

    public void getDXEBankDetailsAPI(boolean showLoader,String regionType)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);
            urlParameter.put("countryName", "India");
            urlParameter.put("region", regionType);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter,
                    ApiConstants.GET_DXE_BANK_DETAILS, ApiConstants.GET_DXE_BANK_DETAILS_ID,
                    showLoader, "POST");

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
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter,
                    ApiConstants.GET_BANK_CHARGES, ApiConstants.GET_BANK_CHARGES_ID,
                    showLoader, "GET");


        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void getCustomPaymentAPI(boolean showLoader, String submit)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            //this both use
               // "region" : "IND", // IND, GCC                                                         <---
              //  "paymentGateway" : "RAZORPAY", // PHONEPE,RAZORPAY,PAYTABS                            <---


            if(RegionType.equalsIgnoreCase("IND"))
            {
                paymentGetwayType="RAZORPAY";
            }
            else  if(RegionType.equalsIgnoreCase("GCC"))
            {
                paymentGetwayType="PAYTABS";
            }

            Log.e("paymentGetwayType","...1001...@@@@@@@@............."+paymentGetwayType);
            Log.e("RegionType","...1001...@@@@@@@@............."+RegionType);
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);
            urlParameter.put("amount", amount_et.getText().toString().trim());
            urlParameter.put("paymentMode", paymentModeType);

            // Check PaymentMode is NEFT Then Send If Condition Value.
            if(paymentModeType.equalsIgnoreCase(NEFT))
            {
                urlParameter.put("bankPaymentMethod", mode_payment_tv.getText().toString().trim());
                urlParameter.put("bankUTRNo", cheque_et.getText().toString().trim());
                urlParameter.put("bankPaymentDate", send_dob_server);
                urlParameter.put("bankNeftId", bankID);
            }
            else{}

            String remark = remark_et.getText().toString().trim();
            if(!remark.equalsIgnoreCase(""))
            {
                remark = remark.replace("\n", " "); // '\n' replace
                //remark = remark.replaceAll("\\s+", " ");
            }
            else{}

           // Log.e("------- remark : ", " remark : " +  remark.toString());
            urlParameter.put("region", RegionType);
            urlParameter.put("paymentGateway", paymentGetwayType);
            urlParameter.put("remark", remark);
            urlParameter.put("submit", submit);
            urlParameter.put("deviceId", ""+ uuid);
            urlParameter.put("deviceType", "Android");

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.CUSTOM_PAYMENT_INIT,
                    ApiConstants.CUSTOM_PAYMENT_INIT_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    // Amount  Calculated Final Amount and Show Amount in Bottom of Screen
    void calculateAmountWithCharges()
    {
        // First Check Amount Field Blank or Not If not Then Call Amount Calculation.
        if(!amount_et.getText().toString().trim().equalsIgnoreCase(""))
        {
            String intPutAmount = amount_et.getText().toString().trim();

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
            total_amount_tv.setText(currencySymbol + "" + totalAmount);
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

                    if(!modelArrayList.isEmpty())
                    {
                        modelArrayList.clear();
                    }
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
                        Log.e("modelArrayList","..1016.....##########/...."+modelArrayList.size());
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
                        //Log.v("------Diamond----- : ", "--------BankJSONObject-------- : " + jsonObject);

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
                            Log.e("popularBankArrayList","....628....##........."+popularBankArrayList.size());
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
                        Log.e("allBankArrayList","....628....##........."+allBankArrayList.size());

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

                case ApiConstants.CUSTOM_PAYMENT_INIT_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                       // Log.e("-------Diamond : ", "RTGS/NEFT : " + jsonObjectData.toString());
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        JSONObject jObjUserDetails = jObjDetails.optJSONObject("user_data");

                        String firstname = CommonUtility.checkString(jObjUserDetails.optString("firstname"));
                        String lastname = CommonUtility.checkString(jObjUserDetails.optString("lastname"));
                        String user_email = CommonUtility.checkString(jObjUserDetails.optString("email"));
                        String user_mobile = CommonUtility.checkString(jObjUserDetails.optString("mobile"));

                        Log.e("paymentModeType","1266......."+paymentModeType);

                        System.out.println("First Name: " + firstname);
                        System.out.println("Last Name: " + lastname);
                        System.out.println("Email: " + user_email);
                        System.out.println("Mobile: " + user_mobile);


                        // Check Final Submit Value or Calculate Amount.
                        if(finalSubmitValue.equalsIgnoreCase("yes"))
                        {
                            //Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();

                            Constant.paymentOrderID = CommonUtility.checkString(jObjDetails.optString("order_id"));
                            Constant.paymentUserID = CommonUtility.checkString(jObjDetails.optString("user_id"));

                            if(paymentModeType.equalsIgnoreCase(NEFT))
                            {
                                Constant.comeFrom = "customPayment";
                                Intent intent = new Intent(activity, PaymentStatusScreenActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0,0);

                            }
                            else{
                                totalAmount = CommonUtility.checkString(jObjDetails.optString("total_amount"));
                                callbackUrl = CommonUtility.checkString(jObjDetails.optString("callback_url"));
                                ///Phone Pe comment here
                                //createCheckSumPaymentInitiatedAndOpenPhonePe();
                                Log.e("amount_et","..1239...****************....."+amount_et.getText().toString());
                                Log.e("Amount In INT","..1240....."+Integer.parseInt(amount_et.getText().toString()));
                                Log.e("RegionType","..1307...."+RegionType);

                                JSONObject billingAddress = jObjUserDetails.optJSONObject("billing_address");

                                if (RegionType.equalsIgnoreCase("IND"))
                                {
                                    if(paymentModeType.equalsIgnoreCase(UPI))
                                    {
                                        RazorpayUtility.INSTANCE.initializePayment(
                                                CustomPaymentScreenActivity.this,
                                                Integer.parseInt(totalAmount),
                                                user_mobile,
                                                user_email,
                                                PaymentMethod.UPI,
                                                context.getResources().getString(R.string.app_name),
                                                R.drawable.appicon
                                        );
                                    }
                                    else if(paymentModeType.equalsIgnoreCase(NET_BANKING))
                                    {
                                        RazorpayUtility.INSTANCE.initializePayment(
                                                CustomPaymentScreenActivity.this,
                                                Integer.parseInt(totalAmount),
                                                user_mobile,
                                                user_email,
                                                PaymentMethod.NET_BANKING,
                                                context.getResources().getString(R.string.app_name),
                                                R.drawable.appicon
                                        );
                                    }
                                    else if(paymentModeType.equalsIgnoreCase(CREDIT_CARD))
                                    {
                                        RazorpayUtility.INSTANCE.initializePayment(
                                                CustomPaymentScreenActivity.this,
                                                Integer.parseInt(totalAmount),
                                                user_mobile,
                                                user_email,
                                                PaymentMethod.CARD,
                                                context.getResources().getString(R.string.app_name),
                                                R.drawable.appicon
                                        );
                                    }
                                }
                                else if (RegionType.equalsIgnoreCase("GCC"))
                                {
                                    String getaddress = "",getcity= "",getstate= "",getcountry= "",getzip= "",getip= "";
                                    if (billingAddress != null) {
                                         getaddress = billingAddress.optString("address");
                                         getcity = billingAddress.optString("city");
                                         getstate = billingAddress.optString("state");
                                         getcountry = billingAddress.optString("country");
                                         getzip = billingAddress.optString("zip");
                                         getip = billingAddress.optString("ip");

                                        Log.e("Billing Address", "Address: " + getaddress);
                                        Log.e("Billing Address", "City: " + getcity);
                                        Log.e("Billing Address", "State: " + getstate);
                                        Log.e("Billing Address", "Country: " + getcountry);
                                        Log.e("Billing Address", "Zip: " + getzip);
                                        //Log.e("Billing Address", "IP: " + ip);
                                    }

                                    if(paymentModeType.equalsIgnoreCase(CREDIT_CARD)){

                                        /*Log.e("exchange_rate",".1399......"+CommonUtility.checkDouble(jObjDetails.optString("exchange_rate")));
                                        Log.e("amount_et",".1400......"+CommonUtility.checkInt(jObjDetails.optString("amount")));*/

                                        double exchangeRate = CommonUtility.checkDouble(jObjDetails.optString("exchange_rate")); // Ensure it's a double
                                        int amount = CommonUtility.checkInt(jObjDetails.optString("amount")); // Ensure it's an integer
                                        int total_amount = CommonUtility.checkInt(jObjDetails.optString("total_amount"));
                                        double resultDouble = total_amount * exchangeRate;

                                        int result = (int) resultDouble;
                                        String formattedResult = String.format("%.2f", resultDouble);

                                        double resultWithTwoDecimals = Double.parseDouble(formattedResult);
                                        Log.e("resultWithTwoDecimals", "..14000..." + resultWithTwoDecimals);

                                        String currecnySymbol=jObjDetails.optString("currency_symbol");
                                        String orderID=jObjDetails.optString("order_id");

                                        Log.e("currecnySymbol","..1424...."+currecnySymbol+"....orderID...."+orderID);
                                        PayTabsPaymentManager.INSTANCE.setCurrencyAndCartId(
                                                currecnySymbol,
                                                orderID
                                        );

                                        PayTabsPaymentManager.INSTANCE.setBillingDetails(
                                                 getcity,
                                               getcountry,
                                                user_email,
                                                firstname+lastname,
                                                 user_mobile,
                                               getstate,
                                               getaddress,
                                                getip
                                        );

                                        PayTabsPaymentManager.INSTANCE.initiateCardPayment(
                                                CustomPaymentScreenActivity.this,
                                                resultWithTwoDecimals,
                                                CustomPaymentScreenActivity.this
                                        );

                                    }
                                }

                            }
                        }
                        else{
                            currencySymbol = CommonUtility.checkString(jObjDetails.optString("currency_symbol"));
                            bankCharge = CommonUtility.checkString(jObjDetails.optString("bank_charge"));
                            bankChargePerc = CommonUtility.checkString(jObjDetails.optString("bank_charge_perc"));
                            totalAmount = CommonUtility.checkString(jObjDetails.optString("total_amount"));
                        }
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }
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

                case  ApiConstants.DISMISS_ORDER_ID:
                    if (jsonObjectData.optString("status").equalsIgnoreCase("1")) {
                        // Get the message
                        String messageFails = jsonObjectData.optString("msg");

                        // Show the message in a toast
                        Toast.makeText(getApplicationContext(), messageFails, Toast.LENGTH_SHORT).show();
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
            /*Log.d("PAPAYACODERS", "requestCode: " + requestCode);
            Log.d("PAPAYACODERS", "resultCode: " + resultCode);
            Log.d("PAPAYACODERS", "data: " + data);*/
            if (resultCode == RESULT_OK && data != null)
            {
                // Get the transaction result from the Intent
               /* String txnResult = data.getStringExtra("key_txn_result");
                if (txnResult != null) {
                    Log.d("PAPAYACODERS", "Non-JSON response: " + txnResult);

                } else {
                    Log.d("PAPAYACODERS", "No transaction result data found");
                    Toast.makeText(activity, "Else : " + requestCode + " : " +resultCode , Toast.LENGTH_SHORT).show();
                }*/
                Constant.comeFrom = "customPayment";
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

    // Utility method to parse the non-JSON response string
    private Map<String, String> parseResponse(String response) {
        Map<String, String> resultMap = new HashMap<>();

        // Split by '::' to get key-value pairs
        String[] pairs = response.split("::");
        for (String pair : pairs) {
            // Split each pair by '=' to get key and value
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                resultMap.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }

        return resultMap;
    }

    // Create Check Sum Fot Payment Status.
    private void checkStatus()
    {
        String xVerify = CommonUtility.sha256("/pg/v1/status/" + MERCHANT_ID + "/" + CommonUtility.generateTransactionId() + SALT) + "###"+SALT_INDEX;
        Log.d("phonepe", "xVerify: " + xVerify);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = BASE_URL + "/pg/v1/status/" + MERCHANT_ID + "/" + Constant.paymentOrderID;
        Log.d("phonepe", "URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        Log.d("phonepe", "response: " + jsonResponse.toString());
                        Toast.makeText(activity, "response: " + jsonResponse.toString(), Toast.LENGTH_SHORT).show();
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");

                        if (success) {
                            Log.d("phonepe", "Success123: " + message);
                            runOnUiThread(() -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show());
                        } else {
                            Log.d("phonepe", "Failure: " + message);
                        }
                    } catch (JSONException e) {
                        Log.e("phonepe", "JSON Parsing error", e);
                    }
                },
                error -> Log.e("phonepe", "Failed to check status", error)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-VERIFY", xVerify);
                headers.put("X-MERCHANT-ID", MERCHANT_ID);
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    private boolean validateFields()
    {
        String name = name_et.getText().toString().trim();
        String companyName = company_name_et.getText().toString().trim();
        String amount = amount_et.getText().toString().trim();
        String remark = remark_et.getText().toString().trim();
        String modePayment = mode_payment_tv.getText().toString().trim();
        String chequeNumber = cheque_et.getText().toString().trim();
        String chequeDate = select_date_tv.getText().toString().trim();

        if (name.length() == 0 || name == null|| name.equalsIgnoreCase("")) {
            name_error_tv.setVisibility(View.VISIBLE);
            name_et.requestFocus();
            name_et.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }
        else if ((companyName.length() == 0 || companyName == null|| companyName.equalsIgnoreCase("")) &&
                !userRole.equalsIgnoreCase("BUYER")) {
            company_name_error_tv.setVisibility(View.VISIBLE);
            company_name_et.requestFocus();
            company_name_et.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }
        else if (amount.length() == 0 || amount == null|| amount.equalsIgnoreCase("")) {
            amount_error_tv.setVisibility(View.VISIBLE);
            amount_et.requestFocus();
            amount_et.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }
        else if (remark.length() == 0 || remark == null|| remark.equalsIgnoreCase("")) {
            remark_error_tv.setVisibility(View.VISIBLE);
            remark_et.requestFocus();
            remark_et.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }
        else if (paymentModeType.length() == 0 || paymentModeType == null|| paymentModeType.equalsIgnoreCase("")) {
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
        /*else if (!isSelectNetBankingBank && paymentModeType.equalsIgnoreCase(NET_BANKING)) {
            select_bank_error_tv.setVisibility(View.VISIBLE);
            show_all_bank_lin.requestFocus();
            return false;
        }*/
        return true;
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
            Log.e("allBankArrayList",",...#######...1158..."+allBankArrayList.size());
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
    /*private void showUPIAppsOption(ArrayList<UPIAppInfoListModel> upiApps) {
        Log.e("upiApps","...1794........"+upiApps.size());
        if (upiApps.isEmpty()) {
            Toast.makeText(this, "No UPI apps installed", Toast.LENGTH_SHORT).show();
            upi_option_lin.setVisibility(View.GONE);
            return;
        }
        upiOptionListAdapter = new UPIOptionListAdapter(upiApps, context , this);
        recycler_view_upi.setAdapter(upiOptionListAdapter);
    }*/

    private void showUPIAppsOption(ArrayList<UPIAppInfoListModel> upiApps) {

        Log.e("upiApps","...2521...."+upiApps.isEmpty());
        if (upiApps.isEmpty())
        {

            noupifound_tv.setVisibility(View.VISIBLE);
            recycler_view_upi.setVisibility(View.GONE);
        }
        else
        {
            noupifound_tv.setVisibility(View.GONE);
            noupifound_tv.setText("Found");
            recycler_view_upi.setVisibility(View.VISIBLE);
        }
        if (upiApps.isEmpty()) {
            Toast.makeText(this, R.string.noupifound, Toast.LENGTH_SHORT).show();
            upi_option_lin.setVisibility(View.GONE);
            return;
        }

        Log.e("upiApps","...2521..**..Size.."+upiApps.size());
        upiOptionListAdapter = new UPIOptionListAdapter(upiApps, context , this);
        recycler_view_upi.setAdapter(upiOptionListAdapter);
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Constant.comeFrom = "customPayment";
        Intent intent = new Intent(activity, PaymentStatusScreenActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        //Toast.makeText(this, "Payment is Fail:" + s, Toast.LENGTH_SHORT).show();
        getPaymentCancel(false);
    }

    // PayTabs payment methods..

    @Override
    public void onError(@NonNull PaymentSdkError paymentSdkError) {
        Log.e("paymentSdkError","...2091.....**...."+paymentSdkError.toString());
        Toast.makeText(this, "Payment is Fail ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentFinish(@NonNull PaymentSdkTransactionDetails paymentSdkTransactionDetails) {
        Log.e("On payment","Finish......2096....");
        Log.e("CartAmount","...2112..**...."+paymentSdkTransactionDetails.getCartAmount());
        Log.e("TransactionType","...2113..**...."+paymentSdkTransactionDetails.getTransactionType());
        Constant.comeFrom = "customPayment";
        Intent intent = new Intent(activity, PaymentStatusScreenActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    @Override
    public void onPaymentCancel()
    {
       // Toast.makeText(this, "Payment is Cancel ", Toast.LENGTH_SHORT).show();
        getPaymentCancel(false);
    }

    public void getPaymentCancel(boolean showLoader) {
        Log.e("paymentOrderID", "...1871......" + Constant.paymentOrderID);

        String uuid = CommonUtility.getAndroidId(this);
        HashMap<String, String> urlParameter = new HashMap<>();
        urlParameter.put("sessionId", uuid);
        urlParameter.put("orderId", String.valueOf(Constant.paymentOrderID));
        urlParameter.put("type", "custom-payment");

        VollyApiActivity vollyApiActivity = new VollyApiActivity(
                this,
                this,
                urlParameter,
                ApiConstants.DISMISS_ORDER,
                ApiConstants.DISMISS_ORDER_ID,
                showLoader,
                "POST"
        );
    }


}