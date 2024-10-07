package com.diamondxe.Fragment;

import static com.diamondxe.ApiCalling.ApiConstants.ECONOMIC_TIMES;
import static com.diamondxe.ApiCalling.ApiConstants.MIDDAY;
import static com.diamondxe.ApiCalling.ApiConstants.RETAIL_JEWELLER;
import static com.diamondxe.ApiCalling.ApiConstants.USER_BUYER;
import static com.diamondxe.ApiCalling.ApiConstants.USER_DEALER;
import static com.diamondxe.ApiCalling.ApiConstants.VIEW_ALL_NEWS;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diamondxe.Activity.AccountSectionActivity;
import com.diamondxe.Activity.DiamondDetailsActivity;
import com.diamondxe.Activity.HomeScreenActivity;
import com.diamondxe.Activity.Jewellery.JewelleryScreenActivity;
import com.diamondxe.Activity.LoginScreenActivity;
import com.diamondxe.Activity.SearchDiamondsActivity;
import com.diamondxe.Adapter.CustomerStoriesListAdapter;
import com.diamondxe.Adapter.GiftImageListAdapter;
import com.diamondxe.Adapter.LabGrownDiamondListAdapter;
import com.diamondxe.Adapter.MediaSpotLightListAdapter;
import com.diamondxe.Adapter.NewArrivalsImageListAdapter;
import com.diamondxe.Adapter.NaturalDiamondListAdapter;
import com.diamondxe.Adapter.TopImageOptionListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.CountryListModel;
import com.diamondxe.Beans.CustomerStoriesModel;
import com.diamondxe.Beans.GiftImageModel;
import com.diamondxe.Beans.HomeListModel;
import com.diamondxe.Beans.MediaSpotLightModel;
import com.diamondxe.Beans.NewArrivalsImageModel;
import com.diamondxe.Beans.PagerImageModel;
import com.diamondxe.Beans.TopDiamondImageModel;
import com.diamondxe.Beans.TopImageOptionModel;
import com.diamondxe.Interface.FragmentActionInterface;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.EndlessRecyclerViewScrollListener;
import com.diamondxe.Network.SuperFragment;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

public class HomeFragment extends SuperFragment implements RecyclerInterface,View.OnClickListener, FragmentActionInterface {

    private TextView natural_diamond_tv, grown_diamond_tv, giftChoiceTextView, newArrivalsTextView, mediaSpotLightTextView, customerStoriesTextView,
            view_all_news_tv;
    private ImageView registerImg, dxeLuxeImg, bottom_search_icon;
    private RelativeLayout search_circle_rel;
    private LinearLayout solitaries_lin, jewellery_lin;
    private CardView solitaires_card_view,rings_card_view,earrings_card_view,pendent_card_view,bracelet_card_view,bangles_card_view,transparent_price_card_view,exchange_buy_back_card_view,
            certificate_lab_card_view,insured_door_step_card_view,customer_support_card_view;
    private RecyclerView recyclerGiftView, recyclerNewArrivalsView, recyclerNaturalGrownView, recyclerMediaSpotlight, recyclerCustomerStories,
            recyclerTopOptionView;
    public ArrayList<PagerImageModel> bannerImageArrayList;
    public ArrayList<GiftImageModel> giftImageArrayList;
    public ArrayList<NewArrivalsImageModel> arrivalsImageArrayList;
    public ArrayList<TopDiamondImageModel> naturalDiamondArrayList;
    public ArrayList<TopDiamondImageModel> labGrownDiamondArrayList;
    public ArrayList<MediaSpotLightModel> mediaSpotLightModelArrayList;
    public ArrayList<CustomerStoriesModel> customerStoriesArrayList;
    public ArrayList<TopImageOptionModel> topOptionArrayList;
    private long lastBackPressedTime = 0;
    private static final long BACK_PRESS_INTERVAL = 2000; // 2 seconds
    int NUM_PAGES = 0, currentPage = 0;
    private HomeScreenActivity drawerActivity;
    private Activity activity;
    private Context context;
    String selectedTopDeals = "naturalDiamond";
    private SwipeRefreshLayout swipeContainer;
    private RelativeLayout home_rel, category_rel, wishlist_rel, cart_rel, account_rel;
    private ImageView home_img, categories_img, wish_img, cart_img, account_img;
    private TextView home_tv, categories_tv, wish_tv, cart_tv, account_tv, cart_count_tv, wish_list_count_tv;

    String user_login ="";
    ViewPager viewPager, viewPagerCustomer;
    TabLayout tabLayout;
    //private CirclePageIndicator circle_indicator;
    private RelativeLayout viewpager_layout, viewpager_layout_customer;

    private ArrayList<HomeListModel> modelArrayList;

    //HomeListAdapter adapter;

    private GiftImageListAdapter giftImageListAdapter;
    private NewArrivalsImageListAdapter arrivalsImageListAdapter;
    private NaturalDiamondListAdapter naturalDiamondListAdapter;
    private LabGrownDiamondListAdapter labGrownDiamondListAdapter;
    private MediaSpotLightListAdapter mediaSpotLightListAdapter;
    private CustomerStoriesListAdapter customerStoriesListAdapter;

    private TopImageOptionListAdapter topImageOptionListAdapter;

    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;

    int page_no = 1;
    private EndlessRecyclerViewScrollListener scrollListener;

    String middleBannersTitle="",featuresTitle="",registerNowTitle="",registerNowImage="", registerNowLink="" , registerNowButton="", dxeLuxeTitle="",
            dxeLuxeImage="",dxeLuxeLink="",mediaTitle="",customerStoriesTitle="";
    String selectedCurrencyCode = "", selectedCurrencyValue = "", selectedCurrencyDesc="", selectedCurrencyImage="";
    String cmsEmail = "", cmsPhone1 = "", cmsPhone2 = "", cmsAddress = "", cmsLinkedinLink = "", cmsTwitterLink = "", cmsFacebookLink = "", cmsInstagramLink = "", cmsYoutubeLink = "";

    int newWith;
    int width;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        context = activity = drawerActivity = (HomeScreenActivity) getActivity();

        drawerActivity.setListener(this);

        init(view);

