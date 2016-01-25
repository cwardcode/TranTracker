package com.cwardcode.trackerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.cwardcode.trackerapp.R;

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
	
	private Button routes;
	
	private Button stops;
	
	SharedPreferences settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle("TranTracker");
		setContentView(R.layout.activity_key);
		
		settings = getSharedPreferences(AppConstants.PREFS, 
					Context.MODE_PRIVATE);
		
		map = (Button) findViewById(R.id.keyMapButton);
		chat = (Button) findViewById(R.id.keyChatButton);
		key = (Button) findViewById(R.id.keyKeyButton);
		about = (Button) findViewById(R.id.keyAboutButton);
		help = (Button) findViewById(R.id.keyHelpButton);
		routes = (Button) findViewById(R.id.route_button);
		stops = (Button) findViewById(R.id.stop_button);
		
		map.setOnClickListener(this);
		chat.setOnClickListener(this);
		key.setOnClickListener(this);
		about.setOnClickListener(this);
		help.setOnClickListener(this);
		routes.setOnClickListener(this);
		stops.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.key, menu);
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
			Intent next = new Intent(this, com.cwardcode.trackerapp.Map.class);
			next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(next);
		} else if (button.equals(chat)) {
			Intent next = new Intent(this, com.cwardcode.trackerapp.Chat.class);
			next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(next);
		} else if (button.equals(about)) {
			Intent next = new Intent(this, com.cwardcode.trackerapp.About.class);
			next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(next);
		} else if (button.equals(help)) {
			Intent next = new Intent(this, com.cwardcode.trackerapp.Help.class);
			next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(next);
		} else if (button.equals(routes)) {
			Intent next = new Intent(this, com.cwardcode.trackerapp.RouteList.class);
			this.startActivity(next);
		} else if (button.equals(stops)) {
			Intent next = new Intent(this, com.cwardcode.trackerapp.StopList.class);
			this.startActivity(next);
		}
		
	}

}
