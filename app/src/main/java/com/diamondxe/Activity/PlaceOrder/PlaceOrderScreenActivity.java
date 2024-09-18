package com.diamondxe.Activity.PlaceOrder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.diamondxe.Activity.AddBillingAddressActivity;
import com.diamondxe.Activity.AddShippingAddressActivity;
import com.diamondxe.Adapter.OrderPlaceItemListAdapter;
import com.diamondxe.Adapter.PlaceOrderBillingAddressListAdapter;
import com.diamondxe.Adapter.PlaceOrderShippingAddressListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.AddToCartListModel;
import com.diamondxe.Beans.AddressListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

public class PlaceOrderScreenActivity extends SuperActivity implements RecyclerInterface {

    private ImageView back_img, shipping_img, kyc_img, payment_img, drop_arrow_img, other_tax_info_img, diamond_tax_info_img;
    private CardView no_shipping_address_card, no_billing_address_card, shipping_card_view, kyc_card_view, payment_card_view;
    private TextView add_shipping_address_tv, add_billing_address_tv, continue_tv, kyc_verified_lbl,kyc_verified_lbl1, total_amount_tv,
            final_amount_tv1,shipping_and_handling_tv, platform_fees_tv, total_charges_tv, other_taxes_tv, diamond_taxes_tv, total_taxes_tv,
            sub_total_tv, bank_charges_tv, final_amount_tv, others_txt_gst_perc_tv, diamond_txt_gst_perc_tv;
    private RelativeLayout shipping_rel, kyc_rel, payment_rel, viewpager_layout, rel_other_tax, rel_diamond_tax;
    private LinearLayout address_main_lin,  view_order_summary_details_lin;
    private CardView order_summary_view_card;
    private ScrollView scrollView;
    private CheckBox save_shipping_cost_checkbox;
    private Activity activity;
    private Context context;
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    ViewPager viewPager;
    TabLayout tabLayout;
    private RecyclerView shipping_address_recycler_view, billing_address_recycler_view;
    ArrayList<AddressListModel> shippingAddressArrayList;
    ArrayList<AddressListModel> billingAddressArrayList;
    PlaceOrderShippingAddressListAdapter addressListAdapter;
    PlaceOrderBillingAddressListAdapter billingAddressListAdapter;
    public ArrayList<AddToCartListModel> orderItemArrayList;
    OrderPlaceItemListAdapter orderPlaceItemListAdapter;

    boolean isSelectShippingAddress = false;
    boolean isSelectBillingAddress = false;
    private boolean isArrowDown = false;
    int lastPosition = 0;
    String wheretoRemove="", shippingAddressID="",billingAddressID="",userRole="",document_status="",setAsPickupAddress="";
    String ADDRESS = "address";
    String KYC_VERIFICATION = "kycVerification";
    String selectedTab = ADDRESS;
    Handler handler1 = new Handler(Looper.getMainLooper());

