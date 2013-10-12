package com.cwardcode.TranTracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;

/**
 * Uses the device's GPS to determine it's location and sends this infomration
 * to the broadcast receiver.
 * 
 * @author  Hayden Thomas
 * @author  Chris Ward
 * @version September 9, 2013
 */
public class SendLoc extends Service {
	
	/**Provides access to the device's GPS services.*/
    private LocationManager lm;
    /**Listens for location updates from the LocationManager.*/
    private LocationListener locListener;
    /**Used to identify the vehicle that is being tracked by this device.*/
    private static int vehicleID;
    
    /**
     * Listens for changes in the device's location.
     */
    private class MyLocationListener implements LocationListener
    {
    	/**
    	 * Updates the device's location.
    	 * 
    	 * @param location the device's current location.	 
    	 */
        @Override
        public void onLocationChanged(Location location)
        {
            if(location != null) {
                updateLocation(location);
            }
        }
        
        /**
         * Called when the provider is enabled by the user. This method is not
         * supported in this implementation.
         * 
         * @param provider the provider that would be enabled. 
         */
        @Override
        public void onProviderEnabled(String provider){}
        
        /**
         * Called when the provider is disabled by the user. This method is not
         * supported in this implementation.
         * 
         * @param provider the provider that would be disabled.
         */
        @Override
        public void onProviderDisabled(String provider){}
        
        /**
         * Called when the provider status changes. This method is not
         * supported in this implementation.
         * 
         * @param provider the name of the location provider.
         * @param status an integer representing the status of the provider.
         * @param extras an optional bundle containing provider specific status
         *               variables.
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){}
    }

    /**
     * Creates the service.
     */
    public void onCreate() {
        super.onCreate();
    }
    
    /**
     * Initializes the service.
     * 
     * @param intent The Intent that provides the service with the vehicleID.
     * @param startID a unique identifier for this request.
     */
    public void onStart(Intent intent, int startID){
        addLocationListener();
        vehicleID = intent.getIntExtra("VehicleID", -1);
    }

    /**
     * Creates a new LocationListener and begins listening.
     */
    private void addLocationListener() {
        Thread sendLocThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Looper.prepare();
                    lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

                    Criteria c = new Criteria();
                    c.setAccuracy(Criteria.ACCURACY_FINE);

                    locListener = new MyLocationListener();
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locListener);
                    Looper.loop();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }, "SendLocThread");

        sendLocThread.start();
    }

    /**
     * Updates the current location.
     * 
     * @param location the device's current Location.
     */
    public static void updateLocation(Location location)
    {
        Context context;
        context = TranTracker.getAppContext();

        double latitude, longitude, speed;

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        speed = location.getSpeed();

        Intent filterRes = new Intent();
        filterRes.setAction("com.cwardcode.intent.action.LOCATION");
        filterRes.putExtra("latitude", latitude);
        filterRes.putExtra("longitude", longitude);
        filterRes.putExtra("speed", speed);
        filterRes.putExtra("VehicleID",vehicleID);
        context.sendBroadcast(filterRes);
    }

    /**
     * Return the communication channel to the service.
     * 
     * @param intent the intent that was used to bind to this service.
     */
    public IBinder onBind(Intent intent) {
        return null;
    }
}
