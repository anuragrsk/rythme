package com.dk.app.isav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.app.Application;

public class AppData extends Application {

	
	private Map dataForDay = Collections.synchronizedMap(new HashMap());
	public static final String APP_LOCATION_DATA="LOCATION_DATA";
	public static final String APP_APPLICATION_DATA="APP_DATA";
	public static final String APP_LOCATION_TIME_SPEND="APP_TIME_SPEND";
	public static final String APP_APPLICATION_TIME_SPEND="LOC_TIME_SPEND";
	public static AppData rythem = new AppData();
	

	public AppData() {
		super();
	}



	
	
	public Map getDataForDay() {
		return dataForDay;
	}





	public void setDataForDay(HashMap dataForDay) {
		this.dataForDay = dataForDay;
	}





	public static  AppData getAppInstance(){
		if (rythem==null){
			rythem=new AppData();
		}else{
			return rythem;
		}
		return rythem;
		
	}
}