        return view;
    }
    // Get Currency Value Code and Image
    void getCurrencyData()
    {
       selectedCurrencyCode = CommonUtility.getGlobalString(context, "selected_currency_code");
       selectedCurrencyValue = CommonUtility.getGlobalString(context, "selected_currency_value");
       selectedCurrencyDesc = CommonUtility.getGlobalString(context, "selected_currency_desc");
       selectedCurrencyImage = CommonUtility.getGlobalString(context, "selected_currency_image");

        if(selectedCurrencyCode.equalsIgnoreCase(""))
        {
            CommonUtility.setGlobalString(context, "selected_currency_code", "INR");
        }else{}

        if(selectedCurrencyValue.equalsIgnoreCase(""))
        {
            CommonUtility.setGlobalString(context, "selected_currency_value", "1");
        }else{}
    }

    private void init(View parentView)
    {
        bannerImageArrayList = new ArrayList<>();
        giftImageArrayList = new ArrayList<>();
        arrivalsImageArrayList = new ArrayList<>();
        naturalDiamondArrayList = new ArrayList<>();
        mediaSpotLightModelArrayList = new ArrayList<>();
        customerStoriesArrayList = new ArrayList<>();
        labGrownDiamondArrayList = new ArrayList<>();
        topOptionArrayList = new ArrayList<>();
        //Constant.currencyArrayList = new ArrayList<>();

        //For calculate Screen width for manage RecyclerView's card's width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        swipeContainer = parentView.findViewById(R.id.swipeContainer);

        getCurrencyData();

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

        home_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));
        categories_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        wish_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        cart_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        account_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));

        home_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
        categories_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));
        wish_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));
        cart_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));
        account_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));


        user_login = CommonUtility.getGlobalString(context, "user_login");
        featuresTitle = CommonUtility.getGlobalString(context, "featuresTitle");
        middleBannersTitle = CommonUtility.getGlobalString(context, "middleBannersTitle");
        mediaTitle = CommonUtility.getGlobalString(context, "mediaTitle");

        // If User Login then User Role Name
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


        solitaries_lin = parentView.findViewById(R.id.solitaries_lin);
        solitaries_lin.setOnClickListener(this);
        jewellery_lin = parentView.findViewById(R.id.jewellery_lin);
        jewellery_lin.setOnClickListener(this);


        solitaires_card_view = parentView.findViewById (R.id.solitaires_card_view);
        solitaires_card_view.setOnClickListener(this);

        rings_card_view = parentView.findViewById (R.id.rings_card_view);
        rings_card_view.setOnClickListener(this);

        earrings_card_view = parentView.findViewById (R.id.earrings_card_view);
        earrings_card_view.setOnClickListener(this);

        pendent_card_view = parentView.findViewById (R.id.pendent_card_view);
        pendent_card_view.setOnClickListener(this);

        bracelet_card_view = parentView.findViewById(R.id.bracelet_card_view);
        bracelet_card_view.setOnClickListener(this);

        bangles_card_view = parentView.findViewById (R.id.bangles_card_view);
        bangles_card_view.setOnClickListener(this);

        viewPager = parentView.findViewById (R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);

        viewPagerCustomer = parentView.findViewById(R.id.viewPagerCustomer);
        viewPagerCustomer.setOffscreenPageLimit(2);

        tabLayout = parentView.findViewById(R.id.tab_layout);

        viewpager_layout = parentView.findViewById(R.id.viewpager_layout);
        viewpager_layout_customer = parentView.findViewById(R.id.viewpager_layout_customer);

        giftChoiceTextView = parentView.findViewById(R.id.gift_of_choice_tv);
        newArrivalsTextView = parentView.findViewById(R.id.new_arrivals_tv);

        natural_diamond_tv = parentView.findViewById(R.id.natural_diamond_tv);
        natural_diamond_tv.setOnClickListener(this);

        grown_diamond_tv = parentView.findViewById(R.id.grown_diamond_tv);
        grown_diamond_tv.setOnClickListener(this);

        registerImg = parentView.findViewById(R.id.register_img);
        registerImg.setOnClickListener(this);

        dxeLuxeImg = parentView.findViewById(R.id.dxe_luxe_img);
        dxeLuxeImg.setOnClickListener(this);

        transparent_price_card_view = parentView.findViewById(R.id.transparent_price_card_view);
        transparent_price_card_view.setOnClickListener(this);

        exchange_buy_back_card_view = parentView.findViewById(R.id.exchange_buy_back_card_view);
        exchange_buy_back_card_view.setOnClickListener(this);

        certificate_lab_card_view = parentView.findViewById(R.id.certificate_lab_card_view);
        certificate_lab_card_view.setOnClickListener(this);

        insured_door_step_card_view = parentView.findViewById(R.id.insured_door_step_card_view);
        insured_door_step_card_view.setOnClickListener(this);

        customer_support_card_view = parentView.findViewById(R.id.customer_support_card_view);
        customer_support_card_view.setOnClickListener(this);

        mediaSpotLightTextView = parentView.findViewById(R.id.media_spot_light_tv);

        view_all_news_tv = parentView.findViewById(R.id.view_all_news_tv);
        view_all_news_tv.setOnClickListener(this);


        bottom_search_icon = parentView.findViewById(R.id.bottom_search_icon);
        search_circle_rel = parentView.findViewById(R.id.search_circle_rel);
        search_circle_rel.setOnClickListener(this);

        bottom_search_icon.setBackgroundResource(R.mipmap.bottom_search);
        // Search Zoom-In and Zoom-Out Animation
        CommonUtility.startZoomAnimation(bottom_search_icon);

        customerStoriesTextView = parentView.findViewById(R.id.customer_stories_tv);

