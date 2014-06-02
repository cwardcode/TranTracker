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
	
	/**
	 * The chat button.
	 */
	private Button chat;
	
	/**
	 * The key button.
	 */
	private Button key;
	
	/**
	 * The help button.
	 */
	private Button help;
	
	/**
	 * The about button.
	 */
	private Button about;

	/**
	 * The list of bus marker definitions.
	 */
	private List<MarkerDef> markerDefs;
	
	/**
	 * The list of stop marker definitions.
	 */
	private List<StopDef> stopDefs;
	
	/**
	 * The list of stop markers.
	 */
	private List<Marker> stops;
	
	/**
	 * The list of bus markers
	 */
	private List<Marker> markers;
	
	/**
	 * The handler used to run the async task.
	 */
	private Handler handler = new Handler();
	
	/**
	 * An async task that downloads the database information as XML from the
	 * server.
	 * 
	 * @author Hayden Thomas
	 * 
	 * @version 5/5/14
	 */
	private class DownloadXmlTask extends AsyncTask<String, Void, String> {
		
		/**
		 * The list of bus marker definitions.
		 */
		private List<MarkerDef> markerDefs;
		
		/**
		 * The list of stop marker definitions.
		 */
		private List<StopDef> stopDefs;
		
		/**
		 * Downloads the XML while running in the background.
		 */
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
		
		/**
		 * Adds the markers to the map.
		 */
		@Override
		protected void onPostExecute(String result) {
			addMarkers();
			addStops();
		}
		
		/**
		 * Downloads and parses the XML from the given URL.
		 * 
		 * @param urlString the URL of the XML file as a String.
		 * @return A String that indicates success or failure.
		 * @throws XmlPullParserException
		 * @throws IOException
		 */
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
		
		/**
		 * Downloads the XML and turns it into an InputStream.
		 * 
		 * @param urlString the URL of the XML file as a String
		 * @return an InputStream
		 * @throws IOException
		 */
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
		
		/**
		 * Adds the stop markers to the map.
		 */
		private void addStops() {
			String name = "";
			double sLat = 0.0;
			double sLong = 0.0;
			
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
		}
		
		/**
		 * Adds the bus markers to the map.
		 */
		private void addMarkers() {
			int id = 0;
			String name = "";
			double vLat = 0.0;
			double vLong = 0.0;
			double speed = 0.0;
			
			String info = "";
			for (MarkerDef def : markerDefs) {
				id = def.id;
				name = def.title;
				vLat = def.vLat;
				vLong = def.vLong;
				speed = def.speed;
				
				info = " Name: " + name + " Speed: " + Math.round(speed 
										* AppConstants.METER_CONV) + " MPH";
				
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
		
		markerDefs = new ArrayList<MarkerDef>();
		markers = new ArrayList<Marker>();
		stops = new ArrayList<Marker>();
		AppConstants.createRoutes(this);
		runnable.run();
		
		
		try {
			// Loading map
			initializeMap();
			initializePolylines();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}

	
	/**
	 * Runs the async task that retrieves the data from our database.
	 */
	public void retrieveData() {
		if (isConnected()) {
			new DownloadXmlTask().execute(XMLURL);
		} else {
			//TODO: Handle error here
		}
	}
	
	/**
	 * Checks whether the App is connected to the server.
	 * 
	 * @return true if there is a connection, false otherwise.
	 */
	private boolean isConnected() {
		ConnectivityManager cm = 
				(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
				              activeNetwork.isConnectedOrConnecting();
		return isConnected;
	}

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
	 * This function creates the polylines that will be used to represent routes
	 * in our application.
	 */
	private void initializePolylines() {
		routes = new ArrayList<Polyline>();
        
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


	/**
	 * Handles button presses.
	 * 
	 * @param v the button that was pressed as a View.
	 */
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
