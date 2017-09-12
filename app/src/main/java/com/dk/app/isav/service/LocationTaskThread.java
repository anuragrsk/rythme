package com.dk.app.isav.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.dk.app.isav.AppData;
import com.dk.app.isav.AppInfoData;
import com.dk.app.isav.LocationData;
import com.dk.app.isav.MainActivity.ErrorDialogFragment;
import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationTaskThread implements Runnable,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		LocationListener


{
	private Context applicationContext;
	private GoogleApiClient mLocationClient;
	private Location location;
	private LocationRequest mLocationRequest;
	private boolean mUpdatesRequested, mResolvingError;
	private int timeCount = 0;
	private final String PHONE_SLEEP = "Phone Power Off";
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final int REQUEST_RESOLVE_ERROR = 1001;
	private static final int MILLISECONDS_PER_SECOND = 1000;
	// Update frequency in seconds
	public static final int UPDATE_INTERVAL_IN_SECONDS = 60 * 2;
	// Update frequency in milliseconds
	private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	// The fastest update frequency, in seconds
	private static final int FASTEST_INTERVAL_IN_SECONDS = 60;
	// A fast frequency ceiling in milliseconds
	private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND
			* FASTEST_INTERVAL_IN_SECONDS;
	private String name = "";

	public LocationTaskThread(Context appContext, int count) {
		// TODO Auto-generated constructor stub
		this.applicationContext = appContext;
		this.name = "THREAD:-" + count;
		if (servicesConnected() && count == 0) {
			mLocationClient = new GoogleApiClient.Builder(appContext)
					.addApi(LocationServices.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.build();
			mLocationRequest = LocationRequest.create();

			// Use high accuracy
			mLocationRequest
					.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			// Set the update interval to 5 seconds
			mLocationRequest.setInterval(UPDATE_INTERVAL);
			// Set the fastest update interval to 1 second
			mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		}
		this.timeCount = count;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		if (servicesConnected()) {
			location = arg0;
			processLocationData();
			Log.d("onLocationChanged", "Name is :" + name);
		}

		try {
			processActivity();
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (mResolvingError) {
			// Already attempting to resolve an error.
			return;
		} else if (result.hasResolution()) {
			mResolvingError = true;
		} else {
			// Show dialog using GooglePlayServicesUtil.getErrorDialog()
			mResolvingError = true;
		}


	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		Log.d("Lo Thread:onConnected:", "Satrt");

		try {
			processActivity();
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if (servicesConnected()) {
			if (mLocationClient != null)
				mLocationRequest = LocationRequest.create();
			mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			mLocationRequest.setInterval(60*1000); // Update location every second

			if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			LocationServices.FusedLocationApi.requestLocationUpdates(
					mLocationClient, mLocationRequest, this);
			Log.d("onConnected", "Name is :"+name);
			processLocationData();
			// Toast.makeText(applicationContext,
			// getDateTime()+((location!=null)?location.toString():"")+"",
			// Toast.LENGTH_SHORT)
			// .show();
		}

		Log.d("Loc Thread:onConnected:", "End:");
	}

	@Override
	public void onConnectionSuspended(int i) {

	}


	public void onDisconnected() {
		// TODO Auto-generated method stub
		mLocationClient.disconnect();
	}

	@Override
	public void run() {
		Log.d("Location Thread:", "Satrt"+name);

		// TODO Auto-generated method stub
		try {
			processActivity();
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if (servicesConnected() && timeCount == 0) {
			mLocationClient.connect();
			processLocationData();
			
		}

		Log.d("Location Thread:RUN", "End:" + timeCount);
	}

	private void processActivity() throws PackageManager.NameNotFoundException {
		ActivityManager activityManager = (ActivityManager) applicationContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		final List<RunningTaskInfo> recentTasks = activityManager
				.getRunningTasks(1);
		PackageManager pm = applicationContext.getPackageManager();

		ActivityManager mActivityManager =(ActivityManager)applicationContext.getSystemService(Context.ACTIVITY_SERVICE);

		if(Build.VERSION.SDK_INT > 20){
			String mPackageName = mActivityManager.getRunningAppProcesses().get(0).processName;
			Log.d("Location Thread:", "mPackageName"+mPackageName);

		}
		else{
			String mpackageName = mActivityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
			Log.d("Location Thread:", "mPackageName"+mpackageName);

		}
		long ts=new Date().getTime();

		UsageStatsManager usageStatsManager = (UsageStatsManager) applicationContext.getSystemService(Context.USAGE_STATS_SERVICE);
		List<UsageStats> usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts-60*1000, ts);
		if (usageStats == null || usageStats.size() == 0) {
			//return null;
		}
		Collections.sort(usageStats, new RecentUseComparator());

		if (usageStats.size() > 0) {
			Log.i("AppList1", "queryUsageStats size: " + usageStats.size());
			String packageName =	usageStats.get(0).getPackageName();
			CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
			Log.i("AppList1", "packageName size: " + packageName);

			processDataforApp(c.toString(), packageName);

		}
		String infom = "";
		// PackageManager pm = v.getContext().getPackageManager();
//		for (int i = 0; i < recentTasks.size(); i++) {
//			// info=info+"\nApplication:"
//			// +recentTasks.get(i).baseActivity.toShortString()+
//			// "\t\t ID: "+recentTasks.get(i).describeContents()+"";
//			try {
//				CharSequence c = recentTasks.get(i).topActivity
//						.getShortClassName(); // pm.getApplicationLabel(pm.getApplicationInfo(recentTasks.get(i).topActivity.getClassName(),
//												// PackageManager.GET_META_DATA));
//				Log.d("APP Info", c + "");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		List<ActivityManager.RunningAppProcessInfo> l = activityManager
//				.getRunningAppProcesses();
//		Iterator<ActivityManager.RunningAppProcessInfo> i = l.iterator();
//
//		while (i.hasNext()) {
//			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
//					.next());
//			try {
//
//				CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(
//						info.processName, PackageManager.GET_META_DATA));
//
//				String draw = info.processName;
//				// infom=infom+"\n"+
//				// c.toString()+":"+info.importance+":"+pm.getActivityInfo(info.importanceReasonComponent,
//				// PackageManager.GET_META_DATA).packageName;
//
//				processDataforApp(c.toString(), draw);
//				Log.d("Aletrante::", c.toString() + " info::" + draw);
//				//break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		//}
	}

	private  String getLollipopFGAppPackageName(Context ctx) {

		try {
			UsageStatsManager usageStatsManager = (UsageStatsManager) ctx.getSystemService(Context.USAGE_STATS_SERVICE);
			long milliSecs = 60 * 1000;
			Date dateToday = new Date();
			AppData appData = AppData.getAppInstance();
			Date todayDate = new Date();
			String date = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG,
					Locale.US).format(todayDate);
			String dateOnly = SimpleDateFormat.getDateInstance(
					SimpleDateFormat.SHORT, Locale.US).format(todayDate);
			List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, dateToday.getTime() - milliSecs, dateToday.getTime());
			if (queryUsageStats.size() > 0) {
				Log.i("LPU", "queryUsageStats size: " + queryUsageStats.size());
			}
			long recentTime = 0;
			String recentPkg = "";
			for (int i = 0; i < queryUsageStats.size(); i++) {
				UsageStats stats = queryUsageStats.get(i);
				stats.getTotalTimeInForeground();
				String date1 = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG,
						Locale.US).format(todayDate);
				AppInfoData dataApp = (AppInfoData) appData.getDataForDay().get(
						dateOnly + "_" + AppData.APP_APPLICATION_DATA);
				AppInfoData dataAppNext = new AppInfoData(stats.getPackageName(),
						new Date(), null, dataApp,
						dataApp.timeSpendThisTime);
				dataAppNext.draw = null;
				appData.getDataForDay().put(
						dateOnly + "_" + AppData.APP_APPLICATION_DATA,
						dataAppNext);
				AppInfoData dataTosend=getClone(dataApp);

			//	params.add(dataTosend);
			//	call.execute(params);

				//				if (i == 0 && !"org.pervacio.pvadiag".equals(stats.getPackageName())) {
//					Log.i("LPU", "PackageName: " + stats.getPackageName() + " " + stats.getLastTimeStamp());
//				}
//				if (stats.getLastTimeStamp() > recentTime) {
//					recentTime = stats.getLastTimeStamp();
//					recentPkg = stats.getPackageName();
//				}
			}
			return recentPkg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	private void processDataforApp(String appName, String draw) {
		Log.d("processDaaforApp", "1"+appName+" draw"+draw);
		AppData appData = AppData.getAppInstance();
		Date todayDate = new Date();
		String date = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG,
				Locale.US).format(todayDate);
		String dateOnly = SimpleDateFormat.getDateInstance(
				SimpleDateFormat.SHORT, Locale.US).format(todayDate);

		if (appData != null) {
			CallWebServiceAsync call = new CallWebServiceAsync();
			ArrayList<AppInfoData> params = new ArrayList<AppInfoData>();
			
			AppInfoData dataApp = (AppInfoData) appData.getDataForDay().get(
					dateOnly + "_" + AppData.APP_APPLICATION_DATA);// Data for
														// the day
			if (dataApp != null) {
				KeyguardManager myKM = (KeyguardManager) applicationContext
						.getSystemService(Context.KEYGUARD_SERVICE);
				if (myKM.inKeyguardRestrictedInputMode()) {
					processSleepMode(appData, dateOnly, call, params, dataApp,appName,draw);
					return;
				}
				String homeName = getHomeName();
				Log.d("Name of Home screen", homeName);
				if (dataApp != null && dataApp.appName != null
						&& dataApp.appName.equals(appName)) {
					Log.d("processDaaforApp", "Same App is being used" + date);
				} else {
					dataApp.endDateTime = new Date();
					dataApp.timeSpendThisTime = dataApp.endDateTime.getTime()
							- dataApp.startDateTime.getTime();
					setTimeSpend(dataApp, dateOnly);
					AppInfoData dataAppNext = new AppInfoData(appName,
							new Date(), null, dataApp,
							dataApp.timeSpendThisTime);
					dataAppNext.draw = draw;
					appData.getDataForDay().put(
							dateOnly + "_" + AppData.APP_APPLICATION_DATA,
							dataAppNext);
					AppInfoData dataTosend=getClone(dataApp);
					
					params.add(dataTosend);
					call.execute(params);	
				}
			} else {
				Log.d("processDaaforApp", "Start Day by puttin data" + date);
				AppInfoData dataAppFresh = new AppInfoData(appName, new Date(),
						null, null, 0);
				dataAppFresh.draw = draw;
				appData.getDataForDay().put(
						dateOnly + "_" + AppData.APP_APPLICATION_DATA,
						dataAppFresh);// put it for day
			
			}

		}

	}

	private void processSleepMode(AppData appData, String dateOnly,
			CallWebServiceAsync call, ArrayList<AppInfoData> params,
			AppInfoData dataApp, String appName, String draw) {
		Log.d("screen is locked ", "Enjoy");
		dataApp.endDateTime = new Date();
		dataApp.timeSpendThisTime = dataApp.endDateTime
				.getTime() - dataApp.startDateTime.getTime();
		setTimeSpend(dataApp, dateOnly);
		AudioManager manager = (AudioManager)applicationContext.getSystemService(Context.AUDIO_SERVICE);
		
		if(PHONE_SLEEP.equals(dataApp.appName)&& !manager.isMusicActive()){
			return;
		}else if(!PHONE_SLEEP.equals(dataApp.appName)&& !manager.isMusicActive()){
			AppInfoData dataAppNext = new AppInfoData(PHONE_SLEEP,
					new Date(), null, dataApp,
					dataApp.timeSpendThisTime);
			dataAppNext.draw=draw;
			appData.getDataForDay().put(
					dateOnly + "_" + AppData.APP_APPLICATION_DATA,
					dataAppNext);
			AppInfoData dataTosend=getClone(dataApp);
			
			params.add(dataTosend);
			call.execute(params);
		}else if(!PHONE_SLEEP.equals(dataApp.appName)&& appName.equals(dataApp.appName) && manager.isMusicActive()){//music still on
			return;
		}else{
			AppInfoData dataAppNext = new AppInfoData(appName,
					new Date(), null, dataApp,
					dataApp.timeSpendThisTime);
			appData.getDataForDay().put(
					dateOnly + "_" + AppData.APP_APPLICATION_DATA,
					dataAppNext);
			AppInfoData dataTosend=getClone(dataApp);
			
			params.add(dataTosend);
			call.execute(params);
			
		}
		

		return;
	}

	private AppInfoData getClone(AppInfoData dataAppNext) {
		AppInfoData dataTosend=null;
		try {
			 dataTosend=(AppInfoData) dataAppNext.clone();
			dataTosend.pastApp=null;
				
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataTosend;
	}

	private void setTimeSpend(AppInfoData dataApp, String dateOnly) {
		Long timeSpendtillNow = (Long) AppData.getAppInstance().getDataForDay()
				.get(dateOnly + "_" + AppData.APP_APPLICATION_TIME_SPEND);
		Log.i("setTimeSpend",
				"1" + timeSpendtillNow + " dataApp" + dataApp.toString());
		if (timeSpendtillNow != null) {
			setTimeData(dataApp, dateOnly, timeSpendtillNow);
		} else {
			Log.d("Fresh Start for Day", new Date().toString());

			setTimeData(dataApp, dateOnly, new Long(0));
		}
	}

	private void setTimeData(AppInfoData dataApp, String dateOnly,
			Long timeSpendtillNow) {
		if (dataApp.endDateTime != null) {
			long timeSpend = dataApp.endDateTime.getTime()
					- dataApp.startDateTime.getTime();
			if (timeSpendtillNow != null) {
				timeSpend = timeSpendtillNow + timeSpend;
				AppData.getAppInstance()
						.getDataForDay()
						.put(dateOnly + "_"
								+ AppData.APP_APPLICATION_TIME_SPEND,
								new Long(timeSpend));
			} else {
				AppData.getAppInstance()
						.getDataForDay()
						.put(dateOnly + "_"
								+ AppData.APP_APPLICATION_TIME_SPEND,
								new Long(0));
			}
		}
	}

	private String getHomeName() {
		Intent homeIntent = new Intent(Intent.ACTION_MAIN);
		homeIntent.addCategory(Intent.CATEGORY_HOME);
		ResolveInfo defaultLauncher = applicationContext.getPackageManager()
				.resolveActivity(homeIntent, PackageManager.MATCH_DEFAULT_ONLY);
		String nameOfLauncherPkg = defaultLauncher.activityInfo.packageName;

		return nameOfLauncherPkg;
	}

	private String getDateTime() {
		// get date time in custom format
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
		return sdf.format(new Date());
	}

	private void processLocationData() {
		AppData appData = AppData.getAppInstance();
		Date todayDate = new Date();
		String date = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG,
				Locale.US).format(todayDate);
		String dateOnly = SimpleDateFormat.getDateInstance(
				SimpleDateFormat.SHORT, Locale.US).format(todayDate);
		if (location == null)
			return;
		@SuppressWarnings("unchecked")
		LocationData hss = (LocationData) appData.getDataForDay().get(
				dateOnly + "_" + AppData.APP_LOCATION_DATA);
		Location loc = new Location(location);
		if (hss != null) {
			LocationData location = hss;
			float distance[] = new float[2];
			Location.distanceBetween(loc.getLatitude(), loc.getLongitude(),
					hss.location.getLatitude(), hss.location.getLongitude(),
					distance);
			if (distance.length > 0)
				Log.d("Distance", distance[0] + "");
			Log.d("Distance", loc.distanceTo(hss.location) + "");
			if (loc.distanceTo(hss.location) < 100) {
				Log.d("Main", "Past Location Equal:" + location.toString()
						+ " Date: " + date);
				Log.d("Main", "New Location Equal:" + loc.toString()
						+ " Date: " + date);
			} else {
				Log.d("Main", "Past Location: Change:" + location.toString()
						+ " Date: " + date);
				Log.d("Main", "New Location: Change:" + loc.toString()
						+ " Date: " + date);
				location.endDateTime = new Date();
				LocationData obj = new LocationData(loc, todayDate, null, null,
						false, null, location, String.valueOf(loc
								.distanceTo(hss.location)));
				appData.getDataForDay().put(
						dateOnly + "_" + AppData.APP_LOCATION_DATA, obj);
			}
		} else {
			LocationData obj = new LocationData(loc, new Date(), null, null,
					false, null, null, "0");
			appData.getDataForDay().put(
					dateOnly + "_" + AppData.APP_LOCATION_DATA, obj);
		}

		if (appData.getDataForDay() != null
				&& !appData.getDataForDay().isEmpty())
			Log.i("getLocationMap:", appData.getDataForDay().toString());

	}

	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this.applicationContext);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Get the error code
			int errorCode = resultCode;
			// Get the error dialog from Google Play services
			Log.d("Location Updates", "Google Play services is not available."
					+ resultCode);

		}
		return false;
	}

}