//      circle_indicator = parentView.findViewById(R.id.circle_indicator);

        recyclerGiftView = parentView.findViewById(R.id.recyclerGiftView);

        recyclerTopOptionView = parentView.findViewById(R.id.recyclerTopOptionView);
        recyclerTopOptionView.setHasFixedSize(true);

        recyclerNewArrivalsView = parentView.findViewById(R.id.recyclerNewArrivalsView);
        recyclerNewArrivalsView.setHasFixedSize(true);

        recyclerNaturalGrownView = parentView.findViewById(R.id.recyclerNaturalGrownView);
        recyclerNaturalGrownView.setHasFixedSize(true);

        recyclerMediaSpotlight = parentView.findViewById(R.id.recyclerMediaSpotlight);
        recyclerMediaSpotlight.setHasFixedSize(true);

        recyclerCustomerStories = parentView.findViewById(R.id.recyclerCustomerStories);
        recyclerCustomerStories.setHasFixedSize(true);

        modelArrayList = new ArrayList<>();

        LinearLayoutManager layoutManagerTopOptionImage = new LinearLayoutManager(activity);
        layoutManagerTopOptionImage.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerTopOptionView.setLayoutManager(layoutManagerTopOptionImage);
        recyclerTopOptionView.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManagerTopOption = new LinearLayoutManager(activity);
        layoutManagerTopOption.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerNaturalGrownView.setLayoutManager(layoutManagerTopOption);
        recyclerNaturalGrownView.setNestedScrollingEnabled(false);

        // Top Deals Section
        LinearLayoutManager layoutManagerTopDeals = new LinearLayoutManager(activity);
        layoutManagerTopDeals.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerNaturalGrownView.setLayoutManager(layoutManagerTopDeals);
        recyclerNaturalGrownView.setNestedScrollingEnabled(false);

        // Gift Of Choice Section
        recyclerGiftView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        recyclerGiftView.setLayoutManager(layoutManager);
        recyclerGiftView.setItemAnimator(new DefaultItemAnimator());
        recyclerGiftView.setNestedScrollingEnabled(false);

        // New Arrivals Section
        final GridLayoutManager layoutManagerNewArrivals = new GridLayoutManager(context, 2);
       // layoutManagerNewArrivals.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerNewArrivalsView.setLayoutManager(layoutManagerNewArrivals);
        recyclerNewArrivalsView.setItemAnimator(new DefaultItemAnimator());
        recyclerNewArrivalsView.setNestedScrollingEnabled(false);

        // Media SpotLight Layout
        LinearLayoutManager layoutManagerMediaSpotLight = new LinearLayoutManager(activity);
        recyclerMediaSpotlight.setLayoutManager(layoutManagerMediaSpotLight);
        recyclerMediaSpotlight.setNestedScrollingEnabled(false);

        // Customer Stories Layout
        final LinearLayoutManager layoutManagerCustomerStories = new LinearLayoutManager(activity);
        layoutManagerCustomerStories.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerCustomerStories.setLayoutManager(layoutManagerCustomerStories);
        recyclerCustomerStories.setNestedScrollingEnabled(false);


        newWith = (int) (width/3.9);

        TopImageOptionModel optionModel = new TopImageOptionModel();
        optionModel.setDrawable(R.mipmap.loose_diamond);
        optionModel.setName(getResources().getString(R.string.solitaires));
        optionModel.setFirstPosition(true);
        optionModel.setLastPosition(false);
        topOptionArrayList.add(optionModel);

        TopImageOptionModel optionModel1 = new TopImageOptionModel();
        optionModel1.setDrawable(R.mipmap.ring);
        optionModel1.setName(getResources().getString(R.string.rings));
        optionModel1.setFirstPosition(false);
        optionModel1.setLastPosition(false);
        topOptionArrayList.add(optionModel1);

        TopImageOptionModel optionModel2 = new TopImageOptionModel();
        optionModel2.setDrawable(R.mipmap.earrings);
        optionModel2.setName(getResources().getString(R.string.earrings));
        optionModel2.setFirstPosition(false);
        optionModel2.setLastPosition(false);
        topOptionArrayList.add(optionModel2);

        TopImageOptionModel optionModel3 = new TopImageOptionModel();
        optionModel3.setDrawable(R.mipmap.pendant);
        optionModel3.setName(getResources().getString(R.string.pendant));
        optionModel3.setFirstPosition(false);
        optionModel3.setLastPosition(false);
        topOptionArrayList.add(optionModel3);

        TopImageOptionModel optionModel4 = new TopImageOptionModel();
        optionModel4.setDrawable(R.mipmap.bracelet);
        optionModel4.setName(getResources().getString(R.string.bracelet));
        optionModel4.setFirstPosition(false);
        optionModel4.setLastPosition(false);
        topOptionArrayList.add(optionModel4);

        TopImageOptionModel optionModel5 = new TopImageOptionModel();
        optionModel5.setDrawable(R.mipmap.bangles);
        optionModel5.setName(getResources().getString(R.string.bangles));
        optionModel5.setFirstPosition(false);
        optionModel5.setLastPosition(true);
        topOptionArrayList.add(optionModel5);

        topImageOptionListAdapter = new TopImageOptionListAdapter(topOptionArrayList,context,this, newWith);
        recyclerTopOptionView.setAdapter(topImageOptionListAdapter);

        PagerImageModel model = new PagerImageModel();
        model.setDrawable(R.drawable.banner1);
        model.setLocal(true);
        bannerImageArrayList.add(model);

        PagerImageModel model1 = new PagerImageModel();
        model1.setDrawable(R.drawable.banner2);
        model1.setLocal(true);
        bannerImageArrayList.add(model1);

        PagerImageModel model2 = new PagerImageModel();
        model2.setDrawable(R.drawable.banner3);
        model2.setLocal(true);
        bannerImageArrayList.add(model2);

        PagerImageModel model3 = new PagerImageModel();
        model3.setDrawable(R.drawable.banner4);
        model3.setLocal(true);
        bannerImageArrayList.add(model3);

        PagerImageModel model4 = new PagerImageModel();
        model4.setDrawable(R.drawable.banner5);
        model4.setLocal(true);
        bannerImageArrayList.add(model4);

        PagerImageModel model5 = new PagerImageModel();
        model5.setDrawable(R.drawable.banner6);
        model5.setLocal(true);
        bannerImageArrayList.add(model5);

        GiftImageModel giftImageModel = new GiftImageModel();
        giftImageModel.setDrawable(R.drawable.gift);
        giftImageModel.setLocal(true);
        giftImageArrayList.add(giftImageModel);

        GiftImageModel giftImageModel1 = new GiftImageModel();
        giftImageModel1.setDrawable(R.drawable.gift_bangles);
        giftImageModel1.setLocal(true);
        giftImageArrayList.add(giftImageModel1);

        GiftImageModel giftImageModel2 = new GiftImageModel();
        giftImageModel2.setDrawable(R.drawable.gift_ring);
        giftImageModel2.setLocal(true);
        giftImageArrayList.add(giftImageModel2);

        GiftImageModel giftImageModel3 = new GiftImageModel();
        giftImageModel3.setDrawable(R.drawable.gift_earrings);
        giftImageModel3.setLocal(true);
        giftImageArrayList.add(giftImageModel3);

        giftImageListAdapter = new GiftImageListAdapter(giftImageArrayList,context,this);
        recyclerGiftView.setAdapter(giftImageListAdapter);

        NewArrivalsImageModel arrivalsImageModel = new NewArrivalsImageModel();
        arrivalsImageModel.setLink("natural-diamonds");
        arrivalsImageModel.setDrawable(R.drawable.arrivals);
        arrivalsImageModel.setLocal(true);
        arrivalsImageArrayList.add(arrivalsImageModel);

        NewArrivalsImageModel arrivalsImageModel1 = new NewArrivalsImageModel();
        arrivalsImageModel1.setLink("lab-grown-diamonds");
        arrivalsImageModel1.setDrawable(R.drawable.arrivals1);
        arrivalsImageModel1.setLocal(true);
        arrivalsImageArrayList.add(arrivalsImageModel1);

        arrivalsImageListAdapter = new NewArrivalsImageListAdapter(arrivalsImageArrayList,context,this);
        recyclerNewArrivalsView.setAdapter(arrivalsImageListAdapter);

        //--------------------------------------------------------------------------------------------------------------------
        MediaSpotLightModel mediaSpotLightListModel1= new MediaSpotLightModel();
        mediaSpotLightListModel1.setDrawable(R.drawable.media_spot1);
        mediaSpotLightListModel1.setDescription("DiamondXE offers solutions on transparent pricing for the industry.");
        mediaSpotLightListModel1.setLink(ECONOMIC_TIMES);
        mediaSpotLightListModel1.setLocal(true);
        mediaSpotLightModelArrayList.add(mediaSpotLightListModel1);

        MediaSpotLightModel mediaSpotLightListModel2= new MediaSpotLightModel();
        mediaSpotLightListModel2.setDrawable(R.drawable.media_spot2);
        mediaSpotLightListModel2.setDescription("DiamondXE offers solutions on transparent pricing for the industry.");
        mediaSpotLightListModel2.setLink(RETAIL_JEWELLER);
        mediaSpotLightListModel2.setLocal(true);
        mediaSpotLightModelArrayList.add(mediaSpotLightListModel2);

        MediaSpotLightModel mediaSpotLightListModel3= new MediaSpotLightModel();
        mediaSpotLightListModel3.setDrawable(R.drawable.media_spot3);
        mediaSpotLightListModel3.setDescription("DiamondXE offers solutions on transparent pricing for the industry.");
        mediaSpotLightListModel3.setLink(MIDDAY);
        mediaSpotLightListModel3.setLocal(true);
        mediaSpotLightModelArrayList.add(mediaSpotLightListModel3);

        mediaSpotLightListAdapter = new MediaSpotLightListAdapter(mediaSpotLightModelArrayList,context,this);
        recyclerMediaSpotlight.setAdapter(mediaSpotLightListAdapter);

        CustomerStoriesModel customerStoriesModel1= new CustomerStoriesModel();
        customerStoriesModel1.setDrawable(R.drawable.customer_story1);
        customerStoriesModel1.setLocal(true);
        customerStoriesArrayList.add(customerStoriesModel1);

        CustomerStoriesModel customerStoriesModel2= new CustomerStoriesModel();
        customerStoriesModel2.setDrawable(R.drawable.customer_story2);
        customerStoriesModel2.setLocal(true);
        customerStoriesArrayList.add(customerStoriesModel2);

        CustomerStoriesModel customerStoriesModel3= new CustomerStoriesModel();
        customerStoriesModel3.setDrawable(R.drawable.customer_story3);
        customerStoriesModel3.setLocal(true);
        customerStoriesArrayList.add(customerStoriesModel3);

        Gson gson = new Gson();

        // Banner Data
        Type type = new TypeToken<ArrayList<PagerImageModel>>() {}.getType();
        ArrayList<PagerImageModel> retrievedBannerImageList = gson.fromJson(CommonUtility.getLocalDataList(context, "bannerImageList"), type);
        if(retrievedBannerImageList!=null && retrievedBannerImageList.size()>0)
        {
            bannerImageArrayList = retrievedBannerImageList;

            /*for (int i = 0; i < retrievedBannerImageList.size() ; i++)
            {
                Log.e("------Diamond----retrievedBannerImageList------ : ", retrievedBannerImageList.get(i).getBannerImage());
            }*/
        }

        // GIft Choice Data
        Type typeGift = new TypeToken<ArrayList<GiftImageModel>>() {}.getType();
        ArrayList<GiftImageModel> retrievedGiftImageList = gson.fromJson(CommonUtility.getLocalDataList(context, "giftImageArrayList"), typeGift);

        if(retrievedGiftImageList!=null && retrievedGiftImageList.size()>0)
        {
            giftImageListAdapter = new GiftImageListAdapter(retrievedGiftImageList,context,this);
            recyclerGiftView.setAdapter(giftImageListAdapter);
        }

        // New Arrivals Data
        Type typeNew = new TypeToken<ArrayList<NewArrivalsImageModel>>() {}.getType();
        ArrayList<NewArrivalsImageModel> retrievedNewImageList = gson.fromJson(CommonUtility.getLocalDataList(context, "newArrivalsImageArrayList"), typeNew);

        if(retrievedNewImageList!=null && retrievedNewImageList.size()>0)
        {
            arrivalsImageListAdapter = new NewArrivalsImageListAdapter(retrievedNewImageList,context,this);
            recyclerNewArrivalsView.setAdapter(arrivalsImageListAdapter);
        }

        String registerImage = CommonUtility.getGlobalString(context, "registerNowImage");
        setRegisterNowImage(registerImage);

        String dxeLuxeImage = CommonUtility.getGlobalString(context, "dxeLuxeImage");
        setDxeLuxeImage(dxeLuxeImage);

        // Media Spotlight Data
        Type typeMedia= new TypeToken<ArrayList<MediaSpotLightModel>>() {}.getType();
        ArrayList<MediaSpotLightModel> retrievedMediaImageList = gson.fromJson(CommonUtility.getLocalDataList(context, "mediaSpotlightArrayList"), typeMedia);

        if(retrievedMediaImageList!=null && retrievedMediaImageList.size()>0)
        {
            mediaSpotLightListAdapter = new MediaSpotLightListAdapter(retrievedMediaImageList,context,this);
            recyclerMediaSpotlight.setAdapter(mediaSpotLightListAdapter);
        }

        Type typeCustomer= new TypeToken<ArrayList<CustomerStoriesModel>>() {}.getType();
        ArrayList<CustomerStoriesModel> retrievedCustomerImageList = gson.fromJson(CommonUtility.getLocalDataList(context, "mediaCustomerArrayList"), typeCustomer);

        if(retrievedCustomerImageList!=null && retrievedCustomerImageList.size()>0)
        {
            customerStoriesArrayList = retrievedCustomerImageList;

            /*for (int i = 0; i < retrievedCustomerImageList.size() ; i++) {
                Log.e("------Diamond----retrievedCustomerImageList------ : ", retrievedCustomerImageList.get(i).getImage());
            }*/
        }

        if(retrievedBannerImageList!=null && retrievedBannerImageList.size()>0 && retrievedGiftImageList!=null && retrievedGiftImageList.size()>0 &&
                retrievedNewImageList!=null && retrievedNewImageList.size()>0 && retrievedMediaImageList!=null && retrievedMediaImageList.size()>0 &&
                retrievedCustomerImageList!=null && retrievedCustomerImageList.size()>0)
        {}
        else
        {
            page_no = 1;
            onHomeDetailsAPI(false);
        }

        if(middleBannersTitle.equalsIgnoreCase(""))
        {
            giftChoiceTextView.setText("Features");
        }
        else{
            giftChoiceTextView.setText(middleBannersTitle);
        }

        if(featuresTitle.equalsIgnoreCase(""))
        {
            newArrivalsTextView.setText("Categories");
        }
        else{
            newArrivalsTextView.setText(featuresTitle);
        }

        if(mediaTitle.equalsIgnoreCase(""))
        {
            mediaSpotLightTextView.setText("Media Spotlight");
        }
        else{
            mediaSpotLightTextView.setText(mediaTitle);
        }


        setpager();
        setPagerCustomerStories();

        onBindData(true);
        getCmsDetails(true);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                page_no = 1;
                onHomeDetailsAPI(true);
                onBindData(true);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeColors(
                ContextCompat.getColor(context, R.color.purple),
                ContextCompat.getColor(context, R.color.purple),
                ContextCompat.getColor(context, R.color.purple),
                ContextCompat.getColor(context, R.color.purple)
        );

        showCardCount();
    }


    @Override
    public void onClick(View view)
    {
        Intent intent;
        int id = view.getId();

        /*if(id == R.id.solitaires_card_view)
        {
            Constant.searchTitleName = "Solitaires";
            Constant.searchType=ApiConstants.NATURAL;
            Constant.filterClear="";
            intent = new Intent(context, SearchDiamondsActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0,0);
        }
        else if(id == R.id.rings_card_view)
        {
        }
        else if(id == R.id.earrings_card_view)
        {
        }
        else if(id == R.id.pendent_card_view)
        {
        }
        else if(id == R.id.bracelet_card_view)
        {
        }
        else if(id == R.id.bangles_card_view)
        {
        }*/
        if(id == R.id.solitaries_lin)
        {
            Constant.searchTitleName = "Solitaires";
            Constant.searchType=ApiConstants.NATURAL;
            Constant.filterClear="";
            intent = new Intent(context, SearchDiamondsActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0,0);
        }
        else if(id == R.id.jewellery_lin)
        {
            intent = new Intent(context, JewelleryScreenActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0,0);
        }
        else if (id == R.id.natural_diamond_tv)
        {
            Utils.hideKeyboard(activity);
            natural_diamond_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
            natural_diamond_tv.setBackgroundResource(R.drawable.background_purple);
            grown_diamond_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
            grown_diamond_tv.setBackgroundResource(R.drawable.border_purple);

            selectedTopDeals = "naturalDiamond";
            setNaturalDiamondAdapter();
        }
        else if (id == R.id.grown_diamond_tv)
        {
            Utils.hideKeyboard(activity);
            natural_diamond_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
            natural_diamond_tv.setBackgroundResource(R.drawable.border_purple);
            grown_diamond_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
            grown_diamond_tv.setBackgroundResource(R.drawable.background_purple);
            selectedTopDeals = "labGrownDiamond";
            setLabGrownDiamondAdapter();
        }
        else if(id == R.id.register_img)
        {
        }
        else if(id == R.id.dxe_luxe_img)
        {
        }
        else if(id == R.id.transparent_price_card_view)
        {
        }
        else if(id == R.id.exchange_buy_back_card_view)
        {
        }
        else if(id == R.id.certificate_lab_card_view)
        {
        }
        else if(id == R.id.insured_door_step_card_view)
        {
        }
        else if(id == R.id.view_all_news_tv)
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(VIEW_ALL_NEWS));
            startActivity(intent);
        }
        else if(id == R.id.customer_support_card_view)
        {
        }
        else if(id == R.id.home_rel)
        {
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
            Fragment fragment = new AddToCartListFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
        else if(id == R.id.account_rel)
        {
            String user_login = CommonUtility.getGlobalString(activity, "user_login");
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
        else if(id == R.id.search_circle_rel)
        {
            Constant.searchTitleName = "Solitaires";
            Constant.searchType=ApiConstants.NATURAL;
            Constant.filterClear="";
            intent = new Intent(context, SearchDiamondsActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0,0);
        }
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

    // Click On Natural Diamond Tab and Call Adapter for Set Data
    void setNaturalDiamondAdapter()
    {
        naturalDiamondListAdapter = new NaturalDiamondListAdapter(naturalDiamondArrayList,context,this);
        recyclerNaturalGrownView.setAdapter(naturalDiamondListAdapter);
        naturalDiamondListAdapter.notifyDataSetChanged();
    }

    // Click On Lab Grown Diamond Tab and Call Adapter for Set Data
    void setLabGrownDiamondAdapter()
    {
        labGrownDiamondListAdapter = new LabGrownDiamondListAdapter(labGrownDiamondArrayList,context,this);
        recyclerNaturalGrownView.setAdapter(labGrownDiamondListAdapter);
        labGrownDiamondListAdapter.notifyDataSetChanged();
    }

    // Home Details API Call
    public void onHomeDetailsAPI(boolean showLoader)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);

            //urlParameter.put("user_id", CommonUtility.getGlobalString(getActivity(),"user_id"));
            //urlParameter.put("authToken", CommonUtility.getGlobalString(getActivity(),"mobile_auth_token"));

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(getActivity(),this, urlParameter, ApiConstants.GET_HOME_PAGE_DETAILS, ApiConstants.GET_HOME_PAGE_DETAILS_ID,showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    //Top Deals Section API Call
    public void onBindData(boolean showLoader)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            //urlParameter.put("user_id", CommonUtility.getGlobalString(getActivity(),"user_id"));
            //urlParameter.put("authToken", CommonUtility.getGlobalString(getActivity(),"mobile_auth_token"));
            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(getActivity(),this, urlParameter, ApiConstants.TOP_DEALS, ApiConstants.TOP_DEALS_ID,showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    public void getCurrencyListAPI(boolean showLoader)
    {
        String uuid = CommonUtility.getAndroidId(context);

        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_CURRENCY_RATES, ApiConstants.GET_CURRENCY_RATES_ID,showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    public void getCmsDetails(boolean showLoader)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);

            //urlParameter.put("user_id", CommonUtility.getGlobalString(getActivity(),"user_id"));
            //urlParameter.put("authToken", CommonUtility.getGlobalString(getActivity(),"mobile_auth_token"));

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(getActivity(),this, urlParameter, ApiConstants.GET_CMS_DETAILS, ApiConstants.GET_CMS_DETAILS_ID,showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    @Override
    public void itemClick(int position, String action)
    {
        if(action.equalsIgnoreCase("giftDetails"))
        {
           // Toast.makeText(context, "Gift position: " + position, Toast.LENGTH_SHORT).show();
        }
        else if(action.equalsIgnoreCase("newArrivalsDetails"))
        {
            NewArrivalsImageModel model = arrivalsImageArrayList.get(position);
            if(model.getLink().equalsIgnoreCase("natural-diamonds"))
            {
                Constant.searchTitleName = "Natural Diamond";
                Constant.searchType=ApiConstants.NATURAL;
                Constant.filterClear="";
                Intent intent = new Intent(context, SearchDiamondsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }
            else{
                Constant.searchTitleName = "Lab Grown Diamond";
                Constant.searchType=ApiConstants.LAB_GROWN;
                Constant.filterClear="";
                Intent intent = new Intent(context, SearchDiamondsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }
        }
        else if(action.equalsIgnoreCase("naturalDiamondDetails"))
        {
            TopDiamondImageModel model = naturalDiamondArrayList.get(position);
            Constant.manageClickEventForRedirection="";
            CommonUtility.setGlobalString(context, "certificate_number", model.getCertificateNo());
            Intent intent = new Intent(activity, DiamondDetailsActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0,0);
        }
        else if(action.equalsIgnoreCase("labGrownDiamondDetails"))
        {
            TopDiamondImageModel model = labGrownDiamondArrayList.get(position);
            Constant.manageClickEventForRedirection="";
            CommonUtility.setGlobalString(context, "certificate_number", model.getCertificateNo());
            Intent intent = new Intent(activity, DiamondDetailsActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0,0);
        }
        else if(action.equalsIgnoreCase("mediaSpotLightDetails"))
        {
            if(!mediaSpotLightModelArrayList.get(position).getLink().equalsIgnoreCase(""))
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mediaSpotLightModelArrayList.get(position).getLink()));
                startActivity(intent);
            }else{}

        }
        else if(action.equalsIgnoreCase("CustomerStoriesDetails"))
        {
        }
        else if(action.equalsIgnoreCase("cardDetails"))
        {
            Constant.searchTitleName = "Solitaires";
            Constant.searchType=ApiConstants.NATURAL;
            Constant.filterClear="";
            Intent intent = new Intent(context, SearchDiamondsActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0,0);
        }
    }

    // Method to refresh the fragment
    public void refresh() {
        // Implement your refresh logic here
        // For example, re-fetch data or update UI components
        //Toast.makeText(activity, "Call this Method", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

        try {
            Log.e("Top_Diamonds : ", "--------Top_JSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {

                case ApiConstants.TOP_DEALS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {

                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        Log.e("----Diamond----- : ", "TopDiamond : " + jObjDetails.toString());

                        // Natural Array List Data Fetch
                        JSONArray naturalDetails = jObjDetails.getJSONArray("natural");

                        if(naturalDiamondArrayList.size() > 0)
                        {
                            naturalDiamondArrayList.clear();
                        }else{}

                        for (int i = 0; i < naturalDetails.length(); i++)
                        {
                            JSONObject objectCode = naturalDetails.getJSONObject(i);

                            TopDiamondImageModel topDiamondImageModel = new TopDiamondImageModel();

                            topDiamondImageModel.setId(CommonUtility.checkString(objectCode.optString("stock_id")));
                            topDiamondImageModel.setName(CommonUtility.checkString(objectCode.optString("item_name")));
                            topDiamondImageModel.setPrice(CommonUtility.checkString(objectCode.optString("subtotal")));
                            topDiamondImageModel.setShowingSubTotal(CommonUtility.checkString(objectCode.optString("subtotal")));
                            topDiamondImageModel.setImage(CommonUtility.checkString(objectCode.optString("diamond_image")));
                            topDiamondImageModel.setCategory(CommonUtility.checkString(objectCode.optString("category")));
                            topDiamondImageModel.setCarat(CommonUtility.checkString(objectCode.optString("carat")));
                            topDiamondImageModel.setCertificateNo(CommonUtility.checkString(objectCode.optString("certificate_no")));
                            topDiamondImageModel.setCurrencySymbol(ApiConstants.rupeesIcon);
                            naturalDiamondArrayList.add(topDiamondImageModel);
                        }

                        getCurrencyData();

                        for (int i = 0; i <naturalDiamondArrayList.size() ; i++)
                        {

                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, naturalDiamondArrayList.get(i).getPrice());
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            naturalDiamondArrayList.get(i).setShowingSubTotal(subTotalFormat);
                            naturalDiamondArrayList.get(i).setCurrencySymbol(getCurrencySymbol);
                        }

                        // lab_grown Array List Data Fetch
                        JSONArray labGrownDetails = jObjDetails.getJSONArray("lab_grown");

                        if(labGrownDiamondArrayList.size() > 0)
                        {
                            labGrownDiamondArrayList.clear();
                        }else{}

                        for (int i = 0; i < labGrownDetails.length(); i++)
                        {
                            JSONObject objectCode = labGrownDetails.getJSONObject(i);

                            TopDiamondImageModel topDiamondImageModel = new TopDiamondImageModel();

                            topDiamondImageModel.setId(CommonUtility.checkString(objectCode.optString("stock_id")));
                            topDiamondImageModel.setName(CommonUtility.checkString(objectCode.optString("item_name")));
                            topDiamondImageModel.setPrice(CommonUtility.checkString(objectCode.optString("subtotal")));
                            topDiamondImageModel.setShowingSubTotal(CommonUtility.checkString(objectCode.optString("subtotal")));
                            topDiamondImageModel.setImage(CommonUtility.checkString(objectCode.optString("diamond_image")));
                            topDiamondImageModel.setCategory(CommonUtility.checkString(objectCode.optString("category")));
                            topDiamondImageModel.setCarat(CommonUtility.checkString(objectCode.optString("carat")));
                            topDiamondImageModel.setCertificateNo(CommonUtility.checkString(objectCode.optString("certificate_no")));
                            topDiamondImageModel.setCurrencySymbol(ApiConstants.rupeesIcon);
                            labGrownDiamondArrayList.add(topDiamondImageModel);
                        }

                        for (int i = 0; i <labGrownDiamondArrayList.size() ; i++)
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, labGrownDiamondArrayList.get(i).getPrice());
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            labGrownDiamondArrayList.get(i).setShowingSubTotal(subTotalFormat);
                            labGrownDiamondArrayList.get(i).setCurrencySymbol(getCurrencySymbol);
                        }

                        if(selectedTopDeals.equalsIgnoreCase("naturalDiamond"))
                        {
                            naturalDiamondListAdapter = new NaturalDiamondListAdapter(naturalDiamondArrayList,context,this);
                            recyclerNaturalGrownView.setAdapter(naturalDiamondListAdapter);
                        }
                        else if(selectedTopDeals.equalsIgnoreCase("naturalDiamond")){
                            labGrownDiamondListAdapter = new LabGrownDiamondListAdapter(labGrownDiamondArrayList,context,this);
                            recyclerNaturalGrownView.setAdapter(labGrownDiamondListAdapter);
                        }
                        else{
                            naturalDiamondListAdapter = new NaturalDiamondListAdapter(naturalDiamondArrayList,context,this);
                            recyclerNaturalGrownView.setAdapter(naturalDiamondListAdapter);
                        }


                       // labGrownDiamondListAdapter = new LabGrownDiamondListAdapter(labGrownDiamondArrayList,context,this);
                       // recyclerNaturalGrownView.setAdapter(labGrownDiamondListAdapter);
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

                case ApiConstants.GET_HOME_PAGE_DETAILS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        //----------------------------Start Banner Section Date Fetch-------------------------------------------------
                        JSONObject banners = jObjDetails.optJSONObject("banners");

                       // String bannerTitle = CommonUtility.checkString(banners.optString("title"));

                        JSONArray bannerContent = banners.getJSONArray("content");

                        if(bannerImageArrayList.size() > 0)
                        {
                            bannerImageArrayList.clear();
                        }else{}

                        for (int i = 0; i < bannerContent.length(); i++)
                        {
                            JSONObject objectCode = bannerContent.getJSONObject(i);

                            PagerImageModel bannerImageModel = new PagerImageModel();

                            bannerImageModel.setBannerImage(CommonUtility.checkString(objectCode.optString("image")));
                            bannerImageModel.setLink(CommonUtility.checkString(objectCode.optString("link")));
                            bannerImageModel.setLocal(false);

                            bannerImageArrayList.add(bannerImageModel);
                        }

                        Gson gson = new Gson();
                        String json = gson.toJson(bannerImageArrayList);
                        CommonUtility.saveLocalDataList(context, "bannerImageList", json);

                        setpager();

                        //----------------------------End Banner Section Date Fetch-------------------------------------------------

                        //------------------------------Start Middle Banner Gift choice--------------------------------------------

                        JSONObject middleBanners = jObjDetails.optJSONObject("middle-banners");
                        middleBannersTitle = CommonUtility.checkString(middleBanners.optString("title"));

                        CommonUtility.setGlobalString(activity, "middleBannersTitle", middleBannersTitle);
                        giftChoiceTextView.setText(middleBannersTitle); // Value set in TextView.

                        JSONArray middleBannersContent = middleBanners.getJSONArray("content");

                        if(giftImageArrayList.size() > 0)
                        {
                            giftImageArrayList.clear();
                        }else{}

                        for (int i = 0; i < middleBannersContent.length(); i++)
                        {
                            JSONObject objectCode = middleBannersContent.getJSONObject(i);

                            GiftImageModel giftImageModel = new GiftImageModel();

                            giftImageModel.setImage(CommonUtility.checkString(objectCode.optString("image")));
                            giftImageModel.setLink(CommonUtility.checkString(objectCode.optString("link")));
                            giftImageModel.setDrawable(getLocalGiftImage(i));
                            giftImageModel.setLocal(false);

                            giftImageArrayList.add(giftImageModel);
                        }

                        Gson gsonGift = new Gson();
                        String jsonGift = gsonGift.toJson(giftImageArrayList);
                        CommonUtility.saveLocalDataList(context, "giftImageArrayList", jsonGift);

                        giftImageListAdapter = new GiftImageListAdapter(giftImageArrayList,context,this);
                        recyclerGiftView.setAdapter(giftImageListAdapter);

                        //------------------------------End Middle Banner Gift choice--------------------------------------------

                        //------------------------------Start New Arrivals--------------------------------------------

                        JSONObject features = jObjDetails.optJSONObject("features");
                        featuresTitle = CommonUtility.checkString(features.optString("title"));

                        CommonUtility.setGlobalString(activity, "featuresTitle", featuresTitle);
                        newArrivalsTextView.setText(featuresTitle); // Value set in TextView.

                        JSONArray featuresContent = features.getJSONArray("content");

                        if(arrivalsImageArrayList.size() > 0)
                        {
                            arrivalsImageArrayList.clear();
                        }else{}

                        for (int i = 0; i < featuresContent.length(); i++)
                        {
                            JSONObject objectCode = featuresContent.getJSONObject(i);

                            NewArrivalsImageModel arrivalsImageModel = new NewArrivalsImageModel();

                            arrivalsImageModel.setImage(CommonUtility.checkString(objectCode.optString("image")));
                            arrivalsImageModel.setLink(CommonUtility.checkString(objectCode.optString("link")));
                            arrivalsImageModel.setDrawable(getLocalNewArrivalsImage(i));
                            arrivalsImageModel.setLocal(false);

                            arrivalsImageArrayList.add(arrivalsImageModel);
                        }

                        Gson gsonNew = new Gson();
                        String jsonNew = gsonNew.toJson(arrivalsImageArrayList);
                        CommonUtility.saveLocalDataList(context, "newArrivalsImageArrayList", jsonNew);

                        arrivalsImageListAdapter = new NewArrivalsImageListAdapter(arrivalsImageArrayList,context,this);
                        recyclerNewArrivalsView.setAdapter(arrivalsImageListAdapter);

                        //------------------------------End New Arrivals--------------------------------------------

                        //------------------------------Start Register Now Image Fetch------------------------------------------------

                        JSONObject registerNow = jObjDetails.optJSONObject("register-now");
                        registerNowTitle = CommonUtility.checkString(registerNow.optString("title"));

                        JSONObject registerNowContent = registerNow.optJSONObject("content");

                        registerNowImage = CommonUtility.checkString(registerNowContent.optString("image"));
                        registerNowLink = CommonUtility.checkString(registerNowContent.optString("link"));
                        registerNowButton = CommonUtility.checkString(registerNowContent.optString("button_text"));

                        CommonUtility.setGlobalString(context, "registerNowImage", registerNowImage);
                        // Register Image
                        setRegisterNowImage(registerNowImage);

                        //------------------------------End Register Now Image Fetch------------------------------------------------

                        //------------------------------Start DXE-LUXE Image Fetch------------------------------------------------

                        JSONObject dxeLuxeNow = jObjDetails.optJSONObject("dxe-luxe");
                        dxeLuxeTitle = CommonUtility.checkString(dxeLuxeNow.optString("title"));

                        JSONObject dxeLuxeContent = dxeLuxeNow.optJSONObject("content");

                        dxeLuxeImage = CommonUtility.checkString(dxeLuxeContent.optString("image"));
                        dxeLuxeLink = CommonUtility.checkString(dxeLuxeContent.optString("link"));

                        CommonUtility.setGlobalString(context, "dxeLuxeImage", dxeLuxeImage);

                        // DxeLuxe Image
                        setDxeLuxeImage(dxeLuxeImage);

                        //------------------------------End DXE-LUXE  Image Fetch------------------------------------------------

                        //------------------------------Start Media Spotlight Date Fetch--------------------------------------------

                        JSONObject media = jObjDetails.optJSONObject("media");
                        mediaTitle = CommonUtility.checkString(media.optString("title"));

                        CommonUtility.setGlobalString(activity, "mediaTitle", mediaTitle);
                        mediaSpotLightTextView.setText(mediaTitle); // Value set in TextView.

                        JSONArray mediaContent = media.getJSONArray("content");

                        if(mediaSpotLightModelArrayList.size() > 0)
                        {
                            mediaSpotLightModelArrayList.clear();
                        }else{}

                        for (int i = 0; i < mediaContent.length(); i++)
                        {
                            JSONObject objectCode = mediaContent.getJSONObject(i);

                            MediaSpotLightModel mediaSpotLightListModel= new MediaSpotLightModel();

                            mediaSpotLightListModel.setImage(CommonUtility.checkString(objectCode.optString("image")));
                            mediaSpotLightListModel.setLink(CommonUtility.checkString(objectCode.optString("link")));
                            mediaSpotLightListModel.setDescription(CommonUtility.checkString(objectCode.optString("description")));
                            mediaSpotLightListModel.setDrawable(getLocalMediaSpotLightImage(i));
                            mediaSpotLightListModel.setLocal(false);

                            mediaSpotLightModelArrayList.add(mediaSpotLightListModel);
                        }

                        Gson gsonMedia = new Gson();
                        String jsonMedia = gsonMedia.toJson(mediaSpotLightModelArrayList);
                        CommonUtility.saveLocalDataList(context, "mediaSpotlightArrayList", jsonMedia);

                        mediaSpotLightListAdapter = new MediaSpotLightListAdapter(mediaSpotLightModelArrayList,context,this);
                        recyclerMediaSpotlight.setAdapter(mediaSpotLightListAdapter);

                        //------------------------------End Media Spotlight Date Fetch-------------------------------------------

                        //------------------------------Start Customer Stories Date Fetch--------------------------------------------

                        JSONObject customerStories = jObjDetails.optJSONObject("customer_stories");
                        customerStoriesTitle = CommonUtility.checkString(customerStories.optString("title"));

                        customerStoriesTextView.setText(customerStoriesTitle); // Value set in TextView.

                        JSONArray customerStoriesContent = customerStories.getJSONArray("content");

                        if(customerStoriesArrayList.size() > 0)
                        {
                            customerStoriesArrayList.clear();
                        }else{}

                        for (int i = 0; i < customerStoriesContent.length(); i++)
                        {
                            JSONObject objectCode = customerStoriesContent.getJSONObject(i);

                            CustomerStoriesModel customerStoriesModel= new CustomerStoriesModel();

                            customerStoriesModel.setImage(CommonUtility.checkString(objectCode.optString("image")));
                            customerStoriesModel.setDrawable(getLocalCustomerStoriesImage(i));
                            customerStoriesModel.setLocal(false);

                            customerStoriesArrayList.add(customerStoriesModel);
                        }

                        Gson gsonCustomer = new Gson();
                        String jsonCustomer = gsonCustomer.toJson(customerStoriesArrayList);
                        CommonUtility.saveLocalDataList(context, "mediaCustomerArrayList", jsonCustomer);

                        setPagerCustomerStories();

                        swipeContainer.setRefreshing(false);

                        //customerStoriesListAdapter = new CustomerStoriesListAdapter(customerStoriesArrayList,context,this);
                        //recyclerCustomerStories.setAdapter(customerStoriesListAdapter);

                        //------------------------------End Customer Stories Date Fetch-------------------------------------------
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

                        if(Constant.currencyArrayList.size() > 0)
                        {
                            Constant.currencyArrayList.clear();
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
                            Constant.currencyArrayList.add(model);

                        }

                        getCurrencyData();

                        if(selectedCurrencyCode.equalsIgnoreCase(""))
                        {
                            CommonUtility.setGlobalString(context, "selected_currency_code", "INR");
                        }else{}

                        if(selectedCurrencyValue.equalsIgnoreCase(""))
                        {
                            CommonUtility.setGlobalString(context, "selected_currency_value", "1");
                        }else{}

                        if(selectedCurrencyDesc.equalsIgnoreCase(""))
                        {
                            CommonUtility.setGlobalString(context, "selected_currency_desc", "Indian Rupees");
                        }else{}

                        if(selectedCurrencyImage.equalsIgnoreCase(""))
                        {
                            CommonUtility.setGlobalString(context, "selected_currency_image", "https://buyer-uat.diamondxe.com/assets/img/flags/inr.png");
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

                case ApiConstants.GET_CMS_DETAILS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        //Log.e("Diamond : ", "jsonObjectData : " + jsonObjectData.toString());
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                         cmsEmail = CommonUtility.checkString(jObjDetails.optString("email"));
                         cmsPhone1 = CommonUtility.checkString(jObjDetails.optString("phone1"));
                         cmsPhone2 = CommonUtility.checkString(jObjDetails.optString("phone2"));
                         cmsAddress = CommonUtility.checkString(jObjDetails.optString("address"));
                         cmsLinkedinLink = CommonUtility.checkString(jObjDetails.optString("linkedin_link"));
                         cmsTwitterLink = CommonUtility.checkString(jObjDetails.optString("twitter_link"));
                         cmsFacebookLink = CommonUtility.checkString(jObjDetails.optString("facebook_link"));
                         cmsInstagramLink = CommonUtility.checkString(jObjDetails.optString("instagram_link"));
                         cmsYoutubeLink = CommonUtility.checkString(jObjDetails.optString("youtube_link"));

                         Constant.cardCount = CommonUtility.checkString(jObjDetails.optString("cart_count"));
                         Constant.wishListCount = CommonUtility.checkString(jObjDetails.optString("wishlist_count"));

                         CommonUtility.setGlobalString(activity, "cmsEmail", cmsEmail);
                         CommonUtility.setGlobalString(activity, "cmsPhone1", cmsPhone1);
                         CommonUtility.setGlobalString(activity, "cmsphone2", cmsPhone2);
                         CommonUtility.setGlobalString(activity, "cmsaddress", cmsAddress);
                         CommonUtility.setGlobalString(activity, "cmsLinkedinLink", cmsLinkedinLink);
                         CommonUtility.setGlobalString(activity, "cmsTwitterLink", cmsTwitterLink);
                         CommonUtility.setGlobalString(activity, "cmsFacebookLink", cmsFacebookLink);
                         CommonUtility.setGlobalString(activity, "cmsInstagramLink", cmsInstagramLink);
                         CommonUtility.setGlobalString(activity, "cmsYoutubeLink", cmsYoutubeLink);

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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    int getLocalGiftImage(int index)
    {
        if(index == 0)
        {
            return R.drawable.gift;
        }
        else if(index == 1)
        {
            return R.drawable.gift_bangles;
        }
        else if(index == 2)
        {
            return R.drawable.gift_earrings;
        }
        else if(index == 3)
        {
            return R.drawable.gift_ring;
        }
        else{
            return R.drawable.gift;
        }
    }

    int getLocalNewArrivalsImage(int index)
    {
        if(index == 0)
        {
            return R.drawable.arrivals;
        }
        else if(index == 1)
        {
            return R.drawable.arrivals1;
        }
        else{
            return R.drawable.arrivals;
        }
    }

    int getLocalMediaSpotLightImage(int index)
    {
        if(index == 0)
        {
            return R.drawable.media_spot1;
        }
        else if(index == 1)
        {
            return R.drawable.media_spot2;
        }
        else if(index == 2)
        {
            return R.drawable.media_spot3;
        }
        else{
            return R.drawable.media_spot1;
        }
    }

    int getLocalCustomerStoriesImage(int index)
    {
        if(index == 0)
        {
            return R.drawable.customer_story1;
        }
        else if(index == 1)
        {
            return R.drawable.customer_story2;
        }
        else if(index == 2)
        {
            return R.drawable.customer_story3;
        }
        else{
            return R.drawable.customer_story1;
        }
    }

    // Register Now Image Set
    void setRegisterNowImage(String image)
    {
        if(image!=null && !image.equalsIgnoreCase(""))
        {
            /*Picasso.with(context)
                    .load(image)
                    .placeholder(R.mipmap.register_now)
                    .error(R.mipmap.register_now)
                    .into(registerImg);*/

            Glide.with(context)
                    .load(image) // Image URL or resource ID
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.register_now) // Placeholder image while loading
                            .error(R.mipmap.register_now)) // Error image if loading fails
                    .into(registerImg);
        }
        else{
            Picasso.with(context)
                    .load(R.mipmap.register_now)
                    .placeholder(R.mipmap.register_now)
                    .error(R.mipmap.register_now)
                    .into(registerImg);
        }

    }

    // Dxe-Luxe Image Set
    void setDxeLuxeImage(String image)
    {
        Log.e("----LUXimage-------- : ", image.toString());
        if(image!=null && !image.equalsIgnoreCase(""))
        {
            /*Picasso.with(context)
                    .load(image)
                    .placeholder(R.mipmap.dxe_luxe)
                    .error(R.mipmap.dxe_luxe)
                    .into(dxeLuxeImg);*/

            Glide.with(context)
                    .load(image) // Image URL or resource ID
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.dxe_luxe) // Placeholder image while loading
                            .error(R.mipmap.dxe_luxe)) // Error image if loading fails
                    .into(dxeLuxeImg);
        }
        else{
            Picasso.with(context)
                    .load(R.mipmap.dxe_luxe)
                    .placeholder(R.mipmap.dxe_luxe)
                    .error(R.mipmap.dxe_luxe)
                    .into(dxeLuxeImg);
        }
    }

    @Override
    public void getErrorResponce(String error, int service_ID) {}

    @Override
    public void actionInterface(String value, String action) {

    }

    final Handler handler = new Handler();
    Timer swipeTimer = new Timer();
    Runnable Update;

    public void setpager() {
        viewPager.setAdapter(new MyPagerAdapter(activity, bannerImageArrayList));
        tabLayout.setupWithViewPager(viewPager, true);
        final float density = getResources().getDisplayMetrics().density;
        // Check Banner Image View Visible and Gone Condition Using Array List Size.
        if(bannerImageArrayList!=null && bannerImageArrayList.size()>=1)
        {
            viewpager_layout.setVisibility(View.VISIBLE);

            if(bannerImageArrayList.size()>1){
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

        NUM_PAGES = bannerImageArrayList.size();

        currentPage = 0;

        if (Update==null)
        {
            // Auto start of viewpager
            Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    viewPager.setCurrentItem(currentPage++, true);
                }
            };

            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 3000, 3000);
        } else {
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Optional implementation
            }

            @Override
            public void onPageSelected(int position) {
                // Highlight the selected tab
                tabLayout.setScrollPosition(position, 0f, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Optional implementation
            }
        });


       /* // Pager listener over indicator
        circle_indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int pos) {
            }
        });*/

    }
    private class MyPagerAdapter extends PagerAdapter {

        ArrayList<PagerImageModel> flag;
        LayoutInflater inflater;

        public MyPagerAdapter(Context context, ArrayList<PagerImageModel> flag) {
            this.flag = flag;
        }

        @Override
        public int getCount() {
            return flag.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            // Declare Variables
            ImageView pagerImg;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.viewpager_image_item, container, false);
            pagerImg = (ImageView) itemView.findViewById(R.id.busines_img);

            if(flag.get(position).isLocal())
            {
                Picasso.with(context)
                        .load(flag.get(position).getDrawable())
                        .placeholder(R.mipmap.view)
                        .error(R.mipmap.view)
                        //.resize(1100, 1100)
                        //.centerCrop()
                        .into(pagerImg);
            }
            else
            {
                Picasso.with(context)
                        .load(flag.get(position).getBannerImage())
                        .placeholder(R.mipmap.view)
                        .error(R.mipmap.view)
                        //.resize(1100, 1100)
                        //.centerCrop()
                        .into(pagerImg);
            }

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



    //--------------------------------------------------Customer Stories Pager----------------------------------------------------

    final Handler handlerCustomerStories = new Handler();
    Timer swipeTimerCustomerStories = new Timer();
    Runnable UpdateCustomerStories;

    public void setPagerCustomerStories() {
        viewPagerCustomer.setAdapter(new MyPagerAdapterCustomerStories(activity, customerStoriesArrayList));
        //tabLayout.setupWithViewPager(viewPagerCustomer, true);
        final float density = getResources().getDisplayMetrics().density;

        // Check Banner Image View Visible and Gone Condition Using Array List Size.
        if(customerStoriesArrayList!=null && customerStoriesArrayList.size()>=1)
        {
            viewpager_layout_customer.setVisibility(View.VISIBLE);

        }
        else
        {
            viewpager_layout_customer.setVisibility(View.GONE);
        }

        viewPagerCustomer.setPageMargin(24);
    }

    private class MyPagerAdapterCustomerStories extends PagerAdapter {

        ArrayList<CustomerStoriesModel> flag;
        LayoutInflater inflater;

        public MyPagerAdapterCustomerStories(Context context, ArrayList<CustomerStoriesModel> flag) {
            this.flag = flag;
        }

        @Override
        public int getCount() {
            return flag.size();
        }


        @Override
        public float getPageWidth(int position) {
            return 0.9f;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            // Declare Variables
            ImageView pagerImg;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.viewpager_image_customer_stories_item, container, false);
            pagerImg = (ImageView) itemView.findViewById(R.id.customer_stories_img);

            if(flag.get(position).isLocal())
            {
                Picasso.with(context)
                        .load(flag.get(position).getDrawable())
                        .placeholder(flag.get(position).getDrawable())
                        .error(flag.get(position).getDrawable())
                        //.resize(1100, 1100)
                        //.centerCrop()
                        .into(pagerImg);
            }
            else
            {
                Picasso.with(context)
                        .load(flag.get(position).getImage())
                        .placeholder(flag.get(position).getDrawable())
                        .error(flag.get(position).getDrawable())
                        //.resize(1100, 1100)
                        //.centerCrop()
                        .into(pagerImg);
            }

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


