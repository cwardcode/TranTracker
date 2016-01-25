package com.cwardcode.TranTracker;

/**
 * A class representing a Vehicle object
 * 
 * @author Chris Ward
 * @version 12/15/2013
 */
public class Vehicle {
	/** Vehicle ID. */
	private int id;
	/** Vehicle Name. */
	private String name;

	/**
	 * Creates an empty Vehicle object.
	 */
	public Vehicle() {
		// Intentionally left blank.
	}
	/**
	 * Creates a Vehicle object.
	 * 
	 * @param id
	 *            Vehicle ID.
	 * @param name
	 *            Vehicle Name
	 */
	public Vehicle(int id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Returns Vehicle ID.
	 * 
	 * @return vehicle ID.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Sets Vehicle ID.
	 * 
	 * @param Vehicle
	 *            ID corresponding to current name.
	 */
	public void setId(int vehId) {
		this.id = vehId;
	}

	/**
	 * Returns Vehicle Name.
	 * 
	 * @return Vehicle Name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns Vehicle Name.
	 * 
	 * @param Vehicle
	 *            Name corresponding to current ID.
	 */
	public void setName(String vehName) {
		this.name = vehName;
	}
}
