package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemPowerCube extends Item
{
	private int powerCubeEnergy;
	
	public ItemPowerCube(JSONArray json) throws JSONException
	{
		super(ItemType.PowerCube, json);
		
		JSONObject item = json.getJSONObject(2);
		JSONObject powerCube = item.getJSONObject("powerCube");
		
		powerCubeEnergy = powerCube.getInt("energy");
	}
	
	public int getPowerCubeEnergy()
	{
		return powerCubeEnergy;
	}
}
