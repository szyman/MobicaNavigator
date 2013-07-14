package com.mobicanavigator.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity{
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
/*	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/
	
	public void launchActivity(Class<?> class1) {
		try{
			Intent intent = new Intent(this, class1);
			startActivity(intent);
		} catch (ActivityNotFoundException ex){
			Log.d("launchActivity", ex.toString());
		}
	}
	
	public void launchActivity(Class<?> activityClass, Object...args) {
		try{
			Intent intent = new Intent(this, activityClass);
			for(Object object : args){
				if(object instanceof float[])
					intent.putExtra("officeDirection", (float[])object);
				else if(object instanceof String)
					intent.putExtra("officeName", (String)object);
				else if(object instanceof List<?>)
					intent.putStringArrayListExtra("hintsArray", (ArrayList<String>)object);
				else {
					Log.e("Unatorizated type of object", "" + object);
				}
			}
			startActivity(intent);
		} catch (ActivityNotFoundException ex){
			Log.d("launchActivity", ex.toString());
		}
	}
}
