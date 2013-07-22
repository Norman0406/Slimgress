package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemModTurret extends ItemMod
{	
	private int attackFrequency;
	private int hitBonus;
	
	public ItemModTurret(JSONArray json) throws JSONException
	{
		super(json);
		
		JSONObject item = json.getJSONObject(2);
		JSONObject modResource = item.getJSONObject("modResource");
		JSONObject stats = modResource.getJSONObject("stats");
		attackFrequency = Integer.parseInt(stats.getString("ATTACK_FREQUENCY"));
		hitBonus = Integer.parseInt(stats.getString("HIT_BONUS"));
	}
	
	public int getAttackFrequency()
	{
		return attackFrequency;
	}
	
	public int getHitBonus()
	{
		return hitBonus;
	}
}