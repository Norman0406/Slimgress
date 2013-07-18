package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;

public class Utils {
	
	public enum Team {
		Resistance,
		Enlightened,
	}
	
	public static Team getTeam(String teamString) {
		if (teamString == "RESISTANCE")
			return Team.Resistance;
		else if (teamString == "ALIENS")
			return Team.Enlightened;
		else
			throw new RuntimeException("invalid team string: " + teamString);
	}
	
	public static Team getTeam(JSONObject json) throws JSONException {
		if (!json.has("team"))
			throw new RuntimeException("invalid json object");
		
		return getTeam(json.getString("team"));		
	}
	
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
}
