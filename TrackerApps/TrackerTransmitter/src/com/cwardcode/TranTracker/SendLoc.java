package com.cwardcode.TranTracker;

import java.util.ArrayList;
import java.util.Random;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;

/**
 * Uses the device's GPS to determine it's location and sends this infomration
 * to the broadcast receiver.
 * 
 * @author Hayden Thomas
 * @author Chris Ward
 * @version September 9, 2013
 */
public class SendLoc extends Service {

	private static final double DIST_THRESHOLD = 0.5;
	private static final double SPEED_THRESHOLD = 0.5;
	private static final double R = 6372.8;
	private static final double MILES_CONVERSION = 0.621371;

	/** Provides access to the device's GPS services. */
	private LocationManager lm;
	/** Listens for location updates from the LocationManager. */
	private LocationListener locListener;
	/** The amount of people using a particular stop */
	private static int numPeople;

	/** Used to identify the vehicle that is being tracked by this device. */
	private static int vehicleID;

	/** Used to identify the vehicle by name */
	private static String title;

	/** StopLocation database */
	private static SQLiteDatabase stopLocations;
	/** Helper for database */
	private static StopLocationDbHelper dbHelper;

	/** Binder to return to client (TranTracker) */
	private final IBinder rtnBinder = new LocationBinder();
	/** Holds current speed of shuttle. */
	private static double curSpeed;
	/** Holds current latitude */
	private static double curLat;
	/** Holds current longitude */
	private static double curLng;
	/** ArrayList that holds each stop's latitude */
	ArrayList<String> stopLatList = new ArrayList<String>();
	/** ArrayList that holds each stop's longitude */
	ArrayList<String> stopLngList = new ArrayList<String>();
	/** Name of the nearest stop */
	private String nameAtStop;
	private static double distanceFromStop;
	/** Global cursor for database */
	private Cursor cursor = null;

	/***
	 * Class to for allowing clients to access this service's public methods.
	 * 
	 * @author Chris Ward
	 * 
	 */
	public class LocationBinder extends Binder {
		SendLoc getService() {
			return SendLoc.this;
		}
	}

	/**
	 * Listens for changes in the device's location.
	 */
	private class MyLocationListener implements LocationListener {
		/**
		 * Updates the device's location.
		 * 
		 * @param location
		 *            the device's current location.
		 */
		@Override
		public void onLocationChanged(Location location) {
			// Check if location is near a stop, if so updateWithPeopleData
			curLat = location.getLatitude();
			curLng = location.getLongitude();
			if (isNearLoc()) {
				//Get peopleData from main activity
				updateWithPeopleData(location);
			}
			if (location != null) {
				updateLocation(location);
			}
		}

		/**
		 * Called when the provider is enabled by the user. This method is not
		 * supported in this implementation.
		 * 
		 * @param provider
		 *            the provider that would be enabled.
		 */
		@Override
		public void onProviderEnabled(String provider) {
		}

		/**
		 * Called when the provider is disabled by the user. This method is not
		 * supported in this implementation.
		 * 
		 * @param provider
		 *            the provider that would be disabled.
		 */
		@Override
		public void onProviderDisabled(String provider) {
		}

		/**
		 * Called when the provider status changes. This method is not supported
		 * in this implementation.
		 * 
		 * @param provider
		 *            the name of the location provider.
		 * @param status
		 *            an integer representing the status of the provider.
		 * @param extras
		 *            an optional bundle containing provider specific status
		 *            variables.
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	/**
	 * Creates the service.
	 */
	public void onCreate() {
		super.onCreate();
		addLocationListener();
		dbHelper = new StopLocationDbHelper(getApplicationContext());
		stopLocations = dbHelper.getReadableDatabase();
		// setup Array with stopLocations
		setupStopLocList();
		//  Just to test since location updates don't change on the transformer
		// prime..
		/**
		curLat = 35.311575;// location.getLatitude();
		curLng = -83.180500;// location.getLongitude();
		curSpeed = .03;
		isNearLoc();
		**/
	}

	/**
	 * Initializes the service.
	 * 
	 * @param intent
	 *            The Intent that provides the service with the vehicleID.
	 * @param startID
	 *            a unique identifier for this request.
	 */
	public int onStartCommand(Intent intent, int startID, int startId) {
		vehicleID = intent.getIntExtra("VehicleID", -1);
		title = intent.getStringExtra("title");
		numPeople = intent.getIntExtra("people", -1);
		return START_NOT_STICKY;
	}

