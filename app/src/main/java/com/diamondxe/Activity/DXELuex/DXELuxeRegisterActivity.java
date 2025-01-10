package com.diamondxe.Activity.DXELuex;

import static com.diamondxe.ApiCalling.ApiConstants.INDIA_COUNTRY_CODE;
import static com.diamondxe.ApiCalling.ApiConstants.INDIA_FLAG_URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Adapter.CountryListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.CountryListModel;
import com.diamondxe.Interface.DialogItemClickInterface;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Utils;
import com.diamondxe.databinding.ActivityDxeluexRegisterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class DXELuxeRegisterActivity extends SuperActivity implements DialogItemClickInterface, RecyclerInterface {

    ActivityDxeluexRegisterBinding binding;
    String email="",phoneno,firstname,lastname,countryCode,user_login;
    private HashMap<String, String> urlParameter;
    private VollyApiActivity vollyApiActivity;
    private boolean isApiCalling = false;
    String buyerPhCountryCode="+91";
    String resendOTP,basicResendOTP,supplierResendOTP,countryCodeFor="",showCountryCode = "";
    android.app.AlertDialog alertDialog;
    ArrayList<CountryListModel> countryArrayList;
    CountryListAdapter countryListAdapter;
    private BottomSheetDialog dialog;
    EditText search_et;
    RecyclerView recycler_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this is for full screen
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        binding=ActivityDxeluexRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        countryArrayList = new ArrayList<>();
        user_login = CommonUtility.getGlobalString(this, "user_login");
        // If User Login then show User Name and User Role
        if(!user_login.equalsIgnoreCase(""))
        {
            email = CommonUtility.getGlobalString(this, "login_email");
            phoneno = CommonUtility.getGlobalString(this, "login_mobile_no");
            firstname = CommonUtility.getGlobalString(this, "login_first_name");
            lastname = CommonUtility.getGlobalString(this, "login_last_name");
            countryCode = CommonUtility.getGlobalString(this, "user_country_code");

            /*binding.buyerMobileVerifyImg.setVisibility(View.VISIBLE);
            binding.verifyMobileLin.setVisibility(View.GONE);*/
            binding.verifyEmailLin.setVisibility(View.GONE);
            binding.buyerEmailVerifyImg.setVisibility(View.VISIBLE);
            binding.firstNameEt.setText(firstname);
            binding.lastNameEt.setText(lastname);
            binding.emailEt.setText(email);
            binding.mobileNumberEt.setText(phoneno);
            binding.countryCode.setText(countryCode);
            binding.submitFrom.setAlpha(1F);
            /*username_tv.setText(CommonUtility.getGlobalString(this, "login_first_name")+" "+CommonUtility.getGlobalString(context, "login_last_name"));
            login_type_role_tv.setText(""+CommonUtility.getGlobalString(this, "login_user_role"));*/
        }
        else {
            binding.buyerEmailVerifyImg.setVisibility(View.GONE);
            binding.verifyEmailLin.setVisibility(View.VISIBLE);
        }


        Picasso.with(this)
                .load(INDIA_FLAG_URL)
                .into(binding.countryImg);
        binding.countryCode.setText(INDIA_COUNTRY_CODE);
        binding.countryCode.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.white_arrow, 0);


        binding.backImg.setOnClickListener(this);
        binding.submitFrom.setOnClickListener(this);
        binding.verifyEmailLin.setOnClickListener(this);
        binding.countryCodeLin.setOnClickListener(this);
        binding.countryCodeLin.setOnClickListener(this);

        binding.mobileNumberEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    // EditText has gained focus
                    Log.d("FocusEvent", "Mobile Number EditText has gained focus");
                } else {
                    // EditText has lost focus
                    getWindow().setFlags(
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    );
                    Log.d("FocusEvent", "Mobile Number EditText has lost focus");
                }
            }
        });

        binding.emailEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    // EditText has gained focus
                    Log.d("FocusEvent", "Mobile Number EditText has gained focus");
                } else {
                    // EditText has lost focus
                    getWindow().setFlags(
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    );
                    Log.d("FocusEvent", "Mobile Number EditText has lost focus");
                }
            }
        });

        binding.lastNameEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    Log.d("FocusEvent", "Mobile Number EditText has gained focus");
                } else {
                    getWindow().setFlags(
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    );
                    Log.d("FocusEvent", "Mobile Number EditText has lost focus");
                }
            }
        });

        binding.firstNameEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    Log.d("FocusEvent", "Mobile Number EditText has gained focus");
                } else {
                    getWindow().setFlags(
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    );
                    Log.d("FocusEvent", "Mobile Number EditText has lost focus");
                }
            }
        });

        binding.emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("emailEt No","Change....111....");
                if (!s.toString().equals(email)) {
                    Log.e("emailEt No", "Text has changed...");
                    binding.verifyEmailLin.setVisibility(View.VISIBLE);
                    binding.buyerEmailVerifyImg.setVisibility(View.GONE);
                    email = s.toString();  // Update the previousText
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    android.app.AlertDialog alertDialog1;
    void showThankyouDialog(final Activity activity)
    {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dxe_luxe_thankyou_dialog, null);
        dialogBuilder.setView(dialogView);
        alertDialog1 = dialogBuilder.create();

        final ImageView cross_img = dialogView.findViewById(R.id.cross_img);
        CardView add_to_cart_card_view=dialogView.findViewById(R.id.add_to_cart_card_view);
        //alertDialog1.setView(webView);

        add_to_cart_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cross_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog1.dismiss();
            }
        });

        alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog1.setCancelable(true);
        alertDialog1.setCanceledOnTouchOutside(false);
        alertDialog1.show();

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        int id = view.getId();
        if(id==binding.submitFrom.getId())
        {
            onLuxeUserRegister(false);
            //showThankyouDialog(DXELuexRegisterActivity.this);
        }
        else if(id==binding.backImg.getId())
        {
            finish();
        }
        else if(id==binding.verifyEmailLin.getId())
        {
            if(validateFieldsEmailVerifyOnly())
            {
                Utils.hideKeyboard(this);
                resendOTP = "";
                onVerifyEmailAPI(false, "1", "");

            }
        }
        else  if(id==binding.countryCodeLin.getId())
        {
            Utils.hideKeyboard(this);
            countryCodeFor = "countryCodeBuyerSection";
            showCountryCode = "yes";
            getCountryListAPI(false);
        }

    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

        try {

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {
                case ApiConstants.VERIFY_EMAIL_ID:
                    Log.e("status","142.....****....."+jsonObjectData.optString("status"));
                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        resendOTP = "";
                        binding.submitFrom.setAlpha(1F);
                        binding.verifyEmailLin.setVisibility(View.GONE);
                        binding.buyerEmailVerifyImg.setVisibility(View.VISIBLE);
                        binding.emailErrorTv.setVisibility(View.GONE);
                        alertDialog.dismiss();
                        //JSONObject jObjDetails = jsonObjectData.optJSONObject("details");
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("2"))
                    {
                        if(resendOTP.equalsIgnoreCase(""))
                        {
                            showEmailVerificationPopup(this, this);
                        }

                        //  Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.GET_COUNTRY_LIST_ID:
                    isApiCalling = false;
                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if(countryArrayList.size() > 0)
                        {
                            countryArrayList.clear();
                        } else{}

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            CountryListModel model = new CountryListModel();
                            model.setId(CommonUtility.checkString(objectCodes.optString("id")));
                            model.setTitle(CommonUtility.checkString(objectCodes.optString("name")));
                            model.setCountryCode(CommonUtility.checkString(objectCodes.optString("country_code")));
                            model.setPhoneCode(CommonUtility.checkString(objectCodes.optString("phonecode")));
                            model.setImage(CommonUtility.checkString(objectCodes.optString("flag")));
                            countryArrayList.add(model);

                        }

                        showCountryCodeList();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ApiConstants.LUXE_USER_REGISTRATION_ID:
                    isApiCalling = false;
                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {

                        String msg = jsonObjectData.optString("msg");
                        Log.e("msg","....405....."+msg);
                        showThankyouDialog(DXELuxeRegisterActivity.this);
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void getErrorResponce(String error, int service_ID) {

    }

    public void onVerifyEmailAPI(boolean showLoader, String requestOtp, String otp)
    {
        if (Utils.isNetworkAvailable(this))
        {
            Log.e("emailEt","...145....."+binding.emailEt.getText().toString().trim());

            urlParameter = new HashMap<String, String>();
            urlParameter.put("email", ""+ binding.emailEt.getText().toString().trim());
            urlParameter.put("requestOtp", ""+ requestOtp);
            urlParameter.put("otp", ""+ otp);
            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(this,this, urlParameter, ApiConstants.VERIFY_EMAIL, ApiConstants.VERIFY_EMAIL_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void onLuxeUserRegister(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(this))
        {

            Log.e("mobile_auth_token","..."+CommonUtility.getGlobalString(this, "mobile_auth_token"));
            Log.e("firstNameEt","..."+binding.firstNameEt.getText().toString());
            Log.e("lastNameEt",".."+binding.lastNameEt.getText().toString());
            Log.e("mobileNumberEt",".."+binding.mobileNumberEt.getText().toString());
            Log.e("emailEt","..."+binding.emailEt.getText().toString());

            urlParameter = new HashMap<String, String>();
            urlParameter.put("firstName", ""+ binding.firstNameEt.getText().toString().trim());
            urlParameter.put("lastName", ""+ binding.lastNameEt.getText().toString().trim());
            urlParameter.put("mobileNo", ""+ binding.mobileNumberEt.getText().toString().trim());
            urlParameter.put("emailId", ""+ binding.emailEt.getText().toString().trim());
            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(this,this, urlParameter, ApiConstants.LUXE_USER_REGISTRATION, ApiConstants.LUXE_USER_REGISTRATION_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showCountryCodeList()
    {
        dialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        dialog.setContentView(R.layout.dialog_show_country_code);

        recycler_view = dialog.findViewById(R.id.recycler_view);
        search_et = dialog.findViewById(R.id.search_et);

        TextView textView2 = dialog.findViewById(R.id.textView2);

        textView2.setText(getResources().getString(R.string.country_list));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);

        countryListAdapter = new CountryListAdapter(countryArrayList, this, this, showCountryCode);
        recycler_view.setAdapter(countryListAdapter);
        countryListAdapter.notifyDataSetChanged();

        search_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

                try {
                    String text = search_et.getText().toString().toLowerCase(Locale.getDefault());
                    int textlength = search_et.getText().length();

                    countryListAdapter.filter(text, textlength);

                } catch (Exception e) {}
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }
        });

        ImageView ib_cross = dialog.findViewById(R.id.ib_cross);

        ib_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = 1000; // Set your desired fixed height here, in pixels

            window.setAttributes(lp);
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
        dialog.show();
        /*dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//      dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);*/

    }

    TextView resend_otp_tv, otp_timer_tv, otp_required_error_tv;
    EditText otp_et;
    LinearLayout otp_lin;

    private CountDownTimer otpTimer;
    void showEmailVerificationPopup(final Activity activity,final Context context)
    {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_email_verification_popup, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();

        final ImageView cross_img = dialogView.findViewById(R.id.cross_img);
        final LinearLayout mobile_lin = dialogView.findViewById(R.id.mobile_lin);
        final LinearLayout email_lin = dialogView.findViewById(R.id.email_lin);
        final LinearLayout verify_lin = dialogView.findViewById(R.id.verify_lin);
        final TextView mobile_tv = dialogView.findViewById(R.id.mobile_tv);
        final TextView email_tv = dialogView.findViewById(R.id.email_tv);
        final TextView otp_send_msg_tv = dialogView.findViewById(R.id.otp_send_msg_tv);
        otp_lin = dialogView.findViewById(R.id.otp_lin);
        otp_et = dialogView.findViewById(R.id.otp_et);
        otp_required_error_tv = dialogView.findViewById(R.id.otp_error_requir_tv);
        otp_timer_tv = dialogView.findViewById(R.id.otp_timer_tv);
        resend_otp_tv = dialogView.findViewById(R.id.resend_otp_tv);

        setOTPTimer();

        cross_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.e("Cross","..604....");
                stopOTPTimer();
                alertDialog.dismiss();
            }
        });

        mobile_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                otp_send_msg_tv.setText(getResources().getString(R.string.opt_send_mobile_msg));

                mobile_lin.setBackgroundResource(R.drawable.background_button_shadow);
                email_lin.setBackgroundResource(R.drawable.background_white_view);
                mobile_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
                email_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));

            }
        });
        email_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                otp_send_msg_tv.setText(getResources().getString(R.string.opt_send_email_msg));

                mobile_lin.setBackgroundResource(R.drawable.background_white_view);
                email_lin.setBackgroundResource(R.drawable.background_button_shadow);
                mobile_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
                email_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
            }
        });

        resend_otp_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                stopOTPTimer();
                setOTPTimer();
                onVerifyEmailAPI(false, "1", "");

            }
        });

        verify_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(validateFieldsEmailVerification())
                {
                    resendOTP = "";
                    basicResendOTP = "";
                    supplierResendOTP = "";
                    onVerifyEmailAPI(false, "0", otp_et.getText().toString().trim());
                }else{}

            }
        });

        otp_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!otp_et.getText().toString().equalsIgnoreCase("")){
                    otp_required_error_tv.setVisibility(View.GONE);
                    otp_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }else{}

                if(otp_et.getText().toString().equalsIgnoreCase("")){
                    otp_required_error_tv.setVisibility(View.GONE);
                    otp_et.setBackgroundResource(R.drawable.border_line_view);
                }else{}
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }
    private boolean validateFieldsEmailVerification()
    {
        String otp = otp_et.getText().toString().trim();

        if (otp_lin.getVisibility() == View.VISIBLE && otp.equalsIgnoreCase("")) {
            otp_required_error_tv.setVisibility(View.VISIBLE);
            otp_et.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }

        return true;
    }
    void setOTPTimer()
    {
        otpTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                resend_otp_tv.setVisibility(View.GONE);
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                //long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                //otp_timer_tv.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                otp_timer_tv.setText(f.format(min) + ":" + f.format(sec));
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                resend_otp_tv.setVisibility(View.VISIBLE);
                otp_timer_tv.setText("00:00");
            }
        }.start();
    }
    void stopOTPTimer() {
        if (otpTimer != null) {
            otpTimer.cancel();
            otpTimer = null;
        }
    }

    private boolean validateFieldsEmailVerifyOnly() {
        String email = binding.emailEt.getText().toString().trim();
        if (email.length() == 0 || email == null|| email.equalsIgnoreCase("")) {
            binding.emailErrorTv.setVisibility(View.VISIBLE);
          //  binding.emailEt.setBackgroundResource(R.drawable.border_red_line_view);
            binding.emailErrorTv.setText(getResources().getString(R.string.email_required));
            return false;
        }
        return true;
    }

    public void getCountryListAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(this))
        {
            urlParameter = new HashMap<String, String>();
            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(this,this, urlParameter, ApiConstants.GET_COUNTRY_LIST, ApiConstants.GET_COUNTRY_LIST_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    @Override
    public void dialogItemClick(String value, String action) {

    }

    @Override
    public void itemClick(int position, String action) {
        Log.e("action","...673......."+action);
        if(action.equalsIgnoreCase("countryName"))
        {
            CountryListModel model = countryArrayList.get(position);
            Log.e("PhoneCode","...673......."+model.getPhoneCode());
            binding.countryCode.setText(model.getPhoneCode());
            buyerPhCountryCode = model.getPhoneCode();
            binding.countryCode.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.white_arrow, 0);
            if (!model.getImage().equalsIgnoreCase(""))
            {
                Picasso.with(this)
                        .load(model.getImage())
                        .into(binding.countryImg);
            }
            dialog.dismiss();
        }
    }
}