package com.cwardcode.trackerapp;

import java.util.ArrayList;

import com.cwardcode.trackerapp.AppConstants;
import com.cwardcode.trackerapp.R;
import com.cwardcode.trackerapp.RouteSelection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class is a custom list adapter for the app's listview.
 * 
 * @author Hayden Thomas
 * @version 11/4/2013
 */
public class CustomListAdapter extends ArrayAdapter<RouteSelection> {

	/**
	 * An array list containing all of the sites in the listview.
	 */
	private ArrayList<RouteSelection> routes;
	
	/**
	 * The calling context.
	 */
	private Context context;
	
	/**
	 * The id of the textview resource.
	 */
	int viewSource;
	
	
	/**
	 * Creates a new CustomListAdapter object using the given parameters.
	 * 
	 * @param context the calling context.
	 * @param textViewResourceID the id of the textview resource.
	 * @param sites the list of site selections.
	 */
	public CustomListAdapter(Context context, int textViewResourceID,
			                 ArrayList<RouteSelection> routes) {
		
		super(context, textViewResourceID, routes);
		this.context = context;
		viewSource = textViewResourceID;
		this.routes = routes;
	}
	
	/**
	 * Gets a view to display the data at the given position.
	 * 
	 * @param position the position where the view will be displayed.
	 * @param listItemView the view that will be displayed.
	 * @param parent the ViewGroup that contains all of the views.
	 * 
	 * @return the retrieved view.
	 */
	@Override
	public View getView(int position, View listItemView, ViewGroup parent) {
		View rowView = listItemView;
		
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) 
					  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			rowView = inflater.inflate(viewSource,  parent, false);
			
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.routeName);
			viewHolder.image = (ImageView) rowView.findViewById(R.id.icon);
			rowView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
		
		RouteSelection selection = AppConstants.routes.get(position);
		
		holder.image.setImageDrawable(routes.get(position).getIcon());
		holder.image.setBackgroundDrawable(null);
		
		holder.text.setText(selection.getRouteName());
		
		return (rowView);
		
	}
	
	/**
	 * An object that holds the views used for the listview.
	 * 
	 * @author Hayden Thomas
	 * @version 11/5/2013
	 *
	 */
	static class ViewHolder {
		/**
		 * The text contained within the view.
		 */
		public TextView text;
		
		/**
		 * The image contained within the view.
		 */
		public ImageView image;
	}
}
