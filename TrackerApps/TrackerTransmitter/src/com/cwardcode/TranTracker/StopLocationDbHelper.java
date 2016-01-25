package com.cwardcode.TranTracker;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.cwardcode.TranTracker.StopLocation.StopEntry;
import com.cwardcode.TranTracker.StopParser.StopDef;

/**
 * Handles setting up and tearing down our StopLocation database that is
 * created. Also handles returning all current StopLocations.
 * 
 * @author Chris Ward
 * @version June 1, 2014
 */
public class StopLocationDbHelper extends SQLiteOpenHelper {
	/** Current DB version. */
	public static final int DATABASE_VERSION = 1;
	/** Name of database to be created. */
	public static final String DATABASE_NAME = "TranTrackStopLocs.db";
	/** URL which returns XML representation of stops/vehicles on the route */
	private static final String STOPURL = "http://tracker.cwardcode.com/static/genxml.php";
	/** Define 'text' datatypes. */
	private static final String TEXT_TYPE = " TEXT";
	/** Define 'decimal' datatypes. */
	private static final String DECI_TYPE = " REAL";
	/** Define CS. */
	private static final String COMMA_SEP = ", ";
	/** Table create statement. */
	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
			+ StopEntry.TABLE_NAME + " (" + StopEntry._ID
			+ " INTEGER PRIMARY KEY," + StopEntry.COLUMN_NAME_STOP_ID
			+ TEXT_TYPE + COMMA_SEP + StopEntry.COLUMN_NAME_NAME + TEXT_TYPE
			+ COMMA_SEP + StopEntry.COLUMN_NAME_LAT + DECI_TYPE + COMMA_SEP
			+ StopEntry.COLUMN_NAME_LNG + DECI_TYPE + ")";

	/** Delete table statement. */
	private static final String SQL_DELETE_ENTRIES = "Drop table if exists "
			+ StopEntry.TABLE_NAME;
	/** Holds all stop definitions. */
	private List<StopDef> stopDefs;

	/**
	 * Call super to create SQLiteOpenHelper.
	 * 
	 * @param context
	 *            Current application context.
	 */
	public StopLocationDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Only called if we open a DB that doesn't exist, or if we need to upgrade.
	 * 
	 * @param db
	 *            The db to manage on DbHelper creation.
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(SQL_CREATE_ENTRIES);
			getXML();
			System.out.println("lol");
			ContentValues entry = new ContentValues();
			for (StopDef stop : stopDefs) {
				entry.put(StopEntry.COLUMN_NAME_STOP_ID, stop.getID());
				entry.put(StopEntry.COLUMN_NAME_NAME, stop.getStopTitle());
				entry.put(StopEntry.COLUMN_NAME_LAT, stop.getStopLat());
				entry.put(StopEntry.COLUMN_NAME_LNG, stop.getStopLng());
				long newRowID = db.insert(StopEntry.TABLE_NAME,
						StopEntry.COLUMN_NAME_NULL, entry);
				if (newRowID == -1) {
					Toast.makeText(
							TranTracker.getAppContext(),
							"Could not make" + " row for: "
									+ stop.getStopTitle(), Toast.LENGTH_SHORT)
							.show();
				}
			}

		} catch (SQLException sqEx) {
			Log.e("StopLocDBHelper", sqEx.getMessage());
		}
	}

	/**
	 * Sets up an InputStream to the specified server.
	 * 
	 * @param urlString
	 *            URL to download XML from.
	 * @return InputStream to server.
	 * @throws IOException
	 *             if there is a problem connecting to the server, etc.
	 */
	private InputStream downloadUrl(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.connect();
		return conn.getInputStream();
	}

	/**
	 * Parses an XML file with an XMLPullParser.
	 */
	private void getXML() {
		InputStream stream = null;
		StopParser parser = new StopParser();
		// Try to download and parse XML.
		try {
			stream = downloadUrl(STOPURL);
			parser.parseXML(stream);
			stopDefs = parser.getStopList();
		} catch (IOException e) {
			Log.e("StopLocationDBHelper", e.getMessage());
		} catch (XmlPullParserException e) {
			Log.e("StopLocationDBHelper", e.getMessage());
		} finally {
			// cleanup
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					Log.e("StopLocationDBHelper", e.getMessage());
				}
			}
		}
	}

	/**
	 * Equivalent to SELECT * WHERE STOP_ID = id
	 * 
	 * @param id
	 */
	public StopDef getEntry(int id) {
		// Grab DB
		SQLiteDatabase db = this.getReadableDatabase();
		// Select entry at current ID
		Cursor cur = db.rawQuery(
				"SELECT stopID, name, latitude, longitude FROM stop WHERE _ID = '"
						+ id + "'", null);
		cur.moveToFirst();

		// Create a definition for the current stop.
		StopDef stop = new StopDef(Integer.parseInt(cur.getString(0)),
				cur.getString(1), Double.parseDouble(cur.getString(2)),
				Double.parseDouble(cur.getString(3)));

		return stop;
	}

	/**
	 * Generates and returns all stops in the database.
	 * 
	 * @return list of stops in database.
	 */
	public List<StopDef> getAllEntries() {
		List<StopDef> list = new ArrayList<StopDef>();
		// Simple, but works.
		String query = "SELECT * FROM stop";
		// Grab DB.
		SQLiteDatabase db = this.getReadableDatabase();
		// Run query on database.
		Cursor cursor = db.rawQuery(query, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				StopDef stop = new StopDef();
				stop.setID(Integer.parseInt(cursor.getString(0)));
				stop.setStopTitle(cursor.getString(2));
				stop.setStopLat(cursor.getString(3));
				stop.setStopLng(cursor.getString(4));
				// Adding contact to list
				list.add(stop);
			} while (cursor.moveToNext());
		}

		return list;
	}

	/**
	 * Upgrades the database to the newest version.
	 * 
	 * @param db
	 *            The database on which to execute downgrade.
	 * @param oldVersion
	 *            The version number of the old database.
	 * @param newVersion
	 *            The version number of the new database.
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
	 *      int, int)
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		Log.d("StopLocDBHelper", "Upgrading database!");
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}

	/**
	 * When we downgrade, we upgrade again. Yep, you read that right. We
	 * shouldn't be downgrading since that would mean the map wouldn't be
	 * up-to-date.
	 * 
	 * @param db
	 *            The database on which to execute downgrade.
	 * @param oldVersion
	 *            The version number of the old database.
	 * @param newVersion
	 *            The version number of the new database.
	 * @see android.database.sqlite.SQLiteOpenHelper#onDowngrade(android.database
	 *      .sqlite.SQLiteDatabase, int, int)
	 */
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
}
