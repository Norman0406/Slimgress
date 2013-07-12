package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemModLinkAmp extends ItemMod {
	
	private int linkAmpMultiplier;

	public ItemModLinkAmp(String guid, String timestamp) {
		super(guid, timestamp);
	}
	
	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		super.initByJSON(json);
		
		JSONObject modResource = json.getJSONObject("modResource");
		JSONObject stats = modResource.getJSONObject("stats");
		linkAmpMultiplier = Integer.parseInt(stats.getString("LINK_RANGE_MULTIPLIER"));
	}
	
	public int getLinkAmpMultiplier() {
		return linkAmpMultiplier;
	}
}