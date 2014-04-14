package edu.wcu.trackerapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import edu.wcu.trackerapp.MarkerParser.MarkerDef;
import edu.wcu.trackerapp.MarkerParser.StopDef;

/**
 * The application's map screen and main page.
 * 
 * @author Hayden Thomas
 * @author Chris Ward
 * @version 11/21/13
 * 
 */
@SuppressWarnings("unused")
public class Map extends Activity implements OnClickListener {

	// URL that generates XML\\
	private static final String XMLURL = "http://tracker.cwardcode.com/static/genxml.php";
	// XML Root Node
	private static final String RootNode = "marker";
	private static final String VidElement = "VID";
	private static final String LatElement = "Latitude";
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
	private Button chat;
	private Button key;
	
	private Button help;
	
	private Button about;

	private List<MarkerDef> markerDefs;
	
	private List<StopDef> stopDefs;
	
	private List<Marker> stops;
	
	private List<Marker> markers;
	
	private Handler handler = new Handler();
	
	private class DownloadXmlTask extends AsyncTask<String, Void, String> {
		
		private List<MarkerDef> markerDefs;
		
		private List<StopDef> stopDefs;
		
		@Override
		protected String doInBackground(String... urls) {
			try {
				return loadXmlFromNetwork(urls[0]);
			} catch (IOException ex) {
				//TODO: Handle this properly with strings from resources
				return "IO Error";
			} catch (XmlPullParserException ex) {
				return "Parser Erorr";
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			addMarkers();
			addStops();
		}
		
		private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
			InputStream stream = null;
			MarkerParser parser = new MarkerParser();
			
			try {
				stream = downloadUrl(urlString);
				parser.parseXML(stream);
				markerDefs = parser.getMarkerList();
				stopDefs = parser.getStopList();
			} finally {
				if (stream != null) {
					stream.close();
				}
			}
			
			return "success";
			
		}
		
		private InputStream downloadUrl(String urlString) throws IOException {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000 /* milliseconds */);
		    conn.setRequestMethod("GET");
		    conn.setDoInput(true);
		    conn.connect();
		    return conn.getInputStream();
		}
		
		private void addStops() {
			String name = "";
			double sLat = 0.0;
			double sLong = 0.0;
			
			//try{
			for (StopDef def : stopDefs) {
				name = def.title;
				sLat = def.sLat;
				sLong = def.sLong;
				
				
				boolean markerFound = false;
				
				for (Marker stop : stops) {
					Log.d("asynctest", stop.getTitle());
					if (stop.getTitle().equals(name)) {
						
						stop.setPosition(new LatLng(sLat, sLong));
						markerFound = true;
						
						if (AppConstants.selectedStops.
								contains(stop.getTitle())) {
						
							stop.setIcon(BitmapDescriptorFactory.
									fromResource(R.drawable.bluestopmarker));
						}
					}
				}
					
				if (!markerFound) {
				
					stops.add(googleMap.addMarker(new MarkerOptions()
				                    .position(new LatLng(sLat, sLong))
				                    .title(name)
				                    .icon(BitmapDescriptorFactory
				                    .fromResource(R.drawable.stopmarker))));
					
					AppConstants.stopNames.add(name);
				}
			}
			//}catch(NullPointerException ex){
			//	Toast.makeText(getApplicationContext(), 
  			//		  "Could not connect to server (stop)",Toast.LENGTH_SHORT).show();
			//}
		}
		
