package ie.ul.id0145076.weatherstation;

public class WeatherReading 
{
	private String date;
	private String pressure;
	private double latitude;
	private double lonitude;
	
	public WeatherReading(String date, String pressure, double lat, double lon)
	{
		this.date = date;
		this.pressure = pressure;
		this.latitude = lat;
		this.lonitude = lon;
	}
	public WeatherReading()
	{
		
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPressure() {
		return pressure;
	}
	public void setPressure(String pressure) {
		this.pressure = pressure;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLonitude() {
		return lonitude;
	}
	public void setLonitude(double lonitude) {
		this.lonitude = lonitude;
	}
	
	

}
