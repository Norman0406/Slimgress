package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemPortalKey extends Item
{
	private String mPortalGuid;
	private String mPortalLocation;
	private String mPortalImageUrl;
	private String mPortalTitle;
	private String mPortalAddress;
	
	public ItemPortalKey(JSONArray json) throws JSONException
	{
		super(ItemType.PortalKey, json);

		JSONObject item = json.getJSONObject(2);
		JSONObject portalCoupler = item.getJSONObject("portalCoupler");
		
		mPortalGuid = portalCoupler.getString("portalGuid");
		mPortalLocation = portalCoupler.getString("portalLocation");
		mPortalImageUrl = portalCoupler.getString("portalImageUrl");
		mPortalTitle = portalCoupler.getString("portalTitle");
		mPortalAddress = portalCoupler.getString("portalAddress");
	}

	public String getPortalGuid()
	{
		return mPortalGuid;
	}

	public String getPortalLocation()
	{
		return mPortalLocation;
	}

	public String getPortalImageUrl()
	{
		return mPortalImageUrl;
	}

	public String getPortalTitle()
	{
		return mPortalTitle;
	}

	public String getPortalAddress()
	{
		return mPortalAddress;
	}	
}
