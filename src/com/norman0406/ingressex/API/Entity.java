package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;

public abstract class Entity {

	private String entityGuid;
	private String entityTimestamp;
	
	public Entity(JSONArray json) throws JSONException {
		if (json.length() != 3)
			throw new JSONException("invalid array size");
		
		entityGuid = json.getString(0);
		entityTimestamp = json.getString(1);
	}

	public String getEntityGuid() {
		return entityGuid;
	}

	public String getEntityTimestamp() {
		return entityTimestamp;
	}
}
