package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemModShield extends ItemMod {
	
	private int shieldRemovalStickiness;
	private int shieldMitigation;
	
	@Override
	protected void initItemByJSON(JSONObject json) throws JSONException
	{
		JSONObject modResource = json.getJSONObject("modResource");
		super.initItemByJSON(modResource);

		JSONObject shieldStats = modResource.getJSONObject("stats");
		
		shieldRemovalStickiness = Integer.parseInt(shieldStats.getString("REMOVAL_STICKINESS"));
		shieldMitigation = Integer.parseInt(shieldStats.getString("MITIGATION"));
	}
	
	public int getShieldRemovalStickiness()
	{
		return shieldRemovalStickiness;
	}

	public int getShieldMitigation()
	{
		return shieldMitigation;
	}	
}