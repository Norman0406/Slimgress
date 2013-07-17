package com.norman0406.ingressex;

import com.google.android.gms.maps.GoogleMap;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.norman0406.ingressex.API.Interface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityMain extends FragmentActivity // implements ActionBar.TabListener 
{
	/** Hold a reference to our GLSurfaceView */
	//private GLSurfaceView mGLSurfaceView;
	private boolean isLoggedIn;
	private String user;
	private String authToken;
	protected Interface ingressInterface;

    private GoogleMap mMap;
    
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.

            mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
           
            LatLng myPos = new LatLng(50.345963, 7.588223);
            
            CameraPosition pos = mMap.getCameraPosition();
            CameraPosition newPos = new CameraPosition.Builder(pos)
            .target(myPos)
            .zoom(16)
            .tilt(50)
            .build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(newPos));
                        
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                    
            UiSettings ui = mMap.getUiSettings();
            ui.setCompassEnabled(false);
            ui.setZoomControlsEnabled(false);
            ui.setZoomGesturesEnabled(true);
            ui.setRotateGesturesEnabled(true);
            ui.setScrollGesturesEnabled(false);
            ui.setTiltGesturesEnabled(false);           
            
            // Check if we were successful in obtaining the map.
            /*if (mMap != null) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
            }*/
        }
        
        final Button buttonOps = (Button)findViewById(R.id.buttonOps);
        buttonOps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
    	        Intent myIntent = new Intent(getApplicationContext(), ActivityOps.class);
    	        startActivity(myIntent);
    	        //startActivityForResult(myIntent, 0);
            }
        });
		
		/*super.onCreate(savedInstanceState);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				isLoggedIn = true;
	        	user = data.getStringExtra("User");
	        	authToken = data.getStringExtra("AuthToken");
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
		
		if (isLoggedIn && ingressInterface == null) {
            // pass the authentication token to the interface

			// TODO: let the interface run asynchronously
			//ingressInterface = new Interface(authToken);
		}
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
