package com.cwardcode.trackerapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * This class defines an object that is used to parse an XML file that contains
 * information used to generate a Marker object for the map. This class parses
 * the information then creates a list of MarkerDef objects that will be used to
 * generate map markers.
 * 
 * @author Hayden Thomas
 * @author Chris Ward
 * @version June 02, 2014
 * 
 */
public class MarkerParser {

	/**
	 * This class is used to store the information about the map markers. Since
	 * it is difficult to generate a marker without adding it directly to the
	 * map, this class provides a way to easily separate the XML parsing
	 * functionality from the map's actual functionality.
	 * 
	 * @author Chris Ward
	 * @author Hayden Thomas
	 * @version June 2, 14
	 * 
	 */
	public static class MarkerDef {
		
		/**
		 * The ID number of the vehicle represented by the marker.
		 */
		public final int id;
				
		/**
		 * The title displayed by the marker.
		 */
		public final String title;

		/**
		 * The marker's latitude coordinate.
		 */
		public final double vLat;

		/**
		 * The marker's longitude coordinate.
		 */
		public final double vLong;

		/**
		 * The current speed of the vehicle represented by the marker.
		 */
		public final double speed;

		/**
		 * The ETA of the vehicle to its next stop.
		 */
		public final double ETA;
		
		/**
		 * The closest stop near the shuttle.
		 */
		public final String nextStop;

		/**
		 * Creates a new marker def with the given properties.
		 * 
		 * @param id
		 *            the vehicle's ID.
		 * @param title
		 *            the marker's title.
		 * @param vLat
		 *            the marker's latitude coordinate.
		 * @param vLong
		 *            the marker's longitude coordinate.
		 * @param speed
		 *            the vehicle's speed.
		 * 
		 * @return a new marker def object.
		 */
		private MarkerDef(int id, String title, double vLat, double vLong,
				double speed, double estWait, String nextStop) {
			this.id = id;
			this.title = title;
			this.vLat = vLat;
			this.vLong = vLong;
			this.speed = speed;
			this.ETA = estWait;
			this.nextStop = nextStop;
		}

		/**
		 * Creates a string representing the marker def.
		 * 
		 * @return a String listing the information contained by the marker def.
		 */
		public String toString() {
			String result = id + " " + title + " " + vLat + " " + vLong + " "
					+ speed + " " + ETA + " " + nextStop;
			return result;
		}
	}

	/**
	 * This class defines a stop def, which is an object that contains the
	 * fields needed to create a stop marker.
	 * 
	 * @author Hayden Thomas
	 * @version June 1, 2014
	 * 
	 */
	public static class StopDef {
		/**
		 * The marker's title.
		 */
		public final String title;

		/**
		 * The marker's latitude coordinate.
		 */
		public final double sLat;

		/**
		 * The marker's longitude coordinate.
		 */
		public final double sLong;

		/**
		 * Creates a new stop def with the given properties.
		 * 
		 * @param title
		 *            the marker's title.
		 * @param sLat
		 *            the marker's latitude coordinate.
		 * @param sLong
		 *            the marker's longitude coordinate.
		 * 
		 * @return a new StopDef object.
		 */
		private StopDef(String title, double sLat, double sLong) {
			this.title = title;
			this.sLat = sLat;
			this.sLong = sLong;
		}

		/**
		 * Creates a string with listing the information contained by the stop
		 * def.
		 * 
		 * @return a String containing the information contained in the stop
		 *         def.
		 */
		public String toString() {
			String result = title + " " + sLat + " " + sLong;
			return result;
		}
	}

	/**
	 * The list containing the marker definitions.
	 */
	ArrayList<MarkerDef> markers;

	/**
	 * The list containing the stop marker definitions.
	 */
	ArrayList<StopDef> stops;

	/**
	 * Null String to be used in parsing functions instead of a namespace.
	 */
	private static final String ns = null;

	/**
	 * Creates a new maker parser with empty lists.
	 */
	public MarkerParser() {
		markers = new ArrayList<MarkerDef>();
		stops = new ArrayList<StopDef>();
	}

	/**
	 * Returns the list of vehicle marker defs.
	 * 
	 * @return a List of MarkerDefs
	 */
	public List<MarkerDef> getMarkerList() {
		return markers;
	}

	/**
	 * Returns the list of stop marker defs.
	 * 
	 * @return a List of StopDefs
	 */
	public List<StopDef> getStopList() {
		return stops;
	}

