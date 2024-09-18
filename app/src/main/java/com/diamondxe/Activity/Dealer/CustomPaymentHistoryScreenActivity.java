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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.diamondxe.Adapter.Dealer.CustomPaymentHistoryAdapter;
import com.diamondxe.Adapter.Dealer.WalletAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.Dealer.CustomPaymentHistoryModel;
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

public class CustomPaymentHistoryScreenActivity extends SuperActivity implements RecyclerInterface {

    private ImageView back_img;
    private LinearLayout custom_payment_lin, history_lin;
    private TextView custom_payment_tv, history_tv, error_tv;
    private SwipeRefreshLayout swipeContainer;
    private ProgressBar progressBar;
    private RecyclerView recycler_view;
    ArrayList<CustomPaymentHistoryModel> modelArrayList;

    CustomPaymentHistoryAdapter adapter;
    private Activity activity;
    private Context context;
    int pageNo = 1;
    private EndlessRecyclerViewScrollListener scrollListener;
    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_payment_history);

        context = activity = this;

        modelArrayList = new ArrayList<>();

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        swipeContainer = findViewById(R.id.swipeContainer);

        error_tv = findViewById(R.id.error_tv);

        progressBar = findViewById(R.id.progressBar);

        custom_payment_lin = findViewById(R.id.custom_payment_lin);
        history_lin = findViewById(R.id.history_lin);

        custom_payment_tv = findViewById(R.id.custom_payment_tv);
        custom_payment_tv.setOnClickListener(this);

        history_tv = findViewById(R.id.history_tv);
        history_tv.setOnClickListener(this);

        recycler_view = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setNestedScrollingEnabled(false);

        /*CustomPaymentHistoryModel model = new CustomPaymentHistoryModel();
        model.setDateTime("06/03/2024, 5:14:24 PM");
        model.setOrderID("#170972546429");
        model.setStatus("Complete");
        model.setAmount("1000000Cr.");
        model.setMode("Net Banking");
        model.setRemark("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s");
        modelArrayList.add(model);*/

        adapter = new CustomPaymentHistoryAdapter(modelArrayList, context, this);
        recycler_view.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                if (modelArrayList.size()>= Constant.lazyLoadingLimit)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    pageNo = pageNo+1;
                    getCustomPaymentHistoryAPI(true);
                } else {
                }
            }
        };

        // Adds the scroll listener to RecyclerView
        recycler_view.addOnScrollListener(scrollListener);

        pageNo = 1;
        getCustomPaymentHistoryAPI(false);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                pageNo = 1;
                getCustomPaymentHistoryAPI(true);
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
        else if (id == R.id.custom_payment_tv)
        {
            Utils.hideKeyboard(activity);
            intent = new Intent(activity, CustomPaymentScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        }
        else if (id == R.id.history_tv) {
            Utils.hideKeyboard(activity);
        }
    }

    public void getCustomPaymentHistoryAPI(boolean showLoader)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);
            urlParameter.put("limit", "" + Constant.lazyLoadingLimit);
            urlParameter.put("page", ""+ pageNo);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.CUSTOM_PAYMENT_HISTORY, ApiConstants.CUSTOM_PAYMENT_HISTORY_ID,
                    showLoader, "POST");

            if(pageNo == 1)
            {
                error_tv.setVisibility(View.GONE);
            }else{}

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
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

                case ApiConstants.CUSTOM_PAYMENT_HISTORY_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

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

                            CustomPaymentHistoryModel model = new CustomPaymentHistoryModel();

                            model.setOrderID(CommonUtility.checkString(objectCodes.optString("reference_no")));
                            model.setCurrencySymbol(CommonUtility.checkString(objectCodes.optString("currency_symbol")));
                            model.setAmount(CommonUtility.checkString(objectCodes.optString("final_amount")));
                            model.setMode(CommonUtility.checkString(objectCodes.optString("payment_mode")));
                            model.setStatus(CommonUtility.checkString(objectCodes.optString("payment_status")));
                            model.setRemark(CommonUtility.checkString(objectCodes.optString("description")));
                            model.setDateTime(CommonUtility.checkString(objectCodes.optString("created_at")));

                            if(!objectCodes.optString("created_at").equalsIgnoreCase(""))
                            {
                                String convertData = CommonUtility.convertDateTimeIntoLocal(objectCodes.optString("created_at"), ApiConstants.DATE_FORMAT, "dd/MM/yyyy, hh:mm:ss a");
                                model.setDateTime(convertData);
                            }
                            else{}

                            modelArrayList.add(model);
                        }
                        // If Page No 1 Then set Data Otherwise only Refresh NotifyDataSet Changed Adapter
                        if(pageNo == 1)
                        {
                            adapter = new CustomPaymentHistoryAdapter(modelArrayList, context, this);
                            recycler_view.setAdapter(adapter);
                        }
                        else{
                            adapter.notifyDataSetChanged();
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