package com.mobicanavigator.activity;

import android.os.Bundle;

import com.main.campusnavigator.R;
import com.mobicanavigator.activity.threats.SplashIntentLauncher;

public class SplashActivity extends MainActivity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		
		SplashIntentLauncher launcher = new SplashIntentLauncher(this);
		launcher.start();
	}
}
