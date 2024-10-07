package com.diamondxe.Activity.PlaceOrder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.diamondxe.Activity.AddBillingAddressActivity;
import com.diamondxe.Activity.AddShippingAddressActivity;
import com.diamondxe.Adapter.Dealer.KYCVerificationAdapter;
import com.diamondxe.Adapter.OrderPlaceItemListAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.AddToCartListModel;
import com.diamondxe.Beans.KYCVerificationModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

public class PlaceOrderKYCVerificationActivity extends SuperActivity implements RecyclerInterface {

    private ImageView back_img, shipping_img, kyc_img, payment_img, drop_arrow_img, other_tax_info_img, diamond_tax_info_img,document_drop_down_img;
    private CardView  shipping_card_view, kyc_card_view, payment_card_view;
    private TextView  continue_tv, kyc_verified_lbl,kyc_verified_lbl1, total_amount_tv, kyc_document_resubmit,
            final_amount_tv1,shipping_and_handling_tv, platform_fees_tv, total_charges_tv, other_taxes_tv, diamond_taxes_tv, total_taxes_tv,
            sub_total_tv, bank_charges_tv, final_amount_tv, others_txt_gst_perc_tv, diamond_txt_gst_perc_tv;
    private RelativeLayout shipping_rel, kyc_rel, payment_rel, viewpager_layout, rel_other_tax, rel_diamond_tax;
    private LinearLayout address_main_lin, kyc_verification_main_lin, view_order_summary_details_lin, kyc_document_layout_lin, open_document_lin;
    private CardView order_summary_view_card;
    private ScrollView scrollView;
    private Activity activity;
    private Context context;
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    ViewPager viewPager;
    TabLayout tabLayout;
    private RecyclerView shipping_address_recycler_view, billing_address_recycler_view, doc_recycler_view;
    ArrayList<KYCVerificationModel> modelArrayList;
    KYCVerificationAdapter kycVerificationAdapter;
    public ArrayList<AddToCartListModel> orderItemArrayList;
    OrderPlaceItemListAdapter orderPlaceItemListAdapter;
    private boolean isArrowDown = false;
    private boolean isArrowDownDocument = false;
    int lastPosition = 0;
    String wheretoRemove="",userRole="",document_status="";
    String selectedCurrencyValue ="",selectedCurrencyCode = "",selectedCurrencyDesc="",selectedCurrencyImage="";
    String aadhaarAttachmentType = "", aadhaarAttachmentDesc = "", aadhaarVerifiedInd  = "", aadhaarNo = "", aadhaarBackAttachmentType = "", aadhaarBackAttachmentDesc = "", aadhaarBackVerifiedInd = "", aadhaarBackNo = "",
            panCardAttachmentType = "", panCardAttachmentDesc = "", panCardVerifiedInd = "", panCardNo = "", companyPanCardAttachmentType = "", companyPanCardAttachmentDesc = "", companyPanCardVerifiedInd = "", companyPanCardNo = "", companyGstCertificateAttachmentType = "",
            companyGstCertificateAttachmentDesc = "", companyGstCertificateVerifiedInd = "", companyGstCertificateNo = "", iecCardAttachmentType = "",
            iecCardAttachmentDesc = "", iecCardVerifiedInd = "", iecCardNo = "", passportFrontAttachmentType = "", passportFrontAttachmentDesc = "", passportFrontVerifiedInd = "", passportFrontNo = "",
            passportBackAttachmentType = "", passportBackAttachmentDesc = "", passportBackVerifiedInd = "", passportBackNo = "";
    String aadhaarFrontAttachmentId ="", aadhaarBackAttachmentId="", panCardAttachmentId="", companyPanCardAttachmentId="", companyGstCertificateAttachmentId="", iecAttachmentId="", passportFrontAttachmentId="", passportBackAttachmentId="";
    String drivingLicenseAttachmentId = "", drivingLicenseAttachmentType = "", drivingLicenseAttachmentDesc = "", drivingLicenseVerifiedInd = "", drivingLicenseNo  = "";
    String businessLicenseAttachmentId = "", businessLicenseAttachmentType ="", businessLicenseAttachmentDesc ="", businessLicenseVerifiedInd ="", businessLicenseNo ="";
    String KYC_VERIFICATION = "kycVerification";
    Handler handler1 = new Handler(Looper.getMainLooper());
    int newWith;
    int width;
    String isCoupanApplied = "", orderCouponCode = "", orderCouponValue = "", orderCouponDiscount = "", orderSubTotal = "", orderCgst = "", orderCgstPerc = "",
            orderSgst = "", orderSgstPerc = "", orderIgst = "", orderIgstPerc = "", orderDiscountPerc = "", orderTax = "", orderSubTotalWithTax = "", orderShippingCharge = "", orderPlatformFee = "",
            orderTotalCharge = "", orderTotalChargeTax = "", orderTotalChargeWithTax = "", orderTotalTaxes = "", orderTotalAmount = "", orderTaxPerOnCharges = "", orderFinalAmount = "", orderBankCharge = "", orderBankChargePerc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_kycverification);

        context = activity = this;

        modelArrayList = new ArrayList<>();
        orderItemArrayList = new ArrayList<>();

        userRole = CommonUtility.getGlobalString(activity, "login_user_role");

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        scrollView = findViewById(R.id.scrollView);

        total_amount_tv = findViewById(R.id.total_amount_tv);
        final_amount_tv1 = findViewById(R.id.final_amount_tv1);

        shipping_and_handling_tv = findViewById(R.id.shipping_and_handling_tv);
        platform_fees_tv = findViewById(R.id.platform_fees_tv);
        total_charges_tv = findViewById(R.id.total_charges_tv);
        other_taxes_tv = findViewById(R.id.other_taxes_tv);
        diamond_taxes_tv = findViewById(R.id.diamond_taxes_tv);
        total_taxes_tv = findViewById(R.id.total_taxes_tv);
        sub_total_tv = findViewById(R.id.sub_total_tv);
        bank_charges_tv = findViewById(R.id.bank_charges_tv);
        final_amount_tv = findViewById(R.id.final_amount_tv);
        others_txt_gst_perc_tv = findViewById(R.id.others_txt_gst_perc_tv);
        diamond_txt_gst_perc_tv = findViewById(R.id.diamond_txt_gst_perc_tv);

        continue_tv = findViewById(R.id.continue_tv);
        continue_tv.setOnClickListener(this);

        shipping_img = findViewById(R.id.shipping_img);
        kyc_img = findViewById(R.id.kyc_img);
        payment_img = findViewById(R.id.payment_img);

        shipping_rel = findViewById(R.id.shipping_rel);
        kyc_rel = findViewById(R.id.kyc_rel);
        payment_rel = findViewById(R.id.payment_rel);

        shipping_card_view = findViewById(R.id.shipping_card_view);
        shipping_card_view.setOnClickListener(this);

        kyc_card_view = findViewById(R.id.kyc_card_view);
        kyc_card_view.setOnClickListener(this);

        payment_card_view = findViewById(R.id.payment_card_view);
        payment_card_view.setOnClickListener(this);

        kyc_verification_main_lin = findViewById(R.id.kyc_verification_main_lin);

        document_drop_down_img = findViewById(R.id.document_drop_down_img);

        open_document_lin = findViewById(R.id.open_document_lin);
        open_document_lin.setOnClickListener(this);

        kyc_document_layout_lin = findViewById(R.id.kyc_document_layout_lin);


        /*other_tax_info_img = findViewById(R.id.other_tax_info_img);
        other_tax_info_img.setOnClickListener(this);

        diamond_tax_info_img = findViewById(R.id.diamond_tax_info_img);
        diamond_tax_info_img.setOnClickListener(this);*/

        rel_other_tax = findViewById(R.id.rel_other_tax);
        rel_other_tax.setOnClickListener(this);

        rel_diamond_tax = findViewById(R.id.rel_diamond_tax);
        rel_diamond_tax.setOnClickListener(this);

        drop_arrow_img = findViewById(R.id.drop_arrow_img);
        view_order_summary_details_lin = findViewById(R.id.view_order_summary_details_lin);
        order_summary_view_card = findViewById(R.id.order_summary_view_card);
        order_summary_view_card.setOnClickListener(this);

        viewPager = findViewById (R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        tabLayout = findViewById(R.id.tab_layout);
        viewpager_layout = findViewById(R.id.viewpager_layout);

        newWith = (int) (width/1.2);

        //-------------------------------------------KYC Verification ID Start-----------------------------------------------

        kyc_verified_lbl = findViewById(R.id.kyc_verified_lbl);
        kyc_verified_lbl1 = findViewById(R.id.kyc_verified_lbl1);

        kyc_document_resubmit = findViewById(R.id.kyc_document_resubmit);
        kyc_document_resubmit.setOnClickListener(this);

        doc_recycler_view = findViewById(R.id.doc_recycler_view);
        LinearLayoutManager layoutManagerKYC = new LinearLayoutManager(activity);
        doc_recycler_view.setLayoutManager(layoutManagerKYC);
        doc_recycler_view.setNestedScrollingEnabled(false);

        getKYCDetailsAPI(false);

        //-------------------------------------------KYC Verification ID End------------------------------------------------

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
        else if(id == R.id.add_shipping_address_tv)
        {
            Utils.hideKeyboard(activity);
            Constant.editShippingAddress = "";
            Constant.addressID = "";
            intent = new Intent(activity, AddShippingAddressActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else if(id == R.id.add_billing_address_tv)
        {
            Utils.hideKeyboard(activity);
            Constant.editBillingAddress = "yes";
            Constant.addressID = "";
            intent = new Intent(activity, AddBillingAddressActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else if(id == R.id.order_summary_view_card)
        {
            Utils.hideKeyboard(activity);
            if (isArrowDown) {
                drop_arrow_img.setImageResource(R.drawable.down);
                view_order_summary_details_lin.setVisibility(View.GONE);
            } else {
                drop_arrow_img.setImageResource(R.drawable.up);
                view_order_summary_details_lin.setVisibility(View.VISIBLE);
            }
            isArrowDown = !isArrowDown;
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
        else if(id == R.id.continue_tv)
        {
            Utils.hideKeyboard(activity);

            intent = new Intent(activity, PaymentProcessedScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        }
        else if(id == R.id.kyc_document_resubmit)
        {
            Utils.hideKeyboard(activity);

            Constant.aadhaarVerifiedInd = aadhaarVerifiedInd;
            Constant.aadhaarBackVerifiedInd = aadhaarBackVerifiedInd;
            Constant.panCardVerifiedInd = panCardVerifiedInd;
            Constant.companyPanCardVerifiedInd = companyPanCardVerifiedInd;
            Constant.companyGstCertificateVerifiedInd = companyGstCertificateVerifiedInd;
            Constant.iecCardVerifiedInd = iecCardVerifiedInd;
            Constant.passportFrontVerifiedInd = passportFrontVerifiedInd;
            Constant.passportBackVerifiedInd = passportBackVerifiedInd;
            Constant.drivingLicenceVerifiedInd = drivingLicenseVerifiedInd;

            Constant.aadhaarFrontAttachmentId = aadhaarFrontAttachmentId;
            Constant.aadhaarBackAttachmentId = aadhaarBackAttachmentId;
            Constant.panCardAttachmentId = panCardAttachmentId;
            Constant.companyPanCardAttachmentId = companyPanCardAttachmentId;
            Constant.companyGstCertificateAttachmentId = companyGstCertificateAttachmentId;
            Constant.iecAttachmentId = iecAttachmentId;
            Constant.passportFrontAttachmentId = passportFrontAttachmentId;
            Constant.passportBackAttachmentId = passportBackAttachmentId;
            Constant.drivingLicenceAttachmentId = drivingLicenseAttachmentId;

            intent = new Intent(activity, PlcaeOrderKYCVerificationDocUploadActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        }
        else if(id == R.id.shipping_card_view)
        {
            Utils.hideKeyboard(activity);
            intent = new Intent(activity, PlaceOrderScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        }
        else if(id == R.id.kyc_card_view)
        {
            Utils.hideKeyboard(activity);
        }
        else if(id == R.id.payment_card_view)
        {
            intent = new Intent(activity, PaymentProcessedScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        }
        else if(id == R.id.open_document_lin)
        {
            Utils.hideKeyboard(activity);
            if (isArrowDownDocument) {
                document_drop_down_img.setImageResource(R.drawable.down);
                kyc_document_layout_lin.setVisibility(View.GONE);
            } else {
                document_drop_down_img.setImageResource(R.drawable.up);
                kyc_document_layout_lin.setVisibility(View.VISIBLE);
            }
            isArrowDownDocument = !isArrowDownDocument;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrencyData();
        userRole = CommonUtility.getGlobalString(activity, "login_user_role");
        getKYCDetailsAPI(true);
        getCheckOutDetailsAPI(true);
    }
    // Get Currency Value Code and Image
    void getCurrencyData()
    {
        selectedCurrencyValue = CommonUtility.getGlobalString(context, "selected_currency_value");
        selectedCurrencyCode = CommonUtility.getGlobalString(context, "selected_currency_code");
        selectedCurrencyDesc = CommonUtility.getGlobalString(context, "selected_currency_desc");
        selectedCurrencyImage = CommonUtility.getGlobalString(context, "selected_currency_image");
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

    public void getCheckOutDetailsAPI(boolean showLoader)
    {
        if (Utils.isNetworkAvailable(context))
        {
            urlParameter = new HashMap<String, String>();

            urlParameter.put("couponCode", "");
            urlParameter.put("walletPoints", "");
            urlParameter.put("paymentMode", "");
            urlParameter.put("deliveryPincode", "" + Constant.deliveryPincode);
            urlParameter.put("collectFromHub", "" + Constant.collectFromHub);
            urlParameter.put("orderType", Constant.orderType);
            urlParameter.put("certificateNo", Constant.certificateNumber);

            vollyApiActivity = null;
            vollyApiActivity = new VollyApiActivity(context,this, urlParameter, ApiConstants.GET_CHECKOUT_DETAILS, ApiConstants.GET_CHECKOUT_DETAILS_ID,showLoader, "POST");

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
            String document_status = jsonObjectData.optString("document_status");

            switch (service_ID) {
                case ApiConstants.GET_KYC_DETAILS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        if(document_status.equalsIgnoreCase("1"))
                        {
                            // re_submit_tv.setVisibility(View.VISIBLE);
                            // bottom_bar_rel.setVisibility(View.VISIBLE);
                            kyc_document_resubmit.setVisibility(View.VISIBLE); // Re-submit Button Visible.
                            kyc_verified_lbl1.setVisibility(View.VISIBLE);
                            kyc_verified_lbl1.setText(getResources().getString(R.string.kyc_doc_submit));
                           // kyc_verified_lbl1.setText(getResources().getString(R.string.technical_reviewing_soon));

                        }
                        else if(document_status.equalsIgnoreCase("1"))
                        {
                            kyc_document_resubmit.setVisibility(View.VISIBLE); // Re-submit Button Visible.
                            kyc_verified_lbl1.setVisibility(View.VISIBLE);
                            kyc_verified_lbl1.setText(getResources().getString(R.string.kyc_doc_submit));
                            // bottom_bar_rel.setVisibility(View.VISIBLE);
                            // re_submit_tv.setVisibility(View.VISIBLE);
                           // kyc_verified_lbl1.setText(getResources().getString(R.string.technical_reviewing_soon));

                        }
                        else if(document_status.equalsIgnoreCase("2"))
                        {
                            kyc_document_resubmit.setVisibility(View.GONE); // Document Verified Re-submit Button Hide.
                            kyc_verified_lbl1.setVisibility(View.VISIBLE);
                            kyc_verified_lbl1.setText(getResources().getString(R.string.kyc_doc_verified));
                            //bottom_bar_rel.setVisibility(View.GONE);
                            //kyc_verified_lbl1.setText(getResources().getString(R.string.doc_verified_thanks));

                        }
                        else if(document_status.equalsIgnoreCase("3"))
                        {
                            kyc_document_resubmit.setVisibility(View.VISIBLE); // Re-submit Button Visible.
                            kyc_verified_lbl1.setVisibility(View.VISIBLE);
                            kyc_verified_lbl1.setText(getResources().getString(R.string.kyc_doc_unverified));
                            //bottom_bar_rel.setVisibility(View.VISIBLE);
                            // re_submit_tv.setVisibility(View.VISIBLE);
                            //kyc_verified_lbl1.setText(getResources().getString(R.string.doc_resubmit_again));

                        }
                        else{
                            kyc_verified_lbl1.setVisibility(View.GONE);
                        }

                        JSONArray details = jObjDetails.getJSONArray("all_document");

                        if(modelArrayList.size() > 0)
                        {
                            modelArrayList.clear();
                        } else{}

                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            KYCVerificationModel model = new KYCVerificationModel();

                            model.setAttachmentId(CommonUtility.checkString(objectCodes.optString("AttachmentId")));
                            model.setAttachmentType(CommonUtility.checkString(objectCodes.optString("AttachmentType")));
                            model.setFileName(CommonUtility.checkString(objectCodes.optString("FileName")));
                            model.setFileUrl(CommonUtility.checkString(objectCodes.optString("FileUrl")));
                            model.setAttachmentDesc(CommonUtility.checkString(objectCodes.optString("AttachmentDesc")));
                            model.setVerifiedInd(CommonUtility.checkString(objectCodes.optString("VerifiedInd")));

                            if(!objectCodes.optString("AttachmentDate").equalsIgnoreCase(""))
                            {
                                String data = CommonUtility.convertDateFormat(objectCodes.optString("AttachmentDate"), ApiConstants.DATE_FORMAT, "MMM dd, yyyy");
                                model.setAttachmentDate(data);
                            } else{}


                            modelArrayList.add(model);
                        }

                        JSONObject aadharCardFront = jObjDetails.optJSONObject("aadhar_card_front");

                        /*String attachmentId = CommonUtility.checkString(aadharCardFront.optString("AttachmentId"));
                        String fileName = CommonUtility.checkString(aadharCardFront.optString("FileName"));
                        String attachmentDate = CommonUtility.checkString(aadharCardFront.optString("AttachmentDate"));*/

                        if (aadharCardFront != null && aadharCardFront.length() > 0)
                        {
                            aadhaarFrontAttachmentId = CommonUtility.checkString(aadharCardFront.optString("AttachmentId"));
                            aadhaarAttachmentType = CommonUtility.checkString(aadharCardFront.optString("AttachmentType"));
                            aadhaarAttachmentDesc = CommonUtility.checkString(aadharCardFront.optString("AttachmentDesc"));
                            aadhaarVerifiedInd = CommonUtility.checkString(aadharCardFront.optString("VerifiedInd"));
                            aadhaarNo = CommonUtility.checkString(aadharCardFront.optString("AadhaarNo"));

                        } else{}

                        JSONObject aadhaarCardBack = jObjDetails.optJSONObject("aadhar_card_back");
                        if (aadhaarCardBack != null && aadhaarCardBack.length() > 0)
                        {
                            aadhaarBackAttachmentId = CommonUtility.checkString(aadhaarCardBack.optString("AttachmentId"));
                            aadhaarBackAttachmentType = CommonUtility.checkString(aadhaarCardBack.optString("AttachmentType"));
                            aadhaarBackAttachmentDesc = CommonUtility.checkString(aadhaarCardBack.optString("AttachmentDesc"));
                            aadhaarBackVerifiedInd = CommonUtility.checkString(aadhaarCardBack.optString("VerifiedInd"));
                            aadhaarBackNo = CommonUtility.checkString(aadhaarCardBack.optString("AadhaarNo"));

                        } else{}


                        JSONObject panCard = jObjDetails.optJSONObject("pan_card");
                        if (panCard != null && panCard.length() > 0)
                        {
                            panCardAttachmentId = CommonUtility.checkString(panCard.optString("AttachmentId"));
                            panCardAttachmentType = CommonUtility.checkString(panCard.optString("AttachmentType"));
                            panCardAttachmentDesc = CommonUtility.checkString(panCard.optString("AttachmentDesc"));
                            panCardVerifiedInd = CommonUtility.checkString(panCard.optString("VerifiedInd"));
                            panCardNo = CommonUtility.checkString(panCard.optString("PANNo"));

                        } else{}


                        JSONObject companyPanCard = jObjDetails.optJSONObject("company_pan_card");
                        if (companyPanCard != null && companyPanCard.length() > 0)
                        {
                            companyPanCardAttachmentId = CommonUtility.checkString(companyPanCard.optString("AttachmentId"));
                            companyPanCardAttachmentType = CommonUtility.checkString(companyPanCard.optString("AttachmentType"));
                            companyPanCardAttachmentDesc = CommonUtility.checkString(companyPanCard.optString("AttachmentDesc"));
                            companyPanCardVerifiedInd = CommonUtility.checkString(companyPanCard.optString("VerifiedInd"));
                            companyPanCardNo = CommonUtility.checkString(companyPanCard.optString("CompanyPANNo"));

                        } else{}


                        JSONObject companyGstCertificate = jObjDetails.optJSONObject("company_gst_certificate");
                        if (companyGstCertificate != null && companyGstCertificate.length() > 0)
                        {
                            companyGstCertificateAttachmentId = CommonUtility.checkString(companyGstCertificate.optString("AttachmentId"));
                            companyGstCertificateAttachmentType = CommonUtility.checkString(companyGstCertificate.optString("AttachmentType"));
                            companyGstCertificateAttachmentDesc = CommonUtility.checkString(companyGstCertificate.optString("AttachmentDesc"));
                            companyGstCertificateVerifiedInd = CommonUtility.checkString(companyGstCertificate.optString("VerifiedInd"));
                            companyGstCertificateNo = CommonUtility.checkString(companyGstCertificate.optString("CompanyGSTNo"));

                        } else{}

                        JSONObject iecCard = jObjDetails.optJSONObject("iec_card");
                        if (iecCard != null && iecCard.length() > 0)
                        {
                            iecAttachmentId = CommonUtility.checkString(iecCard.optString("AttachmentId"));
                            iecCardAttachmentType = CommonUtility.checkString(iecCard.optString("AttachmentType"));
                            iecCardAttachmentDesc = CommonUtility.checkString(iecCard.optString("AttachmentDesc"));
                            iecCardVerifiedInd = CommonUtility.checkString(iecCard.optString("VerifiedInd"));
                            iecCardNo = CommonUtility.checkString(iecCard.optString("IEC"));
                        } else{}


                        JSONObject passportFront = jObjDetails.optJSONObject("passport_front");
                        if (passportFront != null && passportFront.length() > 0)
                        {
                            passportFrontAttachmentId = CommonUtility.checkString(passportFront.optString("AttachmentId"));
                            passportFrontAttachmentType = CommonUtility.checkString(passportFront.optString("AttachmentType"));
                            passportFrontAttachmentDesc = CommonUtility.checkString(passportFront.optString("AttachmentDesc"));
                            passportFrontVerifiedInd = CommonUtility.checkString(passportFront.optString("VerifiedInd"));
                            passportFrontNo = CommonUtility.checkString(passportFront.optString("PassportNo"));
                        } else{}


                        JSONObject passportBack = jObjDetails.optJSONObject("passport_back");
                        if (passportBack != null && passportBack.length() > 0)
                        {
                            passportBackAttachmentId = CommonUtility.checkString(passportBack.optString("AttachmentId"));
                            passportBackAttachmentType = CommonUtility.checkString(passportBack.optString("AttachmentType"));
                            passportBackAttachmentDesc = CommonUtility.checkString(passportBack.optString("AttachmentDesc"));
                            passportBackVerifiedInd = CommonUtility.checkString(passportBack.optString("VerifiedInd"));
                            passportBackNo = CommonUtility.checkString(passportBack.optString("PassportNo"));
                        }
                        else{}

                        JSONObject drivingLicense = jObjDetails.optJSONObject("driving_license");
                        if (drivingLicense != null && drivingLicense.length() > 0)
                        {
                            drivingLicenseAttachmentId = CommonUtility.checkString(drivingLicense.optString("AttachmentId"));
                            drivingLicenseAttachmentType = CommonUtility.checkString(drivingLicense.optString("AttachmentType"));
                            drivingLicenseAttachmentDesc = CommonUtility.checkString(drivingLicense.optString("AttachmentDesc"));
                            drivingLicenseVerifiedInd = CommonUtility.checkString(drivingLicense.optString("VerifiedInd"));
                            drivingLicenseNo = CommonUtility.checkString(drivingLicense.optString("DrivingLicenseNo"));
                        }
                        else{}

                        /*JSONObject tradeMembershipCard = jObjDetails.optJSONObject("trade_membership_card");
                        if (tradeMembershipCard != null && tradeMembershipCard.length() > 0)
                        {
                            businessLicenseAttachmentId = CommonUtility.checkString(tradeMembershipCard.optString("AttachmentId"));
                            businessLicenseAttachmentType = CommonUtility.checkString(tradeMembershipCard.optString("AttachmentType"));
                            businessLicenseAttachmentDesc = CommonUtility.checkString(tradeMembershipCard.optString("AttachmentDesc"));
                            businessLicenseVerifiedInd = CommonUtility.checkString(tradeMembershipCard.optString("VerifiedInd"));
                            businessLicenseNo = CommonUtility.checkString(tradeMembershipCard.optString("TradeMembershipNo"));
                        }
                        else{}*/

                        kycVerificationAdapter = new KYCVerificationAdapter(modelArrayList, context, this);
                        doc_recycler_view.setAdapter(kycVerificationAdapter);

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

                case ApiConstants.GET_CHECKOUT_DETAILS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        Log.v("------Diamond----- : ", "--------CheckOut_Details------- : " + jsonObject);

                        isCoupanApplied = CommonUtility.checkString(jsonObjectData.optString("is_coupan_applied"));
                        orderCouponCode = CommonUtility.checkString(jsonObjectData.optString("coupon_code"));
                        orderCouponValue = CommonUtility.checkString(jsonObjectData.optString("coupon_value"));
                        orderCouponDiscount = CommonUtility.checkString(jsonObjectData.optString("coupon_discount"));
                        orderSubTotal = CommonUtility.checkString(jsonObjectData.optString("sub_total"));
                        orderCgst = CommonUtility.checkString(jsonObjectData.optString("cgst"));
                        orderCgstPerc = CommonUtility.checkString(jsonObjectData.optString("cgst_perc"));
                        orderSgst = CommonUtility.checkString(jsonObjectData.optString("sgst"));
                        orderSgstPerc = CommonUtility.checkString(jsonObjectData.optString("sgst_perc"));
                        orderIgst = CommonUtility.checkString(jsonObjectData.optString("igst"));
                        orderIgstPerc = CommonUtility.checkString(jsonObjectData.optString("igst_perc"));
                        orderDiscountPerc = CommonUtility.checkString(jsonObjectData.optString("discount_perc"));
                        orderTax = CommonUtility.checkString(jsonObjectData.optString("tax"));
                        orderSubTotalWithTax = CommonUtility.checkString(jsonObjectData.optString("sub_total_with_tax"));
                        orderShippingCharge = CommonUtility.checkString(jsonObjectData.optString("shipping_charge"));
                        orderPlatformFee = CommonUtility.checkString(jsonObjectData.optString("platform_fee"));
                        orderTotalCharge = CommonUtility.checkString(jsonObjectData.optString("total_charge"));
                        orderTotalChargeTax = CommonUtility.checkString(jsonObjectData.optString("total_charge_tax"));
                        orderTotalChargeWithTax = CommonUtility.checkString(jsonObjectData.optString("total_charge_with_tax"));
                        orderTotalTaxes = CommonUtility.checkString(jsonObjectData.optString("total_taxes"));
                        orderTotalAmount = CommonUtility.checkString(jsonObjectData.optString("total_amount"));
                        orderTaxPerOnCharges = CommonUtility.checkString(jsonObjectData.optString("tax_per_on_charges"));
                        orderFinalAmount = CommonUtility.checkString(jsonObjectData.optString("final_amount"));
                        orderBankCharge = CommonUtility.checkString(jsonObjectData.optString("bank_charge"));
                        orderBankChargePerc = CommonUtility.checkString(jsonObjectData.optString("bank_charge_perc"));

                        // Sub Total Charges
                        if(orderSubTotal!=null && !orderSubTotal.equalsIgnoreCase(""))
                        {
                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderSubTotal);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            final_amount_tv1.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(subTotalFormat));
                        } else{}

                        // Final Amount
                        if(orderFinalAmount!=null && !orderFinalAmount.equalsIgnoreCase(""))
                        {
                            String finalAmountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderFinalAmount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            total_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(finalAmountTotalFormat));
                            final_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(finalAmountTotalFormat));
                        } else{}

                        // Shipping Charges
                        if(orderShippingCharge!=null && !orderShippingCharge.equalsIgnoreCase(""))
                        {
                            if(orderShippingCharge.equalsIgnoreCase("0"))
                            {
                                shipping_and_handling_tv.setText(getResources().getString(R.string.free_shipping));
                                shipping_and_handling_tv.setTextColor(ContextCompat.getColor(context, R.color.green));
                            }
                            else{
                                String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderShippingCharge);
                                String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                                shipping_and_handling_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));
                                shipping_and_handling_tv.setTextColor(ContextCompat.getColor(context, R.color.grey));
                            }
                        }else{}

                        // PlatFrom Charges
                        if(orderPlatformFee!=null && !orderPlatformFee.equalsIgnoreCase(""))
                        {
                            String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderPlatformFee);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            platform_fees_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));

                        }else{}

                        // Total Charges
                        if(orderTotalCharge!=null && !orderTotalCharge.equalsIgnoreCase(""))
                        {
                            String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderTotalCharge);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            total_charges_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));

                        }else{}

                        // Other Taxes
                        if(orderTotalChargeTax!=null && !orderTotalChargeTax.equalsIgnoreCase(""))
                        {
                            String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderTotalChargeTax);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            other_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));

                        }else{}

                        // Other Text GST
                        if(orderTaxPerOnCharges!=null && !orderTaxPerOnCharges.equalsIgnoreCase(""))
                        {
                            others_txt_gst_perc_tv.setText(orderTaxPerOnCharges + "% GST");
                        }else{}

                        // Diamond Taxes
                        if(orderTax!=null && !orderTax.equalsIgnoreCase(""))
                        {
                            String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderTax);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            diamond_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));

                        }else{}

                        // Total Taxes
                        if(orderTotalTaxes!=null && !orderTotalTaxes.equalsIgnoreCase(""))
                        {
                            String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderTotalTaxes);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            total_taxes_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));

                        }else{}

                        // Sub Total
                        if(orderTotalAmount!=null && !orderTotalAmount.equalsIgnoreCase(""))
                        {
                            String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderTotalAmount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            sub_total_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));

                        }else{}

                        // Bank Charges
                        if(orderBankCharge!=null && !orderBankCharge.equalsIgnoreCase(""))
                        {
                            String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderBankCharge);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            bank_charges_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));

                        }else{}

                        // Final Amount
                        if(orderFinalAmount!=null && !orderFinalAmount.equalsIgnoreCase(""))
                        {
                            String amountTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, orderFinalAmount);
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);
                            final_amount_tv.setText(getCurrencySymbol + "" + CommonUtility.currencyFormat(amountTotalFormat));

                        }else{}


                        JSONArray details = jsonObjectData.getJSONArray("details");

                        if(orderItemArrayList.size() > 0)
                        {
                            orderItemArrayList.clear();
                        }
                        for (int i = 0; i < details.length(); i++)
                        {
                            JSONObject objectCodes = details.getJSONObject(i);

                            AddToCartListModel model = new AddToCartListModel();
                            model.setStockId(CommonUtility.checkString(objectCodes.optString("stock_id")));
                            model.setItemName(CommonUtility.checkString(objectCodes.optString("item_name")));
                            model.setCategory(CommonUtility.checkString(objectCodes.optString("category")));
                            model.setSupplierId(CommonUtility.checkString(objectCodes.optString("supplier_id")));
                            model.setCertificateNo(CommonUtility.checkString(objectCodes.optString("certificate_no")));
                            model.setCarat(CommonUtility.checkString(objectCodes.optString("carat")));
                            model.setColor(CommonUtility.checkString(objectCodes.optString("color")));
                            model.setClarity(CommonUtility.checkString(objectCodes.optString("clarity")));
                            model.setShape(CommonUtility.checkString(objectCodes.optString("shape")));
                            model.setDiamondImage(CommonUtility.checkString(objectCodes.optString("diamond_image")));
                            model.setTotalGstPerc(CommonUtility.checkString(objectCodes.optString("total_gst_perc")));
                            model.setStatus(CommonUtility.checkString(objectCodes.optString("status")));
                            model.setSubtotal(CommonUtility.checkString(objectCodes.optString("subtotal")));
                            model.setShowingSubTotal(CommonUtility.checkString(objectCodes.optString("subtotal")));
                            model.setTotalPrice(CommonUtility.checkString(objectCodes.optString("total_price")));
                            model.setIsReturnable(CommonUtility.checkString(objectCodes.optString("is_returnable")));
                            model.setDxePrefered(CommonUtility.checkString(objectCodes.optString("dxe_prefered")));
                            model.setOnHold(CommonUtility.checkString(objectCodes.optString("on_hold")));
                            model.setStockNo(CommonUtility.checkString(objectCodes.optString("stock_no")));

                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, CommonUtility.checkString(objectCodes.optString("subtotal")));
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            model.setShowingSubTotal(subTotalFormat);
                            model.setCurrencySymbol(getCurrencySymbol);

                            orderItemArrayList.add(model);
                        }

                        //Log.e("recommandDiamondArrayList", "" + recommandDiamondArrayList.size());
                        if(orderItemArrayList!=null && orderItemArrayList.size()>0)
                        {
                            viewpager_layout.setVisibility(View.VISIBLE);
                            setPlaceItemListPager();
                        }
                        else{
                            viewpager_layout.setVisibility(View.GONE);
                        }

                       /* for (int i = 0; i <recommandDiamondArrayList.size() ; i++)
                        {

                            Log.e("--------selectedCurrencyValue : ", selectedCurrencyValue.toString());
                            Log.e("--------selectedCurrencyValue1 : ", selectedCurrencyCode.toString());
                            Log.e("--------selectedCurrencyValue11 : ", "" + recommandDiamondArrayList.get(i).getSubtotal());

                            String subTotalFormat =  CommonUtility.currencyConverter(selectedCurrencyValue, selectedCurrencyCode, recommandDiamondArrayList.get(i).getSubtotal());
                            String getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode);

                            recommandDiamondArrayList.get(i).setShowingSubTotal(subTotalFormat);
                            recommandDiamondArrayList.get(i).setCurrencySymbol(getCurrencySymbol);

                        }*/
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
    public void itemClick(int position, String action)
    {
    }

    final Handler handler = new Handler();
    Timer swipeTimer = new Timer();
    Runnable Update;
    int currentPage = 0;
    int NUM_PAGES = 0;

    public void setPlaceItemListPager() {
        viewPager.setAdapter(new MyPagerAdapter(activity, orderItemArrayList));
        tabLayout.setupWithViewPager(viewPager, true);
        final float density = getResources().getDisplayMetrics().density;
        if(orderItemArrayList!=null && orderItemArrayList.size()>=1)
        {
            viewpager_layout.setVisibility(View.VISIBLE);

            if(orderItemArrayList.size()>1){
                tabLayout.setVisibility(View.VISIBLE);
            }else {
                tabLayout.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            viewpager_layout.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
        }

        NUM_PAGES = orderItemArrayList.size();
        currentPage = 0;

        // Cancel any previous timer tasks if they exist
        if (swipeTimer != null) {
            swipeTimer.cancel();
            swipeTimer = new Timer();
        }

        if (Update == null) {
            // Auto start of viewpager
            Update = new Runnable() {
                public void run() {
                    if (NUM_PAGES > 0) {
                        if (currentPage >= NUM_PAGES) {
                            currentPage = 0;
                        }
                        viewPager.setCurrentItem(currentPage++, true);
                    }
                }
            };

            /*swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 8000, 8000);*/
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Optional implementation
            }

            @Override
            public void onPageSelected(int position) {
                // Highlight the selected tab
                // Highlight the selected tab
                tabLayout.setScrollPosition(position, 0f, true);
                // Update currentPage to the new position
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Optional implementation
            }
        });

    }

    private class MyPagerAdapter extends PagerAdapter {

        ArrayList<AddToCartListModel> list;
        LayoutInflater inflater;
        public MyPagerAdapter(Context context, ArrayList<AddToCartListModel> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }


        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            // Declare Variables
            ImageView pagerImg, status_img, returnable_img;
            CardView root_layout;
            TextView supplier_id_tv_pager, name_tv_Pager, item_type_tv,  return_policy_tv, sub_total_tv,diamond_type;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.row_place_order_item_list, container, false);
            pagerImg = (ImageView) itemView.findViewById(R.id.image_view);
            status_img = (ImageView) itemView.findViewById(R.id.status_img);
            returnable_img = (ImageView) itemView.findViewById(R.id.returnable_img);

            supplier_id_tv_pager = itemView.findViewById(R.id.supplier_id_tv);
            diamond_type = itemView.findViewById(R.id.diamond_type);

            root_layout = itemView.findViewById(R.id.root_layout);

            name_tv_Pager = itemView.findViewById(R.id.name_tv);
            item_type_tv = itemView.findViewById(R.id.item_type_tv);
            return_policy_tv = itemView.findViewById(R.id.return_policy_tv);
            sub_total_tv = itemView.findViewById(R.id.sub_total_tv);

            if(!list.get(position).getDiamondImage().equalsIgnoreCase(""))
            {
                Picasso.with(context)
                        .load(list.get(position).getDiamondImage())
                        .placeholder(R.mipmap.phl_diamond)
                        .error(R.mipmap.phl_diamond)
                        .into(pagerImg);
            }
            else{
                Picasso.with(context)
                        .load(R.mipmap.phl_diamond)
                        .placeholder(R.mipmap.phl_diamond)
                        .error(R.mipmap.phl_diamond)
                        .into(pagerImg);
            }


            supplier_id_tv_pager.setText("#"+list.get(position).getStockNo() + " | " + list.get(position).getSupplierId());
            name_tv_Pager.setText(list.get(position).getShape());
            item_type_tv.setText(list.get(position).getCarat() + getResources().getString(R.string.ct) + " " + list.get(position).getColor() + " " + list.get(position).getClarity());


            if(list.get(position).getCategory().equalsIgnoreCase("Natural"))
            {
                diamond_type.setBackgroundResource(R.drawable.background_yellow);
                diamond_type.setText("NATURAL");
            }
            else{
                diamond_type.setBackgroundResource(R.drawable.background_green_light_small_round_cornor);
                diamond_type.setText("LAB");
            }


            DecimalFormat formatter = new DecimalFormat("#,###,###");

            if(!list.get(position).getSubtotal().equalsIgnoreCase(""))
            {
                sub_total_tv.setText(list.get(position).getCurrencySymbol() + "" + CommonUtility.currencyFormat(list.get(position).getShowingSubTotal()));
            }else {}

            if(list.get(position).getIsReturnable().equalsIgnoreCase("1"))
            {
                returnable_img.setVisibility(View.VISIBLE);
            }
            else{
                returnable_img.setVisibility(View.GONE);
            }

            returnable_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    return_policy_tv.setVisibility(View.VISIBLE);

                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            return_policy_tv.setVisibility(View.GONE);
                        }
                    }, 2000);
                }
            });

            if(list.get(position).getStatus().equalsIgnoreCase("Available"))
            {
                status_img.setVisibility(View.VISIBLE);

                status_img.setBackgroundResource(R.drawable.available);
            }
            else if(list.get(position).getStatus().equalsIgnoreCase("On Hold")){
                status_img.setVisibility(View.VISIBLE);
                status_img.setBackgroundResource(R.drawable.onhold);
            }
            else
            {
                status_img.setVisibility(View.GONE);
            }

            root_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            // Add viewpager_item.xml to ViewPager
            ((ViewPager) container).addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Remove viewpager_item.xml from ViewPager
            ((ViewPager) container).removeView((RelativeLayout) object);

        }
    }
}