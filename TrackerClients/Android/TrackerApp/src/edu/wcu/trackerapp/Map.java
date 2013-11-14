package edu.wcu.trackerapp;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * The application's map screen and main page.
 * 
 * @author Hayden Thomas
 * @version 10/27/13
 * 
 */
public class Map extends Activity implements OnItemSelectedListener {

	// URL that generates XML\\
	private static final String XMLURL = "http://tracker.cwardcode.com/static/genxml.php";
	// XML Root Node
	private static final String RootNode = "marker";
	@SuppressWarnings("unused")
	private static final String VidElement = "VID";
	@SuppressWarnings("unused")
	private static final String LatElement = "Latitude";
	@SuppressWarnings("unused")
	private static final String LngElement = "Longitude";

	// A drop down menu that allows the user to navigate to other pages.\\
	private Spinner menu;
	// Our map object\\
	private GoogleMap googleMap;
	// Cullowhee's LatLng object\\
	private LatLng cullowLocation = new LatLng(35.30917186417894,
			-83.18345546722412);
	// Will eventually update dynamically based on xml values.\\
	private LatLng vehLocation = new LatLng(35.312358699242175,
			-83.18058013916016);

	/**
	 * Initializes the activity.
	 * 
	 * @param savedInstanceState
	 *            saved data from a previous run, if any.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(edu.wcu.trackerapp.R.layout.activity_map);
		// Initialize the spinner
		menu = (Spinner) findViewById(R.id.menuSpinner);
		// Set up the listener for the spinner.
		menu.setOnItemSelectedListener(this);
		try {
			// Loading map
			initializeMap();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initializeMap() {
		NodeList markerList = parseXML();

		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			} else {
				googleMap.setMyLocationEnabled(true);
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						cullowLocation, 15));
				for (int i = 0; i < markerList.getLength(); i++) {
					LatLng pos = vehLocation;
					String vid = "-1";
					googleMap
							.addMarker(new MarkerOptions()
									.position(pos)
									.title(vid)
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
				}
			}
		}
	}
	private NodeList parseXML(){
		ParseXML parser = new ParseXML();
		String xmlStr = parser.getXmlFromUrl(XMLURL);
		Document document = parser.getDomElement(xmlStr);
		return document.getElementsByTagName(RootNode);
	}
	/**
	 * Adds options to the menu.
	 * 
	 * @param menu
	 *            the menu options that will be added, if any.
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	/**
	 * Performs the appropriate action when an item is selected.
	 * 
	 * @param parent
	 *            the object containing all of the list items.
	 * @param view
	 *            ?
	 * @param pos
	 *            the items position.
	 * @param id
	 *            ?
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		String item = parent.getItemAtPosition(pos).toString();

		handleSelection(item);

	}

	/**
	 * Performs the action that is appropriate for the given selection. In this
	 * case, we will match the activity that matches the selection.
	 * 
	 * @param selection
	 *            a string representing the user's selection.
	 */
	private void handleSelection(String selection) {
		Intent next = null;
		if (selection.equals("Key")) {
			next = new Intent(this, edu.wcu.trackerapp.Key.class);
			this.startActivity(next);
		} else if (selection.equals("Contact")) {
			next = new Intent(this, edu.wcu.trackerapp.Contact.class);
			this.startActivity(next);
		} else if (selection.equals("Help")) {
			next = new Intent(this, edu.wcu.trackerapp.Help.class);
			this.startActivity(next);
		} else if (selection.equals("About")) {
			next = new Intent(this, edu.wcu.trackerapp.About.class);
			this.startActivity(next);
		}
	}

	/**
	 * This method is called when nothing is selected.
	 */
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// Right now this does nothing; however, it will likely be useful,
		// so I'm leaving the TODO as a reminder to myself.

		// TODO Auto-generated method stub

	}

}
