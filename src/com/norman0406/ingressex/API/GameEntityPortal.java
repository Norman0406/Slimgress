package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class GameEntityPortal extends GameEntity {

	GameEntityPortal(String guid, String timestamp) {
		super(guid, timestamp);
	}

	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		super.initByJSON(json);
	}
}
