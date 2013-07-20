package com.norman0406.ingressex.API;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.geometry.S2Cap;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2RegionCoverer;

public class Utils {
	
	// Team enumeration
	public enum Team {
		Resistance,
		Enlightened,
	}
	
	// get team enum from json string
	public static Team getTeam(String teamString) {
		if (teamString.equals("RESISTANCE"))
			return Team.Resistance;
		else if (teamString.equals("ALIENS"))
			return Team.Enlightened;
		else
			throw new RuntimeException("invalid team string: " + teamString);
	}
	
	// get team enum from json object
	public static Team getTeam(JSONObject json) throws JSONException {
		if (!json.has("team"))
			throw new RuntimeException("invalid json object");
		
		return getTeam(json.getString("team"));		
	}
	
	// location E6 helper class
	public static class LocationE6 {
		private long latitude;
		private long longitude;
		
		public LocationE6(JSONObject json) throws JSONException {
			latitude = json.getInt("latE6");
			longitude = json.getInt("lngE6");
		}
		
		public LocationE6(long cellId) {
			S2CellId cell = new S2CellId(cellId);
			
			S2LatLng pos = cell.toLatLng();
			latitude = pos.lat().e6();
			longitude = pos.lng().e6();
		}
		
		public LocationE6(double latDeg, double lngDeg) {
			S2LatLng pos = S2LatLng.fromDegrees(latDeg, lngDeg);
			
			latitude = pos.lat().e6();
			longitude = pos.lng().e6();
		}
		
		public LocationE6(long latE6, long lngE6) {
			latitude = latE6;
			longitude = lngE6;
		}

		public long getLatitude() {
			return latitude;
		}

		public long getLongitude() {
			return longitude;
		}
	}
	
	// retrieve cell ids from location and covering area in m2
	public static String[] getCellIdsFromLocationArea(Utils.LocationE6 location, int minLevel, int maxLevel, double area_m2) {
		S2LatLng pointLatLng = S2LatLng.fromE6(location.getLatitude(), location.getLongitude());
		
		//double area_m2 = 2000 * 2000;	// 1 km2
		double radius_m2 = 6371 * 1000; 
		double sr = area_m2 / (radius_m2 * radius_m2);
				
		S2Cap h1 = S2Cap.fromAxisArea(pointLatLng.toPoint(), sr);
		S2RegionCoverer rCov = new S2RegionCoverer();

		rCov.setMinLevel(minLevel);
		rCov.setMaxLevel(maxLevel);
		
		// get cells
		ArrayList<S2CellId> cells = rCov.getCovering(h1).cellIds();
		ArrayList<Long> cellIds = new ArrayList<Long>();
		for (int i = 0; i < cells.size(); i++) {
			
			S2CellId cellId = cells.get(i);
			
			// can happen for some reason
			if (cellId.level() < minLevel || cellId.level() > maxLevel)
				continue;
			
			cellIds.add(cellId.id());
		}
		
		// convert to hex values
		String cellIdsHex[] = new String[cellIds.size()];
		for (int i = 0; i < cellIdsHex.length; i++) {
			cellIdsHex[i] = Long.toHexString(cellIds.get(i));
		}

		return cellIdsHex;
	}
}
