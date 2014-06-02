package com.cwardcode.TranTracker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * This class defines an object that is used to parse an XML file that contains
 * information used to generate a Stop object for the map. This class parses the
 * information then creates a list of StopDef objects that will be used to
 * generate map Stops.
 * 
 * @author Hayden Thomas
 * @author Chris Ward
 * @version June 02, 2014
 * 
 */
public class StopParser {
	/** The list containing the Stop definitions. */
	private ArrayList<StopDef> Stops;
	/** Null String to be used in parsing functions instead of a namespace. */
	private static final String ns = null;

	/**
	 * This class is used to store the information about the map Stops. Since it
	 * is difficult to generate a Stop without adding it directly to the map,
	 * this class provides a way to easily separate the XML parsing
	 * functionality from the map's actual functionality.
	 * 
	 * @author Hayden Thomas
	 * @author Chris Ward
	 * @version June 1, 2014
	 */
	public static class StopDef {
		/** Holds title of current stop definition. */
		private String title;
		/** Holds latitude of current stop. */
		private double sLat;
		/** Holds longitude of current stop. */
		private double sLong;
		/** Holds ID of current stop. */
		private int id;

		/**
		 * Creates as StopDef object.
		 * 
		 * @param id
		 *            ID of stop.
		 * @param title
		 *            Title of Stop.
		 * @param vLat
		 *            Latitude of Stop.
		 * @param vLong
		 *            Longitude of Stop.
		 */
		public StopDef(int id, String title, double vLat, double vLong) {
			this.title = title;
			this.sLat = vLat;
			this.sLong = vLong;
			this.id = id;
		}

		/**
		 * Creates an empty StopDef object.
		 */
		public StopDef() {
		}

		/**
		 * Returns stop ID.
		 * 
		 * @return stop ID.
		 */
		public int getID() {
			return this.id;
		}

		/**
		 * Sets current stop's ID
		 * 
		 * @param id
		 *            stop ID
		 */
		public void setID(int id) {
			this.id = id;
		}

		/**
		 * Returns current stop title.
		 * 
		 * @return title The title of the stop.
		 */
		public String getStopTitle() {
			return this.title;
		}

		/**
		 * Sets the current stop's title.
		 * 
		 * @param title
		 *            Title of stop.
		 */
		public void setStopTitle(String title) {
			this.title = title;
		}

		/**
		 * Returns current stop Latitude.
		 * 
		 * @return Stop's latitude.
		 */
		public double getStopLat() {
			return this.sLat;
		}

		/**
		 * Sets current stop Latitude.
		 * 
		 * @param lat
		 *            Stop's latitude to set.
		 */
		public void setStopLat(String lat) {
			this.sLat = Double.parseDouble(lat);
		}

		/**
		 * Returns current stop Longitude.
		 * 
		 * @return Stop's longitude.
		 */
		public double getStopLng() {
			return this.sLong;
		}

		public void setStopLng(String lng) {
			this.sLong = Double.parseDouble(lng);
		}

		/**
		 * Returns string representation of StopDef object.
		 * 
		 * @return result String representation of StopDef, in
		 *         "<title> <sLat> <sLong>" format.
		 */
		public String toString() {
			String result = title + " " + sLat + " " + sLong;
			return result;
		}
	}

	/**
	 * Creates a new maker parser with an empty list.
	 */
	public StopParser() {
		Stops = new ArrayList<StopDef>();
	}

	/**
	 * Returns list containing all StopDefs.
	 * 
	 * @return List<StopDef> containing all StopDefs.
	 */
	public List<StopDef> getStopList() {
		return Stops;
	}

