package com.skeevisarts.meetrodc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

public class MetroMapView extends ImageView {

	public static int zoomController = 150;
	public static int posX = 0;
	public static int posY = 0;
	public static int minZoom = 150;

	private static Drawable image = null;
	String FILE_PATH = "metromap.jpg";
	String IMAGE_LOCATION = "http://droidee.com/resources/metromapdc.jpg";

	public MetroMapView(Context context) {
		super(context);
		init();

	}

	public MetroMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();

	}

	public MetroMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		Bitmap b = loadImage();
		if (b == null) {
			Log.v("MeetroDC","Downloading Image.");
			b = downloadImage(IMAGE_LOCATION);
			if (b == null) {

			} else {
				saveImageToSDCard(b);
				image = new BitmapDrawable(b);
				
			}

		} else {
			image = new BitmapDrawable(b);
		}

	}

	private Bitmap loadImage() {
		try {
			return BitmapFactory.decodeStream(getContext().openFileInput(FILE_PATH));
		} catch (FileNotFoundException e) {
			Log.e("MeetroDC","FNF reading map to drive: "+e.getMessage());
			return null;
		}
	}

	private void saveImageToSDCard(Bitmap image) {

		OutputStream stream;
		try {
			stream = getContext().openFileOutput(FILE_PATH, Context.MODE_PRIVATE);
			image.compress(CompressFormat.JPEG, 90, stream);
			stream.flush();
			stream.close();
		} catch (FileNotFoundException e) {
			Log.e("MeetroDC","FNF saving map to drive: "+e.getMessage());
 
		} catch (IOException e) {
			Log.e("MeetroDC","Error saving map to drive: "+e.getMessage());

		}

	}

	private Bitmap downloadImage(String URL) {
		Bitmap bitmap = null;
		InputStream in = null;
		try {
			in = OpenHttpConnection(URL);
			bitmap = BitmapFactory.decodeStream(in);
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return bitmap;
	}

	private InputStream OpenHttpConnection(String urlString) throws IOException {
		InputStream in = null;
		int response = -1;

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		} catch (Exception ex) {
			throw new IOException("Error connecting");
		}
		return in;
	}

	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(image != null)
		{
			int left = ((getWidth())/2)-zoomController-posX;
			int top = ((getHeight())/2)-zoomController-posY;
			int right = ((getWidth())/2)+zoomController-posX;
			int bottom =  ((getHeight())/2)+zoomController-posY;
		image.setBounds(left, top,right ,bottom);
		image.draw(canvas);
		}

	}

	
	
	public void zoomImageOut() {
		zoomController -= 30;
		
		if (zoomController < minZoom)
			zoomController = minZoom;
		invalidate();

	}

	public void zoomImageIn() {
		zoomController += 30;
		if (zoomController < minZoom)
			zoomController = minZoom;
		invalidate();

	}
	

	public void panMap(float distanceX, float distanceY) {
		posX += (int) distanceX;
		posY += (int) distanceY;
		invalidate();
		
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP)// zoom in
			zoomImageIn();
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) // zoom out
			zoomImageOut();
		return true;
	}


}
