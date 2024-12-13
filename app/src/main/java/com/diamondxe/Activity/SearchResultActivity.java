package com.diamondxe.Activity;

import static com.diamondxe.ApiCalling.ApiConstants.ACCOUNT_FRAGMENT;
import static com.diamondxe.ApiCalling.ApiConstants.CART_FRAGMENT;
import static com.diamondxe.ApiCalling.ApiConstants.CATEGORY_FRAGMENT;
import static com.diamondxe.ApiCalling.ApiConstants.DXE_CALC;
import static com.diamondxe.ApiCalling.ApiConstants.HOME_FRAGMENT;
import static com.diamondxe.ApiCalling.ApiConstants.USER_BUYER;
import static com.diamondxe.ApiCalling.ApiConstants.USER_DEALER;
import static com.diamondxe.ApiCalling.ApiConstants.WISHLIST_FRAGMENT;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Adapter.CurrencyListAdapter;
import com.diamondxe.Adapter.FilterApplied.ColorTypeAppliedFilterDataAdapter;
import com.diamondxe.Adapter.SearchResultListAdapter;
import com.diamondxe.Adapter.SearchResultListWiseAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.AttributeDetailsModel;
import com.diamondxe.Beans.CountryListModel;
import com.diamondxe.Beans.SearchResultTypeModel;
import com.diamondxe.Fragment.AccountSectionFragment;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.EndlessRecyclerViewScrollListener;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;
import com.diamondxe.databinding.ApiSolutionRequestDialogBinding;
import com.dxe.calc.dashboard.CalculatorActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchResultActivity extends SuperActivity implements RecyclerInterface {
    private ImageView back_img, bottom_search_icon, card_view_img, list_view_img, sort_img, arrow_img, bottom_search_icon_cross_country,
            country_drop_img;
    private RecyclerView recycler_view, recycler_view_apply_filter, recycler_view_country_list;
    private RelativeLayout container_body, show_more_data_rel;
    private LinearLayout lin_enquiry;
    private ProgressBar progressBar;
    private CardView search_circle_card, search_circle_card1_cross_country;
    private RelativeLayout show_popup_rel_country, card_popup1_country, country_layout_rel,curve_rel_country;
    private View translucent_background;
    private Animation rotateAnimation;
    private CountryListModel lastSelectedCountry; // Track the last selected country
    private CircleImageView selected_country_img;
    private TextView selected_country_name,selected_country_desc;
    private RelativeLayout home_rel, category_rel, wishlist_rel, cart_rel, account_rel,dxe_calc_rev;
    private ImageView home_img, categories_img, wish_img, cart_img, account_img,dropdown_;
    private TextView home_tv, categories_tv, wish_tv, cart_tv, account_tv, cart_count_tv, wish_list_count_tv;
    NestedScrollView nestedView;
    private boolean isArrowDown = false;
    private LinearLayout filter_type_lin;
    private CardView card_view_natural, card_view_grown,image_card_view_shimmer;
    private TextView natural_tv, grown_tv, error_tv,diamondtype_text;
    String manageAPICall = "",user_login="";
    ColorTypeAppliedFilterDataAdapter colorTypeAppliedFilterDataAdapter;
    int lastPosition = 0;
    int lastIndex;
    int checkOtherLocation = 0;
    private Spinner spinner_sorting_price;
    SearchResultListAdapter searchResultListAdapter;
    SearchResultListWiseAdapter searchResultListWiseAdapter;
    ArrayList<SearchResultTypeModel> modelArrayList;
    ArrayList<CountryListModel> countryArrayList;
    ArrayList<CountryListModel> selectedItems;
    CurrencyListAdapter countryListAdapter;
    List<AttributeDetailsModel> attributeNames;
    ArrayList<CountryListModel> localCurrencyArrayList = new ArrayList<>();
    int pageNo = 1;
    private EndlessRecyclerViewScrollListener scrollListener;
    private Activity activity;
    private Context context;
    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    private ShimmerFrameLayout shimmer_view_container;
    ArrayList<AttributeDetailsModel> attributeDetailsModels=new ArrayList<>();
    String cardViewAndListViewParttenShow = "CardView";
    String selectedCurrencyValue ="",selectedCurrencyCode = "",selectedCurrencyDesc="",selectedCurrencyImage="";
    String userRole = "";
    LinearLayoutManager layoutManager;
    String stocklocation="";
    String searchType="";
    Spinner spinnerAttributes;
    View dim_overlay;
    String getActivityValue = null;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        context = activity = this;
        attributeDetailsModels=new ArrayList<>();
        Intent intent = getIntent();
        String intentValue = null;


        if (intent != null) {
            intentValue = intent.getStringExtra("intentvalue");
            getActivityValue = intent.getStringExtra("gemStoneValue");
        }

        if (getActivityValue!=null)
        {
            Log.e("IntentValue", "..............162..........."+getActivityValue);
        }
        if (intentValue != null) {
            searchType=intentValue;
            Log.e("Search result", "Received intent value : " + intentValue);
        } else {
            Log.e("IntentValue", "No intent value received.");
        }
        userRole = CommonUtility.getGlobalString(activity, "login_user_role");

        modelArrayList = new ArrayList<>();
        countryArrayList = new ArrayList<>();
        selectedItems = new ArrayList<>();

        getCurrencyData();

        dim_overlay=findViewById(R.id.dim_overlay);
        dropdown_ = findViewById(R.id.dropdown_);
        dropdown_.setOnClickListener(this);
        container_body = findViewById(R.id.main);
        spinnerAttributes = findViewById(R.id.spinnerAttributes);
        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        nestedView = findViewById(R.id.nestedView);
        arrow_img = findViewById(R.id.arrow_img);

        lin_enquiry = findViewById(R.id.lin_enquiry);
        lin_enquiry.setOnClickListener(this);

        translucent_background = findViewById(R.id.translucent_background);
        translucent_background.setOnClickListener(this);

        progressBar = findViewById(R.id.progress_bar);
        show_more_data_rel = findViewById(R.id.show_more_data_rel);

        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        image_card_view_shimmer = findViewById(R.id.image_card_view_shimmer);
        diamondtype_text = findViewById(R.id.diamondtype_text);

        filter_type_lin = findViewById(R.id.filter_type_lin);
        filter_type_lin.setOnClickListener(this);

        nestedView.setVisibility(View.GONE);
        arrow_img.setImageResource(R.drawable.drop_up);
        isArrowDown = true;

        natural_tv = findViewById(R.id.natural_tv);
        natural_tv.setOnClickListener(this);

        grown_tv = findViewById(R.id.grown_tv);
        grown_tv.setOnClickListener(this);

        card_view_natural = findViewById(R.id.card_view_natural);
        card_view_grown = findViewById(R.id.card_view_grown);

        card_view_img = findViewById(R.id.card_view_img);
        card_view_img.setOnClickListener(this);

        list_view_img = findViewById(R.id.list_view_img);
        list_view_img.setOnClickListener(this);

        sort_img = findViewById(R.id.sort_img);
        sort_img.setOnClickListener(this);

        card_view_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));
        list_view_img.setColorFilter(ContextCompat.getColor(context, R.color.black));

        bottom_search_icon = findViewById(R.id.bottom_search_icon);
        bottom_search_icon.setBackgroundResource(R.drawable.plus);

        bottom_search_icon_cross_country = findViewById(R.id.bottom_search_icon_cross_country);

        search_circle_card = findViewById(R.id.search_circle_card);
        search_circle_card.setOnClickListener(this);

        search_circle_card1_cross_country = findViewById(R.id.search_circle_card1_cross_country);
        search_circle_card1_cross_country.setOnClickListener(this);

        curve_rel_country = findViewById(R.id.curve_rel_country);

        show_popup_rel_country = findViewById(R.id.show_popup_rel_country);
        show_popup_rel_country.setOnClickListener(this);

        card_popup1_country = findViewById(R.id.card_popup1_country);
        card_popup1_country.setOnClickListener(this);

        country_drop_img = findViewById(R.id.country_drop_img);

        selected_country_img = findViewById(R.id.selected_country_img);
        selected_country_name = findViewById(R.id.selected_country_name);
        selected_country_desc = findViewById(R.id.selected_country_desc);

        country_layout_rel = findViewById(R.id.country_layout_rel);
        country_layout_rel.setOnClickListener(this);

        home_rel = findViewById(R.id.home_rel);
        home_rel.setOnClickListener(this);

        category_rel = findViewById(R.id.category_rel);
        category_rel.setOnClickListener(this);

        wishlist_rel = findViewById(R.id.wishlist_rel);
        wishlist_rel.setOnClickListener(this);

        dxe_calc_rev=findViewById(R.id.dxe_calc_rev);
        dxe_calc_rev.setOnClickListener(this);

        cart_rel = findViewById(R.id.cart_rel);
        cart_rel.setOnClickListener(this);

        account_rel = findViewById(R.id.account_rel);
        account_rel.setOnClickListener(this);

        home_img = findViewById(R.id.home_img);
        categories_img = findViewById(R.id.categories_img);
        wish_img = findViewById(R.id.wish_img);
        cart_img = findViewById(R.id.cart_img);
        account_img = findViewById(R.id.account_img);

        home_tv = findViewById(R.id.home_tv);
        categories_tv = findViewById(R.id.categories_tv);
        wish_tv = findViewById(R.id.wish_tv);
        cart_tv = findViewById(R.id.cart_tv);
        account_tv = findViewById(R.id.account_tv);
        account_tv.setOnClickListener(this);
        cart_count_tv = findViewById(R.id.cart_count_tv);
        wish_list_count_tv = findViewById(R.id.wish_list_count_tv);

        home_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        categories_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        wish_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        cart_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        account_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        account_img.setOnClickListener(this);
        home_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));
        categories_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));
        wish_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));
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

        error_tv = findViewById(R.id.error_tv);

        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);

        recycler_view_country_list = findViewById(R.id.recycler_view_country_list);
        recycler_view_country_list.setHasFixedSize(true);


        Constant.cutTypeArrayList1 = new ArrayList<>();

        layoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManagerCountryList = new LinearLayoutManager(activity);
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_view_country_list.setLayoutManager(layoutManagerCountryList);

        recycler_view_apply_filter = findViewById(R.id.recycler_view_apply_filter);
        LinearLayoutManager layoutManagerAppliedFilter = new LinearLayoutManager(activity);
        layoutManagerAppliedFilter.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_view_apply_filter.setLayoutManager(layoutManagerAppliedFilter);
        recycler_view_apply_filter.setNestedScrollingEnabled(false);

