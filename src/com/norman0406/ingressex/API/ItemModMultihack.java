package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemModMultihack extends ItemMod
{
	private int multihackInsulation;

	public ItemModMultihack(JSONArray json) throws JSONException
	{
		super(json);
		
		JSONObject item = json.getJSONObject(2);
		JSONObject modResource = item.getJSONObject("modResource");
		JSONObject stats = modResource.getJSONObject("stats");
		multihackInsulation = Integer.parseInt(stats.getString("BURNOUT_INSULATION"));
	}
	
	public int getMultihackInsulation()
	{
		return multihackInsulation;
	}
}