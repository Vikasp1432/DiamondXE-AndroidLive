package com.diamondxe.Fragment;

import static com.diamondxe.ApiCalling.ApiConstants.USER_BUYER;
import static com.diamondxe.ApiCalling.ApiConstants.USER_DEALER;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.diamondxe.Activity.SearchDiamondsActivity;
import com.diamondxe.Adapter.WishListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.WishListModel;
import com.diamondxe.Interface.FragmentActionInterface;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperFragment;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;
import com.dxe.calc.dashboard.CalculatorActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class WishlistFragment extends SuperFragment implements RecyclerInterface,View.OnClickListener, FragmentActionInterface {

    private ShimmerFrameLayout shimmer_view_container;
    private RelativeLayout home_rel, category_rel, wishlist_rel, cart_rel, account_rel;
    private ImageView home_img, categories_img, wish_img, cart_img, account_img;
    private TextView home_tv, categories_tv, wish_tv, cart_tv, account_tv, cart_count_tv, wish_list_count_tv;
    private RelativeLayout search_circle_rel,dxe_calc_rev;

    String user_login = "";

    private RecyclerView recycler_view;
    private LinearLayout error_tv_lin;
    private ImageView bottom_search_icon;
    private HomeScreenActivity drawerActivity;
    private Activity activity;
    private Context context;
    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;

    public ArrayList<WishListModel> modelArrayList;
    WishListAdapter wishListAdapter;

    String selectedCurrencyValue ="",selectedCurrencyCode = "",selectedCurrencyDesc="",selectedCurrencyImage="";

    int lastPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);

        context = activity = drawerActivity = (HomeScreenActivity) getActivity();

        drawerActivity.setListener(this);

        init(view);

        return view;
    }

    private void init(View parentView)
    {
        modelArrayList = new ArrayList<>();

        shimmer_view_container = parentView.findViewById(R.id.shimmer_view_container);

        home_rel = parentView.findViewById(R.id.home_rel);
        home_rel.setOnClickListener(this);

        dxe_calc_rev = parentView.findViewById(R.id.dxe_calc_rev);
        dxe_calc_rev.setOnClickListener(this);

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
        account_img.setOnClickListener(this);
        wish_list_count_tv = parentView.findViewById(R.id.wish_list_count_tv);

        home_tv = parentView.findViewById(R.id.home_tv);
        categories_tv = parentView.findViewById(R.id.categories_tv);
        wish_tv = parentView.findViewById(R.id.wish_tv);
        cart_tv = parentView.findViewById(R.id.cart_tv);
        account_tv = parentView.findViewById(R.id.account_tv);
        account_tv.setOnClickListener(this);
        cart_count_tv = parentView.findViewById(R.id.cart_count_tv);

        home_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        categories_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        wish_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));
        cart_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        account_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));

        home_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));
        categories_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));
        wish_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
        cart_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));
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

        bottom_search_icon = parentView.findViewById(R.id.bottom_search_icon);
        bottom_search_icon.setBackgroundResource(R.mipmap.bottom_search);
        // Search Zoom-In and Zoom-Out Animation
        CommonUtility.startZoomAnimation(bottom_search_icon);


        error_tv_lin = parentView.findViewById(R.id.error_tv_lin);

        recycler_view = parentView.findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(layoutManager);

       /* WishListModel model = new WishListModel();
        model.setDiamond_image("https://thnk18.azureedge.net/img/images/imaged/J429-230135/still.jpg");
        model.setCertificate_no("6435145171");
        model.setSupplier_id("2425");
        model.setShape("Round");
        model.setCarat("0.52");
        model.setColor("F");
        model.setClarity("SI1");
        model.setSubtotal("30918");
        model.setStatus("30918");
        model.setIs_returnable("1");
        modelArrayList.add(model);*/

        getCurrencyData();

        onBindData(false);

        showCardCount();
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
        if(id == R.id.home_rel)
        {
            Fragment newFragment = new HomeFragment();

            // Previous fragments Remove:
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            // For Open New Fragment:
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_body, newFragment);
            transaction.commit();
        }
        else if(id == R.id.dxe_calc_rev) {
            Log.e("Call...", "..839.........");
            Intent intent1 = new Intent(context, CalculatorActivity.class);
            startActivity(intent1);
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

        }
        else if(id == R.id.cart_rel)
        {
            Fragment fragment = new AddToCartListFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if(id == R.id.account_rel)
        {
            user_login = CommonUtility.getGlobalString(activity, "user_login");
            if(user_login.equalsIgnoreCase("yes"))
            {
                /*intent = new Intent(context, AccountSectionActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);*/
                Fragment fragment = new AccountSectionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            else
            {
                intent = new Intent(context, LoginScreenActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }
        }
        else if (id == R.id.account_tv) {
            user_login = CommonUtility.getGlobalString(activity, "user_login");
            if(user_login.equalsIgnoreCase("yes"))
            {
                /*intent = new Intent(context, AccountSectionActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);*/
                Fragment fragment = new AccountSectionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            else
            {
                intent = new Intent(context, LoginScreenActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }
        }
        else if (id == R.id.account_img) {
            user_login = CommonUtility.getGlobalString(activity, "user_login");
            if(user_login.equalsIgnoreCase("yes"))
            {
                /*intent = new Intent(context, AccountSectionActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);*/
                Fragment fragment = new AccountSectionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            else
            {
                intent = new Intent(context, LoginScreenActivity.class);
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

    void showCardCount()
    {
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
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);

            //urlParameter.put("user_id", CommonUtility.getGlobalString(getActivity(),"user_id"));

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(getActivity(),this, urlParameter, ApiConstants.GET_WISHLIST_DETAILS, ApiConstants.GET_WISHLIST_DETAILS_ID,showLoader, "GET");

            error_tv_lin.setVisibility(View.GONE);
            //shimmerShow();

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            recycler_view.setVisibility(View.GONE);
        }
    }

    public void onAddToCartAPI(boolean showLoader, String certificateNo)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("certificateNo", "" + certificateNo);
            urlParameter.put("source", ""+ "wishlist");
            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.ADD_TO_CART, ApiConstants.ADD_TO_CART_ID,showLoader, "POST");
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    public void onRemoveFromWishlistAPI(boolean showLoader, String certificateNo)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("certificateNo", "" + certificateNo);
            urlParameter.put("source", "wishlist");
            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.REMOVE_FROM_WISHLIST, ApiConstants.REMOVE_FROM_WISHLIST_ID,showLoader, "POST");
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

        //shimmerStop();

        try {
            Log.v("----WISHLIST--Diamond----- : ", "--------JSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {

                case ApiConstants.GET_WISHLIST_DETAILS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if(modelArrayList.size() > 0)
                        {
                            modelArrayList.clear();
                        }else{}

                        //isDxeLUXE

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            WishListModel model = new WishListModel();

                            model.setCoupondiscountperc(CommonUtility.checkDouble(objectCodes.optString("coupon_discount_perc")));
                            model.setSubtotalaftercoupondiscount(CommonUtility.checkDouble(objectCodes.optString("subtotal_after_coupon_discount")));
                            model.setStockId(CommonUtility.checkString(objectCodes.optString("stock_id")));
                            model.setItemName(CommonUtility.checkString(objectCodes.optString("item_name")));
                            model.setItem_type(CommonUtility.checkString(objectCodes.optString("item_type")));
                            model.setCategory(CommonUtility.checkString(objectCodes.optString("category")));
                            model.setSupplierId(CommonUtility.checkString(objectCodes.optString("supplier_id")));
                            model.setCutGrade(CommonUtility.checkString(objectCodes.optString("cut_grade")));
                            model.setCertificateName(CommonUtility.checkString(objectCodes.optString("certificate_name")));
                            model.setCertificateNo(CommonUtility.checkString(objectCodes.optString("certificate_no")));
                            model.setPolish(CommonUtility.checkString(objectCodes.optString("polish")));
                            model.setSymmetry(CommonUtility.checkString(objectCodes.optString("symmetry")));
                            model.setMeasurement(CommonUtility.checkString(objectCodes.optString("measurement")));
                            model.setFluorescenceIntensity(CommonUtility.checkString(objectCodes.optString("fluorescence_intensity")));
                            model.setCarat(CommonUtility.checkString(objectCodes.optString("carat")));
                            model.setColor(CommonUtility.checkString(objectCodes.optString("color")));
                            model.setClarity(CommonUtility.checkString(objectCodes.optString("clarity")));
                            model.setShape(CommonUtility.checkString(objectCodes.optString("shape")));
                            model.setShade(CommonUtility.checkString(objectCodes.optString("shade")));
                            model.setTablePerc(CommonUtility.checkString(objectCodes.optString("table_perc")));
                            model.setDepthPerc(CommonUtility.checkString(objectCodes.optString("depth_perc")));
                            model.setLuster(CommonUtility.checkString(objectCodes.optString("luster")));
                            model.setEyeClean(CommonUtility.checkString(objectCodes.optString("eye_clean")));
                            model.setDiamondImage(CommonUtility.checkString(objectCodes.optString("diamond_image")));
                            model.setDiamondVideo(CommonUtility.checkString(objectCodes.optString("diamond_video")));
                            model.setTotalGstPerc(CommonUtility.checkString(objectCodes.optString("total_gst_perc")));
                            model.setStatus(CommonUtility.checkString(objectCodes.optString("status")));
                            model.setSubtotal(CommonUtility.checkString(objectCodes.optString("subtotal")));
                            model.setShowingSubTotal(CommonUtility.checkString(objectCodes.optString("subtotal")));
                            model.setTotalPrice(CommonUtility.checkString(objectCodes.optString("total_price")));
                            model.setIsReturnable(CommonUtility.checkString(objectCodes.optString("is_returnable")));
                            model.setDxePrefered(CommonUtility.checkString(objectCodes.optString("dxe_prefered")));
                            model.setIsCart(CommonUtility.checkString(objectCodes.optString("is_cart")));
                            model.setOnHold(CommonUtility.checkString(objectCodes.optString("on_hold")));
                            model.setStockNo(CommonUtility.checkString(objectCodes.optString("stock_no")));
                            model.setIsDxeLUXE(CommonUtility.checkInt(objectCodes.optString("isDxeLUXE")));
                            modelArrayList.add(model);

                        }
                        for (int i = 0; i <modelArrayList.size() ; i++)
                        {
                            Log.e("selectedCurrencyValue","...."+selectedCurrencyValue);
                            Log.e("selectedCurrencyCode","....."+selectedCurrencyCode);

                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, modelArrayList.get(i).getSubtotal());
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            Log.e("subTotalFormat","...."+subTotalFormat);
                            Log.e("getCurrencySymbol","....."+getCurrencySymbol);

                            modelArrayList.get(i).setShowingSubTotal(subTotalFormat);
                            modelArrayList.get(i).setCurrencySymbol(getCurrencySymbol);

                        }

                        wishListAdapter = new WishListAdapter(modelArrayList,context,this);
                        recycler_view.setAdapter(wishListAdapter);
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        //Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.ADD_TO_CART_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        //Toast.makeText(activity, ""+ message, Toast.LENGTH_SHORT).show();
                        modelArrayList.remove(lastPosition);
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");
                        Constant.cardCount = CommonUtility.checkString(jObjDetails.optString("cart_count"));
                        Constant.wishListCount = CommonUtility.checkString(jObjDetails.optString("wishlist_count"));
                        showCardCount();
                        wishListAdapter.notifyDataSetChanged();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                       // Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.REMOVE_FROM_WISHLIST_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        modelArrayList.remove(lastPosition);
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");
                        Constant.cardCount = CommonUtility.checkString(jObjDetails.optString("cart_count"));
                        Constant.wishListCount = CommonUtility.checkString(jObjDetails.optString("wishlist_count"));
                        showCardCount();
                        wishListAdapter.notifyDataSetChanged();

                        //Toast.makeText(activity, ""+ message, Toast.LENGTH_SHORT).show();
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
            onRemoveFromWishlistAPI(false, modelArrayList.get(position).getCertificateNo());
        }
        else if(action.equalsIgnoreCase("addToCart"))
        {
            lastPosition = position;
            onAddToCartAPI(false, modelArrayList.get(position).getCertificateNo());
        }
        else if(action.equalsIgnoreCase("viewDetails"))
        {
            Log.e("getItem_type",",,,,635...."+modelArrayList.get(position).getItem_type());

            String getItemType=modelArrayList.get(position).getItem_type();
            Constant.manageClickEventForRedirection="";
            CommonUtility.setGlobalString(context, "certificate_number", modelArrayList.get(position).getCertificateNo());
            Intent intent = new Intent(activity, DiamondDetailsActivity.class);
            if (modelArrayList.get(position).getIsDxeLUXE()==1)
            {
                intent.putExtra("intentvalue","dxeluxe");

            }
            if (getItemType.equalsIgnoreCase("gemstone"))
            {
                intent.putExtra("activityvalue", "gemstone");
            }

            startActivity(intent);
            getActivity().overridePendingTransition(0,0);

        }
    }

}


