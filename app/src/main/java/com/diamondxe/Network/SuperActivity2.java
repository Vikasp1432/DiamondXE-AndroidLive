package com.diamondxe.Network;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.diamondxe.Interface.JsonResponce;
import com.diamondxe.Utils.CommonUtility;


public abstract class SuperActivity2 extends AppCompatActivity implements JsonResponce, View.OnClickListener {

	public Context _context;
	NetworkConsumer _networkcomsumer;


	public SuperActivity2() {

		_networkcomsumer = new NetworkConsumer(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		//For Changing status bar color
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().setStatusBarColor(getResources().getColor(R.color.teal));
		}*/

		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			Drawable background = getResources().getDrawable(R.drawable.gradient_status_bar);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);//FLAG_TRANSLUCENT_NAVIGATION
			window.setStatusBarColor(getResources().getColor(R.color.transparent));
			window.setNavigationBarColor(getResources().getColor(R.color.black));
			window.setBackgroundDrawable(background);
		}*/

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			getWindow().setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
					WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}


	public NetworkConsumer networkConsumerInstance() {
		return _networkcomsumer;
	}


	public void showToast(String message) {
		Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();
	}

	public void showLog(String message) {
		Log.v("Diamond", ""+message);
	}



	public int getStatusBarHeight() {
		int result = 0;
		try {
			int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
			if (resourceId > 0) {
				result = getResources().getDimensionPixelSize(resourceId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public void setStatusBarHeight(TextView status_bar_tv) {

		try {
			//status_bar_tv.setHeight(getStatusBarHeight());

			ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) status_bar_tv.getLayoutParams();
			params.height = getStatusBarHeight();
			Log.v("Diamond","getHeight 1 : "+getStatusBarHeight());

			status_bar_tv.setLayoutParams(params);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void logoutFromApp(Context context,String message)
	{
		showToast(message);

		CommonUtility.setGlobalString(context, "user_id", "");
		CommonUtility.clear(context);

		/*Intent _intent = new Intent(context, SplashActivity.class);
		_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(_intent);
		finish();*/
	}




	public boolean isNetworkAvailable(Context _context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
