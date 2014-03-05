package com.cwardcode.TranTracker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.video.BackgroundSubtractorMOG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Provides a user interface that allows the user to select which vehicle is
 * being tracked and begin tracking.
 *  
 * @author Hayden Thomas
 * @author Chris Ward
 * @version September 9, 2013
 */
public class TranTracker extends Activity implements AdapterView.OnItemSelectedListener, CvCameraViewListener2 {

    /** URL which returns JSON array of vehicles. */
    private static final String VID_URL = "http://tracker.cwardcode.com/static/getvid.php";
    /** Holds whether the application is tracking. */
    public static boolean IS_TRACKING;
    /** Holds whether a vehicle is selected. */
    public static boolean VEH_SELECT;
    /** Holds current application context. */
    private static Context context;
    /** Intent to hold sendLoc service. */
    private Intent srvIntent;
    /** Spinner to hold trackable vehicles. */
    Spinner gridSpinner;
    /** ArrayList of trackable vehicles. */
    private ArrayList<Vehicle> VehList;
    /** OpenCV Camera View */
    private CameraBridgeViewBase mOpenCvCameraView;
    /** Caputured image for processing */
	private Mat mRgba;
	/** Foreground mask **/
	private Mat mFGMask;
	/** Background Subtraction **/
	private BackgroundSubtractorMOG mBVSub;
    /** Greyscale image*/
	private Mat mGrey;
	
	private static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);
	private File                   mCascadeFile;
    private CascadeClassifier      mJavaDetector;

    private float                  mRelativeFaceSize   = 0.2f;
    private int                    mAbsoluteFaceSize   = 0;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                	try {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.haarcascade_mcs_upperbody);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (mJavaDetector.empty()) {
                            Log.e(this.getClass().toString(), "Failed to load cascade classifier");
                            mJavaDetector = null;
                        } else
                        cascadeDir.delete();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(this.getClass().toString(), "Failed to load cascade. Exception thrown: " + e);
                    }
                	mBVSub = new BackgroundSubtractorMOG();
                	mFGMask = new Mat();
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    /**
     * Pulls vehicle list from database.
     */
    private class GetVehicles extends AsyncTask<Void, Void, Void> {

        /**
         * Threaded processes, parses JSON output from server.
         *
         * @param arg0 not used
         * @return null to end task
         */
        @Override
        protected Void doInBackground(Void... arg0) {
            ParseJson jsonParser = new ParseJson();
            String json = jsonParser.makeServiceCall(VID_URL, ParseJson.GET);

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
            }

            return null;
        }

        /**
         * After main thread has finished, populate spinner with vehicles.
         * @param result return status of main thread.
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
     * @param parent - the <code>AdapterView</code> in which the view
     *                 exists.
     * @param view   - the <code>View</code> in from which the item was
     *               selected.
     * @param pos    - the position of the menu item seected.
     * @param id     - the id of the item pressed, if available.
     */
     public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        VEH_SELECT = true;
        srvIntent.putExtra("VehicleID", ++pos);
        srvIntent.putExtra("title", parent.getSelectedItem().toString());
     }

     /**
      * If nothing is selected, does nothing.
      *
      * @param arg0 - nothing.
      */
     public void onNothingSelected(AdapterView<?> arg0) {
         VEH_SELECT = false;
     }

    /**
     * Starts the service that allows the device to be tracked.
     * Suppressing unused warning for param, needed only for Android to know
     * calling view.
     * @param view the view for the button calling this method.
     */
    public void startTracking(View view) {
        if(!IS_TRACKING){
            startService(srvIntent);
            IS_TRACKING = true;
        }   else {
            stopService(srvIntent);
            startService(srvIntent);
        }
    }

    /**
     * Called when the activity is first created.
     * 
     * @param savedInstanceState allows the application to return to its
     *                           previous configuration after being closed
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TranTracker.context = getApplicationContext();

        IS_TRACKING = false;
        srvIntent = new Intent(this, SendLoc.class);
        VehList = new ArrayList<Vehicle>();

        gridSpinner = (Spinner) findViewById(R.id.VehicleSelect);
        gridSpinner.setOnItemSelectedListener(this);
        new GetVehicles().execute();
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.cameraView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
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
     * Returns the current application's context. Allows the service to send
     * a message to the broadcast receiver.
     */
    public static Context getAppContext() {
        return context;
    }
    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
   
	@Override
	public void onCameraViewStarted(int width, int height) {		
       mRgba = new Mat(height, width, CvType.CV_8UC4);

	}

	@Override
	public void onCameraViewStopped() {
		mRgba.release();
	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		//mRgba = inputFrame.gray();
		//mBVSub.apply(mRgba, mFGMask);
		//return mFGMask;
		mRgba = inputFrame.rgba();
        mGrey = inputFrame.gray();

        if (mAbsoluteFaceSize == 0) {
            int height = mGrey.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }

        MatOfRect faces = new MatOfRect();

            if (mJavaDetector != null) {
                mJavaDetector.detectMultiScale(mGrey, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                        new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        }
        

        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++)
            Core.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);

        return mRgba;
	}
}