package com.diamondxe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

public class ResetPasswordActivity extends SuperActivity implements RecyclerInterface {

    private ImageView back_img;
    private EditText otp_et;
    private EditText password_et, confirm_password_et;
    private TextView password_required_error_tv, confirm_password_required_error_tv,otp_required_error_tv,otp_timer_tv, resend_otp_tv, submit_tv;
    private Activity activity;
    private Context context;

    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;

    private CountDownTimer otpTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        context = activity = this;

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        password_et = findViewById(R.id.password_et);
        confirm_password_et = findViewById(R.id.confirm_password_et);

        password_required_error_tv = findViewById(R.id.password_required_error_tv);
        confirm_password_required_error_tv = findViewById(R.id.confirm_password_required_error_tv);


        otp_required_error_tv = findViewById(R.id.otp_required_error_tv);
        otp_et = findViewById(R.id.otp_et);
        otp_timer_tv = findViewById(R.id.otp_timer_tv);

        resend_otp_tv = findViewById(R.id.resend_otp_tv);
        resend_otp_tv.setOnClickListener(this);

        submit_tv = findViewById(R.id.submit_tv);
        submit_tv.setOnClickListener(this);

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

        password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!password_et.getText().toString().equalsIgnoreCase("")){
                    password_required_error_tv.setVisibility(View.GONE);
                    password_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(password_et.getText().toString().equalsIgnoreCase("")){
                    password_required_error_tv.setVisibility(View.GONE);
                    password_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirm_password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!confirm_password_et.getText().toString().equalsIgnoreCase("")){
                    confirm_password_required_error_tv.setVisibility(View.GONE);
                    confirm_password_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(confirm_password_et.getText().toString().equalsIgnoreCase("")){
                    confirm_password_required_error_tv.setVisibility(View.GONE);
                    confirm_password_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setOTPTimer();
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
        else if(id == R.id.resend_otp_tv)
        {
            Utils.hideKeyboard(activity);
            stopOTPTimer();
            setOTPTimer();
            onResendOTPAPI(false);
        }
        else if(id == R.id.submit_tv)
        {
            Utils.hideKeyboard(activity);

            if(validateFields())
            {
                onBindAPI(false);
            }
            else{}

        }
    }


    private boolean validateFields()
    {
        String otp = otp_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();
        String confirmPassword = confirm_password_et.getText().toString().trim();

        if (otp.length() == 0 || otp == null|| otp.equalsIgnoreCase("")) {
            otp_required_error_tv.setVisibility(View.VISIBLE);
            otp_et.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }
        else if (password.length() == 0 || password == null|| password.equalsIgnoreCase("")) {
            password_required_error_tv.setVisibility(View.VISIBLE);
            password_et.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }
        else if (password.length()<8) {
            password_required_error_tv.setVisibility(View.VISIBLE);
            password_et.setBackgroundResource(R.drawable.border_red_line_view);
            password_required_error_tv.setText(getResources().getString(R.string.password_length_msg));
            return false;
        }
        else if (!CommonUtility.isValidPassword(password)) {
            password_required_error_tv.setVisibility(View.VISIBLE);
            password_et.setBackgroundResource(R.drawable.border_red_line_view);
            password_required_error_tv.setText(getResources().getString(R.string.password_validation_msg));
            return false;
        }
        else if (confirmPassword.length() == 0 || confirmPassword == null|| confirmPassword.equalsIgnoreCase("")) {
            confirm_password_required_error_tv.setVisibility(View.VISIBLE);
            confirm_password_et.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }
        else if (!confirmPassword.equals(""+password)) {
            confirm_password_required_error_tv.setVisibility(View.VISIBLE);
            confirm_password_et.setBackgroundResource(R.drawable.border_red_line_view);
            confirm_password_required_error_tv.setText(getResources().getString(R.string.password_confirm_password_not_match));
            return false;
        }
        return true;
    }

    public void onBindAPI(boolean showLoader) {
        if (Utils.isNetworkAvailable(context)) {
            //Log.e("----------Diamond------ : ", Constant.shapeDiamondValue.toString());
            urlParameter = new HashMap<String, String>();

            urlParameter.put("email", Constant.emailID);
            urlParameter.put("otp", otp_et.getText().toString().trim());
            urlParameter.put("password", password_et.getText().toString().trim());
            urlParameter.put("confirmPassword", confirm_password_et.getText().toString().trim());

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context, this, urlParameter, ApiConstants.RESET_PASSWORD, ApiConstants.RESET_PASSWORD_ID, showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    public void onResendOTPAPI(boolean showLoader) {
        if (Utils.isNetworkAvailable(context)) {
            //Log.e("----------Diamond------ : ", Constant.shapeDiamondValue.toString());
            urlParameter = new HashMap<String, String>();

            urlParameter.put("email", Constant.emailID);
            urlParameter.put("userType", "Buyer");

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context, this, urlParameter, ApiConstants.FORGOT_PASSWORD, ApiConstants.FORGOT_PASSWORD_ID, showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }


    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

        try {
            //Log.e("Diamonds : ", "--------JSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {
                case ApiConstants.RESET_PASSWORD_ID:
                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(activity, LoginScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();

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

                case ApiConstants.FORGOT_PASSWORD_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
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
    public void itemClick(int position, String action) {

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
}