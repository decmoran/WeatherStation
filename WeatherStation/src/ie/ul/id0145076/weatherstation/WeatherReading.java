/**
*WeatherReading.java - 	This is a class to store a weather reading. It contains a date, pressure reading, a latitude and a longitude field
*						and the methods to get and set the fields.
*
*@author Declan Moran - 0145076		
*@version 1.0										 
*/
package ie.ul.id0145076.weatherstation;

public class WeatherReading 
{
	//attributes
	private String date;
	private String pressure;
	private double latitude;
	private double lonitude;
	
	//constructor
	public WeatherReading(String date, String pressure, double lat, double lon)
	{
		this.date = date;
		this.pressure = pressure;
		this.latitude = lat;
		this.lonitude = lon;
	}
	//default constructor
	public WeatherReading()
	{
		
	}
	//setters and getters
	 /** 
	 * public String getDate()
	 * 
	 * return the date field from object
	 * 
	 * @return date field
	 * 
	 */
	public String getDate() {
		return date;
	}
	
	 /** 
	 * public void setDate(String date)
	 * 
	 * set the date field
	 * 
	 * @param date the date value to set
	 * 
	 */
	public void setDate(String date) {
		this.date = date;
	}
	
	 /** 
	 * public String getPressure()
	 * 
	 * method to return the pressure field
	 * 
	 * @return the pressure field
	 * 
	 */
	public String getPressure() {
		return pressure;
	}
	
	 /** 
	 * public void setPressure(String pressure)
	 * 
	 * method to set the pressure field
	 * 
	 * @param pressure the pressure value to set
	 * 
	 */
	public void setPressure(String pressure) {
		this.pressure = pressure;
	}
	
	 /** 
	 * 	public double getLatitude()
	 * 
	 * method to return the objects latitude
	 * 
	 * @return the latitude
	 * 
	 */
	public double getLatitude() {
		return latitude;
	}
	
	 /** 
	 * public void setLatitude(double latitude)
	 * 
	 * method to set the latitude field
	 * 
	 * @param latitude the latitude value to set
	 * 
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	 /** 
	 * public double getLonitude()
	 * 
	 * method to return the objects longitude
	 * 
	 * @return the longitude
	 * 
	 */
	public double getLonitude() {
		return lonitude;
	}
	
	 /** 
	 * public void setLonitude(double lonitude)
	 * 
	 * method to set the longitude field
	 * 
	 * @param longitude the longitude to set
	 * 
	 */
	public void setLonitude(double lonitude) {
		this.lonitude = lonitude;
	}
	
	

}
