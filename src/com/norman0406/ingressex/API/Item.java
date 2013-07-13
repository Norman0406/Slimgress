package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Item extends Entity {
	
	public enum ItemType {
		Media,
		Mod,
		PortalKey,
		PowerCube,
		Resonator,
		Virus,
		XMP
	}
	
	public enum Rarity { // are there more to come?
		None,
		VeryCommon,
		Common,
		LessCommon,
		Rare,
		VeryRare,
		ExtraRare
	}

	private int itemAccessLevel = 0;
	private Rarity itemRarity = Rarity.None;
	private final ItemType itemType;
	private String itemPlayerId;
	private String itemAcquisitionTimestamp;	
	
	protected Item(String guid, String timestamp, ItemType type) {
		super(guid, timestamp);
		itemType = type;
	}

	public static Item createItem(String guid, String timestamp, JSONObject json) throws JSONException {
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
			newItem = new ItemPortalKey(guid, timestamp);
		else if (itemType.equals("EMP_BURSTER"))
			newItem = new ItemXMP(guid, timestamp);
		else if (itemType.equals("EMITTER_A"))
			newItem = new ItemResonator(guid, timestamp);
		else if (itemType.equals("RES_SHIELD"))
			newItem = new ItemModShield(guid, timestamp);
		else if (itemType.equals("POWER_CUBE"))
			newItem = new ItemPowerCube(guid, timestamp);
		else if (itemType.equals("MEDIA"))
			newItem = new ItemMedia(guid, timestamp);
		else if (itemType.equals("FORCE_AMP"))
			newItem = new ItemModForceAmp(guid, timestamp);
		else if (itemType.equals("MULTIHACK"))
			newItem = new ItemModMultihack(guid, timestamp);
		else if (itemType.equals("LINK_AMPLIFIER"))
			newItem = new ItemModLinkAmp(guid, timestamp);
		else if (itemType.equals("TURRET"))
			newItem = new ItemModTurret(guid, timestamp);
		else if (itemType.equals("HEATSINK"))
			newItem = new ItemModHeatSink(guid, timestamp);
		else if (itemType.equals("FLIP_CARD"))
			newItem = new ItemVirus(guid, timestamp);
		else {
			// unknown resource type
			System.out.println("unknown resource type");
		}

		// init item
		if (newItem != null)		
			newItem.initByJSON(json);
				
		return newItem;
	}
	
	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		JSONObject itemResource = null;
		if (json.has("resource"))
			itemResource = json.getJSONObject("resource");
		else if (json.has("resourceWithLevels"))
			itemResource = json.getJSONObject("resourceWithLevels");
		else if (json.has("modResource"))
			itemResource = json.getJSONObject("modResource");
		
		if (itemResource.has("resourceRarity") || itemResource.has("rarity")) {
			String rarity = null;
			if (itemResource.has("resourceRarity"))
				rarity = itemResource.getString("resourceRarity");
			else if (itemResource.has("rarity"))
				rarity = itemResource.getString("rarity");
			else {
				// unknown rarity string
			}
			
			if (itemRarity != null) {
				if (rarity.equals("VERY_COMMON"))
					itemRarity = Item.Rarity.VeryCommon;
				else if (rarity.equals("COMMON"))
					itemRarity = Item.Rarity.Common;
				else if (rarity.equals("RARE"))
					itemRarity = Item.Rarity.Rare;
				else if (rarity.equals("VERY_RARE"))
					itemRarity = Item.Rarity.VeryRare;
			}
		}

		if (itemResource.has("level")) {
			int level = itemResource.getInt("level");
			itemAccessLevel = level;
		}
		
		JSONObject itemInInventory = json.getJSONObject("inInventory");
		itemPlayerId = itemInInventory.getString("playerId");
		itemAcquisitionTimestamp = itemInInventory.getString("acquisitionTimestampMs");
	}
	
	public int getItemAccessLevel() {
		return itemAccessLevel;
	}

	public Rarity getItemRarity() {
		return itemRarity;
	}

	public String getItemPlayerId() {
		return itemPlayerId;
	}

	public String getItemAcquisitionTimestamp() {
		return itemAcquisitionTimestamp;
	}

	public ItemType getItemType() {
		return itemType;
	}
}