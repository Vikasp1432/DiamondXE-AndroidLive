package com.diamondxe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.CountryListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

public class ForgotPasswordActivity extends SuperActivity implements RecyclerInterface {

    private ImageView back_img;
    private EditText email_et, otp_et;
    private TextView continue_tv, email_required_error_tv, otp_timer_tv, resend_otp_tv, otp_required_error_tv;
    private LinearLayout otp_lin;
    private Activity activity;
    private Context context;

    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        context = activity = this;

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        email_et = findViewById(R.id.email_et);
        otp_et = findViewById(R.id.otp_et);

        otp_lin = findViewById(R.id.otp_lin);

        email_required_error_tv = findViewById(R.id.email_required_error_tv);
        otp_required_error_tv = findViewById(R.id.otp_required_error_tv);

        otp_timer_tv = findViewById(R.id.otp_timer_tv);
        otp_timer_tv.setOnClickListener(this);

        resend_otp_tv = findViewById(R.id.resend_otp_tv);
        resend_otp_tv.setOnClickListener(this);

        continue_tv = findViewById(R.id.continue_tv);
        continue_tv.setOnClickListener(this);

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

        otp_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

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
            public void afterTextChanged(Editable s) {

            }
        });
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
        else if(id == R.id.continue_tv)
        {
            Utils.hideKeyboard(activity);

            if(validateFields())
            {
                onBindAPI(false);

               /* otp_lin.setVisibility(View.VISIBLE);

                // Check If OTP Edit Text is Blank then setOTPTimer() Call.
                if(!otp_et.getText().toString().toString().equalsIgnoreCase(""))
                {
                    intent = new Intent(activity, ResetPasswordActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();
                }
                else
                {
                    setOTPTimer();
                }*/

            }
            else{}
        }
        else if(id == R.id.otp_timer_tv)
        {
            Utils.hideKeyboard(activity);

        }
        else if(id == R.id.resend_otp_tv)
        {
            Utils.hideKeyboard(activity);

            setOTPTimer();
        }
    }

    void setOTPTimer()
    {
        new CountDownTimer(30000, 1000) {
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

    private boolean validateFields()
    {
        String email = email_et.getText().toString().trim();
        String otp = otp_et.getText().toString().trim();

        Log.e("---Diamond--- : ", "-------otp----------- : " + otp);

        if (email.length() == 0 || email == null|| email.equalsIgnoreCase("")) {
            email_required_error_tv.setVisibility(View.VISIBLE);
            email_et.setBackgroundResource(R.drawable.border_red_line_view);
            email_required_error_tv.setText(getResources().getString(R.string.email_required));
            return false;
        }
        else if (!Utils.emailValidator(email))
        {
            email_required_error_tv.setVisibility(View.VISIBLE);
            email_et.setBackgroundResource(R.drawable.border_red_line_view);
            email_required_error_tv.setText(getResources().getString(R.string.email_valid_msg));
            return false;
        }
        /*else if (otp_lin.getVisibility() == View.VISIBLE && otp.equalsIgnoreCase("")) {
            otp_required_error_tv.setVisibility(View.VISIBLE);
            otp_et.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }*/

        return true;
    }

    public void onBindAPI(boolean showLoader) {
        if (Utils.isNetworkAvailable(context)) {
            //Log.e("----------Diamond------ : ", Constant.shapeDiamondValue.toString());
            urlParameter = new HashMap<String, String>();

            urlParameter.put("email", email_et.getText().toString().trim());
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
            Log.e("Diamonds : ", "--------JSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {
                case ApiConstants.FORGOT_PASSWORD_ID:
                    Log.e("------Diamond---- : ", "--Response--- ; " + jsonObjectData.toString());
                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Constant.emailID = email_et.getText().toString().toString();
                        Intent intent = new Intent(activity, ResetPasswordActivity.class);
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