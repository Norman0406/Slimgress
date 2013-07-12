package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemResonator extends Item {
	
	public ItemResonator(String guid, String timestamp) {
		super(guid, timestamp, Item.ItemType.Resonator);
	}

	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		super.initByJSON(json);
	}	
}
