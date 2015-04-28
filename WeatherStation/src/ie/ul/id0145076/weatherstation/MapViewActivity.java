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
			lat = bundle.getDouble("LAT");
			System.out.println("lat from bundle "+lat);
			
			lng = bundle.getDouble("LONG");
			System.out.println("lng from bundle "+lng);
			
			
		}
		
		
		weatherDB = new WeatherStationDB(getApplicationContext());
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
		        .getMap();
		
		LatLng here = new LatLng(lat,lng);
		
		Marker thisLocation = map.addMarker(new MarkerOptions().position(here)
		          .title("Current Location"));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(here, 15));
		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
		
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
	
	private void display(String[] date, String[] pressure, Double[] lat, Double[] lng )
	{
		int length = date.length;
		Marker myMarkers[] = new Marker[length];
		
		
		
		for	(int i=0; i<length; i++)
		{
			myMarkers[i] = map.addMarker(new MarkerOptions()
	        .position(new LatLng(lng[i],lat[i]))
	        .title(date[i])
	        .snippet(pressure[i])
	        .icon(BitmapDescriptorFactory
	            .fromResource(R.drawable.atmospressure100)));
		}
	}
	private void showAllReadings()
	{
		display(weatherDB.getAllDate(), weatherDB.getAllPressure(), weatherDB.getAllLat(), weatherDB.getAllLng());
	}
}
