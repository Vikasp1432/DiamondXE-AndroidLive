package com.diamondxe.Fragment.MyOrder;

import static com.diamondxe.ApiCalling.ApiConstants.rupeesIcon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.diamondxe.Adapter.CurrencyListAdapter;
import com.diamondxe.Adapter.Dealer.CustomPaymentHistoryAdapter;
import com.diamondxe.Adapter.MyOrder.InnerOrderListAdapter;
import com.diamondxe.Adapter.MyOrder.RecentOrderDetailsListAdapter;
import com.diamondxe.Adapter.MyOrder.RecentOrderListAdapter;
import com.diamondxe.Adapter.SearchResultListAdapter;
import com.diamondxe.Adapter.SearchResultListWiseAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.CountryListModel;
import com.diamondxe.Beans.MyOrder.InnerOrderListModel;
import com.diamondxe.Beans.MyOrder.MyOrderListModel;
import com.diamondxe.Beans.SearchResultTypeModel;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    Handler handler1 = new Handler(Looper.getMainLooper());
    int pageNo = 1;
    private EndlessRecyclerViewScrollListener scrollListener;
    String selectedCurrencyValue ="",selectedCurrencyCode = "",selectedCurrencyDesc="",selectedCurrencyImage="";
    String detailsOrderId = "", detailsCreatedAt="";

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
    void setDummyData()
    {
        MyOrderListModel model = new MyOrderListModel();

        model.setOrderNumber("170245895326589");
        model.setOrderDateTime("06/03/24, 05:14:24 PM");
        model.setName("Eleganted Cushion");
        model.setStatus("Confirmed");
        model.setCarat("0.19");
        model.setColor("E");
        model.setClarity("SI2");
        model.setStockNumber("6481931311");
        model.setCategory("LAB");
        model.setSubTotal("1877100000");
        model.setShowingSubTotal("1877100000");
        model.setCurrencySymbol(rupeesIcon);
        model.setImage("https://v360.in/V360Images.aspx?cid=vd&d=KG230-184A");

      /*  innerOrderArrayList = new ArrayList<InnerOrderListModel>();

        InnerOrderListModel innerOrderListModel = new InnerOrderListModel();

        innerOrderListModel.setOrderNumber("170245895326589");
        innerOrderListModel.setOrderDateTime("06/03/24, 05:14:24 PM");
        innerOrderListModel.setName("Eleganted Cushion");
        innerOrderListModel.setStatus("Confirmed");
        innerOrderListModel.setCarat("0.19");
        innerOrderListModel.setColor("E");
        innerOrderListModel.setClarity("SI2");
        innerOrderListModel.setStockNumber("6481931311");
        innerOrderListModel.setCategory("LAB");
        innerOrderListModel.setSubTotal("1877100000");
        innerOrderListModel.setShowingSubTotal("1877100000");
        innerOrderListModel.setCurrencySymbol(rupeesIcon);
        innerOrderListModel.setImage("https://v360.in/V360Images.aspx?cid=vd&d=KG230-184A");

        innerOrderArrayList.add(innerOrderListModel);

        model.setAllItemsInSection(innerOrderArrayList);*/

        modelArrayList.add(model);
        modelArrayList.add(model);
        modelArrayList.add(model);
        modelArrayList.add(model);
        modelArrayList.add(model);
        modelArrayList.add(model);

        adapter = new RecentOrderListAdapter(modelArrayList,context,this);
        recycler_view.setAdapter(adapter);
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

    public void getOrderSummaryAPI(boolean showLoader, String orderID)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("orderId", orderID);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.ORDER_DETAILS, ApiConstants.ORDER_SUMMARY_ID,showLoader,
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
                            model.setShape(CommonUtility.checkString(jOBJNEW.optString("cut_grade")));
                            model.setPolish(CommonUtility.checkString(jOBJNEW.optString("polish")));
                            model.setSymmetry(CommonUtility.checkString(jOBJNEW.optString("symmetry")));
                            model.setFir(CommonUtility.checkString(jOBJNEW.optString("fluorescence_intensity")));
                            model.setDepth(CommonUtility.checkString(jOBJNEW.optString("depth_perc")));
                            model.setTable(CommonUtility.checkString(jOBJNEW.optString("table_perc")));
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

                case ApiConstants.ORDER_SUMMARY_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        detailsOrderId = CommonUtility.checkString(jObjDetails.optString("order_id"));
                        detailsCreatedAt = CommonUtility.checkString(jObjDetails.optString("created_at"));

                        if(!detailsCreatedAt.equalsIgnoreCase(""))
                        {
                            detailsCreatedAt = CommonUtility.convertDateTimeIntoLocal(detailsCreatedAt, ApiConstants.DATE_FORMAT, "dd/MM/yyyy, hh:mm:ss a");

                        } else{}

                        JSONObject jObjUserDetails = jObjDetails.optJSONObject("user_details");

                        String payment_mode = CommonUtility.checkString(jObjDetails.optString("payment_mode"));
                        String payment_status = CommonUtility.checkString(jObjDetails.optString("payment_status"));
                        String sub_total = CommonUtility.checkString(jObjDetails.optString("sub_total"));
                        String delivery_date = CommonUtility.checkString(jObjDetails.optString("delivery_date"));
                        String shipping_charge = CommonUtility.checkString(jObjDetails.optString("shipping_charge"));
                        String platform_fee = CommonUtility.checkString(jObjDetails.optString("platform_fee"));
                        String bank_charge = CommonUtility.checkString(jObjDetails.optString("bank_charge"));
                        String wallet_points = CommonUtility.checkString(jObjDetails.optString("wallet_points"));
                        String coupon_value = CommonUtility.checkString(jObjDetails.optString("coupon_value"));
                        String is_coupon_applied = CommonUtility.checkString(jObjDetails.optString("is_coupon_applied"));
                        String coupon_code = CommonUtility.checkString(jObjDetails.optString("coupon_code"));
                        String coupon_discount = CommonUtility.checkString(jObjDetails.optString("coupon_discount"));
                        String other_tax = CommonUtility.checkString(jObjDetails.optString("total_charge_tax"));
                        String diamond_tax = CommonUtility.checkString(jObjDetails.optString("tax"));
                        String total_charge = CommonUtility.checkString(jObjDetails.optString("total_charge"));
                        String sub_total1 = CommonUtility.checkString(jObjDetails.optString("total_amount"));
                        String final_amount = CommonUtility.checkString(jObjDetails.optString("sub_total"));
                        String diamond_price = CommonUtility.checkString(jObjDetails.optString("sub_total"));

                        String billing_address = CommonUtility.checkString(jObjUserDetails.optString("billing_address"));
                        String shipping_address = CommonUtility.checkString(jObjUserDetails.optString("shipping_address"));
                        String user_email = CommonUtility.checkString(jObjUserDetails.optString("user_email"));
                        String user_mobile = CommonUtility.checkString(jObjUserDetails.optString("user_mobile"));

                        showOrderSummaryBottomDialog();
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
            //getOrderDetailsAPI(false, modelArrayList.get(parantPosition).getOrderId());
            showOrderDetailsBottomDialog();
        }
        else if(action.equalsIgnoreCase("orderSummary"))
        {
            //getOrderSummaryAPI(false, modelArrayList.get(parantPosition).getOrderId());
            showOrderSummaryBottomDialog();
        }
        else if(action.equalsIgnoreCase("orderTrack"))
        {
            showOrderTrackBottomDialog();
        }
        else if(action.equalsIgnoreCase("orderCancel"))
        {
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

       // order_number_tv.setText(detailsOrderId);
       // date_time_tv.setText(detailsCreatedAt);

        recycler_view_order_details.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_view_order_details.setLayoutManager(layoutManager);
        recycler_view_order_details.setNestedScrollingEnabled(false);

        MyOrderListModel model = new MyOrderListModel();

        model.setOrderNumber("170245895326589");
        model.setOrderDateTime("06/03/24, 05:14:24 PM");
        model.setName("Eleganted Cushion");
        model.setStatus("Confirmed");
        model.setShape("Princess");
        model.setCarat("0.19");
        model.setColor("E");
        model.setClarity("SI2");
        model.setType("HPHT");
        model.setCut("VG");
        model.setPolish("EX");
        model.setSymmetry("EX");
        model.setFir("NIL");
        model.setLab("IGI");
        model.setTable("56.00%");
        model.setDepth("64.50%");
        model.setStockNumber("6481931311");
        model.setCategory("LAB");
        model.setSubTotal("854621453");
        model.setShowingSubTotal("854621453");
        model.setCurrencySymbol(rupeesIcon);
        model.setIsReturnable("1");
        model.setImage("https://v360.in/V360Images.aspx?cid=vd&d=KG230-184A");

        recentOrderArrayList.add(model);
        recentOrderArrayList.add(model);
        recentOrderArrayList.add(model);
        recentOrderArrayList.add(model);
        recentOrderArrayList.add(model);
        recentOrderArrayList.add(model);

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

    private TextView order_number_tv, date_time_tv, utr_cheque_no_tv, order_status_tv, amount_tv, order_place_date_tv, delivery_date_tv,
            payment_mode_tv,shipping_address_tv, billing_address_tv,contact_no_tv, email_tv,diamond_price_tv, shipping_and_handling_tv,
            platform_fees_tv, total_charges_tv, other_taxes_tv,diamond_taxes_tv,total_taxes_tv, sub_total_tv, bank_charges_tv,final_amount_tv,
            others_txt_gst_perc_tv, diamond_txt_gst_perc_tv;
    private RelativeLayout rel_other_tax, rel_diamond_tax;
    // Order Summary Popup and Show Order Summary.
    private void showOrderSummaryBottomDialog()
    {
        dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog.setContentView(R.layout.dialog_show_order_summary_details);

        TextView textView2 = dialog.findViewById(R.id.textView2);

        textView2.setText(getResources().getString(R.string.order_summary));

        order_number_tv = dialog.findViewById(R.id.order_number_tv);
        date_time_tv = dialog.findViewById(R.id.date_time_tv);
        utr_cheque_no_tv = dialog.findViewById(R.id.utr_cheque_no_tv);
        order_status_tv = dialog.findViewById(R.id.order_status_tv);
        amount_tv = dialog.findViewById(R.id.amount_tv);
        order_place_date_tv = dialog.findViewById(R.id.order_place_date_tv);
        delivery_date_tv = dialog.findViewById(R.id.delivery_date_tv);
        payment_mode_tv = dialog.findViewById(R.id.payment_mode_tv);
        shipping_address_tv = dialog.findViewById(R.id.shipping_address_tv);
        billing_address_tv = dialog.findViewById(R.id.billing_address_tv);
        contact_no_tv = dialog.findViewById(R.id.contact_no_tv);
        email_tv = dialog.findViewById(R.id.email_tv);
        diamond_price_tv = dialog.findViewById(R.id.diamond_price_tv);
        shipping_and_handling_tv = dialog.findViewById(R.id.shipping_and_handling_tv);
        platform_fees_tv = dialog.findViewById(R.id.platform_fees_tv);
        total_charges_tv = dialog.findViewById(R.id.total_charges_tv);
        other_taxes_tv = dialog.findViewById(R.id.other_taxes_tv);
        diamond_taxes_tv = dialog.findViewById(R.id.diamond_taxes_tv);
        total_taxes_tv = dialog.findViewById(R.id.total_taxes_tv);
        sub_total_tv = dialog.findViewById(R.id.sub_total_tv);
        bank_charges_tv = dialog.findViewById(R.id.bank_charges_tv);
        final_amount_tv = dialog.findViewById(R.id.final_amount_tv);
        others_txt_gst_perc_tv = dialog.findViewById(R.id.others_txt_gst_perc_tv);
        diamond_txt_gst_perc_tv = dialog.findViewById(R.id.diamond_txt_gst_perc_tv);

        rel_other_tax = dialog.findViewById(R.id.rel_other_tax);
        rel_other_tax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                others_txt_gst_perc_tv.setVisibility(View.VISIBLE);
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        others_txt_gst_perc_tv.setVisibility(View.GONE);
                    }
                }, 2000);
            }
        });

        rel_diamond_tax = dialog.findViewById(R.id.rel_diamond_tax);
        rel_diamond_tax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diamond_txt_gst_perc_tv.setVisibility(View.VISIBLE);
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        diamond_txt_gst_perc_tv.setVisibility(View.GONE);
                    }
                }, 2000);
            }
        });

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

    // Order Track Popup and Show Order Summary.
    private void showOrderTrackBottomDialog()
    {
        dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog.setContentView(R.layout.dialog_show_order_track_details);

        TextView textView2 = dialog.findViewById(R.id.textView2);

        textView2.setText(getResources().getString(R.string.order_tracking));

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