package com.diamondxe.Activity.Dealer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.diamondxe.Adapter.Dealer.DealerMarkupAdapter;
import com.diamondxe.Adapter.Dealer.WalletAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.Dealer.DealerMarkupListModel;
import com.diamondxe.Beans.Dealer.WalletListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.EndlessRecyclerViewScrollListener;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class WalletScreenActivity extends SuperActivity implements RecyclerInterface {

    private ImageView back_img;
    private LinearLayout all_lin, debit_lin, credit_lin;
    private TextView all_tv, debit_tv,credit_tv, available_points_tv, redemption_points_value_tv, error_tv;

    private SwipeRefreshLayout swipeContainer;
    private ProgressBar progressBar;
    private RecyclerView recycler_view;
    private RelativeLayout show_more_data_rel;
    ArrayList<WalletListModel> modelArrayList;

    WalletAdapter walletAdapter;
    private Activity activity;
    private Context context;

    String transactionType = "";
    int pageNo = 1;
    private EndlessRecyclerViewScrollListener scrollListener;

    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_screen);

        context = activity = this;

        modelArrayList = new ArrayList<>();

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        show_more_data_rel = findViewById(R.id.show_more_data_rel);

        swipeContainer = findViewById(R.id.swipeContainer);
        progressBar = findViewById(R.id.progressBar);
        error_tv = findViewById(R.id.error_tv);

        available_points_tv = findViewById(R.id.available_points_tv);
        redemption_points_value_tv = findViewById(R.id.redemption_points_value_tv);

        all_lin = findViewById(R.id.all_lin);
        debit_lin = findViewById(R.id.debit_lin);
        credit_lin = findViewById(R.id.credit_lin);

        all_tv = findViewById(R.id.all_tv);
        all_tv.setOnClickListener(this);

        debit_tv = findViewById(R.id.debit_tv);
        debit_tv.setOnClickListener(this);

        credit_tv = findViewById(R.id.credit_tv);
        credit_tv.setOnClickListener(this);

        recycler_view = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setNestedScrollingEnabled(false);

        /*WalletListModel model = new WalletListModel();
        model.setDateTime("06/03/2024, 5:14:24 PM");
        model.setOrderID("#170972546429");
        model.setStatus("Complete");
        model.setPoints("1000000Cr.");
        model.setRemark("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s");

        modelArrayList.add(model);

        walletAdapter = new WalletAdapter(modelArrayList, context, this);
        recycler_view.setAdapter(walletAdapter);*/

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                if (modelArrayList.size()>= Constant.lazyLoadingLimit)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    pageNo = pageNo+1;
                    getWalletHistoryAPI(true, transactionType);
                } else {
                }
            }
        };

        // Adds the scroll listener to RecyclerView
        recycler_view.addOnScrollListener(scrollListener);

        pageNo = 1;
        getWalletHistoryAPI(false, "");

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                pageNo = 1;
                getWalletHistoryAPI(true, transactionType);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeColors(
                ContextCompat.getColor(context, R.color.purple),
                ContextCompat.getColor(context, R.color.purple),
                ContextCompat.getColor(context, R.color.purple),
                ContextCompat.getColor(context, R.color.purple)
        );
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
        else if(id == R.id.all_tv)
        {
            Utils.hideKeyboard(activity);

            all_lin.setBackgroundResource(R.drawable.background_button_shadow);
            debit_lin.setBackgroundResource(R.drawable.background_white_view);
            credit_lin.setBackgroundResource(R.drawable.background_white_view);

            all_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
            debit_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
            credit_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));

            transactionType = "";
            pageNo = 1;
            getWalletHistoryAPI(false, "");
        }
        else if(id == R.id.debit_tv)
        {
            Utils.hideKeyboard(activity);

            all_lin.setBackgroundResource(R.drawable.background_white_view);
            debit_lin.setBackgroundResource(R.drawable.background_button_shadow);
            credit_lin.setBackgroundResource(R.drawable.background_white_view);

            all_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
            debit_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
            credit_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));

            transactionType = "debit";
            pageNo = 1;
            getWalletHistoryAPI(false, "debit");
        }
        else if(id == R.id.credit_tv)
        {
            Utils.hideKeyboard(activity);

            all_lin.setBackgroundResource(R.drawable.background_white_view);
            debit_lin.setBackgroundResource(R.drawable.background_white_view);
            credit_lin.setBackgroundResource(R.drawable.background_button_shadow);

            all_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
            debit_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
            credit_tv.setTextColor(ContextCompat.getColor(context, R.color.white));

            transactionType = "credit";
            pageNo = 1;
            getWalletHistoryAPI(false, "credit");
        }

    }

    public void getWalletHistoryAPI(boolean showLoader, String transactionTYpe)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);
            urlParameter.put("transType", transactionTYpe);
            urlParameter.put("limit", "" + Constant.lazyLoadingLimit);
            urlParameter.put("page", ""+ pageNo);

            /*urlParameter.put("startDate", "" + uuid);
            urlParameter.put("endDate", "" + uuid);*/

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_WALLET_HISTORY, ApiConstants.GET_WALLET_HISTORY_ID,showLoader, "POST");

            if(pageNo == 1)
            {
                error_tv.setVisibility(View.GONE);
            }else{}

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
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
    public void getSuccessResponce(JSONObject jsonObject, int service_ID)
    {
        if(pageNo == 1)
        {}else{
            progressBar.setVisibility(View.GONE);
        }
        try {
            Log.v("------Diamond----- : ", "--------JSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {

                case ApiConstants.GET_WALLET_HISTORY_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        String availableWalletPoints = CommonUtility.checkString(jObjDetails.optString("wallet_points"));

                        available_points_tv.setText(getResources().getString(R.string.available_wallet_points) + availableWalletPoints);
                        redemption_points_value_tv.setText(getResources().getString(R.string.redemption_value));

                        JSONArray details = jObjDetails.getJSONArray("history");

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

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            WalletListModel model = new WalletListModel();

                            model.setUserID(CommonUtility.checkString(objectCodes.optString("user_id")));
                            model.setOrderID(CommonUtility.checkString(objectCodes.optString("order_id")));
                            model.setStatus(CommonUtility.checkString(objectCodes.optString("status")));
                            model.setType(CommonUtility.checkString(objectCodes.optString("type")));
                            model.setRewardType(CommonUtility.checkString(objectCodes.optString("reward_type")));
                            model.setPoints(CommonUtility.checkString(objectCodes.optString("wallet_points")));
                            model.setRemark(CommonUtility.checkString(objectCodes.optString("remark")));
                            model.setDateTime(CommonUtility.checkString(objectCodes.optString("created_at")));

                            if(!objectCodes.optString("created_at").equalsIgnoreCase(""))
                            {
                                String convertData = CommonUtility.convertDateFormat(objectCodes.optString("created_at"), ApiConstants.DATE_FORMAT, "dd/MM/yyyy, HH:mm:ss");
                                model.setDateTime(convertData);
                            }
                            else{}

                            modelArrayList.add(model);
                        }
                        // If Page No 1 Then set Data Otherwise only Refresh NotifyDataSet Changed Adapter
                        if(pageNo == 1)
                        {
                            walletAdapter = new WalletAdapter(modelArrayList, context, this);
                            recycler_view.setAdapter(walletAdapter);
                        }
                        else{
                            walletAdapter.notifyDataSetChanged();
                        }

                        swipeContainer.setRefreshing(false);
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
                error_tv.setVisibility(View.VISIBLE);
                error_tv.setText(ApiConstants.NO_RESULT_FOUND);
                recycler_view.setVisibility(View.GONE);
            } else {
                error_tv.setVisibility(View.GONE);
                recycler_view.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void getErrorResponce(String error, int service_ID) {

    }

    @Override
    public void itemClick(int position, String action) {

    }
}