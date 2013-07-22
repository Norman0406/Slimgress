package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemModLinkAmp extends ItemMod
{	
	private int mLinkRangeMultiplier;

	public ItemModLinkAmp(JSONArray json) throws JSONException
	{
		super(json);
		
		JSONObject item = json.getJSONObject(2);
		JSONObject modResource = item.getJSONObject("modResource");
		JSONObject stats = modResource.getJSONObject("stats");
		mLinkRangeMultiplier = Integer.parseInt(stats.getString("LINK_RANGE_MULTIPLIER"));
	}
	
	public int getLinkRangeMultiplier()
	{
		return mLinkRangeMultiplier;
	}
}