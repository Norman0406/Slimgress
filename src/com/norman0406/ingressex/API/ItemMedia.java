package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemMedia extends Item {
	
	public ItemMedia()
	{
		super(Item.ItemType.Media);
	}

	@Override
	protected void initItemByJSON(JSONObject json) throws JSONException {
	}
}
