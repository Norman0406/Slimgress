package com.norman0406.ingressex;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import org.json.JSONException;

import com.google.android.gms.maps.GoogleMap;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.norman0406.ingressex.API.Agent;
import com.norman0406.ingressex.API.GameBasket;
import com.norman0406.ingressex.API.Utils;
import com.norman0406.ingressex.API.Utils.LocationE6;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityMain extends FragmentActivity
{
	private IngressApplication mApp = IngressApplication.getInstance();
    private GoogleMap mMap = null;
    
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// update agent data
		updateAgent();

		// update world every 5 seconds
		/*new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				updateWorld();
			}
		}, 0, 5 * 1000);*/
		updateWorld();
        
		// initialize map view
		initMap();
        
		// create ops button callback
        final Button buttonOps = (Button)findViewById(R.id.buttonOps);
        buttonOps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
    	        Intent myIntent = new Intent(getApplicationContext(), ActivityOps.class);
    	        startActivity(myIntent);
            }
        });
	}
	
	private void updateAgent()
	{
		// get agent data
		Agent agent = mApp.getInterface().getAgent();
		
		if (agent != null) {
			int textColor = Color.BLUE;
			Utils.Team team = agent.getTeam();
			if (team == Utils.Team.Resistance)
				textColor = Color.BLUE;
			else
				textColor = Color.GREEN;
			
			((TextView)findViewById(R.id.agentname)).setText(agent.getNickname());
			((TextView)findViewById(R.id.agentname)).setTextColor(textColor);
			
			String agentlevel = "L" + Integer.toString(agent.getLevel());
			((TextView)findViewById(R.id.agentlevel)).setText(agentlevel);
			((TextView)findViewById(R.id.agentlevel)).setTextColor(textColor);
			
			((ProgressBar)findViewById(R.id.agentxm)).setMax(agent.getEnergyMax());
			((ProgressBar)findViewById(R.id.agentxm)).setProgress(agent.getEnergy());
			
			String agentinfo = "AP: " + agent.getAp() + " / XM: " + (agent.getEnergy() * 100 / agent.getEnergyMax()) + " %";
			((TextView)findViewById(R.id.agentinfo)).setText(agentinfo);
			((TextView)findViewById(R.id.agentinfo)).setTextColor(textColor);
		}
		
		// get inventory
		mApp.getInterface().intGetInventory(new Runnable() {
			@Override
			public void run() {
				// inventory is loaded
				System.out.println("Hallo");
			}
		});
	}
	
	private void updateWorld()
	{
		/*Utils.LocationE6 playerLocation = new Utils.LocationE6(50.345963, 7.588223);
		double area = 1000 * 1000;
		
		mApp.getInterface().intGetObjectsInCells(playerLocation, area, new Runnable() {
			@Override
			public void run() {
				// finished loading world
				System.out.println("Hallo");
			}
		});*/
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
}
