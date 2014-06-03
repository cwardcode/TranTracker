package edu.wcu.trackerapp;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class WindowAdapter implements InfoWindowAdapter {
	LayoutInflater inflater = null;

	public WindowAdapter(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	@Override
	public View getInfoContents(Marker marker) {
		View window = inflater.inflate(R.layout.window, null);
		TextView text = (TextView) window.findViewById(R.id.title);
		text.setText(marker.getTitle());
		text = (TextView) window.findViewById(R.id.snippet);
		text.setText(marker.getSnippet());
		return window;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

}
