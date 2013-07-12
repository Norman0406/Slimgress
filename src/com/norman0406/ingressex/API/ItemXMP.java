package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemXMP extends Item {
	
	public ItemXMP(String guid, String timestamp) {
		super(guid, timestamp, Item.ItemType.XMP);
	}

	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		super.initByJSON(json);
		
		// which information is needed from the JSONObject?
	}	
}
