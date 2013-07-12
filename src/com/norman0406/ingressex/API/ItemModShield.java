package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemModShield extends ItemMod {
	
	private int shieldMitigation;

	public ItemModShield(String guid, String timestamp) {
		super(guid, timestamp);
	}
	
	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		super.initByJSON(json);
		
		JSONObject modResource = json.getJSONObject("modResource");
		JSONObject stats = modResource.getJSONObject("stats");
		shieldMitigation = Integer.parseInt(stats.getString("MITIGATION"));
	}

	public int getMultihackInsulation() {
		return shieldMitigation;
	}
}