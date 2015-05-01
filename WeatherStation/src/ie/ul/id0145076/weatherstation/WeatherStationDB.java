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
	

	/*********************    Definition of table columns ***********************************************/
  //The index (key) column name for use in where clauses.
  public static final String KEY_ID = "_id";
  
  //The name and column index of each column in your database.
  //These should be descriptive.
  public static final String KEY_PRESSURE_READING =  
		  "pressure_reading";
  public static final String KEY_DATE =
		  "date";
  public static final String KEY_LAT = 
		  "latitude";
  public static final String KEY_LONG =
		  "Longitude";
  
  // Database open/upgrade helper
  private ModuleDBOpenHelper moduleDBOpenHelper;

  /************************    Constructor ***************************************************************/
  public WeatherStationDB(Context context) {
    moduleDBOpenHelper = new ModuleDBOpenHelper(context, ModuleDBOpenHelper.DATABASE_NAME, null, 
                                              ModuleDBOpenHelper.DATABASE_VERSION);
    
    // populate the database with some data in case it is empty
    if (getAll().length == 0) {
	    this.addRow("0000.00", "27/03/2015", 0000.000f, 0000.000f);
	    
    }
  }
  
/************************    Standard Database methods *************************************************/
  
  // Called when you no longer need access to the database.
  public void closeDatabase() {
    moduleDBOpenHelper.close();
  }

  public void addRow(String pressure, String date, double latitude, double longitude) {
      // Create a new row of values to insert.
	    ContentValues newValues = new ContentValues();
	  
	    // Assign values for each row.
	    newValues.put(KEY_PRESSURE_READING, pressure);
	    newValues.put(KEY_DATE, date);
	    newValues.put(KEY_LAT, latitude);
	    newValues.put(KEY_LONG, longitude);
	    
	    // Insert the row into your table
	    SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
	    db.insert(ModuleDBOpenHelper.DATABASE_TABLE, null, newValues); 
  }
  
  public void deleteRow(int idNr) {
    // Specify a where clause that determines which row(s) to delete.
    // Specify where arguments as necessary.
    String where = KEY_ID + "=" + idNr;
    String whereArgs[] = null;
  
    // Delete the rows that match the where clause.
    SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
    db.delete(ModuleDBOpenHelper.DATABASE_TABLE, where, whereArgs);
  }
  
  public void deleteAll() {
	  String where = null;
	    String whereArgs[] = null;
	  
	    // Delete the rows that match the where clause.
	    SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
	    db.delete(ModuleDBOpenHelper.DATABASE_TABLE, where, whereArgs);
  }

  /************************    User specific database queries *******************************************/
   
  /*
   * Obtain all database entries and return as human readable content in a String array
   * A query with all fields set to null will result in the whole database being returned
   * The following SQL query is implemented: SELECT * FROM  Beers
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
	    //
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
  
  
  
  
  /*
   * This is a helper class that takes a lot of the hassle out of using databases. Use as is and complete the following as required:
   * 	- DATABASE_TABLE
   * 	- DATABASE_CREATE 
   */
  private static class ModuleDBOpenHelper extends SQLiteOpenHelper {
	    
	    private static final String DATABASE_NAME = "myDatabase.db";
	    private static final String DATABASE_TABLE = "Weather";
	    private static final int DATABASE_VERSION = 1;
	    
	    // SQL Statement to create a new database.
	    //III. Add volume column to database
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
