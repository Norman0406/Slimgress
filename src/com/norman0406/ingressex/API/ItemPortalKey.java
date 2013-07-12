package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemPortalKey extends Item {

	private String portalGuid;
	private String portalLocation;
	private String portalImageUrl;
	private String portalTitle;
	private String portalAddress;
	
	public ItemPortalKey(String guid, String timestamp) {
		super(guid, timestamp, Item.ItemType.PortalKey);
	}

	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		super.initByJSON(json);
		
		JSONObject portalCoupler = json.getJSONObject("portalCoupler");
		
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
