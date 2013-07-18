package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemModShield extends ItemMod {
	
	private int shieldMitigation;

	public ItemModShield(JSONArray json) throws JSONException {
		super(json);
		
		JSONObject item = json.getJSONObject(2);
		JSONObject modResource = item.getJSONObject("modResource");
		JSONObject stats = modResource.getJSONObject("stats");
		shieldMitigation = Integer.parseInt(stats.getString("MITIGATION"));
	}

	public int getMultihackInsulation() {
		return shieldMitigation;
	}
}