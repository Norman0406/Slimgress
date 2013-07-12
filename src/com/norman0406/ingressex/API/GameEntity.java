package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class GameEntity extends Entity {
	
	private String creatorGuid;			// NOTE: not valid for portals
	private String creationTimestamp;

	GameEntity(String guid, String timestamp) {
		super(guid, timestamp);
	}
	
	public static GameEntity createEntity(String guid, String timestamp, JSONObject json) throws JSONException {
		GameEntity newEntity = null;
		
		// create entity
		if (json.has("edge"))
			newEntity = new GameEntityLink(guid, timestamp);
		else if (json.has("capturedRegion"))
			newEntity = new GameEntityControlField(guid, timestamp);
		else if (json.has("portalV2"))
			newEntity = new GameEntityPortal(guid, timestamp);
		
		// init entity
		if (newEntity != null)
			newEntity.initByJSON(json);
		
		return newEntity;
	}
	
	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		JSONObject creator = json.getJSONObject("creator");
		creatorGuid = creator.getString("creatorGuid");
		creationTimestamp = creator.getString("creationTime");
	}

	public String getCreatorGuid() {
		return creatorGuid;
	}

	public String getCreationTimestamp() {
		return creationTimestamp;
	}
}
