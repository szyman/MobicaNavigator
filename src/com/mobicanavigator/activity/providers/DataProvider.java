package com.mobicanavigator.activity.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;

public class DataProvider {
	private final String OFFICE_CSV_NAME = "office_data.csv";
	private Map<String, float[]> officesMap;
	private Context context;
	
	public DataProvider(Context context){
		this.context = context;
	}
	
	public Map<String, float[]> getOfficesMap() {
		readFromCSV(context);
		return officesMap;
	}

	private void readFromCSV(Context context){
		officesMap = new HashMap<String, float[]>();
		AssetManager assetManager = context.getAssets();
		try {
			InputStream inputStream = assetManager.open(OFFICE_CSV_NAME);
			InputStreamReader streamReader = new InputStreamReader(inputStream);
			BufferedReader buffer = new BufferedReader(streamReader);
			String line;

			while((line = buffer.readLine()) != null){
				String[] splitLine = new String[3];
				float[] coordinates = new float[2];
				splitLine = line.split(";");
				coordinates[0] = Float.parseFloat(splitLine[1]);
				coordinates[1] = Float.parseFloat(splitLine[2]);
				officesMap.put(splitLine[0], coordinates);
			}
			streamReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
