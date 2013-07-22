package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemModTurret extends ItemMod
{	
	private int mAttackFrequency;
	private int mHitBonus;
	
	public ItemModTurret(JSONArray json) throws JSONException
	{
		super(json);
		
		JSONObject item = json.getJSONObject(2);
		JSONObject modResource = item.getJSONObject("modResource");
		JSONObject stats = modResource.getJSONObject("stats");
		mAttackFrequency = Integer.parseInt(stats.getString("ATTACK_FREQUENCY"));
		mHitBonus = Integer.parseInt(stats.getString("HIT_BONUS"));
	}
	
	public int getAttackFrequency()
	{
		return mAttackFrequency;
	}
	
	public int getHitBonus()
	{
		return mHitBonus;
	}
}