package com.campusnavigator.provider;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;

public class SensorsProvider implements SensorEventListener {

	
	private SensorManager sensorManager;
	private Sensor magneticField;
	private Sensor accelelometer;
	
	private float[] matrixR;
	private float[] matrixValues;
	private float[] matrixI;
	private float[] matrixAccelerometer;
	private float[] matrixMagneticField;
	
	
	
	public SensorsProvider(Context context) {
		// TODO Auto-generated constructor stub
		
		sensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
		accelelometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		matrixR = new float[9];
		matrixValues = new float[3];
		matrixI = new float[9];
		matrixAccelerometer = new float[3];
		matrixMagneticField = new float[3];
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		// TODO Auto-generated method stub
		switch (sensorEvent.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			matrixAccelerometer = sensorEvent.values;
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			matrixMagneticField = sensorEvent.values;
			break;
		default:
			break;
		}
		
		
		
		boolean success = SensorManager.getRotationMatrix(matrixR, matrixI, matrixAccelerometer, matrixMagneticField);
		
		if(success)
		   SensorManager.getOrientation(matrixR, matrixValues);
		   
		   double azimuth = Math.toDegrees(matrixValues[0]);
		   double pitch = Math.toDegrees(matrixValues[1]);
		   double roll = Math.toDegrees(matrixValues[2]);
		   
		   Log.e("results", azimuth + " , " + pitch + " , " +roll);
	}


}
