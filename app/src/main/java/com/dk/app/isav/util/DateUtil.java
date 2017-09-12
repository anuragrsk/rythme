package com.dk.app.isav.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

public class DateUtil {
public static final String CLASS_NAME="DateUtil";
//TODO: need to fix this  
	public static String getDate(String date){
		Log.d(CLASS_NAME,"Date is:"+date);
		SimpleDateFormat ft = new SimpleDateFormat ("MMM dd, yyyy HH:mm:ss a",Locale.US); 
		SimpleDateFormat ft1 = new SimpleDateFormat ("MM/dd/yyyy",Locale.US); 
		String returnDate="";
		try {
			Date dateFormated = ft.parse(date);
			returnDate=ft1.format(dateFormated);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d(CLASS_NAME,"Date Return after format:getDate:"+returnDate);
		return returnDate;
	}
	
	public static String getDateTime(String date){
		
		SimpleDateFormat ft = new SimpleDateFormat ("MMM dd, yyyy HH:mm:ss a",Locale.US); 
		SimpleDateFormat ft1 = new SimpleDateFormat ("MM/dd/yyyy HH:mm a",Locale.US); 
		String returnDate="";
		try {
			Date dateFormated = ft.parse(date);
			returnDate=ft1.format(dateFormated);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d(CLASS_NAME,"Date Return after format:getDateTime:"+returnDate);
		return returnDate;
	}
	
	public static Date getDateObject(String date){
		Log.d(CLASS_NAME,"Date is:getDateObject:"+date);
		SimpleDateFormat ft = new SimpleDateFormat ("MMM dd, yyyy HH:mm:ss a",Locale.US); 
		
		Date dateFormated=null;
		try {
			 dateFormated = ft.parse(date);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d(CLASS_NAME,"Date Return after format:getDateObject:"+dateFormated);
		return dateFormated;
	}
	
	public static String getDateTimeMySQL(Date date){
		SimpleDateFormat ft1 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss",Locale.US); 
		String returnDate="";
		try {
			returnDate=ft1.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Date Return after format:getDateTime:"+returnDate);
		return returnDate;
	}
	
	public static void main(String args[]){
		
		String aa=new Date().toLocaleString();
		System.out.print(DateUtil.getDate(aa));
	}
}
