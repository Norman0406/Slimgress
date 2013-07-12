package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemModTurret extends ItemMod {
		
	@Override
	protected void initItemByJSON(JSONObject json) throws JSONException
	{
		JSONObject modResource = json.getJSONObject("modResource");
		super.initItemByJSON(modResource);
	}
}