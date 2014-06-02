package com.cwardcode.TranTracker;

import org.opencv.core.Rect;

/**
 * This class represents a blob.
 * 
 * @author Hayden Thomas
 * @version 5/6/2014
 * 
 */
public class Blob {

	/**
	 * The rectangle that this blob represents.
	 */
	private Rect rectangle;

	/**
	 * The blob's id.
	 */
	private int id;

	/**
	 * Creates a new Blob based on the given rectangle with the given id.
	 * 
	 * @param rectangle
	 *            the rectangle that the blob wraps.
	 * @param id
	 *            identifies the blob.
	 */
	public Blob(Rect rectangle, int id) {
		this.rectangle = rectangle;
		this.id = id;
	}

	/**
	 * Returns the area of the blob. Wraps the rectangle's area function.
	 * 
	 * @return the area of the rectangle represented by the blob as a double.
	 */
	public double area() {
		return rectangle.area();
	}

	/**
	 * Returns the blob's id.
	 * 
	 * @return the blob's id as an int.
	 */
	public int id() {
		return id;
	}

	/**
	 * Set the blob's id to the new id.
	 * 
	 * @param id
	 *            the new id.
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * The x coordinate of the center point.
	 * 
	 * @return the x coordinate of the center point as an int.
	 */
	public int x() {
		return rectangle.width / 2;
	}

	/**
	 * The y coordinate of the center point.
	 * 
	 * @return the y coordinate of the center point as an int.
	 */
	public int y() {
		return rectangle.height / 2;
	}

	public boolean onScreen(int width, int height) {
		boolean result = true;
		if (rectangle.width / 2 == width || rectangle.width == 0) {
			result = false;
		} else if (rectangle.height / 2 == height || rectangle.height / 2 == 0) {
			result = false;
		}

		return result;
	}
}
