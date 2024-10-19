package com.diamondxe.Utils;

import static com.diamondxe.Utils.PaymentUtils.API_END_POINT;
import static com.diamondxe.Utils.PaymentUtils.MERCHANT_ID;
import static com.diamondxe.Utils.PaymentUtils.SALT;
import static com.diamondxe.Utils.PaymentUtils.SALT_INDEX;
import static com.diamondxe.Utils.PaymentUtils.TARGET_URL;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.util.Consumer;

import com.diamondxe.Beans.GiftImageModel;
import com.diamondxe.Interface.DialogItemClickInterface;
import com.diamondxe.Interface.PaymentResultCallback;
import com.diamondxe.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.phonepe.intent.sdk.api.B2BPGRequest;
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder;
import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.PhonePeInitException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
public class CommonUtility {

	private static final long SiXTY_FIVE_MINUTES_IN_MILLIS = 65 * 60 * 1000L; // 65 minutes in milliseconds
	private static final long CHECK_INTERVAL = 1000L; // 1 second interval, // The 'L' suffix indicates that this is a long literal
	static Handler handler = new Handler();

	static CountDownTimer countDownTimer;
	private long timeRemainingMillis; // Store remaining time in milliseconds

	// set app level string global value
	public static final boolean setGlobalString(Context context,final String key, final String value) {
		SharedPreferences sharedPref = context.getSharedPreferences("GLOBAL_PREFERENCE", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(key, value);
		editor.apply();

		return true;
	}

	public static void clear(Context context)
	{
		SharedPreferences prefs = context.getSharedPreferences("GLOBAL_PREFERENCE", Context.MODE_PRIVATE);

		String selectedCurrencyCode = CommonUtility.getGlobalString(context, "selected_currency_code");
		String selectedCurrencyValue = CommonUtility.getGlobalString(context, "selected_currency_value");
		String selectedCurrencyDesc = CommonUtility.getGlobalString(context, "selected_currency_desc");
		String selectedCurrencyImage = CommonUtility.getGlobalString(context, "selected_currency_image");

		String featuresTitle = CommonUtility.getGlobalString(context, "featuresTitle");
		String middleBannersTitle = CommonUtility.getGlobalString(context, "middleBannersTitle");
		String mediaTitle = CommonUtility.getGlobalString(context, "mediaTitle");

		String rememberEmail = CommonUtility.getGlobalString(context, "remember_email");
		String rememberPassword = CommonUtility.getGlobalString(context, "remember_password");

		CommonUtility.setGlobalString(context, "mobile_auth_token", "");
		CommonUtility.setGlobalString(context, "user_login", "");
		CommonUtility.setGlobalString(context, "uuid", "");

		//SharedPreferences prefs; // here you get your preferences by either of two methods
		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		//editor.commit();
		editor.apply();

		CommonUtility.setGlobalString(context, "selected_currency_code", selectedCurrencyCode);
		CommonUtility.setGlobalString(context, "selected_currency_value", selectedCurrencyValue);
		CommonUtility.setGlobalString(context, "selected_currency_desc", selectedCurrencyDesc);
		CommonUtility.setGlobalString(context, "selected_currency_image", selectedCurrencyImage);

		CommonUtility.setGlobalString(context, "featuresTitle", featuresTitle);
		CommonUtility.setGlobalString(context, "middleBannersTitle", middleBannersTitle);
		CommonUtility.setGlobalString(context, "mediaTitle", mediaTitle);

		CommonUtility.setGlobalString(context, "remember_email", rememberEmail);
		CommonUtility.setGlobalString(context, "remember_password", rememberPassword);

		CommonUtility.setGlobalString(context, "mobile_auth_token", "");
		CommonUtility.setGlobalString(context, "user_login", "");
		CommonUtility.setGlobalString(context, "uuid", "");
		editor.apply();

		//return true;
	}

	// get app level string global value
	public static final String getGlobalString(Activity activity,final String key) {
		SharedPreferences sharedPref = activity.getSharedPreferences("GLOBAL_PREFERENCE", Context.MODE_PRIVATE);
		return sharedPref.getString(key, "");
	}

	// get app level string global value
	public static final String getGlobalString(Context activity,
			final String key) {
		SharedPreferences sharedPref = activity.getSharedPreferences("GLOBAL_PREFERENCE", Context.MODE_PRIVATE);
		return sharedPref.getString(key, "");
	}



	// converts date string from its current source format to required target
	// format
	// "yyyy-MM-dd HH:mm:ss" to "MM/dd/yyyy hh:mm a"
	// "MM/dd/yy HH:mm:ss" to "MM/dd/yyyy hh:mm a"
	public static String convertDateFormat(String dateStr, String sourceFormat,String targetFormat) {

		dateStr = checkString(dateStr);

		if (dateStr.equals(""))
		{
			return "";
		}
		Log.d("date", dateStr + "---" + sourceFormat + "---" + targetFormat);
		// "yyyy-MM-dd'T'HH:mm:ss.SSS"
		SimpleDateFormat form = new SimpleDateFormat(sourceFormat);
		Date date = null;
		try {
			date = form.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SimpleDateFormat postFormater = new SimpleDateFormat(targetFormat);
		String newDateStr = postFormater.format(date);
		Log.d("Lead Response", newDateStr);
		return newDateStr;
	}


	// returns current date using format "yyyy-MM-dd HH:mm:ss"
	public static String getCurrentDateTime(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}


	public static String compressImage(String imageUri) {

		String filename = null;
		try {
			String filePath = imageUri;
			Bitmap scaledBitmap = null;

			BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
			options.inJustDecodeBounds = true;
			Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

			int actualHeight = options.outHeight;
			int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

			float maxHeight = 1200.0f;
			float maxWidth = 800.0f;
			float imgRatio = actualWidth / actualHeight;
			float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

			if (actualHeight > maxHeight || actualWidth > maxWidth) {
				if (imgRatio < maxRatio) {
					imgRatio = maxHeight / actualHeight;
					actualWidth = (int) (imgRatio * actualWidth);
					actualHeight = (int) maxHeight;
				} else if (imgRatio > maxRatio) {
					imgRatio = maxWidth / actualWidth;
					actualHeight = (int) (imgRatio * actualHeight);
					actualWidth = (int) maxWidth;
				} else {
					actualHeight = (int) maxHeight;
					actualWidth = (int) maxWidth;

				}
			}

//      setting inSampleSize value allows to load a scaled down version of the original image

			options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
			options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inTempStorage = new byte[16 * 1024];

			try {
				//          load the bitmap from its path
				bmp = BitmapFactory.decodeFile(filePath, options);
			} catch (OutOfMemoryError exception) {
				exception.printStackTrace();

			}
			try {
				scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
			} catch (OutOfMemoryError exception) {
				exception.printStackTrace();
			}

			float ratioX = actualWidth / (float) options.outWidth;
			float ratioY = actualHeight / (float) options.outHeight;
			float middleX = actualWidth / 2.0f;
			float middleY = actualHeight / 2.0f;

			Matrix scaleMatrix = new Matrix();
			scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

			Canvas canvas = new Canvas(scaledBitmap);
			canvas.setMatrix(scaleMatrix);
			canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
			ExifInterface exif;
			try {
				exif = new ExifInterface(filePath);

				int orientation = exif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION, 0);
				Log.d("EXIF", "Exif: " + orientation);
				Matrix matrix = new Matrix();
				if (orientation == 6) {
					matrix.postRotate(90);
					Log.d("EXIF", "Exif: " + orientation);
				} else if (orientation == 3) {
					matrix.postRotate(180);
					Log.d("EXIF", "Exif: " + orientation);
				} else if (orientation == 8) {
					matrix.postRotate(270);
					Log.d("EXIF", "Exif: " + orientation);
				}
				scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
						scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
						true);
			} catch (IOException e) {
				e.printStackTrace();
			}

			FileOutputStream out = null;
			filename = getFilename();
			try {
				out = new FileOutputStream(filename);

				//          write the compressed bitmap at the destination specified by filename.
				scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return filename;

	}

	public static String getFilename()
	{
		String uriSting = null;
		try {
			File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
			if (!file.exists()) {
				file.mkdirs();
			}
			uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uriSting;

	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
	{
		int inSampleSize = 0;
		try {
			final int height = options.outHeight;
			final int width = options.outWidth;
			inSampleSize = 1;

			if (height > reqHeight || width > reqWidth) {
				final int heightRatio = Math.round((float) height / (float) reqHeight);
				final int widthRatio = Math.round((float) width / (float) reqWidth);
				inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			}
			final float totalPixels = width * height;
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;
			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return inSampleSize;
	}

	//For Converting Time from UTC to Local
	public static String convertDateTimeIntoLocal(String send_time, String sourceFormat, String targetFormat) {

		if (sourceFormat.equalsIgnoreCase("dd-MM-yy") || sourceFormat.equalsIgnoreCase("dd-MM-yyyy") || sourceFormat.equalsIgnoreCase("MM-dd-yy") || sourceFormat.equalsIgnoreCase("MM-dd-yyyy")
				|| sourceFormat.equalsIgnoreCase("yyyy-MM-dd") || sourceFormat.equalsIgnoreCase("yy-MM-dd")) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			String currentTime = "" + dateFormat.format(new Date());

			send_time = send_time + " " + currentTime;
			sourceFormat = sourceFormat + " HH:mm:ss";
		}

		if (sourceFormat.equalsIgnoreCase("HH:mm:ss") || sourceFormat.equalsIgnoreCase("hh:mm a")) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			String currentDate = "" + dateFormat.format(new Date());

			send_time = currentDate + " " + send_time;
			sourceFormat = "dd-MM-yyyy " + sourceFormat;
		}


		String formattedDate = "";

		try {
			//  "2018-07-12 13:41:29"
			// String dateStr = "Jul 16, 2013 12:08:59 AM";
			String dateStr = "" + send_time;
			// SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a", Locale.ENGLISH);
			//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			SimpleDateFormat df = new SimpleDateFormat("" + sourceFormat, Locale.ENGLISH);
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date = df.parse(dateStr);
			df.setTimeZone(TimeZone.getDefault());
			formattedDate = df.format(date);

			SimpleDateFormat targetFormate = new SimpleDateFormat("" + targetFormat, Locale.ENGLISH);
			targetFormate.setTimeZone(TimeZone.getDefault());
			formattedDate = targetFormate.format(date);

			//Log.v("Diamond", "convertDateTimeIntoLocal : " + formattedDate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return formattedDate;
	}

	public static String getFileExtension(String url) {
		try {
			URI uri = new URI(url);
			String path = uri.getPath();
			int dotIndex = path.lastIndexOf('.');
			if (dotIndex != -1 && dotIndex < path.length() - 1) {
				return path.substring(dotIndex);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static double checkDouble(String value) {
		double updatedValue = 0.0; // Default value for invalid inputs

		if (value == null || value.trim().equalsIgnoreCase("") || value.equalsIgnoreCase("null")) {
			updatedValue = 0.0; // Return default value
		} else {
			try {
				updatedValue = Double.parseDouble(value); // Parse the value as double
			} catch (NumberFormatException e) {
				updatedValue = 0.0; // Handle the case where value is not a valid double
			}
		}

		return updatedValue;
	}

	public static String checkString(String value)
	{
		String updatedValue = "";
		if(value==null)
		{
			updatedValue = "";
		}
		else if(value.trim().equalsIgnoreCase(""))
		{
			updatedValue = "";
		}
		else if (value.equalsIgnoreCase("null"))
		{
			updatedValue = "";
		}else{
			updatedValue = value;
		}

		return updatedValue;
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPath(final Context context, final Uri uri)
	{
		final boolean isKitKatOrAbove = Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];


//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
				return getDirectory("SECONDARY_STORAGE", "/sdcard") + "/" + split[1];

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] {
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;
	}


	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
	public static File getDirectory(String variableName, String defaultPath) {
		String path = System.getenv(variableName);
		return path == null ? new File(defaultPath) : new File(path);
	}
	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}


	//For Getting Date OBJ from string
	public static Date getDateFromDateStringFormat(String dateStr, String sourceFormat) {

		// "yyyy-MM-dd'T'HH:mm:ss.SSS"
		SimpleDateFormat form = new SimpleDateFormat(sourceFormat);
		Date date = null;
		try {
			date = form.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return date;
	}

	public static long convertDateToLong(String dateStr, String sourceFormat)
	{
		long startDate = 0;
		try {
			String dateString = dateStr;
			SimpleDateFormat sdf = new SimpleDateFormat(sourceFormat);
			Date date = sdf.parse(dateString);

			startDate = date.getTime();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return startDate;
	}


	public static String deviceID(Context context)
	{
		return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
	}


	//Create a File for saving an image or video
	private static File getOutputMediaFile(Activity activity) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.
		//File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + activity.getPackageName() + "/Files");

		// Create a media file name
		String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
		File mediaFile;
		String mImageName = "MI_" + timeStamp + ".png";


		File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+"/.Eleaders");

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists())
		{
			mediaStorageDir.mkdirs();
			if (!mediaStorageDir.exists())
			{
				return null;
			}
		}

		mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);

		return mediaFile;
	}

	public static BottomSheetDialog dialog;


	public static int cu_year,cu_month,cu_mDay;
	public static void datePicker(Context context, final DialogItemClickInterface dialogItemClickInterface,final String date1,final String valueFor,long minDate,long maxDate)
	{
		// For Validate to Event Date
		if (date1!=null && !date1.trim().equalsIgnoreCase(""))
		{
			cu_mDay = Integer.parseInt(CommonUtility.convertDateFormat(date1,"MM/dd/yyyy","dd"));
			cu_month = Integer.parseInt(CommonUtility.convertDateFormat(date1,"MM/dd/yyyy","MM"));
			cu_year = Integer.parseInt(CommonUtility.convertDateFormat(date1,"MM/dd/yyyy","yyyy"));
			cu_month = cu_month-1;
		}else{
			Calendar cc = Calendar.getInstance();
			cu_year=cc.get(Calendar.YEAR);
			cu_month=cc.get(Calendar.MONTH);
			cu_mDay = cc.get(Calendar.DAY_OF_MONTH);

			//cu_month = cu_month-1;
		}

		DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				String input  =""+(monthOfYear + 01)+"-"+dayOfMonth+"-"+year;
				Date date = new Date();
				SimpleDateFormat inFormat = new SimpleDateFormat("MM-dd-yy");
				try {
					date = inFormat.parse(input);
				}catch (Exception e){}

				String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", date);//Thursday
				String stringMonth = (String) android.text.format.DateFormat.format("MMM", date); //Jun
				String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
				String year1 = (String) android.text.format.DateFormat.format("yyyy", date); //2013
				String day = (String) android.text.format.DateFormat.format("dd", date); //20

				dialogItemClickInterface.dialogItemClick(day+"/"+intMonth+"/"+year1,valueFor);

				Calendar cc = Calendar.getInstance();
				cu_year=cc.get(Calendar.YEAR);
				cu_month=cc.get(Calendar.MONTH);
				cu_mDay = cc.get(Calendar.DAY_OF_MONTH);

				cu_month = cu_month-1;

			}
		}, cu_year, cu_month, cu_mDay);
		if (minDate!=0)
		{
			datePickerDialog.getDatePicker().setMinDate(minDate);
		} else {}

		if (maxDate!=0)
		{
			datePickerDialog.getDatePicker().setMaxDate(maxDate);
		} else {}

		datePickerDialog.show();
	}

	public static String generateTransactionId() {
		Random random = new Random();
		int randomNumber = random.nextInt(999999); // Generate a number between 0 and 999999
		return String.format("TXN%06d", randomNumber); // Prefix with "TXN" and pad with zeros
	}

	public static String getAndroidId(Context context) {
		// Get the Android ID for the device
		//String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
		String androidId = "";

		androidId = CommonUtility.getGlobalString(context, "uuid");

		if(androidId.equalsIgnoreCase(""))
		{
			String uuid = UUID.randomUUID().toString();

			CommonUtility.setGlobalString(context, "uuid", uuid);
			androidId = CommonUtility.getGlobalString(context, "uuid");

		} else{}


		//Log.e("DeviceUniqueId", androidId);

		return androidId;
	}

	public static void forSendEmail(Context context)
	{
		// define Intent object with action attribute as ACTION_SEND
		Intent intent = new Intent(Intent.ACTION_SEND);
		// add three fields to intent using putExtra function
		intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@diamondxe.com"});
		intent.putExtra(Intent.EXTRA_SUBJECT, "");
		intent.putExtra(Intent.EXTRA_TEXT, "");
		// set type of intent
		intent.setType("message/rfc822");
		// startActivity with intent with chooser as Email client using createChooser function
		context.startActivity(Intent.createChooser(intent, "Choose an Email client :"));
	}

	public static void makeACallIntent(Context context)
	{
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:+919892003399"));
		context.startActivity(intent);
	}

	public static void askForConfirmation(final Context context, final DialogItemClickInterface dialogItemClickInterface, final String action, final String message)
	{

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		//alertDialogBuilder.setTitle("Exit Application?");
		alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								dialogItemClickInterface.dialogItemClick("Yes",action);
								dialog.dismiss();
							}
						})

				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						//dialogItemClickInterface.dialogItemClick("No",action);
						dialog.cancel();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public static boolean isValidPassword(final String password) {

		Pattern pattern;
		Matcher matcher;

		//final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8}$";
		final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!]).{8,130})";;

		pattern = Pattern.compile(PASSWORD_PATTERN);
		matcher = pattern.matcher(password);

		return matcher.matches();

	}

	public static String currencyFormat(String amount) {
		//DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
		//DecimalFormat formatter = new DecimalFormat("###,###,##");
		//DecimalFormat formatter = new DecimalFormat("#,##,###", DecimalFormatSymbols.getInstance(Locale.US));
		DecimalFormat formatter = new DecimalFormat("#,##,###");
		return formatter.format(Double.parseDouble(amount));
	}

	// Image Zoom-In Zoom-Out Animation
	public static void startZoomAnimation(ImageView imageView) {
		ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 1.3f);
		animator.setDuration(1000); // Duration for one complete zoom in and out cycle
		animator.setInterpolator(new AccelerateDecelerateInterpolator());
		animator.setRepeatCount(ValueAnimator.INFINITE); // Repeat indefinitely
		animator.setRepeatMode(ValueAnimator.REVERSE); // Reverse animation at the end of each cycle

		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float scale = (float) animation.getAnimatedValue();
				imageView.setScaleX(scale);
				imageView.setScaleY(scale);
			}
		});

		animator.start();
	}

	// Get Currency Symbol Like "USD" currency Code and return Symbol "$"
	public static String getCurrencySymbol(String currencyCode)
	{
		String getCurrencySymbol = "";
		Currency myCurrencyCode = Currency.getInstance(currencyCode);
		// Get the currency symbol
		getCurrencySymbol = myCurrencyCode.getSymbol(Locale.getDefault());

		return getCurrencySymbol;
	}

	public static String currencyConverter(String value, String currencyCode, String subTotalAmount)
	{
		double valueType;
		double currencyValue = Double.parseDouble(value);
		double subTotal = Double.parseDouble(subTotalAmount);
		String subTotalFormat = "";
		// currencyCode Not Equal INR Then Using INR Currency Value INT Value "1"
		if(currencyCode.equalsIgnoreCase("INR"))
		{
			valueType  = Double.parseDouble(value);
		}else{
			valueType = 1;
		}

		// Log.e("----Diamond--- : ", "----finalAmount11--- : " + currencyValue);
		double currencyValueType = currencyValue/valueType;
		// Log.e("----Diamond--- : ", "----finalAmount111--- : " + currencyValueType);
		double finalAmount = subTotal*currencyValueType;
		// Log.e("----Diamond--- : ", "----finalAmount1111--- : " + finalAmount);
		String finalSubTotal = String.valueOf(finalAmount);

		// Log.e("----Diamond--- : ", "----finalAmount11111--- : " + finalSubTotal);
		subTotalFormat = String.format("%.2f", finalAmount);

		return subTotalFormat;
		// Log.e("----Diamond--- : ", "----finalAmount11111--- : " + subTotalFormat);
	}

	public static void saveLocalDataList(Context context, String key, String json) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, json);
		editor.apply();
	}

	public static String getLocalDataList(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		String json = sharedPreferences.getString(key, null);
		return json;
	}

	public static void clearLocalArrayListData(Context context)
	{
		SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

		//SharedPreferences prefs; // here you get your prefrences by either of two methods
		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		//editor.commit();
		editor.apply();
		//return true;
	}

	public static void saveGiftChoiceImageList(Context context, ArrayList<GiftImageModel> giftImageArrayList) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		Gson gson = new Gson();
		String json = gson.toJson(giftImageArrayList);
		editor.putString("giftImageArrayList", json);
		editor.apply();
	}

	public static ArrayList<GiftImageModel> getGiftChoiceImageList(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		Gson gson = new Gson();
		String json = sharedPreferences.getString("giftImageArrayList", null);
		Type type = new TypeToken<ArrayList<GiftImageModel>>() {}.getType();
		return gson.fromJson(json, type);
	}

	// Use For Create PhonePe CheckSum
	public static String sha256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : hash) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static void createCheckSumPaymentInitiatedAndOpenPhonePe(
			Activity activity,
			String amountInPaisaString,
			String paymentModeType,
			String selectedUPIPackage,
			String bankID,
			String callbackUrl,
			PaymentResultCallback callback) {

		B2BPGRequest b2BPGRequest;

		JSONObject data = new JSONObject();
		try {
			// Convert the string to an integer value to ensure it's valid
			int amountInPaisa = Integer.parseInt(amountInPaisaString);

			data.put("merchantTransactionId", Constant.paymentOrderID); // String. Mandatory
			data.put("merchantId", MERCHANT_ID); // String. Mandatory
			data.put("merchantUserId", Constant.paymentUserID); // String. Mandatory
			data.put("amount", amountInPaisa * 100);
			data.put("mobileNumber", CommonUtility.getGlobalString(activity, "login_mobile_no")); // String. Optional
			data.put("callbackUrl", callbackUrl); // String. Mandatory

			// Logging the amount for verification
			Log.d("AmountConversion", "Amount in Paisa: " + amountInPaisa);

			JSONObject paymentInstrument = new JSONObject();

			// Check Payment Mode Type and Set Type.
			if (paymentModeType.equalsIgnoreCase("UPI")) {
				paymentInstrument.put("type", "PAYMENT_BY_UPI");
				paymentInstrument.put("targetApp", selectedUPIPackage);
			} else if (paymentModeType.equalsIgnoreCase("NET_BANKING")) {
				paymentInstrument.put("type", "PAYMENT_BY_NET_BANKING");
				paymentInstrument.put("bankId", bankID);
			} else if (paymentModeType.equalsIgnoreCase("CREDIT_CARD")) {
				paymentInstrument.put("type", "PAYMENT_BY_CARD");
			}

			data.put("paymentInstrument", paymentInstrument); // OBJECT. Mandatory

			JSONObject deviceContext = new JSONObject();
			deviceContext.put("deviceOS", "ANDROID");
			data.put("deviceContext", deviceContext);

			String payloadBase64 = Base64.encodeToString(data.toString().getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
			String checksum = CommonUtility.sha256(payloadBase64 + API_END_POINT + SALT) + "###" + SALT_INDEX;

			b2BPGRequest = new B2BPGRequestBuilder()
					.setData(payloadBase64)
					.setChecksum(checksum)
					.setUrl(API_END_POINT)
					.build();

			// Open PhonePe Here
			/*try {
				Intent intent1;
				if (selectedUPIPackage.equalsIgnoreCase("")) {
					intent1 = PhonePe.getImplicitIntent(activity, b2BPGRequest, TARGET_URL);
				} else {
					intent1 = PhonePe.getImplicitIntent(activity, b2BPGRequest, selectedUPIPackage);
				}
				if (intent1 != null) {
					Log.d("phonepe", "Intent created successfully");
					activity.startActivityForResult(intent1, 1);
				} else {
					Log.e("phonepe", "Intent is null");
				}
			} catch (PhonePeInitException e) {
				Log.e("phonepe", "PhonePe initialization error", e);
			}*/

			// Open PhonePe Here
			try {
				Intent intent1;
				if (selectedUPIPackage.equalsIgnoreCase("")) {
					intent1 = PhonePe.getImplicitIntent(activity, b2BPGRequest, TARGET_URL);
				} else {
					intent1 = PhonePe.getImplicitIntent(activity, b2BPGRequest, selectedUPIPackage);
				}
				if (intent1 != null) {
					Log.d("phonepe", "Intent created successfully");
					// Pass the callback to handle results
					/*if (activity instanceof PaymentResultCallback) {
						((PaymentResultCallback) activity).onPaymentResult(1, Activity.RESULT_OK, intent1);
					}*/
					//intentLauncher.accept(intent1);
					activity.startActivityForResult(intent1, 1);
				} else {
					Log.e("phonepe", "Intent is null");
				}
			} catch (PhonePeInitException e) {
				Log.e("phonepe", "PhonePe initialization error", e);
			}

		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("AmountConversion", "Invalid amount format");
		}
	}


	// Start the timer
	public static void startChecking(String createAtTime, CardView cardView) {
		Runnable checkRunnable = new Runnable() {
			@Override
			public void run() {
				if (shouldHideButton(createAtTime))
				{
					Log.e("-------Diamond------- : " , "Button Hide : " + createAtTime);
					cardView.setVisibility(View.GONE);
				} else {
					// Check again after CHECK_INTERVAL milliseconds
					Log.e("-------Diamond------- : " , "Button Not Hide : " + createAtTime);
					cardView.setVisibility(View.VISIBLE);
					handler.postDelayed(this, CHECK_INTERVAL);
				}
			}
		};
		handler.post(checkRunnable);
	}
	public static boolean shouldHideButton(String createAtTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		try {
			Date specifiedDate = dateFormat.parse(createAtTime);
			if (specifiedDate != null) {
				long specifiedTimeMillis = specifiedDate.getTime();
				long hideTimeMillis = specifiedTimeMillis + SiXTY_FIVE_MINUTES_IN_MILLIS;
				long currentTimeMillis = System.currentTimeMillis();
				Log.e("currentTimeMillis : ", "currentTimeMillis : "  + currentTimeMillis);
				return currentTimeMillis > hideTimeMillis;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void startTimerCheck(String dateTimeString, TextView textViewTimer, CardView cardViewCancel)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		// Parse the date-time string to a Date object
		try {
			Date bookingDate = dateFormat.parse(dateTimeString);

			if (bookingDate != null) {
				// Convert the Date object to milliseconds
				long bookingTimeMillis = bookingDate.getTime();
				long timerDurationMillis = 60 * 60 * 1000; // 60 minutes in milliseconds

				// Start the countdown timer
				startCountdownTimer(bookingTimeMillis, timerDurationMillis, textViewTimer, cardViewCancel);
			} else {
				// Handle the case where parsing failed
				//textViewTimer.setText("Invalid date format");
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static void startCountdownTimer(long startTimeMillis, long durationMillis, TextView textView, CardView cardView) {
		long currentTimeMillis = System.currentTimeMillis();
		long endTimeMillis = startTimeMillis + durationMillis;

		// Create a CountDownTimer to countdown until the end time
		countDownTimer = new CountDownTimer(endTimeMillis - currentTimeMillis, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				// Update the TextView with remaining time
				updateTimerTextView(millisUntilFinished, textView, cardView);
			}

			@Override
			public void onFinish() {
				// Timer finished
				//textView.setText("Time's up!");
				textView.setVisibility(View.GONE);
				cardView.setVisibility(View.GONE);
			}
		}.start();
	}

	public static void updateTimerTextView(long millisUntilFinished, TextView textView, CardView cardView) {
		// Convert milliseconds to minutes and seconds
		long minutes = (millisUntilFinished / 1000) / 60;
		long seconds = (millisUntilFinished / 1000) % 60;

		// Format the time and set it to the TextView
		String timeString = String.format("%02d:%02d", minutes, seconds);
		//String timeString = String.format("%02d Min:%02d Sec", minutes, seconds);
		textView.setText("(" +"Timer: " + timeString+ ")");
		textView.setVisibility(View.VISIBLE);
		cardView.setVisibility(View.VISIBLE);
	}

}
