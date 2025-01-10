package com.diamondxe.Activity;

import static android.app.PendingIntent.getActivity;
import static com.diamondxe.ApiCalling.ApiConstants.ACCOUNT_FRAGMENT;
import static com.diamondxe.ApiCalling.ApiConstants.BUY_NOW;
import static com.diamondxe.ApiCalling.ApiConstants.CART;
import static com.diamondxe.ApiCalling.ApiConstants.CART_FRAGMENT;
import static com.diamondxe.ApiCalling.ApiConstants.CATEGORY_FRAGMENT;
import static com.diamondxe.ApiCalling.ApiConstants.HOME_FRAGMENT;
import static com.diamondxe.ApiCalling.ApiConstants.USER_BUYER;
import static com.diamondxe.ApiCalling.ApiConstants.USER_DEALER;
import static com.diamondxe.ApiCalling.ApiConstants.WISHLIST_FRAGMENT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.diamondxe.Activity.PlaceOrder.PlaceOrderScreenActivity;
import com.diamondxe.Adapter.DiamondCaratTypeListAdapter;
import com.diamondxe.Adapter.DiamondShapeImageListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.DiamondShapeImageModel;
import com.diamondxe.Beans.SearchResultTypeModel;
import com.diamondxe.Fragment.AccountSectionFragment;
import com.diamondxe.Interface.RecyclerInterface;

import android.Manifest;

