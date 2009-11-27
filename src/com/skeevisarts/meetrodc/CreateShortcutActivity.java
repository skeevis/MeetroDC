package com.skeevisarts.meetrodc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CreateShortcutActivity extends Activity {

	ListView lv;
	ArrayAdapter<Station> adapter;
	ArrayList<Station> list;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    	setContentView(R.layout.launcher_shortcuts);
        final Intent intent = getIntent();
        final String action = intent.getAction();
        if (Intent.ACTION_CREATE_SHORTCUT.equals(action)) {
			lv = (ListView)findViewById(R.id.shortcut_list);
			list = new ArrayList<Station>();
    		int layoutID = android.R.layout.simple_list_item_1;
			adapter = new ArrayAdapter<Station>(this,layoutID,list);
        	lv.setAdapter(adapter);
        	getFullStationList();
        	lv.setOnItemClickListener(listener);
            return;
        }
    }
    private void getFullStationList()
    {
    	InputStream is = getResources().openRawResource(R.raw.data);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				String[] l = line.split(",");
				Station sta = new Station(l[1], Integer.parseInt(l[0]));
				list.add(sta);
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
			setupShortcut(station);
			finish();
		}
	};
    
    private void setupShortcut(Station station) {
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setClassName("com.skeevisarts.meetrodc", "com.skeevisarts.meetrodc.StationTimesActivity");
        shortcutIntent.putExtra("station_id", station.id);
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, station.name);
        Parcelable iconResource = Intent.ShortcutIconResource.fromContext(
                this,  R.drawable.icon);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);
        setResult(RESULT_OK, intent);
    }
}