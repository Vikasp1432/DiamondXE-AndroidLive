package com.diamondxe.Activity.MyOrder;

import static com.diamondxe.ApiCalling.ApiConstants.FOR_BASE64_IMAGE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Activity.SignupScreenActivity;
import com.diamondxe.Activity.TransparentActivity;
import com.diamondxe.Adapter.MyOrder.CancelReasonOrderListAdapter;
import com.diamondxe.Adapter.MyOrder.ReturnItemOrderListAdapter;
import com.diamondxe.Adapter.MyOrder.ReturnOrderItemImageVideoListAdapter;
import com.diamondxe.Adapter.MyOrder.ReturnOrderPickupAddressListAdapter;
import com.diamondxe.Adapter.MyOrder.ReturnReasonOrderListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.AddressListModel;
import com.diamondxe.Beans.Dealer.DealerMarkupListModel;
import com.diamondxe.Beans.MyOrder.CancelOrderReasonListModel;
import com.diamondxe.Beans.MyOrder.MyOrderListModel;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.MultipartUtility;
import com.diamondxe.Utils.Permission;
import com.diamondxe.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ReturnOrderItemsImageVideoActivity extends SuperActivity implements TwoRecyclerInterface {
    private ImageView back_img,  return_drop_arrow_img, drop_arrow_img;
    private RecyclerView recycler_view;
    private CardView return_product_card_view, return_reason_card_view, order_review_card_view, return_order_summary_view_card;
    private RelativeLayout coupon_code_value_rel, wallet_apply_point_rel, rel_other_tax,rel_diamond_tax, return_wallet_apply_point_rel;
    private TextView previous_tv, confirm_tv,  coupon_code_value_tv, diamond_price_tv, shipping_and_handling_tv,platform_fees_tv, total_charges_tv,other_taxes_tv, diamond_taxes_tv,
            total_taxes_tv, sub_total_tv,wallet_apply_charges_tv, bank_charges_tv,final_amount_tv,others_txt_gst_perc_tv,diamond_txt_gst_perc_tv, final_amount_tv1, order_number_tv, date_time_tv, utr_cheque_no_tv,order_status_tv,
            amount_tv,order_place_date_tv, delivery_date_tv,payment_mode_tv,shipping_address_tv, billing_address_tv,contact_no_tv, email_tv,
            return_diamond_price_tv, return_sub_total_tv,  return_wallet_apply_charges_tv, return_final_amount_tv, return_final_amount_tv1;
    private RadioButton wallet_radio, original_payment_radio;
    private RadioGroup payment_radio_group;
    private CardView cancel_order_card_view, processed_order_card_view,  order_summary_view_card_main;
    private LinearLayout view_order_summary_details_main_lin, order_summery_main_lin, return_view_order_summary_details_lin;
    private Activity activity;
    private Context context;
    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    Handler handler1 = new Handler(Looper.getMainLooper());
    String selectedCurrencyValue ="",selectedCurrencyCode = "",selectedCurrencyDesc="",selectedCurrencyImage="";
    String detailsOrderId = "", detailsCreatedAt="", mediaPickType="";
    private boolean isArrowDown = false;
    private boolean isArrowDownReturn = false;
    StringBuilder selectedIds = new StringBuilder();
    boolean isSelectShippingAddress = false;
    int lastPosition = 0;
    private View lastClickedView; // Global variable to store the last clicked view
    private ArrayList<MyOrderListModel> modelArrayList;
    private ReturnOrderItemImageVideoListAdapter adapter;
    private ArrayList<CancelOrderReasonListModel> cancelReasonArrayList;
    ReturnReasonOrderListAdapter returnReasonOrderListAdapter;
    private final String[] STORAGE_PERMS = {Manifest.permission.CAMERA};
    private final int INITIAL_REQUEST = 250;
    private final int STORAGE_REQUEST = INITIAL_REQUEST + 3;
    //For Pick image from Gallery or camera
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_order_items_image_video);

        context = activity = this;

        modelArrayList = new ArrayList<>();
        cancelReasonArrayList = new ArrayList<>();
        Constant.finalReturnOrderItemArrayList = new ArrayList<>();

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        return_product_card_view = findViewById(R.id.return_product_card_view);
        return_product_card_view.setOnClickListener(this);

        return_reason_card_view = findViewById(R.id.return_reason_card_view);
        return_reason_card_view.setOnClickListener(this);

        order_review_card_view = findViewById(R.id.order_review_card_view);
        order_review_card_view.setOnClickListener(this);

        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);

        previous_tv = findViewById(R.id.previous_tv);
        previous_tv.setOnClickListener(this);

        confirm_tv = findViewById(R.id.confirm_tv);
        confirm_tv.setOnClickListener(this);

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

        date_time_tv = findViewById(R.id.date_time_tv);
        order_number_tv = findViewById(R.id.order_number_tv);

        cancel_order_card_view = findViewById(R.id.cancel_order_card_view);
        processed_order_card_view = findViewById(R.id.processed_order_card_view);

        order_summary_view_card_main = findViewById(R.id.order_summary_view_card_main);
        order_summary_view_card_main.setOnClickListener(this);

        drop_arrow_img = findViewById(R.id.drop_arrow_img);
        final_amount_tv1 = findViewById(R.id.final_amount_tv1);
        view_order_summary_details_main_lin = findViewById(R.id.view_order_summary_details_main_lin);

        //------------------------------Pickup Address-------------------------------------------------------

        payment_radio_group = findViewById(R.id.payment_radio_group);
        wallet_radio = findViewById(R.id.wallet_radio);
        original_payment_radio = findViewById(R.id.original_payment_radio);

        //------------------------------My Order Summary ID'S Start----------------------------------------------

        order_summery_main_lin = findViewById(R.id.order_summery_main_lin);

        coupon_code_value_rel = findViewById(R.id.coupon_code_value_rel);
        wallet_apply_point_rel = findViewById(R.id.wallet_apply_point_rel);

        rel_other_tax = findViewById(R.id.rel_other_tax);
        rel_other_tax.setOnClickListener(this);

        rel_diamond_tax = findViewById(R.id.rel_diamond_tax);
        rel_diamond_tax.setOnClickListener(this);

        coupon_code_value_tv = findViewById(R.id.coupon_code_value_tv);
        diamond_price_tv = findViewById(R.id.diamond_price_tv);
        shipping_and_handling_tv = findViewById(R.id.shipping_and_handling_tv);
        platform_fees_tv = findViewById(R.id.platform_fees_tv);
        total_charges_tv = findViewById(R.id.total_charges_tv);
        other_taxes_tv = findViewById(R.id.other_taxes_tv);
        diamond_taxes_tv = findViewById(R.id.diamond_taxes_tv);
        total_taxes_tv = findViewById(R.id.total_taxes_tv);
        sub_total_tv = findViewById(R.id.sub_total_tv);
        wallet_apply_charges_tv = findViewById(R.id.wallet_apply_charges_tv);
        bank_charges_tv = findViewById(R.id.bank_charges_tv);
        final_amount_tv = findViewById(R.id.final_amount_tv);
        others_txt_gst_perc_tv = findViewById(R.id.others_txt_gst_perc_tv);
        diamond_txt_gst_perc_tv = findViewById(R.id.diamond_txt_gst_perc_tv);

        //------------------------------My Order Summary ID'S End----------------------------------------------

        //----------------------Return Order Summary ID's Start-----------------------------------------------

        return_order_summary_view_card = findViewById(R.id.return_order_summary_view_card);
        return_order_summary_view_card.setOnClickListener(this);

        return_drop_arrow_img = findViewById(R.id.return_drop_arrow_img);
        return_diamond_price_tv = findViewById(R.id.return_diamond_price_tv);
        return_sub_total_tv = findViewById(R.id.return_sub_total_tv);
        return_wallet_apply_point_rel = findViewById(R.id.return_wallet_apply_point_rel);
        return_wallet_apply_charges_tv = findViewById(R.id.return_wallet_apply_charges_tv);

        return_final_amount_tv = findViewById(R.id.return_final_amount_tv);
        return_final_amount_tv1 = findViewById(R.id.return_final_amount_tv1);

        return_view_order_summary_details_lin = findViewById(R.id.return_view_order_summary_details_lin);

        //----------------------Return Order Summary ID's End-------------------------------------------------

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setNestedScrollingEnabled(false);


        Constant.refundPaymentMode = "Wallet"; // By default Wallet
        payment_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.wallet_radio)
                {
                    Constant.refundPaymentMode = "Wallet";

                } else if (checkedId == R.id.original_payment_radio)
                {
                    Constant.refundPaymentMode = "Source";
                }
            }
        });

        getCurrencyData();

        getOrderCancelPartialAPI(false);
        getOrderCancelReasonAPI(true);

        //-------------------------------Start Updated Gallery and Camera Code---------------------------------------------
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            onSelectFromGalleryResult(data);
                        }
                    }
                });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            //onCaptureImageResult(data);
                            onCaptureImageResult(data);
                        }
                    }
                });
        //-------------------------------End Updated Gallery and Camera COde-----------------------------------------------

    }

    void getCurrencyData()
    {
        selectedCurrencyValue = CommonUtility.getGlobalString(context, "selected_currency_value");
        selectedCurrencyCode = CommonUtility.getGlobalString(context, "selected_currency_code");
        selectedCurrencyDesc = CommonUtility.getGlobalString(context, "selected_currency_desc");
        selectedCurrencyImage = CommonUtility.getGlobalString(context, "selected_currency_image");
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
        else if(id == R.id.return_product_card_view)
        {
            Utils.hideKeyboard(activity);
            moveToPreviousScreen();
        }
        else if(id == R.id.return_reason_card_view)
        {
            Utils.hideKeyboard(activity);

        }
        else if(id == R.id.order_review_card_view)
        {
            Utils.hideKeyboard(activity);
            moveToPickupAddressScreen();
        }
        else if(id == R.id.previous_tv)
        {
            Utils.hideKeyboard(activity);
            moveToPreviousScreen();
        }
        else if(id == R.id.confirm_tv)
        {
            Utils.hideKeyboard(activity);

            moveToPickupAddressScreen();
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
        else if(id == R.id.order_summary_view_card_main)
        {
            Utils.hideKeyboard(activity);
            if (isArrowDown) {
                drop_arrow_img.setImageResource(R.drawable.down);
                view_order_summary_details_main_lin.setVisibility(View.GONE);
            } else {
                drop_arrow_img.setImageResource(R.drawable.up);
                view_order_summary_details_main_lin.setVisibility(View.VISIBLE);
            }
            isArrowDown = !isArrowDown;
        }
        else if(id == R.id.return_order_summary_view_card)
        {
            Utils.hideKeyboard(activity);
            if (isArrowDownReturn) {
                return_drop_arrow_img.setImageResource(R.drawable.down);
                return_view_order_summary_details_lin.setVisibility(View.GONE);
            } else {
                return_drop_arrow_img.setImageResource(R.drawable.up);
                return_view_order_summary_details_lin.setVisibility(View.VISIBLE);
            }
            isArrowDownReturn = !isArrowDownReturn;
        }
    }

    void moveToPickupAddressScreen()
    {
        if(modelArrayList!=null && modelArrayList.size()>0)
        {
            ArrayList<MyOrderListModel> updatedList = adapter.getUpdatedList();
            if (!validateReasons(updatedList)) {
                Toast.makeText(this, getResources().getString(R.string.all_reason_items), Toast.LENGTH_LONG).show();
            }
            else{
                Intent intent = new Intent(activity, ReturnOrderPickupAddressWalletScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }

        }else{}
    }
   void moveToPreviousScreen()
    {
        Intent intent = new Intent(activity, ReturnOrderScreenActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    private boolean validateReasons(ArrayList<MyOrderListModel> updatedList) {
        for (MyOrderListModel model : updatedList) {
            if (model.getSelectedReason() == null || model.getSelectedReason().isEmpty()) {
                return false; // A reason is missing
            }
        }
        return true; // All items have reasons
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getOrderCancelPartialAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("orderId", "" + Constant.orderID);

            Log.e("Constant.orderReturnType : " , Constant.orderReturnType.toString());
            Log.e("Constant.orderReturnType1 : " , Constant.returnOrderCertificateIDs.toString());

            if(Constant.orderReturnType.equalsIgnoreCase("partial"))
            {
                urlParameter.put("type", "" + "partial");
                urlParameter.put("diamonds", "" + Constant.returnOrderCertificateIDs); // Pass Certificate Value "," Separated.
            }
            else{
                urlParameter.put("type", "" + "full");
                urlParameter.put("diamonds", "" + "");
            }

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.RETURN_ORDER_CHECKOUT, ApiConstants.RETURN_ORDER_PARTIAL_CHECKOUT_ID,showLoader,
                    "POST");
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void getOrderCancelReasonAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.ORDER_CANCEL_REASON, ApiConstants.ORDER_CANCEL_REASON_ID,showLoader,
                    "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {
        try {
            Log.v("------Diamond----- : ", "--------JSONObjectReturn-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {

                case ApiConstants.RETURN_ORDER_PARTIAL_CHECKOUT_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        detailsOrderId = CommonUtility.checkString(jObjDetails.optString("order_id"));
                        detailsCreatedAt = CommonUtility.checkString(jObjDetails.optString("created_at"));

                        String payment_mode = CommonUtility.checkString(jObjDetails.optString("payment_mode"));
                        String transaction_id = CommonUtility.checkString(jObjDetails.optString("transaction_id"));
                        String order_status = CommonUtility.checkString(jObjDetails.optString("order_status"));
                        String delivery_date = CommonUtility.checkString(jObjDetails.optString("delivery_date"));

                        if(!detailsCreatedAt.equalsIgnoreCase(""))
                        {
                            detailsCreatedAt = CommonUtility.convertDateTimeIntoLocal(detailsCreatedAt, ApiConstants.DATE_FORMAT, "dd/MM/yyyy, hh:mm:ss a");

                        } else{}

                        order_number_tv.setText("#" + detailsOrderId);
                        date_time_tv.setText(detailsCreatedAt);

                        String sub_total = CommonUtility.checkString(jObjDetails.optString("sub_total"));
                        String tax = CommonUtility.checkString(jObjDetails.optString("tax"));
                        String shipping_charge = CommonUtility.checkString(jObjDetails.optString("shipping_charge"));
                        String platform_fee = CommonUtility.checkString(jObjDetails.optString("platform_fee"));
                        String bank_charge = CommonUtility.checkString(jObjDetails.optString("bank_charge"));
                        String total_charge = CommonUtility.checkString(jObjDetails.optString("total_charge"));
                        String total_charge_tax = CommonUtility.checkString(jObjDetails.optString("total_charge_tax"));
                        String is_coupon_applied = CommonUtility.checkString(jObjDetails.optString("is_coupon_applied"));
                        String coupon_discount = CommonUtility.checkString(jObjDetails.optString("coupon_discount"));
                        String final_amount = CommonUtility.checkString(jObjDetails.optString("final_amount"));
                        String wallet_points = CommonUtility.checkString(jObjDetails.optString("wallet_points"));
                        String total_amount = CommonUtility.checkString(jObjDetails.optString("total_amount"));
                        String total_taxes = CommonUtility.checkString(jObjDetails.optString("total_taxes"));


                        if(sub_total!=null && !sub_total.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, sub_total);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            final_amount_tv1.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                            diamond_price_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                            amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(is_coupon_applied!=null && is_coupon_applied.equalsIgnoreCase("1"))
                        {
                            coupon_code_value_rel.setVisibility(View.VISIBLE);
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, coupon_discount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            coupon_code_value_tv.setText("-" + getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(shipping_charge!=null && !shipping_charge.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, shipping_charge);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            shipping_and_handling_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(platform_fee!=null && !platform_fee.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, platform_fee);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            platform_fees_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(total_charge!=null && !total_charge.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, total_charge);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            total_charges_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(total_charge_tax!=null && !total_charge_tax.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, total_charge_tax);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            other_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(tax!=null && !tax.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, tax);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            diamond_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(total_taxes!=null && !total_taxes.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, total_taxes);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            total_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(total_amount!=null && !total_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, total_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            sub_total_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(wallet_points!=null && !wallet_points.equalsIgnoreCase("") && !wallet_points.equalsIgnoreCase("0"))
                        {
                            wallet_apply_point_rel.setVisibility(View.VISIBLE);
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, wallet_points);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            wallet_apply_charges_tv.setText("-" + getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(bank_charge!=null && !bank_charge.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, bank_charge);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            bank_charges_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(final_amount!=null && !final_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, final_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            final_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        //-------------------------------------Return Order Summary-------------------------------------------------
                        JSONObject jObjCancelOrderDetails = jObjDetails.optJSONObject("return_order_summery");

                        String return_sub_total = CommonUtility.checkString(jObjCancelOrderDetails.optString("sub_total"));
                        String return_coupon_discount = CommonUtility.checkString(jObjCancelOrderDetails.optString("coupon_discount"));
                        String return_total_amount = CommonUtility.checkString(jObjCancelOrderDetails.optString("total_amount"));
                        String return_wallet_points = CommonUtility.checkString(jObjCancelOrderDetails.optString("wallet_points"));
                        String return_final_amount = CommonUtility.checkString(jObjCancelOrderDetails.optString("final_amount"));

                        if(return_sub_total!=null && !return_sub_total.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_sub_total);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            return_final_amount_tv1.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                            return_diamond_price_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        /*if(return_coupon_discount!=null && !return_coupon_discount.equalsIgnoreCase("") && !return_coupon_discount.equalsIgnoreCase("0"))
                        {
                            cancel_coupon_code_value_rel.setVisibility(View.VISIBLE);
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_coupon_discount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            cancel_coupon_code_value_tv.setText("-" + getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}*/

                        if(return_total_amount!=null && !return_total_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_total_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            return_sub_total_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(return_wallet_points!=null && !return_wallet_points.equalsIgnoreCase("") && !return_wallet_points.equalsIgnoreCase("0"))
                        {
                            return_wallet_apply_point_rel.setVisibility(View.VISIBLE);
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_wallet_points);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            return_wallet_apply_charges_tv.setText("-" + getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        if(return_final_amount!=null && !return_final_amount.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, return_final_amount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            return_final_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        // User Details Data Fetch
                        JSONObject jObjUserDetails = jObjDetails.optJSONObject("user_details");

                        String billing_address = CommonUtility.checkString(jObjUserDetails.optString("billing_address"));
                        String shipping_address = CommonUtility.checkString(jObjUserDetails.optString("shipping_address"));
                        String user_email = CommonUtility.checkString(jObjUserDetails.optString("user_email"));
                        String user_mobile = CommonUtility.checkString(jObjUserDetails.optString("user_mobile"));

                        utr_cheque_no_tv.setText(transaction_id);

                        if(order_status.equalsIgnoreCase("Delivered") || order_status.equalsIgnoreCase("delivered"))
                        {
                            order_status_tv.setTextColor(ContextCompat.getColor(context, R.color.green));
                        }
                        else{
                            order_status_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                        }
                        order_status_tv.setText(order_status);

                        order_place_date_tv.setText(detailsCreatedAt);
                        delivery_date_tv.setText(delivery_date);
                        payment_mode_tv.setText(payment_mode);
                        shipping_address_tv.setText(shipping_address);
                        billing_address_tv.setText(billing_address);
                        contact_no_tv.setText(getResources().getString(R.string.contact_no) + " " +user_mobile);
                        email_tv.setText(getResources().getString(R.string.email_lbl) + " " +user_email);

                        JSONArray details = jObjDetails.getJSONArray("diamonds");

                        if(modelArrayList!=null && modelArrayList.size()>0)
                        {
                            modelArrayList.clear();
                        }else{}

                        if(Constant.finalReturnOrderItemArrayList!=null && Constant.finalReturnOrderItemArrayList.size()>0)
                        {
                            Constant.finalReturnOrderItemArrayList.clear();
                        }else{}

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject jOBJNEW = details.getJSONObject(i);

                            MyOrderListModel model = new MyOrderListModel();

                            model.setOrderNumber(CommonUtility.checkString(detailsOrderId));
                            model.setOrderDateTime(CommonUtility.checkString(detailsCreatedAt));
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
                            model.setStatus(CommonUtility.checkString(jOBJNEW.optString("status")));
                            model.setCurrencySymbol(ApiConstants.rupeesIcon);
                            model.setChecked(false);
                            model.setSelectedReason("");
                            model.setWriteMessage("");
                            model.setReturnOrderImage("");
                            model.setReturnOrderVideo("");
                            model.setReturnOrderVideoUrl("");

                            modelArrayList.add(model);
                            Constant.finalReturnOrderItemArrayList.add(model);
                        }

                        adapter = new ReturnOrderItemImageVideoListAdapter(modelArrayList,context,this);
                        recycler_view.setAdapter(adapter);
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

                case ApiConstants.ORDER_CANCEL_REASON_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if(cancelReasonArrayList!=null && cancelReasonArrayList.size()>0)
                        {
                            cancelReasonArrayList.clear();
                        }else{}

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            CancelOrderReasonListModel model = new CancelOrderReasonListModel();
                            model.setId(CommonUtility.checkString(objectCodes.optString("id")));
                            model.setReason(CommonUtility.checkString(objectCodes.optString("reason")));
                            cancelReasonArrayList.add(model);
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getErrorResponce(String error, int service_ID) {

    }

    @Override
    public void itemClick(int parantPosition, int chiledPosition, String action)
    {
        if(action.equalsIgnoreCase("reasonType"))
        {
            Utils.hideKeyboard(activity);
            reasonTypePopupWindow(lastClickedView, parantPosition);
        }
        else if(action.equalsIgnoreCase("reason"))
        {
            // Assuming you want to select the reason for the first model if it exists
            /*if (modelArrayList.size() > 0) {
                // Adjusted logic to prevent IndexOutOfBoundsException
                String selectedReason = cancelReasonArrayList.get(parantPosition).getReason();
                mDropdown.dismiss();
                // Always set the reason for the first model or a specific index
                modelArrayList.get(parantPosition).setSelectedReason(selectedReason); // or adjust as necessary
                adapter.notifyDataSetChanged();
            } else {
                Log.e("Error", "Model list is empty.");
            }*/

            // Check if the parent position and child position are within valid bounds
            if (parantPosition >= 0 && parantPosition < modelArrayList.size() &&
                    chiledPosition >= 0 && chiledPosition < cancelReasonArrayList.size())
            {
                // Retrieve the reason corresponding to the selected child position
                String selectedReason = cancelReasonArrayList.get(chiledPosition).getReason();
                // Dismiss the dropdown menu
                mDropdown.dismiss();
                // Update the selected reason for the model item at the parent position

                modelArrayList.get(parantPosition).setSelectedReason(selectedReason); // Update the correct model item
                Constant.finalReturnOrderItemArrayList.get(parantPosition).setSelectedReason(selectedReason); // Update the correct model item


                // Notify the adapter that the data has changed so the view can refresh
                adapter.notifyDataSetChanged(); // Refresh the adapter
            } else {
                Log.e("Error", "Index out of bounds: parentPosition=" + parantPosition + ", childPosition=" + chiledPosition);
            }
        }
        else if(action.equalsIgnoreCase("selectImage"))
        {
            Utils.hideKeyboard(activity);
            if (parantPosition >= 0 && parantPosition < modelArrayList.size())
            {
                lastPosition = parantPosition;
                mediaPickType = "image";
                imageTakeOption();
            } else {
                Log.e("Error", "Index out of bounds: parentPosition=" + parantPosition + ", childPosition=" + chiledPosition);
            }
        }
        else if(action.equalsIgnoreCase("selectVideo"))
        {
            Utils.hideKeyboard(activity);
            if (parantPosition >= 0 && parantPosition < modelArrayList.size())
            {
                lastPosition = parantPosition;
                mediaPickType = "video";
                imageTakeOption();
            } else {
                Log.e("Error", "Index out of bounds: parentPosition=" + parantPosition + ", childPosition=" + chiledPosition);
            }
        }
    }

    // Update this method in the adapter
    public void setLastClickedView(View view) {
        this.lastClickedView = view;
    }

    private PopupWindow mDropdown = null;
    LayoutInflater mInflater;
    // Button pop;
    private TextView select_mode_lbl;
    private RecyclerView recycler_view_reason;
    private PopupWindow reasonTypePopupWindow(View anchor, int parantPosition) {
        try {
            if (mDropdown != null && mDropdown.isShowing()) {
                mDropdown.dismiss();
            }
            mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.custom_menu_reason_type, null);

            select_mode_lbl = layout.findViewById(R.id.select_mode_lbl);
            recycler_view_reason = layout.findViewById(R.id.recycler_view_reason);
            recycler_view_reason.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
            recycler_view_reason.setLayoutManager(layoutManager);
            recycler_view_reason.setNestedScrollingEnabled(false);

            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);

            // Ensure pop is not null before using it
            if (anchor != null) {
                mDropdown.showAsDropDown(anchor, 5, -50);
            } else {
                Log.e("PopupWindow", "pop is null");
            }

            select_mode_lbl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            setAdapter(parantPosition);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;
    }

    void setAdapter(int parentPosition)
    {
        returnReasonOrderListAdapter = new ReturnReasonOrderListAdapter(cancelReasonArrayList,context,this, parentPosition);
        recycler_view_reason.setAdapter(returnReasonOrderListAdapter);
        returnReasonOrderListAdapter.notifyDataSetChanged();
    }

    void imageTakeOption()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (!canAccessCamera()) {
                requestPermissions(STORAGE_PERMS, STORAGE_REQUEST);
            } else {
                if(mediaPickType.equalsIgnoreCase("video"))
                {
                    selectVideo();
                }
                else{
                    selectImage();
                }
            }
        } else {
            if(mediaPickType.equalsIgnoreCase("video"))
            {
                selectVideo();
            }
            else{
                selectImage();
            }
        }
    }

    private boolean canAccessCamera() {
        return ((hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                && (hasPermission(Manifest.permission.CAMERA)));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity, perm));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_REQUEST) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                if(mediaPickType.equalsIgnoreCase("video"))
                {
                    selectVideo();
                }
                else{
                    selectImage();
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(activity, "Camera Permission Not Granted. Please enable it in settings.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, "Permission Not Granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //For Select Image option Dialog
    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Permission.checkPermission(activity);

                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Gallery")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    private void selectVideo()
    {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        galleryLauncher.launch(Intent.createChooser(intent, "Select File"));

    }

    //For Gallery Intent
    private void galleryIntent() {
        if(mediaPickType.equalsIgnoreCase("image"))
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            galleryLauncher.launch(Intent.createChooser(intent, "Select File"));
        }
        else{
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            galleryLauncher.launch(Intent.createChooser(intent, "Select File"));
        }
    }

    //For Camera Intent
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent);
    }

    /*private void onSelectFromGalleryResult1(Intent data) {
        if (data != null) {
            try {
                // Get the URI of the selected image
                Uri uri = data.getData();
                String mimeType = getContentResolver().getType(uri);

                if (mimeType != null) {
                    if (mimeType.startsWith("image/")) {
                        // Handle image
                        //handleImage(uri);
                    } else if (mimeType.startsWith("video/")) {
                        // Handle video
                       // handleVideo(uri);
                    } else {
                        Log.e("onSelectFromGalleryResult", "Unsupported MIME type: " + mimeType);
                    }
                }else{}

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{}

    }*/

    //---------------------------------------Start Camera Gallery image Handle and Save------------------------------------------------
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                // check User Upload Image or Video
                if(mediaPickType.equalsIgnoreCase("video"))
                {
                    // Get the URI of the selected video
                    Uri uri = data.getData();

                    // Log the URI for debugging
                    //Log.e("Selected Video URI: ", uri.toString());

                    // Get the path of the video from the URI
                    String videoPath = getPathFromUri(uri);

                    // Save the video to your app's storage
                    String savedVideoPath = saveVideoToFile(uri, "selectedVideo.mp4");

                    // Log the saved video path for debugging
                   // Log.e("------savedVideoPath----- : ", savedVideoPath);
                    modelArrayList.get(lastPosition).setReturnOrderVideo(savedVideoPath);
                    Constant.finalReturnOrderItemArrayList.get(lastPosition).setReturnOrderVideo(savedVideoPath);
                    adapter.notifyDataSetChanged();

                }
                else{
                    // Get the URI of the selected image
                    Uri uri = data.getData();

                    // Get original image size
                    /*InputStream inputStream = getContentResolver().openInputStream(uri);
                    int originalSizeInBytes = inputStream.available();
                    float originalSizeInMB = originalSizeInBytes / (1024f * 1024f); // Convert to MB*/

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    // Resize the bitmap if necessary
                    bitmap = getResizedBitmap(bitmap, 800, 800); // Resize to a max of 800x800 pixels

                    // Get the path of the image from the URI using your utility method
                    String picturePath = saveBitmapToFileGalley(bitmap, "selectedImage.png");


                    // Get compressed image size
                    /*File compressedFile = new File(picturePath);
                    long compressedSizeInBytes = compressedFile.length();
                    float compressedSizeInMB = compressedSizeInBytes / (1024f * 1024f); // Convert to MB

                    // Print original and compressed sizes
                    Log.d("Image Sizes", "Original Size: " + originalSizeInMB + " MB");
                    Log.d("Image Sizes", "Compressed Size: " + compressedSizeInMB + " MB");*/

                    // Log the picture path for debugging
                    //Log.e("------picturePath----- : ", picturePath);
                    modelArrayList.get(lastPosition).setReturnOrderImage(picturePath);
                    Constant.finalReturnOrderItemArrayList.get(lastPosition).setReturnOrderImage(picturePath);
                    adapter.notifyDataSetChanged();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // For Gallery Image
    private String saveBitmapToFileGalley(Bitmap bitmap, String fileName) {
        FileOutputStream fos = null;
        String path = null;

        try {
            // Create a file in the external storage directory
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
            fos = new FileOutputStream(file);

            // Compress the bitmap and save it to the file
            // Adjust quality as needed (0-100)
            int quality = 60; // Adjust this value as needed for your use case
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos); // Save as JPEG
            fos.flush();

            /*bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); // Save as JPEG
            fos.flush();*/

            path = file.getAbsolutePath(); // Get the file path
        } catch (IOException e) {
            Log.e("ImageSave", "Error saving image", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e("ImageSave", "Error closing stream", e);
                }
            }
        }

        return path; // Return the file path
    }

    private Bitmap getResizedBitmap(Bitmap originalBitmap, int maxWidth, int maxHeight) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();

        if (width > maxWidth || height > maxHeight) {
            float aspectRatio = (float) width / (float) height;
            if (width > height) {
                width = maxWidth;
                height = Math.round(maxWidth / aspectRatio);
            } else {
                height = maxHeight;
                width = Math.round(maxHeight * aspectRatio);
            }
        }

        return Bitmap.createScaledBitmap(originalBitmap, width, height, true);
    }

    // For Video
    private String saveVideoToFile(Uri videoUri, String fileName)
    {
        String path = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            // Create a file in the external storage directory
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES), fileName);
            inputStream = getContentResolver().openInputStream(videoUri);
            outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            path = file.getAbsolutePath(); // Get the file path
        } catch (IOException e) {
            Log.e("VideoSave", "Error saving video", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("VideoSave", "Error closing input stream", e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.e("VideoSave", "Error closing output stream", e);
                }
            }
        }

        return path; // Return the file path
    }

    // Method to get the actual file path from the URI
    private String getPathFromUri(Uri uri) {
        String path = null;
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
            cursor.close();
        }
        return path;
    }

    private void onCaptureImageResult(Intent data)
    {
        // Get the captured image bitmap from the intent
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");

        // Resize the bitmap if necessary (optional)
        imageBitmap = getResizedCameraBitmap(imageBitmap, 800, 800); // Resize to a max of 800x800 pixels

        // Save the bitmap to a file and get the path
        String imagePath = saveBitmapToFile(imageBitmap, "myImage.jpg");

        // Now you can use the imagePath for further operations

        Log.e("picturePath", "-----cameraPicturePath------- : " + imagePath);
        modelArrayList.get(lastPosition).setReturnOrderImage(imagePath);
        Constant.finalReturnOrderItemArrayList.get(lastPosition).setReturnOrderImage(imagePath);
        adapter.notifyDataSetChanged();

    }
    private String saveBitmapToFile(Bitmap bitmap, String fileName)
    {
        FileOutputStream fos = null;
        String path = null;

        try {
            // Create a file in the external storage directory
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
            fos = new FileOutputStream(file);

            // Compress the bitmap and save it to the file
            int compressionQuality = 60; // Adjust this value for desired quality
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressionQuality, fos); // Save as JPEG
            fos.flush();
            path = file.getAbsolutePath(); // Get the file path

           /* bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // Save as PNG
            fos.flush();
            path = file.getAbsolutePath(); // Get the file path*/

        } catch (IOException e) {
            Log.e("ImageSave", "Error saving image", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e("ImageSave", "Error closing stream", e);
                }
            }
        }
        return path; // Return the file path
    }

    private Bitmap getResizedCameraBitmap(Bitmap originalBitmap, int maxWidth, int maxHeight) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();

        if (width > maxWidth || height > maxHeight) {
            float aspectRatio = (float) width / (float) height;
            if (width > height) {
                width = maxWidth;
                height = Math.round(maxWidth / aspectRatio);
            } else {
                height = maxHeight;
                width = Math.round(maxHeight * aspectRatio);
            }
        }

        return Bitmap.createScaledBitmap(originalBitmap, width, height, true);
    }

    //---------------------------------------End Camera Gallery image Handle and Save------------------------------------------------


}