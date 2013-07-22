package com.norman0406.ingressex;

import com.google.android.gms.maps.GoogleMap;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityMain extends FragmentActivity
{
    private GoogleMap mMap = null;
    
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		initMap();
        
        final Button buttonOps = (Button)findViewById(R.id.buttonOps);
        buttonOps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
    	        Intent myIntent = new Intent(getApplicationContext(), ActivityOps.class);
    	        startActivity(myIntent);
            }
        });

		setContentView(R.layout.activity_main);
	}
	
	private void initMap()
	{
		// init map
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.

            mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

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
        }
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
	}

	@Override
	protected void onResume() 
	{
		// The activity must call the GL surface view's onResume() on activity onResume().
		super.onResume();
	}

	@Override
	protected void onPause() 
	{
		// The activity must call the GL surface view's onPause() on activity onPause().
		super.onPause();
	}
}
