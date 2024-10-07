package com.diamondxe.Activity.Dealer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Adapter.CurrencyListAdapter;
import com.diamondxe.Adapter.Dealer.DealerMarkupAdapter;
import com.diamondxe.Adapter.Dealer.KYCVerificationAdapter;
import com.diamondxe.Adapter.SearchResultListWiseAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.CountryListModel;
import com.diamondxe.Beans.Dealer.DealerMarkupListModel;
import com.diamondxe.Beans.KYCVerificationModel;
import com.diamondxe.Beans.SearchResultTypeModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DealerMarkupScreenActivity extends SuperActivity implements RecyclerInterface {

    private ImageView back_img;
    private TextView update_tv, reset_tv;
    private RecyclerView recycler_view;
    ArrayList<DealerMarkupListModel> modelArrayList;
    DealerMarkupAdapter dealerMarkupAdapter;
    private Activity activity;
    private Context context;

    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;

    String apiCallFor = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_markup_screen);

        context = activity = this;

        modelArrayList = new ArrayList<>();

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        update_tv = findViewById(R.id.update_tv);
        update_tv.setOnClickListener(this);

        reset_tv = findViewById(R.id.reset_tv);
        reset_tv.setOnClickListener(this);

        recycler_view = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setNestedScrollingEnabled(false);

        /*DealerMarkupListModel model = new DealerMarkupListModel();
        model.setFromValue("0.01");
        model.setToValue("0.99");
        model.setNaturalValue("2.50");
        model.setLabGrownValue("3.50");

        modelArrayList.add(model);
        modelArrayList.add(model);
        modelArrayList.add(model);
        modelArrayList.add(model);
        modelArrayList.add(model);
        modelArrayList.add(model);

        dealerMarkupAdapter = new DealerMarkupAdapter(modelArrayList, context, this);
        recycler_view.setAdapter(dealerMarkupAdapter);*/

        getSettingAPI(false);
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
        else if(id == R.id.update_tv)
        {
            Utils.hideKeyboard(activity);
            apiCallFor = "update";
            updateSettingAPI(false);
        }
        else if(id == R.id.reset_tv)
        {
            Utils.hideKeyboard(activity);
            apiCallFor = "reset";
            updateSettingAPI(false);
        }
    }

    public void getSettingAPI(boolean showLoader)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("sessionId", "" + uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.DEALER_SETTING, ApiConstants.DEALER_SETTING_ID,showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }
    public void updateSettingAPI(boolean showLoader)
    {
        String uuid = CommonUtility.getAndroidId(context);
        if (Utils.isNetworkAvailable(context))
        {
            if(apiCallFor.equalsIgnoreCase("update"))
            {
                ArrayList<DealerMarkupListModel> updatedList = dealerMarkupAdapter.getUpdatedList();
                JSONArray jsonArray = new JSONArray();

                for (DealerMarkupListModel model : updatedList) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("from", model.getFromValue());
                        jsonObject.put("to", model.getToValue());
                        jsonObject.put("natural", model.getNaturalValue());
                        jsonObject.put("lab_grown", model.getLabGrownValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }

                String jsonString = jsonArray.toString();
                Log.e("------CommissionRange : ", jsonString.toString());

                urlParameter = new HashMap<String, String>();

                urlParameter.put("sessionId", "" + uuid);
                urlParameter.put("CommissionRange", ""+jsonArray);
            }
            else{
                ArrayList<DealerMarkupListModel> updatedList = dealerMarkupAdapter.getUpdatedList();
                JSONArray jsonArray = new JSONArray();

                for (DealerMarkupListModel model : updatedList) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("from", model.getFromValue());
                        jsonObject.put("to", model.getToValue());
                        jsonObject.put("natural", "0");
                        jsonObject.put("lab_grown", "0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }

                String jsonString = jsonArray.toString();
                Log.e("------CommissionRange : ", jsonString.toString());

                urlParameter = new HashMap<String, String>();

                urlParameter.put("sessionId", "" + uuid);
                urlParameter.put("CommissionRange", ""+jsonArray);

            }

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.DEALER_UPDATE_SETTING, ApiConstants.DEALER_UPDATE_SETTING_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }


    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {
        try {
            Log.v("------Diamond----- : ", "--------SettingJSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {

                case ApiConstants.DEALER_SETTING_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        JSONArray details = jObjDetails.getJSONArray("commission_range");

                        if(modelArrayList!=null && modelArrayList.size()>0)
                        {
                            modelArrayList.clear();
                        }
                        else{}
                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            DealerMarkupListModel model = new DealerMarkupListModel();

                            model.setFromValue(CommonUtility.checkString(objectCodes.optString("from")));
                            model.setToValue(CommonUtility.checkString(objectCodes.optString("to")));
                            model.setNaturalValue(CommonUtility.checkString(objectCodes.optString("natural")));
                            model.setLabGrownValue(CommonUtility.checkString(objectCodes.optString("lab_grown")));

                            modelArrayList.add(model);
                        }

                        dealerMarkupAdapter = new DealerMarkupAdapter(modelArrayList, context, this);
                        recycler_view.setAdapter(dealerMarkupAdapter);

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

                case ApiConstants.DEALER_UPDATE_SETTING_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        if(apiCallFor.equalsIgnoreCase("reset"))
                        {
                            getSettingAPI(true);
                        }
                        else{}
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
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

    @Override
    public void getErrorResponce(String error, int service_ID) {

    }

    @Override
    public void itemClick(int position, String action) {

    }
}