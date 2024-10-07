package com.diamondxe.Activity.Buyer;

import static com.diamondxe.ApiCalling.ApiConstants.AADHAARNo;
import static com.diamondxe.ApiCalling.ApiConstants.DRIVING_LICENSE;
import static com.diamondxe.ApiCalling.ApiConstants.FOR_BASE64_IMAGE;
import static com.diamondxe.ApiCalling.ApiConstants.GSTNo;
import static com.diamondxe.ApiCalling.ApiConstants.PANNo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.diamondxe.Activity.PlaceOrder.PlaceOrderBuyerKYCVerificationDocUploadActivity;
import com.diamondxe.Activity.TransparentActivity;
import com.diamondxe.Adapter.Dealer.KYCVerificationAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.KYCVerificationModel;
import com.diamondxe.Interface.DialogItemClickInterface;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.MultipartUtility;
import com.diamondxe.Utils.Permission;
import com.diamondxe.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BuyerKYCVerificationDocUploadActivity extends SuperActivity implements RecyclerInterface, DialogItemClickInterface {

    private LinearLayout company_gst_main_lin, company_gst_no_verify_lin, iec_main_lin, iec_no_verify_lin,aadhaar_main_lin,aadhaar_no_verify_lin,
            pan_card_main_lin,pan_card_no_verify_lin, passport_main_lin,passport_no_verify_lin,driving_licence_main_lin,driving_licence_no_verify_lin,
            dob_main_lin,dob_lin,auth_pan_card_main_lin,auth_pan_card_no_verify_lin;
    private RelativeLayout company_gst_no_rel, iec_no_rel, aadhaar_no_rel,pan_card_no_rel,passport_no_rel,driving_licence_no_rel,auth_pan_card_no_rel;
    private EditText company_gst_no_et, iec_no_et,aadhaar_no_et,pan_card_no_et,passport_no_et,driving_licence_no_et,auth_pan_card_no_et;
    private ImageView back_img, company_gst_doc_verify_img, company_gst_no_verify_img, iec_no_verify_img,aadhaar_no_verify_img,pan_card_no_verify_img,passport_no_verify_img,
            driving_licence_no_verify_img, iec_doc_verify_img,pan_doc_verify_img, auth_pan_card_no_verify_img;
    private TextView company_gst_no_error_tv, gst_dov_upload_tv, iec_no_require_tv, iec_doc_upload_tv,aadhaar_no_required,aadhaar_doc_upload_front_tv,
            aadhaar_doc_upload_back_tv,pan_card_no_required, pan_card_doc_upload_tv,passport_no_required,passport_doc_upload_front_tv,passport_doc_upload_back_tv,
            driving_licence_no_required,driving_licence_doc_upload_tv,date_of_birth_tv,cancel_tv,submit_tv,auth_pan_card_no_required,auth_pan_card_doc_upload_tv;

    private TextView aadhaar_doc_front_verify_img,aadhaar_doc_back_verify_img, auth_pan_doc_verify_img,passport_doc_front_verify_img,
            passport_doc_back_verify_img,driving_licence_doc_verify_img;
    private TextView aadhaar_doc_front_verify_error_tv, aadhaar_doc_back_verify_error_tv, auth_pan_doc_verify_error_tv, passport_doc_front_verify_error_tv,
            passport_doc_back_verify_error_tv, driving_licence_doc_verify_error_tv;

    private Activity activity;
    private Context context;

    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;

    private final String[] STORAGE_PERMS = {Manifest.permission.CAMERA};
    private final int INITIAL_REQUEST = 250;
    private final int STORAGE_REQUEST = INITIAL_REQUEST + 3;

    //For Pick image from Gallery or camera
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    String documentType="", whichDocumentVerify="",send_dob_server="",gSTNoVerified="",userChoosenTask="",picturePath="",aadhaarNoVerified="",
            panNoVerified="",drivingLicenseVerifiedNo="",dob="",authPanNoVerified="";
    String companyGSTDocBase64Url ="", iecDocBase64Url="", aadhaarDocFrontBase64Url = "",aadhaarDocBackBase64Url="",panCardDocBase64Url="",
    passportFrontDocBase64Url="", passportBackDocBase64Url="", drivingLicenceDocBase64Url="",authPanCardDocBase64Url="",userCountryCode="";
    boolean isGSTNoVerified = false;
    boolean isAadhaarNoVerified = false;
    boolean isPanNoVerified = false;
    boolean isAuthPanNoVerified = false;
    boolean isDrivingLicenceVerified = false;

    boolean isGSTNoVerifiedTakeImage = false;
    boolean isAadhaarNoVerifiedTakeImage = false;
    boolean isPanNoVerifiedTakeImage = false;
    boolean isAuthPanNoVerifiedTakeImage = false;
    boolean isDrivingLicenceVerifiedTakeImage = false;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_kyc_verification_doc_upload);

        context = activity = this;

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        userCountryCode = CommonUtility.getGlobalString(context, "user_country_code");

        company_gst_main_lin = findViewById(R.id.company_gst_main_lin);
        company_gst_no_rel = findViewById(R.id.company_gst_no_rel);
        company_gst_no_et = findViewById(R.id.company_gst_no_et);
        company_gst_no_verify_lin = findViewById(R.id.company_gst_no_verify_lin);
        company_gst_no_verify_lin.setOnClickListener(this);
        company_gst_no_verify_img = findViewById(R.id.company_gst_no_verify_img);
        company_gst_no_error_tv = findViewById(R.id.company_gst_no_error_tv);
        gst_dov_upload_tv = findViewById(R.id.gst_dov_upload_tv);
        gst_dov_upload_tv.setOnClickListener(this);

        company_gst_doc_verify_img = findViewById(R.id.company_gst_doc_verify_img);
        company_gst_doc_verify_img.setOnClickListener(this);

        iec_main_lin = findViewById(R.id.iec_main_lin);
        iec_no_rel = findViewById(R.id.iec_no_rel);
        iec_no_et = findViewById(R.id.iec_no_et);
        iec_no_verify_lin = findViewById(R.id.iec_no_verify_lin);
        iec_no_verify_lin.setOnClickListener(this);
        iec_no_verify_img = findViewById(R.id.iec_no_verify_img);
        iec_no_require_tv = findViewById(R.id.iec_no_require_tv);
        iec_doc_upload_tv = findViewById(R.id.iec_doc_upload_tv);
        iec_doc_upload_tv.setOnClickListener(this);
        iec_doc_verify_img = findViewById(R.id.iec_doc_verify_img);
        iec_doc_verify_img.setOnClickListener(this);

        aadhaar_main_lin = findViewById(R.id.aadhaar_main_lin);
        aadhaar_no_rel = findViewById(R.id.aadhaar_no_rel);
        aadhaar_no_et = findViewById(R.id.aadhaar_no_et);
        aadhaar_no_verify_lin = findViewById(R.id.aadhaar_no_verify_lin);
        aadhaar_no_verify_lin.setOnClickListener(this);
        aadhaar_no_verify_img = findViewById(R.id.aadhaar_no_verify_img);
        aadhaar_no_required = findViewById(R.id.aadhaar_no_required);
        aadhaar_doc_upload_front_tv = findViewById(R.id.aadhaar_doc_upload_front_tv);
        aadhaar_doc_upload_front_tv.setOnClickListener(this);
        aadhaar_doc_upload_back_tv = findViewById(R.id.aadhaar_doc_upload_back_tv);
        aadhaar_doc_upload_back_tv.setOnClickListener(this);

        aadhaar_doc_front_verify_img = findViewById(R.id.aadhaar_doc_front_verify_img);
        aadhaar_doc_front_verify_img.setOnClickListener(this);

        aadhaar_doc_back_verify_img = findViewById(R.id.aadhaar_doc_back_verify_img);
        aadhaar_doc_back_verify_img.setOnClickListener(this);

        pan_card_main_lin = findViewById(R.id.pan_card_main_lin);
        pan_card_no_rel = findViewById(R.id.pan_card_no_rel);
        pan_card_no_et = findViewById(R.id.pan_card_no_et);
        pan_card_no_verify_lin = findViewById(R.id.pan_card_no_verify_lin);
        pan_card_no_verify_lin.setOnClickListener(this);
        pan_card_no_verify_img = findViewById(R.id.pan_card_no_verify_img);
        pan_card_no_required = findViewById(R.id.pan_card_no_required);
        pan_card_doc_upload_tv = findViewById(R.id.pan_card_doc_upload_tv);
        pan_card_doc_upload_tv.setOnClickListener(this);
        pan_doc_verify_img = findViewById(R.id.pan_doc_verify_img);
        pan_doc_verify_img.setOnClickListener(this);

        auth_pan_card_main_lin = findViewById(R.id.auth_pan_card_main_lin);
        auth_pan_card_no_rel = findViewById(R.id.auth_pan_card_no_rel);
        auth_pan_card_no_et = findViewById(R.id.auth_pan_card_no_et);
        auth_pan_card_no_verify_lin = findViewById(R.id.auth_pan_card_no_verify_lin);
        auth_pan_card_no_verify_lin.setOnClickListener(this);
        auth_pan_card_no_verify_img = findViewById(R.id.auth_pan_card_no_verify_img);
        auth_pan_card_no_required = findViewById(R.id.auth_pan_card_no_required);
        auth_pan_card_doc_upload_tv = findViewById(R.id.auth_pan_card_doc_upload_tv);
        auth_pan_card_doc_upload_tv.setOnClickListener(this);
        auth_pan_doc_verify_img = findViewById(R.id.auth_pan_doc_verify_img);
        auth_pan_doc_verify_img.setOnClickListener(this);

        passport_main_lin = findViewById(R.id.passport_main_lin);
        passport_no_rel = findViewById(R.id.passport_no_rel);
        passport_no_et = findViewById(R.id.passport_no_et);
        passport_no_verify_lin = findViewById(R.id.passport_no_verify_lin);
        passport_no_verify_lin.setOnClickListener(this);
        passport_no_verify_img = findViewById(R.id.passport_no_verify_img);
        passport_no_required = findViewById(R.id.passport_no_required);
        passport_doc_upload_front_tv = findViewById(R.id.passport_doc_upload_front_tv);
        passport_doc_upload_front_tv.setOnClickListener(this);
        passport_doc_upload_back_tv = findViewById(R.id.passport_doc_upload_back_tv);
        passport_doc_upload_back_tv.setOnClickListener(this);

        passport_doc_front_verify_img = findViewById(R.id.passport_doc_front_verify_img);
        passport_doc_front_verify_img.setOnClickListener(this);

        passport_doc_back_verify_img = findViewById(R.id.passport_doc_back_verify_img);
        passport_doc_back_verify_img.setOnClickListener(this);

        driving_licence_main_lin = findViewById(R.id.driving_licence_main_lin);
        driving_licence_no_rel = findViewById(R.id.driving_licence_no_rel);
        driving_licence_no_et = findViewById(R.id.driving_licence_no_et);
        driving_licence_no_verify_lin = findViewById(R.id.driving_licence_no_verify_lin);
        driving_licence_no_verify_lin.setOnClickListener(this);
        driving_licence_no_verify_img = findViewById(R.id.driving_licence_no_verify_img);
        driving_licence_no_required = findViewById(R.id.driving_licence_no_required);
        driving_licence_doc_upload_tv = findViewById(R.id.driving_licence_doc_upload_tv);
        driving_licence_doc_upload_tv.setOnClickListener(this);
        driving_licence_doc_verify_img = findViewById(R.id.driving_licence_doc_verify_img);
        driving_licence_doc_verify_img.setOnClickListener(this);

        dob_main_lin = findViewById(R.id.dob_main_lin);
        date_of_birth_tv = findViewById(R.id.date_of_birth_tv);
        dob_lin = findViewById(R.id.dob_lin);
        dob_lin.setOnClickListener(this);

        cancel_tv = findViewById(R.id.cancel_tv);
        cancel_tv.setOnClickListener(this);

        submit_tv = findViewById(R.id.submit_tv);
        submit_tv.setOnClickListener(this);

        aadhaar_doc_front_verify_error_tv = findViewById(R.id.aadhaar_doc_front_verify_error_tv);
        aadhaar_doc_back_verify_error_tv = findViewById(R.id.aadhaar_doc_back_verify_error_tv);
        auth_pan_doc_verify_error_tv = findViewById(R.id.auth_pan_doc_verify_error_tv);
        passport_doc_front_verify_error_tv = findViewById(R.id.passport_doc_front_verify_error_tv);
        passport_doc_back_verify_error_tv = findViewById(R.id.passport_doc_back_verify_error_tv);
        driving_licence_doc_verify_error_tv = findViewById(R.id.driving_licence_doc_verify_error_tv);

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
                            onCaptureImageResult(data);
                        }
                    }
                });
        //-------------------------------End Updated Gallery and Camera COde-----------------------------------------------

        kycFieldVisibleHide();

        removeEditFieldError();

        if(userCountryCode.equalsIgnoreCase("IN") || userCountryCode.equalsIgnoreCase("in"))
        {
            aadhaar_main_lin.setVisibility(View.VISIBLE);
            auth_pan_card_main_lin.setVisibility(View.VISIBLE);
        }
        else{
            aadhaar_main_lin.setVisibility(View.GONE);
            auth_pan_card_main_lin.setVisibility(View.GONE);
        }
    }

    void kycFieldVisibleHide()
    {
        if(Constant.aadhaarVerifiedInd.equalsIgnoreCase("1"))
        {
            aadhaar_main_lin.setVisibility(View.GONE);
        }
        else{
            aadhaar_main_lin.setVisibility(View.VISIBLE);
        }

        if(Constant.aadhaarBackVerifiedInd.equalsIgnoreCase("1"))
        {
            aadhaar_main_lin.setVisibility(View.GONE);
        }
        else{
            aadhaar_main_lin.setVisibility(View.VISIBLE);
        }

        if(Constant.panCardVerifiedInd.equalsIgnoreCase("1"))
        {
            auth_pan_card_main_lin.setVisibility(View.GONE);
        }
        else{
            auth_pan_card_main_lin.setVisibility(View.VISIBLE);
        }

        if(Constant.companyPanCardVerifiedInd.equalsIgnoreCase("1"))
        {
            pan_card_main_lin.setVisibility(View.GONE);
        }
        else{
            pan_card_main_lin.setVisibility(View.GONE);
        }

        if(Constant.companyGstCertificateVerifiedInd.equalsIgnoreCase("1"))
        {
            company_gst_main_lin.setVisibility(View.GONE);
        }else{
            company_gst_main_lin.setVisibility(View.GONE);
        }

        if(Constant.iecCardVerifiedInd.equalsIgnoreCase("1"))
        {
            iec_main_lin.setVisibility(View.GONE);
        }
        else{
            iec_main_lin.setVisibility(View.GONE);
        }

        if(Constant.passportFrontVerifiedInd.equalsIgnoreCase("1"))
        {
            passport_main_lin.setVisibility(View.GONE);
        }
        else{
            passport_main_lin.setVisibility(View.VISIBLE);
        }

        if(Constant.passportBackVerifiedInd.equalsIgnoreCase("1"))
        {
            passport_main_lin.setVisibility(View.GONE);
        }
        else{
            passport_main_lin.setVisibility(View.VISIBLE);
        }
        if(Constant.drivingLicenceVerifiedInd.equalsIgnoreCase("1"))
        {
            driving_licence_main_lin.setVisibility(View.GONE);
        }
        else{
            driving_licence_main_lin.setVisibility(View.VISIBLE);
        }
    }

    void removeEditFieldError()
    {
        company_gst_no_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!company_gst_no_et.getText().toString().equalsIgnoreCase("")){
                    company_gst_no_error_tv.setVisibility(View.GONE);
                    company_gst_no_rel.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(company_gst_no_et.getText().toString().equalsIgnoreCase("")){
                    company_gst_no_error_tv.setVisibility(View.GONE);
                    company_gst_no_rel.setBackgroundResource(R.drawable.border_line_view);
                }

                // check GST No Verify for Not and Check GST No Change or Not
                if(isGSTNoVerified)
                {
                    if(gSTNoVerified.equalsIgnoreCase(company_gst_no_et.getText().toString().toString()))
                    {
                        isGSTNoVerifiedTakeImage = true;
                        company_gst_no_verify_img.setVisibility(View.VISIBLE);
                        company_gst_no_verify_lin.setVisibility(View.GONE);
                    }
                    else
                    {
                        isGSTNoVerifiedTakeImage = false;
                        company_gst_no_verify_img.setVisibility(View.GONE);
                        company_gst_no_verify_lin.setVisibility(View.VISIBLE);
                    }
                }else{}
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        aadhaar_no_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!aadhaar_no_et.getText().toString().equalsIgnoreCase("")){
                    aadhaar_no_required.setVisibility(View.GONE);
                    aadhaar_no_rel.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(aadhaar_no_et.getText().toString().equalsIgnoreCase("")){
                    aadhaar_no_required.setVisibility(View.GONE);
                    aadhaar_no_rel.setBackgroundResource(R.drawable.border_line_view);
                }

                if(isAadhaarNoVerified)
                {
                    if(aadhaarNoVerified.equalsIgnoreCase(aadhaar_no_et.getText().toString().toString()))
                    {
                        isAadhaarNoVerifiedTakeImage = true;
                        aadhaar_no_verify_img.setVisibility(View.VISIBLE);
                        aadhaar_no_verify_lin.setVisibility(View.GONE);
                    }
                    else
                    {
                        isAadhaarNoVerifiedTakeImage = false;
                        aadhaar_no_verify_img.setVisibility(View.GONE);
                        aadhaar_no_verify_lin.setVisibility(View.VISIBLE);
                    }
                }else{}
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        pan_card_no_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!pan_card_no_et.getText().toString().equalsIgnoreCase("")){
                    pan_card_no_required.setVisibility(View.GONE);
                    pan_card_no_rel.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(pan_card_no_et.getText().toString().equalsIgnoreCase("")){
                    pan_card_no_required.setVisibility(View.GONE);
                    pan_card_no_rel.setBackgroundResource(R.drawable.border_line_view);
                }

                if(isPanNoVerified)
                {
                    if(panNoVerified.equalsIgnoreCase(pan_card_no_et.getText().toString().toString()))
                    {
                        isPanNoVerifiedTakeImage = true;
                        pan_card_no_verify_img.setVisibility(View.VISIBLE);
                        pan_card_no_verify_lin.setVisibility(View.GONE);
                    }
                    else
                    {
                        isPanNoVerifiedTakeImage = false;
                        pan_card_no_verify_img.setVisibility(View.GONE);
                        pan_card_no_verify_lin.setVisibility(View.VISIBLE);
                    }
                }else{}
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        auth_pan_card_no_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!auth_pan_card_no_et.getText().toString().equalsIgnoreCase("")){
                    auth_pan_card_no_required.setVisibility(View.GONE);
                    auth_pan_card_no_rel.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(auth_pan_card_no_et.getText().toString().equalsIgnoreCase("")){
                    auth_pan_card_no_required.setVisibility(View.GONE);
                    auth_pan_card_no_rel.setBackgroundResource(R.drawable.border_line_view);
                }

                if(isAuthPanNoVerified)
                {
                    if(authPanNoVerified.equalsIgnoreCase(auth_pan_card_no_et.getText().toString().toString()))
                    {
                        isAuthPanNoVerifiedTakeImage = true;
                        auth_pan_card_no_verify_img.setVisibility(View.VISIBLE);
                        auth_pan_card_no_verify_lin.setVisibility(View.GONE);
                    }
                    else
                    {
                        isAuthPanNoVerifiedTakeImage = false;
                        auth_pan_card_no_verify_img.setVisibility(View.GONE);
                        auth_pan_card_no_verify_lin.setVisibility(View.VISIBLE);
                    }
                }else{}
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        driving_licence_no_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!driving_licence_no_et.getText().toString().equalsIgnoreCase("")){
                    driving_licence_no_required.setVisibility(View.GONE);
                    driving_licence_no_rel.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(driving_licence_no_et.getText().toString().equalsIgnoreCase("")){
                    driving_licence_no_required.setVisibility(View.GONE);
                    driving_licence_no_rel.setBackgroundResource(R.drawable.border_line_view);
                }

                if(isDrivingLicenceVerified)
                {
                    if(drivingLicenseVerifiedNo.equalsIgnoreCase(driving_licence_no_et.getText().toString().toString()))
                    {
                        isDrivingLicenceVerifiedTakeImage = true;
                        driving_licence_no_verify_img.setVisibility(View.VISIBLE);
                        driving_licence_no_verify_lin.setVisibility(View.GONE);
                    }
                    else
                    {
                        isDrivingLicenceVerifiedTakeImage = false;
                        driving_licence_no_verify_img.setVisibility(View.GONE);
                        driving_licence_no_verify_lin.setVisibility(View.VISIBLE);
                    }
                }else{}
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
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
        else if(id == R.id.company_gst_no_verify_lin)
        {
            Utils.hideKeyboard(activity);
            if(company_gst_no_et.getText().toString().trim().equalsIgnoreCase(""))
            {
                company_gst_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                company_gst_no_error_tv.setVisibility(View.VISIBLE);
            }
            else
            {
                company_gst_no_error_tv.setVisibility(View.GONE);
                documentType = GSTNo;
                onVerifyDocument(false, GSTNo, company_gst_no_et.getText().toString().trim());
            }
        }
        else if(id == R.id.gst_dov_upload_tv)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "companyGSTDoc";
            if(isGSTNoVerifiedTakeImage)
            {
                imageTakeOption();
            }else{
                company_gst_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                company_gst_no_error_tv.setVisibility(View.VISIBLE);
            }
        }
        else if(id == R.id.company_gst_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "companyGSTDoc";
            if(isGSTNoVerifiedTakeImage)
            {
                imageTakeOption();
            }else{
                company_gst_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                company_gst_no_error_tv.setVisibility(View.VISIBLE);
            }
        }
        else if(id == R.id.iec_doc_upload_tv)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "iecDoc";
            imageTakeOption();
        }
        else if(id == R.id.iec_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "iecDoc";
            imageTakeOption();
        }
        else if(id == R.id.aadhaar_no_verify_lin)
        {
            Utils.hideKeyboard(activity);
            if(aadhaar_no_et.getText().toString().trim().equalsIgnoreCase(""))
            {
                aadhaar_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                aadhaar_no_required.setVisibility(View.VISIBLE);
            }
            else
            {
                aadhaar_no_required.setVisibility(View.GONE);
                documentType = AADHAARNo;
                onVerifyDocument(false, AADHAARNo, aadhaar_no_et.getText().toString().trim());
            }
        }
        else if(id == R.id.aadhaar_doc_upload_front_tv)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "aadhaarFrontDoc";
            if(isAadhaarNoVerifiedTakeImage)
            {
                imageTakeOption();
            }
            else{
                aadhaar_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                aadhaar_no_required.setVisibility(View.VISIBLE);
            }
        }
        else if(id == R.id.aadhaar_doc_front_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "aadhaarFrontDoc";
            if(isAadhaarNoVerifiedTakeImage)
            {
                imageTakeOption();
            }
            else{
                aadhaar_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                aadhaar_no_required.setVisibility(View.VISIBLE);
            }
        }
        else if(id == R.id.aadhaar_doc_upload_back_tv)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "aadhaarBackDoc";
            if(isAadhaarNoVerifiedTakeImage)
            {
                imageTakeOption();
            } else{
                aadhaar_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                aadhaar_no_required.setVisibility(View.VISIBLE);
            }
        }
        else if(id == R.id.aadhaar_doc_back_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "aadhaarBackDoc";
            if(isAadhaarNoVerifiedTakeImage)
            {
                imageTakeOption();
            } else{
                aadhaar_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                aadhaar_no_required.setVisibility(View.VISIBLE);
            }
        }
        else if(id == R.id.pan_card_no_verify_lin)
        {
            Utils.hideKeyboard(activity);
            if(pan_card_no_et.getText().toString().trim().equalsIgnoreCase(""))
            {
                pan_card_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                pan_card_no_required.setVisibility(View.VISIBLE);
            }
            else
            {
                pan_card_no_required.setVisibility(View.GONE);
                documentType = PANNo;
                whichDocumentVerify="";
                onVerifyDocument(false, PANNo, pan_card_no_et.getText().toString().trim());
            }
        }
        else if(id == R.id.pan_card_doc_upload_tv)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "panCardDoc";
            if(isPanNoVerifiedTakeImage)
            {
                imageTakeOption();
            }else{
                pan_card_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                pan_card_no_required.setVisibility(View.VISIBLE);
            }

        }
        else if(id == R.id.pan_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "panCardDoc";
            if(isPanNoVerifiedTakeImage)
            {
                imageTakeOption();
            }else{
                pan_card_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                pan_card_no_required.setVisibility(View.VISIBLE);
            }
        }
        else if(id == R.id.auth_pan_card_no_verify_lin)
        {
            Utils.hideKeyboard(activity);
            if(auth_pan_card_no_et.getText().toString().trim().equalsIgnoreCase(""))
            {
                auth_pan_card_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                auth_pan_card_no_required.setVisibility(View.VISIBLE);
            }
            else
            {
                auth_pan_card_no_required.setVisibility(View.GONE);
                documentType = PANNo;
                whichDocumentVerify = "authPerson";
                onVerifyDocument(false, PANNo, auth_pan_card_no_et.getText().toString().trim());
            }
        }
        else if(id == R.id.auth_pan_card_doc_upload_tv)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "authPanCardDoc";
            if(isAuthPanNoVerifiedTakeImage)
            {
                imageTakeOption();
            }else{
                auth_pan_card_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                auth_pan_card_no_required.setVisibility(View.VISIBLE);
            }
        }
        else if(id == R.id.auth_pan_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "authPanCardDoc";
            if(isAuthPanNoVerifiedTakeImage)
            {
                imageTakeOption();
            }else{
                auth_pan_card_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                auth_pan_card_no_required.setVisibility(View.VISIBLE);
            }
        }
        else if(id == R.id.passport_doc_upload_front_tv)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "passportFrontDoc";
            imageTakeOption();
        }
        else if(id == R.id.passport_doc_front_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "passportFrontDoc";
            imageTakeOption();
        }
        else if(id == R.id.passport_doc_upload_back_tv)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "passportBackDoc";
            imageTakeOption();
        }
        else if(id == R.id.passport_doc_back_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "passportBackDoc";
            imageTakeOption();
        }
        else if(id == R.id.driving_licence_no_verify_lin)
        {
            Utils.hideKeyboard(activity);
            if(driving_licence_no_et.getText().toString().trim().equalsIgnoreCase(""))
            {
                driving_licence_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                driving_licence_no_required.setVisibility(View.VISIBLE);
            }
            else
            {
                driving_licence_no_required.setVisibility(View.GONE);
                documentType = DRIVING_LICENSE;
                onVerifyDocument(false, DRIVING_LICENSE, driving_licence_no_et.getText().toString().trim());
            }
        }
        else if(id == R.id.driving_licence_doc_upload_tv)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "drivingLicenceDoc";
            if(isDrivingLicenceVerifiedTakeImage)
            {
                imageTakeOption();
            }
            else{
                driving_licence_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                driving_licence_no_required.setVisibility(View.VISIBLE);
            }

        }
        else if(id == R.id.driving_licence_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "drivingLicenceDoc";
            if(isDrivingLicenceVerifiedTakeImage)
            {
                imageTakeOption();
            }
            else{
                driving_licence_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                driving_licence_no_required.setVisibility(View.VISIBLE);
            }
        }
        else if(id == R.id.dob_lin)
        {
            Utils.hideKeyboard(activity);
            dob = date_of_birth_tv.getText().toString().trim();
            CommonUtility.datePicker(context, BuyerKYCVerificationDocUploadActivity.this, dob, "dob", 0, System.currentTimeMillis());
        }
        else if(id == R.id.cancel_tv)
        {
            Utils.hideKeyboard(activity);
            finish();
        }
        else if(id == R.id.submit_tv)
        {
            Utils.hideKeyboard(activity);
            if (Utils.isNetworkAvailable(context)) {
                validateAndSendData();
            } else {
                showToast(ApiConstants.MSG_INTERNETERROR);
            }
            //onUpdateKYCDetails(false);
        }
    }

    private void validateAndSendData() {
        boolean isValid = true;


        // Validate Company Aadhaar Document
        if (isAadhaarNoVerified) {
            isValid &= validateDocumentAadhaar(aadhaarDocFrontBase64Url, aadhaarDocBackBase64Url, aadhaar_no_et, aadhaar_doc_front_verify_error_tv, aadhaar_doc_back_verify_error_tv);
        }

        // Validate PAN Card Document
        if (isAuthPanNoVerified) {
            isValid &= validateDocument(authPanCardDocBase64Url, auth_pan_card_no_et, auth_pan_doc_verify_error_tv);
        }

        // Validate Passport Document
        if (!passport_no_et.getText().toString().trim().equalsIgnoreCase("") && (passportFrontDocBase64Url.isEmpty() ||
                passportBackDocBase64Url.isEmpty())) {
            isValid &= validateDocumentPassport(passport_no_et, passport_doc_front_verify_error_tv, passport_doc_back_verify_error_tv);
        }

        // Validate Driving Licence Document
        if (isDrivingLicenceVerified) {
            isValid &= validateDocument(drivingLicenceDocBase64Url, driving_licence_no_et, driving_licence_doc_verify_error_tv);
        }

        // If both validations are successful, proceed with the API call
        if (isValid) {
            // Uncomment and use your async task
            SendDataAsyncTask asyncTask1 = new SendDataAsyncTask(context);
            asyncTask1.executeNetworkCall();
            //Toast.makeText(activity, "Call API", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateDocument(String documentUrl, EditText editText, TextView errorTextView) {
        if (documentUrl.isEmpty()) {
            editText.requestFocus();
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText(getResources().getString(R.string.please_upload_document));
            errorTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
            return false; // Validation failed
        }
        return true; // Validation successful
    }

    private boolean validateDocumentAadhaar(String documentUrl, String documentBackUrl, EditText editText, TextView errorTextView, TextView errorBackTextView)
    {
        if (documentUrl.isEmpty() || documentBackUrl.isEmpty()) {
            editText.requestFocus();

            if(documentUrl.isEmpty())
            {
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setText(getResources().getString(R.string.please_upload_document));
                errorTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
            }else{}

            if(documentBackUrl.isEmpty())
            {
                errorBackTextView.setVisibility(View.VISIBLE);
                errorBackTextView.setText(getResources().getString(R.string.please_upload_document));
                errorBackTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
            }else{}


            return false; // Validation failed
        }
        return true; // Validation successful
    }

    private boolean validateDocumentPassport(EditText editText, TextView errorTextView, TextView errorBackTextView) {
        if (!editText.getText().toString().trim().equalsIgnoreCase("")) {
            editText.requestFocus();

            if(passportFrontDocBase64Url.isEmpty())
            {
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setText(getResources().getString(R.string.please_upload_document));
                errorTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
            } else{}

            if(passportBackDocBase64Url.isEmpty())
            {
                errorBackTextView.setVisibility(View.VISIBLE);
                errorBackTextView.setText(getResources().getString(R.string.please_upload_document));
                errorBackTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
            }else{}

            return false; // Validation failed
        }
        return true; // Validation successful
    }



    void imageTakeOption()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (!canAccessCamera()) {
                requestPermissions(STORAGE_PERMS, STORAGE_REQUEST);
            } else {
                selectImage();
            }
        } else {
            selectImage();
        }
    }
    private boolean canAccessCamera() {
        return ((hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) && (hasPermission(Manifest.permission.CAMERA)));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity, perm));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {   // Comment 1.
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {   // Comment 1.
            selectImage();

        } else {
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) && !shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) && !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {    // Comment 2.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {    // Comment 2.

                //selectImage();
                /*Snackbar s = Snackbar.make(activity.findViewById(android.R.id.content), "Enable all permissions in settings to allow application access.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("SETTINGS", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, 1000);     // Comment 3.
                            }
                        });
                View snackbarView = s.getView();
                // TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                //textView.setMaxLines(3);    // Comment 4.
                s.show();*/
            }
            else{
                //  selectImage();
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
                    /*if (result)
                        galleryIntent();*/
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    //For Gallery Intnet
    private void galleryIntent() {
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);*/

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        galleryLauncher.launch(Intent.createChooser(intent, "Select File"));
    }

    //For Camera Intnet
    private void cameraIntent() {
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);*/

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent);
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }*/

    private void onCaptureImageResult(Intent data)
    {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        sendImageToServer(imageBitmap);

        afterSelectImageDocTextViewVisibleHide();
    }

    private Bitmap compressImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream); // Compress to 50%
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
    private String convertToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    //
    private void sendImageToServer(Bitmap bitmap) {
        //String url = "YOUR_SERVER_URL";
        Bitmap compressedBitmap = compressImage(bitmap);
        String base64Image = convertToBase64(compressedBitmap);

        if(userChoosenTask.equalsIgnoreCase("companyGSTDoc"))
        {
            companyGSTDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("iecDoc"))
        {
            iecDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("aadhaarFrontDoc"))
        {
            aadhaarDocFrontBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("aadhaarBackDoc"))
        {
            aadhaarDocBackBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("panCardDoc"))
        {
            panCardDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("authPanCardDoc"))
        {
            authPanCardDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("passportFrontDoc"))
        {
            passportFrontDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("passportBackDoc"))
        {
            passportBackDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("drivingLicenceDoc"))
        {
            drivingLicenceDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else{}

        Log.e("-------Final_base64Image----- : ", base64Image.toString());
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                // Retrieve the Bitmap from the intent data
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                // Get the URI of the selected image
                Uri uri = data.getData();
                picturePath = CommonUtility.getPath(activity, uri);

                //For Compressing Image
              /*  String path = CommonUtility.compressImage(picturePath);
                if(path!=null && !path.equalsIgnoreCase("") && !path.equalsIgnoreCase("null"))
                {
                    picturePath = path;
                }else{}*/

                // BitmapFactory.Options can be used to further configure bitmap loading, if necessary
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(picturePath, bitmapOptions);

                // Convert Bitmap to Base64 encoded String
                /*String base64Image = bitmapToBase64(bm);
                companyKycPanDocBase64Url = base64Image;*/

                // Log the Base64 string for testing
                // Log.e("------picturePath-----", "Base64 Image : " + base64Image);

                sendImageToServer(bm);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        afterSelectImageDocTextViewVisibleHide();
    }

    void afterSelectImageDocTextViewVisibleHide()
    {
        if(userChoosenTask.equalsIgnoreCase("companyGSTDoc"))
        {
            gst_dov_upload_tv.setVisibility(View.GONE);
            company_gst_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("iecDoc"))
        {
            iec_doc_upload_tv.setVisibility(View.GONE);
            iec_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("aadhaarFrontDoc"))
        {
            aadhaar_doc_upload_front_tv.setVisibility(View.GONE);
            aadhaar_doc_front_verify_img.setVisibility(View.VISIBLE);
            aadhaar_doc_front_verify_error_tv.setVisibility(View.GONE);
        }
        else if(userChoosenTask.equalsIgnoreCase("aadhaarBackDoc"))
        {
            aadhaar_doc_upload_back_tv.setVisibility(View.GONE);
            aadhaar_doc_back_verify_img.setVisibility(View.VISIBLE);
            aadhaar_doc_back_verify_error_tv.setVisibility(View.GONE);
        }
        else if(userChoosenTask.equalsIgnoreCase("panCardDoc"))
        {
            pan_card_doc_upload_tv.setVisibility(View.GONE);
            pan_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("authPanCardDoc"))
        {
            auth_pan_card_doc_upload_tv.setVisibility(View.GONE);
            auth_pan_doc_verify_img.setVisibility(View.VISIBLE);
            auth_pan_doc_verify_error_tv.setVisibility(View.GONE);
        }
        else if(userChoosenTask.equalsIgnoreCase("passportFrontDoc"))
        {
            passport_doc_upload_front_tv.setVisibility(View.GONE);
            passport_doc_front_verify_img.setVisibility(View.VISIBLE);
            passport_doc_front_verify_error_tv.setVisibility(View.GONE);
        }
        else if(userChoosenTask.equalsIgnoreCase("passportBackDoc"))
        {
            passport_doc_upload_back_tv.setVisibility(View.GONE);
            passport_doc_back_verify_img.setVisibility(View.VISIBLE);
            passport_doc_back_verify_error_tv.setVisibility(View.GONE);
        }
        else if(userChoosenTask.equalsIgnoreCase("drivingLicenceDoc"))
        {
            driving_licence_doc_upload_tv.setVisibility(View.GONE);
            driving_licence_doc_verify_img.setVisibility(View.VISIBLE);
            driving_licence_doc_verify_error_tv.setVisibility(View.GONE);
        }
        else{}
    }

    // Method to convert Bitmap to Base64 encoded String
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public void onVerifyDocument(boolean showLoader, String documentType, String documentNumber)
    {
        if (Utils.isNetworkAvailable(context))
        {
            //Log.e("----------Diamond------ : ", Constant.shapeDiamondValue.toString());
            urlParameter = new HashMap<String, String>();

            urlParameter.put("documentType", documentType);

            if(documentType.equalsIgnoreCase(PANNo))
            {
                urlParameter.put("PANNo", documentNumber);
            }
            else if(documentType.equalsIgnoreCase(GSTNo))
            {
                urlParameter.put("GSTNo", documentNumber);
            }
            else if(documentType.equalsIgnoreCase(AADHAARNo))
            {
                urlParameter.put("aadhaarNo", documentNumber);
            }
            else if(documentType.equalsIgnoreCase(DRIVING_LICENSE))
            {
                urlParameter.put("documentType", "otherDocs");
                urlParameter.put("otherDocumentType", "Driving License");
                urlParameter.put("otherDocumentNumber", driving_licence_no_et.getText().toString().trim());
                urlParameter.put("dob", send_dob_server);
            }
            else{}

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.VALIDATE_DOCUMENT, ApiConstants.VALIDATE_DOCUMENT_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

        try {
            Log.e("Diamonds : ", "--------JSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");

            switch (service_ID) {
                case ApiConstants.VALIDATE_DOCUMENT_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();

                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        String first_name = CommonUtility.checkString(jObjDetails.optString("first_name"));
                        String last_name = CommonUtility.checkString(jObjDetails.optString("last_name"));
                        String email = CommonUtility.checkString(jObjDetails.optString("email"));
                        String company_name = CommonUtility.checkString(jObjDetails.optString("company_name"));
                        String company_type = CommonUtility.checkString(jObjDetails.optString("company_type"));
                        String address = CommonUtility.checkString(jObjDetails.optString("address"));
                        String bank_beneficiary_name = CommonUtility.checkString(jObjDetails.optString("bank_beneficiary_name"));
                        String pan_company_name = CommonUtility.checkString(jObjDetails.optString("pan_company_name"));
                        String nature_of_business = CommonUtility.checkString(jObjDetails.optString("nature_of_business"));
                        String pincode = CommonUtility.checkString(jObjDetails.optString("pincode"));

                         if(documentType.equalsIgnoreCase(GSTNo))
                         {
                             isGSTNoVerified = true;
                             isGSTNoVerifiedTakeImage = true;
                             gSTNoVerified = company_gst_no_et.getText().toString().trim();
                             company_gst_no_verify_img.setVisibility(View.VISIBLE);
                             company_gst_no_verify_lin.setVisibility(View.GONE);
                         }
                         else if(documentType.equalsIgnoreCase(AADHAARNo))
                         {
                             isAadhaarNoVerified = true;
                             isAadhaarNoVerifiedTakeImage = true;
                             aadhaarNoVerified = aadhaar_no_et.getText().toString().trim();
                             aadhaar_no_verify_img.setVisibility(View.VISIBLE);
                             aadhaar_no_verify_lin.setVisibility(View.GONE);
                         }
                         else if(documentType.equalsIgnoreCase(PANNo))
                         {
                             if(whichDocumentVerify.equalsIgnoreCase(""))
                             {
                                 isPanNoVerified = true;
                                 isPanNoVerifiedTakeImage = true;
                                 panNoVerified = pan_card_no_et.getText().toString().trim();
                                 pan_card_no_verify_img.setVisibility(View.VISIBLE);
                                 pan_card_no_verify_lin.setVisibility(View.GONE);
                             }
                             else{
                                 isAuthPanNoVerified = true;
                                 isAuthPanNoVerifiedTakeImage = true;
                                 authPanNoVerified = auth_pan_card_no_et.getText().toString().trim();
                                 auth_pan_card_no_verify_img.setVisibility(View.VISIBLE);
                                 auth_pan_card_no_verify_lin.setVisibility(View.GONE);
                             }

                         }
                         else if(documentType.equalsIgnoreCase(DRIVING_LICENSE))
                         {
                             isDrivingLicenceVerified = true;
                             isDrivingLicenceVerifiedTakeImage = true;
                             drivingLicenseVerifiedNo = driving_licence_no_et.getText().toString().trim();
                             driving_licence_no_verify_img.setVisibility(View.VISIBLE);
                             driving_licence_no_verify_lin.setVisibility(View.GONE);
                         }
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

    @Override
    public void dialogItemClick(String value, String action)
    {

        if (action.equalsIgnoreCase("dob")) {
            date_of_birth_tv.setText(value);
            //from_date_required.setVisibility(View.GONE);

           // Log.e("------dob_value----- : ", value);

            // this date send to server formated date yyyy-MM-dd.
            send_dob_server = CommonUtility.convertDateFormat(value, ApiConstants.DATE_DOB_FORMAT, "dd/MM/yyyy");
        }
    }

    public class SendDataAsyncTask {
        private String reply = "";
        //private ProgressDialog _dialog;
        private Context context;
        private ExecutorService executorService;
        private Handler handler;

        public SendDataAsyncTask(Context context) {
            this.context = context;
            this.executorService = Executors.newSingleThreadExecutor();
            this.handler = new Handler(Looper.getMainLooper());
        }

        public void executeNetworkCall() {
            if (Utils.isNetworkAvailable(context)) {
                onPreExecute();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        doInBackground();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onPostExecute();
                            }
                        });
                    }
                });
            } else {
                Toast.makeText(context, ApiConstants.MSG_INTERNETERROR, Toast.LENGTH_SHORT).show();
            }
        }

        protected void onPreExecute() {
            /*_dialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
            _dialog.setIndeterminate(true);
            _dialog.setCancelable(false);
            _dialog.show();*/
            context.startActivity(new Intent(context, TransparentActivity.class));
        }

        protected void doInBackground() {
            try {
                MultipartUtility mu;
                mu = new MultipartUtility(ApiConstants.DOMAIN_NAME + ApiConstants.UPDATE_KYC_DETAILS, "UTF-8", context);

                /*if(Constant.companyGstCertificateVerifiedInd.equalsIgnoreCase("1")) {}
                else{
                    mu.addFormField("companyGSTNo", company_gst_no_et.getText().toString().trim());
                    mu.addFormField("companyGSTNoDoc", companyGSTDocBase64Url);
                    mu.addFormField("companyGSTNoId", Constant.companyGstCertificateAttachmentId);
                }

                if(Constant.companyPanCardVerifiedInd.equalsIgnoreCase("1")) {}
                else{
                    mu.addFormField("companyPANNo", pan_card_no_et.getText().toString().trim());
                    mu.addFormField("companyPANNoDoc", panCardDocBase64Url);
                    mu.addFormField("companyPANNoId", Constant.companyPanCardAttachmentId);
                }

                if(Constant.iecCardVerifiedInd.equalsIgnoreCase("1")) {}
                else{
                    mu.addFormField("IEC", iec_no_et.getText().toString().trim());
                    mu.addFormField("IECDoc", iecDocBase64Url);
                    mu.addFormField("IECId", Constant.iecAttachmentId);
                }*/

                if(Constant.aadhaarVerifiedInd.equalsIgnoreCase("1")) {}
                else{
                    mu.addFormField("aadhaarNo", aadhaar_no_et.getText().toString().trim());
                    mu.addFormField("aadhaarFrontId", Constant.aadhaarFrontAttachmentId);
                    mu.addFormField("aadhaarBackId", Constant.aadhaarBackAttachmentId);
                    mu.addFormField("aadhaarNoFrontDoc", aadhaarDocFrontBase64Url);
                    mu.addFormField("aadhaarNoBackDoc", aadhaarDocBackBase64Url);
                }

                if(Constant.panCardVerifiedInd.equalsIgnoreCase("1")) {}
                else{
                    mu.addFormField("PANNo", auth_pan_card_no_et.getText().toString().trim());
                    mu.addFormField("PANNoDoc", authPanCardDocBase64Url);
                    mu.addFormField("PANNoId", Constant.panCardAttachmentId);
                }

                if(Constant.passportFrontVerifiedInd.equalsIgnoreCase("1") || Constant.passportBackVerifiedInd.equalsIgnoreCase("1")) {}
                else{
                    mu.addFormField("passportNo", passport_no_et.getText().toString().trim());
                    mu.addFormField("passportFrontDoc", passportFrontDocBase64Url);
                    mu.addFormField("passportFrontDocId", Constant.passportFrontAttachmentId);
                    mu.addFormField("passportBackDoc", passportBackDocBase64Url);
                    mu.addFormField("passportBackDocId", Constant.passportBackAttachmentId);
                }

                if(Constant.drivingLicenceVerifiedInd.equalsIgnoreCase("1")) {}
                else{
                    mu.addFormField("dob", send_dob_server);
                    mu.addFormField("drivingLicenseNo", driving_licence_no_et.getText().toString().trim());
                    mu.addFormField("drivingLicenseDoc", drivingLicenceDocBase64Url);
                    mu.addFormField("drivingLicenseDocId", Constant.drivingLicenceAttachmentId);
                }

                mu.addFormField("drivingLicenseDocId1", "");

                reply = mu.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected void onPostExecute() {
            //_dialog.dismiss();
            TransparentActivity.terminateTransparentActivity();
            if (reply != null) {
                showLog("reply : " + reply);
                try {
                    JSONObject jsonObject = new JSONObject(reply);
                    JSONObject jsonObjectData = jsonObject;
                    String message = jsonObjectData.optString("msg");
                    if (jsonObjectData.optString("status").equalsIgnoreCase("1")) {
                        showToast(message);
                        // Assuming `finish()` is some method in the current class or you might need to call some other method
                         finish();
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");
                    } else if (jsonObjectData.optString("status").equalsIgnoreCase("4"))
                    {
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}