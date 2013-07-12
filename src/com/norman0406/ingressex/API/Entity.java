package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Entity {

	private final String entityGuid;
	private final String entityTimestamp;
	
	Entity(String guid, String timestamp) {
		entityGuid = guid;
		entityTimestamp = timestamp;
	}
	
	protected abstract void initByJSON(JSONObject json) throws JSONException;

	public String getEntityGuid() {
		return entityGuid;
	}

	public String getEntityTimestamp() {
		return entityTimestamp;
	}
}
