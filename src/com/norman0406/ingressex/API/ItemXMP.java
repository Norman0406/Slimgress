package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemXMP extends Item {
	
	public ItemXMP()
	{
		super(Item.ItemType.XMP);
	}

	@Override
	protected void initItemByJSON(JSONObject json) throws JSONException {
		// which information is needed from the JSONObject?
	}
	
}
