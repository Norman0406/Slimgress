package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemVirus extends Item {
	
	public ItemVirus()
	{
		super(Item.ItemType.Virus);
	}

	@Override
	protected void initItemByJSON(JSONObject json) throws JSONException {
	}	
}
