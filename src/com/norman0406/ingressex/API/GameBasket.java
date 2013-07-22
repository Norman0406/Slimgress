package com.norman0406.ingressex.API;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameBasket
{
	public interface Callback
	{
		public void handle(GameBasket gameBasket);
	}
	
	private String mLastSyncTimestamp;
	private PlayerEntity mPlayerEntity;
	private List<GameEntity> mGameEntities = new LinkedList<GameEntity>();
	private List<Item> mInventory = new LinkedList<Item>();
	private List<String> mDeletedEntityGuids = new LinkedList<String>();
	private List<XMParticle> mEnergyGlobGuids = new LinkedList<XMParticle>();
	
	public GameBasket(JSONObject json) throws JSONException
	{
		if (json.has("exception")) {
			String excMsg = json.getString("exception");
			throw new RuntimeException(excMsg);
		}
		
		JSONObject gameBasket = json.getJSONObject("gameBasket");
		
		processPlayerDamages(gameBasket.optJSONArray("playerDamages"));
		processPlayerEntity(gameBasket.optJSONArray("playerEntity"));
		processGameEntities(gameBasket.getJSONArray("gameEntities"));
		processAPGains(gameBasket.optJSONArray("apGains"));
		processLevelUp(gameBasket.optJSONObject("levelUp"));
		processInventory(gameBasket.getJSONArray("inventory"));
		processDeletedEntityGuids(gameBasket.getJSONArray("deletedEntityGuids"));
		processEnergyGlobGuids(gameBasket.optJSONArray("energyGlobGuids"), gameBasket.optString("energyGlobTimestamp"));
	
		mLastSyncTimestamp = json.getString("result");
	}
	
	private void processPlayerDamages(JSONArray playerDamages) throws JSONException
	{
		if (playerDamages != null) {
			for (int i = 0; i < playerDamages.length(); i++) {
				// UNDONE
				
				/*JSONObject damage = playerDamages.getJSONObject(i);
				
				int damageAmount = Integer.parseInt(damage.getString("damageAmount"));
				String attackerGuid = damage.getString("attackerGuid");
				String weaponSerializationTag = damage.getString("weaponSerializationTag");*/
			}
		}
	}

	private void processPlayerEntity(JSONArray playerEntity) throws JSONException
	{
		if (playerEntity != null) {
			mPlayerEntity = new PlayerEntity(playerEntity);
		}
	}
	
	private void processGameEntities(JSONArray gameEntities) throws JSONException
	{
		// iterate over game entites
		for (int i = 0; i < gameEntities.length(); i++) {
			JSONArray resource = gameEntities.getJSONArray(i);

			// deserialize the game entity using the JSON representation
			GameEntity newEntity = GameEntity.createEntity(resource);
			
			// add the new entity to the world
			if (newEntity != null) {
				mGameEntities.add(newEntity);
			}
		}
	}
	
	private void processAPGains(JSONArray apGains)
	{
		if (apGains != null) {
			for (int i = 0; i < apGains.length(); i++) {
				// UNDONE
			}
		}
	}
	
	private void processLevelUp(JSONObject levelUp)
	{
		if (levelUp != null) {
			// UNDONE
		}
	}
	
	private void processInventory(JSONArray inventory) throws JSONException
	{
		// iterate over inventory items
		for (int i = 0; i < inventory.length(); i++) {
			JSONArray resource = inventory.getJSONArray(i);
			
			// deserialize the item using the JSON representation
			Item newItem = Item.createItem(resource);

			// add the new item to the player inventory
			if (newItem != null) {
				mInventory.add(newItem);
			}
		}
	}
	
	private void processDeletedEntityGuids(JSONArray deletedEntityGuids) throws JSONException
	{
		// UNDONE
	}

	private void processEnergyGlobGuids(JSONArray energyGlobGuids, String timestamp) throws JSONException
	{
		if (energyGlobGuids != null && timestamp.length() > 0) {
			for (int i = 0; i < energyGlobGuids.length(); i++) {
				String guid = energyGlobGuids.getString(i);
				
				XMParticle newParticle = new XMParticle(guid, timestamp);
				mEnergyGlobGuids.add(newParticle);
			}
		}
	}
	
	public final String getLastSyncTimestamp()
	{
		return mLastSyncTimestamp;
	}
	
	public final PlayerEntity getPlayerEntity()
	{
		return mPlayerEntity;
	}

	public final List<GameEntity> getGameEntities()
	{
		return mGameEntities;
	}
	
	public final List<Item> getInventory()
	{
		return mInventory;
	}
	
	public final List<String> getDeletedEntityGuids()
	{
		return mDeletedEntityGuids;
	}
	
	public final List<XMParticle> getEnergyGlobGuids()
	{
		return mEnergyGlobGuids;
	}
}
