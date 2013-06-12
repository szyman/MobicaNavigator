package com.campusnavigator.patterns;

import com.campusnavigator.activity.CompassActivity;
import com.campusnavigator.view.CompassView;

import android.location.Location;
import android.os.Bundle;

public class GpsCompasListener implements GpsListenerInf {

	private CompassActivity compassActivity;
	final float[] results= new float[3];
	public GpsCompasListener(Object listenerType){
		compassActivity = (CompassActivity)listenerType;
		
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		Location.distanceBetween(location.getLatitude(), location.getLongitude(), compassActivity.getMobicaLodzLoc().getLatitude(), compassActivity.getMobicaLodzLoc().getLongitude(), results);
		((CompassView)compassActivity.getCompassView()).updateBearing(results[1]);
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
