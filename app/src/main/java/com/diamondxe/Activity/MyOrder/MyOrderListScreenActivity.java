package com.diamondxe.Activity.MyOrder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.diamondxe.Activity.HomeScreenActivity;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Fragment.MyOrder.CancelledListOrderFragment;
import com.diamondxe.Fragment.MyOrder.PastOrderListFragment;
import com.diamondxe.Fragment.MyOrder.RecentOrderListFragment;
import com.diamondxe.Fragment.MyOrder.ReservedOrderListFragment;
import com.diamondxe.Fragment.MyOrder.ReturnOrderListFragment;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONObject;

import java.util.HashMap;

public class MyOrderListScreenActivity extends SuperActivity implements TwoRecyclerInterface {

    private ImageView back_img;
    private Activity activity;
    private Context context;

    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;

    TabLayout tabLayout;
    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_list_screen);

        context = activity = this;

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.recent)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.return_lbl)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.reserved)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.past)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.cancelled)));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

       /* final MyAdapter adapter = new MyAdapter(this,  tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {
            }
        });*/

        String[] tabTitles = getResources().getStringArray(R.array.tab_titles);
        MyAdapter adapter = new MyAdapter(this, tabTitles.length);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();

        // Handle Device Back Button code.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed()
            {
                gotoHomeScreen();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onClick(View view)
    {
        Intent intent;
        int id = view.getId();

        if(id == R.id.back_img)
        {
            Utils.hideKeyboard(activity);
            gotoHomeScreen();
        }

    }

    // If Use Come Frm After Complete Payment Screen.
    void gotoHomeScreen()
    {
        Log.e("Constant.comeFrom : ", "Constant.comeFrom : " + Constant.comeFrom.toString());
        if(Constant.comeFrom.equalsIgnoreCase("diamondOrder"))
        {
            Constant.comeFrom = "";
            Intent intent = new Intent(activity, HomeScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
        else{
            Constant.comeFrom = "";
            finish();
        }
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

    }

    @Override
    public void getErrorResponce(String error, int service_ID) {

    }

    @Override
    public void itemClick(int parantPosition, int chiledPosition, String action) {

    }

    private class MyAdapter extends FragmentStateAdapter {
        private Context myContext;
        int totalTabs;

        public MyAdapter(@NonNull FragmentActivity fragmentActivity, int totalTabs) {
            super(fragmentActivity);
            this.totalTabs = totalTabs;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new RecentOrderListFragment();
                case 1:
                    return new ReturnOrderListFragment();
                case 2:
                    return new ReservedOrderListFragment();
                case 3:
                    return new PastOrderListFragment();
                case 4:
                    return new CancelledListOrderFragment();
                default:
                    return new RecentOrderListFragment(); // Default fragment
            }
        }

        @Override
        public int getItemCount() {
            return totalTabs;
        }
    }
}
