package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {

	public enum Team {
		Resistance,
		Enlightened,
	}
	
	public static class LocationE6 {
		private int latitude;
		private int longitude;
		
		public LocationE6(JSONObject json) throws JSONException {
			latitude = json.getInt("latE6");
			longitude = json.getInt("lngE6");
		}

		public int getLatitude() {
			return latitude;
		}

		public int getLongitude() {
			return longitude;
		}
	}

}
