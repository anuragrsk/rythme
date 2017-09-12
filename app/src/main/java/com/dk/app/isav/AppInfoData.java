package com.dk.app.isav;

import java.io.Serializable;
import java.util.Date;

import android.graphics.drawable.Drawable;

public class AppInfoData implements Serializable,Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1017832481559720902L;
	public String appName;
	public Date startDateTime;
	public Date endDateTime;
	public long timeSpendThisTime;
	public AppInfoData pastApp;
	public String draw;
	public AppInfoData(String appName, Date startDateTime, Date endDateTime,AppInfoData pData,long timeSpendTime) {
		super();
		this.appName = appName;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.pastApp=pData;
		this.timeSpendThisTime=timeSpendTime;
	}

	public AppInfoData(String packageName, long totalTimeInForeground) {
		this.appName = packageName;
		this.timeSpendThisTime=totalTimeInForeground;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appName == null) ? 0 : appName.hashCode());
		result = prime * result
				+ ((endDateTime == null) ? 0 : endDateTime.hashCode());
		result = prime * result
				+ ((startDateTime == null) ? 0 : startDateTime.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppInfoData other = (AppInfoData) obj;
		if (appName == null) {
			if (other.appName != null)
				return false;
		} else if (!appName.equals(other.appName))
			return false;
		if (endDateTime == null) {
			if (other.endDateTime != null)
				return false;
		} else if (!endDateTime.equals(other.endDateTime))
			return false;
		if (startDateTime == null) {
			if (other.startDateTime != null)
				return false;
		} else if (!startDateTime.equals(other.startDateTime))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "AppInfoData [appName=" + appName + ", startDateTime="
				+ startDateTime + ", endDateTime=" + endDateTime + "] \n Past App:"+pastApp;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
		}
}
