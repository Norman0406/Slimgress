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

package com.norman0406.slimgress.API.Player;

import org.json.JSONArray;
import org.json.JSONException;

public class Agent extends PlayerEntity
{
    private final String mNickname;

    private static int[] mLevelAP = {
            0,
            10000,
            30000,
            70000,
            150000,
            300000,
            600000,
            1200000
    };

    public Agent(JSONArray json, String nickname) throws JSONException
    {
        super(json);
        mNickname = nickname;
    }

    public String getNickname()
    {
        return mNickname;
    }

    public int getLevel()
    {
        // TODO: more efficient?

        for (int i = mLevelAP.length - 1; i >= 0; i--) {
            if (this.getAp() >= mLevelAP[i])
                return i + 1;
        }

        throw new IndexOutOfBoundsException("agent level could not be retrieved");
    }

    public int getEnergyMax()
    {
        return 2000 + (getLevel() * 1000);
    }
}
