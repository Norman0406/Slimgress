package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemPortalKey extends Item {

	private String portalGuid;
	private String portalLocation;
	private String portalImageUrl;
	private String portalTitle;
	private String portalAddress;
	
	public ItemPortalKey(JSONArray json) throws JSONException {
		super(ItemType.PortalKey, json);

		JSONObject item = json.getJSONObject(2);
		JSONObject portalCoupler = item.getJSONObject("portalCoupler");
		
		portalGuid = portalCoupler.getString("portalGuid");
		portalLocation = portalCoupler.getString("portalLocation");
		portalImageUrl = portalCoupler.getString("portalImageUrl");
		portalTitle = portalCoupler.getString("portalTitle");
		portalAddress = portalCoupler.getString("portalAddress");
	}

	public String getPortalGuid() {
		return portalGuid;
	}

	public String getPortalLocation() {
		return portalLocation;
	}

	public String getPortalImageUrl() {
		return portalImageUrl;
	}

	public String getPortalTitle() {
		return portalTitle;
	}

	public String getPortalAddress() {
		return portalAddress;
	}	
}
