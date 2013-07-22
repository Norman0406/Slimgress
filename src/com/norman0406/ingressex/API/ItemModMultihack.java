package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemModMultihack extends ItemMod
{
	private int mBurnoutInsulation;

	public ItemModMultihack(JSONArray json) throws JSONException
	{
		super(json);
		
		JSONObject item = json.getJSONObject(2);
		JSONObject modResource = item.getJSONObject("modResource");
		JSONObject stats = modResource.getJSONObject("stats");
		mBurnoutInsulation = Integer.parseInt(stats.getString("BURNOUT_INSULATION"));
	}
	
	public int getBurnoutInsulation()
	{
		return mBurnoutInsulation;
	}
}