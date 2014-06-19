package com.cwardcode.TranTracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Receives location and identification information and sends them to a remote
 * database.
 * 
 * @author Hayden Thomas
 * @author Chris Ward
 * @version June 1, 2014
 */
public class LocReceiver extends BroadcastReceiver {

	/**
	 * A MySQL library that allows communication from the application to a MySQL
	 * server
	 */
	private static final String DB_DRIVER = "org.postgresql.Driver";

	/** The connection string that allows DB_DRIVER to connect to the server. */
	private static final String DB_CONNECTION = "jdbc:postgresql://tracker.cwardcode.com:5432/geodatabase";

	/** The user name used for this connection */
	private static final String DB_USER = "cdward4";

	/** The password used for this connection */
	private static final String DB_PASSWORD = "geopassword";

	/**
	 * Receives information from an intent broadcasting to this receiver.
	 * 
	 * @param context
	 *            the context in which the receiver is running.
	 * @param intent
	 *            the intent being received.
	 */
	public void onReceive(Context context, Intent intent) {
		String vehicleName = intent.getStringExtra("title");
		Double latitude = intent.getDoubleExtra("latitude", -1);
		Double longitude = intent.getDoubleExtra("longitude", -1);
		Double speed = intent.getDoubleExtra("speed", -1);
		String nextStop = intent.getStringExtra("nextStop");
		double estWait = intent.getDoubleExtra("estWait", -133);
		updateRemote(vehicleName, latitude, longitude, speed, nextStop, estWait);
	}

	/**
	 * Creates a connection string to the MySQL database and inserts relevant
	 * data.
	 */
	private class SendLocationData extends AsyncTask<String, Void, Void> {

		/**
		 * Runs in the background and continuously sends data to the server.
		 * 
		 * @param s0trings
		 *            the array of items to send.
		 */
		protected Void doInBackground(String... strings) {
			if (isCancelled()) {
				return null;
			}
			try {
				Connection dbConnection = null;
				Statement statement = null;
				//TODO update sql insert statement for postgres
				//insert into tracker_location("VehID_id", "Latitude", "Longitude", "Speed", "estWait", "NextStop") 
				//        values ((select "VehID" from tracker_vehicle where "Title"='Village Express'), 35.31214,
				// 		  -83.10432, 8.94, 30.2, 'Village');

				String insertTableSQL = "INSERT INTO tracker_location"
						+ "(\"VehID_id\", \"Latitude\", \"Longitude\", \"Speed\", \"estWait\", \"NextStop\") "
						+ "VALUES"
						+ "((select \"VehID\" from tracker_vehicle WHERE \"Title\"=\'"
						+ strings[0] + "\')," + strings[1] + "," + strings[2]
						+ "," + strings[3] + "," + strings[4] + ",\' "
						+ strings[5] + "\')";
				Log.i("LocReceiver", "Attempting to execute:"
						+ insertTableSQL);
				try {
					Class.forName(DB_DRIVER);
					dbConnection = DriverManager.getConnection(DB_CONNECTION,
							DB_USER, DB_PASSWORD);
					statement = dbConnection.createStatement();
					statement.executeUpdate(insertTableSQL);

				} catch (SQLTimeoutException e) {
					Log.e("LocReceiver", e.getMessage());
				} catch (ClassNotFoundException e) {
					Log.e("LocReceiver", e.getMessage());
				} finally {
					if (statement != null) {
						statement.close();
					}

					if (dbConnection != null) {
						dbConnection.close();
					}
				}
			} catch (SQLException ex) {
				Log.e("LocReceiver", ex.getMessage());
			}
			return null;
		}

		/**
		 * Sets thread to cancel after doInBackground returns.
		 * 
		 * @param aVoid
		 *            return value of doInBackground.
		 */
		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			this.cancel(true);
		}
	}

	/**
	 * Logs all of the data to the Android debug log and then executes the
	 * SendLocationData method.
	 * 
	 * @param vehName
	 *            the ID name of the vehicle.
	 * @param latitude
	 *            the vehicle's latitude.
	 * @param longitude
	 *            the vehicle's longitude.
	 * @param speed
	 *            the vehicle's speed.
	 * @param estWait
	 *            the estimated time of arrival.
	 */
	private synchronized void updateRemote(String vehName, double latitude,
			double longitude, double speed, String nextStop, double estWait) {
		Log.i("Latitude:", latitude + "");
		Log.i("Longitude:", longitude + "");
		Log.i("Speed:", speed + "");
		Log.i("nextStop:", nextStop + "");
		Log.i("estWait:", estWait + "");
		// Now that the local visualizations are out of the way, let's actually
		// send it to the server.
		SendLocationData send = new SendLocationData();
		send.execute(vehName + "", latitude + "", longitude + "", speed + "",
				estWait + "", nextStop + "");
	}
}
