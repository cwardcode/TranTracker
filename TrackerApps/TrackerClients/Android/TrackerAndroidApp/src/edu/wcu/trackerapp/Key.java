package edu.wcu.trackerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * This screen will display the map legend.
 * 
 * @author Hayden Thomas
 * @verison 10/27/13
 *
 */
public class Key extends Activity implements OnClickListener {

	/**
	 * The map button.
	 */
	private Button map;
	
	private Button chat;
	
	private Button key;
	
	private Button help;
	
	private Button about;
	//private Spinner menu;
	
	/*
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			                 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_key, container, 
				                         false);
		return rootView;
	}
	*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_key);
		
		//menu = (Spinner) findViewById(R.id.menuSpinnerKey);
		
		//menu.setOnItemSelectedListener(this);
		
		map = (Button) findViewById(R.id.keyMapButton);
		chat = (Button) findViewById(R.id.keyChatButton);
		key = (Button) findViewById(R.id.keyKeyButton);
		about = (Button) findViewById(R.id.keyAboutButton);
		help = (Button) findViewById(R.id.keyHelpButton);
		
		map.setOnClickListener(this);
		chat.setOnClickListener(this);
		key.setOnClickListener(this);
		about.setOnClickListener(this);
		help.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.key, menu);
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
	/*
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
	*/

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
		} else if (button.equals(chat)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.Chat.class);
			this.startActivity(next);
		} else if (button.equals(about)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.About.class);
			this.startActivity(next);
		} else if (button.equals(help)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.Help.class);
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
