package com.norman0406.ingressex;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2LatLngRect;
import com.norman0406.ingressex.API.Game;
import com.norman0406.ingressex.API.GameEntity;
import com.norman0406.ingressex.API.GameEntityControlField;
import com.norman0406.ingressex.API.GameEntityLink;
import com.norman0406.ingressex.API.GameEntityPortal;
import com.norman0406.ingressex.API.Utils;
import com.norman0406.ingressex.API.XMParticle;

public class ScannerView extends SupportMapFragment
{
    private IngressApplication mApp = IngressApplication.getInstance();
    private Game mGame = mApp.getGame();
    private GoogleMap mMap = null;

    private Bitmap mXMParticleIcon = null;
    private Bitmap mPortalIconResistance = null;
    private Bitmap mPortalIconEnlightened = null;
    private Bitmap mPortalIconNeutral = null;
    
    private HashMap<String, Marker> mMarkers = null;
    private HashMap<String, Polyline> mLines = null;
    private HashMap<String, Polygon> mPolygons = null;
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        
        // retrieve map
        mMap = getMap();
        if (mMap == null)
            throw new RuntimeException("map is invalid");

        loadAssets();
        
        mMarkers = new HashMap<String, Marker>();
        mLines = new HashMap<String, Polyline>();
        mPolygons = new HashMap<String, Polygon>();
        
        // disable most ui elements
        UiSettings ui = mMap.getUiSettings();
        ui.setAllGesturesEnabled(false);
        ui.setCompassEnabled(false);
        ui.setScrollGesturesEnabled(true);
        ui.setRotateGesturesEnabled(true);
        ui.setZoomControlsEnabled(false);
        ui.setMyLocationButtonEnabled(false);
        
