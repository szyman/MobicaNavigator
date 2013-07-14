package com.mobicanavigator.activity.threats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;


public class RouteComputeThreat implements Runnable {

	private LatLng LODZ_START, LODZ_DEST;
	private String polyline;
	public RouteComputeThreat(LatLng LODZ_START, LatLng LODZ_DEST){
		this.LODZ_START = LODZ_START;
		this.LODZ_DEST = LODZ_DEST;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String originLatLngString = LODZ_START.latitude + "," + LODZ_START.longitude;
		String destLatLngString = LODZ_DEST.latitude + "," + LODZ_DEST.longitude;
        String stringUrl = "http://maps.googleapis.com/maps/api/directions/json?origin=" + originLatLngString + "&destination=" + destLatLngString + "&sensor=false";
        StringBuilder response = new StringBuilder();
		
        URL url;
		try {
			url = new URL(stringUrl);
			HttpClient httpClient =  new DefaultHttpClient();
			HttpPost httpGet = new HttpPost(stringUrl);
			HttpResponse response2 = httpClient.execute(httpGet);
				BufferedReader inputBuff = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
				String inputReaded = null;
				while ((inputReaded = inputBuff.readLine()) !=null ){
					response.append(inputReaded);
				}
				inputBuff.close();
			
			
			String jsonString = response.toString();
			JSONObject routeJson = new JSONObject(jsonString);
			JSONArray routeArray = routeJson.getJSONArray("routes");
			JSONObject route = routeArray.getJSONObject(0);
			JSONObject poly = route.getJSONObject("overview_polyline");
			polyline = poly.getString("points");
			
			
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

	}

	public String getPolyline() {
		return polyline;
	}



}
