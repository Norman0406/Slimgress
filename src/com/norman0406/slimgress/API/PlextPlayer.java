/***********************************************************************
*
* Slimgress: Ingress API for Android
* Copyright (C) 2013 Norman Link <norman.link@gmx.net>
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
***********************************************************************/

package com.norman0406.slimgress.API;

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