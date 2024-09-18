package com.diamondxe.Fragment.MyOrder;

import static com.diamondxe.ApiCalling.ApiConstants.rupeesIcon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.diamondxe.Activity.MyOrder.ReturnOrderSummaryScreenActivity;
import com.diamondxe.Adapter.MyOrder.InnerOrderListAdapter;
import com.diamondxe.Adapter.MyOrder.RecentOrderDetailsListAdapter;
import com.diamondxe.Adapter.MyOrder.RecentOrderListAdapter;
import com.diamondxe.Adapter.MyOrder.ReturnOrderDetailsListAdapter;
import com.diamondxe.Adapter.MyOrder.ReturnOrderListAdapter;
import com.diamondxe.Beans.MyOrder.InnerOrderListModel;
import com.diamondxe.Beans.MyOrder.MyOrderListModel;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.Network.SuperFragment;
import com.diamondxe.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONObject;

import java.util.ArrayList;


public class ReturnOrderListFragment extends SuperFragment implements TwoRecyclerInterface {

    private RecyclerView recycler_view;
    private TextView error_tv;
    private Context context;
    private Activity activity;
    private ArrayList<MyOrderListModel> modelArrayList;
    private ReturnOrderListAdapter adapter;
    private BottomSheetDialog dialog;
    RecyclerView recycler_view_order_details;
    ReturnOrderDetailsListAdapter returnOrderDetailsListAdapter;
    ArrayList<MyOrderListModel> returnOrderArrayList;
    Handler handler1 = new Handler(Looper.getMainLooper());

    public ReturnOrderListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_return_order_list, container, false);

        context = activity = getActivity();

        init(view);

        return  view;
    }

    private void init(View view)
    {
        modelArrayList = new ArrayList<>();
        returnOrderArrayList = new ArrayList<>();

        error_tv = view.findViewById(R.id.error_tv);

        recycler_view = view.findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);

        setDummyData();
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

        adapter = new ReturnOrderListAdapter(modelArrayList,context,this);
        recycler_view.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

    }

    @Override
    public void getErrorResponce(String error, int service_ID) {

    }

    @Override
    public void itemClick(int parantPosition, int chiledPosition, String action)
    {

        if(action.equalsIgnoreCase("orderDetails"))
        {
            showOrderDetailsBottomDialog();
        }
        else if(action.equalsIgnoreCase("orderSummary"))
        {
            Intent intent = new Intent(activity, ReturnOrderSummaryScreenActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0,0);
        }
    }

    // Return Order Details Popup and Show Order Details Item List.
    private void showOrderDetailsBottomDialog()
    {
        dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog.setContentView(R.layout.dialog_show_order_details_list);

        recycler_view_order_details = dialog.findViewById(R.id.recycler_view);

        TextView textView2 = dialog.findViewById(R.id.textView2);

        textView2.setText(getResources().getString(R.string.diamond_details));

        recycler_view_order_details.setHasFixedSize(true);
        //recycler_view.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_view_order_details.setLayoutManager(layoutManager);

        MyOrderListModel model = new MyOrderListModel();

        model.setOrderNumber("170245895326589");
        model.setOrderDateTime("06/03/24, 05:14:24 PM");
        model.setName("Eleganted Cushion");
        model.setStatus("Confirmed");
        model.setShape("Round");
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

        returnOrderArrayList.add(model);
        returnOrderArrayList.add(model);
        returnOrderArrayList.add(model);
        returnOrderArrayList.add(model);
        returnOrderArrayList.add(model);


        returnOrderDetailsListAdapter = new ReturnOrderDetailsListAdapter(returnOrderArrayList, context, this);
        recycler_view_order_details.setAdapter(returnOrderDetailsListAdapter);
        returnOrderDetailsListAdapter.notifyDataSetChanged();

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