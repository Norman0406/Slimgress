package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ItemMod extends Item
{	
	private String mModDisplayName;
	private int mRemovalStickiness;

	public ItemMod(JSONArray json) throws JSONException
	{
		super(ItemType.Mod, json);

		JSONObject item = json.getJSONObject(2);
		JSONObject modResource = item.getJSONObject("modResource");
		JSONObject stats = modResource.getJSONObject("stats");
		mModDisplayName = modResource.getString("displayName");
		mRemovalStickiness = Integer.parseInt(stats.getString("REMOVAL_STICKINESS"));
	}
	
	public String getModDisplayName()
	{
		return mModDisplayName;
	}
	
	public int getRemovalStickiness()
	{
		return mRemovalStickiness;
	}
}
