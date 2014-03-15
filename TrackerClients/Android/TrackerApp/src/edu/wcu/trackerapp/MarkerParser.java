package edu.wcu.trackerapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Xml;

/**
 * This class defines an object that is used to parse an XML file that contains
 * information used to generate a Marker object for the map. This class 
 * parses the information then creates a list of MarkerDef objects that will
 * be used to generate map markers.
 * 
 * @author Hayden Thomas 
 * @version 3.14.14
 *
 */
public class MarkerParser {
	
	/**
	 * This class is used to store the information about the map markers.
	 * Since it is difficult to generate a marker without adding it directly
	 * to the map, this class provides a way to easily separate the XML
	 * parsing functionality from the map's actual functionality.
	 * 
	 * @author Hayden Thomas
	 * @version 3.14.14
	 *
	 */
	public static class MarkerDef {
		public final int id;
		public final String title;
		public final double vLat;
		public final double vLong;
		public final double speed;
		
		private MarkerDef(int id, String title, double vLat, double vLong, double speed) {
			this.id = id;
			this.title = title;
			this.vLat = vLat;
			this.vLong = vLong;
			this.speed = speed;
		}
		
		public String toString() {
			String result = id + " " + title + " " + vLat + " " + vLong + " " + speed;
			return result;
		}
	}
	
	/**
	 * The list containing the marker definitions.
	 */
	ArrayList<MarkerDef> markers;
	
	/**
	 * Null String to be used in parsing functions instead of a namespace.
	 */
	private static final String ns = null;
	
	/**
	 * Creates a new maker parser with an empty list.
	 */
	public MarkerParser() {
		markers = new ArrayList<MarkerDef>();
	}
	
	public List<MarkerDef> getMarkerList() {
		return markers;
	}
	

    /**
	 * Parses the XML from the site into information that the app can use.
	 */
	public void parseXML(InputStream in) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			readFeed(parser);
		} finally {
			in.close();
		}
	}
	
	
	/**
	 * Reads the XML file and builds the list.
	 *  
	 * @param parser the parser used to parse the list.
	 * 
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
		
		parser.require(XmlPullParser.START_TAG, ns, "markers");
		//parser.require(XmlPullParser.END_TAG, ns, "/markers");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("marker")) {
				markers.add(readMarker(parser));
			} else {
				skip(parser);
			}
		}
	}
	
	/**
	 * Creates a new MarkerDef using the information in the XML feed.
	 * 
	 * @param parser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private MarkerDef readMarker(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "marker");
		int vId = 0;
		String title = null;
		double vLat = 0.0;
		double vLong = 0.0;
		double speed = 0.0;
		String tag = "";
		
		//while (parser.next() != XmlPullParser.END_TAG) {
			//tag = parser.getName();
			//if (tag.equals("marker")) {
				vId = Integer.parseInt(parser.getAttributeValue(null, "VID"));
				title = parser.getAttributeValue(null, "title");
				vLat = Double.parseDouble(parser.getAttributeValue(null, "latitude"));
				vLong = Double.parseDouble(parser.getAttributeValue(null, "longitude"));
				speed = Double.parseDouble(parser.getAttributeValue(null, "speed"));
			//}
		//}
		
		return new MarkerDef(vId, title, vLat, vLong, speed);
	}
	
	/**
	 * Reads the vehicle id from the XML.
	 * @param parser
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private int readId(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "VID");
		int vId = Integer.parseInt(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "VID");
		return vId;
	}
	
	/**
	 * Reads the vehicle's name from the XML
	 * @param parser
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
		String title = "";
		parser.require(XmlPullParser.START_TAG, ns, "title");
		title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "title");
		return title;
	}
	
	/**
	 * Reads the vehicle's current latitude from the XML.
	 */
	private double readLat(XmlPullParser parser) throws IOException, XmlPullParserException {
		double vLat = 0.0;
		parser.require(XmlPullParser.START_TAG, ns, "latitude");
		vLat = Double.parseDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "latitude");
		return vLat;
	}
	
	/**
	 * Reads the vehicle's current longitude from the XML.
	 */
	private double readLong(XmlPullParser parser) throws IOException, XmlPullParserException {
		double vLong = 0.0;
		parser.require(XmlPullParser.START_TAG, ns, "longitude");
		vLong = Double.parseDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "longitude");
		return vLong;
	}
	
	private double readSpeed(XmlPullParser parser) throws IOException, XmlPullParserException {
		double speed = 0.0;
		parser.require(XmlPullParser.START_TAG, ns, "speed");
		speed = Double.parseDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "speed");
		return speed;
	}
	
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		
		return result;
	}
	
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
}