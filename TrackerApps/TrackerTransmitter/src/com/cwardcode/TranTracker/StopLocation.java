package com.cwardcode.TranTracker;

import android.provider.BaseColumns;

/**
 * Models a StopLocation
 * 
 * @author Chris Ward
 * @version June 1, 2014
 */
public final class StopLocation {
	/**
	 * Default constructor. Do not instantiate.
	 */
	public StopLocation() {
	}

	/**
	 * Models a StopLocation as an SQLite database.
	 * 
	 * @author chris
	 * @version June 1, 2014
	 */
	public static abstract class StopEntry implements BaseColumns {
		public static final String TABLE_NAME = "stop";
		public static final String COLUMN_NAME_STOP_ID = "stopID";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_LAT = "latitude";
		public static final String COLUMN_NAME_LNG = "longitude";
		public static final String COLUMN_NAME_NULL = "null";
	}
}
