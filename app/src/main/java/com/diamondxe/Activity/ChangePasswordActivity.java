package com.diamondxe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;

public class ChangePasswordActivity extends SuperActivity implements RecyclerInterface {

    private ImageView back_img;
    private EditText current_password_et, password_et, confirm_password_et;
    TextInputLayout current_password_layout, password_layout, confirm_password_layout;
    private TextView current_password_required_error_tv, password_required_error_tv, confirm_password_required_error_tv, cancel_tv, update_tv;
    private Activity activity;
    private Context context;

    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        context = activity = this;

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        current_password_et = findViewById(R.id.current_password_et);
        password_et = findViewById(R.id.password_et);
        confirm_password_et = findViewById(R.id.confirm_password_et);

        current_password_required_error_tv = findViewById(R.id.current_password_required_error_tv);
        password_required_error_tv = findViewById(R.id.password_required_error_tv);
        confirm_password_required_error_tv = findViewById(R.id.confirm_password_required_error_tv);

        current_password_layout = findViewById(R.id.current_password_layout);
        password_layout = findViewById(R.id.password_layout);
        confirm_password_layout = findViewById(R.id.confirm_password_layout);

        cancel_tv = findViewById(R.id.cancel_tv);
        cancel_tv.setOnClickListener(this);

        update_tv = findViewById(R.id.update_tv);
        update_tv.setOnClickListener(this);


        current_password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!current_password_et.getText().toString().equalsIgnoreCase("")){
                    current_password_required_error_tv.setVisibility(View.GONE);
                    current_password_layout.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(current_password_et.getText().toString().equalsIgnoreCase("")){
                    current_password_required_error_tv.setVisibility(View.GONE);
                    current_password_layout.setBackgroundResource(R.drawable.border_line_view);
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
                    password_layout.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(password_et.getText().toString().equalsIgnoreCase("")){
                    password_required_error_tv.setVisibility(View.GONE);
                    password_layout.setBackgroundResource(R.drawable.border_line_view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirm_password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!confirm_password_et.getText().toString().equalsIgnoreCase("")){
                    confirm_password_required_error_tv.setVisibility(View.GONE);
                    confirm_password_layout.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(confirm_password_et.getText().toString().equalsIgnoreCase("")){
                    confirm_password_required_error_tv.setVisibility(View.GONE);
                    confirm_password_layout.setBackgroundResource(R.drawable.border_line_view);
                }
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
        else if(id == R.id.cancel_tv)
        {
            Utils.hideKeyboard(activity);
            finish();
        }
        else if(id == R.id.update_tv)
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
        String currentPassword = current_password_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();
        String confirmPassword = confirm_password_et.getText().toString().trim();

        if (currentPassword.length() == 0 || currentPassword == null || currentPassword.equalsIgnoreCase("")) {
            current_password_required_error_tv.setVisibility(View.VISIBLE);
            current_password_layout.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }
        else if (currentPassword.length()<8) {
            current_password_required_error_tv.setVisibility(View.VISIBLE);
            current_password_layout.setBackgroundResource(R.drawable.border_red_line_view);
            current_password_required_error_tv.setText(getResources().getString(R.string.password_length_msg));
            return false;
        }
        else if (!CommonUtility.isValidPassword(currentPassword)) {
            current_password_required_error_tv.setVisibility(View.VISIBLE);
            current_password_layout.setBackgroundResource(R.drawable.border_red_line_view);
            current_password_required_error_tv.setText(getResources().getString(R.string.password_validation_msg));
            return false;
        }
        else if (password.length() == 0 || password == null|| password.equalsIgnoreCase("")) {
            password_required_error_tv.setVisibility(View.VISIBLE);
            password_layout.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }
        else if (password.length()<8) {
            password_required_error_tv.setVisibility(View.VISIBLE);
            password_layout.setBackgroundResource(R.drawable.border_red_line_view);
            password_required_error_tv.setText(getResources().getString(R.string.password_length_msg));
            return false;
        }
        else if (!CommonUtility.isValidPassword(password)) {
            password_required_error_tv.setVisibility(View.VISIBLE);
            password_layout.setBackgroundResource(R.drawable.border_red_line_view);
            password_required_error_tv.setText(getResources().getString(R.string.password_validation_msg));
            return false;
        }
        else if (confirmPassword.length() == 0 || confirmPassword == null|| confirmPassword.equalsIgnoreCase("")) {
            confirm_password_required_error_tv.setVisibility(View.VISIBLE);
            confirm_password_layout.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }
        else if (!confirmPassword.equals(""+password)) {
            confirm_password_required_error_tv.setVisibility(View.VISIBLE);
            confirm_password_layout.setBackgroundResource(R.drawable.border_red_line_view);
            confirm_password_required_error_tv.setText(getResources().getString(R.string.password_confirm_password_not_match));
            return false;
        }
        return true;
    }

    public void onBindAPI(boolean showLoader) {
        if (Utils.isNetworkAvailable(context)) {
            //Log.e("----------Diamond------ : ", Constant.shapeDiamondValue.toString());
            urlParameter = new HashMap<String, String>();

            urlParameter.put("CurrentPassword", current_password_et.getText().toString().trim());
            urlParameter.put("Password", password_et.getText().toString().trim());
            urlParameter.put("ConfirmPassword", confirm_password_et.getText().toString().trim());

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context, this, urlParameter, ApiConstants.CHANGE_PASSWORD, ApiConstants.CHANGE_PASSWORD_ID, showLoader, "POST");

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
                case ApiConstants.CHANGE_PASSWORD_ID:
                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
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