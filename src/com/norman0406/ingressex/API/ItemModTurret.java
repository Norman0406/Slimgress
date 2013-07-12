package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemModTurret extends ItemMod {
	
	public ItemModTurret(String guid, String timestamp) {
		super(guid, timestamp);
	}
	
	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		super.initByJSON(json);
	}
}