package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemPowerCube extends Item {
	private int powerCubeEnergy;
	
	public ItemPowerCube(String guid, String timestamp) {
		super(guid, timestamp, Item.ItemType.PowerCube);
	}

	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		super.initByJSON(json);
		
		JSONObject powerCube = json.getJSONObject("powerCube");
		
		powerCubeEnergy = powerCube.getInt("energy");
	}
	
	public int getPowerCubeEnergy() {
		return powerCubeEnergy;
	}
}
