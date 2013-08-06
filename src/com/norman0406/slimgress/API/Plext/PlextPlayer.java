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

package com.norman0406.slimgress.API.Plext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.norman0406.slimgress.API.Common.Location;
import com.norman0406.slimgress.API.Common.Team;

public class PlextPlayer extends PlextBase
{
    private Location mLocation;
    private Team mTeam;

    public PlextPlayer(JSONArray json) throws JSONException
    {
        super(PlextType.PlayerGenerated, json);

        JSONObject item = json.getJSONObject(2);

        mLocation = new Location(item.getJSONObject("locationE6"));

        JSONObject controllingTeam = item.optJSONObject("controllingTeam");
        if (controllingTeam != null)
            mTeam = new Team(controllingTeam);
        else
            mTeam = new Team(Team.TeamType.Neutral);
    }

    public boolean isSecure()
    {
        for (Markup markup : getMarkups()) {
            if (markup.getType() == Markup.MarkupType.Secure)
                return true;
        }

        return false;
    }

    public Location getLocation()
    {
        return mLocation;
    }

    public Team getTeam()
    {
        return mTeam;
    }
}
