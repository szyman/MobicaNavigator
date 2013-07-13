package com.campusnavigator.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.main.campusnavigator.R;

public class HintsRouteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		ArrayList<String> hintsArray = extras.getStringArrayList("hintsArray");
		
		setContentView(R.layout.hints_route_layout);

		WebView webView = (WebView) findViewById(R.id.webViewHints);
		StringBuffer html = new StringBuffer("<html><body>");
		for(String hint : hintsArray){
			html.append(hint);
			html.append("<br>");	
		}
		html.append("</body></html>");
		webView.loadData(html.toString(), "text/html", "UTF-8");
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
