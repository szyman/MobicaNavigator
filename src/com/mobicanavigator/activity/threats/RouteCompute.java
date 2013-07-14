package com.mobicanavigator.activity.threats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.mobicanavigator.activity.MapNavigatorActivity;

public class RouteCompute extends AsyncTask<String, String, String>{
	private List<LatLng> routePointsList;
	private Map<LatLng, String> routeInformation;


	private ProgressDialog pDialog;
	private LatLng pointStart;
	public void setPointStart(LatLng pointStart) {
		this.pointStart = pointStart;
	}

	private LatLng officeDest;
	private String waypoints;
	
	private List<String> hintDirection;
	
	private RouteCompute routeCompute;
	

	public RouteCompute(MapNavigatorActivity activity, ProgressDialog pDialog, LatLng LODZ_START, LatLng LODZ_DEST, String waypoints){
		routePointsList = new ArrayList<LatLng>();
		this.pDialog = pDialog;
		this.pointStart = LODZ_START;
		this.officeDest = LODZ_DEST;
		this.waypoints = waypoints;
		pDialog.setOnDismissListener(activity);
		routeInformation = new LinkedHashMap<LatLng, String>();
		
		routeCompute = this;
	}
	
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading route. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){

				@Override
				public void onCancel(DialogInterface arg0) {
					// TODO Auto-generated method stub
					routeCompute.cancel(true);
				}
            	
            });
            pDialog.show();
        }
        
    	public Map<LatLng, String> getRouteInformation() {
    		return routeInformation;
    	}
		
		public List<LatLng> getRoutePointsList() {
			return routePointsList;
		}

		@Override
		protected String doInBackground(String... params) {
			String originLatLngString = pointStart.latitude + "," + pointStart.longitude;
			String destLatLngString = officeDest.latitude + "," + officeDest.longitude;
            //String stringUrl = "http://maps.googleapis.com/maps/api/directions/json?origin=" + originLatLngString + "&destination=" + destLatLngString + "&waypoints=51.1078852,17.0385376|50.0755381,14.4378005&sensor=false";

			String stringUrl = "http://maps.googleapis.com/maps/api/directions/json?origin=" + originLatLngString + "&destination=" + destLatLngString + "&waypoints=" + waypoints +"&sensor=false";
			StringBuilder response = new StringBuilder();
			routePointsList = new ArrayList<LatLng>();
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
				}
				
				String jsonString = response.toString();
				JSONObject routeJson = new JSONObject(jsonString);
				JSONArray routeArray = routeJson.getJSONArray("routes");
				JSONObject route = routeArray.getJSONObject(0);
				JSONObject poly = route.getJSONObject("overview_polyline");
				
				JSONArray legs = route.getJSONArray("legs");
				int legsCount = legs.length();
				
				//JSONObject leg2 = legs.getJSONObject(1);

				for(int j = 0; j < legsCount; j++){
					JSONObject leg = legs.getJSONObject(j);
					JSONArray stepsArray = leg.getJSONArray("steps");
					for(int i=0; i<stepsArray.length(); i++){
						JSONObject step = stepsArray.getJSONObject(i);
						String html_instruction = step.getString("html_instructions");
						JSONObject start_location =  step.getJSONObject("start_location");
						
						LatLng start_locationLatLng = new LatLng(start_location.getDouble("lat"), start_location.getDouble("lng"));
						routeInformation.put(start_locationLatLng, html_instruction);
					}
					hintDirection = getHintDirection(stepsArray);
				}
				
				//JSONObject stepObject = stepsArray.getJSONObject(0);
				//String hintDirection = stepObject.getString("html_instructions");
				
				
				String polyline = poly.getString("points");
                routePointsList = new ArrayList<LatLng>(decodePoly(polyline));
                

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
		protected void onCancelled(String result) {
			// TODO Auto-generated method stub
			super.onCancelled(result);
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			pDialog.dismiss();
		}
		
		private List<String> getHintDirection(JSONArray stepsArray){
			List<String> hintDirection = new ArrayList<String>();
			String hint;
			
			for(int i=0; i < stepsArray.length(); i++){
				JSONObject stepObject;
				try {
					stepObject = stepsArray.getJSONObject(i);
					hint = stepObject.getString("html_instructions");
					hintDirection.add(hint);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return hintDirection;
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
	 
		public List<String> getHintDirection() {
			return hintDirection;
		}
}
