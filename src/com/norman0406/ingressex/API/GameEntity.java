package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class GameEntity extends Entity {
	
	private enum OwnerType {
		Creator,
		Conqueror
	}
	
	OwnerType ownerType;	// created or captured
	String ownerGuid;
	String ownerTimestamp;
		
	GameEntity(JSONArray json) throws JSONException {
		super(json);
		
		JSONObject item = json.getJSONObject(2);

		if (item.has("creator")) {
			JSONObject creator = item.getJSONObject("creator");
			ownerGuid = creator.getString("creatorGuid");
			ownerTimestamp = creator.getString("creationTime");
			ownerType = OwnerType.Creator;
		}
		else if (item.has("captured")) {
			JSONObject creator = item.getJSONObject("captured");
			ownerGuid = creator.getString("capturingPlayerId");
			ownerTimestamp = creator.getString("capturedTime");
			ownerType = OwnerType.Conqueror;
		}
		else
			throw new RuntimeException("no owner information available");		
	}
	
	public static GameEntity createEntity(JSONArray json) throws JSONException {	
		if (json.length() != 3)
			throw new JSONException("invalid array size");

		JSONObject item = json.getJSONObject(2);
		
		// create entity
		GameEntity newEntity = null;
		if (item.has("edge"))
			newEntity = new GameEntityLink(json);
		else if (item.has("capturedRegion"))
			newEntity = new GameEntityControlField(json);
		else if (item.has("portalV2")) {
			System.out.println("Portals not yet working");
			//newEntity = new GameEntityPortal(guid, timestamp);
		}
		else {
			// unknown game entity
			throw new RuntimeException("unknown game entity");
		}
		
		return newEntity;
	}
	
	public OwnerType getOwnerType() {
		return ownerType;
	}
	
	public String getOwnerGuid() {
		return ownerGuid;
	}

	public String getOwnerTimestamp() {
		return ownerTimestamp;
	}
}
