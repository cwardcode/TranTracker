package edu.wcu.trackerapp;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class MainActivity extends Activity {
	
	ActionBar.Tab mapTab;
	ActionBar.Tab keyTab;
	ActionBar.Tab aboutTab;
	
	//Fragment mapFragment = (Fragment) new Map();
	//Fragment keyFragment = new Key();
	//Fragment aboutFragment = new About();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		mapTab = actionBar.newTab();
		keyTab = actionBar.newTab();
		aboutTab = actionBar.newTab();
		
		//mapTab.setTabListener(new TabListener(mapFragment));
		//keyTab.setTabListener(new TabListener(keyFragment));
		//aboutTab.setTabListener(new TabListener(aboutFragment));
		
		//actionBar.addTab(mapTab);
		//actionBar.addTab(keyTab);
		//actionBar.addTab(aboutTab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
