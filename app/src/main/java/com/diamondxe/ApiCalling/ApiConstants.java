package com.diamondxe.ApiCalling;

import android.content.Context;

import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment;

public interface ApiConstants {

    //String DOMAIN_NAME = "https://app-uat.diamondxe.com/app/v1/";

    // Live Server URL
    String DOMAIN_NAME = "https://app.diamondxe.com/app/v1/";

    /*String DOMAIN_IMAGE_NAME = "http://192.168.2.194/diamond/";
    String DOMAIN_IMAGE = "http://192.168.2.194/diamond";*/

    String HOME = "home";
    String TOP_DEALS = "top-deals";
    String GET_HOME_PAGE_DETAILS = "get-home-page-details";
    String GET_ATTRIBUTES = "get-attributes";
    String GET_DIAMONDS = "get-diamonds";
    String ADD_TO_CART = "add-to-cart";
    String ADD_TO_WISHLIST = "add-to-wishlist";
    String REMOVE_FROM_WISHLIST = "remove-from-wishlist";
    String GET_RECOMMENDED_DIAMONDS = "get-recommended-diamonds";
    String GET_DIAMONDS_DETAILS = "get-diamond-details";
    String DIAMOND_SIZE_PREVIEW = "diamond-size-preview";
    String GET_WISHLIST_DETAILS = "get-wishlist-details";
    String GET_CART_DETAILS = "get-cart-details";
    String REMOVE_FROM_CART = "remove-from-cart";
    String GET_CURRENCY_RATES = "get-currency-rates";
    String GET_COUNTRY_LIST = "country-list";
    String GET_STATE_LIST = "state-list";
    String GET_CITY_LIST = "city-list";
    String GET_CMS_DETAILS = "get-cms-details";
    String REGISTER_BUYER = "register-buyer";
    String VALIDATE_DOCUMENT = "validate-document";
    String VERIFY_EMAIL = "verify-email";
    String LOGIN = "login";
    String FORGOT_PASSWORD = "forgot-password";
    String RESET_PASSWORD = "reset-password";
    String REGISTER_DEALER = "register-dealer";
    String REGISTER_SUPPLIER = "register-supplier";
    String CHANGE_PASSWORD = "change-password";
    String GET_PROFILE = "get-profile";
    String UPDATE_PROFILE = "update-profile";
    String ADD_ADDRESS = "add-address";
    String GET_ADDRESS_SHIPPING = "get-address/shipping";
    String GET_ADDRESS_BILLING = "get-address/billing";
    String UPDATE_ADDRESS = "update-address";
    String REMOVE_ADDRESS = "remove-address";
    String LOGOUT = "logout";
    String GET_KYC_DETAILS = "get-kyc-details";
    String UPDATE_KYC_DETAILS = "update-kyc-details";
    String PLACE_ORDER = "place-order";
    String DELETE_ACCOUNT = "delete-user-account";
    String DEALER_SETTING = "dealer/settings";
    String DEALER_UPDATE_SETTING = "dealer/update-settings";
    String GET_WALLET_HISTORY = "wallet-history";
    String GET_DXE_BANK_DETAILS = "get-dxe-bank-details";
    String PHONE_PE_PAYMENT_OPTION = "phonepe-payment-options";
    String CUSTOM_PAYMENT_INIT = "custom-payment/init";
    String CUSTOM_PAYMENT_STATUS = "custom-payment/status";
    String CUSTOM_PAYMENT_HISTORY = "custom-payment/history";
    String GET_BANK_CHARGES = "get-bank-charges";
    String GET_CHECKOUT_DETAILS = "get-checkout-details";
    String CREATE_ORDER = "create-order";
    String ORDER_PAYMENT_STATUS = "order-payment-status";
    String ORDER_LIST = "orders-list";
    String ORDER_DETAILS = "order-details";
    String ORDER_TRACKING_DETAILS = "order-tracking-details";
    String ORDER_SUMMARY = "order-summary";
    String ORDER_CANCEL_REASON = "order-cancel-reasons";
    String CANCEL_ORDER_DETAILS = "cancel-order-details";
    String RETURN_ORDER_DETAILS = "return-order-details";
    String CANCEL_ORDER_CHECKOUT = "cancel-order-checkout";
    String CANCEL_ORDER = "cancel-order";
    String RETURN_ORDER_CHECKOUT = "return-order-checkout";
    String RETURN_ORDER = "return-order";

