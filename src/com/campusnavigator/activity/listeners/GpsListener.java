package com.campusnavigator.activity.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class GpsListener implements LocationListener{

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
