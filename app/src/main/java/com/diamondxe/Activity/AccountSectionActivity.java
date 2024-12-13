package com.diamondxe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.diamondxe.Activity.APISolutions.ApiSolutionRequestActivity;
import com.diamondxe.Activity.Buyer.BuyerKYCVerificationActivity;
import com.diamondxe.Activity.Buyer.BuyerKYCVerificationDocUploadActivity;
import com.diamondxe.Activity.Dealer.CustomPaymentScreenActivity;
import com.diamondxe.Activity.Dealer.DealerMarkupScreenActivity;
import com.diamondxe.Activity.Dealer.KYCVerificationActivity;
import com.diamondxe.Activity.Dealer.WalletScreenActivity;
import com.diamondxe.Activity.MyOrder.MyOrderListScreenActivity;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;

public class AccountSectionActivity extends SuperActivity implements RecyclerInterface {

    private RelativeLayout my_account_rel,my_order_rel, wallet_rel, auction_rel, dealer_mark_up_rel, refer_a_friend_rel, loyalty_program_rel, custom_payment_rel,
            api_solution_rel, delete_account_rel, profile_rel, address_rel, kyc_verification_rel, change_password_rel;
    private LinearLayout my_account_sub_section;
    private ImageView back_img, drop_down_my_account, drop_down_my_order, drop_down_wallet, drop_down_auction,drop_down_mark_up, drop_down_refer, drop_down_loyalty,
            drop_down_payment,drop_down_api,drop_down_delete, img10;
    private TextView title_tv,my_delete_tv;
    String document_status = "",user_login="";

    private boolean isArrowDown = false;

    private Activity activity;
    private Context context;

    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;

