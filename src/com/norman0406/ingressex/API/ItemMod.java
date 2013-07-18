package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ItemMod extends Item {
	
	private String modDisplayName;
	private int removalStickiness;

	public ItemMod(JSONArray json) throws JSONException {
		super(ItemType.Mod, json);

		JSONObject item = json.getJSONObject(2);
		JSONObject modResource = item.getJSONObject("modResource");
		JSONObject stats = modResource.getJSONObject("stats");
		modDisplayName = modResource.getString("displayName");
		removalStickiness = Integer.parseInt(stats.getString("REMOVAL_STICKINESS"));
	}
	
	public String getModDisplayName() {
		return modDisplayName;
	}
	
	public int getRemovalStickiness() {
		return removalStickiness;
	}
}
