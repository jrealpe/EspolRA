package org.mixare.data;

import java.net.URLDecoder;
import java.util.ArrayList;

import org.kokoa.espolra.GPSLocation;
import org.kokoa.espolra.MainActivity;
import org.mixare.Marker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * DataHandler is the model which provides the Marker Objects.
 * 
 * DataHandler is also the Factory for new Marker objects.
 */
public class DataHandler {

	private static final int MAX_OBJECTS = 50;
	private ArrayList<Marker> markerList = new ArrayList<Marker>();

	private LocationManager locManager;
	private LocationListener locListener;
	
	public void createMarker(String title, double latitude, double longitude, double elevation, String link) {
		if (markerList.size() < MAX_OBJECTS) {
			String URL = null;
			if (link != null && link.length() > 0)
				URL = "webpage:" + URLDecoder.decode(link);
			Marker ma = new Marker(title, latitude, longitude, elevation, URL);
			markerList.add(ma);
		}
	}
	
    public static double distDe(double lat1, double lng1, double lat2, double lng2) {  
        double earthRadius = 6371;//kilometers  
        double dLat = Math.toRadians(lat2 - lat1);  
        double dLng = Math.toRadians(lng2 - lng1);  
        double sindLat = Math.sin(dLat / 2);  
        double sindLng = Math.sin(dLng / 2);  
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)  
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));  
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));  
        double dist = earthRadius * c;  
  
        return dist;  
    }      
	
	//android10 added
	public void createMarker(String title, String address, String phonenumber, double latitude, double longitude, double elevation, String link, Location l) {
		if (markerList.size() < MAX_OBJECTS) {
		    double dis;
		    int radio;
			dis = distDe(l.getLatitude(), l.getLongitude(), latitude, longitude)*1000;
			radio=MainActivity.DimensionRadio;
			if (dis < radio){
				Marker ma = new Marker(title, address, phonenumber, latitude, longitude, elevation, link);
				markerList.add(ma);
			}
		}
	}
	
	/**
	 * @deprecated Nobody should get direct access to the list
	 */
	public ArrayList getMarkerList() {
		return markerList;
	}
	
	/**
	 * @deprecated Nobody should get direct access to the list
	 */
	public void setMarkerList(ArrayList markerList) {
		this.markerList = markerList;
	}

	public int getMarkerCount() {
		return markerList.size();
	}
	
	public Marker getMarker(int index) {
		return markerList.get(index);
	}
}
