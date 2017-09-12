package com.dk.app.isav;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import android.location.Location;

public class LocationData implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = -6791773582210945914L;
public Location location;
public Date startDateTime;
public Date endDateTime;
public String locationAlias;
public boolean isPublic;
public String distance;
public ArrayList knownLocation;
public LocationData pastLocation;
public LocationData(Location location, Date startDateTime, Date endDateTime,
		String locationAlias, boolean isPublic, ArrayList knownLocation,LocationData last,String dis) {
	super();
	this.location = location;
	this.startDateTime = startDateTime;
	this.endDateTime = endDateTime;
	this.locationAlias = locationAlias;
	this.isPublic = isPublic;
	this.knownLocation = knownLocation;
	this.pastLocation=last;
	this.distance=dis;
}
@Override
public String toString() {
	return "LocationData [location=" + location + ", startDateTime="
			+ startDateTime + ", endDateTime=" + endDateTime+", distance:"+distance
			
		+"\n, pastLocation="
			+ pastLocation + "]";
}




}
