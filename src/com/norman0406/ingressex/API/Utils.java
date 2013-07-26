package com.norman0406.ingressex.API;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.geometry.S2Cap;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2LatLngRect;
import com.google.common.geometry.S2Region;
import com.google.common.geometry.S2RegionCoverer;

public class Utils
{
	// Team enumeration
	public enum Team
	{
		Resistance,
		Enlightened,
		Neutral
	}
	
	// get team enum from json string
	public static Team getTeam(String teamString)
	{
		if (teamString.equals("RESISTANCE"))
			return Team.Resistance;
		else if (teamString.equals("ALIENS"))
			return Team.Enlightened;
		else if (teamString.equals("NEUTRAL"))
			return Team.Neutral;
		else
			throw new RuntimeException("invalid team string: " + teamString);
	}
	
	public static String getTeam(Team team)
	{
		if (team == Team.Resistance)
			return "RESISTANCE";
		else if (team == Team.Enlightened)
			return "ALIENS";

		return "NEUTRAL";
	}
	
	// get team enum from json object
	public static Team getTeam(JSONObject json) throws JSONException
	{
		if (!json.has("team"))
			throw new RuntimeException("invalid json object");
		
		return getTeam(json.getString("team"));		
	}
	
	// location E6 helper class
	public static class LocationE6
	{
		private long latitude;
		private long longitude;
		
		public LocationE6(JSONObject json) throws JSONException
		{
			latitude = json.getInt("latE6");
			longitude = json.getInt("lngE6");
		}
		
		public LocationE6(long cellId)
		{
			S2CellId cell = new S2CellId(cellId);
			
			S2LatLng pos = cell.toLatLng();
			latitude = pos.lat().e6();
			longitude = pos.lng().e6();
		}
		
		public LocationE6(double latDeg, double lngDeg)
		{
			S2LatLng pos = S2LatLng.fromDegrees(latDeg, lngDeg);
			
			latitude = pos.lat().e6();
			longitude = pos.lng().e6();
		}
		
		public LocationE6(long latE6, long lngE6)
		{
			latitude = latE6;
			longitude = lngE6;
		}

		public long getLatitude()
		{
			return latitude;
		}

		public long getLongitude()
		{
			return longitude;
		}
		
		public LatLng getLatLng()
		{
			S2LatLng pos = S2LatLng.fromE6(latitude, longitude);
			return new LatLng(pos.latDegrees(), pos.lngDegrees());
		}
	}
	
	public static String[] getCellIdsFromLocationArea(Utils.LocationE6 location, double areaM2, int minLevel, int maxLevel)
	{
		final double radius_m2 = 6371 * 1000;
		final double sr = areaM2 / (radius_m2 * radius_m2);

		S2LatLng pointLatLng = S2LatLng.fromE6(location.getLatitude(), location.getLongitude());
		S2Cap cap = S2Cap.fromAxisArea(pointLatLng.toPoint(), sr);
		
		return getCellIdsFromRegion(cap, minLevel, maxLevel);
	}
	
	public static String[] getCellIdsFromMinMax(Utils.LocationE6 min, Utils.LocationE6 max, int minLevel, int maxLevel)
	{
		S2LatLngRect region = S2LatLngRect.fromPointPair(S2LatLng.fromE6(min.getLatitude(), min.getLongitude()),
				S2LatLng.fromE6(max.getLatitude(), max.getLongitude()));
		return getCellIdsFromRegion(region, minLevel, maxLevel);
	}
	
	// retrieve cell ids from location and covering area in m2
	public static String[] getCellIdsFromRegion(S2Region region, int minLevel, int maxLevel)
	{
		//S2LatLng pointLatLng = S2LatLng.fromE6(location.getLatitude(), location.getLongitude());
		
		//double area_m2 = 2000 * 2000;	// 1 km2
		//double radius_m2 = 6371 * 1000; 
		//double sr = area_m2 / (radius_m2 * radius_m2);

		//S2Cap h1 = S2Cap.fromAxisArea(S2Point.normalize(pointLatLng.toPoint()), sr);
		
		/*S2LatLng min = S2LatLng.fromDegrees(50.34056, 7.5505);
		S2LatLng max = S2LatLng.fromDegrees(50.3655, 7.615607);
		
		S2LatLngRect rect = new S2LatLngRect(min, max);*/
		
		S2RegionCoverer rCov = new S2RegionCoverer();

		rCov.setMinLevel(minLevel);
		rCov.setMaxLevel(maxLevel);
		
		// get cells
		ArrayList<S2CellId> cells = new ArrayList<S2CellId>();
		rCov.getCovering(region, cells);
		
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
