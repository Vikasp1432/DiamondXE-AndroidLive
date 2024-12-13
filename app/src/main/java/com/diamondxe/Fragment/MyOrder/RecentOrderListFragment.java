package com.diamondxe.Fragment.MyOrder;

import static com.diamondxe.ApiCalling.ApiConstants.rupeesIcon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diamondxe.Activity.MyOrder.CancelOrderScreenActivity;
import com.diamondxe.Activity.MyOrder.OrderSummaryScreenActivity;
import com.diamondxe.Adapter.MyOrder.InnerOrderListAdapter;
import com.diamondxe.Adapter.MyOrder.RecentOrderDetailsListAdapter;
import com.diamondxe.Adapter.MyOrder.RecentOrderListAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;

public class RecentOrderListFragment extends SuperFragment implements TwoRecyclerInterface {
    private RecyclerView recycler_view;
    private TextView error_tv;
    private ProgressBar progressBar;
    private Context context;
    private Activity activity;
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    private ArrayList<MyOrderListModel> modelArrayList;
    private RecentOrderListAdapter adapter;
    private ArrayList<InnerOrderListModel> innerOrderArrayList;
    InnerOrderListAdapter innerOrderListAdapter;
    private BottomSheetDialog dialog;
    RecyclerView recycler_view_order_details;
    RecentOrderDetailsListAdapter recentOrderDetailsListAdapter;
    ArrayList<MyOrderListModel> recentOrderArrayList;
    ArrayList<MyOrderListModel> recentOrderTrackingArrayList;
    Handler handler1 = new Handler(Looper.getMainLooper());
    int pageNo = 1;
    private EndlessRecyclerViewScrollListener scrollListener;
    String selectedCurrencyValue ="",selectedCurrencyCode = "",selectedCurrencyDesc="",selectedCurrencyImage="";
    String detailsOrderId = "", detailsCreatedAt="", trackingNo = "", currentStatusCode = "", latestStatus = "", remark = "", date = "",trackingDateTeime="";

