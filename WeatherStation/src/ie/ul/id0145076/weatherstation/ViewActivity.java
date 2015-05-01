/**
 * ****REPLACED  WITH***  MapViewActivity.java  ****REPLACED  WITH****
 * 
*ViewActivity.java - 	This is an activity to display the content of the database in text format. It has been replaced with the MapViewActivity.java
*
*@author Declan Moran - 0145076		
*@version 1.0										 
*/

package ie.ul.id0145076.weatherstation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewActivity extends Activity {
	private WeatherStationDB weatherDB;
	
	
	TextView dbView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		
		weatherDB = new WeatherStationDB(getApplicationContext());
		
		dbView = (TextView) findViewById(R.id.database_contents);
		
		showFullDatabase();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view, menu);
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
	
	private void print(String[] content)
	{
		dbView.setText("");
		for	(int i=0; i<content.length; i++)
		{
			dbView.append(content[i]+"\n");
		}
	}
	private void showFullDatabase()
	{
		print(weatherDB.getAll());
	}
}
