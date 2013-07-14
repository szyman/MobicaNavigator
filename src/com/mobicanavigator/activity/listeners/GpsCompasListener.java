package com.mobicanavigator.activity.listeners;

import com.mobicanavigator.activity.CompassActivity;
import com.mobicanavigator.view.CompassView;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class GpsCompasListener implements GpsListenerInf {

	private CompassActivity compassActivity;
	float[] results;
	public GpsCompasListener(Object listenerType){
		compassActivity = (CompassActivity)listenerType;
		
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		results= new float[3];
		Log.e("compasGPS", location.getLatitude() + " : " + location.getLongitude());
		Location.distanceBetween(location.getLatitude(), location.getLongitude(), compassActivity.getMobicaLodzLoc().getLatitude(), compassActivity.getMobicaLodzLoc().getLongitude(), results);
		compassActivity.updateDistance(results[0]);
		((CompassView)compassActivity.getCompassView()).updateBearing(results[0]);
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
