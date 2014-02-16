package edu.wcu.trackerapp;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * The application's map screen and main page.
 * 
 * @author Hayden Thomas
 * @author Chris Ward
 * @version 11/21/13
 * 
 */
public class Map extends Activity implements OnClickListener {

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

	private static final String ALL_CAMPUS = "";
	private static final String OFF_SOUTH = "myovErsuzNGeAMi@M_AM_ASi@SULURSfDm@xAyAp@c@|@bEx@hA~@XdD@bAh@b@[t@DzCb@pDSrCw@|Cu@zBI^m@`@?`DdAt@D~AwBvE_FnCoBfAo@pBs@fBFfBj@zCH|BMl@KM{AiDmDwDjAYOA[~BiCh@yASu@e@Kg@vAcAfA_A^sAJtAS`Aa@n@o@\\o@Zy@\\DVn@g@`B{BjC@f@VLfDeAZCzC`DNbBhBK`CYdDS~Ao@`De@hCRt@TgElCqIbCgKhCiKbDgCrBsE`EeBlBhAzAlA`AKF_Aw@qAcBgBzBmLlPgCvBoEzAwBZ{DHjAlEhBvGh@fCB|IClAJnCHdCRvA~@rDhAbJhAdFnAjIz@bFFfCE?E_C_AuFe@kDc@kC_AkEOo@o@eFQeBcAyDSuAIuCKmC@_B?iD?kCg@oCiBwGoAsEeE@eEJY@Gs@x@cDKO?SYUQUq@]_Ag@m@mA_@o@Oo@O}@My@Q_@]]k@Ok@Gg@EYEPkA@c@E}@G}@??EcA";
	private static final String OFF_NORTH = "oyovEnsuzNEcAOc@QwAQcAIQSQDQXUZKh@KlASVK`@]p@u@t@c@[sASe@QM[h@e@r@_@Rg@Hi@IYK[a@uA{B]c@Y]WOa@q@Wg@Sg@Ke@?sAC}A[k@m@]e@q@gBrCkA~@Kd@[h@_@Vi@Di@Te@p@oDxE_AfASHi@Fm@Ae@KYOSjEEhB@f@@j@Bl@Hz@?r@G^Ib@Kd@Eb@Cb@Cv@E~BCdA?REjAGlAGzBIbCLhA\\dBInBQhA?ANeAFsAB]Mo@Qs@MgAB_AFkADuBFgABm@@_@BeABwABkA@i@BY@YFa@Ha@Ha@F]Ac@Co@G_AAUAk@?g@Bs@BaAJyBDy@[YUWEa@Kw@EUMm@GOUm@]aA]eAMg@Ow@a@sBq@gDSaAQ_@CKm@m@gAaAkA{@eAy@{@g@c@Qc@GYEe@HUP[`@Sd@K^AlA?RCrAa@vCKvAUvAMpAIpCSpBu@K";
	private static final String VILLAGE = "myovEpsuzNR~DCZQrAkAAiAR_AVaAJaAUoCe@kCc@_@J[Zi@xB]rA}AlBMn@?tABhAPdB";
	private static final String HHS = "}invE|tyzN[sAHoBIoCaAqFg@aE{@gEoA}Gk@eFe@oB_@uAUoBCaBIgC?cD@_CGmC_@eCoAqEgBsGkLDCo@v@}CIO";

	// A drop down menu that allows the user to navigate to other pages.\\
	//private Spinner menu;
	// Our map object\\
	private GoogleMap googleMap;
	// Cullowhee's LatLng object\\
	private LatLng cullowLocation = new LatLng(35.30917186417894,
			-83.18345546722412);
	// Will eventually update dynamically based on xml values.\\
	private LatLng vehLocation = new LatLng(35.312358699242175,
			-83.18058013916016);
	
	private RouteArrayHolder routeHolder;
	
	/**
	 * A list of routes as polylines.
	 */
	private ArrayList<Polyline> routes;
	
	/**
	 * The map button.
	 */
	private Button map;
	
	private Button key;
	
	private Button help;
	
	private Button about;

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
		//menu = (Spinner) findViewById(R.id.menuSpinner);
		// Set up the listener for the spinner.
		//menu.setOnItemSelectedListener(this);
		
		map = (Button) findViewById(R.id.mapMapButton);
		key = (Button) findViewById(R.id.mapKeyButton);
		about = (Button) findViewById(R.id.mapAboutButton);
		help = (Button) findViewById(R.id.mapHelpButton);
		
		map.setOnClickListener(this);
		key.setOnClickListener(this);
		about.setOnClickListener(this);
		help.setOnClickListener(this);
		
		try {
			// Loading map
			initializeMap();
			initializePolylines();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}
	
	
	/*
	 * This method is for if I ever get tabs working, lol.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			                 Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.activity_map, container, 
				                         false);
		
		try {
			// Loading map
			initializeMap();
			initializePolylines();
			// TODO: Make this useful
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rootView;
	}
	*/
	

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initializeMap() {

		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			} else {
				googleMap.setMyLocationEnabled(true);
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						cullowLocation, 15));
				routeHolder = new RouteArrayHolder();

			}
		}
	}

	/**
	 * Adds options to the menu.
	 * 
	 * @param menu
	 *            the menu options that will be added, if any.
	 */
	/*
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	*/
	
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
	/*
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		String item = parent.getItemAtPosition(pos).toString();

		handleSelection(item);

	}
	*/

	/**
	 * Performs the action that is appropriate for the given selection. In this
	 * case, we will match the activity that matches the selection.
	 * 
	 * @param selection
	 *            a string representing the user's selection.
	 */
	/*
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
	*/

	/**
	 * This method is called when nothing is selected.
	 */
	/*
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// Right now this does nothing; however, it will likely be useful,
		// so I'm leaving the TODO as a reminder to myself.

		// TODO Auto-generated method stub

	}
	*/

	/**
	 * This function creates the polylines that will be used to represent routes
	 * in our application.
	 */
	private void initializePolylines() {
		routes = new ArrayList<Polyline>();
		//List<LatLng> route1 = decodePoly(OFF_SOUTH);

		Polyline allCampus = googleMap.addPolyline(new PolylineOptions()
				.addAll(routeHolder.getAllCampus()).width(5).color(Color.RED));
		allCampus.setVisible(true);
		
		Polyline village = googleMap.addPolyline(new PolylineOptions()
		         .addAll(routeHolder.getVillage()).width(5).color(Color.YELLOW));
		village.setVisible(true);
		
		Polyline hhs = googleMap.addPolyline(new PolylineOptions()
		         .addAll(routeHolder.getHHS()).width(5).color(Color.MAGENTA));
		hhs.setVisible(true);
		
		Polyline offSouth = googleMap.addPolyline(new PolylineOptions()
				 .addAll(routeHolder.getOffSouth()).width(5).color(Color.BLUE));
	    offSouth.setVisible(true);
	    
	    Polyline offNorth = googleMap.addPolyline(new PolylineOptions()
	    		 .addAll(routeHolder.getOffNorth()).width(5).color(Color.GREEN));
	    offNorth.setVisible(true);
				
		
	}


	@Override
	public void onClick(View v) {
        Button button = (Button) v;
		
		if (button.equals(about)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.About.class);
			this.startActivity(next);
		} else if (button.equals(key)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.Key.class);
			this.startActivity(next);
		} else if (button.equals(help)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.Help.class);
			this.startActivity(next);
		}
		
	}

}
