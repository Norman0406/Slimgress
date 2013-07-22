package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemModShield extends ItemMod
{	
	private int mMitigation;

	public ItemModShield(JSONArray json) throws JSONException
	{
		super(json);
		
		JSONObject item = json.getJSONObject(2);
		JSONObject modResource = item.getJSONObject("modResource");
		JSONObject stats = modResource.getJSONObject("stats");
		mMitigation = Integer.parseInt(stats.getString("MITIGATION"));
	}

	public int getMitigation()
	{
		return mMitigation;
	}
}