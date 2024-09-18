package com.diamondxe.Network;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.diamondxe.Interface.JsonResponce;
import com.diamondxe.Utils.CommonUtility;


public abstract class SuperFragment extends Fragment implements JsonResponce, View.OnClickListener {

	public Context _context;
	NetworkConsumer _networkcomsumer;


	public SuperFragment() {

		_networkcomsumer = new NetworkConsumer(getActivity());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		//View view = inflater.inflate(R.layout.fragment_description, container, false);


		//return view;
		return null;
	}


	public NetworkConsumer networkConsumerInstance() {
		return _networkcomsumer;
	}


	public void showToast(String message) {
		Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
	}

	public void showLog(String message) {
		Log.v("tushar", ""+message);
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
			Log.v("tushar","getHeight 1 : "+getStatusBarHeight());

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
		getActivity().finish();*/
	}
}
