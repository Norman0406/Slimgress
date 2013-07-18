package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;

public class ItemXMP extends Item {
	
	public ItemXMP(JSONArray json) throws JSONException {
		super(ItemType.XMP, json);
		
		// UNDONE: which information is needed from the JSONObject?
	}	
}