    int newWith;
    int width;
    String selectedCurrencyValue ="",selectedCurrencyCode = "",selectedCurrencyDesc="",selectedCurrencyImage="";
    String isCoupanApplied = "", orderCouponCode = "", orderCouponValue = "", orderCouponDiscount = "", orderSubTotal = "", orderCgst = "", orderCgstPerc = "",
    orderSgst = "", orderSgstPerc = "", orderIgst = "", orderIgstPerc = "", orderDiscountPerc = "", orderTax = "", orderSubTotalWithTax = "", orderShippingCharge = "", orderPlatformFee = "",
    orderTotalCharge = "", orderTotalChargeTax = "", orderTotalChargeWithTax = "", orderTotalTaxes = "", orderTotalAmount = "", orderTaxPerOnCharges = "", orderFinalAmount = "", orderBankCharge = "", orderBankChargePerc = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_screen);

        context = activity = this;
        
        shippingAddressArrayList = new ArrayList<>();
        billingAddressArrayList = new ArrayList<>();
        orderItemArrayList = new ArrayList<>();

        userRole = CommonUtility.getGlobalString(activity, "login_user_role");

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        scrollView = findViewById(R.id.scrollView);

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

        continue_tv = findViewById(R.id.continue_tv);
        continue_tv.setOnClickListener(this);

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

        no_shipping_address_card = findViewById(R.id.no_shipping_address_card);
        no_billing_address_card = findViewById(R.id.no_billing_address_card);

        add_shipping_address_tv = findViewById(R.id.add_shipping_address_tv);
        add_shipping_address_tv.setOnClickListener(this);

        add_billing_address_tv = findViewById(R.id.add_billing_address_tv);
        add_billing_address_tv.setOnClickListener(this);

        address_main_lin = findViewById(R.id.address_main_lin);

        /*other_tax_info_img = findViewById(R.id.other_tax_info_img);
        other_tax_info_img.setOnClickListener(this);*/

        rel_other_tax = findViewById(R.id.rel_other_tax);
        rel_other_tax.setOnClickListener(this);

        rel_diamond_tax = findViewById(R.id.rel_diamond_tax);
        rel_diamond_tax.setOnClickListener(this);

        /*diamond_tax_info_img = findViewById(R.id.diamond_tax_info_img);
        diamond_tax_info_img.setOnClickListener(this);*/

        //For calculate Screen width for manage RecyclerView's card's width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        shipping_address_recycler_view = findViewById(R.id.shipping_address_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        shipping_address_recycler_view.setLayoutManager(layoutManager);
        shipping_address_recycler_view.setNestedScrollingEnabled(false);

        billing_address_recycler_view = findViewById(R.id.billing_address_recycler_view);
        LinearLayoutManager layoutManagerBilling = new LinearLayoutManager(activity);
        layoutManagerBilling.setOrientation(LinearLayoutManager.HORIZONTAL);
        billing_address_recycler_view.setLayoutManager(layoutManagerBilling);
        billing_address_recycler_view.setNestedScrollingEnabled(false);

        drop_arrow_img = findViewById(R.id.drop_arrow_img);
        view_order_summary_details_lin = findViewById(R.id.view_order_summary_details_lin);
        order_summary_view_card = findViewById(R.id.order_summary_view_card);
        order_summary_view_card.setOnClickListener(this);

        viewPager = findViewById (R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        tabLayout = findViewById(R.id.tab_layout);
        viewpager_layout = findViewById(R.id.viewpager_layout);

        save_shipping_cost_checkbox = findViewById(R.id.save_shipping_cost_checkbox);

        getPickupAddressCheckBoxValue();

        newWith = (int) (width/1.2);

    }

    void getPickupAddressCheckBoxValue()
    {
        save_shipping_cost_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (save_shipping_cost_checkbox.isChecked())
                {
                    Constant.collectFromHub = "1";
                    getCheckOutDetailsAPI(false);
                } else {
                    Constant.collectFromHub = "0";
                    getCheckOutDetailsAPI(false);
                }
            }
        });

    }
    // Get Currency Value Code and Image
    void getCurrencyData()
    {
        selectedCurrencyValue = CommonUtility.getGlobalString(context, "selected_currency_value");
        selectedCurrencyCode = CommonUtility.getGlobalString(context, "selected_currency_code");
        selectedCurrencyDesc = CommonUtility.getGlobalString(context, "selected_currency_desc");
        selectedCurrencyImage = CommonUtility.getGlobalString(context, "selected_currency_image");
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
        else if(id == R.id.add_shipping_address_tv)
        {
            Utils.hideKeyboard(activity);
            Constant.editShippingAddress = "";
            Constant.addressID = "";
            intent = new Intent(activity, AddShippingAddressActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else if(id == R.id.add_billing_address_tv)
        {
            Utils.hideKeyboard(activity);
            Constant.editBillingAddress = "";
            Constant.addressID = "";
            intent = new Intent(activity, AddBillingAddressActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
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
        else if(id == R.id.continue_tv)
        {
            Utils.hideKeyboard(activity);
            if(validateFields())
            {
                boolean isShippingAddressValid = validateShippingAddress();
                boolean isBillingAddressValid = false;

                // Check Address Selection Validation
                if (isShippingAddressValid) {
                    isBillingAddressValid = validateBillingAddress();
                }else{}

                if (isShippingAddressValid && isBillingAddressValid) {
                    shipping_rel.setBackgroundResource(R.drawable.background_image_white);
                    shipping_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));

                    kyc_rel.setBackgroundResource(R.drawable.background_image_purple);
                    kyc_img.setColorFilter(ContextCompat.getColor(context, R.color.white));

                    payment_rel.setBackgroundResource(R.drawable.background_image_white);
                    payment_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));

                    address_main_lin.setVisibility(View.GONE);

                    // This is USe For placeOrder KYC Verification Screen Intent.
                    gotoPlaceOrderKYCVerificationRedirection();
                }
            }else{}
        }
        else if(id == R.id.shipping_card_view)
        {
            Utils.hideKeyboard(activity);

           selectShippingIcon();
        }
        else if(id == R.id.kyc_card_view)
        {
            Utils.hideKeyboard(activity);

           selectKYCVerificationIcon();

        }
        else if(id == R.id.payment_card_view)
        {
            Utils.hideKeyboard(activity);

            selectPaymentIcon();
        }
    }

    void gotoPlaceOrderKYCVerificationRedirection()
    {
        if(userRole.equalsIgnoreCase("DEALER"))
        {
            Intent intent = new Intent(activity, PlaceOrderKYCVerificationActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        }
        else{
            Log.e("------document_status-------- :" , document_status.toString());
            if(document_status.equalsIgnoreCase("0"))
            {
                Constant.documentStatus = document_status; // For Check Validation Upload At least one document.
                Intent intent = new Intent(activity, PlaceOrderBuyerKYCVerificationDocUploadActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
            else{
                Intent intent = new Intent(activity, PlaceOrderBuyerKYCVerificationActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        }

    }

    void selectShippingIcon()
    {
        selectedTab = ADDRESS;

        shipping_rel.setBackgroundResource(R.drawable.background_image_purple);
        shipping_img.setColorFilter(ContextCompat.getColor(context, R.color.white));

        kyc_rel.setBackgroundResource(R.drawable.background_image_white);
        kyc_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));

        payment_rel.setBackgroundResource(R.drawable.background_image_white);
        payment_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));

        address_main_lin.setVisibility(View.VISIBLE);

        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    void selectKYCVerificationIcon()
    {
        if (!shippingAddressArrayList.isEmpty())
        {
            if(!billingAddressArrayList.isEmpty())
            {
                gotoPlaceOrderKYCVerificationRedirection();
            }
            else{
                Toast.makeText(activity, getResources().getString(R.string.please_add_billing_address), Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(activity, getResources().getString(R.string.please_add_shipping_address), Toast.LENGTH_SHORT).show();
        }
    }

    void selectPaymentIcon()
    {
        if (!shippingAddressArrayList.isEmpty())
        {
            if(!billingAddressArrayList.isEmpty())
            {
                shipping_rel.setBackgroundResource(R.drawable.background_image_white);
                shipping_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));

                kyc_rel.setBackgroundResource(R.drawable.background_image_white);
                kyc_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));

                payment_rel.setBackgroundResource(R.drawable.background_image_purple);
                payment_img.setColorFilter(ContextCompat.getColor(context, R.color.white));

                Intent intent = new Intent(activity, PaymentProcessedScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
            else{
                Toast.makeText(activity, getResources().getString(R.string.please_add_billing_address), Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(activity, getResources().getString(R.string.please_add_shipping_address), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getCurrencyData();

        getShippingAddressListAPI(false);
        getBillingAddressListAPI(true);
        userRole = CommonUtility.getGlobalString(activity, "login_user_role");

        if(userRole.equalsIgnoreCase("BUYER"))
        {
            getKYCDetailsAPI(true);
        }

        getCheckOutDetailsAPI(true);
    }

    public void getKYCDetailsAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_KYC_DETAILS, ApiConstants.GET_KYC_DETAILS_ID,showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void getCheckOutDetailsAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("couponCode", "");
            urlParameter.put("walletPoints", "");
            urlParameter.put("paymentMode", "");
            urlParameter.put("deliveryPincode", "" + Constant.deliveryPincode);
            urlParameter.put("collectFromHub", "" + Constant.collectFromHub);
            urlParameter.put("orderType", Constant.orderType);
            urlParameter.put("certificateNo", Constant.certificateNumber);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_CHECKOUT_DETAILS, ApiConstants.GET_CHECKOUT_DETAILS_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void getShippingAddressListAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_ADDRESS_SHIPPING, ApiConstants.GET_ADDRESS_SHIPPING_ID,showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void getBillingAddressListAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_ADDRESS_BILLING, ApiConstants.GET_ADDRESS_BILLING_ID,showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void getRemoveAddressAPI(boolean showLoader, String addressID)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("addressId", ""+ addressID);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.REMOVE_ADDRESS, ApiConstants.REMOVE_ADDRESS_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

        try {
            Log.e("Diamonds : ", "--------JSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {
                case ApiConstants.GET_ADDRESS_SHIPPING_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if(shippingAddressArrayList.size() > 0)
                        {
                            shippingAddressArrayList.clear();
                        } else{}

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            AddressListModel model = new AddressListModel();

                            model.setAddressId(CommonUtility.checkString(objectCodes.optString("AddressId")));
                            model.setAddressTypeId(CommonUtility.checkString(objectCodes.optString("AddressTypeId")));
                            model.setAddressType(CommonUtility.checkString(objectCodes.optString("AddressType")));
                            model.setAddress1(CommonUtility.checkString(objectCodes.optString("Address1")));
                            model.setAddress2(CommonUtility.checkString(objectCodes.optString("Address2")));
                            model.setCityNameS(CommonUtility.checkString(objectCodes.optString("CityNameS")));
                            model.setCityName(CommonUtility.checkString(objectCodes.optString("CityName")));
                            model.setStateNameS(CommonUtility.checkString(objectCodes.optString("StateNameS")));
                            model.setStateName(CommonUtility.checkString(objectCodes.optString("StateName")));
                            model.setCountryNameS(CommonUtility.checkString(objectCodes.optString("CountryNameS")));
                            model.setCountryName(CommonUtility.checkString(objectCodes.optString("CountryName")));
                            model.setPinCode(CommonUtility.checkString(objectCodes.optString("PinCode")));
                            model.setIsDefault(CommonUtility.checkString(objectCodes.optString("IsDefault")));
                            model.setEmailId(CommonUtility.checkString(objectCodes.optString("EmailId")));
                            model.setMobileNo(CommonUtility.checkString(objectCodes.optString("MobileNo")));
                            model.setgSTNo(CommonUtility.checkString(objectCodes.optString("GSTNo")));
                            model.setBusinessName(CommonUtility.checkString(objectCodes.optString("BusinessName")));
                            model.setMobileDialCode(CommonUtility.checkString(objectCodes.optString("MobileDialCode")));
                            model.setMobileNumber(CommonUtility.checkString(objectCodes.optString("MobileNumber")));

                            // Check IS Default Address Set Selected For Select Radio Button
                            if(objectCodes.optString("IsDefault").equalsIgnoreCase("1"))
                            {
                                model.setSelected(true);
                                isSelectShippingAddress = true;
                                Constant.deliveryPincode = CommonUtility.checkString(objectCodes.optString("PinCode"));
                                Constant.shippingAddressID = CommonUtility.checkString(objectCodes.optString("AddressId"));
                            }
                            else{
                                model.setSelected(false);
                            }
                            shippingAddressArrayList.add(model);
                        }

                        if(shippingAddressArrayList.size()>0)
                        {
                            no_shipping_address_card.setVisibility(View.GONE);
                            shipping_address_recycler_view.setVisibility(View.VISIBLE);
                        }
                        else{
                            no_shipping_address_card.setVisibility(View.VISIBLE);
                            shipping_address_recycler_view.setVisibility(View.GONE);
                        }

                        addressListAdapter = new PlaceOrderShippingAddressListAdapter(shippingAddressArrayList,context,this, newWith);
                        shipping_address_recycler_view.setAdapter(addressListAdapter);

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

                case ApiConstants.GET_ADDRESS_BILLING_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if(billingAddressArrayList.size() > 0)
                        {
                            billingAddressArrayList.clear();
                        } else{}

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            AddressListModel model = new AddressListModel();

                            model.setAddressId(CommonUtility.checkString(objectCodes.optString("AddressId")));
                            model.setAddressTypeId(CommonUtility.checkString(objectCodes.optString("AddressTypeId")));
                            model.setAddressType(CommonUtility.checkString(objectCodes.optString("AddressType")));
                            model.setAddress1(CommonUtility.checkString(objectCodes.optString("Address1")));
                            model.setAddress2(CommonUtility.checkString(objectCodes.optString("Address2")));
                            model.setCityNameS(CommonUtility.checkString(objectCodes.optString("CityNameS")));
                            model.setCityName(CommonUtility.checkString(objectCodes.optString("CityName")));
                            model.setStateNameS(CommonUtility.checkString(objectCodes.optString("StateNameS")));
                            model.setStateName(CommonUtility.checkString(objectCodes.optString("StateName")));
                            model.setCountryNameS(CommonUtility.checkString(objectCodes.optString("CountryNameS")));
                            model.setCountryName(CommonUtility.checkString(objectCodes.optString("CountryName")));
                            model.setPinCode(CommonUtility.checkString(objectCodes.optString("PinCode")));
                            model.setIsDefault(CommonUtility.checkString(objectCodes.optString("IsDefault")));
                            model.setEmailId(CommonUtility.checkString(objectCodes.optString("EmailId")));
                            model.setMobileNo(CommonUtility.checkString(objectCodes.optString("MobileNo")));
                            model.setgSTNo(CommonUtility.checkString(objectCodes.optString("GSTNo")));
                            model.setBusinessName(CommonUtility.checkString(objectCodes.optString("BusinessName")));
                            model.setMobileDialCode(CommonUtility.checkString(objectCodes.optString("MobileDialCode")));
                            model.setMobileNumber(CommonUtility.checkString(objectCodes.optString("MobileNumber")));

                            // Check IS Default Address Set Selected For Select Radio Button
                            if(objectCodes.optString("IsDefault").equalsIgnoreCase("1"))
                            {
                                model.setSelected(true);
                                isSelectBillingAddress = true;
                                Constant.billingAddressID = CommonUtility.checkString(objectCodes.optString("AddressId"));
                            }
                            else{
                                model.setSelected(false);
                            }

                            billingAddressArrayList.add(model);
                        }

                        if(billingAddressArrayList.size()>0)
                        {
                            no_billing_address_card.setVisibility(View.GONE);
                            billing_address_recycler_view.setVisibility(View.VISIBLE);
                        }
                        else{
                            no_billing_address_card.setVisibility(View.VISIBLE);
                            billing_address_recycler_view.setVisibility(View.GONE);
                        }

                        billingAddressListAdapter = new PlaceOrderBillingAddressListAdapter(billingAddressArrayList,context,this, newWith);
                        billing_address_recycler_view.setAdapter(billingAddressListAdapter);

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

                case ApiConstants.REMOVE_ADDRESS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();

                        if(wheretoRemove.equalsIgnoreCase("shippingAddress"))
                        {
                            shippingAddressArrayList.remove(lastPosition);
                            addressListAdapter.notifyDataSetChanged();
                        }
                        else if(wheretoRemove.equalsIgnoreCase("billingAddress"))
                        {
                            billingAddressArrayList.remove(lastPosition);
                            billingAddressListAdapter.notifyDataSetChanged();
                        }else{}
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

                case ApiConstants.GET_KYC_DETAILS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Log.e("Diamonds : ", "--------KYC_DETAILS-------- : " + jsonObjectData);
                        document_status = jsonObjectData.optString("document_status");

                        Constant.documentStatus = document_status;

                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        if(document_status.equalsIgnoreCase("0"))
                        {}
                        else if(document_status.equalsIgnoreCase("1"))
                        {}
                        else if(document_status.equalsIgnoreCase("2"))
                        {}
                        else if(document_status.equalsIgnoreCase("3"))
                        {}
                        else{}
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

                case ApiConstants.GET_CHECKOUT_DETAILS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Log.v("------Diamond----- : ", "--------CheckOut_Details------- : " + jsonObject);

                         isCoupanApplied = CommonUtility.checkString(jsonObjectData.optString("is_coupan_applied"));
                         orderCouponCode = CommonUtility.checkString(jsonObjectData.optString("coupon_code"));
                         orderCouponValue = CommonUtility.checkString(jsonObjectData.optString("coupon_value"));
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
                         orderBankCharge = CommonUtility.checkString(jsonObjectData.optString("bank_charge"));
                         orderBankChargePerc = CommonUtility.checkString(jsonObjectData.optString("bank_charge_perc"));

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
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                        finish();
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
        if(action.equalsIgnoreCase("selectBillingAddress"))
        {
            boolean shouldSelect = !billingAddressArrayList.get(position).isSelected();
            if (shouldSelect) {
                // Deselect all items
                for (int i = 0; i < billingAddressArrayList.size(); i++) {
                    billingAddressArrayList.get(i).setSelected(false);
                }
                // Select the clicked item
                billingAddressArrayList.get(position).setSelected(true);
                billingAddressID = billingAddressArrayList.get(position).getAddressId();
                isSelectBillingAddress = true;
            } else {
                // If the clicked item is already selected, don't change the selection state
                billingAddressArrayList.get(position).setSelected(true);
                billingAddressID = billingAddressArrayList.get(position).getAddressId();
            }

            Constant.billingAddressID = billingAddressArrayList.get(position).getAddressId();
            billingAddressListAdapter.notifyDataSetChanged();
        }
        else if(action.equalsIgnoreCase("selectShippingAddress"))
        {
            boolean shouldSelect = !shippingAddressArrayList.get(position).isSelected();
            if (shouldSelect) {
                // Deselect all items
                for (int i = 0; i < shippingAddressArrayList.size(); i++) {
                    shippingAddressArrayList.get(i).setSelected(false);
                }
                // Select the clicked item
                shippingAddressArrayList.get(position).setSelected(true);
                shippingAddressID = shippingAddressArrayList.get(position).getAddressId();
                isSelectShippingAddress = true;
            } else {
                // If the clicked item is already selected, don't change the selection state
                shippingAddressArrayList.get(position).setSelected(true);
                shippingAddressID = shippingAddressArrayList.get(position).getAddressId();
            }

            Constant.deliveryPincode = shippingAddressArrayList.get(position).getPinCode();
            Constant.shippingAddressID = shippingAddressArrayList.get(position).getAddressId();
            addressListAdapter.notifyDataSetChanged();
        }
        else  if(action.equalsIgnoreCase("editAddress"))
        {
            Constant.editShippingAddress = "yes";
            AddressListModel model = shippingAddressArrayList.get(position);

            Constant.addressID = model.getAddressId();
            Constant.address1 = model.getAddress1();
            Constant.address2 = model.getAddress2();
            Constant.country = model.getCountryNameS();
            Constant.state = model.getStateNameS();
            Constant.city = model.getCityNameS();
            Constant.countryID = model.getCountryName();
            Constant.stateID = model.getStateName();
            Constant.cityID = model.getCityName();
            Constant.pinCode = model.getPinCode();
            Constant.mobileCode = model.getMobileDialCode();
            Constant.mobileNumber = model.getMobileNumber();
            Constant.email = model.getEmailId();
            Constant.setIsDefault = model.getIsDefault();

            Intent intent = new Intent(activity, AddShippingAddressActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else if(action.equalsIgnoreCase("deleteAddress"))
        {
            AddressListModel model = shippingAddressArrayList.get(position);
            wheretoRemove = "shippingAddress";
            lastPosition = position;

            if(shippingAddressArrayList.get(position).getIsDefault().equalsIgnoreCase("1"))
            {
                Toast.makeText(activity, getResources().getString(R.string.default_address_can_not_delete), Toast.LENGTH_SHORT).show();
            }
            else{
                removeAddressConfirmationPopup(activity, context, getResources().getString(R.string.address_delete_msg), model.getAddressId(),lastPosition);
            }
        }
        else if(action.equalsIgnoreCase("editBillingAddress"))
        {
            AddressListModel model = billingAddressArrayList.get(position);

            Constant.editBillingAddress = "yes";
            Constant.addressID = model.getAddressId();
            Constant.address1 = model.getAddress1();
            Constant.address2 = model.getAddress2();
            Constant.country = model.getCountryNameS();
            Constant.state = model.getStateNameS();
            Constant.city = model.getCityNameS();
            Constant.countryID = model.getCountryName();
            Constant.stateID = model.getStateName();
            Constant.cityID = model.getCityName();
            Constant.pinCode = model.getPinCode();
            Constant.mobileCode = model.getMobileDialCode();
            Constant.mobileNumber = model.getMobileNumber();
            Constant.email = model.getEmailId();
            Constant.setIsDefault = model.getIsDefault();
            Constant.gstNumber = model.getgSTNo();
            Constant.businessName = model.getBusinessName();

            Intent intent = new Intent(activity, AddBillingAddressActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else if(action.equalsIgnoreCase("deleteBillingAddress"))
        {
            AddressListModel model = billingAddressArrayList.get(position);
            lastPosition = position;
            wheretoRemove = "billingAddress";

            if(billingAddressArrayList.get(position).getIsDefault().equalsIgnoreCase("1"))
            {
                Toast.makeText(activity, getResources().getString(R.string.default_address_can_not_delete), Toast.LENGTH_SHORT).show();
            }
            else{
                removeAddressConfirmationPopup(activity, context, getResources().getString(R.string.address_delete_msg), model.getAddressId(),lastPosition);
            }
        }
    }

    void removeAddressConfirmationPopup(final Activity activity,final Context context,String message, String addressID, int position)
    {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_remove_layout, null);
        dialogBuilder.setView(dialogView);
        final android.app.AlertDialog alertDialog = dialogBuilder.create();

        final TextView title = dialogView.findViewById(R.id.title);
        final TextView message1 =  dialogView.findViewById(R.id.message);
        final TextView yes_tv =  dialogView.findViewById(R.id.yes_tv);
        final TextView no_tv =  dialogView.findViewById(R.id.no_tv);

        title.setText(""+context.getResources().getString(R.string.app_name));

        message1.setText(""+message);

        yes_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getRemoveAddressAPI(false, addressID);
                alertDialog.dismiss();
            }
        });

        no_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog.setCancelable(true);
        alertDialog.show();

    }

    private boolean validateFields()
    {
        if (shippingAddressArrayList == null || shippingAddressArrayList.isEmpty()) {
            Toast.makeText(activity, getResources().getString(R.string.please_add_shipping_address), Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (billingAddressArrayList == null ||  billingAddressArrayList.isEmpty()) {
            Toast.makeText(activity, getResources().getString(R.string.please_add_billing_address), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateShippingAddress() {
        for (int i = 0; i < shippingAddressArrayList.size(); i++) {
            if (shippingAddressArrayList.get(i).isSelected()) {
                // At least one item is selected, return true
                return true;
            }
        }
        // No items were selected, show a toast message and return false
        Toast.makeText(activity, getResources().getString(R.string.please_select_shipping_address), Toast.LENGTH_SHORT).show();
        return false;
    }
    private boolean validateBillingAddress() {
        for (int i = 0; i < billingAddressArrayList.size(); i++) {
            if (billingAddressArrayList.get(i).isSelected()) {
                // At least one item is selected, return true
                return true;
            }
        }
        // No items were selected, show a toast message and return false
        Toast.makeText(activity, getResources().getString(R.string.please_select_billing_address), Toast.LENGTH_SHORT).show();
        return false;
    }


    private boolean validateShippingAddres1() {
        for (int i = 0; i < shippingAddressArrayList.size(); i++) {
            if (!shippingAddressArrayList.get(i).isSelected()) {
                Toast.makeText(activity, getResources().getString(R.string.please_select_shipping_address), Toast.LENGTH_SHORT).show();
                return false; // Invalid shipping address
            }
        }
        return true; // Valid shipping address
    }
    private boolean validateBillingAddress1() {
        for (int i = 0; i < billingAddressArrayList.size(); i++) {
            if (!billingAddressArrayList.get(i).isSelected()) {
                Toast.makeText(activity, getResources().getString(R.string.please_select_billing_address), Toast.LENGTH_SHORT).show();
                return false; // Invalid billing address
            }
        }
        return true; // Valid billing address
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
}