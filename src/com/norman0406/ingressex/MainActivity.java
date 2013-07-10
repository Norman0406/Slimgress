package com.norman0406.ingressex;

import com.norman0406.ingressex.API.Interface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity 
{
	/** Hold a reference to our GLSurfaceView */
	//private GLSurfaceView mGLSurfaceView;
	private boolean isLoggedIn;
	private String user;
	private String authToken;
	protected Interface ingressInterface;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		isLoggedIn = false;
		
		if (!isLoggedIn) {
	        Intent myIntent = new Intent(getApplicationContext(), AuthActivity.class);
	        startActivityForResult(myIntent, 0);
		}

		/*mGLSurfaceView = new GLSurfaceView(this);

		// Check if the system supports OpenGL ES 2.0.
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

		if (supportsEs2) 
		{
			// Request an OpenGL ES 2.0 compatible context.
			mGLSurfaceView.setEGLContextClientVersion(2);

			// Set the renderer to our demo renderer, defined below.
			mGLSurfaceView.setRenderer(new MainRenderer());
		} 
		else 
		{
			// This is where you could create an OpenGL ES 1.x compatible
			// renderer if you wanted to support both ES 1 and ES 2.
			return;
		}
		setContentView(mGLSurfaceView);*/
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
	        	isLoggedIn = true;
	        	user = data.getStringExtra("User");
	        	authToken = data.getStringExtra("AuthToken");

	            // pass the authentication token to the interface
	            ingressInterface = new Interface(authToken);
			}
			else {
	        	user = data.getStringExtra("User");
	        	isLoggedIn = false;
			}
		}
	}

	@Override
	protected void onResume() 
	{
		// The activity must call the GL surface view's onResume() on activity onResume().
		super.onResume();
				
		/*if (isLoggedIn) {
			mGLSurfaceView.onResume();
		}*/
	}

	@Override
	protected void onPause() 
	{
		// The activity must call the GL surface view's onPause() on activity onPause().
		super.onPause();

		/*if (isLoggedIn) {
			mGLSurfaceView.onPause();
		}*/
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}
