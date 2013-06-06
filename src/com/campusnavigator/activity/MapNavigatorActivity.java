package com.campusnavigator.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.campusnavigator.activity.threats.RouteComputeThreat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.main.campusnavigator.R;

public class MapNavigatorActivity extends Activity implements OnMapClickListener {

		private LatLng LODZ_START = new LatLng(51.7592485,19.45598330000007);
		private LatLng LODZ_DEST;
		
		private GoogleMap map;
		private List<LatLng> routePointsList;
		private Marker startMarker;
		private Polyline polyline;
		private PolylineOptions polylineOptions;
		private RouteCompute routeCompute;
		private static volatile Executor sDefaultExecutor;
		private boolean isFinished;
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
			
			startMarker = map.addMarker(new MarkerOptions()
					.position(LODZ_START)
					.title("Lodz")
					.snippet("Your Poss")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher)));
			Marker destMarker = map.addMarker(new MarkerOptions().position(LODZ_DEST));

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(LODZ_START, 15));

			// Zoom in, animating the camera.
			map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
			polylineOptions = new PolylineOptions();

/*			RouteComputeThreat routeComputeThreat = new RouteComputeThreat(LODZ_START, LODZ_DEST);
			routeComputeThreat.run();*/
			
			routeCompute = new RouteCompute();
			routeCompute.execute();
			

		}
		
		@Override
		public void onMapClick(LatLng actualPos) {
			routeCompute.executeOnExecutor(sDefaultExecutor);
		}
		
		private class RouteCompute extends AsyncTask<String, String, String>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(MapNavigatorActivity.this);
                pDialog.setMessage("Loading route. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }
			
			@Override
			protected String doInBackground(String... params) {
				String originLatLngString = LODZ_START.latitude + "," + LODZ_START.longitude;
				String destLatLngString = LODZ_DEST.latitude + "," + LODZ_DEST.longitude;
	            String stringUrl = "http://maps.googleapis.com/maps/api/directions/json?origin=" + originLatLngString + "&destination=" + destLatLngString + "&sensor=false";
	            StringBuilder response = new StringBuilder();
				
	            URL url;
				try {
					url = new URL(stringUrl);
					HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
					if(urlConn.getResponseCode() == HttpURLConnection.HTTP_OK){
						BufferedReader inputBuff = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
						String inputReaded = null;
						while ((inputReaded = inputBuff.readLine()) !=null ){
							response.append(inputReaded);
						}
						inputBuff.close();
					}
					
					String jsonString = response.toString();
					JSONObject routeJson = new JSONObject(jsonString);
					JSONArray routeArray = routeJson.getJSONArray("routes");
					JSONObject route = routeArray.getJSONObject(0);
					JSONObject poly = route.getJSONObject("overview_polyline");
					String polyline = poly.getString("points");
                    routePointsList = decodePoly(polyline);
					
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}
			
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				for(int i=0; i < routePointsList.size() - 1; i++){
					LatLng startPoint = routePointsList.get(i);
					LatLng destPoint = routePointsList.get(i+1);
					polyline = map.addPolyline(polylineOptions.add(startPoint, destPoint).width(2).color(Color.RED));
				}
				pDialog.dismiss();
			}
			
		}
		 private List<LatLng> decodePoly(String encoded) {

	            List<LatLng> poly = new ArrayList<LatLng>();
	            int index = 0, len = encoded.length();
	            int lat = 0, lng = 0;

	            while (index < len) {
	                int b, shift = 0, result = 0;
	                do {
	                    b = encoded.charAt(index++) - 63;
	                    result |= (b & 0x1f) << shift;
	                    shift += 5;
	                } while (b >= 0x20);
	                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	                lat += dlat;

	                shift = 0;
	                result = 0;
	                do {
	                    b = encoded.charAt(index++) - 63;
	                    result |= (b & 0x1f) << shift;
	                    shift += 5;
	                } while (b >= 0x20);
	                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	                lng += dlng;

	                LatLng p = new LatLng((((double) lat / 1E5)),
	                        (((double) lng / 1E5)));
	                poly.add(p);
	            }

	            return poly;
	        }

				
}