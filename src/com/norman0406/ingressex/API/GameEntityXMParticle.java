package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class GameEntityXMParticle extends GameEntity {

	GameEntityXMParticle(String guid, String timestamp) {
		super(guid, timestamp);
	}

	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		super.initByJSON(json);
	}
}
