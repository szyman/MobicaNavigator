package com.campusnavigator.patterns;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public interface GpsListenerInf extends LocationListener{ 
	
	public void onLocationChanged(Location arg0);
	public void onProviderDisabled(String provider);
	public void onProviderEnabled(String provider);
	public void onStatusChanged(String provider, int status, Bundle extras);

}
