package com.campusnavigator.activity;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.campusnavigator.view.AugRealityView;
import com.main.campusnavigator.R;

public class AugRealityActivity extends MainActivity implements SensorEventListener{
	
	private SensorManager sensorManager;
	private Sensor magneticFieldSensor;
	private Sensor accelelometerSensor;
	private Sensor gravitySensor;
	
	private float[] matrixR;
	private float[] matrixValues;
	private float[] matrixI;
	private float[] matrixAccelerometer;
	private float[] matrixMagneticField;
	private float[] matrixGravity;
	
	final float alpha = 0.8f;
	
	private View augRealityView;
	private LocationManager locationManager;
	private final String mocLocationName = "mocLocation";
	
	
	
	private Location mobicaLodzLoc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		augRealityView = (AugRealityView)SurfaceView.inflate(this, R.layout.aug_reality_layout, null);
		
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		accelelometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		matrixR = new float[9];
		matrixValues = new float[3];
		matrixI = new float[9];
		matrixAccelerometer = new float[3];
		matrixMagneticField = new float[3];
		matrixGravity = new float[3];
		
		Bundle extras = getIntent().getExtras();
		float[] officeDirection = extras.getFloatArray("officeDirection");
		
		mobicaLodzLoc = new Location("mobicaLodzLoc");
		mobicaLodzLoc.setLongitude(officeDirection[0]);
		mobicaLodzLoc.setLatitude(officeDirection[1]);
		
		
		setContentView(augRealityView);
		addContentView(((AugRealityView)augRealityView).getMarkersView(), new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener locListener = new GPSProvider();
		
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
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		sensorManager.registerListener(this,accelelometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		sensorManager.unregisterListener(this, accelelometerSensor);
		sensorManager.unregisterListener(this, magneticFieldSensor);
		//locationManager.removeTestProvider(mocLocationName);
		super.onPause();
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		// TODO Auto-generated method stub
		switch (sensorEvent.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
/*			for(int i = 0; i < 3; i++)
				matrixAccelerometer[i] = sensorEvent.values[i];*/
/*			matrixAccelerometer[0] = sensorEvent.values[0] - matrixGravity[0];
			matrixAccelerometer[1] = sensorEvent.values[1] - matrixGravity[1];
			matrixAccelerometer[2] = sensorEvent.values[2] - matrixGravity[2];*/
			matrixAccelerometer[0]=(matrixAccelerometer[0]*2+sensorEvent.values[0])*0.33334f;
			matrixAccelerometer[1]=(matrixAccelerometer[1]*2+sensorEvent.values[1])*0.33334f;
			matrixAccelerometer[2]=(matrixAccelerometer[2]*2+sensorEvent.values[2])*0.33334f;
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
/*			for(int i = 0; i < 3; i++)
				matrixMagneticField[i] = sensorEvent.values[i];*/
			matrixMagneticField[0]=(matrixMagneticField[0]*1+sensorEvent.values[0])*0.5f;
			matrixMagneticField[1]=(matrixMagneticField[1]*1+sensorEvent.values[1])*0.5f;
			matrixMagneticField[2]=(matrixMagneticField[2]*1+sensorEvent.values[2])*0.5f;
			break;
		case Sensor.TYPE_GRAVITY:
			matrixGravity[0] = alpha * matrixGravity[0] + (1 - alpha) * sensorEvent.values[0];
			matrixGravity[1] = alpha * matrixGravity[1] + (1 - alpha) * sensorEvent.values[1];
			matrixGravity[2] = alpha * matrixGravity[2] + (1 - alpha) * sensorEvent.values[2];
			break;
		default:
			break;
		}
		
				
		boolean success = SensorManager.getRotationMatrix(matrixR, matrixI, matrixAccelerometer, matrixMagneticField);
		
		if(success){
		   
		   SensorManager.remapCoordinateSystem(matrixR, SensorManager.AXIS_X, SensorManager.AXIS_MINUS_Z, matrixR);
		   SensorManager.getOrientation(matrixR, matrixValues);
		   
		   double azimuth = Math.toDegrees(matrixValues[0]);
		   double pitch = Math.toDegrees(matrixValues[1]);
		   double roll = Math.toDegrees(matrixValues[2]);
		   
		   if(pitch > - 10 && pitch < 10)
			   ((AugRealityView)augRealityView).updateDirection(matrixValues[0]);
		   Log.e("results", ""+azimuth);// + " , " + pitch + " , " +roll);
		}
	}
	
	private class GPSProvider implements LocationListener{

		final float[] results= new float[3];
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
			Location.distanceBetween(location.getLatitude(), location.getLongitude(), mobicaLodzLoc.getLatitude(), mobicaLodzLoc.getLongitude(), results);
			((AugRealityView)augRealityView).updateBearing(results[1]);
			
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
