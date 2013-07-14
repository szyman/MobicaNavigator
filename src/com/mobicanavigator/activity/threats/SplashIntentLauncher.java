package com.mobicanavigator.activity.threats;

import android.app.Activity;
import android.content.Intent;

import com.mobicanavigator.activity.MenuActivity;

public class SplashIntentLauncher extends Thread {

	private Activity activity;
	
	public SplashIntentLauncher(Activity activity){
		this.activity = activity;
	}
	
	private final int SLEEP_TIME = 5;
	@Override
	public void run() {
	     try {
	         Thread.sleep(SLEEP_TIME*1000);
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	     
	     Intent intent  = new Intent(activity, MenuActivity.class);
	     activity.startActivity(intent);
	     activity.finish();
	}
}
