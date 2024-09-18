package com.diamondxe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Adapter.CountryListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.CountryListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class PersonalProfileActivity extends SuperActivity implements RecyclerInterface {

    private ImageView back_img, country_img,company_country_img;
    private TextView country_code, company_country_code, company_type_tv,business_nature_tv,first_name_error_tv,last_name_error_tv,
            company_name_error_tv,company_type_error_tv, business_nature_error_tv,update_tv, cancel_tv, company_email_error_tv,
            company_mobile_number_error_tv;
    private EditText first_name_et, last_name_et,mobile_number_et, email_et,company_name_et, company_email_et,company_mobile_number_et,search_et;
    private LinearLayout company_type_lin, business_nature_lin, company_country_code_lin, company_mobile_number_lin,company_details_layout;
    private Activity activity;
    private Context context;

    String mobileNoCountryCode="", companyMobileNoCountryCode="",userRole="", countryBottomPopupOpen = "";

    ArrayList<CountryListModel> countryArrayList;

    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;

    private BottomSheetDialog dialog;
    RecyclerView recycler_view;
    CountryListAdapter countryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personla_profile);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        context = activity = this;

        countryArrayList = new ArrayList<>();

        userRole = CommonUtility.getGlobalString(activity, "login_user_role");

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        country_img = findViewById(R.id.country_img);
        company_country_img = findViewById(R.id.company_country_img);

        country_code = findViewById(R.id.country_code);
        company_country_code = findViewById(R.id.company_country_code);
        company_type_tv = findViewById(R.id.company_type_tv);
        business_nature_tv = findViewById(R.id.business_nature_tv);
        first_name_error_tv = findViewById(R.id.first_name_error_tv);
        last_name_error_tv = findViewById(R.id.last_name_error_tv);
        company_name_error_tv = findViewById(R.id.company_name_error_tv);
        company_type_error_tv = findViewById(R.id.company_type_error_tv);
        business_nature_error_tv = findViewById(R.id.business_nature_error_tv);

        company_email_error_tv = findViewById(R.id.company_email_error_tv);
        company_mobile_number_error_tv = findViewById(R.id.company_mobile_number_error_tv);
        company_mobile_number_lin = findViewById(R.id.company_mobile_number_lin);

        company_details_layout = findViewById(R.id.company_details_layout);

        first_name_et = findViewById(R.id.first_name_et);
        last_name_et = findViewById(R.id.last_name_et);
        mobile_number_et = findViewById(R.id.mobile_number_et);
        email_et = findViewById(R.id.email_et);
        company_name_et = findViewById(R.id.company_name_et);
        company_email_et = findViewById(R.id.company_email_et);
        company_mobile_number_et = findViewById(R.id.company_mobile_number_et);

        company_type_lin = findViewById(R.id.company_type_lin);
        company_type_lin.setOnClickListener(this);

        business_nature_lin = findViewById(R.id.business_nature_lin);
        business_nature_lin.setOnClickListener(this);

        company_country_code_lin = findViewById(R.id.company_country_code_lin);
        company_country_code_lin.setOnClickListener(this);

        update_tv = findViewById(R.id.update_tv);
        update_tv.setOnClickListener(this);

        cancel_tv = findViewById(R.id.cancel_tv);
        cancel_tv.setOnClickListener(this);

        onBindAPI(false);

        first_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!first_name_et.getText().toString().equalsIgnoreCase("")){
                    first_name_error_tv.setVisibility(View.GONE);
                    first_name_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(first_name_et.getText().toString().equalsIgnoreCase("")){
                    first_name_error_tv.setVisibility(View.GONE);
                    first_name_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        last_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!last_name_et.getText().toString().equalsIgnoreCase("")){
                    last_name_error_tv.setVisibility(View.GONE);
                    last_name_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(last_name_et.getText().toString().equalsIgnoreCase("")){
                    last_name_error_tv.setVisibility(View.GONE);
                    last_name_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

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
        company_email_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!company_email_et.getText().toString().equalsIgnoreCase("")){
                    company_email_error_tv.setVisibility(View.GONE);
                    company_email_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(company_email_et.getText().toString().equalsIgnoreCase("")){
                    company_email_error_tv.setVisibility(View.GONE);
                    company_email_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        company_mobile_number_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!company_mobile_number_et.getText().toString().equalsIgnoreCase("")){
                    company_mobile_number_error_tv.setVisibility(View.GONE);
                    company_mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(company_mobile_number_et.getText().toString().equalsIgnoreCase("")){
                    company_mobile_number_error_tv.setVisibility(View.GONE);
                    company_mobile_number_lin.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        if(userRole.equalsIgnoreCase("DEALER"))
        {
            company_details_layout.setVisibility(View.VISIBLE);
        }
        else{
            company_details_layout.setVisibility(View.GONE);
        }

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
        else if(id == R.id.company_type_lin)
        {
            Utils.hideKeyboard(activity);
            Utils.hideKeyboard(activity);
            initiateCompanyTypePopupWindow();
        }
        else if(id == R.id.business_nature_lin)
        {
            Utils.hideKeyboard(activity);
            initiateBusinessTypePopupWindow();
        }
        else if(id == R.id.company_country_code_lin)
        {
            Utils.hideKeyboard(activity);
            countryBottomPopupOpen = "show";
            getCountryListAPI(false);
        }
        else if(id == R.id.update_tv)
        {
            Utils.hideKeyboard(activity);
            if(userRole.equalsIgnoreCase("DEALER"))
            {
                if(validateFieldsDealer())
                {
                    onUpdateProfileAPI(false);

                }else{}
            }
            else{
                if(validateFieldsBuyer())
                {
                    onUpdateProfileAPI(false);

                }else{}
            }
        }
        else if(id == R.id.cancel_tv)
        {
            Utils.hideKeyboard(activity);
            finish();
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


    public void onBindAPI(boolean showLoader) {
        if (Utils.isNetworkAvailable(context)) {
            urlParameter = new HashMap<String, String>();

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context, this, urlParameter, ApiConstants.GET_PROFILE,
                    ApiConstants.GET_PROFILE_ID, showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void onUpdateProfileAPI(boolean showLoader) {
        if (Utils.isNetworkAvailable(context)) {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("firstName", first_name_et.getText().toString().trim());
            urlParameter.put("lastName", last_name_et.getText().toString().trim());
            urlParameter.put("mobileNo", mobileNoCountryCode + " " + mobile_number_et.getText().toString().trim());
            urlParameter.put("companyName", company_name_et.getText().toString().trim());
            urlParameter.put("companyContact", companyMobileNoCountryCode + " " + company_mobile_number_et.getText().toString().trim());
            urlParameter.put("companyEmailId", company_email_et.getText().toString().trim());
            urlParameter.put("typeOfCompany", company_type_tv.getText().toString().trim());
            urlParameter.put("natureOfBusiness", business_nature_tv.getText().toString().trim());

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context, this, urlParameter, ApiConstants.UPDATE_PROFILE, ApiConstants.UPDATE_PROFILE_ID, showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }


    private boolean validateFieldsBuyer()
    {
        String firstName = first_name_et.getText().toString().trim();
        String lastName = last_name_et.getText().toString().trim();
        String mobileNumber = mobile_number_et.getText().toString().trim();
        String email = email_et.getText().toString().trim();


        if (firstName.length() == 0 || firstName == null|| firstName.equalsIgnoreCase("")) {
            first_name_error_tv.setVisibility(View.VISIBLE);
            first_name_et.setBackgroundResource(R.drawable.border_red_line_view);
            first_name_et.requestFocus();
            return false;
        }
        else if (lastName.length() == 0 || lastName == null|| lastName.equalsIgnoreCase("")) {
            last_name_error_tv.setVisibility(View.VISIBLE);
            last_name_et.setBackgroundResource(R.drawable.border_red_line_view);
            last_name_et.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateFieldsDealer()
    {
        String firstName = first_name_et.getText().toString().trim();
        String lastName = last_name_et.getText().toString().trim();
        String mobileNumber = mobile_number_et.getText().toString().trim();
        String email = email_et.getText().toString().trim();
        String companyName = company_name_et.getText().toString().trim();
        String companyEmail = company_email_et.getText().toString().trim();
        String companyMobNo = company_mobile_number_et.getText().toString().trim();


        if (firstName.length() == 0 || firstName == null|| firstName.equalsIgnoreCase("")) {
            first_name_error_tv.setVisibility(View.VISIBLE);
            first_name_et.setBackgroundResource(R.drawable.border_red_line_view);
            first_name_et.requestFocus();
            return false;
        }
        else if (lastName.length() == 0 || lastName == null|| lastName.equalsIgnoreCase("")) {
            last_name_error_tv.setVisibility(View.VISIBLE);
            last_name_et.setBackgroundResource(R.drawable.border_red_line_view);
            last_name_et.requestFocus();
            return false;
        }
        else if (companyName.length() == 0 || companyName == null|| companyName.equalsIgnoreCase("")) {
            company_name_error_tv.setVisibility(View.VISIBLE);
            company_name_et.setBackgroundResource(R.drawable.border_red_line_view);
            company_name_et.requestFocus();
            return false;
        }
        else if (companyEmail.length() == 0 || companyEmail == null|| companyEmail.equalsIgnoreCase("")) {
            company_email_error_tv.setVisibility(View.VISIBLE);
            company_email_et.setBackgroundResource(R.drawable.border_red_line_view);
            company_email_error_tv.setText(getResources().getString(R.string.email_required));
            company_email_et.requestFocus();
            return false;
        }
        else if (!Utils.emailValidator(companyEmail))
        {
            company_email_error_tv.setVisibility(View.VISIBLE);
            company_email_et.setBackgroundResource(R.drawable.border_red_line_view);
            company_email_error_tv.setText(getResources().getString(R.string.email_valid_msg));
            company_email_et.requestFocus();
            return false;
        }
        else if (companyMobNo.length() == 0 || companyMobNo == null || companyMobNo.equalsIgnoreCase("")) {
            company_mobile_number_error_tv.setVisibility(View.VISIBLE);
            company_mobile_number_lin.setBackgroundResource(R.drawable.border_red_line_view);
            company_mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_required));
            company_mobile_number_lin.requestFocus();
            return false;
        } else if (companyMobNo.length() < 7 || companyMobNo.length() > 14) {
            company_mobile_number_error_tv.setVisibility(View.VISIBLE);
            company_mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
            company_mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_valid_msg));
            company_mobile_number_lin.requestFocus();
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
                case ApiConstants.GET_PROFILE_ID:
                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        String UserId = CommonUtility.checkString(jObjDetails.optString("UserId"));
                        String userRole = CommonUtility.checkString(jObjDetails.optString("userRole"));
                        String firstName = CommonUtility.checkString(jObjDetails.optString("FirstName"));
                        String lastName = CommonUtility.checkString(jObjDetails.optString("LastName"));
                        String companyName = CommonUtility.checkString(jObjDetails.optString("CompanyName"));
                        String companyNumber = CommonUtility.checkString(jObjDetails.optString("CompanyNumber"));
                        String companyEmailId = CommonUtility.checkString(jObjDetails.optString("CompanyEmailId"));
                        String typeOfCompany = CommonUtility.checkString(jObjDetails.optString("TypeOfCompany"));
                        String natureOfBusiness = CommonUtility.checkString(jObjDetails.optString("NatureOfBusiness"));
                        String loginEmailId = CommonUtility.checkString(jObjDetails.optString("LoginEmailId"));
                        companyMobileNoCountryCode = CommonUtility.checkString(jObjDetails.optString("CompanyDialCode"));
                        mobileNoCountryCode = CommonUtility.checkString(jObjDetails.optString("MobileDialCode"));
                        String mobileNumber = CommonUtility.checkString(jObjDetails.optString("MobileNumber"));
                        String Status = CommonUtility.checkString(jObjDetails.optString("Status"));

                        first_name_et.setText(firstName);
                        last_name_et.setText(lastName);
                        mobile_number_et.setText(mobileNumber);
                        email_et.setText("Email : " + loginEmailId);
                        company_name_et.setText(companyName);
                        company_email_et.setText(companyEmailId);
                        company_mobile_number_et.setText(companyNumber);
                        company_type_tv.setText(typeOfCompany);
                        business_nature_tv.setText(natureOfBusiness);
                        country_code.setText(mobileNoCountryCode);
                        company_country_code.setText(companyMobileNoCountryCode);

                        countryBottomPopupOpen = "hide";
                        getCountryListAPI(true);

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
                        // Check Condition to Compare CountryCode anc Company Country Code to Get flag Using for Loop
                        if(countryBottomPopupOpen.equalsIgnoreCase("show"))
                        {
                            showCountryCodeList();
                        }
                        else{
                            for (int i = 0; i < countryArrayList.size(); i++)
                            {

                                String compareCountryCode = countryArrayList.get(i).getPhoneCode();
                                if(mobileNoCountryCode.equalsIgnoreCase(compareCountryCode))
                                {
                                    Picasso.with(context)
                                            .load(countryArrayList.get(i).getImage())
                                            .into(country_img);
                                    break;
                                }
                            }

                            for (int i = 0; i < countryArrayList.size(); i++)
                            {

                                String compareCompanyCountryCode = countryArrayList.get(i).getPhoneCode();
                                if(companyMobileNoCountryCode.equalsIgnoreCase(compareCompanyCountryCode))
                                {
                                    Picasso.with(context)
                                            .load(countryArrayList.get(i).getImage())
                                            .into(company_country_img);
                                    break;
                                }
                            }
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
                    else {
                        Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.UPDATE_PROFILE_ID:
                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();

                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");
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

        recycler_view.setHasFixedSize(true);
        //recycler_view.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(layoutManager);

        countryListAdapter = new CountryListAdapter(countryArrayList, context, this, "yes");
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

    private PopupWindow mDropdown = null;
    LayoutInflater mInflater;
    // Button pop;

    private PopupWindow initiateCompanyTypePopupWindow() {
        try {
            if (mDropdown != null && mDropdown.isShowing()) {
                mDropdown.dismiss();
            }
            mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.custom_menu_company_type, null);

            final TextView proprietorship_tv = layout.findViewById(R.id.proprietorship_tv);
            final TextView llp_tv = layout.findViewById(R.id.llp_tv);
            final TextView partnership_tv = layout.findViewById(R.id.partnership_tv);
            final TextView private_limited_tv = layout.findViewById(R.id.private_limited_tv);
            final TextView public_limited_tv = layout.findViewById(R.id.public_limited_tv);
            final TextView others_tv = layout.findViewById(R.id.others_tv);

            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            //mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);

            mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);


            /*Drawable background = getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            mDropdown.setBackgroundDrawable(background);*/

            if (company_type_tv != null) {
                mDropdown.showAsDropDown(company_type_tv, 5, -120);
            } else {
                Log.e("PopupWindow", "pop is null");
            }

            proprietorship_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    company_type_tv.setText(proprietorship_tv.getText().toString().trim());
                    removeCompanySelectError();
                    mDropdown.dismiss();
                }
            });

            llp_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    company_type_tv.setText(llp_tv.getText().toString().trim());
                    removeCompanySelectError();
                    mDropdown.dismiss();
                }
            });

            partnership_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    company_type_tv.setText(partnership_tv.getText().toString().trim());
                    removeCompanySelectError();
                    mDropdown.dismiss();
                }
            });

            private_limited_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    company_type_tv.setText(private_limited_tv.getText().toString().trim());
                    removeCompanySelectError();
                    mDropdown.dismiss();
                }
            });

            public_limited_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    company_type_tv.setText(public_limited_tv.getText().toString().trim());
                    removeCompanySelectError();
                    mDropdown.dismiss();
                }
            });

            others_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    company_type_tv.setText(others_tv.getText().toString().trim());
                    removeCompanySelectError();
                    mDropdown.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;
    }


    void removeCompanySelectError()
    {
        company_type_lin.setBackgroundResource(R.drawable.border_purple_line_view);
        company_type_error_tv.setVisibility(View.GONE);
    }

    private PopupWindow initiateBusinessTypePopupWindow() {
        try {
            if (mDropdown != null && mDropdown.isShowing()) {
                mDropdown.dismiss();
            }
            mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.custom_menu_business_type, null);

            final TextView rerailer_tv = layout.findViewById(R.id.rerailer_tv);
            final TextView wholesaler_tv = layout.findViewById(R.id.wholesaler_tv);
            final TextView trader_tv = layout.findViewById(R.id.trader_tv);
            final TextView jeweller_tv = layout.findViewById(R.id.jeweller_tv);
            final TextView others_tv = layout.findViewById(R.id.others_tv);

            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);

            /*Drawable background = getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            mDropdown.setBackgroundDrawable(background);*/

            // Ensure pop is not null before using it
            if (business_nature_lin != null) {
                mDropdown.showAsDropDown(business_nature_lin, 5, -120);
            } else {
                Log.e("PopupWindow", "pop is null");
            }



            rerailer_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    business_nature_tv.setText(rerailer_tv.getText().toString().trim());
                    removeBusinessSelectError();
                    mDropdown.dismiss();
                }
            });

            wholesaler_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    business_nature_tv.setText(wholesaler_tv.getText().toString().trim());
                    removeBusinessSelectError();
                    mDropdown.dismiss();
                }
            });

            trader_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    business_nature_tv.setText(trader_tv.getText().toString().trim());
                    removeBusinessSelectError();
                    mDropdown.dismiss();
                }
            });

            jeweller_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    business_nature_tv.setText(jeweller_tv.getText().toString().trim());
                    removeBusinessSelectError();
                    mDropdown.dismiss();
                }
            });

            others_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    business_nature_tv.setText(others_tv.getText().toString().trim());
                    removeBusinessSelectError();
                    mDropdown.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;
    }

    void removeBusinessSelectError()
    {
        business_nature_lin.setBackgroundResource(R.drawable.border_purple_line_view);
        business_nature_error_tv.setVisibility(View.GONE);
    }

    @Override
    public void itemClick(int position, String action)
    {
        if(action.equalsIgnoreCase("countryName"))
        {
            CountryListModel model = countryArrayList.get(position);
            company_country_code.setText(model.getPhoneCode());
            companyMobileNoCountryCode = model.getPhoneCode();
            company_country_code.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down_icon, 0);
            if (!model.getImage().equalsIgnoreCase(""))
            {
                Picasso.with(context)
                        .load(model.getImage())
                        .into(company_country_img);
            }
            else {}

            dialog.cancel();
        }
    }
}