    public RecentOrderListFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_recent_order_list, container, false);

        context = activity = getActivity();

        init(view);

        return view;
    }

    private void init(View view)
    {
        modelArrayList = new ArrayList<>();
        recentOrderArrayList = new ArrayList<>();
        recentOrderTrackingArrayList = new ArrayList<>();

        error_tv = view.findViewById(R.id.error_tv);
        progressBar = view.findViewById(R.id.progressBar);

        recycler_view = view.findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setNestedScrollingEnabled(false);

       // setDummyData();
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
        getOrderListAPI(false);
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
            urlParameter.put("orderType", "Recent");

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

    public void getOrderTrackingDetailsAPI(boolean showLoader, String orderID)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("orderId", orderID);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.ORDER_TRACKING_DETAILS, ApiConstants.ORDER_TRACKING_DETAILS_ID,showLoader,
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
            Log.v("------Diamond----- : ", "--------JSONObjectRecent-------- : " + jsonObject);

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

                            if(!objectCodes.optString("created_at").equalsIgnoreCase(""))
                            {
                                String convertData = CommonUtility.convertDateTimeIntoLocal(objectCodes.optString("created_at"), ApiConstants.DATE_FORMAT, "dd/MM/yyyy, hh:mm:ss a");
                                model.setCreatedAt(convertData);
                                String convertData1 = CommonUtility.convertDateTimeIntoLocal(objectCodes.optString("created_at"), ApiConstants.DATE_FORMAT, "yyyy-MM-dd HH:mm:ss");
                                model.setCompareDateTime(convertData1);
                            }
                            else{}

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
                                innerOrderListModel.setCurrencySymbol(ApiConstants.rupeesIcon);

                                innerOrderArrayList.add(innerOrderListModel);
                            }

                            for (int k = 0; k <innerOrderArrayList.size() ; k++)
                            {

                                String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, innerOrderArrayList.get(k).getSubTotal());
                                String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                                innerOrderArrayList.get(k).setShowingSubTotal(subTotalFormat);
                                innerOrderArrayList.get(k).setCurrencySymbol(getCurrencySymbol);

                            }

                            model.setAllItemsInSection(innerOrderArrayList);

                            modelArrayList.add(model);
                        }
                        // If Page No 1 Then set Data Otherwise only Refresh NotifyDataSet Changed Adapter
                        if(pageNo == 1)
                        {
                            adapter = new RecentOrderListAdapter(modelArrayList,context,this);
                            recycler_view.setAdapter(adapter);
                        }
                        else{
                            adapter.notifyDataSetChanged();
                        }

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

                case ApiConstants.ORDER_DETAILS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        detailsOrderId = CommonUtility.checkString(jObjDetails.optString("order_id"));
                        detailsCreatedAt = CommonUtility.checkString(jObjDetails.optString("created_at"));

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
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            recentOrderArrayList.get(k).setShowingSubTotal(subTotalFormat);
                            recentOrderArrayList.get(k).setCurrencySymbol(getCurrencySymbol);
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

                case ApiConstants.ORDER_TRACKING_DETAILS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Log.v("------Diamond----- : ", "--------JSONObjectTracking-------- : " + jsonObjectData);

                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        detailsOrderId = CommonUtility.checkString(jObjDetails.optString("order_id"));


                        JSONObject jObjDetailsTracking = jObjDetails.optJSONObject("tracking_details");

                         trackingNo = CommonUtility.checkString(jObjDetailsTracking.optString("tarcking_no"));
                         currentStatusCode = CommonUtility.checkString(jObjDetailsTracking.optString("current_status_code"));
                         latestStatus = CommonUtility.checkString(jObjDetailsTracking.optString("latest_status"));
                         remark = CommonUtility.checkString(jObjDetailsTracking.optString("remark"));
                         date = CommonUtility.checkString(jObjDetailsTracking.optString("date"));

                        if(!date.equalsIgnoreCase(""))
                        {
                            date = CommonUtility.convertDateTimeIntoLocal(date, ApiConstants.DATE_FORMAT, "dd/MM/yyyy, hh:mm:ss a");

                        } else{}

                        // Check If recentOrderTrackingArrayList Size Is GraterThen 0 Clear First.
                        if(recentOrderTrackingArrayList!=null && recentOrderTrackingArrayList.size()>0)
                        {
                            recentOrderTrackingArrayList.clear();
                        }else{}

                        JSONArray details = jObjDetailsTracking.getJSONArray("history");

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject jOBJNEW = details.getJSONObject(i);

                            MyOrderListModel model = new MyOrderListModel();

                            model.setOrderNumber(CommonUtility.checkString(detailsOrderId));
                            model.setOrderDateTime(CommonUtility.checkString(detailsCreatedAt));
                            model.setTrackingStatusCode(CommonUtility.checkString(jOBJNEW.optString("status_code")));
                            model.setTrackingStatus(CommonUtility.checkString(jOBJNEW.optString("status")));
                            model.setTrackingNote(CommonUtility.checkString(jOBJNEW.optString("note")));

                            //model.setTrackingDateTime(CommonUtility.checkString(jOBJNEW.optString("datetime")));

                            if(!jOBJNEW.optString("datetime").equalsIgnoreCase(""))
                            {
                                String convertData = CommonUtility.convertDateTimeIntoLocal(jOBJNEW.optString("datetime"), ApiConstants.DATE_FORMAT, "dd/MM/yyyy, hh:mm:ss a");
                                model.setTrackingDateTime(convertData);
                            }
                            else{}

                            recentOrderTrackingArrayList.add(model);
                        }

                        showOrderTrackBottomDialog();
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
            Constant.comeFromWhichFragment = "Recent";

            Intent intent = new Intent(activity, OrderSummaryScreenActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0,0);
        }
        else if(action.equalsIgnoreCase("orderTrack"))
        {
            //showOrderTrackBottomDialog();
            getOrderTrackingDetailsAPI(false, modelArrayList.get(parantPosition).getOrderId());
        }
        else if(action.equalsIgnoreCase("orderCancel"))
        {
            Constant.orderID = modelArrayList.get(parantPosition).getOrderId();
            Intent intent = new Intent(activity, CancelOrderScreenActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0,0);

        }
    }

    // Order Details Popup and Show Order Details Item List.
    private void showOrderDetailsBottomDialog()
    {
        dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog.setContentView(R.layout.dialog_show_order_details_list);

        recycler_view_order_details = dialog.findViewById(R.id.recycler_view);

        TextView textView2 = dialog.findViewById(R.id.textView2);
        TextView order_number_tv = dialog.findViewById(R.id.order_number_tv);
        TextView date_time_tv = dialog.findViewById(R.id.date_time_tv);

        textView2.setText(getResources().getString(R.string.diamond_details));

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
            lp.height = 1000;

            window.setAttributes(lp);
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
        dialog.show();
    }

    private TextView order_number_tv, date_time_tv;

    // Order Track Popup and Show Order Summary.
    private void showOrderTrackBottomDialog()
    {
        dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog.setContentView(R.layout.dialog_show_order_track_details);

        TextView textView2 = dialog.findViewById(R.id.textView2);

        TextView order_number_tv = dialog.findViewById(R.id.order_number_tv);
        TextView date_time_tv = dialog.findViewById(R.id.date_time_tv);

        TextView order_place_lbl = dialog.findViewById(R.id.order_place_lbl);
        TextView order_in_progress_lbl = dialog.findViewById(R.id.order_in_progress_lbl);
        TextView order_out_for_delivery_lbl = dialog.findViewById(R.id.order_out_for_delivery_lbl);
        TextView order_delivered_lbl = dialog.findViewById(R.id.order_delivered_lbl);
        TextView order_return_request_lbl = dialog.findViewById(R.id.order_return_request_lbl);


        TextView order_received_place_tv = dialog.findViewById(R.id.order_received_place_tv);
        TextView order_received_place_date_time_tv = dialog.findViewById(R.id.order_received_place_date_time_tv);

        TextView order_in_progress_tv = dialog.findViewById(R.id.order_in_progress_tv);
        TextView order_in_progress_date_time_tv = dialog.findViewById(R.id.order_in_progress_date_time_tv);

        TextView order_out_for_delivery_tv = dialog.findViewById(R.id.order_out_for_delivery_tv);
        TextView order_out_for_delivery_date_time_tv = dialog.findViewById(R.id.order_out_for_delivery_date_time_tv);

        TextView order_delivered_tv = dialog.findViewById(R.id.order_delivered_tv);
        TextView order_delivered_date_time_tv = dialog.findViewById(R.id.order_delivered_date_time_tv);

        TextView order_return_request_tv = dialog.findViewById(R.id.order_return_request_tv);
        TextView order_return_request_date_time_tv = dialog.findViewById(R.id.order_return_request_date_time_tv);

        RelativeLayout order_place_rel = dialog.findViewById(R.id.order_place_rel);
        RelativeLayout order_in_progress_rel = dialog.findViewById(R.id.order_in_progress_rel);
        RelativeLayout order_out_for_delivery_rel = dialog.findViewById(R.id.order_out_for_delivery_rel);
        RelativeLayout order_delivered_rel = dialog.findViewById(R.id.order_delivered_rel);
        RelativeLayout order_return_request_rel = dialog.findViewById(R.id.order_return_request_rel);

        textView2.setText(getResources().getString(R.string.order_tracking));

        order_number_tv.setText("#"+detailsOrderId);
        date_time_tv.setText(date);

        /*for (int i = 0; i < recentOrderTrackingArrayList.size(); i++)
        {
            if(currentStatusCode.equalsIgnoreCase("1"))
            {
                order_received_place_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingStatusCode());
                order_received_place_date_time_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingDateTime());
                order_place_rel.setBackgroundResource(R.drawable.circle_bg);
            }

            if(currentStatusCode.equalsIgnoreCase("2"))
            {
                order_received_place_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingStatusCode());
                order_received_place_date_time_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingDateTime());
                order_place_rel.setBackgroundResource(R.drawable.circle_bg);

                order_in_progress_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingStatusCode());
                order_in_progress_date_time_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingDateTime());
                order_in_progress_rel.setBackgroundResource(R.drawable.circle_bg);
            }

            if(currentStatusCode.equalsIgnoreCase("3"))
            {
                order_received_place_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingStatusCode());
                order_received_place_date_time_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingDateTime());
                order_place_rel.setBackgroundResource(R.drawable.circle_bg);

                order_in_progress_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingStatusCode());
                order_in_progress_date_time_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingDateTime());
                order_in_progress_rel.setBackgroundResource(R.drawable.circle_bg);

                order_out_for_delivery_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingStatusCode());
                order_out_for_delivery_date_time_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingDateTime());
                order_out_for_delivery_rel.setBackgroundResource(R.drawable.circle_bg);
            }

            if(currentStatusCode.equalsIgnoreCase("4"))
            {
                order_received_place_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingStatusCode());
                order_received_place_date_time_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingDateTime());
                order_place_rel.setBackgroundResource(R.drawable.circle_bg);

                order_in_progress_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingStatusCode());
                order_in_progress_date_time_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingDateTime());
                order_in_progress_rel.setBackgroundResource(R.drawable.circle_bg);

                order_out_for_delivery_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingStatusCode());
                order_out_for_delivery_date_time_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingDateTime());
                order_out_for_delivery_rel.setBackgroundResource(R.drawable.circle_bg);

                order_delivered_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingStatusCode());
                order_delivered_date_time_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingDateTime());
                order_delivered_rel.setBackgroundResource(R.drawable.circle_bg);
            }

            if(currentStatusCode.equalsIgnoreCase("5"))
            {
                order_received_place_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingStatusCode());
                order_received_place_date_time_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingDateTime());
                order_place_rel.setBackgroundResource(R.drawable.circle_bg);

                order_in_progress_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingStatusCode());
                order_in_progress_date_time_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingDateTime());
                order_in_progress_rel.setBackgroundResource(R.drawable.circle_bg);

                order_out_for_delivery_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingStatusCode());
                order_out_for_delivery_date_time_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingDateTime());
                order_out_for_delivery_rel.setBackgroundResource(R.drawable.circle_bg);

                order_delivered_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingStatusCode());
                order_delivered_date_time_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingDateTime());
                order_delivered_rel.setBackgroundResource(R.drawable.circle_bg);

                order_return_request_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingStatusCode());
                order_return_request_date_time_tv.setText(recentOrderTrackingArrayList.get(i).getTrackingDateTime());
                order_return_request_rel.setBackgroundResource(R.drawable.circle_bg);
            }
        }*/

        for (int i = 0; i < recentOrderTrackingArrayList.size(); i++) {
            String trackingStatusCode = recentOrderTrackingArrayList.get(i).getTrackingStatusCode();
            String trackingDateTime = recentOrderTrackingArrayList.get(i).getTrackingDateTime();
            String trackingNote = recentOrderTrackingArrayList.get(i).getTrackingNote();

            // Check if the current status code matches the tracking status code
            if (currentStatusCode.equalsIgnoreCase(trackingStatusCode)) {
                // Set the current status
                setView(order_place_lbl, order_received_place_tv, order_received_place_date_time_tv, order_place_rel, trackingNote, trackingDateTime,trackingStatusCode);

                // Set values for statuses 1 and 2 if current status code is 3 or higher
                if (Integer.parseInt(currentStatusCode) >= 2) {
                    // Set for status 2
                    setView(order_in_progress_lbl, order_in_progress_tv, order_in_progress_date_time_tv, order_in_progress_rel, trackingNote, trackingDateTime, trackingStatusCode);
                }

                if (Integer.parseInt(currentStatusCode) >= 1) {
                    // Set for status 1
                    setView(order_place_lbl, order_received_place_tv, order_received_place_date_time_tv, order_place_rel, trackingNote, trackingDateTime, trackingStatusCode);
                }

                // Set values for status 3
                if (Integer.parseInt(currentStatusCode) >= 3) {
                    setView(order_out_for_delivery_lbl, order_out_for_delivery_tv, order_out_for_delivery_date_time_tv, order_out_for_delivery_rel, trackingNote, trackingDateTime, trackingStatusCode);
                }

                // Set values for status 4
                if (Integer.parseInt(currentStatusCode) >= 4) {
                    setView(order_delivered_lbl, order_delivered_tv, order_delivered_date_time_tv, order_delivered_rel, trackingNote, trackingDateTime, trackingStatusCode);
                }

                // Set values for status 5
                if (Integer.parseInt(currentStatusCode) >= 5) {
                    setView(order_return_request_lbl, order_return_request_tv, order_return_request_date_time_tv, order_return_request_rel, trackingNote, trackingDateTime, trackingStatusCode);
                }

                break; // Exit the loop once we've processed the matching status
            }
        }


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

    // Method to set text and background
    private void setView(TextView statusTextViewLbl, TextView statusTextView, TextView dateTimeTextView, View backgroundView, String status, String dateTime, String trackingStatusCode) {
        statusTextView.setText(status);
        dateTimeTextView.setText(dateTime);
        int currentStatus = Integer.parseInt(currentStatusCode);
        int trackingStatus = Integer.parseInt(trackingStatusCode);
        // Change text color based on the status comparison
        if (trackingStatus <= currentStatus) {
            statusTextViewLbl.setTextColor(ContextCompat.getColor(context, R.color.black)); // Change to green if tracking status is less than or equal to current status
            statusTextView.setTextColor(ContextCompat.getColor(context, R.color.black)); // Change to green if tracking status is less than or equal to current status
            dateTimeTextView.setTextColor(ContextCompat.getColor(context, R.color.black)); // Change to green if tracking status is less than or equal to current status
        } else {
            statusTextViewLbl.setTextColor(ContextCompat.getColor(context, R.color.grey_light)); // Change to red if tracking status is greater than current status
            statusTextView.setTextColor(ContextCompat.getColor(context, R.color.grey_light)); // Change to red if tracking status is greater than current status
            dateTimeTextView.setTextColor(ContextCompat.getColor(context, R.color.grey_light)); // Change to red if tracking status is greater than current status
        }

        backgroundView.setBackgroundResource(R.drawable.circle_bg);
    }
}