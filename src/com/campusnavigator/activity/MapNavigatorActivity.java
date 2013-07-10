package com.campusnavigator.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.campusnavigator.activity.providers.DialogProvider;
import com.campusnavigator.activity.providers.GpsProvider;
import com.campusnavigator.activity.threats.RouteCompute;
import com.campusnavigator.model.DialogType;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.main.campusnavigator.R;

public class MapNavigatorActivity extends MainActivity implements
		OnMapClickListener, OnMapLongClickListener, OnDismissListener, OnClickListener {

	private LatLng actualLatLng = new LatLng(0,0);

	private LatLng destLatLng;

	private GoogleMap map;
	private List<LatLng> routePointsList;
	private List<LatLng> routePointsListOrigin;
	private Marker startMarker;
	private Polyline polyline;
	private RouteCompute routeCompute;
	private ProgressDialog pDialog;
	private String waypoints;

	private Button hintsButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.map_layout);
		new GpsProvider(this);
		
		Bundle extras = getIntent().getExtras();
		float[] officeDirection = extras.getFloatArray("officeDirection");
		String officeName = extras.getString("officeName");
		
		destLatLng = new LatLng(officeDirection[1], officeDirection[0]);

		
		((TextView)findViewById(R.id.destinationTextView)).append(officeName);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setOnMapClickListener(this);
		map.setOnMapLongClickListener(this);
		
		pDialog = new ProgressDialog(MapNavigatorActivity.this);
		pDialog.setOnDismissListener(this);
		
		hintsButton = (Button)findViewById(R.id.hintsButton);
		
		Button refreshButton = (Button)findViewById(R.id.refreshMapButton);
		refreshButton.setOnClickListener(this);

		map.addMarker(new MarkerOptions().position(destLatLng));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(destLatLng, 5));
		
		//LatLng midleRoute = new LatLng(
		//		(actualLatLng.latitude + destLatLng.latitude) / 2,
		//		(actualLatLng.longitude + destLatLng.longitude) / 2);
		

		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(5), 2000, null);

		/*
		 * RouteComputeThreat routeComputeThreat = new
		 * RouteComputeThreat(LODZ_START, LODZ_DEST); routeComputeThreat.run();
		 */
		
		waypoints = "";
		
		//DialogProvider alertDialog = new DialogProvider();
		//alertDialog.showDialog(this);		
	}

	@Override
	public void onMapClick(LatLng actualPos) {
		//startMarker.setPosition(actualPos);
		//routeCompute = new RouteCompute(this, pDialog, actualPos, LODZ_DEST);
		//routeCompute.execute();
		
		//---------------------------------------------------------
/*		for(int i=0; i<routePointsList.size() - 10; i++)
			routePointsList.remove(i);
		
		polyline.remove();
		map.clear();
		int size = routePointsList.size() + 10 - routePointsList.size(); 
		
		for (int i = 0; i < size; i++) {
			LatLng startPoint = routePointsList.get(i);
			LatLng destPoint = routePointsList.get(i + 1);
			polyline = map.addPolyline(new PolylineOptions()
					.add(startPoint, destPoint).width(2).color(Color.RED));
			
		}*/
		
		waypoints += actualPos.latitude + "," +actualPos.longitude + "|";
		routeCompute = new RouteCompute(this, pDialog, actualLatLng, destLatLng, waypoints);
		routeCompute.execute();
		
	}
	
	@Override
	public void onMapLongClick(LatLng arg0) {
		// TODO Auto-generated method stub
		if (polyline != null) {
			polyline.remove();
			map.clear();
			startMarker = map.addMarker(new MarkerOptions()
					.position(actualLatLng)
					.title("You")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher)));
			map.addMarker(new MarkerOptions().position(destLatLng));
			polyline = null;
		}
		for (int i = 0; i < routePointsListOrigin.size() - 1; i++) {
			LatLng startPoint = routePointsListOrigin.get(i);
			LatLng destPoint = routePointsListOrigin.get(i + 1);
			polyline = map.addPolyline(new PolylineOptions()
					.add(startPoint, destPoint).width(2).color(Color.RED));
			
		}
		
		waypoints = "";
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		routePointsList = new ArrayList<LatLng>(
				routeCompute.getRoutePointsList());
		if(routePointsList.size() != 0){
			if (routePointsListOrigin == null)
				routePointsListOrigin = new ArrayList<LatLng>(routePointsList);
			
			if (polyline != null) {
				polyline.remove();
				map.clear();
				startMarker = map.addMarker(new MarkerOptions()
						.position(actualLatLng)
						.title("Lodz")
						.snippet("Your Poss")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.ic_launcher)));
				map.addMarker(new MarkerOptions().position(destLatLng));
				polyline = null;
			}
			for (int i = 0; i < routePointsList.size() - 1; i++) {
				LatLng startPoint = routePointsList.get(i);
				LatLng destPoint = routePointsList.get(i + 1);
				polyline = map.addPolyline(new PolylineOptions()
						.add(startPoint, destPoint).width(2).color(Color.RED));
				
			}
		}
		else{
			DialogProvider.showDialog(DialogType.ERROR, this, R.string.dialog_internet_error);
		}

	}

	public void setActualLatLng(Location actualLatLng) {
		this.actualLatLng = new LatLng(actualLatLng.getLatitude(), actualLatLng.getLongitude());
		float[] distance = new float[3];
		Location.distanceBetween(this.actualLatLng.latitude, this.actualLatLng.longitude, destLatLng.latitude, destLatLng.longitude, distance);
		((TextView)findViewById(R.id.distanceTextView)).setText("Distance: "+distance[0]);
		if(routeCompute == null){
			routeCompute = new RouteCompute(this, pDialog, this.actualLatLng, destLatLng, waypoints);
			routeCompute.setPointStart(this.actualLatLng);
			routeCompute.execute();
			
			startMarker = map.addMarker(new MarkerOptions()
			.position(this.actualLatLng)
			.title("You")
			.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_launcher)));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(this.actualLatLng, 5));
			hintsButton.setOnClickListener(this);
		}
		else
			startMarker.setPosition(this.actualLatLng);
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.hintsButton){
			if(routeCompute.getHintDirection() != null)
				launchActivity(HintsRouteActivity.class, routeCompute.getHintDirection());
			else
				DialogProvider.showDialog(DialogType.ERROR, this, R.string.dialog_internet_error);
		}
		else if(v.getId() == R.id.refreshMapButton){
			routeCompute = new RouteCompute(this, pDialog, actualLatLng, destLatLng, waypoints);
			routeCompute.execute();
		}
	}
	

}