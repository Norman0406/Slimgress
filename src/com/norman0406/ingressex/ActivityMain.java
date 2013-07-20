package com.norman0406.ingressex;

import org.json.JSONException;

import com.google.android.gms.maps.GoogleMap;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.norman0406.ingressex.API.IngressInterface;
import com.norman0406.ingressex.API.Interface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityMain extends FragmentActivity // implements ActionBar.TabListener 
{
	private IngressApplication app = IngressApplication.getInstance();
	private Interface ingressInterface = app.getInterface();
    private GoogleMap mMap = null;
    
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (!app.isLoggedIn()) {
	        Intent myIntent = new Intent(getApplicationContext(), ActivityAuth.class);
	        startActivityForResult(myIntent, 0);
		}

        // Do a null check to confirm that we have not already instantiated the map.
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
        
        final Button buttonOps = (Button)findViewById(R.id.buttonOps);
        buttonOps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
    	        Intent myIntent = new Intent(getApplicationContext(), ActivityOps.class);
    	        startActivity(myIntent);
            }
        });
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
	}

	private int numLogins = 0;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 0) { // means ActivityAuth has been called to get a token
			if (resultCode == RESULT_OK) {
	        	String authToken = data.getStringExtra("AuthToken");
	        	
	        	app.getInterface().authenticate(authToken, new AuthenticateCallback(++numLogins));	        	
			}
			else {
				app.setLoggedIn(false);
			}
		}
	}
	
	private void setData() {
		/*R.id.agentname;
		R.id.agentxm;*/
				
		//app.getInterface().getHandshakeData().getAgent().getLevel();
	}

	public class AuthenticateCallback {
		int numLogins = 0;
		
		AuthenticateCallback(int numLogins) {
			this.numLogins = numLogins;
		}
		
		public void authenticationFinished(String token, int returnCode) {
			switch (returnCode) {
			case 0:	// successful
				app.setLoggedIn(true);

	        	IngressInterface theInt = new IngressInterface();
	        	try {
					theInt.intGetInventory();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	        	System.out.println("Hallo");
	        	
				break;
			case 1:	// token expired, try to renew
				
				if (this.numLogins > 1) {	// we already tried too often, is simply not working
					//app.getInterface() = null;
				}
				else {
			        Intent myIntent = new Intent(getApplicationContext(), ActivityAuth.class);
			        myIntent.putExtra("RefreshToken", token);
			        startActivityForResult(myIntent, 0);					
				}
				
				break;
			case 2:	// something else
				break;
			}
		}
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
