package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Item {
	public enum AccessLevel
	{
		NONE,
		L1,
		L2,
		L3,
		L4,
		L5,
		L6,
		L7,
		L8
	};
	
	public enum Rarity
	{	// are there more to come?
		NONE,
		VERY_COMMON,
		COMMON,
		RARE,
		VERY_RARE
	}

	public String itemGuid;
	public AccessLevel itemAccessLevel = AccessLevel.NONE;
	public Rarity itemRarity = Rarity.NONE;
	public String itemPlayerId;
	public String itemAcquisitionTimestamp;

	public static Item createItem(String guid, String timestamp, JSONObject json) throws JSONException
	{
		// UNDONE
		Item newItem = null;

		JSONObject itemResource = null;
		if (json.has("resource"))
			itemResource = json.getJSONObject("resource");
		else if (json.has("resourceWithLevels"))
			itemResource = json.getJSONObject("resourceWithLevels");
		else if (json.has("modResource"))
			itemResource = json.getJSONObject("modResource");
		
		// create item
		String itemType = itemResource.getString("resourceType");
		if (itemType.equals("PORTAL_LINK_KEY"))
			newItem = new ItemPortalKey();
		else if (itemType.equals("EMP_BURSTER"))
			newItem = new ItemXMP();
		else if (itemType.equals("EMITTER_A"))
			newItem = new ItemResonator();
		else if (itemType.equals("RES_SHIELD"))
			newItem = new ItemMod();
		else if (itemType.equals("POWER_CUBE"))
			newItem = new ItemPowerCube();
		else {
			// unknown resource type
			System.out.println("unknown resource type");
		}

		// init item
		if (newItem != null) {
			newItem.itemGuid = guid;
			
			String itemRarity = itemResource.getString("resourceRarity");
			if (itemRarity.equals("VERY_COMMON"))
				newItem.itemRarity = Item.Rarity.VERY_COMMON;
			else if (itemRarity.equals("COMMON"))
				newItem.itemRarity = Item.Rarity.COMMON;
			else if (itemRarity.equals("RARE"))
				newItem.itemRarity = Item.Rarity.RARE;
			else if (itemRarity.equals("VERY_RARE"))
				newItem.itemRarity = Item.Rarity.VERY_RARE;

			JSONObject itemInInventory = json.getJSONObject("inInventory");
			newItem.itemPlayerId = itemInInventory.getString("playerId");
			newItem.itemAcquisitionTimestamp = itemInInventory.getString("acquisitionTimestampMs");
			
			newItem.initItemByJSON(json);
		}
				
		return newItem;		
	}
	
	protected abstract void initItemByJSON(JSONObject json) throws JSONException;
}