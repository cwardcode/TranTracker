package com.cwardcode.TranTracker;

import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.StaleDataException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

/**
 * Uses the device's GPS to determine it's location and sends this infomration
 * to the broadcast receiver.
 * 
 * @author Hayden Thomas
 * @author Chris Ward
 * @version June 1, 2014
 */
public class SendLoc extends Service {
	private static final double DIST_RESET = 9999999.0;
	/** The distance at which the shuttle registers as near a Stop in miles. */
	private static final double DIST_THRESHOLD = 0.5;
	/** Converts m/s to mi/h. */
	public static final double METER_CONV = 2.37;
	/** Sixty seconds are in one minute. */
	public static final double MIN_SEC_CONV = 60;
	/** The speed at which the shuttle registers as stopped, in m/s. */
	private static final double SPEED_THRESHOLD = 2.2352;
	/** Radius of Earth, as accurate I could find. See: http://bit.ly/1wRP08p */
	private static final int R = 6371;
	/** Conversion factor for KM to Miles. */
	private static final double KM_TO_MILES_CONVERSION = 0.621371;
	/** Holds closest distance from stop, compared to all other stops. */
	private double closestDistance = 999.0;
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
	private ArrayList<String> stopLatList = new ArrayList<String>();
	/** ArrayList that holds each stop's longitude */
	private ArrayList<String> stopLngList = new ArrayList<String>();
	/** Name of the nearest stop */
	private static String closestStop = "Off-Route";
	/** ETA until arriving at next stop */
	private static double nextStopETA = -1;
	/** Provides a context from which to send our broadcast messages. */
	private static Context context;
	/** Intent to send the message, along with a filter. */
	private static Intent filterRes;

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
	 * 
	 * @author Chris Ward
	 * @version June 01, 2014
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
				// Get peopleData from main activity
				updateWithPeopleData(location);
				context.sendBroadcast(filterRes);
			} else if (location != null) {
				updateLocation(location);
				context.sendBroadcast(filterRes);
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
		// setup Array with stopLocations
		dbHelper = new StopLocationDbHelper(getApplicationContext());
		stopLocations = dbHelper.getReadableDatabase();
		setupStopLocList();
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
				} catch (RuntimeException ex) {
					Log.e("SendLoc", ex.getMessage());
				}
			}
		}, "SendLocThread");

		sendLocThread.start();
	}

	/**
	 * Sets up the arraylists with coordinate points for each stoplocation.
	 */
	private void setupStopLocList() {
		Cursor cursor = null;

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
		} catch (SQLException e) {
			Log.e("SendLoc", e.getMessage());
		} catch (StaleDataException e) {
			Log.e("SendLoc", e.getMessage());
		} catch (CursorIndexOutOfBoundsException e) {
			Log.e("SendLoc", e.getMessage());
		}
	}

	/**
	 * Determines if vehicle is moving based on the current speed compared to a
	 * threshold of 5MPH anything under is considered stopped to allow time for
	 * the ridership tracking system to kick in.
	 * 
	 * @return true if shuttle is moving.
	 */
	public synchronized boolean isStopped() {
		boolean isStopped = false;
		if (curSpeed < SPEED_THRESHOLD) {
			isStopped = true;
		}
		return isStopped;
	}

	/**
	 * Determines if currently near a shuttle stop. Also determines stop closest
	 * to the shuttle.
	 * 
	 * @return true if within .5 miles of stop.
	 */
	public synchronized boolean isNearLoc() {
		closestDistance = DIST_RESET;
		boolean isNear = false;
		Cursor cursor = null;
		for (int i = 0; i < stopLatList.size(); ++i) {
			int dbIndex = i + 1;
			double lat1 = Double.parseDouble(stopLatList.get(i));
			double lng2 = Double.parseDouble(stopLngList.get(i));

			double distanceFromStop = haversine(curLat, curLng, lat1, lng2);

			if (distanceFromStop <= DIST_THRESHOLD) {
				if (distanceFromStop < closestDistance) {
					isNear = true;
					closestDistance = distanceFromStop;
					cursor = stopLocations.rawQuery(
							"Select name from stop where stopID = " + dbIndex
									+ ";", null);
					cursor.moveToFirst();
					if (cursor.getCount() > 0) {
						closestStop = cursor.getString(0);
					}
					cursor.close();
				}
			}
		}
		
		/* Calculate ETA based on speed and distance if moving. Throwing in a
		   transmission delay for good measure. ETA results in seconds. 
		 */
		if (curSpeed > 0.2 && isNear) {
			nextStopETA =  ((closestDistance / (curSpeed)) * MIN_SEC_CONV*MIN_SEC_CONV);
		}

		return isNear;
	}

	/**
	 * Haversine function for determining distances between two coordinates on a
	 * globe.
	 * 
	 * @param lat1
	 *            Latitudinal coordinate 1
	 * @param lon1
	 *            Longitudinal coordinate 1
	 * @param lat2
	 *            Latitudinal coordinate 2
	 * @param lon2
	 *            Longitudinal coordinate 2
	 * @return Distance, in miles, between two coordinates.
	 */
	public static double haversine(double lat1, double lon1, double lat2,
			double lon2) {
		// Convert to radians since we're doing trig.
		double localLat1 = Math.toRadians(lat1);
		double localLat2 = Math.toRadians(lat2);
		// Get difference between two points then convert to radians
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		// Get length of arch.
		double archLength = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(localLat1) * Math.cos(localLat2)
				* Math.sin(dLon / 2) * Math.sin(dLon / 2);
		// Get angle of arch.
		double arcDistance = 2 * Math.atan2(Math.sqrt(archLength),
				Math.sqrt(1 - archLength));
		// Multiply by average radius of Earth, and convert to miles.
		return R * arcDistance * KM_TO_MILES_CONVERSION;

	}

	/**
	 * Updates the current location of the shuttle to the server.
	 * 
	 * @param location
	 *            the device's current Location.
	 */
	public static synchronized void updateLocation(Location location) {
		filterRes = new Intent();
		context = TranTracker.getAppContext();
		double latitude, longitude, speed;

		latitude = location.getLatitude();
		longitude = location.getLongitude();
		speed = location.getSpeed();
		curSpeed = speed * METER_CONV;
		filterRes.setAction("com.cwardcode.intent.action.LOCATION");
		filterRes.putExtra("latitude", latitude);
		filterRes.putExtra("longitude", longitude);
		filterRes.putExtra("speed", speed);
		filterRes.putExtra("VehicleID", vehicleID);
		filterRes.putExtra("title", title);
		filterRes.putExtra("nextStop", closestStop);
		filterRes.putExtra("estWait", nextStopETA);
	}
	/**
	 * Updates the current location, along with ridership data.
	 * 
	 * @param location
	 *            the device's current Location.
	 */
	public static synchronized void updateWithPeopleData(Location location) {
		// updateLocation, then add peopledata.
		updateLocation(location);
		filterRes.putExtra("people", numPeople);
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