import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.payment.paymentsdk.sharedclasses.sealed.a;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class DiamondDetailsActivity extends SuperActivity implements RecyclerInterface {
    private TextView supplier_id_tv, name_tv, item_type_tv, discount_tv, cut_grade_tv, polish_tv, symmetry_tv, fluorescence_intensity_tv, certificate_name_tv, sub_total_tv, dis_sub_total_tv,
            pincode_tv, select_image_tv, select_360_tv, select_certificate_tv, select_size_tv, type_tv, current_return_policy_tv, details_measurements_tv,
            details_depth_tv, details_table_tv, details_crown_height_tv, details_crown_angle_tv, details_pavilion_depth_tv, details_pavilion_angle_tv, details_shade_tv,
            details_condition_cult_tv, details_growth_type_tv, details_inclusion_tv, details_status_tv, details_location_tv, details_key_tv,
            details_remark_tv, details_report_tv, current_add_to_cart_tv, buy_now_tv, select_lbl, culet_id, recommanded_tv_lbl;
    private ImageView bottom_search_icon, back_img, drop_arrow_img, select_image_img, select_360_img, select_certificate_img, select_size_img,
            diamond_img, current_status_img, current_returnable_img, bottom_search_icon_cross;
    private CardView select_pin_code_card, add_to_cart_card_view, buy_now_card_view, top_img_card, search_circle_card, search_circle_card1_cross;
    private ScrollView scrollView;
    private RelativeLayout home_rel, category_rel, wishlist_rel, cart_rel, account_rel, curve_rel;
    private ImageView home_img, categories_img, wish_img, cart_img, account_img;
    private TextView home_tv, categories_tv, wish_tv, cart_tv, account_tv, cart_count_tv, wish_list_count_tv;
    private RelativeLayout diamond_details_rel, viewpager_layout, show_popup_rel, viewpager_buyer_layout;
    private LinearLayout call_for_enquiry, show_diamond_details_lin, select_img_lin, select_360_view_lin, select_certificate_lin, select_size_lin;
    public ArrayList<SearchResultTypeModel> recommandDiamondArrayList;
    DiamondCaratTypeListAdapter diamondCaratTypeListAdapter;
    DiamondShapeImageListAdapter shapeImageListAdapter;
    public ArrayList<DiamondShapeImageModel> shapeImageArrayList;
    public ArrayList<DiamondShapeImageModel> diamondShapeImageModelArrayList;
    public ArrayList<DiamondShapeImageModel> princessModelArrayList;
    public ArrayList<DiamondShapeImageModel> emeraldModelArrayList;
    public ArrayList<DiamondShapeImageModel> heartModelArrayList;
    public ArrayList<DiamondShapeImageModel> radiantModelArrayList;
    public ArrayList<DiamondShapeImageModel> ovalModelArrayList;
    public ArrayList<DiamondShapeImageModel> pearModelArrayList;
    public ArrayList<DiamondShapeImageModel> marquiseModelArrayList;
    public ArrayList<DiamondShapeImageModel> asscherModelArrayList;
    public ArrayList<DiamondShapeImageModel> cushionModelArrayList;
    ViewPager viewPager, viewPagerBuyer;
    TabLayout tabLayout, tabLayoutBuyer;
    RelativeLayout luex_tag;
    //int NUM_PAGES = 0, currentPage = 0;
    private boolean isArrowDown = false;
    private Activity activity;
    private Context context;
    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_CALL_PHONE = 2;
    private FusedLocationProviderClient fusedLocationClient;
    String selectedCurrencyValue = "", selectedCurrencyCode = "", selectedCurrencyDesc = "", selectedCurrencyImage = "";
    int lastPosition = 0;
    Handler handler1 = new Handler(Looper.getMainLooper());
    String certificate_number = "", addToCartWhereToCall = "", user_login = "", userRole = "";
    String ROUND = "Round";
    String PRINCESS = "Princess";
    String EMERALD = "Emerald";
    String PEAR = "Pear";
    String HEART = "Heart";
    String OVAL = "Oval";
    String MARQUISE = "Marquise";
    String RADIANT = "Radiant";
    String ASSCHER = "Asscher";
    String CUSHION = "Cushion";
    String shapeTypeSelected = "Round";
    String stock_id = "", item_name = "", supplier_id = "", certificate_no = "", category = "", certificate_name = "", cut_grade = "", shape = "",
            clarity = "", carat = "", color = "", growth_type = "", fluorescence_intensity = "", polish = "", symmetry = "", depth_perc = "", table_perc = "",
            discount_amout = "", length = "", width = "", depth = "", measurement = "", shade = "", luster = "", eye_clean = "", crown_angle = "", pavillion_angle = "", diamond_image = "", diamond_video = "", is_returnable = "",
            certificate_file = "", girdle_condition = "", culet = "", location = "", crown_height = "", pavillion_depth = "", inscription = "", key_to_symbols = "", report_comments = "", subtotal = "", is_cart = "", is_wishlist = "",
            avaliable_status = "", r_discount = "", supplier_comment = "", userPincode = "", userCity = "", stock_no = "";
    double coupondiscountperc, subtotalaftercoupondiscount;
    android.app.AlertDialog alertDialog;
    private RelativeLayout card_popup1;
    View translucent_background, size_view;
    String searchType = "";
    String activityValue = "";
    View cut_grade_view, polish_view, symmetry_view, fluorescence_view;

    TextView gravity_tv, weight_ratti_tv, weight_cart_tv, refractive_index_tv, exact_dimensions_tv,
            details_item_tv, treatment_tv, composition_tv,location_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = activity = this;
        Intent intent = getIntent();
        String intentValue = null;

        if (intent != null) {
            intentValue = intent.getStringExtra("intentvalue");
            activityValue = intent.getStringExtra("activityvalue");
        }
        Log.e("activityValue","..191..."+activityValue);
        if (activityValue != null && !activityValue.isEmpty()) {
            if (activityValue.equalsIgnoreCase("gemstone")) {
                setContentView(R.layout.gemstone_details_layout);
            }
            cut_grade_view = findViewById(R.id.cut_grade_view);
            symmetry_view = findViewById(R.id.symmetry_view);
            polish_view = findViewById(R.id.polish_view);
            fluorescence_view = findViewById(R.id.fluorescence_view);
            gravity_tv = findViewById(R.id.gravity_tv);
            weight_ratti_tv = findViewById(R.id.weight_ratti_tv);
            weight_cart_tv = findViewById(R.id.weight_cart_tv);
            refractive_index_tv = findViewById(R.id.refractive_index_tv);
            exact_dimensions_tv = findViewById(R.id.exact_dimensions_tv);
            details_item_tv = findViewById(R.id.details_item_tv);
            treatment_tv = findViewById(R.id.treatment_tv);
            composition_tv = findViewById(R.id.composition_tv);
            location_tv=findViewById(R.id.location_tv);
            /*================================================================*/
        } else {
            setContentView(R.layout.activity_diamond_details);

            select_size_lin = findViewById(R.id.select_size_lin);
            select_size_lin.setOnClickListener(this);
            select_size_tv = findViewById(R.id.select_size_tv);
            select_size_img = findViewById(R.id.select_size_img);
            select_size_tv.setTextColor(ContextCompat.getColor(context, R.color.light_gray1));
            select_size_img.setColorFilter(ContextCompat.getColor(context, R.color.light_gray1));
        }

        if (intentValue != null) {
            searchType = intentValue;
            Log.e("Search result", "Received intent value: " + intentValue);
        } else {
            Log.e("IntentValue", "No intent value received.");
        }
        recommandDiamondArrayList = new ArrayList<>();
        diamondShapeImageModelArrayList = new ArrayList<>();
        princessModelArrayList = new ArrayList<>();
        emeraldModelArrayList = new ArrayList<>();
        heartModelArrayList = new ArrayList<>();
        radiantModelArrayList = new ArrayList<>();
        ovalModelArrayList = new ArrayList<>();
        pearModelArrayList = new ArrayList<>();
        marquiseModelArrayList = new ArrayList<>();
        asscherModelArrayList = new ArrayList<>();
        cushionModelArrayList = new ArrayList<>();

        shapeImageArrayList = new ArrayList<>();

        card_popup1 = findViewById(R.id.card_popup1);

        getData();

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        scrollView = findViewById(R.id.scrollView);
        size_view = findViewById(R.id.size_view);

        diamond_img = findViewById(R.id.diamond_img);
        top_img_card = findViewById(R.id.top_img_card);
        luex_tag = findViewById(R.id.luex_tag);
        top_img_card.setOnClickListener(this);

        buy_now_card_view = findViewById(R.id.buy_now_card_view);
        buy_now_card_view.setOnClickListener(this);

        add_to_cart_card_view = findViewById(R.id.add_to_cart_card_view);
        add_to_cart_card_view.setOnClickListener(this);

        current_add_to_cart_tv = findViewById(R.id.current_add_to_cart_tv);
        buy_now_tv = findViewById(R.id.buy_now_tv);

        select_pin_code_card = findViewById(R.id.select_pin_code_card);
        select_pin_code_card.setOnClickListener(this);

        diamond_details_rel = findViewById(R.id.diamond_details_rel);
        diamond_details_rel.setOnClickListener(this);

        show_diamond_details_lin = findViewById(R.id.show_diamond_details_lin);
        drop_arrow_img = findViewById(R.id.drop_arrow_img);

        current_status_img = findViewById(R.id.current_status_img);
        current_returnable_img = findViewById(R.id.current_returnable_img);
        current_returnable_img.setOnClickListener(this);

        select_image_img = findViewById(R.id.select_image_img);
        select_360_img = findViewById(R.id.select_360_img);
        select_certificate_img = findViewById(R.id.select_certificate_img);


        type_tv = findViewById(R.id.type_tv);
        current_return_policy_tv = findViewById(R.id.current_return_policy_tv);

        select_image_tv = findViewById(R.id.select_image_tv);
        select_360_tv = findViewById(R.id.select_360_tv);
        select_certificate_tv = findViewById(R.id.select_certificate_tv);


        select_img_lin = findViewById(R.id.select_img_lin);
        select_img_lin.setOnClickListener(this);

        select_360_view_lin = findViewById(R.id.select_360_view_lin);
        select_360_view_lin.setOnClickListener(this);

        select_certificate_lin = findViewById(R.id.select_certificate_lin);
        select_certificate_lin.setOnClickListener(this);

        translucent_background = findViewById(R.id.translucent_background);
        translucent_background.setOnClickListener(this);

        home_rel = findViewById(R.id.home_rel);
        home_rel.setOnClickListener(this);

        category_rel = findViewById(R.id.category_rel);
        category_rel.setOnClickListener(this);

        wishlist_rel = findViewById(R.id.wishlist_rel);
        wishlist_rel.setOnClickListener(this);

        cart_rel = findViewById(R.id.cart_rel);
        cart_rel.setOnClickListener(this);

        account_rel = findViewById(R.id.account_rel);
        account_rel.setOnClickListener(this);

        call_for_enquiry = findViewById(R.id.call_for_enquiry);
        call_for_enquiry.setOnClickListener(this);

        home_img = findViewById(R.id.home_img);
        categories_img = findViewById(R.id.categories_img);
        wish_img = findViewById(R.id.wish_img);
        cart_img = findViewById(R.id.cart_img);
        account_img = findViewById(R.id.account_img);
        account_img.setOnClickListener(this);

        home_tv = findViewById(R.id.home_tv);
        categories_tv = findViewById(R.id.categories_tv);
        wish_tv = findViewById(R.id.wish_tv);
        cart_tv = findViewById(R.id.cart_tv);
        account_tv = findViewById(R.id.account_tv);
        cart_count_tv = findViewById(R.id.cart_count_tv);
        wish_list_count_tv = findViewById(R.id.wish_list_count_tv);
        account_tv.setOnClickListener(this);
        home_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        categories_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        wish_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        cart_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        account_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));

        home_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));
        categories_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));
        wish_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));
        cart_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));
        account_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));

        user_login = CommonUtility.getGlobalString(context, "user_login");
        // If User Login then  User Role Name
        if (!user_login.equalsIgnoreCase("")) {
            String role = CommonUtility.getGlobalString(context, "login_user_role");
            if (role.equalsIgnoreCase("BUYER")) {
                account_tv.setText(USER_BUYER);
            } else if (role.equalsIgnoreCase("DEALER")) {
                account_tv.setText(USER_DEALER);
            } else {
            }
        } else {
            account_tv.setText(getResources().getString(R.string.login));
        }

        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        tabLayout = findViewById(R.id.tab_layout);
        viewpager_layout = findViewById(R.id.viewpager_layout);

        viewPagerBuyer = findViewById(R.id.viewPagerBuyer);
        viewPagerBuyer.setOffscreenPageLimit(2);
        tabLayoutBuyer = findViewById(R.id.tab_layout_buyer);
        viewpager_buyer_layout = findViewById(R.id.viewpager_buyer_layout);

        pincode_tv = findViewById(R.id.pincode_tv);
        select_lbl = findViewById(R.id.select_lbl);

        if (userRole.equalsIgnoreCase("BUYER")) {
            viewpager_buyer_layout.setVisibility(View.VISIBLE);
            viewpager_layout.setVisibility(View.GONE);
        } else {
            viewpager_buyer_layout.setVisibility(View.GONE);
            viewpager_layout.setVisibility(View.VISIBLE);
        }

        Log.e("----userPincode----- : ", userPincode.toString());
        Log.e("----userPincode1----- : ", userCity.toString());
        // Check City Blank then show only PinCode Otherwise show City and PinCode.
        if (userCity.equalsIgnoreCase("")) {
            if (!userPincode.equalsIgnoreCase("")) {
                pincode_tv.setText(getResources().getString(R.string.delivering_to) + " " + userPincode);
            } else {
            }

        } else if (!userPincode.equalsIgnoreCase("")) {
            pincode_tv.setText(getResources().getString(R.string.delivering_to) + " " + userCity + ", " + userPincode);
        }

        if (pincode_tv.getText().toString().equalsIgnoreCase("")) {
            select_lbl.setText(getResources().getString(R.string.select));
        } else {
            select_lbl.setText(getResources().getString(R.string.change));
        }

        supplier_id_tv = findViewById(R.id.supplier_id_tv);
        name_tv = findViewById(R.id.name_tv);
        item_type_tv = findViewById(R.id.item_type_tv);
        discount_tv = findViewById(R.id.discount_tv);
        cut_grade_tv = findViewById(R.id.cut_grade_tv);
        polish_tv = findViewById(R.id.polish_tv);
        symmetry_tv = findViewById(R.id.symmetry_tv);
        fluorescence_intensity_tv = findViewById(R.id.fluorescence_intensity_tv);
        certificate_name_tv = findViewById(R.id.certificate_name_tv);
        sub_total_tv = findViewById(R.id.sub_total_tv);
        dis_sub_total_tv = findViewById(R.id.dis_sub_total_tv);
        details_measurements_tv = findViewById(R.id.details_measurements_tv);
        details_depth_tv = findViewById(R.id.details_depth_tv);
        details_table_tv = findViewById(R.id.details_table_tv);
        details_crown_height_tv = findViewById(R.id.details_crown_height_tv);
        details_crown_angle_tv = findViewById(R.id.details_crown_angle_tv);
        details_pavilion_depth_tv = findViewById(R.id.details_pavilion_depth_tv);
        details_pavilion_angle_tv = findViewById(R.id.details_pavilion_angle_tv);
        details_shade_tv = findViewById(R.id.details_shade_tv);
        details_condition_cult_tv = findViewById(R.id.details_condition_cult_tv);
        details_growth_type_tv = findViewById(R.id.details_growth_type_tv);
        details_inclusion_tv = findViewById(R.id.details_inclusion_tv);
        details_status_tv = findViewById(R.id.details_status_tv);
        details_location_tv = findViewById(R.id.details_location_tv);
        details_key_tv = findViewById(R.id.details_key_tv);
        details_remark_tv = findViewById(R.id.details_remark_tv);
        details_report_tv = findViewById(R.id.details_report_tv);

        culet_id = findViewById(R.id.culet_id);
        recommanded_tv_lbl = findViewById(R.id.recommanded_tv_lbl);

        bottom_search_icon = findViewById(R.id.bottom_search_icon);
        bottom_search_icon.setBackgroundResource(R.drawable.plus);

        bottom_search_icon_cross = findViewById(R.id.bottom_search_icon_cross);

        search_circle_card = findViewById(R.id.search_circle_card);
        search_circle_card.setOnClickListener(this);

        search_circle_card1_cross = findViewById(R.id.search_circle_card1_cross);
        search_circle_card1_cross.setOnClickListener(this);

        curve_rel = findViewById(R.id.curve_rel);
        show_popup_rel = findViewById(R.id.show_popup_rel);

        // Diamond Type Shape Image Data Set Method Call.
        shapeImageModelData();

        select_image_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));
        select_360_img.setColorFilter(ContextCompat.getColor(context, R.color.light_gray1));
        select_certificate_img.setColorFilter(ContextCompat.getColor(context, R.color.light_gray1));

        select_image_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
        select_360_tv.setTextColor(ContextCompat.getColor(context, R.color.light_gray1));
        select_certificate_tv.setTextColor(ContextCompat.getColor(context, R.color.light_gray1));


        type_tv.setText("Natural");

        CommonUtility.startZoomAnimation(bottom_search_icon);

        getCurrencyData();

        //gemstone
        Log.e("activityValue", "..443..." + activityValue);
        if (activityValue != null && !activityValue.isEmpty()) {
            if (activityValue.equalsIgnoreCase("gemstone")) {
                onBindDetailsGemStone(false);
            }


        } else {
            onBindDetails(false);
        }

        // diamondImage = "https://thnk18.azureedge.net/img/images/imaged/J429-230135/still.jpg";
        onBindSizePreviewAPI(true);

        manageDeviceBackButton();
        if (searchType.equals("dxeluxe")) {
            luex_tag.setVisibility(View.VISIBLE);
        } else {
            luex_tag.setVisibility(View.GONE);
        }
    }

    // Diamond Shape Image Data set Using Model.
    void shapeImageModelData() {
        DiamondShapeImageModel shapeImageModel1 = new DiamondShapeImageModel();
        shapeImageModel1.setDrawable(R.mipmap.round);
        shapeImageModel1.setAttribCode(ROUND);
        //shapeImageModel1.setSelected(true);
        shapeImageModel1.setSelected(false);
        shapeImageModel1.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageModel1);

        DiamondShapeImageModel shapeImageMode2 = new DiamondShapeImageModel();
        shapeImageMode2.setDrawable(R.mipmap.princess);
        shapeImageMode2.setAttribCode(PRINCESS);
        shapeImageMode2.setSelected(false);
        shapeImageMode2.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode2);

        DiamondShapeImageModel shapeImageMode3 = new DiamondShapeImageModel();
        shapeImageMode3.setDrawable(R.mipmap.emerald);
        shapeImageMode3.setAttribCode(EMERALD);
        shapeImageMode3.setSelected(false);
        shapeImageMode3.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode3);

        DiamondShapeImageModel shapeImageMode4 = new DiamondShapeImageModel();
        shapeImageMode4.setDrawable(R.mipmap.heart);
        shapeImageMode4.setAttribCode(HEART);
        shapeImageMode4.setSelected(false);
        shapeImageMode4.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode4);

        DiamondShapeImageModel shapeImageMode5 = new DiamondShapeImageModel();
        shapeImageMode5.setDrawable(R.mipmap.radiant);
        shapeImageMode5.setAttribCode(RADIANT);
        shapeImageMode5.setSelected(false);
        shapeImageMode5.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode5);

        DiamondShapeImageModel shapeImageMode6 = new DiamondShapeImageModel();
        shapeImageMode6.setDrawable(R.mipmap.oval);
        shapeImageMode6.setAttribCode(OVAL);
        shapeImageMode6.setSelected(false);
        shapeImageMode6.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode6);

        DiamondShapeImageModel shapeImageMode7 = new DiamondShapeImageModel();
        shapeImageMode7.setDrawable(R.mipmap.pear);
        shapeImageMode7.setAttribCode(PEAR);
        shapeImageMode7.setSelected(false);
        shapeImageMode7.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode7);

        DiamondShapeImageModel shapeImageMode8 = new DiamondShapeImageModel();
        shapeImageMode8.setDrawable(R.mipmap.marquise);
        shapeImageMode8.setAttribCode(MARQUISE);
        shapeImageMode8.setSelected(false);
        shapeImageMode8.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode8);

        DiamondShapeImageModel shapeImageMode9 = new DiamondShapeImageModel();
        shapeImageMode9.setDrawable(R.mipmap.asscher);
        shapeImageMode9.setAttribCode(ASSCHER);
        shapeImageMode9.setSelected(false);
        shapeImageMode9.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode9);

        DiamondShapeImageModel shapeImageMode10 = new DiamondShapeImageModel();
        shapeImageMode10.setDrawable(R.mipmap.cushion);
        shapeImageMode10.setAttribCode(CUSHION);
        shapeImageMode10.setSelected(false);
        shapeImageMode10.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode10);
    }

    // Get Currency Value Code and Image
    void getCurrencyData() {
        selectedCurrencyValue = CommonUtility.getGlobalString(context, "selected_currency_value");
        selectedCurrencyCode = CommonUtility.getGlobalString(context, "selected_currency_code");
        selectedCurrencyDesc = CommonUtility.getGlobalString(context, "selected_currency_desc");
        selectedCurrencyImage = CommonUtility.getGlobalString(context, "selected_currency_image");
    }

    void getData() {
        certificate_number = CommonUtility.getGlobalString(context, "certificate_number");

        userPincode = CommonUtility.getGlobalString(context, "user_select_pincode");
        userCity = CommonUtility.getGlobalString(context, "user_select_pincode_city");
        userRole = CommonUtility.getGlobalString(activity, "login_user_role");

        Log.e("----getCertificate_no111--- : ", certificate_number);
    }

    void getCurrnetPinCode() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            getLocation();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        user_login = CommonUtility.getGlobalString(activity, "user_login");
        userRole = CommonUtility.getGlobalString(activity, "login_user_role");
        showCardCount();
    }

    void showCardCount() {
        if (Constant.cardCount.equalsIgnoreCase("") || Constant.cardCount.equalsIgnoreCase("0")) {
            cart_count_tv.setVisibility(View.GONE);
        } else {
            cart_count_tv.setText(Constant.cardCount);
            cart_count_tv.setVisibility(View.VISIBLE);
        }

        if (Constant.wishListCount.equalsIgnoreCase("") || Constant.wishListCount.equalsIgnoreCase("0")) {
            wish_list_count_tv.setVisibility(View.GONE);
        } else {
            wish_list_count_tv.setText(Constant.wishListCount);
            wish_list_count_tv.setVisibility(View.VISIBLE);
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    //Log.e("---------Diamond--------- : ", "grantResults : " + grantResults.toString());
                    // Permission denied
                    // Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                    showSettingsDialog("This app needs location permission to function. You can grant them in app settings.");
                }
                break;
            case REQUEST_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall();
                } else {
                    showSettingsDialog("This app needs phone calls permission to function. You can grant them in app settings.");
                }
                break;
            // Handle other request codes if necessary
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void showSettingsDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        //builder.setMessage("This app needs location permission to function. You can grant them in app settings.");
        builder.setMessage(message);
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

                pincode_tv.setText(getResources().getString(R.string.delivering_to) + " " + userCity + ", " + userPincode);

                if (pincode_tv.getText().toString().equalsIgnoreCase("")) {
                    select_lbl.setText(getResources().getString(R.string.select));
                } else {
                    select_lbl.setText(getResources().getString(R.string.change));
                }

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
        int id = view.getId();
        user_login = CommonUtility.getGlobalString(activity, "user_login");
        if (id == R.id.back_img) {
            Utils.hideKeyboard(activity);
            if (Constant.manageClickEventForRedirection.equalsIgnoreCase("placeOrder")) {
                gotoHomeRedirection();
            } else {
                finish();
            }
        } else if (id == R.id.top_img_card) {
            Utils.hideKeyboard(activity);
            if (!diamond_image.equalsIgnoreCase("")) {
                showDiamondImageWebViewPopup(activity, context, diamond_image);
            }
        } else if (id == R.id.select_pin_code_card) {
            Utils.hideKeyboard(activity);
            showEnterPinCodeDialog(activity, context);
        } else if (id == R.id.diamond_details_rel) {
            if (isArrowDown) {
                drop_arrow_img.setImageResource(R.drawable.down);
                show_diamond_details_lin.setVisibility(View.GONE);
            } else {
                drop_arrow_img.setImageResource(R.drawable.up);
                show_diamond_details_lin.setVisibility(View.VISIBLE);
            }
            isArrowDown = !isArrowDown;

        } else if (id == R.id.buy_now_card_view) {
            if (!user_login.equalsIgnoreCase("")) {
                Constant.showRadioButtonForSelectAddress = "yes";// Under Address List Radio Button Visible
                Constant.orderType = BUY_NOW; // Order Type
                Constant.certificateNumber = certificate_number; // certificate Number
                Constant.manageShippingBillingAddressSelection = "";
                Constant.manageBillingByAddressAddUpdate = "";
                Constant.manageShippingByAddressAddUpdate = "";
                intent = new Intent(activity, PlaceOrderScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            } else {
                Constant.manageClickEventForRedirection = "placeOrder";
                intent = new Intent(activity, LoginScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        } else if (id == R.id.add_to_cart_card_view) {
            if (!is_cart.equalsIgnoreCase("1")) {
                addToCartWhereToCall = "detailsScreen";
                onAddToCartAPI(false, certificate_number);
            } else {
                Constant.manageClickEventForRedirection = "";
                intent = new Intent(activity, MyCardListScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        } else if (id == R.id.current_returnable_img) {
            current_return_policy_tv.setVisibility(View.VISIBLE);
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    current_return_policy_tv.setVisibility(View.GONE);
                }
            }, 2000);
        } else if (id == R.id.select_img_lin) {
            select_image_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));
            select_360_img.setColorFilter(ContextCompat.getColor(context, R.color.dot_gray));
            select_certificate_img.setColorFilter(ContextCompat.getColor(context, R.color.dot_gray));
            if (activityValue == null || activityValue.isEmpty()) {
                int grayColor = ContextCompat.getColor(context, R.color.dot_gray);
                select_size_img.setColorFilter(grayColor);
                select_size_tv.setTextColor(grayColor);
            }


            select_image_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
            select_360_tv.setTextColor(ContextCompat.getColor(context, R.color.dot_gray));
            select_certificate_tv.setTextColor(ContextCompat.getColor(context, R.color.dot_gray));


            Log.e("diamond_image", "...838..............." + diamond_image);
            //   diamondImage = "https://thnk18.azureedge.net/img/images/imaged/J429-230135/still.jpg";
            showDiamondImageWebViewPopup(activity, context, diamond_image);

        } else if (id == R.id.select_360_view_lin) {

            select_image_img.setColorFilter(ContextCompat.getColor(context, R.color.dot_gray));
            select_360_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));
            select_certificate_img.setColorFilter(ContextCompat.getColor(context, R.color.dot_gray));
            //  select_size_img.setColorFilter(ContextCompat.getColor(context, R.color.dot_gray));
            if (activityValue == null || activityValue.isEmpty()) {
                int grayColor = ContextCompat.getColor(context, R.color.dot_gray);
                select_size_img.setColorFilter(grayColor);
                select_size_tv.setTextColor(grayColor);
            }

            select_image_tv.setTextColor(ContextCompat.getColor(context, R.color.dot_gray));
            select_360_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
            select_certificate_tv.setTextColor(ContextCompat.getColor(context, R.color.dot_gray));
            //  select_size_tv.setTextColor(ContextCompat.getColor(context, R.color.dot_gray));

            //  diamondImage = "https://thnk18.azureedge.net/img/images/Vision360.html?d=J429-230135";
            showDiamondVideoWebViewPopup(activity, context, diamond_video);
        } else if (id == R.id.select_certificate_lin) {
            user_login = CommonUtility.getGlobalString(activity, "user_login");

            select_image_img.setColorFilter(ContextCompat.getColor(context, R.color.dot_gray));
            select_360_img.setColorFilter(ContextCompat.getColor(context, R.color.dot_gray));
            select_certificate_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));
            if (activityValue == null || activityValue.isEmpty()) {
                int grayColor = ContextCompat.getColor(context, R.color.dot_gray);
                select_size_img.setColorFilter(grayColor);
                select_size_tv.setTextColor(grayColor);
            }

            //select_size_img.setColorFilter(ContextCompat.getColor(context, R.color.dot_gray));

            select_image_tv.setTextColor(ContextCompat.getColor(context, R.color.dot_gray));
            select_360_tv.setTextColor(ContextCompat.getColor(context, R.color.dot_gray));
            select_certificate_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
            //select_size_tv.setTextColor(ContextCompat.getColor(context, R.color.dot_gray));

            if (user_login.equalsIgnoreCase("yes")) {
                CommonUtility.setGlobalString(activity, "certificate_file_url", certificate_file);
                if (!certificate_file.equalsIgnoreCase("")) {
                    intent = new Intent(context, CertificateWebViewActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    //showDiamondCertificateWebViewPopup(activity, context, certificate_file);
                    //showDiamondCertificateWebViewPopup(activity, context, "https://www.igi.org/verify-your-report/?r=464120312");
                } else {
                    intent = new Intent(context, CertificateWebViewActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    //showDiamondCertificateWebViewPopup(activity, context, "");
                }
            } else {
                intent = new Intent(context, LoginScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }

        } else if (id == R.id.select_size_lin) {
            select_image_img.setColorFilter(ContextCompat.getColor(context, R.color.dot_gray));
            select_360_img.setColorFilter(ContextCompat.getColor(context, R.color.dot_gray));
            select_certificate_img.setColorFilter(ContextCompat.getColor(context, R.color.dot_gray));
            //select_size_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));

            if (activityValue == null || activityValue.isEmpty()) {
                int grayColor = ContextCompat.getColor(context, R.color.dot_gray);
                select_size_img.setColorFilter(grayColor);
                select_size_tv.setTextColor(grayColor);
            }

            select_image_tv.setTextColor(ContextCompat.getColor(context, R.color.dot_gray));
            select_360_tv.setTextColor(ContextCompat.getColor(context, R.color.dot_gray));
            select_certificate_tv.setTextColor(ContextCompat.getColor(context, R.color.dot_gray));
            //select_size_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));

            if (shape.equalsIgnoreCase("")) {
                shapeTypeSelected = "Round";
            } else {
                shapeTypeSelected = shape;
            }

            //shapeTypeSelected="Round";
            showDiamondShapePopup(activity, context);
        } else if (id == R.id.search_circle_card) {
            translucent_background.setVisibility(View.VISIBLE); // Activity Transparency Visible
            bottomBarClickableFalse();// When Transparency Show Click False

            bottom_search_icon.setImageResource(R.drawable.cross);
            bottom_search_icon.setColorFilter(ContextCompat.getColor(context, R.color.white));
            bottom_search_icon.animate().rotation(45).setDuration(300).start();

            card_popup1.setVisibility(View.VISIBLE); // Option Popup Show
            curve_rel.setVisibility(View.VISIBLE); // Bottom Cut Center Position Layout show
            search_circle_card.setVisibility(View.GONE); // Bottom Plus Icon Hide
            search_circle_card1_cross.setVisibility(View.VISIBLE); // Bottom Cross Icon show
            show_popup_rel.setVisibility(View.VISIBLE); // Bottom Cross Icon show

            Animation popup1Animation = createPopupAnimation();
            card_popup1.startAnimation(popup1Animation);
        } else if (id == R.id.search_circle_card1_cross) {
            bottom_search_icon.setImageResource(R.drawable.plus);
            translucent_background.setVisibility(View.GONE); // Activity Transparency Gone
            bottomBarClickableTrue();// When Transparency Hide Click True

            bottom_search_icon.animate().rotation(0).setDuration(300).start();

            // Hide popups
            card_popup1.setVisibility(View.GONE); // Option Popup Hide
            curve_rel.setVisibility(View.GONE); // Bottom Cut Center Position Layout Hide
            search_circle_card1_cross.setVisibility(View.GONE); // Bottom Cross Icon Hide
            search_circle_card.setVisibility(View.VISIBLE); // Bottom Plus Icon Show
            show_popup_rel.setVisibility(View.GONE); // Bottom Plus Icon Show
        } else if (id == R.id.home_rel) {
            Constant.manageFragmentCalling = HOME_FRAGMENT;
            intent = new Intent(activity, HomeScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.category_rel) {
            Constant.manageFragmentCalling = CATEGORY_FRAGMENT;
            intent = new Intent(activity, HomeScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.wishlist_rel) {
            Constant.manageFragmentCalling = WISHLIST_FRAGMENT;
            intent = new Intent(activity, HomeScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.cart_rel) {
            Constant.manageFragmentCalling = CART_FRAGMENT;
            intent = new Intent(activity, HomeScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.account_rel) {
            user_login = CommonUtility.getGlobalString(activity, "user_login");
            if (user_login.equalsIgnoreCase("yes")) {
                /*intent = new Intent(context, AccountSectionActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);*/
                Constant.manageFragmentCalling = ACCOUNT_FRAGMENT;
                intent = new Intent(activity, HomeScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
              /* Fragment fragment = new AccountSectionFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
            } else {
                intent = new Intent(context, LoginScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        } else if (id == R.id.account_img) {
            user_login = CommonUtility.getGlobalString(activity, "user_login");
            if (user_login.equalsIgnoreCase("yes")) {
                /*intent = new Intent(context, AccountSectionActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);*/
                Constant.manageFragmentCalling = ACCOUNT_FRAGMENT;
                intent = new Intent(activity, HomeScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
              /* Fragment fragment = new AccountSectionFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
            } else {
                intent = new Intent(context, LoginScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        } else if (id == R.id.account_tv) {
            user_login = CommonUtility.getGlobalString(activity, "user_login");
            if (user_login.equalsIgnoreCase("yes")) {
                /*intent = new Intent(context, AccountSectionActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);*/
                Constant.manageFragmentCalling = ACCOUNT_FRAGMENT;
                intent = new Intent(activity, HomeScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
              /* Fragment fragment = new AccountSectionFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
            } else {
                intent = new Intent(context, LoginScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        } else if (id == R.id.translucent_background) {

            translucent_background.setVisibility(View.GONE); // Activity Transparency Gone
            bottomBarClickableTrue();// When Transparency Hide Click True

            bottom_search_icon.setImageResource(R.drawable.plus);
            bottom_search_icon.animate().rotation(0).setDuration(300).start();

            // Hide popups
            card_popup1.setVisibility(View.GONE); // Option Popup Hide
            curve_rel.setVisibility(View.GONE); // Bottom Cut Center Position Layout Hide
            search_circle_card1_cross.setVisibility(View.GONE); // Bottom Cross Icon Hide
            search_circle_card.setVisibility(View.VISIBLE); // Bottom Plus Icon Show
            show_popup_rel.setVisibility(View.GONE); // Bottom Plus Icon Show
        } else if (id == R.id.call_for_enquiry) {
            Intent intentcall = new Intent(Intent.ACTION_DIAL);
            intentcall.setData(Uri.parse("tel:9892003399"));
            startActivity(intentcall);
        }
    }

    // This function ensures that all bottom bar elements (home, category, wishlist, cart, account) are both
    // disabled and non-clickable, preventing users from interacting with these elements.
    void bottomBarClickableFalse() {
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
    void bottomBarClickableTrue() {
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

    void manageDeviceBackButton() {
        // Handle Device Back Button code.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (Constant.manageClickEventForRedirection.equalsIgnoreCase("placeOrder")) {
                    gotoHomeRedirection();
                } else {
                    finish();
                }
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    // User Come For Login Screen to Redirection Diamond Screen Then Work This Condition.
    void gotoHomeRedirection() {
        if (Constant.manageClickEventForRedirection.equalsIgnoreCase("placeOrder")) {
            Constant.manageClickEventForRedirection = "";
            Log.e("manageClickEventForRedirection : ", Constant.manageClickEventForRedirection);
            Intent intent = new Intent(context, HomeScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else {
        }
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

    final Handler handler = new Handler();
    Timer swipeTimer = new Timer();
    Runnable Update;
    int currentPage = 0;
    int NUM_PAGES = 0;


    public void setpager() {
        // Check Login USer Type Like Buyer and Dealer and Set Condition and Layout.
        if (userRole.equalsIgnoreCase("BUYER")) {
            viewPagerBuyer.setAdapter(new MyPagerAdapter(activity, recommandDiamondArrayList));
            tabLayoutBuyer.setupWithViewPager(viewPagerBuyer, true);
            final float density = getResources().getDisplayMetrics().density;
            // Check Banner Image View Visible and Gone Condition Using Array List Size.
            if (recommandDiamondArrayList != null && recommandDiamondArrayList.size() >= 1) {
                viewpager_buyer_layout.setVisibility(View.VISIBLE);

                if (recommandDiamondArrayList.size() > 1) {
                    tabLayoutBuyer.setVisibility(View.VISIBLE);
                } else {
                    tabLayoutBuyer.setVisibility(View.VISIBLE);
                }
            } else {
                viewpager_buyer_layout.setVisibility(View.GONE);
                tabLayoutBuyer.setVisibility(View.VISIBLE);
            }

            viewpager_layout.setVisibility(View.GONE);
            NUM_PAGES = recommandDiamondArrayList.size();
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
                            viewPagerBuyer.setCurrentItem(currentPage++, true);
                        }
                    }
                };
                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 8000, 8000);
            }
            viewPagerBuyer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    // Optional implementation
                }

                @Override
                public void onPageSelected(int position) {
                    // Highlight the selected tab
                    tabLayoutBuyer.setScrollPosition(position, 0f, true);
                    // Update currentPage to the new position
                    currentPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    // Optional implementation
                }
            });

        } else {

            viewPager.setAdapter(new MyPagerAdapter(activity, recommandDiamondArrayList));
            tabLayout.setupWithViewPager(viewPager, true);
            final float density = getResources().getDisplayMetrics().density;
            // Check Banner Image View Visible and Gone Condition Using Array List Size.
            if (recommandDiamondArrayList != null && recommandDiamondArrayList.size() >= 1) {
                viewpager_layout.setVisibility(View.VISIBLE);

                if (recommandDiamondArrayList.size() > 1) {
                    tabLayout.setVisibility(View.VISIBLE);
                } else {
                    tabLayout.setVisibility(View.VISIBLE);
                }
            } else {
                viewpager_layout.setVisibility(View.GONE);
                tabLayout.setVisibility(View.VISIBLE);
            }

            viewpager_buyer_layout.setVisibility(View.GONE);
            NUM_PAGES = recommandDiamondArrayList.size();
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

                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 8000, 8000);
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

    }

    // Change for new condition
    /*public void setpager() {
        // Check Login USer Type Like Buyer and Dealer and Set Condition and Layout.
        if(userRole.equalsIgnoreCase("DEALER"))
        {
            viewPager.setAdapter(new MyPagerAdapter(activity, recommandDiamondArrayList));
            tabLayout.setupWithViewPager(viewPager, true);
            final float density = getResources().getDisplayMetrics().density;
            // Check Banner Image View Visible and Gone Condition Using Array List Size.
            if(recommandDiamondArrayList!=null && recommandDiamondArrayList.size()>=1)
            {
                viewpager_layout.setVisibility(View.VISIBLE);

                if(recommandDiamondArrayList.size()>1){
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

            viewpager_buyer_layout.setVisibility(View.GONE);
            NUM_PAGES = recommandDiamondArrayList.size();
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

                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 8000, 8000);
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
        else{
            viewPagerBuyer.setAdapter(new MyPagerAdapter(activity, recommandDiamondArrayList));
            tabLayoutBuyer.setupWithViewPager(viewPagerBuyer, true);
            final float density = getResources().getDisplayMetrics().density;
            // Check Banner Image View Visible and Gone Condition Using Array List Size.
            if(recommandDiamondArrayList!=null && recommandDiamondArrayList.size()>=1)
            {
                viewpager_buyer_layout.setVisibility(View.VISIBLE);

                if(recommandDiamondArrayList.size()>1){
                    tabLayoutBuyer.setVisibility(View.VISIBLE);
                }else {
                    tabLayoutBuyer.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                viewpager_buyer_layout.setVisibility(View.GONE);
                tabLayoutBuyer.setVisibility(View.VISIBLE);
            }

            viewpager_layout.setVisibility(View.GONE);
            NUM_PAGES = recommandDiamondArrayList.size();
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
                            viewPagerBuyer.setCurrentItem(currentPage++, true);
                        }
                    }
                };
                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 8000, 8000);
            }
            viewPagerBuyer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    // Optional implementation
                }

                @Override
                public void onPageSelected(int position) {
                    // Highlight the selected tab
                    tabLayoutBuyer.setScrollPosition(position, 0f, true);
                    // Update currentPage to the new position
                    currentPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    // Optional implementation
                }
            });
        }

    }*/

    private class MyPagerAdapter extends PagerAdapter {

        ArrayList<SearchResultTypeModel> list;
        LayoutInflater inflater;

        public MyPagerAdapter(Context context, ArrayList<SearchResultTypeModel> list) {
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
            RelativeLayout luex_tag;
            ImageView pagerImg, status_img, returnable_img, add_to_favt_img;
            CardView root_layout;
            TextView supplier_id_tv_pager, name_tv_Pager, item_type_tv, cut_grade_tv, polish_tv, symmetry_tv, fluorescence_intensity_tv, certificate_name_tv, discount_tv, return_policy_tv,
                    table_perc_tv, depth_perc, measurement_tv, add_to_cart_tv, sub_total_tv, dis_sub_total_tv, diamond_type;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View itemView;

            // Check Login USer Type Like Buyer and Dealer and Set Condition and Layout.
            Log.e("userRole", ".....userRole......56......" + userRole);
            if (userRole.equalsIgnoreCase("DEALER")) {
                itemView = inflater.inflate(R.layout.row_recommand_diamond_list, container, false);

            } else if (userRole.equalsIgnoreCase("BUYER")) {
                itemView = inflater.inflate(R.layout.row_recommand_diamond_list_for_buyer, container, false);
            } else {
                itemView = inflater.inflate(R.layout.row_recommand_diamond_list, container, false);
            }
            luex_tag = (RelativeLayout) itemView.findViewById(R.id.luex_tag);
            pagerImg = (ImageView) itemView.findViewById(R.id.image_view);
            status_img = (ImageView) itemView.findViewById(R.id.status_img);
            returnable_img = (ImageView) itemView.findViewById(R.id.returnable_img);
            add_to_favt_img = (ImageView) itemView.findViewById(R.id.add_to_favt_img);

            supplier_id_tv_pager = itemView.findViewById(R.id.supplier_id_tv);

            root_layout = itemView.findViewById(R.id.root_layout);

            name_tv_Pager = itemView.findViewById(R.id.name_tv);
            item_type_tv = itemView.findViewById(R.id.item_type_tv);
            cut_grade_tv = itemView.findViewById(R.id.cut_grade_tv);
            polish_tv = itemView.findViewById(R.id.polish_tv);
            symmetry_tv = itemView.findViewById(R.id.symmetry_tv);
            fluorescence_intensity_tv = itemView.findViewById(R.id.fluorescence_intensity_tv);
            certificate_name_tv = itemView.findViewById(R.id.certificate_name_tv);
            discount_tv = itemView.findViewById(R.id.discount_tv);
            return_policy_tv = itemView.findViewById(R.id.return_policy_tv);
            table_perc_tv = itemView.findViewById(R.id.table_perc_tv);
            depth_perc = itemView.findViewById(R.id.depth_perc);
            measurement_tv = itemView.findViewById(R.id.measurement_tv);
            add_to_cart_tv = itemView.findViewById(R.id.add_to_cart_tv);
            sub_total_tv = itemView.findViewById(R.id.sub_total_tv);
            dis_sub_total_tv = itemView.findViewById(R.id.dis_sub_total_tv);
            diamond_type = itemView.findViewById(R.id.diamond_type);

            if (!list.get(position).getDiamond_image().equalsIgnoreCase("")) {
                Picasso.with(context)
                        .load(list.get(position).getDiamond_image())
                        .placeholder(R.mipmap.phl_diamond)
                        .error(R.mipmap.phl_diamond)
                        .into(pagerImg);
            } else {
                Picasso.with(context)
                        .load(R.mipmap.phl_diamond)
                        .placeholder(R.mipmap.phl_diamond)
                        .error(R.mipmap.phl_diamond)
                        .into(pagerImg);
            }

            Log.e("getIsDxeLUXE", "1460**,............." + list.get(position).getIsDxeLUXE());

            if (list.get(position).getIsDxeLUXE() == 1) {
                luex_tag.setVisibility(View.VISIBLE);
            } else {
                luex_tag.setVisibility(View.GONE);
            }


            supplier_id_tv_pager.setText("#" + list.get(position).getStock_no() + " | " + list.get(position).getSupplier_id());
            name_tv_Pager.setText(list.get(position).getShape());

            String getOriginalTxt = list.get(position).getCarat() + getResources().getString(R.string.ct_gem) + " " + list.get(position).getColor() + " " + list.get(position).getClarity();
            if (getOriginalTxt.length() > 25) {
                String truncatedText = getOriginalTxt.substring(0, 25) + "...";
                item_type_tv.setText(truncatedText);
            } else {
                item_type_tv.setText(getOriginalTxt);
            }

            // item_type_tv.setText(list.get(position).getCarat() + getResources().getString(R.string.ct) + " " + list.get(position).getColor() + " " + list.get(position).getClarity());


            Log.e("activityValue", "..1605....." + activityValue);
            if (activityValue != null && !activityValue.isEmpty()) {
                if (activityValue.equalsIgnoreCase("gemstone")) {
                    if (list.get(position).getCut_grade().equalsIgnoreCase("")) {
                        cut_grade_tv.setVisibility(View.GONE);
                        cut_grade_tv.setText("-");
                    } else {
                        cut_grade_tv.setText(list.get(position).getCut_grade());
                    }

                    if (list.get(position).getPolish().equalsIgnoreCase("")) {
                        polish_tv.setVisibility(View.GONE);
                        polish_tv.setText("-");
                    } else {
                        polish_tv.setText(list.get(position).getPolish());
                    }

                    if (list.get(position).getSymmetry().equalsIgnoreCase("")) {
                        symmetry_tv.setVisibility(View.GONE);
                        symmetry_tv.setText("-");
                    } else {
                        symmetry_tv.setText(list.get(position).getSymmetry());
                    }

                    if (list.get(position).getFluorescence_intensity().equalsIgnoreCase("")) {
                        fluorescence_intensity_tv.setVisibility(View.GONE);
                        fluorescence_intensity_tv.setText("-");
                    } else {
                        fluorescence_intensity_tv.setText(list.get(position).getFluorescence_intensity());
                    }

                    if (list.get(position).getFluorescence_intensity().equalsIgnoreCase("")) {
                        certificate_name_tv.setVisibility(View.GONE);
                        certificate_name_tv.setText("-");
                    } else {
                        certificate_name_tv.setText(list.get(position).getCertificate_name());
                    }
                }


            } else {
                if (list.get(position).getCut_grade().equalsIgnoreCase("")) {
                    cut_grade_tv.setText("-");
                } else {
                    cut_grade_tv.setText(list.get(position).getCut_grade());
                }

                if (list.get(position).getPolish().equalsIgnoreCase("")) {
                    polish_tv.setText("-");
                } else {
                    polish_tv.setText(list.get(position).getPolish());
                }

                if (list.get(position).getSymmetry().equalsIgnoreCase("")) {
                    symmetry_tv.setText("-");
                } else {
                    symmetry_tv.setText(list.get(position).getSymmetry());
                }

                if (list.get(position).getFluorescence_intensity().equalsIgnoreCase("")) {
                    fluorescence_intensity_tv.setText("-");
                } else {
                    fluorescence_intensity_tv.setText(list.get(position).getFluorescence_intensity());
                }

                if (list.get(position).getFluorescence_intensity().equalsIgnoreCase("")) {
                    certificate_name_tv.setText("-");
                } else {
                    certificate_name_tv.setText(list.get(position).getCertificate_name());
                }
            }


            table_perc_tv.setText("T: " + list.get(position).getTable_perc());
            depth_perc.setText("D: " + list.get(position).getDepth_perc());
            measurement_tv.setText("M: " + list.get(position).getMeasurement());

            if (!list.get(position).getR_discount().equalsIgnoreCase("") && !list.get(position).getR_discount().equalsIgnoreCase("-")) {
                discount_tv.setText(list.get(position).getR_discount());
                discount_tv.setVisibility(View.VISIBLE);
            } else {
                discount_tv.setVisibility(View.GONE);
            }


            if (list.get(position).getCategory().equalsIgnoreCase("Natural")) {
                diamond_type.setBackgroundResource(R.drawable.background_yellow);
                diamond_type.setText("NATURAL");
            } else {
                diamond_type.setBackgroundResource(R.drawable.background_green_light_small_round_cornor);
                diamond_type.setText("LAB");
            }


            DecimalFormat formatter = new DecimalFormat("#,###,###");

            // Log.e("getCoupondiscountperc","..1134..."+list.get(position).getCoupondiscountperc());
            /*if (list.get(position).getCoupondiscountperc() > 0.0) {
                Log.e("In IF","..1356*******...");
                String getsubtotalPrice= String.valueOf(list.get(position).getSubtotalaftercoupondiscount());
                sub_total_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(getsubtotalPrice));
                dis_sub_total_tv.setPaintFlags(dis_sub_total_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                dis_sub_total_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));

                //   holder.sub_total_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));
            }
            else
            {
                Log.e("In else","..1356*******...");
                if(!list.get(position).getSubtotal().equalsIgnoreCase(""))
                {
                    dis_sub_total_tv.setVisibility(View.GONE);
                    sub_total_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));
                }
            }*/

           /* String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode,
                    list.get(position).getShowingSubTotal());*/
            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

            String subTotalDiscountFormat = CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode,
                    String.valueOf(list.get(position).getSubtotalaftercoupondiscount()));
            Log.e("In IF", "subTotalDiscountFormat......1356*******.1...." + subTotalDiscountFormat + ".....Original..." + list.get(position).getSubtotalaftercoupondiscount());
            Log.e("In IF", "subTotalFormat......1356*******..2..." + "...Original..." + list.get(position).getShowingSubTotal());
            Log.e("In IF", "getCurrencySymbol......1356*******..3..." + getCurrencySymbol);
            if (list.get(position).getCoupondiscountperc() > 0) {
                sub_total_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalDiscountFormat));
                dis_sub_total_tv.setPaintFlags(dis_sub_total_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                dis_sub_total_tv.setText(getCurrencySymbol + "" +
                        CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));
            } else {
                Log.e("Here ", "Call........@@@@@@@@@@@@@@@........");
                dis_sub_total_tv.setVisibility(View.GONE);
                sub_total_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));
            }


            if (list.get(position).getIs_returnable().equalsIgnoreCase("1")) {
                returnable_img.setVisibility(View.VISIBLE);
            } else {
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

            if (list.get(position).getStatus().equalsIgnoreCase("Available")) {
                status_img.setVisibility(View.VISIBLE);

                status_img.setBackgroundResource(R.drawable.available);
            } else if (list.get(position).getStatus().equalsIgnoreCase("On Hold")) {
                status_img.setVisibility(View.VISIBLE);
                status_img.setBackgroundResource(R.drawable.onhold);
            } else {
                status_img.setVisibility(View.GONE);
            }

            if (list.get(position).getIs_cart().equalsIgnoreCase("1")) {
                add_to_cart_tv.setText(context.getResources().getString(R.string.go_to_cart));
            } else {
                add_to_cart_tv.setText(context.getResources().getString(R.string.add_to_cart));
            }

            if (list.get(position).getIs_wishlist().equalsIgnoreCase("1")) {
                add_to_favt_img.setBackgroundResource(R.drawable.wishlist_filled);
            } else {
                add_to_favt_img.setBackgroundResource(R.drawable.wishlist);
            }

            add_to_cart_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastPosition = position;
                    if (list.get(position).getIs_cart().equalsIgnoreCase("") || list.get(position).getIs_cart().equalsIgnoreCase("0")) {
                        addToCartWhereToCall = "recommendedDiamondList";
                        onAddToCartAPI(false, list.get(position).getCertificate_no());
                    } else {
                        Constant.manageClickEventForRedirection = "";
                        Intent intent = new Intent(activity, MyCardListScreenActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }
            });

            add_to_favt_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    lastPosition = position;
                    if (list.get(position).getIs_wishlist().equalsIgnoreCase("1")) {
                        onRemoveFromWishlistAPI(false, list.get(position).getCertificate_no());
                    } else {
                        onAddToWishlistAPI(false, list.get(position).getCertificate_no());
                    }

                }
            });

            root_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    shapeImageArrayList = new ArrayList<>();
                    diamondShapeImageModelArrayList = new ArrayList<>();
                    princessModelArrayList = new ArrayList<>();
                    emeraldModelArrayList = new ArrayList<>();
                    heartModelArrayList = new ArrayList<>();
                    radiantModelArrayList = new ArrayList<>();
                    ovalModelArrayList = new ArrayList<>();
                    pearModelArrayList = new ArrayList<>();
                    marquiseModelArrayList = new ArrayList<>();
                    asscherModelArrayList = new ArrayList<>();
                    cushionModelArrayList = new ArrayList<>();

                    shapeImageModelData();

                    lastPosition = position;

                    //certificate_number = CommonUtility.getGlobalString(context, "certificate_number");
                    certificate_number = list.get(position).getCertificate_no();
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                    onBindDetails(false);
                    onBindSizePreviewAPI(true);
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

    public void onBindDetails(boolean showLoader) {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context)) {
            //Log.e("----------Diamond------ : ", Constant.shapeDiamondValue.toString());
            urlParameter = new HashMap<String, String>();


            urlParameter.put("certificateNo", certificate_number);
            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context, this, urlParameter, ApiConstants.GET_DIAMONDS_DETAILS, ApiConstants.GET_DIAMONDS_DETAILS_ID, showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    public void onBindDetailsGemStone(boolean showLoader) {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context)) {
            //Log.e("----------Diamond------ : ", Constant.shapeDiamondValue.toString());
            urlParameter = new HashMap<String, String>();
            Log.e("certificate_number", "....." + certificate_number);
            urlParameter.put("certificateNo", certificate_number);
            urlParameter.put("sessionId", "" + uuid);
            //GRS2021-080408
            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context, this, urlParameter, ApiConstants.GET_GEMSTONES_DETAILS, ApiConstants.GET_GEMSTONES_DETAILS_ID, showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }


    public void onBindSizePreviewAPI(boolean showLoader) {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context)) {
            //Log.e("----------Diamond------ : ", Constant.shapeDiamondValue.toString());
            urlParameter = new HashMap<String, String>();

            urlParameter.put("deviceType", "Android");
            urlParameter.put("sessionId", "" + uuid);
            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context, this, urlParameter, ApiConstants.DIAMOND_SIZE_PREVIEW, ApiConstants.DIAMOND_SIZE_PREVIEW_ID, showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    public void onBindRecommendedDiamondList(boolean showLoader) {
        String uuid = CommonUtility.getAndroidId(context);

        if (Utils.isNetworkAvailable(context)) {
            //Log.e("----------Diamond------ : ", Constant.shapeDiamondValue.toString());
            urlParameter = new HashMap<String, String>();

            urlParameter.put("certificateNo", certificate_number);
            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context, this, urlParameter, ApiConstants.GET_RECOMMENDED_DIAMONDS, ApiConstants.GET_RECOMMENDED_DIAMONDS_ID, showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    public void onAddToCartAPI(boolean showLoader, String certificateNo) {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context)) {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("certificateNo", "" + certificateNo);
            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context, this, urlParameter, ApiConstants.ADD_TO_CART, ApiConstants.ADD_TO_CART_ID, showLoader, "POST");
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    public void onAddToWishlistAPI(boolean showLoader, String certificateNo) {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context)) {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("certificateNo", "" + certificateNo);
            urlParameter.put("source", "");
            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context, this, urlParameter, ApiConstants.ADD_TO_WISHLIST, ApiConstants.ADD_TO_WISHLIST_ID, showLoader, "POST");
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    public void onRemoveFromWishlistAPI(boolean showLoader, String certificateNo) {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context)) {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("certificateNo", "" + certificateNo);
            urlParameter.put("source", "");
            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context, this, urlParameter, ApiConstants.REMOVE_FROM_WISHLIST, ApiConstants.REMOVE_FROM_WISHLIST_ID, showLoader, "POST");
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

        try {
            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {

                case ApiConstants.GET_DIAMONDS_DETAILS_ID:

                    Log.v("------Diamond----- : ", "--------JSONObject-------- : " + jsonObject);

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1")) {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        stock_id = CommonUtility.checkString(jObjDetails.optString("stock_id"));
                        item_name = CommonUtility.checkString(jObjDetails.optString("item_name"));
                        supplier_id = CommonUtility.checkString(jObjDetails.optString("supplier_id"));
                        certificate_no = CommonUtility.checkString(jObjDetails.optString("certificate_no"));
                        category = CommonUtility.checkString(jObjDetails.optString("category"));
                        certificate_name = CommonUtility.checkString(jObjDetails.optString("certificate_name"));
                        cut_grade = CommonUtility.checkString(jObjDetails.optString("cut_grade"));
                        shape = CommonUtility.checkString(jObjDetails.optString("shape"));
                        clarity = CommonUtility.checkString(jObjDetails.optString("clarity"));
                        carat = CommonUtility.checkString(jObjDetails.optString("carat"));
                        color = CommonUtility.checkString(jObjDetails.optString("color"));
                        growth_type = CommonUtility.checkString(jObjDetails.optString("growth_type"));
                        fluorescence_intensity = CommonUtility.checkString(jObjDetails.optString("fluorescence_intensity"));
                        polish = CommonUtility.checkString(jObjDetails.optString("polish"));
                        symmetry = CommonUtility.checkString(jObjDetails.optString("symmetry"));
                        depth_perc = CommonUtility.checkString(jObjDetails.optString("depth_perc"));
                        table_perc = CommonUtility.checkString(jObjDetails.optString("table_perc"));
                        discount_amout = CommonUtility.checkString(jObjDetails.optString("discount_amout"));
                        length = CommonUtility.checkString(jObjDetails.optString("length"));
                        width = CommonUtility.checkString(jObjDetails.optString("width"));
                        depth = CommonUtility.checkString(jObjDetails.optString("depth"));
                        measurement = CommonUtility.checkString(jObjDetails.optString("measurement"));
                        shade = CommonUtility.checkString(jObjDetails.optString("shade"));
                        luster = CommonUtility.checkString(jObjDetails.optString("luster"));
                        eye_clean = CommonUtility.checkString(jObjDetails.optString("eye_clean"));
                        crown_angle = CommonUtility.checkString(jObjDetails.optString("crown_angle"));
                        pavillion_angle = CommonUtility.checkString(jObjDetails.optString("pavillion_angle"));
                        diamond_image = CommonUtility.checkString(jObjDetails.optString("diamond_image"));
                        diamond_video = CommonUtility.checkString(jObjDetails.optString("diamond_video"));
                        is_returnable = CommonUtility.checkString(jObjDetails.optString("is_returnable"));
                        certificate_file = CommonUtility.checkString(jObjDetails.optString("certificate_file"));
                        girdle_condition = CommonUtility.checkString(jObjDetails.optString("girdle_condition"));
                        culet = CommonUtility.checkString(jObjDetails.optString("culet"));
                        location = CommonUtility.checkString(jObjDetails.optString("location"));
                        crown_height = CommonUtility.checkString(jObjDetails.optString("crown_height"));
                        pavillion_depth = CommonUtility.checkString(jObjDetails.optString("pavillion_depth"));
                        inscription = CommonUtility.checkString(jObjDetails.optString("inscription"));
                        key_to_symbols = CommonUtility.checkString(jObjDetails.optString("key_to_symbols"));
                        report_comments = CommonUtility.checkString(jObjDetails.optString("report_comments"));
                        subtotal = CommonUtility.checkString(jObjDetails.optString("subtotal"));
                        coupondiscountperc = CommonUtility.checkDouble(jObjDetails.optString("coupon_discount_perc"));
                        subtotalaftercoupondiscount = CommonUtility.checkDouble(jObjDetails.optString("subtotal_after_coupon_discount"));
                        is_cart = CommonUtility.checkString(jObjDetails.optString("is_cart"));
                        is_wishlist = CommonUtility.checkString(jObjDetails.optString("is_wishlist"));
                        avaliable_status = CommonUtility.checkString(jObjDetails.optString("status"));
                        r_discount = CommonUtility.checkString(jObjDetails.optString("r_discount"));
                        supplier_comment = CommonUtility.checkString(jObjDetails.optString("supplier_comment"));
                        stock_no = CommonUtility.checkString(jObjDetails.optString("stock_no"));

                        /*stock_no = CommonUtility.checkString(jObjDetails.optString("coupon_discount_perc"));
                        stock_no = CommonUtility.checkString(jObjDetails.optString("subtotal_after_coupon_discount"));*/

                        if (!diamond_image.equalsIgnoreCase("")) {
                            Picasso.with(context)
                                    .load(diamond_image)
                                    .placeholder(R.mipmap.phl_diamond)
                                    .error(R.mipmap.phl_diamond)
                                    .into(diamond_img);
                        } else {
                            Picasso.with(context)
                                    .load(R.mipmap.phl_diamond)
                                    .placeholder(R.mipmap.phl_diamond)
                                    .error(R.mipmap.phl_diamond)
                                    .into(diamond_img);
                        }

                        supplier_id_tv.setText("#" + stock_no + "  |  " + "ID: " + supplier_id);
                        name_tv.setText(shape);

                        String getOriginalTxt = "- " + carat + getResources().getString(R.string.ct) + " " + color + " " + clarity;
                        if (getOriginalTxt.length() > 25) {
                            String truncatedText = getOriginalTxt.substring(0, 25) + "...";
                            item_type_tv.setText(truncatedText);
                        } else {
                            item_type_tv.setText(getOriginalTxt);
                        }

                        // item_type_tv.setText("- " +  carat + getResources().getString(R.string.ct) + " " + color + " " + clarity);

                        if (category.equalsIgnoreCase("Natural")) {
                            type_tv.setBackgroundResource(R.drawable.background_yellow);
                        } else {
                            type_tv.setBackgroundResource(R.drawable.background_green_light);
                        }
                        type_tv.setText(category);

                        if (is_returnable.equalsIgnoreCase("1")) {
                            current_returnable_img.setVisibility(View.VISIBLE);
                        } else {
                            current_returnable_img.setVisibility(View.GONE);
                        }

                        if (cut_grade.equalsIgnoreCase("")) {
                            cut_grade_tv.setText("-");
                        } else {
                            cut_grade_tv.setText(cut_grade);
                        }
                        if (polish.equalsIgnoreCase("")) {
                            polish_tv.setText("-");
                        } else {
                            polish_tv.setText(polish);
                        }

                        if (symmetry.equalsIgnoreCase("")) {
                            symmetry_tv.setText("-");
                        } else {
                            symmetry_tv.setText(symmetry);
                        }

                        if (fluorescence_intensity.equalsIgnoreCase("")) {
                            fluorescence_intensity_tv.setText("-");
                        } else {
                            fluorescence_intensity_tv.setText(fluorescence_intensity);
                        }

                        if (certificate_name.equalsIgnoreCase("")) {
                            certificate_name_tv.setText("-");
                        } else {
                            certificate_name_tv.setText(certificate_name);
                        }


                        if (subtotal != null && !subtotal.equalsIgnoreCase("")) {
                            String subTotalFormat = CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, subtotal);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            String subTotalDiscountFormat = CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, String.valueOf(subtotalaftercoupondiscount));
                            /*Log.e("In IF","subTotalDiscountFormat......1946*******..."+subTotalDiscountFormat+"..original..."+subtotalaftercoupondiscount);
                            Log.e("In IF","subTotalDiscountFormat......1947*******..."+subTotalFormat+"...subtotal.."+subtotal);
                            Log.e("In IF","subTotalDiscountFormat......1948*******..."+subTotalDiscountFormat);*/
                            if (coupondiscountperc > 0) {
                                dis_sub_total_tv.setVisibility(View.VISIBLE);
                                sub_total_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalDiscountFormat));
                                dis_sub_total_tv.setPaintFlags(dis_sub_total_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                dis_sub_total_tv.setText(getCurrencySymbol + "" +
                                        CommonUtility.currencyFormat(subTotalFormat));
                            } else {
                                Log.e("Here ", "Call........@@@@@@@@@@@@@@@........");
                                dis_sub_total_tv.setVisibility(View.GONE);
                                sub_total_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                            }
                            /*if (list.get(position).getCoupondiscountperc() > 0.0) {
                                Log.e("In IF","..1356*******...");
                                String getsubtotalPrice= String.valueOf(list.get(position).getSubtotalaftercoupondiscount());
                                sub_total_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(getsubtotalPrice));
                                dis_sub_total_tv.setPaintFlags(dis_sub_total_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                dis_sub_total_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));
                                //   holder.sub_total_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));
                            }
                            else
                            {
                            }*/


                        } else {
                        }

                        if (is_cart.equalsIgnoreCase("1")) {
                            current_add_to_cart_tv.setText(getResources().getString(R.string.go_to_cart));
                        } else {
                            current_add_to_cart_tv.setText(getResources().getString(R.string.add_to_cart));
                        }

                        if (measurement.equalsIgnoreCase("")) {
                            details_measurements_tv.setText("-");
                        } else {
                            details_measurements_tv.setText(measurement);
                        }

                        if (depth_perc.equalsIgnoreCase("")) {
                            details_depth_tv.setText("-");
                        } else {
                            details_depth_tv.setText(depth_perc + "%");
                        }

                        if (table_perc.equalsIgnoreCase("")) {
                            details_table_tv.setText("-");
                        } else {
                            details_table_tv.setText(table_perc + "%");
                        }

                        if (crown_height.equalsIgnoreCase("")) {
                            details_crown_height_tv.setText("-");
                        } else {
                            details_crown_height_tv.setText(crown_height + "%");
                        }

                        if (crown_angle.equalsIgnoreCase("")) {
                            details_crown_angle_tv.setText("-");
                        } else {
                            details_crown_angle_tv.setText(crown_angle);
                        }

                        if (pavillion_depth.equalsIgnoreCase("")) {
                            details_pavilion_depth_tv.setText("-");
                        } else {
                            details_pavilion_depth_tv.setText(pavillion_depth + "%");
                        }

                        if (pavillion_angle.equalsIgnoreCase("")) {
                            details_pavilion_angle_tv.setText("-");
                        } else {
                            details_pavilion_angle_tv.setText(pavillion_angle);
                        }

                        if (shade.equalsIgnoreCase("")) {
                            details_shade_tv.setText("-");
                        } else {
                            details_shade_tv.setText(shade);
                        }

                        if (girdle_condition.equalsIgnoreCase("")) {
                            details_condition_cult_tv.setText("-");
                        } else {
                            details_condition_cult_tv.setText(girdle_condition);
                        }

                        if (growth_type.equalsIgnoreCase("")) {
                            details_growth_type_tv.setText("-");
                        } else {
                            details_growth_type_tv.setText(growth_type);
                        }

                        if (eye_clean.equalsIgnoreCase("")) {
                            details_inclusion_tv.setText("-");
                        } else {
                            details_inclusion_tv.setText(eye_clean);
                        }


                        if (avaliable_status.equalsIgnoreCase("")) {
                            details_status_tv.setText("-");
                        } else {
                            details_status_tv.setText(avaliable_status);
                        }

                        Log.e("details_location_tv : ", location);
                        if (location.equalsIgnoreCase("")) {
                            details_location_tv.setText("-");
                        } else {
                            details_location_tv.setText(location);
                        }

                        details_key_tv.setText(key_to_symbols);

                        if (supplier_comment.equalsIgnoreCase("")) {
                            details_remark_tv.setText("-");
                        } else {
                            details_remark_tv.setText(supplier_comment);
                        }

                        if (report_comments.equalsIgnoreCase("")) {
                            details_report_tv.setText("-");
                        } else {
                            details_report_tv.setText(report_comments);
                        }

                        if (culet.equalsIgnoreCase("")) {
                            culet_id.setText("-");
                        } else {
                            culet_id.setText(culet);
                        }

                        discount_tv.setText(r_discount);

                        if (!r_discount.equalsIgnoreCase("") && !r_discount.equalsIgnoreCase("-")) {
                            discount_tv.setVisibility(View.VISIBLE);
                        } else {
                            discount_tv.setVisibility(View.GONE);
                        }

                        if (avaliable_status.equalsIgnoreCase("Available")) {
                            current_status_img.setVisibility(View.VISIBLE);
                            current_status_img.setBackgroundResource(R.drawable.available);
                        } else if (avaliable_status.equalsIgnoreCase("On Hold")) {
                            current_status_img.setVisibility(View.VISIBLE);
                            current_status_img.setBackgroundResource(R.drawable.onhold);
                        } else {
                            current_status_img.setVisibility(View.GONE);
                        }
                        onBindRecommendedDiamondList(true);
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("0")) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                        onBindRecommendedDiamondList(true);
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("4")) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    break;


                case ApiConstants.GET_GEMSTONES_DETAILS_ID:

                    Log.v("------Diamond----- : ", "--------JSONObject------#$#$#$#$#$$#$------- : " + jsonObject);

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1")) {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        stock_id = CommonUtility.checkString(jObjDetails.optString("stock_id"));
                        item_name = CommonUtility.checkString(jObjDetails.optString("item_name"));
                        supplier_id = CommonUtility.checkString(jObjDetails.optString("supplier_id"));
                        certificate_no = CommonUtility.checkString(jObjDetails.optString("certificate_no"));
                        category = CommonUtility.checkString(jObjDetails.optString("category"));
                        certificate_name = CommonUtility.checkString(jObjDetails.optString("certificate_name"));
                        cut_grade = CommonUtility.checkString(jObjDetails.optString("cut_grade"));
                        shape = CommonUtility.checkString(jObjDetails.optString("shape"));
                        clarity = CommonUtility.checkString(jObjDetails.optString("clarity"));
                        carat = CommonUtility.checkString(jObjDetails.optString("carat"));
                        color = CommonUtility.checkString(jObjDetails.optString("color"));
                        growth_type = CommonUtility.checkString(jObjDetails.optString("growth_type"));
                        fluorescence_intensity = CommonUtility.checkString(jObjDetails.optString("fluorescence_intensity"));
                        polish = CommonUtility.checkString(jObjDetails.optString("polish"));
                        symmetry = CommonUtility.checkString(jObjDetails.optString("symmetry"));
                        depth_perc = CommonUtility.checkString(jObjDetails.optString("depth_perc"));
                        table_perc = CommonUtility.checkString(jObjDetails.optString("table_perc"));
                        discount_amout = CommonUtility.checkString(jObjDetails.optString("discount_amout"));
                        length = CommonUtility.checkString(jObjDetails.optString("length"));
                        width = CommonUtility.checkString(jObjDetails.optString("width"));
                        depth = CommonUtility.checkString(jObjDetails.optString("depth"));
                        measurement = CommonUtility.checkString(jObjDetails.optString("measurement"));
                        shade = CommonUtility.checkString(jObjDetails.optString("shade"));
                        luster = CommonUtility.checkString(jObjDetails.optString("luster"));
                        eye_clean = CommonUtility.checkString(jObjDetails.optString("eye_clean"));
                        crown_angle = CommonUtility.checkString(jObjDetails.optString("crown_angle"));
                        pavillion_angle = CommonUtility.checkString(jObjDetails.optString("pavillion_angle"));
                        diamond_image = CommonUtility.checkString(jObjDetails.optString("diamond_image"));
                        diamond_video = CommonUtility.checkString(jObjDetails.optString("diamond_video"));
                        is_returnable = CommonUtility.checkString(jObjDetails.optString("is_returnable"));
                        certificate_file = CommonUtility.checkString(jObjDetails.optString("certificate_file"));
                        girdle_condition = CommonUtility.checkString(jObjDetails.optString("girdle_condition"));
                        culet = CommonUtility.checkString(jObjDetails.optString("culet"));
                        location = CommonUtility.checkString(jObjDetails.optString("location"));
                        crown_height = CommonUtility.checkString(jObjDetails.optString("crown_height"));
                        pavillion_depth = CommonUtility.checkString(jObjDetails.optString("pavillion_depth"));
                        inscription = CommonUtility.checkString(jObjDetails.optString("inscription"));
                        key_to_symbols = CommonUtility.checkString(jObjDetails.optString("key_to_symbols"));
                        report_comments = CommonUtility.checkString(jObjDetails.optString("report_comments"));
                        subtotal = CommonUtility.checkString(jObjDetails.optString("subtotal"));
                        coupondiscountperc = CommonUtility.checkDouble(jObjDetails.optString("coupon_discount_perc"));
                        subtotalaftercoupondiscount = CommonUtility.checkDouble(jObjDetails.optString("subtotal_after_coupon_discount"));
                        is_cart = CommonUtility.checkString(jObjDetails.optString("is_cart"));
                        is_wishlist = CommonUtility.checkString(jObjDetails.optString("is_wishlist"));
                        avaliable_status = CommonUtility.checkString(jObjDetails.optString("status"));
                        r_discount = CommonUtility.checkString(jObjDetails.optString("r_discount"));
                        supplier_comment = CommonUtility.checkString(jObjDetails.optString("supplier_comment"));
                        stock_no = CommonUtility.checkString(jObjDetails.optString("stock_no"));
                       String stone_range = CommonUtility.checkString(jObjDetails.optString("stone_range"));
                        String dia_wt = CommonUtility.checkString(jObjDetails.optString("dia_wt"));
                        String treatment = CommonUtility.checkString(jObjDetails.optString("treatment"));
                        String fancy_color = CommonUtility.checkString(jObjDetails.optString("fancy_color"));

                        /*stock_no = CommonUtility.checkString(jObjDetails.optString("coupon_discount_perc"));
                        stock_no = CommonUtility.checkString(jObjDetails.optString("subtotal_after_coupon_discount"));*/

                        Log.e("diamond_image", "..GemStone....2414..." + diamond_image);
                        if (!diamond_image.equalsIgnoreCase("")) {
                            Picasso.with(context)
                                    .load(diamond_image)
                                    .placeholder(R.mipmap.phl_diamond)
                                    .error(R.mipmap.phl_diamond)
                                    .into(diamond_img);
                        } else {
                            Picasso.with(context)
                                    .load(R.mipmap.phl_diamond)
                                    .placeholder(R.mipmap.phl_diamond)
                                    .error(R.mipmap.phl_diamond)
                                    .into(diamond_img);
                        }

                        supplier_id_tv.setText("#" + stock_no + "  |  " + "ID: " + supplier_id);

                        Log.e("shape", "..2501/........." + shape);
                        Log.e("shape", "..2501/........." + shape);
                        // item_type_tv
                        //  name_tv.setText(shape);

                        String getOriginalTxt = shape + " " + carat + getResources().getString(R.string.ct) + " | " + clarity;
                        if (getOriginalTxt.length() > 40) {
                            String truncatedText = getOriginalTxt.substring(0, 40) + "...";
                            item_type_tv.setText(truncatedText);
                        } else {
                            item_type_tv.setText(getOriginalTxt);
                        }

                        //  item_type_tv.setText(carat + getResources().getString(R.string.ct) + " " + color + " " + clarity);
                        //  item_type_tv.setText("- " +  carat + getResources().getString(R.string.ct) + " " + color + " " + clarity);

                        if (category.equalsIgnoreCase("Natural")) {
                            type_tv.setBackgroundResource(R.drawable.background_yellow);
                        } else {
                            type_tv.setBackgroundResource(R.drawable.background_green_light);
                        }
                        type_tv.setText(category);

                        if (is_returnable.equalsIgnoreCase("1")) {
                            current_returnable_img.setVisibility(View.VISIBLE);
                        } else {
                            current_returnable_img.setVisibility(View.GONE);
                        }

                        if (cut_grade.equalsIgnoreCase("")) {
                            cut_grade_tv.setVisibility(View.VISIBLE);
                            cut_grade_tv.setText("-");
                        } else {
                            /*cut_grade_tv.setText(limitCharacters(cut_grade,5));*/
                            cut_grade_tv.setVisibility(View.VISIBLE);
                            cut_grade_tv.setText(certificate_name);
                        }

                        /*if(polish.equalsIgnoreCase(""))
                        {
                            //cut_grade_view.setVisibility(View.GONE);
                            polish_tv.setVisibility(View.GONE);
                            polish_tv.setText("-");
                        }
                        else{
                           *//* polish_tv.setText(limitCharacters(polish,5));*//*
                            polish_tv.setText(limitCharacters(polish,5));
                        }*/


                        if (inscription.equalsIgnoreCase("")) {
                            // polish_view.setVisibility(View.GONE);
                            // symmetry_tv.setVisibility(View.GONE);
                            symmetry_tv.setVisibility(View.VISIBLE);
                            symmetry_tv.setText("-");
                        } else {
                            symmetry_tv.setVisibility(View.VISIBLE);
                            symmetry_tv.setText(inscription);
                            //  inscription
                            //symmetry_tv.setText(limitCharacters(symmetry,5));
                        }

                        /*if(fluorescence_intensity.equalsIgnoreCase(""))
                        {
                            symmetry_view.setVisibility(View.GONE);
                            fluorescence_intensity_tv.setVisibility(View.GONE);
                            fluorescence_intensity_tv.setText("-");
                        }
                        else{
                            fluorescence_intensity_tv.setText(limitCharacters(fluorescence_intensity,5));
                        }

                        if(certificate_name.equalsIgnoreCase(""))
                        {
                            fluorescence_view.setVisibility(View.GONE);
                            certificate_name_tv.setVisibility(View.GONE);
                            certificate_name_tv.setText("-");
                        }
                        else{
                            certificate_name_tv.setText(limitCharacters(certificate_name,5));
                        }*/



                        Log.e("fancy_color","2360,,,"+fancy_color);
                        exact_dimensions_tv.setText(measurement);
                        // wait for confirmation
                        refractive_index_tv.setText(stone_range);
                        weight_cart_tv.setText(carat);
                        weight_ratti_tv.setText(fancy_color);
                        if (key_to_symbols.equalsIgnoreCase(""))
                        {
                            gravity_tv.setText("-");
                        }
                        else {
                            gravity_tv.setText(key_to_symbols);
                        }

                        if (growth_type.equalsIgnoreCase(""))
                        {
                            details_item_tv.setText("-");
                        }
                        else {
                            details_item_tv.setText(growth_type);
                        }

                        if(treatment.equalsIgnoreCase(""))
                        {
                            treatment_tv.setText("-");
                        }
                        else {
                            treatment_tv.setText(treatment);
                        }

                        if(category.equalsIgnoreCase(""))
                        {
                            composition_tv.setText("-");
                        }
                        else {
                            composition_tv.setText(category);
                        }

                        if(location.equalsIgnoreCase(""))
                        {
                            location_tv.setText("-");
                        }
                        else {
                            location_tv.setText(location);
                        }
                        if(stock_no.equalsIgnoreCase(""))
                        {
                            details_inclusion_tv.setText("-");
                        }
                        else {
                           /* details_inclusion_tv.setText(certificate_no);*/
                            details_inclusion_tv.setText(stock_no);
                        }

                        if (supplier_comment.equalsIgnoreCase("")) {
                            details_remark_tv.setText("-");
                        } else {
                            details_remark_tv.setText(supplier_comment);
                        }

                        if (report_comments.equalsIgnoreCase("")) {
                            details_report_tv.setText("-");
                        } else {
                            details_report_tv.setText(report_comments);
                        }



                        /*  gravity_tv, weight_ratti_tv, weight_cart_tv, refractive_index_tv, exact_dimensions_tv,
                                details_item_tv, treatment_tv, composition_tv*/

                        Log.e("subtotal", "..2567....." + subtotal);
                        if (subtotal != null && !subtotal.equalsIgnoreCase("")) {
                            String subTotalFormat = CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, subtotal);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            String subTotalDiscountFormat = CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, String.valueOf(subtotalaftercoupondiscount));
                            Log.e("In IF", "subTotalDiscountFormat......1946*******..." + subTotalDiscountFormat + "..original..." + subtotalaftercoupondiscount);
                            Log.e("In IF", "subTotalDiscountFormat......1947*******..." + subTotalFormat + "...subtotal.." + subtotal);
                            Log.e("In IF", "subTotalDiscountFormat......1948*******..." + subTotalDiscountFormat);
                            Log.e("In IF", "coupondiscountperc......2577*******..." + coupondiscountperc);

                            if (coupondiscountperc > 0) {
                                dis_sub_total_tv.setVisibility(View.VISIBLE);
                                sub_total_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalDiscountFormat));
                                dis_sub_total_tv.setPaintFlags(dis_sub_total_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                dis_sub_total_tv.setText(getCurrencySymbol + "" +
                                        CommonUtility.currencyFormat(subTotalFormat));
                            } else {
                                Log.e("Here ", "Call........@@@@@@@@@@@@@@@........");
                                dis_sub_total_tv.setVisibility(View.GONE);
                                sub_total_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                            }
                            /*if (list.get(position).getCoupondiscountperc() > 0.0) {
                                Log.e("In IF","..1356*******...");
                                String getsubtotalPrice= String.valueOf(list.get(position).getSubtotalaftercoupondiscount());
                                sub_total_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(getsubtotalPrice));
                                dis_sub_total_tv.setPaintFlags(dis_sub_total_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                dis_sub_total_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));

                                //   holder.sub_total_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));
                            }
                            else
                            {

                            }*/
                        } else {
                        }

                        if (is_cart.equalsIgnoreCase("1")) {
                            current_add_to_cart_tv.setText(getResources().getString(R.string.go_to_cart));
                        } else {
                            current_add_to_cart_tv.setText(getResources().getString(R.string.add_to_cart));
                        }

                        /*if (measurement.equalsIgnoreCase("")) {
                            details_measurements_tv.setVisibility(View.GONE);
                            details_measurements_tv.setText("-");
                        } else {
                            details_measurements_tv.setText(measurement);
                        }*/

                        if (depth_perc.equalsIgnoreCase("")) {
                            details_depth_tv.setVisibility(View.GONE);
                            details_depth_tv.setText("-");
                        } else {
                            details_depth_tv.setText(depth_perc + "%");
                        }

                        if (table_perc.equalsIgnoreCase("")) {
                            details_table_tv.setText("-");
                        } else {
                            details_table_tv.setText(table_perc + "%");
                        }

                        if (crown_height.equalsIgnoreCase("")) {
                            details_crown_height_tv.setText("-");
                        } else {
                            details_crown_height_tv.setText(crown_height + "%");
                        }

                        if (crown_angle.equalsIgnoreCase("")) {
                            details_crown_angle_tv.setText("-");
                        } else {
                            details_crown_angle_tv.setText(crown_angle);
                        }

                       /* if (pavillion_depth.equalsIgnoreCase("")) {
                            details_pavilion_depth_tv.setText("-");
                        } else {
                            details_pavilion_depth_tv.setText(pavillion_depth + "%");
                        }

                        if (pavillion_angle.equalsIgnoreCase("")) {
                            details_pavilion_angle_tv.setText("-");
                        } else {
                            details_pavilion_angle_tv.setText(pavillion_angle);
                        }*/

                        /*if (shade.equalsIgnoreCase("")) {
                            details_shade_tv.setText("-");
                        } else {
                            details_shade_tv.setText(shade);
                        }*/

                       /* if (girdle_condition.equalsIgnoreCase("")) {
                            details_condition_cult_tv.setText("-");
                        } else {
                            details_condition_cult_tv.setText(girdle_condition);
                        }*/

                        /*if (growth_type.equalsIgnoreCase("")) {
                            details_growth_type_tv.setText("-");
                        } else {
                            details_growth_type_tv.setText(growth_type);
                        }*/

                        if (avaliable_status.equalsIgnoreCase("")) {
                            details_status_tv.setText("-");
                        } else {
                            details_status_tv.setText(avaliable_status);
                        }


                        details_key_tv.setText(key_to_symbols);



                        /*if (culet.equalsIgnoreCase("")) {
                            culet_id.setText("-");
                        } else {
                            culet_id.setText(culet);
                        }*/


                        discount_tv.setText(r_discount);

                        if (!r_discount.equalsIgnoreCase("") && !r_discount.equalsIgnoreCase("-")) {
                            discount_tv.setVisibility(View.VISIBLE);
                        } else {
                            discount_tv.setVisibility(View.GONE);
                        }

                        if (avaliable_status.equalsIgnoreCase("Available")) {
                            current_status_img.setVisibility(View.VISIBLE);
                            current_status_img.setBackgroundResource(R.drawable.available);
                        } else if (avaliable_status.equalsIgnoreCase("On Hold")) {
                            current_status_img.setVisibility(View.VISIBLE);
                            current_status_img.setBackgroundResource(R.drawable.onhold);
                        } else {
                            current_status_img.setVisibility(View.GONE);
                        }
                        onBindRecommendedDiamondList(true);
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("0")) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                        onBindRecommendedDiamondList(true);
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("4")) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.GET_RECOMMENDED_DIAMONDS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1")) {
                        Log.v("------Diamond----- : ", "GET_RECOMMENDED_DIAMONDS_ID--------JSONObject----Recomm---- : " + jsonObject);
                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if (recommandDiamondArrayList.size() > 0) {
                            recommandDiamondArrayList.clear();
                        }
                        for (int i = 0; i < details.length(); i++) {
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
                            model.setIsDxeLUXE(CommonUtility.checkInt(objectCodes.optString("isDxeLUXE")));
                            model.setCurrencySymbol(ApiConstants.rupeesIcon);

                            String subTotalFormat = CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, CommonUtility.checkString(objectCodes.optString("subtotal")));
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            model.setShowingSubTotal(subTotalFormat);
                            model.setCurrencySymbol(getCurrencySymbol);

                            recommandDiamondArrayList.add(model);
                        }

                        //Log.e("recommandDiamondArrayList", "" + recommandDiamondArrayList.size());
                        if (recommandDiamondArrayList != null && recommandDiamondArrayList.size() > 0) {
                            viewpager_layout.setVisibility(View.VISIBLE);
                            recommanded_tv_lbl.setVisibility(View.VISIBLE);
                            setpager();
                        } else {
                            viewpager_layout.setVisibility(View.GONE);
                            recommanded_tv_lbl.setVisibility(View.GONE);
                        }

                       /* for (int i = 0; i <recommandDiamondArrayList.size() ; i++)
                        {

                            Log.e("--------selectedCurrencyValue : ", selectedCurrencyValue.toString());
                            Log.e("--------selectedCurrencyValue1 : ", selectedCurrencyCode.toString());
                            Log.e("--------selectedCurrencyValue11 : ", "" + recommandDiamondArrayList.get(i).getSubtotal());

                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, recommandDiamondArrayList.get(i).getSubtotal());
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            recommandDiamondArrayList.get(i).setShowingSubTotal(subTotalFormat);
                            recommandDiamondArrayList.get(i).setCurrencySymbol(getCurrencySymbol);

                        }*/
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("0")) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("4")) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.ADD_TO_CART_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1")) {
                        //Toast.makeText(activity, ""+ message, Toast.LENGTH_SHORT).show();

                        // Check If User Add to Cart By Details Screen Single Button If Condition Work, Otherwise User Come Recommended Diamond List
                        if (addToCartWhereToCall.equalsIgnoreCase("detailsScreen")) {
                            is_cart = "1";
                            current_add_to_cart_tv.setText(getResources().getString(R.string.go_to_cart));
                        } else {
                            recommandDiamondArrayList.get(lastPosition).setIs_cart("1");
                            viewPager.setAdapter(new MyPagerAdapter(activity, recommandDiamondArrayList));
                        }

                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");
                        Constant.cardCount = CommonUtility.checkString(jObjDetails.optString("cart_count"));
                        Constant.wishListCount = CommonUtility.checkString(jObjDetails.optString("wishlist_count"));
                        showCardCount();
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("0")) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("4")) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.ADD_TO_WISHLIST_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1")) {
                        /*if(modelArrayList.get(lastPosition).getIs_wishlist().equalsIgnoreCase("1"))
                        {
                            modelArrayList.get(lastPosition).setIs_wishlist("0");

                        }
                        else if(modelArrayList.get(lastPosition).getIs_wishlist().equalsIgnoreCase("0"))
                        {
                            modelArrayList.get(lastPosition).setIs_wishlist("1");
                        }
                        else{}*/

                        recommandDiamondArrayList.get(lastPosition).setIs_wishlist("1");

                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");
                        Constant.cardCount = CommonUtility.checkString(jObjDetails.optString("cart_count"));
                        Constant.wishListCount = CommonUtility.checkString(jObjDetails.optString("wishlist_count"));
                        showCardCount();

                        viewPager.setAdapter(new MyPagerAdapter(activity, recommandDiamondArrayList));
                        //Toast.makeText(activity, ""+ message, Toast.LENGTH_SHORT).show();

                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("0")) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("4")) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.REMOVE_FROM_WISHLIST_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1")) {
                        recommandDiamondArrayList.get(lastPosition).setIs_wishlist("0");

                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");
                        Constant.cardCount = CommonUtility.checkString(jObjDetails.optString("cart_count"));
                        Constant.wishListCount = CommonUtility.checkString(jObjDetails.optString("wishlist_count"));
                        showCardCount();

                        viewPager.setAdapter(new MyPagerAdapter(activity, recommandDiamondArrayList));
                        //Toast.makeText(activity, ""+ message, Toast.LENGTH_SHORT).show();
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("0")) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("4")) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ApiConstants.DIAMOND_SIZE_PREVIEW_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1")) {
                        Log.e("jsonObjectData : ", "Diamond Size : " + jsonObjectData.toString());
                        JSONArray details = jsonObjectData.getJSONArray("details");

                        /*for (int i = 0; i < details.length(); i++) {
                            JSONObject shapeObject = details.getJSONObject(i);
                            DiamondShapeImageModel diamondShape = new DiamondShapeImageModel();

                            diamondShape.setShape(shapeObject.getString("shape"));

                            JSONArray detailsArray = shapeObject.getJSONArray("details");
                            List<DiamondShapeImageNameModel> details1 = new ArrayList<>();

                            for (int j = 0; j < detailsArray.length(); j++) {
                                JSONObject detailObject = detailsArray.getJSONObject(j);
                                DiamondShapeImageNameModel diamondDetail = new DiamondShapeImageNameModel();

                                diamondDetail.setCarat(detailObject.getString("carat"));
                                diamondDetail.setImage(detailObject.getString("image"));
                                details1.add(diamondDetail);
                            }
                            diamondShape.setDetails(details1);
                            diamondShapeImageModelArrayList.add(diamondShape);
                        }*/

                        for (int i = 0; i < details.length(); i++) {
                            JSONObject objectCode = details.getJSONObject(i);
                            String shape = objectCode.getString("shape");

                            JSONArray shapeDetails = objectCode.getJSONArray("details");

                            for (int j = 0; j < shapeDetails.length(); j++) {
                                JSONObject shapeObjectCodes = shapeDetails.getJSONObject(j);

                                DiamondShapeImageModel model = new DiamondShapeImageModel();

                                model.setCarat(CommonUtility.checkString(shapeObjectCodes.optString("carat")));
                                model.setImage(CommonUtility.checkString(shapeObjectCodes.optString("image")));
                                Log.e("-----------Shape-------- :", shape);
                                model.setShapeType(shape);
                                model.setSelected(false);

                                setParsingData(model, shape);

                            }
                        }
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("0")) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("4")) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
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

    void setParsingData(DiamondShapeImageModel model, String type) {
        if (type.equalsIgnoreCase(ROUND)) {
            diamondShapeImageModelArrayList.add(model);
        } else if (type.equalsIgnoreCase(PRINCESS)) {
            princessModelArrayList.add(model);
        } else if (type.equalsIgnoreCase(EMERALD)) {
            emeraldModelArrayList.add(model);
        } else if (type.equalsIgnoreCase(PEAR)) {
            pearModelArrayList.add(model);
        } else if (type.equalsIgnoreCase(HEART)) {
            heartModelArrayList.add(model);
        } else if (type.equalsIgnoreCase(OVAL)) {
            ovalModelArrayList.add(model);
        } else if (type.equalsIgnoreCase(MARQUISE)) {
            marquiseModelArrayList.add(model);
        } else if (type.equalsIgnoreCase(RADIANT)) {
            radiantModelArrayList.add(model);
        } else if (type.equalsIgnoreCase(ASSCHER)) {
            asscherModelArrayList.add(model);
        } else if (type.equalsIgnoreCase(CUSHION)) {
            cushionModelArrayList.add(model);
        }
    }

    private Bitmap bitmap; // Bitmap to hold screenshot
    WebView webViewImage, webViewVideo;

    android.app.AlertDialog alertDialog1;

    void showDiamondImageWebViewPopup(final Activity activity, final Context context, String diamondUrl) {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_web_view_popup, null);
        dialogBuilder.setView(dialogView);
        alertDialog1 = dialogBuilder.create();

        final ImageView cross_img = dialogView.findViewById(R.id.cross_img);
        final ImageView share_img = dialogView.findViewById(R.id.share_img);
        final TextView not_found_tv = dialogView.findViewById(R.id.not_found_tv);
        webViewImage = dialogView.findViewById(R.id.webView);

        if (diamondUrl.equalsIgnoreCase("")) {
            not_found_tv.setText(getResources().getString(R.string.image_not_found));
            not_found_tv.setVisibility(View.VISIBLE);
            share_img.setVisibility(View.GONE);
            webViewImage.setVisibility(View.INVISIBLE);
        } else {
            not_found_tv.setVisibility(View.GONE);
            share_img.setVisibility(View.VISIBLE);
            webViewImage.setVisibility(View.VISIBLE);

            webViewImage.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // Apply CSS to ensure the image scales within WebView boundaries
                    webViewImage.loadUrl("javascript:(function() { " +
                            "var img = document.querySelector('img'); " +
                            "if(img) { " +
                            "img.style.maxWidth = '100%'; " +
                            "img.style.height = 'auto'; " +
                            "img.style.width = 'auto'; " +
                            "document.body.style.overflow = 'hidden'; " +
                            "} " +
                            "})()");
                }

            });

            webViewImage.setInitialScale(70);
            webViewImage.getSettings().setUseWideViewPort(true);
            webViewImage.getSettings().setLoadWithOverviewMode(true);
            webViewImage.getSettings().setBuiltInZoomControls(true);
            webViewImage.getSettings().setDisplayZoomControls(false); // Disable default zoom controls

            // Set WebView background color to transparent
            webViewImage.setBackgroundResource(R.drawable.background_gradient);
            webViewImage.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
            Log.e("diamondUrl", "..3112...." + diamondUrl);
            webViewImage.loadUrl(diamondUrl);
        }

        //alertDialog1.setView(webView);

        cross_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog1.dismiss();
            }
        });


        share_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shareImage(diamondUrl);
                captureScreenshotAndShare("Image");
            }
        });

        alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog1.setCancelable(true);
        alertDialog1.setCanceledOnTouchOutside(false);
        alertDialog1.show();

    }

    //--------------------------------------------------WebView ScreenShot And Share----------------------------------------------
    private void captureScreenshotAndShare(String shareType) {
        // Capture the content of the WebView as Bitmap
        Bitmap webViewBitmap = captureWebViewContent(shareType);
        // Share the captured screenshot
        shareScreenshot(webViewBitmap);
    }

    private Bitmap captureWebViewContent(String shareType) {
        // Create a Bitmap of the WebView content
        Bitmap bitmap;
        // Check Video Type image share or Image Share and Pass WebView Object
        if (shareType.equalsIgnoreCase("Image")) {
            bitmap = Bitmap.createBitmap(webViewImage.getWidth(), webViewImage.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            webViewImage.draw(canvas);
        } else {
            bitmap = Bitmap.createBitmap(webViewVideo.getWidth(), webViewVideo.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            webViewVideo.draw(canvas);
        }
        return bitmap;
    }

    private void shareScreenshot(Bitmap bitmap) {
        if (bitmap != null) {
            try {
                // Save bitmap to a temporary file (optional)
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Screenshot", null);
                Uri imageUri = Uri.parse(path);

                // Share via Intent
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                startActivity(Intent.createChooser(shareIntent, "Share Screenshot"));
            } catch (Exception e) {
                Toast.makeText(this, "Failed to share screenshot", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Failed to capture screenshot", Toast.LENGTH_SHORT).show();
        }
    }

    //--------------------------------------------------WebView ScreenShot And Share End----------------------------------------------

    //---------------------------------------------------Take Dialog Screen Shot and Share----------------------------------------------------


    // Capture content of AlertDialog's view as Bitmap
    private Bitmap captureDialogView() {
        View dialogView = alertDialog1.getWindow().getDecorView();
        dialogView.setDrawingCacheEnabled(true); // Enable drawing cache
        Bitmap bitmap = Bitmap.createBitmap(dialogView.getDrawingCache());
        dialogView.setDrawingCacheEnabled(false); // Disable drawing cache
        return bitmap;
    }


    //----------------------------------------------------End-------------------------------------------------------

    void showDiamondVideoWebViewPopup(final Activity activity, final Context context, String diamondUrl) {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_web_view_video_popup, null);
        dialogBuilder.setView(dialogView);
        final android.app.AlertDialog alertDialog = dialogBuilder.create();

        final ImageView cross_img = dialogView.findViewById(R.id.cross_img);
        final ImageView share_img = dialogView.findViewById(R.id.share_img);
        webViewVideo = dialogView.findViewById(R.id.webView);
        final TextView title_tv = dialogView.findViewById(R.id.title_tv);
        final TextView not_found_tv = dialogView.findViewById(R.id.not_found_tv);

        share_img.setVisibility(View.GONE);

        Log.e("-------diamondUrlvideo------- : ", diamondUrl.toString());

        if (diamondUrl.equalsIgnoreCase("")) {
            not_found_tv.setText(getResources().getString(R.string.video_not_found));
            not_found_tv.setVisibility(View.VISIBLE);
            webViewVideo.setVisibility(View.INVISIBLE);
        } else {
            not_found_tv.setVisibility(View.GONE);
            webViewVideo.setVisibility(View.VISIBLE);

            title_tv.setText(getResources().getString(R.string.diamond_video));
            webViewVideo.getSettings().setJavaScriptEnabled(true);
            webViewVideo.getSettings().setAllowFileAccess(true);
            webViewVideo.getSettings().setDomStorageEnabled(true);
            webViewVideo.getSettings().setUseWideViewPort(true);
            webViewVideo.getSettings().setLoadWithOverviewMode(true);
            webViewVideo.getSettings().setBuiltInZoomControls(false);
            webViewVideo.getSettings().setDisplayZoomControls(false); // Disable default zoom controls

            // Set WebView background color to transparent
            webViewVideo.setBackgroundColor(Color.TRANSPARENT);
            webViewVideo.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

            webViewVideo.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    // Handle error loading the page here
                    Log.e("--------Errore------- : ", error.toString());
                }
            });

            //webView.loadDataWithBaseURL(diamondUrl, "https://thnk18.azureedge.net/img/images/Vision360.html?d=J429-230135", "text/html", "UTF-8", null);

            //webView.loadUrl("https://thnk18.azureedge.net/img/images/Vision360.html?d=J429-230135");
            webViewVideo.loadUrl(diamondUrl);

            // Set WebView background color to transparent
            webViewVideo.setBackgroundColor(Color.TRANSPARENT);
            webViewVideo.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

            //alertDialog.setView(webView);

        }

        cross_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        share_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureScreenshotAndShare("Video");
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    // Not Use
    void showDiamondCertificateWebViewPopup(final Activity activity, final Context context, String diamondUrl) {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_web_view_video_popup, null);
        dialogBuilder.setView(dialogView);
        final android.app.AlertDialog alertDialog = dialogBuilder.create();

        final ImageView cross_img = dialogView.findViewById(R.id.cross_img);
        final ImageView share_img = dialogView.findViewById(R.id.share_img);
        final WebView webView = dialogView.findViewById(R.id.webView);
        final TextView title_tv = dialogView.findViewById(R.id.title_tv);
        final TextView not_found_tv = dialogView.findViewById(R.id.not_found_tv);

        share_img.setVisibility(View.GONE);

        title_tv.setText(getResources().getString(R.string.certificate));

        Log.e("-------diamondUrlvideo1------- : ", diamondUrl.toString());

        // Check URL Available or Not
        if (diamondUrl.equalsIgnoreCase("")) {
            not_found_tv.setText(getResources().getString(R.string.certificate_not_found));
            not_found_tv.setVisibility(View.VISIBLE);
            webView.setVisibility(View.INVISIBLE);
        } else {
            not_found_tv.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // Apply CSS to ensure the image scales within WebView boundaries
                    webView.loadUrl("javascript:(function() { " +
                            "var img = document.querySelector('img'); " +
                            "if(img) { " +
                            "img.style.maxWidth = '100%'; " +
                            "img.style.height = 'auto'; " +
                            "img.style.width = 'auto'; " +
                            "document.body.style.overflow = 'hidden'; " +
                            "} " +
                            "})()");
                }

            });

            //webView.setInitialScale(120);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(false); // Disable default zoom controls
            // Set WebView background color to transparent
            webView.setBackgroundColor(Color.TRANSPARENT);
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
            String pdf = diamondUrl;
            // webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);;

            String extension = CommonUtility.getFileExtension(diamondUrl);
            webView.getSettings().setSupportZoom(true);
            //Log.e("------extension------ : ", extension.toString());
            if (extension.equalsIgnoreCase(".pdf")) {
                webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + diamondUrl);
            } else {
                webView.loadUrl(diamondUrl);
            }

            webView.setWebViewClient(new WebViewClient() {
                boolean checkOnPageStartedCalled = false;

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    checkOnPageStartedCalled = true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                }
            });
        }


        cross_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }


    RecyclerView recycler_carat_view, recycler_shape_view;
    ImageView hand_img;

    void showDiamondShapePopup(final Activity activity, final Context context) {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_show_diamond_shape_popup, null);
        dialogBuilder.setView(dialogView);
        final android.app.AlertDialog alertDialog = dialogBuilder.create();


        final ImageView cross_img = dialogView.findViewById(R.id.cross_img);
        final TextView title_tv = dialogView.findViewById(R.id.title_tv);
        hand_img = dialogView.findViewById(R.id.hand_img);
        recycler_carat_view = dialogView.findViewById(R.id.recycler_carat_view);
        recycler_shape_view = dialogView.findViewById(R.id.recycler_shape_view);
        title_tv.setText(getResources().getString(R.string.diamond_size_preview));

        recycler_carat_view.setHasFixedSize(true);
        recycler_shape_view.setHasFixedSize(true);

        LinearLayoutManager layoutManagerCaratView = new LinearLayoutManager(activity);
        layoutManagerCaratView.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_carat_view.setLayoutManager(layoutManagerCaratView);
        recycler_carat_view.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManagerShapeView = new LinearLayoutManager(activity);
        layoutManagerShapeView.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_shape_view.setLayoutManager(layoutManagerShapeView);
        recycler_shape_view.setNestedScrollingEnabled(false);

        // Default First Position Selected Here.
        if (diamondShapeImageModelArrayList.size() > 0) {

            Log.e("-------ShapeType----- : ", diamondShapeImageModelArrayList.get(0).getShapeType().toString());

            // Round, Princess, Emerald, Heart, Radiant, Oval, Pear, Marquise, Asscher, Cushion
            if (shapeTypeSelected.equalsIgnoreCase(ROUND)) {
                setSelectedCaratDiamondImage(diamondShapeImageModelArrayList.get(0).getImage());

                diamondShapeImageModelArrayList.get(0).setSelected(true);
                shapeImageArrayList.get(0).setSelected(true);
            } else if (shapeTypeSelected.equalsIgnoreCase(PRINCESS)) {
                setSelectedCaratDiamondImage(princessModelArrayList.get(0).getImage());

                princessModelArrayList.get(0).setSelected(true);
                shapeImageArrayList.get(1).setSelected(true);
            } else if (shapeTypeSelected.equalsIgnoreCase(EMERALD)) {
                setSelectedCaratDiamondImage(emeraldModelArrayList.get(0).getImage());

                emeraldModelArrayList.get(0).setSelected(true);
                shapeImageArrayList.get(2).setSelected(true);
            } else if (shapeTypeSelected.equalsIgnoreCase(HEART)) {
                setSelectedCaratDiamondImage(heartModelArrayList.get(0).getImage());

                heartModelArrayList.get(0).setSelected(true);
                shapeImageArrayList.get(3).setSelected(true);
            } else if (shapeTypeSelected.equalsIgnoreCase(HEART)) {
                setSelectedCaratDiamondImage(radiantModelArrayList.get(0).getImage());

                radiantModelArrayList.get(0).setSelected(true);
                shapeImageArrayList.get(4).setSelected(true);
            } else if (shapeTypeSelected.equalsIgnoreCase(OVAL)) {
                setSelectedCaratDiamondImage(ovalModelArrayList.get(0).getImage());

                ovalModelArrayList.get(0).setSelected(true);
                shapeImageArrayList.get(5).setSelected(true);
            } else if (shapeTypeSelected.equalsIgnoreCase(PEAR)) {
                setSelectedCaratDiamondImage(pearModelArrayList.get(0).getImage());

                pearModelArrayList.get(0).setSelected(true);
                shapeImageArrayList.get(6).setSelected(true);
            } else if (shapeTypeSelected.equalsIgnoreCase(MARQUISE)) {
                setSelectedCaratDiamondImage(marquiseModelArrayList.get(0).getImage());

                marquiseModelArrayList.get(0).setSelected(true);
                shapeImageArrayList.get(7).setSelected(true);
            } else if (shapeTypeSelected.equalsIgnoreCase(ASSCHER)) {
                setSelectedCaratDiamondImage(asscherModelArrayList.get(0).getImage());

                asscherModelArrayList.get(0).setSelected(true);
                shapeImageArrayList.get(8).setSelected(true);
            } else if (shapeTypeSelected.equalsIgnoreCase(CUSHION)) {
                setSelectedCaratDiamondImage(cushionModelArrayList.get(0).getImage());

                cushionModelArrayList.get(0).setSelected(true);
                shapeImageArrayList.get(9).setSelected(true);
            } else {
                // Default
                setSelectedCaratDiamondImage(diamondShapeImageModelArrayList.get(0).getImage());

                diamondShapeImageModelArrayList.get(0).setSelected(true);
                shapeImageArrayList.get(0).setSelected(true);
            }
        } else {
            //setSelectedCaratDiamondImage(diamondShapeImageModelArrayList.get(0).getImage());
        }

        if (shapeTypeSelected.equalsIgnoreCase(ROUND)) {
            diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(diamondShapeImageModelArrayList, context, this);
            recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
        } else if (shapeTypeSelected.equalsIgnoreCase(PRINCESS)) {
            diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(princessModelArrayList, context, this);
            recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
        } else if (shapeTypeSelected.equalsIgnoreCase(EMERALD)) {
            diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(emeraldModelArrayList, context, this);
            recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
        } else if (shapeTypeSelected.equalsIgnoreCase(HEART)) {
            diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(heartModelArrayList, context, this);
            recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
        } else if (shapeTypeSelected.equalsIgnoreCase(RADIANT)) {
            diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(radiantModelArrayList, context, this);
            recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
        } else if (shapeTypeSelected.equalsIgnoreCase(OVAL)) {
            diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(ovalModelArrayList, context, this);
            recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
        } else if (shapeTypeSelected.equalsIgnoreCase(PEAR)) {
            diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(pearModelArrayList, context, this);
            recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
        } else if (shapeTypeSelected.equalsIgnoreCase(MARQUISE)) {
            diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(marquiseModelArrayList, context, this);
            recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
        } else if (shapeTypeSelected.equalsIgnoreCase(ASSCHER)) {
            diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(asscherModelArrayList, context, this);
            recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
        } else if (shapeTypeSelected.equalsIgnoreCase(CUSHION)) {
            diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(cushionModelArrayList, context, this);
            recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
        }
        shapeImageListAdapter = new DiamondShapeImageListAdapter(shapeImageArrayList, context, this);
        recycler_shape_view.setAdapter(shapeImageListAdapter);

        cross_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    void setSelectedCaratDiamondImage(String imageURL) {
        Picasso.with(context)
                .load(imageURL)
                .into(hand_img);
    }

    @Override
    public void itemClick(int position, String action) {
        if (action.equalsIgnoreCase("shapeImage")) {
            boolean shouldSelect = !shapeImageArrayList.get(position).isSelected();
            for (int i = 0; i < shapeImageArrayList.size(); i++) {
                shapeImageArrayList.get(i).setSelected(false);
            }
            shapeImageArrayList.get(position).setSelected(shouldSelect);

            // Clear existing data for new selection
            //diamondShapeImageModelArrayList.clear();
            String selectedShape = shapeImageArrayList.get(position).getAttribCode(); // Assuming you have a method to get shape name

            shapeTypeSelected = shapeImageArrayList.get(position).getAttribCode();
            Log.e("---------shapeTypeSelected--------- : ", shapeTypeSelected);
            if (selectedShape.equalsIgnoreCase(ROUND)) {
                setAllItemsToNotSelected(princessModelArrayList);
                setAllItemsToNotSelected(emeraldModelArrayList);
                setAllItemsToNotSelected(pearModelArrayList);
                setAllItemsToNotSelected(heartModelArrayList);
                setAllItemsToNotSelected(ovalModelArrayList);
                setAllItemsToNotSelected(marquiseModelArrayList);
                setAllItemsToNotSelected(radiantModelArrayList);
                setAllItemsToNotSelected(asscherModelArrayList);
                setAllItemsToNotSelected(cushionModelArrayList);

                setSelectedCaratDiamondImage(diamondShapeImageModelArrayList.get(0).getImage());
                diamondShapeImageModelArrayList.get(0).setSelected(true);

                diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(diamondShapeImageModelArrayList, context, this);
                recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
            } else if (selectedShape.equalsIgnoreCase(PRINCESS)) {
                setAllItemsToNotSelected(diamondShapeImageModelArrayList);
                setAllItemsToNotSelected(emeraldModelArrayList);
                setAllItemsToNotSelected(pearModelArrayList);
                setAllItemsToNotSelected(heartModelArrayList);
                setAllItemsToNotSelected(ovalModelArrayList);
                setAllItemsToNotSelected(marquiseModelArrayList);
                setAllItemsToNotSelected(radiantModelArrayList);
                setAllItemsToNotSelected(asscherModelArrayList);
                setAllItemsToNotSelected(cushionModelArrayList);

                setSelectedCaratDiamondImage(princessModelArrayList.get(0).getImage());
                princessModelArrayList.get(0).setSelected(true);

                //diamondCaratTypeListAdapter.notifyDataSetChanged();
                diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(princessModelArrayList, context, this);
                recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
            } else if (shapeTypeSelected.equalsIgnoreCase(EMERALD)) {
                setAllItemsToNotSelected(diamondShapeImageModelArrayList);
                setAllItemsToNotSelected(princessModelArrayList);
                setAllItemsToNotSelected(pearModelArrayList);
                setAllItemsToNotSelected(heartModelArrayList);
                setAllItemsToNotSelected(ovalModelArrayList);
                setAllItemsToNotSelected(marquiseModelArrayList);
                setAllItemsToNotSelected(radiantModelArrayList);
                setAllItemsToNotSelected(asscherModelArrayList);
                setAllItemsToNotSelected(cushionModelArrayList);

                setSelectedCaratDiamondImage(emeraldModelArrayList.get(0).getImage());
                emeraldModelArrayList.get(0).setSelected(true);

                diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(emeraldModelArrayList, context, this);
                recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
            } else if (shapeTypeSelected.equalsIgnoreCase(HEART)) {
                setAllItemsToNotSelected(diamondShapeImageModelArrayList);
                setAllItemsToNotSelected(princessModelArrayList);
                setAllItemsToNotSelected(pearModelArrayList);
                setAllItemsToNotSelected(emeraldModelArrayList);
                setAllItemsToNotSelected(ovalModelArrayList);
                setAllItemsToNotSelected(marquiseModelArrayList);
                setAllItemsToNotSelected(radiantModelArrayList);
                setAllItemsToNotSelected(asscherModelArrayList);
                setAllItemsToNotSelected(cushionModelArrayList);

                setSelectedCaratDiamondImage(heartModelArrayList.get(0).getImage());
                heartModelArrayList.get(0).setSelected(true);

                diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(heartModelArrayList, context, this);
                recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
            } else if (shapeTypeSelected.equalsIgnoreCase(RADIANT)) {
                setAllItemsToNotSelected(diamondShapeImageModelArrayList);
                setAllItemsToNotSelected(princessModelArrayList);
                setAllItemsToNotSelected(pearModelArrayList);
                setAllItemsToNotSelected(emeraldModelArrayList);
                setAllItemsToNotSelected(heartModelArrayList);
                setAllItemsToNotSelected(ovalModelArrayList);
                setAllItemsToNotSelected(marquiseModelArrayList);
                setAllItemsToNotSelected(asscherModelArrayList);
                setAllItemsToNotSelected(cushionModelArrayList);

                setSelectedCaratDiamondImage(radiantModelArrayList.get(0).getImage());
                radiantModelArrayList.get(0).setSelected(true);

                diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(radiantModelArrayList, context, this);
                recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
            } else if (shapeTypeSelected.equalsIgnoreCase(OVAL)) {
                setAllItemsToNotSelected(diamondShapeImageModelArrayList);
                setAllItemsToNotSelected(princessModelArrayList);
                setAllItemsToNotSelected(pearModelArrayList);
                setAllItemsToNotSelected(emeraldModelArrayList);
                setAllItemsToNotSelected(heartModelArrayList);
                setAllItemsToNotSelected(radiantModelArrayList);
                setAllItemsToNotSelected(marquiseModelArrayList);
                setAllItemsToNotSelected(asscherModelArrayList);
                setAllItemsToNotSelected(cushionModelArrayList);

                setSelectedCaratDiamondImage(ovalModelArrayList.get(0).getImage());
                ovalModelArrayList.get(0).setSelected(true);

                diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(ovalModelArrayList, context, this);
                recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
            } else if (shapeTypeSelected.equalsIgnoreCase(PEAR)) {
                setAllItemsToNotSelected(diamondShapeImageModelArrayList);
                setAllItemsToNotSelected(princessModelArrayList);
                setAllItemsToNotSelected(emeraldModelArrayList);
                setAllItemsToNotSelected(heartModelArrayList);
                setAllItemsToNotSelected(ovalModelArrayList);
                setAllItemsToNotSelected(radiantModelArrayList);
                setAllItemsToNotSelected(marquiseModelArrayList);
                setAllItemsToNotSelected(asscherModelArrayList);
                setAllItemsToNotSelected(cushionModelArrayList);

                setSelectedCaratDiamondImage(pearModelArrayList.get(0).getImage());
                pearModelArrayList.get(0).setSelected(true);

                diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(pearModelArrayList, context, this);
                recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
            } else if (shapeTypeSelected.equalsIgnoreCase(MARQUISE)) {
                setAllItemsToNotSelected(diamondShapeImageModelArrayList);
                setAllItemsToNotSelected(princessModelArrayList);
                setAllItemsToNotSelected(emeraldModelArrayList);
                setAllItemsToNotSelected(heartModelArrayList);
                setAllItemsToNotSelected(ovalModelArrayList);
                setAllItemsToNotSelected(radiantModelArrayList);
                setAllItemsToNotSelected(pearModelArrayList);
                setAllItemsToNotSelected(asscherModelArrayList);
                setAllItemsToNotSelected(cushionModelArrayList);

                setSelectedCaratDiamondImage(marquiseModelArrayList.get(0).getImage());
                marquiseModelArrayList.get(0).setSelected(true);

                diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(marquiseModelArrayList, context, this);
                recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
            } else if (shapeTypeSelected.equalsIgnoreCase(ASSCHER)) {
                setAllItemsToNotSelected(diamondShapeImageModelArrayList);
                setAllItemsToNotSelected(princessModelArrayList);
                setAllItemsToNotSelected(emeraldModelArrayList);
                setAllItemsToNotSelected(heartModelArrayList);
                setAllItemsToNotSelected(ovalModelArrayList);
                setAllItemsToNotSelected(radiantModelArrayList);
                setAllItemsToNotSelected(pearModelArrayList);
                setAllItemsToNotSelected(marquiseModelArrayList);
                setAllItemsToNotSelected(cushionModelArrayList);

                setSelectedCaratDiamondImage(asscherModelArrayList.get(0).getImage());
                asscherModelArrayList.get(0).setSelected(true);

                diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(asscherModelArrayList, context, this);
                recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
            } else if (shapeTypeSelected.equalsIgnoreCase(CUSHION)) {
                setAllItemsToNotSelected(diamondShapeImageModelArrayList);
                setAllItemsToNotSelected(princessModelArrayList);
                setAllItemsToNotSelected(emeraldModelArrayList);
                setAllItemsToNotSelected(heartModelArrayList);
                setAllItemsToNotSelected(ovalModelArrayList);
                setAllItemsToNotSelected(radiantModelArrayList);
                setAllItemsToNotSelected(pearModelArrayList);
                setAllItemsToNotSelected(marquiseModelArrayList);
                setAllItemsToNotSelected(asscherModelArrayList);

                setSelectedCaratDiamondImage(cushionModelArrayList.get(0).getImage());
                cushionModelArrayList.get(0).setSelected(true);

                diamondCaratTypeListAdapter = new DiamondCaratTypeListAdapter(cushionModelArrayList, context, this);
                recycler_carat_view.setAdapter(diamondCaratTypeListAdapter);
            }

            shapeImageListAdapter.notifyDataSetChanged();
        } else if (action.equalsIgnoreCase("caratType")) {
            //String selectedShape = shapeImageArrayList.get(position).getAttribCode(); // Assuming you have a method to get shape name
            //shapeTypeSelected = shapeImageArrayList.get(position).getAttribCode();

            // Log.e("----diamondShapeImageModelArrayList--- : ", "Click Carart : " + diamondShapeImageModelArrayList.get(position).getCarat());


            if (shapeTypeSelected.equalsIgnoreCase(ROUND)) {
                setArrayListData(position, diamondShapeImageModelArrayList);
            } else if (shapeTypeSelected.equalsIgnoreCase(PRINCESS)) {
                setArrayListData(position, princessModelArrayList);
            } else if (shapeTypeSelected.equalsIgnoreCase(EMERALD)) {
                setArrayListData(position, emeraldModelArrayList);
            } else if (shapeTypeSelected.equalsIgnoreCase(PEAR)) {
                setArrayListData(position, pearModelArrayList);
            } else if (shapeTypeSelected.equalsIgnoreCase(HEART)) {
                setArrayListData(position, heartModelArrayList);
            } else if (shapeTypeSelected.equalsIgnoreCase(OVAL)) {
                setArrayListData(position, ovalModelArrayList);
            } else if (shapeTypeSelected.equalsIgnoreCase(MARQUISE)) {
                setArrayListData(position, marquiseModelArrayList);
            } else if (shapeTypeSelected.equalsIgnoreCase(RADIANT)) {
                setArrayListData(position, radiantModelArrayList);
            } else if (shapeTypeSelected.equalsIgnoreCase(ASSCHER)) {
                setArrayListData(position, asscherModelArrayList);
            } else if (shapeTypeSelected.equalsIgnoreCase(CUSHION)) {
                setArrayListData(position, cushionModelArrayList);
            }

            // Set Image Using Carat.
            if (shapeTypeSelected.equalsIgnoreCase(ROUND)) {
                setSelectedCaratDiamondImage(diamondShapeImageModelArrayList.get(position).getImage());
            } else if (shapeTypeSelected.equalsIgnoreCase(PRINCESS)) {
                setSelectedCaratDiamondImage(princessModelArrayList.get(position).getImage());
            } else if (shapeTypeSelected.equalsIgnoreCase(EMERALD)) {
                setSelectedCaratDiamondImage(emeraldModelArrayList.get(position).getImage());
            } else if (shapeTypeSelected.equalsIgnoreCase(PEAR)) {
                setSelectedCaratDiamondImage(pearModelArrayList.get(position).getImage());
            } else if (shapeTypeSelected.equalsIgnoreCase(HEART)) {
                setSelectedCaratDiamondImage(heartModelArrayList.get(position).getImage());
            } else if (shapeTypeSelected.equalsIgnoreCase(OVAL)) {
                setSelectedCaratDiamondImage(ovalModelArrayList.get(position).getImage());
            } else if (shapeTypeSelected.equalsIgnoreCase(MARQUISE)) {
                setSelectedCaratDiamondImage(marquiseModelArrayList.get(position).getImage());
            } else if (shapeTypeSelected.equalsIgnoreCase(RADIANT)) {
                setSelectedCaratDiamondImage(radiantModelArrayList.get(position).getImage());
            } else if (shapeTypeSelected.equalsIgnoreCase(ASSCHER)) {
                setSelectedCaratDiamondImage(asscherModelArrayList.get(position).getImage());
            } else if (shapeTypeSelected.equalsIgnoreCase(CUSHION)) {
                setSelectedCaratDiamondImage(cushionModelArrayList.get(position).getImage());
            }

            diamondCaratTypeListAdapter.notifyDataSetChanged();
        }
    }

    private void setAllItemsToNotSelected(ArrayList<DiamondShapeImageModel> list) {
        for (DiamondShapeImageModel item : list) {
            item.setSelected(false);
        }
    }

    void setArrayListData(int position, ArrayList<DiamondShapeImageModel> arrayList) {
        boolean shouldSelect = !arrayList.get(position).isSelected();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).setSelected(false);
        }
        arrayList.get(position).setSelected(shouldSelect);
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

        if (userPincode.equalsIgnoreCase("")) {
            enter_pincode_et.setHint(getResources().getString(R.string.enter_pin_code));
        } else {
            enter_pincode_et.setText("");
            // if City is blank sow only pin-code
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
                    Toast.makeText(activity, "Please Enter PinCode", Toast.LENGTH_SHORT).show();
                } else {
                    if (!enter_pincode_et.getText().toString().equalsIgnoreCase("")) {
                        if (enter_pincode_et.getText().toString().equalsIgnoreCase("")) {
                            select_lbl.setText(getResources().getString(R.string.select));
                        } else {
                            select_lbl.setText(getResources().getString(R.string.change));
                        }
                        pincode_tv.setText(getResources().getString(R.string.delivering_to) + " " + enter_pincode_et.getText().toString().trim());

                        CommonUtility.setGlobalString(context, "user_select_pincode", enter_pincode_et.getText().toString());
                        CommonUtility.setGlobalString(context, "user_select_pincode_city", "");

                    } else {
                    }

                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    public String limitCharacters(String text, int maxChars) {
        if (text.length() > maxChars) {
            return text.substring(0, maxChars) + "..";
        } else {
            return text;
        }
    }

    // Not Use
    void orderSuccessfullyPopup(final Activity activity, final Context context) {
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

        title.setText("" + context.getResources().getString(R.string.app_name));

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
                overridePendingTransition(0, 0);
                alertDialog.dismiss();
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