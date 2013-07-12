package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemResonator extends Item {
	
	public ItemResonator()
	{
		super(Item.ItemType.Resonator);
	}

	@Override
	protected void initItemByJSON(JSONObject json) throws JSONException {
	}
	
}
