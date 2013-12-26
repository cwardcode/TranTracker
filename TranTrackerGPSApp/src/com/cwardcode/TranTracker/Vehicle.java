package com.cwardcode.TranTracker;

/**
 * @author Chris Ward
 * @version 12/15/2013
 *
 * A class representing a Vehicle object
 */
public class Vehicle {

    /** Vehicle ID. */
    private int id;
    /** Vehicle Name */
    private String name;

    /**
     * Constructor for Vehicle .
     * @param id Vehicle ID.
     * @param name Vehicle Name
     */
    public Vehicle(int id, String name){
        this.id = id;
        this.name = name;
    }

    /**
     * Returns Vehicle ID.
     * @return id vehicle ID.
     */
    @SuppressWarnings("unused")
    public int getId(){
        return id;
    }

    /**
     * Returns Vehicle Name.
     * @return name Vehicle Name
     */
    public String getName() {
        return this.name;
    }
}
