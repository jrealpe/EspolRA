package org.kokoa.espolra;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class Maps extends FragmentActivity  {
	String id_supevisor = "";
	String longi;
	String lati;
	JSONArray results;
	JSONParser jsonParser;
	String URL="";
	 private GoogleMap googleMap;
	
	/** Called when the activity is first created. */

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maps);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		jsonParser=new JSONParser();
        results=new JSONArray();
		URL = this.getIntent().getExtras().getString("url");
		results = jsonParser.getJSONFromUrl(URL);
		
		try {
            // Loading map
            initilizeMap();
            googleMap.setMyLocationEnabled(true);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(-2.146059799999991,-79.95848261534422)).zoom(13).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }
		/*
		 * JSONOBJECT
                     "streetAddress":"Kokoa",
                     "lat":"-2.1453085026708800",
                     "lng":"-79.96610602903290",
                     "titleNoFormatting":"ADMINISTRATIVOS"
        */
		
		try {
			for (int i = 0; i < results.length(); i++) {
				JSONObject buf = results.getJSONObject(i);

				String lugar = buf.getString("streetAddress");
				String tipo = buf.getString("titleNoFormatting");
				String longitud = buf.getString("lng");
				String latitud = buf.getString("lat");
				if((longitud==null&&latitud==null)||(longitud.compareTo("null")==0&&latitud.compareTo("null")==0)){
					longitud=""+0.00;
					latitud=""+0.00;
				}
				// create marker
				MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud))).title("Lugar.:"+ lugar+"\nTipo:"+tipo);
				marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_lugar));
				// adding marker
				googleMap.addMarker(marker);
				
			}
			
			//Pruebas
			/*
			MarkerOptions marker = new MarkerOptions().position(new LatLng(-2.1540036,-79.9238942)).title("Lugar.:"+ "Augusto"+"\nTipo:"+"LABORATORIOS");
			marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_lugar));
			googleMap.addMarker(marker);
			
			
			MarkerOptions marker2 = new MarkerOptions().position(new LatLng(-2.1606613,-79.92521669999996)).title("Lugar.:"+ "Federacion"+"\nTipo:"+"LABORATORIOS");
			marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_lugar));
			googleMap.addMarker(marker2);
			
			MarkerOptions marker3 = new MarkerOptions().position(new LatLng(-2.1576084,-79.92519709999999)).title("Lugar.:"+ "Mapa"+"\nTipo:"+"LABORATORIOS");
			marker3.icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_lugar));
			googleMap.addMarker(marker3);
			
			MarkerOptions marker4 = new MarkerOptions().position(new LatLng(-2.161212,-79.9250232)).title("Lugar.:"+ "Otro"+"\nTipo:"+"LABORATORIOS");
			marker4.icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_lugar));
			googleMap.addMarker(marker4);
			*/
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

	}
	
	  /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
        	SupportMapFragment fm = (SupportMapFragment)   getSupportFragmentManager().findFragmentById(R.id.map);
        	googleMap = fm.getMap();
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

	public void AlertBox(String title, String message) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message)
				.setPositiveButton("OK", new OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

					}
				}).show();
	}

}