package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;

public class ItemResonator extends Item
{	
	public ItemResonator(JSONArray json) throws JSONException
	{
		super(ItemType.Resonator, json);
	}
}
