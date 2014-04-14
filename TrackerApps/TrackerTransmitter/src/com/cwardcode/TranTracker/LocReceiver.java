package com.cwardcode.TranTracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
 * @version September 9, 2013
 */
public class LocReceiver extends BroadcastReceiver {

	/**
	 * A MySQL library that allows communication from the application to a MySQL
	 * server
	 */
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";

	/** The connection string that allows DB_DRIVER to connect to the server. */
	private static final String DB_CONNECTION = "jdbc:mysql://tracker.cwardcode.com:3306/gpstracker";

	/** The username used for this connection */
	private static final String DB_USER = "gpstracker";

	/** The password used for this connection */
	private static final String DB_PASSWORD = "tracker";

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
		Double distance = intent.getDoubleExtra("Distance", -1);
		updateRemote(vehicleName, latitude, longitude, speed, distance);
	}

	/**
	 * Creates a connection string to the MySQL database and inserts relevant
	 * data.
	 */
	private class SendLocationData extends AsyncTask<String, Void, Void> {

		/**
		 * Runs in the background and continuously sends data to the server.
		 * 
		 * @param strings
		 *            the array of items to send.
		 */
		protected Void doInBackground(String... strings) {
			if (isCancelled()) {
				return null;
			}
			try {
				Connection dbConnection = null;
				Statement statement = null;
				// insert into tracker_location (VehID_id, Latitude, Longitude,
				// Speed, Distance)
				// VALUES ((select VehID from tracker_vehicle WHERE
				// title="Chris"), 35.42, -84.31, 34.4, .4);
				String insertTableSQL = "INSERT INTO tracker_location"
						+ "(VehID_id, Latitude, Longitude, Speed, Distance) "
						+ "VALUES"
						+ "((select VehID from tracker_vehicle WHERE title=\""
						+ strings[0] + "\")," + strings[1] + "," + strings[2]
						+ "," + strings[3] + "," + strings[4] + ")";
				Log.e("com.cwardcode.TranTracker", "Attempting to execute:"
						+ insertTableSQL);
				try {
					Class.forName(DB_DRIVER);
					dbConnection = DriverManager.getConnection(DB_CONNECTION,
							DB_USER, DB_PASSWORD);
					statement = dbConnection.createStatement();
					statement.executeUpdate(insertTableSQL);

				} catch (Exception e) {
					System.out.println("Error Occurred!");
					System.out.println(e.getMessage());

				} finally {

					if (statement != null) {
						statement.close();
					}

					if (dbConnection != null) {
						dbConnection.close();
					}
				}
			} catch (SQLException ex) {
				ex.getMessage();
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
	 */
	private void updateRemote(String vehName, double latitude,
			double longitude, double speed, double distance) {
		Log.e("Latitude:", latitude + "");
		Log.e("Longitude:", longitude + "");
		Log.e("Speed:", speed + "");
		Log.e("Dist:", distance + "");
		// Now that the local visualizations are out of the way, let's actually
		// send it to the server.
		SendLocationData send = new SendLocationData();
		send.execute(vehName + "", latitude + "", longitude + "", speed + "",
				distance + "");
	}
}
