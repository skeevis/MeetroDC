package com.skeevisarts.meetrodc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.ZoomControls;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

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

	MetroMapView map_view;

	ZoomControls zc;

	private GestureDetector _gestureDetector;
	public MainActivity curAct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		curAct = this;

		red_view = (ListView) findViewById(R.id.red_view);
		orange_view = (ListView) findViewById(R.id.orange_view);
		green_view = (ListView) findViewById(R.id.green_view);
		blue_view = (ListView) findViewById(R.id.blue_view);
		yellow_view = (ListView) findViewById(R.id.yellow_view);
		map_view = (MetroMapView) findViewById(R.id.map_view);
		zc = (ZoomControls) findViewById(R.id.zoom_controls);

		redStations = new ArrayList<Station>();
		greenStations = new ArrayList<Station>();
		orangeStations = new ArrayList<Station>();
		yellowStations = new ArrayList<Station>();
		blueStations = new ArrayList<Station>();

		mTabHost = getTabHost();

		mTabHost.addTab(mTabHost.newTabSpec("tab_blue").setIndicator("Blue",
				getResources().getDrawable(R.drawable.blue)).setContent(
				R.id.blue_view));
		mTabHost.addTab(mTabHost.newTabSpec("tab_orange").setIndicator(
				"Orange", getResources().getDrawable(R.drawable.orange))
				.setContent(R.id.orange_view));
		mTabHost.addTab(mTabHost.newTabSpec("tab_red").setIndicator("Red",
				getResources().getDrawable(R.drawable.red)).setContent(
				R.id.red_view));
		mTabHost.addTab(mTabHost.newTabSpec("tab_yellow").setIndicator(
				"Yellow", getResources().getDrawable(R.drawable.yellow))
				.setContent(R.id.yellow_view));
		mTabHost.addTab(mTabHost.newTabSpec("tab_green").setIndicator("Green",
				getResources().getDrawable(R.drawable.green)).setContent(
				R.id.green_view));
		mTabHost.setCurrentTab(0);
		mTabHost.addTab(mTabHost.newTabSpec("tab_map").setIndicator("Map",
				getResources().getDrawable(R.drawable.compass)).setContent(
				R.id.map_wrapper_view));

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

		red_view.setOnItemLongClickListener(long_listener);
		orange_view.setOnItemLongClickListener(long_listener);
		blue_view.setOnItemLongClickListener(long_listener);
		yellow_view.setOnItemLongClickListener(long_listener);
		green_view.setOnItemLongClickListener(long_listener);

		readStationCSV();
		zc.setOnZoomInClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				map_view.zoomImageIn();
			}
		});

		zc.setOnZoomOutClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				map_view.zoomImageOut();
			}
		});
		_gestureDetector = new GestureDetector(this, _gestureListener);
		// Now I have to find the current station.

		int defaultStationId = getDefaultStation();

		if (defaultStationId >= 0) {
			int foundIndex = -1;
			foundIndex = hasStation(redStations, defaultStationId);
			if (foundIndex > 0) {
				red_view.setSelection(foundIndex);
				mTabHost.setCurrentTabByTag("tab_red");
			}
			foundIndex = hasStation(blueStations, defaultStationId);
			if (foundIndex > 0) {
				blue_view.setSelection(foundIndex);
				mTabHost.setCurrentTabByTag("tab_blue");
			}
			foundIndex = hasStation(yellowStations, defaultStationId);
			if (foundIndex > 0) {
				yellow_view.setSelection(foundIndex);
				mTabHost.setCurrentTabByTag("tab_yellow");
			}
			foundIndex = hasStation(greenStations, defaultStationId);
			if (foundIndex > 0) {
				green_view.setSelection(foundIndex);
				mTabHost.setCurrentTabByTag("tab_green");
			}
			foundIndex = hasStation(orangeStations, defaultStationId);
			if (foundIndex > 0) {
				orange_view.setSelection(foundIndex);
				mTabHost.setCurrentTabByTag("tab_orange");
			}

		}

	}

	private int getDefaultStation() {
		return getSharedPreferences(getClass().getName(), MODE_PRIVATE).getInt(
				"default_station", -1);
	}

	private int hasStation(ArrayList<Station> stations, int station_id) {
		Iterator<Station> sIti = stations.iterator();
		int x = 0;
		while (sIti.hasNext()) {
			Station curr = sIti.next();
			if (curr.id == station_id)
				return x;
			x++;
		}
		return -1;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		_gestureDetector.onTouchEvent(event);

		return true;
	}

	protected void readStationCSV() {
		InputStream is = getResources().openRawResource(R.raw.data);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				String[] l = line.split(",");
				Station sta = new Station(l[1], Integer.parseInt(l[0]));
				String lines = l[2];
				if (lines.contains("Red"))
					redStations.add(sta);
				if (lines.contains("Green"))
					greenStations.add(sta);
				if (lines.contains("Orange"))
					orangeStations.add(sta);
				if (lines.contains("Yellow"))
					yellowStations.add(sta);
				if (lines.contains("Blue"))
					blueStations.add(sta);
			}
			br.close();
		} catch (Exception e) {
			Log.e(this.getClass().toString(), "Exception! "
					+ e.getClass().toString());
		}
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> av, View v, int index, long arg3) {
			Station station = (Station) av.getAdapter().getItem(index);
			Intent intent = new Intent(
					"com.skeevisarts.meetrodc.StationTimesActivity");
			Bundle b = new Bundle();
			b.putInt("station_id", station.getId());
			intent.putExtras(b);
			startActivity(intent);
		}
	};

	private OnItemLongClickListener long_listener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> av, View v, int index,
				long arg3) {
			Station station = (Station) av.getAdapter().getItem(index);
			curAct.setDefaultStation(station);
			return true;
		}

	};

	public void setDefaultStation(Station station) {
		new AlertDialog.Builder(this).setMessage("We'll automatically open up "+ station.name+" next time.").setPositiveButton("Cool!", null).show();;
		SharedPreferences prefs = getSharedPreferences(getClass().getName(),
				MODE_PRIVATE);
		Editor e = prefs.edit();
		e.putInt("default_station", station.id);
		e.commit();
	}

	private OnGestureListener _gestureListener = new OnGestureListener() {

		float actX = 0;
		float actY = 0;

		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		public void onShowPress(MotionEvent e) {
		}

		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float _distanceX, float _distanceY) {
			if (actX == 0)
				actX = e2.getX();
			if (actY == 0)
				actY = e2.getY();
			float distanceX = actX - e2.getX();
			float distanceY = actY - e2.getY();
			actX = e2.getX();
			actY = e2.getY();
			map_view.panMap(distanceX, distanceY);
			return false;
		}

		public void onLongPress(MotionEvent e) {
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			return false;
		}

		public boolean onDown(MotionEvent e) {
			actX = 0;
			actY = 0;
			return false;
		}
	};
}
