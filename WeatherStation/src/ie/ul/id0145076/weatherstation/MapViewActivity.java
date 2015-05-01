/**
*MapViewActivity.java - This is the MapView Activity which zooms to the users current location and displays all the recorded
*						pressure readings from the database on the Google map on the location of their recording.
*
*						Each record is displayed as a marker with a custom icon and upon clicking the icon it displays the date 
*						and pressure recorded.
*						
*
*@author Declan Moran - 0145076	
*@version 1.0											 
*/

package ie.ul.id0145076.weatherstation;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapViewActivity extends FragmentActivity {
	
	//Strings for lat and long bundle items
	protected static final String LATITUDE = "LAT";
	protected static final String LONGITUDE = "LONG";
	
	private WeatherStationDB weatherDB;
	private double lat;
	private double lng;
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);
		
		Bundle bundle = getIntent().getExtras();
		
		if(bundle != null)
		{
			lat = bundle.getDouble(LATITUDE);
			lng = bundle.getDouble(LONGITUDE);
		}
		
		//create database object
		weatherDB = new WeatherStationDB(getApplicationContext());
		
		//inflate map
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
		        .getMap();
		
		//create a LatLong object with the current location extracted from bundle
		LatLng here = new LatLng(lat,lng);
		
		//add a marker on the map with the current location
		Marker thisLocation = map.addMarker(new MarkerOptions().position(here).title("Current Location"));
		//zoom to current location
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(here, 15));
		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
		
		//display database records
		showAllReadings();
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/** 
	 * private void display(String[] date, String[] pressure, Double[] lat, Double[] lng )
	 * 
	 * creates an array of markers to display on map. each marker contains a record from the database
	 * 
	 * @param date array of dates from database
	 * @param pressure array of pressure values from database
	 * @param lat array of latitudes from database
	 * @param lng array of Longitudes from database
	 */
	private void display(String[] date, String[] pressure, Double[] lat, Double[] lng )
	{
		//length variable for marker array size
		int length = date.length;
		Marker myMarkers[] = new Marker[length];
		
		
		
		for	(int i=0; i<length; i++)
		{
			myMarkers[i] = map.addMarker(new MarkerOptions()
	        .position(new LatLng(lng[i],lat[i]))//create new location and set marker to this position
	        .title(date[i])//set the date as the marker title
	        .snippet(pressure[i])//set the snippet text to the pressure reading
	        .icon(BitmapDescriptorFactory.fromResource(R.drawable.atmospressure100)));//set marker custom icon
		}
	}
	
	/** 
	 * private void showAllReadings()
	 * 
	 *retrive all records from the database and display them using the display method
	 */
	private void showAllReadings()
	{
		display(weatherDB.getAllDate(), weatherDB.getAllPressure(), weatherDB.getAllLat(), weatherDB.getAllLng());
	}
}
