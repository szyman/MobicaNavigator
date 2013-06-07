package com.campusnavigator.activity.providers;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


public class GpsProvider {

	//private static final GpsProvider instance;
	private final String mocLocationName = "mocLocation";
	
	public GpsProvider(Context context, Object locListener2){
		LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		LocationListener locListener = (LocationListener) locListener2;
		
		if (locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000L,500.0f, locListener);
	    	locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		else{
			if(locationManager.getProvider(mocLocationName) == null)
				locationManager.addTestProvider(mocLocationName, false, false, false, false, true, true, true, 0, 5);
			
			//Temporary
			//Required ACCESS_MOCK_LOCATIOn
			locationManager.requestLocationUpdates(mocLocationName, 0, 0, locListener);
			Location location = new Location(mocLocationName);
			location.setLongitude(19.449553728627507);
			location.setLatitude(51.745472398279915);
			locationManager.setTestProviderEnabled(mocLocationName, true);
			locationManager.setTestProviderLocation(mocLocationName, location);	
		}
	}
	
/*	static{
		instance = new GpsProvider();
	}
	
	public static GpsProvider getInstance(){
		return instance;
	}*/
}
