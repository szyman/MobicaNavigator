package com.mobicanavigator.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.main.campusnavigator.R;
import com.mobicanavigator.activity.providers.DialogProvider;
import com.mobicanavigator.activity.providers.GpsProvider;
import com.mobicanavigator.activity.threats.RouteCompute;
import com.mobicanavigator.model.DialogType;

public class MapNavigatorActivity extends MainActivity 
	implements OnMapLongClickListener, OnDismissListener, OnClickListener {

	private LatLng actualLatLng = new LatLng(0,0);

	private LatLng destLatLng;

	private GoogleMap map;
	private List<LatLng> routePointsList;
	private List<LatLng> routePointsListOrigin;
	private Marker startMarker;
	private Marker destMarker;
	private List<MarkerOptions> markersInfoRoute;
	private LatLng tapPos;
	private Polyline polyline;
	private RouteCompute routeCompute;
	private ProgressDialog pDialog;
	private String waypoints;
	
	private List<LatLng> waypointsArray;

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

		map.setOnMapLongClickListener(this);
		
		pDialog = new ProgressDialog(MapNavigatorActivity.this);
		pDialog.setOnDismissListener(this);
		
		hintsButton = (Button)findViewById(R.id.hintsButton);
		
		Button refreshButton = (Button)findViewById(R.id.refreshMapButton);
		refreshButton.setOnClickListener(this);
		
		Button revertButton = (Button)findViewById(R.id.revertMapButton);
		revertButton.setOnClickListener(this);

		destMarker = map.addMarker(new MarkerOptions().position(destLatLng).title(officeName));
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
		waypointsArray = new ArrayList<LatLng>();
		waypoints = "";
		
		//DialogProvider alertDialog = new DialogProvider();
		//alertDialog.showDialog(this);		
	}

	
	public void onMapLongClick(LatLng tapPos) {
		waypoints += tapPos.latitude + "," +tapPos.longitude + "|";
		routeCompute = new RouteCompute(this, pDialog, actualLatLng, destLatLng, waypoints);
		routeCompute.execute();
		
		this.tapPos = tapPos;
	}
	
	private void revertChanges(){
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
			waypointsArray.clear();
			
			for(MarkerOptions markerOption : markersInfoRoute){
				map.addMarker(markerOption);
			}
		}
		if(routePointsListOrigin != null){
			for (int i = 0; i < routePointsListOrigin.size() - 1; i++) {
				LatLng startPoint = routePointsListOrigin.get(i);
				LatLng destPoint = routePointsListOrigin.get(i + 1);
				polyline = map.addPolyline(new PolylineOptions()
						.add(startPoint, destPoint).width(2).color(Color.RED));		
			}
			
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
						.title("You")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.ic_launcher)));
				map.addMarker(new MarkerOptions().position(destLatLng).title(destMarker.getTitle()));
				polyline = null;
			}
			for (int i = 0; i < routePointsList.size() - 1; i++) {
				LatLng startPoint = routePointsList.get(i);
				LatLng destPoint = routePointsList.get(i + 1);
				polyline = map.addPolyline(new PolylineOptions()
						.add(startPoint, destPoint).width(2).color(Color.RED));
				
			}
			
			if(tapPos != null &&
					tapPos != startMarker.getPosition() &&
					tapPos != destMarker.getPosition()){
				waypointsArray.add(tapPos);
				tapPos = null;
			}
			
			for(LatLng waypoint : waypointsArray)
				map.addMarker(new MarkerOptions().position(waypoint));
			addInfoToRoute(routeCompute.getRouteInformation());
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
		else{
			startMarker.setPosition(this.actualLatLng);
			//getRouteInfo(routeCompute.getRouteInformation(), this.actualLatLng);
		}
	}
	
	private void addInfoToRoute(Map<LatLng, String> routeInfoMap){
		Iterator<Entry<LatLng, String>> itStart = routeInfoMap.entrySet().iterator();
		boolean isFisrtInitMap = false;
		if (markersInfoRoute == null){
			markersInfoRoute = new ArrayList<MarkerOptions>();
			isFisrtInitMap = true;
		}
		while(itStart.hasNext()){
			Map.Entry<LatLng, String> entryStart = (Map.Entry<LatLng, String>) itStart.next();
			LatLng latlngStart = entryStart.getKey();
			MarkerOptions marker = new MarkerOptions().position(latlngStart).icon(BitmapDescriptorFactory
					.fromResource(R.drawable.info_icon)).title(entryStart.getValue()); 
			if(isFisrtInitMap)
				markersInfoRoute.add(marker);
			map.addMarker(marker);
		}
	}
	
	private List<String> getHints(Map<LatLng, String> routeInfoMap){
		List<String> hintsList = new ArrayList<String>();
		for (String hint : routeInfoMap.values())
			hintsList.add(hint);
		return hintsList;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.hintsButton){
			if(routeCompute.getHintDirection() != null)
				launchActivity(HintsRouteActivity.class, getHints(routeCompute.getRouteInformation()));
			else
				DialogProvider.showDialog(DialogType.ERROR, this, R.string.dialog_internet_error);
		}
		else if(v.getId() == R.id.refreshMapButton){
			routeCompute = new RouteCompute(this, pDialog, actualLatLng, destLatLng, waypoints);
			routeCompute.execute();
		}
		else if(v.getId() == R.id.revertMapButton){
			revertChanges();
		}
	}
	

}