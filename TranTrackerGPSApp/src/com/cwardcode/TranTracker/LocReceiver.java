package com.cwardcode.TranTracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
	 * A MySQL library that allows communication from the application to a 
	 * MySQL server
	 */
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    
    /** The connection string that allows DB_DRIVER to connect to the server.*/
    private static final String DB_CONNECTION = "jdbc:mysql://tracker.cwardcode.com:3306/gpstracker";
    
    /** The username used for this connection */
    private static final String DB_USER = "gpstracker";
    
    /** The password used for this connection */
    private static final String DB_PASSWORD = "tracker";


    /**
     * Receives information from an intent broadcasting to this receiver.
     * 
     * @param context the context in which the receiver is running.
     * @param intent the intent being received.
     */
    public void onReceive(Context context, Intent intent) {
        int vehicleID =  intent.getIntExtra("VehicleID", -1);
        Double latitude = intent.getDoubleExtra("latitude", -1);
        Double longitude = intent.getDoubleExtra("longitude", -1);
        Double speed = intent.getDoubleExtra("speed", -1);
        updateRemote(vehicleID, latitude, longitude, speed);
    }
    
    /**
     * Creates a connection string to the MySQL database and inserts relevant
     * data.
     */
    private class SendLocationData extends AsyncTask<String, Void, Void> {
    	
    	/**
    	 * Runs in the background and continuously sends data to the server. 
    	 * 
    	 * @param strings the array of items to send.
    	 */
        protected Void doInBackground(String... strings){
            if(isCancelled()) {
                return null;
            }
            try {
                Connection dbConnection = null;
                Statement statement = null;

                String insertTableSQL = "INSERT INTO tracker_location"
                        + "(VehID, Latitude, Longitude, Speed) " + "VALUES"
                        + "(" + strings[0] + "," + strings[1] + "," + strings[2] + "," + strings[3] +")";
                Log.e("com.cwardcode.TranTracker", "Attempting to execute:" +insertTableSQL);
                try {
                    Class.forName(DB_DRIVER);
                    dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
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
         * @param aVoid return value of doInBackground.
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
     * @param vid the ID number of the vehicle.
     * @param latitude the vehicle's latitude.
     * @param longitude the vehicle's longitude.
     * @param speed the vehicle's speed.
     */
    private void updateRemote(int vid, double latitude, double longitude, double speed) {
        Log.e("Latitude:", latitude + "");
        Log.e("Longitude:", longitude + "");
        Log.e("Speed:", speed + "");
        //Now that the local visualizations are out of the way, let's actually send it to the server.
        SendLocationData send = new SendLocationData();
        send.execute(vid + "", latitude + "", longitude + "", speed + "");
    }
}
