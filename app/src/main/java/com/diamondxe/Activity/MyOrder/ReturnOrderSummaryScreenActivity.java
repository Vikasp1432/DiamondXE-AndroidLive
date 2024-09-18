package com.diamondxe.Activity.MyOrder;

import static com.diamondxe.ApiCalling.ApiConstants.rupeesIcon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Adapter.MyOrder.CancelOrderListAdapter;
import com.diamondxe.Adapter.MyOrder.ReturnOrderSummaryListAdapter;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.MyOrder.MyOrderListModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ReturnOrderSummaryScreenActivity extends SuperActivity implements TwoRecyclerInterface {

    private ImageView back_img;
    private RelativeLayout coupon_code_value_rel,rel_other_tax, rel_diamond_tax, wallet_apply_point_rel;
    private TextView error_tv,return_diamond_price_tv, return_sub_total_tv, return_final_amount_tv, diamond_price_tv, coupon_code_value_tv,
            shipping_and_handling_tv,platform_fees_tv,total_charges_tv, other_taxes_tv, others_txt_gst_perc_tv, diamond_taxes_tv, diamond_txt_gst_perc_tv,
            total_taxes_tv, sub_total_tv, wallet_apply_charges_tv,bank_charges_tv, final_amount_tv, utr_cheque_no_tv,order_status_tv,
            amount_tv,order_place_date_tv, delivery_date_tv,payment_mode_tv,shipping_address_tv, billing_address_tv,contact_no_tv, email_tv;
    private RecyclerView recycler_view;
    private ArrayList<MyOrderListModel> modelArrayList;
    private ReturnOrderSummaryListAdapter adapter;
    private Activity activity;
    private Context context;
    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    Handler handler1 = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_order_summary_screen);

        context = activity = this;

        modelArrayList = new ArrayList<>();

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        error_tv = findViewById(R.id.error_tv);

        return_diamond_price_tv = findViewById(R.id.return_diamond_price_tv);
        return_sub_total_tv = findViewById(R.id.return_sub_total_tv);
        return_final_amount_tv = findViewById(R.id.return_final_amount_tv);

        coupon_code_value_rel = findViewById(R.id.coupon_code_value_rel);

        rel_other_tax = findViewById(R.id.rel_other_tax);
        rel_other_tax.setOnClickListener(this);

        rel_diamond_tax = findViewById(R.id.rel_diamond_tax);
        rel_diamond_tax.setOnClickListener(this);

        wallet_apply_point_rel = findViewById(R.id.wallet_apply_point_rel);

        diamond_price_tv = findViewById(R.id.diamond_price_tv);
        coupon_code_value_tv = findViewById(R.id.coupon_code_value_tv);
        shipping_and_handling_tv = findViewById(R.id.shipping_and_handling_tv);
        platform_fees_tv = findViewById(R.id.platform_fees_tv);
        total_charges_tv = findViewById(R.id.total_charges_tv);
        other_taxes_tv = findViewById(R.id.other_taxes_tv);
        others_txt_gst_perc_tv = findViewById(R.id.others_txt_gst_perc_tv);
        diamond_taxes_tv = findViewById(R.id.diamond_taxes_tv);
        diamond_txt_gst_perc_tv = findViewById(R.id.diamond_txt_gst_perc_tv);
        total_taxes_tv = findViewById(R.id.total_taxes_tv);
        sub_total_tv = findViewById(R.id.sub_total_tv);
        wallet_apply_charges_tv = findViewById(R.id.wallet_apply_charges_tv);
        bank_charges_tv = findViewById(R.id.bank_charges_tv);
        final_amount_tv = findViewById(R.id.final_amount_tv);

        utr_cheque_no_tv = findViewById(R.id.utr_cheque_no_tv);
        order_status_tv = findViewById(R.id.order_status_tv);
        amount_tv = findViewById(R.id.amount_tv);
        order_place_date_tv = findViewById(R.id.order_place_date_tv);
        delivery_date_tv = findViewById(R.id.delivery_date_tv);
        payment_mode_tv = findViewById(R.id.payment_mode_tv);
        shipping_address_tv = findViewById(R.id.shipping_address_tv);
        billing_address_tv = findViewById(R.id.billing_address_tv);
        contact_no_tv = findViewById(R.id.contact_no_tv);
        email_tv = findViewById(R.id.email_tv);

        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setNestedScrollingEnabled(false);

        setDummyData();

    }

    void setDummyData()
    {
        MyOrderListModel model = new MyOrderListModel();

        model.setOrderNumber("170245895326589");
        model.setOrderDateTime("06/03/24, 05:14:24 PM");
        model.setName("Eleganted Cushion");
        model.setStatus("Confirmed");
        model.setCarat("0.19Ct");
        model.setColor("E");
        model.setClarity("SI2");
        model.setStockNumber("6481931311");
        model.setCategory("LAB");
        model.setSubTotal("1877100000");
        model.setShowingSubTotal("1877100000");
        model.setCurrencySymbol(rupeesIcon);
        model.setImage("https://v360.in/V360Images.aspx?cid=vd&d=KG230-184A");
        model.setChecked(false);

        /*innerOrderArrayList = new ArrayList<InnerOrderListModel>();

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
        innerOrderListModel.setChecked(false);

        innerOrderArrayList.add(innerOrderListModel);

        model.setAllItemsInSection(innerOrderArrayList);*/

        modelArrayList.add(model);
        modelArrayList.add(model);
        modelArrayList.add(model);
        modelArrayList.add(model);
        modelArrayList.add(model);
        modelArrayList.add(model);

        // Set Adapter Value.
        adapter = new ReturnOrderSummaryListAdapter(modelArrayList,context,this, "fullOrderCancel");
        recycler_view.setAdapter(adapter);
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
        else if(id == R.id.rel_other_tax)
        {
            Utils.hideKeyboard(activity);
            others_txt_gst_perc_tv.setVisibility(View.VISIBLE);
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    others_txt_gst_perc_tv.setVisibility(View.GONE);
                }
            }, 2000);
        }
        else if(id == R.id.rel_diamond_tax)
        {
            Utils.hideKeyboard(activity);
            diamond_txt_gst_perc_tv.setVisibility(View.VISIBLE);
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    diamond_txt_gst_perc_tv.setVisibility(View.GONE);
                }
            }, 2000);
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
}