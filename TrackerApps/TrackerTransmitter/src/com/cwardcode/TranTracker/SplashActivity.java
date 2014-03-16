package com.cwardcode.TranTracker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class SplashActivity extends Activity {

	final int splashDelay = 4000;
	private Handler handler;
	final Runnable r = new Runnable()
	{
	    public void run() {	    	
	    	gotoNextScreen();
	    	finish();
	    }
	};
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LOW_PROFILE);

		}

		setContentView(R.layout.activity_splash);

	}

	protected void onStart() {
		super.onStart();

		// After a delay start the Runable r via the handler.
		handler = new Handler();
		handler.postDelayed(r, splashDelay);
	}
	
	private void gotoNextScreen()
	{
		//Start screen menu1
		Intent menu = new Intent(this, TranTracker.class);
    	this.startActivity(menu);
    	this.finish();

	}
}
