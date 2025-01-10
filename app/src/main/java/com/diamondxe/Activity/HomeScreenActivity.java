package com.diamondxe.Activity;

import static com.diamondxe.ApiCalling.ApiConstants.ACCOUNT_FRAGMENT;
import static com.diamondxe.ApiCalling.ApiConstants.CART_FRAGMENT;
import static com.diamondxe.ApiCalling.ApiConstants.CATEGORY_FRAGMENT;
import static com.diamondxe.ApiCalling.ApiConstants.DXE_CALC;
import static com.diamondxe.ApiCalling.ApiConstants.FACEBOOK_URL;
import static com.diamondxe.ApiCalling.ApiConstants.HOME_FRAGMENT;
import static com.diamondxe.ApiCalling.ApiConstants.INDIA_CURRENCY_CODE;
import static com.diamondxe.ApiCalling.ApiConstants.INDIA_CURRENCY_DESC;
import static com.diamondxe.ApiCalling.ApiConstants.INDIA_CURRENCY_VALUE;
import static com.diamondxe.ApiCalling.ApiConstants.INDIA_FLAG_URL;
import static com.diamondxe.ApiCalling.ApiConstants.INSTAGRAM_URL;
import static com.diamondxe.ApiCalling.ApiConstants.LINKDIN_URL;
import static com.diamondxe.ApiCalling.ApiConstants.WISHLIST_FRAGMENT;
import static com.diamondxe.ApiCalling.ApiConstants.YOUTUBE_URL;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.TimeZone;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Activity.CreateDemand.CreateDemandHome;
import com.diamondxe.Activity.DXELuex.DXELuxeRegisterActivity;
import com.diamondxe.Activity.Gemstones.GemstomeSearchActivity;
import com.diamondxe.Activity.MyOrder.MyOrderListScreenActivity;
import com.diamondxe.Activity.NovaaraFrom.NovaaraFromActivity;
import com.diamondxe.Activity.PaymentPages.AppInfoManager;
import com.diamondxe.Activity.SharedData.SharedPreferenceHelper;
import com.diamondxe.Adapter.CurrencyListAdapter;
import com.diamondxe.Adapter.DrawerItemCustomAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.CountryListModel;
import com.diamondxe.Beans.DrawerMenuModel;
import com.diamondxe.Fragment.AccountSectionFragment;
import com.diamondxe.Fragment.AddToCartListFragment;
import com.diamondxe.Fragment.CategoryFragmentList;
import com.diamondxe.Fragment.HomeFragment;
import com.diamondxe.Fragment.WishlistFragment;
import com.diamondxe.Interface.FragmentActionInterface;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.Network.NetworkConsumer;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.TimeZoneCountryCodeMapper;
import com.diamondxe.Utils.Utils;
import com.dxe.calc.dashboard.CalculatorActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeScreenActivity extends SuperActivity implements TwoRecyclerInterface, RecyclerInterface {

    //For Api Calling
    VollyApiActivity vollyApiActivity;
    HashMap<String, String> urlParameter;
    private HomeFragment homeFragment;
    FragmentActionInterface fragmentActionInterface;
    public Toolbar toolbar;
    public TextView title_tv;
    Fragment fragment = null;

    public String title = "";
    RelativeLayout layout;
    public ImageView user_img;
    public TextView username_tv, login_type_role_tv, version_code_tv;
    private ImageView menu_img;
    private LinearLayout container_toolbar, contentView1, lin_bottom_social_media;
    String userPincode = "", userCity = "";
    String selectedCurrencyCode = "", selectedCurrencyValue = "", selectedCurrencyDesc = "", selectedCurrencyImage = "", user_login = "";
    android.app.AlertDialog alertDialog;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private CardView contentView;
    private static final float END_SCALE = 0.9f;
    private static final float MAX_ELEVATION = 8f;
    CurrencyListAdapter currencyListAdapter;
    DrawerLayout drawer_layout;
    public static Activity context;
    NetworkConsumer networkConsumer;
    public String image = "";
    private ExpandableListView recyclerview;
    private ImageView notification_img, pincode_img, instagram_img, facebook_img, linkdin_img, youtube_img;
    private CircleImageView flag_img;
    private DrawerItemCustomAdapter adapter;
    private ArrayList<DrawerMenuModel> menuList = new ArrayList<>();
    String intnetOnPerticularFragment = "", versionName = "";
    private int selectedPosition = -1;
    private int childSelectedPosition = -1;
    private Parcelable state;
    public static boolean backFromHome = false;
    private long lastPress = 0;
    int showDialog = 0;
    private SharedPreferenceHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        sharedPrefHelper = new SharedPreferenceHelper(this);
        //sharedPrefHelper.resetAppUpdateDialogFlag();

        notification_img = findViewById(R.id.notification_img);
        pincode_img = findViewById(R.id.pincode_img);
        flag_img = findViewById(R.id.flag_img);
        layout = findViewById(R.id.toolbar_layout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title_tv = findViewById(R.id.title_tv);
        contentView = findViewById(R.id.content);
        title_tv.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        context = HomeScreenActivity.this;

        getData();
        getCurrencyData();
        getAppVersionName();

        Constant.currencyArrayList = new ArrayList<>();

        intnetOnPerticularFragment = getIntent().getStringExtra("intentOn");

        recyclerview = findViewById(R.id.expandableList);
        drawer_layout = findViewById(R.id.drawer_layout);

        login_type_role_tv = findViewById(R.id.login_type_role_tv);
        user_img = findViewById(R.id.user_img);
        username_tv = findViewById(R.id.username_tv);

        lin_bottom_social_media = findViewById(R.id.lin_bottom_social_media);

        menu_img = findViewById(R.id.menu_img);
        container_toolbar = findViewById(R.id.container_toolbar);

        version_code_tv = findViewById(R.id.version_code_tv);

        if (!versionName.equalsIgnoreCase("")) {
            version_code_tv.setText(getResources().getString(R.string.version_name) + " - " + versionName);
        } else {
        }

        setDataToAdepter();
        setUserData();

        instagram_img = findViewById(R.id.instagram_img);
        instagram_img.setOnClickListener(this);

        facebook_img = findViewById(R.id.facebook_img);
        facebook_img.setOnClickListener(this);

        linkdin_img = findViewById(R.id.linkdin_img);
        linkdin_img.setOnClickListener(this);

        youtube_img = findViewById(R.id.youtube_img);
        youtube_img.setOnClickListener(this);

        toolbar.setNavigationIcon(new DrawerArrowDrawable(this));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                                                         drawer_layout.closeDrawer(GravityCompat.START);
                                                     } else {
                                                         drawer_layout.openDrawer(GravityCompat.START);
                                                     }
                                                 }
                                             }
        );

        //drawer_layout.setScrimColor(getResources().getColor(R.color.transparent));
        drawer_layout.setScrimColor(ContextCompat.getColor(context, R.color.transparent));
        drawer_layout.setDrawerElevation(0); // Ensure there's no elevation
        drawer_layout.setDrawerShadow(new ColorDrawable(Color.TRANSPARENT), GravityCompat.END);

        // Drawer Half Animation.
        drawer_layout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.P) // Requires API level P or higher
                                            @Override
                                            public void onDrawerSlide(View drawerView, float slideOffset) {
                                                // labelView.setVisibility(slideOffset > 0 ? View.VISIBLE : View.GONE);

                                                // Calculate the scaled offset for the content view based on the drawer's slide offset
                                                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                                                final float offsetScale = 1 - diffScaledOffset;

                                                // Apply scaling transformation to the content view
                                                contentView.setScaleX(offsetScale);
                                                contentView.setScaleY(offsetScale);

                                                // Calculate translation for the content view based on the drawer's position
                                                final float xOffset = drawerView.getWidth() * slideOffset;
                                                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 3;
                                                final float xTranslation = xOffset - xOffsetDiff;

                                                // Apply translation to the content view
                                                contentView.setTranslationX(xTranslation); // Move content view based on drawer position

                                                // Apply shadow effect
                                                // Shadow effect for the drawer
                                                if (slideOffset > 0) {
                                                    // Optional: Add shadow effect when the drawer is open (code not shown)
                                                } else {
                                                    // Reset shadow for closing drawer or negative slide
                                                    drawer_layout.setDrawerElevation(0);
                                                }
                                            }

                                            @Override
                                            public void onDrawerClosed(View drawerView) {
                                                // Reset shadow effect when the drawer is fully closed
                                                drawer_layout.setDrawerElevation(0); // Ensure no shadow elevation when closed
                                            }
                                        }
        );
        notification_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, NotificationScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        pincode_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnterPinCodeDialog(context, context);
            }
        });

        flag_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateCurrencyPopupWindow();
                // fragmentActionInterface.actionInterface("","PinCode");
            }
        });

        //for open particular Fragment inside Activity
        if (intnetOnPerticularFragment != null && intnetOnPerticularFragment.equalsIgnoreCase("Notification111")) {
            changeFragment(intnetOnPerticularFragment);
            //notification_img.setVisibility(View.VISIBLE);
        } else {
            Log.e("manageClickEventForRedirection", Constant.manageClickEventForRedirection);
            if (Constant.manageClickEventForRedirection.equalsIgnoreCase("placeOrderAddToCardFragment")) {
                Constant.manageClickEventForRedirection = "";
                Log.e("manageClickEventForRedirection1", Constant.manageClickEventForRedirection);
                title = "DiamondXE";
                Fragment fragment = new AddToCartListFragment();
                changefrag(fragment);
            } else {
                //title = "Home";
                title = "DiamondXE";
                Fragment fragment = new HomeFragment();
                changefrag(fragment);
            }

        }

        getCurrencyListAPI(true);

        // Add a callback to handle back press events
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPress();
            }
        });


    }

    void PlayStoreDialog(final Activity activity, Boolean ishown) {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.playstore_update_layout, null);
        dialogBuilder.setView(dialogView);
        final android.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        final ImageView cancel_dialog = dialogView.findViewById(R.id.cancel_dialog);
        if (ishown) {
            cancel_dialog.setVisibility(View.VISIBLE);
        } else {
            cancel_dialog.setVisibility(View.GONE);
        }
        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                showDialog = 0;
                sharedPrefHelper.setAppUpdateDialogShown();
            }
        });
        final Button update_now_button = dialogView.findViewById(R.id.update_now_button);
        update_now_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showDialog = 0;
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(true);
        //showDialog=0
        Log.e("showDialog", "........@@@@@@@@........" + showDialog);
        /*if(showDialog!=1)
        {
            alertDialog.show();
        }*/
        if (!alertDialog.isShowing()) {
            alertDialog.dismiss();
            if (showDialog == 0) {
                showDialog = 1;
                alertDialog.show();
            }

        }

    }

    public boolean isUpdateRequired(String currentVersion, String playStoreVersion) {
        return compareVersions(currentVersion, playStoreVersion) < 0;
    }

    public int compareVersions(String version1, String version2) {
        version1 = version1.replaceAll("^v", "");
        version2 = version2.replaceAll("^v", "");

        Log.e("version1", "..." + version1);
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        for (int i = 0; i < Math.min(v1.length, v2.length); i++) {
            try {
                int v1Part = Integer.parseInt(v1[i]);
                int v2Part = Integer.parseInt(v2[i]);
                if (v1Part > v2Part) {
                    return 1;
                } else if (v1Part < v2Part) {
                    return -1;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return Integer.compare(v1.length, v2.length);
    }


    /*public String getCurrentAppVersion(Context context) {
        String versionName = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }*/


    private void checkCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.container_body); // fragment_container container ka id hai

        if (currentFragment instanceof HomeFragment) {
            // Home visible hai
            Constant.manageFragmentCalling = "";  // Blank After Call Fragment
            title = "DiamondXE";
            Fragment fragment = new HomeFragment();
            changefrag(fragment);
        } else if (currentFragment instanceof CategoryFragmentList) {
            // Category visible hai
            //Toast.makeText(this, "Category is visible", Toast.LENGTH_SHORT).show();
        } else if (currentFragment instanceof WishlistFragment) {
            // Wishlist visible hai
            Constant.manageFragmentCalling = ""; // Blank After Call Fragment
            title = "DiamondXE";
            Fragment fragment = new WishlistFragment();
            changefrag(fragment);

        } else if (currentFragment instanceof AddToCartListFragment) {
            // Add to Cart visible hai
            //Toast.makeText(this, "Add to Cart is visible", Toast.LENGTH_SHORT).show();
            Constant.manageFragmentCalling = "";  // Blank After Call Fragment
            title = "DiamondXE";
            Fragment fragment = new AddToCartListFragment();
            changefrag(fragment);
        } else {
            // Default case
            //  Toast.makeText(this, "No Fragment is visible", Toast.LENGTH_SHORT).show();
        }
    }

    void getData() {
        userPincode = CommonUtility.getGlobalString(context, "user_select_pincode");
        userCity = CommonUtility.getGlobalString(context, "user_select_pincode_city");
    }

    // Get Currency Value Code and Image
    void getCurrencyData() {
        selectedCurrencyCode = CommonUtility.getGlobalString(context, "selected_currency_code");
        selectedCurrencyValue = CommonUtility.getGlobalString(context, "selected_currency_value");
        selectedCurrencyDesc = CommonUtility.getGlobalString(context, "selected_currency_desc");
        selectedCurrencyImage = CommonUtility.getGlobalString(context, "selected_currency_image");

        // Log.e("-------country_image1------- : ", selectedCurrencyImage);

        // Select Currency Image is not blank show select currency country flag otherwise show india flag
        if (!selectedCurrencyImage.equalsIgnoreCase("")) {
            Picasso.with(context)
                    .load(selectedCurrencyImage)
                    .into(flag_img);
        } else {
            Picasso.with(context)
                    .load(INDIA_FLAG_URL)
                    .into(flag_img);
        }

        getCurrencySelectCountryCurrencySymbolAndValue(selectedCurrencyCode, selectedCurrencyValue);
    }

    // Check Current Currency and Currency Value If Currency Not Select Set By Default India.
    void getCurrencySelectCountryCurrencySymbolAndValue(String selectedCurrencyCode, String selectedCurrencyValue) {
        /*Log.e("-------selectedCurrencyCode---------- : ", selectedCurrencyCode);
        Log.e("-------selectedCurrencyCode1---------- : ", selectedCurrencyValue);*/
        if (!Constant.getCurrencySymbol.equalsIgnoreCase("")) {
            Constant.getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
            Constant.getCurrencyValue = selectedCurrencyValue;

        } else {
            Constant.getCurrencySymbol = CommonUtility.getCurrencySymbol(INDIA_CURRENCY_CODE);
            Constant.getCurrencyValue = INDIA_CURRENCY_VALUE;
        }
    }

    public void getCurrencyListAPI(boolean showLoader) {
        String uuid = CommonUtility.getAndroidId(context);

        if (Utils.isNetworkAvailable(context)) {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context, this, urlParameter, ApiConstants.GET_CURRENCY_RATES,
                    ApiConstants.GET_CURRENCY_RATES_ID, showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    // Get Version Name Here.
    void getAppVersionName() {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Manage Device Back Button to Close App.
    private void handleBackPress() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.container_body); // fragment_container aapke container ka id hai

        // Check If Current Fragment Home Is Visible Then App Close Back Button Call.
        if (currentFragment instanceof HomeFragment) {
            if (!backFromHome) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastPress > 5000) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.press_back_again), Toast.LENGTH_SHORT).show();
                        lastPress = currentTime;
                    } else {
                        finish(); // Close the activity
                    }
                }
            } else {
                finish(); // Handle back press when backFromHome is true
            }
        } else {
            Constant.manageFragmentCalling = "";  // Blank After Call Fragment
            title = "DiamondXE";
            Fragment fragment = new HomeFragment();
            changefrag(fragment);
        }

    }

    public void getAppVersionCheck(boolean showLoader) {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context)) {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);
            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(this, this, urlParameter, ApiConstants.GET_APP_CHECK_VERSION, ApiConstants.GET_APP_CHECK_VERSION_ID, showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /////////////////////VERSION CHECK////////////////////////
        getAppVersionCheck(true);
        // This is Use When User Come For Any Other Activity to Wish List Fragment and Call Fragment.
        if (Constant.manageFragmentCalling.equalsIgnoreCase(DXE_CALC)) {
            // Toast.makeText(this, "WishList is visible..8", Toast.LENGTH_SHORT).show();
            Constant.manageFragmentCalling = ""; // Blank After Call Fragment
            title = "DiamondXE";
            Fragment fragment = new WishlistFragment();
            changefrag(fragment);
        }
        if (Constant.manageFragmentCalling.equalsIgnoreCase(WISHLIST_FRAGMENT)) {
            // Toast.makeText(this, "WishList is visible..8", Toast.LENGTH_SHORT).show();
            Constant.manageFragmentCalling = ""; // Blank After Call Fragment
            title = "DiamondXE";
            Fragment fragment = new WishlistFragment();
            changefrag(fragment);
        } else if (Constant.manageFragmentCalling.equalsIgnoreCase(CART_FRAGMENT)) {
            Constant.manageFragmentCalling = "";  // Blank After Call Fragment
            title = "DiamondXE";
            Fragment fragment = new AddToCartListFragment();
            changefrag(fragment);
        } else if (Constant.manageFragmentCalling.equalsIgnoreCase(CATEGORY_FRAGMENT)) {
            Constant.manageFragmentCalling = "";  // Blank After Call Fragment
            title = "DiamondXE";
            Fragment fragment = new CategoryFragmentList();
            changefrag(fragment);
        } else if (Constant.manageFragmentCalling.equalsIgnoreCase(HOME_FRAGMENT)) {
            Constant.manageFragmentCalling = "";  // Blank After Call Fragment
            title = "DiamondXE";
            Fragment fragment = new HomeFragment();
            changefrag(fragment);
        } else if (Constant.manageFragmentCalling.equalsIgnoreCase(ACCOUNT_FRAGMENT)) {
            // Toast.makeText(this, "WishList is visible..8", Toast.LENGTH_SHORT).show();
            Constant.manageFragmentCalling = ""; // Blank After Call Fragment
            title = "DiamondXE";
            Fragment fragment = new AccountSectionFragment();
            changefrag(fragment);
        } else {
        }

        if (Constant.callHomeScreenOnResume.equalsIgnoreCase("yes")) {
            getCurrencyData();
            Constant.callHomeScreenOnResume = ""; // after Use blank.
        } else {
        }
        //  setNotificationCount();
    }

    public void setUserData() {
        if (!CommonUtility.getGlobalString(context, "profile_pic").equalsIgnoreCase("")) {
            Picasso.with(context)
                    .load("" + CommonUtility.getGlobalString(context, "profile_pic").replaceAll(" ", "%20"))
                    // .placeholder(R.mipmap.phl_circle_profile)
                    // .error(R.mipmap.phl_circle_profile)
                    .resize(150, 150)
                    .centerCrop()
                    .into(user_img);
        } else {
        }

        user_login = CommonUtility.getGlobalString(context, "user_login");

        // If User Login then show User Name and User Role
        if (!user_login.equalsIgnoreCase("")) {
            username_tv.setText(CommonUtility.getGlobalString(context, "login_first_name") + " " + CommonUtility.getGlobalString(context, "login_last_name"));
            login_type_role_tv.setText("" + CommonUtility.getGlobalString(context, "login_user_role"));
        } else {
            username_tv.setText(getResources().getString(R.string.hello_guest));
            login_type_role_tv.setText("--");
        }

    }

    private void setDataToAdepter() {
        user_login = CommonUtility.getGlobalString(context, "user_login");

        ArrayList<DrawerMenuModel> subMenu = new ArrayList<>();

        // Home Text
        DrawerMenuModel model = new DrawerMenuModel();
        model.setName(getResources().getString(R.string.navigation_home));
        model.setImage(R.drawable.home_nav);
        model.setImageSelected(R.drawable.home_nav);
        model.setSelected(true);
        menuList.add(model);

        // My Order Text
        DrawerMenuModel modelMyOrder = new DrawerMenuModel();
        modelMyOrder.setName(getResources().getString(R.string.my_orders));
        modelMyOrder.setImage(R.drawable.my_order);
        modelMyOrder.setImageSelected(R.drawable.my_order);
        modelMyOrder.setSelected(true);
        menuList.add(modelMyOrder);

        //-----------------------------Start Search Diamond------------------------------------------------------
        DrawerMenuModel model1 = new DrawerMenuModel();
        model1.setName(getResources().getString(R.string.navigation_search_diamond));
        model1.setImage(R.drawable.search_diamonds_nav);
        model1.setImageSelected(R.drawable.search_diamonds_nav);
        model1.setSelected(false);

        ArrayList<DrawerMenuModel> drawerSearchModels = new ArrayList<>();

        DrawerMenuModel modelSearch1 = new DrawerMenuModel();
        modelSearch1.setName(getResources().getString(R.string.navigation_natural_diamond));
        modelSearch1.setImage(R.drawable.natural_diamonds_nav_sub);
        modelSearch1.setImageSelected(R.drawable.natural_diamonds_nav_sub);
        modelSearch1.setSelected(false);
        drawerSearchModels.add(modelSearch1);

        DrawerMenuModel modelSearch2 = new DrawerMenuModel();
        modelSearch2.setName(getResources().getString(R.string.navigation_lab_grown_diamond));
        modelSearch2.setImage(R.drawable.lab_grown_diamonds_nav_sub);
        modelSearch2.setImageSelected(R.drawable.lab_grown_diamonds_nav_sub);
        modelSearch2.setSelected(false);
        drawerSearchModels.add(modelSearch2);

        DrawerMenuModel modelGemstone = new DrawerMenuModel();
        modelGemstone.setName(getResources().getString(R.string.gematones));
        modelGemstone.setImage(R.drawable.gemstone_navigation);
        modelGemstone.setImageSelected(R.drawable.gemstone_navigation);
        modelGemstone.setSelected(false);
        drawerSearchModels.add(modelGemstone);

        /*DrawerMenuModel modelSearch3 = new DrawerMenuModel();
        modelSearch3.setName(getResources().getString(R.string.navigation_fancy_diamond));
        *//*model31.setImage(R.mipmap.nav_tenant);
        model31.setImageSelected(R.mipmap.nav_tenant_active);*//*
        modelSearch3.setSelected(false);
        drawerSearchModels.add(modelSearch3);*/

        model1.setSubMenu(drawerSearchModels);
        menuList.add(model1);
        //-----------------------------End Search Diamond------------------------------------------------------

        DrawerMenuModel model3 = new DrawerMenuModel();
        model3.setName(getResources().getString(R.string.navigation_dxe_luxe));
        model3.setImage(R.drawable.luxe_navigation);
        model3.setImageSelected(R.drawable.luxe_navigation);
        model3.setSelected(false);
        menuList.add(model3);

        DrawerMenuModel model2 = new DrawerMenuModel();
        model2.setName(getResources().getString(R.string.navigation_create_diamond));
        model2.setImage(R.drawable.create_demand_nav);
        model2.setImageSelected(R.drawable.create_demand_nav);
        model2.setSelected(false);
        menuList.add(model2);


        DrawerMenuModel modelNo1 = new DrawerMenuModel();
        modelNo1.setName(getResources().getString(R.string.navigation_franchise_with_us));
        modelNo1.setImage(R.drawable.franchise_with_nav);
        modelNo1.setImageSelected(R.drawable.franchise_with_nav);
        modelNo1.setSelected(false);
        menuList.add(modelNo1);

        //-------------------------------------Start Jewellery-----------------------------------------------------
        /*DrawerMenuModel model3 = new DrawerMenuModel();
        model3.setName(getResources().getString(R.string.navigation_Jewellery));
        model3.setImage(R.drawable.jewellery_nav);
        model3.setImageSelected(R.drawable.jewellery_nav);
        model3.setSelected(false);

        ArrayList<DrawerMenuModel> drawerMenuJewelleryModels = new ArrayList<>();

        DrawerMenuModel modelRings = new DrawerMenuModel();
        modelRings.setName(getResources().getString(R.string.navigation_rings));
        modelRings.setImage(R.drawable.rings_nav_sub);
        modelRings.setImageSelected(R.drawable.rings_nav_sub);
        modelRings.setSelected(false);
        drawerMenuJewelleryModels.add(modelRings);

        DrawerMenuModel modelEarrings = new DrawerMenuModel();
        modelEarrings.setName(getResources().getString(R.string.navigation_earrings));
        modelEarrings.setImage(R.drawable.earrings_nav_sub);
        modelEarrings.setImageSelected(R.drawable.earrings_nav_sub);
        modelEarrings.setSelected(false);
        drawerMenuJewelleryModels.add(modelEarrings);

        DrawerMenuModel modelPendent = new DrawerMenuModel();
        modelPendent.setName(getResources().getString(R.string.navigation_pendent));
        modelPendent.setImage(R.drawable.pendant_nav_sub);
        modelPendent.setImageSelected(R.drawable.pendant_nav_sub);
        modelPendent.setSelected(false);
        drawerMenuJewelleryModels.add(modelPendent);

        DrawerMenuModel modelBracelets = new DrawerMenuModel();
        modelBracelets.setName(getResources().getString(R.string.navigation_bracelets));
        modelBracelets.setImage(R.drawable.bracelet_nav_sub);
        modelBracelets.setImageSelected(R.drawable.bracelet_nav_sub);
        modelBracelets.setSelected(false);
        drawerMenuJewelleryModels.add(modelBracelets);

        DrawerMenuModel modelBangles = new DrawerMenuModel();
        modelBangles.setName(getResources().getString(R.string.navigation_bangles));
        modelBangles.setImage(R.drawable.bangles_nav_sub);
        modelBangles.setImageSelected(R.drawable.bangles_nav_sub);
        modelBangles.setSelected(false);
        drawerMenuJewelleryModels.add(modelBangles);

        model3.setSubMenu(drawerMenuJewelleryModels);
        menuList.add(model3);*/
        //-------------------------------------End Jewellery-----------------------------------------------------

        /*DrawerMenuModel modelDxeLuxe = new DrawerMenuModel();
        modelDxeLuxe.setName(getResources().getString(R.string.navigation_dxe_luxe));
        modelDxeLuxe.setImage(R.drawable.dxe_luxe_nav);
        modelDxeLuxe.setImageSelected(R.drawable.dxe_luxe_nav);
        modelDxeLuxe.setSelected(false);
        menuList.add(modelDxeLuxe);*/

        /*DrawerMenuModel model5 = new DrawerMenuModel();
        model5.setName("Marketing");
        model5.setImage(R.mipmap.ic_notification);
        model5.setImageSelected(R.mipmap.ic_notification_circle);
        model5.setSelected(false);
        menuList.add(model5);*/

        DrawerMenuModel modelDiamondEducation = new DrawerMenuModel();
        modelDiamondEducation.setName(getResources().getString(R.string.navigation_diamond_education));
        modelDiamondEducation.setImage(R.drawable.diamond_education_nav);
        modelDiamondEducation.setImageSelected(R.drawable.diamond_education_nav);
        modelDiamondEducation.setSelected(false);
        menuList.add(modelDiamondEducation);

        //------------------------------------Start Explore Now-----------------------------------------------------------
        DrawerMenuModel modelExploreNow = new DrawerMenuModel();
        modelExploreNow.setName(getResources().getString(R.string.navigation_explore_now));
        modelExploreNow.setImage(R.drawable.explore_now_nav);
        modelExploreNow.setImageSelected(R.drawable.explore_now_nav);
        modelExploreNow.setSelected(false);

        ArrayList<DrawerMenuModel> drawerExploreModels = new ArrayList<>();

        DrawerMenuModel modelExploreJeweller = new DrawerMenuModel();
        modelExploreJeweller.setName(getResources().getString(R.string.navigation_jeweller));
        modelExploreJeweller.setImage(R.drawable.jeweller_nav_sub);
        modelExploreJeweller.setImageSelected(R.drawable.jeweller_nav_sub);
        modelExploreJeweller.setSelected(false);
        drawerExploreModels.add(modelExploreJeweller);

        DrawerMenuModel modelExploreSupplier = new DrawerMenuModel();
        modelExploreSupplier.setName(getResources().getString(R.string.navigation_supplier));
        modelExploreSupplier.setImage(R.drawable.supplier_nav_sub);
        modelExploreSupplier.setImageSelected(R.drawable.supplier_nav_sub);
        modelExploreSupplier.setSelected(false);
        drawerExploreModels.add(modelExploreSupplier);

        modelExploreNow.setSubMenu(drawerExploreModels);
        menuList.add(modelExploreNow);

        //------------------------------------End Explore Now-----------------------------------------------------------

        /*DrawerMenuModel model8 = new DrawerMenuModel();
        model8.setName("Online Payment");
        model8.setImage(R.mipmap.ic_notification);
        model8.setImageSelected(R.mipmap.ic_notification_circle);
        model8.setSelected(false);
        menuList.add(model8);*/

        DrawerMenuModel modelPriceCalculator = new DrawerMenuModel();
        modelPriceCalculator.setName(getResources().getString(R.string.navigationcalc));
        modelPriceCalculator.setImage(R.drawable.calc_logo);
        modelPriceCalculator.setImageSelected(R.drawable.calc_logo);
        modelPriceCalculator.setSelected(false);
        menuList.add(modelPriceCalculator);

        //--------------------------------------Start More------------------------------------------------------------
        DrawerMenuModel modelMore = new DrawerMenuModel();
        modelMore.setName(getResources().getString(R.string.navigation_more));
        modelMore.setImage(R.drawable.more_nav);
        modelMore.setImageSelected(R.drawable.more_nav);
        modelMore.setSelected(false);
        //menuList.add(modelMore);

        ArrayList<DrawerMenuModel> drawerMoreModels = new ArrayList<>();

        DrawerMenuModel modelAboutUs = new DrawerMenuModel();
        modelAboutUs.setName(getResources().getString(R.string.navigation_about_us));
        modelAboutUs.setImage(R.drawable.about_us_nav_sub);
        modelAboutUs.setImageSelected(R.drawable.about_us_nav_sub);
        modelAboutUs.setSelected(false);
        drawerMoreModels.add(modelAboutUs);

        DrawerMenuModel modelWhyUs = new DrawerMenuModel();
        modelWhyUs.setName(getResources().getString(R.string.navigation_why_us));
        modelWhyUs.setImage(R.drawable.why_us_nav_sub);
        modelWhyUs.setImageSelected(R.drawable.why_us_nav_sub);
        modelWhyUs.setSelected(false);
        drawerMoreModels.add(modelWhyUs);

        DrawerMenuModel modelBlogs = new DrawerMenuModel();
        modelBlogs.setName(getResources().getString(R.string.navigation_blogs));
        modelBlogs.setImage(R.drawable.blog_nav_sub);
        modelBlogs.setImageSelected(R.drawable.blog_nav_sub);
        modelBlogs.setSelected(false);
        drawerMoreModels.add(modelBlogs);

        DrawerMenuModel modelMediaGallery = new DrawerMenuModel();
        modelMediaGallery.setName(getResources().getString(R.string.navigation_media_gallery));
        modelMediaGallery.setImage(R.drawable.media_gallery_nav_sub);
        modelMediaGallery.setImageSelected(R.drawable.media_gallery_nav_sub);
        modelMediaGallery.setSelected(false);
        drawerMoreModels.add(modelMediaGallery);

        DrawerMenuModel modelSupport = new DrawerMenuModel();
        modelSupport.setName(getResources().getString(R.string.navigation_support));
        modelSupport.setImage(R.drawable.support_new);
        modelSupport.setImageSelected(R.drawable.support_new);
        modelSupport.setSelected(false);
        drawerMoreModels.add(modelSupport);

        DrawerMenuModel modelTermFaq = new DrawerMenuModel();
        modelTermFaq.setName(getResources().getString(R.string.navigation_faq));
        modelTermFaq.setImage(R.drawable.faq_);
        modelTermFaq.setImageSelected(R.drawable.faq_);
        modelTermFaq.setSelected(false);
        drawerMoreModels.add(modelTermFaq);

        DrawerMenuModel modelTermCondition = new DrawerMenuModel();
        modelTermCondition.setName(getResources().getString(R.string.navigation_terms_condition));
        modelTermCondition.setImage(R.drawable.term_condition_nav_sub);
        modelTermCondition.setImageSelected(R.drawable.term_condition_nav_sub);
        modelTermCondition.setSelected(false);
        drawerMoreModels.add(modelTermCondition);

        DrawerMenuModel modelPrivacyPolicy = new DrawerMenuModel();
        modelPrivacyPolicy.setName(getResources().getString(R.string.navigation_privacy_policy));
        modelPrivacyPolicy.setImage(R.drawable.privacy_policy_nav_sub);
        modelPrivacyPolicy.setImageSelected(R.drawable.privacy_policy_nav_sub);
        modelPrivacyPolicy.setSelected(false);
        drawerMoreModels.add(modelPrivacyPolicy);

        modelMore.setSubMenu(drawerMoreModels);
        menuList.add(modelMore);
        //--------------------------------------End Start More------------------------------------------------------------

        //------------------------------------Start Contact us-----------------------------------------------------------
        DrawerMenuModel modelContactUs = new DrawerMenuModel();
        modelContactUs.setName(getResources().getString(R.string.navigation_contact_us));
        modelContactUs.setImage(R.drawable.contact_us_new);
        modelContactUs.setImageSelected(R.drawable.contact_us_new);
        modelContactUs.setSelected(false);

        ArrayList<DrawerMenuModel> drawerContactUsModels = new ArrayList<>();

        DrawerMenuModel modelSupportEmail = new DrawerMenuModel();
        modelSupportEmail.setName(getResources().getString(R.string.navigation_support_email));
        modelSupportEmail.setImage(R.drawable.email_nav_sub);
        modelSupportEmail.setImageSelected(R.drawable.email_nav_sub);
        modelSupportEmail.setSelected(false);
        drawerContactUsModels.add(modelSupportEmail);

        DrawerMenuModel modelSupportNumber = new DrawerMenuModel();
        modelSupportNumber.setName(getResources().getString(R.string.navigation_support_number));
        modelSupportNumber.setImage(R.drawable.contact_us_nav);
        modelSupportNumber.setImageSelected(R.drawable.contact_us_nav);
        modelSupportNumber.setSelected(false);
        drawerContactUsModels.add(modelSupportNumber);

        modelContactUs.setSubMenu(drawerContactUsModels);
        menuList.add(modelContactUs);

        //------------------------------------End contact us-----------------------------------------------------------

        DrawerMenuModel modelRateUs = new DrawerMenuModel();
        modelRateUs.setName(getResources().getString(R.string.navigation_rate_us));
        modelRateUs.setImage(R.drawable.rate_us_nav);
        modelRateUs.setImageSelected(R.drawable.rate_us_nav);
        modelRateUs.setSelected(false);
        menuList.add(modelRateUs);

        if (user_login.equalsIgnoreCase("yes")) {
            DrawerMenuModel modelLogout = new DrawerMenuModel();
            modelLogout.setName(getResources().getString(R.string.navigation_logout));
            modelLogout.setImage(R.drawable.logout);
            modelLogout.setImageSelected(R.drawable.logout);
            modelLogout.setSelected(false);
            menuList.add(modelLogout);
        } else {
            DrawerMenuModel modelLogout = new DrawerMenuModel();
            modelLogout.setName(getResources().getString(R.string.navigation_login));
            modelLogout.setImage(R.drawable.logout);
            modelLogout.setImageSelected(R.drawable.logout);
            modelLogout.setSelected(false);
            menuList.add(modelLogout);
        }

        adapter = new DrawerItemCustomAdapter(context, menuList, recyclerview, this);
        recyclerview.setAdapter(adapter);
    }


    //For Open Fragment from Other Screens.
    public void changefrag(Fragment fragment) {
        if (title.equalsIgnoreCase("Home")) {
            //  Toast.makeText(context, "title : " + title, Toast.LENGTH_SHORT).show();
            selectedPosition = 0;
            adapter = new DrawerItemCustomAdapter(context, menuList, recyclerview, this);
            recyclerview.setAdapter(adapter);

        } else if (title.equalsIgnoreCase("Diamond1")) {
            selectedPosition = 1;
            adapter = new DrawerItemCustomAdapter(context, menuList, recyclerview, this);
            recyclerview.setAdapter(adapter);
        } else if (title.equalsIgnoreCase("Diamond2")) {

            selectedPosition = 2;
            adapter = new DrawerItemCustomAdapter(context, menuList, recyclerview, this);
            recyclerview.setAdapter(adapter);
        } else if (title.equalsIgnoreCase("Diamond3")) {
            selectedPosition = 3;
            adapter = new DrawerItemCustomAdapter(context, menuList, recyclerview, this);
            recyclerview.setAdapter(adapter);
        } else if (title.equalsIgnoreCase("Diamond4")) {

            selectedPosition = 4;
            adapter = new DrawerItemCustomAdapter(context, menuList, recyclerview, this);
            recyclerview.setAdapter(adapter);
        } else if (title.equalsIgnoreCase("Diamond5")) {

            selectedPosition = 5;
            adapter = new DrawerItemCustomAdapter(context, menuList, recyclerview, this);
            recyclerview.setAdapter(adapter);
        } else if (title.equalsIgnoreCase("Diamond6")) {

            selectedPosition = 6;
            adapter = new DrawerItemCustomAdapter(context, menuList, recyclerview, this);
            recyclerview.setAdapter(adapter);
        } else if (title.equalsIgnoreCase("Settings")) {

            selectedPosition = 7;
            adapter = new DrawerItemCustomAdapter(context, menuList, recyclerview, this);
            recyclerview.setAdapter(adapter);
        } else if (title.equalsIgnoreCase("Log Out")) {

            selectedPosition = 8;
            adapter = new DrawerItemCustomAdapter(context, menuList, recyclerview, this);
            recyclerview.setAdapter(adapter);
        } else {
        }

        if (fragment != null) {
            // title_tv.setText(title);
            // fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        drawer_layout.closeDrawer(GravityCompat.START);
    }


    //For Edit Profile Fragments :
    public void changeFragment(String type) {
        if (type.equalsIgnoreCase("account")) {
            title = "Edit Profile";
        }
    }


    public void showEnterPinCodeDialog(final Activity activity, final Context context) {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_layout_enter_pincode, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();

        final TextView title = dialogView.findViewById(R.id.title);
        final TextView submit_btn = dialogView.findViewById(R.id.submit_btn);
        final ImageView cross_img = dialogView.findViewById(R.id.cross_img);
        final EditText enter_pincode_et = dialogView.findViewById(R.id.enter_pincode_et);
        final LinearLayout locate_me_lin = dialogView.findViewById(R.id.locate_me_lin);

        getData();

        // Check Pincode is blank or not if blank show hint text.
        if (userPincode.equalsIgnoreCase("")) {
            enter_pincode_et.setHint(getResources().getString(R.string.enter_pin_code));
        } else {
            enter_pincode_et.setText("");
            // if City is blank sow only pincode
            if (userCity.equalsIgnoreCase("")) {
                enter_pincode_et.setText(userPincode);
            } else {
                enter_pincode_et.setText(userCity + ", " + userPincode);
            }
        }
        cross_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        locate_me_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getCurrnetPinCode();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (enter_pincode_et.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(activity, getResources().getString(R.string.please_enter_pincode), Toast.LENGTH_SHORT).show();
                } else {
                    if (!enter_pincode_et.getText().toString().equalsIgnoreCase("")) {
                        //pincode_tv.setText(getResources().getString(R.string.delivering_to) + " " + enter_pincode_et.getText().toString().trim());
                    } else {
                    }

                    userPincode = enter_pincode_et.getText().toString().trim();

                    CommonUtility.setGlobalString(context, "user_select_pincode", enter_pincode_et.getText().toString());
                    CommonUtility.setGlobalString(context, "user_select_pincode_city", "");

                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    void getCurrnetPinCode() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Log.e("---------Diamond--------- : ", "grantResults : " + grantResults.toString());
                // Permission denied
                // Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                showSettingsDialog();
            }
        }
    }

    private void showSettingsDialog() {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs location permission to function. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    getPincode(location.getLatitude(), location.getLongitude());
                }
            }
        });
    }

    private void getPincode(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                userPincode = addresses.get(0).getPostalCode();
                userCity = addresses.get(0).getLocality();

                CommonUtility.setGlobalString(context, "user_select_pincode", userPincode);
                CommonUtility.setGlobalString(context, "user_select_pincode_city", userCity);

                alertDialog.dismiss();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        String url = "";
        int id = view.getId();

        if (id == R.id.instagram_img) {
            url = CommonUtility.getGlobalString(context, "cmsInstagramLink");
            if (!url.equalsIgnoreCase("")) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(INSTAGRAM_URL));
                startActivity(intent);
            }
        } else if (id == R.id.facebook_img) {
            url = CommonUtility.getGlobalString(context, "cmsFacebookLink");
            if (!url.equalsIgnoreCase("")) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(FACEBOOK_URL));
                startActivity(intent);
            }
        } else if (id == R.id.linkdin_img) {
            url = CommonUtility.getGlobalString(context, "cmsLinkedinLink");
            if (!url.equalsIgnoreCase("")) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(LINKDIN_URL));
                startActivity(intent);
            }

        } else if (id == R.id.youtube_img) {
            url = CommonUtility.getGlobalString(context, "cmsYoutubeLink");
            if (!url.equalsIgnoreCase("")) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(YOUTUBE_URL));
                startActivity(intent);
            }

        }
    }

    public void editClick(View view) {
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {
        try {
            Log.v("diamond", "JSONObject" + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {

                case ApiConstants.LOGOUT_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1")) {

                        Toast.makeText(HomeScreenActivity.this, "" + message, Toast.LENGTH_SHORT).show();

                        CommonUtility.clear(HomeScreenActivity.this);
                        CommonUtility.setGlobalString(HomeScreenActivity.this, "user_id", "");
                        CommonUtility.setGlobalString(HomeScreenActivity.this, "mobile_auth_token", "");
                        CommonUtility.setGlobalString(HomeScreenActivity.this, "user_login", "");

                        Intent _login_intent = new Intent(HomeScreenActivity.this, HomeScreenActivity.class);
                        _login_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        _login_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(_login_intent);
                        overridePendingTransition(0, 0);
                        finish();
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("4")) {
                        logoutFromApp(context, message);
                    } else {
                        //  Toast.makeText(HomeScreenActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.GET_CURRENCY_RATES_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1")) {
                        Constant.cardCount = jsonObjectData.optString("cart_count");
                        Constant.wishListCount = jsonObjectData.optString("wishlist_count");

                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if (Constant.currencyArrayList.size() > 0) {
                            Constant.currencyArrayList.clear();
                        } else {
                        }

                        for (int i = 0; i < details.length(); i++) {
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
                            model.setCountryCode(CommonUtility.checkString(objectCodes.optString("country_code")));
                            model.setSelected(false);
                            Constant.currencyArrayList.add(model);

                        }

                        Log.e("getCountryCode  1339: ", ""+Constant.currencyArrayList.size());
                        // Loop through the list of currencies
                        for (int i = 0; i < Constant.currencyArrayList.size(); i++) {
                            String timeZoneId = "", timeZoneCountryCode = "";

                            // Get the default TimeZone ID
                            timeZoneId = TimeZone.getDefault().getID();

                            // Retrieve the country code corresponding to the current timezone
                            timeZoneCountryCode = TimeZoneCountryCodeMapper.getCountryCodeFromTimeZone(timeZoneId);

                            // Check if the country code from the timezone matches the country code in the currency list
                            if (Constant.currencyArrayList.get(i).getCountryCode().equalsIgnoreCase(timeZoneCountryCode)) {
                                // If a match is found, retrieve the currency details
                                selectedCurrencyCode = Constant.currencyArrayList.get(i).getCurrency();
                                selectedCurrencyValue = Constant.currencyArrayList.get(i).getValue();
                                selectedCurrencyDesc = Constant.currencyArrayList.get(i).getDesc();
                                selectedCurrencyImage = Constant.currencyArrayList.get(i).getImage();

                                // Store the selected currency details in global preferences
                                CommonUtility.setGlobalString(context, "selected_currency_value", Constant.currencyArrayList.get(i).getValue());
                                CommonUtility.setGlobalString(context, "selected_currency_code", Constant.currencyArrayList.get(i).getCurrency());
                                CommonUtility.setGlobalString(context, "selected_currency_desc", Constant.currencyArrayList.get(i).getDesc());
                                CommonUtility.setGlobalString(context, "selected_currency_image", Constant.currencyArrayList.get(i).getImage());

                                // Exit the loop once the currency has been found and stored
                                break;
                            } else {
                            }
                        }

                        getCurrencyData();

                        if (selectedCurrencyCode.equalsIgnoreCase("")) {
                            CommonUtility.setGlobalString(context, "selected_currency_code", INDIA_CURRENCY_CODE);
                        } else {
                        }

                        if (selectedCurrencyValue.equalsIgnoreCase("")) {
                            CommonUtility.setGlobalString(context, "selected_currency_value", INDIA_CURRENCY_VALUE);
                        } else {
                        }

                        if (selectedCurrencyDesc.equalsIgnoreCase("")) {
                            CommonUtility.setGlobalString(context, "selected_currency_desc", INDIA_CURRENCY_DESC);
                        } else {
                        }

                        if (selectedCurrencyImage.equalsIgnoreCase("")) {
                            CommonUtility.setGlobalString(context, "selected_currency_image", "https://buyer-uat.diamondxe.com/assets/img/flags/inr.png");
                        } else {
                        }

                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("0")) {
                        //Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("4")) {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.LUXE_USER_STATUS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1")) {
                        JSONObject details = jsonObjectData.getJSONObject("details");

                        Log.e("details", "...1581...**************......" + details);
                        int isLuxeMember = details.optInt("isLuxeMember");
                        Log.e("isLuxeMember", "...1581...**************......" + isLuxeMember);

                        // working comment for check
                        if (isLuxeMember == 0) {
                            startActivity(new Intent(context, DXELuxeRegisterActivity.class));
                            overridePendingTransition(0, 0);
                        } else {
                            Intent intent = new Intent(context, SearchDiamondsActivity.class);
                            intent.putExtra("intentvalue", "dxeluxe");
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            //start search activity
                        }

                         /*Intent intent = new Intent(context, SearchDiamondsActivity.class);
                        intent.putExtra("intentvalue","dxeluxe");
                        startActivity(intent);*/
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("0")) {
                        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("4")) {
                        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ApiConstants.GET_APP_CHECK_VERSION_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1")) {
                        JSONObject details = jsonObjectData.getJSONObject("details");
                        Log.e("details", "..1519......" + details);
                        String availableUpdate = details.optString("available_update");
                        String forceUpdate = details.optString("force");
                        Log.e("availableUpdate", "..1519......" + availableUpdate);
                        Log.e("forceUpdate", "..1519......" + forceUpdate);
                        if (availableUpdate.equalsIgnoreCase("1") && forceUpdate.equalsIgnoreCase("1")) {
                            Log.e("forceUpdate", "CALL.......1522......");
                            // Force available update
                            ///showDialog=1;

                            PlayStoreDialog(this, false);
                        } else if (availableUpdate.equalsIgnoreCase("1") && forceUpdate.equalsIgnoreCase("0")) {
                            Log.e("Update", "CALL.......1535......");
                            //showDialog=1;
                            //available update
                            if (sharedPrefHelper.shouldShowAppUpdateDialog()) {
                                PlayStoreDialog(this, true);
                            }

                        } else if (availableUpdate.equalsIgnoreCase("0") && forceUpdate.equalsIgnoreCase("0")) {
                            // App is up-to-date
                            Log.e("No Update", "CALL.......1549......");
                        }
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
    public void itemClick(int parantPosition, int chiledPosition, String action) {
        user_login = CommonUtility.getGlobalString(context, "user_login");
        selectedPosition = parantPosition;

        for (int i = 0; i < menuList.size(); i++) {
            menuList.get(i).setSelected(false);
        }
        menuList.get(selectedPosition).setSelected(true);

        // Home
        if (parantPosition == 0) {
            title = menuList.get(parantPosition).getName();
            //fragment = new HomeFragment();

            Constant.manageFragmentCalling = "";  // Blank After Call Fragment
            title = "DiamondXE";
            Fragment fragment = new HomeFragment();
            changefrag(fragment);
        }
        // My Order
        else if (parantPosition == 1) {
            title = menuList.get(parantPosition).getName();
            Constant.comeFrom = "";
            Constant.afterCancelOrderManageScreenCall = "";
            Constant.afterReturnOrderManageScreenCall = "";
            user_login = CommonUtility.getGlobalString(context, "user_login");
            if (user_login.equalsIgnoreCase("yes")) {
                Intent intent = new Intent(context, MyOrderListScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else {
                Intent intent = new Intent(context, LoginScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }

        }
        // Search Diamonds
        else if (parantPosition == 2) {
            if (chiledPosition == 0) {
                title = menuList.get(parantPosition).getSubMenu().get(0).getName();
                // fragment = new ContactsTenantFragment();
                Constant.searchTitleName = title;
                Constant.searchType = ApiConstants.NATURAL;
                Constant.filterClear = "";
                Intent intent = new Intent(context, SearchDiamondsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
            if (chiledPosition == 1) {
                title = menuList.get(parantPosition).getSubMenu().get(1).getName();
                Constant.searchTitleName = title;
                Constant.searchType = ApiConstants.LAB_GROWN;
                Constant.filterClear = "";
                Intent intent = new Intent(context, SearchDiamondsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
            if (chiledPosition == 2) {

                Constant.searchTitleName = ApiConstants.GEMESTONES;
                Constant.searchType = ApiConstants.NATURAL;
                Constant.filterClear = "";

                Intent intent = new Intent(context, GemstomeSearchActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

                /*Fragment fragment = new GemstoneFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
                /*Intent intent = new Intent(context, JewelleryScreenActivity.class);
                intent.putExtra("intentvalue","Gemston");
                startActivity(intent);
                overridePendingTransition(0,0);*/
            }
        }
        //
        else if (parantPosition == 3) {
            title = menuList.get(parantPosition).getName();
            Log.e("User", "Login h...**************...1498........." + title);
            Constant.searchTitleName = title;
            Constant.searchType = ApiConstants.DXE_LUXE;
            Constant.filterClear = "";
            if (!user_login.equalsIgnoreCase("")) {
                Log.e("User", "Login h...**************");
                /*Intent intent1 = new Intent(context, SearchDiamondsActivity.class);
                intent1.putExtra("intentvalue","dxeluxe");
                startActivity(intent1);*/
                getLuxeUserStatus(false);
                //startActivity(new Intent(context, DXELuexRegisterActivity.class));
            } else {
                //startActivity(new Intent(context, LoginScreenActivity.class));
                Intent intent = new Intent(context, DXELuxeRegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }

            //fragment = new LandLoardDashboardFragment();
        }

        // Create Demand
        else if (parantPosition == 4) {
            title = menuList.get(parantPosition).getName();
            /*Intent intent = new Intent(context, NovaaraFromActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);*/

            Intent intent = new Intent(context, CreateDemandHome.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            //fragment = new LandLoardDashboardFragment();
        }
        // Franchise with us
        else if (parantPosition == 5) {
            title = menuList.get(parantPosition).getName();
            /*Intent intent = new Intent(context, NovaaraFromActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);*/

            Intent intent = new Intent(context, NovaaraFromActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            //fragment = new LandLoardDashboardFragment();
        }
        // Diamonds Education
        else if (parantPosition == 6) {
            title = menuList.get(parantPosition).getName();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://diamondxe.com/diamond-education/5C-explained"));
            startActivity(intent);
            //fragment = new LandLoardDashboardFragment();
        }
        // Explore Now
        else if (parantPosition == 7) {
         /*   title = menuList.get(parantPosition).getName();
            // fragment = new MaintenanceRequestFragment();*/

            if (chiledPosition == 0) {
                title = menuList.get(parantPosition).getSubMenu().get(0).getName();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://diamondxe.com/for-dealer"));
                startActivity(intent);
                //   fragment = new ContactsTenantFragment();
            }
            if (chiledPosition == 1) {
                title = menuList.get(parantPosition).getSubMenu().get(1).getName();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://diamondxe.com/for-supplier"));
                startActivity(intent);
                //  fragment = new ContactsRealtorFragment();
            }

        }
        // Price Calculator
        else if (parantPosition == 8) {
            title = menuList.get(parantPosition).getName();
            Intent intent = new Intent(context, CalculatorActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            //  fragment = new SettingLandLoardFragment();
        }
        // More
        else if (parantPosition == 9) {
            if (chiledPosition == 0) {
                title = menuList.get(parantPosition).getSubMenu().get(0).getName();
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://diamondxe.com/about"));
                startActivity(intent);*/

                Intent intent1 = new Intent(context, WebViewNewActivity.class);
                intent1.putExtra("url", "https://diamondxe.com/about");
                intent1.putExtra("title", title);
                startActivity(intent1);
                //   fragment = new ContactsTenantFragment();
            }
            if (chiledPosition == 1) {
                title = menuList.get(parantPosition).getSubMenu().get(1).getName();
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://diamondxe.com/why-us"));
                startActivity(intent);*/

                Intent intent1 = new Intent(context, WebViewNewActivity.class);
                intent1.putExtra("url", "https://diamondxe.com/why-us");
                intent1.putExtra("title", title);
                startActivity(intent1);
                //  fragment = new ContactsRealtorFragment();
            }
            if (chiledPosition == 2) {
                title = menuList.get(parantPosition).getSubMenu().get(2).getName();
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://diamondxe.com/blogs"));
                startActivity(intent);*/

                Intent intent1 = new Intent(context, WebViewNewActivity.class);
                intent1.putExtra("url", "https://diamondxe.com/blogs");
                intent1.putExtra("title", title);
                startActivity(intent1);
                //   fragment = new ContactsTenantFragment();
            }
            if (chiledPosition == 3) {
                title = menuList.get(parantPosition).getSubMenu().get(3).getName();
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://diamondxe.com/media-gallery"));
                startActivity(intent);*/
                Intent intent1 = new Intent(context, WebViewNewActivity.class);
                intent1.putExtra("url", "https://diamondxe.com/media-gallery");
                intent1.putExtra("title", title);
                startActivity(intent1);
                //  fragment = new ContactsRealtorFragment();
            }
            if (chiledPosition == 4) {
                title = menuList.get(parantPosition).getSubMenu().get(4).getName();
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://diamondxe.com/contact"));
                startActivity(intent);*/

                Intent intent1 = new Intent(context, WebViewNewActivity.class);
                intent1.putExtra("url", "https://diamondxe.com/contact");
                intent1.putExtra("title", title);
                startActivity(intent1);
                //   fragment = new ContactsTenantFragment();
            }
            if (chiledPosition == 5) {
                title = menuList.get(parantPosition).getSubMenu().get(5).getName();
               /* Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://diamondxe.com/faq"));
                startActivity(intent);*/
                Intent intent1 = new Intent(context, WebViewNewActivity.class);
                intent1.putExtra("url", "https://diamondxe.com/faq");
                intent1.putExtra("title", title);
                startActivity(intent1);
                //  fragment = new ContactsRealtorFragment();
            }
            if (chiledPosition == 6) {
                title = menuList.get(parantPosition).getSubMenu().get(6).getName();
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://diamondxe.com/policy/terms-conditions"));
                startActivity(intent);*/
                Intent intent1 = new Intent(context, WebViewNewActivity.class);
                intent1.putExtra("url", "https://diamondxe.com/policy/terms-conditions");
                intent1.putExtra("title", title);
                startActivity(intent1);
                //  fragment = new ContactsRealtorFragment();
            }
            if (chiledPosition == 7) {
                title = menuList.get(parantPosition).getSubMenu().get(7).getName();
                Intent intent1 = new Intent(context, WebViewNewActivity.class);
                intent1.putExtra("url", "https://diamondxe.com/policy/privacy-policy");
                intent1.putExtra("title", title);
                startActivity(intent1);
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://diamondxe.com/policy/privacy-policy"));
                startActivity(intent);*/
            }
        }
        // Contact US
        else if (parantPosition == 10) {
            if (chiledPosition == 0) {
                title = menuList.get(parantPosition).getSubMenu().get(0).getName();
                //   fragment = new ContactsTenantFragment();
            }
            if (chiledPosition == 1) {
                title = menuList.get(parantPosition).getSubMenu().get(1).getName();
                //  fragment = new ContactsRealtorFragment();
            }
        }
        // Rate Us
        else if (parantPosition == 11) {
            title = menuList.get(parantPosition).getName();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://maps.app.goo.gl/TSUh7AtAfrJSLV8u5"));
            startActivity(intent);
            //  fragment = new SettingLandLoardFragment();
        }
        // Logout
        else if (parantPosition == 12) {

            if (user_login.equalsIgnoreCase("yes")) {
                logoutDialogPopup(context, context, getResources().getString(R.string.logout_message));
            } else {
                Intent intent = new Intent(HomeScreenActivity.this, LoginScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }

        }

        changefrag(fragment);

    }

    public void setListener(FragmentActionInterface fragmentActionInterface) {
        this.fragmentActionInterface = fragmentActionInterface;
    }

    public void getLuxeUserStatus(boolean showLoader) {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context)) {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);

            //urlParameter.put("user_id", CommonUtility.getGlobalString(getActivity(),"user_id"));
            //urlParameter.put("authToken", CommonUtility.getGlobalString(getActivity(),"mobile_auth_token"));

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(this, this, urlParameter, ApiConstants.LUXE_USER_Status, ApiConstants.LUXE_USER_STATUS_ID, showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    void logoutDialogPopup(final Activity activity, final Context context, String message) {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_layout_logout, null);
        dialogBuilder.setView(dialogView);
        final android.app.AlertDialog alertDialog = dialogBuilder.create();

        final TextView title = dialogView.findViewById(R.id.title);
        final TextView message1 = dialogView.findViewById(R.id.message);
        final TextView yes_tv = dialogView.findViewById(R.id.yes_tv);
        final TextView no_tv = dialogView.findViewById(R.id.no_tv);

        title.setText("" + context.getResources().getString(R.string.app_name));

        message1.setText("" + message);

        yes_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutAPI(false);
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

    public void onLogoutAPI(boolean showLoader) {
        //String uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String uuid = CommonUtility.getAndroidId(context);
        Log.e("-----uuid------", uuid);
        if (Utils.isNetworkAvailable(context)) {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("deviceId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context, this, urlParameter, ApiConstants.LOGOUT, ApiConstants.LOGOUT_ID, showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    private PopupWindow mDropdown = null;
    LayoutInflater mInflater;
    // Button pop;

    private PopupWindow initiateCurrencyPopupWindow() {
        try {
            // Get the overlay view
            View dimOverlay = findViewById(R.id.dim_overlay);

            if (mDropdown != null && mDropdown.isShowing()) {
                mDropdown.dismiss();
            }

            mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.custom_menu_currency, null);

            RecyclerView recyclerViewCurrency = layout.findViewById(R.id.recycler_view_currency);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerViewCurrency.setLayoutManager(layoutManager);

            currencyListAdapter = new CurrencyListAdapter(Constant.currencyArrayList, context, this);
            recyclerViewCurrency.setAdapter(currencyListAdapter);

            int fixedWidth = dpToPx(200); // Width in dp
            mDropdown = new PopupWindow(layout, fixedWidth, FrameLayout.LayoutParams.WRAP_CONTENT, true);

            // Show the PopupWindow
            if (flag_img != null) {
                mDropdown.showAsDropDown(flag_img, 5, -40);

                // Make the overlay visible
                dimOverlay.setVisibility(View.VISIBLE);
            } else {
                Log.e("PopupWindow", "flag_img is null");
            }

            // Dismiss listener for PopupWindow
            mDropdown.setOnDismissListener(() -> {
                // Hide the overlay when the popup is dismissed
                dimOverlay.setVisibility(View.GONE);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;
    }



    /*private PopupWindow initiateCurrencyPopupWindow() {
        try {
            View dimOverlay = findViewById(R.id.dim_overlay);
            if (mDropdown != null && mDropdown.isShowing()) {
                mDropdown.dismiss();
            }
            mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.custom_menu_currency, null);

            final RecyclerView recycler_view_currency = layout.findViewById(R.id.recycler_view_currency);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recycler_view_currency.setLayoutManager(layoutManager);

            currencyListAdapter = new CurrencyListAdapter(Constant.currencyArrayList,context,this);
            recycler_view_currency.setAdapter(currencyListAdapter);

            int fixedWidth = dpToPx(200); // Width in dp
            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            mDropdown = new PopupWindow(layout, fixedWidth, FrameLayout.LayoutParams.WRAP_CONTENT, true);

            *//*Drawable background = getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            mDropdown.setBackgroundDrawable(background);*//*

            // Ensure pop is not null before using it
            if (flag_img != null) {
                mDropdown.showAsDropDown(flag_img, 5, -40);
                // Make the overlay visible
                dimOverlay.setVisibility(View.VISIBLE);
            } else {
                Log.e("PopupWindow", "pop is null");
            }


            // Dismiss listener for PopupWindow
            mDropdown.setOnDismissListener(() -> dimOverlay.setVisibility(View.GONE));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;
    }*/

    public int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    public void itemClick(int position, String action) {
        if (action.equalsIgnoreCase("countryType")) {
            CountryListModel model = Constant.currencyArrayList.get(position);

            if (!model.getImage().equalsIgnoreCase("")) {
                Picasso.with(context)
                        .load(model.getImage())
                        .into(flag_img);
            }

            Log.e("1958","**...."+model.getValue());
            Log.e("1959","**....."+model.getCurrency());

            // selected_country_name.setText(model.getCurrency());
            // selected_country_desc.setText(model.getDesc());

            // Store the currently selected country as lastSelectedCountry
            //lastSelectedCountry = model;

            CommonUtility.setGlobalString(context, "selected_currency_value", model.getValue());
            CommonUtility.setGlobalString(context, "selected_currency_code", model.getCurrency());
            CommonUtility.setGlobalString(context, "selected_currency_desc", model.getDesc());
            CommonUtility.setGlobalString(context, "selected_currency_image", model.getImage());

            getCurrencySelectCountryCurrencySymbolAndValue(model.getCurrency(), model.getValue());

            if (mDropdown != null && mDropdown.isShowing()) {
                mDropdown.dismiss();
            } else {
                Log.e("PopupWindow", "mDropdown is null or not showing");
            }

            checkCurrentFragment();

            // Notify adapter of the change
            currencyListAdapter.notifyDataSetChanged();
        }
    }
}

