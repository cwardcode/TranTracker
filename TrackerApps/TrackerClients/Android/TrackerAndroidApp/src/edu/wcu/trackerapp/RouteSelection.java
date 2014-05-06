package edu.wcu.trackerapp;

import android.content.Context;
import android.graphics.drawable.Drawable;


/**
 * This class represents a selection for a list view containing a list of
 * routes.
 */
public class RouteSelection {
    /**
     * The name of the route.
     */
	private String routeName;
	
	/**
	 * Whether the route has been selected.
	 */
	private boolean selected;
	
	/**
	 * The icon the route uses in the list.
	 */
	//TODO: add icons once the functionality is complete.
	private Drawable icon;
	
	/**
	 * The route's id number.
	 */
	private int routeID;
	
	/**
	 * Creates a new route selection using the given parameters. The selected
	 * field will default to false.
	 */
	public RouteSelection(String name, int id, Drawable icon) {
		this.routeName = name;
		this.routeID = id;
		this.icon = icon;
	}
	
	/**
	 * Returns the selection's icon.
	 * 
	 * @return the icon as a Drawable object.
	 */
	public Drawable getIcon() {
		return icon;
	}
	
	/**
	 * Set the selections icon.
	 * 
	 * @param icon the new icon.
	 */
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	/**
	 * Returns the route's name.
	 * 
	 * @return the route's name as a string.
	 */
	public String getRouteName() {
		return routeName;
	}
	
	/**
	 * Sets the site's name to the given String.
	 * 
	 * @param name the site's new name.
	 */
	public void setRouteName(String name) {
		this.routeName = name;
	}
	
	/**
	 * Returns whether the site is selected.
	 * 
	 * @return true if it is selected, false otherwise.
	 */
	public boolean isSelected() {
		return selected;
	}
	
	/**
	 * Changes whether the site is selected or not.
	 * 
	 * @param selected a boolean representing the selections selected state.
	 */
	public void setSelected(boolean selected, Context context) {
		this.selected = selected;
		
		if (selected) {
			Drawable icon = context.getResources().getDrawable(
	                R.drawable.map_marker_green);
			this.setIcon(icon);
		} else {
			Drawable icon = context.getResources().getDrawable(
	                R.drawable.map_marker_blk);
			this.setIcon(icon);
		}
	}
	
	/**
	 * Returns the site's ID number.
	 * 
	 * @return the site's ID number as an int.
	 */
	public int getRouteID() {
		return routeID;
	}
	
	/**
	 * Sets the site's ID number to the new ID number.
	 * 
	 * @para id the new ID number.
	 */
	public void setRouteID(int id) {
		this.routeID = id;
	}
}
