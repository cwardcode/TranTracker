package com.cwardcode.TranTracker;

import android.provider.BaseColumns;

public final class StopLocation {
	// Do not instantiate
	public StopLocation() {
	}

	public static abstract class StopEntry implements BaseColumns {
		public static final String TABLE_NAME          = "stop";
		public static final String COLUMN_NAME_STOP_ID = "stopID";
		public static final String COLUMN_NAME_NAME    = "name";
		public static final String COLUMN_NAME_LAT     = "latitude";
		public static final String COLUMN_NAME_LNG     = "longitude";
		public static final String COLUMN_NAME_NULL    = "null";
	}
}
