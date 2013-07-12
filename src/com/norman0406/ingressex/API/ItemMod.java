package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ItemMod extends Item {
	
	private String modDisplayName;
	private int removalStickiness;

	public ItemMod(String guid, String timestamp) {
		super(guid, timestamp, Item.ItemType.Mod);
	}
	
	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		super.initByJSON(json);
		
		JSONObject modResource = json.getJSONObject("modResource");
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