        mMap.setMyLocationEnabled(true);
        
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location myLocation)
            {
                // update camera position
                CameraPosition pos = mMap.getCameraPosition();
                CameraPosition newPos = new CameraPosition.Builder(pos)
                .target(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
                .zoom(16)
                .tilt(40)
                .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newPos));
                
                // update game position
                mGame.updateLocation(new Utils.LocationE6(myLocation.getLatitude(), myLocation.getLongitude()));

                //updateWorld();
            }
        });
        
        // deactivate standard map
        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        
        // add custom map tiles
        addIngressTiles();
    }
    
    private void loadAssets()
    {
        int portalSize = 80;
        mPortalIconResistance = Bitmap.createScaledBitmap(getBitmapFromAsset("portalTexture_RESISTANCE.png"), portalSize, portalSize, true);
        mPortalIconEnlightened = Bitmap.createScaledBitmap(getBitmapFromAsset("portalTexture_ALIENS.png"), portalSize, portalSize, true);
        mPortalIconNeutral = Bitmap.createScaledBitmap(getBitmapFromAsset("portalTexture_NEUTRAL.png"), portalSize, portalSize, true);
        mXMParticleIcon = Bitmap.createScaledBitmap(getBitmapFromAsset("particle.png"), 10, 10, true);
    }

    private Bitmap getBitmapFromAsset(String name)
    {
        AssetManager assetManager = getActivity().getAssets();
        
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
    
    private void addIngressTiles()
    {
        // create a custom tile provider with an ingress-like map
        TileProvider tiles = new UrlTileProvider(256, 256) {
            @Override
            public synchronized URL getTileUrl(int x, int y, int zoom) {
                final String apistyle = "s.e%3Al%7Cp.v%3Aoff%2Cs.e%3Ag%7Cp.c%3A%23ff000000%2Cs.t%3A3%7Cs.e%3Ag%7Cp.c%3A%23ff5e9391";
                final String style = "59,37%7Csmartmaps";
                
                final String format = "http://mt1.googleapis.com/vt?lyrs=m&src=apiv3&hl=de-DE&x=%d&s=&y=%d&z=%d&s=Galileo";                 
                String mapUrl = String.format(Locale.US, format, x, y, zoom);
                
                mapUrl += "&apistyle=" + apistyle + "&style=" + style;
                
                URL url = null;
                try {
                    url = new URL(mapUrl);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
                return url;
            }               
        };
        
        TileOverlayOptions tileOverlay = new TileOverlayOptions();
        tileOverlay.tileProvider(tiles);
        
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tiles));
    }
    
    private void updateWorld()
    {
        // get visible region
        LatLng northeast = mMap.getProjection().getVisibleRegion().latLngBounds.northeast;
        LatLng southwest = mMap.getProjection().getVisibleRegion().latLngBounds.southwest;      
        final S2LatLngRect region = S2LatLngRect.fromPointPair(S2LatLng.fromDegrees(southwest.latitude, southwest.longitude),
                S2LatLng.fromDegrees(northeast.latitude, northeast.longitude));

        mGame.intGetObjectsInCells(region, new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                
                // clear map first
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //mMap.clear();
                    }
                });
                
                // draw xm particles
                drawXMParticles();
                
                // draw game entities
                Map<String, GameEntity> entities = mGame.getWorld().getGameEntities();
                Set<String> keys = entities.keySet();
                for (String key : keys) {
                    GameEntity entity = entities.get(key);
                    
                    if (entity.getGameEntityType() == GameEntity.GameEntityType.Portal)
                        drawPortal((GameEntityPortal)entity);
                    else if (entity.getGameEntityType() == GameEntity.GameEntityType.Link)
                        drawLink((GameEntityLink)entity);
                    else if (entity.getGameEntityType() == GameEntity.GameEntityType.ControlField)
                        drawField((GameEntityControlField)entity);
                }

                Log.d("ScannerView", "world updated");
                
                return true;
            }
        }));
    }
    
    private void drawXMParticles()
    {
        // draw xm particles
        /*World world = mGame.getWorld();
        Map<String, XMParticle> xmParticles = world.getXMParticles();
        Set<String> keys = xmParticles.keySet();
        for (String key : keys) {
            XMParticle particle = xmParticles.get(key);

            final Utils.LocationE6 location = particle.getCellLocation();
            
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(mXMParticleIcon);        
                    mMap.addMarker(new MarkerOptions()
                    .position(location.getLatLng())
                    .icon(icon));
                }
            });
        }*/
    }
    
    private void drawPortal(final GameEntityPortal portal)
    {
        final Utils.Team team = portal.getPortalTeam();
        if (mMap != null) {
            // only update if marker has not yet been added
            if (!mMarkers.containsKey(portal.getEntityGuid())) {
                final Utils.LocationE6 location = portal.getPortalLocation();
                
                getActivity().runOnUiThread(new Runnable() {
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
                        
                        Marker m = mMap.addMarker(marker);
                        mMarkers.put(portal.getEntityGuid(), m);
                    }
                });
            }            
        }
    }
    
    private void drawLink(final GameEntityLink link)
    {
        if (mMap != null) {
            // only update if line has not yet been added
            if (!mLines.containsKey(link.getEntityGuid())) {
                final Utils.LocationE6 origin = link.getLinkOriginLocation();
                final Utils.LocationE6 dest = link.getLinkDestinationLocation();
                
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int color = 0xff0000ff; // blue without alpha
                        Utils.Team team = link.getLinkControllingTeam();
                        if (team == Utils.Team.Enlightened)
                            color = 0xff00ff00; // green without alpha
                        
                        PolylineOptions line = new PolylineOptions();
                        line.add(origin.getLatLng());
                        line.add(dest.getLatLng());
                        line.color(color);
                        line.width(2);
                        line.zIndex(2);
                        
                        Polyline l = mMap.addPolyline(line);
                        mLines.put(link.getEntityGuid(), l);
                    }
                });
            }
        }
    }
    
    private void drawField(final GameEntityControlField field)
    {
        if (mMap != null) {
            // only update if line has not yet been added
            if (!mPolygons.containsKey(field.getEntityGuid())) {
                final Utils.LocationE6 vA = field.getFieldVertexA().getPortalLocation();
                final Utils.LocationE6 vB = field.getFieldVertexB().getPortalLocation();
                final Utils.LocationE6 vC = field.getFieldVertexC().getPortalLocation();
                
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        
                        int color = 0x320000ff; // blue with alpha
                        Utils.Team team = field.getFieldControllingTeam();
                        if (team == Utils.Team.Enlightened)
                            color = 0x3200ff00; // green with alpha
                        
                        PolygonOptions polygon = new PolygonOptions();
                        polygon.add(vA.getLatLng());
                        polygon.add(vB.getLatLng());
                        polygon.add(vC.getLatLng());
                        polygon.fillColor(color);
                        polygon.strokeWidth(0);
                        polygon.zIndex(1);
                        
                        Polygon p = mMap.addPolygon(polygon);
                        mPolygons.put(field.getEntityGuid(), p);
                    }
                });
            }
        }
    }
}