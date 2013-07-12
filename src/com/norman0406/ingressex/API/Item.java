package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Item {
	
	public enum ItemType
	{
		Media,
		Mod,
		PortalKey,
		PowerCube,
		Resonator,
		Virus,
		XMP
	}
	
	public enum Rarity
	{	// are there more to come?
		None,
		VeryCommon,
		Common,
		Rare,
		VeryRare
	}

	private String itemGuid;
	private int itemAccessLevel = 0;
	private Rarity itemRarity = Rarity.None;
	private final ItemType itemType;
	private String itemPlayerId;
	private String itemAcquisitionTimestamp;
	
	protected Item(ItemType type)
	{
		itemType = type;
	}

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
			newItem = new ItemModShield();
		else if (itemType.equals("POWER_CUBE"))
			newItem = new ItemPowerCube();
		else if (itemType.equals("MEDIA"))
			newItem = new ItemMedia();
		else if (itemType.equals("FORCE_AMP"))
			newItem = new ItemModForceAmp();
		else if (itemType.equals("MULTIHACK"))
			newItem = new ItemModMultihack();
		else if (itemType.equals("LINK_AMPLIFIER"))
			newItem = new ItemModLinkAmp();
		else if (itemType.equals("TURRET"))
			newItem = new ItemModTurret();
		else if (itemType.equals("HEAT_SINK"))
			newItem = new ItemModHeatSink();
		else if (itemType.equals("FLIP_CARD"))
			newItem = new ItemVirus();
		else {
			// unknown resource type
			System.out.println("unknown resource type");
		}

		// init item
		if (newItem != null) {
			newItem.itemGuid = guid;
			
			if (itemResource.has("resourceRarity") || itemResource.has("rarity")) {
				String itemRarity = null;
				if (itemResource.has("resourceRarity"))
					itemRarity = itemResource.getString("resourceRarity");
				else if (itemResource.has("rarity"))
					itemRarity = itemResource.getString("rarity");
				else {
					// unknown rarity string
				}
				
				if (itemRarity != null) {
					if (itemRarity.equals("VERY_COMMON"))
						newItem.itemRarity = Item.Rarity.VeryCommon;
					else if (itemRarity.equals("COMMON"))
						newItem.itemRarity = Item.Rarity.Common;
					else if (itemRarity.equals("RARE"))
						newItem.itemRarity = Item.Rarity.Rare;
					else if (itemRarity.equals("VERY_RARE"))
						newItem.itemRarity = Item.Rarity.VeryRare;
				}
			}

			if (itemResource.has("level")) {
				int level = itemResource.getInt("level");
				newItem.itemAccessLevel = level;
			}
			
			JSONObject itemInInventory = json.getJSONObject("inInventory");
			newItem.itemPlayerId = itemInInventory.getString("playerId");
			newItem.itemAcquisitionTimestamp = itemInInventory.getString("acquisitionTimestampMs");
			
			newItem.initItemByJSON(json);
		}
				
		return newItem;
	}
	
	protected abstract void initItemByJSON(JSONObject json) throws JSONException;
	
	public String getItemGuid()
	{
		return itemGuid;
	}

	public int getItemAccessLevel()
	{
		return itemAccessLevel;
	}

	public Rarity getItemRarity()
	{
		return itemRarity;
	}

	public String getItemPlayerId()
	{
		return itemPlayerId;
	}

	public String getItemAcquisitionTimestamp()
	{
		return itemAcquisitionTimestamp;
	}

	public ItemType getItemType()
	{
		return itemType;
	}
}