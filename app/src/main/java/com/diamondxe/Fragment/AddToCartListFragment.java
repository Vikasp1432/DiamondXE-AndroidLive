package com.diamondxe.Fragment;

import static com.diamondxe.ApiCalling.ApiConstants.CART;
import static com.diamondxe.ApiCalling.ApiConstants.USER_BUYER;
import static com.diamondxe.ApiCalling.ApiConstants.USER_DEALER;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Activity.AccountSectionActivity;
import com.diamondxe.Activity.DiamondDetailsActivity;
import com.diamondxe.Activity.HomeScreenActivity;
import com.diamondxe.Activity.LoginScreenActivity;
import com.diamondxe.Activity.PlaceOrder.PlaceOrderScreenActivity;
import com.diamondxe.Activity.SearchDiamondsActivity;
import com.diamondxe.Adapter.AddToCartListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.AddToCartListModel;
import com.diamondxe.Interface.FragmentActionInterface;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperFragment;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AddToCartListFragment extends SuperFragment implements RecyclerInterface,View.OnClickListener, FragmentActionInterface {

    private ShimmerFrameLayout shimmer_view_container;
    private RelativeLayout  total_amount_lin, rel_main_bg;

    private RelativeLayout home_rel, category_rel, wishlist_rel, cart_rel, account_rel;
    private ImageView home_img, categories_img, wish_img, cart_img, account_img;
    private TextView home_tv, categories_tv, wish_tv, cart_tv, account_tv, cart_count_tv, wish_list_count_tv;
    private TextView place_order_tv;
    String user_login = "";

    private RelativeLayout search_circle_rel;

    private RecyclerView recycler_view;
    private TextView total_amount_tv;
    private LinearLayout error_tv_lin;
    private ImageView bottom_search_icon;
    private HomeScreenActivity drawerActivity;
    private Activity activity;
    private Context context;
    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    int lastPosition = 0;
    public ArrayList<AddToCartListModel> modelArrayList;
    AddToCartListAdapter addToCartListAdapter;

    String isCoupanApplied = "", couponCode = "", couponValue = "", couponDiscount = "", subTotal = "", cgst = "", cgstPerc = "", sgst = "", sgstPerc = "", igst = "", igstPerc = "", discountPerc = "", tax = "",
            subTotalWithTax = "", shippingCharge = "", platformFee = "", totalCharge = "", totalChargeTax = "", totalChargeWithTax = "", totalTaxes = "", totalAmount = "", taxPerOnCharges = "", finalAmount = "", bankCharge = "", bankChargePerc = "";

    double coupondiscountperc=0,subtotalaftercoupondiscount=0;
    String selectedCurrencyValue ="",selectedCurrencyCode = "",selectedCurrencyDesc="",selectedCurrencyImage="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_to_cart_list, container, false);

//        context = activity = drawerActivity = (HomeScreenActivity) getActivity();
          context = activity = getActivity();

  //      drawerActivity.setListener(this);

        init(view);

        return view;
    }


    private void init(View parentView)
    {
        modelArrayList = new ArrayList<>();

        shimmer_view_container = parentView.findViewById(R.id.shimmer_view_container);
        total_amount_lin = parentView.findViewById(R.id.total_amount_lin);
        rel_main_bg = parentView.findViewById(R.id.rel_main_bg);

        place_order_tv = parentView.findViewById(R.id.place_order_tv);
        place_order_tv.setOnClickListener(this);

        home_rel = parentView.findViewById(R.id.home_rel);
        home_rel.setOnClickListener(this);

        category_rel = parentView.findViewById(R.id.category_rel);
        category_rel.setOnClickListener(this);

        wishlist_rel = parentView.findViewById(R.id.wishlist_rel);
        wishlist_rel.setOnClickListener(this);

        cart_rel = parentView.findViewById(R.id.cart_rel);
        cart_rel.setOnClickListener(this);

        account_rel = parentView.findViewById(R.id.account_rel);
        account_rel.setOnClickListener(this);

        home_img = parentView.findViewById(R.id.home_img);
        categories_img = parentView.findViewById(R.id.categories_img);
        wish_img = parentView.findViewById(R.id.wish_img);
        cart_img = parentView.findViewById(R.id.cart_img);
        account_img = parentView.findViewById(R.id.account_img);

        home_tv = parentView.findViewById(R.id.home_tv);
        categories_tv = parentView.findViewById(R.id.categories_tv);
        wish_tv = parentView.findViewById(R.id.wish_tv);
        cart_tv = parentView.findViewById(R.id.cart_tv);
        account_tv = parentView.findViewById(R.id.account_tv);
        cart_count_tv = parentView.findViewById(R.id.cart_count_tv);
        wish_list_count_tv = parentView.findViewById(R.id.wish_list_count_tv);

        home_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        categories_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        wish_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        cart_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));
        account_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));

        home_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));
        categories_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));
        wish_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));
        cart_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
        account_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));

        user_login = CommonUtility.getGlobalString(context, "user_login");
        // If User Login then  User Role Name
        if(!user_login.equalsIgnoreCase(""))
        {
            String role = CommonUtility.getGlobalString(context, "login_user_role");
            if(role.equalsIgnoreCase("BUYER"))
            {
                account_tv.setText(USER_BUYER);
            }
            else if(role.equalsIgnoreCase("DEALER"))
            {
                account_tv.setText(USER_DEALER);
            }else{}
        }
        else{
            account_tv.setText(getResources().getString(R.string.login));
        }

        search_circle_rel = parentView.findViewById(R.id.search_circle_rel);
        search_circle_rel.setOnClickListener(this);

        total_amount_tv = parentView.findViewById(R.id.total_amount_tv);

        bottom_search_icon = parentView.findViewById(R.id.bottom_search_icon);
        bottom_search_icon.setBackgroundResource(R.mipmap.bottom_search);
        // Search Zoom-In and Zoom-Out Animation
        CommonUtility.startZoomAnimation(bottom_search_icon);


        error_tv_lin = parentView.findViewById(R.id.error_tv_lin);

        recycler_view = parentView.findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(layoutManager);

        getCurrencyData();

        onBindData(false);

        showCardCount();

        //manageDeviceBackButton();
    }

    void showCardCount()
    {
        //Log.e("Constant.wishListCount : ", Constant.wishListCount.toString());

        if(Constant.cardCount.equalsIgnoreCase("") || Constant.cardCount.equalsIgnoreCase("0"))
        {
            cart_count_tv.setVisibility(View.GONE);
        }
        else{
            cart_count_tv.setText(Constant.cardCount);
            cart_count_tv.setVisibility(View.VISIBLE);
        }

        if(Constant.wishListCount.equalsIgnoreCase("") || Constant.wishListCount.equalsIgnoreCase("0"))
        {
            wish_list_count_tv.setVisibility(View.GONE);
        }
        else{
            wish_list_count_tv.setText(Constant.wishListCount);
            wish_list_count_tv.setVisibility(View.VISIBLE);
        }
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
                if(Constant.manageClickEventForRedirection.equalsIgnoreCase("placeOrderAddToCardFragment"))
                {
                    gotoHomeRedirection();
                }
                else{}
            }
        };

        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }

    void gotoHomeRedirection()
    {
        if(Constant.manageClickEventForRedirection.equalsIgnoreCase("placeOrderAddToCardFragment"))
        {
            Constant.manageClickEventForRedirection = "";
            Log.e("manageClickEventForRedirection : ", Constant.manageClickEventForRedirection);
            Fragment newFragment = new HomeFragment();

            // Previous fragments Remove:
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            // For Open New Fragment:
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_body, newFragment);
            transaction.commit();
        }
        else{}
    }

    @Override
    public void onClick(View view)
    {
        Intent intent;
        int id = view.getId();
        if(id == R.id.home_rel)
        {
            if(Constant.manageClickEventForRedirection.equalsIgnoreCase("placeOrderAddToCardFragment"))
            {
                Constant.manageClickEventForRedirection = "";
            }
            else{}
            Fragment newFragment = new HomeFragment();

            // Previous fragments Remove:
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            // For Open New Fragment:
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_body, newFragment);
            transaction.commit();

            //getActivity().getSupportFragmentManager().popBackStack();
        }
        else if(id == R.id.category_rel)
        {
            Fragment fragment = new CategoryFragmentList();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if(id == R.id.wishlist_rel)
        {
            Fragment fragment = new WishlistFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if(id == R.id.cart_rel)
        {

        }
        else if(id == R.id.account_rel)
        {
            user_login = CommonUtility.getGlobalString(activity, "user_login");
            if(user_login.equalsIgnoreCase("yes"))
            {
                intent = new Intent(context, AccountSectionActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }
            else
            {
                intent = new Intent(context, LoginScreenActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }
        }
        else if(id == R.id.place_order_tv)
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
                getActivity().overridePendingTransition(0,0);

                //onPlaceOrder(false);
            }
            else{
                Constant.manageClickEventForRedirection = "placeOrderAddToCardFragment";
                intent = new Intent(activity, LoginScreenActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }
        }
        else if(id == R.id.search_circle_rel)
        {
            Constant.searchTitleName = "Solitaires";
            Constant.searchType= ApiConstants.NATURAL;
            Constant.filterClear="";
            intent = new Intent(context, SearchDiamondsActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0,0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        shimmer_view_container.stopShimmerAnimation();
        super.onPause();
    }

    private void shimmerShow()
    {
        //top_layout.setVisibility(View.GONE);
        recycler_view.setVisibility(View.GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmerAnimation();

    }

    private void shimmerStop()
    {
        shimmer_view_container.stopShimmerAnimation();
        //top_layout.setVisibility(View.VISIBLE);
        recycler_view.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
    }

    public void onBindData(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            String uuid = CommonUtility.getAndroidId(context);
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(getActivity(),this, urlParameter, ApiConstants.GET_CART_DETAILS, ApiConstants.GET_CART_DETAILS_ID,showLoader, "GET");

            error_tv_lin.setVisibility(View.GONE);
           // shimmerShow();

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
            vollyApiActivity = new VollyApiActivity(getActivity(),this, urlParameter, ApiConstants.GET_CART_DETAILS, ApiConstants.GET_CART_DETAILS_FOR_CALCULATION_ID,showLoader, "GET");

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

        //shimmerStop();

        try {
            Log.v("---Add to cart---Diamond----- : ", "--------JSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {

                case ApiConstants.GET_CART_DETAILS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                         rel_main_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color));
                         total_amount_lin.setVisibility(View.VISIBLE);
                        /*coupondiscountperc=CommonUtility.checkDouble(jsonObjectData.optString("coupon_discount_perc"));
                        subtotalaftercoupondiscount= CommonUtility.checkDouble(jsonObjectData.optString("subtotal_after_coupon_discount"));*/
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
                        // Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                        if(modelArrayList!=null && modelArrayList.size()==0)
                        {
                            total_amount_lin.setVisibility(View.GONE);
                            rel_main_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        }
                        /*if(modelArrayList!=null && modelArrayList.size()>0)
                        {
                            modelArrayList.clear();
                        }else{}
                        total_amount_lin.setVisibility(View.GONE);
                        rel_main_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        Constant.cardCount = "0";
                        showCardCount();*/
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
                        Log.e("jsonObjectData : ", "jsonObjectData : " + jsonObjectData.toString());

                        modelArrayList.remove(lastPosition);
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");
                        Constant.cardCount = CommonUtility.checkString(jObjDetails.optString("cart_count"));
                        Constant.wishListCount = CommonUtility.checkString(jObjDetails.optString("wishlist_count"));
                        showCardCount();
                        addToCartListAdapter.notifyDataSetChanged();
                        onCartTotalCalculation(true);

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
                        //Log.e("Delete", "Delete");

                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");
                        Constant.cardCount = CommonUtility.checkString(jObjDetails.optString("cart_count"));
                        showCardCount();
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

                            total_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        }
                        else{}

                        //total_amount_tv.setText(ApiConstants.rupeesIcon + "" + CommonUtility.currencyFormat(subTotal));
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        //   Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                        if(modelArrayList!=null && modelArrayList.size()==0)
                        {
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
                        Constant.manageClickEventForRedirection = "";
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
    public void getErrorResponce(String error, int service_ID)
    {
        //shimmerStop();
    }

    @Override
    public void actionInterface(String value, String action) {

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
        else if(action.equalsIgnoreCase("viewDetails"))
        {
            Constant.manageClickEventForRedirection="";
            CommonUtility.setGlobalString(context, "certificate_number", modelArrayList.get(position).getCertificateNo());
            Intent intent = new Intent(activity, DiamondDetailsActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0,0);
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

        cross_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constant.manageClickEventForRedirection = "";
                Intent intent = new Intent(context, HomeScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }
}


