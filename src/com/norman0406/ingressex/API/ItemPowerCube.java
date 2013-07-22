package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemPowerCube extends Item
{
	private int mEnergy;
	
	public ItemPowerCube(JSONArray json) throws JSONException
	{
		super(ItemType.PowerCube, json);
		
		JSONObject item = json.getJSONObject(2);
		JSONObject powerCube = item.getJSONObject("powerCube");
		
		mEnergy = powerCube.getInt("energy");
	}
	
	public int getEnergy()
	{
		return mEnergy;
	}
}
