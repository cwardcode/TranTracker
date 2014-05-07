package com.cwardcode.TranTracker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.video.BackgroundSubtractorMOG;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cwardcode.TranTracker.SendLoc.LocationBinder;
import com.cwardcode.TranTracker.StopLocation.StopEntry;
import com.cwardcode.TranTracker.StopParser.StopDef;

/**
 * Provides a user interface that allows the user to select which vehicle is
 * being tracked and begin tracking.
 * 
 * @author Hayden Thomas
 * @author Chris Ward
 * @version September 9, 2013
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class TranTracker extends Activity implements
		AdapterView.OnItemSelectedListener, CvCameraViewListener2 {
	/** URL which returns JSON array of vehicles. */
	private static final String VID_URL = "http://tracker.cwardcode.com/static/getvid.php";
	/** Holds whether the application is tracking. */
	public static boolean IS_TRACKING;
	/** Holds whether the service is currently bound. */
	public static boolean IS_BOUND;
	/** Holds whether a vehicle is selected. */
	public static boolean VEH_SELECT;
	/** Color of the rectangle drawn around bodies */
	private static final Scalar BODY_RECT_COLOR = new Scalar(0, 255, 0, 255);
	/** Determines if we're using the android/Java camera */
	public static final int JAVA_DETECTOR = 0;
	/** Determines if we're using the native camera */
	public static final int NATIVE_DETECTOR = 1;
	private static final String STOPURL = "http://tracker.cwardcode.com/static/genxml.php";
	/** Set default detector to java */
	private int mDetectorType = JAVA_DETECTOR;
	/** Holds current application context. */
	private static Context context;
	/** Holds alert notifying user of no network */
	private static Builder noNetworkAlert;
	/** Intent to hold sendLoc service. */
	private Intent srvIntent;
	/** Shows the current number of people in the frame */
	TextView peopleData;
	/** Spinner to hold trackable vehicles. */
	private Spinner gridSpinner;
	/** ArrayList of trackable vehicles. */
	private ArrayList<Vehicle> VehList;
	/** OpenCV Camera View */
	private CameraBridgeViewBase mOpenCvCameraView;
	/** Caputured image for processing */
	private Mat mRgba;
	/** Greyscale image */
	private Mat mGrey;
	/** Holds non-counting image */
	private Mat nonCountImg;
	/** Holds the haar cascade file used to find bodies */
	private File mCascadeFile;
	/** Interprets mCascadeFile */
	private CascadeClassifier mJavaDetector;
	/** Size of body relative to screen */
	private float mRelativeBodySize = 0.2f;
	/** Size of body */
	private int mAbsoluteBodySize = 0;
	/** Holds our native OpenCV detector */
	private DetectionBasedTracker mNativeDetector;
	/** Holds all detector types */
	private String[] mDetectorName;
	/** A service instance */
	SendLoc locService;

	private List<MatOfPoint> contours;

	// private BackgroundSubtractorMOG2 mBVSub;
	private BackgroundSubtractorMOG mBVSub;
	private Mat mFGMask;
	private Mat mPyrDownMat;
	private Mat average;

	private MenuItem mItemFace40;
	private MenuItem mItemFace50;
	private MenuItem mItemFace30;
	private MenuItem mItemFace20;
	private MenuItem mItemType;
	private List<StopDef> stopDefs = new ArrayList<StopDef>();

	private List<Blob> blobs = new ArrayList<Blob>();
	private int blobID = 0;

	private int screenWidth;
	private int screenHeight;

	/** Initializes OpenCV */
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				// System.loadLibrary("opencv_java");
				System.loadLibrary("TranTracker");
				try {
					// load cascade file from application resources
					InputStream is = getResources().openRawResource(
							R.raw.haarcascade_mcs_upperbody);
					// Create 'cascade' directory on android device
					File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
					mCascadeFile = new File(cascadeDir,
							"haarcascade_upperbody.xml");
					FileOutputStream os = new FileOutputStream(mCascadeFile);

					byte[] buffer = new byte[4096];
					int bytesRead;
					// write cascade file to android file system
					while ((bytesRead = is.read(buffer)) != -1) {
						os.write(buffer, 0, bytesRead);
					}
					// close file streams
					is.close();
					os.close();
					// Set up java camera
					mJavaDetector = new CascadeClassifier(
							mCascadeFile.getAbsolutePath());
					if (mJavaDetector.empty()) {
						Log.e(this.getClass().toString(),
								"Failed to load cascade classifier :(");
						mJavaDetector = null;
					} else {
						mNativeDetector = new DetectionBasedTracker(
								mCascadeFile.getAbsolutePath(), 0);
						cascadeDir.delete();
					}
				} catch (IOException e) {
					Log.e(this.getClass().toString(),
							"Failed to load cascade. Exception thrown: "
									+ e.getMessage());
				}
				// Allow us to view OpenCV window.
				mOpenCvCameraView.enableView();

				// Initialize our background subtraction stuff.
				mBVSub = new BackgroundSubtractorMOG(5, 3, .1);
				average = new Mat();
			}
				break;
			default: {
				super.onManagerConnected(status);
			}
				break;
			}
		}
	};

	private class DownloadXmlTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			try {
				return loadXmlFromNetwork(urls[0]);
			} catch (IOException ex) {
				Log.d("com.cwardcode.TranTracker", ex.getMessage());
				return "IO Error";
			} catch (XmlPullParserException ex) {
				Log.d("com.cwardcode.TranTracker", ex.getMessage());
				return "Parser Erorr";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			updateSQL();
		}

		private String loadXmlFromNetwork(String urlString)
				throws XmlPullParserException, IOException {
			InputStream stream = null;
			StopParser parser = new StopParser();

			try {
				stream = downloadUrl(urlString);
				parser.parseXML(stream);
				stopDefs = parser.getStopList();
			} finally {
				if (stream != null) {
					stream.close();
				}
			}

			return "success";

		}

		private void updateSQL() {
			StopLocationDbHelper stopHelper = new StopLocationDbHelper(
					getApplicationContext());
			SQLiteDatabase db = stopHelper.getWritableDatabase();
			db.setVersion(0);
			db.close();
			db = stopHelper.getWritableDatabase();
			if (db.needUpgrade(2)) {
				ContentValues entry = new ContentValues();
				for (StopDef stop : stopDefs) {
					entry.put(StopEntry.COLUMN_NAME_STOP_ID, stop.id);
					entry.put(StopEntry.COLUMN_NAME_NAME, stop.title);
					entry.put(StopEntry.COLUMN_NAME_LAT, stop.sLat);
					entry.put(StopEntry.COLUMN_NAME_LNG, stop.sLong);
					long newRowID = db.insert(StopEntry.TABLE_NAME,
							StopEntry.COLUMN_NAME_NULL, entry);
					if (newRowID == -1) {
						Toast.makeText(getApplicationContext(),
								"Could not make" + " row for: " + stop.title,
								Toast.LENGTH_SHORT).show();
					}
				}
			}
			// List<StopDef> list = stopHelper.getAllEntries();
			db.close();
		}

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

	}

	/**
	 * Pulls vehicle list from database.
	 */
	private class GetVehicles extends AsyncTask<Void, Void, Void> {
		String json;

		/**
		 * Threaded processes, parses JSON output from server.
		 * 
		 * @param arg0
		 *            not used
		 * @return null to end task
		 */
		@Override
		protected Void doInBackground(Void... arg0) {
			ParseJson jsonParser = new ParseJson();
			json = jsonParser.makeServiceCall(VID_URL, ParseJson.GET);

			if (json != null) {
				try {
					JSONObject jsonObj = new JSONObject(json);
					JSONArray categories = jsonObj.getJSONArray("vehicles");
					for (int i = 0; i < categories.length(); i++) {
						JSONObject catObj = (JSONObject) categories.get(i);
						Vehicle cat = new Vehicle(catObj.getInt("id"),
								catObj.getString("name"));
						VehList.add(cat);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				Log.e("JSON Data", "Didn't receive any data from server!");
				this.cancel(true);
			}

			return null;
		}

		@Override
		protected void onCancelled() {
			noNetworkAlert.show();
		};

		/**
		 * After main thread has finished, populate spinner with vehicles.
		 * 
		 * @param result
		 *            return status of main thread.
		 */
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			populateSpinner();
		}

	}

	/**
	 * Triggered when an item within the spinner's list is selected.
	 * 
	 * @param parent
	 *            the <code>AdapterView</code> in which the view exists.
	 * @param view
	 *            the <code>View</code> in from which the item was selected.
	 * @param pos
	 *            the position of the menu item seected.
	 * @param id
	 *            the id of the item pressed, if available.
	 */
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		VEH_SELECT = true;
		srvIntent.putExtra("VehicleID", ++pos);
		srvIntent.putExtra("title", parent.getSelectedItem().toString());
	}

	/**
	 * If nothing is selected, does nothing.
	 * 
	 * @param arg0
	 *            - nothing.
	 */
	public void onNothingSelected(AdapterView<?> arg0) {
		VEH_SELECT = false;
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			LocationBinder binder = (LocationBinder) service;
			locService = binder.getService();
			IS_BOUND = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			IS_BOUND = false;
		}
	};

	/**
	 * Starts the service that allows the device to be tracked. Suppressing
	 * unused warning for param, needed only for Android to know calling view.
	 * 
	 * @param view
	 *            the view for the button calling this method.
	 */
	public void startTracking(View view) {
		Button resetButton = (Button) findViewById(R.id.trackButton);
		if (!IS_TRACKING) {
			startService(srvIntent);
			bindService(srvIntent, mConnection, Context.BIND_AUTO_CREATE);
			resetButton.setText(R.string.resetText);
			IS_TRACKING = true;
		} else {
			// clean up old service instance
			resetButton.setText(R.string.resetting);
			stopService(srvIntent);
			unbindService(mConnection);
			// create new service instance
			startService(srvIntent);
			bindService(srvIntent, mConnection, Context.BIND_AUTO_CREATE);
			resetButton.setText(R.string.resetText);
			Toast.makeText(context, "reset.", Toast.LENGTH_SHORT).show();
		}
	}

	public static void showDialog() {
		noNetworkAlert.show();
	}

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            allows the application to return to its previous configuration
	 *            after being closed
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TranTracker.context = getApplicationContext();

		Intent intent = getIntent();
		if (intent.hasExtra("exit")) {
			finish();
		}

		noNetworkAlert = new AlertDialog.Builder(this).setTitle("No Network!")
				.setPositiveButton("Exit",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(TranTracker.context,
										TranTracker.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra("exit", "true");
								startActivity(intent);
							}

						});

		peopleData = (TextView) findViewById(R.id.PeopleDataView);

		mDetectorName = new String[2];
		mDetectorName[JAVA_DETECTOR] = "Java";
		mDetectorName[NATIVE_DETECTOR] = "Native (tracking)";

		IS_TRACKING = false;
		srvIntent = new Intent(this, SendLoc.class);
		VehList = new ArrayList<Vehicle>();

		gridSpinner = (Spinner) findViewById(R.id.VehicleSelect);
		gridSpinner.setOnItemSelectedListener(this);

		new GetVehicles().execute();
		new DownloadXmlTask().execute(STOPURL);
		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.cameraView);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		mOpenCvCameraView.setCvCameraViewListener(this);
		contours = new ArrayList<MatOfPoint>();

	}

	/**
	 * Allows us to update the peopleData TextView with the current number of
	 * people found in an image.
	 * 
	 * @param numPeople
	 */
	private void setPeopleData(final int numPeople) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (IS_BOUND) {
					peopleData.setText("People: " + numPeople);
				}
			}
		});
	}

	/**
	 * Creates and attaches ArrayAdapter to populate spinner.
	 */
	private void populateSpinner() {
		ArrayList<String> vehicles = new ArrayList<String>();
		for (Vehicle aVehList : VehList) {
			vehicles.add(aVehList.getName());
		}

		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, vehicles);

		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		gridSpinner.setAdapter(spinnerAdapter);
	}

	/**
	 * Returns the current application's context. Allows the service to send a
	 * message to the broadcast receiver.
	 */
	public static Context getAppContext() {
		return context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPause() {
		super.onPause();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onResume() {
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this,
				mLoaderCallback);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCameraViewStarted(int width, int height) {
		mRgba = new Mat(height, width, CvType.CV_8UC4);
		mGrey = new Mat(height, width, CvType.CV_8UC4);
		mFGMask = new Mat(height, width, CvType.CV_8UC4);
		nonCountImg = new Mat(height, width, CvType.CV_8UC4);

		screenWidth = width;
		screenHeight = height;

		InputStream bitmap;
		Bitmap img = null;
		try {
			bitmap = getAssets().open("notinitailized.jpg");
			img = BitmapFactory.decodeStream(bitmap);
			if (bitmap != null) {
				bitmap.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (img != null) {
			img.copy(Bitmap.Config.ARGB_8888, true);
			Utils.bitmapToMat(img, nonCountImg.reshape(width, height));
		} else {
			Log.e("TranTracker", "Could not load image");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCameraViewStopped() {
		mRgba.release();
		mGrey.release();
		nonCountImg.release();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Generates rectangles around upper-body objects.
	 */
	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		// Holds color frame
		mRgba = inputFrame.rgba();
		// Holds grey frame
		mGrey = inputFrame.gray();
		// Check to see if near a stop, and it is stopped.
		while (IS_BOUND && locService.isNearLoc() && locService.isStopped()) {
			if (mAbsoluteBodySize == 0) {
				int height = mGrey.rows();
				if (Math.round(height * mRelativeBodySize) > 0) {
					mAbsoluteBodySize = Math.round(height * mRelativeBodySize);
				}
			}
			mNativeDetector.setMinFaceSize(mAbsoluteBodySize);

			MatOfRect faces = new MatOfRect();

			if (mJavaDetector != null) {
				mJavaDetector.detectMultiScale(mGrey, faces, 1.1, 2, 2,
						new Size(mAbsoluteBodySize, mAbsoluteBodySize),
						new Size());
			}

			Rect[] facesArray = faces.toArray();
			// Draw rect around detected person.
			// TODO: label and track person so not counted twice.
			for (int i = 0; i < facesArray.length; i++) {
				Core.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(),
						BODY_RECT_COLOR, 3);

				Blob blob = new Blob(facesArray[i], blobID);
				if (blobs.size() == 0) {
					blobs.add(blob);
				}

				boolean blobFound = false;

				for (Blob b : blobs) {

					if (b.onScreen(screenWidth, screenHeight)) {
						double blobDiff = blob.area() - b.area();

						// Check to see if the blobs are about the same
						// size.
						if (Math.abs(blobDiff) < 1200) {
							// Check to see if the distance between blobs is
							// the same-ish.
							int yDist = Math.abs(blob.y() - b.y());
							int xDist = Math.abs(blob.x() - b.x());

							if ((yDist < 100) && (xDist < 100)) {
								// Pretty sure the blobs are the same, so we
								// replace instead of adding.
								blob.setID(b.id());

								blobs.set(blobs.indexOf(b), blob);

								blobFound = true;
							}
						}
					}
				}

				if (!blobFound) {
					blobs.add(blob);
					blobID++;
				}
				// setPeopleData(facesArray.length);

				// List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

				/* mBVSub.apply(mGrey, mFGMask, 0.01); */

				// Mat hierarchy = new Mat();

				// Scalar color = new Scalar(255);

				// Imgproc.findContours(mFGMask, contours, hierarchy,
				// Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_NONE);

				// Imgproc.findContours(mFGMask, contours, hierarchy,
				// Imgproc.RETR_EXTERNAL, Imgproc.CV_CONTOURS_MATCH_I1);

				// for (int i = 0; i < contours.size(); i++) {
				// List<Point> points = new ArrayList<Point>(5);
				// Mat contour = contours.get(i);
				// double contourArea = Imgproc.contourArea(contour);

				// if (contourArea > 1000) {
				// int num = (int) contour.total();
				// int buff[] = new int[num * 2];
				// contour.get(0, 0, buff);
				// for (int j = 0; j < num * 2; j = j + 2) {
				// points.add(new Point(buff[j], buff[j + 1]));
				// }

				// Point[] pointArray = new Point[0];

				// pointArray = points.toArray(pointArray);
				// MatOfPoint rectPoints = new MatOfPoint(pointArray);
				// Core.rectangle(mFGMask, Imgproc.boundingRect(rectPoints)
				// .br(), Imgproc.boundingRect(rectPoints).tl(),
				// new Scalar(255, 255, 0, 255));
				// Core.putText(mFGMask, "ANT",
				// Imgproc.boundingRect(rectPoints)
				// .tl(), Core.FONT_HERSHEY_COMPLEX, 1,
				// new Scalar(255, 200, 0, 255));

				// Core.rectangle(mFGMask, new Point(0, 0), new Point(100,
				// 100), new Scalar(0, 255, 255, 255));

				// Blob blob = new Blob(Imgproc.boundingRect(rectPoints),
				// blobID);

				/*
				 * if (blobs.size() == 0) { blobs.add(blob); }
				 * 
				 * boolean blobFound = false;
				 * 
				 * for (Blob b : blobs) {
				 * 
				 * if (b.onScreen(screenWidth, screenHeight)) { double blobDiff
				 * = blob.area() - b.area();
				 * 
				 * // Check to see if the blobs are about the same // size. if
				 * (Math.abs(blobDiff) < 1200) { // Check to see if the distance
				 * between blobs is // the same-ish. int yDist =
				 * Math.abs(blob.y() - b.y()); int xDist = Math.abs(blob.x() -
				 * b.x());
				 * 
				 * if ((yDist < 100) && (xDist < 100)) { // Pretty sure the
				 * blobs are the same, so we // replace instead of adding.
				 * blob.setID(b.id());
				 * 
				 * blobs.set(blobs.indexOf(b), blob);
				 * 
				 * blobFound = true; } } } }
				 * 
				 * if (!blobFound) { blobs.add(blob); blobID++; }
				 * 
				 * } }
				 */

				// Imgproc.drawContours(mFGMask, contours, -1, color, -1);

				// Log.d("contours", "Number of contours found: " +
				// contours.size());

				// Bitmap bmp = Bitmap.createBitmap(mFGMask.cols(),
				// mFGMask.rows(),
				// Bitmap.Config.ARGB_8888);

				// Utils.matToBitmap(mFGMask, bmp);

				// Imgproc.pyrDown(mGrey, mPyrDownMat);
				// Imgproc.pyrDown(mPyrDownMat, mPyrDownMat);

				// List<MatOfPoint> contours = new ArrayList<MatofPoint>();

				// Imgproc.findContours(image, contours, hierarchy, mode,
				// method);
				setPeopleData(blobs.size());
				//return mFGMask;
				return mRgba;
			}

		}
		// setPeopleData(blobs.size());
		blobs.clear();

		return nonCountImg;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i("", "called onCreateOptionsMenu");
		mItemFace50 = menu.add("Face size 50%");
		mItemFace40 = menu.add("Face size 40%");
		mItemFace30 = menu.add("Face size 30%");
		mItemFace20 = menu.add("Face size 20%");
		mItemType = menu.add(mDetectorName[mDetectorType]);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i("", "called onOptionsItemSelected; selected item: " + item);

		if (item == mItemFace50)
			setMinFaceSize(0.5f);
		else if (item == mItemFace40)
			setMinFaceSize(0.4f);
		else if (item == mItemFace30)
			setMinFaceSize(0.3f);
		else if (item == mItemFace20)
			setMinFaceSize(0.2f);
		else if (item == mItemType) {
			mDetectorType = (mDetectorType + 1) % mDetectorName.length;
			item.setTitle(mDetectorName[mDetectorType]);
			setDetectorType(mDetectorType);
		}
		return true;
	}

	/**
	 * Sets the minimum threshold for a face
	 * 
	 * @param faceSize
	 */
	private void setMinFaceSize(float faceSize) {
		mRelativeBodySize = faceSize;
		mAbsoluteBodySize = 0;
	}

	/**
	 * Sets whether to use a native detector or the android java detector.
	 * 
	 * @param type
	 */
	private void setDetectorType(int type) {
		if (mDetectorType != type) {
			mDetectorType = type;

			if (type == NATIVE_DETECTOR) {
				Log.i("", "Detection Based Tracker enabled");
				mNativeDetector.start();
			} else {
				Log.i("", "Cascade detector enabled");
				mNativeDetector.stop();
			}
		}
	}
}