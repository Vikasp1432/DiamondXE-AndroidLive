package com.diamondxe.Activity;

import static com.diamondxe.ApiCalling.ApiConstants.CART;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Activity.PlaceOrder.PlaceOrderScreenActivity;
import com.diamondxe.Adapter.AddToCartListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.AddToCartListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyCardListScreenActivity extends SuperActivity implements RecyclerInterface {

    private ImageView back_img;
    private RecyclerView recycler_view;
    private TextView total_amount_tv, title_tv;
    private RelativeLayout place_order_card;
    private RelativeLayout total_amount_lin, rel_main_bg;
    private LinearLayout error_tv_lin;
    private Activity activity;
    private Context context;
    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;

    public ArrayList<AddToCartListModel> modelArrayList;
    int lastPosition = 0;
    AddToCartListAdapter addToCartListAdapter;

    String isCoupanApplied = "", couponCode = "", couponValue = "", couponDiscount = "", subTotal = "", cgst = "", cgstPerc = "", sgst = "", sgstPerc = "", igst = "", igstPerc = "", discountPerc = "", tax = "",
            subTotalWithTax = "", shippingCharge = "", platformFee = "", totalCharge = "", totalChargeTax = "", totalChargeWithTax = "",
            totalTaxes = "", totalAmount = "", taxPerOnCharges = "", finalAmount = "", bankCharge = "", bankChargePerc = "",user_login="";

    String selectedCurrencyValue ="",selectedCurrencyCode = "",selectedCurrencyDesc="",selectedCurrencyImage="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card_list_screen);

        context = activity = this;

        modelArrayList = new ArrayList<>();

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        total_amount_tv  = findViewById(R.id.total_amount_tv);

        title_tv  = findViewById(R.id.title_tv);

        place_order_card  = findViewById(R.id.place_order_card);
        place_order_card.setOnClickListener(this);

        total_amount_lin = findViewById(R.id.total_amount_lin);
        rel_main_bg = findViewById(R.id.rel_main_bg);

        error_tv_lin = findViewById(R.id.error_tv_lin);

        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(layoutManager);

        getCurrencyData();

        onBindData(false);

        manageDeviceBackButton();
    }
    // Get Currency Value Code and Image
    void getCurrencyData()
    {
        selectedCurrencyValue = CommonUtility.getGlobalString(context, "selected_currency_value");
        selectedCurrencyCode = CommonUtility.getGlobalString(context, "selected_currency_code");
        selectedCurrencyDesc = CommonUtility.getGlobalString(context, "selected_currency_desc");
        selectedCurrencyImage = CommonUtility.getGlobalString(context, "selected_currency_image");
    }

    void manageDeviceBackButton()
    {
        // Handle Device Back Button code.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed()
            {
                if(Constant.manageClickEventForRedirection.equalsIgnoreCase("placeOrderMyCard"))
                {
                    gotoHomeRedirection();
                }
                else{
                    finish();
                }
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    void gotoHomeRedirection()
    {
        if(Constant.manageClickEventForRedirection.equalsIgnoreCase("placeOrderMyCard"))
        {
            Constant.manageClickEventForRedirection = "";
            Log.e("manageClickEventForRedirection : ", Constant.manageClickEventForRedirection);
            Intent intent = new Intent(context, HomeScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else{}
    }


    @Override
    public void onClick(View view)
    {
        Intent intent;
        int id = view.getId();

        if(id == R.id.back_img)
        {
            Utils.hideKeyboard(activity);
            if(Constant.manageClickEventForRedirection.equalsIgnoreCase("placeOrderMyCard"))
            {
                gotoHomeRedirection();
            }
            else{
                finish();
            }
        }
        else if(id == R.id.place_order_card)
        {
            Utils.hideKeyboard(activity);
            user_login = CommonUtility.getGlobalString(activity, "user_login");
            if(user_login.equalsIgnoreCase("yes"))
            {
                Constant.showRadioButtonForSelectAddress = "yes";// Under Address List Radio Button Visible
                Constant.orderType = CART; // Order Type
                Constant.certificateNumber = ""; // Blank Certificate Number
                Constant.manageShippingBillingAddressSelection="";
                Constant.manageBillingByAddressAddUpdate = "";
                Constant.manageShippingByAddressAddUpdate = "";
                intent = new Intent(activity, PlaceOrderScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                //onPlaceOrder(false);
            }
            else{
                Constant.manageClickEventForRedirection = "placeOrderMyCard";
                intent = new Intent(activity, LoginScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        }
    }

    public void onBindData(boolean showLoader)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);

            //urlParameter.put("user_id", CommonUtility.getGlobalString(getActivity(),"user_id"));
            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(activity,this, urlParameter, ApiConstants.GET_CART_DETAILS, ApiConstants.GET_CART_DETAILS_ID,showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            recycler_view.setVisibility(View.GONE);
        }
    }

    public void onCartTotalCalculation(boolean showLoader)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            //urlParameter.put("user_id", CommonUtility.getGlobalString(getActivity(),"user_id"));
            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(activity,this, urlParameter, ApiConstants.GET_CART_DETAILS, ApiConstants.GET_CART_DETAILS_FOR_CALCULATION_ID,showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void onAddToWishlistAPI(boolean showLoader, String certificateNo)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("certificateNo", "" + certificateNo);
            urlParameter.put("source", "cart");
            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.ADD_TO_WISHLIST, ApiConstants.ADD_TO_WISHLIST_ID,showLoader, "POST");
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    public void onRemoveFromCartListAPI(boolean showLoader, String certificateNo)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("certificateNo", "" + certificateNo);
            urlParameter.put("source", "cart");
            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.REMOVE_FROM_CART, ApiConstants.REMOVE_FROM_CART_ID,showLoader, "POST");
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    public void onPlaceOrder(boolean showLoader)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("orderType", "Cart");
            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.PLACE_ORDER, ApiConstants.PLACE_ORDER_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }


    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

        try {
            Log.v("------Diamond----- : ", "--------JSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {

                case ApiConstants.GET_CART_DETAILS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {

                        rel_main_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color));
                        total_amount_lin.setVisibility(View.VISIBLE);

                        isCoupanApplied= jsonObjectData.optString("is_coupan_applied");
                        couponCode= jsonObjectData.optString("coupon_code");
                        couponValue= jsonObjectData.optString("coupon_value");
                        couponDiscount= jsonObjectData.optString("coupon_discount");
                        subTotal= jsonObjectData.optString("sub_total");
                        cgst= jsonObjectData.optString("cgst");
                        cgstPerc= jsonObjectData.optString("cgst_perc");
                        sgst= jsonObjectData.optString("sgst");
                        sgstPerc= jsonObjectData.optString("sgst_perc");
                        igst= jsonObjectData.optString("igst");
                        igstPerc= jsonObjectData.optString("igst_perc");
                        discountPerc= jsonObjectData.optString("discount_perc");
                        tax= jsonObjectData.optString("tax");
                        subTotalWithTax= jsonObjectData.optString("sub_total_with_tax");
                        shippingCharge= jsonObjectData.optString("shipping_charge");
                        platformFee= jsonObjectData.optString("platform_fee");
                        totalCharge= jsonObjectData.optString("total_charge");
                        totalChargeTax= jsonObjectData.optString("total_charge_tax");
                        totalChargeWithTax= jsonObjectData.optString("total_charge_with_tax");
                        totalTaxes= jsonObjectData.optString("total_taxes");
                        totalAmount= jsonObjectData.optString("total_amount");
                        taxPerOnCharges= jsonObjectData.optString("tax_per_on_charges");
                        finalAmount= jsonObjectData.optString("final_amount");
                        bankCharge= jsonObjectData.optString("bank_charge");
                        bankChargePerc= jsonObjectData.optString("bank_charge_perc");

                        if(!finalAmount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, subTotal);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            total_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        }
                        else{}

                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if(modelArrayList.size() > 0)
                        {
                            modelArrayList.clear();
                        }
                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            AddToCartListModel model = new AddToCartListModel();
                            model.setCoupondiscountperc(CommonUtility.checkDouble(objectCodes.optString("coupon_discount_perc")));
                            model.setSubtotalaftercoupondiscount(CommonUtility.checkDouble(objectCodes.optString("subtotal_after_coupon_discount")));
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

                            modelArrayList.add(model);

                        }

                        if(modelArrayList !=null && modelArrayList.size()>0)
                        {
                            title_tv.setText(getResources().getString(R.string.my_cart) + " (" + modelArrayList.size() + ")");
                        }
                        else{
                            title_tv.setText(getResources().getString(R.string.my_cart));
                        }

                        for (int i = 0; i <modelArrayList.size() ; i++)
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, modelArrayList.get(i).getSubtotal());
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            modelArrayList.get(i).setShowingSubTotal(subTotalFormat);
                            modelArrayList.get(i).setCurrencySymbol(getCurrencySymbol);

                        }


                        addToCartListAdapter = new AddToCartListAdapter(modelArrayList,context,this);
                        recycler_view.setAdapter(addToCartListAdapter);
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        //   Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                        if(modelArrayList!=null && modelArrayList.size()==0)
                        {
                            total_amount_lin.setVisibility(View.GONE);
                            rel_main_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        }
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.ADD_TO_WISHLIST_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        modelArrayList.remove(lastPosition);
                        addToCartListAdapter.notifyDataSetChanged();
                        onCartTotalCalculation(true);
                        setListItemCount();
                        //Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("4")) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.REMOVE_FROM_CART_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        //Toast.makeText(activity, ""+ message, Toast.LENGTH_SHORT).show();
                        modelArrayList.remove(lastPosition);
                        addToCartListAdapter.notifyDataSetChanged();
                        onCartTotalCalculation(true);
                        setListItemCount();
                        Log.e("Delete", "Delete");
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

                case ApiConstants.GET_CART_DETAILS_FOR_CALCULATION_ID:
                    // use Only For Total Re Calculation.
                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        rel_main_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color));
                        total_amount_lin.setVisibility(View.VISIBLE);

                        isCoupanApplied= jsonObjectData.optString("is_coupan_applied");
                        couponCode= jsonObjectData.optString("coupon_code");
                        couponValue= jsonObjectData.optString("coupon_value");
                        couponDiscount= jsonObjectData.optString("coupon_discount");
                        subTotal= jsonObjectData.optString("sub_total");
                        cgst= jsonObjectData.optString("cgst");
                        cgstPerc= jsonObjectData.optString("cgst_perc");
                        sgst= jsonObjectData.optString("sgst");
                        sgstPerc= jsonObjectData.optString("sgst_perc");
                        igst= jsonObjectData.optString("igst");
                        igstPerc= jsonObjectData.optString("igst_perc");
                        discountPerc= jsonObjectData.optString("discount_perc");
                        tax= jsonObjectData.optString("tax");
                        subTotalWithTax= jsonObjectData.optString("sub_total_with_tax");
                        shippingCharge= jsonObjectData.optString("shipping_charge");
                        platformFee= jsonObjectData.optString("platform_fee");
                        totalCharge= jsonObjectData.optString("total_charge");
                        totalChargeTax= jsonObjectData.optString("total_charge_tax");
                        totalChargeWithTax= jsonObjectData.optString("total_charge_with_tax");
                        totalTaxes= jsonObjectData.optString("total_taxes");
                        totalAmount= jsonObjectData.optString("total_amount");
                        taxPerOnCharges= jsonObjectData.optString("tax_per_on_charges");
                        finalAmount= jsonObjectData.optString("final_amount");
                        bankCharge= jsonObjectData.optString("bank_charge");
                        bankChargePerc= jsonObjectData.optString("bank_charge_perc");

                        if(!finalAmount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, subTotal);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            //total_amount_tv.setText(ApiConstants.rupeesIcon + "" + CommonUtility.currencyFormat(subTotal));
                            total_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        }
                        else{}
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        //   Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                        if(modelArrayList!=null && modelArrayList.size()==0)
                        {
                            title_tv.setText(getResources().getString(R.string.my_cart));

                            rel_main_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                            total_amount_lin.setVisibility(View.GONE);
                        }
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.PLACE_ORDER_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        // Open Phone-pe
                        orderSuccessfullyPopup(activity, context);
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
        finally
        {
            if (modelArrayList !=null && modelArrayList.size()<=0)
            {
                error_tv_lin.setVisibility(View.VISIBLE);
                recycler_view.setVisibility(View.GONE);
            } else {
                error_tv_lin.setVisibility(View.GONE);
                recycler_view.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void getErrorResponce(String error, int service_ID) {

    }

    // Check List Item Count and Set Size.
    void setListItemCount()
    {
        // Check Array List Size and Set Item Count of Top
        if(modelArrayList!=null && modelArrayList.size()>0)
        {
            int total = modelArrayList.size();
            title_tv.setText(getResources().getString(R.string.my_cart) + " (" + total + ")");
        }
        else{}
    }

    @Override
    public void itemClick(int position, String action)
    {
        if(action.equalsIgnoreCase("deleteView"))
        {
            lastPosition = position;
            onRemoveFromCartListAPI(false, modelArrayList.get(position).getCertificateNo());
        }
        else if(action.equalsIgnoreCase("wishList"))
        {
            lastPosition = position;
            onAddToWishlistAPI(false, modelArrayList.get(position).getCertificateNo());
        }
    }

    void orderSuccessfullyPopup(final Activity activity,final Context context)
    {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_order_success_layout, null);
        dialogBuilder.setView(dialogView);
        final android.app.AlertDialog alertDialog = dialogBuilder.create();

        final TextView title = dialogView.findViewById(R.id.title);
        final TextView message = dialogView.findViewById(R.id.message);
        final TextView message1 = dialogView.findViewById(R.id.message1);
        final ImageView cross_img = dialogView.findViewById(R.id.cross_img);
        final TextView mobile_no_tv = dialogView.findViewById(R.id.mobile_no_tv);
        final TextView email_id_tv = dialogView.findViewById(R.id.email_id_tv);

        message.setText(getResources().getString(R.string.thanks_you));
        message1.setText(getResources().getString(R.string.thanks_you_msg));

        title.setText(""+context.getResources().getString(R.string.app_name));

        cross_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constant.manageClickEventForRedirection = "";
                Intent intent = new Intent(context, HomeScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(0,0);
                alertDialog.dismiss();
            }
        });

        email_id_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtility.forSendEmail(context);
            }
        });

        mobile_no_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommonUtility.makeACallIntent(context);

                /*if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_CALL_PHONE);
                } else {
                    // Permission is granted, proceed with the call
                    makePhoneCall();
                }*/
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    private void makePhoneCall() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        //Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+919892003399"));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        }
    }
}