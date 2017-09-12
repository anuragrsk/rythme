package com.dk.app.isav.restservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.dk.app.isav.dto.Result;

public class RestServiceCall {
	private static String CLASS_NAME = RestServiceCall.class.getSimpleName();
	public static final String URL = "http://192.168.0.6:8080/AppBeat/";
	private static ArrayList<NameValuePair> headers = new ArrayList();
	private String message;
	private ArrayList<NameValuePair> params = new ArrayList();
	private String response;
	private int responseCode;
	private String url;

	public RestServiceCall(String paramString) {
		this.url = paramString;
		if (!isAdded())
			headers.add(new BasicNameValuePair("CUSTOM-USER-AGENT", "ANDROID"));
	}

	private static String convertStreamToString(InputStream paramInputStream) {
		BufferedReader localBufferedReader = new BufferedReader(
				new InputStreamReader(paramInputStream));
		StringBuilder localStringBuilder = new StringBuilder();
		try {
			while (true) {
				String str = localBufferedReader.readLine();
				if (str == null)
					break;
				localStringBuilder.append(str + "\n");
			}
		} catch (IOException localIOException2) {
			localIOException2 = localIOException2;
			localIOException2.printStackTrace();
			try {
				paramInputStream.close();
				while (true) {
					return localStringBuilder.toString();

				}
			} catch (IOException localIOException3) {
				while (true)
					localIOException3.printStackTrace();
			}
		} finally {
		}
		try {
			paramInputStream.close();
			// throw localObject;
		} catch (IOException localIOException1) {
			while (true)
				localIOException1.printStackTrace();
		}
		return localStringBuilder.toString();
	}

	private void executeRequest(HttpUriRequest paramHttpUriRequest,
			String paramString) {
		DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
		paramHttpUriRequest.getURI();
		try {
			HttpResponse localHttpResponse = localDefaultHttpClient
					.execute(paramHttpUriRequest);
			this.responseCode = localHttpResponse.getStatusLine()
					.getStatusCode();
			this.message = localHttpResponse.getStatusLine().getReasonPhrase();
			HttpEntity localHttpEntity = localHttpResponse.getEntity();
			if (localHttpEntity != null) {
				InputStream localInputStream = localHttpEntity.getContent();
				this.response = convertStreamToString(localInputStream);
				localInputStream.close();
			}
			return;
		} catch (ClientProtocolException localClientProtocolException) {
			localDefaultHttpClient.getConnectionManager().shutdown();
			localClientProtocolException.printStackTrace();
			return;
		} catch (IOException localIOException) {
			localDefaultHttpClient.getConnectionManager().shutdown();
			localIOException.printStackTrace();
		}
	}

	public static String getSessionID() {
		String str1 = "";
		Iterator localIterator = headers.iterator();
		while (localIterator.hasNext()) {
			NameValuePair localNameValuePair = (NameValuePair) localIterator
					.next();
			String str2 = localNameValuePair.getName();
			if ((str2 != null) && ("SessionID".equals(str2)))
				str1 = localNameValuePair.getValue();
		}
		return str1;
	}

	private boolean isAdded() {
		Iterator localIterator = headers.iterator();
		while (localIterator.hasNext())
			if ("CUSTOM-USER-AGENT".equals(((NameValuePair) localIterator
					.next()).getName()))
				return true;
		return false;
	}

	public static void resetSession() {
		if ((headers != null) && (!headers.isEmpty())) {
			Log.d(CLASS_NAME, Arrays.toString(headers.toArray()));
			headers.clear();
			return;
		}
		Log.d(CLASS_NAME, headers + " is null");
	}

	public static void setSessionInfo(String paramString) {
		headers = new ArrayList();
		headers.add(new BasicNameValuePair("SID", paramString));
	}

	public void addParam(String paramString1, String paramString2) {
		this.params.add(new BasicNameValuePair(paramString1, paramString2));
	}

	public Result callService(Context paramContext,
			RequestMethod paramRequestMethod) {
		Log.d("RestServiceCall", this.url);
		Log.d("RestServiceCall Params", Arrays.toString(this.params.toArray()));
		Log.d("RestServiceCall Headers", Arrays.toString(headers.toArray()));
		Result localResult = new Result();
		if (isOnline(paramContext))
			;

		try {
			execute(paramRequestMethod);
			String str1 = getResponse();
			if (str1 != null) {
				Log.d("RestServiceCall", str1);
				String str2 = getErrorMessage();
				int i = getResponseCode();
				Log.d("RestServiceCall::code", i + "");
				if (i == 200) {
					localResult.setSuccess(true);
					localResult.setSuccessMessage(str1);
					Log.d("RestServiceCall", localResult.isSuccess() + "");
					Log.d("RestServiceCall", localResult.toString());
					return localResult;
				}
				localResult.setSuccess(false);
				Log.d("RestServiceCall", str2);
				localResult
				.setFailureMessage("We are unable to process your request. Please try again."
						+ str2);

			}
		} catch (Exception localException) {
			localException.printStackTrace();
			Log.d("ERROR", localException.getLocalizedMessage());
			localResult.setSuccess(false);
			localResult
			.setFailureMessage("We are unable to process your request. Please try again.");
			// continue;
			localResult.setSuccess(false);
			localResult
			.setFailureMessage("We are unable to process your request.Please check your internet connection.");

			localResult.setSuccess(false);
			localResult
			.setFailureMessage("We are unable to process your request.Please check your internet connection.");
		}
		return localResult;
	}

