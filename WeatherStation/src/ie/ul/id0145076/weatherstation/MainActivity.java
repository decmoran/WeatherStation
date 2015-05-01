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

	public enum PressureStatus{
		FALLING, STABLE, RISING
	}
	//min time and distance for GPS updates
	public static final float MIN_DISTANCE = 10; //10 meters
	public static final long MIN_TIME = 35000;// 35 seconds
	protected static final String TAG = "addr";
	Location currentLocation;
	
	//Barometer level
	public static final double LOW_LIMIT = 1009.14;
	public static final double HIGH_LIMIT = 1022.69;
	
	//sensor managers
	private LocationManager mLocationManager;
	private SensorManager mSensorManager;
	
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
        
        
        //GUI elements
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
        
        //Setup date string
        Date date = new Date();
        String today = DateFormat.getDateInstance().format(date);
        
		
		weatherReading.setDate(today);//set todays date in weatherReading object
		
		//save button Listener
        saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				
				weatherDB.addRow(weatherReading.getPressure(),weatherReading.getDate() , weatherReading.getLonitude(), weatherReading.getLatitude());
			}
        });
        
        //view button listener
        viewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				if (currentLocation != null)
				{
					//start text-based view as intent
					//Intent viewWeatherReadingIntent = new Intent(getBaseContext(), ViewActivity.class);
					//startActivity(viewWeatherReadingIntent);
					
					//start map view as intent
					Intent viewMapIntent = new Intent(getBaseContext(), MapViewActivity.class);
					viewMapIntent.putExtra("LAT", currentLocation.getLatitude());
					viewMapIntent.putExtra("LONG", currentLocation.getLongitude());
					startActivity(viewMapIntent);
					
				}
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
	  
      //request location updates
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

	@Override
	public void onSensorChanged(SensorEvent event)
	{
		// TODO Auto-generated method stub
		
		
		float sensorValue = event.values[0];
			    
		viewPressure.setText(new DecimalFormat("##.##").format(sensorValue));
		weatherReading.setPressure(viewPressure.getText().toString());	
		calcRiseFall(sensorValue);
		predictWeather(sensorValue);
		
	}
	
	//GPS location methods
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
			displayLocation.setText("");
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
	

	public void calcRiseFall(float reading)
	{
		//get yesterdays date and format correctly
		DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = dateFormat.format(cal.getTime());
		String [] result = weatherDB.getPastPressure(yesterday);
		 if (result.length == 0)
		 {
			 status = PressureStatus.STABLE;
		 }
		 else
		 {
			 double lastRead = Double.parseDouble(result[0]);
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
	
	public void predictWeather(float reading)
	{
		//double reading = Double.parseDouble(weatherReading.getPressure());
		if (reading < LOW_LIMIT )
		{
			if(status==PressureStatus.STABLE || status==PressureStatus.FALLING)
			{
				//set image
				displayWeather.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.rain));
			}
			else
			{
				displayWeather.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.overcast));
			}	
		}else if (reading >= LOW_LIMIT && reading <= HIGH_LIMIT ){
			
			if(status==PressureStatus.RISING || status==PressureStatus.STABLE)
			{
				displayWeather.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.suncloud));
			}
			else
			{
				displayWeather.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.overcast));
			}
		}else if (reading >= HIGH_LIMIT){
			
			if(status==PressureStatus.RISING || status==PressureStatus.STABLE)
			{
				displayWeather.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.sunny));
			}
			else
			{
				displayWeather.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.suncloud));
			}
		}
			
	}
	

}
