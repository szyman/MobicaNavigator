package com.campusnavigator.activity.providers;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.campusnavigator.model.DialogType;
import com.campusnavigator.patterns.GpsListenerFactory;
import com.main.campusnavigator.R;


public class GpsProvider {

	//private static final GpsProvider instance;
	private final String mocLocationName = "mocLocation";
	
	public GpsProvider(Object context){
		LocationManager locationManager = (LocationManager)((Context)context).getSystemService(Context.LOCATION_SERVICE);
		
		LocationListener locListener = GpsListenerFactory.createListener(context);
		
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
			
			DialogProvider.showDialog(DialogType.INFO, ((Context)context), R.string.dialog_moc_location_info);
		}
	}
	
/*	static{
		instance = new GpsProvider();
	}
	
	public static GpsProvider getInstance(){
		return instance;
	}*/
}
