package com.dk.app.isav.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

public class ActivityReceiver extends BroadcastReceiver {
	public ActivityReceiver() {
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO: This method is called when the BroadcastReceiver is receiving
		// an Intent broadcast.
		//throw new UnsupportedOperationException("Not yet implemented");
		PackageManager pm  = context.getPackageManager();
		Log.i("ActivityReceiver",intent.getAction());
		Log.i("Activity Receiver",intent.getPackage());
		//Log.i("Activity Receiver",pm.getApplicationLabel(intent.get));
	}
}
