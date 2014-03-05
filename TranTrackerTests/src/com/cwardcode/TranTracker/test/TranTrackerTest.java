package com.cwardcode.TranTracker.test;

import android.test.ActivityInstrumentationTestCase2;

import com.cwardcode.TranTracker.SplashActivity;
import com.robotium.solo.Solo;

public class TranTrackerTest extends ActivityInstrumentationTestCase2<SplashActivity> {
	private Solo solo;
	
	public TranTrackerTest(){
		super(SplashActivity.class);
	}
	
	public void setup() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	public void testDropDownPopulates() {
		//ArrayList<Spinner> spins = solo.getCurrentViews(Spinner.class);
		//assertTrue(spins.get(0).getAdapter().getCount() != 0);
	}
	public void testDropDownTracking() {
		solo.pressSpinnerItem(0, 1);
		solo.clickOnButton("Start Tracking!");
		solo.pressSpinnerItem(0, 0);
		solo.clickOnButton("Start Tracking!");
	}
	
	@Override
	public void tearDown() throws Exception {
	   // solo.finishOpenedActivities();
	 }
}
