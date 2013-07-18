package com.norman0406.ingressex.API;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONHandlerGameBasket implements JSONHandler {

	private String lastSyncTimestamp;
	private List<GameEntity> gameEntities;
	private List<Item> inventory;
	private List<String> deletedEntityGuids;
	private List<XMParticle> energyGlobGuids;
	
	@Override
	public void handleJSON(JSONObject json) throws JSONException {
		if (json.has("exception")) {
			String excMsg = json.getString("exception");
			throw new RuntimeException(excMsg);
		}
		
		JSONObject gameBasket = json.getJSONObject("gameBasket");
		processGameEntities(gameBasket.getJSONArray("gameEntities"));
		processInventory(gameBasket.getJSONArray("inventory"));
		processDeletedEntityGuids(gameBasket.getJSONArray("deletedEntityGuids"));
		processEnergyGlobGuids(gameBasket.optJSONArray("energyGlobGuids"), gameBasket.optString("energyGlobTimestamp"));
	
		lastSyncTimestamp = json.getString("result");
	}
	
	private void processGameEntities(JSONArray gameEntities) throws JSONException {
		// iterate over game entites
		for (int i = 0; i < gameEntities.length(); i++) {
			JSONArray resource = gameEntities.getJSONArray(i);

			// deserialize the game entity using the JSON representation
			GameEntity newEntity = GameEntity.createEntity(resource);
			
			// add the new entity to the world
			if (newEntity != null) {
				gameEntities.put(newEntity);
			}
		}
	}
	
	private void processInventory(JSONArray inventory) throws JSONException {
		// iterate over inventory items
		for (int i = 0; i < inventory.length(); i++) {
			JSONArray resource = inventory.getJSONArray(i);
			
			// deserialize the item using the JSON representation
			Item newItem = Item.createItem(resource);//.getString(0), resource.getString(1), resource.getJSONObject(2));

			// add the new item to the player inventory
			if (newItem != null) {
				inventory.put(newItem);
			}
		}
	}
	
	private void processDeletedEntityGuids(JSONArray deletedEntityGuids) throws JSONException {
		// UNDONE
	}

	private void processEnergyGlobGuids(JSONArray energyGlobGuids, String timestamp) throws JSONException {
		if (energyGlobGuids != null && timestamp.length() > 0) {
			for (int i = 0; i < energyGlobGuids.length(); i++) {
				String guid = energyGlobGuids.getString(i);
				
				XMParticle newParticle = new XMParticle(guid, timestamp);
				energyGlobGuids.put(newParticle);
			}
		}
	}
	
	public String getLastSyncTimestamp() {
		return lastSyncTimestamp;
	}

	public List<GameEntity> getGameEntities() {
		return gameEntities;
	}
	
	public List<Item> getInventory() {
		return inventory;
	}
	
	public List<String> getDeletedEntityGuids() {
		return deletedEntityGuids;
	}
	
	public List<XMParticle> getEnergyGlobGuids() {
		return energyGlobGuids;
	}
}
