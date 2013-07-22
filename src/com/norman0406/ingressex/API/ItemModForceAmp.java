package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemModForceAmp extends ItemMod
{	
	private int mForceAmplifier;

	public ItemModForceAmp(JSONArray json) throws JSONException
	{
		super(json);
		
		JSONObject item = json.getJSONObject(2);
		JSONObject modResource = item.getJSONObject("modResource");
		JSONObject stats = modResource.getJSONObject("stats");
		mForceAmplifier = Integer.parseInt(stats.getString("FORCE_AMPLIFIER"));
	}
	
	public int getForceAmplifier() 
	{
		return mForceAmplifier;
	}
}