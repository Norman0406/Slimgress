package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlextPlayer extends Plext
{
	private Utils.LocationE6 mLocation;
	private Utils.Team mTeam;
	
	public PlextPlayer(JSONArray json) throws JSONException
	{
		super(PlextType.PlayerGenerated, json);
		
		JSONObject item = json.getJSONObject(2);
		
		mLocation = new Utils.LocationE6(item.getJSONObject("locationE6"));
		
		JSONObject controllingTeam = item.optJSONObject("controllingTeam");
		if (controllingTeam != null)
			mTeam = Utils.getTeam(controllingTeam);
		else
			mTeam = Utils.Team.Neutral;
	}
	
	public boolean isSecure()
	{
		for (Markup markup : getMarkups()) {
			if (markup.getType() == MarkupType.Secure)
				return true;
		}
		
		return false;
	}
	
	public Utils.LocationE6 getLocation()
	{
		return mLocation;
	}

	public Utils.Team getTeam()
	{
		return mTeam;
	}
}