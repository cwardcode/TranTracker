package edu.wcu.trackerapp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class TabListener implements ActionBar.TabListener {
    Fragment fragment;
    
    public TabListener(Fragment fragmentTab1) {
    	this.fragment = fragmentTab1;
    }
    
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
    	ft.replace(R.id.fragment_container, fragment);
    }
    
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    	ft.remove(fragment);
    }
    
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    	
    }
}
