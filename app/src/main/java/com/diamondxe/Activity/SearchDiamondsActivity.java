package com.diamondxe.Activity;

import static com.diamondxe.ApiCalling.ApiConstants.CART_FRAGMENT;
import static com.diamondxe.ApiCalling.ApiConstants.CATEGORY_FRAGMENT;
import static com.diamondxe.ApiCalling.ApiConstants.INDIA_CURRENCY_CODE;
import static com.diamondxe.ApiCalling.ApiConstants.USER_BUYER;
import static com.diamondxe.ApiCalling.ApiConstants.USER_DEALER;
import static com.diamondxe.ApiCalling.ApiConstants.WISHLIST_FRAGMENT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Adapter.CertificateTypeListAdapter;
import com.diamondxe.Adapter.ClarityTypeListAdapter;
import com.diamondxe.Adapter.ColorTypeListAdapter;

import com.diamondxe.Adapter.FancyColorTypeListAdapter;
import com.diamondxe.Adapter.FluoRescenceTypeListAdapter;
import com.diamondxe.Adapter.MakeTypeListAdapter;
import com.diamondxe.Adapter.ShapeImageListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.AttributeDetailsModel;
import com.diamondxe.Beans.ShapeImageModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchDiamondsActivity extends SuperActivity implements RecyclerInterface {

    private ImageView back_img, bottom_search_icon;
    private TextView natural_tv, grown_tv, best_tv, medium_tv, low_tv, from_price_tv, to_price_tv, carat_from_tv, carat_to_tv,title_tv,yes_tv,
            no_tv,advance_filters_tv, search_diamond_error_tv,clear_all_filter;

    private RelativeLayout home_rel, category_rel, wishlist_rel, cart_rel, account_rel;
    private ImageView home_img, categories_img, wish_img, cart_img, account_img;
    private TextView home_tv, categories_tv, wish_tv, cart_tv, account_tv, cart_count_tv, wish_list_count_tv;

    private EditText search_et, from_price_et, to_price_et, carat_from_et, carat_to_et;
    private RelativeLayout search_circle_rel, all_img_rel, container_body;
    private CardView all_img_card, card_view_natural, card_view_grown, card_view_best, card_view_medium, card_view_low,search_circle_card;

    String diamondQualitySelected = "", makeSelectedValue="";
    boolean isBestSelected = false; // Add a flag to track the selection state
    boolean isMediumSelected = false; // Add a flag to track the selection state
    boolean isLowSelected = false; // Add a flag to track the selection state

    boolean isReturnableYes = false;
    boolean isReturnableNo = false;

    private RadioGroup color_radio_group;
    private RadioButton white_radio, fancy_radio;
    String colorSelectedValue="",select_color_type="",clearAllClick="";

    ShapeImageListAdapter shapeImageListAdapter;
    ColorTypeListAdapter colorTypeListAdapter;
    FancyColorTypeListAdapter fancyColorTypeListAdapter;
    ClarityTypeListAdapter clarityTypeListAdapter;

    CertificateTypeListAdapter certificateTypeListAdapter;
    FluoRescenceTypeListAdapter fluoRescenceTypeListAdapter;

    String BEST = "best";
    String MEDIUM = "medium";
    String LOW = "low";
    String WHITE = "white";
    String FANCY = "fancy";
    MakeTypeListAdapter makeTypeListAdapter;

    private RecyclerView recycler_color_view, recycler_clarity_view, recycler_certificate_view, recycler_fluorescence_view,recycler_make_view,
            recycler_shape_view;
    public ArrayList<ShapeImageModel> shapeImageArrayList;
    public ArrayList<AttributeDetailsModel> colorTypeArrayList;
    public ArrayList<AttributeDetailsModel> fancyColorTypeArrayList;
    public ArrayList<AttributeDetailsModel> clarityTypeArrayList;
    public ArrayList<AttributeDetailsModel> certificateTypeArrayList;
    public ArrayList<AttributeDetailsModel> rescenceTypeModelArrayList;
    public ArrayList<AttributeDetailsModel> makeTypeArrayList;

    private Activity activity;
    private Context context;

    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;

    String user_login = "";

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_diamonds);

        context = activity = this;

        colorTypeArrayList = new ArrayList<>();
        fancyColorTypeArrayList = new ArrayList<>();
        clarityTypeArrayList = new ArrayList<>();
        certificateTypeArrayList = new ArrayList<>();
        rescenceTypeModelArrayList = new ArrayList<>();
        makeTypeArrayList = new ArrayList<>();
        shapeImageArrayList = new ArrayList<>();
        Constant.cutTypeArrayList = new ArrayList<>();
        Constant.polishTypeArrayList = new ArrayList<>();
        Constant.symmertyTypeArrayList = new ArrayList<>();
        Constant.technologyTypeArrayList = new ArrayList<>();
        Constant.fancyColorIntensityArrayList = new ArrayList<>();
        Constant.fancyColorOverToneArrayList = new ArrayList<>();
        Constant.tableDataPercantageArrayList = new ArrayList<>();
        Constant.depthDataPercantageArrayList = new ArrayList<>();
        Constant.crowmArrayList = new ArrayList<>();
        Constant.pavillionArrayList = new ArrayList<>();
        Constant.eyeCleanArrayList = new ArrayList<>();
        Constant.shadeArrayList = new ArrayList<>();
        Constant.lusterArrayList = new ArrayList<>();

        Constant.colorTypeFilterApploedArrayList = new ArrayList<>();

        title_tv = findViewById(R.id.title_tv);
        container_body = findViewById(R.id.container_body);

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
        cart_count_tv = findViewById(R.id.cart_count_tv);
        wish_list_count_tv = findViewById(R.id.wish_list_count_tv);

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

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        search_et = findViewById(R.id.search_et);
        from_price_et = findViewById(R.id.from_price_et);
        to_price_et = findViewById(R.id.to_price_et);

        search_diamond_error_tv = findViewById(R.id.search_diamond_error_tv);

        natural_tv = findViewById(R.id.natural_tv);
        natural_tv.setOnClickListener(this);

        grown_tv = findViewById(R.id.grown_tv);
        grown_tv.setOnClickListener(this);

        card_view_natural = findViewById(R.id.card_view_natural);
        card_view_grown = findViewById(R.id.card_view_grown);

        card_view_best = findViewById(R.id.card_view_best);
        card_view_medium = findViewById(R.id.card_view_medium);
        card_view_low = findViewById(R.id.card_view_low);

        clear_all_filter = findViewById(R.id.clear_all_filter);
        clear_all_filter.setOnClickListener(this);

        best_tv = findViewById(R.id.best_tv);
        best_tv.setOnClickListener(this);

        medium_tv = findViewById(R.id.medium_tv);
        medium_tv.setOnClickListener(this);

        low_tv = findViewById(R.id.low_tv);
        low_tv.setOnClickListener(this);

        from_price_tv = findViewById(R.id.from_price_tv);
        to_price_tv = findViewById(R.id.to_price_tv);

        from_price_et = findViewById(R.id.from_price_et);
        to_price_et = findViewById(R.id.to_price_et);

        carat_from_tv = findViewById(R.id.carat_from_tv);
        carat_to_tv = findViewById(R.id.carat_to_tv);

        carat_from_et = findViewById(R.id.carat_from_et);
        carat_to_et = findViewById(R.id.carat_to_et);

        // Set Title Name According to Diamond Type
        if(Constant.searchTitleName!=null && Constant.searchTitleName!="")
        {
            if(Constant.searchTitleName.equalsIgnoreCase("Solitaires"))
            {
                title_tv.setText(getResources().getString(R.string.search_lbl) + " " + Constant.searchTitleName);
            }
            else{
                title_tv.setText(Constant.searchTitleName);
            }
        }
        else{
            title_tv.setText(getResources().getString(R.string.naturalDiamonds));
        }

        // Check Diamond Type Search and Select Tab
        if(Constant.searchType.equalsIgnoreCase(ApiConstants.NATURAL))
        {
            Constant.searchType = ApiConstants.NATURAL;
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
        }
        else if(Constant.searchType.equalsIgnoreCase(ApiConstants.LAB_GROWN))
        {
            Constant.searchType = ApiConstants.LAB_GROWN;
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
        }


        color_radio_group = findViewById(R.id.color_radio_group);
        white_radio = findViewById(R.id.white_radio);
        fancy_radio = findViewById(R.id.fancy_radio);

        Constant.colorType = WHITE; // Default White

        yes_tv = findViewById(R.id.yes_tv);
        yes_tv.setOnClickListener(this);

        no_tv = findViewById(R.id.no_tv);
        no_tv.setOnClickListener(this);

        advance_filters_tv = findViewById(R.id.advance_filters_tv);
        advance_filters_tv.setOnClickListener(this);


        bottom_search_icon = findViewById(R.id.bottom_search_icon);
        search_circle_rel = findViewById(R.id.search_circle_rel);
        search_circle_rel.setOnClickListener(this);

        recycler_shape_view = findViewById(R.id.recycler_shape_view);
        recycler_shape_view.setOnClickListener(this);

        LinearLayoutManager layoutManagerShapeView = new LinearLayoutManager(activity);
        layoutManagerShapeView.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_shape_view.setLayoutManager(layoutManagerShapeView);
        recycler_shape_view.setNestedScrollingEnabled(false);

        recycler_color_view = findViewById(R.id.recycler_color_view);
        recycler_color_view.setHasFixedSize(true);

        LinearLayoutManager layoutManagerColorView = new LinearLayoutManager(activity);
        layoutManagerColorView.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_color_view.setLayoutManager(layoutManagerColorView);
        recycler_color_view.setNestedScrollingEnabled(false);

        recycler_clarity_view = findViewById(R.id.recycler_clarity_view);
        recycler_clarity_view.setHasFixedSize(true);

        LinearLayoutManager layoutManagerClarityView = new LinearLayoutManager(activity);
        layoutManagerClarityView.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_clarity_view.setLayoutManager(layoutManagerClarityView);
        recycler_clarity_view.setNestedScrollingEnabled(false);

        recycler_certificate_view = findViewById(R.id.recycler_certificate_view);
        recycler_certificate_view.setHasFixedSize(true);

        LinearLayoutManager layoutManagerCertificateView = new LinearLayoutManager(activity);
        layoutManagerCertificateView.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_certificate_view.setLayoutManager(layoutManagerCertificateView);
        recycler_certificate_view.setNestedScrollingEnabled(false);

        recycler_fluorescence_view = findViewById(R.id.recycler_fluorescence_view);
        recycler_fluorescence_view.setHasFixedSize(true);

        LinearLayoutManager layoutManagerFluorescenceView = new LinearLayoutManager(activity);
        layoutManagerFluorescenceView.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_fluorescence_view.setLayoutManager(layoutManagerFluorescenceView);
        recycler_fluorescence_view.setNestedScrollingEnabled(false);

        recycler_make_view = findViewById(R.id.recycler_make_view);
        recycler_make_view.setHasFixedSize(true);

        LinearLayoutManager layoutManagerMakeView = new LinearLayoutManager(activity);
        layoutManagerMakeView.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_make_view.setLayoutManager(layoutManagerMakeView);
        recycler_make_view.setNestedScrollingEnabled(false);


        ShapeImageModel shapeImageModel = new ShapeImageModel();
        shapeImageModel.setDrawable(R.mipmap.all);
        shapeImageModel.setAttribCode("All");
        shapeImageModel.setSelected(false);
        shapeImageModel.setFirstPosition(true);
        shapeImageArrayList.add(shapeImageModel);

        ShapeImageModel shapeImageModel1 = new ShapeImageModel();
        shapeImageModel1.setDrawable(R.mipmap.round);
        shapeImageModel1.setAttribCode("Round");
        shapeImageModel1.setSelected(false);
        shapeImageModel1.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageModel1);

        ShapeImageModel shapeImageMode2 = new ShapeImageModel();
        shapeImageMode2.setDrawable(R.mipmap.princess);
        shapeImageMode2.setAttribCode("Princess");
        shapeImageMode2.setSelected(false);
        shapeImageMode2.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode2);

        ShapeImageModel shapeImageMode3 = new ShapeImageModel();
        shapeImageMode3.setDrawable(R.mipmap.emerald);
        shapeImageMode3.setAttribCode("Emerald");
        shapeImageMode3.setSelected(false);
        shapeImageMode3.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode3);

        ShapeImageModel shapeImageMode4 = new ShapeImageModel();
        shapeImageMode4.setDrawable(R.mipmap.heart);
        shapeImageMode4.setAttribCode("Heart");
        shapeImageMode4.setSelected(false);
        shapeImageMode4.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode4);

        ShapeImageModel shapeImageMode5 = new ShapeImageModel();
        shapeImageMode5.setDrawable(R.mipmap.radiant);
        shapeImageMode5.setAttribCode("Radiant");
        shapeImageMode5.setSelected(false);
        shapeImageMode5.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode5);

        ShapeImageModel shapeImageMode6 = new ShapeImageModel();
        shapeImageMode6.setDrawable(R.mipmap.oval);
        shapeImageMode6.setAttribCode("Oval");
        shapeImageMode6.setSelected(false);
        shapeImageMode6.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode6);

        ShapeImageModel shapeImageMode7 = new ShapeImageModel();
        shapeImageMode7.setDrawable(R.mipmap.pear);
        shapeImageMode7.setAttribCode("Pear");
        shapeImageMode7.setSelected(false);
        shapeImageMode7.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode7);

        ShapeImageModel shapeImageMode8 = new ShapeImageModel();
        shapeImageMode8.setDrawable(R.mipmap.marquise);
        shapeImageMode8.setAttribCode("Marquise");
        shapeImageMode8.setSelected(false);
        shapeImageMode8.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode8);

        ShapeImageModel shapeImageMode9 = new ShapeImageModel();
        shapeImageMode9.setDrawable(R.mipmap.asscher);
        shapeImageMode9.setAttribCode("Asscher");
        shapeImageMode9.setSelected(false);
        shapeImageMode9.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode9);

        ShapeImageModel shapeImageMode10 = new ShapeImageModel();
        shapeImageMode10.setDrawable(R.mipmap.cushion);
        shapeImageMode10.setAttribCode("Cushion");
        shapeImageMode10.setSelected(false);
        shapeImageMode10.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode10);

        ShapeImageModel shapeImageMode11 = new ShapeImageModel();
        shapeImageMode11.setDrawable(R.mipmap.others);
        shapeImageMode11.setAttribCode("Other");
        shapeImageMode11.setSelected(false);
        shapeImageMode11.setFirstPosition(false);
        shapeImageArrayList.add(shapeImageMode11);

        shapeImageListAdapter = new ShapeImageListAdapter(shapeImageArrayList,context,this);
        recycler_shape_view.setAdapter(shapeImageListAdapter);

        bottom_search_icon.setBackgroundResource(R.mipmap.bottom_search);
        // Search Zoom-In and Zoom-Out Animation
        CommonUtility.startZoomAnimation(bottom_search_icon);

        color_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.white_radio)
                {
                    select_color_type = WHITE;
                    Constant.colorType = WHITE;

                    for (int i = 0; i < colorTypeArrayList.size(); i++)
                    {
                     colorTypeArrayList.get(i).setSelected(false);
                    }
                    setWhiteColorAdapter();

                } else if (checkedId == R.id.fancy_radio)
                {
                    select_color_type = FANCY;
                    Constant.colorType = FANCY;

                    for (int i = 0; i < fancyColorTypeArrayList.size(); i++)
                    {
                        fancyColorTypeArrayList.get(i).setSelected(false);
                    }

                    setFancyAdapter();
                }
            }
        });

        Gson gson = new Gson();

        // Color Type
        Type typeColor = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retriveColorTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "colorArrayList"), typeColor);
        if(retriveColorTypeArrayList!=null && retriveColorTypeArrayList.size()>0)
        {
            colorTypeArrayList = retriveColorTypeArrayList;
            colorTypeListAdapter = new ColorTypeListAdapter(colorTypeArrayList,context,this);
            recycler_color_view.setAdapter(colorTypeListAdapter);
        }

        // Fancy Color Type
        Type typeFancyColor = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retriveFancyColorTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "fancyColorArrayList"), typeFancyColor);
        //fancyColorTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "fancyColorArrayList"), typeFancyColor);
        if(retriveFancyColorTypeArrayList!=null && retriveFancyColorTypeArrayList.size()>0)
        {
            fancyColorTypeArrayList = retriveFancyColorTypeArrayList;
           // fancyColorTypeListAdapter = new FancyColorTypeListAdapter(fancyColorTypeArrayList,context,this);
           // recycler_color_view.setAdapter(fancyColorTypeListAdapter);
        }

        // Clarity Type
        Type typeClarity = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retrievedClarityTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "clarityArrayList"), typeClarity);
        //clarityTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "clarityArrayList"), typeClarity);
        if(retrievedClarityTypeArrayList!=null && retrievedClarityTypeArrayList.size()>0)
        {
            clarityTypeArrayList = retrievedClarityTypeArrayList;
            clarityTypeListAdapter = new ClarityTypeListAdapter(clarityTypeArrayList,context,this);
            recycler_clarity_view.setAdapter(clarityTypeListAdapter);
        }

        // Certificate Type
        Type typeCertificate = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retrievedCertificateTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "certificateArrayList"), typeCertificate);
        //certificateTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "certificateArrayList"), typeCertificate);
        if(retrievedCertificateTypeArrayList!=null && retrievedCertificateTypeArrayList.size()>0)
        {
            certificateTypeArrayList = retrievedCertificateTypeArrayList;
            certificateTypeListAdapter = new CertificateTypeListAdapter(certificateTypeArrayList,context,this);
            recycler_certificate_view.setAdapter(certificateTypeListAdapter);
        }

        // Fulo Type
        Type typeFluo = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retrievedRescenceTypeModelArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "rescenceModelArrayList"), typeFluo);
        //rescenceTypeModelArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "rescenceModelArrayList"), typeFluo);
        if(retrievedRescenceTypeModelArrayList!=null && retrievedRescenceTypeModelArrayList.size()>0)
        {
            rescenceTypeModelArrayList = retrievedRescenceTypeModelArrayList;
            fluoRescenceTypeListAdapter = new FluoRescenceTypeListAdapter(rescenceTypeModelArrayList,context,this);
            recycler_fluorescence_view.setAdapter(fluoRescenceTypeListAdapter);
        }

        // Make Type
        Type typeMake = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retrievedMakeTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "makeArrayList"), typeMake);
       // makeTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "makeArrayList"), typeMake);
        if(retrievedMakeTypeArrayList!=null && retrievedMakeTypeArrayList.size()>0)
        {
            makeTypeArrayList = retrievedMakeTypeArrayList;
            makeTypeListAdapter = new MakeTypeListAdapter(makeTypeArrayList,context,this);
            recycler_make_view.setAdapter(makeTypeListAdapter);
        }

        // Cut Type
        Type typeCut = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retrievedCutTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "cutArrayList"), typeCut);
        //Constant.cutTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "cutArrayList"), typeCut);
        if(retrievedCutTypeArrayList!=null && retrievedCutTypeArrayList.size()>0)
        {
            Constant.cutTypeArrayList = retrievedCutTypeArrayList;
        }

        // Polish Type
        Type typePolish = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retrievedPolishTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "polishArrayList"), typePolish);
        //Constant.polishTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "polishArrayList"), typePolish);
        if(retrievedPolishTypeArrayList!=null && retrievedPolishTypeArrayList.size()>0)
        {
            Constant.polishTypeArrayList = retrievedPolishTypeArrayList;
        }

        // Symmerty Type
        Type typeSymmetry = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retrievedSymmertyTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "symmertyArrayList"), typeSymmetry);
       // Constant.symmertyTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "symmertyArrayList"), typeSymmetry);
        if(retrievedSymmertyTypeArrayList!=null && retrievedSymmertyTypeArrayList.size()>0)
        {
            Constant.symmertyTypeArrayList = retrievedSymmertyTypeArrayList;
        }

        // Technology Type
        Type typeTechnology = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retrievedTechnologyTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "technologyArrayList"), typeTechnology);
        //Constant.technologyTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "technologyArrayList"), typeTechnology);
        if(retrievedTechnologyTypeArrayList!=null && retrievedTechnologyTypeArrayList.size()>0)
        {
            Constant.technologyTypeArrayList = retrievedTechnologyTypeArrayList;
        }

        // Fancy Color Intensity Type
        Type typeFancyColorIntensity = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retrievedFancyColorIntensityTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "fancyColorIntensityArrayListData"), typeFancyColorIntensity);
        if(retrievedFancyColorIntensityTypeArrayList!=null && retrievedFancyColorIntensityTypeArrayList.size()>0)
        {
            Constant.fancyColorIntensityArrayList = retrievedFancyColorIntensityTypeArrayList;
        }

        // Fancy Color Overtone Type
        Type typeFancyColorOver = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retrievedFancyColorOverTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "fancyColorOverToneArrayListData"), typeFancyColorOver);
        if(retrievedFancyColorOverTypeArrayList!=null && retrievedFancyColorOverTypeArrayList.size()>0)
        {
            Constant.fancyColorOverToneArrayList = retrievedFancyColorOverTypeArrayList;
        }

        // Table Type
        Type typeTable = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retrievedTableTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "tableDataPercantageArrayListData"), typeTable);
        if(retrievedTableTypeArrayList!=null && retrievedTableTypeArrayList.size()>0)
        {
            Constant.tableDataPercantageArrayList = retrievedTableTypeArrayList;
        }

        // Depth Type
        Type typeDepth = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retrievedDepthTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "depthDataPercantageArrayListData"), typeDepth);
        if(retrievedDepthTypeArrayList!=null && retrievedDepthTypeArrayList.size()>0)
        {
            Constant.depthDataPercantageArrayList = retrievedDepthTypeArrayList;
        }

        // Crown Type
        Type typeCrown = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retrievedCrownTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "crowmArrayListData"), typeCrown);
        if(retrievedCrownTypeArrayList!=null && retrievedCrownTypeArrayList.size()>0)
        {
            Constant.crowmArrayList = retrievedCrownTypeArrayList;
        }

        // Pavillion Type
        Type typePavillion = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retrievedPavillionTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "pavillionArrayListData"), typePavillion);
        if(retrievedPavillionTypeArrayList!=null && retrievedPavillionTypeArrayList.size()>0)
        {
            Constant.pavillionArrayList = retrievedPavillionTypeArrayList;
        }

        // Eye Type
        Type typeEye = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retrievedEyeTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "eyeCleanArrayListData"), typeEye);
        if(retrievedEyeTypeArrayList!=null && retrievedEyeTypeArrayList.size()>0)
        {
            Constant.eyeCleanArrayList = retrievedEyeTypeArrayList;
        }

        // Shade Type
        Type typeShade = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retrievedShadeTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "shadeArrayListData"), typeShade);
        if(retrievedShadeTypeArrayList!=null && retrievedShadeTypeArrayList.size()>0)
        {
            Constant.shadeArrayList = retrievedShadeTypeArrayList;
        }

        // Luster Type
        Type typeLuster = new TypeToken<ArrayList<AttributeDetailsModel>>() {}.getType();
        ArrayList<AttributeDetailsModel> retrievedLusterTypeArrayList = gson.fromJson(CommonUtility.getLocalDataList(context, "lusterArrayListData"), typeLuster);
        if(retrievedLusterTypeArrayList!=null && retrievedLusterTypeArrayList.size()>0)
        {
            Constant.lusterArrayList = retrievedLusterTypeArrayList;
        }

        // Check If Array List Size is 0 and null Then Call API Other wise Manage Locally
        if(retriveColorTypeArrayList!=null && retriveColorTypeArrayList.size()>0 &&
                retriveFancyColorTypeArrayList!=null && retriveFancyColorTypeArrayList.size()>0 &&
                retrievedClarityTypeArrayList!=null && retrievedClarityTypeArrayList.size()>0 &&
                retrievedCertificateTypeArrayList!=null && retrievedCertificateTypeArrayList.size()>0 &&
                retrievedRescenceTypeModelArrayList!=null && retrievedRescenceTypeModelArrayList.size()>0 &&
                retrievedMakeTypeArrayList!=null && retrievedMakeTypeArrayList.size()>0 &&
                retrievedCutTypeArrayList!=null && retrievedCutTypeArrayList.size()>0 &&
                retrievedPolishTypeArrayList!=null && retrievedPolishTypeArrayList.size()>0 &&
                retrievedSymmertyTypeArrayList!=null && retrievedSymmertyTypeArrayList.size()>0 &&
                retrievedTechnologyTypeArrayList!=null && retrievedTechnologyTypeArrayList.size()>0 &&
                retrievedFancyColorIntensityTypeArrayList!=null && retrievedFancyColorIntensityTypeArrayList.size()>0 &&
                retrievedFancyColorOverTypeArrayList!=null && retrievedFancyColorOverTypeArrayList.size()>0 &&
                retrievedTableTypeArrayList!=null && retrievedTableTypeArrayList.size()>0 &&
                retrievedDepthTypeArrayList!=null && retrievedDepthTypeArrayList.size()>0 &&
                retrievedCrownTypeArrayList!=null && retrievedCrownTypeArrayList.size()>0 &&
                retrievedPavillionTypeArrayList!=null && retrievedPavillionTypeArrayList.size()>0 &&
                retrievedEyeTypeArrayList!=null && retrievedEyeTypeArrayList.size()>0 &&
                retrievedShadeTypeArrayList!=null && retrievedShadeTypeArrayList.size()>0 &&
                retrievedLusterTypeArrayList!=null && retrievedLusterTypeArrayList.size()>0)
        {}
        else
        {
            onBindDetails(false);
        }

        // Handle Device Back Button code.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed()
            {
                clearAllFilter();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);


        search_et.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true; // Indicates that you've handled the action
            }
            return false; // Indicates that you haven't handled the action
        });

        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!search_et.getText().toString().equalsIgnoreCase("")){
                    search_diamond_error_tv.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void performSearch() {
        String query = search_et.getText().toString().trim();
        if(query.equalsIgnoreCase(""))
        {
            search_diamond_error_tv.setVisibility(View.VISIBLE);
            //Toast.makeText(activity, "Please enter search diamond", Toast.LENGTH_SHORT).show();
        }
        else{

            Constant.searchKeyword = search_et.getText().toString().trim();
            Constant.priceFrm = from_price_et.getText().toString().trim();
            Constant.priceTo = to_price_et.getText().toString().trim();
            Constant.caratFrm = carat_from_et.getText().toString().trim();
            Constant.caratTo = carat_to_et.getText().toString().trim();

            sendFilterValue();
        }
        // Implement your search logic here
        // For demonstration, we're just showing a Toast message
        //Toast.makeText(this, "Searching for: " + query, Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onResume() {
        super.onResume();
        // Check User Frm Clear Filter Button Click Then If Condition Work
        // This Code Not Use.
        if(Constant.filterClear.equalsIgnoreCase("yes"))
        {
            clearSelectionFilterBGColor();
        }

        // Get Currency Symbol Using Currency Code and Set in Edit Text Price Frm and Price To Field..
        String getCurrencySymbol="", selectedCurrencyCode="";
        selectedCurrencyCode = CommonUtility.getGlobalString(context, "selected_currency_code");
        if(!selectedCurrencyCode.equalsIgnoreCase(""))
        {
            getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
            Constant.getCurrencySymbol = getCurrencySymbol;
        }
        else{
            selectedCurrencyCode = INDIA_CURRENCY_CODE;
            getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
            Constant.getCurrencySymbol = getCurrencySymbol;
        }
        if(Constant.callForBackScreenForUpdateCurrencySymbol.equalsIgnoreCase("yes"))
        {
            from_price_tv.setText(Constant.getCurrencySymbol + " " + getResources().getString(R.string.from));
            to_price_tv.setText(Constant.getCurrencySymbol + " " + getResources().getString(R.string.to));
            Constant.callForBackScreenForUpdateCurrencySymbol = ""; // After use Key Blank
        }
       else{
            from_price_tv.setText(Constant.getCurrencySymbol + " " + getResources().getString(R.string.from));
            to_price_tv.setText(Constant.getCurrencySymbol + " " + getResources().getString(R.string.to));
        }

        showCardCount();
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
    // Clear All Selected Filter Background and EditText Value Blank..
    @RequiresApi(api = Build.VERSION_CODES.P)
    void clearSelectionFilterBGColor()
    {
        clearFilterApplyAllSelections();

        search_et.setText("");
        from_price_et.setText("");
        to_price_et.setText("");
        carat_from_et.setText("");
        carat_to_et.setText("");

        yes_tv.setBackgroundResource(R.drawable.background_white);
        no_tv.setBackgroundResource(R.drawable.background_white);
        no_tv.setElevation(0f);
        yes_tv.setElevation(0f);
        yes_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        no_tv.setTextColor(ContextCompat.getColor(context, R.color.black));

        best_tv.setBackgroundResource(R.drawable.border_without_line);
        medium_tv.setBackgroundResource(R.drawable.border_without_line);
        low_tv.setBackgroundResource(R.drawable.border_without_line);

        best_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
        medium_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
        low_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));

        card_view_best.setCardElevation(0f);
        card_view_best.setCardElevation(0f);
        card_view_medium.setCardElevation(0f);
        card_view_medium.setCardElevation(0f);
        card_view_low.setCardElevation(0f);
        card_view_low.setCardElevation(0f);

        card_view_best.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_best.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_medium.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_medium.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_low.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_low.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.bg_color));

        shapeImageListAdapter.notifyDataSetChanged();
        if(Constant.colorType.equalsIgnoreCase(WHITE))
        {
            colorTypeListAdapter.notifyDataSetChanged();
        }
        else
        {
            fancyColorTypeListAdapter.notifyDataSetChanged();
        }
        clarityTypeListAdapter.notifyDataSetChanged();
        certificateTypeListAdapter.notifyDataSetChanged();
        fluoRescenceTypeListAdapter.notifyDataSetChanged();
        makeTypeListAdapter.notifyDataSetChanged();

        Constant.filterClear = "";
        Constant.makeSelectedValue = "";
        makeSelectedValue = "";
    }

    void setWhiteColorAdapter()
    {
        colorTypeListAdapter = new ColorTypeListAdapter(colorTypeArrayList,context,this);
        recycler_color_view.setAdapter(colorTypeListAdapter);
        colorTypeListAdapter.notifyDataSetChanged();
    }

    void setFancyAdapter()
    {
        fancyColorTypeListAdapter = new FancyColorTypeListAdapter(fancyColorTypeArrayList,context,this);
        recycler_color_view.setAdapter(fancyColorTypeListAdapter);
        fancyColorTypeListAdapter.notifyDataSetChanged();
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
            clearAllFilter();
        }
        else if(id == R.id.natural_tv)
        {
            Constant.searchType = ApiConstants.NATURAL;
            title_tv.setText(getResources().getString(R.string.naturalDiamond));
            naturalCardTabColorSet();
        }
        else if(id == R.id.grown_tv)
        {
            Constant.searchType = ApiConstants.LAB_GROWN;
            title_tv.setText(getResources().getString(R.string.labGrownDiamond));
            labGrownCardTabColorSet();
        }
        /*else if(id == R.id.all_img_rel)
        {
            all_img_rel.setBackgroundResource(R.drawable.background_white_with_border);
        }*/

        else if(id == R.id.clear_all_filter)
        {
            Utils.hideKeyboard(activity);
            clearAllClick = "yes";
            clearAllFilter();
        }
        else if(id == R.id.best_tv)
        {
            handleBestDiamondQualitySelection();
        }
        else if(id == R.id.medium_tv)
        {
            handleMediumDiamondQualitySelection();
        }
        else if(id == R.id.low_tv)
        {
            handleLowDiamondQualitySelection();
        }
        else if(id == R.id.yes_tv)
        {
            isReturnableNo = false;
            if (!isReturnableYes)
            {
                Constant.isReturnable = "1";
                yes_tv.setBackgroundResource(R.drawable.background_gradient);
                no_tv.setBackgroundResource(R.drawable.background_white);

                yes_tv.setElevation(37f);
                no_tv.setElevation(0f);

                yes_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
                no_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
            else{
                Constant.isReturnable = "";
                yes_tv.setBackgroundResource(R.drawable.background_white);
                yes_tv.setElevation(0f);
                yes_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
            isReturnableYes = !isReturnableYes;
        }
        else if(id == R.id.no_tv)
        {
            isReturnableYes = false;
            if (!isReturnableNo)
            {
                Constant.isReturnable = "0";
                yes_tv.setBackgroundResource(R.drawable.background_white);
                no_tv.setBackgroundResource(R.drawable.background_gradient);

                no_tv.setElevation(37f);
                yes_tv.setElevation(0f);

                yes_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                no_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
            }
            else
            {
                Constant.isReturnable = "";
                no_tv.setBackgroundResource(R.drawable.background_white);
                no_tv.setElevation(0f);
                no_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
            isReturnableNo = !isReturnableNo;
        }
        else if(id == R.id.advance_filters_tv)
        {
            Constant.diamondQualitySelected = diamondQualitySelected;
            Constant.makeSelectedValue = makeSelectedValue;
            intent = new Intent(activity, AdvanceFiltersActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else if(id == R.id.search_circle_rel)
        {
            Constant.searchKeyword = search_et.getText().toString().trim();
            Constant.priceFrm = from_price_et.getText().toString().trim();
            Constant.priceTo = to_price_et.getText().toString().trim();
            Constant.caratFrm = carat_from_et.getText().toString().trim();
            Constant.caratTo = carat_to_et.getText().toString().trim();

            sendFilterValue();
        }
        /*if(id == R.id.home_rel)
        {
            Fragment newFragment = new HomeFragment();

            // Previous fragments Remove:
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            // For Open New Fragment:
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_body, newFragment);
            transaction.commit();
        }*/
        else if(id == R.id.home_rel)
        {
            /*intent = new Intent(activity, HomeScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);*/
            finish();
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
        else if(id == R.id.account_rel)
        {
            user_login = CommonUtility.getGlobalString(activity, "user_login");
            if(user_login.equalsIgnoreCase("yes"))
            {
                intent = new Intent(context, AccountSectionActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
            else
            {
                intent = new Intent(context, LoginScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        }
    }

    void sendFilterValue()
    {
        Constant.colorTypeFilterApploedArrayList = new ArrayList<>();

        //Log.e("from_price_tv : ", from_price_tv.getText().toString());

        if(!search_et.getText().toString().equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.searchKeyword);
            model.setSelected(true);
            model.setFilterType("SearchFrm");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }

        if(!from_price_et.getText().toString().equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.priceFrm);
            model.setSelected(true);
            model.setFilterType("PriceFrm");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }
        if(!to_price_et.getText().toString().equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.priceTo);
            model.setSelected(true);
            model.setFilterType("PriceTo");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }

        if(!carat_to_et.getText().toString().equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.caratFrm);
            model.setSelected(true);
            model.setFilterType("CaratFrom");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }
        if(!carat_to_et.getText().toString().equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.caratTo);
            model.setSelected(true);
            model.setFilterType("CaratTo");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }

        if(!Constant.isReturnable.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            if(Constant.isReturnable.equalsIgnoreCase("1"))
            {
                model.setDisplayAttr("Yes");
            }
            else
            {
                model.setDisplayAttr("No");
            }
            model.setSelected(true);
            model.setFilterType("Returnable");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }

        Constant.shapeDiamondValue = "";
        for (int i = 0; i < shapeImageArrayList.size(); i++)
        {
            if(shapeImageArrayList.get(i).isSelected())
            {
                AttributeDetailsModel model = new AttributeDetailsModel();
                model.setDisplayAttr(shapeImageArrayList.get(i).getAttribCode());
                model.setAttribCode(shapeImageArrayList.get(i).getAttribCode());
                model.setFilterType("Shape");
                model.setSelected(true);

                if (Constant.shapeDiamondValue.equalsIgnoreCase("")) {
                    Constant.shapeDiamondValue = shapeImageArrayList.get(i).getAttribCode();
                } else {
                    Constant.shapeDiamondValue = Constant.shapeDiamondValue + "," + shapeImageArrayList.get(i).getAttribCode();
                }
                Constant.colorTypeFilterApploedArrayList.add(model);
            }
            else {}
        }

        if(Constant.colorType.equalsIgnoreCase(WHITE))
        {
            Constant.colorValue = "";

            for (int i = 0; i < colorTypeArrayList.size(); i++)
            {
                if(colorTypeArrayList.get(i).isSelected())
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(colorTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(colorTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Color");
                    model.setSelected(true);

                    if (Constant.colorValue.equalsIgnoreCase("")) {
                        Constant.colorValue = colorTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.colorValue = Constant.colorValue + "," + colorTypeArrayList.get(i).getAttribCode();
                    }
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else {}
            }
            Log.e("colorSelectedValue :" , Constant.colorValue.toString());
        }
        else
        {
            if(Constant.colorType.equalsIgnoreCase(FANCY))
            {
                Constant.fancyColorValue = "";

                for (int i = 0; i < fancyColorTypeArrayList.size(); i++)
                {
                    if(fancyColorTypeArrayList.get(i).isSelected())
                    {
                        AttributeDetailsModel model = new AttributeDetailsModel();
                        model.setDisplayAttr(fancyColorTypeArrayList.get(i).getDisplayAttr());
                        model.setAttribCode(fancyColorTypeArrayList.get(i).getAttribCode());
                        model.setFilterType("FancyColor");
                        model.setSelected(true);

                        if (Constant.fancyColorValue.equalsIgnoreCase("")) {
                            Constant.fancyColorValue = fancyColorTypeArrayList.get(i).getAttribCode();
                        } else {
                            Constant.fancyColorValue = Constant.fancyColorValue + "," + fancyColorTypeArrayList.get(i).getAttribCode();
                        }
                        Constant.colorTypeFilterApploedArrayList.add(model);
                    }
                    else {}
                }
                Log.e("colorSelectedValue :" , Constant.fancyColorValue.toString());
            }
        }

        Constant.clarityValue = "";
        for (int i = 0; i < clarityTypeArrayList.size(); i++)
        {
            if(clarityTypeArrayList.get(i).isSelected())
            {
                AttributeDetailsModel model = new AttributeDetailsModel();
                model.setDisplayAttr(clarityTypeArrayList.get(i).getDisplayAttr());
                model.setAttribCode(clarityTypeArrayList.get(i).getAttribCode());
                model.setFilterType("Clarity");
                model.setSelected(true);

                if (Constant.clarityValue.equalsIgnoreCase("")) {
                    Constant.clarityValue = clarityTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.clarityValue = Constant.clarityValue + "," + clarityTypeArrayList.get(i).getAttribCode();
                }
                Constant.colorTypeFilterApploedArrayList.add(model);
            }
            else {}
        }

        Constant.certificateValue = "";
        for (int i = 0; i < certificateTypeArrayList.size(); i++)
        {
            if(certificateTypeArrayList.get(i).isSelected())
            {
                AttributeDetailsModel model = new AttributeDetailsModel();
                model.setDisplayAttr(certificateTypeArrayList.get(i).getDisplayAttr());
                model.setAttribCode(certificateTypeArrayList.get(i).getAttribCode());
                model.setFilterType("Certificate");
                model.setSelected(true);

                if (Constant.certificateValue.equalsIgnoreCase("")) {
                    Constant.certificateValue = certificateTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.certificateValue = Constant.certificateValue + "," + certificateTypeArrayList.get(i).getAttribCode();
                }

                Constant.colorTypeFilterApploedArrayList.add(model);
            }
            else {}
        }

        Constant.fluorescenceValue = "";
        for (int i = 0; i < rescenceTypeModelArrayList.size(); i++)
        {
            if(rescenceTypeModelArrayList.get(i).isSelected())
            {
                AttributeDetailsModel model = new AttributeDetailsModel();
                model.setDisplayAttr(rescenceTypeModelArrayList.get(i).getDisplayAttr());
                model.setAttribCode(rescenceTypeModelArrayList.get(i).getAttribCode());
                model.setFilterType("FluoRescence");
                model.setSelected(true);

                if (Constant.fluorescenceValue.equalsIgnoreCase("")) {
                    Constant.fluorescenceValue = rescenceTypeModelArrayList.get(i).getAttribCode();
                } else {
                    Constant.fluorescenceValue = Constant.fluorescenceValue + "," + rescenceTypeModelArrayList.get(i).getAttribCode();
                }
                Constant.colorTypeFilterApploedArrayList.add(model);
            }
            else {}
        }

        Constant.makeValue = "";
        for (int i = 0; i <makeTypeArrayList.size() ; i++)
        {
            if(makeTypeArrayList.get(i).isSelected())
            {
                /*AttributeDetailsModel model = new AttributeDetailsModel();
                model.setDisplayAttr(makeTypeArrayList.get(i).getDisplayAttr());
                model.setAttribCode(makeTypeArrayList.get(i).getAttribCode());
                model.setFilterType("Make");
                model.setSelected(true);*/

                Constant.makeValue = makeTypeArrayList.get(i).getAttribCode();

                //Constant.colorTypeFilterApploedArrayList.add(model);

            }else{}
        }

        Log.e("-----makeSelectedValue-------- : ", makeSelectedValue.toString());
        Log.e("-----makeSelectedValue1-------- : ", diamondQualitySelected.toString());

        // If Make is Selected Then Work This Code.

        // diamondQualitySelected Means Best, Low, Medium
        if(makeSelectedValue.equalsIgnoreCase("3EX") && diamondQualitySelected.equalsIgnoreCase(""))
        {
            //Constant.colorTypeFilterApploedArrayList.clear();
            Constant.cutValue = "";
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Ideal") || displayAttr.equalsIgnoreCase("Excellent"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.cutTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.cutTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Cut");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}
                if (displayAttr.equalsIgnoreCase("Ideal") || displayAttr.equalsIgnoreCase("Excellent")) {
                    if (Constant.cutValue.equalsIgnoreCase("")) {
                        Constant.cutValue = Constant.cutTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.cutValue = Constant.cutValue + "," + Constant.cutTypeArrayList.get(i).getAttribCode();
                    }
                } else {
                    //Constant.cutValue = Constant.cutValue + "," + Constant.cutTypeArrayList.get(i).getAttribCode();
                }
                /*if (Constant.cutValue.equalsIgnoreCase("")) {
                    Constant.cutValue = Constant.cutTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.cutValue = Constant.cutValue + "," + Constant.cutTypeArrayList.get(i).getAttribCode();
                }*/
            }

            Constant.polishValue = "";
            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Excellent"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.polishTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.polishTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Polish");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}

                if (displayAttr.equalsIgnoreCase("Excellent")) {
                    if (Constant.polishValue.equalsIgnoreCase("")) {
                        Constant.polishValue = Constant.polishTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.polishValue = Constant.polishValue + "," + Constant.polishTypeArrayList.get(i).getAttribCode();
                    }
                } else {}

                /*if (Constant.polishValue.equalsIgnoreCase("")) {
                    Constant.polishValue = Constant.polishTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.polishValue = Constant.polishValue + "," + Constant.polishTypeArrayList.get(i).getAttribCode();
                }*/
            }

            Constant.symmetryValue = "";
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Excellent"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.symmertyTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.symmertyTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Symmetry");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}

                if (displayAttr.equalsIgnoreCase("Excellent")) {
                    if (Constant.symmetryValue.equalsIgnoreCase("")) {
                        Constant.symmetryValue = Constant.symmertyTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.symmetryValue = Constant.symmetryValue + "," + Constant.symmertyTypeArrayList.get(i).getAttribCode();
                    }
                } else {}

                /*if (Constant.symmetryValue.equalsIgnoreCase("")) {
                    Constant.symmetryValue = Constant.symmertyTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.symmetryValue = Constant.symmetryValue + "," + Constant.symmertyTypeArrayList.get(i).getAttribCode();
                }*/
            }
        }
        else{
            Constant.cutValue = "";
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                if(Constant.cutTypeArrayList.get(i).isSelected())
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.cutTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.cutTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Cut");
                    model.setSelected(true);

                    if (Constant.cutValue.equalsIgnoreCase("")) {
                        Constant.cutValue = Constant.cutTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.cutValue = Constant.cutValue + "," + Constant.cutTypeArrayList.get(i).getAttribCode();
                    }
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else {
                    Log.e("Constant.cutTypeArrayList : ", "Cut : " + Constant.cutTypeArrayList.get(i).isSelected());
                }
            }

            Constant.polishValue = "";
            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                if(Constant.polishTypeArrayList.get(i).isSelected())
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.polishTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.polishTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Polish");
                    model.setSelected(true);

                    if (Constant.polishValue.equalsIgnoreCase("")) {
                        Constant.polishValue = Constant.polishTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.polishValue = Constant.polishValue + "," + Constant.polishTypeArrayList.get(i).getAttribCode();
                    }
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else {
                    Log.e("Constant.cutTypeArrayList1 : ", "Polish : " + Constant.polishTypeArrayList.get(i).isSelected());
                }
            }

            Constant.symmetryValue = "";
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                if(Constant.symmertyTypeArrayList.get(i).isSelected())
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.symmertyTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.symmertyTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Symmetry");
                    model.setSelected(true);

                    if (Constant.symmetryValue.equalsIgnoreCase("")) {
                        Constant.symmetryValue = Constant.symmertyTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.symmetryValue = Constant.symmetryValue + "," + Constant.symmertyTypeArrayList.get(i).getAttribCode();
                    }
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else {
                    Log.e("Constant.cutTypeArrayList2 : ", "Symmetry : " + Constant.symmertyTypeArrayList.get(i).isSelected());
                }
            }
        }

        if(makeSelectedValue.equalsIgnoreCase("EX CUT") && diamondQualitySelected.equalsIgnoreCase(""))
        {
           // Constant.colorTypeFilterApploedArrayList.clear();
            Constant.cutValue = "";
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Excellent"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.cutTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.cutTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Cut");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}

                if (displayAttr.equalsIgnoreCase("Excellent")) {
                    if (Constant.cutValue.equalsIgnoreCase("")) {
                        Constant.cutValue = Constant.cutTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.cutValue = Constant.cutValue + "," + Constant.cutTypeArrayList.get(i).getAttribCode();
                    }
                } else {}

                /*if (Constant.cutValue.equalsIgnoreCase("")) {
                    Constant.cutValue = Constant.cutTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.cutValue = Constant.cutValue + "," + Constant.cutTypeArrayList.get(i).getAttribCode();
                }*/

            }

            Constant.polishValue = "";
            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.polishTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.polishTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Polish");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}

                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good")) {
                    if (Constant.polishValue.equalsIgnoreCase("")) {
                        Constant.polishValue = Constant.polishTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.polishValue = Constant.polishValue + "," + Constant.polishTypeArrayList.get(i).getAttribCode();
                    }
                } else {}

                /*if (Constant.polishValue.equalsIgnoreCase("")) {
                    Constant.polishValue = Constant.polishTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.polishValue = Constant.polishValue + "," + Constant.polishTypeArrayList.get(i).getAttribCode();
                }*/

            }

            Constant.symmetryValue = "";
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.symmertyTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.symmertyTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Symmetry");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}

                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good")) {
                    if (Constant.symmetryValue.equalsIgnoreCase("")) {
                        Constant.symmetryValue = Constant.symmertyTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.symmetryValue = Constant.symmetryValue + "," + Constant.symmertyTypeArrayList.get(i).getAttribCode();
                    }
                } else {}

                /*if (Constant.symmetryValue.equalsIgnoreCase("")) {
                    Constant.symmetryValue = Constant.symmertyTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.symmetryValue = Constant.symmetryValue + "," + Constant.symmertyTypeArrayList.get(i).getAttribCode();
                }*/
            }
        }

        if(makeSelectedValue.equalsIgnoreCase("3 VG+") && diamondQualitySelected.equalsIgnoreCase(""))
        {
           // Constant.colorTypeFilterApploedArrayList.clear();
            Constant.cutValue = "";
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.cutTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.cutTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Cut");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}

                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good")) {
                    if (Constant.cutValue.equalsIgnoreCase("")) {
                        Constant.cutValue = Constant.cutTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.cutValue = Constant.cutValue + "," + Constant.cutTypeArrayList.get(i).getAttribCode();
                    }
                } else {}

                /*if (Constant.cutValue.equalsIgnoreCase("")) {
                    Constant.cutValue = Constant.cutTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.cutValue = Constant.cutValue + "," + Constant.cutTypeArrayList.get(i).getAttribCode();
                }*/

            }

            Constant.polishValue = "";
            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.polishTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.polishTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Polish");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}

                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good")) {
                    if (Constant.polishValue.equalsIgnoreCase("")) {
                        Constant.polishValue = Constant.polishTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.polishValue = Constant.polishValue + "," + Constant.polishTypeArrayList.get(i).getAttribCode();
                    }
                } else {}


                /*if (Constant.polishValue.equalsIgnoreCase("")) {
                    Constant.polishValue = Constant.polishTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.polishValue = Constant.polishValue + "," + Constant.polishTypeArrayList.get(i).getAttribCode();
                }*/
            }

            Constant.symmetryValue = "";
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.symmertyTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.symmertyTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Symmetry");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}

                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good")) {
                    if (Constant.symmetryValue.equalsIgnoreCase("")) {
                        Constant.symmetryValue = Constant.symmertyTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.symmetryValue = Constant.symmetryValue + "," + Constant.symmertyTypeArrayList.get(i).getAttribCode();
                    }
                } else {}

                /*if (Constant.symmetryValue.equalsIgnoreCase("")) {
                    Constant.symmetryValue = Constant.symmertyTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.symmetryValue = Constant.symmetryValue + "," + Constant.symmertyTypeArrayList.get(i).getAttribCode();
                }*/
            }
        }

        //-----------------------------------------------Combination of Diamond Type and make Type When User Manual Select

        //----------------------------Check diamondQualitySelected Means best. low. medium not blank and makeSelectedValue is selected.
        if(makeSelectedValue.equalsIgnoreCase("3EX") && !diamondQualitySelected.equalsIgnoreCase(""))
        {
            //Constant.colorTypeFilterApploedArrayList.clear();
            Constant.cutValue = "";
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Ideal") || displayAttr.equalsIgnoreCase("Excellent"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.cutTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.cutTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Cut");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}
                if (displayAttr.equalsIgnoreCase("Ideal") || displayAttr.equalsIgnoreCase("Excellent")) {
                    if (Constant.cutValue.equalsIgnoreCase("")) {
                        Constant.cutValue = Constant.cutTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.cutValue = Constant.cutValue + "," + Constant.cutTypeArrayList.get(i).getAttribCode();
                    }
                } else {
                    //Constant.cutValue = Constant.cutValue + "," + Constant.cutTypeArrayList.get(i).getAttribCode();
                }
                /*if (Constant.cutValue.equalsIgnoreCase("")) {
                    Constant.cutValue = Constant.cutTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.cutValue = Constant.cutValue + "," + Constant.cutTypeArrayList.get(i).getAttribCode();
                }*/
            }

            Constant.polishValue = "";
            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Excellent"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.polishTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.polishTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Polish");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}

                if (displayAttr.equalsIgnoreCase("Excellent")) {
                    if (Constant.polishValue.equalsIgnoreCase("")) {
                        Constant.polishValue = Constant.polishTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.polishValue = Constant.polishValue + "," + Constant.polishTypeArrayList.get(i).getAttribCode();
                    }
                } else {}

                /*if (Constant.polishValue.equalsIgnoreCase("")) {
                    Constant.polishValue = Constant.polishTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.polishValue = Constant.polishValue + "," + Constant.polishTypeArrayList.get(i).getAttribCode();
                }*/
            }

            Constant.symmetryValue = "";
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Excellent"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.symmertyTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.symmertyTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Symmetry");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}

                if (displayAttr.equalsIgnoreCase("Excellent")) {
                    if (Constant.symmetryValue.equalsIgnoreCase("")) {
                        Constant.symmetryValue = Constant.symmertyTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.symmetryValue = Constant.symmetryValue + "," + Constant.symmertyTypeArrayList.get(i).getAttribCode();
                    }
                } else {}

                /*if (Constant.symmetryValue.equalsIgnoreCase("")) {
                    Constant.symmetryValue = Constant.symmertyTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.symmetryValue = Constant.symmetryValue + "," + Constant.symmertyTypeArrayList.get(i).getAttribCode();
                }*/
            }
        }

        if(makeSelectedValue.equalsIgnoreCase("EX CUT") && !diamondQualitySelected.equalsIgnoreCase(""))
        {
            // Constant.colorTypeFilterApploedArrayList.clear();
            Constant.cutValue = "";
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Excellent"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.cutTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.cutTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Cut");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}

                if (displayAttr.equalsIgnoreCase("Excellent")) {
                    if (Constant.cutValue.equalsIgnoreCase("")) {
                        Constant.cutValue = Constant.cutTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.cutValue = Constant.cutValue + "," + Constant.cutTypeArrayList.get(i).getAttribCode();
                    }
                } else {}

                /*if (Constant.cutValue.equalsIgnoreCase("")) {
                    Constant.cutValue = Constant.cutTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.cutValue = Constant.cutValue + "," + Constant.cutTypeArrayList.get(i).getAttribCode();
                }*/

            }

            Constant.polishValue = "";
            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.polishTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.polishTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Polish");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}

                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good")) {
                    if (Constant.polishValue.equalsIgnoreCase("")) {
                        Constant.polishValue = Constant.polishTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.polishValue = Constant.polishValue + "," + Constant.polishTypeArrayList.get(i).getAttribCode();
                    }
                } else {}

                /*if (Constant.polishValue.equalsIgnoreCase("")) {
                    Constant.polishValue = Constant.polishTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.polishValue = Constant.polishValue + "," + Constant.polishTypeArrayList.get(i).getAttribCode();
                }*/

            }

            Constant.symmetryValue = "";
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.symmertyTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.symmertyTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Symmetry");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}

                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good")) {
                    if (Constant.symmetryValue.equalsIgnoreCase("")) {
                        Constant.symmetryValue = Constant.symmertyTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.symmetryValue = Constant.symmetryValue + "," + Constant.symmertyTypeArrayList.get(i).getAttribCode();
                    }
                } else {}

                /*if (Constant.symmetryValue.equalsIgnoreCase("")) {
                    Constant.symmetryValue = Constant.symmertyTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.symmetryValue = Constant.symmetryValue + "," + Constant.symmertyTypeArrayList.get(i).getAttribCode();
                }*/
            }
        }

        if(makeSelectedValue.equalsIgnoreCase("3 VG+") && !diamondQualitySelected.equalsIgnoreCase(""))
        {
            // Constant.colorTypeFilterApploedArrayList.clear();
            Constant.cutValue = "";
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.cutTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.cutTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Cut");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}

                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good")) {
                    if (Constant.cutValue.equalsIgnoreCase("")) {
                        Constant.cutValue = Constant.cutTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.cutValue = Constant.cutValue + "," + Constant.cutTypeArrayList.get(i).getAttribCode();
                    }
                } else {}

                /*if (Constant.cutValue.equalsIgnoreCase("")) {
                    Constant.cutValue = Constant.cutTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.cutValue = Constant.cutValue + "," + Constant.cutTypeArrayList.get(i).getAttribCode();
                }*/

            }

            Constant.polishValue = "";
            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.polishTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.polishTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Polish");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}

                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good")) {
                    if (Constant.polishValue.equalsIgnoreCase("")) {
                        Constant.polishValue = Constant.polishTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.polishValue = Constant.polishValue + "," + Constant.polishTypeArrayList.get(i).getAttribCode();
                    }
                } else {}


                /*if (Constant.polishValue.equalsIgnoreCase("")) {
                    Constant.polishValue = Constant.polishTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.polishValue = Constant.polishValue + "," + Constant.polishTypeArrayList.get(i).getAttribCode();
                }*/
            }

            Constant.symmetryValue = "";
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();

                if(displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    AttributeDetailsModel model = new AttributeDetailsModel();
                    model.setDisplayAttr(Constant.symmertyTypeArrayList.get(i).getDisplayAttr());
                    model.setAttribCode(Constant.symmertyTypeArrayList.get(i).getAttribCode());
                    model.setFilterType("Symmetry");
                    model.setSelected(true);
                    Constant.colorTypeFilterApploedArrayList.add(model);
                }
                else{}

                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good")) {
                    if (Constant.symmetryValue.equalsIgnoreCase("")) {
                        Constant.symmetryValue = Constant.symmertyTypeArrayList.get(i).getAttribCode();
                    } else {
                        Constant.symmetryValue = Constant.symmetryValue + "," + Constant.symmertyTypeArrayList.get(i).getAttribCode();
                    }
                } else {}

                /*if (Constant.symmetryValue.equalsIgnoreCase("")) {
                    Constant.symmetryValue = Constant.symmertyTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.symmetryValue = Constant.symmetryValue + "," + Constant.symmertyTypeArrayList.get(i).getAttribCode();
                }*/
            }
        }

        //----------------------------Check diamondQualitySelected Means best. low. medium not blank and makeSelectedValue is selected.


        Constant.technologyValue = "";
        for (int i = 0; i < Constant.technologyTypeArrayList.size(); i++)
        {
            if(Constant.technologyTypeArrayList.get(i).isSelected())
            {
                AttributeDetailsModel model = new AttributeDetailsModel();
                model.setDisplayAttr(Constant.technologyTypeArrayList.get(i).getDisplayAttr());
                model.setAttribCode(Constant.technologyTypeArrayList.get(i).getAttribCode());
                model.setFilterType("Technology");
                model.setSelected(true);

                if (Constant.technologyValue.equalsIgnoreCase("")) {
                    Constant.technologyValue = Constant.technologyTypeArrayList.get(i).getAttribCode();
                } else {
                    Constant.technologyValue = Constant.technologyValue + "," + Constant.technologyTypeArrayList.get(i).getAttribCode();
                }
                Constant.colorTypeFilterApploedArrayList.add(model);
            }
            else {}
        }

        Constant.eyeCleanValue = "";
        for (int i = 0; i < Constant.eyeCleanArrayList.size(); i++)
        {
            if(Constant.eyeCleanArrayList.get(i).isSelected())
            {
                AttributeDetailsModel model = new AttributeDetailsModel();
                model.setDisplayAttr(Constant.eyeCleanArrayList.get(i).getDisplayAttr());
                model.setAttribCode(Constant.eyeCleanArrayList.get(i).getAttribCode());
                model.setFilterType("Eye");
                model.setSelected(true);

                if (Constant.eyeCleanValue.equalsIgnoreCase("")) {
                    Constant.eyeCleanValue = Constant.eyeCleanArrayList.get(i).getAttribCode();
                } else {
                    Constant.eyeCleanValue = Constant.eyeCleanValue + "," + Constant.eyeCleanArrayList.get(i).getAttribCode();
                }
                Constant.colorTypeFilterApploedArrayList.add(model);
            }
            else {}
        }

        Constant.shadeValue = "";
        for (int i = 0; i < Constant.shadeArrayList.size(); i++)
        {
            if(Constant.shadeArrayList.get(i).isSelected())
            {
                AttributeDetailsModel model = new AttributeDetailsModel();
                model.setDisplayAttr(Constant.shadeArrayList.get(i).getDisplayAttr());
                model.setAttribCode(Constant.shadeArrayList.get(i).getAttribCode());
                model.setFilterType("Shade");
                model.setSelected(true);

                if (Constant.shadeValue.equalsIgnoreCase("")) {
                    Constant.shadeValue = Constant.shadeArrayList.get(i).getAttribCode();
                } else {
                    Constant.shadeValue = Constant.shadeValue + "," + Constant.shadeArrayList.get(i).getAttribCode();
                }
                Constant.colorTypeFilterApploedArrayList.add(model);
            }
            else {}
        }

        Constant.lusterValue = "";
        for (int i = 0; i < Constant.lusterArrayList.size(); i++)
        {
            if(Constant.lusterArrayList.get(i).isSelected())
            {
                AttributeDetailsModel model = new AttributeDetailsModel();
                model.setDisplayAttr(Constant.lusterArrayList.get(i).getDisplayAttr());
                model.setAttribCode(Constant.lusterArrayList.get(i).getAttribCode());
                model.setFilterType("Luster");
                model.setSelected(true);

                if (Constant.lusterValue.equalsIgnoreCase("")) {
                    Constant.lusterValue = Constant.lusterArrayList.get(i).getAttribCode();
                } else {
                    Constant.lusterValue = Constant.lusterValue + "," + Constant.lusterArrayList.get(i).getAttribCode();
                }
                Constant.colorTypeFilterApploedArrayList.add(model);
            }
            else {}
        }

        if(!Constant.fancyColorIntensity.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.fancyColorIntensity);
            model.setSelected(true);
            model.setFilterType("FancyColorIntensity");
            Constant.colorTypeFilterApploedArrayList.add(model);
            Log.e("DEBUG", "Added FancyColorIntensity");
        }
        if(!Constant.fancyColorOvertone.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.fancyColorOvertone);
            model.setSelected(true);
            model.setFilterType("FancyColorOvertone");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }
        if(!Constant.tableFrm.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.tableFrm);
            model.setSelected(true);
            model.setFilterType("TableFrom");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }
        if(!Constant.tableTo.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.tableTo);
            model.setSelected(true);
            model.setFilterType("TableTo");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }
        if(!Constant.depthFrmSpinner.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.depthFrmSpinner);
            model.setSelected(true);
            model.setFilterType("DepthFromSpinner");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }
        if(!Constant.depthToSpinner.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.depthToSpinner);
            model.setSelected(true);
            model.setFilterType("DepthToSpinner");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }

        if(!Constant.lengthFrm.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.lengthFrm);
            model.setSelected(true);
            model.setFilterType("LengthFrom");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }
        if(!Constant.lengthTo.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.lengthTo);
            model.setSelected(true);
            model.setFilterType("LengthTo");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }

        if(!Constant.widthFrm.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.widthFrm);
            model.setSelected(true);
            model.setFilterType("WidthFrom");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }
        if(!Constant.widthTo.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.widthTo);
            model.setSelected(true);
            model.setFilterType("WidthTo");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }

        if(!Constant.depthFrmInput.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.depthFrmInput);
            model.setSelected(true);
            model.setFilterType("DepthFrmInput");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }
        if(!Constant.depthToInput.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.depthToInput);
            model.setSelected(true);
            model.setFilterType("DepthToInput");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }

        if(!Constant.crownFrm.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.crownFrm);
            model.setSelected(true);
            model.setFilterType("CrownFrom");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }
        if(!Constant.crownTo.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.crownTo);
            model.setSelected(true);
            model.setFilterType("CrownTo");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }

        if(!Constant.pavillionFrm.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.pavillionFrm);
            model.setSelected(true);
            model.setFilterType("PavillionFrom");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }
        if(!Constant.pavillionTo.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.pavillionTo);
            model.setSelected(true);
            model.setFilterType("PavillionTo");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }
        if(!Constant.lotID.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.lotID);
            model.setSelected(true);
            model.setFilterType("LotID");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }
        if(!Constant.location.equalsIgnoreCase(""))
        {
            AttributeDetailsModel model = new AttributeDetailsModel();
            model.setDisplayAttr(Constant.location);
            model.setSelected(true);
            model.setFilterType("Location");
            Constant.colorTypeFilterApploedArrayList.add(model);
        }

        Log.e("------Diamond------cutValue-------- : ", Constant.cutValue);
        Log.e("------Diamond------polishValue-------- : ", Constant.polishValue);
        Log.e("------Diamond------symmetryValue-------- : ", Constant.symmetryValue);
        Log.e("------Diamond------technologyValue-------- : ", Constant.technologyValue);
        Log.e("------Diamond------eyeCleanValue-------- : ", Constant.eyeCleanValue);
        Log.e("------Diamond------shadeValue-------- : ", Constant.shadeValue);
        Log.e("------Diamond------lusterValue-------- : ", Constant.lusterValue);
        Log.e("------Diamond------fancyColorIntensity-------- : ", Constant.fancyColorIntensity);
        Log.e("------Diamond------fancyColorOvertone-------- : ", Constant.fancyColorOvertone);
        Log.e("------Diamond------tableFrm-------- : ", Constant.tableFrm);
        Log.e("------Diamond------tableTo-------- : ", Constant.tableTo);
        Log.e("------Diamond------depthFrmSpinner-------- : ", Constant.depthFrmSpinner);
        Log.e("------Diamond------depthToSpinner-------- : ", Constant.depthToSpinner);
        Log.e("------Diamond------crownFrm-------- : ", Constant.crownFrm);
        Log.e("------Diamond------crownTo-------- : ", Constant.crownTo);
        Log.e("------Diamond------pavillionFrm-------- : ", Constant.pavillionFrm);
        Log.e("------Diamond------pavillionTo-------- : ", Constant.pavillionTo);

        Constant.searchKeyword = search_et.getText().toString().trim();
        Constant.sortingBy = "PriceLow";

        // If Diamond Type is Blank Or Diamond Type is Natural is Selected Then Goto If Condition Other Wise Else Condition.
        if(Constant.searchType.equalsIgnoreCase("") || Constant.searchType.equalsIgnoreCase(ApiConstants.NATURAL))
        {
            Constant.searchType = ApiConstants.NATURAL;
        }
        else
        {
            Constant.searchType = ApiConstants.LAB_GROWN;
        }
        Log.e("----------shapeDiamondValue------ ; ", Constant.shapeDiamondValue);
        Intent intent = new Intent(activity, SearchResultActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
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
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void handleBestDiamondQualitySelection()
    {
        isMediumSelected = false;
        isLowSelected = false;

        if (!isBestSelected) {
            diamondQualitySelected = BEST;

            bestDiamondCardAndTextColorSet();

            // Set all items to not selected
            resetAllSelections();
            resetAdvacnceFilterAllSelections();

            // Set selected items For Color Array
            for (int i = 0; i < colorTypeArrayList.size(); i++)
            {
                String displayAttr = colorTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("D") || displayAttr.equalsIgnoreCase("E") ||
                        displayAttr.equalsIgnoreCase("F"))
                {
                    colorTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("F"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Fancy Color Array
            for (int i = 0; i < fancyColorTypeArrayList.size(); i++)
            {
                String displayAttr = fancyColorTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Yellow-Bn") || displayAttr.equalsIgnoreCase("Green-Blue") ||
                        displayAttr.equalsIgnoreCase("Brown-Pink"))
                {
                    fancyColorTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Brown-Pink"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Clarity Array
            for (int i = 0; i < clarityTypeArrayList.size(); i++)
            {
                String displayAttr = clarityTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("FL") || displayAttr.equalsIgnoreCase("IF") ||
                        displayAttr.equalsIgnoreCase("VVS1") || displayAttr.equalsIgnoreCase("VVS2"))
                {
                    clarityTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("VVS2"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Certificate Array
            for (int i = 0; i < certificateTypeArrayList.size(); i++)
            {
                String displayAttr = certificateTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("GIA") || displayAttr.equalsIgnoreCase("IGI"))
                {
                    certificateTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("IGI"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Fluorescence Array
            for (int i = 0; i < rescenceTypeModelArrayList.size(); i++)
            {
                String displayAttr = rescenceTypeModelArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("None") )
                {
                    rescenceTypeModelArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("None"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Make Array
            for (int i = 0; i < makeTypeArrayList.size(); i++)
            {
                String displayAttr = makeTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("3EX") )
                {
                    makeTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("3EX"))
                    {
                        makeSelectedValue = displayAttr;
                        break;
                    }
                }
            }

            if(diamondQualitySelected.equalsIgnoreCase(BEST) && makeSelectedValue.equalsIgnoreCase("3EX"))
            {
                for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Ideal") || displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        Constant.cutTypeArrayList.get(i).setSelected(true);
                    /*if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        break;
                    }*/
                    }
                }

                for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        Constant.polishTypeArrayList.get(i).setSelected(true);
                    }
                }

                // Set selected items For Symmetry Array
                for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        Constant.symmertyTypeArrayList.get(i).setSelected(true);
                    }
                }

            }
            else if(diamondQualitySelected.equalsIgnoreCase(BEST) && makeSelectedValue.equalsIgnoreCase("EX CUT"))
            {
                for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        Constant.cutTypeArrayList.get(i).setSelected(true);
                    }
                }
                for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        Constant.polishTypeArrayList.get(i).setSelected(true);
                    }
                }
                for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        Constant.symmertyTypeArrayList.get(i).setSelected(true);
                    }
                }
            }
            else if(diamondQualitySelected.equalsIgnoreCase(BEST) && makeSelectedValue.equalsIgnoreCase("3 VG+"))
            {
                for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        Constant.cutTypeArrayList.get(i).setSelected(true);
                    }
                }

                for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        Constant.polishTypeArrayList.get(i).setSelected(true);
                    }
                }

                for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        Constant.symmertyTypeArrayList.get(i).setSelected(true);
                    }
                }
            }

            adapterNotifyData();
        }
        else
        {
            diamondQualitySelected = "";
            makeSelectedValue = "";
            best_tv.setBackgroundResource(R.drawable.border_without_line);
            best_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
            card_view_best.setCardElevation(0f);
            card_view_best.setCardElevation(0f);

            // Set all items to not selected
            resetAllSelections();
            resetAdvacnceFilterAllSelections();
            adapterNotifyData();
        }
        isBestSelected = !isBestSelected; // Toggle the state
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void handleMediumDiamondQualitySelection()
    {
        isBestSelected = false;
        isLowSelected = false;

        if (!isMediumSelected)
        {
            diamondQualitySelected = MEDIUM;

            mediumDiamondCardAndTextColorSet();

            // Set all items to not selected
            resetAllSelections();
            resetAdvacnceFilterAllSelections();

            // Set selected items Color List.
            for (int i = 0; i < colorTypeArrayList.size(); i++)
            {
                String displayAttr = colorTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("G") || displayAttr.equalsIgnoreCase("H") ||
                        displayAttr.equalsIgnoreCase("I"))
                {
                    colorTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("I"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Fancy Color Array
            for (int i = 0; i < fancyColorTypeArrayList.size(); i++)
            {
                String displayAttr = fancyColorTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Black") || displayAttr.equalsIgnoreCase("Blue") ||
                        displayAttr.equalsIgnoreCase("Brown"))
                {
                    fancyColorTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Brown"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Clarity Array
            for (int i = 0; i < clarityTypeArrayList.size(); i++)
            {
                String displayAttr = clarityTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("VS1") || displayAttr.equalsIgnoreCase("VS2"))
                {
                    clarityTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("VS2"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Certificate Array
            for (int i = 0; i < certificateTypeArrayList.size(); i++)
            {
                String displayAttr = certificateTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("GIA") || displayAttr.equalsIgnoreCase("IGI"))
                {
                    certificateTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("IGI"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Fluorescence Array
            for (int i = 0; i < rescenceTypeModelArrayList.size(); i++)
            {
                String displayAttr = rescenceTypeModelArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("None") || displayAttr.equalsIgnoreCase("Faint") ||
                        displayAttr.equalsIgnoreCase("Medium"))
                {
                    rescenceTypeModelArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Medium"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Make Array
            for (int i = 0; i < makeTypeArrayList.size(); i++)
            {
                String displayAttr = makeTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("3 VG+") )
                {
                    makeTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("3 VG+"))
                    {
                        makeSelectedValue = displayAttr;
                        break;
                    }
                }
            }

            if(diamondQualitySelected.equalsIgnoreCase(MEDIUM) && makeSelectedValue.equalsIgnoreCase("3EX"))
            {
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Ideal") || displayAttr.equalsIgnoreCase("Excellent"))
                {
                    Constant.cutTypeArrayList.get(i).setSelected(true);
                }
            }

            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent"))
                {
                    Constant.polishTypeArrayList.get(i).setSelected(true);
                }
            }

            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent"))
                {
                    Constant.symmertyTypeArrayList.get(i).setSelected(true);
                }
            }
        }
            else if(diamondQualitySelected.equalsIgnoreCase(MEDIUM) && makeSelectedValue.equalsIgnoreCase("EX CUT"))
            {
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent"))
                {
                    Constant.cutTypeArrayList.get(i).setSelected(true);
                }
            }
            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.polishTypeArrayList.get(i).setSelected(true);
                }
            }
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.symmertyTypeArrayList.get(i).setSelected(true);
                }
            }
        }
            else if(diamondQualitySelected.equalsIgnoreCase(MEDIUM) && makeSelectedValue.equalsIgnoreCase("3 VG+"))
            {
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.cutTypeArrayList.get(i).setSelected(true);
                }
            }

            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.polishTypeArrayList.get(i).setSelected(true);
                }
            }
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.symmertyTypeArrayList.get(i).setSelected(true);
                }
            }
        }

            adapterNotifyData();
        }
        else
        {
            diamondQualitySelected = "";
            makeSelectedValue = "";
            medium_tv.setBackgroundResource(R.drawable.border_dark_purple);
            medium_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));

            card_view_medium.setCardElevation(0f);
            card_view_medium.setCardElevation(0f);

            // Set all items to not selected

            resetAllSelections();
            resetAdvacnceFilterAllSelections();
            adapterNotifyData();
        }
        isMediumSelected = !isMediumSelected; // Toggle the state
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void handleLowDiamondQualitySelection()
    {
        isBestSelected = false;
        isMediumSelected = false;

        if (!isLowSelected)
        {
            diamondQualitySelected = LOW;

            lowDiamondCardAndTextColorSet();

            // Set all items to not selected
            resetAllSelections();
            resetAdvacnceFilterAllSelections();

            // Set selected items Color List.
            for (int i = 0; i < colorTypeArrayList.size(); i++)
            {
                String displayAttr = colorTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("J") || displayAttr.equalsIgnoreCase("K") ||
                        displayAttr.equalsIgnoreCase("L"))
                {
                    colorTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("L"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Fancy Color Array
            for (int i = 0; i < fancyColorTypeArrayList.size(); i++)
            {
                String displayAttr = fancyColorTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Gray") || displayAttr.equalsIgnoreCase("Gray-Blue") ||
                        displayAttr.equalsIgnoreCase("Green"))
                {
                    fancyColorTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Green"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Clarity Array
            for (int i = 0; i < clarityTypeArrayList.size(); i++)
            {
                String displayAttr = clarityTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("SI1") || displayAttr.equalsIgnoreCase("SI2")
                        || displayAttr.equalsIgnoreCase("SI3") || displayAttr.equalsIgnoreCase("I1"))
                {
                    clarityTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("I1"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Certificate Array
            for (int i = 0; i < certificateTypeArrayList.size(); i++)
            {
                String displayAttr = certificateTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("GIA") || displayAttr.equalsIgnoreCase("IGI"))
                {
                    certificateTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("IGI"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Fluorescence Array
            for (int i = 0; i < rescenceTypeModelArrayList.size(); i++)
            {
                String displayAttr = rescenceTypeModelArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("None") || displayAttr.equalsIgnoreCase("Faint") ||
                        displayAttr.equalsIgnoreCase("Medium") ||
                        displayAttr.equalsIgnoreCase("Strong")
                        || displayAttr.equalsIgnoreCase("V-Strong"))
                {
                    rescenceTypeModelArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("V-Strong"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Make Array
            for (int i = 0; i < makeTypeArrayList.size(); i++)
            {
                String displayAttr = makeTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("3 VG+") )
                {
                    makeTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("3 VG+"))
                    {
                        makeSelectedValue = displayAttr;
                        break;
                    }
                }
            }

            if(diamondQualitySelected.equalsIgnoreCase(LOW) && makeSelectedValue.equalsIgnoreCase("3EX"))
            {
                for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Ideal") || displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        Constant.cutTypeArrayList.get(i).setSelected(true);
                    }
                }
                for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        Constant.polishTypeArrayList.get(i).setSelected(true);
                    }
                }
                for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        Constant.symmertyTypeArrayList.get(i).setSelected(true);
                    }
                }
            }
            else if(diamondQualitySelected.equalsIgnoreCase(LOW) && makeSelectedValue.equalsIgnoreCase("EX CUT"))
            {
                for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        Constant.cutTypeArrayList.get(i).setSelected(true);
                    }
                }
                for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        Constant.polishTypeArrayList.get(i).setSelected(true);
                    }
                }
                for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        Constant.symmertyTypeArrayList.get(i).setSelected(true);
                    }
                }
            }
            else if(diamondQualitySelected.equalsIgnoreCase(LOW) && makeSelectedValue.equalsIgnoreCase("3 VG+"))
            {
                for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        Constant.cutTypeArrayList.get(i).setSelected(true);
                    }
                }

                for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        Constant.polishTypeArrayList.get(i).setSelected(true);
                    }
                }
                for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
                {
                    String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                    if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        Constant.symmertyTypeArrayList.get(i).setSelected(true);
                    }
                }
            }

            adapterNotifyData();
        }
        else
        {
            diamondQualitySelected = "";
            makeSelectedValue = "";
            low_tv.setBackgroundResource(R.drawable.border_dark_purple);
            low_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
            card_view_low.setCardElevation(0f);
            card_view_low.setCardElevation(0f);

            // Set all items to not selected

            resetAllSelections();
            resetAdvacnceFilterAllSelections();

            adapterNotifyData();

        }
        isLowSelected = !isLowSelected; // Toggle the state
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void bestDiamondCardAndTextColorSet()
    {
        best_tv.setBackgroundResource(R.drawable.background_gradient);
        medium_tv.setBackgroundResource(R.drawable.border_without_line);
        low_tv.setBackgroundResource(R.drawable.border_without_line);

        best_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        medium_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
        low_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));

        card_view_best.setCardElevation(37f);
        card_view_best.setCardElevation(18f);
        card_view_medium.setCardElevation(0f);
        card_view_medium.setCardElevation(0f);
        card_view_low.setCardElevation(0f);
        card_view_low.setCardElevation(0f);

        card_view_best.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
        card_view_best.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
        card_view_medium.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_medium.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_low.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_low.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.bg_color));

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void mediumDiamondCardAndTextColorSet()
    {
        best_tv.setBackgroundResource(R.drawable.border_without_line);
        medium_tv.setBackgroundResource(R.drawable.background_gradient);
        low_tv.setBackgroundResource(R.drawable.border_without_line);

        best_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
        medium_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        low_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));

        card_view_best.setCardElevation(0f);
        card_view_best.setCardElevation(0f);
        card_view_medium.setCardElevation(37f);
        card_view_medium.setCardElevation(18f);
        card_view_low.setCardElevation(0f);
        card_view_low.setCardElevation(0f);

        card_view_best.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_best.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_medium.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
        card_view_medium.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
        card_view_low.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_low.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color));
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void lowDiamondCardAndTextColorSet()
    {
        best_tv.setBackgroundResource(R.drawable.border_without_line);
        medium_tv.setBackgroundResource(R.drawable.border_without_line);
        low_tv.setBackgroundResource(R.drawable.background_gradient);

        best_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
        medium_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
        low_tv.setTextColor(ContextCompat.getColor(context, R.color.white));

        card_view_best.setCardElevation(0f);
        card_view_best.setCardElevation(0f);
        card_view_medium.setCardElevation(0f);
        card_view_medium.setCardElevation(0f);
        card_view_low.setCardElevation(37f);
        card_view_low.setCardElevation(18f);

        card_view_best.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_best.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_medium.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_medium.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_low.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
        card_view_low.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));

    }

    private void resetAdvacnceFilterAllSelections() {
        setAllItemsToNotSelected(Constant.cutTypeArrayList);
        setAllItemsToNotSelected(Constant.polishTypeArrayList);
        setAllItemsToNotSelected(Constant.symmertyTypeArrayList);
    }

    private void resetAllSelections() {
        setAllItemsToNotSelected(colorTypeArrayList);
        setAllItemsToNotSelected(fancyColorTypeArrayList);
        setAllItemsToNotSelected(clarityTypeArrayList);
        setAllItemsToNotSelected(certificateTypeArrayList);
        setAllItemsToNotSelected(rescenceTypeModelArrayList);
        setAllItemsToNotSelected(makeTypeArrayList);
    }

    private void clearFilterApplyAllSelections() {
        setShapeImageAllItemsToNotSelected(shapeImageArrayList);
        setAllItemsToNotSelected(colorTypeArrayList);
        setAllItemsToNotSelected(fancyColorTypeArrayList);
        setAllItemsToNotSelected(clarityTypeArrayList);
        setAllItemsToNotSelected(certificateTypeArrayList);
        setAllItemsToNotSelected(rescenceTypeModelArrayList);
        setAllItemsToNotSelected(makeTypeArrayList);

        setAllItemsToNotSelected(Constant.cutTypeArrayList);
        setAllItemsToNotSelected(Constant.polishTypeArrayList);
        setAllItemsToNotSelected(Constant.symmertyTypeArrayList);
        setAllItemsToNotSelected(Constant.technologyTypeArrayList);
        setAllItemsToNotSelected(Constant.eyeCleanArrayList);
        setAllItemsToNotSelected(Constant.shadeArrayList);
        setAllItemsToNotSelected(Constant.lusterArrayList);
    }

    private void setAllItemsToNotSelected(ArrayList<AttributeDetailsModel> list) {
        for (AttributeDetailsModel item : list) {
            item.setSelected(false);
        }
    }

    private void setShapeImageAllItemsToNotSelected(ArrayList<ShapeImageModel> list) {
        for (ShapeImageModel item : list) {
            item.setSelected(false);
        }
    }

    // This Call When Adapter Refresh
    void adapterNotifyData()
    {
        if(Constant.colorType.equalsIgnoreCase(WHITE))
        {
            colorTypeListAdapter.notifyDataSetChanged();
        }
        else
        {
            fancyColorTypeListAdapter.notifyDataSetChanged();
        }
        clarityTypeListAdapter.notifyDataSetChanged();
        certificateTypeListAdapter.notifyDataSetChanged();
        fluoRescenceTypeListAdapter.notifyDataSetChanged();
        makeTypeListAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void unSelectBestDiamond()
    {
        best_tv.setBackgroundResource(R.drawable.border_dark_purple);
        best_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
        card_view_best.setCardElevation(0f);
        card_view_best.setCardElevation(0f);
        card_view_best.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.bg_color));
        card_view_best.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.bg_color));
        diamondQualitySelected = "";
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void selectBestDiamond()
    {
        best_tv.setBackgroundResource(R.drawable.background_gradient);
        best_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        card_view_best.setCardElevation(37f);
        card_view_best.setCardElevation(18f);
        card_view_best.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
        card_view_best.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
        diamondQualitySelected = BEST;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void itemClick(int position, String action) {

        if (action.equalsIgnoreCase("colorType"))
        {
            /*if(colorTypeArrayList.get(position).isSelected())
            {
                colorTypeArrayList.get(position).setSelected(!colorTypeArrayList.get(position).isSelected());
            }
            else{
                colorTypeArrayList.get(position).setSelected(true);
            }*/

            //String displayAttr = colorTypeArrayList.get(position).getDisplayAttr();

            colorTypeArrayList.get(position).setSelected(!colorTypeArrayList.get(position).isSelected());

            forBestFilterSelectUnselectUsingColor("colorType");

            colorTypeListAdapter.notifyDataSetChanged();

        } else if (action.equalsIgnoreCase("fancyColorType")) {
            fancyColorTypeArrayList.get(position).setSelected(!fancyColorTypeArrayList.get(position).isSelected());
            forBestFilterSelectUnselectUsingColor("fancyColorType");
            fancyColorTypeListAdapter.notifyDataSetChanged();
        } else if (action.equalsIgnoreCase("clarityType")) {
            clarityTypeArrayList.get(position).setSelected(!clarityTypeArrayList.get(position).isSelected());
            forBestFilterSelectUnselectUsingColor("clarityType");
            clarityTypeListAdapter.notifyDataSetChanged();
        } else if (action.equalsIgnoreCase("certificateType")) {
            certificateTypeArrayList.get(position).setSelected(!certificateTypeArrayList.get(position).isSelected());
            forBestFilterSelectUnselectUsingColor("certificateType");
            certificateTypeListAdapter.notifyDataSetChanged();
        } else if (action.equalsIgnoreCase("fluoresenceType")) {
            rescenceTypeModelArrayList.get(position).setSelected(!rescenceTypeModelArrayList.get(position).isSelected());
            forBestFilterSelectUnselectUsingColor("fluoresenceType");
            fluoRescenceTypeListAdapter.notifyDataSetChanged();
        } else if (action.equalsIgnoreCase("makeType")) {
            //makeTypeArrayList.get(position).setSelected(!makeTypeArrayList.get(position).isSelected());

            boolean shouldSelect = !makeTypeArrayList.get(position).isSelected();
            for (int i = 0; i < makeTypeArrayList.size(); i++) {
                makeTypeArrayList.get(i).setSelected(false);
            }

            makeTypeArrayList.get(position).setSelected(shouldSelect);

            if (shouldSelect) {
                makeSelectedValue = makeTypeArrayList.get(position).getDisplayAttr();
                Constant.makeValue = makeTypeArrayList.get(position).getAttribCode();
            } else {
                makeSelectedValue = "";
            }

            // resetAdvacnceFilterAllSelections();
            // Constant.colorTypeFilterApploedArrayList.clear();

            Log.e("------shouldSelect------ : ", "" + makeSelectedValue);

            /*if(makeSelectedValue.equalsIgnoreCase(""))
            {
                Constant.colorTypeFilterApploedArrayList.clear();
            }
            else{}*/

            forBestFilterSelectUnselectUsingColor("makeType");

            makeTypeListAdapter.notifyDataSetChanged();
        } else if (action.equalsIgnoreCase("shapeImage")) {
            // Click First Position Select All and Again Click First Position Unselect all
            /*if(shapeImageArrayList.get(position).isFirstPosition())
            {
                boolean shouldSelectAll = !shapeImageArrayList.get(position).isSelected();
                for (int i = 0; i < shapeImageArrayList.size(); i++) {
                    shapeImageArrayList.get(i).setSelected(shouldSelectAll);
                }
            }
            else
            {
                shapeImageArrayList.get(position).setSelected(!shapeImageArrayList.get(position).isSelected());
            }
            shapeImageListAdapter.notifyDataSetChanged();*/


            ShapeImageModel selectedItem = shapeImageArrayList.get(position);

            if (position == 0) { // Assuming position 0 is "All"
                boolean selectAll = !selectedItem.isSelected(); // Toggle the "All" state
                for (int i = 0; i < shapeImageArrayList.size(); i++) {
                    shapeImageArrayList.get(i).setSelected(selectAll); // Apply the state to all items
                }
            } else {
                // Handle individual item selection
                boolean selectedItemSelected = selectedItem.isSelected();
                selectedItem.setSelected(!selectedItemSelected); // Toggle the state of the selected item

                // Check if "All" should be updated
                boolean allSelected = true;
                for (int i = 1; i < shapeImageArrayList.size(); i++) { // Skip "All"
                    if (!shapeImageArrayList.get(i).isSelected()) {
                        allSelected = false;
                        break;
                    }
                }

                // Update "All" based on the state of the other items
                shapeImageArrayList.get(0).setSelected(allSelected);
            }

            // Notify the adapter of data changes
            shapeImageListAdapter.notifyDataSetChanged();
        }
    }

    public void onBindDetails(boolean showLoader)
    {
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

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

        try {
            Log.v("Diamonds : ", "--------JSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {

                case ApiConstants.GET_ATTRIBUTES_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if(colorTypeArrayList.size()>0)
                        {
                            colorTypeArrayList.clear();
                        }
                        if(fancyColorTypeArrayList.size()>0)
                        {
                            fancyColorTypeArrayList.clear();
                        }
                        if(clarityTypeArrayList.size()>0)
                        {
                            clarityTypeArrayList.clear();
                        }
                        if(certificateTypeArrayList.size()>0)
                        {
                            certificateTypeArrayList.clear();
                        }
                        if(rescenceTypeModelArrayList.size()>0)
                        {
                            rescenceTypeModelArrayList.clear();
                        }
                        if(makeTypeArrayList.size()>0)
                        {
                            makeTypeArrayList.clear();
                        }
                        if(Constant.cutTypeArrayList.size()>0)
                        {
                            Constant.cutTypeArrayList.clear();
                        }
                        if(Constant.polishTypeArrayList.size()>0)
                        {
                            Constant.polishTypeArrayList.clear();
                        }
                        if(Constant.symmertyTypeArrayList.size()>0)
                        {
                            Constant.symmertyTypeArrayList.clear();
                        }
                        if(Constant.technologyTypeArrayList.size()>0)
                        {
                            Constant.technologyTypeArrayList.clear();
                        }
                        if(Constant.fancyColorIntensityArrayList.size()>0)
                        {
                            Constant.fancyColorIntensityArrayList.clear();
                        }
                        if(Constant.fancyColorOverToneArrayList.size()>0)
                        {
                            Constant.fancyColorOverToneArrayList.clear();
                        }
                        if(Constant.tableDataPercantageArrayList.size()>0)
                        {
                            Constant.tableDataPercantageArrayList.clear();
                        }
                        if(Constant.depthDataPercantageArrayList.size()>0)
                        {
                            Constant.depthDataPercantageArrayList.clear();
                        }
                        if(Constant.crowmArrayList.size()>0)
                        {
                            Constant.crowmArrayList.clear();
                        }
                        if(Constant.pavillionArrayList.size()>0)
                        {
                            Constant.pavillionArrayList.clear();
                        }
                        if(Constant.eyeCleanArrayList.size()>0)
                        {
                            Constant.eyeCleanArrayList.clear();
                        }
                        if(Constant.shadeArrayList.size()>0)
                        {
                            Constant.shadeArrayList.clear();
                        }
                        if(Constant.lusterArrayList.size()>0)
                        {
                            Constant.lusterArrayList.clear();
                        }

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCode = details.getJSONObject(i);

                            String AttribType = objectCode.getString("AttribType");

                            JSONArray attribDetails = objectCode.getJSONArray("AttribDetails");

                            for (int j = 0; j < attribDetails.length(); j++)
                            {
                                JSONObject innerObjectCodes = attribDetails.getJSONObject(j);

                               // String ss = innerObjectCodes.optString("DisplayAttr");

                                AttributeDetailsModel model = new AttributeDetailsModel();

                                model.setAttribId(CommonUtility.checkString(innerObjectCodes.optString("AttribId")));
                                model.setAttribTypeId(CommonUtility.checkString(innerObjectCodes.optString("AttribTypeId")));
                                model.setAttribType(CommonUtility.checkString(innerObjectCodes.optString("AttribType")));
                                model.setAttribCode(CommonUtility.checkString(innerObjectCodes.optString("AttribCode")));
                                model.setSortOrder(CommonUtility.checkString(innerObjectCodes.optString("SortOrder")));
                                model.setDisplayAttr(CommonUtility.checkString(innerObjectCodes.optString("DisplayAttr")));
                                model.setFirstPosition(getFirstPosition(model, j));
                                model.setSelected(false);

                                setParsingData(model, AttribType);

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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void setParsingData(AttributeDetailsModel model, String type)
    {
        if(type.equalsIgnoreCase("STONE COLOR"))
        {
            colorTypeArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(colorTypeArrayList);
            CommonUtility.saveLocalDataList(context, "colorArrayList", json);

            colorTypeListAdapter = new ColorTypeListAdapter(colorTypeArrayList,context,this);
            recycler_color_view.setAdapter(colorTypeListAdapter);
        }
        else if(type.equalsIgnoreCase("FANCY COLOR"))
        {
            fancyColorTypeArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(fancyColorTypeArrayList);
            CommonUtility.saveLocalDataList(context, "fancyColorArrayList", json);

            fancyColorTypeListAdapter = new FancyColorTypeListAdapter(fancyColorTypeArrayList,context,this);
            recycler_color_view.setAdapter(fancyColorTypeListAdapter);
        }
        else if(type.equalsIgnoreCase("CLARITY"))
        {
            clarityTypeArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(clarityTypeArrayList);
            CommonUtility.saveLocalDataList(context, "clarityArrayList", json);

            clarityTypeListAdapter = new ClarityTypeListAdapter(clarityTypeArrayList,context,this);
            recycler_clarity_view.setAdapter(clarityTypeListAdapter);
        }
        else if(type.equalsIgnoreCase("LAB NAME"))
        {
            certificateTypeArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(certificateTypeArrayList);
            CommonUtility.saveLocalDataList(context, "certificateArrayList", json);

            certificateTypeListAdapter = new CertificateTypeListAdapter(certificateTypeArrayList,context,this);
            recycler_certificate_view.setAdapter(certificateTypeListAdapter);
        }
        else if(type.equalsIgnoreCase("FLUORESCENCE INTENSITY"))
        {
            rescenceTypeModelArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(rescenceTypeModelArrayList);
            CommonUtility.saveLocalDataList(context, "rescenceModelArrayList", json);

            fluoRescenceTypeListAdapter = new FluoRescenceTypeListAdapter(rescenceTypeModelArrayList,context,this);
            recycler_fluorescence_view.setAdapter(fluoRescenceTypeListAdapter);
        }
       else if(type.equalsIgnoreCase("MAKE"))
        {
            makeTypeArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(makeTypeArrayList);
            CommonUtility.saveLocalDataList(context, "makeArrayList", json);

            makeTypeListAdapter = new MakeTypeListAdapter(makeTypeArrayList,context,this);
            recycler_make_view.setAdapter(makeTypeListAdapter);
        }
        else if(type.equalsIgnoreCase("CUT GRADE"))
        {
            //cutTypeArrayList.add(model);
            Constant.cutTypeArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(Constant.cutTypeArrayList);
            CommonUtility.saveLocalDataList(context, "cutArrayList", json);
        }
        else if(type.equalsIgnoreCase("POLISH"))
        {
            Constant.polishTypeArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(Constant.polishTypeArrayList);
            CommonUtility.saveLocalDataList(context, "polishArrayList", json);
        }
        else if(type.equalsIgnoreCase("SYMMETRY"))
        {
            Constant.symmertyTypeArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(Constant.symmertyTypeArrayList);
            CommonUtility.saveLocalDataList(context, "symmertyArrayList", json);
        }
        else if(type.equalsIgnoreCase("TECHNOLOGY"))
        {
            Constant.technologyTypeArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(Constant.technologyTypeArrayList);
            CommonUtility.saveLocalDataList(context, "technologyArrayList", json);
        }
        else if(type.equalsIgnoreCase("FANCY COLOR INTENSITY"))
        {
            Constant.fancyColorIntensityArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(Constant.fancyColorIntensityArrayList);
            CommonUtility.saveLocalDataList(context, "fancyColorIntensityArrayListData", json);
        }
        else if(type.equalsIgnoreCase("FANCY COLOR OVERTONE"))
        {
            Constant.fancyColorOverToneArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(Constant.fancyColorOverToneArrayList);
            CommonUtility.saveLocalDataList(context, "fancyColorOverToneArrayListData", json);
        }
        else if(type.equalsIgnoreCase("TABLE PERCENTAGE"))
        {
            Constant.tableDataPercantageArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(Constant.tableDataPercantageArrayList);
            CommonUtility.saveLocalDataList(context, "tableDataPercantageArrayListData", json);
        }
        else if(type.equalsIgnoreCase("DEPTH PERCENTAGE"))
        {
            Constant.depthDataPercantageArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(Constant.depthDataPercantageArrayList);
            CommonUtility.saveLocalDataList(context, "depthDataPercantageArrayListData", json);
        }
        else if(type.equalsIgnoreCase("CROWN ANGLE"))
        {
            Constant.crowmArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(Constant.crowmArrayList);
            CommonUtility.saveLocalDataList(context, "crowmArrayListData", json);
        }
        else if(type.equalsIgnoreCase("PAVILION ANGLE"))
        {
            Constant.pavillionArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(Constant.pavillionArrayList);
            CommonUtility.saveLocalDataList(context, "pavillionArrayListData", json);
        }
        else if(type.equalsIgnoreCase("EYE CLEAN"))
        {
            Constant.eyeCleanArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(Constant.eyeCleanArrayList);
            CommonUtility.saveLocalDataList(context, "eyeCleanArrayListData", json);
        }
        else if(type.equalsIgnoreCase("SHADE"))
        {
            Constant.shadeArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(Constant.shadeArrayList);
            CommonUtility.saveLocalDataList(context, "shadeArrayListData", json);
        }
        else if(type.equalsIgnoreCase("LUSTER"))
        {
            Constant.lusterArrayList.add(model);

            Gson gson = new Gson();
            String json = gson.toJson(Constant.lusterArrayList);
            CommonUtility.saveLocalDataList(context, "lusterArrayListData", json);
        }

    }

    boolean getFirstPosition(AttributeDetailsModel model, int index)
    {
        if(index == 0)
        {
            model.setFirstPosition(true);
            return true;
        }else{
          model.setFirstPosition(false);;
            return false;
        }
    }

    @Override
    public void getErrorResponce(String error, int service_ID) {

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void forBestFilterSelectUnselectUsingColor(String type)
    {
        if(diamondQualitySelected.equalsIgnoreCase(BEST))
        {
            handleFilterBestTabCombination();
        }
        else if(diamondQualitySelected.equalsIgnoreCase(MEDIUM))
        {
            handleFilterMediumTabCombination();

        }
        else if(diamondQualitySelected.equalsIgnoreCase(LOW))
        {
            handleFilterLowTabCombination();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void handleFilterBestTabCombination()
    {
        boolean hasD = false, hasE = false, hasF = false;
        boolean hasYellowBn = false, hasGreenBlue = false, hasBrownPink = false;
        boolean hasFL = false, hasIF = false, hasVVS1 = false, hasVVS2 = false;
        boolean hasGIA = false, hasIGI = false;
        boolean hasNone = false;
        boolean has3EX = false;
        boolean hasOtherColorType = false, hasOtherFancyColorType = false, hasOtherClarityType = false, hasOtherCertificateType = false,hasOtherFluorescenceType = false,
                hasOtherMakeType = false;

        // Check colorTypeArrayList
        for (AttributeDetailsModel item : colorTypeArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            if (displayAttr.equalsIgnoreCase("D") && isSelected) {
                hasD = true;
            } else if (displayAttr.equalsIgnoreCase("E") && isSelected) {
                hasE = true;
            } else if (displayAttr.equalsIgnoreCase("F") && isSelected) {
                hasF = true;
            } else if (isSelected) {
                hasOtherColorType = true;
            }
        }

        // Check fancyColorTypeArrayList
        for (AttributeDetailsModel item : fancyColorTypeArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            if (displayAttr.equalsIgnoreCase("Yellow-Bn") && isSelected) {
                hasYellowBn = true;
            } else if (displayAttr.equalsIgnoreCase("Green-Blue") && isSelected) {
                hasGreenBlue = true;
            } else if (displayAttr.equalsIgnoreCase("Brown-Pink") && isSelected) {
                hasBrownPink = true;
            } else if (isSelected) {
                hasOtherFancyColorType = true;
            }
        }

        // Check clarityTypeArrayList
        for (AttributeDetailsModel item : clarityTypeArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            if (displayAttr.equalsIgnoreCase("FL") && isSelected) {
                hasFL = true;
            } else if (displayAttr.equalsIgnoreCase("IF") && isSelected) {
                hasIF = true;
            } else if (displayAttr.equalsIgnoreCase("VVS1") && isSelected) {
                hasVVS1 = true;
            } else if (displayAttr.equalsIgnoreCase("VVS2") && isSelected) {
                hasVVS2 = true;
            } else if (isSelected) {
                hasOtherClarityType = true;
            }
        }

        // Check certificateTypeArrayList
        for (AttributeDetailsModel item : certificateTypeArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            if (displayAttr.equalsIgnoreCase("GIA") && isSelected) {
                hasGIA = true;
            } else if (displayAttr.equalsIgnoreCase("IGI") && isSelected) {
                hasIGI = true;
            } else if (isSelected) {
                hasOtherCertificateType = true;
            }
        }

        // Check rescenceTypeModelArrayList
        for (AttributeDetailsModel item : rescenceTypeModelArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            if (displayAttr.equalsIgnoreCase("None") && isSelected) {
                hasNone = true;
            } else if (isSelected) {
                hasOtherFluorescenceType = true;
            }
        }

        // Check makeTypeArrayList
        for (AttributeDetailsModel item : makeTypeArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            if (displayAttr.equalsIgnoreCase("3EX") && isSelected) {
                has3EX = true;
            } else if (isSelected) {
                hasOtherMakeType = true;
            }
        }

        // Check if all required items are selected and no other items are selected
        boolean allColorTypeSelected = hasD && hasE && hasF;
        boolean allColorFancyTypeSelected = hasYellowBn && hasGreenBlue && hasBrownPink;
        boolean allClarityTypeSelected = hasFL && hasIF && hasVVS1 && hasVVS2;
        boolean allCertificateTypeSelected = hasGIA && hasIGI;
        boolean allFluorescenceTypeSelected = hasNone;
        boolean allMakeTypeSelected = has3EX;

        if(Constant.colorType.equalsIgnoreCase(WHITE))
        {
            if (allColorTypeSelected && allClarityTypeSelected && allCertificateTypeSelected  && allFluorescenceTypeSelected && allMakeTypeSelected &&
                    !hasOtherColorType && !hasOtherClarityType && !hasOtherCertificateType && !hasOtherFluorescenceType &&
                    !hasOtherMakeType) {
                best_tv.setBackgroundResource(R.drawable.background_gradient);
                best_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
                card_view_best.setCardElevation(37f);
                card_view_best.setCardElevation(18f);
                card_view_best.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
                card_view_best.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
            } else {
                best_tv.setBackgroundResource(R.drawable.border_without_line);
                best_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
                card_view_best.setCardElevation(0f);
                card_view_best.setCardElevation(0f);
                card_view_best.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.bg_color));
                card_view_best.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.bg_color));
            }
        }
        else{
            if (allColorFancyTypeSelected && allClarityTypeSelected && allCertificateTypeSelected  && allFluorescenceTypeSelected && allMakeTypeSelected &&
                    !hasOtherFancyColorType && !hasOtherClarityType && !hasOtherCertificateType && !hasOtherFluorescenceType &&
                    !hasOtherMakeType) {
                best_tv.setBackgroundResource(R.drawable.background_gradient);
                best_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
                card_view_best.setCardElevation(37f);
                card_view_best.setCardElevation(18f);
                card_view_best.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
                card_view_best.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
            } else {
                best_tv.setBackgroundResource(R.drawable.border_without_line);
                best_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
                card_view_best.setCardElevation(0f);
                card_view_best.setCardElevation(0f);
                card_view_best.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.bg_color));
                card_view_best.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.bg_color));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void handleFilterMediumTabCombination()
    {
        boolean hasD = false, hasE = false, hasF = false;
        boolean hasYellowBn = false, hasGreenBlue = false, hasBrownPink = false;
        boolean hasFL = false, hasIF = false;
        boolean hasGIA = false, hasIGI = false;
        boolean hasNone = false, hasFaint = false, hasMedium =false;
        boolean has3EX = false;
        boolean hasOtherColorType = false, hasOtherFancyColorType = false, hasOtherClarityType = false, hasOtherCertificateType = false,hasOtherFluorescenceType = false,
                hasOtherMakeType = false;

        // Check colorTypeArrayList
        for (AttributeDetailsModel item : colorTypeArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            if (displayAttr.equalsIgnoreCase("G") && isSelected) {
                hasD = true;
            } else if (displayAttr.equalsIgnoreCase("H") && isSelected) {
                hasE = true;
            } else if (displayAttr.equalsIgnoreCase("I") && isSelected) {
                hasF = true;
            } else if (isSelected) {
                hasOtherColorType = true;
            }
        }

        // Check fancyColorTypeArrayList
        for (AttributeDetailsModel item : fancyColorTypeArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            if (displayAttr.equalsIgnoreCase("Black") && isSelected) {
                hasYellowBn = true;
            } else if (displayAttr.equalsIgnoreCase("Blue") && isSelected) {
                hasGreenBlue = true;
            } else if (displayAttr.equalsIgnoreCase("Brown") && isSelected) {
                hasBrownPink = true;
            } else if (isSelected) {
                hasOtherFancyColorType = true;
            }
        }

        // Check clarityTypeArrayList
        for (AttributeDetailsModel item : clarityTypeArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            if (displayAttr.equalsIgnoreCase("VS1") && isSelected) {
                hasFL = true;
            } else if (displayAttr.equalsIgnoreCase("VS2") && isSelected) {
                hasIF = true;
            }  else if (isSelected) {
                hasOtherClarityType = true;
            }
        }

        // Check certificateTypeArrayList
        for (AttributeDetailsModel item : certificateTypeArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            if (displayAttr.equalsIgnoreCase("GIA") && isSelected) {
                hasGIA = true;
            } else if (displayAttr.equalsIgnoreCase("IGI") && isSelected) {
                hasIGI = true;
            } else if (isSelected) {
                hasOtherCertificateType = true;
            }
        }

        // Check rescenceTypeModelArrayList
        for (AttributeDetailsModel item : rescenceTypeModelArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            if (displayAttr.equalsIgnoreCase("None") && isSelected) {
                hasNone = true;
            }
            else if (displayAttr.equalsIgnoreCase("Faint") && isSelected) {
                hasFaint = true;
            }
            else if (displayAttr.equalsIgnoreCase("Medium") && isSelected) {
                hasMedium = true;
            }else if (isSelected) {
                hasOtherFluorescenceType = true;
            }
        }

        // Check makeTypeArrayList
        for (AttributeDetailsModel item : makeTypeArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            //Log.e("displayAttr : ", "displayAttr : " + displayAttr.toString() + " : " + isSelected);
            if (displayAttr.equalsIgnoreCase("3 VG+") && isSelected) {
                has3EX = true;
            } else if (isSelected) {
                hasOtherMakeType = true;
            }
        }

        // Check if all required items are selected and no other items are selected
        boolean allColorTypeSelected = hasD && hasE && hasF;
        boolean allColorFancyTypeSelected = hasYellowBn && hasGreenBlue && hasBrownPink;
        boolean allClarityTypeSelected = hasFL && hasIF;
        boolean allCertificateTypeSelected = hasGIA && hasIGI;
        boolean allFluorescenceTypeSelected = hasNone && hasFaint && hasMedium;
        boolean allMakeTypeSelected = has3EX;

        if(Constant.colorType.equalsIgnoreCase(WHITE))
        {
            if (allColorTypeSelected && allClarityTypeSelected && allCertificateTypeSelected  && allFluorescenceTypeSelected && allMakeTypeSelected &&
                    !hasOtherColorType && !hasOtherClarityType && !hasOtherCertificateType && !hasOtherFluorescenceType &&
                    !hasOtherMakeType) {
                medium_tv.setBackgroundResource(R.drawable.background_gradient);
                medium_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
                card_view_medium.setCardElevation(37f);
                card_view_medium.setCardElevation(18f);
                card_view_medium.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
                card_view_medium.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
            } else {
                medium_tv.setBackgroundResource(R.drawable.border_without_line);
                medium_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
                card_view_medium.setCardElevation(0f);
                card_view_medium.setCardElevation(0f);
                card_view_medium.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.bg_color));
                card_view_medium.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.bg_color));
            }
        }
        else{
            if (allColorFancyTypeSelected && allClarityTypeSelected && allCertificateTypeSelected  && allFluorescenceTypeSelected && allMakeTypeSelected &&
                    !hasOtherFancyColorType && !hasOtherClarityType && !hasOtherCertificateType && !hasOtherFluorescenceType &&
                    !hasOtherMakeType) {
                medium_tv.setBackgroundResource(R.drawable.background_gradient);
                medium_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
                card_view_medium.setCardElevation(37f);
                card_view_medium.setCardElevation(18f);
                card_view_medium.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
                card_view_medium.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
            } else {
                medium_tv.setBackgroundResource(R.drawable.border_without_line);
                medium_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
                card_view_medium.setCardElevation(0f);
                card_view_medium.setCardElevation(0f);
                card_view_medium.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.bg_color));
                card_view_medium.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.bg_color));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void handleFilterLowTabCombination()
    {
        boolean hasD = false, hasE = false, hasF = false;
        boolean hasYellowBn = false, hasGreenBlue = false, hasBrownPink = false;
        boolean hasFL = false, hasIF = false, hasSI = false, hasII = false;
        boolean hasGIA = false, hasIGI = false;
        boolean hasNone = false, hasFaint = false, hasMedium =false, hasStrong =false, hasVStrong =false;
        boolean has3EX = false;
        boolean hasOtherColorType = false, hasOtherFancyColorType = false, hasOtherClarityType = false, hasOtherCertificateType = false,hasOtherFluorescenceType = false,
                hasOtherMakeType = false;

        // Check colorTypeArrayList
        for (AttributeDetailsModel item : colorTypeArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            if (displayAttr.equalsIgnoreCase("J") && isSelected) {
                hasD = true;
            } else if (displayAttr.equalsIgnoreCase("K") && isSelected) {
                hasE = true;
            } else if (displayAttr.equalsIgnoreCase("L") && isSelected) {
                hasF = true;
            } else if (isSelected) {
                hasOtherColorType = true;
            }
        }

        // Check fancyColorTypeArrayList
        for (AttributeDetailsModel item : fancyColorTypeArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            if (displayAttr.equalsIgnoreCase("Gray") && isSelected) {
                hasYellowBn = true;
            } else if (displayAttr.equalsIgnoreCase("Gray-Blue") && isSelected) {
                hasGreenBlue = true;
            } else if (displayAttr.equalsIgnoreCase("Green") && isSelected) {
                hasBrownPink = true;
            } else if (isSelected) {
                hasOtherFancyColorType = true;
            }
        }

        // Check clarityTypeArrayList
        for (AttributeDetailsModel item : clarityTypeArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            if (displayAttr.equalsIgnoreCase("SI1") && isSelected) {
                hasFL = true;
            } else if (displayAttr.equalsIgnoreCase("SI2") && isSelected) {
                hasIF = true;
            } else if (displayAttr.equalsIgnoreCase("SI3") && isSelected) {
                hasSI = true;
            } else if (displayAttr.equalsIgnoreCase("I1") && isSelected) {
                hasII = true;
            }  else if (isSelected) {
                hasOtherClarityType = true;
            }
        }

        // Check certificateTypeArrayList
        for (AttributeDetailsModel item : certificateTypeArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            if (displayAttr.equalsIgnoreCase("GIA") && isSelected) {
                hasGIA = true;
            } else if (displayAttr.equalsIgnoreCase("IGI") && isSelected) {
                hasIGI = true;
            } else if (isSelected) {
                hasOtherCertificateType = true;
            }
        }

        // Check rescenceTypeModelArrayList
        for (AttributeDetailsModel item : rescenceTypeModelArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            if (displayAttr.equalsIgnoreCase("None") && isSelected) {
                hasNone = true;
            }
            else if (displayAttr.equalsIgnoreCase("Faint") && isSelected) {
                hasFaint = true;
            }
            else if (displayAttr.equalsIgnoreCase("Medium") && isSelected) {
                hasMedium = true;
            }else if (displayAttr.equalsIgnoreCase("Strong") && isSelected) {
                hasStrong = true;
            }else if (displayAttr.equalsIgnoreCase("V-Strong") && isSelected) {
                hasVStrong = true;
            }else if (isSelected) {
                hasOtherFluorescenceType = true;
            }
        }

        // Check makeTypeArrayList
        for (AttributeDetailsModel item : makeTypeArrayList) {
            String displayAttr = item.getDisplayAttr();
            boolean isSelected = item.isSelected();

            if (displayAttr.equalsIgnoreCase("3 VG+") && isSelected) {
                has3EX = true;
            } else if (isSelected) {
                hasOtherMakeType = true;
            }
        }

        // Check if all required items are selected and no other items are selected
        boolean allColorTypeSelected = hasD && hasE && hasF;
        boolean allColorFancyTypeSelected = hasYellowBn && hasGreenBlue && hasBrownPink;
        boolean allClarityTypeSelected = hasFL && hasIF && hasSI && hasII;
        boolean allCertificateTypeSelected = hasGIA && hasIGI;
        boolean allFluorescenceTypeSelected = hasNone && hasFaint && hasMedium && hasStrong && hasVStrong;
        boolean allMakeTypeSelected = has3EX;

        if(Constant.colorType.equalsIgnoreCase(WHITE))
        {
            if (allColorTypeSelected && allClarityTypeSelected && allCertificateTypeSelected  && allFluorescenceTypeSelected && allMakeTypeSelected &&
                    !hasOtherColorType && !hasOtherClarityType && !hasOtherCertificateType && !hasOtherFluorescenceType &&
                    !hasOtherMakeType) {
                low_tv.setBackgroundResource(R.drawable.background_gradient);
                low_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
                card_view_low.setCardElevation(37f);
                card_view_low.setCardElevation(18f);
                card_view_low.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
                card_view_low.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
            } else {
                low_tv.setBackgroundResource(R.drawable.border_without_line);
                low_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
                card_view_low.setCardElevation(0f);
                card_view_low.setCardElevation(0f);
                card_view_low.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.bg_color));
                card_view_low.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.bg_color));
            }
        }
        else{
            if (allColorFancyTypeSelected && allClarityTypeSelected && allCertificateTypeSelected  && allFluorescenceTypeSelected && allMakeTypeSelected &&
                    !hasOtherFancyColorType && !hasOtherClarityType && !hasOtherCertificateType && !hasOtherFluorescenceType &&
                    !hasOtherMakeType) {
                low_tv.setBackgroundResource(R.drawable.background_gradient);
                low_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
                card_view_low.setCardElevation(37f);
                card_view_low.setCardElevation(18f);
                card_view_low.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
                card_view_low.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.purple_gradient_bottom));
            } else {
                low_tv.setBackgroundResource(R.drawable.border_without_line);
                low_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
                card_view_low.setCardElevation(0f);
                card_view_low.setCardElevation(0f);
                card_view_low.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.bg_color));
                card_view_low.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.bg_color));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void clearAllFilter()
    {
        //Constant.filterClear = "yes"; // This is USe For Clear All Select Filter BG Color

        Constant.shapeDiamondValue="";
        Constant.priceFrm="";
        Constant.priceTo="";
        Constant.caratFrm="";
        Constant.caratTo="";
        //Constant.colorType="";
        Constant.colorValue="";
        Constant.fancyColorValue="";
        Constant.clarityValue="";
        Constant.certificateValue="";
        Constant.fluorescenceValue="";
        Constant.makeValue="";
        Constant.isReturnable="";
        Constant.searchKeyword="";
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
        Constant.sortingBy="";

        clearSelectionFilterBGColor();

        // If clearAllClick != to "yes", Then finish() Work. clearAllClick "yes" Value Conation When USer Click "Clear All" Text.
        if(!clearAllClick.equalsIgnoreCase("yes"))
        {
            finish();
        }
        else{
            clearAllClick = "";
        }

        /*// Constant.filterClear Blank Means Back Screen.
        if(Constant.filterClear.equalsIgnoreCase(""))
        {
            finish();
        }
        else{

            clearSelectionFilterBGColor();
        }*/
    }
}