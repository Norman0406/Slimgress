package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Item extends Entity
{
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
	{
		None,
		VeryCommon,
		Common,
		LessCommon,
		Rare,
		VeryRare,
		ExtraRare
	}

	private int mItemAccessLevel = 0;
	private Rarity mItemRarity = Rarity.None;
	private final ItemType mItemType;
	private String mItemPlayerId;
	private String mItemAcquisitionTimestamp;	
	
	protected Item(ItemType type, JSONArray json) throws JSONException
	{
		super(json);
		mItemType = type;

		JSONObject item = json.getJSONObject(2);
		JSONObject itemResource = null;
		if (item.has("resource"))
			itemResource = item.getJSONObject("resource");
		else if (item.has("resourceWithLevels"))
			itemResource = item.getJSONObject("resourceWithLevels");
		else if (item.has("modResource"))
			itemResource = item.getJSONObject("modResource");
		else
			throw new JSONException("resource not found");
		
		if (itemResource.has("resourceRarity") || itemResource.has("rarity")) {
			String rarity = null;
			if (itemResource.has("resourceRarity"))
				rarity = itemResource.getString("resourceRarity");
			else if (itemResource.has("rarity"))
				rarity = itemResource.getString("rarity");
			else
				throw new RuntimeException("unknown rarity string");
			
			if (mItemRarity != null) {
				if (rarity.equals("VERY_COMMON"))
					mItemRarity = Item.Rarity.VeryCommon;
				else if (rarity.equals("COMMON"))
					mItemRarity = Item.Rarity.Common;
                else if (rarity.equals("LESS_COMMON"))
                    mItemRarity = Item.Rarity.LessCommon;
				else if (rarity.equals("RARE"))
					mItemRarity = Item.Rarity.Rare;
				else if (rarity.equals("VERY_RARE"))
					mItemRarity = Item.Rarity.VeryRare;
                else if (rarity.equals("EXTREMELY_RARE"))
                    mItemRarity = Item.Rarity.ExtraRare;
			}
		}

		if (itemResource.has("level")) {
			int level = itemResource.getInt("level");
			mItemAccessLevel = level;
		}
		
		JSONObject itemInInventory = item.getJSONObject("inInventory");
		mItemPlayerId = itemInInventory.getString("playerId");
		mItemAcquisitionTimestamp = itemInInventory.getString("acquisitionTimestampMs");
	}

	public static Item createItem(JSONArray json) throws JSONException
	{
		if (json.length() != 3)
			throw new JSONException("invalid array size");
		
		JSONObject item = json.getJSONObject(2);
		
		JSONObject itemResource = null;
		if (item.has("resource"))
			itemResource = item.getJSONObject("resource");
		else if (item.has("resourceWithLevels"))
			itemResource = item.getJSONObject("resourceWithLevels");
		else if (item.has("modResource"))
			itemResource = item.getJSONObject("modResource");
		
		// create item
		Item newItem = null;
		
		String itemType = itemResource.getString("resourceType");
		if (itemType.equals("PORTAL_LINK_KEY"))
			newItem = new ItemPortalKey(json);
		else if (itemType.equals("EMP_BURSTER"))
			newItem = new ItemXMP(json);
		else if (itemType.equals("EMITTER_A"))
			newItem = new ItemResonator(json);
		else if (itemType.equals("RES_SHIELD"))
			newItem = new ItemModShield(json);
		else if (itemType.equals("POWER_CUBE"))
			newItem = new ItemPowerCube(json);
		else if (itemType.equals("MEDIA"))
			newItem = new ItemMedia(json);
		else if (itemType.equals("FORCE_AMP"))
			newItem = new ItemModForceAmp(json);
		else if (itemType.equals("MULTIHACK"))
			newItem = new ItemModMultihack(json);
		else if (itemType.equals("LINK_AMPLIFIER"))
			newItem = new ItemModLinkAmp(json);
		else if (itemType.equals("TURRET"))
			newItem = new ItemModTurret(json);
		else if (itemType.equals("HEATSINK"))
			newItem = new ItemModHeatSink(json);
		else if (itemType.equals("FLIP_CARD"))
			newItem = new ItemVirus(json);
		else {
			// unknown resource type
			throw new RuntimeException("unknown resource type");
		}

		return newItem;
	}
	
	public int getItemAccessLevel()
	{
		return mItemAccessLevel;
	}

	public Rarity getItemRarity()
	{
		return mItemRarity;
	}

	public String getItemPlayerId()
	{
		return mItemPlayerId;
	}

	public String getItemAcquisitionTimestamp()
	{
		return mItemAcquisitionTimestamp;
	}

	public ItemType getItemType()
	{
		return mItemType;
	}
}