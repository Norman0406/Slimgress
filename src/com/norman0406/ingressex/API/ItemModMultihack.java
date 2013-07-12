package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemModMultihack extends ItemMod {

	private int multihackInsulation;

	public ItemModMultihack(String guid, String timestamp) {
		super(guid, timestamp);
	}
	
	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		super.initByJSON(json);
		
		JSONObject modResource = json.getJSONObject("modResource");
		JSONObject stats = modResource.getJSONObject("stats");
		multihackInsulation = Integer.parseInt(stats.getString("BURNOUT_INSULATION"));
	}
	
	public int getMultihackInsulation() {
		return multihackInsulation;
	}
}