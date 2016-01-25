package com.cwardcode.trackerapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.cwardcode.trackerapp.R;


/**
 * This class represents a selection for a list view containing a list of
 * stops.
 */
public class StopSelection {
    /**
     * The name of the stop.
     */
	private String stopName;
	
	/**
	 * Whether the stop has been selected.
	 */
	private boolean selected;
	
	/**
	 * The icon the stop uses in the list.
	 */
	//TODO: add icons once the functionality is complete.
	private Drawable icon;
	
	/**
	 * The stop's id number.
	 */
	private int stopID;
	
	/**
	 * Creates a new stop selection using the given parameters. The selected
	 * field will default to false.
	 */
	public StopSelection(String name, int id, Drawable icon) {
		this.stopName = name;
		this.stopID = id;
		this.icon = icon;
	}
	
	/**
	 * Returns the stop's icon.
	 * 
	 * @return the icon as a Drawable.
	 */
	public Drawable getIcon() {
	    return icon;
	}
	
	/**
	 * Sets the icon to the given icon.
	 * 
	 * @param icon the new icon.
	 */
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	
	/**
	 * Returns the stop's name.
	 * 
	 * @return the stop's name as a string.
	 */
	public String getStopName() {
		return stopName;
	}
	
	/**
	 * Sets the site's name to the given String.
	 * 
	 * @param name the site's new name.
	 */
	public void setStopName(String name) {
		this.stopName = name;
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
	 * @param the calling context. This is needed to properly update icons.
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
	public int getStopID() {
		return stopID;
	}
	
	/**
	 * Sets the site's ID number to the new ID number.
	 * 
	 * @para id the new ID number.
	 */
	public void setStopID(int id) {
		this.stopID = id;
	}
}
