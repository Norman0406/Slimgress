package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;

public class GameEntityItem extends GameEntity
{
	GameEntityItem(JSONArray json) throws JSONException {
		super(GameEntityType.Item, json);
		
		// UNDONE
	}	
}