    int HOME_ID = 2;
    int LOGOUT_ID = 3;
    int TOP_DEALS_ID = 4;
    int GET_HOME_PAGE_DETAILS_ID = 5;
    int GET_ATTRIBUTES_ID = 6;
    int GET_DIAMONDS_ID = 7;
    int ADD_TO_CART_ID = 8;
    int ADD_TO_WISHLIST_ID = 9;
    int REMOVE_FROM_WISHLIST_ID = 10;
    int GET_RECOMMENDED_DIAMONDS_ID = 11;
    int GET_DIAMONDS_DETAILS_ID = 12;
    int DIAMOND_SIZE_PREVIEW_ID = 13;
    int GET_WISHLIST_DETAILS_ID = 14;
    int GET_CART_DETAILS_ID = 15;
    int REMOVE_FROM_CART_ID = 16;
    int GET_CART_DETAILS_FOR_CALCULATION_ID = 17;
    int GET_CURRENCY_RATES_ID = 18;
    int GET_COUNTRY_LIST_ID = 19;
    int GET_STATE_LIST_ID = 20;
    int GET_CITY_LIST_ID = 21;
    int GET_CMS_DETAILS_ID = 22;
    int REGISTER_BUYER_ID = 23;
    int VALIDATE_DOCUMENT_ID = 24;
    int VERIFY_EMAIL_ID = 25;
    int LOGIN_ID = 26;
    int FORGOT_PASSWORD_ID = 27;
    int RESET_PASSWORD_ID = 28;
    int REGISTER_DEALER_ID = 29;
    int REGISTER_SUPPLIER_ID = 30;
    int CHANGE_PASSWORD_ID = 31;
    int GET_PROFILE_ID = 32;
    int UPDATE_PROFILE_ID = 33;
    int ADD_ADDRESS_ID = 34;
    int GET_ADDRESS_SHIPPING_ID = 35;
    int GET_ADDRESS_BILLING_ID = 36;
    int UPDATE_ADDRESS_ID = 37;
    int REMOVE_ADDRESS_ID = 38;
    int GET_KYC_DETAILS_ID = 39;
    int UPDATE_KYC_DETAILS_ID = 40;
    int PLACE_ORDER_ID = 41;
    int DELETE_ACCOUNT_ID = 42;
    int DEALER_SETTING_ID = 43;
    int DEALER_UPDATE_SETTING_ID = 44;
    int GET_WALLET_HISTORY_ID = 45;
    int GET_DXE_BANK_DETAILS_ID = 46;
    int PHONE_PE_PAYMENT_OPTION_ID = 47;
    int CUSTOM_PAYMENT_INIT_ID = 48;
    int CUSTOM_PAYMENT_STATUS_ID = 49;
    int CUSTOM_PAYMENT_HISTORY_ID = 50;
    int GET_BANK_CHARGES_ID = 51;
    int GET_CHECKOUT_DETAILS_ID = 52;
    int CREATE_ORDER_ID = 53;
    int ORDER_PAYMENT_STATUS_ID = 54;
    int ORDER_LIST_ID = 55;
    int ORDER_DETAILS_ID = 56;
    int ORDER_SUMMARY_ID = 57;
    int ORDER_TRACKING_DETAILS_ID = 58;
    int ORDER_CANCEL_REASON_ID = 59;
    int CANCEL_ORDER_DETAILS_ID = 60;
    int RETURN_ORDER_DETAILS_ID = 61;
    int CANCEL_ORDER_CHECKOUT_ID = 62;
    int CANCEL_ORDER_PARTIAL_CHECKOUT_ID = 63;
    int CANCEL_ORDER_ID = 64;
    int RETURN_ORDER_CHECKOUT_ID = 65;
    int RETURN_ORDER_PARTIAL_CHECKOUT_ID = 66;


    String MSG_INTERNETERROR = "Internet connection is not available.";
    String SERVER_ERROR="Oops ! - server not responding, please try after some time.";
    String NO_DATA_FOUND = "No Data Found";
    String NO_RESULT_FOUND = "No result found";
    String NO_INTERNET = "Internet is not connected";


    // String DATE_SOURCE_FORMAT = "yyyy-MM-dd";
   // String DATE_TARGET_FORMAT = "MM/dd/yyyy";
    String DATE_DOB_FORMAT = "dd/MM/yyyy";

    String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    String rupeesIcon = "₹";


    /*public static String GetSoapServicePath(String requestType)
    {
        return DOMAIN_NAME + requestType;
    }*/

    String NATURAL = "natural";
    String LAB_GROWN = "labgrown";

    String WISHLIST_FRAGMENT = "wishlist_fragment";
    String CART_FRAGMENT = "cart_fragment";
    String CATEGORY_FRAGMENT = "category_fragment";
    String HOME_FRAGMENT = "Home_fragment";

    String INDIA_FLAG_URL = "https://app-uat.diamondxe.com/img/country-flags/32x32/in.png";
    String INDIA_COUNTRY_CODE = "+91";
    String INDIA_CURRENCY_CODE = "INR";
    String INDIA_CURRENCY_VALUE = "1";
    String INDIA_CURRENCY_DESC = "Indian Rupees";

    String LINKDIN_URL = "https://www.linkedin.com/company/86411293/admin/feed/posts/";
    String INSTAGRAM_URL = "https://www.instagram.com/diamond_xe/";
    String FACEBOOK_URL = "https://www.facebook.com/DiamondXE";
    String YOUTUBE_URL = "https://youtube.com/@d-xe?si=VuTSaNDDvStzT1In";

    String USER_DEALER = "Dealer";
    String USER_BUYER = "Buyer";
    String PANNo = "PANNo";
    String GSTNo = "GSTNo";
    String AADHAARNo = "aadhaarNo";
    String DRIVING_LICENSE = "drivingLicense";

    String FOR_BASE64_IMAGE = "data:image/png;base64,";

    // Order Type Value.
    String CART = "cart";
    String BUY_NOW = "buy now";

    String ECONOMIC_TIMES = "https://economictimes.indiatimes.com/small-biz/sme-sector/online-diamond-trading-platform-d[…]sparent-pricing-for-industry/articleshow/102904102.cms";
    String RETAIL_JEWELLER = "https://retailjewellerindia.com/online-diamond-trading-platform-diamondxe-seeks-to-change-the-way-diamonds-are-bought-and-sold/";
    String MIDDAY = "https://www.mid-day.com/brand-media/article/diamondxe-set-to-revolutionise-the-industry-with-innovative-solutions-23306755";
    String VIEW_ALL_NEWS = "https://diamondxe.com/media-gallery";

}
