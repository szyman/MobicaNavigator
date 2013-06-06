package com.campusnavigator.activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.campusnavigator.view.AugRealityView;
import com.campusnavigator.view.CompassView;
import com.main.campusnavigator.R;

public class CompassActivity extends MainActivity implements
		SensorEventListener {

	private CompassView compassView;

	SensorManager sensorManager;
	private Sensor sensorAccelerometer;
	private Sensor sensorMagneticField;

	private float[] valuesAccelerometer;
	private float[] valuesMagneticField;

	private float[] matrixR;
	private float[] matrixI;
	private float[] matrixValues;
	
	final float alpha = 0.8f;
	
	private LocationManager locationManager;
	private final String mocLocationName = "mocLocation";
	private Location mobicaLodzLoc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass);

		compassView = (CompassView) findViewById(R.id.compassRoute);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorAccelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorMagneticField = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		valuesAccelerometer = new float[3];
		valuesMagneticField = new float[3];

		matrixR = new float[9];
		matrixI = new float[9];
		matrixValues = new float[3];
		
		Intent intent = new Intent();

	    Bundle extras = getIntent().getExtras();
		float[] officeDirection = extras.getFloatArray("officeDirection");
		
		mobicaLodzLoc = new Location("mobicaLodzLoc");
		mobicaLodzLoc.setLongitude(officeDirection[0]);
		mobicaLodzLoc.setLatitude(officeDirection[1]);
		
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener locListener = new GPSProvider();
		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compas, menu);
		return true;
	}

	@Override
	protected void onResume() {

		sensorManager.registerListener(this, sensorAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, sensorMagneticField,
				SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

	@Override
	protected void onPause() {

		sensorManager.unregisterListener(this, sensorAccelerometer);
		sensorManager.unregisterListener(this, sensorMagneticField);
		super.onPause();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			for (int i = 0; i < 3; i++) {
				valuesAccelerometer[i] = event.values[i];
			}
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			for (int i = 0; i < 3; i++) {
				valuesMagneticField[i] = event.values[i];
			}
			break;
		}
		boolean success = SensorManager.getRotationMatrix(matrixR, matrixI,
				valuesAccelerometer, valuesMagneticField);

		if (success) {
			SensorManager.getOrientation(matrixR, matrixValues);

			double azimuth = Math.toDegrees(matrixValues[0]);
			double pitch = Math.toDegrees(matrixValues[1]);
			double roll = Math.toDegrees(matrixValues[2]);

			compassView.update(matrixValues[0]);
		}
	}
	
	private class GPSProvider implements LocationListener{

		final float[] results= new float[3];
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
			Location.distanceBetween(location.getLatitude(), location.getLongitude(), mobicaLodzLoc.getLatitude(), mobicaLodzLoc.getLongitude(), results);
			((CompassView)compassView).updateBearing(results[1]);
			
			//double angle = ((AugRealityView)augRealityView).getAngle(location, mobicaLodzLoc);
			//double degree = Math.toDegrees(angle);
			//Log.e("mylocation", location.getLatitude() + " " + location.getLongitude());
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

}
