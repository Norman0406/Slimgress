package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class GameEntity extends Entity
{
	public enum GameEntityType
	{
		ControlField,
		Link,
		Portal,
		Item
	}
	
	private enum OwnerType
	{
		Creator,
		Conqueror
	}
	
	GameEntityType mGameEntityType;
	OwnerType mOwnerType;	// created or captured
	String mOwnerGuid;
	String mOwnerTimestamp;
		
	GameEntity(GameEntityType type, JSONArray json) throws JSONException
	{
		super(json);
		mGameEntityType = type;
		
		JSONObject item = json.getJSONObject(2);

		if (item.has("creator")) {
			JSONObject creator = item.getJSONObject("creator");
			mOwnerGuid = creator.getString("creatorGuid");
			mOwnerTimestamp = creator.getString("creationTime");
			mOwnerType = OwnerType.Creator;
		}
		else if (item.has("captured")) {
			JSONObject creator = item.getJSONObject("captured");
			mOwnerGuid = creator.getString("capturingPlayerId");
			mOwnerTimestamp = creator.getString("capturedTime");
			mOwnerType = OwnerType.Conqueror;
		}
		else {
			// UNDONE: GameEntityItem does not have owner information
			//throw new RuntimeException("no owner information available");
		}
	}
	
	public static GameEntity createEntity(JSONArray json) throws JSONException
	{	
		if (json.length() != 3)
			throw new JSONException("invalid array size");

		JSONObject item = json.getJSONObject(2);
		
		// create entity
		GameEntity newEntity = null;
		if (item.has("edge"))
			newEntity = new GameEntityLink(json);
		else if (item.has("capturedRegion"))
			newEntity = new GameEntityControlField(json);
		else if (item.has("portalV2"))
			newEntity = new GameEntityPortal(json);
		else if (item.has("resource") || item.has("resourceWithLevels") || item.has("modResource"))
			newEntity = new GameEntityItem(json);
		else {
			// unknown game entity
			throw new RuntimeException("unknown game entity");
		}
		
		return newEntity;
	}
	
	public GameEntityType getGameEntityType()
	{
		return mGameEntityType;
	}
	
	public OwnerType getOwnerType()
	{
		return mOwnerType;
	}
	
	public String getOwnerGuid()
	{
		return mOwnerGuid;
	}

	public String getOwnerTimestamp()
	{
		return mOwnerTimestamp;
	}
}