	public Result callService(RequestMethod paramRequestMethod) {
		Log.d("RestServiceCall", this.url);
		Log.d("RestServiceCall", paramRequestMethod.toString());
		Log.d("RestServiceCall Params", Arrays.toString(this.params.toArray()));
		Log.d("RestServiceCall Headers", Arrays.toString(headers.toArray()));
		Result localResult = new Result();
		try {
			execute(paramRequestMethod);
			String str1 = getResponse();
			Log.d("RestServiceCall", str1);
			String str2 = getErrorMessage();
			int i = getResponseCode();
			Log.d("RestServiceCall::code", i + "");
			if (i == 200) {
				localResult.setSuccess(true);
				localResult.setSuccessMessage(str1);
				Log.d("RestServiceCall sucess", str1);
			}
			while (true) {
				Log.d("RestServiceCall", localResult.isSuccess() + "");
				Log.d("RestServiceCall", localResult.toString());
				// return localResult;
				localResult.setSuccess(false);
				Log.d("RestServiceCall", str2);
				localResult
				.setFailureMessage("We are unable to process your request. Please try again."
						+ str2);
			}
		} catch (Exception localException) {
			while (true) {
				localException.printStackTrace();
				Log.d("ERROR", localException.getLocalizedMessage());
				localResult.setSuccess(false);
				localResult
				.setFailureMessage("We are unable to process your request. Please try again.");
			}
		}
	}

	public void execute(RequestMethod paramRequestMethod) throws Exception {
		switch (paramRequestMethod) {
		default:
			return;
		case GET:
			String str1 = "";
			if (!this.params.isEmpty()) {
				str1 = str1 + "";
				Iterator localIterator4 = this.params.iterator();
				while (localIterator4.hasNext()) {
					NameValuePair localNameValuePair4 = (NameValuePair) localIterator4
							.next();
					String str2 = localNameValuePair4.getName() + "="
							+ localNameValuePair4.getValue();
					if (str1.length() > 1)
						str1 = str1 + "&" + str2;
					else
						str1 = str1 + str2;
				}
			}
			HttpGet localHttpGet = new HttpGet(this.url + str1);
			Iterator localIterator3 = headers.iterator();
			while (localIterator3.hasNext()) {
				NameValuePair localNameValuePair3 = (NameValuePair) localIterator3
						.next();
				localHttpGet.addHeader(localNameValuePair3.getName(),
						localNameValuePair3.getValue());
			}
			executeRequest(localHttpGet, this.url);
			return;
		case POST:
			HttpPost localHttpPost = new HttpPost(this.url);
			Iterator localIterator2 = headers.iterator();
			while (localIterator2.hasNext()) {
				NameValuePair localNameValuePair2 = (NameValuePair) localIterator2
						.next();
				localHttpPost.addHeader(localNameValuePair2.getName(),
						localNameValuePair2.getValue());
			}
			if (!this.params.isEmpty())
				localHttpPost.setEntity(new UrlEncodedFormEntity(this.params,
						"UTF-8"));
			executeRequest(localHttpPost, this.url);
			return;
		case DELETE:
		}
		HttpDelete localHttpDelete = new HttpDelete(this.url);
		Iterator localIterator1 = headers.iterator();
		while (localIterator1.hasNext()) {
			NameValuePair localNameValuePair1 = (NameValuePair) localIterator1
					.next();
			localHttpDelete.addHeader(localNameValuePair1.getName(),
					localNameValuePair1.getValue());
		}
		executeRequest(localHttpDelete, this.url);
	}

	public String getErrorMessage() {
		return this.message;
	}

	public String getResponse() {
		return this.response;
	}

	public int getResponseCode() {
		return this.responseCode;
	}

	public String getUrl() {
		return this.url;
	}

	public boolean isOnline(Context paramContext) {
		NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
				.getSystemService("connectivity")).getActiveNetworkInfo();
		return (localNetworkInfo != null)
				&& (localNetworkInfo.isConnectedOrConnecting());
	}

	public void setUrl(String paramString) {
		this.url = paramString;
	}

	public static enum RequestMethod {
		GET, POST, DELETE

	}

	public static void main(String arg[]) {

		RestServiceCall client = new RestServiceCall("getData.jsp");
		Result result = client.callService(RequestMethod.GET);
		if (result.isSuccess()) {
			Log.i("Call", result.getSuccessMessage());
		}
	}
}