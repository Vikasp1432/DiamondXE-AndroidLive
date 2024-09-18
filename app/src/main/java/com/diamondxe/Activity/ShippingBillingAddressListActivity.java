package com.diamondxe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Adapter.Dealer.AddressListAdapter;
import com.diamondxe.Adapter.Dealer.BillingAddressListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.AddressListModel;
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

public class ShippingBillingAddressListActivity extends SuperActivity implements RecyclerInterface {

    private ImageView back_img;
    private CardView no_shipping_address_card, no_billing_address_card;
    private TextView add_shipping_address_tv, add_billing_address_tv;
    private Activity activity;
    private Context context;

    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;

    private RecyclerView shipping_address_recycler_view, billing_address_recycler_view;
    ArrayList<AddressListModel> shippingAddressArrayList;
    ArrayList<AddressListModel> billingAddressArrayList;
    AddressListAdapter addressListAdapter;
    BillingAddressListAdapter billingAddressListAdapter;

    int lastPosition = 0;
    String wheretoRemove="";

    int newWith;
    int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shpping_billing_address_list);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        context = activity = this;

        shippingAddressArrayList = new ArrayList<>();
        billingAddressArrayList = new ArrayList<>();

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        no_shipping_address_card = findViewById(R.id.no_shipping_address_card);
        no_billing_address_card = findViewById(R.id.no_billing_address_card);

        add_shipping_address_tv = findViewById(R.id.add_shipping_address_tv);
        add_shipping_address_tv.setOnClickListener(this);

        add_billing_address_tv = findViewById(R.id.add_billing_address_tv);
        add_billing_address_tv.setOnClickListener(this);

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

        newWith = (int) (width/1.2);

        /*AddressListModel model = new AddressListModel();
        model.setAddress1("Satya Sai Square");
        model.setAddress2("Vijay Nagar");
        model.setCityNameS("Indore");
        model.setStateNameS("Madhya Pradesh");
        model.setPinCode("452010");
        model.setCountryNameS("India");
        model.setEmailId("ravi_dealer@mailinator.com");
        model.setMobileDialCode("+91");
        model.setMobileNumber("7999169403");
        model.setIsDefault("1");

        shippingAddressArrayList.add(model);
        shippingAddressArrayList.add(model);
        shippingAddressArrayList.add(model);*/
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
            Constant.editShippingAddress = "";
            Constant.addressID = "";
            intent = new Intent(activity, AddShippingAddressActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else if(id == R.id.add_billing_address_tv)
        {
            Constant.editBillingAddress = "";
            Constant.addressID = "";
            intent = new Intent(activity, AddBillingAddressActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getShippingAddressListAPI(false);
        getBillingAddressListAPI(true);
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

                        addressListAdapter = new AddressListAdapter(shippingAddressArrayList,context,this, newWith);
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

                        billingAddressListAdapter = new BillingAddressListAdapter(billingAddressArrayList,context,this, newWith);
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

        if(action.equalsIgnoreCase("editAddress"))
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
}