//        Log.e("colorType","FilterApploedArrayList.....336....."+Constant.colorTypeFilterApploedArrayList.size());
        colorTypeAppliedFilterDataAdapter = new ColorTypeAppliedFilterDataAdapter(Constant.colorTypeFilterApploedArrayList,context,this);
        recycler_view_apply_filter.setAdapter(colorTypeAppliedFilterDataAdapter);

        // CardView Type Format List.
        searchResultListAdapter = new SearchResultListAdapter(modelArrayList,context,this, userRole,searchType);
        recycler_view.setAdapter(searchResultListAdapter);

        CommonUtility.startZoomAnimation(bottom_search_icon);
        CommonUtility.startZoomAnimation(bottom_search_icon_cross_country);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.e("Load more","...222...######.........74");
                Log.e("Load more",".Search1.....######.........74.."+modelArrayList.size());
                Log.e("Load more",".2...Search..######.........74.."+Constant.lazyLoadingLimit);

                if (modelArrayList.size()>= Constant.lazyLoadingLimit)
                {
                    //showLoadingIndicator(true);
                    shimmerShow();
                    pageNo = pageNo+1;
                    onBindDetails(true);
                } else {
                }
            }
        };
        // Adds the scroll listener to RecyclerView
        recycler_view.addOnScrollListener(scrollListener);

        if(Constant.searchType.equalsIgnoreCase(ApiConstants.NATURAL))
        {
            naturalCardTabColorSet();
        }
        else
        {
            labGrownCardTabColorSet();
        }

        updateLocalCurrencyList();

        countryListAdapter = new CurrencyListAdapter(localCurrencyArrayList,context,this);
        recycler_view_country_list.setAdapter(countryListAdapter);

        // First Position Country Data Set.
        setFirstPositionCountry();

        showCardCount();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed()
            {
                Constant.searchType = "";
                finish();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
        searchTypeIntent();
        onAttributeCall(false);
    }

    public void onAttributeCall(boolean showLoader)
    {
        Log.e("onBindDetails","............CALL......................");
        String uuid = CommonUtility.getAndroidId(context);

        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);

            //urlParameter.put("user_id", CommonUtility.getGlobalString(getActivity(),"user_id"));
            //urlParameter.put("authToken", CommonUtility.getGlobalString(context,"mobile_auth_token"));

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_ATTRIBUTES, ApiConstants.GET_ATTRIBUTES_ID,showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    // Updates the local currency list based on the selected currency code.
    // This function clears the existing local currency list and repopulates it with currencies from a constant list,
    // excluding the selected currency code. It then updates the adapter for the recycler view to reflect the changes.
    void updateLocalCurrencyList()
    {
        // Clear the local currency list to remove any previously added currencies
        localCurrencyArrayList.clear();

        //Log.e("------selectedCurrencyCode : ", selectedCurrencyCode.toString());

        // Iterate through the list of all currencies
        for (int i = 0; i < Constant.currencyArrayList.size(); i++)
        {
            // Check if the current currency is not the selected currency
           if(!Constant.currencyArrayList.get(i).getCurrency().equalsIgnoreCase(selectedCurrencyCode))
           {
               // Add the currency to the local currency list
               localCurrencyArrayList.add(Constant.currencyArrayList.get(i));
              // Log.e("------selectedCurrencyCode----IF : ", Constant.currencyArrayList.get(i).getCurrency().toString());
           }else{
              // Log.e("------selectedCurrencyCode----Else : ", Constant.currencyArrayList.get(i).getCurrency().toString());
           }
        }

        countryListAdapter = new CurrencyListAdapter(localCurrencyArrayList,context,this);
        recycler_view_country_list.setAdapter(countryListAdapter);
    }

    public void searchTypeIntent()
    {
        if(searchType.equals("dxeluxe"))
        {
            card_view_natural.setVisibility(View.GONE);
            card_view_grown.setVisibility(View.GONE);
            diamondtype_text.setVisibility(View.GONE);
        }
    }

    // Get Currency value, Code, Flag
    void getCurrencyData()
    {
        selectedCurrencyValue = CommonUtility.getGlobalString(context, "selected_currency_value");
        selectedCurrencyCode = CommonUtility.getGlobalString(context, "selected_currency_code");
        selectedCurrencyDesc = CommonUtility.getGlobalString(context, "selected_currency_desc");
        selectedCurrencyImage = CommonUtility.getGlobalString(context, "selected_currency_image");
    }

    void listViewFormatAdapterSet()
    {
        searchResultListWiseAdapter = new SearchResultListWiseAdapter(modelArrayList,context,this, userRole,searchType);
        recycler_view.setAdapter(searchResultListWiseAdapter);
    }

    void cardViewFormatAdapterSet()
    {
        searchResultListAdapter = new SearchResultListAdapter(modelArrayList,context,this, userRole,searchType);
        recycler_view.setAdapter(searchResultListAdapter);
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onClick(View view)
    {
        Intent intent;
        int id = view.getId();

        if(id == R.id.back_img)
        {
            Utils.hideKeyboard(activity);
            Constant.searchType = "";
            finish();
        }
        else if(id == R.id.natural_tv)
        {
            naturalCardTabColorSet();
        }
        else if(id == R.id.grown_tv)
        {
            labGrownCardTabColorSet();
        }
        else if(id == R.id.card_view_img)
        {
            cardViewAndListViewParttenShow = "CardView";

            card_view_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));
            list_view_img.setColorFilter(ContextCompat.getColor(context, R.color.black));

            cardViewFormatAdapterSet();
        }
        else if(id == R.id.list_view_img)
        {
            cardViewAndListViewParttenShow = "ListView";

            card_view_img.setColorFilter(ContextCompat.getColor(context, R.color.black));
            list_view_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));

            listViewFormatAdapterSet();
        }
        else  if (id == R.id.dropdown_){
            //spinnerAttributes.performClick();
            dim_overlay.setVisibility(View.VISIBLE);
            showCustomDropdown(spinnerAttributes, attributeNames);
        }
        else if(id == R.id.search_circle_card)
        {
            Log.e("search_circle_card",".523...********************...");

            translucent_background.setVisibility(View.VISIBLE);

            bottomBarClickableFalse();

            bottom_search_icon.setImageResource(R.drawable.cross);
            bottom_search_icon.setColorFilter(ContextCompat.getColor(context, R.color.white));
            bottom_search_icon.animate().rotation(45).setDuration(300).start();

            card_popup1_country.setVisibility(View.VISIBLE); // Option Popup Show
            curve_rel_country.setVisibility(View.VISIBLE); // Bottom Cut Center Position Layout show
            search_circle_card.setVisibility(View.GONE); // Bottom Plus Icon Hide
            search_circle_card1_cross_country.setVisibility(View.VISIBLE); // Bottom Cross Icon show
            show_popup_rel_country.setVisibility(View.VISIBLE); // Bottom Cross Icon show
            card_popup1_country.setVisibility(View.VISIBLE); // Selected Country View Layout
            Animation popup1Animation = createPopupAnimation();
            card_popup1_country.startAnimation(popup1Animation);

        }
        else if(id == R.id.search_circle_card1_cross_country)
        {
            hideCurrencyListLayout();
        }
        else if(id == R.id.country_layout_rel)
        {
            //card_popup_list_country.setVisibility(View.GONE);
            if (recycler_view_country_list.getVisibility() == View.VISIBLE) {
                recycler_view_country_list.setVisibility(View.GONE);
                country_drop_img.setImageResource(R.drawable.down);
               setBottomCountryPopupMargin();
            }
            else
            {
                country_drop_img.setImageResource(R.drawable.up);
                recycler_view_country_list.setVisibility(View.VISIBLE);
                setBottomCountryPopupListMargin();
            }

            //expand(show_popup_rel_country, 1040);
        }

        else if(id == R.id.sort_img)
        {
            initiatePopupWindow();
        }

        else if(id == R.id.filter_type_lin)
        {
            if (isArrowDown) {
                arrow_img.setImageResource(R.drawable.drop_down);
                nestedView.setVisibility(View.VISIBLE);
            } else {
                arrow_img.setImageResource(R.drawable.drop_up);
                nestedView.setVisibility(View.GONE);
            }
            isArrowDown = !isArrowDown;
        }

        else if(id == R.id.home_rel)
        {
            Constant.manageFragmentCalling = HOME_FRAGMENT;
            intent = new Intent(activity, HomeScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else if(id == R.id.dxe_calc_rev)
        {
            Constant.manageFragmentCalling = DXE_CALC;
            Intent intent1 = new Intent(context, CalculatorActivity.class);
            startActivity(intent1);
            overridePendingTransition(0,0);
        }
        else if(id == R.id.category_rel)
        {
            Constant.manageFragmentCalling = CATEGORY_FRAGMENT;
            intent = new Intent(activity, HomeScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else if(id == R.id.wishlist_rel)
        {
            Constant.manageFragmentCalling = WISHLIST_FRAGMENT;
            intent = new Intent(activity, HomeScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else if(id == R.id.cart_rel)
        {
            Constant.manageFragmentCalling = CART_FRAGMENT;
            intent = new Intent(activity, HomeScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else if (id == R.id.account_img) {
            user_login = CommonUtility.getGlobalString(activity, "user_login");
            if(user_login.equalsIgnoreCase("yes"))
            {
                /*intent = new Intent(context, AccountSectionActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);*/
                Constant.manageFragmentCalling = ACCOUNT_FRAGMENT;
                intent = new Intent(activity, HomeScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
               /* Fragment fragment = new AccountSectionFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/

            }
            else
            {
                intent = new Intent(context, LoginScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        }
        else if (id == R.id.account_tv) {
            user_login = CommonUtility.getGlobalString(activity, "user_login");
            if(user_login.equalsIgnoreCase("yes"))
            {
                /*intent = new Intent(context, AccountSectionActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);*/
                Constant.manageFragmentCalling = ACCOUNT_FRAGMENT;
                intent = new Intent(activity, HomeScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
               /* Fragment fragment = new AccountSectionFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/

            }
            else
            {
                intent = new Intent(context, LoginScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        }
        else if(id == R.id.account_rel)
        {
            user_login = CommonUtility.getGlobalString(activity, "user_login");
            if(user_login.equalsIgnoreCase("yes"))
            {
                /*intent = new Intent(context, AccountSectionActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);*/
                Constant.manageFragmentCalling = ACCOUNT_FRAGMENT;
                intent = new Intent(activity, HomeScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
               /* Fragment fragment = new AccountSectionFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/

            }
            else
            {
                intent = new Intent(context, LoginScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        }
        else if(id == R.id.translucent_background)
        {

            hideCurrencyListLayout();
        }
        else if(id == R.id.show_popup_rel_country)
        {}
        else if(id == R.id.lin_enquiry){
            Intent intentcall = new Intent(Intent.ACTION_DIAL);
            intentcall.setData(Uri.parse("tel:9892003399"));
            startActivity(intentcall);
        }
    }

    // Hide Currency List View Show in Screen On Cross Button click and Screen Touch Event Screen Touch Means When Show Transparency.
    // Hides the currency list layout and updates the UI elements accordingly.
    // This function performs several actions to hide the currency list layout and reset the UI to its
    void hideCurrencyListLayout()
    {
        bottom_search_icon.setImageResource(R.drawable.plus);
        bottom_search_icon.animate().rotation(0).setDuration(300).start();

        setBottomCountryPopupMargin();

        translucent_background.setVisibility(View.GONE); // Activity Transparency Gone
        recycler_view_country_list.setVisibility(View.GONE);

        bottomBarClickableTrue(); // When Transparency Hide Click True

        // Hide popups
        card_popup1_country.setVisibility(View.GONE); // Option Popup Hide
        curve_rel_country.setVisibility(View.GONE); // Bottom Cut Center Position Layout Hide
        search_circle_card1_cross_country.setVisibility(View.GONE); // Bottom Cross Icon Hide
        search_circle_card.setVisibility(View.VISIBLE); // Bottom Plus Icon Show
        show_popup_rel_country.setVisibility(View.GONE); // Bottom Plus Icon Show
    }

    // This function ensures that all bottom bar elements (home, category, wishlist, cart, account) are both
    // disabled and non-clickable, preventing users from interacting with these elements.
    void bottomBarClickableFalse()
    {
        home_rel.setEnabled(false);
        category_rel.setEnabled(false);
        wishlist_rel.setEnabled(false);
        cart_rel.setEnabled(false);
        account_rel.setEnabled(false);
        home_rel.setClickable(false);
        category_rel.setClickable(false);
        wishlist_rel.setClickable(false);
        cart_rel.setClickable(false);
        account_rel.setClickable(false);
    }

    // This function ensures that all bottom bar elements (home, category, wishlist, cart, account) are both
    // enabled and clickable, allowing users to interact with these elements.
    void bottomBarClickableTrue()
    {
        home_rel.setEnabled(true);
        category_rel.setEnabled(true);
        wishlist_rel.setEnabled(true);
        cart_rel.setEnabled(true);
        account_rel.setEnabled(true);
        home_rel.setClickable(true);
        category_rel.setClickable(true);
        wishlist_rel.setClickable(true);
        cart_rel.setClickable(true);
        account_rel.setClickable(true);
    }

    // Relative Layout Height Expand
    private void expand(final View v, final int newHeight) {
        final int currentHeight = v.getMeasuredHeight();
        final int heightDifference = newHeight - currentHeight;

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = currentHeight + (int) (heightDifference * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(300); // 300 milliseconds duration
        v.startAnimation(a);
    }

    private Animation createPopupAnimation() {
        Animation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0);
        translateAnimation.setDuration(300);

        Animation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(300);

        Animation animationSet = new AnimationSet(true);
        ((AnimationSet) animationSet).addAnimation(translateAnimation);
        ((AnimationSet) animationSet).addAnimation(alphaAnimation);

        return animationSet;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // If User Click Add to Cart and WishList Full List not Reload.
        userRole = CommonUtility.getGlobalString(activity, "login_user_role");

        if(manageAPICall.equalsIgnoreCase(""))
        {
            pageNo = 1;
            onBindDetails(true);
        }
        else{
            manageAPICall = ""; // Blank Here
        }

        //Toast.makeText(activity, "onResume : " + manageAPICall, Toast.LENGTH_SHORT).show();
    }

    private PopupWindow mDropdown = null;
    LayoutInflater mInflater;
   // Button pop;

    // Initializes and displays a PopupWindow for sorting options.
    // This method sets up a PopupWindow with various sorting options, applies the appropriate styles based on
    // the current sorting criteria, and manages interactions with the PopupWindow's items.
    private PopupWindow initiatePopupWindow() {
        try {
            if (mDropdown != null && mDropdown.isShowing()) {
                mDropdown.dismiss();
            }
            mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.custom_menu_sorting, null);

            final TextView recently_added = layout.findViewById(R.id.recently_added);
            final TextView price_low_high = layout.findViewById(R.id.price_low_high);
            final TextView price_high_low = layout.findViewById(R.id.price_high_low);
            final TextView size_low_high = layout.findViewById(R.id.size_low_high);
            final TextView size_high_low = layout.findViewById(R.id.size_high_low);

            recently_added.setText(getResources().getString(R.string.recently_added));
            price_low_high.setText(getResources().getString(R.string.price_low_high));
            price_high_low.setText(getResources().getString(R.string.price_high_low));
            size_low_high.setText(getResources().getString(R.string.size_low_high));
            size_high_low.setText(getResources().getString(R.string.size_high_low));

            // Apply the selected sorting style based on the current sorting criteria
            if(Constant.sortingBy.equalsIgnoreCase("RecentlyAdd"))
            {
                recently_added.setBackgroundResource(R.drawable.background_sorting_selected);
                recently_added.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
            else if(Constant.sortingBy.equalsIgnoreCase("PriceLow"))
            {
                price_low_high.setBackgroundResource(R.drawable.background_sorting_selected);
                price_low_high.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
            else if(Constant.sortingBy.equalsIgnoreCase("PriceHigh"))
            {
                price_high_low.setBackgroundResource(R.drawable.background_sorting_selected);
                price_high_low.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
            else if(Constant.sortingBy.equalsIgnoreCase("SizeLow"))
            {
                size_low_high.setBackgroundResource(R.drawable.background_sorting_selected);
                size_low_high.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
            else if(Constant.sortingBy.equalsIgnoreCase("SizeHigh"))
            {
                size_high_low.setBackgroundResource(R.drawable.background_sorting_selected);
                size_high_low.setTextColor(ContextCompat.getColor(context, R.color.black));
            }


            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);

            /*Drawable background = getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            mDropdown.setBackgroundDrawable(background);*/

            // Ensure pop is not null before using it
            if (sort_img != null) {
                mDropdown.showAsDropDown(sort_img, 5, -100);
            } else {
                Log.e("PopupWindow", "pop is null");
            }

            recently_added.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Constant.sortingBy = "RecentlyAdd";
                    if (mDropdown != null && mDropdown.isShowing()) {
                        mDropdown.dismiss();
                    } else {
                        Log.e("PopupWindow", "mDropdown is null or not showing");
                    }
                    apiCallForSortingValue();
                }
            });

            price_low_high.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Constant.sortingBy = "PriceLow";
                    if (mDropdown != null && mDropdown.isShowing()) {
                        mDropdown.dismiss();
                    } else {
                        Log.e("PopupWindow", "mDropdown is null or not showing");
                    }
                    apiCallForSortingValue();
                }
            });

            price_high_low.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Constant.sortingBy = "PriceHigh";
                    if (mDropdown != null && mDropdown.isShowing()) {
                        mDropdown.dismiss();
                    } else {
                        Log.e("PopupWindow", "mDropdown is null or not showing");
                    }
                    apiCallForSortingValue();
                }
            });

            size_low_high.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Constant.sortingBy = "SizeLow";
                    if (mDropdown != null && mDropdown.isShowing()) {
                        mDropdown.dismiss();
                    } else {
                        Log.e("PopupWindow", "mDropdown is null or not showing");
                    }
                    apiCallForSortingValue();
                }
            });

            size_high_low.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Constant.sortingBy = "SizeHigh";

                    if (mDropdown != null && mDropdown.isShowing()) {
                        mDropdown.dismiss();
                    } else {
                        Log.e("PopupWindow", "mDropdown is null or not showing");
                    }
                    apiCallForSortingValue();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;
    }

    void apiCallForSortingValue()
    {
        pageNo = 1;
        onBindDetails(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void naturalCardTabColorSet()
    {
        natural_tv.setBackgroundResource(R.drawable.background_gradient);
        grown_tv.setBackgroundResource(R.drawable.border_dark_purple);

        natural_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        grown_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));

        card_view_natural.setCardElevation(37f);
        card_view_natural.setCardElevation(18f);
        card_view_natural.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
        card_view_natural.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));

        card_view_grown.setCardElevation(0f);
        card_view_grown.setCardElevation(0f);
        card_view_grown.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_grown.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_grown.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_grown.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.bg_color));

        getCurrencyData();
        Constant.searchType = ApiConstants.NATURAL;
        pageNo = 1;
        onBindDetails(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void labGrownCardTabColorSet()
    {
        natural_tv.setBackgroundResource(R.drawable.border_dark_purple);
        grown_tv.setBackgroundResource(R.drawable.background_gradient);

        natural_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
        grown_tv.setTextColor(ContextCompat.getColor(context, R.color.white));

        card_view_natural.setCardElevation(0f);
        card_view_natural.setCardElevation(0f);
        card_view_natural.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_natural.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_natural.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_natural.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.bg_color));

        card_view_grown.setCardElevation(37f);
        card_view_grown.setCardElevation(18f);
        card_view_grown.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
        card_view_grown.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));

        getCurrencyData();
        Constant.searchType = ApiConstants.LAB_GROWN;
        pageNo = 1;
        onBindDetails(true);
    }

    @Override
    public void onPause() {
        shimmer_view_container.stopShimmerAnimation();
        super.onPause();
    }

    private void shimmerShow()
    {
        if(cardViewAndListViewParttenShow.equalsIgnoreCase("CardView"))
        {
            image_card_view_shimmer.setVisibility(View.VISIBLE);
        }
        else
        {
            image_card_view_shimmer.setVisibility(View.GONE);
        }
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

    // 899
    public void onBindDetails(boolean showLoader)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {

            // urlParameter.put("isLuxe", "1");
            //Log.e("----------Diamond------ : ", Constant.shapeDiamondValue.toString());
            urlParameter = new HashMap<String, String>();

            urlParameter.put("limit", "" + Constant.lazyLoadingLimit);
            urlParameter.put("page", ""+ pageNo);
            if(searchType.equals("dxeluxe"))
            {
                urlParameter.put("isLuxe", "1");
            }
            else {
                urlParameter.put("isLuxe", "0");
            }
            Log.e("stocklocation","1074......#######..................."+stocklocation);
            urlParameter.put("searchLocation", stocklocation);
            urlParameter.put("certificateNo", "");
            urlParameter.put("keyWord", Constant.searchKeyword);
            urlParameter.put("category", Constant.searchType);
            urlParameter.put("shape", Constant.shapeDiamondValue);
            urlParameter.put("colorType", Constant.colorType);
            urlParameter.put("color", Constant.colorValue);
            urlParameter.put("fancyColor", Constant.fancyColorValue);
            urlParameter.put("fancyColorIntencity", Constant.fancyColorIntensity);
            urlParameter.put("fancyColorOvertone", Constant.fancyColorOvertone);
            urlParameter.put("clarity", Constant.clarityValue);
            urlParameter.put("lengthFrom", Constant.lengthFrm);
            urlParameter.put("lengthTo", Constant.lengthTo);
            urlParameter.put("widthFrom", Constant.widthFrm);
            urlParameter.put("widthTo", Constant.widthTo);
            urlParameter.put("depthFrom", Constant.depthFrmInput);
            urlParameter.put("depthTo", Constant.depthToInput);
            urlParameter.put("lotId", Constant.lotID);
            urlParameter.put("certificate", Constant.certificateValue);
            urlParameter.put("fluorescence", Constant.fluorescenceValue);
            urlParameter.put("cut", Constant.cutValue);
            urlParameter.put("polish", Constant.polishValue);
            urlParameter.put("symmetry", Constant.symmetryValue);
            urlParameter.put("technology", Constant.technologyValue);
            urlParameter.put("eyeClean", Constant.eyeCleanValue);
            urlParameter.put("shade", Constant.shadeValue);
            urlParameter.put("luster", Constant.lusterValue);
            urlParameter.put("returnable", Constant.isReturnable);
            urlParameter.put("caratFrom", Constant.caratFrm);
            urlParameter.put("caratTo", Constant.caratTo);
            urlParameter.put("priceFrom", Constant.priceFrm);
            urlParameter.put("priceTo", Constant.priceTo);


            if(Constant.priceFrm.equalsIgnoreCase("") && Constant.priceTo.equalsIgnoreCase(""))
            {
                urlParameter.put("currValue", "");
            }
            else
            {
                if(!Constant.getCurrencyValue.equalsIgnoreCase(""))
                {
                    urlParameter.put("currValue", Constant.getCurrencyValue);
                }
                else{
                    urlParameter.put("currValue", "1");
                }
            }
            urlParameter.put("tablePerFrom", Constant.tableFrm);
            urlParameter.put("tablePerTo", Constant.tableTo);
            urlParameter.put("depthPerFrom", Constant.depthFrmSpinner);
            urlParameter.put("depthPerTo", Constant.depthToSpinner);
            urlParameter.put("crownFrom", Constant.crownFrm);
            urlParameter.put("crownTo", Constant.crownTo);
            urlParameter.put("pavillionFrom", Constant.pavillionFrm);
            urlParameter.put("pavillionTo", Constant.pavillionTo);
            urlParameter.put("sortBy", Constant.sortingBy);
            urlParameter.put("sessionId", "" + uuid);


            for (Map.Entry<String, String> entry : urlParameter.entrySet()) {
                Log.e("Constant Value..Search...1152....", entry.getKey() + " : " + entry.getValue());
            }

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_DIAMONDS, ApiConstants.GET_DIAMONDS_ID,showLoader, "GET");

            if(pageNo == 1)
            {
                error_tv.setVisibility(View.GONE);
                shimmerShow();
            }else{}


        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    public void getCountryListAPI(boolean showLoader)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            Log.e("----------Diamond------ : ", Constant.shapeDiamondValue.toString());
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_CURRENCY_RATES, ApiConstants.GET_CURRENCY_RATES_ID,showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }


    public void onAddToCartAPI(boolean showLoader, String certificateNo)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("certificateNo", "" + certificateNo);
            urlParameter.put("sessionId", "" + uuid);


            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.ADD_TO_CART, ApiConstants.ADD_TO_CART_ID,showLoader, "POST");
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    public void onAddToWishlistAPI(boolean showLoader, String certificateNo)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("certificateNo", "" + certificateNo);
            urlParameter.put("source", "");
            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.ADD_TO_WISHLIST, ApiConstants.ADD_TO_WISHLIST_ID,showLoader, "POST");
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
            urlParameter.put("source", "");
            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.REMOVE_FROM_WISHLIST, ApiConstants.REMOVE_FROM_WISHLIST_ID,showLoader, "POST");
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    private void showLoadingIndicator(boolean show) {
        if (show) {
            show_more_data_rel.setVisibility(View.VISIBLE);;
        } else {
            show_more_data_rel.setVisibility(View.GONE);
        }

    }
    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

        if(pageNo == 1)
        {
            shimmerStop();
        }else{
            //showLoadingIndicator(false);
            shimmerStop();
        }
        try {
            Log.e("------Diamond----- : ", "--------JSONObject-----**--- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {

                case ApiConstants.GET_DIAMONDS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONArray details = jsonObjectData.getJSONArray("details");
                        int avgDiamondPrice = jsonObjectData.optInt("check_other_locations");

                        checkOtherLocation=avgDiamondPrice;
                        Log.e("checkOtherLocation",",....1272..."+checkOtherLocation);
                        if(pageNo == 1)
                        {
                            if(modelArrayList.size() > 0)
                            {
                                modelArrayList.clear();
                            }
                        } else{}

                        //For Lazzy Loading : Disable or Enable
                        if (details == null && details.length()< Constant.lazyLoadingLimit)
                        {
                            scrollListener.loading=false;
                        }
                        else {
                            scrollListener.loading=true;
                        }

                        // Check if the page number is greater than 1
                        if(pageNo>1)
                        {
                            if(modelArrayList!=null && modelArrayList.size()>0)
                            {
                                // If so, hide the last item in the modelArrayList
                                modelArrayList.get(modelArrayList.size()-1).setVisible(false);
                                // Notify the adapter that the data has changed, so it can update the view
                                if(cardViewAndListViewParttenShow.equalsIgnoreCase("CardView"))
                                {
                                    searchResultListAdapter.notifyDataSetChanged();
                                }
                                else{
                                    searchResultListWiseAdapter.notifyDataSetChanged();
                                }
                            }else{}
                        } else {
                            // If the page number is 1 or less, no action is performed
                        }

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            SearchResultTypeModel model = new SearchResultTypeModel();
                            model.setCoupondiscountperc(CommonUtility.checkDouble(objectCodes.optString("coupon_discount_perc")));
                            model.setSubtotalaftercoupondiscount(CommonUtility.checkDouble(objectCodes.optString("subtotal_after_coupon_discount")));
                            model.setStock_id(CommonUtility.checkString(objectCodes.optString("stock_id")));
                            model.setItem_name(CommonUtility.checkString(objectCodes.optString("item_name")));
                            model.setCategory(CommonUtility.checkString(objectCodes.optString("category")));
                            model.setSupplier_id(CommonUtility.checkString(objectCodes.optString("supplier_id")));
                            model.setCut_grade(CommonUtility.checkString(objectCodes.optString("cut_grade")));
                            model.setCertificate_name(CommonUtility.checkString(objectCodes.optString("certificate_name")));
                            model.setCertificate_no(CommonUtility.checkString(objectCodes.optString("certificate_no")));
                            model.setPolish(CommonUtility.checkString(objectCodes.optString("polish")));
                            model.setSymmetry(CommonUtility.checkString(objectCodes.optString("symmetry")));
                            model.setMeasurement(CommonUtility.checkString(objectCodes.optString("measurement")));
                            model.setFluorescence_intensity(CommonUtility.checkString(objectCodes.optString("fluorescence_intensity")));
                            model.setCarat(CommonUtility.checkString(objectCodes.optString("carat")));
                            model.setColor(CommonUtility.checkString(objectCodes.optString("color")));
                            model.setClarity(CommonUtility.checkString(objectCodes.optString("clarity")));
                            model.setShape(CommonUtility.checkString(objectCodes.optString("shape")));
                            model.setShade(CommonUtility.checkString(objectCodes.optString("shade")));
                            model.setTable_perc(CommonUtility.checkString(objectCodes.optString("table_perc")));
                            model.setDepth_perc(CommonUtility.checkString(objectCodes.optString("depth_perc")));
                            model.setLuster(CommonUtility.checkString(objectCodes.optString("luster")));
                            model.setEye_clean(CommonUtility.checkString(objectCodes.optString("eye_clean")));
                            model.setDiamond_image(CommonUtility.checkString(objectCodes.optString("diamond_image")));
                            model.setDiamond_video(CommonUtility.checkString(objectCodes.optString("diamond_video")));
                            model.setTotal_gst_perc(CommonUtility.checkString(objectCodes.optString("total_gst_perc")));
                            model.setStatus(CommonUtility.checkString(objectCodes.optString("status")));
                            model.setSubtotal(CommonUtility.checkString(objectCodes.optString("subtotal")));
                            model.setShowingSubTotal(CommonUtility.checkString(objectCodes.optString("subtotal")));
                            model.setTotal_price(CommonUtility.checkString(objectCodes.optString("total_price")));
                            model.setIs_returnable(CommonUtility.checkString(objectCodes.optString("is_returnable")));
                            model.setDxe_prefered(CommonUtility.checkString(objectCodes.optString("dxe_prefered")));
                            model.setIs_cart(CommonUtility.checkString(objectCodes.optString("is_cart")));
                            model.setIs_wishlist(CommonUtility.checkString(objectCodes.optString("is_wishlist")));
                            model.setOn_hold(CommonUtility.checkString(objectCodes.optString("on_hold")));
                            model.setR_discount(CommonUtility.checkString(objectCodes.optString("r_discount")));
                            model.setR_discount_type(CommonUtility.checkString(objectCodes.optString("r_discount_type")));
                            model.setStock_no(CommonUtility.checkString(objectCodes.optString("stock_no")));
                            model.setCurrencySymbol(ApiConstants.rupeesIcon);


                            if(details.length()-1 == i)
                            {
                                model.setVisible(true);
                            }
                            else{
                                model.setVisible(false);
                            }
                            modelArrayList.add(model);
                        }

                        for (int i = 0; i <modelArrayList.size() ; i++)
                        {

                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, modelArrayList.get(i).getSubtotal());
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            modelArrayList.get(i).setShowingSubTotal(subTotalFormat);
                            modelArrayList.get(i).setCurrencySymbol(getCurrencySymbol);

                        }

                        // If Page No 1 Then set Data Otherwise only Refresh NotifyDataSet Changed Adapter
                       if(pageNo == 1)
                       {
                           // Check Which Type Of icon Selected From Ex CardView Icon Selected Sow List CardView Format otherwise Show ListView Format
                           if(cardViewAndListViewParttenShow.equalsIgnoreCase("CardView"))
                           {
                               searchResultListAdapter = new SearchResultListAdapter(modelArrayList,context,this, userRole,searchType);
                               recycler_view.setAdapter(searchResultListAdapter);
                           }
                           else{
                               searchResultListWiseAdapter = new SearchResultListWiseAdapter(modelArrayList,context,this, userRole,searchType);
                               recycler_view.setAdapter(searchResultListWiseAdapter);
                           }
                       }
                       else{
                           if(cardViewAndListViewParttenShow.equalsIgnoreCase("CardView"))
                           {
                               searchResultListAdapter.notifyDataSetChanged();
                           }
                           else{
                               searchResultListWiseAdapter.notifyDataSetChanged();
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

                case ApiConstants.ADD_TO_CART_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        modelArrayList.get(lastPosition).setIs_cart("1");

                        if(cardViewAndListViewParttenShow.equalsIgnoreCase("ListView"))
                        {
                            searchResultListWiseAdapter.notifyDataSetChanged();
                        }
                        else{
                            searchResultListAdapter.notifyDataSetChanged();
                        }

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

                case ApiConstants.ADD_TO_WISHLIST_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        /*if(modelArrayList.get(lastPosition).getIs_wishlist().equalsIgnoreCase("1"))
                        {
                            modelArrayList.get(lastPosition).setIs_wishlist("0");

                        }
                        else if(modelArrayList.get(lastPosition).getIs_wishlist().equalsIgnoreCase("0"))
                        {
                            modelArrayList.get(lastPosition).setIs_wishlist("1");
                        }
                        else{}*/

                        modelArrayList.get(lastPosition).setIs_wishlist("1");
                        if(cardViewAndListViewParttenShow.equalsIgnoreCase("ListView"))
                        {
                            searchResultListWiseAdapter.notifyDataSetChanged();
                        }
                        else{
                            searchResultListAdapter.notifyDataSetChanged();

                        }

                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");
                        Constant.wishListCount = CommonUtility.checkString(jObjDetails.optString("wishlist_count"));
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

                case ApiConstants.REMOVE_FROM_WISHLIST_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        modelArrayList.get(lastPosition).setIs_wishlist("0");
                        if(cardViewAndListViewParttenShow.equalsIgnoreCase("ListView"))
                        {
                            searchResultListWiseAdapter.notifyDataSetChanged();
                        }
                        else{
                            searchResultListAdapter.notifyDataSetChanged();
                        }
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");
                        Constant.wishListCount = CommonUtility.checkString(jObjDetails.optString("wishlist_count"));
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

                case ApiConstants.GET_CURRENCY_RATES_ID:

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
                            model.setTitle(CommonUtility.checkString(objectCodes.optString("title")));

                            model.setDesc(CommonUtility.checkString(objectCodes.optString("desc")));
                            model.setCurrency(CommonUtility.checkString(objectCodes.optString("currency")));
                            model.setCurrencySymbol(CommonUtility.checkString(objectCodes.optString("currency_symbol")));
                            model.setBaseCurrency(CommonUtility.checkString(objectCodes.optString("base_currency")));
                            model.setCurrencyRate(CommonUtility.checkString(objectCodes.optString("currency_rate")));
                            model.setValue(CommonUtility.checkString(objectCodes.optString("value")));
                            model.setImage(CommonUtility.checkString(objectCodes.optString("img")));
                            model.setSelected(false);
                            countryArrayList.add(model);

                        }
                        // Check Which Type Of icon Selected From Ex CardView Icon Selected Sow List CardView Format otherwise Show ListView Format
                        countryListAdapter = new CurrencyListAdapter(countryArrayList,context,this);
                        recycler_view_country_list.setAdapter(countryListAdapter);

                        // First Position Country Data Set.
                        //setFirstPositionCountry();

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

                case ApiConstants.GET_ATTRIBUTES_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1")) {
                        JSONArray details = jsonObjectData.getJSONArray("details");
                        for (int i = 0; i < details.length(); i++) {
                            attributeDetailsModels.clear();
                            JSONObject objectCode = details.getJSONObject(i);

                            String AttribType = objectCode.getString("AttribType");

                            JSONArray attribDetails = objectCode.getJSONArray("AttribDetails");

                            for (int j = 0; j < attribDetails.length(); j++) {
                                JSONObject innerObjectCodes = attribDetails.getJSONObject(j);

                                // String ss = innerObjectCodes.optString("DisplayAttr");

                                AttributeDetailsModel model = new AttributeDetailsModel();

                                model.setAttribId(CommonUtility.checkString(innerObjectCodes.optString("AttribId")));
                                model.setAttribTypeId(CommonUtility.checkString(innerObjectCodes.optString("AttribTypeId")));
                                model.setAttribType(CommonUtility.checkString(innerObjectCodes.optString("AttribType")));
                                model.setAttribCode(CommonUtility.checkString(innerObjectCodes.optString("AttribCode")));
                                model.setSortOrder(CommonUtility.checkString(innerObjectCodes.optString("SortOrder")));
                                model.setDisplayAttr(CommonUtility.checkString(innerObjectCodes.optString("DisplayAttr")));
                                // model.setFirstPosition(getFirstPosition(model, j));
                                model.setSelected(false);

                                attributeDetailsModels.add(model);


                            }
                        }

                        //Log.e("attributeDetailsModels", ".3525....################........" + attributeDetailsModels.size());
                       // spinnerAttributes.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.purple_light), PorterDuff.Mode.SRC_ATOP);

                       attributeNames = new ArrayList<>(attributeDetailsModels);

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                convertAttributeListToNames(attributeNames)
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerAttributes.setAdapter(adapter);

                        spinnerAttributes.setOnTouchListener((v, event) -> {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                dim_overlay.setVisibility(View.VISIBLE);
                                showCustomDropdown(v, attributeNames);
                                return true;
                            }
                            return false;
                        });
                    }
                        break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            if (checkOtherLocation==1)
            {
                StockLocationNoDataFound(this);
            }
            else if (modelArrayList !=null && modelArrayList.size()<=0)
            {
                error_tv.setVisibility(View.VISIBLE);
                error_tv.setText(""+ ApiConstants.NO_RESULT_FOUND);
                recycler_view.setVisibility(View.GONE);
            } else {
                if (dialogBuilder!=null)
                {
                    if(alertDialog.isShowing())
                    {
                        alertDialog.dismiss();
                        isDialogVisible = false;
                    }
                }
                error_tv.setVisibility(View.GONE);
                recycler_view.setVisibility(View.VISIBLE);
            }
        }
    }
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    private boolean isDialogVisible = false;
    public void StockLocationNoDataFound(Activity activity) {
        if (isDialogVisible) {
            return;
        }

         dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogView = inflater.inflate(R.layout.stock_location_notfound_dialog, null);

        dialogBuilder.setView(dialogView);
         alertDialog = dialogBuilder.create();

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        isDialogVisible = true;

        LinearLayout okbutton = dialogView.findViewById(R.id.ok_button);
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                isDialogVisible = false;
            }
        });

        ImageView crossImg = dialogView.findViewById(R.id.cross_img);
        crossImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                isDialogVisible = false;
            }
        });
    }


    private List<String> convertAttributeListToNames(List<AttributeDetailsModel> models) {
        List<String> names = new ArrayList<>();
        for (AttributeDetailsModel model : models) {
            names.add(model.getDisplayAttr());
        }
        return names;
    }

    private void showCustomDropdown(View anchor, List<AttributeDetailsModel> data) {
        PopupWindow popupWindow = new PopupWindow(this);
        ListView listView = new ListView(this);
        List<String> displayNames = new ArrayList<>();
        for (AttributeDetailsModel model : data) {
            displayNames.add(model.getDisplayAttr());
        }
        popupWindow.setOnDismissListener(() -> {
            // Hide the overlay when the popup is dismissed
            dim_overlay.setVisibility(View.GONE);
        });
        // Set the adapter for the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayNames
        );
        listView.setAdapter(adapter);

        popupWindow.setContentView(listView);
        popupWindow.setWidth(anchor.getWidth());
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.showAsDropDown(anchor);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            AttributeDetailsModel selectedModel = data.get(position);

            // Log the selected item's details
            Log.e("Dropdown", "Selected item details: " + selectedModel.getDisplayAttr());
            Log.e("Dropdown", "Selected item details: " + selectedModel.getAttribCode());
            spinnerAttributes.setSelection(position);
            stocklocation = selectedModel.getAttribCode();
            onBindDetails(false);
            dim_overlay.setVisibility(View.GONE);
            // Dismiss the popup
            popupWindow.dismiss();
        });
    }


    void setSubTotalAccordingCurrencyWise(String value, String currencyCode)
    {
        for (int i = 0; i <modelArrayList.size() ; i++)
        {
            String subTotalFormat =  CommonUtility.currencyConverter(value, currencyCode, modelArrayList.get(i).getSubtotal());
            String getCurrencySymbol = CommonUtility.getCurrencySymbol(currencyCode);
            modelArrayList.get(i).setShowingSubTotal(subTotalFormat);
            modelArrayList.get(i).setCurrencySymbol(getCurrencySymbol);
        }

        if(cardViewAndListViewParttenShow.equalsIgnoreCase("CardView"))
        {
            searchResultListAdapter.notifyDataSetChanged();
        }
        else{
            searchResultListWiseAdapter.notifyDataSetChanged();
        }

        currencyListCloseAfterCurrencySelect();
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
    void currencyListCloseAfterCurrencySelect()
    {
        bottom_search_icon.setImageResource(R.drawable.plus);
        bottom_search_icon.animate().rotation(0).setDuration(300).start();

        show_popup_rel_country.setVisibility(View.GONE); // Country Popup Layout Gone
        search_circle_card.setVisibility(View.VISIBLE); // Bottom Plus Icon Show
        search_circle_card1_cross_country.setVisibility(View.GONE); // Bottom Cross Icon Hide
        recycler_view_country_list.setVisibility(View.GONE);
        card_popup1_country.setVisibility(View.GONE); // Selected Country View Layout

        setBottomCountryPopupMargin();
    }

    void setFirstPositionCountry()
    {
        if(!selectedCurrencyImage.equalsIgnoreCase(""))
        {
            Picasso.with(context)
                    .load(selectedCurrencyImage)
                    .into(selected_country_img);
        } else{}

        selected_country_name.setText(selectedCurrencyCode);
        selected_country_desc.setText(selectedCurrencyDesc);
    }

    @Override
    public void getErrorResponce(String error, int service_ID)
    {
        if(pageNo == 1)
        {
            shimmerStop();
        }else{}
    }

    @Override
    public void itemClick(int position, String action) {

        Log.e("action","1829,,..Search...##....,,"+action);
        if(action.equalsIgnoreCase("searchDiamondDetails"))
        {
            bottom_search_icon.setImageResource(R.drawable.plus);
            bottom_search_icon.animate().rotation(0).setDuration(300).start();

            show_popup_rel_country.setVisibility(View.GONE); // Country Popup Layout Gone
            search_circle_card.setVisibility(View.VISIBLE); // Bottom Plus Icon Show
            search_circle_card1_cross_country.setVisibility(View.GONE); // Bottom Cross Icon Hide
            recycler_view_country_list.setVisibility(View.GONE);
            card_popup1_country.setVisibility(View.GONE); // Selected Country View Layout

            setBottomCountryPopupMargin();

            SearchResultTypeModel model = modelArrayList.get(position);
            //Log.e("----getCertificate_no--- : ", model.getCertificate_no().toString());
            Constant.manageClickEventForRedirection="";
            CommonUtility.setGlobalString(context, "certificate_number", model.getCertificate_no());
            Intent intent = new Intent(activity, DiamondDetailsActivity.class);
            if(searchType.equals("dxeluxe"))
            {
                intent.putExtra("intentvalue","dxeluxe");
            }
            intent.putExtra("activityvalue","");
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else if(action.equalsIgnoreCase("countryType"))
        {


            //LinearLayout linearLayout = findViewById(R.id.lin_enquiry);
            /*ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) linearLayout.getLayoutParams();
            int bottomMarginInPx = (int) (60 * getResources().getDisplayMetrics().density);
            layoutParams.bottomMargin = bottomMarginInPx;
            linearLayout.setLayoutParams(layoutParams);*/
            /*if (linearLayout.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) linearLayout.getLayoutParams();

                // Convert 60dp to pixels using the display metrics
                int bottomMarginInPx = (int) (60 * getResources().getDisplayMetrics().density);

                // Update the bottom margin
                layoutParams.bottomMargin = bottomMarginInPx;

                // Reassign the updated layout parameters to the LinearLayout
                linearLayout.setLayoutParams(layoutParams);
            } else {
                // Log or handle if LayoutParams is not an instance of MarginLayoutParams
                Log.e("LayoutParamsError", "LinearLayout does not support MarginLayoutParams.");
            }*/

            ///////////////////////////////////////////////////////////////////////////////////

            //CountryListModel model = Constant.currencyArrayList.get(position);
            CountryListModel model = localCurrencyArrayList.get(position);
            SearchResultTypeModel resultModel = modelArrayList.get(position);

            if(!model.getImage().equalsIgnoreCase(""))
            {
                Picasso.with(context)
                        .load(model.getImage())
                        .into(selected_country_img);
            }else{}

            selected_country_name.setText(model.getCurrency());
            selected_country_desc.setText(model.getDesc());

            // Remove the selected item from the list
            //Constant.currencyArrayList.remove(position);

            // Store the currently selected country as lastSelectedCountry
            // lastSelectedCountry = model;

            Constant.callHomeScreenOnResume = "yes"; // This Is Use For Call Home Screen OnResume Method To update Currency Flag and Current Rate, Symbol
            Constant.callForBackScreenForUpdateCurrencySymbol = "yes"; // This Is Use For Call Back Screen OnResume Method To update Currency Flag and Current Rate, Symbol

            CommonUtility.setGlobalString(context, "selected_currency_value", model.getValue());
            CommonUtility.setGlobalString(context, "selected_currency_code", model.getCurrency());
            CommonUtility.setGlobalString(context, "selected_currency_desc", model.getDesc());
            CommonUtility.setGlobalString(context, "selected_currency_image", model.getImage());

            translucent_background.setVisibility(View.GONE); // Hide Transparency
            bottomBarClickableTrue();// When Transparency Hide Click True

            getCurrencyData();

            Log.e("-------country_image------- : ", model.getImage());


            // Set Sub Total Amount According to Currency
            setSubTotalAccordingCurrencyWise(model.getValue(),model.getCurrency());
            updateLocalCurrencyList();

        }
        else if(action.equalsIgnoreCase("returnablePopup"))
        {
        }
        else if(action.equalsIgnoreCase("addToFavt"))
        {
            lastPosition = position;
            manageAPICall = "no";
            onAddToWishlistAPI(false, modelArrayList.get(position).getCertificate_no());
        }
        else if(action.equalsIgnoreCase("removeFromFavt"))
        {
            lastPosition = position;
            manageAPICall = "no";
            onRemoveFromWishlistAPI(false, modelArrayList.get(position).getCertificate_no());
        }
        else if(action.equalsIgnoreCase("addToCart"))
        {
            lastPosition = position;
            if(modelArrayList.get(position).getIs_cart().equalsIgnoreCase("1"))
            {
                Constant.manageClickEventForRedirection ="";
                Intent intent = new Intent(activity, MyCardListScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
            else
            {
                manageAPICall = "no";
                onAddToCartAPI(false, modelArrayList.get(position).getCertificate_no());
            }

        }
        else if(action.equalsIgnoreCase("colorAppliedType"))
        {
        }
        else if(action.equalsIgnoreCase("clarityAppliedType"))
        {
        }
        else if(action.equalsIgnoreCase("filterAppliedType"))
        {
            //Toast.makeText(activity, "Click : " + Constant.colorTypeFilterApploedArrayList.size(), Toast.LENGTH_SHORT).show();

            if(Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("Shape") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("Color") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("FancyColor") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("Clarity") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("Certificate") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("FluoRescence") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("Make") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("Cut") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("Polish") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("Symmetry") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("Technology") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("Eye") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("Shade") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("Luster") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("PriceFrm") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("SearchFrm") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("PriceTo") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("FancyColorIntensity") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("FancyColorOvertone") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("TableFrom") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("TableTo") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("DepthFromSpinner") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("DepthToSpinner") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("LengthFrom") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("LengthTo") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("WidthFrom") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("WidthTo") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("DepthFrmInput") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("DepthToInput") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("CrownFrom") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("CrownTo") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("PavillionFrom") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("PavillionTo") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("LotID") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("Location") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("CaratFrom") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("CaratTo") ||
                    Constant.colorTypeFilterApploedArrayList.get(position).getFilterType().equalsIgnoreCase("Returnable"))

            {
                String removeType = Constant.colorTypeFilterApploedArrayList.get(position).getFilterType();
              //  Toast.makeText(activity, "removeType : " + removeType, Toast.LENGTH_SHORT).show();
                Constant.colorTypeFilterApploedArrayList.remove(position);
                colorTypeAppliedFilterDataAdapter.notifyDataSetChanged();

                Constant.shapeDiamondValue = "";
                Constant.colorValue = "";
                Constant.fancyColorValue = "";
                Constant.clarityValue = "";
                Constant.certificateValue="";
                Constant.fluorescenceValue="";
                Constant.cutValue="";
                Constant.polishValue="";
                Constant.symmetryValue="";
                Constant.technologyValue="";
                Constant.eyeCleanValue="";
                Constant.shadeValue="";
                Constant.lusterValue="";
                /*Constant.fancyColorIntensity="";
                Constant.fancyColorOvertone="";*/

                if (removeType.equalsIgnoreCase("FancyColorIntensity")) {
                    Constant.fancyColorIntensity = "";
                } else if (removeType.equalsIgnoreCase("FancyColorOvertone")) {
                    Constant.fancyColorOvertone = "";
                }
                else if (removeType.equalsIgnoreCase("TableFrom")) {
                    Constant.tableFrm = "";
                }
                else if (removeType.equalsIgnoreCase("TableTo")) {
                    Constant.tableTo = "";
                }
                else if (removeType.equalsIgnoreCase("DepthFromSpinner")) {
                    Constant.depthFrmSpinner = "";
                }
                else if (removeType.equalsIgnoreCase("DepthToSpinner")) {
                    Constant.depthToSpinner = "";
                }
                else if (removeType.equalsIgnoreCase("LengthFrom")) {
                    Constant.lengthFrm = "";
                }
                else if (removeType.equalsIgnoreCase("LengthTo")) {
                    Constant.lengthTo = "";
                }
                else if (removeType.equalsIgnoreCase("WidthFrom")) {
                    Constant.widthFrm = "";
                }
                else if (removeType.equalsIgnoreCase("WidthTo")) {
                    Constant.widthTo = "";
                }
                else if (removeType.equalsIgnoreCase("DepthFrmInput")) {
                    Constant.depthFrmInput = "";
                }
                else if (removeType.equalsIgnoreCase("DepthToInput")) {
                    Constant.depthToInput = "";
                }
                else if (removeType.equalsIgnoreCase("CrownFrom")) {
                    Constant.crownFrm = "";
                }
                else if (removeType.equalsIgnoreCase("CrownTo")) {
                    Constant.crownTo = "";
                }
                else if (removeType.equalsIgnoreCase("PavillionFrom")) {
                    Constant.pavillionFrm = "";
                }
                else if (removeType.equalsIgnoreCase("PavillionTo")) {
                    Constant.pavillionTo = "";
                }
                else if (removeType.equalsIgnoreCase("LotID")) {
                    Constant.lotID = "";
                }
                else if (removeType.equalsIgnoreCase("Location")) {
                    Constant.location = "";
                }
                else if (removeType.equalsIgnoreCase("CaratFrom")) {
                    Constant.caratFrm = "";
                }
                else if (removeType.equalsIgnoreCase("CaratTo")) {
                    Constant.caratTo = "";
                }
                else if (removeType.equalsIgnoreCase("Returnable")) {
                    Constant.isReturnable = "";
                }

                if(Constant.colorTypeFilterApploedArrayList.size()>0)
                {
                    for (int i = 0; i <Constant.colorTypeFilterApploedArrayList.size() ; i++) {

                        if(Constant.colorTypeFilterApploedArrayList.get(i).getFilterType().equalsIgnoreCase("SearchFrm"))
                        {
                            Constant.searchKeyword = "";
                        }
                        else if(Constant.colorTypeFilterApploedArrayList.get(i).getFilterType().equalsIgnoreCase("PriceFrm"))
                        {
                            Constant.priceFrm = "";
                        }
                        else if(Constant.colorTypeFilterApploedArrayList.get(i).getFilterTypeTo().equalsIgnoreCase("PriceTo"))
                        {
                            Constant.priceTo = "";
                        }
                        else if(Constant.colorTypeFilterApploedArrayList.get(i).getFilterType().equalsIgnoreCase("Shape"))
                        {
                            if(Constant.colorTypeFilterApploedArrayList.get(i).isSelected())
                            {
                                if (Constant.shapeDiamondValue.equalsIgnoreCase("")) {
                                    Constant.shapeDiamondValue = Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                } else {
                                    Constant.shapeDiamondValue = Constant.shapeDiamondValue + "," + Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                }
                            }
                            else {}
                        }
                        else if(Constant.colorTypeFilterApploedArrayList.get(i).getFilterType().equalsIgnoreCase("Color"))
                        {
                            if(Constant.colorTypeFilterApploedArrayList.get(i).isSelected())
                            {
                                if (Constant.colorValue.equalsIgnoreCase("")) {
                                    Constant.colorValue = Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                } else {
                                    Constant.colorValue = Constant.colorValue + "," + Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                }
                            }
                            else {}
                        }
                        else if(Constant.colorTypeFilterApploedArrayList.get(i).getFilterType().equalsIgnoreCase("FancyColor"))
                        {
                            if(Constant.colorTypeFilterApploedArrayList.get(i).isSelected())
                            {
                                if (Constant.fancyColorValue.equalsIgnoreCase("")) {
                                    Constant.fancyColorValue = Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                } else {
                                    Constant.fancyColorValue = Constant.fancyColorValue + "," + Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                }
                            }
                            else {}
                        }
                        else if(Constant.colorTypeFilterApploedArrayList.get(i).getFilterType().equalsIgnoreCase("Clarity"))
                        {
                            if(Constant.colorTypeFilterApploedArrayList.get(i).isSelected())
                            {
                                if (Constant.clarityValue.equalsIgnoreCase("")) {
                                    Constant.clarityValue = Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                } else {
                                    Constant.clarityValue = Constant.clarityValue + "," + Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                }
                            }
                            else {}
                        }
                        else if(Constant.colorTypeFilterApploedArrayList.get(i).getFilterType().equalsIgnoreCase("Certificate"))
                        {
                            if(Constant.colorTypeFilterApploedArrayList.get(i).isSelected())
                            {
                                if (Constant.certificateValue.equalsIgnoreCase("")) {
                                    Constant.certificateValue = Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                } else {
                                    Constant.certificateValue = Constant.certificateValue + "," + Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                }
                            }
                            else {}
                        }
                        else if(Constant.colorTypeFilterApploedArrayList.get(i).getFilterType().equalsIgnoreCase("FluoRescence"))
                        {
                            if(Constant.colorTypeFilterApploedArrayList.get(i).isSelected())
                            {
                                if (Constant.fluorescenceValue.equalsIgnoreCase("")) {
                                    Constant.fluorescenceValue = Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                } else {
                                    Constant.fluorescenceValue = Constant.fluorescenceValue + "," + Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                }
                            }
                            else {}
                        }
                        else if(Constant.colorTypeFilterApploedArrayList.get(i).getFilterType().equalsIgnoreCase("Cut"))
                        {
                            if(Constant.colorTypeFilterApploedArrayList.get(i).isSelected())
                            {
                                if (Constant.cutValue.equalsIgnoreCase("")) {
                                    Constant.cutValue = Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                } else {
                                    Constant.cutValue = Constant.cutValue + "," + Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                }
                            }
                            else {}
                        }
                        else if(Constant.colorTypeFilterApploedArrayList.get(i).getFilterType().equalsIgnoreCase("Polish"))
                        {
                            if(Constant.colorTypeFilterApploedArrayList.get(i).isSelected())
                            {
                                if (Constant.polishValue.equalsIgnoreCase("")) {
                                    Constant.polishValue = Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                } else {
                                    Constant.polishValue = Constant.polishValue + "," + Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                }
                            }
                            else {}
                        }
                        else if(Constant.colorTypeFilterApploedArrayList.get(i).getFilterType().equalsIgnoreCase("Symmetry"))
                        {
                            if(Constant.colorTypeFilterApploedArrayList.get(i).isSelected())
                            {
                                if (Constant.symmetryValue.equalsIgnoreCase("")) {
                                    Constant.symmetryValue = Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                } else {
                                    Constant.symmetryValue = Constant.symmetryValue + "," + Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                }
                            }
                            else {}
                        }
                        else if(Constant.colorTypeFilterApploedArrayList.get(i).getFilterType().equalsIgnoreCase("Technology"))
                        {
                            if(Constant.colorTypeFilterApploedArrayList.get(i).isSelected())
                            {
                                if (Constant.technologyValue.equalsIgnoreCase("")) {
                                    Constant.technologyValue = Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                } else {
                                    Constant.technologyValue = Constant.technologyValue + "," + Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                }
                            }
                            else {}
                        }
                        else if(Constant.colorTypeFilterApploedArrayList.get(i).getFilterType().equalsIgnoreCase("Eye"))
                        {
                            if(Constant.colorTypeFilterApploedArrayList.get(i).isSelected())
                            {
                                if (Constant.eyeCleanValue.equalsIgnoreCase("")) {
                                    Constant.eyeCleanValue = Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                } else {
                                    Constant.eyeCleanValue = Constant.eyeCleanValue + "," + Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                }
                            }
                            else {}
                        }
                        else if(Constant.colorTypeFilterApploedArrayList.get(i).getFilterType().equalsIgnoreCase("Shade"))
                        {
                            if(Constant.colorTypeFilterApploedArrayList.get(i).isSelected())
                            {
                                if (Constant.shadeValue.equalsIgnoreCase("")) {
                                    Constant.shadeValue = Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                } else {
                                    Constant.shadeValue = Constant.shadeValue + "," + Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                }
                            }
                            else {}
                        }
                        else if(Constant.colorTypeFilterApploedArrayList.get(i).getFilterType().equalsIgnoreCase("Luster"))
                        {
                            if(Constant.colorTypeFilterApploedArrayList.get(i).isSelected())
                            {
                                if (Constant.lusterValue.equalsIgnoreCase("")) {
                                    Constant.lusterValue = Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                } else {
                                    Constant.lusterValue = Constant.lusterValue + "," + Constant.colorTypeFilterApploedArrayList.get(i).getAttribCode();
                                }
                            }
                            else {}
                        }
                        else if(removeType.equalsIgnoreCase("FancyColorIntensity"))
                        {
                            Constant.fancyColorIntensity = "";
                        }
                        else if(removeType.equalsIgnoreCase("FancyColorOvertone"))
                        {
                            Constant.fancyColorOvertone = "";
                        }
                        else if (removeType.equalsIgnoreCase("TableFrom")) {
                            Constant.tableFrm = "";
                        }
                        else if (removeType.equalsIgnoreCase("TableTo")) {
                            Constant.tableTo = "";
                        }
                        else if (removeType.equalsIgnoreCase("DepthFromSpinner")) {
                            Constant.depthFrmSpinner = "";
                        }
                        else if (removeType.equalsIgnoreCase("DepthToSpinner")) {
                            Constant.depthToSpinner = "";
                        }
                    }
                }
                else{
                    Constant.priceFrm = "";
                    Constant.searchKeyword = "";
                    Constant.priceTo = "";
                    Constant.caratFrm = "";
                    Constant.caratTo = "";
                    Constant.shapeDiamondValue = "";
                    Constant.colorValue = "";
                    Constant.fancyColorValue = "";
                    Constant.clarityValue = "";
                    Constant.certificateValue="";
                    Constant.fluorescenceValue="";
                    Constant.cutValue="";
                    Constant.polishValue="";
                    Constant.symmetryValue="";
                    Constant.technologyValue="";
                    Constant.eyeCleanValue="";
                    Constant.shadeValue="";
                    Constant.lusterValue="";
                    Constant.fancyColorIntensity="";
                    Constant.fancyColorOvertone="";
                    Constant.tableFrm="";
                    Constant.tableTo="";
                    Constant.depthFrmSpinner="";
                    Constant.depthToSpinner="";
                    Constant.lengthFrm="";
                    Constant.lengthTo="";
                    Constant.widthFrm="";
                    Constant.widthTo="";
                    Constant.depthFrmInput="";
                    Constant.depthToInput="";
                    Constant.crownFrm="";
                    Constant.crownTo="";
                    Constant.pavillionFrm="";
                    Constant.pavillionTo="";
                    Constant.lotID="";
                    Constant.location="";
                    Constant.isReturnable = "";
                }
                pageNo = 1;
                onBindDetails(true);
            }
            else
            {
            }
        }
    }

    // Country Popup Margin Set
    void setBottomCountryPopupMargin()
    {
        // search_circle_card1_cross_country Icon
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) search_circle_card1_cross_country.getLayoutParams();
        int marginBottomInPx = getResources().getDimensionPixelSize(R.dimen.dimen_8); // margin_bottom dimen resource
        layoutParams.bottomMargin = marginBottomInPx;

        // curve_rel_country Layout
        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) curve_rel_country.getLayoutParams();
        int marginBottomInPx1 = getResources().getDimensionPixelSize(R.dimen.dimen_3); // margin_bottom dimen resource
        layoutParams1.bottomMargin = marginBottomInPx1;

        // show_popup_rel_country Layout
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) show_popup_rel_country.getLayoutParams();
        int marginBottomInPx2 = getResources().getDimensionPixelSize(R.dimen.dimen_5); // margin_bottom dimen resource
        layoutParams2.bottomMargin = marginBottomInPx2;

        RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) lin_enquiry.getLayoutParams();
        int marginTopInPx3 = getResources().getDimensionPixelSize(R.dimen.dimen_1); // margin_bottom dimen resource
        layoutParams4.topMargin = marginTopInPx3;

        search_circle_card1_cross_country.setLayoutParams(layoutParams);
        curve_rel_country.setLayoutParams(layoutParams1);
        show_popup_rel_country.setLayoutParams(layoutParams2);

        expand(show_popup_rel_country, 425);
    }

    // Country List View Popup Margin Set
    void setBottomCountryPopupListMargin()
    {
        // search_circle_card1_cross_country Icon
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) search_circle_card1_cross_country.getLayoutParams();
        int marginBottomInPx = getResources().getDimensionPixelSize(R.dimen.dimen_8); // margin_bottom dimen resource
        layoutParams.bottomMargin = marginBottomInPx;

        // curve_rel_country Layout
        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) curve_rel_country.getLayoutParams();
        int marginBottomInPx1 = getResources().getDimensionPixelSize(R.dimen.dimen_3); // margin_bottom dimen resource
        layoutParams1.bottomMargin = marginBottomInPx1;

        // show_popup_rel_country Layout
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) show_popup_rel_country.getLayoutParams();
        int marginBottomInPx2 = getResources().getDimensionPixelSize(R.dimen.dimen_5); // margin_bottom dimen resource
        layoutParams2.bottomMargin = marginBottomInPx2;

        RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) lin_enquiry.getLayoutParams();
        int marginTopInPx3 = getResources().getDimensionPixelSize(R.dimen.dimen_8); // margin_bottom dimen resource
        layoutParams4.topMargin = marginTopInPx3;

        // Layout params ko apply
        search_circle_card1_cross_country.setLayoutParams(layoutParams);
        curve_rel_country.setLayoutParams(layoutParams1);
        //show_popup_rel_country.setLayoutParams(layoutParams2);

        expand(show_popup_rel_country, 1350);
    }

}