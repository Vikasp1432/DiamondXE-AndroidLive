package com.diamondxe.ApiCalling;

import static com.diamondxe.ApiCalling.ApiConstants.DEALER_SETTING;
import static com.diamondxe.ApiCalling.ApiConstants.DIAMOND_SIZE_PREVIEW;
import static com.diamondxe.ApiCalling.ApiConstants.GET_ADDRESS_BILLING;
import static com.diamondxe.ApiCalling.ApiConstants.GET_ADDRESS_SHIPPING;
import static com.diamondxe.ApiCalling.ApiConstants.GET_BANK_CHARGES;
import static com.diamondxe.ApiCalling.ApiConstants.GET_CART_DETAILS;
import static com.diamondxe.ApiCalling.ApiConstants.GET_CHECKOUT_DETAILS;
import static com.diamondxe.ApiCalling.ApiConstants.GET_COUNTRY_LIST;
import static com.diamondxe.ApiCalling.ApiConstants.GET_CURRENCY_RATES;
import static com.diamondxe.ApiCalling.ApiConstants.GET_DIAMONDS;
import static com.diamondxe.ApiCalling.ApiConstants.GET_DIAMONDS_DETAILS;
import static com.diamondxe.ApiCalling.ApiConstants.GET_DXE_BANK_DETAILS;
import static com.diamondxe.ApiCalling.ApiConstants.GET_KYC_DETAILS;
import static com.diamondxe.ApiCalling.ApiConstants.GET_PROFILE;
import static com.diamondxe.ApiCalling.ApiConstants.GET_RECOMMENDED_DIAMONDS;
import static com.diamondxe.ApiCalling.ApiConstants.GET_WISHLIST_DETAILS;
import static com.diamondxe.ApiCalling.ApiConstants.ORDER_CANCEL_REASON;
import static com.diamondxe.ApiCalling.ApiConstants.PHONE_PE_PAYMENT_OPTION;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.opengl.Visibility;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.diamondxe.Activity.TransparentActivity;
import com.diamondxe.Interface.JsonResponce;
import com.diamondxe.MyApplication;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.TimeZoneCountryCodeMapper;
import com.diamondxe.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class VollyApiActivity {


    ProgressDialog _dialog;
    String _requestType;
    int service_ID = 0;
    SuperActivity _eventLoggerClass;
    JsonResponce jsonResponce;
    String type="";
    HashMap<String, String> _dataToPost;
    Context context;
    boolean isLoaderHide;

    String apiType="";

    RequestQueue requestQueue;

    public VollyApiActivity(Context con,SuperActivity _baseClass, HashMap<String, String> _dataEntity, String _type, int ID)
    {
        this.context = con;
        this._dataToPost = _dataEntity;
        _eventLoggerClass  = _baseClass;
        _requestType = _type;
        this.service_ID = ID;
        this.type = "Activity";
        executeNetworkCall();
    }

    public VollyApiActivity(Context con,JsonResponce jsonResponce, HashMap<String, String> _dataEntity, String _type, int ID)
    {
        this.context = con;
        this._dataToPost = _dataEntity;
        this.jsonResponce  = jsonResponce;
        _requestType = _type;
        this.service_ID = ID;
        this.type = "Fragment";
        executeNetworkCall();
    }


    /*Just For Hide Loader - For Map */

    public VollyApiActivity(Context con, SuperActivity _baseClass, HashMap<String, String> _dataEntity, String _type, int ID,boolean isLoaderHide)
    {
        this.context = con;
        this._dataToPost = _dataEntity;
        _eventLoggerClass  = _baseClass;
        _requestType = _type;
        this.service_ID = ID;
        this.type = "Activity";
        this.isLoaderHide = isLoaderHide;
        executeNetworkCall();
    }


    public VollyApiActivity(Context con, JsonResponce jsonResponce, HashMap<String, String> _dataEntity, String _type, int ID,boolean isLoaderHide, String apiType)
    {
        this.context = con;
        this._dataToPost = _dataEntity;
        this.jsonResponce  = jsonResponce;
        _requestType = _type;
        this.service_ID = ID;
        this.type = "Fragment";
        this.isLoaderHide = isLoaderHide;
        this.apiType = apiType;
        executeNetworkCall();
    }


    public void executeNetworkCall()
    {
       // _dataToPost.put("device_type", "android");
        Log.e("------Diamond-----", "Sending Parameters : " + _dataToPost);

        if (Utils.isNetworkAvailable(context))
        {
            callApi();
        } else {
            Toast.makeText(context, ApiConstants.MSG_INTERNETERROR, Toast.LENGTH_SHORT).show();
        }
    }


    public void callApi(){
        if (!isLoaderHide) {
            Log.e("Diamond", "isLoaderHide1 : " + isLoaderHide);
            if (type.equalsIgnoreCase("Activity"))
            {
                Log.e("Diamond", "isLoaderHide2 : " + isLoaderHide);
                if(!TransparentActivity.active){
                    Log.e("Diamond", "isLoaderHide3 : " + isLoaderHide);
                    _eventLoggerClass.startActivity(new Intent(_eventLoggerClass, TransparentActivity.class));
                }
            } else {
                Log.v("Diamond", "isLoaderHide4 : " + isLoaderHide);
                if(!TransparentActivity.active){

                    Log.e("Diamond", "isLoaderHide5 : " + isLoaderHide);
                   // Log.e("Diamond", "isLoaderHide6 : " + MyApplication.isTransparentActivityRunning());

                    if (!MyApplication.isTransparentActivityRunning()) {
                        context.startActivity(new Intent(context, TransparentActivity.class));
                    }
                    else{
                        //Toast.makeText(context, "TransparentActivity is already running", Toast.LENGTH_SHORT).show();
                    }

                    /*ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
                    if (!tasks.isEmpty()) {
                        ComponentName topActivity = tasks.get(0).topActivity;
                        if (topActivity.getClassName().equals("com.diamondxe.Activity.TransparentActivity")) {
                            // TransparentActivity is already running
                            //Toast.makeText(context, "TransparentActivity is already running", Toast.LENGTH_SHORT).show();
                        } else {
                            context.startActivity(new Intent(context, TransparentActivity.class));
                        }
                    } else {
                        context.startActivity(new Intent(context, TransparentActivity.class));
                    }*/

                    //context.startActivity(new Intent(context, TransparentActivity.class));
                }
            }
        } else {
        }
        try {

            requestQueue = Volley.newRequestQueue(context);

            //String _url = ApiConstants.GetSoapServicePath(_requestType);
            String _url = "";

            Log.e("------Diamond-----", "_requestType111 : " + _requestType);

            if(_requestType.equalsIgnoreCase(GET_DIAMONDS) || _requestType.equalsIgnoreCase(GET_RECOMMENDED_DIAMONDS)
            || _requestType.equalsIgnoreCase(GET_DIAMONDS_DETAILS) || _requestType.equalsIgnoreCase(DIAMOND_SIZE_PREVIEW)
            || _requestType.equalsIgnoreCase(GET_WISHLIST_DETAILS) || _requestType.equalsIgnoreCase(GET_CART_DETAILS) ||
            _requestType.equalsIgnoreCase(GET_CURRENCY_RATES) || _requestType.equalsIgnoreCase(GET_PROFILE) ||
            _requestType.equalsIgnoreCase(GET_ADDRESS_SHIPPING) || _requestType.equalsIgnoreCase(GET_ADDRESS_BILLING) ||
            _requestType.equalsIgnoreCase(GET_KYC_DETAILS) || _requestType.equalsIgnoreCase(DEALER_SETTING) ||
            _requestType.equalsIgnoreCase(PHONE_PE_PAYMENT_OPTION) ||
            _requestType.equalsIgnoreCase(GET_BANK_CHARGES) || _requestType.equalsIgnoreCase(ORDER_CANCEL_REASON))
            {
                Uri.Builder builder = Uri.parse(ApiConstants.DOMAIN_NAME + _requestType).buildUpon();
                for (Map.Entry<String, String> entry : _dataToPost.entrySet()) {
                    builder.appendQueryParameter(entry.getKey(), entry.getValue());
                }
                // _url = builder.build().toString();

                _url = ApiConstants.DOMAIN_NAME + _requestType + constructUrlParameters(_dataToPost);

                Log.e("------Diamond-----", "_url_final : " + _url);
                Log.e("------Diamond-----", "_requestType : " + _requestType);

               // _url = ApiConstants.DOMAIN_NAME + _requestType;
            }
            else
            {
                _url = ApiConstants.DOMAIN_NAME + _requestType;
            }


            //_url = ApiConstants.DOMAIN_NAME + _requestType;

            Log.v("------Diamond-----","_url : "+_url);
            Log.v("------Diamond-----","_requestType : "+_requestType);

            // Using "apiType" Check API Type GET Or Post.
            StringRequest jsObjRequest = new StringRequest(apiType.equalsIgnoreCase("GET") ? Request.Method.GET : Request.Method.POST, _url, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response) {

                    JSONObject _results = null;
                    try {
                        Log.v("Diamond", "_results11111111111");
                        _results = new JSONObject(response);
                        Log.v("Diamond", "_results : " + _results.toString(4));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    /*if (_dialog!=null && _dialog.isShowing())
                    {
                        _dialog.dismiss();
                    } else {}*/


                    TransparentActivity.terminateTransparentActivity();

                    if (type.equalsIgnoreCase("Activity"))
                    {
                        if (_results != null) {
                            _eventLoggerClass.getSuccessResponce(_results,service_ID);

                        }else{
                            Toast.makeText(context, ApiConstants.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (_results != null) {
                            jsonResponce.getSuccessResponce(_results,service_ID);

                        }else{
                            Toast.makeText(context, ApiConstants.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // handle error response

                    JSONObject _results = null;
                   // _eventLoggerClass.getFinalLogger(_results,service_ID);

                    if (type.equalsIgnoreCase("Activity")) {
                        _eventLoggerClass.getErrorResponce("Error", service_ID);
                    }
                    else{
                        jsonResponce.getSuccessResponce(_results,service_ID);
                    }

                    error.printStackTrace();


                    /*For Print Error */

                    // As of f605da3 the following should work
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            // Now you can use any deserializer to make sense of data

                            Log.v("Diamond","Error : "+res);
                            JSONObject obj = new JSONObject(res);
                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (Exception e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }

                    parseVolleyError(error);
                    Toast.makeText(context, ApiConstants.SERVER_ERROR, Toast.LENGTH_SHORT).show();

                    /*if (_dialog!=null && _dialog.isShowing())
                    {
                        _dialog.dismiss();
                    } else {}*/

                    TransparentActivity.terminateTransparentActivity();
                }
            })

            {
                @Override
                public byte[] getBody() throws AuthFailureError {

                    Log.e("params", _dataToPost.toString());
                    if (_dataToPost != null && _dataToPost.size() > 0)
                    {
                       // return encodeParameters(_dataToPost, getParamsEncoding());
                    return encodeParameters(_dataToPost, getParamsEncoding());
                    }
                    return null;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //params.put("Content-Type", "application/x-www-form-urlencoded");

                     /*String uuid = UUID.randomUUID().toString();
                     Log.e("-----uuid1------", uuid);*/
                     //String uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                     String uuid = CommonUtility.getAndroidId(context);

                     String authToken = "", timeZoneId = "", timeZoneCountryCode="";
                     // Get the default TimeZone ID
                     timeZoneId = TimeZone.getDefault().getID();
                     timeZoneCountryCode = TimeZoneCountryCodeMapper.getCountryCodeFromTimeZone(timeZoneId);

                    // Use the country code as needed
                    // Log.e("Country_Code_Using_TimeZone: " , timeZoneId);
                    // Log.e("Country_Code_Using_TimeZone1: " , timeZoneCountryCode);
                    //Log.e("Country_Code_Using_TimeZone2: " , uuid);

                     authToken= CommonUtility.getGlobalString(context, "mobile_auth_token");
                    _dataToPost.put("apikey", "b8795c60-1400-4d70-b254-837a2a1da9e7");
                    _dataToPost.put("location", timeZoneCountryCode);
                    //_dataToPost.put("Authorization", CommonUtility.getGlobalString(context, "tokenType")+ " "+ CommonUtility.getGlobalString(context, "mobile_auth_token"));
                    Log.e("authToken", authToken);
                    if(authToken!=null && authToken!="")
                    {
                        _dataToPost.put("Authorization", "Bearer " + CommonUtility.getGlobalString(context, "mobile_auth_token"));
                    }
                    else{
                        _dataToPost.put("Authorization", "");
                    }
                    _dataToPost.put("deviceId", uuid);
                    _dataToPost.put("sessionId", uuid);

                    return _dataToPost;
                }

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset="+ getParamsEncoding();
                    //return "application/x-www-form-urlencoded; Content-Type: application/json; charset="+ getParamsEncoding();
                }

                private byte[] encodeParameters(Map<String, String> params,String paramsEncoding) {
                    StringBuilder encodedParams = new StringBuilder();
                    try {
                        for (Map.Entry<String, String> entry : params.entrySet()) {
                            encodedParams.append(URLEncoder.encode(entry.getKey(),paramsEncoding));
                            encodedParams.append('=');
                            encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                            encodedParams.append('&');
                        }
                        return encodedParams.toString().getBytes(paramsEncoding);
                    } catch (UnsupportedEncodingException uee) {
                        throw new RuntimeException("Encoding not supported: "+ paramsEncoding, uee);
                    }
                }

            };

            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(25000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsObjRequest);
            //jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String constructUrlParameters(HashMap<String, String> params) {
        StringBuilder encodedParams = new StringBuilder("?");
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), "UTF-8").replace("+", "%20"));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), "UTF-8").replace("+", "%20"));
                encodedParams.append('&');
            }

            // Remove the trailing '&' character
            if (encodedParams.length() > 1) {
                encodedParams.setLength(encodedParams.length() - 1);
            }

            // URL decode the string
            String decodedParams = URLDecoder.decode(encodedParams.toString(), StandardCharsets.UTF_8.name());
            return decodedParams;
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + "UTF-8", uee);
        }
    }

    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            JSONArray errors = data.getJSONArray("errors");
            JSONObject jsonMessage = errors.getJSONObject(0);
            String message = jsonMessage.getString("message");
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            Log.v("Diamond","errors : "+errors);
            Log.v("Diamond","message : "+message);
        } catch (Exception e) {
        }
    }
}
