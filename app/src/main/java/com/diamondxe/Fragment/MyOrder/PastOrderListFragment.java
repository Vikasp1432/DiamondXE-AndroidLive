package com.diamondxe.Fragment.MyOrder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.diamondxe.Activity.MyOrder.CancelOrderScreenActivity;
import com.diamondxe.Activity.MyOrder.OrderSummaryScreenActivity;
import com.diamondxe.Activity.MyOrder.ReturnOrderScreenActivity;
import com.diamondxe.Adapter.MyOrder.PastOrderListAdapter;
import com.diamondxe.Adapter.MyOrder.RecentOrderDetailsListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.MyOrder.InnerOrderListModel;
import com.diamondxe.Beans.MyOrder.MyOrderListModel;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.Network.EndlessRecyclerViewScrollListener;
import com.diamondxe.Network.SuperFragment;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

public class PastOrderListFragment extends SuperFragment implements TwoRecyclerInterface {
    private RecyclerView recycler_view;
    private TextView error_tv;
    private ProgressBar progressBar;
    private Context context;
    private Activity activity;
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    private ArrayList<MyOrderListModel> modelArrayList;
    private ArrayList<InnerOrderListModel> innerOrderArrayList;
    private PastOrderListAdapter adapter;
    RecentOrderDetailsListAdapter recentOrderDetailsListAdapter;
    ArrayList<MyOrderListModel> recentOrderArrayList;
    private BottomSheetDialog dialog;
    RecyclerView recycler_view_order_details;
    int pageNo = 1;
    private EndlessRecyclerViewScrollListener scrollListener;
    Handler handler1 = new Handler(Looper.getMainLooper());
    String selectedCurrencyValue ="",selectedCurrencyCode = "",selectedCurrencyDesc="",selectedCurrencyImage="";
    String detailsOrderId = "", detailsCreatedAt="";