    String userRole = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_section);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        context = activity = this;

        userRole = CommonUtility.getGlobalString(activity, "login_user_role");

        title_tv = findViewById(R.id.title_tv);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        my_account_rel = findViewById(R.id.my_account_rel);
        my_account_rel.setOnClickListener(this);

        my_order_rel = findViewById(R.id.my_order_rel);
        my_order_rel.setOnClickListener(this);

        wallet_rel = findViewById(R.id.wallet_rel);
        wallet_rel.setOnClickListener(this);

        auction_rel = findViewById(R.id.auction_rel);
        auction_rel.setOnClickListener(this);

        dealer_mark_up_rel = findViewById(R.id.dealer_mark_up_rel);
        dealer_mark_up_rel.setOnClickListener(this);

        refer_a_friend_rel = findViewById(R.id.refer_a_friend_rel);
        refer_a_friend_rel.setOnClickListener(this);

        loyalty_program_rel = findViewById(R.id.loyalty_program_rel);
        loyalty_program_rel.setOnClickListener(this);

        custom_payment_rel = findViewById(R.id.custom_payment_rel);
        custom_payment_rel.setOnClickListener(this);

        api_solution_rel = findViewById(R.id.api_solution_rel);
        api_solution_rel.setOnClickListener(this);

        delete_account_rel = findViewById(R.id.delete_account_rel);
        delete_account_rel.setOnClickListener(this);

        profile_rel = findViewById(R.id.profile_rel);
        profile_rel.setOnClickListener(this);

        address_rel = findViewById(R.id.address_rel);
        address_rel.setOnClickListener(this);

        kyc_verification_rel = findViewById(R.id.kyc_verification_rel);
        kyc_verification_rel.setOnClickListener(this);

        change_password_rel = findViewById(R.id.change_password_rel);
        change_password_rel.setOnClickListener(this);

        my_delete_tv = findViewById(R.id.my_delete_tv);
        img10 = findViewById(R.id.img10);

        my_account_sub_section = findViewById(R.id.my_account_sub_section);
        drop_down_my_account = findViewById(R.id.drop_down_my_account);
        drop_down_my_order = findViewById(R.id.drop_down_my_order);
        drop_down_wallet = findViewById(R.id.drop_down_wallet);
        drop_down_auction = findViewById(R.id.drop_down_auction);
        drop_down_mark_up = findViewById(R.id.drop_down_mark_up);
        drop_down_refer = findViewById(R.id.drop_down_refer);
        drop_down_loyalty = findViewById(R.id.drop_down_loyalty);
        drop_down_payment = findViewById(R.id.drop_down_payment);
        drop_down_api = findViewById(R.id.drop_down_api);
        drop_down_delete = findViewById(R.id.drop_down_delete);

        if(userRole.equalsIgnoreCase("DEALER"))
        {
            title_tv.setText(getResources().getString(R.string.dealer_account));
            dealer_mark_up_rel.setVisibility(View.VISIBLE);
            custom_payment_rel.setVisibility(View.VISIBLE);
            //wallet_rel.setVisibility(View.VISIBLE);
            /*custom_payment_rel.setVisibility(View.VISIBLE);*/
            api_solution_rel.setVisibility(View.VISIBLE);
        }
        else if(userRole.equalsIgnoreCase("BUYER")) {
            title_tv.setText(getResources().getString(R.string.buyer_account));
            dealer_mark_up_rel.setVisibility(View.GONE);
            custom_payment_rel.setVisibility(View.VISIBLE);
            //wallet_rel.setVisibility(View.GONE);
            /*dealer_mark_up_rel.setVisibility(View.GONE);
            custom_payment_rel.setVisibility(View.GONE);*/
            api_solution_rel.setVisibility(View.GONE);
        }
        else{
            title_tv.setText(getResources().getString(R.string.account));
        }

        user_login = CommonUtility.getGlobalString(activity, "user_login");

        if(user_login.equalsIgnoreCase("yes"))
        {
            delete_account_rel.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
            my_delete_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
            img10.setColorFilter(ContextCompat.getColor(context, R.color.purple_light));
        }
        else{
            delete_account_rel.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray1));
            img10.setColorFilter(ContextCompat.getColor(context, R.color.shimmer_color));
            my_delete_tv.setTextColor(ContextCompat.getColor(context, R.color.shimmer_color));
        }
    }

    @Override
    public void onClick(View view)
    {
        Intent intent;
        int id = view.getId();

        user_login = CommonUtility.getGlobalString(activity, "user_login");

        if(id == R.id.back_img)
        {
            Utils.hideKeyboard(activity);
            finish();
        }
        else if(id == R.id.my_account_rel)
        {
            Utils.hideKeyboard(activity);

            if (isArrowDown) {
                drop_down_my_account.setImageResource(R.drawable.down);
                my_account_sub_section.setVisibility(View.GONE);
            } else {
                drop_down_my_account.setImageResource(R.drawable.up);
                my_account_sub_section.setVisibility(View.VISIBLE);
            }
            isArrowDown = !isArrowDown;
        }
        else if(id == R.id.profile_rel)
        {
            Utils.hideKeyboard(activity);
            if(user_login.equalsIgnoreCase("yes"))
            {
                intent = new Intent(activity, PersonalProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
            else{
                finish();
            }

        }
        else if(id == R.id.address_rel)
        {
            Utils.hideKeyboard(activity);
            if(user_login.equalsIgnoreCase("yes"))
            {
                Constant.showRadioButtonForSelectAddress = ""; // Under Address List Radio Button Not Visible
                intent = new Intent(activity, ShippingBillingAddressListActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
            else{
                finish();
            }
        }
        else if(id == R.id.kyc_verification_rel)
        {
            Utils.hideKeyboard(activity);
            if(user_login.equalsIgnoreCase("yes"))
            {
                if(userRole.equalsIgnoreCase("DEALER"))
                {
                    intent = new Intent(activity, KYCVerificationActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
                else
                {
                    if(document_status.equalsIgnoreCase("0"))
                    {
                        intent = new Intent(activity, BuyerKYCVerificationDocUploadActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }
                    else{
                        intent = new Intent(activity, BuyerKYCVerificationActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }

                }

            }
            else{
                finish();
            }
        }
        else if(id == R.id.change_password_rel)
        {
            Utils.hideKeyboard(activity);
            if(user_login.equalsIgnoreCase("yes"))
            {
                intent = new Intent(activity, ChangePasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
            else{
                finish();
            }

        }
        else if(id == R.id.my_order_rel)
        {
            Utils.hideKeyboard(activity);
            if(user_login.equalsIgnoreCase("yes"))
            {
                Constant.comeFrom = "";
                Constant.afterCancelOrderManageScreenCall="";
                Constant.afterReturnOrderManageScreenCall = "";
                intent = new Intent(activity, MyOrderListScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
            else{
                finish();
            }
        }
        else if(id == R.id.wallet_rel)
        {
            Utils.hideKeyboard(activity);
            if(user_login.equalsIgnoreCase("yes"))
            {
                intent = new Intent(activity, WalletScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
            else{
                finish();
            }
        }
        else if(id == R.id.auction_rel)
        {
            Utils.hideKeyboard(activity);
            if(user_login.equalsIgnoreCase("yes"))
            {
            }
            else{
                finish();
            }
        }
        else if(id == R.id.dealer_mark_up_rel)
        {
            Utils.hideKeyboard(activity);
            if(user_login.equalsIgnoreCase("yes"))
            {
                intent = new Intent(activity, DealerMarkupScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
            else{
                finish();
            }
        }
        else if(id == R.id.refer_a_friend_rel)
        {
            Utils.hideKeyboard(activity);
            if(user_login.equalsIgnoreCase("yes"))
            {
            }
            else{
                finish();
            }
        }
        else if(id == R.id.loyalty_program_rel)
        {
            Utils.hideKeyboard(activity);
            if(user_login.equalsIgnoreCase("yes"))
            {
            }
            else{
                finish();
            }
        }
        else if(id == R.id.custom_payment_rel)
        {
            Utils.hideKeyboard(activity);
            if(user_login.equalsIgnoreCase("yes"))
            {
                intent = new Intent(activity, CustomPaymentScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
            else{
                finish();
            }
        }
        else if(id == R.id.api_solution_rel)
        {
            Utils.hideKeyboard(activity);
            if(user_login.equalsIgnoreCase("yes"))
            {
                intent = new Intent(activity, ApiSolutionRequestActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
            else{
                finish();
            }
        }
        else if(id == R.id.delete_account_rel)
        {
            Utils.hideKeyboard(activity);
            if(user_login.equalsIgnoreCase("yes"))
            {
                deleteAccountConfirmationDialogPopup(activity, context, getResources().getString(R.string.delete_account_msg));
            }
            else{
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        userRole = CommonUtility.getGlobalString(activity, "login_user_role");

        if(userRole.equalsIgnoreCase("BUYER"))
        {
            getKYCDetailsAPI(true);
        }else{}

    }

    public void getKYCDetailsAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_KYC_DETAILS, ApiConstants.GET_KYC_DETAILS_ID,showLoader, "GET");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    public void onDeleteAccountAPI(boolean showLoader)
    {
        //String uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String uuid = CommonUtility.getAndroidId(context);
        Log.e("-----uuid------", uuid);
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("deviceId", ""+ uuid);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.DELETE_ACCOUNT, ApiConstants.DELETE_ACCOUNT_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
        }
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

        try {
            Log.e("Diamonds : ", "--------JSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {
                case ApiConstants.GET_KYC_DETAILS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        document_status = jsonObjectData.optString("document_status");

                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        if(document_status.equalsIgnoreCase("0"))
                        {}
                        else if(document_status.equalsIgnoreCase("1"))
                        {}
                        else if(document_status.equalsIgnoreCase("2"))
                        {}
                        else if(document_status.equalsIgnoreCase("3"))
                        {}
                        else{}
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

                case ApiConstants.DELETE_ACCOUNT_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1")) {

                        Toast.makeText(AccountSectionActivity.this, "" + message, Toast.LENGTH_SHORT).show();

                        CommonUtility.clear(AccountSectionActivity.this);
                        CommonUtility.clearLocalArrayListData(AccountSectionActivity.this);
                        CommonUtility.setGlobalString(AccountSectionActivity.this, "uuid", "");
                        CommonUtility.setGlobalString(AccountSectionActivity.this, "user_id", "");
                        CommonUtility.setGlobalString(AccountSectionActivity.this, "mobile_auth_token", "");
                        CommonUtility.setGlobalString(AccountSectionActivity.this, "user_login", "");

                        Intent _login_intent = new Intent(AccountSectionActivity.this, HomeScreenActivity.class);
                        _login_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        _login_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(_login_intent);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                        logoutFromApp(context,message);
                    }

                    else {
                          Toast.makeText(AccountSectionActivity.this, "" + message, Toast.LENGTH_SHORT).show();
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

    void deleteAccountConfirmationDialogPopup(final Activity activity,final Context context,String message)
    {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_layout_logout, null);
        dialogBuilder.setView(dialogView);
        final android.app.AlertDialog alertDialog = dialogBuilder.create();

        final TextView title = dialogView.findViewById(R.id.title);
        final TextView message1 =  dialogView.findViewById(R.id.message);
        final TextView yes_tv =  dialogView.findViewById(R.id.yes_tv);
        final TextView no_tv =  dialogView.findViewById(R.id.no_tv);

        title.setText(""+context.getResources().getString(R.string.app_name));

        message1.setText(""+message);

        yes_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                deleteAccountConfirmationDialogPopupAgain(activity, context, getResources().getString(R.string.delete_account_msg1));
                alertDialog.dismiss();
            }
        });

        no_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(true);
        alertDialog.show();

    }

    void deleteAccountConfirmationDialogPopupAgain(final Activity activity,final Context context,String message)
    {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_layout_logout, null);
        dialogBuilder.setView(dialogView);
        final android.app.AlertDialog alertDialog = dialogBuilder.create();

        final TextView title = dialogView.findViewById(R.id.title);
        final TextView message1 =  dialogView.findViewById(R.id.message);
        final TextView yes_tv =  dialogView.findViewById(R.id.yes_tv);
        final TextView no_tv =  dialogView.findViewById(R.id.no_tv);

        title.setText(""+context.getResources().getString(R.string.app_name));

        message1.setText(""+message);

        yes_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onDeleteAccountAPI(false);
                alertDialog.dismiss();
            }
        });

        no_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(true);
        alertDialog.show();

    }
}