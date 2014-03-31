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
 * information used to generate a Stop object for the map. This class 
 * parses the information then creates a list of StopDef objects that will
 * be used to generate map Stops.
 * 
 * @author Hayden Thomas 
 * @author Chris Ward
 * @version 3.14.14
 *
 */
public class StopParser {
	
	/**
	 * This class is used to store the information about the map Stops.
	 * Since it is difficult to generate a Stop without adding it directly
	 * to the map, this class provides a way to easily separate the XML
	 * parsing functionality from the map's actual functionality.
	 * 
	 * @author Hayden Thomas
	 * @version 3.14.14
	 *
	 */
	public static class StopDef {
		public String title;
		public double sLat;
		public double sLong;
		public int id;
		
		public StopDef(int id, String title, double vLat, double vLong) {
			this.title = title;
			this.sLat = vLat;
			this.sLong = vLong;
			this.id = id;
		}
				
		public StopDef() {}
		
		public int getID(){
			return this.id;
		}
		
		public void setID(int id){
			this.id = id;
		}
		public String getEntryName() {
			return this.title;
		}
		
		public void setEntryName(String name) {
			this.title = name;
		}
		
		public double getEntryLat(){
			return this.sLat;
		}
		
		public void setEntryLat(String lat){
			this.sLat = Double.parseDouble(lat);
		}
		
		public double getEntryLng(){
			return this.sLong;
		}
		
		public void setEntryLng(String lng){
			this.sLong = Double.parseDouble(lng);
		}
		public String toString() {
			String result = title + " " + sLat + " " + sLong;
			return result;
		}
	}
	
	/**
	 * The list containing the Stop definitions.
	 */
	ArrayList<StopDef> Stops;
	
	/**
	 * Null String to be used in parsing functions instead of a namespace.
	 */
	private static final String ns = null;
	
	/**
	 * Creates a new maker parser with an empty list.
	 */
	public StopParser() {
		Stops = new ArrayList<StopDef>();
	}
	
	public List<StopDef> getStopList() {
		return Stops;
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
		
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
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
	private StopDef readStop(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "stop");
		String stopName = null;
		double vLat = 0.0;
		double vLong = 0.0;
		int id = 0;
		
		while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagname = parser.getName();
            if (tagname.equals("stopID")) {
                stopName = readId(parser);
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
		
		return new StopDef(id,stopName, vLat, vLong);
	}
	
	/**
	 * Reads the vehicle id from the XML.
	 * @param parser
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private String readId(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "stopID");
		String stopName = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "stopID");
		return stopName;
	}
	/**
	 * Reads the vehicle id from the XML.
	 * @param parser
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "stopName");
		String stopName = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "stopName");
		return stopName;
	}
	
	/**
	 * Reads the vehicle's current latitude from the XML.
	 */
	private double readLat(XmlPullParser parser) throws IOException, XmlPullParserException {
		double vLat = 0.0;
		parser.require(XmlPullParser.START_TAG, ns, "stopLat");
		vLat = Double.parseDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "stopLat");
		return vLat;
	}
	
	/**
	 * Reads the vehicle's current longitude from the XML.
	 */
	private double readLong(XmlPullParser parser) throws IOException, XmlPullParserException {
		double vLong = 0.0;
		parser.require(XmlPullParser.START_TAG, ns, "stopLong");
		vLong = Double.parseDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "stopLong");
		return vLong;
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