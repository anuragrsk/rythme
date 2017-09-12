package com.dk.app.isav;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.dk.app.isav.adapter.AppDataAdapter;
import com.dk.rhythm.R;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

public class AppList extends Activity {
	ArrayList<AppInfoData> infoData= null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_list);
		infoData= new ArrayList<AppInfoData>();
		Date todayDate = new Date();
		String date=SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG,Locale.US).format(todayDate);
		String dateOnly=SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT,Locale.US).format(todayDate);
		AppInfoData hss=(AppInfoData) AppData.getAppInstance().getDataForDay().get(dateOnly+"_"+AppData.APP_APPLICATION_DATA);
		Long timeSpendtillNow=(Long) AppData.getAppInstance().getDataForDay().get(dateOnly+"_"+AppData.APP_APPLICATION_TIME_SPEND);
		if(hss!=null){

			HashMap<String,AppInfoData> listMap = new HashMap<String,AppInfoData>();
			getLsit(hss,listMap);

			if(!listMap.isEmpty() ){
				Log.i("Hashmap:",listMap.size()+"");
				Set keys = listMap.keySet();
				Iterator iterator=keys.iterator();
				while(iterator.hasNext()){
					String key = (String) iterator.next();

					AppInfoData data = listMap.get(key);
					Log.i("Key",key+data.timeSpendThisTime);
					infoData.add(data);
				}
			}
			Collections.sort(infoData,new Comparator<AppInfoData>(){

				@Override
				public int compare(AppInfoData lhs, AppInfoData rhs) {
					// TODO Auto-generated method stub
					return (int) (rhs.timeSpendThisTime-lhs.timeSpendThisTime);
				}

			});
//			Log.i("AppList::",infoData.size()+"");
		if(checkForPermission(this)) {

//			infoData = getLollipopFGAppPackageName(this);
//			Collections.sort(infoData, new Comparator<AppInfoData>() {
//				@Override
//				public int compare(AppInfoData appInfoData, AppInfoData t1) {
//					return ()t1.timeSpendThisTime-appInfoData.timeSpendThisTime;
//				}
//			});
//			ListView list = (ListView) findViewById(R.id.tabitem);
//			AppDataAdapter aap = new AppDataAdapter(this, R.id.tabitem, infoData);
//			list.setAdapter(aap);
		}else{
			startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

		}
		}
		
		
	}
	private boolean checkForPermission(Context context) {
		AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
		PackageManager packageManager = context.getPackageManager();
		ApplicationInfo applicationInfo=null;

		try {
			 applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}


		int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, applicationInfo.uid, context.getPackageName());
		return mode == MODE_ALLOWED;
	}
	private ArrayList<AppInfoData> getLollipopFGAppPackageName(Context ctx) {

		try {
			UsageStatsManager usageStatsManager = (UsageStatsManager) ctx.getSystemService(Context.USAGE_STATS_SERVICE);
			long milliSecs = 10 * 1000;
			Date dateToday = new Date();

			AppData appData = AppData.getAppInstance();
			Date todayDate = new Date();
			String date = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG,
					Locale.US).format(todayDate);
			String dateOnly = SimpleDateFormat.getDateInstance(
					SimpleDateFormat.SHORT, Locale.US).format(todayDate);
			//List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, dateToday.getTime() - milliSecs, dateToday.getTime());
			long time = System.currentTimeMillis();
			Log.d("AppList", String.valueOf(new Date((time-79200*1000))));
			Log.d("AppList", String.valueOf(new Date((time))));
			// We get usage stats for the last 10 seconds
			Map<String,UsageStats> queryUsageStats = usageStatsManager.queryAndAggregateUsageStats( time-79200*1000, time);
			if (queryUsageStats.size() > 0) {
				Log.i("LPU", "queryUsageStats size: " + queryUsageStats.size());
			}
			long recentTime = 0;
			String recentPkg = "";
			AppInfoData dataApp =null;
			Set set=queryUsageStats.keySet();
			Iterator i = set.iterator();
			while(i.hasNext()){
				String key= (String) i.next();
				Log.d(this.getClass().getName(), key + "Package Name:"+queryUsageStats.get(key).getTotalTimeInForeground());
				if(queryUsageStats.get(key).getTotalTimeInForeground()>0) {
					AppInfoData dataAppNext = new AppInfoData(key,
							queryUsageStats.get(key).getTotalTimeInForeground());
					infoData.add(dataAppNext);
				}
			}

//			for (int i = 0; i < queryUsageStats.size(); i++) {
//				UsageStats stats = queryUsageStats.get(i);
//				if(stats!=null && stats.getTotalTimeInForeground()>0) {
//					Log.d(this.getClass().getName(), stats.getTotalTimeInForeground() + "Package Name:"+stats.getPackageName()+"Content"+stats.describeContents());
//
//
//					AppInfoData dataAppNext = new AppInfoData(stats.getPackageName(),
//							stats.getTotalTimeInForeground());
//					infoData.add(dataAppNext);
//				}
//			}
			return infoData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infoData;
	}

	private void getLsit(AppInfoData hss,HashMap<String,AppInfoData> hs) {
		if(hss!=null){
			if(hs.containsKey(hss.appName)){
				
				AppInfoData previousAppData =hs.get(hss.appName);
				long timeSpendPreviously=0;
				if(hss.endDateTime!=null)
					timeSpendPreviously =hss.endDateTime.getTime()-hss.startDateTime.getTime();
				//long timeSpendThisTime = hss.endDateTime.getTime()-hss.startDateTime.getTime();
				Log.i("timeSpendThisTime"," :timeSpendPreviously:"+timeSpendPreviously);
				if(timeSpendPreviously>0)
				previousAppData.timeSpendThisTime=previousAppData.timeSpendThisTime+timeSpendPreviously;
				Log.i("App from hashmap:",hss.appName+" timeSpendThisTime after"+hss.timeSpendThisTime);
				hs.put(hss.appName, previousAppData);
			}else{
				Log.i("putting App In hashmap:",hss.appName);
				if (hss.endDateTime != null) {
					long timeSpendThisTime = hss.endDateTime.getTime()
							- hss.startDateTime.getTime();
					hss.timeSpendThisTime = timeSpendThisTime;
				}
				hs.put(hss.appName, hss);
			}
			
			if(hss.pastApp!=null){
				Log.i("Past App Is:",hss.pastApp.appName+" This app:"+hss.appName);
				getLsit(hss.pastApp,hs);	
			}
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.app_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
