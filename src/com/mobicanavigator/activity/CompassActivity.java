package com.mobicanavigator.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.main.campusnavigator.R;
import com.mobicanavigator.activity.providers.GpsProvider;
import com.mobicanavigator.view.CompassView;

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
	
	private Location officeLoc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass);
		new GpsProvider(this);
		compassView = (CompassView) findViewById(R.id.compassRoute);
		
	    Bundle extras = getIntent().getExtras();
		float[] officeDirection = extras.getFloatArray("officeDirection");
		String officeName = extras.getString("officeName");
		
		((TextView)findViewById(R.id.destinationTextView)).append(officeName);

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
		

		officeLoc = new Location("officeLoc");
		officeLoc.setLongitude(officeDirection[0]);
		officeLoc.setLatitude(officeDirection[1]);
		
		
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

			//double azimuth = Math.toDegrees(matrixValues[0]);
			//double pitch = Math.toDegrees(matrixValues[1]);
			//double roll = Math.toDegrees(matrixValues[2]);

			compassView.update(matrixValues[0]);
		}
	}
	
	public void updateDistance(float distance){
		((TextView)findViewById(R.id.distanceTextView)).setText("Distance: "+distance);
	}
	
	
	public CompassView getCompassView() {
		return compassView;
	}

	public Location getMobicaLodzLoc() {
		return officeLoc;
	}
}
