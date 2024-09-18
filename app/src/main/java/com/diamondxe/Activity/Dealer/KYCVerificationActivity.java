package com.diamondxe.Activity.Dealer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Adapter.Dealer.KYCVerificationAdapter;
import com.diamondxe.ApiCalling.ApiConstants;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.KYCVerificationModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class KYCVerificationActivity extends SuperActivity implements RecyclerInterface {

    private ImageView back_img;
    private TextView support_tv, re_submit_tv,kyc_verified_lbl,kyc_verified_lbl1;
    private RelativeLayout bottom_bar_rel;
    private RecyclerView doc_recycler_view;
    ArrayList<KYCVerificationModel> modelArrayList;
    KYCVerificationAdapter kycVerificationAdapter;

    private Activity activity;
    private Context context;

    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;

    String document_status = "";
    String aadhaarAttachmentType = "", aadhaarAttachmentDesc = "", aadhaarVerifiedInd  = "", aadhaarNo = "", aadhaarBackAttachmentType = "", aadhaarBackAttachmentDesc = "", aadhaarBackVerifiedInd = "", aadhaarBackNo = "",
            panCardAttachmentType = "", panCardAttachmentDesc = "", panCardVerifiedInd = "", panCardNo = "", companyPanCardAttachmentType = "", companyPanCardAttachmentDesc = "", companyPanCardVerifiedInd = "", companyPanCardNo = "", companyGstCertificateAttachmentType = "",
            companyGstCertificateAttachmentDesc = "", companyGstCertificateVerifiedInd = "", companyGstCertificateNo = "", iecCardAttachmentType = "",
            iecCardAttachmentDesc = "", iecCardVerifiedInd = "", iecCardNo = "", passportFrontAttachmentType = "", passportFrontAttachmentDesc = "", passportFrontVerifiedInd = "", passportFrontNo = "",
            passportBackAttachmentType = "", passportBackAttachmentDesc = "", passportBackVerifiedInd = "", passportBackNo = "";
    String aadhaarFrontAttachmentId ="", aadhaarBackAttachmentId="", panCardAttachmentId="", companyPanCardAttachmentId="", companyGstCertificateAttachmentId="", iecAttachmentId="", passportFrontAttachmentId="", passportBackAttachmentId="";
    String drivingLicenseAttachmentId = "", drivingLicenseAttachmentType = "", drivingLicenseAttachmentDesc = "", drivingLicenseVerifiedInd = "", drivingLicenseNo  = "";
    String businessLicenseAttachmentId = "", businessLicenseAttachmentType ="", businessLicenseAttachmentDesc ="", businessLicenseVerifiedInd ="", businessLicenseNo ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc_verification);

        context = activity = this;

        modelArrayList = new ArrayList<>();

        kyc_verified_lbl = findViewById(R.id.kyc_verified_lbl);
        kyc_verified_lbl1 = findViewById(R.id.kyc_verified_lbl1);

        bottom_bar_rel = findViewById(R.id.bottom_bar_rel);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        support_tv = findViewById(R.id.support_tv);
        support_tv.setOnClickListener(this);

        re_submit_tv = findViewById(R.id.re_submit_tv);
        re_submit_tv.setOnClickListener(this);

        doc_recycler_view = findViewById(R.id.doc_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        doc_recycler_view.setLayoutManager(layoutManager);
        doc_recycler_view.setNestedScrollingEnabled(false);

        getKYCDetailsAPI(false);
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
        else if(id == R.id.support_tv)
        {
            Utils.hideKeyboard(activity);
        }
        else if(id == R.id.re_submit_tv)
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

            intent = new Intent(activity, KYCVerificationDocUploadActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
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

    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

        try {
            Log.e("Diamonds : ", "--------JSONObject-------- : " + jsonObject);

            JSONObject jsonObjectData = jsonObject;
            String message = jsonObjectData.optString("msg");
            document_status = jsonObjectData.optString("document_status");

            switch (service_ID) {
                case ApiConstants.GET_KYC_DETAILS_ID:

                    if (jsonObjectData.optString("status").equalsIgnoreCase("1"))
                    {
                        JSONObject jObjDetails = jsonObjectData.optJSONObject("details");

                        if(document_status.equalsIgnoreCase("1"))
                        {
                            re_submit_tv.setVisibility(View.VISIBLE);
                            bottom_bar_rel.setVisibility(View.VISIBLE);
                            kyc_verified_lbl.setText(getResources().getString(R.string.kyc_doc_submit));
                            kyc_verified_lbl1.setText(getResources().getString(R.string.technical_reviewing_soon));

                        }
                        else if(document_status.equalsIgnoreCase("1"))
                        {
                            bottom_bar_rel.setVisibility(View.VISIBLE);
                            re_submit_tv.setVisibility(View.VISIBLE);
                            kyc_verified_lbl.setText(getResources().getString(R.string.kyc_doc_submit));
                            kyc_verified_lbl1.setText(getResources().getString(R.string.technical_reviewing_soon));

                        }
                        else if(document_status.equalsIgnoreCase("2"))
                        {
                            bottom_bar_rel.setVisibility(View.GONE);
                            kyc_verified_lbl.setText(getResources().getString(R.string.kyc_doc_verified));
                            kyc_verified_lbl1.setText(getResources().getString(R.string.doc_verified_thanks));

                        }
                        else if(document_status.equalsIgnoreCase("3"))
                        {
                            bottom_bar_rel.setVisibility(View.VISIBLE);
                            re_submit_tv.setVisibility(View.VISIBLE);
                            kyc_verified_lbl.setText(getResources().getString(R.string.kyc_doc_unverified));
                            kyc_verified_lbl1.setText(getResources().getString(R.string.doc_resubmit_again));

                        }
                        else{}

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
}