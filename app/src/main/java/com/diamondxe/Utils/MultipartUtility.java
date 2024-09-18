package com.diamondxe.Utils;
import android.content.Context;
import android.icu.util.TimeZone;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MultipartUtility {
	
	private final String boundary;
	private static final String LINE_FEED = "\r\n";
	private HttpURLConnection httpConn;
	private String charset;
	private OutputStream outputStream;
	private PrintWriter writer;
	Context context;

	/**
	 * This constructor initializes a new HTTP POST request with content type
	 * is set to multipart/form-data
	 * @param requestURL
	 * @param charset
	 * @throws IOException
	 */
	
	public MultipartUtility(String requestURL, String charset, Context con)
			throws IOException {
		
		Log.v("Diamond","MultipartUtility URL : "+requestURL);
		this.charset = charset;
		this.context = con;

		// creates a unique boundary based on time stamp
		boundary = "--" + System.currentTimeMillis() + "--";
		
		URL url = new URL(requestURL);
		httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setDoOutput(true);	// indicates POST method
		httpConn.setDoInput(true);
		//httpConn.setChunkedStreamingMode(1024);
		httpConn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + boundary);
		httpConn.addRequestProperty("apikey", "b8795c60-1400-4d70-b254-837a2a1da9e7");


		String uuid = CommonUtility.getAndroidId(context);

		String authToken = "", timeZoneId = "", timeZoneCountryCode="";
		// Get the default TimeZone ID
		timeZoneId = TimeZone.getDefault().getID();
		timeZoneCountryCode = TimeZoneCountryCodeMapper.getCountryCodeFromTimeZone(timeZoneId);

		// Use the country code as needed
		// Log.e("Country_Code_Using_TimeZone: " , timeZoneId);
		// Log.e("Country_Code_Using_TimeZone1: " , timeZoneCountryCode);

		authToken= CommonUtility.getGlobalString(context, "mobile_auth_token");

		if(authToken!=null && authToken!="")
		{
			httpConn.addRequestProperty("Authorization", "Bearer " + CommonUtility.getGlobalString(context, "mobile_auth_token"));
		}
		else{
			httpConn.addRequestProperty("Authorization", "");
		}
		httpConn.addRequestProperty("location", timeZoneCountryCode);
		httpConn.addRequestProperty("deviceId", uuid);
		httpConn.addRequestProperty("sessionId", uuid);

		outputStream = httpConn.getOutputStream();
		writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),true);
	}

	/**
	 * Adds a form field to the request
	 * @param name field name
	 * @param value field value
	 */
	public void addFormField(String name, String value)
	{
		Log.v("Diamond",""+name+":"+value);
		//Log.v("Diamond","name : "+name);
		//Log.v("Diamond","value : "+value);

	//	Log.e(name,value);
		writer.append("--" + boundary).append(LINE_FEED);
		writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE_FEED);
		writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
		writer.append(LINE_FEED);
		writer.append(value).append(LINE_FEED);
		writer.flush();
	}
	
	/**
	 * Adds a upload file section to the request 
	 * @param fieldName name attribute in <input type="file" name="..." />
//	 * @param uploadFile a File to be uploaded
	 * @throws IOException
	 */
	public void addFilePart(String fieldName, String filename)
			throws IOException {
		String fileName = filename;
	//	Log.e("fileNAME",filename);
		writer.append("--" + boundary).append(LINE_FEED);
		writer.append("Content-Disposition: form-data; name=\"" + fieldName
						+ "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
		writer.append("Content-Type: "
						+ URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
		writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
		writer.append(LINE_FEED);
		writer.flush();
		Log.v("fileName", fileName);
		FileInputStream inputStream = new FileInputStream(filename);
		//byte[] buffer = new byte[4096];
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		outputStream.flush();
		inputStream.close();
		writer.append(LINE_FEED);
		writer.flush();		
	}

	/**
	 * Completes the request and receives response from the server.
	 * @return a list of Strings as response in case the server returned
	 * status OK, otherwise an exception is thrown.
	 * @throws IOException
	 */
	
	public String finish() throws IOException {
		String response = null;
		Log.e("finish()","finish()");
		writer.append(LINE_FEED).flush();
		writer.append("--" + boundary + "--").append(LINE_FEED);
		writer.close();

		// checks server's status code first
		int status = httpConn.getResponseCode();
		if (status == HttpURLConnection.HTTP_OK) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				response = line;
				Log.v("Diamond","response : "+line);
			}
			reader.close();
			httpConn.disconnect();
		} else {
			Log.e("HTTP-ERROR",httpConn.toString());
			throw new IOException("Server returned non-OK status: " + status);
		}
		return response;
	}
}
