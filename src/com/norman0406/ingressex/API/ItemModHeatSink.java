package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemModHeatSink extends ItemMod {
	
	public ItemModHeatSink(JSONArray json) throws JSONException {
		super(json);

		JSONObject item = json.getJSONObject(2);
		// UNDONE
	}
}