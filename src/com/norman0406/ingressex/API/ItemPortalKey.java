package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemPortalKey extends Item {
	public String portalGuid;
	public String portalLocation;
	public String portalImageUrl;
	public String portalTitle;
	public String portalAddress;

	@Override
	protected void initItemByJSON(JSONObject json) throws JSONException {
		JSONObject portalCoupler = json.getJSONObject("portalCoupler");
		
		portalGuid = portalCoupler.getString("portalGuid");
		portalLocation = portalCoupler.getString("portalLocation");
		portalImageUrl = portalCoupler.getString("portalImageUrl");
		portalTitle = portalCoupler.getString("portalTitle");
		portalAddress = portalCoupler.getString("portalAddress");
	}
	
}