	/**
	 * Creates a new LocationListener and begins listening.
	 */
	private void addLocationListener() {
		Thread sendLocThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Looper.prepare();
					lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

					Criteria c = new Criteria();
					c.setAccuracy(Criteria.ACCURACY_FINE);

					locListener = new MyLocationListener();
					lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
							2000, 0, locListener);
					Looper.loop();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}, "SendLocThread");

		sendLocThread.start();
	}

	/**
	 * generates a random number b/t 1-10
	 * 
	 * @return 'random' number
	 */
	public int getRandomNum() {
		return new Random().nextInt(10);
	}

	/**
	 * Returns next closest stop name, if exists.
	 * 
	 * @return next closest stop
	 */
	public String getNextStop() {
		return nameAtStop;
	}
	/**
	 * Sets up the arraylists with coordinate points for each stoplocation.
	 */
	private void setupStopLocList() {
		try {
			String query = "select latitude from stop";
			cursor = stopLocations.rawQuery(query, null);
			if (cursor != null && cursor.moveToFirst()) {
				stopLatList = new ArrayList<String>();
				do {
					stopLatList.add(cursor.getString(0));
				} while (cursor.moveToNext());
			}
			cursor.close();
			query = "select longitude from stop";

			cursor = stopLocations.rawQuery(query, null);
			if (cursor != null && cursor.moveToFirst()) {
				stopLngList = new ArrayList<String>();
				do {
					stopLngList.add(cursor.getString(0));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
			stopLatList = null;
			stopLngList = null;
		}
	}

	/**
	 * Determines if vehicle is moving.
	 * 
	 * @return true if shuttle is moving.
	 */
	public boolean isStopped() {
		boolean isMoving = false;
		if (curSpeed < SPEED_THRESHOLD) {
			isMoving = true;
		}
		return isMoving;
	}

	/**
	 * Determines if near stoplocation
	 * 
	 * @return true if within .5 miles of stop.
	 */
	public boolean isNearLoc() {
		boolean isNear = false;
		for (int i = 0; i < stopLatList.size(); i++) {

			double lat1 = Double.parseDouble(stopLatList.get(i));
			double lng2 = Double.parseDouble(stopLngList.get(i));

			 distanceFromStop = haversine(curLat, curLng, lat1, lng2);

			if (distanceFromStop <= DIST_THRESHOLD) {
				isNear = true;
				cursor = stopLocations
						.rawQuery("Select name from stop where stopid = " + i
								+ ";", null);
				cursor.moveToFirst();
				if (cursor.getCount() > 0) {
					nameAtStop = cursor.getString(0);
				}
			}
		}
		return isNear;
	}

	/**
	 * Haversine function Ripped from Rosetta Code
	 */
	public static double haversine(double lat1, double lon1, double lat2,
			double lon2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2)
				* Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.asin(Math.sqrt(a));
		return R * c * MILES_CONVERSION;
	}

	/**
	 * Updates the current location.
	 * 
	 * @param location
	 *            the device's current Location.
	 */
	public static void updateLocation(Location location) {
		Context context;
		context = TranTracker.getAppContext();// getAppContext();
		double latitude, longitude, speed;

		latitude = location.getLatitude();
		longitude = location.getLongitude();
		speed = location.getSpeed();
		curSpeed = speed;
		Intent filterRes = new Intent();
		filterRes.setAction("com.cwardcode.intent.action.LOCATION");
		filterRes.putExtra("latitude", latitude);
		filterRes.putExtra("longitude", longitude);
		filterRes.putExtra("speed", speed);
		filterRes.putExtra("Distance", distanceFromStop);
		filterRes.putExtra("VehicleID", vehicleID);
		filterRes.putExtra("title", title);
		context.sendBroadcast(filterRes);
	}

	/**
	 * Updates the current location, along with ridership data.
	 * 
	 * @param location
	 *            the device's current Location.
	 */
	public static void updateWithPeopleData(Location location) {
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
		filterRes.putExtra("Distance", distanceFromStop);
		filterRes.putExtra("VehicleID", vehicleID);
		filterRes.putExtra("title", title);
		filterRes.putExtra("people", numPeople);
		context.sendBroadcast(filterRes);
	}

	/**
	 * Return the communication channel to the service.
	 * 
	 * @param intent
	 *            the intent that was used to bind to this service.
	 */
	public IBinder onBind(Intent intent) {
		return rtnBinder;
	}
}
