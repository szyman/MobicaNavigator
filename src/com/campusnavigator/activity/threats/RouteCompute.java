package com.campusnavigator.activity.threats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.campusnavigator.activity.MapNavigatorActivity;
import com.google.android.gms.maps.model.LatLng;

public class RouteCompute extends AsyncTask<String, String, String>{
	private List<LatLng> routePointsList;
	private ProgressDialog pDialog;
	private LatLng LODZ_START;
	private LatLng LODZ_DEST;
	
	public RouteCompute(MapNavigatorActivity activity, ProgressDialog pDialog, LatLng LODZ_START, LatLng LODZ_DEST){
		routePointsList = new ArrayList<LatLng>();
		this.pDialog = pDialog;
		this.LODZ_START = LODZ_START;
		this.LODZ_DEST = LODZ_DEST;
		pDialog.setOnDismissListener(activity);
	}
	
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            routePointsList = new ArrayList<LatLng>();
            pDialog.setMessage("Loading route. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
		
		public List<LatLng> getRoutePointsList() {
			return routePointsList;
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
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			pDialog.dismiss();
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
