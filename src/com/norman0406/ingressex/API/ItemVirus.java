package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemVirus extends Item
{	
	public enum VirusType
	{
		Jarvis,
		Ada
	}
	
	private VirusType mVirusType;
	
	public ItemVirus(JSONArray json) throws JSONException
	{
		super(ItemType.Virus, json);

		JSONObject item = json.getJSONObject(2);
		JSONObject flipCard = item.getJSONObject("flipCard");
		
		if (flipCard.getString("flipCardType").equals("JARVIS"))
			mVirusType = VirusType.Jarvis;
		else if (flipCard.getString("flipCardType").equals("ADA"))
			mVirusType = VirusType.Ada;
		else
			System.out.println("unknown virus type");
	}
	
	public VirusType getVirusType()
	{
		return mVirusType;
	}
}
