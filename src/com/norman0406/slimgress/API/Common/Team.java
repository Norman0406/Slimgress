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

package com.norman0406.slimgress.API.Common;

import org.json.JSONException;
import org.json.JSONObject;

public class Team
{
    public enum TeamType
    {
        Resistance,
        Enlightened,
        Neutral
    }

    private TeamType mTeam;

    public Team(JSONObject json) throws JSONException
    {
        if (!json.has("team"))
            throw new RuntimeException("invalid json object");

        mTeam = fromString(json.getString("team"));
    }

    public Team(String teamString)
    {
        mTeam = fromString(teamString);
    }

    public Team(TeamType teamType)
    {
        mTeam = teamType;
    }

    private TeamType fromString(String teamString)
    {
        if (teamString.equals("RESISTANCE"))
            return TeamType.Resistance;
        else if (teamString.equals("ALIENS"))
            return TeamType.Enlightened;
        else if (teamString.equals("NEUTRAL"))
            return TeamType.Neutral;
        else
            throw new RuntimeException("invalid team string: " + teamString);
    }

    public TeamType getTeamType()
    {
        return mTeam;
    }

    public String toString()
    {
        if (mTeam == TeamType.Resistance)
            return "RESISTANCE";
        else if (mTeam == TeamType.Enlightened)
            return "ALIENS";

        return "NEUTRAL";
    }
}
