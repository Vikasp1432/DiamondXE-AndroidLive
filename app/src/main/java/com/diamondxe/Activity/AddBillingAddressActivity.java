package com.diamondxe.Activity;

import static com.diamondxe.ApiCalling.ApiConstants.AADHAARNo;
import static com.diamondxe.ApiCalling.ApiConstants.DRIVING_LICENSE;
import static com.diamondxe.ApiCalling.ApiConstants.GSTNo;
import static com.diamondxe.ApiCalling.ApiConstants.PANNo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Activity.LoginScreenActivity;
import com.diamondxe.Adapter.CityListAdapter;
import com.diamondxe.Adapter.CountryListAdapter;
import com.diamondxe.Adapter.StateListAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class AddBillingAddressActivity extends SuperActivity implements RecyclerInterface {

    private ImageView back_img,country_img;
    private LinearLayout country_lin,state_lin ,city_lin,mobile_number_lin,country_code_lin,gst_business_number_lin ;
    private TextView country_tv, country_error_tv,state_tv, state_error_tv,city_tv, city_error_tv, pincode_error_tv,country_code,
            mobile_number_error_tv,email_error_tv, cancel_tv,update_tv,address_line1_error_tv, title_tv;
    private EditText address_line1_et,address_line2_et, pincode_et,mobile_number_et, email_et,gts_number, business_number, search_et;
    CheckBox set_as_default_address_check, same_as_shipping_address_check,use_gst_invoice_check;
    private Activity activity;
    private Context context;
    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    private boolean isApiCalling = false; // Flag to track API call
    private BottomSheetDialog dialog;
    RecyclerView recycler_view;
    ArrayList<CountryListModel> countryArrayList;
    ArrayList<CountryListModel> stateArrayList;
    ArrayList<CountryListModel> cityArrayList;
    CountryListAdapter countryListAdapter;
    StateListAdapter stateListAdapter;
    CityListAdapter cityListAdapter;
    String countryId = "", stateId = "", cityId = "",countryName="", stateName="", cityName="",countryCodeForNumber="", countryCode="+91",
            email;
    String setAsDefaultAddress = "0",sameAsShipping="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_billing_address);

        context = activity = this;

        countryArrayList = new ArrayList<>();
        stateArrayList = new ArrayList<>();
        cityArrayList = new ArrayList<>();

        email = CommonUtility.getGlobalString(activity, "login_email");

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        title_tv = findViewById(R.id.title_tv);

        address_line1_et = findViewById(R.id.address_line1_et);
        address_line2_et = findViewById(R.id.address_line2_et);
        pincode_et = findViewById(R.id.pincode_et);
        mobile_number_et = findViewById(R.id.mobile_number_et);
        email_et = findViewById(R.id.email_et);
        gts_number = findViewById(R.id.gts_number);
        business_number = findViewById(R.id.business_number);

        country_tv = findViewById(R.id.country_tv);
        country_tv.setOnClickListener(this);

        country_error_tv = findViewById(R.id.country_error_tv);

        state_tv = findViewById(R.id.state_tv);
        state_tv.setOnClickListener(this);

        state_error_tv = findViewById(R.id.state_error_tv);

        city_tv = findViewById(R.id.city_tv);
        city_tv.setOnClickListener(this);

        city_error_tv = findViewById(R.id.city_error_tv);
        pincode_error_tv = findViewById(R.id.pincode_error_tv);
        country_code = findViewById(R.id.country_code);
        mobile_number_error_tv = findViewById(R.id.mobile_number_error_tv);
        email_error_tv = findViewById(R.id.email_error_tv);
        address_line1_error_tv = findViewById(R.id.address_line1_error_tv);

        country_img = findViewById(R.id.country_img);

        country_lin = findViewById(R.id.country_lin);
        country_lin.setOnClickListener(this);

        state_lin = findViewById(R.id.state_lin);
        state_lin.setOnClickListener(this);

        city_lin = findViewById(R.id.city_lin);
        city_lin.setOnClickListener(this);

        mobile_number_lin = findViewById(R.id.mobile_number_lin);

        gst_business_number_lin = findViewById(R.id.gst_business_number_lin);

        country_code_lin = findViewById(R.id.country_code_lin);
        country_code_lin.setOnClickListener(this);


        set_as_default_address_check = findViewById(R.id.set_as_default_address_check);
        same_as_shipping_address_check = findViewById(R.id.same_as_shipping_address_check);
        use_gst_invoice_check = findViewById(R.id.use_gst_invoice_check);

        cancel_tv = findViewById(R.id.cancel_tv);
        cancel_tv.setOnClickListener(this);

        update_tv = findViewById(R.id.update_tv);
        update_tv.setOnClickListener(this);

        email_et.setText(email);

        getCheckBoxValue();

        removeEditTextError();

        // If Address is EDit Then same_as_shipping_address_check Layout gone
        if(Constant.editBillingAddress.equalsIgnoreCase("yes"))
        {
            title_tv.setText(getResources().getString(R.string.edit_billing_address));
            same_as_shipping_address_check.setVisibility(View.GONE);
            setData();
        }else{
            same_as_shipping_address_check.setVisibility(View.VISIBLE);
            title_tv.setText(getResources().getString(R.string.add_billing_address));
        }

    }

    void setData()
    {
        address_line1_et.setText(Constant.address1);
        address_line2_et.setText(Constant.address2);
        country_tv.setText(Constant.country);
        state_tv.setText(Constant.state);
        city_tv.setText(Constant.city);;
        pincode_et.setText(Constant.pinCode);
        if(!Constant.mobileCode.equalsIgnoreCase(""))
        {
            country_code.setText(Constant.mobileCode);
        }
        else{
            country_code.setText("+91");
        }

        mobile_number_et.setText(Constant.mobileNumber);
        email_et.setText(Constant.email);
        gts_number.setText(Constant.gstNumber);
        business_number.setText(Constant.businessName);

        countryId = Constant.countryID;
        stateId = Constant.stateID;
        cityId = Constant.cityID;
        countryCode = Constant.mobileCode;
        setAsDefaultAddress = Constant.setIsDefault;

        if(setAsDefaultAddress.equalsIgnoreCase("1"))
        {
            set_as_default_address_check.setChecked(true);
        }
        else{
            set_as_default_address_check.setChecked(false);
        }
    }

    // Get CheckBox Checked or Unchecked Value.
    void getCheckBoxValue()
    {
        set_as_default_address_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (set_as_default_address_check.isChecked()) {
                    setAsDefaultAddress = "1";
                } else {
                    //Toast.makeText(getApplicationContext(), "Please accept terms and conditions", Toast.LENGTH_SHORT).show();
                    setAsDefaultAddress = "0";
                }
            }
        });

        same_as_shipping_address_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (same_as_shipping_address_check.isChecked()) {
                    sameAsShipping = "1";
                } else {
                    sameAsShipping = "0";
                }
            }
        });

        use_gst_invoice_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (use_gst_invoice_check.isChecked()) {
                    gst_business_number_lin.setVisibility(View.VISIBLE);
                } else {
                    gst_business_number_lin.setVisibility(View.GONE);
                }
            }
        });

    }

    // Remove Validation Error on Edit Text Field
    void removeEditTextError()
    {
        address_line1_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!address_line1_et.getText().toString().equalsIgnoreCase("")){
                    address_line1_error_tv.setVisibility(View.GONE);
                    address_line1_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(address_line1_et.getText().toString().equalsIgnoreCase("")){
                    address_line1_error_tv.setVisibility(View.GONE);
                    address_line1_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        mobile_number_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!mobile_number_et.getText().toString().equalsIgnoreCase("")){
                    mobile_number_error_tv.setVisibility(View.GONE);
                    mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(mobile_number_et.getText().toString().equalsIgnoreCase("")){
                    mobile_number_error_tv.setVisibility(View.GONE);
                    mobile_number_lin.setBackgroundResource(R.drawable.border_line_view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        pincode_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!pincode_et.getText().toString().equalsIgnoreCase("")){
                    pincode_error_tv.setVisibility(View.GONE);
                    pincode_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(pincode_et.getText().toString().equalsIgnoreCase("")){
                    pincode_error_tv.setVisibility(View.GONE);
                    pincode_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        email_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!email_et.getText().toString().equalsIgnoreCase("")){
                    email_error_tv.setVisibility(View.GONE);
                    email_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(email_et.getText().toString().equalsIgnoreCase("")){
                    email_error_tv.setVisibility(View.GONE);
                    email_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
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
        else if(id == R.id.country_code_lin)
        {
            Utils.hideKeyboard(activity);
            countryCodeForNumber = "yes";
            getCountryListAPI(false);
        }
        else if(id == R.id.country_lin || id == R.id.country_tv)
        {
            if(!isApiCalling) // Check if API is not already calling
            {
                isApiCalling = true; // Set flag to true
                Utils.hideKeyboard(activity);
                countryCodeForNumber = "";
                getCountryListAPI(false);
            } else{}
        }
        else if(id == R.id.state_lin || id == R.id.state_tv)
        {
            if(!country_tv.getText().toString().trim().equalsIgnoreCase(""))
            {
                if(!isApiCalling) // Check if API is not already calling
                {
                    isApiCalling = true; // Set flag to true
                    Utils.hideKeyboard(activity);
                    getStateListAPI(false);
                } else{}
            }
            else
            {
                country_error_tv.setVisibility(View.VISIBLE);
                country_lin.setBackgroundResource(R.drawable.border_red_line_view);
            }

        }
        else if(id == R.id.city_lin || id == R.id.city_tv)
        {
            Utils.hideKeyboard(activity);
            if(!state_tv.getText().toString().trim().equalsIgnoreCase(""))
            {
                if(!isApiCalling) // Check if API is not already calling
                {
                    isApiCalling = true; // Set flag to true
                    getCityListAPI(false);
                } else{}
            }
            else
            {
                state_error_tv.setVisibility(View.VISIBLE);
                state_lin.setBackgroundResource(R.drawable.border_red_line_view);
            }
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
                onAddAddressAPI(false);
            }
        }
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

    public void getStateListAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();
            urlParameter.put("countryId", ""+ countryId);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_STATE_LIST, ApiConstants.GET_STATE_LIST_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    public void getCityListAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("stateId", ""+ stateId);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_CITY_LIST, ApiConstants.GET_CITY_LIST_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }
    public void onAddAddressAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            // If Address is Edit Then Send addressID and sameAS Shipping Address.
            if(Constant.editBillingAddress.equalsIgnoreCase("yes"))
            {
                urlParameter.put("addressId", ""+ Constant.addressID);
            }
            else {}
            urlParameter.put("emailId", ""+ email_et.getText().toString().trim());
            urlParameter.put("mobileNo", countryCode + " " + mobile_number_et.getText().toString().trim());
            urlParameter.put("addressType", ""+ "BILLING ADDRESS");
            urlParameter.put("address1", ""+ address_line1_et.getText().toString().trim());
            urlParameter.put("address2", ""+ address_line2_et.getText().toString().trim());
            urlParameter.put("pinCode", ""+ pincode_et.getText().toString().trim());
            urlParameter.put("isDefault", ""+ setAsDefaultAddress);
            urlParameter.put("GSTNo", ""+ gts_number.getText().toString().trim());
            urlParameter.put("businessName", ""+ business_number.getText().toString().trim());
            urlParameter.put("country", ""+ countryId);
            urlParameter.put("state", ""+ stateId);
            urlParameter.put("city", ""+ cityId);
            urlParameter.put("sameAsShipping", ""+ sameAsShipping);

            vollyApiActivity = null;
            // Check If Edit Address Call update Address API Other wise call add address API
            if(Constant.editBillingAddress.equalsIgnoreCase("yes"))
            {
                vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.UPDATE_ADDRESS, ApiConstants.UPDATE_ADDRESS_ID,showLoader, "POST");
            }
            else {
                vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.ADD_ADDRESS, ApiConstants.ADD_ADDRESS_ID,showLoader, "POST");
            }


        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    private boolean validateFields()
    {
        String addressLine1 = address_line1_et.getText().toString().trim();
        String country = country_tv.getText().toString().trim();
        String state = state_tv.getText().toString().trim();
        String city = city_tv.getText().toString().trim();
        String pinCode = pincode_et.getText().toString().trim();
        String email = email_et.getText().toString().trim();
        String mobileNumber = mobile_number_et.getText().toString().trim();

        if (addressLine1.length() == 0 || addressLine1 == null|| addressLine1.equalsIgnoreCase("")) {
            address_line1_error_tv.setVisibility(View.VISIBLE);
            address_line1_et.setBackgroundResource(R.drawable.border_red_line_view);
            address_line1_et.requestFocus();
            return false;
        }
        else if (country.length() == 0 || country == null || country.equalsIgnoreCase("")) {
            country_error_tv.setVisibility(View.VISIBLE);
            country_lin.setBackgroundResource(R.drawable.border_red_line_view);
            country_lin.requestFocus();
            return false;
        } else if (state.length() == 0 || state == null || state.equalsIgnoreCase("")) {
            state_error_tv.setVisibility(View.VISIBLE);
            state_lin.setBackgroundResource(R.drawable.border_red_line_view);
            state_tv.requestFocus();
            return false;
        } else if (city.length() == 0 || city == null || city.equalsIgnoreCase("")) {
            city_error_tv.setVisibility(View.VISIBLE);
            city_lin.setBackgroundResource(R.drawable.border_red_line_view);
            city_tv.requestFocus();
            return false;
        } else if ((pinCode.length() == 0 || pinCode == null || pinCode.equalsIgnoreCase("")) &&
                countryId.equalsIgnoreCase("101")) {
            pincode_error_tv.setVisibility(View.VISIBLE);
            pincode_et.setBackgroundResource(R.drawable.border_red_line_view);
            pincode_et.requestFocus();
            return false;
        } else if (mobileNumber.length() == 0 || mobileNumber == null || mobileNumber.equalsIgnoreCase("")) {
            mobile_number_error_tv.setVisibility(View.VISIBLE);
            mobile_number_lin.setBackgroundResource(R.drawable.border_red_line_view);
            mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_required));
            mobile_number_lin.requestFocus();
            return false;
        } else if (mobileNumber.length() < 7 || mobileNumber.length() > 14) {
            mobile_number_error_tv.setVisibility(View.VISIBLE);
            mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
            mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_valid_msg));
            mobile_number_lin.requestFocus();
            return false;
        }
        else if (email.length() == 0 || email == null|| email.equalsIgnoreCase("")) {
            email_error_tv.setVisibility(View.VISIBLE);
            email_et.setBackgroundResource(R.drawable.border_red_line_view);
            email_error_tv.setText(getResources().getString(R.string.email_required));
            email_et.requestFocus();
            return false;
        }
        else if (!Utils.emailValidator(email))
        {
            email_error_tv.setVisibility(View.VISIBLE);
            email_et.setBackgroundResource(R.drawable.border_red_line_view);
            email_error_tv.setText(getResources().getString(R.string.email_valid_msg));
            email_et.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

        try {
            Log.e("Diamonds : ", "--------JSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {
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

                case ApiConstants.GET_STATE_LIST_ID:

                    isApiCalling = false;

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if(stateArrayList.size() > 0)
                        {
                            stateArrayList.clear();
                        } else{}

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            CountryListModel model = new CountryListModel();
                            model.setId(CommonUtility.checkString(objectCodes.optString("id")));
                            model.setTitle(CommonUtility.checkString(objectCodes.optString("name")));
                            stateArrayList.add(model);
                        }

                        showStateList();
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

                case ApiConstants.GET_CITY_LIST_ID:

                    isApiCalling = false;

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if(cityArrayList.size() > 0)
                        {
                            cityArrayList.clear();
                        } else{}

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            CountryListModel model = new CountryListModel();
                            model.setId(CommonUtility.checkString(objectCodes.optString("id")));
                            model.setTitle(CommonUtility.checkString(objectCodes.optString("name")));
                            cityArrayList.add(model);
                        }

                        showCityList();
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

                case ApiConstants.ADD_ADDRESS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Constant.manageShippingBillingAddressSelection = "";
                        if(setAsDefaultAddress.equalsIgnoreCase("1"))
                        {
                            Constant.manageBillingByAddressAddUpdate = "yes";
                        }else{}
                        finish();
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

                case ApiConstants.UPDATE_ADDRESS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Constant.manageShippingBillingAddressSelection = "";
                        if(setAsDefaultAddress.equalsIgnoreCase("1"))
                        {
                            Constant.manageBillingByAddressAddUpdate = "yes";
                        }else{}
                        Log.e("message", message);
                        finish();
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

    private void showCountryCodeList()
    {
        dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog.setContentView(R.layout.dialog_show_country_code);

        recycler_view = dialog.findViewById(R.id.recycler_view);
        search_et = dialog.findViewById(R.id.search_et);

        TextView textView2 = dialog.findViewById(R.id.textView2);

        textView2.setText(getResources().getString(R.string.country_list));

        recycler_view.setHasFixedSize(false);
        //recycler_view.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(layoutManager);

        countryListAdapter = new CountryListAdapter(countryArrayList, context, this, countryCodeForNumber);
        recycler_view.setAdapter(countryListAdapter);
        countryListAdapter.notifyDataSetChanged();


        search_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

                try {
                    String text = search_et.getText().toString().toLowerCase(Locale.getDefault());
                    int textlength = search_et.getText().length();

                    countryListAdapter.filter(text, textlength);

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

    private void showStateList()
    {
        dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog.setContentView(R.layout.dialog_show_country_code);

        recycler_view = dialog.findViewById(R.id.recycler_view);
        search_et = dialog.findViewById(R.id.search_et);

        TextView textView2 = dialog.findViewById(R.id.textView2);

        textView2.setText(getResources().getString(R.string.state_list));

        recycler_view.setHasFixedSize(false);
        //recycler_view.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(layoutManager);

        stateListAdapter = new StateListAdapter(stateArrayList, context, this);
        recycler_view.setAdapter(stateListAdapter);
        stateListAdapter.notifyDataSetChanged();


        search_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

                try {
                    String text = search_et.getText().toString().toLowerCase(Locale.getDefault());
                    int textlength = search_et.getText().length();

                    stateListAdapter.filter(text, textlength);

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

    private void showCityList()
    {
        dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog.setContentView(R.layout.dialog_show_country_code);

        recycler_view = dialog.findViewById(R.id.recycler_view);
        search_et = dialog.findViewById(R.id.search_et);

        TextView textView2 = dialog.findViewById(R.id.textView2);

        textView2.setText(getResources().getString(R.string.city_list));

        recycler_view.setHasFixedSize(false);
        //recycler_view.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(layoutManager);

        cityListAdapter = new CityListAdapter(cityArrayList, context, this);
        recycler_view.setAdapter(cityListAdapter);
        cityListAdapter.notifyDataSetChanged();


        search_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

                try {
                    String text = search_et.getText().toString().toLowerCase(Locale.getDefault());
                    int textlength = search_et.getText().length();

                    cityListAdapter.filter(text, textlength);

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
    public void itemClick(int position, String action)
    {
        if(action.equalsIgnoreCase("countryName"))
        {
            if(countryCodeForNumber.equalsIgnoreCase(""))
            {
                country_error_tv.setVisibility(View.GONE);
                country_lin.setBackgroundResource(R.drawable.border_purple_line_view);

                CountryListModel model = countryArrayList.get(position);
                countryId = model.getId();
                country_tv.setText(model.getTitle());

                if(countryId.equalsIgnoreCase("101"))
                {
                    pincode_et.setHint(getResources().getString(R.string.enter_pincode));
                }
                else{
                    pincode_et.setHint(getResources().getString(R.string.enter_pincode1));
                }

                if(!countryName.equalsIgnoreCase(model.getTitle()))
                {
                    state_tv.setHint(getResources().getString(R.string.select_state));
                    state_tv.setText("");
                    stateId = "";
                    city_tv.setHint(getResources().getString(R.string.select_city));
                    city_tv.setText("");
                    cityId = "";
                } else{}

                countryName = model.getTitle();
            }
            else
            {
                CountryListModel model = countryArrayList.get(position);
                country_code.setText(model.getPhoneCode());
                countryCode = model.getPhoneCode();
                country_code.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down_icon, 0);
                if (!model.getImage().equalsIgnoreCase(""))
                {
                    Picasso.with(context)
                            .load(model.getImage())
                            .into(country_img);
                }
                else {}
            }

            dialog.cancel();
        }
        else if(action.equalsIgnoreCase("stateName"))
        {
            state_error_tv.setVisibility(View.GONE);
            state_lin.setBackgroundResource(R.drawable.border_purple_line_view);

            CountryListModel model = stateArrayList.get(position);
            stateId = model.getId();
            state_tv.setText(model.getTitle());

            if(!stateName.equalsIgnoreCase(model.getTitle()))
            {
                city_tv.setHint(getResources().getString(R.string.select_city));
                city_tv.setText("");
                cityId = "";
            } else{}

            stateName = model.getTitle();
            dialog.cancel();

        }
        else if(action.equalsIgnoreCase("cityName"))
        {
            city_error_tv.setVisibility(View.GONE);
            city_lin.setBackgroundResource(R.drawable.border_purple_line_view);

            CountryListModel model = cityArrayList.get(position);
            cityId = model.getId();
            city_tv.setText(model.getTitle());
            cityName = model.getTitle();
            dialog.cancel();
        }
    }
}