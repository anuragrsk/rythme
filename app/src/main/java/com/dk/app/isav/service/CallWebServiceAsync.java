package com.dk.app.isav.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import com.dk.app.isav.util.DateUtil;
import com.google.gson.Gson;

import android.os.AsyncTask;
import android.util.Log;

public class CallWebServiceAsync extends AsyncTask<ArrayList<AppInfoData>, Void, Result> {
	protected void onPreExecute(){
		Log.i("CallWebServiceAsync","CallWebServiceAsync:onPreExecute");
	}
	@Override
	protected Result doInBackground(ArrayList<AppInfoData>... params) {
		

		return doTask(params);
	}

	private Result doTask(ArrayList<AppInfoData>... params) {
		Log.i("CallWebServiceAsync","Start");
		if(params!=null){
			ArrayList<AppInfoData>[] info = params;
			if(info!=null && info.length>0){
				ArrayList<AppInfoData> all= info[0];
				Gson gson= new Gson();
				String out=gson.toJson(all);
				Log.d("CallWebServiceAsync", out+" HERE JSON NO");
				final HttpParams httpParams = new BasicHttpParams();
			    HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
			    HttpConnectionParams.setSoTimeout(httpParams, 10);
				DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);  
				
				HttpPost postRequest = new HttpPost("http://192.168.0.6:8080/AppBeat/test.jsp");
			//	postRequest.setHeader("Content-Type", "application/vnd.emc.documentum+json; charset=utf-8");  

				//convert the object to json using gson  
				
				try {
					postRequest.setEntity(new StringEntity(gson.toJson(all)));
				//	postRequest.setHeader("json", gson.toJson(all));;
					//postRequest.getE
					//do the POST
					ArrayList<NameValuePair> arr = new ArrayList<NameValuePair>();
				
					for(int i=0;i<all.size();i++){
						AppInfoData data =all.get(i);
						NameValuePair userName = new BasicNameValuePair("userName", "anuragdehra@yahoo.com");
						NameValuePair appName = new BasicNameValuePair("appName", data.appName);
						NameValuePair endDateTime = new BasicNameValuePair("endDateTime", DateUtil.getDateTimeMySQL(data.endDateTime));
						NameValuePair startDateTime = new BasicNameValuePair("startDateTime", DateUtil.getDateTimeMySQL(data.startDateTime));
						NameValuePair timeSpend = new BasicNameValuePair("timeSpend", (data.endDateTime.getTime()-data.startDateTime.getTime())/60*1000+"");
						NameValuePair createdOn = new BasicNameValuePair("createdOn", new SimpleDateFormat ("yyyy-MM-dd",Locale.US).format(new Date()));
						NameValuePair draw = new BasicNameValuePair("draw", data.draw);
//						NameValuePair appName = new BasicNameValuePair("appName", data.appName);
						arr.add(userName);	
						arr.add(appName);
						arr.add(draw);		
						arr.add(startDateTime);
						arr.add(endDateTime);
						arr.add(timeSpend);
						arr.add(createdOn);
					}
					
				//	arr.add(pair1);
					postRequest.setEntity(new UrlEncodedFormEntity(arr, "UTF-8"));
					
					HttpResponse postResponse = httpClient.execute(postRequest);  
					
				      int responseCode = postResponse.getStatusLine().getStatusCode();
				      String messageReason = postResponse.getStatusLine().getReasonPhrase();
				      Log.i("CallWebServiceAsync",responseCode+" "+messageReason);
					if(responseCode==200){
						
					}else{
						
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  catch(Exception el){
					el.printStackTrace();
				}
				  
				
			}
		}
		
		return null;
	}
	
	protected void onPostExecute(Long result) {
        Log.d("CallWebServiceAsync","CallWebServiceAsync:onPostExecute");
    }


}
