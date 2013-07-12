package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemModForceAmp extends ItemMod {
	
	private int forceAmpValue;

	public ItemModForceAmp(String guid, String timestamp) {
		super(guid, timestamp);
	}
	
	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		super.initByJSON(json);
		
		JSONObject modResource = json.getJSONObject("modResource");
		JSONObject stats = modResource.getJSONObject("stats");
		forceAmpValue = Integer.parseInt(stats.getString("FORCE_AMPLIFIER"));
	}
	
	public int getForceAmpValue() {
		return forceAmpValue;
	}
}