package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemPowerCube extends Item {
	public int powerCubeEnergy;
	
	public ItemPowerCube()
	{
		super(Item.ItemType.PowerCube);
	}

	@Override
	protected void initItemByJSON(JSONObject json) throws JSONException {
		JSONObject powerCube = json.getJSONObject("powerCube");
		
		powerCubeEnergy = powerCube.getInt("energy");
	}
	
}
