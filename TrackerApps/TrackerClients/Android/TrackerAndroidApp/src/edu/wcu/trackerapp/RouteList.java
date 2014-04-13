package edu.wcu.trackerapp;

import edu.wcu.trackerapp.AppConstants;
import edu.wcu.trackerapp.RouteSelection;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * This class represents the site list screen. This screen will display a 
 * listview containing several preset sites that the user can choose from.
 * Once the user selects one of these sites, a new page will be launched to
 * display the site.
 * 
 * @author Hayden Thomas
 * @version 11/4/2013
 */
public class RouteList extends Activity implements OnItemClickListener {
	
	/**
	 * The listview that will contain the list.
	 */
	private ListView list;
	
	/**
	 * The arrayadapter used to contain the list elements.
	 */
	private ArrayAdapter<RouteSelection> adapter;

	/**
	 * Called when the activity is first created to initialize the activity.
	 * 
	 * @param savedInstanceState the bundle received from the previous
	 *                           activity, if any.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		AppConstants.createRoutes(this);
		
		list = (ListView) findViewById(R.id.listView1);
		
		list.setItemsCanFocus(false);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		int layout = R.layout.list_element;
		
		adapter = new CustomListAdapter(this, layout, AppConstants.routes);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	}

	/**
	 * Initializes the activity's standard options menu.
	 * 
	 * @param menu the options menu.
	 */
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.route_list, menu);
		return true;
	}
	*/

	/**
	 * Performs the appropriate action when an item is selected.
	 * 
	 * @param parent the adapter view containing the selection.
	 * @param view the view containing the selection.
	 * @param position the selection's position.
	 * @param id the selection's id.
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, 
			                long id) {
		RouteSelection selection = AppConstants.routes.get(position);
		String routeName = selection.getRouteName();
		Log.d("RouteList", routeName + " " + selection.isSelected());
		if (!selection.isSelected()) {
		    AppConstants.selectedRoutes.add(routeName);
		    Log.d("RouteList", routeName + " Added");
		    selection.setSelected(true);
		} else {
			AppConstants.selectedRoutes.remove(
					AppConstants.selectedRoutes.indexOf(routeName));
			Log.d("RouteList", routeName + " Removed");
			selection.setSelected(false);
		}
		
		
		/*
		RouteSelection selection = AppConstants.sites.get(position);
		Intent next = new Intent(this, edu.wcu.trackerapp.Map.class);
		next.putExtra(AppConstants.URL_KEY, selection.getURL());
		this.startActivity(next);
		*/
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent next = new Intent(this, edu.wcu.trackerapp.Map.class);
			this.startActivity(next);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

}
