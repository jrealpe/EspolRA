package org.kokoa.espolra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.view.LayoutInflater;

public class GPSLocation implements Runnable{

	public Location currentLocation = null;
	public LocationManager mLocationManager;
	public Location mLocation;
	public MyLocationListener mLocationListener;
	public ProgressDialog pd;
	private String id_arbol,lat = "",lon= "",obs,nombre_area;
	public Activity activity;

		
	public GPSLocation(Activity activity){
		
		this.activity = activity;
		
	}

	// GPS
	private void setCurrentLocation(Location loc) {
		currentLocation = loc;
	}

	public void writeSignalGPS() {

		DialogInterface.OnCancelListener dialogCancel = new DialogInterface.OnCancelListener() {

			public void onCancel(DialogInterface dialog) {
				Toast.makeText(activity,"nada",Toast.LENGTH_LONG).show();
				handler.sendEmptyMessage(0);
			}

		};

		pd = ProgressDialog.show(activity, "GPS",
				"Searching GPS Satellites", true, true, dialogCancel);

		Thread thread = new Thread(this);
		thread.start();

	}

	@Override
	public void run() {

		mLocationManager = (LocationManager) activity.getSystemService(
				Context.LOCATION_SERVICE);

		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			Looper.prepare();

			mLocationListener = new MyLocationListener();

			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
			Looper.loop();
			Looper.myLooper().quit();

		} else {

			Toast.makeText(activity,"nada",Toast.LENGTH_LONG).show();
			

		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			mLocationManager.removeUpdates(mLocationListener);
			if (currentLocation != null) {

				lon = String.valueOf(currentLocation.getLongitude());
				lat = String.valueOf(currentLocation.getLatitude());
				
				Toast.makeText(activity, lon + " " + lat , Toast.LENGTH_LONG).show();
				// Guardado de Informacion de Planta en BD.
				
				
			
				
				// Mostrando el Fragment Informe Mensual
				
				
			}
		}
	};
	
	private Handler handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(activity,"nada",Toast.LENGTH_LONG).show();				
		}
	};

	private class MyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location loc) {
			if (loc != null) {
				
				handler2.sendEmptyMessage(0);
				setCurrentLocation(loc);
				handler.sendEmptyMessage(0);
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}
	
	

}