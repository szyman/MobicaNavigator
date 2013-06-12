package com.campusnavigator.activity.listeners;

import android.location.Location;
import android.os.Bundle;

import com.campusnavigator.activity.AugRealityActivity;
import com.campusnavigator.view.AugRealityView;

public class GpsAugRealityListener implements GpsListenerInf{

	private AugRealityActivity arActivity;
	private final float[] results= new float[3];
	
	public GpsAugRealityListener(Object listenerType){
		arActivity = (AugRealityActivity)listenerType;
	}
	
	@Override
	public void onLocationChanged(Location curLocation) {
		// TODO Auto-generated method stub
		Location.distanceBetween(curLocation.getLatitude(), curLocation.getLongitude(), arActivity.getMobicaLodzLoc().getLatitude(), arActivity.getMobicaLodzLoc().getLongitude(), results);
		((AugRealityView) arActivity.getAugRealityView()).updateBearing(results[1]);
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
