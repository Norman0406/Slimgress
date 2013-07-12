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

		public long getLatitude() {
			return latitude;
		}

		public long getLongitude() {
			return longitude;
		}
	}
}
