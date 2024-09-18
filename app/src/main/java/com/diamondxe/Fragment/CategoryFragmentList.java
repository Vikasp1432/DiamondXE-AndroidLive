package com.diamondxe.Fragment;

import static com.diamondxe.ApiCalling.ApiConstants.USER_BUYER;
import static com.diamondxe.ApiCalling.ApiConstants.USER_DEALER;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diamondxe.Activity.AccountSectionActivity;
import com.diamondxe.Activity.HomeScreenActivity;
import com.diamondxe.Activity.LoginScreenActivity;
import com.diamondxe.Activity.SearchDiamondsActivity;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.Interface.FragmentActionInterface;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;


public class CategoryFragmentList extends Fragment implements RecyclerInterface,View.OnClickListener, FragmentActionInterface {

    private RelativeLayout home_rel, category_rel, wishlist_rel, cart_rel, account_rel;
    private ImageView home_img, categories_img, wish_img, cart_img, account_img;
    private TextView home_tv, categories_tv, wish_tv, cart_tv, account_tv, cart_count_tv, wish_list_count_tv;

    String user_login = "";
    private RelativeLayout search_circle_rel;

    private ImageView bottom_search_icon;

    private HomeScreenActivity drawerActivity;
    private Activity activity;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);

        context = activity = drawerActivity = (HomeScreenActivity) getActivity();

        drawerActivity.setListener(this);

        init(view);

        return view;
    }

    private void init(View parentView)
    {
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

        search_circle_rel = parentView.findViewById(R.id.search_circle_rel);
        search_circle_rel.setOnClickListener(this);

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
        categories_img.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));
        wish_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        cart_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));
        account_img.setColorFilter(ContextCompat.getColor(context, R.color.grey_light));

        home_tv.setTextColor(ContextCompat.getColor(context, R.color.grey_light));
        categories_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
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

        bottom_search_icon = parentView.findViewById(R.id.bottom_search_icon);
        bottom_search_icon.setBackgroundResource(R.mipmap.bottom_search);
        // Search Zoom-In and Zoom-Out Animation
        CommonUtility.startZoomAnimation(bottom_search_icon);
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
        else if(id == R.id.category_rel)
        {
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

    @Override
    public void actionInterface(String value, String action) {

    }

    @Override
    public void itemClick(int position, String action) {

    }
}