	/**
	 * Parses the XML from the site into information that the app can use.
	 */
	public void parseXML(InputStream in) throws XmlPullParserException,
			IOException {
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
	 * @param parser
	 *            the parser used to parse the list.
	 * 
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void readFeed(XmlPullParser parser) throws XmlPullParserException,
			IOException {

		parser.require(XmlPullParser.START_TAG, ns, "markers");

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("marker")) {
				MarkerDef marker = readMarker(parser);
				markers.add(marker);
				// markers.add(readMarker(parser));
			} else if (name.equals("stop")) {
				// stops.add(readStop(parser));
				StopDef stop = readStop(parser);
				stops.add(stop);
			} else {
				skip(parser);
			}
		}
	}

	/**
	 * Creates a new MarkerDef using the information in the XML feed.
	 * 
	 * @param parser
	 *            the parser used to parse the list.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private MarkerDef readMarker(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "marker");
		int vId = 0;
		String title = null;
		double vLat = 0.0;
		double vLong = 0.0;
		double speed = 0.0;
		double estWait = 0;
		String nextStop = null;

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tagname = parser.getName();
			if (tagname.equals("VID")) {
				vId = readId(parser);
			} else if (tagname.equals("title")) {
				title = readTitle(parser);
			} else if (tagname.equals("latitude")) {
				vLat = readLat(parser);
			} else if (tagname.equals("longitude")) {
				vLong = readLong(parser);
			} else if (tagname.equals("speed")) {
				speed = readSpeed(parser);
			} else if (tagname.equals("estwait")) {
				estWait = readWait(parser);
			} else if (tagname.equals("nextstop")) {
				nextStop = readNextStop(parser);
			} else {
				skip(parser);
			}
		}

		return new MarkerDef(vId, title, vLat, vLong, speed, estWait, nextStop);
	}

	/**
	 * Parses the information required to create a StopDef.
	 * 
	 * @param parser
	 *            the parser used to parse the XML.
	 * @return a new StopDef with the information gathered by the parser.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private StopDef readStop(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "stop");
		String title = null;
		double sLat = 0.0;
		double sLong = 0.0;

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tagname = parser.getName();
			if (tagname.equals("stopName")) {
				title = readStopTitle(parser);
			} else if (tagname.equals("stopLat")) {
				sLat = readStopLat(parser);
			} else if (tagname.equals("stopLong")) {
				sLong = readStopLong(parser);
			} else {
				skip(parser);
			}
		}

		return new StopDef(title, sLat, sLong);
	}

	/**
	 * Reads the vehicle id from the XML.
	 * 
	 * @param parser
	 * @return the vehicle ID as an int.
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private int readId(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "VID");
		int vId = Integer.parseInt(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "VID");
		return vId;
	}

	/**
	 * Reads the vehicle's name from the XML
	 * 
	 * @param parser
	 * @return returns the vehicle name as a String.
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private String readTitle(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String title = "";
		parser.require(XmlPullParser.START_TAG, ns, "title");
		title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "title");
		return title;
	}

	/**
	 * Reads the stop's name from the XML
	 * 
	 * @param parser
	 *            the XML parser
	 * @return the stop name as a String.
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private String readStopTitle(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String title = "";
		parser.require(XmlPullParser.START_TAG, ns, "stopName");
		title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "stopName");
		return title;
	}

	/**
	 * Reads the vehicle's current latitude from the XML.
	 * 
	 * @param parser
	 *            the parser used to parse the list.
	 * 
	 * @return the latitude as a double.
	 * 
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private double readLat(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		double vLat = 0.0;
		parser.require(XmlPullParser.START_TAG, ns, "latitude");
		vLat = Double.parseDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "latitude");
		return vLat;
	}

	/**
	 * Reads the stop's current latitude from the XML.
	 * 
	 * @param parser
	 *            the parser used to parse the list.
	 * 
	 * @return the latitude as a double.
	 * 
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private double readStopLat(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		double vLat = 0.0;
		parser.require(XmlPullParser.START_TAG, ns, "stopLat");
		vLat = Double.parseDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "stopLat");
		return vLat;
	}

	/**
	 * Reads the stop's current longitude from the XML.
	 * 
	 * @param parser
	 *            the XmlPullParser used to read the XML.
	 * @return the longitude as a double.
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private double readStopLong(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		double vLong = 0.0;
		parser.require(XmlPullParser.START_TAG, ns, "stopLong");
		vLong = Double.parseDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "stopLong");
		return vLong;
	}

	/**
	 * Reads the vehicle's current longitude from the XML.
	 * 
	 * @param parser
	 *            the XmlPullParser used to read the XML.
	 * @return the longitude as a double.
	 */
	private double readLong(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		double vLong = 0.0;
		parser.require(XmlPullParser.START_TAG, ns, "longitude");
		vLong = Double.parseDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "longitude");
		return vLong;
	}

	/**
	 * Reads the vehicle's speed from the XML.
	 * 
	 * @param parser
	 *            the XmlPullParser used to read the XML.
	 * @return the speed as a double.
	 */
	private double readSpeed(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		double speed = 0.0;
		parser.require(XmlPullParser.START_TAG, ns, "speed");
		speed = Double.parseDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "speed");
		return speed;
	}

	/**
	 * Reads the vehicle's ETA from the XML.
	 * 
	 * @param parser
	 *            the XmlPullParser used to read the XML.
	 * @return the ETA represented in seconds as an integer.
	 */
	private double readWait(XmlPullParser parser) throws IOException,
			XmlPullParserException, NumberFormatException {
		double estWait = 0;
		parser.require(XmlPullParser.START_TAG, ns, "estwait");
		estWait = Double.parseDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "estwait");
		return estWait;
	}

	/**
	 * Reads the next stop's name from the XML
	 * 
	 * @param parser
	 * @return returns the next stop name as a String.
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private String readNextStop(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String nextStop = "";
		parser.require(XmlPullParser.START_TAG, ns, "nextstop");
		nextStop = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "nextstop");
		return nextStop;
	}
	
	/**
	 * Reads text from the XML.
	 * 
	 * @param parser
	 *            the XmlPullParser used to read the XML.
	 * @return the text as a string.
	 */
	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}

		return result;
	}

	/**
	 * Skips tags that don't need to be read.
	 * 
	 * @param parser
	 *            the XmlPullParser used to parse.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
				case XmlPullParser.END_TAG :
					depth--;
					break;
				case XmlPullParser.START_TAG :
					depth++;
					break;
			}
		}
	}
}