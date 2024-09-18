package com.diamondxe.Utils;

import android.annotation.SuppressLint;

import com.diamondxe.Beans.AttributeDetailsModel;
import com.diamondxe.Beans.CountryListModel;

import java.util.ArrayList;


@SuppressLint({ "NewApi", "SimpleDateFormat" }) public class Constant {
    public static int lazyLoadingLimit = 20;

    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_LOADER = 1;

    public static String searchTitleName= "";
    public static String searchType= "";
    public static String emailID= "";

    public static String getCurrencySymbol= ""; // Hold Currnecy Symbol Like $ and Other
    public static String getCurrencyValue= ""; // Hold Currnecy Value and Other

    public static String editBillingAddress= "";
    public static String editShippingAddress= "";
    public static String addressID= "";
    public static String address1= "";
    public static String address2= "";
    public static String country= "";
    public static String state= "";
    public static String city= "";
    public static String countryID= "";
    public static String stateID= "";
    public static String cityID= "";
    public static String pinCode= "";
    public static String mobileCode= "";
    public static String mobileNumber= "";
    public static String email= "";
    public static String setIsDefault= "";
    public static String gstNumber= "";
    public static String businessName= "";

    public static String aadhaarVerifiedInd = "";
    public static String aadhaarBackVerifiedInd = "";
    public static String panCardVerifiedInd = "";
    public static String companyPanCardVerifiedInd = "";
    public static String companyGstCertificateVerifiedInd = "";
    public static String iecCardVerifiedInd = "";
    public static String passportFrontVerifiedInd = "";
    public static String passportBackVerifiedInd = "";
    public static String drivingLicenceVerifiedInd = "";

    public static String aadhaarFrontAttachmentId = "";
    public static String aadhaarBackAttachmentId = "";
    public static String panCardAttachmentId = "";
    public static String companyPanCardAttachmentId = "";
    public static String companyGstCertificateAttachmentId = "";
    public static String iecAttachmentId = "";
    public static String passportFrontAttachmentId = "";
    public static String passportBackAttachmentId = "";
    public static String drivingLicenceAttachmentId = "";

    public static String manageClickEventForRedirection = "";
    public static String callHomeScreenOnResume = "";// Use This Key For HomeScreen OnResume Call Or Not
    public static String callForBackScreenForUpdateCurrencySymbol = "";// Use This Key For Back Screen OnResume Call Or Not


    public static String paymentOrderID="";
    public static String paymentUserID="";
    public static String diamondQualitySelected="";
    public static String makeSelectedValue="";

    public static String manageFragmentCalling="";

    public static ArrayList<AttributeDetailsModel> cutTypeArrayList;
    public static ArrayList<AttributeDetailsModel> polishTypeArrayList;
    public static ArrayList<AttributeDetailsModel> symmertyTypeArrayList;
    public static ArrayList<AttributeDetailsModel> technologyTypeArrayList;
    public static ArrayList<AttributeDetailsModel> fancyColorIntensityArrayList;
    public static ArrayList<AttributeDetailsModel> fancyColorOverToneArrayList;
    public static ArrayList<AttributeDetailsModel> tableDataPercantageArrayList;
    public static ArrayList<AttributeDetailsModel> depthDataPercantageArrayList;
    public static ArrayList<AttributeDetailsModel> crowmArrayList;
    public static ArrayList<AttributeDetailsModel> pavillionArrayList;
    public static ArrayList<AttributeDetailsModel> eyeCleanArrayList;
    public static ArrayList<AttributeDetailsModel> shadeArrayList;
    public static ArrayList<AttributeDetailsModel> lusterArrayList;

    public static ArrayList<CountryListModel> currencyArrayList;

    public static String shapeDiamondValue="";
    public static String searchFrm="";
    public static String priceFrm="";
    public static String priceTo="";
    public static String caratFrm="";
    public static String caratTo="";
    public static String colorType="";
    public static String colorValue="";
    public static String fancyColorValue="";
    public static String clarityValue="";
    public static String certificateValue="";
    public static String fluorescenceValue="";
    public static String makeValue="";
    public static String isReturnable="";
    public static String searchKeyword="";
    public static String cutValue="";
    public static String polishValue="";
    public static String symmetryValue="";
    public static String technologyValue="";
    public static String eyeCleanValue="";
    public static String shadeValue="";
    public static String lusterValue="";
    public static String fancyColorIntensity="";
    public static String fancyColorOvertone="";
    public static String tableFrm="";
    public static String tableTo="";
    public static String depthFrmSpinner="";
    public static String depthToSpinner="";
    public static String lengthFrm="";
    public static String lengthTo="";
    public static String widthFrm="";
    public static String widthTo="";
    public static String depthFrmInput="";
    public static String depthToInput="";
    public static String crownFrm="";
    public static String crownTo="";
    public static String pavillionFrm="";
    public static String pavillionTo="";
    public static String lotID="";
    public static String location="";
    public static String sortingBy="";
    public static ArrayList<AttributeDetailsModel> cutTypeArrayList1;

    public static ArrayList<AttributeDetailsModel> colorTypeFilterApploedArrayList;

    public static String filterClear="";
    public static String showRadioButtonForSelectAddress="";
    public static String cardCount="";
    public static String wishListCount="";
    public static String documentStatus="";
    public static String deliveryPincode="";
    public static String collectFromHub="0";
    public static String shippingAddressID=""; // This is Use When Create Place Order
    public static String billingAddressID=""; // This is Use When Create Place Order
    public static String comeFrom=""; // This is Use After Payment Complete and Goto Payment Status Screen And Check User Come Frm Which Type Payment
    public static String orderType=""; // This is Use Which Type Order Order Type "Cart" or "buy now"
    public static String certificateNumber=""; // This is Use When User Pay Payment Through BUY Now

}