package com.norman0406.ingressex;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.MapTileRequestState;
import org.osmdroid.tileprovider.modules.MapTileDownloader;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;

import com.google.android.gms.maps.GoogleMap;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.common.geometry.S2Cap;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2LatLngRect;
import com.norman0406.ingressex.API.Agent;
import com.norman0406.ingressex.API.Game;
import com.norman0406.ingressex.API.GameEntity;
import com.norman0406.ingressex.API.GameEntityControlField;
import com.norman0406.ingressex.API.GameEntityLink;
import com.norman0406.ingressex.API.GameEntityPortal;
import com.norman0406.ingressex.API.Utils;
import com.norman0406.ingressex.API.World;
import com.norman0406.ingressex.API.XMParticle;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityMain extends FragmentActivity
{
	private IngressApplication mApp = IngressApplication.getInstance();
	private Game mGame = mApp.getGame();
    private GoogleMap mMap = null;

    Bitmap mXMParticleIcon = null;
    Bitmap mPortalIconResistance = null;
    Bitmap mPortalIconEnlightened = null;
    Bitmap mPortalIconNeutral = null;
    GroundOverlayOptions mGameOverlay = null;
    //private GLSurfaceView mGLSurfaceView;
    
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		/*mGLSurfaceView = new GLSurfaceView(this);
		mGLSurfaceView.setEGLContextClientVersion(2);
		mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		mGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		mGLSurfaceView.setZOrderOnTop(true);
		mGLSurfaceView.setRenderer(new MainRenderer());
		addContentView(mGLSurfaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));*/
		
		/*final MapTile tile = new MapTile(1, 256, 256);
		MapTileRequestState state = null;
		
		MapTileDownloader downloader = null;
		
		MapTileProviderBasic provider = new MapTileProviderBasic(this);
		provider.setTileSource(TileSourceFactory.MAPQUESTOSM);
		
		TilesOverlay overlay = new TilesOverlay(provider, this);
		
		MapView osmv = new MapView(this, 256);*/
		
		
		// load assets into memory
		loadAssets();

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
	
	@Override
	protected void onResume()
	{
		// The activity must call the GL surface view's onResume() on activity onResume().
		super.onResume();
		//mGLSurfaceView.onResume();
	}

	@Override
	protected void onPause()
	{
		// The activity must call the GL surface view's onPause() on activity onPause().
		super.onPause();
		//mGLSurfaceView.onPause();
	}
	
	private void loadAssets()
	{
		int portalSize = 80;
		mPortalIconResistance = Bitmap.createScaledBitmap(getBitmapFromAsset("portalTexture_RESISTANCE.png"), portalSize, portalSize, true);
		mPortalIconEnlightened = Bitmap.createScaledBitmap(getBitmapFromAsset("portalTexture_ALIENS.png"), portalSize, portalSize, true);
		mPortalIconNeutral = Bitmap.createScaledBitmap(getBitmapFromAsset("portalTexture_NEUTRAL.png"), portalSize, portalSize, true);
		mXMParticleIcon = Bitmap.createScaledBitmap(getBitmapFromAsset("particle.png"), 10, 10, true);

		mGameOverlay = new GroundOverlayOptions();
	}
	
	private void updateAgent()
	{		
		// get agent data
		Agent agent = mGame.getAgent();
		
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
		//Utils.LocationE6 playerLocation = new Utils.LocationE6(0, 0);
		/*mApp.getInterface().intGetInventory(playerLocation, new Runnable() {
			@Override
			public void run() {
				// inventory is loaded
				System.out.println("inventory loaded");
			}
		});
		
		mApp.getInterface().intGetGameScore(new Handler.Callback() {
	        public boolean handleMessage(Message msg) {
				int resistanceScore = msg.getData().getInt("ResistanceScore");
				int enlightenedScore = msg.getData().getInt("EnlightenedScore");
				System.out.println("resistance score loaded");
				return true;
	        }
		});*/
		
		/*mGame.intRedeemReward("5vzd5augustar276u", new Handler.Callback() {
	        public boolean handleMessage(Message msg) {
				System.out.println("resistance score loaded");
				return true;
	        }
		});*/
	}
	
	private Bitmap getBitmapFromAsset(String name)
	{
		AssetManager assetManager = getAssets();
		
		InputStream istr;
		Bitmap bitmap = null;
		try {
			istr = assetManager.open(name);
			bitmap = BitmapFactory.decodeStream(istr);
		} catch (IOException e) {
			return null;
		}
		
		return bitmap;
	}
	
	private void updateWorld()
	{
		Utils.LocationE6 playerLocation = new Utils.LocationE6(50.345963, 7.588223);

		// get visible region
		LatLng northeast = mMap.getProjection().getVisibleRegion().latLngBounds.northeast;
		LatLng southwest = mMap.getProjection().getVisibleRegion().latLngBounds.southwest;		
		S2LatLngRect region = S2LatLngRect.fromPointPair(S2LatLng.fromDegrees(southwest.latitude, southwest.longitude),
				S2LatLng.fromDegrees(northeast.latitude, northeast.longitude));
				
		mGame.intGetObjectsInCells(playerLocation, region, new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				
				// clear map first
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mMap.clear();
					}
				});
				
				// finished loading world
				System.out.println("world updated");
				World world = mGame.getWorld();
				Map<String, GameEntity> entities = world.getGameEntities();

				Set<String> keys = null;
				
				// draw xm particles
				/*Map<String, XMParticle> xmParticles = world.getXMParticles();
				Set<String> keys = xmParticles.keySet();
				for (String key : keys) {
					XMParticle particle = xmParticles.get(key);

					final Utils.LocationE6 location = particle.getCellLocation();
					
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(mXMParticleIcon);		
							mMap.addMarker(new MarkerOptions()
							.position(location.getLatLng())
							.icon(icon));
						}
					});
				}*/
								
				// draw game entities
				keys = entities.keySet();
				for (String key : keys) {
					GameEntity entity = entities.get(key);
					
					if (entity.getGameEntityType() == GameEntity.GameEntityType.Portal) {
						final GameEntityPortal portal = (GameEntityPortal)entity;
						final Utils.Team team = portal.getPortalTeam();
						if (mMap != null) {
							final Utils.LocationE6 location = portal.getPortalLocation();
							
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									
									Bitmap portalIcon = null;
									if (team == Utils.Team.Resistance)
										portalIcon = mPortalIconResistance;
									else if (team == Utils.Team.Enlightened)
										portalIcon = mPortalIconEnlightened;
									else
										portalIcon = mPortalIconNeutral; 
																		
									BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(portalIcon);									
									
									MarkerOptions marker = new MarkerOptions();
									marker.position(location.getLatLng())
									.title(portal.getPortalTitle())
									.icon(icon)
									.anchor(0.5f, 0.5f);
									
									mMap.addMarker(marker);
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
				return true;
			}
		});
	}
	
	private void initMap()
	{
		// init map
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.

        	SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        	mMap = mapFragment.getMap();

            LatLng myPos = new LatLng(50.345963, 7.588223);
            
            CameraPosition pos = mMap.getCameraPosition();
            CameraPosition newPos = new CameraPosition.Builder(pos)
            .target(myPos)
            .zoom(16)
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
