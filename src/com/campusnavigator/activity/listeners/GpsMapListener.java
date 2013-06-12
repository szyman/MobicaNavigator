package com.campusnavigator.activity.listeners;

import com.campusnavigator.activity.MapNavigatorActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class GpsMapListener implements GpsListenerInf{

	private MapNavigatorActivity mapActivity;
	
	public GpsMapListener(Object listenerType) {
		// TODO Auto-generated constructor stub
		mapActivity = (MapNavigatorActivity)listenerType;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		Log.e("location", arg0.getLongitude() + " : " + arg0.getLatitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
