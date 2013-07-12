package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class GameEntityLink extends GameEntity {
	
	private String linkOriginGuid;
	private String linkDestinationGuid;
	private Utils.LocationE6 linkOriginLocation;
	private Utils.LocationE6 linkDestinationLocation;
	private Utils.Team linkControllingTeam;

	GameEntityLink(String guid, String timestamp) {
		super(guid, timestamp);
	}

	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		super.initByJSON(json);
		
		JSONObject edge = json.getJSONObject("edge");
		String team = json.getJSONObject("controllingTeam").getString("team");
		
		linkOriginGuid = edge.getString("originPortalGuid");
		linkDestinationGuid = edge.getString("destinationPortalGuid");
		linkOriginLocation = new Utils.LocationE6(edge.getJSONObject("originPortalLocation"));
		linkDestinationLocation = new Utils.LocationE6(edge.getJSONObject("destinationPortalLocation"));
		
		if (team.equals("RESISTANCE"))
			linkControllingTeam = Utils.Team.Resistance;
		else if (team.equals("ALIENS"))
			linkControllingTeam = Utils.Team.Enlightened;
		else
			System.out.println("unknown team string");		
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
