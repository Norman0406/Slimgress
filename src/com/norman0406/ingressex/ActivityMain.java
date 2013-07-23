package com.norman0406.ingressex;

import java.util.Map;
import java.util.Set;

import com.google.android.gms.maps.GoogleMap;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2LatLngRect;
import com.norman0406.ingressex.API.Agent;
import com.norman0406.ingressex.API.GameEntity;
import com.norman0406.ingressex.API.GameEntityControlField;
import com.norman0406.ingressex.API.GameEntityLink;
import com.norman0406.ingressex.API.GameEntityPortal;
import com.norman0406.ingressex.API.Utils;
import com.norman0406.ingressex.API.World;

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
        
		// initialize map view
		initMap();

		// update world every 5 seconds
		/*new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				updateWorld();
			}
		}, 0, 5 * 1000);*/
		//updateWorld();
        
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
		Utils.LocationE6 playerLocation = new Utils.LocationE6(50.345963, 7.588223);
		mApp.getInterface().intGetInventory(playerLocation, new Runnable() {
			@Override
			public void run() {
				// inventory is loaded
				System.out.println("inventory loaded");
			}
		});
	}
	
	private void updateWorld()
	{
		Utils.LocationE6 playerLocation = new Utils.LocationE6(50.345963, 7.588223);

		// get visible region
		LatLng northeast = mMap.getProjection().getVisibleRegion().latLngBounds.northeast;
		LatLng southwest = mMap.getProjection().getVisibleRegion().latLngBounds.southwest;		
		S2LatLngRect region = S2LatLngRect.fromPointPair(S2LatLng.fromDegrees(southwest.latitude, southwest.longitude),
				S2LatLng.fromDegrees(northeast.latitude, northeast.longitude));
				
		mApp.getInterface().intGetObjectsInCells(playerLocation, region, new Runnable() {
			@Override
			public void run() {
				
				// clear map first
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mMap.clear();
					}
				});
				
				// finished loading world
				System.out.println("world updated");
				World world = mApp.getInterface().getWorld();
				Map<String, GameEntity> entities = world.getGameEntities();
				
				/*List<XMParticle> xmParticles = world.getXMParticles();
				for (XMParticle particle : xmParticles) {
					final Utils.LocationE6 location = particle.getCellLocation();
					
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mMap.addMarker(new MarkerOptions()
							.position(location.getLatLng())
							.title("xm"));
						}
					});
				}*/
				
				Set<String> keys = entities.keySet();
				for (String key : keys) {
					GameEntity entity = entities.get(key);
					
					if (entity.getGameEntityType() == GameEntity.GameEntityType.Portal) {
						final GameEntityPortal portal = (GameEntityPortal)entity;
						if (mMap != null) {
							final Utils.LocationE6 location = portal.getPortalLocation();
							
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									mMap.addMarker(new MarkerOptions()
									.position(location.getLatLng())
									.title(portal.getPortalTitle()));
								}
							});
						}
					}
					else if (entity.getGameEntityType() == GameEntity.GameEntityType.Link) {
						final GameEntityLink link = (GameEntityLink)entity;
						if (mMap != null) {
							final Utils.LocationE6 origin = link.getLinkOriginLocation();
							final Utils.LocationE6 dest = link.getLinkDestinationLocation();
														
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									int color = 0xff0000ff;	// blue with alpha
									Utils.Team team = link.getLinkControllingTeam();
									if (team == Utils.Team.Enlightened)
										color = 0xff00ff00;	// green with alpha
									
									PolylineOptions line = new PolylineOptions();
									line.add(origin.getLatLng());
									line.add(dest.getLatLng());
									line.color(color);
									line.width(2);
									line.zIndex(2);
									mMap.addPolyline(line);
								}
							});
						}
					}
					else if (entity.getGameEntityType() == GameEntity.GameEntityType.ControlField) {
						final GameEntityControlField field = (GameEntityControlField)entity;
						if (mMap != null) {
							final Utils.LocationE6 vA = field.getFieldVertexA().getPortalLocation();
							final Utils.LocationE6 vB = field.getFieldVertexB().getPortalLocation();
							final Utils.LocationE6 vC = field.getFieldVertexC().getPortalLocation();
							
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									int color = 0x320000ff;	// blue with alpha
									Utils.Team team = field.getFieldControllingTeam();
									if (team == Utils.Team.Enlightened)
										color = 0x3200ff00;	// green with alpha
									
									PolygonOptions polygon = new PolygonOptions();
									polygon.add(vA.getLatLng());
									polygon.add(vB.getLatLng());
									polygon.add(vC.getLatLng());
									polygon.fillColor(color);
									polygon.strokeWidth(0);
									polygon.zIndex(1);
									mMap.addPolygon(polygon);
								}
							});
						}
					}
				}
			}
		});
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
            .zoom(15)
            .tilt(40)
            .build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(newPos));
            
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            
            UiSettings ui = mMap.getUiSettings();
            ui.setCompassEnabled(false);
            ui.setZoomControlsEnabled(false);
            //ui.setZoomGesturesEnabled(true);
            //ui.setRotateGesturesEnabled(true);
            //ui.setScrollGesturesEnabled(false);
            //ui.setTiltGesturesEnabled(false);
            
            mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
				@Override
				public void onCameraChange(CameraPosition arg0) {
					updateWorld();
				}
            });
        }
	}
}
