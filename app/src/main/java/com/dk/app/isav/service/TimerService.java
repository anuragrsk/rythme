package com.dk.app.isav.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class TimerService extends Service {
	public static final long NOTIFY_INTERVAL = 5 * 1000; 
	private Handler mHandler = new Handler();	
	private Timer mTimer = null;
	private int count=0;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		if (mTimer != null) {
			mTimer.cancel();
		} else {
			mTimer = new Timer();
		}
		mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0,
				NOTIFY_INTERVAL);
	}
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}
	class TimeDisplayTimerTask extends TimerTask {
		// private LocationClient mLocationClient;
		@Override
		public void run() {
			// run on another thread
			mHandler.post(new LocationTaskThread(getApplicationContext(),count));
			 count=count+1;
		}

		private String getDateTime() {
			// get date time in custom format
			SimpleDateFormat sdf = new SimpleDateFormat(
					"[yyyy/MM/dd - HH:mm:ss]");
			return sdf.format(new Date());
		}

	}
}
