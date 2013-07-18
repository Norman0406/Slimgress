package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemModLinkAmp extends ItemMod {
	
	private int linkAmpMultiplier;

	public ItemModLinkAmp(JSONArray json) throws JSONException {
		super(json);
		
		JSONObject item = json.getJSONObject(2);
		JSONObject modResource = item.getJSONObject("modResource");
		JSONObject stats = modResource.getJSONObject("stats");
		linkAmpMultiplier = Integer.parseInt(stats.getString("LINK_RANGE_MULTIPLIER"));
	}
	
	public int getLinkAmpMultiplier() {
		return linkAmpMultiplier;
	}
}