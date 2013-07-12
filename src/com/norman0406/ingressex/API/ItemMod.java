package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ItemMod extends Item {
	
	private String modDisplayName;

	public ItemMod()
	{
		super(Item.ItemType.Mod);
	}
	
	@Override
	protected void initItemByJSON(JSONObject json) throws JSONException
	{
		modDisplayName = json.getString("displayName");
	}
	
	public String getModDisplayName()
	{
		return modDisplayName;
	}
}