		private void addMarkers() {
			int id = 0;
			String name = "";
			double vLat = 0.0;
			double vLong = 0.0;
			double speed = 0.0;
			
			String info = "";
			//try{
			for (MarkerDef def : markerDefs) {
				id = def.id;
				name = def.title;
				vLat = def.vLat;
				vLong = def.vLong;
				speed = def.speed;
				
				info = " Name: " + name + " Speed: " + speed;
				
				boolean markerFound = false;
				
				for (Marker marker : markers) {
					Log.d("asynctest", marker.getTitle());
					if (marker.getTitle().equals(info)) {
						
						marker.setPosition(new LatLng(vLat, vLong));
						setTitle(info);
						markerFound = true;
					}
				}
					
				if (!markerFound) {
				
					markers.add(googleMap.addMarker(new MarkerOptions()
				                    .position(new LatLng(vLat, vLong))
				                    .title(info)
				                    .icon(BitmapDescriptorFactory
						             .fromResource(R.drawable.shuttle))));
				}
			}
			//}catch(NullPointerException ex){
			//	Toast.makeText(getApplicationContext(), 
  			//		  "Could not connect to server (marker)",Toast.LENGTH_SHORT).show();
			//}
		}
	}

	/**
	 * This is our little friend which continually makes calls to the 
	 * DownloadXMLTask. This allows us to update the current positions
	 * of the vehicles every 5 seconds.
	 */
	private Runnable runnable = new Runnable() {
		
		public void run() {
			retrieveData();
			handler.postDelayed(this, 5000);
		}
	};
	
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
		chat = (Button) findViewById(R.id.mapChatButton);
		key = (Button) findViewById(R.id.mapKeyButton);
		about = (Button) findViewById(R.id.mapAboutButton);
		help = (Button) findViewById(R.id.mapHelpButton);
		
		map.setOnClickListener(this);
		chat.setOnClickListener(this);
		key.setOnClickListener(this);
		about.setOnClickListener(this);
		help.setOnClickListener(this);
		
		//Initializing it here will keep the app from breaking under 
		//certain errors. Since I'm too lazy to write log messages
		//right now and I want to see these errors, I'm going to let it break.
		
		//markerDefs = new ArrayList<MarkerDef>();
		markers = new ArrayList<Marker>();
		stops = new ArrayList<Marker>();
		runnable.run();
		
		
		try {
			// Loading map
			initializeMap();
			initializePolylines();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}

	
	public void retrieveData() {
		if (isConnected()) {
			new DownloadXmlTask().execute(XMLURL);
		} else {
			//TODO: Handle error here
		}
	}
	
	private boolean isConnected() {
		ConnectivityManager cm = 
				(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
				              activeNetwork.isConnectedOrConnecting();
		return isConnected;
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
        
		if (AppConstants.selectedRoutes.contains(AppConstants.ROUTE_ALL_CAMPUS)) {
		    Polyline allCampus = googleMap.addPolyline(new PolylineOptions()
				    .addAll(routeHolder.getAllCampus()).width(5).color(Color.RED));
		    allCampus.setVisible(true);
		}
		
		if (AppConstants.selectedRoutes.contains(AppConstants.ROUTE_VILLAGE)) {
		    Polyline village = googleMap.addPolyline(new PolylineOptions()
		             .addAll(routeHolder.getVillage()).width(5).color(Color.YELLOW));
		    village.setVisible(true);
		}
		
		if (AppConstants.selectedRoutes.contains(AppConstants.ROUTE_HHS)) {
		    Polyline hhs = googleMap.addPolyline(new PolylineOptions()
		             .addAll(routeHolder.getHHS()).width(5).color(Color.MAGENTA));
		    hhs.setVisible(true);
		}
		
		if (AppConstants.selectedRoutes.contains(AppConstants.ROUTE_OFF_SOUTH)) {
		    Polyline offSouth = googleMap.addPolyline(new PolylineOptions()
				     .addAll(routeHolder.getOffSouth()).width(5).color(Color.BLUE));
	        offSouth.setVisible(true);
		}
	    
		if (AppConstants.selectedRoutes.contains(AppConstants.ROUTE_OFF_NORTH)) {
	        Polyline offNorth = googleMap.addPolyline(new PolylineOptions()
	    		     .addAll(routeHolder.getOffNorth()).width(5).color(Color.GREEN));
	        offNorth.setVisible(true);
		}
				
		
	}


	@Override
	public void onClick(View v) {
        Button button = (Button) v;
		
        if (button.equals(about)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.About.class);
			next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(next);
		} else if (button.equals(chat)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.Chat.class);
			next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(next);
		} else if (button.equals(key)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.Key.class);
			next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(next);
		} else if (button.equals(help)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.Help.class);
			next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(next);
		}
		
	}

}
