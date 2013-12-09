package com.cwardcode.TranTracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Array;

/**
 * Provides a user interface that allows the user to select which vehicle is
 * being tracked and begin tracking.
 * 
 * @author Hayden Thomas
 * @author Chris Ward
 * @version September 9, 2013
 */
public class TranTracker extends Activity {
    public static boolean IS_TRACKING;
    public static boolean VEH_SELECT;
    public static boolean NID_SELECT;
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
           VEH_SELECT = true;
           srvIntent.putExtra("VehicleID", ++pos);
           srvIntent.putExtra("title", parent.getSelectedItem().toString());
        }

        /**
         * If nothing is selected, does nothing.
         *
         * @param arg0 - nothing.
         */
        public void onNothingSelected(AdapterView<?> arg0) {
            VEH_SELECT = false;
        }
    }


    /**
     * Listens for user input from a drop-down menu.
     */
    private class NameIDSelectionListener implements AdapterView.OnItemSelectedListener {

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
            NID_SELECT = true;
            srvIntent.putExtra("VehicleID", ++pos);
        }

        /**
         * If nothing is selected, does nothing.
         *
         * @param arg0 - nothing.
         */
        public void onNothingSelected(AdapterView<?> arg0) {
            NID_SELECT = false;
        }
    }

    /**
     * Starts the service that allows the device to be tracked.
     * 
     * @param view the area of the screen for the button calling this method.
     */
    public void startTracking(View view) {
        if(NID_SELECT){
            EditText nameText= (EditText)findViewById(R.id.nameField);
            String name = nameText.getText().toString();
            srvIntent.putExtra("title", name);
        }
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

        Spinner nameIDSpinner = (Spinner)findViewById(R.id.nameIDSelect);
        ArrayAdapter<CharSequence> nameIDAdapter = ArrayAdapter.createFromResource(this, R.array.nameID,
                android.R.layout.simple_spinner_item);
        nameIDAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nameIDSpinner.setAdapter(nameIDAdapter);
        nameIDSpinner.setOnItemSelectedListener(new NameIDSelectionListener());

    }

    /**
     * Returns the current application's context. Allows the service to send
     * a message to the broadcast receiver.
     */
    public static Context getAppContext() {
        return context;
    }
}
