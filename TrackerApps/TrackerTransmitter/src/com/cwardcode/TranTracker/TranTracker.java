package com.cwardcode.TranTracker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cwardcode.TranTracker.SendLoc.LocationBinder;

/**
 * Provides a user interface that allows the user to select which vehicle is
 * being tracked and begin tracking. Provides integration with OpenCV to allow
 * ridership statistic tracking.
 * 
 * @author Hayden Thomas
 * @author Chris Ward
 * @version June 1, 2014
 */
public class TranTracker extends Activity
		implements
			AdapterView.OnItemSelectedListener,
			CvCameraViewListener2 {
	/** Holds application tracking state. */
	public static boolean IS_TRACKING;
	/** Holds service state. */
	public static boolean IS_BOUND;
	/** Holds whether a vehicle is selected. */
	public static boolean VEH_SELECT;
	/**
	 * TODO: Uncomment if we switch back to blob-detection. Color of the
	 * rectangle drawn around bodies.
	 * 
	 * private static final Scalar BODY_RECT_COLOR = new Scalar(0, 255, 0, 255);
	 */
	/** Determines if we're using the native camera */
	public static final int NATIVE_DETECTOR = 1;
	/** Holds current application context. */
	private static Context context;
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
	/** A service instance */
	private SendLoc locService;
	/**
	 * TODO: May use in the future, commenting out for warnings. private
	 * List<MatOfPoint> contours;
	 */
	/** Using MOG Background Subtraction Alg. */
	private BackgroundSubtractorMOG mBVSub;
	private Mat mFGMask;
	/**
	 * TODO May use in future, commenting for warnings.
	 * 
	 * private Mat mPyrDownMat;
	 * 
	 * private Mat average;
	 */
	/** List of blobs counted. */
	private List<Blob> blobs = new ArrayList<Blob>();
	
	/** List of blobs entering the shuttle */
	private List<Blob> onBlobs = new ArrayList<Blob>();
	
	/** List of blobs exiting the shuttle */
	private List<Blob> offBlobs = new ArrayList<Blob>();
	/** Initialize blobID to zero. */
	private int blobID = 0;
	/** Holds current screen width. */
	private int screenWidth;
	/** Holds current screen height. */
	private int screenHeight;

	/** Initializes OpenCV */
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
				case LoaderCallbackInterface.SUCCESS : {
					// System.loadLibrary("opencv_java");
					System.loadLibrary("TranTracker");
					try {
						// load cascade file from application resources
						InputStream is = getResources().openRawResource(
								R.raw.haarcascade_mcs_upperbody);
						// Create 'cascade' directory on android device
						File cascadeDir = getDir("cascade",
								Context.MODE_PRIVATE);
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

					} catch (IOException e) {
						Log.e(this.getClass().toString(),
								"Failed to load cascade, " + e.getMessage());
					}
					// Allow us to view OpenCV window.
					mOpenCvCameraView.enableView();

					// Initialize our background subtraction stuff.
					mBVSub = new BackgroundSubtractorMOG(5, 3, .1, 20);
					// TODO: Uncomment if we use field.
					// average = new Mat();
				}
					break;
				default : {
					super.onManagerConnected(status);
				}
					break;
			}
		}
	};

	/**
	 * Triggered when an item within the spinner is selected.
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

	/** Allows direct communication between the service and activity. */
	private ServiceConnection mConnection = new ServiceConnection() {

		/**
		 * When service is started, create pointer to it.
		 * 
		 * @param className
		 *            Name of class calling service
		 * @param service
		 *            Service to be bound.
		 * @see android.content.ServiceConnection#onServiceConnected(android.content.ComponentName,
		 *      android.os.IBinder)
		 */
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			LocationBinder binder = (LocationBinder) service;
			locService = binder.getService();
			IS_BOUND = true;
		}
		/**
		 * Notifies activity that service has disconnected.
		 * 
		 * @param className
		 *            unused
		 * @see android.content.ServiceConnection#onServiceDisconnected(android.content.ComponentName)
		 */
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

		peopleData = (TextView) findViewById(R.id.PeopleDataView);

		IS_TRACKING = false;
		srvIntent = new Intent(this, SendLoc.class);
		Bundle bundle = getIntent().getExtras();
		VehList = bundle.getParcelable("vehicles");

		gridSpinner = (Spinner) findViewById(R.id.VehicleSelect);
		gridSpinner.setOnItemSelectedListener(this);
		populateSpinner();
		
		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.cameraView);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		mOpenCvCameraView.setCvCameraViewListener(this);
		// TODO Uncomment if we decide to do contour stuff
		// contours = new ArrayList<MatOfPoint>();

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
		for (Vehicle aVeh : VehList) {
			vehicles.add(aVeh.getName());
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
	 * 
	 * @return Application Context
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
		while (true) {//(IS_BOUND && locService.isNearLoc() && locService.isStopped()) {
			/*
			 * if (mAbsoluteBodySize == 0) { int height = mGrey.rows(); if
			 * (Math.round(height * mRelativeBodySize) > 0) { mAbsoluteBodySize
			 * = Math.round(height * mRelativeBodySize); } }
			 * mNativeDetector.setMinFaceSize(mAbsoluteBodySize);
			 * 
			 * MatOfRect faces = new MatOfRect();
			 * 
			 * if (mJavaDetector != null) {
			 * mJavaDetector.detectMultiScale(mGrey, faces, 1.1, 2, 2, new
			 * Size(mAbsoluteBodySize, mAbsoluteBodySize), new Size()); }
			 * 
			 * Rect[] facesArray = faces.toArray(); // Draw rect around detected
			 * person. // TODO: label and track person so not counted twice. for
			 * (int i = 0; i < facesArray.length; i++) { Core.rectangle(mRgba,
			 * facesArray[i].tl(), facesArray[i].br(), BODY_RECT_COLOR, 3);
			 * 
			 * Blob blob = new Blob(facesArray[i], blobID); if (blobs.size() ==
			 * 0) { blobs.add(blob); }
			 * 
			 * boolean blobFound = false;
			 * 
			 * for (Blob b : blobs) {
			 * 
			 * if (b.onScreen(screenWidth, screenHeight)) { double blobDiff =
			 * blob.area() - b.area();
			 * 
			 * // Check to see if the blobs are about the same // size. if
			 * (Math.abs(blobDiff) < 1200) { // Check to see if the distance
			 * between blobs is // the same-ish. int yDist = Math.abs(blob.y() -
			 * b.y()); int xDist = Math.abs(blob.x() - b.x());
			 * 
			 * if ((yDist < 100) && (xDist < 100)) { // Pretty sure the blobs
			 * are the same, so we // replace instead of adding.
			 * blob.setID(b.id());
			 * 
			 * blobs.set(blobs.indexOf(b), blob);
			 * 
			 * blobFound = true; } } } }
			 * 
			 * if (!blobFound) { blobs.add(blob); blobID++; }
			 */
			// setPeopleData(facesArray.length);

			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

			mBVSub.apply(mGrey, mFGMask, 0.15);

			Mat hierarchy = new Mat();

			Scalar color = new Scalar(255);

			// Imgproc.findContours(mFGMask, contours, hierarchy,
			// Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_NONE);

			Imgproc.findContours(mFGMask, contours, hierarchy,
					Imgproc.RETR_EXTERNAL, Imgproc.CV_CONTOURS_MATCH_I1);

			for (int i = 0; i < contours.size(); i++) {
				List<Point> points = new ArrayList<Point>(5);
				Mat contour = contours.get(i);
				double contourArea = Imgproc.contourArea(contour);

				if (contourArea > 1000) {
					int num = (int) contour.total();
					int buff[] = new int[num * 2];
					contour.get(0, 0, buff);

					for (int j = 0; j < num * 2; j = j + 2) {
						points.add(new Point(buff[j], buff[j + 1]));
					}

					Point[] pointArray = new Point[0];

					pointArray = points.toArray(pointArray);
					MatOfPoint rectPoints = new MatOfPoint(pointArray);

					Core.rectangle(mFGMask, Imgproc.boundingRect(rectPoints)
							.br(), Imgproc.boundingRect(rectPoints).tl(),
							new Scalar(255, 255, 0, 255));

					Blob blob = new Blob(Imgproc.boundingRect(rectPoints),
							blobID);

					if (blobs.size() == 0) {
						blobs.add(blob);
					}

					boolean blobFound = false;

					for (Blob b : blobs) {

						if (b.onScreen(screenWidth, screenHeight)) {
							double blobDiff = blob.area() - b.area();

							// Check to see if the blobs are about the same //
							// size.
							if (Math.abs(blobDiff) < 1200) {
								// Check to see if the distance
								// between blobs is the same-ish.
								int yDist = Math.abs(blob.y() - b.y());
								int xDist = Math.abs(blob.x() - b.x());

								if ((yDist < 100) && (xDist < 100)) {
									// Pretty sure the
									// blobs are the same, so we replace instead
									// of adding.
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
					
					findBlobDir(blob);

				}

			}

			Imgproc.drawContours(mFGMask, contours, -1, color, -1);

			Log.d("contours", "Number of contours found: " + contours.size());
			Log.d("blobs", "Number of blobs found: " + blobs.size());
			Log.d("blobs", "Number entering: " + onBlobs.size());
			Log.d("blobs", "Number exiting: " + offBlobs.size());
			setPeopleData(blobs.size());
			return mFGMask;

		}
		// setPeopleData(blobs.size());
		//blobs.clear();

		//return mGrey;
	}
	
	private void findBlobDir(Blob blob) {
		/* First, we set up our intersection points. These points let us 
		 * tell the current direction of our blob.
		 */
		
		/* The lefthand point. If a blob started to the left of this point,
		 * we assume that it is entering the shuttle. It should be the right 
		 * hand edge of the rectangle that forms the leftmost 4th of the 
		 * screen.
		 */
	    int leftPoint = screenWidth / 4;
	    
	    /* The righthand point, used to see if the blob is exiting. It is the 
	     * lefthand edge of the rightmost 4th of the screen. Taking the width
	     * of the screen and subtracting it from 1/4 of itself should
	     * give us this point.
	     */
		int rightPoint = screenWidth - leftPoint; 
		
		// Check to see if the blob was already entering or exiting.
		if (!(blob.getEntrance() == -1)) {
			// Now we check the state of entering blobs.
			if (blob.getEntrance() == 0) {
				if (blob.x() < leftPoint) {
					// This means that the blob is either yet to enter
					// or changed its mind, so we set its state to 0.
					blob.setDir(0);
				} else {
					// This means that it is entering the bus.
					blob.setDir(1);
				}
			// Now we check exiting blobs with the same method.
			} else if (blob.getEntrance() == 1) {
				if (blob.x() > rightPoint) {
					blob.setDir(0);
				} else {
					blob.setDir(2);
				}
			}
		
		// Now, find the direction of a blob that has just entered the screen.
		} else {
			// Check to see if the blob is entering from the left.
			if (blob.x() < leftPoint) {
				blob.setEntrance(0);
			// Now we check to see if the blob is entering from the right.
			} else if (blob.x() > rightPoint) {
				blob.setEntrance(1);
			}
		}
		
		// Now, we update our blob lists appropriately.
		if (blob.getDir() == 1) {
			onBlobs.add(blob);
		} else if (blob.getDir() == 2) {
			offBlobs.add(blob);
		}
		
		if (onBlobs.contains(blob) && blob.getDir() != 1) {
			onBlobs.remove(blob);
		} else if (offBlobs.contains(blob) && blob.getDir() != 2) {
			offBlobs.remove(blob);
		}
		
		Log.d("blobs", "Blob dir: " + blob.getDir());
		Log.d("blobs", "Blob entrance: " + blob.getEntrance());
		// Hope it works!
	}
}