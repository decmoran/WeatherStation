/**
*WeatherStationDB.java - 	This is the database class to build the database to store the pressure readings.
*							It also contains methods initiate SQL queries on the database.
*
*						
*
*@author Declan Moran - 0145076		
*@version 1.0										 
*/
package ie.ul.id0145076.weatherstation;


import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class WeatherStationDB {
	

	/*Define tables*/
  //The index (key) column name for use in where clauses.
  public static final String KEY_ID = "_id";
  
  //The name of each column in the database.
  public static final String KEY_PRESSURE_READING = "pressure_reading";
  public static final String KEY_DATE = "date";
  public static final String KEY_LAT = "latitude";
  public static final String KEY_LONG = "Longitude";
  
  // Database open/upgrade helper
  private ModuleDBOpenHelper moduleDBOpenHelper;

 //construct the database
  public WeatherStationDB(Context context) {
    moduleDBOpenHelper = new ModuleDBOpenHelper(context, ModuleDBOpenHelper.DATABASE_NAME, null, ModuleDBOpenHelper.DATABASE_VERSION);
    
    // add test reading to database
    if (getAll().length == 0) {
	    this.addRow("0000.00", "27/03/2015", 0000.000f, 0000.000f);
	    
    }
  }
  
  /*Database Methods for inserting and retrieving data from database*/
  
  // close the database
  public void closeDatabase() {
    moduleDBOpenHelper.close();
  }

  /** 
	 * public void addRow(String pressure, String date, double latitude, double longitude)
	 * 
	 * method to insert new row of data to database.
	 * 
	 * @param pressure pressure reading to insert
	 * @param date current date to insert
	 * @param latitude latitude to insert
	 * @param longitude longitude to insert
	 */
  public void addRow(String pressure, String date, double latitude, double longitude) {
     
	    ContentValues newValues = new ContentValues();
	  
	    // add the values to each row
	    newValues.put(KEY_PRESSURE_READING, pressure);
	    newValues.put(KEY_DATE, date);
	    newValues.put(KEY_LAT, latitude);
	    newValues.put(KEY_LONG, longitude);
	    
	    // SQL query to insert data into database table
	    SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
	    db.insert(ModuleDBOpenHelper.DATABASE_TABLE, null, newValues); 
  }
  
  /** 
 	 *   public void deleteRow(int idNr)
 	 * 
 	 * delete the specified row of the database
 	 * 
 	 * @param idNr the key of the row to delete
 	 */
  public void deleteRow(int idNr) {
  
	//construct query to delete the row
    String where = KEY_ID + "=" + idNr;
    String whereArgs[] = null;
  
    // execute query
    SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
    db.delete(ModuleDBOpenHelper.DATABASE_TABLE, where, whereArgs);
  }
  
  /** 
	 *    public void deleteAll())
	 * 
	 * delete the database
	 * 
	 */
  public void deleteAll() {
	  String where = null;
	    String whereArgs[] = null;
	  
	    //execute delete query
	    SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
	    db.delete(ModuleDBOpenHelper.DATABASE_TABLE, where, whereArgs);
  }

  /** 
 	 * public String[] getAll()
 	 * 
 	 * return database contents
 	 * 
 	 */
  public String[] getAll() {

	  ArrayList<String> outputArray = new ArrayList<String>();
	  String[] result_columns = new String[] { 
		      KEY_PRESSURE_READING, KEY_DATE, KEY_LAT, KEY_LONG}; 
		    
	  
	  String pressureReading;
	  String date;
	  double latitude;
	  double longitude;
	  
	    String where = null;
	    String whereArgs[] = null;
	    String groupBy = null;
	    String having = null;
	    String order = null;
	    
	    SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
	    Cursor cursor = db.query(ModuleDBOpenHelper.DATABASE_TABLE, 
	                             result_columns, where,
	                             whereArgs, groupBy, having, order);
	   
	    boolean result = cursor.moveToFirst();
		  while (result) {
			  pressureReading = cursor.getString(cursor.getColumnIndex(KEY_PRESSURE_READING));
			  date = cursor.getString(cursor.getColumnIndex(KEY_DATE));
			  latitude = cursor.getDouble(cursor.getColumnIndex(KEY_LAT));
			  longitude= cursor.getDouble(cursor.getColumnIndex(KEY_LONG));
			  
			  outputArray.add(pressureReading+" "+date+" "+latitude+" "+longitude);
			  result=cursor.moveToNext();

			  }
		  return outputArray.toArray(new String[outputArray.size()]);
  }
  
  /** 
	 * public Double[] getAllLat()
	 * 
	 * return all latitude readings
	 * 
	 */
  public Double[] getAllLat() {

	  ArrayList<Double> outputArray = new ArrayList<Double>();
	  String[] result_columns = new String[] { KEY_LAT}; 
		    
	  
	
	  double latitude;
	  
	    String where = null;
	    String whereArgs[] = null;
	    String groupBy = null;
	    String having = null;
	    String order = null;
	    
	    SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
	    Cursor cursor = db.query(ModuleDBOpenHelper.DATABASE_TABLE, 
	                             result_columns, where,
	                             whereArgs, groupBy, having, order);
	    //
	    boolean result = cursor.moveToFirst();
		  while (result) {
			  
			  latitude = cursor.getDouble(cursor.getColumnIndex(KEY_LAT));
			 
			  
			  outputArray.add(latitude);
			  result=cursor.moveToNext();

			  }
		  return outputArray.toArray(new Double[outputArray.size()]);
  }
  
  /** 
	 * public Double[] getAllLng()
	 * 
	 * return all longitude readings
	 * 
	 */
  public Double[] getAllLng() {

	  ArrayList<Double> outputArray = new ArrayList<Double>();
	  String[] result_columns = new String[] { KEY_LONG}; 
		    
	  
	
	  double longitude;
	 
	  
	    String where = null;
	    String whereArgs[] = null;
	    String groupBy = null;
	    String having = null;
	    String order = null;
	    
	    SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
	    Cursor cursor = db.query(ModuleDBOpenHelper.DATABASE_TABLE, 
	                             result_columns, where,
	                             whereArgs, groupBy, having, order);
	    //
	    boolean result = cursor.moveToFirst();
		  while (result) {
			  
			  longitude = cursor.getDouble(cursor.getColumnIndex(KEY_LONG));
			 
			  
			  outputArray.add(longitude);
			  result=cursor.moveToNext();

			  }
		  return outputArray.toArray(new Double[outputArray.size()]);
  }
  
  
  /** 
	 * String[] getAllPressure()
	 * 
	 * return all pressure readings
	 * 
	 */
  public String[] getAllPressure() {

	  ArrayList<String> outputArray = new ArrayList<String>();
	  String[] result_columns = new String[] { 
		      KEY_PRESSURE_READING}; 
		    
	  
	  String pressureReading;
	 
	  
	    String where = null;
	    String whereArgs[] = null;
	    String groupBy = null;
	    String having = null;
	    String order = null;
	    
	    SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
	    Cursor cursor = db.query(ModuleDBOpenHelper.DATABASE_TABLE, 
	                             result_columns, where,
	                             whereArgs, groupBy, having, order);
	    //
	    boolean result = cursor.moveToFirst();
		  while (result) {
			  pressureReading = cursor.getString(cursor.getColumnIndex(KEY_PRESSURE_READING));
		
			  
			  outputArray.add(pressureReading);
			  result=cursor.moveToNext();

			  }
		  return outputArray.toArray(new String[outputArray.size()]);
  }
 
  /** 
 	 * public String[] getAllDate()
 	 * 
 	 * return all date items
 	 * 
 	 */
  public String[] getAllDate() {

	  ArrayList<String> outputArray = new ArrayList<String>();
	  String[] result_columns = new String[] { KEY_DATE}; 
		    
	  

	  String date;

	    String where = null;
	    String whereArgs[] = null;
	    String groupBy = null;
	    String having = null;
	    String order = null;
	    
	    SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
	    Cursor cursor = db.query(ModuleDBOpenHelper.DATABASE_TABLE, 
	                             result_columns, where,
	                             whereArgs, groupBy, having, order);
	    //
	    boolean result = cursor.moveToFirst();
		  while (result) {
			  date = cursor.getString(cursor.getColumnIndex(KEY_DATE));
			  
			  outputArray.add(date);
			  result=cursor.moveToNext();

			  }
		  return outputArray.toArray(new String[outputArray.size()]);
  }
  
  /** 
	 *  public String[] getPastPressure(String yesterday)
	 * 
	 * return all pressure readings from the previous day
	 * 
	 * @param yesterday string with the previous days date
	 */
  public String[] getPastPressure(String yesterday) {

	  ArrayList<String> outputArray = new ArrayList<String>();
	  String[] result_columns = new String[] { 
		      KEY_PRESSURE_READING}; 
		    
	  
	  	String pressureReading;
	 
	  	String where = KEY_DATE + "= ?";
	    String whereArgs[] = {yesterday};
	    String groupBy = null;
	    String having = null;
	    String order = null;
	    
	    SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
	    Cursor cursor = db.query(ModuleDBOpenHelper.DATABASE_TABLE, 
	                             result_columns, where,
	                             whereArgs, groupBy, having, order);
	    //
	    boolean result = cursor.moveToFirst();
		  while (result) {
			  pressureReading = cursor.getString(cursor.getColumnIndex(KEY_PRESSURE_READING));
		
			  
			  outputArray.add(pressureReading);
			  result=cursor.moveToNext();

			  }
		  return outputArray.toArray(new String[outputArray.size()]);
  }
  
  
  
  
  /*Create the database and table*/
  private static class ModuleDBOpenHelper extends SQLiteOpenHelper {
	    
	    private static final String DATABASE_NAME = "myDatabase.db";
	    private static final String DATABASE_TABLE = "Weather";
	    private static final int DATABASE_VERSION = 1;
	    
	    // build SQL statement to create table
	    private static final String DATABASE_CREATE = "create table " +
	      DATABASE_TABLE + " (" + KEY_ID +
	      " integer primary key autoincrement, " +
	      KEY_PRESSURE_READING + " text, " +
	      KEY_DATE + " text, " +
	      KEY_LAT + " double, " +
	      KEY_LONG + " double);";

	    
	    
	    public ModuleDBOpenHelper(Context context, String name,
	                      CursorFactory factory, int version) {
	      super(context, name, factory, version);
	    }

	    // Called when no database exists in disk and the helper class needs
	    // to create a new one.
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	      db.execSQL(DATABASE_CREATE);
	    }

	    // Called when there is a database version mismatch meaning that
	    // the version of the database on disk needs to be upgraded to
	    // the current version.
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, 
	                          int newVersion) {
	      // Log the version upgrade.
	      Log.w("TaskDBAdapter", "Upgrading from version " +
	        oldVersion + " to " +
	        newVersion + ", which will destroy all old data");

	      // Upgrade the existing database to conform to the new 
	      // version. Multiple previous versions can be handled by 
	      // comparing oldVersion and newVersion values.

	      // The simplest case is to drop the old table and create a new one.
	      db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
	      // Create a new one.
	      onCreate(db);
	    }
	  } 


}