    public PastOrderListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_order_list, container, false);

        context = activity = getActivity();

        init(view);

        return view;
    }

    private void init(View view)
    {
        modelArrayList = new ArrayList<>();
        recentOrderArrayList = new ArrayList<>();

        error_tv = view.findViewById(R.id.error_tv);
        progressBar = view.findViewById(R.id.progressBar);

        recycler_view = view.findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);

        getCurrencyData();

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                if (modelArrayList.size()>= Constant.lazyLoadingLimit)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    pageNo = pageNo+1;
                    getOrderListAPI(true);
                } else {
                }
            }
        };
        // Adds the scroll listener to RecyclerView
        recycler_view.addOnScrollListener(scrollListener);

        pageNo = 1;
        getOrderListAPI(true);
    }

    void getCurrencyData()
    {
        selectedCurrencyValue = CommonUtility.getGlobalString(context, "selected_currency_value");
        selectedCurrencyCode = CommonUtility.getGlobalString(context, "selected_currency_code");
        selectedCurrencyDesc = CommonUtility.getGlobalString(context, "selected_currency_desc");
        selectedCurrencyImage = CommonUtility.getGlobalString(context, "selected_currency_image");
    }

    @Override
    public void onClick(View view) {}

    @Override
    public void onResume() {
        super.onResume();
        getCurrencyData();
    }


    public void getOrderListAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("page", ""+pageNo);
            urlParameter.put("limit", ""+Constant.lazyLoadingLimit);
            urlParameter.put("orderType", "Past");

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.ORDER_LIST, ApiConstants.ORDER_LIST_ID,showLoader,
                    "POST");

            if(pageNo == 1)
            {
                error_tv.setVisibility(View.GONE);
            }else{}

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void getOrderDetailsAPI(boolean showLoader, String orderID)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("orderId", orderID);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.ORDER_DETAILS, ApiConstants.ORDER_DETAILS_ID,showLoader,
                    "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {
        if(pageNo == 1)
        {}else{
            progressBar.setVisibility(View.GONE);
        }
        try {
            Log.v("------Diamond----- : ", "--------JSONObjectPast-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {

                case ApiConstants.ORDER_LIST_ID:
                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONArray details = jsonObjectData.getJSONArray("details");

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
                            MyOrderListModel model = new MyOrderListModel();

                            JSONObject objectCodes = details.getJSONObject(i);

                            model.setOrderId(CommonUtility.checkString(objectCodes.optString("order_id")));
                            model.setOrderDate(CommonUtility.checkString(objectCodes.optString("order_date")));
                            model.setCurrencyCode(CommonUtility.checkString(objectCodes.optString("currency_code")));
                            model.setTotalAmount(CommonUtility.checkString(objectCodes.optString("total_amount")));
                            model.setOrderStatus(CommonUtility.checkString(objectCodes.optString("order_status")));
                            model.setPaymentStatus(CommonUtility.checkString(objectCodes.optString("payment_status")));
                            //model.setCreatedAt(CommonUtility.checkString(objectCodes.optString("created_at")));
                            model.setIsCancelable(CommonUtility.checkString(objectCodes.optString("is_cancelable")));
                            model.setIsReturnable(CommonUtility.checkString(objectCodes.optString("is_returnable")));
                            model.setIsReserveOrder(CommonUtility.checkString(objectCodes.optString("is_reserve_order")));
                            model.setPaymentReceivedDate(CommonUtility.checkString(objectCodes.optString("payment_received_date")));
                            model.setTimeLeftForCancel(CommonUtility.checkString(objectCodes.optString("time_left_for_cancel")));

                            String getCurrencySymbol=CommonUtility.checkString(objectCodes.optString("currency_symbol"));
                            String getexchange_rate=CommonUtility.checkString(objectCodes.optString("exchange_rate"));
                            Log.e("getCurrencySymbol","270..@@..."+getCurrencySymbol);
                            Log.e("getexchange_rate","270..@@..."+getexchange_rate);
                            if(!objectCodes.optString("created_at").equalsIgnoreCase(""))
                            {
                                String convertData = CommonUtility.convertDateTimeIntoLocal(objectCodes.optString("created_at"), ApiConstants.DATE_FORMAT, "dd/MM/yyyy, hh:mm:ss a");
                                model.setCreatedAt(convertData);
                                String convertData1 = CommonUtility.convertDateTimeIntoLocal(objectCodes.optString("created_at"), ApiConstants.DATE_FORMAT, "yyyy-MM-dd HH:mm:ss");
                                model.setCompareDateTime(convertData1);
                            } else{}

                            if(!objectCodes.optString("delivery_date").equalsIgnoreCase(""))
                            {
                                String convertData = CommonUtility.convertDateTimeIntoLocal(objectCodes.optString("delivery_date"), ApiConstants.DATE_FORMAT, "dd/MM/yyyy, hh:mm:ss a");
                                model.setDeliveryDate(convertData);
                            } else{}

                            if(!objectCodes.optString("return_eligible_date").equalsIgnoreCase(""))
                            {
                                String convertData = CommonUtility.convertDateTimeIntoLocal(objectCodes.optString("return_eligible_date"), ApiConstants.DATE_FORMAT, "dd/MM/yyyy");
                                model.setReturnEligibleDate(convertData);
                            } else{}

                            if(!objectCodes.optString("return_policy_url").equalsIgnoreCase(""))
                            {
                                model.setReturnPolicyUrl(CommonUtility.checkString(objectCodes.optString("return_policy_url")));
                            } else{}

                            innerOrderArrayList = new ArrayList<InnerOrderListModel>();

                            JSONArray jArray = objectCodes.optJSONArray("diamonds");

                            for (int j = 0; j < jArray.length(); j++)
                            {
                                JSONObject jOBJNEW = jArray.getJSONObject(j);

                                InnerOrderListModel innerOrderListModel = new InnerOrderListModel();

                                innerOrderListModel.setStockId(CommonUtility.checkString(jOBJNEW.optString("stock_id")));
                                innerOrderListModel.setCertificateNo(CommonUtility.checkString(jOBJNEW.optString("certificate_no")));
                                innerOrderListModel.setStockNo(CommonUtility.checkString(jOBJNEW.optString("stock_no")));
                                innerOrderListModel.setIsReturnable(CommonUtility.checkString(jOBJNEW.optString("is_returnable")));
                                innerOrderListModel.setDxePrefered(CommonUtility.checkString(jOBJNEW.optString("dxe_prefered")));
                                innerOrderListModel.setCategory(CommonUtility.checkString(jOBJNEW.optString("category")));
                                innerOrderListModel.setItemName(CommonUtility.checkString(jOBJNEW.optString("item_name")));
                                innerOrderListModel.setDiscount(CommonUtility.checkString(jOBJNEW.optString("discount")));
                                innerOrderListModel.setTotalAmount(CommonUtility.checkString(jOBJNEW.optString("total_amount")));
                                innerOrderListModel.setTotalPrice(CommonUtility.checkString(jOBJNEW.optString("total_price")));
                                innerOrderListModel.setSubTotal(CommonUtility.checkString(jOBJNEW.optString("sub_total")));
                                innerOrderListModel.setShowingSubTotal(CommonUtility.checkString(jOBJNEW.optString("sub_total")));
                                innerOrderListModel.setDiamondImage(CommonUtility.checkString(jOBJNEW.optString("diamond_image")));
                                innerOrderListModel.setStatus(CommonUtility.checkString(jOBJNEW.optString("status")));
                                innerOrderListModel.setCarat(CommonUtility.checkString(jOBJNEW.optString("carat")));
                                innerOrderListModel.setColor(CommonUtility.checkString(jOBJNEW.optString("color")));
                                innerOrderListModel.setClarity(CommonUtility.checkString(jOBJNEW.optString("clarity")));
                                innerOrderListModel.setShape(CommonUtility.checkString(jOBJNEW.optString("shape")));
                                innerOrderListModel.setCancelBy(CommonUtility.checkString(jOBJNEW.optString("cancel_by")));
                                innerOrderListModel.setRefundStatus(CommonUtility.checkString(jOBJNEW.optString("refund_status")));
                                innerOrderListModel.setCurrencySymbol(ApiConstants.rupeesIcon);

                                innerOrderArrayList.add(innerOrderListModel);
                            }

                            for (int k = 0; k <innerOrderArrayList.size() ; k++)
                            {

                                String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, innerOrderArrayList.get(k).getSubTotal());
                               /* String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                                innerOrderArrayList.get(k).setShowingSubTotal(subTotalFormat);
                                innerOrderArrayList.get(k).setCurrencySymbol(getCurrencySymbol);*/
                                try {
                                    double subTotal = Double.parseDouble(subTotalFormat);
                                    double exchangeRate = Double.parseDouble(getexchange_rate);

                                    double resultDouble = subTotal * exchangeRate;
                                    int result = (int) resultDouble;

                                    // Log.e("result", "..14000..." + result);
                                    BigDecimal bd = new BigDecimal(resultDouble).setScale(2, RoundingMode.HALF_UP);
                                    double resultWithTwoDecimals = bd.doubleValue();
                                    Log.e("resultWithTwoDecimals","..333...."+resultWithTwoDecimals);
                                    innerOrderArrayList.get(k).setShowingSubTotal(String.valueOf(resultWithTwoDecimals));
                                    innerOrderArrayList.get(k).setCurrencySymbol(getCurrencySymbol);


                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                            }

                            model.setAllItemsInSection(innerOrderArrayList);

                            modelArrayList.add(model);
                        }
                        // If Page No 1 Then set Data Otherwise only Refresh NotifyDataSet Changed Adapter
                        if(pageNo == 1)
                        {
                            adapter = new PastOrderListAdapter(modelArrayList,context,this);
                            recycler_view.setAdapter(adapter);
                        }
                        else{
                            adapter.notifyDataSetChanged();
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

                case ApiConstants.ORDER_DETAILS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        detailsOrderId = CommonUtility.checkString(jObjDetails.optString("order_id"));
                        detailsCreatedAt = CommonUtility.checkString(jObjDetails.optString("created_at"));
                        String getCurrencySymbol=CommonUtility.checkString(jObjDetails.optString("currency_symbol"));
                        String  getexchange_rate=CommonUtility.checkString(jObjDetails.optString("exchange_rate"));
                        Log.e("getCurrencySymbol","270..@@..."+getCurrencySymbol);
                        Log.e("getexchange_rate","270..@@..."+getexchange_rate);
                        if(!detailsCreatedAt.equalsIgnoreCase(""))
                        {
                            detailsCreatedAt = CommonUtility.convertDateTimeIntoLocal(detailsCreatedAt, ApiConstants.DATE_FORMAT, "dd/MM/yyyy, hh:mm:ss a");

                        } else{}

                        // Check If recentOrderArrayList Size Is GraterThen 0 Clear First.
                        if(recentOrderArrayList!=null && recentOrderArrayList.size()>0)
                        {
                            recentOrderArrayList.clear();
                        }else{}

                        JSONArray details = jObjDetails.getJSONArray("diamonds");

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject jOBJNEW = details.getJSONObject(i);

                            MyOrderListModel model = new MyOrderListModel();

                            model.setStockId(CommonUtility.checkString(jOBJNEW.optString("stock_id")));
                            model.setCertificateNo(CommonUtility.checkString(jOBJNEW.optString("certificate_no")));
                            model.setStockNo(CommonUtility.checkString(jOBJNEW.optString("stock_no")));
                            model.setIsReturnable(CommonUtility.checkString(jOBJNEW.optString("is_returnable")));
                            model.setDxePrefered(CommonUtility.checkString(jOBJNEW.optString("dxe_prefered")));
                            model.setCategory(CommonUtility.checkString(jOBJNEW.optString("category")));
                            model.setSubTotal(CommonUtility.checkString(jOBJNEW.optString("sub_total")));
                            model.setShowingSubTotal(CommonUtility.checkString(jOBJNEW.optString("sub_total")));
                            model.setDiamondImage(CommonUtility.checkString(jOBJNEW.optString("diamond_image")));
                            model.setCarat(CommonUtility.checkString(jOBJNEW.optString("carat")));
                            model.setColor(CommonUtility.checkString(jOBJNEW.optString("color")));
                            model.setClarity(CommonUtility.checkString(jOBJNEW.optString("clarity")));
                            model.setShape(CommonUtility.checkString(jOBJNEW.optString("shape")));
                            model.setGrowthType(CommonUtility.checkString(jOBJNEW.optString("growth_type")));
                            model.setCut(CommonUtility.checkString(jOBJNEW.optString("cut_grade")));
                            model.setPolish(CommonUtility.checkString(jOBJNEW.optString("polish")));
                            model.setSymmetry(CommonUtility.checkString(jOBJNEW.optString("symmetry")));
                            model.setFir(CommonUtility.checkString(jOBJNEW.optString("fluorescence_intensity")));
                            model.setDepth(CommonUtility.checkString(jOBJNEW.optString("depth_perc")));
                            model.setTable(CommonUtility.checkString(jOBJNEW.optString("table_perc")));
                            model.setCertificateName(CommonUtility.checkString(jOBJNEW.optString("certificate_name")));
                            model.setCurrencySymbol(ApiConstants.rupeesIcon);

                            recentOrderArrayList.add(model);
                        }

                        for (int k = 0; k <recentOrderArrayList.size() ; k++)
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, recentOrderArrayList.get(k).getSubTotal());
                            /*String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            recentOrderArrayList.get(k).setShowingSubTotal(subTotalFormat);
                            recentOrderArrayList.get(k).setCurrencySymbol(getCurrencySymbol);*/
                            try {
                                double subTotal = Double.parseDouble(subTotalFormat);
                                double exchangeRate = Double.parseDouble(getexchange_rate);

                                double resultDouble = subTotal * exchangeRate;
                                int result = (int) resultDouble;

                                // Log.e("result", "..14000..." + result);
                                BigDecimal bd = new BigDecimal(resultDouble).setScale(2, RoundingMode.HALF_UP);
                                double resultWithTwoDecimals = bd.doubleValue();
                                Log.e("resultWithTwoDecimals","..333...."+resultWithTwoDecimals);
                                recentOrderArrayList.get(k).setShowingSubTotal(String.valueOf(resultWithTwoDecimals));
                                recentOrderArrayList.get(k).setCurrencySymbol(getCurrencySymbol);


                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }

                        showOrderDetailsBottomDialog();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("0"))
                    {
                        //getCountryListAPI(true);
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
                error_tv.setText(""+ ApiConstants.NO_RESULT_FOUND);
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
    public void itemClick(int parantPosition, int chiledPosition, String action)
    {
        if(action.equalsIgnoreCase("orderDetails"))
        {
            getOrderDetailsAPI(false, modelArrayList.get(parantPosition).getOrderId());
        }
        else if(action.equalsIgnoreCase("orderSummary"))
        {
            Constant.orderID = modelArrayList.get(parantPosition).getOrderId();
            Constant.comeFromWhichFragment = "Past";

            Intent intent = new Intent(activity, OrderSummaryScreenActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0,0);
        }
        else if(action.equalsIgnoreCase("orderReturn"))
        {
            Constant.orderID = modelArrayList.get(parantPosition).getOrderId();
            Intent intent = new Intent(activity, ReturnOrderScreenActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0,0);
        }
        else if(action.equalsIgnoreCase("orderViewPolicy"))
        {
            if(!modelArrayList.get(parantPosition).getReturnPolicyUrl().equalsIgnoreCase(""))
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(modelArrayList.get(parantPosition).getReturnPolicyUrl()));
                startActivity(intent);
            }else{
                Toast.makeText(activity, getResources().getString(R.string.no_policy_url_find), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showOrderDetailsBottomDialog()
    {
        dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog.setContentView(R.layout.dialog_show_order_details_list);

        recycler_view_order_details = dialog.findViewById(R.id.recycler_view);

        TextView textView2 = dialog.findViewById(R.id.textView2);
        TextView order_number_tv = dialog.findViewById(R.id.order_number_tv);
        TextView date_time_tv = dialog.findViewById(R.id.date_time_tv);

        //textView2.setText(getResources().getString(R.string.diamond_details));
        textView2.setText(getResources().getString(R.string.order_details));
        order_number_tv.setText("#"+detailsOrderId);
        date_time_tv.setText(detailsCreatedAt);

        recycler_view_order_details.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_view_order_details.setLayoutManager(layoutManager);
        recycler_view_order_details.setNestedScrollingEnabled(false);

        recentOrderDetailsListAdapter = new RecentOrderDetailsListAdapter(recentOrderArrayList, context, this);
        recycler_view_order_details.setAdapter(recentOrderDetailsListAdapter);
        recentOrderDetailsListAdapter.notifyDataSetChanged();

        ImageView ib_cross = dialog.findViewById(R.id.ib_cross);

        ib_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = 1000; // Set your desired fixed height here, in pixels

            window.setAttributes(lp);
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
        dialog.show();
    }
}