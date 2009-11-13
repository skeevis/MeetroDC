package com.skeevisarts.meetrodc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends TabActivity {

	private TabHost mTabHost;

	private ArrayList<Station> redStations;
	private ArrayList<Station> greenStations;
	private ArrayList<Station> orangeStations;
	private ArrayList<Station> yellowStations;
	private ArrayList<Station> blueStations;
	
	
	ListView red_view;
	ArrayAdapter<Station> red_aa;
	ListView blue_view;
	ArrayAdapter<Station> blue_aa;
	ListView yellow_view;
	ArrayAdapter<Station> yellow_aa;
	ListView green_view;
	ArrayAdapter<Station> green_aa;
	ListView orange_view;
	ArrayAdapter<Station> orange_aa;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		red_view = (ListView) findViewById(R.id.red_view);
		orange_view = (ListView) findViewById(R.id.orange_view);
		green_view = (ListView) findViewById(R.id.green_view);
		blue_view = (ListView) findViewById(R.id.blue_view);
		yellow_view = (ListView) findViewById(R.id.yellow_view);
		
		
		redStations = new ArrayList<Station>();
		greenStations = new ArrayList<Station>();
		orangeStations = new ArrayList<Station>();
		yellowStations = new ArrayList<Station>();
		blueStations = new ArrayList<Station>();
		
		mTabHost = getTabHost();

		mTabHost.addTab(mTabHost.newTabSpec("tab_blue").setIndicator("Blue",
				getResources().getDrawable(R.drawable.blue)).setContent(
				R.id.blue_view));
		mTabHost.addTab(mTabHost.newTabSpec("tab_blue").setIndicator("Orange",
				getResources().getDrawable(R.drawable.orange)).setContent(
				R.id.orange_view));
		mTabHost.addTab(mTabHost.newTabSpec("tab_blue").setIndicator("Red",
				getResources().getDrawable(R.drawable.red)).setContent(
				R.id.red_view));
		mTabHost.addTab(mTabHost.newTabSpec("tab_blue").setIndicator("Yellow",
				getResources().getDrawable(R.drawable.yellow)).setContent(
				R.id.yellow_view));
		mTabHost.addTab(mTabHost.newTabSpec("tab_blue").setIndicator("Green",
				getResources().getDrawable(R.drawable.green)).setContent(
				R.id.green_view));

		

		int layoutID = android.R.layout.simple_list_item_1;
		red_aa = new ArrayAdapter<Station>(this, layoutID, redStations);
		red_view.setAdapter(red_aa);

		blue_aa = new ArrayAdapter<Station>(this, layoutID, blueStations);
		blue_view.setAdapter(blue_aa);

		green_aa = new ArrayAdapter<Station>(this, layoutID, greenStations);
		green_view.setAdapter(green_aa);

		yellow_aa = new ArrayAdapter<Station>(this, layoutID, yellowStations);
		yellow_view.setAdapter(yellow_aa);

		orange_aa = new ArrayAdapter<Station>(this, layoutID, orangeStations);
		orange_view.setAdapter(orange_aa);
		
		
		red_view.setOnItemClickListener(listener);
		orange_view.setOnItemClickListener(listener);
		blue_view.setOnItemClickListener(listener);
		yellow_view.setOnItemClickListener(listener);
		green_view.setOnItemClickListener(listener);

		mTabHost.setCurrentTab(0);
		readStationCSV();
		
	}

	protected void readStationCSV() {
		InputStream is = getResources().openRawResource(R.raw.data);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				String[] l = line.split(",");
				Station sta = new Station(l[1],Integer.parseInt(l[0]));
				String lines = l[2];
				if(lines.contains("Red")) redStations.add(sta);
				if(lines.contains("Green")) greenStations.add(sta);
				if(lines.contains("Orange")) orangeStations.add(sta);
				if(lines.contains("Yellow")) yellowStations.add(sta);
				if(lines.contains("Blue")) blueStations.add(sta);
			}
			br.close();
		} catch (Exception e) {
			Log.e(this.getClass().toString(), "Exception!");
		}
	}
	
	
	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> av, View v, int index,
				long arg3) {
			Station station = (Station) av.getAdapter().getItem(index);
			
			Intent intent = new Intent("com.skeevisarts.meetrodc.StationTimesActivity");
			Bundle b = new Bundle();
			b.putInt("station_id", station.getId());
			intent.putExtras(b);
			startActivity(intent);
		}
	};

}