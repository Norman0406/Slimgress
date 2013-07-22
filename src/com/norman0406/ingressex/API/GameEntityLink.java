package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameEntityLink extends GameEntity
{	
	private String mLinkOriginGuid;
	private String mLinkDestinationGuid;
	private Utils.LocationE6 mLinkOriginLocation;
	private Utils.LocationE6 mLinkDestinationLocation;
	private Utils.Team mLinkControllingTeam;

	GameEntityLink(JSONArray json) throws JSONException
	{
		super(json);
		
		JSONObject item = json.getJSONObject(2);

		JSONObject edge = item.getJSONObject("edge");
		
		mLinkOriginGuid = edge.getString("originPortalGuid");
		mLinkDestinationGuid = edge.getString("destinationPortalGuid");
		mLinkOriginLocation = new Utils.LocationE6(edge.getJSONObject("originPortalLocation"));
		mLinkDestinationLocation = new Utils.LocationE6(edge.getJSONObject("destinationPortalLocation"));
		mLinkControllingTeam = Utils.getTeam(item.getJSONObject("controllingTeam"));	
	}
	
	public String getLinkOriginGuid()
	{
		return mLinkOriginGuid;
	}
	
	public String getLinkDestinationGuid()
	{
		return mLinkDestinationGuid;
	}
	
	public Utils.LocationE6 getLinkOriginLocation()
	{
		return mLinkOriginLocation;
	}
	
	public Utils.LocationE6 getLinkDestinationLocation()
	{
		return mLinkDestinationLocation;
	}
	
	public Utils.Team getLinkControllingTeam()
	{
		return mLinkControllingTeam;
	}
}
