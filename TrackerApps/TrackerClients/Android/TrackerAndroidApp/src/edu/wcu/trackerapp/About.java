package edu.wcu.trackerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * This class represents the application's about page.
 * 
 * @author Hayden Thomas
 * @version 10/28/13
 *
 */
public class About extends Activity implements OnClickListener {
	
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
		setContentView(R.layout.activity_about);

		map = (Button) findViewById(R.id.aboutMapButton);
		key = (Button) findViewById(R.id.aboutKeyButton);
		about = (Button) findViewById(R.id.aboutAboutButton);
		help = (Button) findViewById(R.id.aboutHelpButton);
		
		map.setOnClickListener(this);
		key.setOnClickListener(this);
		about.setOnClickListener(this);
		help.setOnClickListener(this);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
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
			next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(next);
		} else if (button.equals(key)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.Key.class);
			next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(next);
		} else if (button.equals(help)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.Help.class);
			next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(next);
		}
		
	}
}
