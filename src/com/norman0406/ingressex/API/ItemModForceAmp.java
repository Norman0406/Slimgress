package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemModForceAmp extends ItemMod {
	
	private int forceAmpValue;

	public ItemModForceAmp(JSONArray json) throws JSONException {
		super(json);
		
		JSONObject item = json.getJSONObject(2);
		JSONObject modResource = item.getJSONObject("modResource");
		JSONObject stats = modResource.getJSONObject("stats");
		forceAmpValue = Integer.parseInt(stats.getString("FORCE_AMPLIFIER"));
	}
	
	public int getForceAmpValue() {
		return forceAmpValue;
	}
}