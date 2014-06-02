package com.cwardcode.TranTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Displays splash screen.
 * 
 * @author Chris Ward
 * @version June 1, 2014
 */
public class SplashActivity extends Activity {
	/**
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		// Remove title bar.
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set as full-screen.
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Hide status bar.
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LOW_PROFILE);
		}

		setContentView(R.layout.activity_splash);
		new DisplaySplash().execute();
		new GetData().execute();

	}

	private class DisplaySplash extends AsyncTask<Void, Void, Void> {

		private static final long SPLASH_DELAY = 5000;

		/**
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		/**
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(SPLASH_DELAY);
			} catch (InterruptedException ie) {
				Log.e("SplashActivity", ie.getMessage());
			}
			return null;
		}

	}

	/**
	 * AsyncTask which populates the Stops database and also the spinner for the
	 * TranTracker Activity.
	 * 
	 * @author Chris Ward
	 * @version June 01, 2014
	 */
	private class GetData extends AsyncTask<Void, Void, Void> {

		/** URL which returns JSON array of vehicles. */
		private static final String VID_URL = "http://tracker.cwardcode.com/static/getvid.php";

		/** ArrayList of currently operational vehicles. */
		private VehicleArrayList VehList = new VehicleArrayList();

		/**
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * Load data in background before displaying TranTrackerActivity.
		 * 
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected Void doInBackground(Void... arg0) {
			// Start with parsing stops into SQLiteDB - Create DbHelper.
			StopLocationDbHelper stopHelper = new StopLocationDbHelper(
					getApplicationContext());
			SQLiteDatabase db = stopHelper.getWritableDatabase();

			// Forces db to update, since need it to be as current as possible.
			db.setVersion(0);
			db.close();
			db = stopHelper.getWritableDatabase();

			// Get list of currently operable vehicles.
			collectVehicles();
			return null;
		}

		/**
		 * @see android.os.AsyncTask#onPostExecute()
		 */
		@Override
		protected void onPostExecute(Void result) {
			Bundle bundle = new Bundle();
			bundle.putParcelable("vehicles", VehList);
			Intent trackerActivity = new Intent(SplashActivity.this,
					TranTracker.class);
			trackerActivity.putExtras(bundle);
			startActivity(trackerActivity);
			finish();
		}

		/**
		 * Parses JSON response from server to collect current list of shuttles
		 * in service.
		 */
		private void collectVehicles() {
			ParseJson jsonParser = new ParseJson();
			// Try for a response.
			String json = jsonParser.makeServiceCall(VID_URL, ParseJson.GET);

			if (json != null) {
				try {
					JSONObject jsonObj = new JSONObject(json);
					/**
					 * Basically like STARTTAG for a child in XML, defines a
					 * vehicle object.
					 */
					JSONArray categories = jsonObj.getJSONArray("vehicles");
					// Loop through and add all vehicle objects to VehList
					for (int i = 0; i < categories.length(); i++) {
						// Get next object.
						JSONObject vehicleObj = (JSONObject) categories.get(i);
						// Create new vehicle object.
						Vehicle cat = new Vehicle(vehicleObj.getInt("id"),
								vehicleObj.getString("name"));

						VehList.add(cat);
					}

				} catch (JSONException e) {
					Log.e("SplashActivity-JSON", e.getMessage());
				}

			} else {
				Log.e("SplashActivity-JSON",
						"Didn't receive any data from server!");
				this.cancel(true);
			}
		}

	}
}
