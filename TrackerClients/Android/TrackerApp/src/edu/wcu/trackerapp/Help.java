package edu.wcu.trackerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * This class represents the application's help page.
 * 
 * @author Hayden Thomas
 * @version 10/28/13
 * 
 */
public class Help extends Activity implements OnClickListener {
	
	/**
	 * The map button.
	 */
	private Button map;
	
	private Button key;
	
	private Button help;
	
	private Button about;
	
	/**
	 * Initializes the activity.
	 * 
	 * @param savedInstanceState saved data from a previous run, if any.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		map = (Button) findViewById(R.id.helpMapButton);
		key = (Button) findViewById(R.id.helpKeyButton);
		about = (Button) findViewById(R.id.helpAboutButton);
		help = (Button) findViewById(R.id.helpHelpButton);
		
		map.setOnClickListener(this);
		key.setOnClickListener(this);
		about.setOnClickListener(this);
		help.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}
	
    /**
     * Performs the appropriate action when an item is selected.
     * 
     * @param parent the object containing all of the list items.
     * @param view ?
     * @param pos the items position.
     * @param id ?
     */
	/*
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		String item = parent.getItemAtPosition(pos).toString();
		
		handleSelection(item);
		
	}
	*/
	
	/**
	 * Performs the action that is appropriate for the given selection. In this
	 * case, we will match the activity that matches the selection.
	 * 
	 * @param selection a string representing the user's selection.
	 */
	@SuppressWarnings("unused")
	private void handleSelection(String selection) {
		Intent next = null;
		if (selection.equals("Key")) {
			next = new Intent(this, edu.wcu.trackerapp.Key.class);
			this.startActivity(next);
		} else if (selection.equals("Contact")) {
			next = new Intent(this, edu.wcu.trackerapp.Contact.class);
			this.startActivity(next);
		} else if (selection.equals("Help")) {
			next = new Intent(this, edu.wcu.trackerapp.Help.class);
			this.startActivity(next);
		} else if (selection.equals("About")) {
			next = new Intent(this, edu.wcu.trackerapp.About.class);
			this.startActivity(next);
		}
	}

	/**
	 * Returns to the home screen when the home button is pressed.
	 * 
	 * @param v the view that was pressed.
	 */
	@Override
	public void onClick(View v) {
		Button button = (Button) v;	
		
		if (button.equals(map)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.Map.class);
			this.startActivity(next);
		} else if (button.equals(key)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.Key.class);
			this.startActivity(next);
		} else if (button.equals(about)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.About.class);
			this.startActivity(next);
		}
		
	}

	/*
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	*/


}
