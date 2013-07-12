package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemVirus extends Item {
	
	public enum VirusType {
		Jarvis,
		Ada
	}
	
	private VirusType virusType;
	
	public ItemVirus(String guid, String timestamp) {
		super(guid, timestamp, Item.ItemType.Virus);
	}

	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		super.initByJSON(json);
		
		JSONObject flipCard = json.getJSONObject("flipCard");
		
		if (flipCard.getString("flipCardType").equals("JARVIS"))
			virusType = VirusType.Jarvis;
		else if (flipCard.getString("flipCardType").equals("ADA"))
			virusType = VirusType.Ada;
		else
			System.out.println("unknown virus type");
	}
	
	public VirusType getVirusType() {
		return virusType;
	}
}
