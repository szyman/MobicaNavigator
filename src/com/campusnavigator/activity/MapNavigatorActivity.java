package com.campusnavigator.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.campusnavigator.activity.providers.GpsProvider;
import com.campusnavigator.activity.threats.RouteCompute;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.main.campusnavigator.R;

public class MapNavigatorActivity extends Activity implements
		OnMapClickListener, OnDismissListener, LocationListener {

	private LatLng LODZ_START = new LatLng(51.7592485, 19.45598330000007);
	private LatLng LODZ_DEST;

	private GoogleMap map;
	private List<LatLng> routePointsList;
	private Marker startMarker;
	private Polyline polyline;
	private RouteCompute routeCompute;
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		float[] officeDirection = extras.getFloatArray("officeDirection");
		LODZ_DEST = new LatLng(officeDirection[1], officeDirection[0]);

		setContentView(R.layout.map_layout);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setOnMapClickListener(this);
		
		
		new GpsProvider(getApplicationContext(), this);

		pDialog = new ProgressDialog(MapNavigatorActivity.this);
		pDialog.setOnDismissListener(this);

		startMarker = map.addMarker(new MarkerOptions()
				.position(LODZ_START)
				.title("Lodz")
				.snippet("Your Poss")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ic_launcher)));
		map.addMarker(new MarkerOptions().position(LODZ_DEST));

		LatLng midleRoute = new LatLng(
				(LODZ_START.latitude + LODZ_DEST.latitude) / 2,
				(LODZ_START.longitude + LODZ_DEST.longitude) / 2);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(midleRoute, 5));

		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(8), 2000, null);

		/*
		 * RouteComputeThreat routeComputeThreat = new
		 * RouteComputeThreat(LODZ_START, LODZ_DEST); routeComputeThreat.run();
		 */

		routeCompute = new RouteCompute(this, pDialog, LODZ_START, LODZ_DEST);
		routeCompute.execute();

	}

	@Override
	public void onMapClick(LatLng actualPos) {
		LODZ_START = actualPos;
		startMarker.setPosition(actualPos);
		routeCompute = new RouteCompute(this, pDialog, actualPos, LODZ_DEST);
		routeCompute.execute();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		routePointsList = new ArrayList<LatLng>(
				routeCompute.getRoutePointsList());
		if (polyline != null) {
			polyline.remove();
			map.clear();
			startMarker = map.addMarker(new MarkerOptions()
					.position(LODZ_START)
					.title("Lodz")
					.snippet("Your Poss")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher)));
			map.addMarker(new MarkerOptions().position(LODZ_DEST));
			polyline = null;
		}
		for (int i = 0; i < routePointsList.size() - 1; i++) {
			LatLng startPoint = routePointsList.get(i);
			LatLng destPoint = routePointsList.get(i + 1);
			polyline = map.addPolyline(new PolylineOptions()
					.add(startPoint, destPoint).width(2).color(Color.RED));
		}
	}

	@Override
	public void onLocationChanged(Location changedLoc) {
		// TODO Auto-generated method stub
		LatLng latLng = new LatLng(changedLoc.getLatitude(), changedLoc.getLongitude());
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