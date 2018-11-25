package com.bmh.trackchild.helper;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/***
 * Location Helper Class. 
 * Handles creation of the Location Manager and Location Listener.
 *
 * @author Scott Helme
 */
public class LocationHelper{

	//variables to store lat and long
	private double latitude  = 0.0;
	private double longitude = 0.0;

	//flag for when we have co-ords
	private boolean isGotLocation = false;

	//my location manager and listener
	private LocationManager    locationManager;
	private MyLocationListener locationListener;

	/**
	 * Constructor.
	 *
	 * @param context - The context of the calling activity.
	 */
    @SuppressLint("MissingPermission")
	public LocationHelper(Context context)
    {
        //setup the location manager
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        //create the location listener
        locationListener = new MyLocationListener();

        //setup a callback for when the GRPS/WiFi gets a lock and we receive data

        if(locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
       // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        //setup a callback for when the GPS gets a lock and we receive data
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
	
    /***
     * Used to receive notifications from the Location Manager when they are sent. These methods are called when
     * the Location Manager is registered with the Location Service and a state changes.
     *
     * @author Scott Helme
     */
	public class MyLocationListener implements LocationListener {
		
		//called when the location service reports a change in location
		@Override
		public void onLocationChanged(Location location) {
			
			//store lat and long
			latitude = location.getLatitude();
			longitude = location.getLongitude();

			//now we have our location we can stop the service from sending updates
			//comment out this line if you want the service to continue updating the users location
		//	locationManager.removeUpdates(locationListener);
			
			//change the flag to indicate we now have a location
			isGotLocation = true;
		}

		//called when the provider is disabled
		@Override
		public void onProviderDisabled(String provider) {}
		//called when the provider is enabled
		@Override
		public void onProviderEnabled(String provider) {}
		//called when the provider changes state
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	}
	
	/***
	 * Stop updates from the Location Service.
	 */
	public void killLocationServices(){
		locationManager.removeUpdates(locationListener);
	}
	
	/***
	 * Get Latitude from GPS Helper. 
	 * Should check gotLocation() first.
	 * @return - The current Latitude.
	 */
	public double getLat(){
		return latitude;
	}
	
	/***
	 * Get Longitude from GPS Helper. 
	 * Should check gotLocation() first.
	 * @return - The current Longitude.
	 */
	public double getLong(){
		return longitude;
	}
	
	/***
	 * Check if a location has been found yet.
	 * @return - True if a location has been acquired. False otherwise.
	 */
	public boolean gotLocation(){
		return isGotLocation;
	}	
	
	
	
}