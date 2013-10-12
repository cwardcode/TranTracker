package com.cwardcode.TranTracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Provides a user interface that allows the user to select which vehicle is
 * being tracked and begin tracking.
 * 
 * @author Hayden Thomas
 * @author Chris Ward
 * @version September 9, 2013
 */
public class TranTracker extends Activity {
    private static final String VEHICLE3 = "Catran3";
    private static final String VEHICLE2 = "Catran2";
    public static  boolean IS_TRACKING;
    private static Context context;
    private Intent srvIntent;


    /**
     * Listens for user input from a drop-down menu.
     */
    private class VehicleSelectionListener implements AdapterView.OnItemSelectedListener {

        /**
         * Triggered when an item within the spinner's list is selected.
         *
         * @param parent - the <code>AdapterView</code> in which the view
         *                 exists.
         * @param view   - the <code>View</code> in from which the item was
         *               selected.
         * @param pos    - the position of the menu item seected.
         * @param id     - the id of the item pressed, if available.
         */
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String strPos = parent.getItemAtPosition(pos).toString();
            int vehicleID;

            if (strPos.equals(VEHICLE3)) {
                vehicleID = 3;
            } else if (strPos.equals(VEHICLE2)) {
                vehicleID = 2;
            } else {
                vehicleID = 1;
            }
            srvIntent.putExtra("VehicleID", vehicleID);
        }

        /**
         * If nothing is selected, does nothing.
         *
         * @param arg0 - nothing.
         */
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /**
     * Starts the service that allows the device to be tracked.
     * 
     * @param view the area of the screen for the button calling this method.
     */
    public void startTracking(View view) {
        if(!IS_TRACKING){
            startService(srvIntent);
            IS_TRACKING = true;
        }   else {
            stopService(srvIntent);

            startService(srvIntent);
        }
    }

    /**
     * Called when the activity is first created.
     * 
     * @param savedInstanceState allows the application to return to its
     *                           previous configuration after being closed
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        IS_TRACKING = false;
        srvIntent = new Intent(this, SendLoc.class);
        TranTracker.context = getApplicationContext();

        Spinner gridSpinner = (Spinner) findViewById(R.id.VehicleSelect);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.vehicles,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        gridSpinner.setAdapter(adapter);
        gridSpinner.setOnItemSelectedListener(new VehicleSelectionListener());
    }

    /**
     * Returns the current application's context. Allows the service to send
     * a message to the broadcast receiver.
     */
    public static Context getAppContext() {
        return context;
    }
}
