/**
*Atmospheric pressure taken at several fixed locations is used in meteorology to help forecast the weather. Most modern smartphones
*now include pressure, temperature and GPS sensors. This app aims to demonstrate how smartphones could be used to gather more 
*atmospheric pressure readings from more locations to create more accurate and localised weather forecasting.
*
*DISCLAIMER: Please don't rely on this app for weather forecast. It for demonstration purposes only and emulates an aneroid barometer.
*
*
*MainActivity.java - 	This is the main activity for the Android app Weather Station. Weather Station is an app that uses 
*						a smartphone's GPS and pressure sensors to capture the atmospheric pressure and GPS coordinates at
*						the devices current location and store this information in a SQLite database. The readings stored in
*						this database can then be used to help forecast the weather. The contents of this database can also
*						be displayed on a Google Map in the MapViewActivity.java activity.
*
*						On the main activity screen the app user can see their current address, the current atmospheric pressure
*						and a weather prediction graphic (with a pinch of salt). There are two buttons; one that saves the current data
*						to the database and another to view the database contents.
*
*
*@author Declan Moran - 0145076		
*@version 1.0										 
*/

package ie.ul.id0145076.weatherstation;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements SensorEventListener, LocationListener{

	//enum to store pressure status for forecasting
	public enum PressureStatus{
		FALLING, STABLE, RISING
	}
	
	//min time and distance for GPS updates
	public static final float MIN_DISTANCE = 10; //10 meters
	public static final long MIN_TIME = 35000;// 35 seconds
	protected static final String TAG = "addr";
	protected static final String LATITUDE = "LAT";
	protected static final String LONGITUDE = "LONG";
	
	
	//Barometer levels taken from http://www.sciencecompany.com/How-a-Barometer-Works.aspx
	public static final double LOW_LIMIT = 1009.14;
	public static final double HIGH_LIMIT = 1022.69;
	
	//sensor managers
	private LocationManager mLocationManager;
	private SensorManager mSensorManager;
	Location currentLocation;
	
	//GUI elements
	private Sensor mPressure;
	private TextView displayLocation;
	private TextView viewPressure;
	private ImageView displayWeather;
	private Button saveButton;
	private Button viewButton;
	PressureStatus status;
	
	//Database object
	private WeatherStationDB weatherDB;
	
	//Weather reading object
	private WeatherReading weatherReading; 
	
	

	
 
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        //Inflate GUI elements
        displayLocation = (TextView) findViewById(R.id.location);
        viewPressure = (TextView) findViewById(R.id.pressure);
        displayWeather = (ImageView) findViewById(R.id.weather);
        saveButton = (Button) findViewById(R.id.store);
        viewButton = (Button) findViewById(R.id.view);
        weatherReading = new WeatherReading();
        
        //Get location
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE); 
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
       
        
        
        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor        
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        
        //construct database
        weatherDB = new WeatherStationDB(getApplicationContext());
        
        //Setup date string to date reading stored in database
        Date date = new Date();
        String today = DateFormat.getDateInstance().format(date);
		weatherReading.setDate(today);//set todays date in weatherReading object
		
		/** 
		 * saveButton
		 * 
		 * An onClickListener that uses its onClick method to store the current pressure, GPS and date in the database;
		 * 
		 */
        saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				
				weatherDB.addRow(weatherReading.getPressure(),weatherReading.getDate() , weatherReading.getLonitude(), weatherReading.getLatitude());
			}
        });
        
        /** 
		 * viewButton
		 * 
		 * An onClickListener that uses its onClick method to initiate an intent to start the MapViewActivity Activity
		 * 
		 */
        //view button listener to display database contents on Google Map
        viewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				if (currentLocation != null)
				{
					//start text-based view as intent 
					//Intent viewWeatherReadingIntent = new Intent(getBaseContext(), ViewActivity.class);
					//startActivity(viewWeatherReadingIntent);
					
					//start map view intent and bundle the current location
					Intent viewMapIntent = new Intent(getBaseContext(), MapViewActivity.class);
					viewMapIntent.putExtra(LATITUDE, currentLocation.getLatitude());
					viewMapIntent.putExtra(LONGITUDE, currentLocation.getLongitude());
					startActivity(viewMapIntent);
					
				}
				//If location not updated display toast notification
				else
				{
					Toast.makeText(getApplicationContext(), R.string.location_update_wait, Toast.LENGTH_LONG).show(); //show toast if location not fixed yet 
				}
			}
        });
    }
    
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    
   

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu not used
        switch (item.getItemId())
        {
        	case R.id.start:
        		//method();
        		return true;
    		
        	case R.id.stop:
        		//method();
        		return true;
        		
    		default:
    			return super.onOptionsItemSelected(item);
        } 
    }
    
    
    @Override
    protected void onResume() {
      super.onResume();
      // Register a listener for the sensor.
      mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
	  
      //request location updates from network and GPS
	  mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
      mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
     
	 
      
      
    }

    @Override
    protected void onPause() {
    	super.onPause();
    	//unregister the sensor when the activity pauses.
    	mSensorManager.unregisterListener(this);
    	//unregister GPS when the activity pauses.
    	mLocationManager.removeUpdates(this);
      
      
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	/** 
	 * onSensorChanged(SensorEvent event)
	 * 
	 * As the pressure sensor reading changes - display the reading to screen and store in WeatherReading object
	 * perform calls to calculate if the pressure is rising or falling and call the forecast weather method.
	 * 
	 */
	@Override
	public void onSensorChanged(SensorEvent event)
	{
		float sensorValue = event.values[0];
			    
		viewPressure.setText(new DecimalFormat("##.##").format(sensorValue));//display pressure to screen
		weatherReading.setPressure(viewPressure.getText().toString());	//store reading in reading oject
		calcRiseFall(sensorValue);//calculate if pressure is rising or falling
		predictWeather(sensorValue);//forecast the weather
		
	}
	
	//GPS location methods
	/** 
	 * public void onLocationChanged(Location location)
	 * 
	 * method to handle GPS updates. Adds the current GPS location (Latitude, Longitude) to WeatherReading object
	 * calls the method displayAddress(Location) to geocode the latitude, longitude to an address.
	 * 
	 */
	public void onLocationChanged(Location location)
	{
		weatherReading.setLonitude(location.getLongitude());
		weatherReading.setLatitude(location.getLatitude());
		currentLocation = location;
		displayAddress(location);
		
		
	}
	
	public void onStatusChanged(String provider, int status, Bundle extras){
		
	}
	
	public void onProviderEnabled(String provider){
		
	}
	
	public void onProviderDisabled(String provider){
		
	}
	
	
	
	//Geocoding method to show current location as a text address 
	/** 
	 * public void displayAddress(Location location)
	 * 
	 * A method to turn the current location (Lat,Long) to a human readable address and display to screen
	 * 
	 * @param location current GPS location object
	 */
	public void displayAddress(Location location)
	{
		String errorMessage = "";
		double lon = location.getLongitude();
		double lat = location.getLatitude();
		
		Geocoder geocoder = new Geocoder(getBaseContext(),Locale.getDefault());
		List<Address> addresses = null;
		try {
			addresses = geocoder.getFromLocation(lat, lon, 1);
			
		}
		//catch network IO problems
		catch (IOException ex)
		{
			errorMessage = getString(R.string.service_not_available);
			Log.e(TAG, errorMessage, ex);
		}
		//catch invalid lat long
		catch (IllegalArgumentException ex)
		{
			errorMessage = getString(R.string.invalid_lat_long);
			Log.e(TAG, errorMessage+"Lat = "+lat+"Long = "+lon, ex);
		}
		//handle no address found
		if (addresses == null || addresses.size() == 0)
		{
			if (errorMessage.isEmpty())
			{
				displayLocation.setText(lat+", "+lon);
				errorMessage = getString(R.string.no_address_found);
				Log.e(TAG, errorMessage);
			}
			
		}
		else
		{
			displayLocation.setText("");//clear the TextView
			Address address = addresses.get(0);
    		ArrayList<String> addressFragments = new ArrayList<String>();
    		
    		for(int i=0; i<address.getMaxAddressLineIndex(); i++)
    		{
    			addressFragments.add(address.getAddressLine(i));
    		}
    		
    		for(String line:addressFragments)
    		{
    			displayLocation.append(line+" ");
    		}
			
		}
		
		
	}
	
	/** 
	 * public void calcRiseFall(float reading)
	 * 
	 * A method to determine if the pressure is rising, falling or stable.
	 * 
	 * If the pressure is less than yesterdays reading pressure is falling
	 * If the pressure is greater than yesterdays reading pressure is rising.
	 * If its the same pressure is stable
	 * 
	 * @param reading the current pressure reading from the pressure sensor
	 */
	public void calcRiseFall(float reading)
	{
		//get yesterdays date and format correctly for SQL query
		DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = dateFormat.format(cal.getTime());
		
		//query the database with yesterdays date
		String [] result = weatherDB.getPastPressure(yesterday);
		
		//test condition for no previous readings - set as stable and forecast
		 if (result.length == 0)
		 {
			 status = PressureStatus.STABLE;
		 }
		 else
		 {
			 //convert string double to double
			 double lastRead = Double.parseDouble(result[0]);
			 
			 //determine falling/rising
			 if (lastRead < reading  )
			 {
				 status = PressureStatus.FALLING;
			 }
			 else
			 {
				 status = PressureStatus.RISING;
			 }
			 
		 }
		 
	}
	
	/** 
	 * public void predictWeather(float reading)
	 * 
	 * A method to forecast the weather based on rising/falling pressure over time 
	 * and the current reading
	 * 
	 * Disclaimer: Forecast is not accurate!!. If app forecasts sunshine and it rains I'm not responsible!!
	 *
	 * 
	 * @param reading the current pressure reading from the pressure sensor
	 */
	public void predictWeather(float reading)
	{
		if (reading < LOW_LIMIT )
		{
				if(status==PressureStatus.STABLE || status==PressureStatus.FALLING)
				{
					//set image rain
					displayWeather.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.rain));
				}
				else
				{
					//set image overcast
					displayWeather.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.overcast));
				}	
		}else if (reading >= LOW_LIMIT && reading <= HIGH_LIMIT )
		{
			
				if(status==PressureStatus.RISING || status==PressureStatus.STABLE)
				{
					//set image broken sunshine/cloud
					displayWeather.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.suncloud));
				}
				else
				{
					//set image overcast
					displayWeather.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.overcast));
				}
		}else if (reading >= HIGH_LIMIT)
			{
			
				if(status==PressureStatus.RISING || status==PressureStatus.STABLE)
				{
					//set image sunny
					displayWeather.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.sunny));
				}
				else
				{
					//set image broken sunshine/cloud
					displayWeather.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.suncloud));
				}
			}
			
	}
	

}
