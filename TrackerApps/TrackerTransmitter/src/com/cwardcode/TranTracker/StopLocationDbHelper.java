package com.cwardcode.TranTracker;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cwardcode.TranTracker.StopLocation.StopEntry;
import com.cwardcode.TranTracker.StopParser.StopDef;

public class StopLocationDbHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "TranTrack.db";

	private static final String TEXT_TYPE = " TEXT";
	private static final String DECI_TYPE = " REAL";
	private static final String COMMA_SEP = ", ";
	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
			+ StopEntry.TABLE_NAME + " (" + StopEntry._ID
			+ " INTEGER PRIMARY KEY," + StopEntry.COLUMN_NAME_STOP_ID
			+ TEXT_TYPE + COMMA_SEP + StopEntry.COLUMN_NAME_NAME + TEXT_TYPE
			+ COMMA_SEP + StopEntry.COLUMN_NAME_LAT + DECI_TYPE + COMMA_SEP
			+ StopEntry.COLUMN_NAME_LNG + DECI_TYPE + ")";

	private static final String SQL_DELETE_ENTRIES = "Drop table if exists "
			+ StopEntry.TABLE_NAME;

	public StopLocationDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_DELETE_ENTRIES);
		db.execSQL(SQL_CREATE_ENTRIES);
	}
	/**
	 * Equivalent to SELECT * WHERE STOP_ID = id
	 * @param id
	 */
	public StopDef getEntry(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT stopID, name, latitude, longitude FROM stop WHERE _ID = '"+id+"'",null);
		cur.moveToFirst();
		StopDef stop = new StopDef(Integer.parseInt(cur.getString(0)),cur.getString(1), Double.parseDouble(cur.getString(2)), Double.parseDouble(cur.getString(3)));
		return stop;
	}
	
	public List<StopDef> getAllEntries() {
		List<StopDef> list = new ArrayList<StopDef>();
		String query = "SELECT * FROM stop";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            StopDef stop = new StopDef();
	            String zero = cursor.getString(0);
	            String one = cursor.getString(1);
	            String two = cursor.getString(2);
	            String thre = cursor.getString(3);
	            String thfour = cursor.getString(4);
	            stop.setID(Integer.parseInt(cursor.getString(0)));
	            stop.setEntryName(cursor.getString(2));
	            stop.setEntryLat(cursor.getString(3));
	            stop.setEntryLng(cursor.getString(4));
	            // Adding contact to list
	            list.add(stop);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return list;
	}
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		db.execSQL(SQL_DELETE_ENTRIES);
		// Trash and recreate for now - just to test, ensure version later TODO
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
}
