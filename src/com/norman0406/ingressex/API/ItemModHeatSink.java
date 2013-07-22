package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemModHeatSink extends ItemMod
{	
	private int mHackSpeed;
	
	public ItemModHeatSink(JSONArray json) throws JSONException
	{
		super(json);

		JSONObject item = json.getJSONObject(2);
		JSONObject modResource = item.getJSONObject("modResource");
		JSONObject stats = modResource.getJSONObject("stats");
		mHackSpeed = Integer.parseInt(stats.getString("HACK_SPEED"));
	}
	
	public int getHackSpeed()
	{
		return mHackSpeed;
	}
}