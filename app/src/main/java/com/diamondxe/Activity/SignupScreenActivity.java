package com.diamondxe.Activity;

import static com.diamondxe.ApiCalling.ApiConstants.AADHAARNo;
import static com.diamondxe.ApiCalling.ApiConstants.DRIVING_LICENSE;
import static com.diamondxe.ApiCalling.ApiConstants.FOR_BASE64_IMAGE;
import static com.diamondxe.ApiCalling.ApiConstants.GSTNo;
import static com.diamondxe.ApiCalling.ApiConstants.INDIA_COUNTRY_CODE;
import static com.diamondxe.ApiCalling.ApiConstants.INDIA_FLAG_URL;
import static com.diamondxe.ApiCalling.ApiConstants.PANNo;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Adapter.CityListAdapter;
import com.diamondxe.Adapter.CountryCodeListAdapter;
import com.diamondxe.Adapter.CountryListAdapter;
import com.diamondxe.Adapter.DiamondCaratTypeListAdapter;
import com.diamondxe.Adapter.DiamondShapeImageListAdapter;
import com.diamondxe.Adapter.StateListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.CountryListModel;
import com.diamondxe.Interface.DialogItemClickInterface;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CircleTransform;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.CompressionUtils;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.MultipartUtility;
import com.diamondxe.Utils.Permission;
import com.diamondxe.Utils.Utility;
import com.diamondxe.Utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupScreenActivity extends SuperActivity implements DialogItemClickInterface, RecyclerInterface {

    private CardView buyer_signup_card, auth_person_card;
    boolean isEmailVerified = false;
    boolean isBasicEmailVerified = false;
    boolean isCompanyGSTNoVerified = false;
    boolean isCompanyPanNoVerified = false;
    boolean isCompanyPanDocUpload = false;
    boolean isAuthPersonAadharNo = false;
    boolean isAuthPersonPanNo = false;
    boolean isDrivingLicenseNo = false;
    boolean isBusinessLicenceNo = false;
    boolean isSupplierEmailVerified = false;
    boolean isSupplierGSTNoVerified = false;
    boolean isSupplierPanNoVerified = false;
    boolean isSupplierPanDocUpload = false;
    boolean isSupplierBusinessDocUpload = false;
    String supplierGSTNoVerified = "", supplierPanNoVerified = "", whoSelectCompanyType = "", whoSelectBusinessNatureType="";

    private boolean isApiCalling = false; // Flag to track API call

    String superviosrPersonPhCountryCode="+91", supplierCompanyPhCountryCode="+91";
    String dealerBasicPhCountryCode="+91", dealerCompanyPhCountryCode="+91";
    String buyerPhCountryCode="+91";

    String companyKycGstDocBase64Url = "", companyKycPanDocBase64Url = "", companyIECPanDocBase64Url = "", authAadharFrontDocBase64Url ="",
            authAadharBackDocBase64Url = "", authAadharPanDocBase64Url = "", passPortFrontDocBase64Url="", passPortBackDocBase64Url="",
            drivingLicenceDocBase64Url="", businessLicenceNoDocBase64Url="";

    String supplierCompanyGSTDocBase64Url="", supplierCompanyPanDocBase64Url="", supplierCompanyIECDocBase64Url="" ,
            supplierCompanyBusinessDocBase64Url="";

    String verifiedEmail = "", basicVerifiedEmail="", companyGstNoVerified="", companyPanNoVerified="", authPersonVerifiedAadharNo="",
            authPersonVerifiedPanNo="", drivingLicenseVerifiedNo="", supplierVerifiedEmail="";
    String buyerCountryName = "", buyerStateName="", buyerCityName="", dealerCountryName = "", dealerStateName="", dealerCityName="",
            supplierCountryName = "", supplierStateName="", supplierCityName="";
    private LinearLayout buyer_lin, dealer_lin,supplier_lin, dealer_signup_card, supplier_signup_card, gst_pan_no_lin, business_licence_lin;

    private EditText first_name_et, last_name_et, mobile_number_et, email_et, password_et, confirm_password_et, pincode_et, address_line1_et,
            address_line2_et, referral_code_et, search_et;

    private TextView first_name_error_tv,last_name_error_tv,mobile_number_error_tv, email_error_tv, password_error_tv, confirm_password_error_tv,
            country_error_tv, state_error_tv,city_error_tv,pincode_error_tv, address_line1_error_tv, terms_condition_check_error,
            terms_condition_tv;

    CheckBox termsConditionCheck;

    private ImageView buyer_email_verify_img;

    private LinearLayout country_code_lin, verify_email_lin, country_lin, state_lin, city_lin, mobile_number_lin;
    private TextView buyer_tv, dealer_tv, supplier_tv, signup_lbl_tv, country_code, already_login_tv, referral_error_tv, create_account_tv,
    country_tv, state_tv, city_tv;

    String companyCountryId = "", companyStateId="", companyCityId="";
    String supplierCountryId = "", supplierStateId="", supplierCityId="";

    //--------------------------------------Company Layout Details----------------------------------------

    private boolean isArrowDownCompanyDetails = false;
    private boolean isArrowDownCompanyKyc = false;
    private boolean isArrowDownCompanyBasic = false;
    private boolean isArrowDownCompanyAuth = false;
    private boolean isArrowDownCompanyPassport = false;
    private ImageView drop_arrow_company_details_img, drop_arrow_company_kyc_img, drop_arrow_basic_details_img, drop_arrow_auth_img, drop_passport_img,
            pan_verify_img, company_gst_no_verify_img, company_gst_doc_verify_img, company_pan_doc_verify_img, company_iec_doc_verify_img,
            basic_country_img, auth_aadhar_no_verify_img, auth_pan_no_verify_img, auth_pan_doc_verify_img, auth_aadhar_front_doc_verify_img,
            auth_aadhar_back_doc_verify_img, passport_front_doc_verify_img, passport_back_doc_verify_img, driving_lic_doc_verify_img,
            driving_lic_no_verify_img, basic_email_verify_img, company_business_lic_no_verify_img, company_business_lic_doc_verify_img;
    private RelativeLayout company_details_rel, company_kyc_rel, basic_details_rel, auth_person_rel, passport_rel, company_pan_back_rel,
            company_gst_no_rel, auth_back_rel, auth_pan_back_rel, driving_lic_bacl_rel, basic_email_rel,company_pan_doc_rel, company_business_lic_no_rel,
            company_business_lic_doc_rel;
    private LinearLayout  company_details_lin, company_kyc_lin, basic_details_lin, auth_person_lin, passport_lin, pincode_lin,
            company_business_lic_no_verify_lin, upload_business_lic_doc_lin, suppier_business_licence_lin, suppier_gst_pan_no_lin;
    private EditText company_name_et, company_email_et, company_mobile_number_et, company_pincode_et, company_address_line1_et,
            company_address_line2_et, company_pan_no_et, company_gst_no_et, basic_mobile_number_et, auth_aadhar_no_et, auth_pan_no_et,
            driving_lic_no_et, company_iec_no_et, basic_first_name_et, basic_last_name_et, basic_email_et, passport_no_et,
    basic_password_et, basic_confirm_password_et, company_business_lic_no_et;
    private TextView company_name_error_tv, company_email_error_tv, company_country_code, company_mobile_number_error_tv, company_country_tv,
            company_country_error_tv, company_state_tv, company_state_error_tv, company_city_tv, company_city_error_tv, company_address_line1_error_tv,
            company_type_tv, company_type_error_tv, business_nature_tv, business_nature_error_tv, company_pincode_error_tv, date_of_birth_tv,
            company_pan_no_error_tv, company_gst_no_error_tv, basic_country_code, basic_mobile_number_error_tv, auth_aadhar_no_error_tv,
            auth_pan_no_error_tv, driving_lic_error_required_tv, basic_email_error_tv, company_pan_doc_error_tv,basic_first_name_error_tv,
            basic_last_name_error_tv, basic_password_error_tv, basic_confirm_password_error_tv, company_business_lic_no_error_tv, company_business_lic_doc_error_tv;
    private LinearLayout company_verify_email_lin, company_mobile_number_lin, company_country_code_lin, company_country_lin, company_state_lin,
            company_city_lin, company_type_lin, business_nature_lin, dob_lin, company_pan_no_verify_lin,
            company_gst_no_verify_lin, upload_gst_doc_lin, upload_pan_doc_lin, upload_iec_doc_lin,basic_mobile_number_lin, basic_country_code_lin,
            auth_aadhar_no_verify_lin, auth_pan_no_verify_lin, auth_pan_doc_lin, upload_aadhar_front_lin, upload_aadhar_back_lin,
            upload_passport_front_lin, upload_passport_back_lin, driving_lic_doc_lin, driving_lic_no_verify_lin, basic_verify_email_lin;
    private CircleImageView company_country_img;

    //--------------------------------------Supplier Layout Details----------------------------------------

    private RelativeLayout supplier_company_gst_no_rel, supplier_company_gst_doc_rel, supplier_company_pan_back_rel, supplier_company_pan_doc_rel,
            supplier_company_iec_doc_rel, suppier_business_lic_no_rel, suppier_business_lic_doc_rel;
    private EditText supplier_company_name_et,supplier_company_email_et, supplier_password_et, supplier_confirm_password_et,supplier_mobile_number_et,
            supplier_address_line1_et, supplier_address_line2_et, supplier_pincode_et, bank_name_et, branch_name_et, bank_account_no_et,
            bank_account_type_et, ifsc_code_et, bank_swift_code_et, superviosr_name_et,superviosr_email_et, superviosr_mobile_number_et,
            supplier_company_gst_no_et, supplier_company_pan_no_et, supplier_company_iec_no_et, suppier_business_lic_no_et;

    private TextView suppliercompany_name_error_tv, supplier__email_error_tv, supplier_password_error_tv, supplier_confirm_password_error_tv,supplier_mobile_number_error_tv,
            supplier_country_code, inventory_error_tv,supplier_country_tv, supplier_country_error_tv,supplier_state_tv, supplier_state_error_tv,supplier_city_tv,
            supplier_city_error_tv, supplier_address_line1_error_tv, supplier_pincode_error_tv, superviosr_name_error_tv, superviosremail_error_tv,
            superviosr_country_code, superviosr_mobile_number_error_tv, supplier_company_gst_no_error_tv,supplier_company_pan_no_error_tv,
            supplier_company_pan_doc_error_tv, suppier_business_lic_no_error_tv, suppier_business_lic_doc_error_tv, supplier_company_type_tv,
            supplier_business_nature_tv, supplier_company_type_error_tv, supplier_business_nature_error_tv;
    private LinearLayout supplier_mobile_number_lin, supplier_country_code_lin, supplier_country_lin, supplier_state_lin, supplier_city_lin,
            superviosr_mobile_number_lin, superviosr_country_code_lin, supplier_company_gst_no_verify_lin, supplier_upload_gst_doc_lin,
            supplier_company_pan_no_verify_lin, supplier_upload_pan_doc_lin, supplier_upload_iec_doc_lin, suppier_business_lic_no_verify_lin,
            suppier_upload_business_lic_doc_lin, supplier_company_type_lin, supplier_business_nature_lin, supplier_verify_email_lin;
    private ImageView supplier_country_img, superviosr_country_img, supplier_company_gst_doc_verify_img, supplier_company_gst_no_verify_img,
            supplier_company_pan_no_verify_img,supplier_company_pan_doc_verify_img, supplier_company_iec_doc_verify_img, suppier_business_lic_no_verify_img,
            suppier_business_lic_doc_verify_img, supplier_email_verify_img;

    private RelativeLayout supplier_company_details_rel, supplier_company_kyc_rel, bank_info_rel, superviosr_details_rel;
    private ImageView drop_arrow_supplier_company_details_img, drop_arrow_supplier_kyc_img, bank_details_img, drop_arrow_superviosr_details_img;
    private LinearLayout supplier_company_details_lin, supplier_company_kyc_lin, bank_details_lin, superviosr_details_lin, inventory_lin;
    private TextView inventory_tv;
    private boolean isArrowDownSupplierCompanyDetails = false;
    private boolean isArrowDownSupplierCompanyKyc = false;
    private boolean isArrowDownBankInfo = false;
    private boolean isArrowDownSupervisor = false;

    //private CheckBox terms_condition_check;
    private ImageView back_img;
    private CircleImageView country_img;

    String BUYER = "Buyer";
    String DEALER = "Dealer";
    String SUPPLIER = "Supplier";

    String selectedSignupTypeTab = "Buyer";

    private Activity activity;
    private Context context;

    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;

    private BottomSheetDialog dialog;
    RecyclerView recycler_view;
    ArrayList<CountryListModel> countryArrayList;
    ArrayList<CountryListModel> stateArrayList;
    ArrayList<CountryListModel> cityArrayList;
    CountryListAdapter countryListAdapter;
    StateListAdapter stateListAdapter;
    CityListAdapter cityListAdapter;

    String countryId = "", stateId = "", cityId = "",dob="",send_dob_server="",documentType="",whereToVerifyDoc="",countryCodeFor="",picturePath="",userChoosenTask="",
    stateCodeFor="", cityCodeFor="";

    String showCountryCode = "";

    //private final String[] STORAGE_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private final String[] STORAGE_PERMS = {Manifest.permission.CAMERA};
    private final int INITIAL_REQUEST = 250;
    private final int STORAGE_REQUEST = INITIAL_REQUEST + 3;

    //For Pick image from Gallery or camera
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    String resendOTP = "", emailVerifyOrNot="", basicEmailVerifyOrNot="", basicResendOTP="", supplierResendOTP="", supplierEmailVerifyOrNot="";
    android.app.AlertDialog alertDialog;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screenctivity);

        context = activity = this;

        countryArrayList = new ArrayList<>();
        stateArrayList = new ArrayList<>();
        cityArrayList = new ArrayList<>();

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        buyer_lin = findViewById(R.id.buyer_lin);
        dealer_lin = findViewById(R.id.dealer_lin);
        supplier_lin = findViewById(R.id.supplier_lin);

        buyer_tv = findViewById(R.id.buyer_tv);
        buyer_tv.setOnClickListener(this);

        dealer_tv = findViewById(R.id.dealer_tv);
        dealer_tv.setOnClickListener(this);

        supplier_tv = findViewById(R.id.supplier_tv);
        supplier_tv.setOnClickListener(this);

        signup_lbl_tv = findViewById(R.id.signup_lbl_tv);

        buyer_signup_card = findViewById(R.id.buyer_signup_card);
        dealer_signup_card = findViewById(R.id.dealer_signup_card);
        supplier_signup_card = findViewById(R.id.supplier_signup_card);

        gst_pan_no_lin = findViewById(R.id.gst_pan_no_lin);
        business_licence_lin = findViewById(R.id.business_licence_lin);

        first_name_et = findViewById(R.id.first_name_et);
        last_name_et = findViewById(R.id.last_name_et);
        mobile_number_et = findViewById(R.id.mobile_number_et);
        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        confirm_password_et = findViewById(R.id.confirm_password_et);
        country_tv = findViewById(R.id.country_tv);
        country_tv.setOnClickListener(this);
        state_tv = findViewById(R.id.state_tv);
        state_tv.setOnClickListener(this);
        city_tv = findViewById(R.id.city_tv);
        city_tv.setOnClickListener(this);
        pincode_et = findViewById(R.id.pincode_et);
        address_line1_et = findViewById(R.id.address_line1_et);
        address_line2_et = findViewById(R.id.address_line2_et);
        referral_code_et = findViewById(R.id.referral_code_et);

        country_img = findViewById(R.id.country_img);
        country_code = findViewById(R.id.country_code);

        mobile_number_lin = findViewById(R.id.mobile_number_lin);

        country_code_lin = findViewById(R.id.country_code_lin);
        country_code_lin.setOnClickListener(this);

        buyer_email_verify_img = findViewById(R.id.buyer_email_verify_img);

        verify_email_lin = findViewById(R.id.verify_email_lin);
        verify_email_lin.setOnClickListener(this);

        country_lin = findViewById(R.id.country_lin);
        country_lin.setOnClickListener(this);

        state_lin = findViewById(R.id.state_lin);
        state_lin.setOnClickListener(this);

        city_lin = findViewById(R.id.city_lin);
        city_lin.setOnClickListener(this);

        referral_error_tv = findViewById(R.id.referral_error_tv);

        first_name_error_tv = findViewById(R.id.first_name_error_tv);
        last_name_error_tv = findViewById(R.id.last_name_error_tv);
        mobile_number_error_tv = findViewById(R.id.mobile_number_error_tv);
        email_error_tv = findViewById(R.id.email_error_tv);
        password_error_tv = findViewById(R.id.password_error_tv);
        confirm_password_error_tv = findViewById(R.id.confirm_password_error_tv);
        country_error_tv = findViewById(R.id.country_error_tv);
        state_error_tv = findViewById(R.id.state_error_tv);
        city_error_tv = findViewById(R.id.city_error_tv);
        pincode_error_tv = findViewById(R.id.pincode_error_tv);
        address_line1_error_tv = findViewById(R.id.address_line1_error_tv);
        terms_condition_check_error = findViewById(R.id.terms_condition_check_error);

        company_verify_email_lin = findViewById(R.id.company_verify_email_lin);
        company_mobile_number_lin = findViewById(R.id.company_mobile_number_lin);
        company_country_code_lin = findViewById(R.id.company_country_code_lin);
        company_country_code_lin.setOnClickListener(this);
        company_country_lin = findViewById(R.id.company_country_lin);
        company_country_lin.setOnClickListener(this);

        company_state_lin = findViewById(R.id.company_state_lin);
        company_state_lin.setOnClickListener(this);

        company_city_lin = findViewById(R.id.company_city_lin);
        company_city_lin.setOnClickListener(this);

        company_type_lin = findViewById(R.id.company_type_lin);
        company_type_lin.setOnClickListener(this);
        business_nature_lin = findViewById(R.id.business_nature_lin);
        business_nature_lin.setOnClickListener(this);

        company_country_img = findViewById(R.id.company_country_img);

        //---------------------------------------Company Details------------------------------------------------

        drop_arrow_company_details_img = findViewById(R.id.drop_arrow_company_details_img);
        drop_arrow_company_kyc_img = findViewById(R.id.drop_arrow_company_kyc_img);
        drop_arrow_basic_details_img = findViewById(R.id.drop_arrow_basic_details_img);
        drop_arrow_auth_img = findViewById(R.id.drop_arrow_auth_img);
        drop_passport_img = findViewById(R.id.drop_passport_img);

        company_kyc_rel = findViewById(R.id.company_kyc_rel);
        company_kyc_rel.setOnClickListener(this);

        company_details_lin = findViewById(R.id.company_details_lin);
        company_kyc_lin = findViewById(R.id.company_kyc_lin);
        basic_details_lin = findViewById(R.id.basic_details_lin);
        auth_person_lin = findViewById(R.id.auth_person_lin);
        passport_lin = findViewById(R.id.passport_lin);

        company_details_rel = findViewById(R.id.company_details_rel);
        company_details_rel.setOnClickListener(this);

        basic_details_rel = findViewById(R.id.basic_details_rel);
        basic_details_rel.setOnClickListener(this);

        auth_person_rel = findViewById(R.id.auth_person_rel);
        auth_person_rel.setOnClickListener(this);

        passport_rel = findViewById(R.id.passport_rel);
        passport_rel.setOnClickListener(this);

        date_of_birth_tv = findViewById(R.id.date_of_birth_tv);

        dob_lin = findViewById(R.id.dob_lin);
        dob_lin.setOnClickListener(this);

        company_gst_no_rel = findViewById(R.id.company_gst_no_rel);
        company_gst_no_et = findViewById(R.id.company_gst_no_et);
        company_gst_no_verify_img = findViewById(R.id.company_gst_no_verify_img);
        company_gst_no_error_tv = findViewById(R.id.company_gst_no_error_tv);

        company_gst_no_verify_lin = findViewById(R.id.company_gst_no_verify_lin);
        company_gst_no_verify_lin.setOnClickListener(this);

        company_gst_doc_verify_img = findViewById(R.id.company_gst_doc_verify_img);
        company_gst_doc_verify_img.setOnClickListener(this);

        upload_gst_doc_lin = findViewById(R.id.upload_gst_doc_lin);
        upload_gst_doc_lin.setOnClickListener(this);

        pan_verify_img = findViewById(R.id.pan_verify_img);
        company_pan_no_et = findViewById(R.id.company_pan_no_et);
        company_iec_no_et = findViewById(R.id.company_iec_no_et);
        company_pan_no_error_tv = findViewById(R.id.company_pan_no_error_tv);
        company_pan_back_rel = findViewById(R.id.company_pan_back_rel);

        company_pan_no_verify_lin = findViewById(R.id.company_pan_no_verify_lin);
        company_pan_no_verify_lin.setOnClickListener(this);

        upload_pan_doc_lin = findViewById(R.id.upload_pan_doc_lin);
        upload_pan_doc_lin.setOnClickListener(this);

        company_pan_doc_verify_img = findViewById(R.id.company_pan_doc_verify_img);
        company_pan_doc_verify_img.setOnClickListener(this);

        upload_iec_doc_lin = findViewById(R.id.upload_iec_doc_lin);
        upload_iec_doc_lin.setOnClickListener(this);

        company_iec_doc_verify_img = findViewById(R.id.company_iec_doc_verify_img);
        company_iec_doc_verify_img.setOnClickListener(this);

        company_name_et = findViewById(R.id.company_name_et);
        company_email_et = findViewById(R.id.company_email_et);
        company_mobile_number_et = findViewById(R.id.company_mobile_number_et);
        company_pincode_et = findViewById(R.id.company_pincode_et);
        pincode_lin = findViewById(R.id.pincode_lin);
        company_pincode_error_tv = findViewById(R.id.company_pincode_error_tv);
        company_address_line1_et = findViewById(R.id.company_address_line1_et);
        company_address_line2_et = findViewById(R.id.company_address_line2_et);

        company_name_error_tv = findViewById(R.id.company_name_error_tv);
        company_email_error_tv = findViewById(R.id.company_email_error_tv);
        company_country_code = findViewById(R.id.company_country_code);
        company_mobile_number_error_tv = findViewById(R.id.company_mobile_number_error_tv);

        company_country_tv = findViewById(R.id.company_country_tv);
        company_country_tv.setOnClickListener(this);

        company_country_error_tv = findViewById(R.id.company_country_error_tv);

        company_state_tv = findViewById(R.id.company_state_tv);
        company_state_tv.setOnClickListener(this);

        company_state_error_tv = findViewById(R.id.company_state_error_tv);

        company_city_tv = findViewById(R.id.company_city_tv);
        company_city_tv.setOnClickListener(this);

        company_city_error_tv = findViewById(R.id.company_city_error_tv);
        company_address_line1_error_tv = findViewById(R.id.company_address_line1_error_tv);

        company_type_tv = findViewById(R.id.company_type_tv);
        company_type_tv.setOnClickListener(this);

        company_type_error_tv = findViewById(R.id.company_type_error_tv);

        business_nature_tv = findViewById(R.id.business_nature_tv);
        business_nature_tv.setOnClickListener(this);

        business_nature_error_tv = findViewById(R.id.business_nature_error_tv);
        company_pincode_error_tv = findViewById(R.id.company_pincode_error_tv);

        company_pan_doc_error_tv = findViewById(R.id.company_pan_doc_error_tv);
        company_pan_doc_rel = findViewById(R.id.company_pan_doc_rel);

        supplier_company_type_lin = findViewById(R.id.supplier_company_type_lin);
        supplier_company_type_lin.setOnClickListener(this);
        supplier_company_type_tv = findViewById(R.id.supplier_company_type_tv);
        supplier_company_type_error_tv = findViewById(R.id.supplier_company_type_error_tv);

        supplier_business_nature_lin = findViewById(R.id.supplier_business_nature_lin);
        supplier_business_nature_lin.setOnClickListener(this);
        supplier_business_nature_tv = findViewById(R.id.supplier_business_nature_tv);
        supplier_business_nature_error_tv = findViewById(R.id.supplier_business_nature_error_tv);

        company_business_lic_no_rel = findViewById(R.id.company_business_lic_no_rel);
        company_business_lic_no_et = findViewById(R.id.company_business_lic_no_et);
        company_business_lic_no_verify_lin = findViewById(R.id.company_business_lic_no_verify_lin);
        company_business_lic_no_verify_lin.setOnClickListener(this);
        company_business_lic_no_verify_img = findViewById(R.id.company_business_lic_no_verify_img);
        company_business_lic_no_error_tv = findViewById(R.id.company_business_lic_no_error_tv);
        supplier_company_gst_no_verify_img = findViewById(R.id.supplier_company_gst_no_verify_img);

        supplier_email_verify_img = findViewById(R.id.supplier_email_verify_img);
        supplier_verify_email_lin = findViewById(R.id.supplier_verify_email_lin);
        supplier_verify_email_lin.setOnClickListener(this);

        suppier_business_licence_lin = findViewById(R.id.suppier_business_licence_lin);
        suppier_gst_pan_no_lin = findViewById(R.id.suppier_gst_pan_no_lin);

        company_business_lic_doc_rel = findViewById(R.id.company_business_lic_doc_rel);
        upload_business_lic_doc_lin = findViewById(R.id.upload_business_lic_doc_lin);
        upload_business_lic_doc_lin.setOnClickListener(this);
        company_business_lic_doc_verify_img = findViewById(R.id.company_business_lic_doc_verify_img);
        company_business_lic_doc_verify_img.setOnClickListener(this);
        company_business_lic_doc_error_tv = findViewById(R.id.company_business_lic_doc_error_tv);

        suppier_business_lic_no_rel = findViewById(R.id.suppier_business_lic_no_rel);
        suppier_business_lic_no_et = findViewById(R.id.suppier_business_lic_no_et);
        suppier_business_lic_no_verify_lin = findViewById(R.id.suppier_business_lic_no_verify_lin);
        suppier_business_lic_no_verify_lin.setOnClickListener(this);
        suppier_business_lic_no_verify_img = findViewById(R.id.suppier_business_lic_no_verify_img);
        suppier_business_lic_no_error_tv = findViewById(R.id.suppier_business_lic_no_error_tv);

        suppier_business_lic_doc_rel = findViewById(R.id.suppier_business_lic_doc_rel);
        suppier_upload_business_lic_doc_lin = findViewById(R.id.suppier_upload_business_lic_doc_lin);
        suppier_upload_business_lic_doc_lin.setOnClickListener(this);
        suppier_business_lic_doc_verify_img = findViewById(R.id.suppier_business_lic_doc_verify_img);
        suppier_business_lic_doc_verify_img.setOnClickListener(this);
        suppier_business_lic_doc_error_tv = findViewById(R.id.suppier_business_lic_doc_error_tv);


        supplier_company_pan_back_rel = findViewById(R.id.supplier_company_pan_back_rel);
        supplier_company_pan_no_et = findViewById(R.id.supplier_company_pan_no_et);
        supplier_company_pan_no_verify_lin = findViewById(R.id.supplier_company_pan_no_verify_lin);
        supplier_company_pan_no_verify_lin.setOnClickListener(this);
        supplier_company_pan_no_verify_img = findViewById(R.id.supplier_company_pan_no_verify_img);
        supplier_company_pan_no_error_tv = findViewById(R.id.supplier_company_pan_no_error_tv);

        supplier_company_pan_doc_rel = findViewById(R.id.supplier_company_pan_doc_rel);
        supplier_upload_pan_doc_lin = findViewById(R.id.supplier_upload_pan_doc_lin);
        supplier_upload_pan_doc_lin.setOnClickListener(this);
        supplier_company_pan_doc_error_tv = findViewById(R.id.supplier_company_pan_doc_error_tv);
        supplier_company_pan_doc_verify_img = findViewById(R.id.supplier_company_pan_doc_verify_img);
        supplier_company_pan_doc_verify_img.setOnClickListener(this);

        supplier_company_iec_no_et = findViewById(R.id.supplier_company_iec_no_et);
        supplier_company_iec_doc_rel = findViewById(R.id.supplier_company_iec_doc_rel);
        supplier_upload_iec_doc_lin = findViewById(R.id.supplier_upload_iec_doc_lin);
        supplier_upload_iec_doc_lin.setOnClickListener(this);
        supplier_company_iec_doc_verify_img = findViewById(R.id.supplier_company_iec_doc_verify_img);
        supplier_company_iec_doc_verify_img.setOnClickListener(this);

        basic_mobile_number_lin = findViewById(R.id.basic_mobile_number_lin);
        basic_country_img = findViewById(R.id.basic_country_img);
        basic_country_code = findViewById(R.id.basic_country_code);
        basic_mobile_number_et = findViewById(R.id.basic_mobile_number_et);
        basic_mobile_number_error_tv = findViewById(R.id.basic_mobile_number_error_tv);
        basic_country_code_lin = findViewById(R.id.basic_country_code_lin);
        basic_country_code_lin.setOnClickListener(this);

        basic_first_name_et = findViewById(R.id.basic_first_name_et);
        basic_last_name_et = findViewById(R.id.basic_last_name_et);
        basic_email_et = findViewById(R.id.basic_email_et);
        basic_password_et = findViewById(R.id.basic_password_et);
        basic_confirm_password_et = findViewById(R.id.basic_confirm_password_et);

        basic_first_name_error_tv = findViewById(R.id.basic_first_name_error_tv);
        basic_last_name_error_tv = findViewById(R.id.basic_last_name_error_tv);
        basic_password_error_tv = findViewById(R.id.basic_password_error_tv);
        basic_confirm_password_error_tv = findViewById(R.id.basic_confirm_password_error_tv);

        basic_email_rel = findViewById(R.id.basic_email_rel);
        basic_verify_email_lin = findViewById(R.id.basic_verify_email_lin);
        basic_verify_email_lin.setOnClickListener(this);
        basic_email_error_tv = findViewById(R.id.basic_email_error_tv);
        basic_email_verify_img = findViewById(R.id.basic_email_verify_img);

        auth_person_card = findViewById(R.id.auth_person_card);

        auth_aadhar_no_verify_lin = findViewById(R.id.auth_aadhar_no_verify_lin);
        auth_aadhar_no_verify_lin.setOnClickListener(this);
        auth_back_rel = findViewById(R.id.auth_back_rel);
        auth_aadhar_no_et = findViewById(R.id.auth_aadhar_no_et);
        auth_aadhar_no_error_tv = findViewById(R.id.auth_aadhar_no_error_tv);
        auth_aadhar_no_verify_img = findViewById(R.id.auth_aadhar_no_verify_img);

        auth_pan_back_rel = findViewById(R.id.auth_pan_back_rel);
        auth_pan_no_et = findViewById(R.id.auth_pan_no_et);
        auth_pan_no_error_tv = findViewById(R.id.auth_pan_no_error_tv);
        auth_pan_no_verify_img = findViewById(R.id.auth_pan_no_verify_img);

        auth_pan_no_verify_lin = findViewById(R.id.auth_pan_no_verify_lin);
        auth_pan_no_verify_lin.setOnClickListener(this);

        auth_pan_doc_lin = findViewById(R.id.auth_pan_doc_lin);
        auth_pan_doc_lin.setOnClickListener(this);

        auth_pan_doc_verify_img = findViewById(R.id.auth_pan_doc_verify_img);
        auth_pan_doc_verify_img.setOnClickListener(this);


        upload_aadhar_front_lin = findViewById(R.id.upload_aadhar_front_lin);
        upload_aadhar_front_lin.setOnClickListener(this);

        auth_aadhar_front_doc_verify_img = findViewById(R.id.auth_aadhar_front_doc_verify_img);
        auth_aadhar_front_doc_verify_img.setOnClickListener(this);

        upload_aadhar_back_lin = findViewById(R.id.upload_aadhar_back_lin);
        upload_aadhar_back_lin.setOnClickListener(this);

        auth_aadhar_back_doc_verify_img = findViewById(R.id.auth_aadhar_back_doc_verify_img);
        auth_aadhar_back_doc_verify_img.setOnClickListener(this);

        passport_no_et = findViewById(R.id.passport_no_et);

        upload_passport_front_lin = findViewById(R.id.upload_passport_front_lin);
        upload_passport_front_lin.setOnClickListener(this);

        passport_front_doc_verify_img = findViewById(R.id.passport_front_doc_verify_img);
        passport_front_doc_verify_img.setOnClickListener(this);

        upload_passport_back_lin = findViewById(R.id.upload_passport_back_lin);
        upload_passport_back_lin.setOnClickListener(this);

        passport_back_doc_verify_img = findViewById(R.id.passport_back_doc_verify_img);
        passport_back_doc_verify_img.setOnClickListener(this);

        driving_lic_doc_lin = findViewById(R.id.driving_lic_doc_lin);
        driving_lic_doc_lin.setOnClickListener(this);

        driving_lic_doc_verify_img = findViewById(R.id.driving_lic_doc_verify_img);
        driving_lic_doc_verify_img.setOnClickListener(this);

        driving_lic_bacl_rel = findViewById(R.id.driving_lic_bacl_rel);
        driving_lic_no_et = findViewById(R.id.driving_lic_no_et);
        driving_lic_no_verify_lin = findViewById(R.id.driving_lic_no_verify_lin);
        driving_lic_no_verify_lin.setOnClickListener(this);
        driving_lic_no_verify_img = findViewById(R.id.driving_lic_no_verify_img);
        driving_lic_error_required_tv = findViewById(R.id.driving_lic_error_required_tv);

        //---------------------------------------Company ID End-----------------------------------------------------

        //--------------------------------------Supplier Details-----------------------------------------------------

         supplier_company_details_rel = findViewById(R.id.supplier_company_details_rel);
         supplier_company_details_rel.setOnClickListener(this);

        supplier_company_kyc_rel = findViewById(R.id.supplier_company_kyc_rel);
        supplier_company_kyc_rel.setOnClickListener(this);

        bank_info_rel = findViewById(R.id.bank_info_rel);
        bank_info_rel.setOnClickListener(this);

        superviosr_details_rel = findViewById(R.id.superviosr_details_rel);
        superviosr_details_rel.setOnClickListener(this);

        drop_arrow_supplier_company_details_img = findViewById(R.id.drop_arrow_supplier_company_details_img);
        drop_arrow_supplier_kyc_img = findViewById(R.id.drop_arrow_supplier_kyc_img);
        bank_details_img = findViewById(R.id.bank_details_img);
        drop_arrow_superviosr_details_img = findViewById(R.id.drop_arrow_superviosr_details_img);

        supplier_company_details_lin = findViewById(R.id.supplier_company_details_lin);
        supplier_company_kyc_lin = findViewById(R.id.supplier_company_kyc_lin);
        bank_details_lin = findViewById(R.id.bank_details_lin);
        superviosr_details_lin = findViewById(R.id.superviosr_details_lin);

        supplier_company_name_et = findViewById(R.id.supplier_company_name_et);
        supplier_company_email_et = findViewById(R.id.supplier_company_email_et);
        supplier_password_et = findViewById(R.id.supplier_password_et);
        supplier_confirm_password_et = findViewById(R.id.supplier_confirm_password_et);
        supplier_mobile_number_et = findViewById(R.id.supplier_mobile_number_et);
        supplier_address_line1_et = findViewById(R.id.supplier_address_line1_et);
        supplier_address_line2_et = findViewById(R.id.supplier_address_line2_et);
        supplier_pincode_et = findViewById(R.id.supplier_pincode_et);

        supplier_company_gst_no_rel = findViewById(R.id.supplier_company_gst_no_rel);
        supplier_company_gst_no_et = findViewById(R.id.supplier_company_gst_no_et);
        supplier_company_gst_no_verify_lin = findViewById(R.id.supplier_company_gst_no_verify_lin);
        supplier_company_gst_no_verify_lin.setOnClickListener(this);
        supplier_company_gst_no_error_tv = findViewById(R.id.supplier_company_gst_no_error_tv);
        supplier_company_gst_doc_rel = findViewById(R.id.supplier_company_gst_doc_rel);
        supplier_upload_gst_doc_lin = findViewById(R.id.supplier_upload_gst_doc_lin);
        supplier_upload_gst_doc_lin.setOnClickListener(this);
        supplier_company_gst_doc_verify_img = findViewById(R.id.supplier_company_gst_doc_verify_img);
        supplier_company_gst_doc_verify_img.setOnClickListener(this);

        bank_name_et = findViewById(R.id.bank_name_et);
        branch_name_et = findViewById(R.id.branch_name_et);
        bank_account_no_et = findViewById(R.id.bank_account_no_et);
        bank_account_type_et = findViewById(R.id.bank_account_type_et);
        ifsc_code_et = findViewById(R.id.ifsc_code_et);
        bank_swift_code_et = findViewById(R.id.bank_swift_code_et);

        superviosr_name_et = findViewById(R.id.superviosr_name_et);
        superviosr_name_error_tv = findViewById(R.id.superviosr_name_error_tv);
        superviosr_email_et = findViewById(R.id.superviosr_email_et);
        superviosremail_error_tv = findViewById(R.id.superviosremail_error_tv);

        superviosr_mobile_number_lin = findViewById(R.id.superviosr_mobile_number_lin);

        superviosr_country_code_lin = findViewById(R.id.superviosr_country_code_lin);
        superviosr_country_code_lin.setOnClickListener(this);

        superviosr_country_img = findViewById(R.id.superviosr_country_img);
        superviosr_country_code = findViewById(R.id.superviosr_country_code);
        superviosr_mobile_number_et = findViewById(R.id.superviosr_mobile_number_et);
        superviosr_mobile_number_error_tv = findViewById(R.id.superviosr_mobile_number_error_tv);

        suppliercompany_name_error_tv = findViewById(R.id.suppliercompany_name_error_tv);
        supplier__email_error_tv = findViewById(R.id.supplier__email_error_tv);
        supplier_password_error_tv = findViewById(R.id.supplier_password_error_tv);
        supplier_confirm_password_error_tv = findViewById(R.id.supplier_confirm_password_error_tv);
        supplier_mobile_number_error_tv = findViewById(R.id.supplier_mobile_number_error_tv);
        supplier_country_code = findViewById(R.id.supplier_country_code);
        inventory_error_tv = findViewById(R.id.inventory_error_tv);
        supplier_country_tv = findViewById(R.id.supplier_country_tv);
        supplier_country_error_tv = findViewById(R.id.supplier_country_error_tv);
        supplier_state_tv = findViewById(R.id.supplier_state_tv);
        supplier_state_error_tv = findViewById(R.id.supplier_state_error_tv);
        supplier_city_tv = findViewById(R.id.supplier_city_tv);
        supplier_city_error_tv = findViewById(R.id.supplier_city_error_tv);
        supplier_address_line1_error_tv = findViewById(R.id.supplier_address_line1_error_tv);
        supplier_pincode_error_tv = findViewById(R.id.supplier_pincode_error_tv);

        supplier_mobile_number_lin = findViewById(R.id.supplier_mobile_number_lin);
        supplier_country_code_lin = findViewById(R.id.supplier_country_code_lin);
        supplier_country_code_lin.setOnClickListener(this);
        supplier_country_lin = findViewById(R.id.supplier_country_lin);
        supplier_country_lin.setOnClickListener(this);
        supplier_state_lin = findViewById(R.id.supplier_state_lin);
        supplier_state_lin.setOnClickListener(this);
        supplier_city_lin = findViewById(R.id.supplier_city_lin);
        supplier_city_lin.setOnClickListener(this);
        supplier_country_img = findViewById(R.id.supplier_country_img);

        inventory_tv = findViewById(R.id.inventory_tv);

        inventory_lin = findViewById(R.id.inventory_lin);
        inventory_lin.setOnClickListener(this);

        create_account_tv = findViewById(R.id.create_account_tv);
        create_account_tv.setOnClickListener(this);

        Picasso.with(context)
                .load(INDIA_FLAG_URL)
                .into(country_img);
        country_code.setText(INDIA_COUNTRY_CODE);
        country_code.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down_icon, 0);

        //terms_condition_check = findViewById(R.id.terms_condition_check);

        termsConditionCheck = findViewById(R.id.terms_condition_check);

        terms_condition_tv = findViewById(R.id.terms_condition_tv);
        terms_condition_tv.setOnClickListener(this);

        already_login_tv = findViewById(R.id.already_login_tv);
        already_login_tv.setOnClickListener(this);

        termsConditionCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (termsConditionCheck.isChecked()) {
                    //Toast.makeText(getApplicationContext(), "Terms and conditions accepted", Toast.LENGTH_SHORT).show();
                    create_account_tv.setAlpha(1F);
                } else {
                    //Toast.makeText(getApplicationContext(), "Please accept terms and conditions", Toast.LENGTH_SHORT).show();
                    create_account_tv.setAlpha(0.4F);
                }
            }
        });

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

        removeValidationError();

    }

    void removeValidationError()
    {
        first_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!first_name_et.getText().toString().equalsIgnoreCase("")){
                    first_name_error_tv.setVisibility(View.GONE);
                    first_name_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(first_name_et.getText().toString().equalsIgnoreCase("")){
                    first_name_error_tv.setVisibility(View.GONE);
                    first_name_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        last_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!last_name_et.getText().toString().equalsIgnoreCase("")){
                    last_name_error_tv.setVisibility(View.GONE);
                    last_name_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(last_name_et.getText().toString().equalsIgnoreCase("")){
                    last_name_error_tv.setVisibility(View.GONE);
                    last_name_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        mobile_number_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!mobile_number_et.getText().toString().equalsIgnoreCase("")){
                    mobile_number_error_tv.setVisibility(View.GONE);
                    mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(mobile_number_et.getText().toString().equalsIgnoreCase("")){
                    mobile_number_error_tv.setVisibility(View.GONE);
                    mobile_number_lin.setBackgroundResource(R.drawable.border_line_view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        email_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!email_et.getText().toString().equalsIgnoreCase("")){
                    email_error_tv.setVisibility(View.GONE);
                    email_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(email_et.getText().toString().equalsIgnoreCase("")){
                    email_error_tv.setVisibility(View.GONE);
                    email_et.setBackgroundResource(R.drawable.border_line_view);
                }
                // check Email Verify for Not and Check Email Change or Not
                if(isEmailVerified)
                {
                    if(verifiedEmail.equalsIgnoreCase(email_et.getText().toString().toString()))
                    {
                        emailVerifyOrNot = "done";
                        buyer_email_verify_img.setVisibility(View.VISIBLE);
                        verify_email_lin.setVisibility(View.GONE);
                    }
                    else
                    {
                        emailVerifyOrNot = "";
                        buyer_email_verify_img.setVisibility(View.GONE);
                        verify_email_lin.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!password_et.getText().toString().equalsIgnoreCase("")){
                    password_error_tv.setVisibility(View.GONE);
                    password_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(password_et.getText().toString().equalsIgnoreCase("")){
                    password_error_tv.setVisibility(View.GONE);
                    password_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirm_password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!confirm_password_et.getText().toString().equalsIgnoreCase("")){
                    confirm_password_error_tv.setVisibility(View.GONE);
                    confirm_password_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(confirm_password_et.getText().toString().equalsIgnoreCase("")){
                    confirm_password_error_tv.setVisibility(View.GONE);
                    confirm_password_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        pincode_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!pincode_et.getText().toString().equalsIgnoreCase("")){
                    pincode_error_tv.setVisibility(View.GONE);
                    pincode_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(pincode_et.getText().toString().equalsIgnoreCase("")){
                    pincode_error_tv.setVisibility(View.GONE);
                    pincode_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        address_line1_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!address_line1_et.getText().toString().equalsIgnoreCase("")){
                    address_line1_error_tv.setVisibility(View.GONE);
                    address_line1_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(address_line1_et.getText().toString().equalsIgnoreCase("")){
                    address_line1_error_tv.setVisibility(View.GONE);
                    address_line1_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        //---------------------------------- Dealer Signup Field--------------------------------------------

        company_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!company_name_et.getText().toString().equalsIgnoreCase("")){
                    company_name_error_tv.setVisibility(View.GONE);
                    company_name_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(company_name_et.getText().toString().equalsIgnoreCase("")){
                    company_name_error_tv.setVisibility(View.GONE);
                    company_name_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        company_email_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!company_email_et.getText().toString().equalsIgnoreCase("")){
                    company_email_error_tv.setVisibility(View.GONE);
                    company_email_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(company_email_et.getText().toString().equalsIgnoreCase("")){
                    company_email_error_tv.setVisibility(View.GONE);
                    company_email_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        company_mobile_number_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!company_mobile_number_et.getText().toString().equalsIgnoreCase("")){
                    company_mobile_number_error_tv.setVisibility(View.GONE);
                    company_mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(company_mobile_number_et.getText().toString().equalsIgnoreCase("")){
                    company_mobile_number_error_tv.setVisibility(View.GONE);
                    company_mobile_number_lin.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        company_pincode_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!company_pincode_et.getText().toString().equalsIgnoreCase("")){
                    company_pincode_error_tv.setVisibility(View.GONE);
                    company_pincode_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(company_pincode_et.getText().toString().equalsIgnoreCase("")){
                    company_pincode_error_tv.setVisibility(View.GONE);
                    company_pincode_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        company_address_line1_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!company_address_line1_et.getText().toString().equalsIgnoreCase("")){
                    company_address_line1_error_tv.setVisibility(View.GONE);
                    company_address_line1_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(company_address_line1_et.getText().toString().equalsIgnoreCase("")){
                    company_address_line1_error_tv.setVisibility(View.GONE);
                    company_address_line1_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        company_business_lic_no_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!company_business_lic_no_et.getText().toString().equalsIgnoreCase("")){
                    company_business_lic_no_error_tv.setVisibility(View.GONE);
                    company_business_lic_no_rel.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(company_business_lic_no_et.getText().toString().equalsIgnoreCase("")){
                    company_business_lic_no_error_tv.setVisibility(View.GONE);
                    company_business_lic_no_rel.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        company_pan_no_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!company_pan_no_et.getText().toString().equalsIgnoreCase("")){
                    company_pan_no_error_tv.setVisibility(View.GONE);
                    company_pan_back_rel.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(company_pan_no_et.getText().toString().equalsIgnoreCase("")){
                    company_pan_no_error_tv.setVisibility(View.GONE);
                    company_pan_back_rel.setBackgroundResource(R.drawable.border_line_view);
                }

                // check Pan No Verify for Not and Check Pan No Change or Not
                if(isCompanyPanNoVerified)
                {
                    if(companyPanNoVerified.equalsIgnoreCase(company_pan_no_et.getText().toString().toString()))
                    {
                        pan_verify_img.setVisibility(View.VISIBLE);
                        company_pan_no_verify_lin.setVisibility(View.GONE);
                    }
                    else
                    {
                        pan_verify_img.setVisibility(View.GONE);
                        company_pan_no_verify_lin.setVisibility(View.VISIBLE);
                    }
                }else{}
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
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
                if(isCompanyGSTNoVerified)
                {
                    if(companyGstNoVerified.equalsIgnoreCase(company_gst_no_et.getText().toString().toString()))
                    {
                        company_gst_no_verify_img.setVisibility(View.VISIBLE);
                        company_gst_no_verify_lin.setVisibility(View.GONE);
                    }
                    else
                    {
                        company_gst_no_verify_img.setVisibility(View.GONE);
                        company_gst_no_verify_lin.setVisibility(View.VISIBLE);
                    }
                }else{}
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        basic_first_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!basic_first_name_et.getText().toString().equalsIgnoreCase("")){
                    basic_first_name_error_tv.setVisibility(View.GONE);
                    basic_first_name_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(basic_first_name_et.getText().toString().equalsIgnoreCase("")){
                    basic_first_name_error_tv.setVisibility(View.GONE);
                    basic_first_name_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        basic_last_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!basic_last_name_et.getText().toString().equalsIgnoreCase("")){
                    basic_last_name_error_tv.setVisibility(View.GONE);
                    basic_last_name_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(basic_last_name_et.getText().toString().equalsIgnoreCase("")){
                    basic_last_name_error_tv.setVisibility(View.GONE);
                    basic_last_name_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        basic_mobile_number_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!basic_mobile_number_et.getText().toString().equalsIgnoreCase("")){
                    basic_mobile_number_error_tv.setVisibility(View.GONE);
                    basic_mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(basic_mobile_number_et.getText().toString().equalsIgnoreCase("")){
                    basic_mobile_number_error_tv.setVisibility(View.GONE);
                    basic_mobile_number_lin.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        basic_email_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!basic_email_et.getText().toString().equalsIgnoreCase("")){
                    basic_email_error_tv.setVisibility(View.GONE);
                    basic_email_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(basic_email_et.getText().toString().equalsIgnoreCase("")){
                    basic_email_error_tv.setVisibility(View.GONE);
                    basic_email_et.setBackgroundResource(R.drawable.border_line_view);
                }
                // check Email Verify for Not and Check Email Change or Not
                if(isBasicEmailVerified)
                {
                    if(basicVerifiedEmail.equalsIgnoreCase(basic_email_et.getText().toString().toString()))
                    {
                        basicEmailVerifyOrNot = "done";
                        basic_email_verify_img.setVisibility(View.VISIBLE);
                        basic_verify_email_lin.setVisibility(View.GONE);
                    }
                    else
                    {
                        basicEmailVerifyOrNot = "";
                        basic_email_verify_img.setVisibility(View.GONE);
                        basic_verify_email_lin.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        basic_password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!basic_password_et.getText().toString().equalsIgnoreCase("")){
                    basic_password_error_tv.setVisibility(View.GONE);
                    basic_password_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(basic_password_et.getText().toString().equalsIgnoreCase("")){
                    basic_password_error_tv.setVisibility(View.GONE);
                    basic_password_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        basic_confirm_password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!basic_confirm_password_et.getText().toString().equalsIgnoreCase("")){
                    basic_confirm_password_error_tv.setVisibility(View.GONE);
                    basic_confirm_password_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(basic_confirm_password_et.getText().toString().equalsIgnoreCase("")){
                    basic_confirm_password_error_tv.setVisibility(View.GONE);
                    basic_confirm_password_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        auth_aadhar_no_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!auth_aadhar_no_et.getText().toString().equalsIgnoreCase("")){
                    auth_aadhar_no_error_tv.setVisibility(View.GONE);
                    auth_back_rel.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(auth_aadhar_no_et.getText().toString().equalsIgnoreCase("")){
                    auth_aadhar_no_error_tv.setVisibility(View.GONE);
                    auth_back_rel.setBackgroundResource(R.drawable.border_line_view);
                }

                // check Auth Person Aadhar No for Not and Check Aadhar no or Not
                if(isAuthPersonAadharNo)
                {
                    if(authPersonVerifiedAadharNo.equalsIgnoreCase(auth_aadhar_no_et.getText().toString().toString()))
                    {
                        auth_aadhar_no_verify_img.setVisibility(View.VISIBLE);
                        auth_aadhar_no_verify_lin.setVisibility(View.GONE);
                    }
                    else
                    {
                        auth_aadhar_no_verify_img.setVisibility(View.GONE);
                        auth_aadhar_no_verify_lin.setVisibility(View.VISIBLE);
                    }
                }else{}
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        auth_pan_no_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!auth_pan_no_et.getText().toString().equalsIgnoreCase("")){
                    auth_pan_no_error_tv.setVisibility(View.GONE);
                    auth_pan_back_rel.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(auth_pan_no_et.getText().toString().equalsIgnoreCase("")){
                    auth_pan_no_error_tv.setVisibility(View.GONE);
                    auth_pan_back_rel.setBackgroundResource(R.drawable.border_line_view);
                }

                // check Auth Person Pan No for Not and Check Pan no or Not
                if(isAuthPersonPanNo)
                {
                    if(authPersonVerifiedPanNo.equalsIgnoreCase(auth_pan_no_et.getText().toString().toString()))
                    {
                        auth_pan_no_verify_img.setVisibility(View.VISIBLE);
                        auth_pan_no_verify_lin.setVisibility(View.GONE);
                    }
                    else
                    {
                        auth_pan_no_verify_img.setVisibility(View.GONE);
                        auth_pan_no_verify_lin.setVisibility(View.VISIBLE);
                    }
                }else{}
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        driving_lic_no_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!driving_lic_no_et.getText().toString().equalsIgnoreCase("")){
                    driving_lic_error_required_tv.setVisibility(View.GONE);
                    driving_lic_bacl_rel.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(driving_lic_no_et.getText().toString().equalsIgnoreCase("")){
                    driving_lic_error_required_tv.setVisibility(View.GONE);
                    driving_lic_bacl_rel.setBackgroundResource(R.drawable.border_line_view);
                }
                // check Driving license No for Not and Check Change Driving Licence or Not
                if(isDrivingLicenseNo)
                {
                    if(drivingLicenseVerifiedNo.equalsIgnoreCase(driving_lic_no_et.getText().toString().toString()))
                    {
                        driving_lic_no_verify_img.setVisibility(View.VISIBLE);
                        driving_lic_no_verify_lin.setVisibility(View.GONE);
                    }
                    else
                    {
                        driving_lic_no_verify_img.setVisibility(View.GONE);
                        driving_lic_no_verify_lin.setVisibility(View.VISIBLE);
                    }
                }else{}
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        //-------------------------------Supplier Fields----------------------------------------------

        supplier_company_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!supplier_company_name_et.getText().toString().equalsIgnoreCase("")){
                    suppliercompany_name_error_tv.setVisibility(View.GONE);
                    supplier_company_name_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(supplier_company_name_et.getText().toString().equalsIgnoreCase("")){
                    suppliercompany_name_error_tv.setVisibility(View.GONE);
                    supplier_company_name_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        supplier_company_email_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!supplier_company_email_et.getText().toString().equalsIgnoreCase("")){
                    supplier__email_error_tv.setVisibility(View.GONE);
                    supplier_company_email_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(supplier_company_email_et.getText().toString().equalsIgnoreCase("")){
                    supplier__email_error_tv.setVisibility(View.GONE);
                    supplier_company_email_et.setBackgroundResource(R.drawable.border_line_view);
                }
                // check Email Verify for Not and Check Email Change or Not
                if(isSupplierEmailVerified)
                {
                    if(supplierVerifiedEmail.equalsIgnoreCase(supplier_company_email_et.getText().toString().toString()))
                    {
                        supplierEmailVerifyOrNot = "done";
                        supplier_email_verify_img.setVisibility(View.VISIBLE);
                        supplier_verify_email_lin.setVisibility(View.GONE);
                    }
                    else
                    {
                        supplierEmailVerifyOrNot = "";
                        supplier_email_verify_img.setVisibility(View.GONE);
                        supplier_verify_email_lin.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        supplier_password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!supplier_password_et.getText().toString().equalsIgnoreCase("")){
                    supplier_password_error_tv.setVisibility(View.GONE);
                    supplier_password_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(supplier_password_et.getText().toString().equalsIgnoreCase("")){
                    supplier_password_error_tv.setVisibility(View.GONE);
                    supplier_password_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        supplier_confirm_password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!supplier_confirm_password_et.getText().toString().equalsIgnoreCase("")){
                    supplier_confirm_password_error_tv.setVisibility(View.GONE);
                    supplier_confirm_password_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(supplier_confirm_password_et.getText().toString().equalsIgnoreCase("")){
                    supplier_confirm_password_error_tv.setVisibility(View.GONE);
                    supplier_confirm_password_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        supplier_mobile_number_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!supplier_mobile_number_et.getText().toString().equalsIgnoreCase("")){
                    supplier_mobile_number_error_tv.setVisibility(View.GONE);
                    supplier_mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(supplier_mobile_number_et.getText().toString().equalsIgnoreCase("")){
                    supplier_mobile_number_error_tv.setVisibility(View.GONE);
                    supplier_mobile_number_lin.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        supplier_address_line1_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!supplier_address_line1_et.getText().toString().equalsIgnoreCase("")){
                    supplier_address_line1_error_tv.setVisibility(View.GONE);
                    supplier_address_line1_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(supplier_address_line1_et.getText().toString().equalsIgnoreCase("")){
                    supplier_address_line1_error_tv.setVisibility(View.GONE);
                    supplier_address_line1_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        supplier_pincode_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!supplier_pincode_et.getText().toString().equalsIgnoreCase("")){
                    supplier_pincode_error_tv.setVisibility(View.GONE);
                    supplier_pincode_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(supplier_pincode_et.getText().toString().equalsIgnoreCase("")){
                    supplier_pincode_error_tv.setVisibility(View.GONE);
                    supplier_pincode_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });


        supplier_company_gst_no_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!supplier_company_gst_no_et.getText().toString().equalsIgnoreCase("")){
                    supplier_company_gst_no_error_tv.setVisibility(View.GONE);
                    supplier_company_gst_no_rel.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(supplier_company_gst_no_et.getText().toString().equalsIgnoreCase("")){
                    supplier_company_gst_no_error_tv.setVisibility(View.GONE);
                    supplier_company_gst_no_rel.setBackgroundResource(R.drawable.border_line_view);
                }

                // check GST No Verify for Not and Check GST No Change or Not
                if(isSupplierGSTNoVerified)
                {
                    if(supplierGSTNoVerified.equals(supplier_company_gst_no_et.getText().toString().toString()))
                    {
                        supplier_company_gst_no_verify_img.setVisibility(View.VISIBLE);
                        supplier_company_gst_no_verify_lin.setVisibility(View.GONE);
                    }
                    else
                    {
                        supplier_company_gst_no_verify_img.setVisibility(View.GONE);
                        supplier_company_gst_no_verify_lin.setVisibility(View.VISIBLE);
                    }
                }else{}
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        supplier_company_pan_no_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!supplier_company_pan_no_et.getText().toString().equalsIgnoreCase("")){
                    supplier_company_pan_no_error_tv.setVisibility(View.GONE);
                    supplier_company_pan_back_rel.setBackgroundResource(R.drawable.border_purple_line_view);
                }

                if(supplier_company_pan_no_et.getText().toString().equalsIgnoreCase("")){
                    supplier_company_pan_no_error_tv.setVisibility(View.GONE);
                    supplier_company_pan_back_rel.setBackgroundResource(R.drawable.border_line_view);
                }

                if(isSupplierPanNoVerified)
                {
                    if(supplierPanNoVerified.equalsIgnoreCase(supplier_company_pan_no_et.getText().toString().toString()))
                    {
                        supplier_company_pan_no_verify_img.setVisibility(View.VISIBLE);
                        supplier_company_pan_no_verify_lin.setVisibility(View.GONE);
                    }
                    else
                    {
                        supplier_company_pan_no_verify_img.setVisibility(View.GONE);
                        supplier_company_pan_no_verify_lin.setVisibility(View.VISIBLE);
                    }
                }else{}
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        superviosr_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!superviosr_name_et.getText().toString().equalsIgnoreCase("")){
                    superviosr_name_error_tv.setVisibility(View.GONE);
                    superviosr_name_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(superviosr_name_et.getText().toString().equalsIgnoreCase("")){
                    superviosr_name_error_tv.setVisibility(View.GONE);
                    superviosr_name_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        superviosr_email_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!superviosr_email_et.getText().toString().equalsIgnoreCase("")){
                    superviosremail_error_tv.setVisibility(View.GONE);
                    superviosr_email_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(superviosr_email_et.getText().toString().equalsIgnoreCase("")){
                    superviosremail_error_tv.setVisibility(View.GONE);
                    superviosr_email_et.setBackgroundResource(R.drawable.border_line_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        superviosr_mobile_number_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!superviosr_mobile_number_et.getText().toString().equalsIgnoreCase("")){
                    superviosr_mobile_number_error_tv.setVisibility(View.GONE);
                    superviosr_mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
                }
                if(superviosr_mobile_number_et.getText().toString().equalsIgnoreCase("")){
                    superviosr_mobile_number_error_tv.setVisibility(View.GONE);
                    superviosr_mobile_number_lin.setBackgroundResource(R.drawable.border_line_view);
                }
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
        else if(id == R.id.buyer_tv)
        {
            Utils.hideKeyboard(activity);
            selectedSignupTypeTab = BUYER;

            buyer_signup_card.setVisibility(View.VISIBLE);
            dealer_signup_card.setVisibility(View.GONE);
            supplier_signup_card.setVisibility(View.GONE);

            signup_lbl_tv.setText(getResources().getString(R.string.buyer_register_msg));

            buyer_lin.setBackgroundResource(R.drawable.background_button_shadow);
            dealer_lin.setBackgroundResource(R.drawable.background_white_view);
            supplier_lin.setBackgroundResource(R.drawable.background_white_view);

            buyer_tv.setTextColor(ContextCompat.getColor(activity, R.color.white));
            dealer_tv.setTextColor(ContextCompat.getColor(activity, R.color.purple));
            supplier_tv.setTextColor(ContextCompat.getColor(activity, R.color.purple));

            country_code.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down_icon, 0);
        }
        else if(id == R.id.dealer_tv)
        {
            Utils.hideKeyboard(activity);
            selectedSignupTypeTab = DEALER;


            buyer_signup_card.setVisibility(View.GONE);
            dealer_signup_card.setVisibility(View.VISIBLE);
            supplier_signup_card.setVisibility(View.GONE);

            isArrowDownCompanyDetails = true;
            drop_arrow_company_details_img.setImageResource(R.drawable.up);
            company_details_lin.setVisibility(View.VISIBLE);

            signup_lbl_tv.setText(getResources().getString(R.string.dealer_register_msg));

            buyer_lin.setBackgroundResource(R.drawable.background_white_view);
            dealer_lin.setBackgroundResource(R.drawable.background_button_shadow);
            supplier_lin.setBackgroundResource(R.drawable.background_white_view);

            buyer_tv.setTextColor(ContextCompat.getColor(activity, R.color.purple));
            dealer_tv.setTextColor(ContextCompat.getColor(activity, R.color.white));
            supplier_tv.setTextColor(ContextCompat.getColor(activity, R.color.purple));

            company_country_code.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down_icon, 0);
        }
        else if(id == R.id.supplier_tv)
        {
            Utils.hideKeyboard(activity);
            selectedSignupTypeTab = SUPPLIER;

            buyer_signup_card.setVisibility(View.GONE);
            dealer_signup_card.setVisibility(View.GONE);
            supplier_signup_card.setVisibility(View.VISIBLE);

            isArrowDownSupplierCompanyDetails = true;
            drop_arrow_supplier_company_details_img.setImageResource(R.drawable.up);
            supplier_company_details_lin.setVisibility(View.VISIBLE);

            signup_lbl_tv.setText(getResources().getString(R.string.supplier_register_msg));

            buyer_lin.setBackgroundResource(R.drawable.background_white_view);
            dealer_lin.setBackgroundResource(R.drawable.background_white_view);
            supplier_lin.setBackgroundResource(R.drawable.background_button_shadow);

            buyer_tv.setTextColor(ContextCompat.getColor(activity, R.color.purple));
            dealer_tv.setTextColor(ContextCompat.getColor(activity, R.color.purple));
            supplier_tv.setTextColor(ContextCompat.getColor(activity, R.color.white));

            supplier_country_code.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down_icon, 0);
        }
        else if(id == R.id.company_details_rel)
        {
            if (isArrowDownCompanyDetails) {
                drop_arrow_company_details_img.setImageResource(R.drawable.down);
                company_details_lin.setVisibility(View.GONE);
            } else {
                drop_arrow_company_details_img.setImageResource(R.drawable.up);
                company_details_lin.setVisibility(View.VISIBLE);
            }
            isArrowDownCompanyDetails = !isArrowDownCompanyDetails;
        }
        else if(id == R.id.company_kyc_rel)
        {
            if (isArrowDownCompanyKyc) {
                drop_arrow_company_kyc_img.setImageResource(R.drawable.down);
                company_kyc_lin.setVisibility(View.GONE);
            } else {
                drop_arrow_company_kyc_img.setImageResource(R.drawable.up);
                company_kyc_lin.setVisibility(View.VISIBLE);
            }
            isArrowDownCompanyKyc = !isArrowDownCompanyKyc;
        }
        else if(id == R.id.basic_details_rel)
        {
            if (isArrowDownCompanyBasic) {
                drop_arrow_basic_details_img.setImageResource(R.drawable.down);
                basic_details_lin.setVisibility(View.GONE);
            } else {
                drop_arrow_basic_details_img.setImageResource(R.drawable.up);
                basic_details_lin.setVisibility(View.VISIBLE);
            }
            isArrowDownCompanyBasic = !isArrowDownCompanyBasic;
        }
        else if(id == R.id.auth_person_rel)
        {
            if (isArrowDownCompanyAuth) {
                drop_arrow_auth_img.setImageResource(R.drawable.down);
                auth_person_lin.setVisibility(View.GONE);
            } else {
                drop_arrow_auth_img.setImageResource(R.drawable.up);
                auth_person_lin.setVisibility(View.VISIBLE);
            }
            isArrowDownCompanyAuth = !isArrowDownCompanyAuth;
        }
        else if(id == R.id.passport_rel)
        {
            if (isArrowDownCompanyPassport) {
                drop_passport_img.setImageResource(R.drawable.down);
                passport_lin.setVisibility(View.GONE);
            } else {
                drop_passport_img.setImageResource(R.drawable.up);
                passport_lin.setVisibility(View.VISIBLE);
            }
            isArrowDownCompanyPassport = !isArrowDownCompanyPassport;
        }
        else if(id == R.id.supplier_company_details_rel)
        {
            if (isArrowDownSupplierCompanyDetails) {
                drop_arrow_supplier_company_details_img.setImageResource(R.drawable.down);
                supplier_company_details_lin.setVisibility(View.GONE);
            } else {
                drop_arrow_supplier_company_details_img.setImageResource(R.drawable.up);
                supplier_company_details_lin.setVisibility(View.VISIBLE);
            }
            isArrowDownSupplierCompanyDetails = !isArrowDownSupplierCompanyDetails;
        }
        else if(id == R.id.supplier_company_kyc_rel)
        {
            if (isArrowDownSupplierCompanyKyc) {
                drop_arrow_supplier_kyc_img.setImageResource(R.drawable.down);
                supplier_company_kyc_lin.setVisibility(View.GONE);
            } else {
                drop_arrow_supplier_kyc_img.setImageResource(R.drawable.up);
                supplier_company_kyc_lin.setVisibility(View.VISIBLE);
            }
            isArrowDownSupplierCompanyKyc = !isArrowDownSupplierCompanyKyc;
        }
        else if(id == R.id.bank_info_rel)
        {
            if (isArrowDownBankInfo) {
                bank_details_img.setImageResource(R.drawable.down);
                bank_details_lin.setVisibility(View.GONE);
            } else {
                bank_details_img.setImageResource(R.drawable.up);
                bank_details_lin.setVisibility(View.VISIBLE);
            }
            isArrowDownBankInfo = !isArrowDownBankInfo;
        }
        else if(id == R.id.superviosr_details_rel)
        {
            if (isArrowDownSupervisor) {
                drop_arrow_superviosr_details_img.setImageResource(R.drawable.down);
                superviosr_details_lin.setVisibility(View.GONE);
            } else {
                drop_arrow_superviosr_details_img.setImageResource(R.drawable.up);
                superviosr_details_lin.setVisibility(View.VISIBLE);
            }
            isArrowDownSupervisor = !isArrowDownSupervisor;
        }
        else if(id == R.id.country_code_lin)
        {
            Utils.hideKeyboard(activity);
            countryCodeFor = "countryCodeBuyerSection";
            showCountryCode = "yes";
            getCountryListAPI(false);
        }
        else if(id == R.id.company_country_code_lin)
        {
            Utils.hideKeyboard(activity);
            countryCodeFor = "countryCodeCompanyDetails";
            showCountryCode = "yes";
            getCountryListAPI(false);
        }
        else if(id == R.id.supplier_country_code_lin)
        {
            Utils.hideKeyboard(activity);
            countryCodeFor = "supplierCompanyDetails";
            showCountryCode = "yes";
            getCountryListAPI(false);
        }
        else if(id == R.id.superviosr_country_code_lin)
        {
            Utils.hideKeyboard(activity);
            countryCodeFor = "supplierCountryCode";
            showCountryCode = "yes";
            getCountryListAPI(false);
        }
        else if(id == R.id.basic_country_code_lin)
        {
            Utils.hideKeyboard(activity);
            countryCodeFor = "basicDetailsNumber";
            showCountryCode = "yes";
            getCountryListAPI(false);
        }
        else if(id == R.id.verify_email_lin)
        {
           if(validateFieldsEmailVerifyOnly())
           {
               Utils.hideKeyboard(activity);
               resendOTP = "";
               onVerifyEmailAPI(false, "1", "");

           }else{}
        }
        else if(id == R.id.basic_verify_email_lin)
        {
            if(validateFieldsEmailVerifyOnly())
            {
                Utils.hideKeyboard(activity);
                basicResendOTP = "";
                onVerifyEmailAPI(false, "1", "");

            }else{}
        }
        else if(id == R.id.supplier_verify_email_lin)
        {
            if(validateFieldsEmailVerifyOnly())
            {
                Utils.hideKeyboard(activity);
                supplierResendOTP = "";
                onVerifyEmailAPI(false, "1", "");

            }else{}
        }
        else if(id == R.id.country_lin || id == R.id.country_tv)
        {
            if(!isApiCalling) // Check if API is not already calling
            {
                isApiCalling = true; // Set flag to true
                Utils.hideKeyboard(activity);
                countryCodeFor = "";
                showCountryCode = "";
                getCountryListAPI(false);
            } else{}
        }
        else if(id == R.id.state_lin || id == R.id.state_tv)
        {
            if(!country_tv.getText().toString().trim().equalsIgnoreCase(""))
            {
                if(!isApiCalling) // Check if API is not already calling
                {
                    isApiCalling = true; // Set flag to true
                    Utils.hideKeyboard(activity);
                    stateCodeFor = "";
                    getStateListAPI(false);
                } else{}
            }
            else
            {
                country_error_tv.setVisibility(View.VISIBLE);
                country_lin.setBackgroundResource(R.drawable.border_red_line_view);
            }
        }
        else if(id == R.id.city_lin  || id == R.id.city_tv)
        {
            Utils.hideKeyboard(activity);
            if(!state_tv.getText().toString().trim().equalsIgnoreCase(""))
            {
                if(!isApiCalling) // Check if API is not already calling
                {
                    isApiCalling = true; // Set flag to true
                    Utils.hideKeyboard(activity);
                    cityCodeFor="";
                    getCityListAPI(false);
                } else{}
            }
            else
            {
                state_error_tv.setVisibility(View.VISIBLE);
                state_lin.setBackgroundResource(R.drawable.border_red_line_view);
            }
        }
        else if(id == R.id.company_country_lin || id == R.id.company_country_tv)
        {
            if(!isApiCalling) // Check if API is not already calling
            {
                isApiCalling = true; // Set flag to true
                Utils.hideKeyboard(activity);
                countryCodeFor = "companyDetails";
                showCountryCode = "";
                getCountryListAPI(false);
            } else{}
        }
        else if(id == R.id.company_state_lin || id == R.id.company_state_tv)
        {
            if(!company_country_tv.getText().toString().trim().equalsIgnoreCase(""))
            {
                if(!isApiCalling) // Check if API is not already calling
                {
                    isApiCalling = true; // Set flag to true
                    Utils.hideKeyboard(activity);
                    stateCodeFor = "companyDetails";
                    getStateListAPI(false);
                } else{}

            }
            else
            {
                company_country_error_tv.setVisibility(View.VISIBLE);
                company_country_lin.setBackgroundResource(R.drawable.border_red_line_view);
            }

        }
        else if(id == R.id.company_city_lin || id == R.id.company_city_tv)
        {
            Utils.hideKeyboard(activity);
            if(!company_state_tv.getText().toString().trim().equalsIgnoreCase(""))
            {
                if(!isApiCalling) // Check if API is not already calling
                {
                    isApiCalling = true; // Set flag to true
                    cityCodeFor = "companyDetails";
                    getCityListAPI(false);
                } else{}
            }
            else
            {
                company_state_error_tv.setVisibility(View.VISIBLE);
                company_state_lin.setBackgroundResource(R.drawable.border_red_line_view);
            }
        }

        else if(id == R.id.supplier_country_lin)
        {
            Utils.hideKeyboard(activity);
            countryCodeFor = "SupplierDetails";
            showCountryCode = "yes";
            getCountryListAPI(false);
        }
        else if(id == R.id.supplier_state_lin)
        {
            if(!supplier_country_tv.getText().toString().trim().equalsIgnoreCase(""))
            {
                Utils.hideKeyboard(activity);
                stateCodeFor = "SupplierDetails";
                getStateListAPI(false);
            }
            else
            {
                supplier_country_error_tv.setVisibility(View.VISIBLE);
                supplier_country_lin.setBackgroundResource(R.drawable.border_red_line_view);
            }

        }
        else if(id == R.id.supplier_city_lin)
        {
            Utils.hideKeyboard(activity);
            if(!supplier_state_tv.getText().toString().trim().equalsIgnoreCase(""))
            {
                cityCodeFor = "SupplierDetails";
                getCityListAPI(false);
            }
            else
            {
                supplier_state_error_tv.setVisibility(View.VISIBLE);
                supplier_state_lin.setBackgroundResource(R.drawable.border_red_line_view);
            }
        }
        else if(id == R.id.company_type_lin || id == R.id.company_type_tv)
        {
            if(!isApiCalling) // Check if API is not already calling
            {
                isApiCalling = true; // Set flag to true
                Utils.hideKeyboard(activity);
                whoSelectCompanyType = "";
                initiateCompanyTypePopupWindow();
            } else{}
        }
        else if(id == R.id.business_nature_lin || id == R.id.business_nature_tv)
        {
            if(!isApiCalling) // Check if API is not already calling
            {
                isApiCalling = true; // Set flag to true
                Utils.hideKeyboard(activity);
                whoSelectBusinessNatureType = "";
                initiateBusinessTypePopupWindow();
            } else{}
        }
        else if(id == R.id.dob_lin)
        {
            dob = date_of_birth_tv.getText().toString().trim();
            CommonUtility.datePicker(context, SignupScreenActivity.this, dob, "dob", 0, System.currentTimeMillis());
        }
        else if(id == R.id.company_gst_no_verify_lin)
        {
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
        else if(id == R.id.upload_business_lic_doc_lin)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "companyBusinessLicenceNo";
            imageTakeOption();
        }
        else if(id == R.id.company_business_lic_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "companyBusinessLicenceNo";
            imageTakeOption();
        }
        else if(id == R.id.upload_gst_doc_lin)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "companyGSTNo";
            imageTakeOption();
        }
        else if(id == R.id.company_gst_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "companyGSTNo";
            selectImage();
            /*if (Build.VERSION.SDK_INT >= 23)
            {
                if (!canAccessCamera()) {
                    requestPermissions(STORAGE_PERMS, STORAGE_REQUEST);
                } else {
                    selectImage();
                }
            } else {
                selectImage();
            }*/
        }
        else if(id == R.id.company_pan_no_verify_lin)
        {
            if(company_pan_no_et.getText().toString().trim().equalsIgnoreCase(""))
            {
                company_pan_back_rel.setBackgroundResource(R.drawable.border_red_line_view);
                company_pan_no_error_tv.setVisibility(View.VISIBLE);
            }
            else
            {
                company_pan_no_error_tv.setVisibility(View.GONE);
                documentType = PANNo;
                onVerifyDocument(false, PANNo, company_pan_no_et.getText().toString().trim());
            }

        }
        else if(id == R.id.upload_pan_doc_lin)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "companyPanCard";
            imageTakeOption();
        }
        else if(id == R.id.company_pan_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "companyPanCard";
            selectImage();
            /*if (Build.VERSION.SDK_INT >= 23)
            {
                if (!canAccessCamera()) {
                    requestPermissions(STORAGE_PERMS, STORAGE_REQUEST);
                } else {
                    selectImage();
                }
            } else {
                selectImage();
            }*/
        }

        else if(id == R.id.upload_iec_doc_lin)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "companyIECCard";
            imageTakeOption();
        }
        else if(id == R.id.company_iec_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "companyIECCard";
            selectImage();
            /*if (Build.VERSION.SDK_INT >= 23)
            {
                if (!canAccessCamera()) {
                    requestPermissions(STORAGE_PERMS, STORAGE_REQUEST);
                } else {
                    selectImage();
                }
            } else {
                selectImage();
            }*/
        }

        else if(id == R.id.auth_aadhar_no_verify_lin)
        {
            Utils.hideKeyboard(activity);

            if(auth_aadhar_no_et.getText().toString().trim().equalsIgnoreCase(""))
            {
                auth_back_rel.setBackgroundResource(R.drawable.border_red_line_view);
                auth_aadhar_no_error_tv.setVisibility(View.VISIBLE);
            }
            else
            {
                auth_aadhar_no_error_tv.setVisibility(View.GONE);
                documentType = AADHAARNo;
                whereToVerifyDoc = "authorisedPerson";
                onVerifyDocument(false, AADHAARNo, auth_aadhar_no_et.getText().toString().trim());
            }
        }

        else if(id == R.id.upload_aadhar_front_lin)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "authorisedPersonAadharFront";
            imageTakeOption();
        }
        else if(id == R.id.auth_aadhar_front_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "authorisedPersonAadharFront";
            imageTakeOption();
        }
        else if(id == R.id.upload_aadhar_back_lin)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "authorisedPersonAadharBack";
            imageTakeOption();
        }
        else if(id == R.id.auth_aadhar_back_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "authorisedPersonAadharBack";
            imageTakeOption();
        }
        else if(id == R.id.auth_pan_no_verify_lin)
        {
            Utils.hideKeyboard(activity);

            if(auth_pan_no_et.getText().toString().trim().equalsIgnoreCase(""))
            {
                auth_pan_back_rel.setBackgroundResource(R.drawable.border_red_line_view);
                auth_pan_no_error_tv.setVisibility(View.VISIBLE);
            }
            else
            {
                auth_pan_no_error_tv.setVisibility(View.GONE);
                documentType = PANNo;
                whereToVerifyDoc = "authorisedPersonPan";
                onVerifyDocument(false, PANNo, auth_pan_no_et.getText().toString().trim());
            }
        }
        else if(id == R.id.auth_pan_doc_lin)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "authorisedPersonPanCard";
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
        else if(id == R.id.auth_pan_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "authorisedPersonPanCard";
            imageTakeOption();
        }

        else if(id == R.id.upload_passport_front_lin)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "passportFront";
            imageTakeOption();
        }
        else if(id == R.id.passport_front_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "passportFront";
            imageTakeOption();
        }
        else if(id == R.id.upload_passport_back_lin)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "passportBack";
            imageTakeOption();
        }
        else if(id == R.id.passport_back_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "passportBack";
            imageTakeOption();
        }
        else if(id == R.id.driving_lic_doc_lin)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "drivingLicense";
            imageTakeOption();
        }
        else if(id == R.id.driving_lic_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "drivingLicense";
            imageTakeOption();
        }

        else if(id == R.id.driving_lic_no_verify_lin)
        {
            Utils.hideKeyboard(activity);

            if(driving_lic_no_et.getText().toString().trim().equalsIgnoreCase(""))
            {
                driving_lic_bacl_rel.setBackgroundResource(R.drawable.border_red_line_view);
                driving_lic_error_required_tv.setVisibility(View.VISIBLE);
            }
            else
            {
                driving_lic_error_required_tv.setVisibility(View.GONE);
                documentType = DRIVING_LICENSE;
                onVerifyDocument(false, DRIVING_LICENSE, driving_lic_no_et.getText().toString().trim());
            }
        }

        else if(id == R.id.supplier_company_gst_no_verify_lin)
        {
            if(supplier_company_gst_no_et.getText().toString().trim().equalsIgnoreCase(""))
            {
                supplier_company_gst_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                supplier_company_gst_no_error_tv.setVisibility(View.VISIBLE);
            }
            else
            {
                supplier_company_gst_no_error_tv.setVisibility(View.GONE);
                documentType = GSTNo;
                whereToVerifyDoc = "supplierCompnayGSTNo";
                onVerifyDocument(false, GSTNo, supplier_company_gst_no_et.getText().toString().trim());
            }

        }
        else if(id == R.id.supplier_upload_gst_doc_lin)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "supplierCompanyGSTNo";
            imageTakeOption();
        }
        else if(id == R.id.supplier_company_gst_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "supplierCompanyGSTNo";
            imageTakeOption();
        }

        else if(id == R.id.supplier_company_pan_no_verify_lin)
        {
            if(supplier_company_pan_no_et.getText().toString().trim().equalsIgnoreCase(""))
            {
                supplier_company_pan_back_rel.setBackgroundResource(R.drawable.border_red_line_view);
                supplier_company_pan_no_error_tv.setVisibility(View.VISIBLE);
            }
            else
            {
                supplier_company_pan_no_error_tv.setVisibility(View.GONE);
                documentType = PANNo;
                whereToVerifyDoc = "supplierPanNo";
                onVerifyDocument(false, PANNo, supplier_company_pan_no_et.getText().toString().trim());
            }

        }
        else if(id == R.id.supplier_upload_pan_doc_lin)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "supplierCompanyPanCard";
            imageTakeOption();
        }
        else if(id == R.id.supplier_company_pan_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "supplierCompanyPanCard";
            imageTakeOption();
        }

        else if(id == R.id.supplier_company_type_lin)
        {
            Utils.hideKeyboard(activity);
            whoSelectCompanyType = "supplier";
            initiateCompanyTypePopupWindow();
        }
        else if(id == R.id.supplier_business_nature_lin)
        {
            Utils.hideKeyboard(activity);
            whoSelectBusinessNatureType = "supplier";
            initiateBusinessTypePopupWindow();
        }
        else if(id == R.id.inventory_lin)
        {
            Utils.hideKeyboard(activity);
            initiateInventoryTypePopupWindow();
        }

        else if(id == R.id.supplier_upload_iec_doc_lin)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "supplierCompanyIECDoc";
            imageTakeOption();
        }
        else if(id == R.id.supplier_company_iec_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "supplierCompanyIECDoc";
            imageTakeOption();
        }
        else if(id == R.id.suppier_upload_business_lic_doc_lin)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "supplierBusinessLicDoc";
            imageTakeOption();
        }
        else if(id == R.id.suppier_business_lic_doc_verify_img)
        {
            Utils.hideKeyboard(activity);
            userChoosenTask = "supplierBusinessLicDoc";
            imageTakeOption();
        }

        else if(id == R.id.already_login_tv)
        {
            intent = new Intent(activity, LoginScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        }
        else if(id == R.id.create_account_tv)
        {
            if(termsConditionCheck.isChecked())
            {
                Utils.hideKeyboard(activity);

                if(selectedSignupTypeTab.equalsIgnoreCase(BUYER))
                {
                    // First Check All Field Validation then check onr by one missing field Validation
                    if(validateBuyerAllFields())
                    {
                        if(validateFields())
                        {
                            if(emailVerifyOrNot.equalsIgnoreCase("done"))
                            {
                                onBindAPI(false);
                            }
                            else
                            {
                                email_et.setBackgroundResource(R.drawable.border_red_line_view);
                                email_error_tv.setVisibility(View.VISIBLE);
                                email_et.requestFocus();
                                email_error_tv.setText(getResources().getString(R.string.email_verify_required));
                            }

                        }
                    }
                    else{}
                }
                else if(selectedSignupTypeTab.equalsIgnoreCase(DEALER))
                {
                    if(validateDealerAllFieldSignupFields())
                    {
                        if(validateDealerSignupFields())
                        {
                            if(company_country_tv.getText().toString().trim().equalsIgnoreCase("India"))
                            {
                                if(!company_gst_no_et.getText().toString().trim().equalsIgnoreCase(""))
                                {
                                    if(company_gst_no_et.getText().toString().trim().contains(company_pan_no_et.getText().toString().toString()))
                                    {
                                    if (Utils.isNetworkAvailable(context))
                                    {
                                        SendDealerSignupDataAsyncTask asyncTask1 = new SendDealerSignupDataAsyncTask(context);
                                        asyncTask1.executeNetworkCall();
                                    } else {
                                        showToast(ApiConstants.MSG_INTERNETERROR);
                                    }


                                    }
                                    else
                                    {
                                        isArrowDownCompanyKyc = true;//for Company Card Open Visible
                                        drop_arrow_company_kyc_img.setImageResource(R.drawable.up);//for Company Card Open Visible
                                        company_kyc_lin.setVisibility(View.VISIBLE); //for Company Card Open Visible
                                        company_pan_no_error_tv.setVisibility(View.VISIBLE);
                                        company_pan_no_error_tv.setText(getResources().getString(R.string.pan_no_associated_gst_no));
                                        company_pan_back_rel.setBackgroundResource(R.drawable.border_red_line_view);
                                        company_pan_no_et.requestFocus();
                                    }
                                }
                                else{
                                    if (Utils.isNetworkAvailable(context))
                                    {
                                        SendDealerSignupDataAsyncTask asyncTask1 = new SendDealerSignupDataAsyncTask(context);
                                        asyncTask1.executeNetworkCall();
                                    } else {
                                        showToast(ApiConstants.MSG_INTERNETERROR);
                                    }

                                }

                            }
                            else
                            {
                                if (Utils.isNetworkAvailable(context))
                                {
                                    SendDealerSignupDataAsyncTask asyncTask1 = new SendDealerSignupDataAsyncTask(context);
                                    asyncTask1.executeNetworkCall();
                                } else {
                                    showToast(ApiConstants.MSG_INTERNETERROR);
                                }

                            }

                        }else{}
                    }else{}

                }
                else if(selectedSignupTypeTab.equalsIgnoreCase(SUPPLIER))
                {
                    if(validateSupplierAllFieldSignupFields())
                    {
                        if(validateSupplierFields())
                        {
                            if(supplier_country_tv.getText().toString().trim().equalsIgnoreCase("India"))
                            {
                                if(!supplier_company_gst_no_et.getText().toString().trim().equalsIgnoreCase(""))
                                {
                                    if(supplier_company_gst_no_et.getText().toString().trim().contains(supplier_company_pan_no_et.getText().toString().toString()))
                                    {
                                        if (Utils.isNetworkAvailable(context))
                                        {
                                            SendSupplierSignupDataAsyncTask asyncTask1 = new SendSupplierSignupDataAsyncTask(context);
                                            asyncTask1.executeNetworkCall();
                                        } else {
                                            showToast(ApiConstants.MSG_INTERNETERROR);
                                        }

                                    }
                                    else
                                    {
                                        isArrowDownSupplierCompanyKyc = true;//for Company Card Open Visible
                                        drop_arrow_supplier_kyc_img.setImageResource(R.drawable.up);//for Company Card Open Visible
                                        supplier_company_kyc_lin.setVisibility(View.VISIBLE); //for Company Card Open Visible
                                        supplier_company_pan_no_error_tv.setVisibility(View.VISIBLE);
                                        supplier_company_pan_no_error_tv.setText(getResources().getString(R.string.pan_no_associated_gst_no));
                                        supplier_company_pan_back_rel.setBackgroundResource(R.drawable.border_red_line_view);
                                        supplier_company_pan_no_et.requestFocus();
                                    }
                                }
                                else{
                                    if (Utils.isNetworkAvailable(context))
                                    {
                                        SendSupplierSignupDataAsyncTask asyncTask1 = new SendSupplierSignupDataAsyncTask(context);
                                        asyncTask1.executeNetworkCall();
                                    } else {
                                        showToast(ApiConstants.MSG_INTERNETERROR);
                                    }

                                }
                            }
                            else
                            {
                                if (Utils.isNetworkAvailable(context))
                                {
                                    SendSupplierSignupDataAsyncTask asyncTask1 = new SendSupplierSignupDataAsyncTask(context);
                                    asyncTask1.executeNetworkCall();
                                } else {
                                    showToast(ApiConstants.MSG_INTERNETERROR);
                                }
                            }

                        }else{}
                    }
                    else{}
                }
               else{}
            }
            else {}
        }
        else if(id == R.id.terms_condition_tv)
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://diamondxe.com/policy/terms-conditions"));
            startActivity(intent);
        }
    }

    private PopupWindow mDropdown = null;
    LayoutInflater mInflater;
    // Button pop;

    private PopupWindow initiateCompanyTypePopupWindow() {
        try {
            if (mDropdown != null && mDropdown.isShowing()) {
                mDropdown.dismiss();
            }
            mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.custom_menu_sign_company_type, null);

            isApiCalling = false;

            final TextView proprietorship_tv = layout.findViewById(R.id.proprietorship_tv);
            final TextView llp_tv = layout.findViewById(R.id.llp_tv);
            final TextView partnership_tv = layout.findViewById(R.id.partnership_tv);
            final TextView private_limited_tv = layout.findViewById(R.id.private_limited_tv);
            final TextView public_limited_tv = layout.findViewById(R.id.public_limited_tv);
            final TextView others_tv = layout.findViewById(R.id.others_tv);

            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            //mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);

            if(whoSelectCompanyType.equalsIgnoreCase("supplier"))
            {
                mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);
            }
            else{
                mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);

            }


            /*Drawable background = getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            mDropdown.setBackgroundDrawable(background);*/

            // Ensure pop is not null before using it
            if(whoSelectCompanyType.equalsIgnoreCase("supplier"))
            {
                if (supplier_company_type_lin != null) {
                    mDropdown.showAsDropDown(supplier_company_type_lin, 5, -120);
                } else {
                    Log.e("PopupWindow", "pop is null");
                }
            }
            else{
                if (company_type_tv != null) {
                    mDropdown.showAsDropDown(company_type_tv, 5, -120);
                } else {
                    Log.e("PopupWindow", "pop is null");
                }
            }

            proprietorship_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(whoSelectCompanyType.equalsIgnoreCase("supplier"))
                    {
                        supplier_company_type_tv.setText(proprietorship_tv.getText().toString().trim());
                        removeCompanySelectError();
                    }
                    else{
                        company_type_tv.setText(proprietorship_tv.getText().toString().trim());
                        removeCompanySelectError();
                    }
                    mDropdown.dismiss();
                }
            });

            llp_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(whoSelectCompanyType.equalsIgnoreCase("supplier"))
                    {
                        supplier_company_type_tv.setText(llp_tv.getText().toString().trim());
                        removeCompanySelectError();
                    }
                    else{
                        company_type_tv.setText(llp_tv.getText().toString().trim());
                        removeCompanySelectError();
                    }

                    mDropdown.dismiss();
                }
            });

            partnership_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(whoSelectCompanyType.equalsIgnoreCase("supplier"))
                    {
                        supplier_company_type_tv.setText(partnership_tv.getText().toString().trim());
                        removeCompanySelectError();
                    }
                    else{
                        company_type_tv.setText(partnership_tv.getText().toString().trim());
                        removeCompanySelectError();
                    }
                    mDropdown.dismiss();
                }
            });

            private_limited_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(whoSelectCompanyType.equalsIgnoreCase("supplier"))
                    {
                        supplier_company_type_tv.setText(private_limited_tv.getText().toString().trim());
                        removeCompanySelectError();
                    }
                    else{
                        company_type_tv.setText(private_limited_tv.getText().toString().trim());
                        removeCompanySelectError();
                    }

                    mDropdown.dismiss();
                }
            });

            public_limited_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(whoSelectCompanyType.equalsIgnoreCase("supplier"))
                    {
                        supplier_company_type_tv.setText(public_limited_tv.getText().toString().trim());
                        removeCompanySelectError();
                    }
                    else{
                        company_type_tv.setText(public_limited_tv.getText().toString().trim());
                        removeCompanySelectError();
                    }

                    mDropdown.dismiss();
                }
            });

            others_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(whoSelectCompanyType.equalsIgnoreCase("supplier"))
                    {
                        supplier_company_type_tv.setText(others_tv.getText().toString().trim());
                        removeCompanySelectError();
                    }
                    else{
                        company_type_tv.setText(others_tv.getText().toString().trim());
                        removeCompanySelectError();
                    }

                    mDropdown.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;
    }


    void removeCompanySelectError()
    {
        if(whoSelectCompanyType.equalsIgnoreCase("supplier"))
        {
            supplier_company_type_lin.setBackgroundResource(R.drawable.border_purple_line_view);
            supplier_company_type_error_tv.setVisibility(View.GONE);
            whoSelectCompanyType="";
        }
        else
        {
            company_type_lin.setBackgroundResource(R.drawable.border_purple_line_view);
            company_type_error_tv.setVisibility(View.GONE);
        }
    }

    private PopupWindow initiateBusinessTypePopupWindow() {
        try {
            if (mDropdown != null && mDropdown.isShowing()) {
                mDropdown.dismiss();
            }
            mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.custom_menu_sign_business_type, null);

            isApiCalling = false;

            final TextView rerailer_tv = layout.findViewById(R.id.rerailer_tv);
            final TextView wholesaler_tv = layout.findViewById(R.id.wholesaler_tv);
            final TextView trader_tv = layout.findViewById(R.id.trader_tv);
            final TextView jeweller_tv = layout.findViewById(R.id.jeweller_tv);
            final TextView others_tv = layout.findViewById(R.id.others_tv);

            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            if(whoSelectBusinessNatureType.equalsIgnoreCase("supplier"))
            {
                mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);
            }
            else{
                mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);

            }

            /*Drawable background = getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            mDropdown.setBackgroundDrawable(background);*/

            // Ensure pop is not null before using it
            if(whoSelectBusinessNatureType.equalsIgnoreCase("supplier"))
            {
                if (supplier_business_nature_lin != null) {
                    mDropdown.showAsDropDown(supplier_business_nature_lin, 5, -120);
                } else {
                    Log.e("PopupWindow", "pop is null");
                }
            }
            else{
                if (business_nature_lin != null) {
                    mDropdown.showAsDropDown(business_nature_lin, 5, -120);
                } else {
                    Log.e("PopupWindow", "pop is null");
                }
            }



            rerailer_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(whoSelectBusinessNatureType.equalsIgnoreCase("supplier"))
                    {
                        supplier_business_nature_tv.setText(rerailer_tv.getText().toString().trim());
                        removeBusinessSelectError();
                    }
                    else
                    {
                        business_nature_tv.setText(rerailer_tv.getText().toString().trim());
                        removeBusinessSelectError();
                    }

                    mDropdown.dismiss();
                }
            });

            wholesaler_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(whoSelectBusinessNatureType.equalsIgnoreCase("supplier"))
                    {
                        supplier_business_nature_tv.setText(wholesaler_tv.getText().toString().trim());
                        removeBusinessSelectError();
                    }
                    else
                    {
                        business_nature_tv.setText(wholesaler_tv.getText().toString().trim());
                        removeBusinessSelectError();
                    }

                    mDropdown.dismiss();
                }
            });

            trader_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(whoSelectBusinessNatureType.equalsIgnoreCase("supplier"))
                    {
                        supplier_business_nature_tv.setText(trader_tv.getText().toString().trim());
                        removeBusinessSelectError();
                    }
                    else
                    {
                        business_nature_tv.setText(trader_tv.getText().toString().trim());
                        removeBusinessSelectError();
                    }

                    mDropdown.dismiss();
                }
            });

            jeweller_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(whoSelectBusinessNatureType.equalsIgnoreCase("supplier"))
                    {
                        supplier_business_nature_tv.setText(jeweller_tv.getText().toString().trim());
                        removeBusinessSelectError();
                    }
                    else
                    {
                        business_nature_tv.setText(jeweller_tv.getText().toString().trim());
                        removeBusinessSelectError();
                    }

                    mDropdown.dismiss();
                }
            });

            others_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(whoSelectBusinessNatureType.equalsIgnoreCase("supplier"))
                    {
                        supplier_business_nature_tv.setText(others_tv.getText().toString().trim());
                        removeBusinessSelectError();
                    }
                    else
                    {
                        business_nature_tv.setText(others_tv.getText().toString().trim());
                        removeBusinessSelectError();
                    }
                    mDropdown.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;
    }

    void removeBusinessSelectError()
    {
        if(whoSelectBusinessNatureType.equalsIgnoreCase("supplier"))
        {
            supplier_business_nature_lin.setBackgroundResource(R.drawable.border_purple_line_view);
            supplier_business_nature_error_tv.setVisibility(View.GONE);
            whoSelectBusinessNatureType="";
        }
        else
        {
            business_nature_lin.setBackgroundResource(R.drawable.border_purple_line_view);
            business_nature_error_tv.setVisibility(View.GONE);
        }
    }

    private PopupWindow initiateInventoryTypePopupWindow() {
        try {
            if (mDropdown != null && mDropdown.isShowing()) {
                mDropdown.dismiss();
            }
            mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.custom_menu_inventory_type, null);

            final TextView natural_diamond_tv = layout.findViewById(R.id.natural_diamond_tv);
            final TextView lab_diamond_tv = layout.findViewById(R.id.lab_diamond_tv);
            final TextView gematones_tv = layout.findViewById(R.id.gematones_tv);

            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);

            /*Drawable background = getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            mDropdown.setBackgroundDrawable(background);*/

            // Ensure pop is not null before using it
            if (inventory_tv != null) {
                mDropdown.showAsDropDown(inventory_tv, 5, -100);
            } else {
                Log.e("PopupWindow", "pop is null");
            }

            natural_diamond_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inventory_tv.setText(natural_diamond_tv.getText().toString().trim());
                    removeInvertoryErrorAfterSelect();
                    mDropdown.dismiss();
                }
            });

            lab_diamond_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inventory_tv.setText(lab_diamond_tv.getText().toString().trim());
                    removeInvertoryErrorAfterSelect();
                    mDropdown.dismiss();
                }
            });

            gematones_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inventory_tv.setText(gematones_tv.getText().toString().trim());
                    removeInvertoryErrorAfterSelect();
                    mDropdown.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;
    }

    void removeInvertoryErrorAfterSelect()
    {
        inventory_error_tv.setVisibility(View.GONE);
        inventory_lin.setBackgroundResource(R.drawable.border_purple_line_view);
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

                Toast.makeText(activity, "Camera Permission Not Grant", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(activity, "Permission Not Grant", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        galleryLauncher.launch(Intent.createChooser(intent, "Select File"));
    }

    //For Camera Intnet
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent);
    }

    private void onCaptureImageResult(Intent data)
    {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        sendImageToServer(imageBitmap);

        if(userChoosenTask.equalsIgnoreCase("companyBusinessLicenceNo"))
        {
            isBusinessLicenceNo = true;
            upload_business_lic_doc_lin.setVisibility(View.GONE);
            company_business_lic_doc_verify_img.setVisibility(View.VISIBLE);
            company_business_lic_doc_error_tv.setVisibility(View.GONE);
            company_business_lic_doc_rel.setBackgroundResource(R.drawable.border_purple_line_view);
        }
        else if(userChoosenTask.equalsIgnoreCase("companyGSTNo"))
        {
            upload_gst_doc_lin.setVisibility(View.GONE);
            company_gst_doc_verify_img.setVisibility(View.VISIBLE);
            //company_gst_doc_verify_img.setImageBitmap(thumbnail);
        }
        else if(userChoosenTask.equalsIgnoreCase("companyPanCard"))
        {
            isCompanyPanDocUpload = true;
            upload_pan_doc_lin.setVisibility(View.GONE);
            company_pan_doc_verify_img.setVisibility(View.VISIBLE);
            company_pan_doc_error_tv.setVisibility(View.GONE);
            company_pan_doc_rel.setBackgroundResource(R.drawable.border_purple_line_view);
        }
        else if(userChoosenTask.equalsIgnoreCase("companyIECCard"))
        {
            upload_iec_doc_lin.setVisibility(View.GONE);
            company_iec_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("authorisedPersonPanCard"))
        {
            auth_pan_doc_lin.setVisibility(View.GONE);
            auth_pan_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("authorisedPersonAadharFront"))
        {
            upload_aadhar_front_lin.setVisibility(View.GONE);
            auth_aadhar_front_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("authorisedPersonAadharBack"))
        {
            upload_aadhar_back_lin.setVisibility(View.GONE);
            auth_aadhar_back_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("passportFront"))
        {
            upload_passport_front_lin.setVisibility(View.GONE);
            passport_front_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("passportBack"))
        {
            upload_passport_back_lin.setVisibility(View.GONE);
            passport_back_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("drivingLicense"))
        {
            driving_lic_doc_lin.setVisibility(View.GONE);
            driving_lic_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("supplierCompanyGSTNo"))
        {
            supplier_upload_gst_doc_lin.setVisibility(View.GONE);
            supplier_company_gst_doc_verify_img.setVisibility(View.VISIBLE);
            //company_gst_doc_verify_img.setImageBitmap(thumbnail);
        }
        else if(userChoosenTask.equalsIgnoreCase("supplierCompanyPanCard"))
        {
            isSupplierPanDocUpload = true;
            supplier_upload_pan_doc_lin.setVisibility(View.GONE);
            supplier_company_pan_doc_verify_img.setVisibility(View.VISIBLE);
            supplier_company_pan_doc_error_tv.setVisibility(View.GONE);
            supplier_company_pan_doc_rel.setBackgroundResource(R.drawable.border_purple_line_view);
        }
        else if(userChoosenTask.equalsIgnoreCase("supplierCompanyIECDoc"))
        {
            supplier_upload_iec_doc_lin.setVisibility(View.GONE);
            supplier_company_iec_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("supplierBusinessLicDoc"))
        {
            isSupplierBusinessDocUpload = true;
            suppier_upload_business_lic_doc_lin.setVisibility(View.GONE);
            suppier_business_lic_doc_verify_img.setVisibility(View.VISIBLE);
            suppier_business_lic_doc_error_tv.setVisibility(View.GONE);
            suppier_business_lic_doc_rel.setBackgroundResource(R.drawable.border_purple_line_view);
        }

        else {}
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
        //companyKycPanDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        if(userChoosenTask.equalsIgnoreCase("companyBusinessLicenceNo"))
        {
            businessLicenceNoDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("companyGSTNo"))
        {
            companyKycGstDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("companyPanCard"))
        {
            companyKycPanDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("companyIECCard"))
        {
            companyIECPanDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("authorisedPersonPanCard"))
        {
            authAadharPanDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("authorisedPersonAadharFront"))
        {
            authAadharFrontDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("authorisedPersonAadharBack"))
        {
            authAadharBackDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("passportFront"))
        {
            passPortFrontDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("passportBack"))
        {
            passPortBackDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("drivingLicense"))
        {
            drivingLicenceDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("supplierCompanyGSTNo"))
        {
            supplierCompanyGSTDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("supplierCompanyPanCard"))
        {
            supplierCompanyPanDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("supplierCompanyIECDoc"))
        {
            supplierCompanyIECDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        else if(userChoosenTask.equalsIgnoreCase("supplierBusinessLicDoc"))
        {
            supplierCompanyBusinessDocBase64Url = FOR_BASE64_IMAGE +base64Image;
        }
        Log.e("-------Final_base64Image----- : ", companyKycPanDocBase64Url.toString());
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

        //Log.e("------picturePath----- : ", picturePath.toString());

        if(userChoosenTask.equalsIgnoreCase("companyBusinessLicenceNo"))
        {
            isBusinessLicenceNo = true;
            upload_business_lic_doc_lin.setVisibility(View.GONE);
            company_business_lic_doc_verify_img.setVisibility(View.VISIBLE);
            company_business_lic_doc_error_tv.setVisibility(View.GONE);
            company_business_lic_doc_rel.setBackgroundResource(R.drawable.border_purple_line_view);
        }
        else if(userChoosenTask.equalsIgnoreCase("companyGSTNo"))
        {
            upload_gst_doc_lin.setVisibility(View.GONE);
            company_gst_doc_verify_img.setVisibility(View.VISIBLE);
            //company_gst_doc_verify_img.setImageBitmap(bm);
        }
        else if(userChoosenTask.equalsIgnoreCase("companyPanCard"))
        {
            isCompanyPanDocUpload = true;
            upload_pan_doc_lin.setVisibility(View.GONE);
            company_pan_doc_verify_img.setVisibility(View.VISIBLE);
            company_pan_doc_error_tv.setVisibility(View.GONE);
            company_pan_doc_rel.setBackgroundResource(R.drawable.border_purple_line_view);
        }
        else if(userChoosenTask.equalsIgnoreCase("companyIECCard"))
        {
            upload_iec_doc_lin.setVisibility(View.GONE);
            company_iec_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("authorisedPersonPanCard"))
        {
            auth_pan_doc_lin.setVisibility(View.GONE);
            auth_pan_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("authorisedPersonAadharFront"))
        {
            upload_aadhar_front_lin.setVisibility(View.GONE);
            auth_aadhar_front_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("authorisedPersonAadharBack"))
        {
            upload_aadhar_back_lin.setVisibility(View.GONE);
            auth_aadhar_back_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("passportFront"))
        {
            upload_passport_front_lin.setVisibility(View.GONE);
            passport_front_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("passportBack"))
        {
            upload_passport_back_lin.setVisibility(View.GONE);
            passport_back_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("drivingLicense"))
        {
            driving_lic_doc_lin.setVisibility(View.GONE);
            driving_lic_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("supplierCompanyGSTNo"))
        {
            supplier_upload_gst_doc_lin.setVisibility(View.GONE);
            supplier_company_gst_doc_verify_img.setVisibility(View.VISIBLE);
            //company_gst_doc_verify_img.setImageBitmap(thumbnail);
        }
        else if(userChoosenTask.equalsIgnoreCase("supplierCompanyPanCard"))
        {
            isSupplierPanDocUpload = true;
            supplier_upload_pan_doc_lin.setVisibility(View.GONE);
            supplier_company_pan_doc_verify_img.setVisibility(View.VISIBLE);
            supplier_company_pan_doc_error_tv.setVisibility(View.GONE);
            supplier_company_pan_doc_rel.setBackgroundResource(R.drawable.border_purple_line_view);
        }
        else if(userChoosenTask.equalsIgnoreCase("supplierCompanyIECDoc"))
        {
            supplier_upload_iec_doc_lin.setVisibility(View.GONE);
            supplier_company_iec_doc_verify_img.setVisibility(View.VISIBLE);
        }
        else if(userChoosenTask.equalsIgnoreCase("supplierBusinessLicDoc"))
        {
            isSupplierBusinessDocUpload = true;
            suppier_upload_business_lic_doc_lin.setVisibility(View.GONE);
            suppier_business_lic_doc_verify_img.setVisibility(View.VISIBLE);
            suppier_business_lic_doc_error_tv.setVisibility(View.GONE);
            suppier_business_lic_doc_rel.setBackgroundResource(R.drawable.border_purple_line_view);
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

    @SuppressWarnings("deprecation")


    // Buyer Signup API
    public void onBindAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            //Log.e("----------Diamond------ : ", Constant.shapeDiamondValue.toString());
            urlParameter = new HashMap<String, String>();

            urlParameter.put("firstName", first_name_et.getText().toString().trim());
            urlParameter.put("lastName", last_name_et.getText().toString().trim());
            urlParameter.put("mobileNo", buyerPhCountryCode + " " + mobile_number_et.getText().toString().trim());
            urlParameter.put("email", email_et.getText().toString().trim());
            urlParameter.put("password", password_et.getText().toString().trim());
            urlParameter.put("confirmPassword", confirm_password_et.getText().toString().trim());
            urlParameter.put("country", countryId);
            urlParameter.put("state", stateId);
            urlParameter.put("city", cityId);
            urlParameter.put("pinCode", pincode_et.getText().toString().trim());
            urlParameter.put("address", address_line1_et.getText().toString().trim());
            urlParameter.put("address2", address_line2_et.getText().toString().trim());
            urlParameter.put("referralCode", referral_code_et.getText().toString().trim());

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.REGISTER_BUYER, ApiConstants.REGISTER_BUYER_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
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
                urlParameter.put("otherDocumentNumber", driving_lic_no_et.getText().toString().trim());
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

    private boolean validateBuyerAllFields()
    {
        String firstName = first_name_et.getText().toString().trim();
        String lastName = last_name_et.getText().toString().trim();
        String mobileNumber = mobile_number_et.getText().toString().trim();
        String email = email_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();
        String confirmPassword = confirm_password_et.getText().toString().trim();
        String country = country_tv.getText().toString().trim();
        String state = state_tv.getText().toString().trim();
        String city = city_tv.getText().toString().trim();
        String pinCode = pincode_et.getText().toString().trim();
        String addressLine1 = address_line1_et.getText().toString().trim();

        if ((firstName.length() == 0 || firstName == null|| firstName.equalsIgnoreCase("")) &&
                (lastName.length() == 0 || lastName == null|| lastName.equalsIgnoreCase("")) &&
                (mobileNumber.length() == 0 || mobileNumber == null || mobileNumber.equalsIgnoreCase("")) &&
                (email.length() == 0 || email == null|| email.equalsIgnoreCase("")) &&
                (password.length() == 0 || password == null|| password.equalsIgnoreCase("")) &&
                (confirmPassword.length() == 0 || confirmPassword == null|| confirmPassword.equalsIgnoreCase("")) &&
                (country.length() == 0 || country == null|| country.equalsIgnoreCase("")) &&
                (state.length() == 0 || state == null|| state.equalsIgnoreCase("")) &&
                (city.length() == 0 || city == null|| city.equalsIgnoreCase("")) &&
                (pinCode.length() == 0 || pinCode == null|| pinCode.equalsIgnoreCase("")) &&
                (addressLine1.length() == 0 || addressLine1 == null|| addressLine1.equalsIgnoreCase(""))) {

            first_name_et.requestFocus();

            first_name_error_tv.setVisibility(View.VISIBLE);
            first_name_et.setBackgroundResource(R.drawable.border_red_line_view);

            last_name_error_tv.setVisibility(View.VISIBLE);
            last_name_et.setBackgroundResource(R.drawable.border_red_line_view);

            mobile_number_error_tv.setVisibility(View.VISIBLE);
            mobile_number_lin.setBackgroundResource(R.drawable.border_red_line_view);

            email_error_tv.setVisibility(View.VISIBLE);
            email_et.setBackgroundResource(R.drawable.border_red_line_view);

            password_error_tv.setVisibility(View.VISIBLE);
            password_et.setBackgroundResource(R.drawable.border_red_line_view);

            confirm_password_error_tv.setVisibility(View.VISIBLE);
            confirm_password_et.setBackgroundResource(R.drawable.border_red_line_view);

            country_error_tv.setVisibility(View.VISIBLE);
            country_lin.setBackgroundResource(R.drawable.border_red_line_view);

            state_error_tv.setVisibility(View.VISIBLE);
            state_lin.setBackgroundResource(R.drawable.border_red_line_view);

            city_error_tv.setVisibility(View.VISIBLE);
            city_lin.setBackgroundResource(R.drawable.border_red_line_view);

            pincode_error_tv.setVisibility(View.VISIBLE);
            pincode_et.setBackgroundResource(R.drawable.border_red_line_view);

            address_line1_error_tv.setVisibility(View.VISIBLE);
            address_line1_et.setBackgroundResource(R.drawable.border_red_line_view);

            return false;
        }

        return true;
    }
    private boolean validateFields()
    {
        String firstName = first_name_et.getText().toString().trim();
        String lastName = last_name_et.getText().toString().trim();
        String mobileNumber = mobile_number_et.getText().toString().trim();
        String email = email_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();
        String confirmPassword = confirm_password_et.getText().toString().trim();
        String country = country_tv.getText().toString().trim();
        String state = state_tv.getText().toString().trim();
        String city = city_tv.getText().toString().trim();
        String pinCode = pincode_et.getText().toString().trim();
        String addressLine1 = address_line1_et.getText().toString().trim();

        if (firstName.length() == 0 || firstName == null|| firstName.equalsIgnoreCase("")) {
            first_name_error_tv.setVisibility(View.VISIBLE);
            first_name_et.setBackgroundResource(R.drawable.border_red_line_view);
            first_name_et.requestFocus();
            return false;
        }
        else if (lastName.length() == 0 || lastName == null|| lastName.equalsIgnoreCase("")) {
            last_name_error_tv.setVisibility(View.VISIBLE);
            last_name_et.setBackgroundResource(R.drawable.border_red_line_view);
            last_name_et.requestFocus();
            return false;
        }
        else if (mobileNumber.length() == 0 || mobileNumber == null || mobileNumber.equalsIgnoreCase("")) {
            mobile_number_error_tv.setVisibility(View.VISIBLE);
            mobile_number_lin.setBackgroundResource(R.drawable.border_red_line_view);
            mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_required));
            mobile_number_lin.requestFocus();
            return false;
        } else if (mobileNumber.length() < 7 || mobileNumber.length() > 14) {
            mobile_number_error_tv.setVisibility(View.VISIBLE);
            mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
            mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_valid_msg));
            mobile_number_lin.requestFocus();
            return false;
        }
        else if (email.length() == 0 || email == null|| email.equalsIgnoreCase("")) {
            email_error_tv.setVisibility(View.VISIBLE);
            email_et.setBackgroundResource(R.drawable.border_red_line_view);
            email_error_tv.setText(getResources().getString(R.string.email_required));
            email_et.requestFocus();
            return false;
        }
        else if (!Utils.emailValidator(email))
        {
            email_error_tv.setVisibility(View.VISIBLE);
            email_et.setBackgroundResource(R.drawable.border_red_line_view);
            email_error_tv.setText(getResources().getString(R.string.email_valid_msg));
            email_et.requestFocus();
            return false;
        }

       /* else  if (password.length() == 0 || password == null|| password.equalsIgnoreCase("")) {
            email_required.setVisibility(View.GONE);
            passowrd_required.setVisibility(View.VISIBLE);
            password_et.setBackgroundResource(R.drawable.border_red);

            return false;
        }*/
        else if (password.length() == 0 || password == null|| password.equalsIgnoreCase("")) {
            password_error_tv.setVisibility(View.VISIBLE);
            password_et.setBackgroundResource(R.drawable.border_red_line_view);
            password_et.requestFocus();
            return false;
        }
        else if (password.length()<8) {
            password_error_tv.setVisibility(View.VISIBLE);
            password_et.setBackgroundResource(R.drawable.border_red_line_view);
            password_error_tv.setText(getResources().getString(R.string.password_length_msg));
            password_et.requestFocus();
            return false;
        }
        else if (!CommonUtility.isValidPassword(password)) {
            password_error_tv.setVisibility(View.VISIBLE);
            password_et.setBackgroundResource(R.drawable.border_red_line_view);
            password_error_tv.setText(getResources().getString(R.string.password_validation_msg));
            password_et.requestFocus();
            return false;
        }
        else if (confirmPassword.length() == 0 || confirmPassword == null|| confirmPassword.equalsIgnoreCase("")) {
            confirm_password_error_tv.setVisibility(View.VISIBLE);
            confirm_password_et.setBackgroundResource(R.drawable.border_red_line_view);
            confirm_password_et.requestFocus();
            return false;
        }
        else if (!confirmPassword.equals(""+password)) {
            confirm_password_error_tv.setVisibility(View.VISIBLE);
            confirm_password_et.setBackgroundResource(R.drawable.border_red_line_view);
            confirm_password_error_tv.setText(getResources().getString(R.string.password_confirm_password_not_match));
            confirm_password_et.requestFocus();
            return false;
        }
        else if (country.length() == 0 || country == null|| country.equalsIgnoreCase("")) {
            country_error_tv.setVisibility(View.VISIBLE);
            country_lin.setBackgroundResource(R.drawable.border_red_line_view);
            country_lin.requestFocus();
            return false;
        }
        else if (state.length() == 0 || state == null|| state.equalsIgnoreCase("")) {
            state_error_tv.setVisibility(View.VISIBLE);
            state_lin.setBackgroundResource(R.drawable.border_red_line_view);
            state_tv.requestFocus();
            return false;
        }
        else if (city.length() == 0 || city == null|| city.equalsIgnoreCase("")) {
            city_error_tv.setVisibility(View.VISIBLE);
            city_lin.setBackgroundResource(R.drawable.border_red_line_view);
            city_tv.requestFocus();
            return false;
        }
        else if (pinCode.length() == 0 || pinCode == null|| pinCode.equalsIgnoreCase("")) {
            pincode_error_tv.setVisibility(View.VISIBLE);
            pincode_et.setBackgroundResource(R.drawable.border_red_line_view);
            pincode_et.requestFocus();
            return false;
        }
        else if (addressLine1.length() == 0 || addressLine1 == null|| addressLine1.equalsIgnoreCase("")) {
            address_line1_error_tv.setVisibility(View.VISIBLE);
            address_line1_et.setBackgroundResource(R.drawable.border_red_line_view);
            address_line1_et.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateDealerAllFieldSignupFields()
    {
        String companyName = company_name_et.getText().toString().trim();
        String companyEmail = company_email_et.getText().toString().trim();
        String companyMobileNo = company_mobile_number_et.getText().toString().trim();
        String companyCountry = company_country_tv.getText().toString().trim();
        String companyState = company_state_tv.getText().toString().trim();
        String companyCity = company_city_tv.getText().toString().trim();
        String companyPinCode = company_pincode_et.getText().toString().trim();
        String addressLine1 = company_address_line1_et.getText().toString().trim();
        String companyType = company_type_tv.getText().toString().trim();
        String businessType = business_nature_tv.getText().toString().trim();
        //String companyGSTNo = company_gst_no_et.getText().toString().trim();
        String companyPanNo = company_pan_no_et.getText().toString().trim();
        String businessLicenceNo = company_business_lic_no_et.getText().toString().trim();

        String basicFirstName = basic_first_name_et.getText().toString().trim();
        String basicLastName = basic_last_name_et.getText().toString().trim();
        String basicMobileNo = basic_mobile_number_et.getText().toString().trim();
        String basicEmail = basic_email_et.getText().toString().trim();
        String basicPassword = basic_password_et.getText().toString().trim();
        String basicConfirmPassword = basic_confirm_password_et.getText().toString().trim();


        if ((companyName.length() == 0 || companyName == null|| companyName.equalsIgnoreCase("")) &&
                (companyEmail.length() == 0 || companyEmail == null|| companyEmail.equalsIgnoreCase("")) &&
                (companyMobileNo.length() == 0 || companyMobileNo == null || companyMobileNo.equalsIgnoreCase("")) &&
                (companyCountry.length() == 0 || companyCountry == null|| companyCountry.equalsIgnoreCase("")) &&
                (companyState.length() == 0 || companyState == null|| companyState.equalsIgnoreCase("")) &&
                (companyCity.length() == 0 || companyCity == null|| companyCity.equalsIgnoreCase("")) &&
                (companyPinCode.length() == 0 || companyPinCode == null|| companyPinCode.equalsIgnoreCase("")) &&
                (addressLine1.length() == 0 || addressLine1 == null|| addressLine1.equalsIgnoreCase("")) &&
                (companyType.length() == 0 || companyType == null|| companyType.equalsIgnoreCase("")) &&
                (businessType.length() == 0 || businessType == null|| businessType.equalsIgnoreCase("")) &&
                (businessLicenceNo.length() == 0 || businessLicenceNo == null|| businessLicenceNo.equalsIgnoreCase("")) &&
                (companyPanNo.length() == 0 || companyPanNo == null|| companyPanNo.equalsIgnoreCase("")) &&
                (!isCompanyPanDocUpload) &&
                (basicFirstName.length() == 0 || basicFirstName == null|| basicFirstName.equalsIgnoreCase(""))&&
                (basicLastName.length() == 0 || basicLastName == null|| basicLastName.equalsIgnoreCase("")) &&
                (basicMobileNo.length() == 0 || basicMobileNo == null|| basicMobileNo.equalsIgnoreCase("")) &&
                (basicEmail.length() == 0 || basicEmail == null|| basicEmail.equalsIgnoreCase("")) &&
                (basicPassword.length() == 0 || basicPassword == null|| basicPassword.equalsIgnoreCase("")) &&
                (basicConfirmPassword.length() == 0 || basicConfirmPassword == null|| basicConfirmPassword.equalsIgnoreCase("")) &&
                (basicFirstName.length() == 0 || basicFirstName == null|| basicFirstName.equalsIgnoreCase("")))
        {

            company_name_et.requestFocus();

            isArrowDownCompanyKyc = true;//for Company Card Open Visible
            drop_arrow_company_kyc_img.setImageResource(R.drawable.up);//for Company Card Open Visible
            company_kyc_lin.setVisibility(View.VISIBLE); //for Company Card Open Visible

            isArrowDownCompanyBasic = true;//for Company Basic Details Open Visible
            drop_arrow_basic_details_img.setImageResource(R.drawable.up);//for Company Card Open Visible
            basic_details_lin.setVisibility(View.VISIBLE); //for Company Card Open Visible

            company_name_error_tv.setVisibility(View.VISIBLE);
            company_name_et.setBackgroundResource(R.drawable.border_red_line_view);

            company_email_error_tv.setVisibility(View.VISIBLE);
            company_email_et.setBackgroundResource(R.drawable.border_red_line_view);

            company_mobile_number_error_tv.setVisibility(View.VISIBLE);
            company_mobile_number_lin.setBackgroundResource(R.drawable.border_red_line_view);
            company_mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_required));

            company_country_error_tv.setVisibility(View.VISIBLE);
            company_country_lin.setBackgroundResource(R.drawable.border_red_line_view);

            company_state_error_tv.setVisibility(View.VISIBLE);
            company_state_lin.setBackgroundResource(R.drawable.border_red_line_view);

            company_city_error_tv.setVisibility(View.VISIBLE);
            company_city_lin.setBackgroundResource(R.drawable.border_red_line_view);

            company_pincode_error_tv.setVisibility(View.VISIBLE);
            company_pincode_et.setBackgroundResource(R.drawable.border_red_line_view);

            company_address_line1_error_tv.setVisibility(View.VISIBLE);
            company_address_line1_et.setBackgroundResource(R.drawable.border_red_line_view);

            company_type_error_tv.setVisibility(View.VISIBLE);
            company_type_lin.setBackgroundResource(R.drawable.border_red_line_view);

            business_nature_error_tv.setVisibility(View.VISIBLE);
            business_nature_lin.setBackgroundResource(R.drawable.border_red_line_view);

            String companyCountryName = company_country_tv.getText().toString().trim();
            // Check Country India or Not If India Check GST and Pan Number Verification Other wise business Licence No Verification.
            if(companyCountryName.equalsIgnoreCase("") || companyCountryName.equalsIgnoreCase("India"))
            {
                auth_person_card.setVisibility(View.VISIBLE);
                gst_pan_no_lin.setVisibility(View.VISIBLE);
                business_licence_lin.setVisibility(View.GONE);

                company_pan_no_error_tv.setVisibility(View.VISIBLE);
                company_pan_back_rel.setBackgroundResource(R.drawable.border_red_line_view);
                company_pan_no_error_tv.requestFocus();

                company_pan_doc_error_tv.setVisibility(View.VISIBLE);
                company_pan_doc_rel.setBackgroundResource(R.drawable.border_red_line_view);
                company_pan_doc_rel.requestFocus();
            }
            else{
                auth_person_card.setVisibility(View.GONE);
                gst_pan_no_lin.setVisibility(View.GONE);
                business_licence_lin.setVisibility(View.VISIBLE);

                company_business_lic_no_error_tv.setVisibility(View.VISIBLE);
                company_business_lic_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                company_business_lic_no_et.requestFocus();

                company_business_lic_doc_error_tv.setVisibility(View.VISIBLE);
                company_business_lic_doc_rel.setBackgroundResource(R.drawable.border_red_line_view);
                company_business_lic_doc_rel.requestFocus();
            }

            basic_first_name_error_tv.setVisibility(View.VISIBLE);
            basic_first_name_et.setBackgroundResource(R.drawable.border_red_line_view);

            basic_last_name_error_tv.setVisibility(View.VISIBLE);
            basic_last_name_et.setBackgroundResource(R.drawable.border_red_line_view);

            basic_mobile_number_error_tv.setVisibility(View.VISIBLE);
            basic_mobile_number_lin.setBackgroundResource(R.drawable.border_red_line_view);
            basic_mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_required));

            basic_email_error_tv.setVisibility(View.VISIBLE);
            basic_email_et.setBackgroundResource(R.drawable.border_red_line_view);

            basic_password_error_tv.setVisibility(View.VISIBLE);
            basic_password_et.setBackgroundResource(R.drawable.border_red_line_view);

            basic_confirm_password_error_tv.setVisibility(View.VISIBLE);
            basic_confirm_password_et.setBackgroundResource(R.drawable.border_red_line_view);

            return false;
        }

        return true;


    }

    private boolean validateDealerSignupFields()
    {
        String companyName = company_name_et.getText().toString().trim();
        String companyEmail = company_email_et.getText().toString().trim();
        String companyMobileNo = company_mobile_number_et.getText().toString().trim();
        String companyCountry = company_country_tv.getText().toString().trim();
        String companyState = company_state_tv.getText().toString().trim();
        String companyCity = company_city_tv.getText().toString().trim();
        String companyPinCode = company_pincode_et.getText().toString().trim();
        String addressLine1 = company_address_line1_et.getText().toString().trim();
        String companyType = company_type_tv.getText().toString().trim();
        String businessType = business_nature_tv.getText().toString().trim();
        //String companyGSTNo = company_gst_no_et.getText().toString().trim();
        String companyPanNo = company_pan_no_et.getText().toString().trim();
        String businessLicenceNo = company_business_lic_no_et.getText().toString().trim();

        String basicFirstName = basic_first_name_et.getText().toString().trim();
        String basicLastName = basic_last_name_et.getText().toString().trim();
        String basicMobileNo = basic_mobile_number_et.getText().toString().trim();
        String basicEmail = basic_email_et.getText().toString().trim();
        String basicPassword = basic_password_et.getText().toString().trim();
        String basicConfirmPassword = basic_confirm_password_et.getText().toString().trim();


        if (companyName.length() == 0 || companyName == null|| companyName.equalsIgnoreCase("")) {
            company_name_error_tv.setVisibility(View.VISIBLE);
            company_name_et.setBackgroundResource(R.drawable.border_red_line_view);
            company_name_et.requestFocus();
            return false;
        }
        else if (companyEmail.length() == 0 || companyEmail == null|| companyEmail.equalsIgnoreCase("")) {
            company_email_error_tv.setVisibility(View.VISIBLE);
            company_email_et.setBackgroundResource(R.drawable.border_red_line_view);
            company_email_et.requestFocus();
            return false;
        }
        else if (!Utils.emailValidator(companyEmail))
        {
            company_email_error_tv.setVisibility(View.VISIBLE);
            company_email_et.setBackgroundResource(R.drawable.border_red_line_view);
            company_email_error_tv.setText(getResources().getString(R.string.email_valid_msg));
            company_email_et.requestFocus();
            return false;
        }
        else if (companyMobileNo.length() == 0 || companyMobileNo == null || companyMobileNo.equalsIgnoreCase("")) {
            company_mobile_number_error_tv.setVisibility(View.VISIBLE);
            company_mobile_number_lin.setBackgroundResource(R.drawable.border_red_line_view);
            company_mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_required));
            company_mobile_number_lin.requestFocus();
            return false;
        } else if (companyMobileNo.length() < 7 || companyMobileNo.length() > 14) {
            company_mobile_number_error_tv.setVisibility(View.VISIBLE);
            company_mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
            company_mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_valid_msg));
            company_mobile_number_lin.requestFocus();
            return false;
        }
        else if (companyCountry.length() == 0 || companyCountry == null || companyCountry.equalsIgnoreCase("")) {
            company_country_error_tv.setVisibility(View.VISIBLE);
            company_country_lin.setBackgroundResource(R.drawable.border_red_line_view);
            company_country_tv.requestFocus();
            return false;
        }
        else if (companyState.length() == 0 || companyState == null || companyState.equalsIgnoreCase("")) {
            company_state_error_tv.setVisibility(View.VISIBLE);
            company_state_lin.setBackgroundResource(R.drawable.border_red_line_view);
            company_state_tv.requestFocus();
            return false;
        }
        else if (companyCity.length() == 0 || companyCity == null || companyCity.equalsIgnoreCase("")) {
            company_city_error_tv.setVisibility(View.VISIBLE);
            company_city_lin.setBackgroundResource(R.drawable.border_red_line_view);
            company_city_tv.requestFocus();
            return false;
        }
        else if (companyPinCode.length() == 0 || companyPinCode == null || companyPinCode.equalsIgnoreCase("")) {
            company_pincode_error_tv.setVisibility(View.VISIBLE);
            company_pincode_et.setBackgroundResource(R.drawable.border_red_line_view);
            company_pincode_et.requestFocus();
            return false;
        }
        else if (addressLine1.length() == 0 || addressLine1 == null|| addressLine1.equalsIgnoreCase("")) {
            company_address_line1_error_tv.setVisibility(View.VISIBLE);
            company_address_line1_et.setBackgroundResource(R.drawable.border_red_line_view);
            company_address_line1_et.requestFocus();
            return false;
        }
        else if (companyType.length() == 0 || companyType == null|| companyType.equalsIgnoreCase("")) {
            company_type_error_tv.setVisibility(View.VISIBLE);
            company_type_lin.setBackgroundResource(R.drawable.border_red_line_view);
            company_type_tv.requestFocus();
            return false;
        }
        else if (businessType.length() == 0 || businessType == null|| businessType.equalsIgnoreCase("")) {
            business_nature_error_tv.setVisibility(View.VISIBLE);
            business_nature_lin.setBackgroundResource(R.drawable.border_red_line_view);
            business_nature_tv.requestFocus();
            return false;
        }
        /*else if (companyGSTNo.length() == 0 || companyGSTNo == null|| companyGSTNo.equalsIgnoreCase("")) {
            address_line1_error_tv.setVisibility(View.VISIBLE);
            address_line1_et.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }*/

        else if ((companyPanNo.length() == 0 || companyPanNo == null|| companyPanNo.equalsIgnoreCase("")) && company_country_tv.getText().toString().equalsIgnoreCase("India"))
        {
            isArrowDownCompanyKyc = true;//for Company Card Open Visible
            drop_arrow_company_kyc_img.setImageResource(R.drawable.up);//for Company Card Open Visible
            company_kyc_lin.setVisibility(View.VISIBLE); //for Company Card Open Visible
            company_pan_no_error_tv.setVisibility(View.VISIBLE);
            company_pan_no_error_tv.setText(getResources().getString(R.string.pan_number_required));
            company_pan_back_rel.setBackgroundResource(R.drawable.border_red_line_view);
            company_pan_no_et.requestFocus();
            return false;
        }
        else if(!isCompanyPanNoVerified && company_country_tv.getText().toString().equalsIgnoreCase("India"))
        {
            company_pan_no_error_tv.setVisibility(View.VISIBLE);
            company_pan_back_rel.setBackgroundResource(R.drawable.border_red_line_view);
            company_pan_no_et.requestFocus();
            return false;
        }
        else if(isCompanyPanDocUpload == false && company_country_tv.getText().toString().equalsIgnoreCase("India"))
        {
            company_pan_doc_error_tv.setVisibility(View.VISIBLE);
            company_pan_doc_rel.setBackgroundResource(R.drawable.border_red_line_view);
            company_pan_doc_rel.requestFocus();
            return false;
        }

        else if ((businessLicenceNo.length() == 0 || businessLicenceNo == null|| businessLicenceNo.equalsIgnoreCase("")) && !company_country_tv.getText().toString().equalsIgnoreCase("India"))
        {
            isArrowDownCompanyKyc = true;//for Company Card Open Visible
            drop_arrow_company_kyc_img.setImageResource(R.drawable.up);//for Company Card Open Visible
            company_kyc_lin.setVisibility(View.VISIBLE); //for Company Card Open Visible
            company_business_lic_no_error_tv.setVisibility(View.VISIBLE);
            company_business_lic_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
            company_business_lic_no_et.requestFocus();
            return false;
        }
        else if(isBusinessLicenceNo == false && !company_country_tv.getText().toString().equalsIgnoreCase("India"))
        {
            company_business_lic_doc_error_tv.setVisibility(View.VISIBLE);
            company_business_lic_doc_rel.setBackgroundResource(R.drawable.border_red_line_view);
            company_business_lic_doc_rel.requestFocus();
            return false;
        }
        else if (basicFirstName.length() == 0 || basicFirstName == null|| basicFirstName.equalsIgnoreCase(""))
        {
            isArrowDownCompanyBasic = true;//for Company Baisc Details Open Visible
            drop_arrow_basic_details_img.setImageResource(R.drawable.up);//for Company Card Open Visible
            basic_details_lin.setVisibility(View.VISIBLE); //for Company Card Open Visible

            basic_first_name_error_tv.setVisibility(View.VISIBLE);
            basic_first_name_et.setBackgroundResource(R.drawable.border_red_line_view);
            basic_first_name_et.requestFocus();
            return false;
        }
        else if (basicLastName.length() == 0 || basicLastName == null|| basicLastName.equalsIgnoreCase("")) {
            basic_last_name_error_tv.setVisibility(View.VISIBLE);
            basic_last_name_et.setBackgroundResource(R.drawable.border_red_line_view);
            basic_last_name_et.requestFocus();
            return false;
        }
        else if (basicMobileNo.length() == 0 || basicMobileNo == null || basicMobileNo.equalsIgnoreCase("")) {
            basic_mobile_number_error_tv.setVisibility(View.VISIBLE);
            basic_mobile_number_lin.setBackgroundResource(R.drawable.border_red_line_view);
            basic_mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_required));
            basic_mobile_number_lin.requestFocus();
            return false;
        } else if (basicMobileNo.length() < 7 || basicMobileNo.length() > 14) {
            basic_mobile_number_error_tv.setVisibility(View.VISIBLE);
            basic_mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
            basic_mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_valid_msg));
            basic_mobile_number_lin.requestFocus();
            return false;
        }
        else if (basicEmail.length() == 0 || basicEmail == null|| basicEmail.equalsIgnoreCase("")) {
            basic_email_error_tv.setVisibility(View.VISIBLE);
            basic_email_et.setBackgroundResource(R.drawable.border_red_line_view);
            basic_email_error_tv.setText(getResources().getString(R.string.email_required));
            basic_email_et.requestFocus();
            return false;
        }
        else if (!Utils.emailValidator(basicEmail))
        {
            basic_email_error_tv.setVisibility(View.VISIBLE);
            basic_email_et.setBackgroundResource(R.drawable.border_red_line_view);
            basic_email_error_tv.setText(getResources().getString(R.string.email_valid_msg));
            basic_email_et.requestFocus();
            return false;
        }
        else if(!isBasicEmailVerified)
        {
            basic_email_error_tv.setVisibility(View.VISIBLE);
            basic_email_et.setBackgroundResource(R.drawable.border_red_line_view);
            basic_email_error_tv.setText(getResources().getString(R.string.email_verify_required));
            basic_email_et.requestFocus();
            return false;
        }
        else if (basicPassword.length() == 0 || basicPassword == null|| basicPassword.equalsIgnoreCase("")) {
            basic_password_error_tv.setVisibility(View.VISIBLE);
            basic_password_et.setBackgroundResource(R.drawable.border_red_line_view);
            basic_password_et.requestFocus();
            return false;
        }
        else if (basicPassword.length()<8) {
            basic_password_error_tv.setVisibility(View.VISIBLE);
            basic_password_et.setBackgroundResource(R.drawable.border_red_line_view);
            basic_password_error_tv.setText(getResources().getString(R.string.password_length_msg));
            basic_password_et.requestFocus();
            return false;
        }
        else if (!CommonUtility.isValidPassword(basicPassword)) {
            basic_password_error_tv.setVisibility(View.VISIBLE);
            basic_password_et.setBackgroundResource(R.drawable.border_red_line_view);
            basic_password_error_tv.setText(getResources().getString(R.string.password_validation_msg));
            basic_password_et.requestFocus();
            return false;
        }
        else if (basicConfirmPassword.length() == 0 || basicConfirmPassword == null|| basicConfirmPassword.equalsIgnoreCase("")) {
            basic_confirm_password_error_tv.setVisibility(View.VISIBLE);
            basic_confirm_password_et.setBackgroundResource(R.drawable.border_red_line_view);
            basic_confirm_password_et.requestFocus();
            return false;
        }
        else if (!basicConfirmPassword.equals(""+basicPassword)) {
            basic_confirm_password_error_tv.setVisibility(View.VISIBLE);
            basic_confirm_password_et.setBackgroundResource(R.drawable.border_red_line_view);
            basic_confirm_password_error_tv.setText(getResources().getString(R.string.password_confirm_password_not_match));
            basic_confirm_password_et.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateSupplierFields()
    {
        String companyName = supplier_company_name_et.getText().toString().trim();
        String email = supplier_company_email_et.getText().toString().trim();
        String password = supplier_password_et.getText().toString().trim();
        String confirmPassword = supplier_confirm_password_et.getText().toString().trim();
        String mobileNumber = supplier_mobile_number_et.getText().toString().trim();
        String inventory = inventory_tv.getText().toString().trim();
        String country = supplier_country_tv.getText().toString().trim();
        String state = supplier_state_tv.getText().toString().trim();
        String city = supplier_city_tv.getText().toString().trim();
        String addressLine1 = supplier_address_line1_et.getText().toString().trim();
        String pinCode = supplier_pincode_et.getText().toString().trim();
        String supplierPanNo = supplier_company_pan_no_et.getText().toString().trim();
        String businessLicenceNo = suppier_business_lic_no_et.getText().toString().trim();
        String companyType = supplier_company_type_tv.getText().toString().trim();
        String businessType = supplier_business_nature_tv.getText().toString().trim();
        String superviosrName = superviosr_name_et.getText().toString().trim();
        String superviosrEmail = superviosr_email_et.getText().toString().trim();
        String superviosrMobileNumber = superviosr_mobile_number_et.getText().toString().trim();

        if (companyName.length() == 0 || companyName == null|| companyName.equalsIgnoreCase("")) {
            suppliercompany_name_error_tv.setVisibility(View.VISIBLE);
            supplier_company_name_et.setBackgroundResource(R.drawable.border_red_line_view);
            supplier_company_name_et.requestFocus();
            return false;
        }
        else if (email.length() == 0 || email == null|| email.equalsIgnoreCase("")) {
            supplier__email_error_tv.setVisibility(View.VISIBLE);
            supplier_company_email_et.setBackgroundResource(R.drawable.border_red_line_view);
            supplier__email_error_tv.setText(getResources().getString(R.string.email_required));
            supplier_company_email_et.requestFocus();
            return false;
        }
        else if (!Utils.emailValidator(email))
        {
            supplier__email_error_tv.setVisibility(View.VISIBLE);
            supplier_company_email_et.setBackgroundResource(R.drawable.border_red_line_view);
            supplier__email_error_tv.setText(getResources().getString(R.string.email_valid_msg));
            supplier_company_email_et.requestFocus();
            return false;
        }
        else if(!isSupplierEmailVerified)
        {
            supplier__email_error_tv.setVisibility(View.VISIBLE);
            supplier_company_email_et.setBackgroundResource(R.drawable.border_red_line_view);
            supplier__email_error_tv.setText(getResources().getString(R.string.email_verify_required));
            supplier_company_email_et.requestFocus();
            return false;
        }
        else if (password.length() == 0 || password == null|| password.equalsIgnoreCase("")) {
            supplier_password_error_tv.setVisibility(View.VISIBLE);
            supplier_password_et.setBackgroundResource(R.drawable.border_red_line_view);
            supplier_password_et.requestFocus();
            return false;
        }
        else if (password.length()<8) {
            supplier_password_error_tv.setVisibility(View.VISIBLE);
            supplier_password_et.setBackgroundResource(R.drawable.border_red_line_view);
            supplier_password_error_tv.setText(getResources().getString(R.string.password_length_msg));
            supplier_password_et.requestFocus();
            return false;
        }
        else if (!CommonUtility.isValidPassword(password)) {
            supplier_password_error_tv.setVisibility(View.VISIBLE);
            supplier_password_et.setBackgroundResource(R.drawable.border_red_line_view);
            supplier_password_error_tv.setText(getResources().getString(R.string.password_validation_msg));
            supplier_password_et.requestFocus();
            return false;
        }
        else if (confirmPassword.length() == 0 || confirmPassword == null|| confirmPassword.equalsIgnoreCase("")) {
            supplier_confirm_password_error_tv.setVisibility(View.VISIBLE);
            supplier_confirm_password_et.setBackgroundResource(R.drawable.border_red_line_view);
            supplier_confirm_password_error_tv.setText(getResources().getString(R.string.confirm_new_password_require));
            supplier_confirm_password_et.requestFocus();
            return false;
        }
        else if (!confirmPassword.equals(""+password)) {
            supplier_confirm_password_error_tv.setVisibility(View.VISIBLE);
            supplier_confirm_password_et.setBackgroundResource(R.drawable.border_red_line_view);
            supplier_confirm_password_error_tv.setText(getResources().getString(R.string.password_confirm_password_not_match));
            supplier_confirm_password_et.requestFocus();
            return false;
        }
        else if (mobileNumber.length() == 0 || mobileNumber == null || mobileNumber.equalsIgnoreCase("")) {
            supplier_mobile_number_error_tv.setVisibility(View.VISIBLE);
            supplier_mobile_number_lin.setBackgroundResource(R.drawable.border_red_line_view);
            supplier_mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_required));
            supplier_mobile_number_lin.requestFocus();
            return false;
        } else if (mobileNumber.length() < 7 || mobileNumber.length() > 14) {
            supplier_mobile_number_error_tv.setVisibility(View.VISIBLE);
            supplier_mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
            supplier_mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_valid_msg));
            supplier_mobile_number_lin.requestFocus();
            return false;
        }
        else if (inventory.length() == 0 || inventory == null|| inventory.equalsIgnoreCase("")) {
            inventory_error_tv.setVisibility(View.VISIBLE);
            inventory_lin.setBackgroundResource(R.drawable.border_red_line_view);
            inventory_lin.requestFocus();
            return false;
        }
        else if (country.length() == 0 || country == null|| country.equalsIgnoreCase("")) {
            supplier_country_error_tv.setVisibility(View.VISIBLE);
            supplier_country_lin.setBackgroundResource(R.drawable.border_red_line_view);
            supplier_country_lin.requestFocus();
            return false;
        }
        else if (state.length() == 0 || state == null|| state.equalsIgnoreCase("")) {
            supplier_state_error_tv.setVisibility(View.VISIBLE);
            supplier_state_lin.setBackgroundResource(R.drawable.border_red_line_view);
            supplier_state_lin.requestFocus();
            return false;
        }
        else if (city.length() == 0 || city == null|| city.equalsIgnoreCase("")) {
            supplier_city_error_tv.setVisibility(View.VISIBLE);
            supplier_city_lin.setBackgroundResource(R.drawable.border_red_line_view);
            supplier_city_lin.requestFocus();
            return false;
        }
        else if (addressLine1.length() == 0 || addressLine1 == null|| addressLine1.equalsIgnoreCase("")) {
            supplier_address_line1_error_tv.setVisibility(View.VISIBLE);
            supplier_address_line1_et.setBackgroundResource(R.drawable.border_red_line_view);
            supplier_address_line1_et.requestFocus();
            return false;
        }
        else if (pinCode.length() == 0 || pinCode == null|| pinCode.equalsIgnoreCase("")) {
            supplier_pincode_error_tv.setVisibility(View.VISIBLE);
            supplier_pincode_et.setBackgroundResource(R.drawable.border_red_line_view);
            supplier_pincode_et.requestFocus();
            return false;
        }
        else if ((supplierPanNo.length() == 0 || supplierPanNo == null|| supplierPanNo.equalsIgnoreCase("")) && supplier_country_tv.getText().toString().equalsIgnoreCase("India"))
        {
            isArrowDownSupplierCompanyKyc = true;//for Company Card Open Visible
            drop_arrow_supplier_kyc_img.setImageResource(R.drawable.up);//for Company Card Open Visible
            supplier_company_kyc_lin.setVisibility(View.VISIBLE); //for Company Card Open Visible
            supplier_company_pan_no_error_tv.setVisibility(View.VISIBLE);
            supplier_company_pan_no_error_tv.setText(getResources().getString(R.string.pan_number_required));
            supplier_company_pan_back_rel.setBackgroundResource(R.drawable.border_red_line_view);
            supplier_company_pan_no_et.requestFocus();
            return false;
        }
        else if(!isSupplierPanNoVerified && supplier_country_tv.getText().toString().equalsIgnoreCase("India"))
        {
            supplier_company_pan_no_error_tv.setVisibility(View.VISIBLE);
            supplier_company_pan_back_rel.setBackgroundResource(R.drawable.border_red_line_view);
            company_pan_no_et.requestFocus();
            return false;
        }
        else if(isSupplierPanDocUpload == false && supplier_country_tv.getText().toString().equalsIgnoreCase("India"))
        {
            supplier_company_pan_doc_error_tv.setVisibility(View.VISIBLE);
            supplier_company_pan_doc_rel.setBackgroundResource(R.drawable.border_red_line_view);
            supplier_company_pan_doc_rel.requestFocus();
            return false;
        }
        else if ((businessLicenceNo.length() == 0 || businessLicenceNo == null|| businessLicenceNo.equalsIgnoreCase("")) && !supplier_country_tv.getText().toString().equalsIgnoreCase("India"))
        {
            isArrowDownSupplierCompanyKyc = true;//for Company Card Open Visible
            drop_arrow_supplier_kyc_img.setImageResource(R.drawable.up);//for Company Card Open Visible
            supplier_company_kyc_lin.setVisibility(View.VISIBLE); //for Company Card Open Visible
            suppier_business_lic_no_error_tv.setVisibility(View.VISIBLE);
            suppier_business_lic_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
            suppier_business_lic_no_et.requestFocus();
            return false;
        }
        else if(isSupplierBusinessDocUpload == false && !supplier_country_tv.getText().toString().equalsIgnoreCase("India"))
        {
            suppier_business_lic_doc_error_tv.setVisibility(View.VISIBLE);
            suppier_business_lic_doc_rel.setBackgroundResource(R.drawable.border_red_line_view);
            suppier_business_lic_doc_rel.requestFocus();
            return false;
        }
        else if (companyType.length() == 0 || companyType == null|| companyType.equalsIgnoreCase("")) {
            supplier_company_type_error_tv.setVisibility(View.VISIBLE);
            supplier_company_type_lin.setBackgroundResource(R.drawable.border_red_line_view);
            supplier_company_type_tv.requestFocus();
            return false;
        }
        else if (businessType.length() == 0 || businessType == null|| businessType.equalsIgnoreCase("")) {
            supplier_business_nature_error_tv.setVisibility(View.VISIBLE);
            supplier_business_nature_lin.setBackgroundResource(R.drawable.border_red_line_view);
            supplier_business_nature_tv.requestFocus();
            return false;
        }
        else if (superviosrName.length() == 0 || superviosrName == null|| superviosrName.equalsIgnoreCase(""))
        {
            isArrowDownSupervisor = true;
            drop_arrow_superviosr_details_img.setImageResource(R.drawable.up);
            superviosr_details_lin.setVisibility(View.VISIBLE);

            superviosr_name_error_tv.setVisibility(View.VISIBLE);
            superviosr_name_et.setBackgroundResource(R.drawable.border_red_line_view);
            superviosr_name_et.requestFocus();
            return false;
        }
        else if (superviosrEmail.length() == 0 || superviosrEmail == null|| superviosrEmail.equalsIgnoreCase("")) {
            superviosremail_error_tv.setVisibility(View.VISIBLE);
            superviosr_email_et.setBackgroundResource(R.drawable.border_red_line_view);
            superviosremail_error_tv.setText(getResources().getString(R.string.email_required));
            superviosr_email_et.requestFocus();
            return false;
        }
        else if (!Utils.emailValidator(superviosrEmail))
        {
            superviosremail_error_tv.setVisibility(View.VISIBLE);
            superviosr_email_et.setBackgroundResource(R.drawable.border_red_line_view);
            superviosremail_error_tv.setText(getResources().getString(R.string.email_valid_msg));
            superviosr_email_et.requestFocus();
            return false;
        }
        else if (superviosrMobileNumber.length() == 0 || superviosrMobileNumber == null || superviosrMobileNumber.equalsIgnoreCase("")) {
            superviosr_mobile_number_error_tv.setVisibility(View.VISIBLE);
            superviosr_mobile_number_lin.setBackgroundResource(R.drawable.border_red_line_view);
            superviosr_mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_required));
            superviosr_mobile_number_lin.requestFocus();
            return false;
        } else if (superviosrMobileNumber.length() < 7 || superviosrMobileNumber.length() > 14) {
            superviosr_mobile_number_error_tv.setVisibility(View.VISIBLE);
            superviosr_mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
            superviosr_mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_valid_msg));
            superviosr_mobile_number_lin.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateSupplierAllFieldSignupFields()
    {
        String companyName = supplier_company_name_et.getText().toString().trim();
        String companyEmail = supplier_company_email_et.getText().toString().trim();
        String password = supplier_password_et.getText().toString().trim();
        String confirmPassword = supplier_confirm_password_et.getText().toString().trim();
        String companyMobileNo = supplier_mobile_number_et.getText().toString().trim();
        String inventory = inventory_tv.getText().toString().trim();
        String companyCountry = supplier_country_tv.getText().toString().trim();
        String companyState = supplier_state_tv.getText().toString().trim();
        String companyCity = supplier_city_tv.getText().toString().trim();
        String addressLine1 = supplier_address_line1_et.getText().toString().trim();
        String companyPinCode = supplier_pincode_et.getText().toString().trim();
        String supplierPanNo = supplier_company_pan_no_et.getText().toString().trim();
        String businessLicenceNo = suppier_business_lic_no_et.getText().toString().trim();
        String companyType = supplier_company_type_tv.getText().toString().trim();
        String businessType = supplier_business_nature_tv.getText().toString().trim();
        String superviosrName = superviosr_name_et.getText().toString().trim();
        String superviosrEmail = superviosr_email_et.getText().toString().trim();
        String superviosrMobileNumber = superviosr_mobile_number_et.getText().toString().trim();

        if ((companyName.length() == 0 || companyName == null|| companyName.equalsIgnoreCase("")) &&
                (companyEmail.length() == 0 || companyEmail == null|| companyEmail.equalsIgnoreCase("")) &&
                (password.length() == 0 || password == null|| password.equalsIgnoreCase("")) &&
                (confirmPassword.length() == 0 || confirmPassword == null|| confirmPassword.equalsIgnoreCase("")) &&
                (companyMobileNo.length() == 0 || companyMobileNo == null || companyMobileNo.equalsIgnoreCase("")) &&
                (inventory.length() == 0 || inventory == null || inventory.equalsIgnoreCase("")) &&
                (companyCountry.length() == 0 || companyCountry == null|| companyCountry.equalsIgnoreCase("")) &&
                (companyState.length() == 0 || companyState == null|| companyState.equalsIgnoreCase("")) &&
                (companyCity.length() == 0 || companyCity == null|| companyCity.equalsIgnoreCase("")) &&
                (addressLine1.length() == 0 || addressLine1 == null|| addressLine1.equalsIgnoreCase("")) &&
                (companyPinCode.length() == 0 || companyPinCode == null|| companyPinCode.equalsIgnoreCase("")) &&
                (supplierPanNo.length() == 0 || supplierPanNo == null|| supplierPanNo.equalsIgnoreCase("")) &&
                (!isSupplierPanDocUpload) &&
                (businessLicenceNo.length() == 0 || businessLicenceNo == null|| businessLicenceNo.equalsIgnoreCase("")) &&
                (companyType.length() == 0 || companyType == null|| companyType.equalsIgnoreCase("")) &&
                (businessType.length() == 0 || businessType == null|| businessType.equalsIgnoreCase("")) &&
                (superviosrName.length() == 0 || superviosrName == null|| superviosrName.equalsIgnoreCase(""))&&
                (superviosrEmail.length() == 0 || superviosrEmail == null|| superviosrEmail.equalsIgnoreCase("")) &&
                (superviosrMobileNumber.length() == 0 || superviosrMobileNumber == null|| superviosrMobileNumber.equalsIgnoreCase("")))
        {

            supplier_company_name_et.requestFocus();

            isArrowDownSupplierCompanyKyc = true;//for Company Card Open Visible
            drop_arrow_supplier_kyc_img.setImageResource(R.drawable.up);//for Company Card Open Visible
            supplier_company_kyc_lin.setVisibility(View.VISIBLE); //for Company Card Open Visible

            isArrowDownSupervisor = true;
            drop_arrow_superviosr_details_img.setImageResource(R.drawable.up);
            superviosr_details_lin.setVisibility(View.VISIBLE);

            suppliercompany_name_error_tv.setVisibility(View.VISIBLE);
            supplier_company_name_et.setBackgroundResource(R.drawable.border_red_line_view);

            supplier__email_error_tv.setVisibility(View.VISIBLE);
            supplier_company_email_et.setBackgroundResource(R.drawable.border_red_line_view);
            supplier__email_error_tv.setText(getResources().getString(R.string.email_required));

            supplier_password_error_tv.setVisibility(View.VISIBLE);
            supplier_password_et.setBackgroundResource(R.drawable.border_red_line_view);

            supplier_confirm_password_error_tv.setVisibility(View.VISIBLE);
            supplier_confirm_password_et.setBackgroundResource(R.drawable.border_red_line_view);

            supplier_mobile_number_error_tv.setVisibility(View.VISIBLE);
            supplier_mobile_number_lin.setBackgroundResource(R.drawable.border_red_line_view);
            supplier_mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_required));

            inventory_error_tv.setVisibility(View.VISIBLE);
            inventory_lin.setBackgroundResource(R.drawable.border_red_line_view);

            supplier_country_error_tv.setVisibility(View.VISIBLE);
            supplier_country_lin.setBackgroundResource(R.drawable.border_red_line_view);

            supplier_state_error_tv.setVisibility(View.VISIBLE);
            supplier_state_lin.setBackgroundResource(R.drawable.border_red_line_view);

            supplier_city_error_tv.setVisibility(View.VISIBLE);
            supplier_city_lin.setBackgroundResource(R.drawable.border_red_line_view);

            supplier_address_line1_error_tv.setVisibility(View.VISIBLE);
            supplier_address_line1_et.setBackgroundResource(R.drawable.border_red_line_view);

            supplier_pincode_error_tv.setVisibility(View.VISIBLE);
            supplier_pincode_et.setBackgroundResource(R.drawable.border_red_line_view);


            String supplierCountryName = supplier_country_tv.getText().toString().trim();
            // Check Country India or Not If India Check GST and Pan Number Verification Other wise business Licence No Verification.
            if(supplierCountryName.equalsIgnoreCase("") || supplierCountryName.equalsIgnoreCase("India"))
            {

                suppier_gst_pan_no_lin.setVisibility(View.VISIBLE);
                suppier_business_licence_lin.setVisibility(View.GONE);

                supplier_company_pan_no_error_tv.setVisibility(View.VISIBLE);
                supplier_company_pan_back_rel.setBackgroundResource(R.drawable.border_red_line_view);
                supplier_company_pan_no_error_tv.requestFocus();
            }
            else{
                suppier_gst_pan_no_lin.setVisibility(View.GONE);
                suppier_business_licence_lin.setVisibility(View.VISIBLE);

                suppier_business_lic_no_error_tv.setVisibility(View.VISIBLE);
                suppier_business_lic_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                suppier_business_lic_no_error_tv.requestFocus();
            }

            supplier_company_type_error_tv.setVisibility(View.VISIBLE);
            supplier_company_type_lin.setBackgroundResource(R.drawable.border_red_line_view);

            supplier_business_nature_error_tv.setVisibility(View.VISIBLE);
            supplier_business_nature_lin.setBackgroundResource(R.drawable.border_red_line_view);

            superviosr_name_error_tv.setVisibility(View.VISIBLE);
            superviosr_name_et.setBackgroundResource(R.drawable.border_red_line_view);

            superviosremail_error_tv.setVisibility(View.VISIBLE);
            superviosr_email_et.setBackgroundResource(R.drawable.border_red_line_view);
            superviosremail_error_tv.setText(getResources().getString(R.string.email_required));

            superviosr_mobile_number_error_tv.setVisibility(View.VISIBLE);
            superviosr_mobile_number_lin.setBackgroundResource(R.drawable.border_red_line_view);
            superviosr_mobile_number_error_tv.setText(getResources().getString(R.string.phone_number_required));

            return false;
        }

        return true;

    }





    private boolean validateFieldsEmailVerifyOnly()
    {
        String email = email_et.getText().toString().trim();
        String basicEmail = basic_email_et.getText().toString().trim();

        if(selectedSignupTypeTab.equalsIgnoreCase(BUYER))
        {
            if (email.length() == 0 || email == null|| email.equalsIgnoreCase("")) {
                email_error_tv.setVisibility(View.VISIBLE);
                email_et.setBackgroundResource(R.drawable.border_red_line_view);
                email_error_tv.setText(getResources().getString(R.string.email_required));
                return false;
            }
            else if (!Utils.emailValidator(email))
            {
                email_error_tv.setVisibility(View.VISIBLE);
                email_et.setBackgroundResource(R.drawable.border_red_line_view);
                email_error_tv.setText(getResources().getString(R.string.email_valid_msg));
                return false;
            }
        }
        else if(selectedSignupTypeTab.equalsIgnoreCase(DEALER))
        {
            if (basicEmail.length() == 0 || basicEmail == null|| basicEmail.equalsIgnoreCase("")) {
                basic_email_error_tv.setVisibility(View.VISIBLE);
                basic_email_et.setBackgroundResource(R.drawable.border_red_line_view);
                basic_email_error_tv.setText(getResources().getString(R.string.email_required));
                return false;
            }
            else if (!Utils.emailValidator(basicEmail))
            {
                basic_email_error_tv.setVisibility(View.VISIBLE);
                basic_email_et.setBackgroundResource(R.drawable.border_red_line_view);
                basic_email_error_tv.setText(getResources().getString(R.string.email_valid_msg));
                return false;
            }
        }
        else{}


        return true;
    }

    public void onVerifyEmailAPI(boolean showLoader, String requestOtp, String otp)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();
            if(selectedSignupTypeTab.equalsIgnoreCase(BUYER))
            {
                urlParameter.put("email", ""+ email_et.getText().toString().trim());
            }
            else if(selectedSignupTypeTab.equalsIgnoreCase(DEALER))
            {
                urlParameter.put("email", ""+ basic_email_et.getText().toString().trim());
            }
            else if(selectedSignupTypeTab.equalsIgnoreCase(SUPPLIER))
            {
                urlParameter.put("email", ""+ supplier_company_email_et.getText().toString().trim());
            }
            urlParameter.put("requestOtp", ""+ requestOtp);
            urlParameter.put("otp", ""+ otp);
            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.VERIFY_EMAIL, ApiConstants.VERIFY_EMAIL_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }


    public void getCountryListAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();
            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_COUNTRY_LIST, ApiConstants.GET_COUNTRY_LIST_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    public void getStateListAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();
            if(selectedSignupTypeTab.equalsIgnoreCase(DEALER))
            {
                urlParameter.put("countryId", ""+ companyCountryId);
            }
            else if(selectedSignupTypeTab.equalsIgnoreCase(SUPPLIER))
            {
                urlParameter.put("countryId", ""+ supplierCountryId);
            }
            else
            {
                urlParameter.put("countryId", ""+ countryId);
            }

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_STATE_LIST, ApiConstants.GET_STATE_LIST_ID,showLoader, "POST");

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR);
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    public void getCityListAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();
            if(selectedSignupTypeTab.equalsIgnoreCase(DEALER))
            {
                urlParameter.put("stateId", ""+ companyStateId);
            }
            else if(selectedSignupTypeTab.equalsIgnoreCase(SUPPLIER))
            {
                urlParameter.put("stateId", ""+ supplierStateId);
            }
            else
            {
                urlParameter.put("stateId", ""+ stateId);
            }
            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_CITY_LIST, ApiConstants.GET_CITY_LIST_ID,showLoader, "POST");

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
                case ApiConstants.GET_COUNTRY_LIST_ID:
                    isApiCalling = false;
                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if(countryArrayList.size() > 0)
                        {
                            countryArrayList.clear();
                        } else{}

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            CountryListModel model = new CountryListModel();
                            model.setId(CommonUtility.checkString(objectCodes.optString("id")));
                            model.setTitle(CommonUtility.checkString(objectCodes.optString("name")));
                            model.setCountryCode(CommonUtility.checkString(objectCodes.optString("country_code")));
                            model.setPhoneCode(CommonUtility.checkString(objectCodes.optString("phonecode")));
                            model.setImage(CommonUtility.checkString(objectCodes.optString("flag")));
                            countryArrayList.add(model);

                        }

                        showCountryCodeList();
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

                case ApiConstants.GET_STATE_LIST_ID:
                    isApiCalling = false;
                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if(stateArrayList.size() > 0)
                        {
                            stateArrayList.clear();
                        } else{}

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            CountryListModel model = new CountryListModel();
                            model.setId(CommonUtility.checkString(objectCodes.optString("id")));
                            model.setTitle(CommonUtility.checkString(objectCodes.optString("name")));
                            stateArrayList.add(model);
                        }

                        showStateList();
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

                case ApiConstants.GET_CITY_LIST_ID:
                    isApiCalling = false;
                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if(cityArrayList.size() > 0)
                        {
                            cityArrayList.clear();
                        } else{}

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            CountryListModel model = new CountryListModel();
                            model.setId(CommonUtility.checkString(objectCodes.optString("id")));
                            model.setTitle(CommonUtility.checkString(objectCodes.optString("name")));
                            cityArrayList.add(model);
                        }

                        showCityList();
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

                case ApiConstants.REGISTER_BUYER_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();

                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        String userId = CommonUtility.checkString(jObjDetails.optString("userId"));
                        String userRole = CommonUtility.checkString(jObjDetails.optString("userRole"));

                        CommonUtility.setGlobalString(context, "user_id", userId);
                        CommonUtility.setGlobalString(context, "user_role", userRole);

                        Intent intent = new Intent(context, LoginScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(0,0);

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

                case ApiConstants.REGISTER_DEALER_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();

                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        String userId = CommonUtility.checkString(jObjDetails.optString("userId"));
                        String userRole = CommonUtility.checkString(jObjDetails.optString("userRole"));

                        Log.e("------Dealer--------- : ", "userId : " +  userId);
                        Log.e("------Dealer--------- : ", "userRole : " + userRole);

                        CommonUtility.setGlobalString(context, "user_id", userId);
                        CommonUtility.setGlobalString(context, "user_role", userRole);

                        Intent intent = new Intent(context, LoginScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(0,0);

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

                case ApiConstants.REGISTER_SUPPLIER_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();

                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        String userId = CommonUtility.checkString(jObjDetails.optString("userId"));
                        String userRole = CommonUtility.checkString(jObjDetails.optString("userRole"));

                        Log.e("------Dealer--------- : ", "userId : " +  userId);

                        CommonUtility.setGlobalString(context, "user_id", userId);
                        CommonUtility.setGlobalString(context, "user_role", userRole);

                        Intent intent = new Intent(context, LoginScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(0,0);

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

                        if(documentType.equalsIgnoreCase(PANNo))
                        {
                            if(whereToVerifyDoc.equalsIgnoreCase("authorisedPersonPan"))
                            {
                                isAuthPersonPanNo = true;
                                authPersonVerifiedPanNo = auth_pan_no_et.getText().toString().trim();

                                //Authorise Person Pan No Verify
                                auth_pan_no_verify_img.setVisibility(View.VISIBLE);
                                auth_pan_no_verify_lin.setVisibility(View.GONE);
                            }
                            else if(whereToVerifyDoc.equalsIgnoreCase("supplierPanNo"))
                            {
                                isSupplierPanNoVerified = true;
                                supplierPanNoVerified = supplier_company_pan_no_et.getText().toString().trim();
                                supplier_company_pan_no_verify_img.setVisibility(View.VISIBLE);
                                supplier_company_pan_no_verify_lin.setVisibility(View.GONE);
                            }
                            else{
                                // Company Pan No Verify and Condition
                                pan_verify_img.setVisibility(View.VISIBLE);
                                company_pan_no_verify_lin.setVisibility(View.GONE);

                                isCompanyPanNoVerified = true;
                                companyPanNoVerified = company_pan_no_et.getText().toString().trim();
                                company_pan_no_error_tv.setVisibility(View.GONE);
                                company_pan_back_rel.setBackgroundResource(R.drawable.border_purple_line_view);

                                if(selectedSignupTypeTab.equalsIgnoreCase(SUPPLIER))
                                {
                                    if(!pan_company_name.equalsIgnoreCase(""))
                                    {
                                        supplier_company_name_et.setText(pan_company_name);
                                    }else{}

                                    if(!pincode.equalsIgnoreCase(""))
                                    {
                                        supplier_pincode_et.setText(pan_company_name);
                                    }else{}

                                    if(!address.equalsIgnoreCase(""))
                                    {
                                        supplier_address_line1_et.setText(pan_company_name);
                                    }else{}
                                }
                                else{
                                    if(!pan_company_name.equalsIgnoreCase(""))
                                    {
                                        company_name_et.setText(pan_company_name);
                                    }else{}

                                    if(!pincode.equalsIgnoreCase(""))
                                    {
                                        company_pincode_et.setText(pan_company_name);
                                    }else{}

                                    if(!address.equalsIgnoreCase(""))
                                    {
                                        company_address_line1_et.setText(pan_company_name);
                                    }else{}
                                }
                            }
                        }
                        else if(documentType.equalsIgnoreCase(GSTNo))
                        {
                            if(whereToVerifyDoc.equalsIgnoreCase("supplierCompnayGSTNo"))
                            {
                                isSupplierGSTNoVerified = true;
                                supplierGSTNoVerified = supplier_company_gst_no_et.getText().toString().trim();
                                supplier_company_gst_no_verify_img.setVisibility(View.VISIBLE);
                                supplier_company_gst_no_verify_lin.setVisibility(View.GONE);

                                supplier_company_type_tv.setText(company_type);
                                supplier_business_nature_tv.setText(nature_of_business);
                                supplier_pincode_et.setText(pincode);
                                supplier_company_name_et.setText(company_name);
                                supplier_address_line1_et.setText(address);

                                whoSelectCompanyType = "supplier";
                                whoSelectBusinessNatureType = "supplier";

                                removeCompanySelectError();
                                removeBusinessSelectError();
                            }
                            else
                            {
                                isCompanyGSTNoVerified = true;
                                companyGstNoVerified = company_gst_no_et.getText().toString().trim();
                                company_gst_no_verify_img.setVisibility(View.VISIBLE);
                                company_gst_no_verify_lin.setVisibility(View.GONE);

                                company_type_tv.setText(company_type);
                                business_nature_tv.setText(nature_of_business);
                                company_pincode_et.setText(pincode);
                                company_name_et.setText(company_name);
                                company_address_line1_et.setText(address);

                                removeCompanySelectError();
                                removeBusinessSelectError();
                            }

                        }
                        else if(documentType.equalsIgnoreCase(AADHAARNo))
                        {
                            isAuthPersonAadharNo = true;
                            authPersonVerifiedAadharNo = auth_aadhar_no_et.getText().toString().trim();

                            auth_aadhar_no_verify_img.setVisibility(View.VISIBLE);
                            auth_aadhar_no_verify_lin.setVisibility(View.GONE);
                        }
                        else if(documentType.equalsIgnoreCase(DRIVING_LICENSE))
                        {
                            isDrivingLicenseNo = true;
                            drivingLicenseVerifiedNo = driving_lic_no_et.getText().toString().trim();
                            driving_lic_no_verify_img.setVisibility(View.VISIBLE);
                            driving_lic_no_verify_lin.setVisibility(View.GONE);
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

                case ApiConstants.VERIFY_EMAIL_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        if(selectedSignupTypeTab.equalsIgnoreCase(BUYER))
                        {
                            resendOTP = "";
                            emailVerifyOrNot = "done";
                            isEmailVerified = true;
                            verifiedEmail = email_et.getText().toString().trim();
                            verify_email_lin.setVisibility(View.GONE);
                            buyer_email_verify_img.setVisibility(View.VISIBLE);

                            email_error_tv.setVisibility(View.GONE);
                            email_et.setBackgroundResource(R.drawable.border_purple_line_view);

                        }
                        else if(selectedSignupTypeTab.equalsIgnoreCase(DEALER))
                        {
                            basicResendOTP="";
                            basicEmailVerifyOrNot ="done";
                            isBasicEmailVerified = true;
                            basicVerifiedEmail = basic_email_et.getText().toString().trim();

                            basic_verify_email_lin.setVisibility(View.GONE);
                            basic_email_verify_img.setVisibility(View.VISIBLE);
                            basic_email_error_tv.setVisibility(View.GONE);
                            basic_email_et.setBackgroundResource(R.drawable.border_purple_line_view);
                        }
                        else if(selectedSignupTypeTab.equalsIgnoreCase(SUPPLIER))
                        {
                            supplierResendOTP="";
                            supplierEmailVerifyOrNot ="done";
                            isSupplierEmailVerified = true;
                            supplierVerifiedEmail = supplier_company_email_et.getText().toString().trim();

                            supplier_verify_email_lin.setVisibility(View.GONE);
                            supplier_email_verify_img.setVisibility(View.VISIBLE);
                            supplier__email_error_tv.setVisibility(View.GONE);
                            supplier_company_email_et.setBackgroundResource(R.drawable.border_purple_line_view);
                        }
                        else{}

                        alertDialog.dismiss();
                        //JSONObject jObjDetails = jsonObjectData.optJSONObject("details");
                    }
                    else if (jsonObjectData.optString("status").equalsIgnoreCase("2"))
                    {
                        if(selectedSignupTypeTab.equalsIgnoreCase(BUYER))
                        {
                            if(resendOTP.equalsIgnoreCase(""))
                            {
                                showEmailVerificationPopup(activity, context);
                            }else{}

                        }
                        else if(selectedSignupTypeTab.equalsIgnoreCase(DEALER))
                        {
                            if(basicResendOTP.equalsIgnoreCase(""))
                            {
                                showEmailVerificationPopup(activity, context);
                            }else{}

                        }
                        else if(selectedSignupTypeTab.equalsIgnoreCase(SUPPLIER))
                        {
                            if(supplierResendOTP.equalsIgnoreCase(""))
                            {
                                showEmailVerificationPopup(activity, context);
                            }else{}

                        }
                        else{}

                      //  Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
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
            isApiCalling = false;
            e.printStackTrace();
        }

    }

    @Override
    public void getErrorResponce(String error, int service_ID) {
        isApiCalling = false;
    }

    private void showCountryCodeList()
    {
        dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog.setContentView(R.layout.dialog_show_country_code);

        recycler_view = dialog.findViewById(R.id.recycler_view);
        search_et = dialog.findViewById(R.id.search_et);

        TextView textView2 = dialog.findViewById(R.id.textView2);

        textView2.setText(getResources().getString(R.string.country_list));

        recycler_view.setHasFixedSize(true);
        //recycler_view.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(layoutManager);

        countryListAdapter = new CountryListAdapter(countryArrayList, context, this, showCountryCode);
        recycler_view.setAdapter(countryListAdapter);
        countryListAdapter.notifyDataSetChanged();


        search_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

                try {
                    String text = search_et.getText().toString().toLowerCase(Locale.getDefault());
                    int textlength = search_et.getText().length();

                    countryListAdapter.filter(text, textlength);

                } catch (Exception e) {}
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
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
        /*dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);*/

    }

    private void showStateList()
    {
        dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog.setContentView(R.layout.dialog_show_country_code);

        recycler_view = dialog.findViewById(R.id.recycler_view);
        search_et = dialog.findViewById(R.id.search_et);

        TextView textView2 = dialog.findViewById(R.id.textView2);

        textView2.setText(getResources().getString(R.string.state_list));

        recycler_view.setHasFixedSize(true);
        //recycler_view.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(layoutManager);

        stateListAdapter = new StateListAdapter(stateArrayList, context, this);
        recycler_view.setAdapter(stateListAdapter);
        stateListAdapter.notifyDataSetChanged();


        search_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

                try {
                    String text = search_et.getText().toString().toLowerCase(Locale.getDefault());
                    int textlength = search_et.getText().length();

                    stateListAdapter.filter(text, textlength);

                } catch (Exception e) {}
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
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
        /*dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);*/

    }

    private void showCityList()
    {
        dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog.setContentView(R.layout.dialog_show_country_code);

        recycler_view = dialog.findViewById(R.id.recycler_view);
        search_et = dialog.findViewById(R.id.search_et);

        TextView textView2 = dialog.findViewById(R.id.textView2);

        textView2.setText(getResources().getString(R.string.city_list));

        recycler_view.setHasFixedSize(true);
        //recycler_view.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(layoutManager);

        cityListAdapter = new CityListAdapter(cityArrayList, context, this);
        recycler_view.setAdapter(cityListAdapter);
        cityListAdapter.notifyDataSetChanged();


        search_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

                try {
                    String text = search_et.getText().toString().toLowerCase(Locale.getDefault());
                    int textlength = search_et.getText().length();

                    cityListAdapter.filter(text, textlength);

                } catch (Exception e) {}
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
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
        /*dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);*/

    }


    public void itemClick(int position, String action)
    {
        if(action.equalsIgnoreCase("countryName"))
        {
            if(countryCodeFor.equalsIgnoreCase("countryCodeBuyerSection"))
            {
                CountryListModel model = countryArrayList.get(position);
                /*mobile_number_error_tv.setVisibility(View.GONE);
                mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);*/
                country_code.setText(model.getPhoneCode());
                buyerPhCountryCode = model.getPhoneCode();
                country_code.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down_icon, 0);
                if (!model.getImage().equalsIgnoreCase(""))
                {
                    Picasso.with(context)
                            .load(model.getImage())
                            .into(country_img);
                }
                else {}
            }
            else if(countryCodeFor.equalsIgnoreCase("basicDetailsNumber"))
            {
                CountryListModel model = countryArrayList.get(position);
                basic_mobile_number_error_tv.setVisibility(View.GONE);
                basic_mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
                basic_country_code.setText(model.getPhoneCode());
                dealerBasicPhCountryCode = model.getPhoneCode();
                basic_country_code.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down_icon, 0);
                if (!model.getImage().equalsIgnoreCase(""))
                {
                    Picasso.with(context)
                            .load(model.getImage())
                            .into(basic_country_img);
                } else {}
            }
            else if(countryCodeFor.equalsIgnoreCase("companyDetails"))
            {
                company_country_error_tv.setVisibility(View.GONE);
                company_country_lin.setBackgroundResource(R.drawable.border_purple_line_view);

                CountryListModel model = countryArrayList.get(position);
                companyCountryId = model.getId();
                company_country_tv.setText(model.getTitle());

                if(!dealerCountryName.equalsIgnoreCase(model.getTitle()))
                {
                    company_state_tv.setHint(getResources().getString(R.string.select_state));
                    company_state_tv.setText("");
                    companyStateId = "";
                    company_city_tv.setHint(getResources().getString(R.string.select_city));
                    company_city_tv.setText("");
                    companyCityId = "";
                } else{}

                dealerCountryName = model.getTitle();

                // Check Condition If Country India Show Authorised Person Card Otherwise Not show
                // For India Gst No or PanCard Field Visible, Business No Licence Field Hide
                if(model.getTitle().equalsIgnoreCase("India"))
                {
                    auth_person_card.setVisibility(View.VISIBLE);
                    gst_pan_no_lin.setVisibility(View.VISIBLE);
                    business_licence_lin.setVisibility(View.GONE);
                }
                else{
                    auth_person_card.setVisibility(View.GONE);
                    gst_pan_no_lin.setVisibility(View.GONE);
                    business_licence_lin.setVisibility(View.VISIBLE);
                }
            }
            else if(countryCodeFor.equalsIgnoreCase("countryCodeCompanyDetails"))
            {
                CountryListModel model = countryArrayList.get(position);
                company_country_code.setText(model.getPhoneCode());
                dealerCompanyPhCountryCode = model.getPhoneCode();
                company_country_code.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down_icon, 0);
                if (!model.getImage().equalsIgnoreCase(""))
                {
                    Picasso.with(context)
                            .load(model.getImage())
                            .into(company_country_img);
                }
                else {}
            }
            else if(countryCodeFor.equalsIgnoreCase("supplierCompanyDetails"))
            {
                CountryListModel model = countryArrayList.get(position);
                /*supplier_mobile_number_error_tv.setVisibility(View.GONE);
                supplier_mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);*/
                supplier_country_code.setText(model.getPhoneCode());
                supplierCompanyPhCountryCode = model.getPhoneCode();
                supplier_country_code.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down_icon, 0);
                if (!model.getImage().equalsIgnoreCase(""))
                {
                    Picasso.with(context)
                            .load(model.getImage())
                            .into(supplier_country_img);
                }
                else {}
            }
            else if(countryCodeFor.equalsIgnoreCase("supplierCountryCode"))
            {
                CountryListModel model = countryArrayList.get(position);
                superviosr_mobile_number_error_tv.setVisibility(View.GONE);
                superviosr_mobile_number_lin.setBackgroundResource(R.drawable.border_purple_line_view);
                superviosr_country_code.setText(model.getPhoneCode());
                superviosrPersonPhCountryCode = model.getPhoneCode();
                superviosr_country_code.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down_icon, 0);
                if (!model.getImage().equalsIgnoreCase(""))
                {
                    Picasso.with(context)
                            .load(model.getImage())
                            .into(superviosr_country_img);
                }
                else {}
            }
            else if(countryCodeFor.equalsIgnoreCase("SupplierDetails"))
            {
                supplier_country_error_tv.setVisibility(View.GONE);
                supplier_country_lin.setBackgroundResource(R.drawable.border_purple_line_view);

                CountryListModel model = countryArrayList.get(position);
                supplierCountryId = model.getId();
                supplier_country_tv.setText(model.getTitle());

                if(!supplierCountryName.equalsIgnoreCase(model.getTitle()))
                {
                    supplier_state_tv.setHint(getResources().getString(R.string.select_state));
                    supplier_state_tv.setText("");
                    supplierStateId = "";
                    supplier_city_tv.setHint(getResources().getString(R.string.select_city));
                    supplier_city_tv.setText("");
                    supplierCityId = "";
                } else{}

                supplierCountryName = model.getTitle();

                // Check Condition If Country India Show Authorised Person Card Otherwise Not show
                // For India Gst No or PanCard Field Visible, Business No Licence Field Hide
                if(model.getTitle().equalsIgnoreCase("India"))
                {
                    suppier_gst_pan_no_lin.setVisibility(View.VISIBLE);
                    suppier_business_licence_lin.setVisibility(View.GONE);

                    supplier_company_pan_no_error_tv.setText(getResources().getString(R.string.pan_number_required));
                    supplier_company_pan_back_rel.setBackgroundResource(R.drawable.border_red_line_view);
                    supplier_company_pan_doc_error_tv.setVisibility(View.VISIBLE);
                    supplier_company_pan_doc_rel.setBackgroundResource(R.drawable.border_red_line_view);
                }
                else{
                    suppier_gst_pan_no_lin.setVisibility(View.GONE);
                    suppier_business_licence_lin.setVisibility(View.VISIBLE);

                    suppier_business_lic_no_error_tv.setVisibility(View.VISIBLE);
                    suppier_business_lic_no_rel.setBackgroundResource(R.drawable.border_red_line_view);
                    suppier_business_lic_doc_error_tv.setVisibility(View.VISIBLE);
                    suppier_business_lic_doc_rel.setBackgroundResource(R.drawable.border_red_line_view);
                }
            }
            else
            {
                country_error_tv.setVisibility(View.GONE);
                country_lin.setBackgroundResource(R.drawable.border_purple_line_view);

                CountryListModel model = countryArrayList.get(position);
                countryId = model.getId();
                country_tv.setText(model.getTitle());

                if(!buyerCountryName.equalsIgnoreCase(model.getTitle()))
                {
                    state_tv.setHint(getResources().getString(R.string.select_state));
                    state_tv.setText("");
                    stateId = "";
                    city_tv.setHint(getResources().getString(R.string.select_city));
                    city_tv.setText("");
                    cityId = "";
                } else{}

                buyerCountryName = model.getTitle();
            }

            dialog.cancel();
        }
        else if(action.equalsIgnoreCase("stateName"))
        {
            if(stateCodeFor.equalsIgnoreCase("companyDetails"))
            {
                company_state_error_tv.setVisibility(View.GONE);
                company_state_lin.setBackgroundResource(R.drawable.border_purple_line_view);

                CountryListModel model = stateArrayList.get(position);
                companyStateId = model.getId();
                company_state_tv.setText(model.getTitle());

                if(!dealerStateName.equalsIgnoreCase(model.getTitle()))
                {
                    company_city_tv.setHint(getResources().getString(R.string.select_city));
                    company_city_tv.setText("");
                    companyCityId = "";
                } else{}

                dealerStateName = model.getTitle();
            }
            else if(stateCodeFor.equalsIgnoreCase("SupplierDetails"))
            {
                supplier_state_error_tv.setVisibility(View.GONE);
                supplier_state_lin.setBackgroundResource(R.drawable.border_purple_line_view);

                CountryListModel model = stateArrayList.get(position);
                supplierStateId = model.getId();
                supplier_state_tv.setText(model.getTitle());

                if(!supplierStateName.equalsIgnoreCase(model.getTitle()))
                {
                    supplier_city_tv.setHint(getResources().getString(R.string.select_city));
                    supplier_city_tv.setText("");
                    supplierCityId = "";
                } else{}

                supplierStateName = model.getTitle();
            }
            else
            {
                state_error_tv.setVisibility(View.GONE);
                state_lin.setBackgroundResource(R.drawable.border_purple_line_view);

                CountryListModel model = stateArrayList.get(position);
                stateId = model.getId();
                state_tv.setText(model.getTitle());

                if(!buyerStateName.equalsIgnoreCase(model.getTitle()))
                {
                    city_tv.setHint(getResources().getString(R.string.select_city));
                    city_tv.setText("");
                    cityId = "";
                } else{}

                buyerStateName = model.getTitle();
            }
            dialog.cancel();

        }
        else if(action.equalsIgnoreCase("cityName"))
        {
            if(cityCodeFor.equalsIgnoreCase("companyDetails"))
            {
                company_city_error_tv.setVisibility(View.GONE);
                company_city_lin.setBackgroundResource(R.drawable.border_purple_line_view);

                CountryListModel model = cityArrayList.get(position);
                companyCityId = model.getId();
                company_city_tv.setText(model.getTitle());
                dialog.cancel();
            }
            else if(cityCodeFor.equalsIgnoreCase("SupplierDetails"))
            {
                supplier_city_error_tv.setVisibility(View.GONE);
                supplier_city_lin.setBackgroundResource(R.drawable.border_purple_line_view);

                CountryListModel model = cityArrayList.get(position);
                supplierCityId = model.getId();
                supplier_city_tv.setText(model.getTitle());
                dialog.cancel();
            }
            else
            {
                city_error_tv.setVisibility(View.GONE);
                city_lin.setBackgroundResource(R.drawable.border_purple_line_view);

                CountryListModel model = cityArrayList.get(position);
                cityId = model.getId();
                city_tv.setText(model.getTitle());
                buyerCityName = model.getTitle();
                dialog.cancel();
            }
        }
    }

    TextView resend_otp_tv, otp_timer_tv, otp_required_error_tv;
    EditText otp_et;
    LinearLayout otp_lin;

    private CountDownTimer otpTimer;

    void showEmailVerificationPopup(final Activity activity,final Context context)
    {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_email_verification_popup, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();

        final ImageView cross_img = dialogView.findViewById(R.id.cross_img);
        final LinearLayout mobile_lin = dialogView.findViewById(R.id.mobile_lin);
        final LinearLayout email_lin = dialogView.findViewById(R.id.email_lin);
        final LinearLayout verify_lin = dialogView.findViewById(R.id.verify_lin);
        final TextView mobile_tv = dialogView.findViewById(R.id.mobile_tv);
        final TextView email_tv = dialogView.findViewById(R.id.email_tv);
        final TextView otp_send_msg_tv = dialogView.findViewById(R.id.otp_send_msg_tv);
        otp_lin = dialogView.findViewById(R.id.otp_lin);
        otp_et = dialogView.findViewById(R.id.otp_et);
        otp_required_error_tv = dialogView.findViewById(R.id.otp_error_requir_tv);
        otp_timer_tv = dialogView.findViewById(R.id.otp_timer_tv);
        resend_otp_tv = dialogView.findViewById(R.id.resend_otp_tv);

        setOTPTimer();

        cross_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                stopOTPTimer();
                alertDialog.dismiss();
            }
        });

        mobile_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                otp_send_msg_tv.setText(getResources().getString(R.string.opt_send_mobile_msg));

                mobile_lin.setBackgroundResource(R.drawable.background_button_shadow);
                email_lin.setBackgroundResource(R.drawable.background_white_view);
                mobile_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
                email_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));

            }
        });
        email_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                otp_send_msg_tv.setText(getResources().getString(R.string.opt_send_email_msg));

                mobile_lin.setBackgroundResource(R.drawable.background_white_view);
                email_lin.setBackgroundResource(R.drawable.background_button_shadow);
                mobile_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_light));
                email_tv.setTextColor(ContextCompat.getColor(context, R.color.white));
            }
        });

        resend_otp_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                stopOTPTimer();
                setOTPTimer();
                if(selectedSignupTypeTab.equalsIgnoreCase(BUYER))
                {
                    resendOTP = "yes";
                    onVerifyEmailAPI(false, "1", "");
                }
                else if(selectedSignupTypeTab.equalsIgnoreCase(DEALER))
                {
                    basicResendOTP = "yes";
                    onVerifyEmailAPI(false, "1", "");
                }
                else if(selectedSignupTypeTab.equalsIgnoreCase(SUPPLIER))
                {
                    supplierResendOTP = "yes";
                    onVerifyEmailAPI(false, "1", "");
                }

            }
        });

        verify_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(validateFieldsEmailVerification())
                {
                    resendOTP = "";
                    basicResendOTP = "";
                    supplierResendOTP = "";
                    onVerifyEmailAPI(false, "0", otp_et.getText().toString().trim());
                }else{}

            }
        });

        otp_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!otp_et.getText().toString().equalsIgnoreCase("")){
                    otp_required_error_tv.setVisibility(View.GONE);
                    otp_et.setBackgroundResource(R.drawable.border_purple_line_view);
                }else{}

                if(otp_et.getText().toString().equalsIgnoreCase("")){
                    otp_required_error_tv.setVisibility(View.GONE);
                    otp_et.setBackgroundResource(R.drawable.border_line_view);
                }else{}
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }



    void setOTPTimer()
    {
        otpTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                resend_otp_tv.setVisibility(View.GONE);
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                //long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                //otp_timer_tv.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                otp_timer_tv.setText(f.format(min) + ":" + f.format(sec));
            }

            public void onFinish() {
                resend_otp_tv.setVisibility(View.VISIBLE);
                otp_timer_tv.setText("00:00");
            }
        }.start();
    }

    // Method to stop the CountDownTimer
    void stopOTPTimer() {
        if (otpTimer != null) {
            otpTimer.cancel();
            otpTimer = null;
        }
    }

    private boolean validateFieldsEmailVerification()
    {
        String otp = otp_et.getText().toString().trim();

        if (otp_lin.getVisibility() == View.VISIBLE && otp.equalsIgnoreCase("")) {
            otp_required_error_tv.setVisibility(View.VISIBLE);
            otp_et.setBackgroundResource(R.drawable.border_red_line_view);
            return false;
        }

        return true;
    }

    @Override
    public void dialogItemClick(String value, String action) {

        if (action.equalsIgnoreCase("dob")) {
            date_of_birth_tv.setText(value);
            //from_date_required.setVisibility(View.GONE);

            Log.e("------dob_value----- : ", value);

            // this date send to server formated date yyyy-MM-dd.
            send_dob_server = CommonUtility.convertDateFormat(value, ApiConstants.DATE_DOB_FORMAT, "dd/MM/yyyy");
        }

    }

    public class SendDealerSignupDataAsyncTask {
        private String reply = "";
        //private ProgressDialog _dialog;
        private Context context;
        private ExecutorService executorService;
        private Handler handler;

        public SendDealerSignupDataAsyncTask(Context context) {
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
                mu = new MultipartUtility(ApiConstants.DOMAIN_NAME + ApiConstants.REGISTER_DEALER, "UTF-8", context);

                mu.addFormField("firstName", basic_first_name_et.getText().toString().trim());
                mu.addFormField("lastName", basic_last_name_et.getText().toString().trim());
                mu.addFormField("email", basic_email_et.getText().toString().trim());
                mu.addFormField("mobileNo", dealerBasicPhCountryCode + " " + basic_mobile_number_et.getText().toString().trim());
                mu.addFormField("country", companyCountryId);
                mu.addFormField("state", companyStateId);
                mu.addFormField("city", companyStateId);
                mu.addFormField("password", basic_password_et.getText().toString().trim());
                mu.addFormField("confirmPassword", basic_confirm_password_et.getText().toString().trim());
                mu.addFormField("pinCode", company_pincode_et.getText().toString().trim());
                mu.addFormField("address", company_address_line1_et.getText().toString().trim());
                mu.addFormField("address2", company_address_line2_et.getText().toString().trim());
                mu.addFormField("companyName", company_name_et.getText().toString().trim());
                mu.addFormField("companyEmail", company_email_et.getText().toString().trim());
                mu.addFormField("companyContact", dealerCompanyPhCountryCode + " " + company_mobile_number_et.getText().toString().trim());
                mu.addFormField("typeOfCompany", company_type_tv.getText().toString().trim());
                mu.addFormField("natureOfBusiness", business_nature_tv.getText().toString().trim());
                mu.addFormField("aadhaarNo", auth_aadhar_no_et.getText().toString().trim());
                mu.addFormField("PANNo", auth_pan_no_et.getText().toString().trim());
                //mu.addFormField("tradeMembershipNo", "");
                mu.addFormField("IECNo", company_iec_no_et.getText().toString().trim());
                mu.addFormField("dob", send_dob_server);
                //mu.addFormField("inventoryType", "");
                //mu.addFormField("emiratesId", "");
                mu.addFormField("aadhaarNoDocFront", authAadharFrontDocBase64Url);
                mu.addFormField("aadhaarNoDocBack", authAadharBackDocBase64Url);
                mu.addFormField("PANNoDoc", authAadharPanDocBase64Url);

                if(company_country_tv.getText().toString().trim().equalsIgnoreCase("India"))
                {
                    mu.addFormField("companyPANNo", company_pan_no_et.getText().toString().trim());
                    mu.addFormField("companyGSTNo", company_gst_no_et.getText().toString().trim());
                    mu.addFormField("companyPANNoDoc", companyKycPanDocBase64Url);
                    mu.addFormField("companyGSTNoDoc", companyKycGstDocBase64Url);
                }
                else
                {
                    mu.addFormField("tradeMembershipNo", company_business_lic_no_et.getText().toString().trim());
                    mu.addFormField("tradeMembershipNoDoc", businessLicenceNoDocBase64Url);
                }

                //mu.addFormField("tradeMembershipNoDoc", "");
                mu.addFormField("IECDoc", companyIECPanDocBase64Url);
                //mu.addFormField("emiratesIdDoc", "");
                mu.addFormField("passportNo", passport_no_et.getText().toString().trim());
                mu.addFormField("passportFrontDoc", passPortFrontDocBase64Url);
                mu.addFormField("passportBackDoc", passPortBackDocBase64Url);
                mu.addFormField("drivingLicenseNo", driving_lic_no_et.getText().toString().trim());
                mu.addFormField("drivingLicenseDoc", drivingLicenceDocBase64Url);

                mu.addFormField("referralCode", referral_code_et.getText().toString().trim());

                mu.addFormField("referralCode1", "");

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

    public class SendSupplierSignupDataAsyncTask {
        private String reply = "";
        //private ProgressDialog _dialog;
        private Context context;
        private ExecutorService executorService;
        private Handler handler;

        public SendSupplierSignupDataAsyncTask(Context context) {
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
                mu = new MultipartUtility(ApiConstants.DOMAIN_NAME + ApiConstants.REGISTER_SUPPLIER, "UTF-8", context);

                mu.addFormField("email", supplier_company_email_et.getText().toString().trim());
                mu.addFormField("mobileNo", supplierCompanyPhCountryCode + " " + supplier_mobile_number_et.getText().toString().trim());
                mu.addFormField("country", supplierCountryId);
                mu.addFormField("state", supplierStateId);
                mu.addFormField("city", supplierCityId);
                mu.addFormField("password", supplier_password_et.getText().toString().trim());
                mu.addFormField("confirmPassword", supplier_confirm_password_et.getText().toString().trim());
                mu.addFormField("pinCode", supplier_pincode_et.getText().toString().trim());
                mu.addFormField("address", supplier_address_line1_et.getText().toString().trim());
                mu.addFormField("address2", supplier_address_line2_et.getText().toString().trim());

                mu.addFormField("bankName", bank_name_et.getText().toString().trim());
                mu.addFormField("branchName", branch_name_et.getText().toString().trim());
                mu.addFormField("accountNo", bank_account_no_et.getText().toString().trim());
                mu.addFormField("accountType", bank_account_type_et.getText().toString().trim());
                mu.addFormField("IFSCCode", ifsc_code_et.getText().toString().trim());
                mu.addFormField("swiftCode", bank_swift_code_et.getText().toString().trim());
                mu.addFormField("authPersonName", superviosr_name_et.getText().toString().trim());
                mu.addFormField("authPersonContact", superviosrPersonPhCountryCode + " " + superviosr_mobile_number_et.getText().toString().trim());
                mu.addFormField("authPersonEmail", superviosr_email_et.getText().toString().trim());

                mu.addFormField("companyName", supplier_company_name_et.getText().toString().trim());
                mu.addFormField("companyEmail", supplier_company_email_et.getText().toString().trim());
                mu.addFormField("companyContact", supplierCompanyPhCountryCode + " " + supplier_mobile_number_et.getText().toString().trim());
                mu.addFormField("typeOfCompany", supplier_company_type_tv.getText().toString().trim());
                mu.addFormField("natureOfBusiness", supplier_business_nature_tv.getText().toString().trim());
                mu.addFormField("aadhaarNo", "");
                mu.addFormField("PANNo", "");
                mu.addFormField("IECNo", supplier_company_iec_no_et.getText().toString().trim());
                mu.addFormField("dob", "");
                mu.addFormField("inventoryType", inventory_tv.getText().toString().trim());
                //mu.addFormField("emiratesId", "");
                mu.addFormField("aadhaarNoDocFront", "");
                mu.addFormField("aadhaarNoDocBack", "");
                mu.addFormField("PANNoDoc", "");

                if(company_country_tv.getText().toString().trim().equalsIgnoreCase("India"))
                {
                    mu.addFormField("companyPANNo", supplier_company_pan_no_et.getText().toString().trim());
                    mu.addFormField("companyGSTNo", supplier_company_gst_no_et.getText().toString().trim());
                    mu.addFormField("companyPANNoDoc", supplierCompanyPanDocBase64Url);
                    mu.addFormField("companyGSTNoDoc", supplierCompanyGSTDocBase64Url);
                }
                else
                {
                    mu.addFormField("tradeMembershipNo", suppier_business_lic_no_et.getText().toString().trim());
                    mu.addFormField("tradeMembershipNoDoc", supplierCompanyBusinessDocBase64Url);
                }

                mu.addFormField("IECDoc", supplierCompanyIECDocBase64Url);
                //mu.addFormField("emiratesIdDoc", "");

                mu.addFormField("referralCode", referral_code_et.getText().toString().trim());

                mu.addFormField("referralCode1", "");

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