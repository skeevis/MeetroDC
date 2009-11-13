package com.skeevisarts.meetrodc;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class StationTimesActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
setContentView(R.layout.web_view);
		
		int station_id = getIntent().getExtras().getInt("station_id");
		WebView wv = (WebView) findViewById(R.id.webview);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.loadUrl("http://www.wmata.com/rider_tools/pids/showpid.cfm?station_id="+station_id);
		
		
	}
	
	
}
