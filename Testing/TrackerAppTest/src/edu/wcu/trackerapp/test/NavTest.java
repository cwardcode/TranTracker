package edu.wcu.trackerapp.test;

import com.robotium.solo.Solo;

import edu.wcu.trackerapp.Map;
import edu.wcu.trackerapp.R;
import android.test.ActivityInstrumentationTestCase2;

public class NavTest extends ActivityInstrumentationTestCase2<Map> {

	private Solo solo;
	
	public NavTest() {
		super(Map.class);
	}
	
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testNavigation() throws Exception {
		solo.waitForFragmentById(R.id.map);
		solo.clickOnButton("Map");
		solo.clickOnButton("Key");
		solo.waitForActivity("Key");
		assertTrue(solo.searchText("Key"));
		solo.clickOnButton("Help");
		solo.waitForActivity("Help");
		assertTrue(solo.searchText("Help"));
		solo.clickOnButton("About");
		solo.waitForActivity("About");
		assertTrue(solo.searchText("About"));
		solo.clickOnButton("Map");
		solo.waitForActivity("Map");
		assertTrue(solo.searchText("Map"));
	}
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}


