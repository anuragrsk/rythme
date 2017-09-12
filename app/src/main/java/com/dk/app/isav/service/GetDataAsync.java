
package com.dk.app.isav.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.dk.app.isav.AppInfoData;
import com.dk.app.isav.dto.Result;
import com.dk.app.isav.restservice.ProcessResponse;
import com.dk.app.isav.restservice.RestServiceCall;
import com.dk.app.isav.restservice.RestServiceCall.RequestMethod;
import com.dk.app.isav.util.DateUtil;
import com.google.gson.Gson;

import dto.AppDataArray;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class GetDataAsync extends AsyncTask<ArrayList<NameValuePair>, Void, Result> {
	  private static final String CLASS_NAME = null;
	private Context context;
	private String methodName;
public GetDataAsync(Context context2,String method ){
	this.context=context2;
	this.methodName=method;
}
	protected void onPreExecute(){
		Log.i("CallWebServiceAsync","CallWebServiceAsync:onPreExecute");
	}
	@Override
	protected Result doInBackground(ArrayList<NameValuePair>... params) {
		

		return doTask(params);
	}

	private Result doTask(ArrayList<NameValuePair>... params) {
		Log.i("CallWebServiceAsync", "Start");
		try {
			if (params != null) {
				ArrayList<NameValuePair>[] info = params;
				if (info != null && info.length > 0) {
					ArrayList<NameValuePair> all = info[0];
					Gson gson = new Gson();
					String out = gson.toJson(all);
					Log.d("CallWebServiceAsync", out + " HERE JSON NO");
					final HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams
							.setConnectionTimeout(httpParams, 30000);
					HttpConnectionParams.setSoTimeout(httpParams, 10);
					DefaultHttpClient httpClient = new DefaultHttpClient(
							httpParams);

					HttpPost postRequest = new HttpPost(
							"http://192.168.0.6:8080/AppBeat/test.jsp");
					RestServiceCall client = new RestServiceCall(
							RestServiceCall.URL + "/test.jsp");
					Result result = client.callService(context,
							RequestMethod.POST);
					if (result.isSuccess()) {
						Log.i("Call", result.getSuccessMessage());
						AppDataArray arrayData = (AppDataArray) ProcessResponse
								.process(result.getSuccessMessage(),
										new AppDataArray());
						if (arrayData != null && arrayData.getAppData() != null
								&& arrayData.getAppData().length > 0) {
							AppInfoData[] dataEle = arrayData.getAppData();
							for (int i = 0; i < arrayData.getAppData().length; i++) {
								AppInfoData d = dataEle[i];
								Log.i("CALLED", d.toString());
							}

						}
					}
				}
			}
		} catch (Exception el) {
			el.printStackTrace();
		}

		return null;
	}
	
	protected void onPostExecute(Result paramResult) {
        Log.d("CallWebServiceAsync","CallWebServiceAsync:onPostExecute");
        try
        {
        	Class[] arrayOfClass = new Class[1];
        	arrayOfClass[0] = paramResult.getClass();
        	Log.d(CLASS_NAME, arrayOfClass.toString());
        	Object[] arrayOfObject = { paramResult };
        	Method[] arrayOfMethod = this.context.getClass().getDeclaredMethods();
        	Result   localResult = (Result)this.context.getClass().getMethod(this.methodName, arrayOfClass).invoke(this.context.getClass().newInstance(), arrayOfObject);
        }catch (Exception localException){
        	Log.i(CLASS_NAME,localException.getLocalizedMessage()+"Hello ");
        	localException.printStackTrace();
        }
    }

	
}
