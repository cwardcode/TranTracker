package edu.wcu.trackerapp;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

/**
 * This class defines a set of app-wide constants.
 * 
 * @author Hayden Thomas
 * @version 4/12/14
 */
public class AppConstants {
    
	/**
	 * Used to access the app's shared preferences.
	 */
	public static final String PREFS = "AppSettings";
	
	/**
	 * The number of routes in the list.
	 */
	public static final int NUM_ROUTES = 5;
	
	/**
	 * The list containing all of the route list selections.
	 */
	public static ArrayList<RouteSelection> routes;
	
	/**
	 * The list of all available stop names.
	 */
	public static ArrayList<String> stopNames = new ArrayList<String>();
	
	/**
	 * The list containing all of the stop list selections.
	 */
	public static ArrayList<StopSelection> stops;
	
	/**
	 * The list of routes that the user has selected.
	 */
	public static ArrayList<String> selectedRoutes = new ArrayList<String>();
	
	/**
	 * The list of stops that the user has selected.
	 */
	public static ArrayList<String> selectedStops = new ArrayList<String>();
	
	/**
	 * These strings are the names of the routes that will be displayed on the
	 * map. In the future, it should be possible to retrieve routes from the
	 * database rather than hardcoding them, which would make this method
	 * unnecessary.
	 */
	public static final String ROUTE_ALL_CAMPUS = "All-Campus",
            ROUTE_VILLAGE = "Village Express",
            ROUTE_HHS = "HHS Express",
            ROUTE_OFF_SOUTH = "Off Campus South",
            ROUTE_OFF_NORTH = "Off Campus North";
            
	/**
	 * Initializes the list of selections.
	 * 
	 * @param context the calling object.
	 */
	public static void createRoutes(Context context) {
		// Only create it if it doesn't already exist.
		if (routes == null) {
			routes = new ArrayList<RouteSelection>(); 
			
			for (int i = 0; i < AppConstants.NUM_ROUTES; i++) {
				routes.add(getRouteSelection(i, context));
			}
		}
	}
	
	/**
	 * Returns the name of the route with the given id to the given context.
	 * @param id the route's id number.
	 * @param context the context of the caller.
	 * @return the name of the route as a String.
	 */
	public static String getRouteName(int id, Context context) {
		switch(id) {
			case 0: return ROUTE_ALL_CAMPUS;
			case 1: return ROUTE_VILLAGE;
			case 2: return ROUTE_HHS;
			case 3: return ROUTE_OFF_SOUTH;
			case 4: return ROUTE_OFF_NORTH;
		}
		// Default to an error message (shouldn't have to default)
		return "ERROR";
	}
	
	public static RouteSelection getRouteSelection(int id, Context context) {
		String name = getRouteName(id, context);
		
		return new RouteSelection(name, id);
	}
	
	public static void createStops(Context context) {
		if (stops == null) {
			stops = new ArrayList<StopSelection>();
			
			for (int i = 0; i < stopNames.size(); i++) {
				stops.add(getStopSelection(i, context));
			}
		}
	}
	
	public static StopSelection getStopSelection(int id, Context context) {
		String name = stopNames.get(id);
		return new StopSelection(name, id);
	}
}