	/**
	 * Parses the XML from the site into information that the app can use.
	 * 
	 * @param in
	 *            InputStream with XML to parse.
	 */
	public void parseXML(InputStream in) throws XmlPullParserException,
			IOException {

		try {
			// Create parser.
			XmlPullParser parser = Xml.newPullParser();
			// Don't respect namespaces, since we're not using them.
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			// Parse using inputstream passed in.
			parser.setInput(in, null);
			// Get next tag.
			parser.nextTag();
			// Read server response.
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

		// Set XML tag that starts XML response.
		parser.require(XmlPullParser.START_TAG, ns, "markers");
		// Grab all attributes.
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				// Skip if not "markers"
				continue;
			}
			String name = parser.getName();
			// If we find a stop add it to the stops list, else nothingtoseehere
			if (name.equals("stop")) {
				Stops.add(readStop(parser));
			} else {
				skip(parser);
			}
		}
	}

	/**
	 * Creates a new StopDef using the information in the XML feed.
	 * 
	 * @param parser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private StopDef readStop(XmlPullParser parser)
			throws XmlPullParserException, IOException {

		// Define starting tag of a stop is "stop".
		parser.require(XmlPullParser.START_TAG, ns, "stop");

		// Initial values.
		String stopName = null;
		double vLat = 0.0;
		double vLong = 0.0;
		int id = 0;
		// Loop over attributes.
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tagname = parser.getName();
			// Grab attributes.
			if (tagname.equals("stopID")) {
				id = Integer.parseInt(readId(parser));
			} else if (tagname.equals("stopName")) {
				stopName = readName(parser);
			} else if (tagname.equals("stopLat")) {
				vLat = readLat(parser);
			} else if (tagname.equals("stopLong")) {
				vLong = readLong(parser);
			} else {
				skip(parser);
			}
		}

		return new StopDef(id, stopName, vLat, vLong);
	}

	/**
	 * Reads the vehicle id from the XML.
	 * 
	 * @param parser
	 *            The parser to read from.
	 * @return Name of stop.
	 * @throws IOException
	 *             If connection between parser and the server fails.
	 * @throws XmlPullParserException
	 *             thrown to signal XML Pull Parser related faults.
	 */
	private String readId(XmlPullParser parser) throws IOException,
			XmlPullParserException {

		parser.require(XmlPullParser.START_TAG, ns, "stopID");
		String stopID = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "stopID");
		return stopID;
	}

	/**
	 * Reads the vehicle name from the XML response.
	 * 
	 * @param parser
	 *            The parser to read from.
	 * @return id The Vehicle ID
	 * @throws IOException
	 *             if parser's connection is inavalid.
	 * @throws XmlPullParserException
	 *             Thrown to signal XML Pull Parser related faults.
	 */
	private String readName(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "stopName");
		String stopName = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "stopName");
		return stopName;
	}

	/**
	 * Reads the vehicle's current latitude from the XML.
	 * 
	 * @return id The Vehicle Latitude
	 * @throws IOException
	 *             if parser's connection is inavalid.
	 * @throws XmlPullParserException
	 *             Thrown to signal XML Pull Parser related faults.
	 */
	private double readLat(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		double vLat = 0.0;
		parser.require(XmlPullParser.START_TAG, ns, "stopLat");
		vLat = Double.parseDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "stopLat");
		return vLat;
	}

	/**
	 * Reads the vehicle's current longitude from the XML.
	 * 
	 * @param parser
	 *            The parser to read from.
	 * @return id The Vehicle Longitude
	 * @throws IOException
	 *             if parser's connection is inavalid.
	 * @throws XmlPullParserException
	 *             Thrown to signal XML Pull Parser related faults.
	 */
	private double readLong(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		double vLong = 0.0;
		parser.require(XmlPullParser.START_TAG, ns, "stopLong");
		vLong = Double.parseDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "stopLong");
		return vLong;
	}

	/**
	 * Reads text from attribute.
	 * 
	 * @param parser
	 *            The parser to read from.
	 * @return Text within attribute.
	 * @throws IOException
	 *             If connection between parser and server fails.
	 * @throws XmlPullParserException
	 *             Thrown to signal XML Pull Parser related faults.
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
	 * Skips current child of XML doc.
	 * 
	 * @param parser
	 *            The parser to read from.
	 * @throws XmlPullParserException
	 *             Thrown to signal XML Pull Parser related faults.
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