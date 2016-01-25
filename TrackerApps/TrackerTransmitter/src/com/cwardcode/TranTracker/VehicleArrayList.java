package com.cwardcode.TranTracker;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * Implementation of an ArrayList that implements Parcelable to allow us to pass
 * the list of vehicles from SplashActivity to TranTracker's main Activity.
 * 
 * @author Chris Ward
 * @version June 01, 2014
 */
public class VehicleArrayList extends ArrayList<Vehicle> implements Parcelable {
	/** Serialized */
	private static final long serialVersionUID = -2295614094441046909L;
	/** Creates the Parcelable object. */
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		/**
		 * (non-Javadoc)
		 * 
		 * @see android.os.Parcelable.Creator#createFromParcel(android.os.Parcel)
		 */
		public VehicleArrayList createFromParcel(Parcel parcelIn) {
			return new VehicleArrayList(parcelIn);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.os.Parcelable.Creator#newArray(int)
		 */
		@Override
		public Object[] newArray(int size) {
			return null;
		}
	};

	/**
	 * Creates a VehicleArrayList object from a Parcel.
	 * 
	 * @param inParcel
	 */
	public VehicleArrayList(Parcel inParcel) {
		readFromParcel(inParcel);
	}

	/**
	 * Parse the parcel into an arraylist.
	 * 
	 * @param parcel
	 */
	public void readFromParcel(Parcel parcel) {
		this.clear();
		int size = parcel.readInt();

		// Order is important. Read in as we wrote out.
		for (int i = 0; i < size; i++) {
			Vehicle vehicleElement = new Vehicle();
			vehicleElement.setId(parcel.readInt());
			vehicleElement.setName(parcel.readString());
			this.add(vehicleElement);
		}
	}

	/**
	 * Creates empty VehicleArrayList object.
	 */
	public VehicleArrayList() {
	}

	/**
	 * Returns creator, just wrote this so we wouldn't get an "Unused" warning.s
	 * 
	 * @return the creator
	 */
	@SuppressWarnings("unchecked")
	public Parcelable.Creator<Vehicle> getCreator() {
		return CREATOR;
	}

	/**
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		// How about no? Good.
		return 0;
	}

	/**
	 * Specifies how to write-out a parcel.
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel outParcel, int flags) {
		// Set size of this arraylist.
		int size = this.size();
		outParcel.writeInt(size);
		// Must be read back in this order as well.
		for (int i = 0; i < size; i++) {
			Vehicle vehicleElement = this.get(i);
			outParcel.writeInt(vehicleElement.getId());
			outParcel.writeString(vehicleElement.getName());
		}
	}
}
