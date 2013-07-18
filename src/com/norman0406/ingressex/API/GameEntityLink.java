package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameEntityLink extends GameEntity {
	
	private String linkOriginGuid;
	private String linkDestinationGuid;
	private Utils.LocationE6 linkOriginLocation;
	private Utils.LocationE6 linkDestinationLocation;
	private Utils.Team linkControllingTeam;

	GameEntityLink(JSONArray json) throws JSONException {
		super(json);
		
		JSONObject item = json.getJSONObject(2);

		JSONObject edge = item.getJSONObject("edge");
		
		linkOriginGuid = edge.getString("originPortalGuid");
		linkDestinationGuid = edge.getString("destinationPortalGuid");
		linkOriginLocation = new Utils.LocationE6(edge.getJSONObject("originPortalLocation"));
		linkDestinationLocation = new Utils.LocationE6(edge.getJSONObject("destinationPortalLocation"));
		linkControllingTeam = Utils.getTeam(item.getJSONObject("controllingTeam"));	
	}
	
	public String getLinkOriginGuid() {
		return linkOriginGuid;
	}
	
	public String getLinkDestinationGuid() {
		return linkDestinationGuid;
	}
	
	public Utils.LocationE6 getLinkOriginLocation() {
		return linkOriginLocation;
	}
	
	public Utils.LocationE6 getLinkDestinationLocation() {
		return linkDestinationLocation;
	}
	
	public Utils.Team getLinkControllingTeam() {
		return linkControllingTeam;
	}
}
