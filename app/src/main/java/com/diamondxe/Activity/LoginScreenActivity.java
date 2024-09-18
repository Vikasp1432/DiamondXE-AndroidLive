package com.diamondxe.Activity;

import static com.diamondxe.ApiCalling.ApiConstants.INDIA_COUNTRY_CODE;
import static com.diamondxe.ApiCalling.ApiConstants.INDIA_FLAG_URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Adapter.CountryCodeListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.CountryListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class LoginScreenActivity extends SuperActivity implements RecyclerInterface {

    private TextView email_tv, mobile_tv, forgot_password_tv, login_tv, signup_now_tv, email_required_error_tv, passowrd_required_error_tv,
            login_now_tv;
    private EditText email_et, password_et, mobile_number_et, search_et, otp_et;
    private TextView mobile_number_required_tv, country_code, otp_required_error_tv, otp_timer_tv, resend_otp_tv;
    private LinearLayout mobile_number_lin,country_code_lin, email_password_lin,email_lin, mobile_lin, main_mobile_lin, otp_lin;
    private ImageView country_img, back_img;
    private CheckBox remember_me_check;
    private RelativeLayout remember_me_rel;

    boolean rememberMeCheck = false;

    private BottomSheetDialog dialog;
    RecyclerView recycler_view;
    ArrayList<CountryListModel> countryArrayList;
    CountryCodeListAdapter countryCodeListAdapter;

    String selectedLoginTypeTab = "Email",loginType="email";
    String checkOtpValidation="",rememberEmail="", rememberPassword="";

    private CountDownTimer otpTimer;

    private Activity activity;
    private Context context;

    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        context = activity = this;

        countryArrayList = new ArrayList<>();

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        email_tv = findViewById(R.id.email_tv);
        email_tv.setOnClickListener(this);

        mobile_tv = findViewById(R.id.mobile_tv);
        mobile_tv.setOnClickListener(this);

        forgot_password_tv = findViewById(R.id.forgot_password_tv);
        forgot_password_tv.setOnClickListener(this);

        login_tv = findViewById(R.id.login_tv);
        login_tv.setOnClickListener(this);

        remember_me_rel = findViewById(R.id.remember_me_rel);
        remember_me_check = findViewById(R.id.remember_me_check);

        otp_et = findViewById(R.id.otp_et);
        otp_lin = findViewById(R.id.otp_lin);
        otp_required_error_tv = findViewById(R.id.otp_required_error_tv);
        otp_timer_tv = findViewById(R.id.otp_timer_tv);
        resend_otp_tv = findViewById(R.id.resend_otp_tv);
        resend_otp_tv.setOnClickListener(this);

        email_required_error_tv = findViewById(R.id.email_required_error_tv);
        passowrd_required_error_tv = findViewById(R.id.passowrd_required_error_tv);
        mobile_number_required_tv = findViewById(R.id.mobile_number_required_tv);

        country_img = findViewById(R.id.country_img);

        email_lin = findViewById(R.id.email_lin);
        mobile_lin = findViewById(R.id.mobile_lin);

        mobile_number_lin = findViewById(R.id.mobile_number_lin);
        email_password_lin = findViewById(R.id.email_password_lin);

        country_code_lin = findViewById(R.id.country_code_lin);
        country_code_lin.setOnClickListener(this);

        country_code = findViewById(R.id.country_code);
        main_mobile_lin = findViewById(R.id.main_mobile_lin);

        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        mobile_number_et = findViewById(R.id.mobile_number_et);

        signup_now_tv = findViewById(R.id.signup_now_tv);
        signup_now_tv.setOnClickListener(this);

        login_now_tv = findViewById(R.id.login_now_tv);
        login_now_tv.setOnClickListener(this);

        /*signup_now_tv.setMovementMethod(LinkMovementMethod.getInstance());
        signup_now_tv.setText(addClickablePart(getResources().getString(R.string.signup_now)));*/

        // Check Login Type Email Then Remember Me Check Show Other Wise Hide.
        if(loginType.equalsIgnoreCase("email"))
        {
            remember_me_rel.setVisibility(View.VISIBLE);
        }
        else{
            remember_me_rel.setVisibility(View.GONE);
        }

        setRememberMeTextValue();

        email_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!email_et.getText().toString().equalsIgnoreCase("")){
                    email_required_error_tv.setVisibility(View.GONE);
                    email_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(email_et.getText().toString().equalsIgnoreCase("")){
                    email_required_error_tv.setVisibility(View.GONE);
                    email_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!password_et.getText().toString().equalsIgnoreCase("")){
                    passowrd_required_error_tv.setVisibility(View.GONE);
                    passowrd_required_error_tv.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(password_et.getText().toString().equalsIgnoreCase("")){
                    passowrd_required_error_tv.setVisibility(View.GONE);
                    passowrd_required_error_tv.setBackgroundResource(R.drawable.border_line_view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobile_number_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!mobile_number_et.getText().toString().equalsIgnoreCase("")){
                    mobile_number_required_tv.setVisibility(View.GONE);
                    mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(mobile_number_et.getText().toString().equalsIgnoreCase("")){
                    mobile_number_required_tv.setVisibility(View.GONE);
                    mobile_number_lin.setBackgroundResource(R.drawable.border_line_view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otp_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!otp_et.getText().toString().equalsIgnoreCase("")){
                    otp_required_error_tv.setVisibility(View.GONE);
                    otp_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(otp_et.getText().toString().equalsIgnoreCase("")){
                    otp_required_error_tv.setVisibility(View.GONE);
                    otp_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        remember_me_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remember_me_check.isChecked())
                {
                    rememberMeCheck = true;
                } else
                {
                    rememberMeCheck = false;
                }
            }
        });



        manageDeviceBackButton();
    }

    // Set Remember Me Value
    void setRememberMeTextValue()
    {
        rememberEmail = CommonUtility.getGlobalString(activity, "remember_email");
        rememberPassword = CommonUtility.getGlobalString(activity, "remember_password");

        if(!rememberEmail.equalsIgnoreCase(""))
        {
            email_et.setText(rememberEmail);
            remember_me_check.setChecked(true);
            rememberMeCheck = true;
        }
        else{
            email_et.setHint(getResources().getString(R.string.email));
            remember_me_check.setChecked(false);
            rememberMeCheck = false;
        }

        if(!rememberPassword.equalsIgnoreCase(""))
        {
            password_et.setText(rememberPassword);
            remember_me_check.setChecked(true);
            rememberMeCheck = true;
        }
        else{
            password_et.setHint(getResources().getString(R.string.password));
            remember_me_check.setChecked(false);
            rememberMeCheck = false;
        }
    }

    /*private SpannableStringBuilder addClickablePart(String str) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);

        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent intent = new Intent(activity, SignupScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(context, R.color.black_light));
                ds.setUnderlineText(true);
            }
        }, 23, 34, 0);


        return ssb;
    }*/

    // manageClickEventForRedirection Key Blank Here.
    void constantKeyBlank()
    {
        Constant.manageClickEventForRedirection = "";
    }
    @Override
    public void onClick(View view)
    {
        Intent intent;
        int id = view.getId();

        if(id == R.id.back_img)
        {
            Utils.hideKeyboard(activity);
            constantKeyBlank();
            finish();
            /*intent = new Intent(activity, AccountSectionActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);*/
        }
       else if(id == R.id.email_tv)
        {
            Utils.hideKeyboard(activity);
            selectedLoginTypeTab = "Email";
            loginType = "email";

            email_password_lin.setVisibility(View.VISIBLE);
            main_mobile_lin.setVisibility(View.GONE);

            email_lin.setBackgroundResource(R.drawable.background_button_shadow);
            mobile_lin.setBackgroundResource(R.drawable.background_white_view);

            email_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
            mobile_tv.setTextColor(ContextCompat.getColor(context, R.color.purple));
        }
        else if(id == R.id.mobile_tv)
        {
            Utils.hideKeyboard(activity);
            selectedLoginTypeTab = "Mobile";
            loginType = "mobile";

            email_password_lin.setVisibility(View.GONE);
            main_mobile_lin.setVisibility(View.VISIBLE);

            email_lin.setBackgroundResource(R.drawable.background_white_view);
            mobile_lin.setBackgroundResource(R.drawable.background_button_shadow);

            email_tv.setTextColor(ContextCompat.getColor(context, R.color.purple));
            mobile_tv.setTextColor(ContextCompat.getColor(context, R.color.white));

            Picasso.with(context)
                    .load(INDIA_FLAG_URL)
                    .into(country_img);

            country_code.setText(INDIA_COUNTRY_CODE);
            country_code.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down_icon, 0);
        }
        else if(id == R.id.forgot_password_tv)
        {
            Utils.hideKeyboard(activity);
            constantKeyBlank();
            intent = new Intent(activity, ForgotPasswordActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else if(id == R.id.country_code_lin)
        {
            Utils.hideKeyboard(activity);

            getCountryListAPI(false);
        }
        else if(id == R.id.login_tv)
        {
            if(validateFields())
            {
                if(loginType.equalsIgnoreCase("email"))
                {
                    onBindAPI(false, loginType, "");
                }
                else
                {
                    if(checkOtpValidation.equalsIgnoreCase("check"))
                    {
                        // check OTP Field Validation
                        if(validateFieldsOTPValidation())
                        {
                            onBindAPI(false, loginType, "0");
                        }
                        else{}
                    }else{
                        onBindAPI(false, loginType, "1");
                    }
                }
            }else{}
        }
        else if(id == R.id.signup_now_tv)
        {
            Utils.hideKeyboard(activity);
            constantKeyBlank();
            intent = new Intent(activity, SignupScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else if(id == R.id.login_now_tv)
        {
            Utils.hideKeyboard(activity);
            constantKeyBlank();
            /*intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://supplier.diamondxe.com/"));
            startActivity(intent);*/
            intent = new Intent(activity, SupplierSignupWebViewActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else if(id == R.id.resend_otp_tv)
        {
            Utils.hideKeyboard(activity);
            onBindAPI(false, loginType, "1");
            //setOTPTimer();
        }
    }

    // Manage Device Back Button Event.
    void manageDeviceBackButton()
    {
        // Handle Device Back Button code.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed()
            {
                Constant.manageClickEventForRedirection="";
                finish();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
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


    public void onBindAPI(boolean showLoader, String loginType, String requestOtp)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context)) {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("email", email_et.getText().toString().trim());
            urlParameter.put("password", password_et.getText().toString().trim());
            urlParameter.put("mobileNo", mobile_number_et.getText().toString().trim());
            urlParameter.put("requestOtp", requestOtp);
            urlParameter.put("deviceType", "Android");
            urlParameter.put("userType", "Buyer");
            urlParameter.put("loginType", loginType);
            urlParameter.put("otp", otp_et.getText().toString().trim());
            urlParameter.put("sessionId", "" + uuid);
            urlParameter.put("deviceId", "" + uuid);
            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context, this, urlParameter, ApiConstants.LOGIN, ApiConstants.LOGIN_ID, showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    private boolean validateFields()
    {
        String email = email_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();
        String mobileNumber = mobile_number_et.getText().toString().trim();

        if(selectedLoginTypeTab.equalsIgnoreCase("Email"))
        {
            if (email.length() == 0 || email == null|| email.equalsIgnoreCase("")) {
                email_required_error_tv.setVisibility(View.VISIBLE);
                passowrd_required_error_tv.setVisibility(View.GONE);
                email_et.setBackgroundResource(R.drawable.border_red_line_view);
                email_required_error_tv.setText(getResources().getString(R.string.email_required));
                return false;
            }
            else if (!Utils.emailValidator(email))
            {
                email_required_error_tv.setVisibility(View.VISIBLE);
                email_et.setBackgroundResource(R.drawable.border_red_line_view);
                passowrd_required_error_tv.setVisibility(View.GONE);
                email_required_error_tv.setText(getResources().getString(R.string.email_valid_msg));
                return false;
            }
       /* else  if (password.length() == 0 || password == null|| password.equalsIgnoreCase("")) {
            email_required.setVisibility(View.GONE);
            passowrd_required.setVisibility(View.VISIBLE);
            password_et.setBackgroundResource(R.drawable.border_red);

            return false;
        }*/
            else if (password.length() == 0 || password == null|| password.equalsIgnoreCase("")) {
                passowrd_required_error_tv.setVisibility(View.VISIBLE);
                password_et.setBackgroundResource(R.drawable.border_red_line_view);
                return false;
            }

        }
        else
        {
            if (mobileNumber.length() == 0 || mobileNumber == null || mobileNumber.equalsIgnoreCase("")) {
                mobile_number_required_tv.setVisibility(View.VISIBLE);
                mobile_number_lin.setBackgroundResource(R.drawable.border_red_line_view);
                mobile_number_required_tv.setText(getResources().getString(R.string.phone_number_required));
                return false;
            } else if (mobileNumber.length() < 7 || mobileNumber.length() > 14) {
                mobile_number_required_tv.setVisibility(View.VISIBLE);
                mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
                mobile_number_required_tv.setText(getResources().getString(R.string.phone_number_valid_msg));
                return false;
            }
        }

        return true;
    }

    private boolean validateFieldsOTPValidation()
    {
        String otp = otp_et.getText().toString().trim();

        Log.e("---Diamond--- : ", "-------otp----------- : " + otp);

       if (otp_lin.getVisibility() == View.VISIBLE && otp.equalsIgnoreCase("")) {
            otp_required_error_tv.setVisibility(View.VISIBLE);
            otp_et.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }

        return true;
    }

    public void getCountryListAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();
            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_COUNTRY_LIST, ApiConstants.GET_COUNTRY_LIST_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }


    private void showCountryCodeList()
    {
        dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog.setContentView(R.layout.dialog_show_country_code);

        recycler_view = dialog.findViewById(R.id.recycler_view);
        search_et = dialog.findViewById(R.id.search_et);

        TextView textView2 = dialog.findViewById(R.id.textView2);

        textView2.setText(getResources().getString(R.string.country_list));

        recycler_view.setHasFixedSize(true);
        //recycler_view.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(layoutManager);

        countryCodeListAdapter = new CountryCodeListAdapter(countryArrayList, context, this);
        recycler_view.setAdapter(countryCodeListAdapter);
        countryCodeListAdapter.notifyDataSetChanged();


        search_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

                try {
                    String text = search_et.getText().toString().toLowerCase(Locale.getDefault());
                    int textlength = search_et.getText().length();

                    countryCodeListAdapter.filter(text, textlength);

                } catch (Exception e) {}
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
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
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);*/

    }


    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

        try {
            Log.e("Diamonds : ", "--------JSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {
                case ApiConstants.GET_COUNTRY_LIST_ID:

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

                case ApiConstants.LOGIN_ID:

                    Log.e("------Diamond---- : ", "Login Mobile Verification ; " + jsonObjectData.toString());
                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        checkOtpValidation = "";
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        String userRole = CommonUtility.checkString(jObjDetails.optString("userRole"));
                        String email = CommonUtility.checkString(jObjDetails.optString("email"));
                        String mobileNo = CommonUtility.checkString(jObjDetails.optString("mobileNo"));
                        String firstName = CommonUtility.checkString(jObjDetails.optString("firstName"));
                        String lastName = CommonUtility.checkString(jObjDetails.optString("lastName"));
                        String companyName = CommonUtility.checkString(jObjDetails.optString("companyName"));
                        String status = CommonUtility.checkString(jObjDetails.optString("status"));
                        String kycStatus = CommonUtility.checkString(jObjDetails.optString("kycStatus"));
                        String tokenType = CommonUtility.checkString(jObjDetails.optString("tokenType"));
                        String tokenExpiresIn = CommonUtility.checkString(jObjDetails.optString("tokenExpiresIn"));
                        String authToken = CommonUtility.checkString(jObjDetails.optString("authToken"));
                        String userCountryCode = CommonUtility.checkString(jObjDetails.optString("countryCode"));

                        CommonUtility.setGlobalString(activity, "user_login", "yes");
                        CommonUtility.setGlobalString(activity, "login_user_role", userRole);
                        CommonUtility.setGlobalString(activity, "mobile_auth_token", authToken);
                        CommonUtility.setGlobalString(activity, "login_email", email);
                        CommonUtility.setGlobalString(activity, "login_mobile_no", mobileNo);
                        CommonUtility.setGlobalString(activity, "login_first_name", firstName);
                        CommonUtility.setGlobalString(activity, "login_last_name", lastName);
                        CommonUtility.setGlobalString(activity, "login_company_name", companyName);
                        CommonUtility.setGlobalString(activity, "login_status", status);
                        CommonUtility.setGlobalString(activity, "login_kyc_status", kycStatus);
                        CommonUtility.setGlobalString(activity, "user_country_code", userCountryCode);

                        if(!tokenType.equalsIgnoreCase(""))
                        {
                            tokenType = "Bearer";
                            CommonUtility.setGlobalString(activity, "login_token_type", tokenType);
                        }else{
                            CommonUtility.setGlobalString(activity, "login_token_type", tokenType);
                        }

                        CommonUtility.setGlobalString(activity, "login_token_expires_in", tokenExpiresIn);

                        // Check Login Type Email Then Store Remember Me Email and Password
                        if(loginType.equalsIgnoreCase("email"))
                        {
                            if(rememberMeCheck)
                            {
                                CommonUtility.setGlobalString(activity, "remember_email", email_et.getText().toString().trim());
                                CommonUtility.setGlobalString(activity, "remember_password", password_et.getText().toString().trim());
                            }
                            else{
                                CommonUtility.setGlobalString(activity, "remember_email", "");
                                CommonUtility.setGlobalString(activity, "remember_password", "");
                            }
                        } else{}

                        //Log.e("---manageClickEventForRedirection--- : ", Constant.manageClickEventForRedirection.toString());
                        if(Constant.manageClickEventForRedirection.equalsIgnoreCase("placeOrder"))
                        {
                            Constant.manageClickEventForRedirection = "";
                            Intent intent = new Intent(context, DiamondDetailsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }
                        else if(Constant.manageClickEventForRedirection.equalsIgnoreCase("placeOrderMyCard"))
                        {
                            Constant.manageClickEventForRedirection = "";
                            Intent intent = new Intent(context, MyCardListScreenActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }else if(Constant.manageClickEventForRedirection.equalsIgnoreCase("placeOrderAddToCardFragment"))
                        {
                            Intent intent = new Intent(context, HomeScreenActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }

                        else{
                            Intent intent = new Intent(context, HomeScreenActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("2"))
                    {
                        stopOTPTimer();
                        setOTPTimer();
                        checkOtpValidation = "check";
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                        otp_lin.setVisibility(View.VISIBLE);

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
    public void itemClick(int position, String action)
    {

        if(action.equalsIgnoreCase("countryCode"))
        {
            CountryListModel model = countryArrayList.get(position);

            if (!model.getImage().equalsIgnoreCase(""))
            {
                Picasso.with(context)
                        .load(model.getImage())
                        .into(country_img);
            }
            else {}

            country_code.setText(model.getPhoneCode());
            country_code.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down_icon, 0);

            dialog.cancel();
        }
    }
}